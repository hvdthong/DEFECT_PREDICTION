package org.apache.camel;

/**
 * A runtime exception thrown if an attempt is made to resolve an unknown
 * language definition.
 * 
 * @see org.apache.camel.CamelContext#resolveLanguage(String)
 * 
 * @version $Revision: 563607 $
 */
public class NoSuchLanguageException extends RuntimeCamelException {
    private static final long serialVersionUID = -8721487431101572630L;
    private final String language;

    public NoSuchLanguageException(String language) {
        super("No language could be found for: " + language);
        this.language = language;
    }

    public String getLanguage() {
        return language;
    }
}
