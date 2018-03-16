package org.apache.html.dom;


import org.w3c.dom.*;
import org.w3c.dom.html.*;


/**
 * @version $Revision: 315281 $ $Date: 2000-02-10 12:00:13 +0800 (周四, 2000-02-10) $
 * @author <a href="mailto:arkin@exoffice.com">Assaf Arkin</a>
 * @see org.w3c.dom.html.HTMLScriptElement
 * @see ElementImpl
 */
public final class HTMLScriptElementImpl
    extends HTMLElementImpl
    implements HTMLScriptElement
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

    
       public String getHtmlFor()
    {
        return getAttribute( "for" );
    }
    
    
    public void setHtmlFor( String htmlFor )
    {
        setAttribute( "for", htmlFor );
    }

    
       public String getEvent()
    {
        return getAttribute( "event" );
    }
    
    
    public void setEvent( String event )
    {
        setAttribute( "event", event );
    }
    
       public String getCharset()
    {
        return getAttribute( "charset" );
    }
    
    
    public void setCharset( String charset )
    {
        setAttribute( "charset", charset );
    }

    
    public boolean getDefer()
    {
        return getBinary( "defer" );
    }
    
    
    public void setDefer( boolean defer )
    {
        setAttribute( "defer", defer );
    }

  
       public String getSrc()
    {
        return getAttribute( "src" );
    }
    
    
    public void setSrc( String src )
    {
        setAttribute( "src", src );
    }

  
    public String getType()
    {
        return getAttribute( "type" );
    }
    
    
    public void setType( String type )
    {
        setAttribute( "type", type );
    }
    
    
      /**
     * Constructor requires owner document.
     * 
     * @param owner The owner HTML document
     */
    public HTMLScriptElementImpl( HTMLDocumentImpl owner, String name )
    {
        super( owner, name );
    }

  
}

