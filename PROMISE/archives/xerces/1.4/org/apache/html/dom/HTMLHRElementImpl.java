package org.apache.html.dom;


import org.w3c.dom.*;
import org.w3c.dom.html.*;


/**
 * @version $Revision: 316847 $ $Date: 2001-01-31 07:58:06 +0800 (周三, 2001-01-31) $
 * @author <a href="mailto:arkin@exoffice.com">Assaf Arkin</a>
 * @see org.w3c.dom.html.HTMLHRElement
 * @see ElementImpl
 */
public class HTMLHRElementImpl
    extends HTMLElementImpl
    implements HTMLHRElement
{

    
    public String getAlign()
    {
        return capitalize( getAttribute( "align" ) );
    }
    
    
    public void setAlign( String align )
    {
        setAttribute( "align", align );
    }
  
    
    public boolean getNoShade()
    {
        return getBinary( "noshade" );
    }
    
    
    public void setNoShade( boolean noShade )
    {
        setAttribute( "noshade", noShade );
    }

    
    public String getSize()
    {
        return getAttribute( "size" );
    }
    
    
    public void setSize( String size )
    {
        setAttribute( "size", size );
    }
  
  
      public String getWidth()
    {
        return getAttribute( "width" );
    }
    
    
    public void setWidth( String width )
    {
        setAttribute( "width", width );
    }
    

    /**
     * Constructor requires owner document.
     * 
     * @param owner The owner HTML document
     */
    public HTMLHRElementImpl( HTMLDocumentImpl owner, String name )
    {
        super( owner, name );
    }


}

