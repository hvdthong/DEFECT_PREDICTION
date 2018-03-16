package org.apache.xml.serializer;

import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.io.Writer;


/**
 * This class writes unicode characters to a byte stream (java.io.OutputStream)
 * as quickly as possible. It buffers the output in an internal
 * buffer which must be flushed to the OutputStream when done. This flushing
 * is done via the close() flush() or flushBuffer() method. 
 * 
 * This class is only used internally within Xalan.
 * 
 * @xsl.usage internal
 */
final class WriterToUTF8Buffered extends Writer implements WriterChain
{
    
  /** number of bytes that the byte buffer can hold.
   * This is a fixed constant is used rather than m_outputBytes.lenght for performance.
   */
  private static final int BYTES_MAX=16*1024;
  /** number of characters that the character buffer can hold.
   * This is 1/3 of the number of bytes because UTF-8 encoding
   * can expand one unicode character by up to 3 bytes.
   */
  private static final int CHARS_MAX=(BYTES_MAX/3);
  
  
  /** The byte stream to write to. (sc & sb remove final to compile in JDK 1.1.8) */
  private final OutputStream m_os;

  /**
   * The internal buffer where data is stored.
   * (sc & sb remove final to compile in JDK 1.1.8)
   */
  private final byte m_outputBytes[];
  
  private final char m_inputChars[];

  /**
   * The number of valid bytes in the buffer. This value is always
   * in the range <tt>0</tt> through <tt>m_outputBytes.length</tt>; elements
   * <tt>m_outputBytes[0]</tt> through <tt>m_outputBytes[count-1]</tt> contain valid
   * byte data.
   */
  private int count;

  /**
   * Create an buffered UTF-8 writer.
   *
   *
   * @param   out    the underlying output stream.
   *
   * @throws UnsupportedEncodingException
   */
  public WriterToUTF8Buffered(OutputStream out)
          throws UnsupportedEncodingException
  {
      m_os = out;
      m_outputBytes = new byte[BYTES_MAX + 3];
      
      m_inputChars = new char[CHARS_MAX + 2];
      count = 0;
      
  }

  /**
   * Create an buffered UTF-8 writer to write data to the
   * specified underlying output stream with the specified buffer
   * size.
   *
   * @param   out    the underlying output stream.
   * @param   size   the buffer size.
   * @exception IllegalArgumentException if size <= 0.
   */

  /**
   * Write a single character.  The character to be written is contained in
   * the 16 low-order bits of the given integer value; the 16 high-order bits
   * are ignored.
   *
   * <p> Subclasses that intend to support efficient single-character output
   * should override this method.
   *
   * @param c  int specifying a character to be written.
   * @exception  IOException  If an I/O error occurs
   */
  public void write(final int c) throws IOException
  {
    
    /* If we are close to the end of the buffer then flush it.
     * Remember the buffer can hold a few more bytes than BYTES_MAX
     */ 
    if (count >= BYTES_MAX)
        flushBuffer();

    if (c < 0x80)
    {
       m_outputBytes[count++] = (byte) (c);
    }
    else if (c < 0x800)
    {
      m_outputBytes[count++] = (byte) (0xc0 + (c >> 6));
      m_outputBytes[count++] = (byte) (0x80 + (c & 0x3f));
    }
    else if (c < 0x10000)
    {
      m_outputBytes[count++] = (byte) (0xe0 + (c >> 12));
      m_outputBytes[count++] = (byte) (0x80 + ((c >> 6) & 0x3f));
      m_outputBytes[count++] = (byte) (0x80 + (c & 0x3f));
    }
	else
	{
	  m_outputBytes[count++] = (byte) (0xf0 + (c >> 18));
	  m_outputBytes[count++] = (byte) (0x80 + ((c >> 12) & 0x3f));
	  m_outputBytes[count++] = (byte) (0x80 + ((c >> 6) & 0x3f));
	  m_outputBytes[count++] = (byte) (0x80 + (c & 0x3f));
	}

  }


  /**
   * Write a portion of an array of characters.
   *
   * @param  chars  Array of characters
   * @param  start   Offset from which to start writing characters
   * @param  length   Number of characters to write
   *
   * @exception  IOException  If an I/O error occurs
   *
   * @throws java.io.IOException
   */
  public void write(final char chars[], final int start, final int length)
          throws java.io.IOException
  {


    int lengthx3 = 3*length;

    if (lengthx3 >= BYTES_MAX - count)
    {
      flushBuffer();

      if (lengthx3 > BYTES_MAX)
      {
        /*
         * The requested length exceeds the size of the buffer.
         * Cut the buffer up into chunks, each of which will
         * not cause an overflow to the output buffer m_outputBytes,
         * and make multiple recursive calls.
         * Be careful about integer overflows in multiplication.
         */
        int split = length/CHARS_MAX; 
        final int chunks;
        if (split > 1)
            chunks = split;
        else
            chunks = 2;
        int end_chunk = start;
        for (int chunk = 1; chunk <= chunks; chunk++)
        {
            int start_chunk = end_chunk;
            end_chunk = start + (int) ((((long) length) * chunk) / chunks);
            
            final char c = chars[end_chunk - 1]; 
            int ic = chars[end_chunk - 1];
            if (c >= 0xD800 && c <= 0xDBFF) {

                if (end_chunk < start + length) {
                    end_chunk++;
                } else {
                    /* This is the last char of the last chunk,
                     * and it is the high char of a high/low pair with
                     * no low char provided.
                     * TODO: error message needed.
                     * The char array incorrectly ends in a high char
                     * of a high/low surrogate pair, but there is
                     * no corresponding low as the high is the last char 
                     */
                    end_chunk--;
                }
            }


            int len_chunk = (end_chunk - start_chunk);
            this.write(chars,start_chunk, len_chunk);
        }
        return;
      }
    }



    final int n = length+start;
    int i = start;
    {
        /* This block could be omitted and the code would produce
         * the same result. But this block exists to give the JIT
         * a better chance of optimizing a tight and common loop which
         * occurs when writing out ASCII characters. 
         */ 
        char c;
        for(; i < n && (c = chars[i])< 0x80 ; i++ )
            buf_loc[count_loc++] = (byte)c;
    }
    for (; i < n; i++)
    {

      final char c = chars[i];

      if (c < 0x80)
        buf_loc[count_loc++] = (byte) (c);
      else if (c < 0x800)
      {
        buf_loc[count_loc++] = (byte) (0xc0 + (c >> 6));
        buf_loc[count_loc++] = (byte) (0x80 + (c & 0x3f));
      }
      /**
        * The following else if condition is added to support XML 1.1 Characters for 
        * UTF-8:   [1111 0uuu] [10uu zzzz] [10yy yyyy] [10xx xxxx]*
        * Unicode: [1101 10ww] [wwzz zzyy] (high surrogate)
        *          [1101 11yy] [yyxx xxxx] (low surrogate)
        *          * uuuuu = wwww + 1
        */
      else if (c >= 0xD800 && c <= 0xDBFF) 
      {
          char high, low;
          high = c;
          i++;
          low = chars[i];

          buf_loc[count_loc++] = (byte) (0xF0 | (((high + 0x40) >> 8) & 0xf0));
          buf_loc[count_loc++] = (byte) (0x80 | (((high + 0x40) >> 2) & 0x3f));
          buf_loc[count_loc++] = (byte) (0x80 | ((low >> 6) & 0x0f) + ((high << 4) & 0x30));
          buf_loc[count_loc++] = (byte) (0x80 | (low & 0x3f));
      }
      else
      {
        buf_loc[count_loc++] = (byte) (0xe0 + (c >> 12));
        buf_loc[count_loc++] = (byte) (0x80 + ((c >> 6) & 0x3f));
        buf_loc[count_loc++] = (byte) (0x80 + (c & 0x3f));
      }
    }
    count = count_loc;

  }

  /**
   * Write a string.
   *
   * @param  s  String to be written
   *
   * @exception  IOException  If an I/O error occurs
   */
  public void write(final String s) throws IOException
  {

    final int length = s.length();
    int lengthx3 = 3*length;

    if (lengthx3 >= BYTES_MAX - count)
    {
      flushBuffer();

      if (lengthx3 > BYTES_MAX)
      {
        /*
         * The requested length exceeds the size of the buffer,
         * so break it up in chunks that don't exceed the buffer size.
         */
         final int start = 0;
         int split = length/CHARS_MAX; 
         final int chunks;
         if (split > 1)
             chunks = split;
         else
             chunks = 2;
         int end_chunk = 0;
         for (int chunk = 1; chunk <= chunks; chunk++)
         {
             int start_chunk = end_chunk;
             end_chunk = start + (int) ((((long) length) * chunk) / chunks);
             s.getChars(start_chunk,end_chunk, m_inputChars,0);
             int len_chunk = (end_chunk - start_chunk);

             final char c = m_inputChars[len_chunk - 1];
             if (c >= 0xD800 && c <= 0xDBFF) {
                 end_chunk--;
                 len_chunk--;
                 if (chunk == chunks) {
                     /* TODO: error message needed.
                      * The String incorrectly ends in a high char
                      * of a high/low surrogate pair, but there is
                      * no corresponding low as the high is the last char
                      * Recover by ignoring this last char.
                      */
                 }
             }

             this.write(m_inputChars,0, len_chunk);
         }
         return;
      }
    }


    s.getChars(0, length , m_inputChars, 0);
    final char[] chars = m_inputChars;
    final int n = length;
    int i = 0;
    {
        /* This block could be omitted and the code would produce
         * the same result. But this block exists to give the JIT
         * a better chance of optimizing a tight and common loop which
         * occurs when writing out ASCII characters. 
         */ 
        char c;
        for(; i < n && (c = chars[i])< 0x80 ; i++ )
            buf_loc[count_loc++] = (byte)c;
    }
    for (; i < n; i++)
    {

      final char c = chars[i];

      if (c < 0x80)
        buf_loc[count_loc++] = (byte) (c);
      else if (c < 0x800)
      {
        buf_loc[count_loc++] = (byte) (0xc0 + (c >> 6));
        buf_loc[count_loc++] = (byte) (0x80 + (c & 0x3f));
      }
    /**
      * The following else if condition is added to support XML 1.1 Characters for 
      * UTF-8:   [1111 0uuu] [10uu zzzz] [10yy yyyy] [10xx xxxx]*
      * Unicode: [1101 10ww] [wwzz zzyy] (high surrogate)
      *          [1101 11yy] [yyxx xxxx] (low surrogate)
      *          * uuuuu = wwww + 1
      */
    else if (c >= 0xD800 && c <= 0xDBFF) 
    {
        char high, low;
        high = c;
        i++;
        low = chars[i];

        buf_loc[count_loc++] = (byte) (0xF0 | (((high + 0x40) >> 8) & 0xf0));
        buf_loc[count_loc++] = (byte) (0x80 | (((high + 0x40) >> 2) & 0x3f));
        buf_loc[count_loc++] = (byte) (0x80 | ((low >> 6) & 0x0f) + ((high << 4) & 0x30));
        buf_loc[count_loc++] = (byte) (0x80 | (low & 0x3f));
    }
      else
      {
        buf_loc[count_loc++] = (byte) (0xe0 + (c >> 12));
        buf_loc[count_loc++] = (byte) (0x80 + ((c >> 6) & 0x3f));
        buf_loc[count_loc++] = (byte) (0x80 + (c & 0x3f));
      }
    }
    count = count_loc;

  }

  /**
   * Flush the internal buffer
   *
   * @throws IOException
   */
  public void flushBuffer() throws IOException
  {

    if (count > 0)
    {
      m_os.write(m_outputBytes, 0, count);

      count = 0;
    }
  }

  /**
   * Flush the stream.  If the stream has saved any characters from the
   * various write() methods in a buffer, write them immediately to their
   * intended destination.  Then, if that destination is another character or
   * byte stream, flush it.  Thus one flush() invocation will flush all the
   * buffers in a chain of Writers and OutputStreams.
   *
   * @exception  IOException  If an I/O error occurs
   *
   * @throws java.io.IOException
   */
  public void flush() throws java.io.IOException
  {
    flushBuffer();
    m_os.flush();
  }

  /**
   * Close the stream, flushing it first.  Once a stream has been closed,
   * further write() or flush() invocations will cause an IOException to be
   * thrown.  Closing a previously-closed stream, however, has no effect.
   *
   * @exception  IOException  If an I/O error occurs
   *
   * @throws java.io.IOException
   */
  public void close() throws java.io.IOException
  {
    flushBuffer();
    m_os.close();
  }

  /**
   * Get the output stream where the events will be serialized to.
   *
   * @return reference to the result stream, or null of only a writer was
   * set.
   */
  public OutputStream getOutputStream()
  {
    return m_os;
  }

  public Writer getWriter()
  {
    return null;
  }
}
