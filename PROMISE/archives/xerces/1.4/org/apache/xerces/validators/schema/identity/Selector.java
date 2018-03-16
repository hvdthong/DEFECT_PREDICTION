package org.apache.xerces.validators.schema.identity;

import org.apache.xerces.framework.XMLAttrList;
import org.apache.xerces.validators.schema.SchemaGrammar;
import org.apache.xerces.utils.QName;
import org.apache.xerces.utils.NamespacesScope;
import org.apache.xerces.utils.StringPool;

import org.xml.sax.SAXException;

/**
 * Schema identity constraint selector.
 *
 * @author Andy Clark, IBM
 * @version $Id: Selector.java 317201 2001-06-01 20:08:45Z neilg $
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
     * @version $Id: Selector.java 317201 2001-06-01 20:08:45Z neilg $
     */
    public static class XPath
        extends org.apache.xerces.validators.schema.identity.XPath {
    
    
        /** Constructs a selector XPath expression. */
        public XPath(String xpath, StringPool stringPool, 
                     NamespacesScope context) throws XPathException {
			super(((xpath.trim().startsWith("/") ||xpath.trim().startsWith("."))?
				xpath:"./"+xpath), stringPool, context);
    
			for (int i=0;i<fLocationPaths.length;i++) {
				org.apache.xerces.validators.schema.identity.XPath.Axis axis =
					fLocationPaths[i].steps[fLocationPaths[i].steps.length-1].axis;
            if (axis.type == axis.ATTRIBUTE) {
                throw new XPathException("selectors cannot select attributes");
            }
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
            super(xpath, false, Selector.this.fIdentityConstraint);
            fFieldActivator = activator;

    
        public void startDocumentFragment(StringPool stringPool)
            throws Exception {
            super.startDocumentFragment(stringPool);
            fElementDepth = 0;
            fMatchedDepth = -1;

        /**
         * The start of an element. If the document specifies the start element
         * by using an empty tag, then the startElement method will immediately
         * be followed by the endElement method, with no intervening methods.
         * 
         * @param element    The name of the element.
         * @param attributes The element attributes.
         * @param handle:  beginning of the attribute list 
         * @param elemIndex:  index of the element holding these attributes
         * @param grammar:  the SchemaGrammar that all this is being validated by
         *
         * @throws SAXException Thrown by handler to signal an error.
         */
        public void startElement(QName element, XMLAttrList attributes, 
                                 int handle, int elemIndex, SchemaGrammar grammar) throws Exception {
            super.startElement(element, attributes, handle, elemIndex, grammar);
            fElementDepth++;
    
            if (fMatchedDepth == -1 && isMatched()) {
                fMatchedDepth = fElementDepth;
                fFieldActivator.startValueScopeFor(fIdentityConstraint);
                int count = fIdentityConstraint.getFieldCount();
                for (int i = 0; i < count; i++) {
                    Field field = fIdentityConstraint.getFieldAt(i);
                    XPathMatcher matcher = fFieldActivator.activateField(field);
                    matcher.startElement(element, attributes, handle, elemIndex, grammar);
                }
            }
    
    
        public void endElement(QName element, int elemIndex, SchemaGrammar grammar) throws Exception {
            super.endElement(element, elemIndex, grammar);
            if (fElementDepth-- == fMatchedDepth) {
                fMatchedDepth = -1;
                fFieldActivator.endValueScopeFor(fIdentityConstraint);
            }
        }


