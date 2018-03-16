package org.apache.ivy.util.url;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.HttpURLConnection;
import java.util.regex.Pattern;
import java.io.IOException;

public abstract class AbstractURLHandler implements URLHandler {
    
    private static final Pattern ESCAPE_PATTERN = Pattern.compile("%25([0-9a-fA-F][0-9a-fA-F])");

    private static int requestMethod = REQUEST_METHOD_HEAD;

    public boolean isReachable(URL url) {
        return getURLInfo(url).isReachable();
    }

    public boolean isReachable(URL url, int timeout) {
        return getURLInfo(url, timeout).isReachable();
    }

    public long getContentLength(URL url) {
        return getURLInfo(url).getContentLength();
    }

    public long getContentLength(URL url, int timeout) {
        return getURLInfo(url, timeout).getContentLength();
    }

    public long getLastModified(URL url) {
        return getURLInfo(url).getLastModified();
    }

    public long getLastModified(URL url, int timeout) {
        return getURLInfo(url, timeout).getLastModified();
    }

    protected void validatePutStatusCode(
            URL dest, int statusCode, String statusMessage) throws IOException {
        switch (statusCode) {
            case HttpURLConnection.HTTP_OK:
                /* intentional fallthrough */
            case HttpURLConnection.HTTP_CREATED:
                /* intentional fallthrough */
            case HttpURLConnection.HTTP_ACCEPTED:
                /* intentional fallthrough */
            case HttpURLConnection.HTTP_NO_CONTENT:
                break;
            case HttpURLConnection.HTTP_UNAUTHORIZED:
                /* intentional fallthrough */
            case HttpURLConnection.HTTP_FORBIDDEN:
                throw new IOException("Access to URL " + dest + " was refused by the server" 
                    + (statusMessage == null ? "" : ": " + statusMessage));
            default:
                throw new IOException("PUT operation to URL " + dest + " failed with status code " 
                    + statusCode + (statusMessage == null ? "" : ": " + statusMessage));
        }
    }
    
    public void setRequestMethod(int requestMethod) {
        AbstractURLHandler.requestMethod = requestMethod;
    }
    
    public int getRequestMethod() {
        return requestMethod;
    }
    
    protected String normalizeToString(URL url) throws IOException {
        if (!"http".equals(url.getProtocol()) && !"https".equals(url.getProtocol())) {
            return url.toExternalForm();
        }
        
        try {
            URI uri = new URI(url.getProtocol(), url.getUserInfo(), url.getHost(), 
                    url.getPort(), url.getPath(), url.getQuery(), url.getRef());
            
            String uriString = uri.toASCIIString();
            
            uriString = uriString.replaceAll("\\+", "%2B");
            
            return ESCAPE_PATTERN.matcher(uriString).replaceAll("%$1");
        } catch (URISyntaxException e) {
            IOException ioe = new MalformedURLException("Couldn't convert '" 
                + url.toString() + "' to a valid URI"); 
            ioe.initCause(e); 
            throw ioe;
        }
    }
    
    protected URL normalizeToURL(URL url) throws IOException {
        if (!"http".equals(url.getProtocol()) && !"https".equals(url.getProtocol())) {
            return url;
        }
        
        return new URL(normalizeToString(url));
    }

}
