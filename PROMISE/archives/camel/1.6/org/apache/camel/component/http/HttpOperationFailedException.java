package org.apache.camel.component.http;

import java.io.InputStream;

import org.apache.camel.CamelException;
import org.apache.camel.util.ObjectHelper;
import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.StatusLine;

public class HttpOperationFailedException extends CamelException {    
    private final String redirectLocation;
    private final int statusCode;
    private final StatusLine statusLine;
    private final Header[] responseHeaders;
    private final InputStream responseBody;

    public HttpOperationFailedException(int statusCode, StatusLine statusLine, String location, Header[] responseHeaders, InputStream responseBody) {
        super("HTTP operation failed with statusCode: " + statusCode + ", status: " + statusLine + (location != null ? ", redirectLocation: " + location : ""));
        this.statusCode = statusCode;
        this.statusLine = statusLine;
        this.redirectLocation = location;
        this.responseHeaders = responseHeaders;
        this.responseBody = responseBody;
    }

    public HttpOperationFailedException(int statusCode, StatusLine statusLine, Header[] responseHeaders, InputStream responseBody) {
        this(statusCode, statusLine, null, responseHeaders, responseBody);
    }

    public boolean isRedirectError() {
        return statusCode >= 300 && statusCode < 400;
    }

    public boolean hasRedirectLocation() {
        return ObjectHelper.isNotNullAndNonEmpty(redirectLocation);
    }

    public String getRedirectLocation() {
        return redirectLocation;
    }

    public StatusLine getStatusLine() {
        return statusLine;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public Header[] getResponseHeaders() {
        return responseHeaders;
    }

    public InputStream getResponseBody() {
        return responseBody;
    }

}
