package org.apache.xalan.xsltc;

import java.text.Collator;
import java.util.Locale;

public interface CollatorFactory {
    
    public Collator getCollator(String lang, String country);
    public Collator getCollator(Locale locale);
}
