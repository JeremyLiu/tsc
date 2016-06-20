package com.jec.utils.event;

import java.util.Vector;

public class DefaultEventManager<T> implements EventManager<T> {
	
	private Vector<EventListener<T>> listeners = new Vector<EventListener<T>>();

	public DefaultEventManager() {
		// TODO Auto-generated constructor stub
	}

	@SuppressWarnings("unchecked")
	public void fireEvent(T arg) {

		Vector<EventListener<T>> local = null;

    	synchronized (this) {
    		local = (Vector<EventListener<T>>)listeners.clone();
    	}
    	
    	for(EventListener<T> listener : local) {
    		listener.onEvent(arg);
    	}
	}

	@Override
	public synchronized void registerListener(EventListener<T> listener) {

		if(!listeners.contains(listener)) {
			listeners.addElement(listener);
		}
	}

	@Override
	public synchronized void unregisterListener(EventListener<T> listener) {
		// TODO Auto-generated method stub
		listeners.removeElement(listener);
	}

}
