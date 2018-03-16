package org.apache.xalan.serialize;

import java.io.*;

/**
 * This class writes ASCII to a byte stream as quickly as possible.  For the
 * moment it does not do buffering, though I reserve the right to do some
 * buffering down the line if I can prove that it will be faster even if the
 * output stream is buffered.
 */
public class WriterToUTF8 extends Writer
{

  /** The byte stream to write to. */
  private final OutputStream m_os;

  /**
   * Create an unbuffered UTF-8 writer.
   *
   *
   * @param os The byte stream to write to.
   *
   * @throws UnsupportedEncodingException
   */
  public WriterToUTF8(OutputStream os) throws UnsupportedEncodingException
  {
    m_os = os;
  }

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

    if (c < 0x80)
      m_os.write(c);
    else if (c < 0x800)
    {
      m_os.write(0xc0 + (c >> 6));
      m_os.write(0x80 + (c & 0x3f));
    }
    else
    {
      m_os.write(0xe0 + (c >> 12));
      m_os.write(0x80 + ((c >> 6) & 0x3f));
      m_os.write(0x80 + (c & 0x3f));
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

    final OutputStream os = m_os;

    int n = length+start;
    for (int i = start; i < n; i++)
    {
      final char c = chars[i];

      if (c < 0x80)
        os.write(c);
      else if (c < 0x800)
      {
        os.write(0xc0 + (c >> 6));
        os.write(0x80 + (c & 0x3f));
      }
      else
      {
        os.write(0xe0 + (c >> 12));
        os.write(0x80 + ((c >> 6) & 0x3f));
        os.write(0x80 + (c & 0x3f));
      }
    }
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

    final int n = s.length();
    final OutputStream os = m_os;

    for (int i = 0; i < n; i++)
    {
      final char c = s.charAt(i);

      if (c < 0x80)
        os.write(c);
      else if (c < 0x800)
      {
        os.write(0xc0 + (c >> 6));
        os.write(0x80 + (c & 0x3f));
      }
      else
      {
        os.write(0xe0 + (c >> 12));
        os.write(0x80 + ((c >> 6) & 0x3f));
        os.write(0x80 + (c & 0x3f));
      }
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
}
