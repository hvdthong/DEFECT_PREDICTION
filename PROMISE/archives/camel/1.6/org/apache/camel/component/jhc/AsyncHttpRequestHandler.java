package org.apache.camel.component.jhc;

import java.io.IOException;

import org.apache.http.HttpException;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.protocol.HttpContext;
import org.apache.http.protocol.HttpRequestHandler;

public interface AsyncHttpRequestHandler extends HttpRequestHandler {

    void handle(HttpRequest request, HttpContext context, AsyncResponseHandler handler) throws HttpException,
        IOException;

}
