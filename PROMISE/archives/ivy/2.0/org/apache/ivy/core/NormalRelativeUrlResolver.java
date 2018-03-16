package org.apache.ivy.core;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * Normal implementation of RelativeUrlResolver. 
 */
public class NormalRelativeUrlResolver extends RelativeUrlResolver {

    public URL getURL(URL context, String url) throws MalformedURLException {
        return new URL(context , url);
    }

}
