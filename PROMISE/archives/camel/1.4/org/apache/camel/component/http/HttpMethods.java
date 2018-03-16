package org.apache.camel.component.http;

import org.apache.camel.Exchange;
import org.apache.camel.Expression;
import org.apache.camel.builder.ExpressionBuilder;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.methods.DeleteMethod;
import org.apache.commons.httpclient.methods.EntityEnclosingMethod;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.HeadMethod;
import org.apache.commons.httpclient.methods.OptionsMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.PutMethod;
import org.apache.commons.httpclient.methods.TraceMethod;

public enum HttpMethods implements Expression<Exchange> {

    GET(GetMethod.class), POST(PostMethod.class), PUT(PutMethod.class), DELETE(DeleteMethod.class), HEAD(
        HeadMethod.class), OPTIONS(OptionsMethod.class), TRACE(TraceMethod.class);

    public static final String HTTP_METHOD = "http.requestMethod";

    final Class<? extends HttpMethod> clazz;
    final boolean entity;

    HttpMethods(Class<? extends HttpMethod> clazz) {
        this.clazz = clazz;
        entity = EntityEnclosingMethod.class.isAssignableFrom(clazz);
    }

    public HttpMethod createMethod(final String url) {
        try {
            return clazz.getDeclaredConstructor(String.class).newInstance(url);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public HttpMethod createMethod() {
        try {
            return clazz.newInstance();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public final boolean isEntityEnclosing() {
        return entity;
    }

    public Object evaluate(Exchange exchange) {
        return ExpressionBuilder.constantExpression(name()).evaluate(exchange);
    }

}
