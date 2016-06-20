package com.jec.plugin.shell.core.telnet;

import java.io.IOException;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.HashSet;
import java.util.Set;

import com.jec.plugin.shell.core.Shell;


public class Listener {

    private int localPort;
    private Thread thread;
    private boolean stop = false;
    private ServerSocket socket;
    private int soTimeout;

    private AtomicInteger userCounter;
    private int maxConnections;
    private Set<Telnet> connections;
    
    
    
    /**
     * Activates this listener on a listener thread (telnetconsole.Listener).
     */
    public void activate()
    {
        //configure from framework property
        localPort = 6666 ;
        soTimeout = 0;
        maxConnections = 2;
        userCounter = new AtomicInteger( 0 );
        connections = new HashSet<Telnet>();
        thread = new Thread( new Acceptor(), "shell.Listener" );
        thread.start();
    }//activate


    /**
     * Deactivates this listener.
     * <p/>
     * The listener's socket will be closed, which should cause an interrupt in the
     * listener thread and allow for it to return. The calling thread joins the listener
     * thread until it returns (to ensure a clean stop).
     */
    public void deactivate()
    {
        try
        {
            stop = true;
            //wait for the listener thread
            socket.close();
            thread.join();
        }
        catch ( Exception ex )
        {
            //Activator.getServices().error( "Listener::deactivate()", ex );
        }
        
        // get the active connections (and clear the list)
        // we have to work on a copy, since stopping any active connection
        // will try to remove itself from the set, which might cause a
        // ConcurrentModificationException if we would iterate over the list
        Telnet[] connectionArray;
        synchronized ( connections )
        {
        	connectionArray = ( Telnet[] ) connections.toArray( new Telnet[connections.size()] );
            connections.clear();
        }

        // now terminate all active connections
        for ( int i = 0; i < connectionArray.length; i++ )
        {
        	connectionArray[i].terminate();
        }
    }//deactivate
    
    
    private class Acceptor implements Runnable
    {

        /**
         * Listens constantly to a server socket and handles incoming connections.
         * One connection will be accepted and routed into the shell, all others will
         * be notified and closed.
         * <p/>
         * The mechanism that should allow the thread to unblock from the ServerSocket.accept() call
         * is currently closing the ServerSocket from another thread. When the stop flag is set,
         * this should cause the thread to return and stop.
         */
        public void run()
        {
            try
            {
                /*
                    A server socket is opened with a connectivity queue of a size specified
                    in int floodProtection.  Concurrent login handling under normal circumstances
                    should be handled properly, but denial of extern attacks via massive parallel
                    program logins should be prevented with this.
                */
                socket = new ServerSocket( localPort, 1/*, InetAddress.getByName( localHost ) */);
                socket.setSoTimeout( soTimeout );
                do
                {
                    try
                    {
                        Socket s = socket.accept();
                        if ( userCounter.get() >= maxConnections )
                        {
                            //reject with message
                            PrintStream out = new PrintStream( s.getOutputStream() );
                            out.print( INUSE_MESSAGE );
                            out.flush();
                            //close
                            out.close();
                            s.close();
                        }
                        else
                        {
                        	userCounter.increment();
                        	
                            //run on the connection thread
                            Thread connectionThread = new Thread( new Telnet( Listener.this, s, userCounter ) );
                            connectionThread.setName( "shell.telnet remote=" + s.getRemoteSocketAddress() );
                            connectionThread.start();
                        }
                    }
                    catch ( SocketException ex )
                    {
                    }
                    catch ( SocketTimeoutException ste) {
                        // Caught socket timeout exception. continue
                    }
                }
                while ( !stop );
                
                

            }
            catch ( IOException e )
            {
                //Activator.getServices().error( "Listener.Acceptor::activate()", e );
            }
        }//run

    }//inner class Acceptor

    private static final String INUSE_MESSAGE = "Connection refused.\r\n"
        + "All possible connections are currently being used.\r\n";

	
    /**
     * Registers the given {@link Telnet} instance handling a remote connection
     * to this listener.
     * <p>
     * This method is called by the {@link Shell#run()} method to register the
     * remote connection for it to be terminated in case this listener is
     * {@link #deactivate() deactivated} before the remote connection is
     * terminated.
     * 
     * @param connection The {@link Telnet} connection to register
     */
    void registerConnection( Telnet connection )
    {
        synchronized ( connections )
        {
            connections.add( connection );
        }
    }

    /**
     * Unregisters the given {@link Telnet} instance handling a remote connection
     * from this listener.
     * <p>
     * This method is called when the {@link Shell#run()} method terminates to
     * inform this listener instance that the remote connection has ended and
     * thus does not need to be cleaned up when this listener terminates.
     * 
     * @param connection The {@link Telnet} connection to unregister
     */
    void unregisterConnection( Telnet connection )
    {
        synchronized ( connections )
        {
            connections.remove( connection );
        }
    }
    
    
}
