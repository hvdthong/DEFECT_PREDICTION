package org.apache.camel.util;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.CharacterCodingException;
import java.util.BitSet;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


public class UnsafeUriCharactersEncoder {
    private static final transient Log LOG = LogFactory.getLog(UnsafeUriCharactersEncoder.class);
    static BitSet unsafeCharacters;
    static {
        unsafeCharacters = new BitSet(256);
        unsafeCharacters.set(' ');
        unsafeCharacters.set('"');
        unsafeCharacters.set('<');
        unsafeCharacters.set('>');
        unsafeCharacters.set('#');
        unsafeCharacters.set('%');
        unsafeCharacters.set('{');
        unsafeCharacters.set('}');
        unsafeCharacters.set('|');
        unsafeCharacters.set('\\');
        unsafeCharacters.set('^');
        unsafeCharacters.set('~'); 
        unsafeCharacters.set('[');
        unsafeCharacters.set(']');
        unsafeCharacters.set('`');        
    }
    
    
    private UnsafeUriCharactersEncoder() {
    }
    
    public static String encode(String s) {
        int n = s.length();
        if (n == 0)
            return s;
        
        
        try {
            byte[] bytes = s.getBytes("UTF8");        
            for (int i = 0;;) {
                if (unsafeCharacters.get(bytes[i]))
                    break;
                if (++i >= bytes.length)
                    return s;
            }
            
            StringBuffer sb = new StringBuffer();
            for (byte b : bytes) {            
                if (unsafeCharacters.get(b)) {
                    appendEscape(sb, (byte)b);
                }    
                else
                    sb.append((char)b);
            }
            return sb.toString();
        } catch (UnsupportedEncodingException e) {
            LOG.error("Can't encoding the uri: ", e);            
            return null;
        }    
    }
    
    private static void appendEscape(StringBuffer sb, byte b) {
        sb.append('%');
        sb.append(hexDigits[(b >> 4) & 0x0f]);
        sb.append(hexDigits[(b >> 0) & 0x0f]);
    }
    
    private final static char[] hexDigits = {
                                        '0', '1', '2', '3', '4', '5', '6', '7',
                                        '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'
                                         };
    
      

}
