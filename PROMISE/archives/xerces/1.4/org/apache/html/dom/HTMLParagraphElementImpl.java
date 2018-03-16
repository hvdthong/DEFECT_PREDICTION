package org.apache.html.dom;


import org.w3c.dom.*;
import org.w3c.dom.html.*;


/**
 * @version $Revision: 316847 $ $Date: 2001-01-31 07:58:06 +0800 (周三, 2001-01-31) $
 * @author <a href="mailto:arkin@exoffice.com">Assaf Arkin</a>
 * @see org.w3c.dom.html.HTMLParagraphElement
 * @see ElementImpl
 */
public class HTMLParagraphElementImpl
    extends HTMLElementImpl
    implements HTMLParagraphElement
{
    
    
    public String getAlign()
    {
        return getAttribute( "align" );
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
    public HTMLParagraphElementImpl( HTMLDocumentImpl owner, String name )
    {
        super( owner, name );
    }

  
}

