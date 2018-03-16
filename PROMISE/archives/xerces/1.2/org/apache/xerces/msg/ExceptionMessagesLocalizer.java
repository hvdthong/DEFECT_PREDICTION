package org.apache.xerces.msg;

import java.util.Locale;
import java.util.ListResourceBundle;
import java.util.ResourceBundle;
import java.util.StringTokenizer;


public class ExceptionMessagesLocalizer {
    
    static final String FMT001 = "FMT001";
    
    
    
    /** This method returns the localized message */
    public static String localizeMessage(String string, Locale locale) {
        
        ResourceBundle fResourceBundle = null;
        if (locale != null)
            fResourceBundle = ListResourceBundle.getBundle("org.apache.xerces.msg.ExceptionMessages", locale);
        if (fResourceBundle == null || locale == null)
            fResourceBundle = ListResourceBundle.getBundle("org.apache.xerces.msg.ExceptionMessages");
        
        int keyIndex = string.indexOf(' ');
        String key = string.substring(0, keyIndex);
        int nlIndex = string.indexOf('\n');
 
        String msg = fResourceBundle.getString(key);
        if (nlIndex < 0) {
            return msg;
        }
       
        StringTokenizer st = new StringTokenizer(string.substring(nlIndex+1), "\t");
        int count = st.countTokens();
        String [] args = new String[count];
        for(int i=0; i < count; i++) {
            args[i] = st.nextToken();
        }
        
        if (args != null) {
            try {
                msg = java.text.MessageFormat.format(msg, args);
            } catch (Exception e) {
                msg = fResourceBundle.getString(FMT001)+ ": " + msg;
            }
        } 
        return msg;
    }
    
}
