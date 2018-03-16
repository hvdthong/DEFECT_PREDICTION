package org.apache.html.dom;


import org.w3c.dom.*;
import org.w3c.dom.html.*;


/**
 * @version $Revision: 316847 $ $Date: 2001-01-31 07:58:06 +0800 (周三, 2001-01-31) $
 * @author <a href="mailto:arkin@exoffice.com">Assaf Arkin</a>
 * @see org.w3c.dom.html.HTMLLabelElement
 * @see ElementImpl
 */
public class HTMLLabelElementImpl
    extends HTMLElementImpl
    implements HTMLLabelElement, HTMLFormControl
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

    
       public String getHtmlFor()
    {
        return getAttribute( "for" );
    }
    
    
    public void setHtmlFor( String htmlFor )
    {
        setAttribute( "for", htmlFor );
    }

    
    /**
     * Constructor requires owner document.
     * 
     * @param owner The owner HTML document
     */
    public HTMLLabelElementImpl( HTMLDocumentImpl owner, String name )
    {
        super( owner, name );
    }


}

