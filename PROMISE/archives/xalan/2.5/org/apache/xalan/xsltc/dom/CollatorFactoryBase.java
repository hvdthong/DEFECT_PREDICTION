package org.apache.xalan.xsltc.dom;

import java.text.Collator;
import java.util.Locale;

import org.apache.xalan.xsltc.CollatorFactory;

public class CollatorFactoryBase implements CollatorFactory {
    
    public static final Locale DEFAULT_LOCALE = Locale.getDefault();
    public static final Collator DEFAULT_COLLATOR = Collator.getInstance();
    
    public CollatorFactoryBase() {
    }
    
    public Collator getCollator(String lang, String country) {
        return Collator.getInstance(new Locale(lang, country));
    }
    
    public Collator getCollator(Locale locale) {
        if (locale == DEFAULT_LOCALE)
            return DEFAULT_COLLATOR;
        else
            return Collator.getInstance(locale);
    }
}
