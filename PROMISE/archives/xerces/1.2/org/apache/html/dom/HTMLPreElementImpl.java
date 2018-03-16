package org.apache.html.dom;


import org.w3c.dom.*;
import org.w3c.dom.html.*;


/**
 * @version $Revision: 315281 $ $Date: 2000-02-10 12:00:13 +0800 (周四, 2000-02-10) $
 * @author <a href="mailto:arkin@exoffice.com">Assaf Arkin</a>
 * @see org.w3c.dom.html.HTMLPreElement
 * @see ElementImpl
 */
public final class HTMLPreElementImpl
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

