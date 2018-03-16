package org.apache.xerces.validators.schema.identity;

import org.apache.xerces.utils.NamespacesScope;
import org.apache.xerces.utils.StringPool;
import org.apache.xerces.validators.datatype.DatatypeValidator;

import org.xml.sax.SAXException;

/**
 * Schema identity constraint field.
 *
 * @author Andy Clark, IBM
 * @version $Id: Field.java 316817 2001-01-25 07:19:01Z andyc $
 */
public class Field {


    /** Field XPath. */
    protected Field.XPath fXPath;

    /** Datatype. */
    protected DatatypeValidator fDatatypeValidator;

    /** Identity constraint. */
    protected IdentityConstraint fIdentityConstraint;


    /** Constructs a selector. */
    public Field(Field.XPath xpath, DatatypeValidator datatypeValidator,
                 IdentityConstraint identityConstraint) {
        fXPath = xpath;
        fDatatypeValidator = datatypeValidator;
        fIdentityConstraint = identityConstraint;


    /** Returns the field XPath. */
    public org.apache.xerces.validators.schema.identity.XPath getXPath() {
        return fXPath;

    /** Returns the datatype validator. */
    public DatatypeValidator getDatatypeValidator() {
        return fDatatypeValidator;

    /** Returns the identity constraint. */
    public IdentityConstraint getIdentityConstraint() {
        return fIdentityConstraint;


    /** Creates a field matcher. */
    public XPathMatcher createMatcher(ValueStore store) {
        return new Field.Matcher(fXPath, store);


    /** Returns a string representation of this object. */
    public String toString() {
        return fXPath.toString();


    /**
     * Field XPath.
     *
     * @author Andy Clark, IBM
     */
    public static class XPath
        extends org.apache.xerces.validators.schema.identity.XPath {


        /** Constructs a field XPath expression. */
        public XPath(String xpath, StringPool stringPool,
                     NamespacesScope context) throws XPathException {
            super("./"+xpath, stringPool, context);


    /**
     * Field matcher.
     *
     * @author Andy Clark, IBM
     */
    protected class Matcher
        extends XPathMatcher {


        /** Value store for data values. */
        protected ValueStore fStore;


        /** Constructs a field matcher. */
        public Matcher(Field.XPath xpath, ValueStore store) {
            super(xpath);
            fStore = store;


        /**
         * This method is called when the XPath handler matches the
         * XPath expression.
         */
        protected void matched(String content) throws Exception {
            super.matched(content);
            fStore.addValue(Field.this, content);


