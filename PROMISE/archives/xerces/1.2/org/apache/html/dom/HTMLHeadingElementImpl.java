package org.apache.html.dom;


import org.w3c.dom.*;
import org.w3c.dom.html.*;


/**
 * @version $Revision: 315281 $ $Date: 2000-02-10 12:00:13 +0800 (周四, 2000-02-10) $
 * @author <a href="mailto:arkin@exoffice.com">Assaf Arkin</a>
 * @see org.w3c.dom.html.HTMLHeadingElement
 * @see ElementImpl
 */
public final class HTMLHeadingElementImpl
    extends HTMLElementImpl
    implements HTMLHeadingElement
{

    
    public String getAlign()
    {
        return getCapitalized( "align" );
    }
    
    
    public void setAlign( String align )
    {
        setAttribute( "align", align );
    }
  
    
    /**
     * Constructor requires owner document.
     * 
     * @param owner The owner HTML document
     */
    public HTMLHeadingElementImpl( HTMLDocumentImpl owner, String name )
    {
        super( owner, name );
    }


}

