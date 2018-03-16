package org.apache.xml.utils.res;

/**
 *
 * It is a mutable object to wrap the char[] used in
 * the contents of the XResourceBundle class
 */
public class CharArrayWrapper {
    private char[] m_char;
    
    public CharArrayWrapper(char[] arg){
        m_char = arg;
    }
    
    public char getChar(int index){
        return m_char[index];
    }
    
    public int getLength(){
        return m_char.length;
    }
}
