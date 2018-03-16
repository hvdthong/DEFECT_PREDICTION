package org.apache.camel.builder;

import java.util.Map;

import org.apache.camel.Expression;
import org.apache.camel.builder.xml.Namespaces;
import org.apache.camel.model.ExpressionNode;
import org.apache.camel.model.language.ExpressionType;
import org.apache.camel.model.language.MethodCallExpression;
import org.apache.camel.model.language.XPathExpression;
import org.apache.camel.model.language.XQueryExpression;

/**
 * Represents an expression clause within the DSL which when the expression is complete
 * the clause continues to another part of the DSL
 *
 * @version $Revision: 700514 $
 */
public class ExpressionClause<T> extends ExpressionType {
    private T result;
    private String language;

    public ExpressionClause(T result) {
        this.result = result;
    }

    public static <T extends ExpressionNode> ExpressionClause<T> createAndSetExpression(T result) {
        ExpressionClause<T> clause = new ExpressionClause<T>(result);
        result.setExpression(clause);
        return clause;
    }



    /**
     * Specify an {@link Expression} instance
     */
    public T expression(Expression expression) {
        setExpressionValue(expression);
        return result;
    }

    /**
     * Specify the constant expression value
     */
    public T constant(Object value) {
        return expression(ExpressionBuilder.constantExpression(value));
    }

    /**
     * An expression of the exchange
     */
    public T exchange() {
        return expression(ExpressionBuilder.exchangeExpression());
    }

    /**
     * An expression of an inbound message
     */
    public T inMessage() {
        return expression(ExpressionBuilder.inMessageExpression());
    }

    /**
     * An expression of an inbound message
     */
    public T outMessage() {
        return expression(ExpressionBuilder.outMessageExpression());
    }

    /**
     * An expression of an inbound message body
     */
    public T body() {
        return expression(ExpressionBuilder.bodyExpression());
    }

    /**
     * An expression of an inbound message body converted to the expected type
     */
    public T body(Class expectedType) {
        return expression(ExpressionBuilder.bodyExpression(expectedType));
    }

    /**
     * An expression of an outbound message body
     */
    public T outBody() {
        return expression(ExpressionBuilder.outBodyExpression());
    }

    /**
     * An expression of an outbound message body converted to the expected type
     */
    public T outBody(Class expectedType) {
        return expression(ExpressionBuilder.outBodyExpression(expectedType));
    }

    /**
     * An expression of an inbound message header of the given name
     */
    public T header(String name) {
        return expression(ExpressionBuilder.headerExpression(name));
    }

    /**
     * An expression of the inbound headers
     */
    public T headers() {
        return expression(ExpressionBuilder.headersExpression());
    }

    /**
     * An expression of an outbound message header of the given name
     */
    public T outHeader(String name) {
        return expression(ExpressionBuilder.outHeaderExpression(name));
    }

    /**
     * An expression of the outbound headers
     */
    public T outHeaders() {
        return expression(ExpressionBuilder.outHeadersExpression());
    }

    /**
     * An expression of an exchange property of the given name
     */
    public T property(String name) {
        return expression(ExpressionBuilder.propertyExpression(name));
    }

    /**
     * An expression of the exchange properties
     */
    public T properties() {
        return expression(ExpressionBuilder.propertiesExpression());
    }


    /**
     * Evaluates an expression using the
     * which basically means the bean is invoked to determine the expression value.
     *
     * @param bean the name of the bean looked up the registry
     * @return the builder to continue processing the DSL
     */
    public T method(String bean) {
        MethodCallExpression expression = new MethodCallExpression(bean);
        setExpressionType(expression);
        return result;
    }

    /**
     * Evaluates an expression using the
     * which basically means the bean is invoked to determine the expression value.
     *
     * @param bean   the name of the bean looked up the registry
     * @param method the name of the method to invoke on the bean
     * @return the builder to continue processing the DSL
     */
    public T method(String bean, String method) {
        MethodCallExpression expression = new MethodCallExpression(bean, method);
        setExpressionType(expression);
        return result;
    }

    /**
     *
     * @param text the expression to be evaluated
     * @return the builder to continue processing the DSL
     * @deprecated use language(<tt>"beanshell"</tt>, text). Will be removed in Camel 2.0.
     */
    public T beanShell(String text) {
        return language("beanshell", text);
    }

    /**
     *
     * @param text the expression to be evaluated
     * @return the builder to continue processing the DSL
     */
    public T el(String text) {
        return language("el", text);
    }

    /**
     *
     * @param text the expression to be evaluated
     * @return the builder to continue processing the DSL
     */
    public T groovy(String text) {
        return language("groovy", text);
    }

    /**
     *
     * @param text the expression to be evaluated
     * @return the builder to continue processing the DSL
     */
    public T javaScript(String text) {
        return language("js", text);
    }

    /**
     *
     * @param text the expression to be evaluated
     * @return the builder to continue processing the DSL
     */
    public T jxpath(String text) {
        return language("jxpath", text);
    }

    /**
     *
     * @param text the expression to be evaluated
     * @return the builder to continue processing the DSL
     */
    public T ognl(String text) {
        return language("ognl", text);
    }

    /**
     *
     * @param text the expression to be evaluated
     * @return the builder to continue processing the DSL
     */
    public T php(String text) {
        return language("php", text);
    }

    /**
     *
     * @param text the expression to be evaluated
     * @return the builder to continue processing the DSL
     */
    public T python(String text) {
        return language("python", text);
    }

    /**
     *
     * @param text the expression to be evaluated
     * @return the builder to continue processing the DSL
     */
    public T ruby(String text) {
        return language("ruby", text);
    }

    /**
     *
     * @param text the expression to be evaluated
     * @return the builder to continue processing the DSL
     */
    public T sql(String text) {
        return language("sql", text);
    }

    /**
     *
     * @param text the expression to be evaluated
     * @return the builder to continue processing the DSL
     */
    public T simple(String text) {
        return language("simple", text);
    }

    /**
     *
     * @param text the expression to be evaluated
     * @return the builder to continue processing the DSL
     */
    public T xpath(String text) {
        return language("xpath", text);
    }

    /**
     * with the specified result type
     *
     * @param text       the expression to be evaluated
     * @param resultType the return type expected by the expressiopn
     * @return the builder to continue processing the DSL
     */
    public T xpath(String text, Class resultType) {
        XPathExpression expression = new XPathExpression(text);
        expression.setResultType(resultType);
        setExpressionType(expression);
        return result;
    }

    /**
     * with the specified result type and set of namespace prefixes and URIs
     *
     * @param text       the expression to be evaluated
     * @param resultType the return type expected by the expression
     * @param namespaces the namespace prefix and URIs to use
     * @return the builder to continue processing the DSL
     */
    public T xpath(String text, Class resultType, Namespaces namespaces) {
        return xpath(text, resultType, namespaces.getNamespaces());
    }

    /**
     * with the specified result type and set of namespace prefixes and URIs
     *
     * @param text       the expression to be evaluated
     * @param resultType the return type expected by the expression
     * @param namespaces the namespace prefix and URIs to use
     * @return the builder to continue processing the DSL
     */
    public T xpath(String text, Class resultType, Map<String, String> namespaces) {
        XPathExpression expression = new XPathExpression(text);
        expression.setResultType(resultType);
        expression.setNamespaces(namespaces);
        setExpressionType(expression);
        return result;
    }

    /**
     * with the specified set of namespace prefixes and URIs
     *
     * @param text       the expression to be evaluated
     * @param namespaces the namespace prefix and URIs to use
     * @return the builder to continue processing the DSL
     */
    public T xpath(String text, Namespaces namespaces) {
        return xpath(text, namespaces.getNamespaces());
    }

    /**
     * with the specified set of namespace prefixes and URIs
     *
     * @param text       the expression to be evaluated
     * @param namespaces the namespace prefix and URIs to use
     * @return the builder to continue processing the DSL
     */
    public T xpath(String text, Map<String, String> namespaces) {
        XPathExpression expression = new XPathExpression(text);
        expression.setNamespaces(namespaces);
        setExpressionType(expression);
        return result;
    }

    /**
     *
     * @param text the expression to be evaluated
     * @return the builder to continue processing the DSL
     */
    public T xquery(String text) {
        return language("xquery", text);
    }

    /**
     * with the specified result type
     *
     * @param text       the expression to be evaluated
     * @param resultType the return type expected by the expressiopn
     * @return the builder to continue processing the DSL
     */
    public T xquery(String text, Class resultType) {
        XQueryExpression expression = new XQueryExpression(text);
        expression.setResultType(resultType);
        setExpressionType(expression);
        return result;
    }

    /**
     * with the specified result type and set of namespace prefixes and URIs
     *
     * @param text       the expression to be evaluated
     * @param resultType the return type expected by the expression
     * @param namespaces the namespace prefix and URIs to use
     * @return the builder to continue processing the DSL
     */
    public T xquery(String text, Class resultType, Namespaces namespaces) {
        return xquery(text, resultType, namespaces.getNamespaces());
    }

    /**
     * with the specified result type and set of namespace prefixes and URIs
     *
     * @param text       the expression to be evaluated
     * @param resultType the return type expected by the expression
     * @param namespaces the namespace prefix and URIs to use
     * @return the builder to continue processing the DSL
     */
    public T xquery(String text, Class resultType, Map<String, String> namespaces) {
        XQueryExpression expression = new XQueryExpression(text);
        expression.setResultType(resultType);
        expression.setNamespaces(namespaces);
        setExpressionType(expression);
        return result;
    }

    /**
     * with the specified set of namespace prefixes and URIs
     *
     * @param text       the expression to be evaluated
     * @param namespaces the namespace prefix and URIs to use
     * @return the builder to continue processing the DSL
     */
    public T xquery(String text, Namespaces namespaces) {
        return xquery(text, namespaces.getNamespaces());
    }

    /**
     * with the specified set of namespace prefixes and URIs
     *
     * @param text       the expression to be evaluated
     * @param namespaces the namespace prefix and URIs to use
     * @return the builder to continue processing the DSL
     */
    public T xquery(String text, Map<String, String> namespaces) {
        XQueryExpression expression = new XQueryExpression(text);
        expression.setNamespaces(namespaces);
        setExpressionType(expression);
        return result;
    }

    /**
     * Evaluates a given language name with the expression text
     *
     * @param language   the name of the language
     * @param expression the expression in the given language
     * @return the builder to continue processing the DSL
     */
    public T language(String language, String expression) {
        setLanguage(language);
        setExpression(expression);
        return result;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }
}
