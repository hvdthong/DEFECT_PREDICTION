package org.apache.xml.serialize;


import java.io.Writer;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;


/**
 * Provides information about encodings. Depends on the Java runtime
 * to provides writers for the different encodings, but can be used
 * to override encoding names and provide the last printable character
 * for each encoding.
 *
 * @version $Revision: 316040 $ $Date: 2000-08-31 02:59:22 +0800 (周四, 2000-08-31) $
 * @author <a href="mailto:arkin@intalio.com">Assaf Arkin</a>
 */
class Encodings
{


    /**
     * The last printable character for unknown encodings.
     */
    static final int DefaultLastPrintable = 0x7F;


    /**
     * Returns a writer for the specified encoding based on
     * an output stream.
     *
     * @param output The output stream
     * @param encoding The encoding
     * @return A suitable writer
     * @throws UnsupportedEncodingException There is no convertor
     *  to support this encoding
     */
    static Writer getWriter( OutputStream output, String encoding )
        throws UnsupportedEncodingException
    {
        for ( int i = 0 ; i < _encodings.length ; ++i ) {
            if ( _encodings[ i ].name.equals( encoding ) )
                return new OutputStreamWriter( output, _encodings[ i ].javaName );
        }
        return new OutputStreamWriter( output, encoding );
    }


    /**
     * Returns the last printable character for the specified
     * encoding.
     *
     * @param encoding The encoding
     * @return The last printable character
     */
    static int getLastPrintable( String encoding )
    {
        for ( int i = 0 ; i < _encodings.length ; ++i ) {
            if ( _encodings[ i ].name.equalsIgnoreCase( encoding ) )
                return _encodings[ i ].lastPrintable;
        }
        return DefaultLastPrintable;
    }


    /**
     * Returns the last printable character for an unspecified
     * encoding.
     */
    static int getLastPrintable()
    {
        return DefaultLastPrintable;
    }


    /**
     * Holds information about a given encoding.
     */
    static final class EncodingInfo
    {
       
        /**
         * The encoding name.
         */ 
        final String name;

        /**
         * The name used by the Java convertor.
         */
        final String javaName;

        /**
         * The last printable character.
         */
        final int    lastPrintable;

        EncodingInfo( String name, String javaName, int lastPrintable )
        {
            this.name = name;
            this.javaName = javaName;
            this.lastPrintable = lastPrintable;
        }

    }


    /**
     * Constructs a list of all the supported encodings.
     */
    private static final EncodingInfo[] _encodings = new EncodingInfo[] {
        new EncodingInfo( "ASCII", "ASCII", 0x7F ),
        new EncodingInfo( "ISO-Latin-1", "ASCII", 0xFF ),
        new EncodingInfo( "ISO-8859-1", "ISO8859_1", 0xFF ),
        new EncodingInfo( "ISO-8859-2", "ISO8859_2", 0xFF ),
        new EncodingInfo( "ISO-8859-3", "ISO8859_3", 0xFF ),
        new EncodingInfo( "ISO-8859-4", "ISO8859_4", 0xFF ),
        new EncodingInfo( "ISO-8859-5", "ISO8859_5", 0xFF ),
        new EncodingInfo( "ISO-8859-6", "ISO8859_6", 0xFF ),
        new EncodingInfo( "ISO-8859-7", "ISO8859_7", 0xFF ),
        new EncodingInfo( "ISO-8859-8", "ISO8859_8", 0xFF ),
        new EncodingInfo( "ISO-8859-9", "ISO8859_9", 0xFF ),
        new EncodingInfo( "UTF-8", "UTF8", 0xFFFF ),
        new EncodingInfo( "UNICODE", "Unicode", 0xFFFF )
    };


}
