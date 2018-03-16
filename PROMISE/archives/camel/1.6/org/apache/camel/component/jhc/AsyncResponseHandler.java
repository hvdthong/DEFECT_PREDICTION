package org.apache.camel.component.jhc;

import java.io.IOException;

import org.apache.http.HttpException;
import org.apache.http.HttpResponse;

public interface AsyncResponseHandler {

    void sendResponse(HttpResponse response) throws IOException, HttpException;

}
