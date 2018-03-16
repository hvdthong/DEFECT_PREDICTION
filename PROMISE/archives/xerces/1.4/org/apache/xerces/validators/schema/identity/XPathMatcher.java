package org.apache.xerces.validators.schema.identity;

import org.apache.xerces.framework.XMLAttrList;
import org.apache.xerces.validators.common.XMLAttributeDecl;
import org.apache.xerces.validators.common.XMLElementDecl;
import org.apache.xerces.validators.schema.SchemaGrammar;
import org.apache.xerces.validators.schema.SchemaSymbols;
import org.apache.xerces.validators.datatype.DatatypeValidator;

import org.apache.xerces.utils.IntStack;
import org.apache.xerces.utils.QName;
import org.apache.xerces.utils.NamespacesScope;
import org.apache.xerces.utils.StringPool;

/***
import org.xml.sax.SAXException;
import org.xml.sax.SAXNotRecognizedException;
import org.xml.sax.SAXNotSupportedException;
/***/

/**
 * XPath matcher.
 *
 * @author Andy Clark, IBM
 *
 * @version $Id: XPathMatcher.java 317137 2001-05-15 22:18:15Z neilg $
 */
public class XPathMatcher {



    /** Compile to true to debug everything. */
    protected static final boolean DEBUG_ALL = false;

    /** Compile to true to debug method callbacks. */
    protected static final boolean DEBUG_METHODS = false || DEBUG_ALL;

    /** Compile to true to debug important method callbacks. */
    protected static final boolean DEBUG_METHODS2 = false || DEBUG_METHODS || DEBUG_ALL;

    /** Compile to true to debug the <em>really</em> important methods. */
    protected static final boolean DEBUG_METHODS3 = false || DEBUG_METHODS || DEBUG_ALL;
                                                      
    /** Compile to true to debug match. */
    protected static final boolean DEBUG_MATCH = false || DEBUG_ALL;

    /** Compile to true to debug step index stack. */
    protected static final boolean DEBUG_STACK = false || DEBUG_ALL;

    /** Don't touch this value unless you add more debug constants. */
    protected static final boolean DEBUG_ANY = DEBUG_METHODS || 
                                               DEBUG_METHODS2 ||
                                               DEBUG_METHODS3 ||
                                               DEBUG_MATCH ||
                                               DEBUG_STACK;


    /** XPath location path. */
    private XPath.LocationPath[] fLocationPaths;

    /** Application preference to buffer content or not. */
    private boolean fShouldBufferContent;

    /** True if should buffer character content <em>at this time</em>. */
    private boolean fBufferContent;

    /** Buffer to hold match text. */
    private StringBuffer fMatchedBuffer = new StringBuffer();

    /** True if XPath has been matched. */
    private boolean[] fMatched;

    /** The matching string. */
    private String fMatchedString;

    /** Integer stack of step indexes. */
    private IntStack[] fStepIndexes;

    /** Current step. */
    private int[] fCurrentStep;

    /** 
     * No match depth. The value of this field will be zero while
     * matching is successful for the given xpath expression.
     */
    private int [] fNoMatchDepth;


    /** String pool. */
    protected StringPool fStringPool;

    /** Namespace scope. */
    protected NamespacesScope fNamespacesScope;

    protected IdentityConstraint fIDConstraint;


    /** 
     * Constructs an XPath matcher that implements a document fragment 
     * handler. 
     *
     * @param xpath   The xpath.
     */
    public XPathMatcher(XPath xpath) {
        this(xpath, false, null);

    /** 
     * Constructs an XPath matcher that implements a document fragment 
     * handler. 
     *
     * @param xpath   The xpath.
     * @param shouldBufferContent True if the matcher should buffer the
     *                            matched content.
     * @param idConstraint:  the identity constraint we're matching for; 
     *      null unless it's a Selector.
     */
    public XPathMatcher(XPath xpath, boolean shouldBufferContent, IdentityConstraint idConstraint) {
        fLocationPaths = xpath.getLocationPaths();
        fShouldBufferContent = shouldBufferContent;
        fIDConstraint = idConstraint;
        fStepIndexes = new IntStack[fLocationPaths.length];
        for(int i=0; i<fStepIndexes.length; i++) fStepIndexes[i] = new IntStack();
        fCurrentStep = new int[fLocationPaths.length];
        fNoMatchDepth = new int[fLocationPaths.length];
        fMatched = new boolean[fLocationPaths.length];
        if (DEBUG_METHODS) {
            System.out.println(toString()+"#<init>()");
        }


    /** Returns true if XPath has been matched. */
    public boolean isMatched() {
        for (int i=0; i < fLocationPaths.length; i++) 
            if (fMatched[i]) return true;
        return false;

    public boolean getIsSelector() {
        return (fIDConstraint == null);

    public IdentityConstraint getIDConstraint() {
        return fIDConstraint; 

    /** Returns the matched string. */
    public String getMatchedString() {
        return fMatchedString;


    /**
     * This method is called when the XPath handler matches the
     * XPath expression. Subclasses can override this method to
     * provide default handling upon a match.
     */
    protected void matched(String content, DatatypeValidator val, boolean isNil) throws Exception {
        if (DEBUG_METHODS3) {
            System.out.println(toString()+"#matched(\""+normalize(content)+"\")");
        }


    /**
     * The start of the document fragment.
     *
     * @param namespaceScope The namespace scope in effect at the
     *                       start of this document fragment.
     * @param grammar:  the schema grammar we're validating against.
     *
     * @throws SAXException Thrown by handler to signal an error.
     */
    public void startDocumentFragment(StringPool stringPool) 
        throws Exception {
        if (DEBUG_METHODS) {
            System.out.println(toString()+"#startDocumentFragment("+
                               "stringPool="+stringPool+','+
                               ")");
        }

        clear();
        for(int i = 0; i < fLocationPaths.length; i++) {
            fStepIndexes[i].clear();
            fCurrentStep[i] = 0;
            fNoMatchDepth[i] = 0;
            fMatched[i]=false;
        }

        fStringPool = stringPool;


    /**
     * The start of an element. If the document specifies the start element
     * by using an empty tag, then the startElement method will immediately
     * be followed by the endElement method, with no intervening methods.
     * 
     * @param element    The name of the element.
     * @param attributes The element attributes.
     * @param eIndex:  the element index of the current element
     * @param grammar:  the currently-active Schema Grammar
     *
     * @throws SAXException Thrown by handler to signal an error.
     */
    public void startElement(QName element, XMLAttrList attributes, int handle, 
                int eIndex, SchemaGrammar grammar)
        throws Exception {
        if (DEBUG_METHODS2) {
            System.out.println(toString()+"#startElement("+
                               "element={"+
                               "prefix="+fStringPool.toString(element.prefix)+','+
                               "localpart="+fStringPool.toString(element.localpart)+','+
                               "rawname="+fStringPool.toString(element.rawname)+','+
                               "uri="+fStringPool.toString(element.uri)+
                               "},"+
                               ")");
        }

        for(int i = 0; i < fLocationPaths.length; i++) {
            int startStep = fCurrentStep[i];
            fStepIndexes[i].push(startStep);

            if (fMatched[i] || fNoMatchDepth[i] > 0) {
                fNoMatchDepth[i]++;
                continue;
            }

            if (DEBUG_STACK) {
                System.out.println(toString()+": "+fStepIndexes[i]);
            }

            XPath.Step[] steps = fLocationPaths[i].steps;
            while (fCurrentStep[i] < steps.length && 
                    steps[fCurrentStep[i]].axis.type == XPath.Axis.SELF) {
                if (DEBUG_MATCH) {
                    XPath.Step step = steps[fCurrentStep[i]];
                    System.out.println(toString()+" [SELF] MATCHED!");
                }
                fCurrentStep[i]++;
            }
            if (fCurrentStep[i] == steps.length) {
                if (DEBUG_MATCH) {
                    System.out.println(toString()+" XPath MATCHED!");
                }
                fMatched[i] = true;
                int j=0;
                for(; j<i && !fMatched[j]; j++);
                if(j==i)
                    fBufferContent = fShouldBufferContent;
                continue;
            }
        
            int descendantStep = fCurrentStep[i];
            while(fCurrentStep[i] < steps.length && steps[fCurrentStep[i]].axis.type == XPath.Axis.DESCENDANT) {
                if (DEBUG_MATCH) {
                    XPath.Step step = steps[fCurrentStep[i]];
                    System.out.println(toString()+" [DESCENDANT] MATCHED!");
                }
                fCurrentStep[i]++;
            }
            if (fCurrentStep[i] == steps.length) {
                if (DEBUG_MATCH) {
                    System.out.println(toString()+" XPath DIDN'T MATCH!");
                }
                fNoMatchDepth[i]++;
                if (DEBUG_MATCH) {
                    System.out.println(toString()+" [CHILD] after NO MATCH");
                }
                continue;
            }

            if ((fCurrentStep[i] == startStep || fCurrentStep[i] > descendantStep) &&
                steps[fCurrentStep[i]].axis.type == XPath.Axis.CHILD) {
                XPath.Step step = steps[fCurrentStep[i]];
                XPath.NodeTest nodeTest = step.nodeTest;
                if (DEBUG_MATCH) {
                    System.out.println(toString()+" [CHILD] before");
                }
                if (nodeTest.type == XPath.NodeTest.QNAME) {
                    if (!nodeTest.name.equals(element)) {
                        if(fCurrentStep[i] > descendantStep) {
                            fCurrentStep[i] = descendantStep;
                            continue;
                        }
                        fNoMatchDepth[i]++;
                        if (DEBUG_MATCH) {
                            System.out.println(toString()+" [CHILD] after NO MATCH");
                        }
                        continue;
                    }
                }
                fCurrentStep[i]++;
                if (DEBUG_MATCH) {
                    System.out.println(toString()+" [CHILD] after MATCHED!");
                }
            }
            if (fCurrentStep[i] == steps.length) {
                fMatched[i] = true;
                int j=0;
                for(; j<i && !fMatched[j]; j++);
                if(j==i)
                    fBufferContent = fShouldBufferContent;
                continue;
            }

            if (fCurrentStep[i] < steps.length &&
                steps[fCurrentStep[i]].axis.type == XPath.Axis.ATTRIBUTE) {
                if (DEBUG_MATCH) {
                    System.out.println(toString()+" [ATTRIBUTE] before");
                }
                int aindex = attributes.getFirstAttr(handle);
                if (aindex != -1) {
                    XPath.NodeTest nodeTest = steps[fCurrentStep[i]].nodeTest;
                    while (aindex != -1) {
                        int aprefix = attributes.getAttrPrefix(aindex);
                        int alocalpart = attributes.getAttrLocalpart(aindex);
                        int arawname = attributes.getAttrName(aindex);
                        int auri = attributes.getAttrURI(aindex);
                        aname.setValues(aprefix, alocalpart, arawname, auri);
                        if (nodeTest.type != XPath.NodeTest.QNAME ||
                            nodeTest.name.equals(aname)) {
                            fCurrentStep[i]++;
                            if (fCurrentStep[i] == steps.length) {
                                fMatched[i] = true;
                                int j=0;
                                for(; j<i && !fMatched[j]; j++);
                                if(j==i) {
                                    int avalue = attributes.getAttValue(aindex);
                                    fMatchedString = fStringPool.toString(avalue);
                                    int attIndex = grammar.getAttributeDeclIndex(eIndex, aname);
                                    XMLAttributeDecl tempAttDecl = new XMLAttributeDecl();
                                    grammar.getAttributeDecl(attIndex, tempAttDecl);
                                    DatatypeValidator aValidator = tempAttDecl.datatypeValidator;
                                    matched(fMatchedString, aValidator, false);
                                }
                            }
                            break;
                        }
                        aindex = attributes.getNextAttr(aindex);
                    }
                }
                if (!fMatched[i]) {
                    if(fCurrentStep[i] > descendantStep) {
                        fCurrentStep[i] = descendantStep;
                        continue;
                    }
                    fNoMatchDepth[i]++;
                    if (DEBUG_MATCH) {
                        System.out.println(toString()+" [ATTRIBUTE] after");
                    }
                    continue;
                }
                if (DEBUG_MATCH) {
                    System.out.println(toString()+" [ATTRIBUTE] after MATCHED!");
                }
            }
        }


    /** Character content. */
    public void characters(char[] ch, int offset, int length) 
        throws Exception {
        if (DEBUG_METHODS) {
            System.out.println(toString()+"#characters("+
                               "text="+normalize(new String(ch, offset, length))+
                               ")");
        }

        for(int i=0; i<fLocationPaths.length; i++) 
            if (fBufferContent && fNoMatchDepth[i] == 0) {
                if (!DEBUG_METHODS && DEBUG_METHODS2) {
                    System.out.println(toString()+"#characters("+
                                   "text="+normalize(new String(ch, offset, length))+
                                   ")");
                }
                fMatchedBuffer.append(ch, offset, length);
                break;
            }
        

    /**
     * The end of an element.
     * 
     * @param element The name of the element.
     * @param eIndex:  the elementDeclIndex of the current element;
     *      needed so that we can look up its datatypeValidator.
     *
     * @throws SAXException Thrown by handler to signal an error.
     */
    public void endElement(QName element, int eIndex, SchemaGrammar grammar) throws Exception {
        if (DEBUG_METHODS2) {
            System.out.println(toString()+"#endElement("+
                               "element={"+
                               "prefix="+fStringPool.toString(element.prefix)+','+
                               "localpart="+fStringPool.toString(element.localpart)+','+
                               "rawname="+fStringPool.toString(element.rawname)+','+
                               "uri="+fStringPool.toString(element.uri)+
                               "ID constraint="+fIDConstraint+
                               "})");
        }
        
        for(int i = 0; i<fLocationPaths.length; i++) {
            if (fNoMatchDepth[i] > 0) {
                fNoMatchDepth[i]--;
            }

            else {
                int j=0;
                for(; j<i && !fMatched[j]; j++);
                if (j<i) continue;
                if (fBufferContent) {
                    fBufferContent = false;
                    fMatchedString = fMatchedBuffer.toString();
                    XMLElementDecl temp = new XMLElementDecl();
                    grammar.getElementDecl(eIndex, temp);
                    DatatypeValidator val = temp.datatypeValidator;
                    if(temp != null) {
                        matched(fMatchedString, val, (grammar.getElementDeclMiscFlags(eIndex) & SchemaSymbols.NILLABLE) != 0);
                    } else 
                        matched(fMatchedString, null, false);
                }
                clear();
            }

            fCurrentStep[i] = fStepIndexes[i].pop();
        
            if (DEBUG_STACK) {
                System.out.println(toString()+": "+fStepIndexes[i]);
            }
        }


    /**
     * The end of the document fragment.
     *
     * @throws SAXException Thrown by handler to signal an error.
     */
    public void endDocumentFragment() throws Exception {
        if (DEBUG_METHODS) {
            System.out.println(toString()+"#endDocumentFragment()");
        }
        clear();

    
    /** Returns a string representation of this object. */
    public String toString() {
        /***
        return fLocationPath.toString();
        /***/
        StringBuffer str = new StringBuffer();
        String s = super.toString();
        int index2 = s.lastIndexOf('.');
        if (index2 != -1) {
            s = s.substring(index2 + 1);
        }
        str.append(s);
        for(int i =0;i<fLocationPaths.length; i++) {
            str.append('[');
            XPath.Step[] steps = fLocationPaths[i].steps;
            for (int j = 0; j < steps.length; j++) {
                if (j == fCurrentStep[i]) {
                    str.append('^');
                }
                str.append(steps[i].toString());
                if (j < steps.length - 1) {
                    str.append('/');
                }
            }
            if (fCurrentStep[i] == steps.length) {
                str.append('^');
            }
            str.append(']');
            str.append(',');
        }
        return str.toString();


    /** Clears the match values. */
    private void clear() {
        fBufferContent = false;
        fMatchedBuffer.setLength(0);
        fMatchedString = null;
        for(int i = 0; i<fLocationPaths.length; i++)
            fMatched[i] = false;

    /** Normalizes text. */
    private String normalize(String s) {
        StringBuffer str = new StringBuffer();
        int length = s.length();
        for (int i = 0; i < length; i++) {
            char c = s.charAt(i);
            switch (c) {
                case '\n': {
                    str.append("\\n");
                    break;
                }
                default: {
                    str.append(c);
                }
            }
        }
        return str.toString();


    
    /** Main program. */
    /***
    public static void main(String[] argv) throws Exception {

        if (DEBUG_ANY) {
            for (int i = 0; i < argv.length; i++) {
                final String expr = argv[i];
                final StringPool symbols = new StringPool();
                final XPath xpath = new XPath(expr, symbols, null);
                final XPathMatcher matcher = new XPathMatcher(xpath, true);
                org.apache.xerces.parsers.SAXParser parser = 
                    new org.apache.xerces.parsers.SAXParser(symbols) {
                    public void startDocument() throws Exception {
                        matcher.startDocumentFragment(symbols, null);
                    }
                    public void startElement(QName element, XMLAttrList attributes, int handle) throws Exception {
                        matcher.startElement(element, attributes, handle);
                    }
                    public void characters(char[] ch, int offset, int length) throws Exception {
                        matcher.characters(ch, offset, length);
                    }
                    public void endElement(QName element) throws Exception {
                        matcher.endElement(element);
                    }
                    public void endDocument() throws Exception {
                        matcher.endDocumentFragment();
                    }
                };
                System.out.println("#### argv["+i+"]: \""+expr+"\" -> \""+xpath.toString()+'"');
                final String uri = argv[++i];
                System.out.println("#### argv["+i+"]: "+uri);
                parser.parse(uri);
            }
        }

    /***/

