package com.jec.plugin.shell.core.telnet;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;

public class TerminalPrintStream extends PrintStream
{

    /**
     * Constructs a new instance wrapping the given <tt>OutputStream</tt>.
     *
     * @param tout the <tt>OutputStream</tt> to be wrapped.
     */
    public TerminalPrintStream( OutputStream tout )
    {
        super( tout );
    }//constructor


    public void print( String str )
    {
        try
        {
            byte[] bytes = str.getBytes();
            out.write( bytes, 0, bytes.length );
            flush();
        }
        catch ( IOException ex )
        {
            //Activator.getServices().error( "TerminalPrintStream::print()", ex );
        }
    }//print


    public void println( String str )
    {
        print( str + " \r\n" );
    }//println


    public void flush()
    {
        try
        {
            out.flush();
        }
        catch ( IOException ex )
        {
            //Activator.getServices().error( "TerminalPrintStream::println()", ex );
        }
    }//flush

}//class TerminalPrintStream
