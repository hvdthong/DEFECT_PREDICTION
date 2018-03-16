package org.apache.html.dom;


import org.w3c.dom.*;
import org.w3c.dom.html.*;


/**
 * @version $Revision: 316847 $ $Date: 2001-01-31 07:58:06 +0800 (周三, 2001-01-31) $
 * @author <a href="mailto:arkin@exoffice.com">Assaf Arkin</a>
 * @see org.w3c.dom.html.HTMLIsIndexElement
 * @see ElementImpl
 */
public class HTMLIsIndexElementImpl
    extends HTMLElementImpl
    implements HTMLIsIndexElement
{

    
    public String getPrompt()
    {
        return getAttribute( "prompt" );
    }
    
    
    public void setPrompt( String prompt )
    {
        setAttribute( "prompt", prompt );
    }

    
    /**
     * Constructor requires owner document.
     * 
     * @param owner The owner HTML document
     */
    public HTMLIsIndexElementImpl( HTMLDocumentImpl owner, String name )
    {
        super( owner, name );
    }


}

