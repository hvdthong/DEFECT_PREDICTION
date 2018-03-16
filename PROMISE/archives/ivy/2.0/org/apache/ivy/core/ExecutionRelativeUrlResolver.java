package org.apache.ivy.core;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * Resolve relative url relatively to the current execution directory instead
 * of relatively to the context.
 * This is was actually done prior 2.0.  This class allow thus to work
 * in a backward compatible mode.
 */
public class ExecutionRelativeUrlResolver extends RelativeUrlResolver {

    public URL getURL(URL context, String url) throws MalformedURLException {
        return new URL(url);
    }

}
