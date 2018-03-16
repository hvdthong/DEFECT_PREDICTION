package org.apache.xml.utils.res;

/**
 *
 * It is a mutable object to wrap the int[] used in
 * the contents of the XResourceBundle class
 */
public class IntArrayWrapper {
    private int[] m_int;
    
    public IntArrayWrapper(int[] arg){
        m_int = arg;
    }
    
    public int getInt(int index){
        return m_int[index];
    }
    
    public int getLength(){
        return m_int.length;
    }
}
