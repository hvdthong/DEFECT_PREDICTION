package org.apache.html.dom;


import org.w3c.dom.*;
import org.w3c.dom.html.*;


/**
 * @version $Revision: 316847 $ $Date: 2001-01-31 07:58:06 +0800 (周三, 2001-01-31) $
 * @author <a href="mailto:arkin@exoffice.com">Assaf Arkin</a>
 * @see org.w3c.dom.html.HTMLDirectoryElement
 * @see ElementImpl
 */
public class HTMLDirectoryElementImpl
    extends HTMLElementImpl
    implements HTMLDirectoryElement
{
    
    
    public boolean getCompact()
    {
        return getBinary( "compact" );
    }
    
    
    public void setCompact( boolean compact )
    {
        setAttribute( "compact", compact );
    }
  

      /**
     * Constructor requires owner document.
     * 
     * @param owner The owner HTML document
     */
    public HTMLDirectoryElementImpl( HTMLDocumentImpl owner, String name )
    {
        super( owner, name );
    }


}

