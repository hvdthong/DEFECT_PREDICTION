package fr.jayasoft.ivy.matcher;

public class AnyMatcher implements Matcher {
    private final static Matcher INSTANCE = new AnyMatcher();
    
    public static Matcher getInstance() {
        return INSTANCE;
    }
    
    private AnyMatcher() {
        
    }

    public boolean matches(String str) {
        return true;
    }

    public boolean isExact() {
        return false;
    }

}
