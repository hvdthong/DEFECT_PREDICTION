package org.apache.html.dom;


import org.w3c.dom.*;
import org.w3c.dom.html.*;


/**
 * @version $Revision: 315281 $ $Date: 2000-02-10 12:00:13 +0800 (周四, 2000-02-10) $
 * @author <a href="mailto:arkin@exoffice.com">Assaf Arkin</a>
 * @see org.w3c.dom.html.HTMLTableRowElement
 * @see ElementImpl
 */
public final class HTMLTableRowElementImpl
    extends HTMLElementImpl
    implements HTMLTableRowElement
{

    
    public int getRowIndex()
    {
        Node    parent;
        
        parent = getParentNode();
        if ( parent instanceof HTMLTableSectionElement )
            parent = parent.getParentNode();
        if ( parent instanceof HTMLTableElement )
            return getRowIndex( parent );;
        return -1;
    }
    
    
    public void setRowIndex( int rowIndex )
    {
        Node    parent;
        
        parent = getParentNode();
        if ( parent instanceof HTMLTableSectionElement )
            parent = parent.getParentNode();
        if ( parent instanceof HTMLTableElement )
            ( (HTMLTableElementImpl) parent ).insertRowX( rowIndex, this );
    }

  
    public int getSectionRowIndex()
    {
        Node    parent;
        
        parent = getParentNode();
        if ( parent instanceof HTMLTableSectionElement )
            return getRowIndex( parent );
        else
            return -1;
    }
    
    
    public void setSectionRowIndex( int sectionRowIndex )
    {
        Node    parent;
        
        parent = getParentNode();
        if ( parent instanceof HTMLTableSectionElement )
            ( (HTMLTableSectionElementImpl) parent ).insertRowX( sectionRowIndex, this );
    }
  
  
    int getRowIndex( Node parent )
    {
        NodeList    rows;
        int            i;
        
        rows = ( (HTMLElement) parent ).getElementsByTagName( "TR" );
        for ( i = 0 ; i < rows.getLength() ; ++i )
            if ( rows.item( i ) == this )
                return i;
        return -1;
    }

  
    public HTMLCollection  getCells()
    {
        if ( _cells == null )
            _cells = new HTMLCollectionImpl( this, HTMLCollectionImpl.CELL );
        return _cells;
    }
    
    
    public void setCells( HTMLCollection cells )
    {
        Node    child;
        int        i;
        
        child = getFirstChild();
        while ( child != null )
        {
            removeChild( child );
            child = child.getNextSibling();
        }
        i = 0;
        child = cells.item( i );
        while ( child != null )
        {
            appendChild ( child );
            ++i;
            child = cells.item( i );
        }
    }

  
    public HTMLElement insertCell( int index )
    {
        Node        child;
        HTMLElement    newCell;
        
        newCell = new HTMLTableCellElementImpl( (HTMLDocumentImpl) getOwnerDocument(), "TD" );
        child = getFirstChild();
        while ( child != null )
        {
            if ( child instanceof HTMLTableCellElement )
            {
                if ( index == 0 )
                {
                    insertBefore( newCell, child );
                    return newCell;
                }
                --index;
            }
            child = child.getNextSibling();
        }
        appendChild( newCell );
        return newCell;
    }
    
    
    public void deleteCell( int index )
    {
        Node    child;
        
        child = getFirstChild();
        while ( child != null )
        {
            if ( child instanceof HTMLTableCellElement )
            {
                if ( index == 0 )
                {
                    removeChild ( child );
                    return;
                }
                --index;
            }
            child = child.getNextSibling();
        }
    }

  
    public String getAlign()
    {
        return capitalize( getAttribute( "align" ) );
    }
    
    
    public void setAlign( String align )
    {
        setAttribute( "align", align );
    }

    
    public String getBgColor()
    {
        return getAttribute( "bgcolor" );
    }
    
    
    public void setBgColor( String bgColor )
    {
        setAttribute( "bgcolor", bgColor );
    }

  
    public String getCh()
    {
        String    ch;
        
        ch = getAttribute( "char" );
        if ( ch != null && ch.length() > 1 )
            ch = ch.substring( 0, 1 );
        return ch;
    }
    
    
    public void setCh( String ch )
    {
        if ( ch != null && ch.length() > 1 )
            ch = ch.substring( 0, 1 );
        setAttribute( "char", ch );
    }

    
    public String getChOff()
    {
        return getAttribute( "charoff" );
    }
    
    
    public void setChOff( String chOff )
    {
        setAttribute( "charoff", chOff );
    }
  
  
    public String getVAlign()
    {
        return capitalize( getAttribute( "valign" ) );
    }
    
    
    public void setVAlign( String vAlign )
    {
        setAttribute( "valign", vAlign );
    }

    
      /**
     * Constructor requires owner document.
     * 
     * @param owner The owner HTML document
     */
    public HTMLTableRowElementImpl( HTMLDocumentImpl owner, String name )
    {
        super( owner, name );
    }
  
  
    HTMLCollection    _cells;

  
}

