package org.apache.ivy.plugins.matcher;

import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

public class MapMatcher {
    private Map/*<String, Matcher>*/ matchers = new HashMap();

    private PatternMatcher pm;

    private Map attributes;

    public MapMatcher(Map attributes, PatternMatcher pm) {
        this.attributes = attributes;
        this.pm = pm;
        for (Iterator iter = attributes.entrySet().iterator(); iter.hasNext();) {
            Entry entry = (Entry) iter.next();
            String value = (String) entry.getValue();
            if (value != null) {
                matchers.put(entry.getKey(), pm.getMatcher(value));
            }
        }
    }

    public boolean matches(Map/*<String,String>*/ m) {
        for (Iterator iter = matchers.entrySet().iterator(); iter.hasNext();) {
            Entry entry = (Entry) iter.next();
            
            Matcher matcher = (Matcher) entry.getValue();
            String value = (String) m.get(entry.getKey());
            if ((value == null) || !matcher.matches(value)) {
                return false;
            }
        }
        
        return true;
    }

    public String toString() {
        return attributes + " (" + pm.getName() + ")";
    }

    public Map getAttributes() {
        return Collections.unmodifiableMap(attributes);
    }
    
    public PatternMatcher getPatternMatcher() {
        return pm;
    }
}
