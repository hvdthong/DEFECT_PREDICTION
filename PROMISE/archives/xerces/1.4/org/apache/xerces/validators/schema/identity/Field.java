package org.apache.xerces.validators.schema.identity;

import org.apache.xerces.utils.NamespacesScope;
import org.apache.xerces.utils.StringPool;

import org.apache.xerces.validators.datatype.DatatypeValidator;

import org.xml.sax.SAXException;

/**
 * Schema identity constraint field.
 *
 * @author Andy Clark, IBM
 * @version $Id: Field.java 317201 2001-06-01 20:08:45Z neilg $
 */
public class Field {


    /** Field XPath. */
    protected Field.XPath fXPath;

    /** Datatype. */

    /** Identity constraint. */
    protected IdentityConstraint fIdentityConstraint;

    protected boolean mayMatch = true;


    /** Constructs a field. */
    public Field(Field.XPath xpath, 
                 IdentityConstraint identityConstraint) {
        fXPath = xpath;
        fIdentityConstraint = identityConstraint;


    public void setMayMatch(boolean b) {
        mayMatch = b;
    
    public boolean mayMatch() {
        return mayMatch;
    
    /** Returns the field XPath. */
    public org.apache.xerces.validators.schema.identity.XPath getXPath() {
        return fXPath;

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
			super(((xpath.trim().startsWith("/") ||xpath.trim().startsWith("."))?
				xpath:"./"+xpath), stringPool, context);
			


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
            super(xpath, true, null);
            fStore = store;


        /**
         * This method is called when the XPath handler matches the
         * XPath expression.
         */
        protected void matched(String content, DatatypeValidator val, boolean isNil) throws Exception {
            super.matched(content, val, isNil);
            if(isNil) {
                fStore.reportNilError(fIdentityConstraint);
            }
            fStore.addValue(Field.this, new IDValue(content, val));
            mayMatch = false;


