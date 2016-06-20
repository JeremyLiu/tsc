package com.jec.plugin.share;

public interface ShareService {
	
	public void put(String key, Object value);
	public Object get(String key, Object def);
	public <T> T get(String key, T def, Class<T> clazz);
	
	/*
	void put(String key, String value);
	String get(String key, String def);
    public void putInt(String key, int value);
    public int getInt(String key, int def) ;
    public void putLong(String key, long value) ;
    public long getLong(String key, long def);
    public void putBoolean(String key, boolean value);
    public boolean getBoolean(String key, boolean def);
    public void putFloat(String key, float value);
    public float getFloat(String key, float def);
    public void putDouble(String key, double value);
    public double getDouble(String key, double def);
    public void putByteArray(String key, byte[] value);
    public byte[] getByteArray(String key, byte[] def);
	*/
	
}
