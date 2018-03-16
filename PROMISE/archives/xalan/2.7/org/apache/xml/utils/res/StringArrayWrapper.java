package org.apache.xml.utils.res;

/**
 *
 * It is a mutable object to wrap the String[] used in
 * the contents of the XResourceBundle class
 */
public class StringArrayWrapper {
    private String[] m_string;
    
    public StringArrayWrapper(String[] arg){
        m_string = arg;
    }
    
    public String getString(int index){
        return m_string[index];
    }
    
    public int getLength(){
        return m_string.length;
    }
}

