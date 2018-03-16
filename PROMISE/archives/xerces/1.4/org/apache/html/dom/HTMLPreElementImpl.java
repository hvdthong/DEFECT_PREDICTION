package org.apache.html.dom;


import org.w3c.dom.*;
import org.w3c.dom.html.*;


/**
 * @version $Revision: 316847 $ $Date: 2001-01-31 07:58:06 +0800 (周三, 2001-01-31) $
 * @author <a href="mailto:arkin@exoffice.com">Assaf Arkin</a>
 * @see org.w3c.dom.html.HTMLPreElement
 * @see ElementImpl
 */
public class HTMLPreElementImpl
    extends HTMLElementImpl
    implements HTMLPreElement
{
    
    
      public int getWidth()
    {
        return getInteger( getAttribute( "width" ) );
    }
    
    
    public void setWidth( int width )
    {
        setAttribute( "width", String.valueOf( width ) );
    }

    
    /**
     * Constructor requires owner document.
     * 
     * @param owner The owner HTML document
     */
    public HTMLPreElementImpl( HTMLDocumentImpl owner, String name )
    {
        super( owner, name );
    }

  
}

