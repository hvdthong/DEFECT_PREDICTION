package org.apache.xerces.validators.schema.identity;

import org.apache.xerces.framework.XMLAttrList;
import org.apache.xerces.utils.NamespacesScope;
import org.apache.xerces.utils.QName;
import org.apache.xerces.utils.StringPool;

import org.xml.sax.SAXException;

/**
 * Schema identity constraint selector.
 *
 * @author Andy Clark, IBM
 * @version $Id: Selector.java 316817 2001-01-25 07:19:01Z andyc $
 */
public class Selector {


    /** XPath. */
    protected Selector.XPath fXPath;

    /** Identity constraint. */
    protected IdentityConstraint fIdentityConstraint;


    /** Constructs a selector. */
    public Selector(Selector.XPath xpath, 
                    IdentityConstraint identityConstraint) {
        fXPath = xpath;
        fIdentityConstraint = identityConstraint;


    /** Returns the selector XPath. */
    public org.apache.xerces.validators.schema.identity.XPath getXPath() {
        return fXPath;

    /** Returns the identity constraint. */
    public IdentityConstraint getIdentityConstraint() {
        return fIdentityConstraint;


    /** Creates a selector matcher. */
    public XPathMatcher createMatcher(FieldActivator activator) {
        return new Selector.Matcher(fXPath, activator);


    /** Returns a string representation of this object. */
    public String toString() {
        return fXPath.toString();


    /**
     * Schema identity constraint selector XPath expression.
     *
     * @author Andy Clark, IBM
     * @version $Id: Selector.java 316817 2001-01-25 07:19:01Z andyc $
     */
    public static class XPath
        extends org.apache.xerces.validators.schema.identity.XPath {
    
    
        /** Constructs a selector XPath expression. */
        public XPath(String xpath, StringPool stringPool, 
                     NamespacesScope context) throws XPathException {
            super("./"+xpath, stringPool, context);
    
            XPath.Axis axis = fLocationPath.steps[fLocationPath.steps.length-1].axis;
            if (axis.type == axis.ATTRIBUTE) {
                throw new XPathException("selectors cannot select attributes");
            }
    
    

    /**
     * Selector matcher.
     *
     * @author Andy Clark, IBM
     */
    protected class Matcher
        extends XPathMatcher {
    

        /** Field activator. */
        protected FieldActivator fFieldActivator;

        /** Element depth. */
        protected int fElementDepth;

        /** Depth at match. */
        protected int fMatchedDepth;


        /** Constructs a selector matcher. */
        public Matcher(Selector.XPath xpath, FieldActivator activator) {
            super(xpath);
            fFieldActivator = activator;

    
        public void startDocumentFragment(StringPool stringPool,
                                          NamespacesScope namespacesScope)
            throws Exception {
            super.startDocumentFragment(stringPool,namespacesScope);
            fElementDepth = 0;
            fMatchedDepth = -1;

        /**
         * The start of an element. If the document specifies the start element
         * by using an empty tag, then the startElement method will immediately
         * be followed by the endElement method, with no intervening methods.
         * 
         * @param element    The name of the element.
         * @param attributes The element attributes.
         *
         * @throws SAXException Thrown by handler to signal an error.
         */
        public void startElement(QName element, XMLAttrList attributes, 
                                 int handle) throws Exception {
            super.startElement(element, attributes, handle);
            fElementDepth++;
    
            if (isMatched()) {
                fMatchedDepth = fElementDepth;
                fFieldActivator.startValueScopeFor(fIdentityConstraint);
                int count = fIdentityConstraint.getFieldCount();
                for (int i = 0; i < count; i++) {
                    Field field = fIdentityConstraint.getFieldAt(i);
                    XPathMatcher matcher = fFieldActivator.activateField(field);
                    matcher.startElement(element, attributes, handle);
                }
            }
    
    
        public void endElement(QName element) throws Exception {
            super.endElement(element);
            if (fElementDepth-- == fMatchedDepth) {
                fMatchedDepth = -1;
                fFieldActivator.endValueScopeFor(fIdentityConstraint);
            }
        }


