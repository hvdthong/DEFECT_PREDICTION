package org.apache.html.dom;


import org.w3c.dom.*;
import org.w3c.dom.html.*;


/**
 * @version $Revision: 316847 $ $Date: 2001-01-31 07:58:06 +0800 (周三, 2001-01-31) $
 * @author <a href="mailto:arkin@exoffice.com">Assaf Arkin</a>
 * @see org.w3c.dom.html.HTMLFieldSetElement
 * @see ElementImpl
 */
public class HTMLFieldSetElementImpl
    extends HTMLElementImpl
    implements HTMLFieldSetElement, HTMLFormControl
{


      /**
     * Constructor requires owner document.
     * 
     * @param owner The owner HTML document
     */
    public HTMLFieldSetElementImpl( HTMLDocumentImpl owner, String name )
    {
        super( owner, name );
    }
  

}

