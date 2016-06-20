package com.jec.plugin.shell.core.telnet;

public class AtomicInteger {

    private int m_Value;


    public AtomicInteger()
    {
        m_Value = 0;
    }//constructor


    /**
     * Constructs a new <tt>AtomicInteger</tt>
     * with a given initial value.
     *
     * @param value the initial value.
     */
    public AtomicInteger( int value )
    {
        m_Value = value;
    }//constructor


    /**
     * Increments this <tt>AtomicInteger</tt> by one.
     *
     * @return the resulting value.
     */
    public synchronized int increment()
    {
        return ++m_Value;
    }//increment


    /**
     * Decrements this <tt>AtomicInteger</tt> by one.
     *
     * @return the resulting value.
     */
    public synchronized int decrement()
    {
        return --m_Value;
    }//decrement


    /**
     * Sets the value of this <tt>AtomicInteger</tt>.
     *
     * @param i the new value.
     */
    public synchronized void set( int i )
    {
        m_Value = i;
    }//set


    /**
     * Returns the value of this <tt>AtomicInteger</tt>.
     *
     * @return the actual value.
     */
    public synchronized int get()
    {
        return m_Value;
    }//get

	
}
