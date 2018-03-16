package org.apache.html.dom;


import org.w3c.dom.*;
import org.w3c.dom.html.*;


/**
 * @version $Revision: 316847 $ $Date: 2001-01-31 07:58:06 +0800 (周三, 2001-01-31) $
 * @author <a href="mailto:arkin@exoffice.com">Assaf Arkin</a>
 * @see org.w3c.dom.html.HTMLBodyElement
 * @see ElementImpl
 */
public class HTMLBodyElementImpl
    extends HTMLElementImpl
    implements HTMLBodyElement
{
    
    public String getALink()
    {
        return getAttribute( "alink" );
    }

    
    public void setALink(String aLink)
    {
        setAttribute( "alink", aLink );
    }
    
  
    public String getBackground()
    {
        return getAttribute( "background" );
    }
    
  
    public void setBackground( String background )
    {
        setAttribute( "background", background );
    }
    
  
    public String getBgColor()
    {
        return getAttribute( "bgcolor" );
    }
    
    
    public void setBgColor(String bgColor)
    {
        setAttribute( "bgcolor", bgColor );
    }
    
  
    public String getLink()
    {
        return getAttribute( "link" );
    }
  
    
    public void setLink(String link)
    {
        setAttribute( "link", link );
    }
    
  
    public String getText()
    {
        return getAttribute( "text" );
    }
    
  
    public void setText(String text)
    {
        setAttribute( "text", text );
    }
    
  
    public String getVLink()
    {
        return getAttribute( "vlink" );
    }
  
    
    public void  setVLink(String vLink)
    {
        setAttribute( "vlink", vLink );
    }
  
    
      /**
     * Constructor requires owner document.
     * 
     * @param owner The owner HTML document
     */
    public HTMLBodyElementImpl( HTMLDocumentImpl owner, String name )
    {
        super( owner, name );
    }

  
}

