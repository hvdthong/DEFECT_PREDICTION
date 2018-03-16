package org.apache.xerces.msg;

import java.util.ListResourceBundle;

/**
 * This file contains error and warning messages to be localized
 * The messages are arranged in key and value tuples in a ListResourceBundle.
 *
 * @version $Id: ExceptionMessages.java 315404 2000-04-04 21:14:25Z andyc $
 */
public class ExceptionMessages extends ListResourceBundle {
    /** The list resource bundle contents. */
    public static final Object CONTENTS[][] = {
    
    { "FMT001", "Message Formatting Error." },
    


    { "HTM001", "State error: startDocument fired twice on one builder." },
    { "HTM002", "State error: document never started or missing document element." },
    { "HTM003", "State error: document ended before end of document element." },
    { "HTM004", "Argument ''tagName'' is null." },
    { "HTM005", "State error: Document.getDocumentElement returns null." },
    { "HTM006", "State error: startElement called after end of document element." },
    { "HTM007", "State error: endElement called with no current node." },
    { "HTM008", "State error: mismatch in closing tag name {0}" },
    { "HTM009", "State error: character data found outside of root element." },
    { "HTM010", "State error: character data found outside of root element." },
    { "HTM011", "Argument ''topLevel'' is null." },
    { "HTM012", "Argument ''index'' is negative." },
    { "HTM013", "Argument ''name'' is null." },
    { "HTM014", "Argument ''title'' is null." },
    { "HTM015", "Tag ''{0}'' associated with an Element class that failed to construct." },
    { "HTM016", "Argument ''caption'' is not an element of type <CAPTION>." },
    { "HTM017", "Argument ''tHead'' is not an element of type <THEAD>." },
    { "HTM018", "Argument ''tFoot'' is not an element of type <TFOOT>." },
    { "HTM019", "OpenXML Error: Could not find class {0} implementing HTML element {1}" },


    { "SER001", "Argument ''output'' is null." },
    { "SER002", "No writer supplied for serializer" },
    { "SER003", "The resource [{0}] could not be found." },
    { "SER004", "The resource [{0}] could not load: {1}" },
    { "SER005", "The method ''{0}'' is not supported by this factory" },


    { "DOM001", "Modification not allowed" },
    { "DOM002", "Illegal character" },
    { "DOM003", "Namespace error" },
    { "DOM004", "Index out of bounds" },
    { "DOM005", "Wrong document" },
    { "DOM006", "Hierarchy request error" },
    { "DOM007", "Not supported" },
    { "DOM008", "Not found" },
    { "DOM009", "Attribute already in use" },
    { "DOM010", "Unspecified event type" },
    { "DOM011", "Invalid state" },
    { "DOM012", "Invalid node type" },
    { "DOM013", "Bad boundary points" },

    { "FWK001", "{0}] scannerState: {1}" },
    { "FWK002", "{0}] popElementType: fElementDepth-- == 0." },
    { "FWK003", "TrailingMiscDispatcher.endOfInput moreToFollow" },
    { "FWK004", "cannot happen: {0}" },
    { "FWK005", "parse may not be called while parsing." },
    { "FWK006", "setLocale may not be called while parsing." },
    { "FWK007", "Unknown error domain \"{0}\"." },
    { "FWK008", "Element stack underflow." },
        
    { "PAR001", "Fatal error constructing DOMParser." },
    { "PAR002", "Class, \"{0}\", is not of type org.w3c.dom" },
    { "PAR003", "Class, \"{0}\", not found." },
    { "PAR004", "Cannot setFeature({0}): parse is in progress." },
    { "PAR005", "Property, \"{0}\" is read-only." },
    { "PAR006", "Property value must be of type java.lang.String." },
    { "PAR007", "Current element node cannot be queried when node expansion is deferred." },
    { "PAR008", "Fatal error getting document factory." },
    { "PAR009", "Fatal error reading expansion mode." },
    { "PAR010", "Can''t copy node type, {0} ({1})." },
    { "PAR011", "Feature {0} not supported during parse." },
    { "PAR012", "For propertyId \"{0}\", the value \""+
                "{1}\" cannot be cast to {2}." },
    { "PAR013", "Property \"{0}\" is read only." },
    { "PAR014", "Cannot getProperty(\"{0}\". No DOM tree exists." },
    { "PAR015", "startEntityReference(): ENTITYTYPE_UNPARSED" },
    { "PAR016", "endEntityReference(): ENTITYTYPE_UNPARSED" },
    { "PAR017", "cannot happen: {0}" },
    

    { "RDR001", "untested" },
    { "RDR002", "cannot happen" },
            
    { "UTL001", "untested" },
    { "UTL002", "cannot happen" },


    { "VAL001", "Element stack underflow" },
    { "VAL002", "getValidatorForAttType ({0})" },
    { "VAL003",  "cannot happen" }

        
    };
    /** Returns the list resource bundle contents. */

    public Object[][] getContents() {
        return CONTENTS;
    }
}
