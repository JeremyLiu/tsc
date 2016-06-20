package com.jec.plugin.framework;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.swing.JOptionPane;


public class Framework {
	
	public interface EventListener {
		
		public void START_begin();
		public void START_load_config();
		public void START_check_plugins();
		public void START_plugins_begin();
		public void START_plugin_begin(Plugin plugin);
		public void START_plugin_end(Plugin plugin);
		public void START_plugins_end();
		public void START_end();
		
		public void STOP_begin();
		public void STOP_plugins_begin();
		public void STOP_plugin_begin(Plugin plugin);
		public void STOP_plugin_end(Plugin plugin);
		public void STOP_plugins_end();
		public void STOP_end();

	}
	

	public static Framework Default = new Framework();
	
	private List<Plugin> plugins = new LinkedList<Plugin>();
	
	private Map<String, Object> services = new HashMap<String, Object>();

	private EventListener listener = null;
//	private List<EventListener> listeners = new LinkedList<EventListener>();
	
	
	private Framework() {
	}
	
	public void start(String[] args) {
		
		try {
			
			/*
			Runtime.getRuntime().addShutdownHook(new Thread() {

				@Override
				public void run() {
					Framework.Default.stop(-1);
				}
				
			});
			*/
			
			getListener().START_begin();
			
			loadPlugins();
			
			checkPlugins();
			
			System.out.println(paramString());
			
			initializePlugins();
			
			getListener().START_end();
			
			System.gc();
			System.runFinalization();
			
		} catch(Exception e) {
			
			JOptionPane.showMessageDialog(
					null, 
					e.getMessage(),
					"系统错误",
					JOptionPane.ERROR_MESSAGE);
			
			e.printStackTrace();
			
			Framework.Default.stop(-1);
			
		}

	}
	
	private void loadPlugins() {
		getListener().START_load_config();
	}
	
	private void checkPlugins() throws Exception {
		
		getListener().START_check_plugins();
		
		if(plugins.size() == 0) {
			throw(new RuntimeException("系统未载入任何插件，请查看配置文件！"));
		}
	}
	

	private void initializePlugins() throws Exception {

		getListener().START_plugins_begin();

		// 从头到尾初始化模块
		int count = plugins.size();
		for(int i = 0; i < count; i++) {
			
			Plugin plugin = plugins.get(i);
			getListener().START_plugin_begin(plugin);
			plugin.start();
			getListener().START_plugin_end(plugin);
			
			System.out.println(plugin.getName() + " start");
		}
		
		getListener().START_plugins_end();	
	}
	
	private void terminatePlugins() {
		
		getListener().STOP_plugins_begin();
		
		// 从尾到头终止模块
		int count = plugins.size();
		for(int i = 0; i < count; i++) {
			Plugin plugin = plugins.get(count - 1 - i);
			getListener().STOP_plugin_begin(plugin);
			plugin.stop();
			getListener().STOP_plugin_end(plugin);
			System.out.println(plugin.getName() + " stop");
		}
		
		getListener().STOP_plugins_end();
	}
	
	public void stop(int status) {
		
		getListener().STOP_begin();
		
		terminatePlugins();
		
		System.runFinalization();
		
		getListener().STOP_end();
		
		System.exit(status);
	}
	
	public String getAuthor() {
		return "lingdm";
	}

	public String getVersion() {
		return "1.0";
	}

	public Plugin getPlugin(String pluginName) {
		Iterator<Plugin> it = plugins.iterator();
		while(it.hasNext()) {
			Plugin plugin = it.next();
			if(plugin.getName().equals(pluginName)) {
				return plugin;
			}
		}
		return null;
	}

	public List<Plugin> getPlugins() {
		return new LinkedList<Plugin>(plugins);
	}
	
	public void addPlugin(Plugin plugin) throws Exception{
		
		if(plugin == null) {
			throw(new RuntimeException("plugin should not be null."));
		}
		
		if(null != getPlugin(plugin.getName())) {
			throw(new RuntimeException("plugin is already registered."));
		}
		
		plugins.add(plugin);
		
	}
	
	public int getPluginCount() {
		return plugins.size();
	}
	
	private String paramString() {
		
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		pw.println("=========================================================");
		pw.println("Application&Plugin Framework");
		pw.println("Version: " + getVersion());
		pw.println("Author: " + getAuthor());
		pw.println("=========================================================");
		pw.println("Plugins count=" + plugins.size());
		for(int i = 0; i < plugins.size(); i++) {
			Plugin plugin = plugins.get(i);
			pw.println("Index=" + i + " Name=" + plugin.getName() + " Version=" + plugin.getVersion() + " Author=" + plugin.getAuthor());
		}
		pw.println("=========================================================");
		return sw.toString();
	}

	public Object getService(String className) {
		return services.get(className);
	}

	public void registerService(String className, Object service) {
		services.put(className, service);
	}

	public void unregisterService(String className) {
		services.remove(className);
	}
	
	public void setEventListener(EventListener listener) {
		this.listener = listener;
	}
	
	private EventListener getListener() {
		if(this.listener == null) {
			return DefaultEventListener;
		} else {
			return this.listener;
		}
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Framework.Default.start(args);
	}
	
	/*
	 * 
	 * 
	 * 
	 * 
	 */

	public static Object getService(Class<?> clazz) {
		
		// check null
		if(clazz == null) {
			return null;
		}
		
		return Default.getService(clazz.getName());
		
	}

	public static void registerService(Class<?> clazz, Object service) {
		if(clazz != null && service != null) {
			Default.registerService(clazz.getName(), service);
		}
	}
	
	public static void unregisterService(Class<?> clazz) {
		if(clazz != null) {
			Default.unregisterService(clazz.getName());
		}
	}

	
	/*
	 * 
	 * 
	 * 
	 * 
	 */
	
	static EventListener DefaultEventListener = new EventListener() {

		@Override
		public void START_begin() {
		}

		@Override
		public void START_check_plugins() {
		}

		@Override
		public void START_end() {
		}

		@Override
		public void START_load_config() {
		}

		@Override
		public void START_plugin_begin(Plugin plugin) {
		}

		@Override
		public void START_plugin_end(Plugin plugin) {
		}

		@Override
		public void START_plugins_begin() {
		}

		@Override
		public void START_plugins_end() {
		}

		@Override
		public void STOP_begin() {
		}

		@Override
		public void STOP_end() {
		}

		@Override
		public void STOP_plugin_begin(Plugin plugin) {
		}

		@Override
		public void STOP_plugin_end(Plugin plugin) {
		}

		@Override
		public void STOP_plugins_begin() {
		}

		@Override
		public void STOP_plugins_end() {
		}
	};
	
}
