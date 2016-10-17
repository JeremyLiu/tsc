package com.jec.plugin.framework;


public class Service<T> {

	private Class<T> clazz = null;
	private T service = null;
	
	public Service(Class<T> clazz) {
		this.clazz = clazz;
	}
	
	@SuppressWarnings("unchecked")
	protected void initialize() {
		if(service == null) {
			service = (T) Framework.getService(clazz);
			if(service == null) {
				System.out.println(clazz.getSimpleName() + " is not ready.");
			}
		}
	}
	
	public boolean isReady() {
		return service != null;
	}
	
	public void initWithException() throws RuntimeException {
		initialize();
		if(!isReady()) {
			throw new RuntimeException(clazz.getSimpleName() + " is not ready.");
		}
	}

	public T get() {
		initialize();
		return service;
	}
	
}
