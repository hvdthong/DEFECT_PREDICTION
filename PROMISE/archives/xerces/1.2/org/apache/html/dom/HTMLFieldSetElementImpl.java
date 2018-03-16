package org.apache.html.dom;


import org.w3c.dom.*;
import org.w3c.dom.html.*;


/**
 * @version $Revision: 315281 $ $Date: 2000-02-10 12:00:13 +0800 (周四, 2000-02-10) $
 * @author <a href="mailto:arkin@exoffice.com">Assaf Arkin</a>
 * @see org.w3c.dom.html.HTMLFieldSetElement
 * @see ElementImpl
 */
public final class HTMLFieldSetElementImpl
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

