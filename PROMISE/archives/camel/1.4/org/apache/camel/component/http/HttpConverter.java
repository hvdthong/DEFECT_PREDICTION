package org.apache.camel.component.http;

import java.io.BufferedReader;
import java.io.IOException;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;

import org.apache.camel.Converter;

/**
 * Some converter methods making it easy to convert the body of a message to servlet types or to switch between
 * the underlying {@link ServletInputStream} or {@link BufferedReader} payloads etc.
 *
 * @version $Revision: 667428 $
 */
@Converter
public class HttpConverter {

    @Converter
    public HttpServletRequest toServletRequest(HttpMessage message) {
        if (message == null) {
            return null;
        }
        return message.getRequest();
    }

    @Converter
    public ServletInputStream toServletInputStream(HttpMessage message) throws IOException {
        HttpServletRequest request = toServletRequest(message);
        if (request != null) {
            return request.getInputStream();
        }
        return null;
    }

    @Converter
    public BufferedReader toReader(HttpMessage message) throws IOException {
        HttpServletRequest request = toServletRequest(message);
        if (request != null) {
            return request.getReader();
        }
        return null;
    }

}
