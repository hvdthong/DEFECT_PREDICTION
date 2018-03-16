package org.apache.ivy.core;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

/** 
 * Resolve an file or url relatively to its context.  
 */
public abstract class RelativeUrlResolver {

    /**
     * Resolve the url in the context of context.  
     * @param context The URL of the ressource containing the reference url
     * @param url a relative or absolution url string
     * @throws MalformedURLException
     */
    public abstract URL getURL(URL context , String url) throws MalformedURLException;
    
    /**
     * Relsovle file or url path relatively to a context.  file is considered first.
     * If file is not defined, url will be considered.  
     * @param context The URL of the ressource containing the reference file or url
     * @param file a relative or absolute path
     * @param url a relative or absolution url string
     * @return the resulting url or null if faile and url are null.
     * @throws MalformedURLException
     */
    public URL getURL(URL context, String file, String url) throws MalformedURLException {
        if (file != null) {
            File f = new File(file);
            if (f.isAbsolute()) {
                return f.toURI().toURL();
            } else {
                return getURL(context, file);
            }
        } else if (url != null) {
            return getURL(context, url);
        } else {
            return null;
        }
    }
}
