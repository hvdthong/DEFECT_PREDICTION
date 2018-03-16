package org.apache.html.dom;


import org.w3c.dom.*;
import org.w3c.dom.html.*;


/**
 * @version $Revision: 316847 $ $Date: 2001-01-31 07:58:06 +0800 (周三, 2001-01-31) $
 * @author <a href="mailto:arkin@exoffice.com">Assaf Arkin</a>
 * @see org.w3c.dom.html.HTMLAreaElement
 * @see ElementImpl
 */
public class HTMLAreaElementImpl
    extends HTMLElementImpl
    implements HTMLAreaElement
{
    
    
    public String getAccessKey()
    {
        String    accessKey;
        
        accessKey = getAttribute( "accesskey" );
        if ( accessKey != null && accessKey.length() > 1 )
            accessKey = accessKey.substring( 0, 1 );
        return accessKey;
    }
    
    
    public void setAccessKey( String accessKey )
    {
        if ( accessKey != null && accessKey.length() > 1 )
            accessKey = accessKey.substring( 0, 1 );
        setAttribute( "accesskey", accessKey );
    }

    
    public String getAlt()
    {
        return getAttribute( "alt" );
    }
    
    
    public void setAlt( String alt )
    {
        setAttribute( "alt", alt );
    }
    
    public String getCoords()
    {
        return getAttribute( "coords" );
    }
    
    
    public void setCoords( String coords )
    {
        setAttribute( "coords", coords );
    }
  
  
    public String getHref()
    {
        return getAttribute( "href" );
    }
    
    
    public void setHref( String href )
    {
        setAttribute( "href", href );
    }

    
    public boolean getNoHref()
    {
        return getBinary( "href" );
    }
    
    
    public void setNoHref( boolean noHref )
    {
        setAttribute( "nohref", noHref );
    }
    
    
    public String getShape()
    {
        return capitalize( getAttribute( "shape" ) );
    }
    
    
    public void setShape( String shape )
    {
        setAttribute( "shape", shape );
    }

    
    public int getTabIndex()
    {
        return getInteger( getAttribute( "tabindex" ) );
    }
    
    
    public void setTabIndex( int tabIndex )
    {
        setAttribute( "tabindex", String.valueOf( tabIndex ) );
    }

    
    public String getTarget()
    {
        return getAttribute( "target" );
    }
    
    
    public void setTarget( String target )
    {
        setAttribute( "target", target );
    }
    
    
    /**
     * Constructor requires owner document.
     * 
     * @param owner The owner HTML document
     */
    public HTMLAreaElementImpl( HTMLDocumentImpl owner, String name )
    {
        super( owner, name );
    }
    
}

