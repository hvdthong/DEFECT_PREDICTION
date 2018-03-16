package org.apache.html.dom;


import org.w3c.dom.*;
import org.w3c.dom.html.*;


/**
 * @version $Revision: 316847 $ $Date: 2001-01-31 07:58:06 +0800 (周三, 2001-01-31) $
 * @author <a href="mailto:arkin@exoffice.com">Assaf Arkin</a>
 * @see org.w3c.dom.html.HTMLTextAreaElement
 * @see ElementImpl
 */
public class HTMLTextAreaElementImpl
    extends HTMLElementImpl
    implements HTMLTextAreaElement, HTMLFormControl
{
    
    
    public String getDefaultValue()
    {
        return getAttribute( "default-value" );
    }
    
    
    public void setDefaultValue( String defaultValue )
    {
        setAttribute( "default-value", defaultValue );
    }
  
  

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

    
    public int getCols()
    {
        return getInteger( getAttribute( "cols" ) );
    }
    
    
    public void setCols( int cols )
    {
        setAttribute( "cols", String.valueOf( cols ) );
    }
  
  
    public boolean getDisabled()
    {
        return getBinary( "disabled" );
    }
    
    
    public void setDisabled( boolean disabled )
    {
        setAttribute( "disabled", disabled );
    }

    
    public String getName()
    {
        return getAttribute( "name" );
    }
    
    
    public void setName( String name )
    {
        setAttribute( "name", name );
    }

    
    public boolean getReadOnly()
    {
        return getBinary( "readonly" );
    }
    
    
    public void setReadOnly( boolean readOnly )
    {
        setAttribute( "readonly", readOnly );
    }

    
       public int getRows()
    {
        return getInteger( getAttribute( "rows" ) );
    }
    
    
    public void setRows( int rows )
    {
        setAttribute( "rows", String.valueOf( rows ) );
    }

  
       public int getTabIndex()
    {
        return getInteger( getAttribute( "tabindex" ) );
    }
    
    
    public void setTabIndex( int tabIndex )
    {
        setAttribute( "tabindex", String.valueOf( tabIndex ) );
    }

  
    public String getType()
    {
        return getAttribute( "type" );
    }

    
      public String getValue()
    {
        return getAttribute( "value" );
    }
    
    
    public void setValue( String value )
    {
        setAttribute( "value", value );
    }

    
    public void blur()
    {
    }
    
    
    public void focus()
    {
    }
    
    
    public void select()
    {
    }
    
      
      /**
     * Constructor requires owner document.
     * 
     * @param owner The owner HTML document
     */
    public HTMLTextAreaElementImpl( HTMLDocumentImpl owner, String name )
    {
        super( owner, name );
    }

  
}

