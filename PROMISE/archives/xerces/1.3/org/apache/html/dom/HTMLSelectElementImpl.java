package org.apache.html.dom;


import org.w3c.dom.*;
import org.w3c.dom.html.*;


/**
 * @version $Revision: 316847 $ $Date: 2001-01-31 07:58:06 +0800 (周三, 2001-01-31) $
 * @author <a href="mailto:arkin@exoffice.com">Assaf Arkin</a>
 * @see org.w3c.dom.html.HTMLSelectElement
 * @see ElementImpl
 */
public class HTMLSelectElementImpl
    extends HTMLElementImpl
    implements HTMLSelectElement, HTMLFormControl
{
    
    
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

    
    public int getSelectedIndex()
    {
        NodeList    options;
        int            i;
        
        options = getElementsByTagName( "OPTION" );
        for ( i = 0 ; i < options.getLength() ; ++i )
            if ( ( (HTMLOptionElement) options.item( i ) ).getSelected() )
                return i;
        return -1;
    }
    
    
    public void setSelectedIndex( int selectedIndex )
    {
        NodeList    options;
        int            i;
        
        options = getElementsByTagName( "OPTION" );
        for ( i = 0 ; i < options.getLength() ; ++i )
            ( (HTMLOptionElementImpl) options.item( i ) ).setSelected( i == selectedIndex );
    }

  
    public HTMLCollection getOptions()
    {
        if ( _options == null )
            _options = new HTMLCollectionImpl( this, HTMLCollectionImpl.OPTION );
        return _options;
    }
    

    public int getLength()
    {
        return getOptions().getLength();
    }
    
    
    public boolean getDisabled()
    {
        return getBinary( "disabled" );
    }
    
    
    public void setDisabled( boolean disabled )
    {
        setAttribute( "disabled", disabled );
    }

    
      public boolean getMultiple()
    {
        return getBinary( "multiple" );
    }
    
    
    public void setMultiple( boolean multiple )
    {
        setAttribute( "multiple", multiple );
    }

  
      public String getName()
    {
        return getAttribute( "name" );
    }
    
    
    public void setName( String name )
    {
        setAttribute( "name", name );
    }

    
    public int getSize()
    {
        return getInteger( getAttribute( "size" ) );
    }
    
    
    public void setSize( int size )
    {
        setAttribute( "size", String.valueOf( size ) );
    }

  
    public int getTabIndex()
    {
        return getInteger( getAttribute( "tabindex" ) );
    }
    
    
    public void setTabIndex( int tabIndex )
    {
        setAttribute( "tabindex", String.valueOf( tabIndex ) );
    }

    
    public void add( HTMLElement element, HTMLElement before )
    {
        insertBefore( element, before );
    }
  
  
    public void remove( int index )
    {
        NodeList    options;
        Node        removed;
        
        options = getElementsByTagName( "OPTION" );
        removed = options.item( index );
        if ( removed != null )
            removed.getParentNode().removeChild ( removed );
    }

  
    public void               blur()
    {
    }
      
      
    public void               focus()
    {
    }

    /*
     * Explicit implementation of getChildNodes() to avoid problems with
     * overriding the getLength() method hidden in the super class.
     */
    public NodeList getChildNodes() {
        return getChildNodesUnoptimized();
    }
  
    /**
     * Constructor requires owner document.
     * 
     * @param owner The owner HTML document
     */
    public HTMLSelectElementImpl( HTMLDocumentImpl owner, String name )
    {
        super( owner, name );
    }


    private HTMLCollection    _options;
  
  
}

