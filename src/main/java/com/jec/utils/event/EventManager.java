package com.jec.utils.event;

public interface EventManager<T> {
//	public void fireEvent(T arg);
	public void registerListener(EventListener<T> listener);
	public void unregisterListener(EventListener<T> listener);
}
