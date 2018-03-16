package org.apache.camel.component.jhc;

import java.io.IOException;
import java.io.OutputStream;

import org.apache.http.ConnectionReuseStrategy;
import org.apache.http.HttpEntity;
import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpException;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.HttpResponseFactory;
import org.apache.http.HttpStatus;
import org.apache.http.HttpVersion;
import org.apache.http.MethodNotSupportedException;
import org.apache.http.ProtocolException;
import org.apache.http.ProtocolVersion;
import org.apache.http.UnsupportedHttpVersionException;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.nio.ContentDecoder;
import org.apache.http.nio.ContentEncoder;
import org.apache.http.nio.NHttpServerConnection;
import org.apache.http.nio.NHttpServiceHandler;
import org.apache.http.nio.entity.ContentBufferEntity;
import org.apache.http.nio.entity.ContentOutputStream;
import org.apache.http.nio.protocol.NHttpServiceHandlerBase;
import org.apache.http.nio.util.ByteBufferAllocator;
import org.apache.http.nio.util.ContentInputBuffer;
import org.apache.http.nio.util.ContentOutputBuffer;
import org.apache.http.nio.util.HeapByteBufferAllocator;
import org.apache.http.nio.util.SimpleInputBuffer;
import org.apache.http.nio.util.SimpleOutputBuffer;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.ExecutionContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.protocol.HttpProcessor;
import org.apache.http.protocol.HttpRequestHandler;
import org.apache.http.util.EncodingUtils;

/**
 * HTTP service handler implementation that buffers the content of HTTP messages
 * entirely in memory and processes HTTP requests on the main I/O thread.
 *
 * <p>This service handler should be used only when dealing with HTTP messages
 * that are known to be limited in length</p>
 *
 * @author <a href="mailto:oleg at ural.ru">Oleg Kalnichevski</a>
 *
 */
public class BufferingHttpServiceHandler extends NHttpServiceHandlerBase
                                         implements NHttpServiceHandler {

    public BufferingHttpServiceHandler(
            final HttpProcessor httpProcessor,
            final HttpResponseFactory responseFactory,
            final ConnectionReuseStrategy connStrategy,
            final ByteBufferAllocator allocator,
            final HttpParams params) {
        super(httpProcessor, responseFactory, connStrategy, allocator, params);
    }

    public BufferingHttpServiceHandler(
            final HttpProcessor httpProcessor,
            final HttpResponseFactory responseFactory,
            final ConnectionReuseStrategy connStrategy,
            final HttpParams params) {
        this(httpProcessor, responseFactory, connStrategy,
                new HeapByteBufferAllocator(), params);
    }

    public void connected(final NHttpServerConnection conn) {
        HttpContext context = conn.getContext();

        ServerConnState connState = new ServerConnState(allocator);
        context.setAttribute(CONN_STATE, connState);

        if (this.eventListener != null) {
            this.eventListener.connectionOpen(conn);
        }
    }

    public void requestReceived(final NHttpServerConnection conn) {
        HttpContext context = conn.getContext();

        HttpRequest request = conn.getHttpRequest();
        request.setParams(this.params);

        ServerConnState connState = (ServerConnState) context.getAttribute(CONN_STATE);

        connState.resetInput();
        connState.setRequest(request);
        connState.setInputState(ServerConnState.REQUEST_RECEIVED);

        ProtocolVersion ver = request.getRequestLine().getProtocolVersion();
        if (!ver.lessEquals(HttpVersion.HTTP_1_1)) {
            ver = HttpVersion.HTTP_1_1;
        }

        HttpResponse response;

        try {

            if (request instanceof HttpEntityEnclosingRequest) {
                if (((HttpEntityEnclosingRequest) request).expectContinue()) {
                    response = this.responseFactory.newHttpResponse(
                        ver, HttpStatus.SC_CONTINUE, context);
                    request.setParams(this.params);

                    if (this.expectationVerifier != null) {
                        try {
                            this.expectationVerifier.verify(request, response, context);
                        } catch (HttpException ex) {
                            response = this.responseFactory.newHttpResponse(
                                    HttpVersion.HTTP_1_0,
                                    HttpStatus.SC_INTERNAL_SERVER_ERROR,
                                    context);
                            request.setParams(this.params);
                            handleException(ex, response);
                        }
                    }

                    if (response.getStatusLine().getStatusCode() < 200) {
                        conn.submitResponse(response);
                    } else {
                        conn.resetInput();
                        connState.resetInput();
                        sendResponse(conn, response);
                    }
                }
            } else {
                conn.suspendInput();
                processRequest(conn, request);
            }

        } catch (IOException ex) {
            shutdownConnection(conn, ex);
            if (this.eventListener != null) {
                this.eventListener.fatalIOException(ex, conn);
            }
        } catch (HttpException ex) {
            closeConnection(conn, ex);
            if (this.eventListener != null) {
                this.eventListener.fatalProtocolException(ex, conn);
            }
        }
    }

    public void closed(final NHttpServerConnection conn) {
        if (this.eventListener != null) {
            this.eventListener.connectionClosed(conn);
        }
    }

    public void exception(final NHttpServerConnection conn, final HttpException httpex) {
        HttpContext context = conn.getContext();
        try {
            HttpResponse response = this.responseFactory.newHttpResponse(
                HttpVersion.HTTP_1_0, HttpStatus.SC_INTERNAL_SERVER_ERROR, context);
            response.setParams(this.params);
            handleException(httpex, response);
            response.setEntity(null);
            sendResponse(conn, response);

        } catch (IOException ex) {
            shutdownConnection(conn, ex);
            if (this.eventListener != null) {
                this.eventListener.fatalIOException(ex, conn);
            }
        } catch (HttpException ex) {
            closeConnection(conn, ex);
            if (this.eventListener != null) {
                this.eventListener.fatalProtocolException(ex, conn);
            }
        }
    }

    public void inputReady(final NHttpServerConnection conn, final ContentDecoder decoder) {
        HttpContext context = conn.getContext();
        HttpRequest request = conn.getHttpRequest();

        ServerConnState connState = (ServerConnState) context.getAttribute(CONN_STATE);
        ContentInputBuffer buffer = connState.getInbuffer();

        connState.setInputState(ServerConnState.REQUEST_BODY_STREAM);

        try {
            buffer.consumeContent(decoder);
            if (decoder.isCompleted()) {
                connState.setInputState(ServerConnState.REQUEST_BODY_DONE);

                HttpEntityEnclosingRequest entityReq = (HttpEntityEnclosingRequest) request;
                if (entityReq.getEntity() != null) {
                    entityReq.setEntity(new ContentBufferEntity(
                            entityReq.getEntity(),
                            connState.getInbuffer()));
                }
                conn.suspendInput();
                processRequest(conn, request);
            }

        } catch (IOException ex) {
            shutdownConnection(conn, ex);
            if (this.eventListener != null) {
                this.eventListener.fatalIOException(ex, conn);
            }
        } catch (HttpException ex) {
            closeConnection(conn, ex);
            if (this.eventListener != null) {
                this.eventListener.fatalProtocolException(ex, conn);
            }
        }
    }

    public void responseReady(final NHttpServerConnection conn) {
    }

    public void outputReady(final NHttpServerConnection conn, final ContentEncoder encoder) {

        HttpContext context = conn.getContext();
        HttpResponse response = conn.getHttpResponse();

        ServerConnState connState = (ServerConnState) context.getAttribute(CONN_STATE);
        ContentOutputBuffer buffer = connState.getOutbuffer();

        connState.setOutputState(ServerConnState.RESPONSE_BODY_STREAM);

        try {

            buffer.produceContent(encoder);
            if (encoder.isCompleted()) {
                connState.setOutputState(ServerConnState.RESPONSE_BODY_DONE);
                connState.resetOutput();
                if (!this.connStrategy.keepAlive(response, context)) {
                    conn.close();
                } else {
                    conn.requestInput();
                }
            }

        } catch (IOException ex) {
            shutdownConnection(conn, ex);
            if (this.eventListener != null) {
                this.eventListener.fatalIOException(ex, conn);
            }
        }
    }

    protected void handleException(final HttpException ex, final HttpResponse response) {
        int code = HttpStatus.SC_INTERNAL_SERVER_ERROR;
        if (ex instanceof MethodNotSupportedException) {
            code = HttpStatus.SC_NOT_IMPLEMENTED;
        } else if (ex instanceof UnsupportedHttpVersionException) {
            code = HttpStatus.SC_HTTP_VERSION_NOT_SUPPORTED;
        } else if (ex instanceof ProtocolException) {
            code = HttpStatus.SC_BAD_REQUEST;
        }
        response.setStatusCode(code);

        byte[] msg = EncodingUtils.getAsciiBytes(ex.getMessage());
        ByteArrayEntity entity = new ByteArrayEntity(msg);
        entity.setContentType("text/plain; charset=US-ASCII");
        response.setEntity(entity);
    }

    protected void processRequest(
            final NHttpServerConnection conn,
            final HttpRequest request) throws IOException, HttpException {

        HttpContext context = conn.getContext();
        ProtocolVersion ver = request.getRequestLine().getProtocolVersion();

        if (!ver.lessEquals(HttpVersion.HTTP_1_1)) {
            ver = HttpVersion.HTTP_1_1;
        }

        HttpResponse response = this.responseFactory.newHttpResponse(
            ver, HttpStatus.SC_OK, conn.getContext());
        request.setParams(this.params);

        context.setAttribute(ExecutionContext.HTTP_REQUEST, request);
        context.setAttribute(ExecutionContext.HTTP_CONNECTION, conn);
        context.setAttribute(ExecutionContext.HTTP_RESPONSE, response);

        try {

            this.httpProcessor.process(request, context);

            HttpRequestHandler handler = null;
            if (this.handlerResolver != null) {
                String requestURI = request.getRequestLine().getUri();
                handler = this.handlerResolver.lookup(requestURI);
            }
            if (handler != null) {
                handler.handle(request, response, context);
            } else {
                response.setStatusCode(HttpStatus.SC_NOT_IMPLEMENTED);
            }

        } catch (HttpException ex) {
            response = this.responseFactory.newHttpResponse(HttpVersion.HTTP_1_0,
                HttpStatus.SC_INTERNAL_SERVER_ERROR, context);
            request.setParams(this.params);
            handleException(ex, response);
        }

        sendResponse(conn, response);
    }

    protected void sendResponse(
            final NHttpServerConnection conn,
            final HttpResponse response) throws IOException, HttpException {

        HttpContext context = conn.getContext();

        ServerConnState connState = (ServerConnState) context.getAttribute(CONN_STATE);
        ContentOutputBuffer buffer = connState.getOutbuffer();

        this.httpProcessor.process(response, context);

        if (!canResponseHaveBody(connState.getRequest(), response)) {
            response.setEntity(null);
        }

        conn.submitResponse(response);

        connState.setOutputState(ServerConnState.RESPONSE_SENT);

        HttpEntity entity = response.getEntity();
        if (entity != null) {
            OutputStream outstream = new ContentOutputStream(buffer);
            entity.writeTo(outstream);
            outstream.flush();
            outstream.close();
        } else {
            connState.resetOutput();
            if (!this.connStrategy.keepAlive(response, context)) {
                conn.close();
            } else {
                conn.requestInput();
            }
        }
    }

    static class ServerConnState {

        public static final int READY                      = 0;
        public static final int REQUEST_RECEIVED           = 1;
        public static final int REQUEST_BODY_STREAM        = 2;
        public static final int REQUEST_BODY_DONE          = 4;
        public static final int RESPONSE_SENT              = 8;
        public static final int RESPONSE_BODY_STREAM       = 16;
        public static final int RESPONSE_BODY_DONE         = 32;

        private SimpleInputBuffer inbuffer;
        private ContentOutputBuffer outbuffer;

        private int inputState;
        private int outputState;

        private HttpRequest request;
        private final ByteBufferAllocator allocator;

        public ServerConnState(final ByteBufferAllocator allocator) {
            super();
            this.inputState = READY;
            this.outputState = READY;
            this.allocator = allocator;
        }

        public ContentInputBuffer getInbuffer() {
            if (this.inbuffer == null) {
                this.inbuffer = new SimpleInputBuffer(2048, allocator);
            }
            return this.inbuffer;
        }

        public ContentOutputBuffer getOutbuffer() {
            if (this.outbuffer == null) {
                this.outbuffer = new SimpleOutputBuffer(2048, allocator);
            }
            return this.outbuffer;
        }

        public int getInputState() {
            return this.inputState;
        }

        public void setInputState(int inputState) {
            this.inputState = inputState;
        }

        public int getOutputState() {
            return this.outputState;
        }

        public void setOutputState(int outputState) {
            this.outputState = outputState;
        }

        public HttpRequest getRequest() {
            return this.request;
        }

        public void setRequest(final HttpRequest request) {
            this.request = request;
        }

        public void resetInput() {
            this.inbuffer = null;
            this.request = null;
            this.inputState = READY;
        }

        public void resetOutput() {
            this.outbuffer = null;
            this.outputState = READY;
        }

    }

}
