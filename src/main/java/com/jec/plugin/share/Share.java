package com.jec.plugin.share;

import java.util.HashMap;
import java.util.Map;

import com.jec.plugin.framework.Framework;
import com.jec.plugin.framework.Plugin;

public class Share implements ShareService, Plugin {
	
	Map<String, Object> map = new HashMap<String, Object>();

	@Override
	public String getName() {
		return "Share";
	}

	@Override
	public String getVersion() {
		return "1.0";
	}

	@Override
	public String getAuthor() {
		return "lingdm";
	}

	@Override
	public String getDescription() {
		return "提供各个Plugin之间共享数据服务";
	}

	@Override
	public void start() throws Exception {
		Framework.registerService(ShareService.class, this);
	}

	@Override
	public void stop() {
		map.clear();
		Framework.unregisterService(ShareService.class);
	}

	@Override
	public void put(String key, Object value) {
		map.put(key, value);
	}
	
	@Override
	public Object get(String key, Object def) {
        Object value = map.get(key);
        if(value == null) {
        	value = def; 
        }
        return value;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> T get(String key, T def, Class<T> clazz) {
        T result = def;
        final Object value = this.get(key, null);
        if ( value != null ) {
        	if(value.getClass().equals(clazz)) {
        		result = (T)value;
        	}
        }
        return result;
	}

}
