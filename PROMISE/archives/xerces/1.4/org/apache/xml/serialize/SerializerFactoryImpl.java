package org.apache.xml.serialize;


import java.io.OutputStream;
import java.io.Writer;
import java.io.UnsupportedEncodingException;


/**
 * Default serializer factory can construct serializers for the three
 * markup serializers (XML, HTML, XHTML ).
 *
 *
 * @version $Revision: 316040 $ $Date: 2000-08-31 02:59:22 +0800 (周四, 2000-08-31) $
 * @author <a href="mailto:Scott_Boag/CAM/Lotus@lotus.com">Scott Boag</a>
 * @author <a href="mailto:arkin@intalio.com">Assaf Arkin</a>
 */
final class SerializerFactoryImpl
    extends SerializerFactory
{


    private String _method;
    
    
    SerializerFactoryImpl( String method )
    {
        _method = method;
        if ( ! _method.equals( Method.XML ) &&
             ! _method.equals( Method.HTML ) &&
             ! _method.equals( Method.XHTML ) &&
             ! _method.equals( Method.TEXT ) )
            throw new IllegalArgumentException( "SER004 The method '" + method + "' is not supported by this factory\n" + method);
    }


    public Serializer makeSerializer( OutputFormat format )
    {
        Serializer serializer;
        
        serializer = getSerializer( format );
        serializer.setOutputFormat( format );
        return serializer;
    }
    
    
    
    public Serializer makeSerializer( Writer writer,
                                      OutputFormat format )
    {
        Serializer serializer;
        
        serializer = getSerializer( format );
        serializer.setOutputCharStream( writer );
        return serializer;
    }
    
    
    public Serializer makeSerializer( OutputStream output,
                                      OutputFormat format )
        throws UnsupportedEncodingException
    {
        Serializer serializer;
        
        serializer = getSerializer( format );
        serializer.setOutputByteStream( output );
        return serializer;
    }
    
    
    private Serializer getSerializer( OutputFormat format )
    {
        if ( _method.equals( Method.XML ) ) {
            return new XMLSerializer( format );
        } else if ( _method.equals( Method.HTML ) ) {
            return new HTMLSerializer( format );
        }  else if ( _method.equals( Method.XHTML ) ) {
            return new XHTMLSerializer( format );
        }  else if ( _method.equals( Method.TEXT ) ) {
            return new TextSerializer();
        } else {
            throw new IllegalStateException( "SER005 The method '" + _method + "' is not supported by this factory\n" + _method);
        }
    }
    
    
    protected String getSupportedMethod()
    {
        return _method;
    }


}

