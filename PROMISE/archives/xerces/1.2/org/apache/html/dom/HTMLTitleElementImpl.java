package org.apache.html.dom;


import org.w3c.dom.*;
import org.w3c.dom.html.*;


/**
 * @version $Revision: 315281 $ $Date: 2000-02-10 12:00:13 +0800 (周四, 2000-02-10) $
 * @author <a href="mailto:arkin@exoffice.com">Assaf Arkin</a>
 * @see org.w3c.dom.html.HTMLTitleElement
 * @see ElementImpl
 */
public final class HTMLTitleElementImpl
    extends HTMLElementImpl
    implements HTMLTitleElement
{
 
    
    public String getText()
    {
        Node    child;
        String    text;
        
        child = getFirstChild();
        text = "";
        while ( child != null )
        {
            if ( child instanceof Text )
                text = text + ( (Text) child ).getData();
            child = child.getNextSibling();
        }
        return text;
    }
    
    
    public void setText( String text )
    {
        Node    child;
        Node    next;
        
        child = getFirstChild();
        while ( child != null )
        {
            next = child.getNextSibling();
            removeChild( child );
            child = next;
        }
        insertBefore( getOwnerDocument().createTextNode( text ), getFirstChild() );
    }

        
      /**
     * Constructor requires owner document.
     * 
     * @param owner The owner HTML document
     */
    public HTMLTitleElementImpl( HTMLDocumentImpl owner, String name )
    {
        super( owner, name );
    }

  
}

