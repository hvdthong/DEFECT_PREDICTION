package org.apache.html.dom;


import org.w3c.dom.*;
import org.w3c.dom.html.*;


/**
 * @version $Revision: 315281 $ $Date: 2000-02-10 12:00:13 +0800 (周四, 2000-02-10) $
 * @author <a href="mailto:arkin@openxml.org">Assaf Arkin</a>
 * @see org.w3c.dom.html.HTMLOptionElement
 * @see ElementImpl
 */
public final class HTMLOptionElementImpl
    extends HTMLElementImpl
    implements HTMLOptionElement
{

    

    public boolean getDefaultSelected()
    {
        return getBinary( "default-selected" );
    }
    
    
    public void setDefaultSelected( boolean defaultSelected )
    {
        setAttribute( "default-selected", defaultSelected );
    }

  
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
    
    
    public int getIndex()
    {
        Node        parent;
        NodeList    options;
        int            i;
        
        parent = getParentNode();
        while ( parent != null && ! ( parent instanceof HTMLSelectElement ) )
            parent = parent.getParentNode();
        if ( parent != null )
        {
            options = ( (HTMLElement) parent ).getElementsByTagName( "OPTION" );
            for ( i = 0 ; i < options.getLength() ; ++i )
                if ( options.item( i ) == this )
                    return i;
        }
        return -1;
    }
    
    
    public void setIndex( int index )
    {
        Node        parent;
        NodeList    options;
        Node        item;
        
        parent = getParentNode();
        while ( parent != null && ! ( parent instanceof HTMLSelectElement ) )
            parent = parent.getParentNode();
        if ( parent != null )
        {
            options = ( (HTMLElement) parent ).getElementsByTagName( "OPTION" );
            if ( options.item( index ) != this )
            {
                getParentNode().removeChild( this );
                item = options.item( index );
                item.getParentNode().insertBefore( this, item );
            }
        }
    }
  
  
    public boolean getDisabled()
    {
        return getBinary( "disabled" );
    }
    
    
    public void setDisabled( boolean disabled )
    {
        setAttribute( "disabled", disabled );
    }

    
      public String getLabel()
    {
        return capitalize( getAttribute( "label" ) );
    }
    
    
    public void setLabel( String label )
    {
        setAttribute( "label", label );
    }

    
    public boolean getSelected()
    {
        return getBinary( "selected" );
    }
  
  
    public void setSelected( boolean selected )
    {
        setAttribute( "selected", selected );
    }
    
        
    public String getValue()
    {
        return getAttribute( "value" );
    }
    
    
    public void setValue( String value )
    {
        setAttribute( "value", value );
    }

    
    /**
     * Constructor requires owner document.
     * 
     * @param owner The owner HTML document
     */
    public HTMLOptionElementImpl( HTMLDocumentImpl owner, String name )
    {
        super( owner, name );
    }


}

