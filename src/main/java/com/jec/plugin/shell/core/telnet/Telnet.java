package com.jec.plugin.shell.core.telnet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

import com.jec.plugin.shell.ShellCommand;
import com.jec.plugin.shell.ShellModule;
import com.jec.plugin.shell.core.CmdLine;
import com.jec.plugin.shell.core.Module;
import com.jec.plugin.shell.core.Shell;

public class Telnet implements Runnable {

    private Listener m_owner;
    private Socket m_Socket;
    private AtomicInteger m_UseCounter;
    
    private Module module = null;
    
    private boolean login = false;

    public Telnet( Listener owner, Socket s, AtomicInteger counter )
    {
        m_owner = owner;
        m_Socket = s;
        m_UseCounter = counter;
    }//constructor

    void terminate()
    {
        // called by Listener.deactivate() to terminate this session
        exit( "\r\nRemote Shell Console Terminating" );
    }//terminate
    
    /**
     * Runs the shell.
     */
    public void run()
    {
        m_owner.registerConnection( this );
        
        try
        {
            PrintStream out = new TerminalPrintStream( m_Socket.getOutputStream() );
            TerminalReader reader = new TerminalReader( m_Socket.getInputStream(), out, true ); 
            BufferedReader in = new BufferedReader( reader );
            ReentrantLock lock = new ReentrantLock();

            // Print welcome banner.
            out.println("=============================================================");
            out.println("                 Welcome to the Telnet Shell                 ");
            out.println("=============================================================");
            out.println("");
            out.println("");

            do
            {
            	prompt(out);
            	
                String line = "";
                CmdLine cl = null;
                
                try
                {
                    line = in.readLine();
                    //make sure to capture end of stream
                    if ( line == null )
                    {
                        out.println( "exit" );
                        return;
                    }
                }
                catch ( Exception ex )
                {
                    return;
                }

                line = line.trim();
                
    			//
    			if(!login) {
    				
    				if(line.equals("iamolins")) {
    					login = true;
    					reader.setMask(false);
    				} else {
    					out.println("Invalid password.");
    				}
    				
    			} 
                
                if ( line.equalsIgnoreCase( "exit" ) || line.equalsIgnoreCase( "disconnect" ) )
                {
                    return;
                }
                
                // get cmdLine
                try {
					cl = CmdLine.parse(line);
					if(cl == null) {
						continue;
					}
					
				} catch (Exception e) {
					out.println( e.getMessage() );
					continue;
				}
				
				// check exit
				if(cl.equalString("quit") || cl.equalString("exit") || cl.equalString("disconnect")) {
					out.println("Good bye!");
					out.flush();
					break;
				}

                try
                {
                    lock.lock();

    				if(!process(cl, out) && module != null) {
    					
    					for(ShellCommand command : module.getCommands()) {
    						
    						if(command.getName().equalsIgnoreCase(cl.getName())) {
    							command.process(cl, out);
    							break;
    						}
    						
    					}
    				}

                }
                catch ( Exception ex )
                {
                    //Activator.getServices().error( "Shell::run()", ex );
                }
                finally
                {
                    lock.unlock();
                }
            }
            while ( true );
        }
        catch ( IOException ex )
        {
            //Activator.getServices().error( "Shell::run()", ex );
        }
        finally
        {
            // no need to clean up in/out, since exit does it all
            exit( null );
        }
    }//run


    private void exit(String message)
    {
        // farewell message
        try
        {
            PrintStream out = new TerminalPrintStream( m_Socket.getOutputStream() );
            if ( message != null )
            {
                out.println( message );
            }
            out.println( "Good Bye!" );
            out.close();
        }
        catch ( IOException ioe )
        {
            // ignore
        }

        try
        {
            m_Socket.close();
        }
        catch ( IOException ex )
        {
            //Activator.getServices().error( "Shell::exit()", ex );
        }
        m_owner.unregisterConnection( this );
        m_UseCounter.decrement();
    }//exit

	protected void prompt(PrintStream out) throws IOException {
		
		if(!login) {
			
			out.println("Please input password: ");
			
		} else {
			
			if(module == null) {
				out.print("root> ");
			} else {
				out.print(module.getName() + "> ");
			}
			
		}

		
		/*
		if(!login) {
			out.println("Please input key: ");
			setMask(!login);
		} else {
			if(module == null) {
				send("root> ");
			} else {
				send(module.getName() + "> ");
			}
		}
		*/
	}
    
	private boolean process(CmdLine cl, PrintStream out) throws Exception {
				
		if(cl.equalString("list")) {
			list(out);
			return true;
		}
		
		if(cl.equalString("help")) {
			help(out);
			return true;
		}
		
		if(cl.equalString("cd")) {
			String name = cl.getParameter("name");
			cd(name, out);
			return true;
		}
		
		if(cl.equalString("ver")) {
			ver(out);
			return true;
		}		
		
		return false;
	}
	
	private void help(PrintStream out) throws Exception {
		
		out.println(format("exit", "Exit telnet"));
		out.println(format("list", "List all modules if root level,\r\n" +
							  "List all commands if module level"));
		out.println(format("cd", "Enter a module level (a 'name' parameter supplied), \r\n" +
							"or return to root level"));
		
		out.println(format("ver", "Show telnet version"));		
		
	}
	
	private void cd(String name, PrintStream out) throws Exception {
		
		if(name == null) {
			this.module = null;
			return;
		}
		
		boolean found = false;
		
		for(Module module : Shell.getModules()) {
			if(module.getName().equalsIgnoreCase(name)) {
				this.module = module;
				found = true;
				break;
			}
		}
		
		if(!found) {
			out.println();
			out.println("Unknown module name<" + name + ">.");
			out.println();
		}
		
	}
	

	
	private void list(PrintStream out) throws Exception {
		
		// if in root
		if(module == null) {

			out.println();
			out.println("All modules:");
			out.println();
			for(ShellModule module : Shell.getModules()) {
				out.println(format(module.getName(), module.getSummary()));
			}
			out.println("Total count is " + Shell.getModules().size());
			out.println();
			
		} else {
			
			out.println();
			out.println("All commands in [" + module.getName() + "]");
			out.println();
			for(ShellCommand command : module.getCommands()) {
				out.println(format(command.getName(), command.gethelp()));
			}
			out.println("Total count is " + module.getCommands().size());
			out.println();
			
		}
		
		
	}
	
	private void ver(PrintStream out) throws Exception {
		out.println();
		out.println("Version: 1.0.0");
		out.println("Author : lingdm");
		out.println("Build  : 2010-12-10");
		out.println();
	}
	
	
	
	private String format(String key, String val) {
		
		int keyWidth = 12;
		int interval = 4;
		int valWidth = 56;
		
		List<String> keys = splitString(key, keyWidth, true);
		List<String> vals = splitString(val, valWidth, true);
		
		String result = "";
		
		for(int i = 0; i < Math.max(keys.size(), vals.size()); i++) {
			
			String str = "  " + getString(keys, i, keyWidth) + 
								getString(null, 0, interval) +
								getString(vals, i, valWidth);
			
			result += str + "\r\n";
			
		}
		
		return result;
	}
	
	private String getString(List<String> list, int index, int width) {

		if(list == null || index < 0 || index >= list.size() || width < 1) {
			
			String str = "";
			
			for(int i = 0; i < width; i++) {
				str += " ";
			}
			
			return str;
		}
		
		String s = list.get(index);
		
		if(s.getBytes().length > width) {
			
			List<String> l = splitString(s, width, true);
			
			return l.get(0);
			
		} else {
			
			String str = s;
			
			while(str.getBytes().length < width) {
				
				str += " ";
				
			}
			
			return str;
			
		}
		
	}
	
	private List<String> splitString(String s, int width, boolean crlf) {
		
		List<String> list = new LinkedList<String>();
		
		if(crlf) {
			
			s = s.replaceAll("\r\n", "\n");
			s = s.replaceAll("\r", "\n");
			String[] strings = s.split("\n");
			
			for(String str : strings) {
				
				list.addAll(splitString(str, width, false));
				
			}
			
		} else {
			
			if(s.getBytes().length <= width) {
				
				list.add(s);
				
			} else {
				
				String str = "";
				String temp;
				
				for(int i = 0; i < s.length(); i++) {
					
					char c = s.charAt(i);
					
					temp = str + c;
					
					if(temp.getBytes().length > width) {
						
						list.add(str);
						
						str = "" + c;
						
					} else {
						
						str = temp;
						
					}
					
				}
				
				if(!str.isEmpty()) {
					
					list.add(str);
					
				}
				
				
				
			}
			
		}
		
		return list;
		
		
	}

	
	
	
	
	
	
    
}
