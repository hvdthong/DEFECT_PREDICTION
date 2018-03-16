package org.apache.xerces.tree;

import java.io.*;
import java.util.Hashtable;




/**
 * This handles several XML-related tasks that normal java.io Readers
 * don't support, inluding use of IETF standard encoding names and
 * automatic detection of most XML encodings.  The former is needed
 * for interoperability; the latter is needed to conform with the XML
 * spec.  This class also optimizes reading some common encodings by
 * providing low-overhead unsynchronized Reader support.
 *
 * <P> Note that the autodetection facility should be used only on
 * data streams which have an unknown character encoding.  For example,
 * it should never be used on MIME text/xml entities.
 *
 * <P> Note that XML processors are only required to support UTF-8 and
 * UTF-16 character encodings.  Autodetection permits the underlying Java
 * implementation to provide support for many other encodings, such as
 * US-ASCII, ISO-8859-5, Shift_JIS, EUC-JP, and ISO-2022-JP.
 *
 * @author David Brownell
 * @version $Revision: 315418 $
 */
final class XmlReader extends Reader
{
    private static final int MAXPUSHBACK = 512;

    private Reader	in;
    private String	assignedEncoding;
    private boolean	closed;


    /**
     * Constructs the reader from an input stream, autodetecting
     * the encoding to use according to the heuristic specified
     * in the XML 1.0 recommendation.
     *
     * @param in the input stream from which the reader is constructed
     * @exception IOException on error, such as unrecognized encoding
     */
    public static Reader createReader (InputStream in) throws IOException
    {
	return new XmlReader (in);
    }

    /**
     * Creates a reader supporting the given encoding, mapping
     * from standard encoding names to ones that understood by
     * Java where necessary.
     *
     * @param in the input stream from which the reader is constructed
     * @param encoding the IETF standard name of the encoding to use;
     *	if null, autodetection is used.
     * @exception IOException on error, including unrecognized encoding
     */
    public static Reader createReader (InputStream in, String encoding)
    throws IOException
    {
	if (encoding == null)
	    return new XmlReader (in);
	if ("UTF-8".equalsIgnoreCase (encoding)
		|| "UTF8".equalsIgnoreCase (encoding))
	    return new Utf8Reader (in);
	if ("US-ASCII".equalsIgnoreCase (encoding)
		|| "ASCII".equalsIgnoreCase (encoding))
	    return new AsciiReader (in);
	if ("ISO-8859-1".equalsIgnoreCase (encoding)
		)
	    return new Iso8859_1Reader (in);

	return new InputStreamReader (in, std2java (encoding));
    }

    static private final Hashtable charsets = new Hashtable (31);

    static {
	charsets.put ("UTF-16", "Unicode");
	charsets.put ("ISO-10646-UCS-2", "Unicode");


	charsets.put ("EBCDIC-CP-US", "cp037");
	charsets.put ("EBCDIC-CP-CA", "cp037");
	charsets.put ("EBCDIC-CP-NL", "cp037");
	charsets.put ("EBCDIC-CP-WT", "cp037");

	charsets.put ("EBCDIC-CP-DK", "cp277");
	charsets.put ("EBCDIC-CP-NO", "cp277");
	charsets.put ("EBCDIC-CP-FI", "cp278");
	charsets.put ("EBCDIC-CP-SE", "cp278");

	charsets.put ("EBCDIC-CP-IT", "cp280");
	charsets.put ("EBCDIC-CP-ES", "cp284");
	charsets.put ("EBCDIC-CP-GB", "cp285");
	charsets.put ("EBCDIC-CP-FR", "cp297");

	charsets.put ("EBCDIC-CP-AR1", "cp420");
	charsets.put ("EBCDIC-CP-HE", "cp424");
	charsets.put ("EBCDIC-CP-BE", "cp500");
	charsets.put ("EBCDIC-CP-CH", "cp500");

	charsets.put ("EBCDIC-CP-ROECE", "cp870");
	charsets.put ("EBCDIC-CP-YU", "cp870");
	charsets.put ("EBCDIC-CP-IS", "cp871");
	charsets.put ("EBCDIC-CP-AR2", "cp918");

    }

    private static String std2java (String encoding)
    {
	String temp = encoding.toUpperCase ();
	temp = (String) charsets.get (temp);
	return temp != null ? temp : encoding;
    }

    /** Returns the standard name of the encoding in use */
    public String getEncoding ()
    {
	return assignedEncoding;
    }

    private XmlReader (InputStream stream) throws IOException
    {
	super (stream);

	PushbackInputStream	pb;
        byte			buf [];
	int			len;

	/*if (stream instanceof PushbackInputStream)
	    pb = (PushbackInputStream) stream;
	else*/
	/**
	 * Commented out the above code to make sure it works when the
	 * document is accessed using http. URL connection in the code uses
	 * a PushbackInputStream with size 7 and when we try to push back
	 * MAX which default value is set to 512 we get and exception. So
	 * that's why we need to wrap the stream irrespective of what type
	 * of stream we start off with.
	 */
	pb = new PushbackInputStream (stream, MAXPUSHBACK);

	buf = new byte [4];
	len = pb.read (buf);
	if (len > 0)
	    pb.unread (buf, 0, len);

	if (len == 4) switch (buf [0] & 0x0ff) {
            case 0:
              if (buf [1] == 0x3c && buf [2] == 0x00 && buf [3] == 0x3f) {
		  setEncoding (pb, "UnicodeBig");
                  return;
              }
	      break;

              switch (buf [1] & 0x0ff) {
                default:
                  break;

                case 0x00:
                  if (buf [2] == 0x3f && buf [3] == 0x00) {
		      setEncoding (pb, "UnicodeLittle");
		      return;
                  }
		  break;

                case '?': 
                  if (buf [2] != 'x' || buf [3] != 'm')
		      break;
		  useEncodingDecl (pb, "UTF8");
                  return;
              }
	      break;

            case 0x4c:
              if (buf [1] == 0x6f
		    && (0x0ff & buf [2]) == 0x0a7
		    && (0x0ff & buf [3]) == 0x094) {
		  useEncodingDecl (pb, "CP037");
		  return;
	      }
	      break;

            case 0xfe:
              if ((buf [1] & 0x0ff) != 0xff)
                  break;
	      setEncoding (pb, "UTF-16");
              return;

            case 0xff:
              if ((buf [1] & 0x0ff) != 0xfe)
                  break;
	      setEncoding (pb, "UTF-16");
	      return;

            default:
              break;
        }

	setEncoding (pb, "UTF-8");
    }

    /*
     * Read the encoding decl on the stream, knowing that it should
     * be readable using the specified encoding (basically, ASCII or
     * EBCDIC).  The body of the document may use a wider range of
     * characters than the XML/Text decl itself, so we switch to use
     * the specified encoding as soon as we can.  (ASCII is a subset
     * of UTF-8, ISO-8859-*, ISO-2022-JP, EUC-JP, and more; EBCDIC
     * has a variety of "code pages" that have these characters as
     * a common subset.)
     */
    private void useEncodingDecl (PushbackInputStream pb, String encoding)
    throws IOException
    {
	byte			buffer [] = new byte [MAXPUSHBACK];
	int			len;
	Reader			r;
	int			c;

	len = pb.read (buffer, 0, buffer.length);
	pb.unread (buffer, 0, len);
	r = new InputStreamReader (
		new ByteArrayInputStream (buffer, 4, len),
		encoding);

	if ((c = r.read ()) != 'l') {
	    setEncoding (pb, "UTF-8");
	    return;
	}

	StringBuffer	buf = new StringBuffer ();
	StringBuffer	keyBuf = null;
	String		key = null;
	boolean		sawEq = false;
	char		quoteChar = 0;
	boolean		sawQuestion = false;

    XmlDecl:
	for (int i = 0; i < MAXPUSHBACK - 5; ++i) {
	    if ((c = r.read ()) == -1)
		break;

	    if (c == ' ' || c == '\t' || c == '\n' || c == '\r')
		continue;

	    if (i == 0)
		break;
	    
	    if (c == '?')
		sawQuestion = true;
	    else if (sawQuestion) {
		if (c == '>')
		    break;
		sawQuestion = false;
	    }
	    
	    if (key == null || !sawEq) {
		if (keyBuf == null) {
		    if (Character.isWhitespace ((char) c))
			continue;
		    keyBuf = buf;
		    buf.setLength (0);
		    buf.append ((char)c);
		    sawEq = false;
		} else if (Character.isWhitespace ((char) c)) {
		    key = keyBuf.toString ();
		} else if (c == '=') {
		    if (key == null)
			key = keyBuf.toString ();
		    sawEq = true;
		    keyBuf = null;
		    quoteChar = 0;
		} else
		    keyBuf.append ((char)c);
		continue;
	    }

	    if (Character.isWhitespace ((char) c))
		continue;
	    if (c == '"' || c == '\'') {
		if (quoteChar == 0) {
		    quoteChar = (char) c;
		    buf.setLength (0);
		    continue;
		} else if (c == quoteChar) {
		    if ("encoding".equals (key)) {
			assignedEncoding = buf.toString ();

			for (i = 0; i < assignedEncoding.length(); i++) {
			    c = assignedEncoding.charAt (i);
			    if ((c >= 'A' && c <= 'Z')
				    || (c >= 'a' && c <= 'z'))
				continue;
			    if (i == 0)
				break XmlDecl;
			    if (i > 0 && (c == '-'
				    || (c >= '0' && c <= '9')
				    || c == '.' || c == '_'))
				continue;
			    break XmlDecl;
			}

			setEncoding (pb, assignedEncoding);
			return;

		    } else {
			key = null;
			continue;
		    }
		}
	    }
	    buf.append ((char) c);
	}

	setEncoding (pb, "UTF-8");
    }

    private void setEncoding (InputStream stream, String encoding)
    throws IOException
    {
	assignedEncoding = encoding;
	in = createReader (stream, encoding);
    }

    /**
     * Reads the number of characters read into the buffer, or -1 on EOF.
     */
    public int read (char buf [], int off, int len) throws IOException
    {
	int	val;

	if (closed)
	val = in.read (buf, off, len);
	if (val == -1)
	    close ();
	return val;
    }

    /**
     * Reads a single character.
     */
    public int read () throws IOException
    {
	int	val;

	if (closed)
	    throw new IOException ("closed");
	val = in.read ();
	if (val == -1)
	    close ();
	return val;
    }

    /**
     * Returns true iff the reader supports mark/reset.
     */
    public boolean markSupported ()
    {
	return in == null ? false : in.markSupported ();
    }

    /**
     * Sets a mark allowing a limited number of characters to
     * be "peeked", by reading and then resetting.
     * @param value how many characters may be "peeked".
     */
    public void mark (int value) throws IOException
    {
	if (in != null) in.mark (value);
    }

    /**
     * Resets the current position to the last marked position.
     */
    public void reset () throws IOException
    {
	if (in != null) in.reset ();
    }

    /**
     * Skips a specified number of characters.
     */
    public long skip (long value) throws IOException
    {
	return in == null ? 0 : in.skip (value);
    }

    /**
     * Returns true iff input characters are known to be ready.
     */
    public boolean ready () throws IOException
    {
	return in == null ? false : in.ready ();
    }

    /**
     * Closes the reader.
     */
    public void close () throws IOException
    {
	if (closed)
	    return;
	in.close ();
	in = null;
	closed = true;
    }

    static abstract class BaseReader extends Reader
    {
	protected InputStream	instream;
	protected byte		buffer [];
	protected int		start, finish;

	BaseReader (InputStream stream)
	{
	    super (stream);

	    instream = stream;
	    buffer = new byte [8192];
	}

	public boolean ready () throws IOException
	{
	    return instream == null
		|| (finish - start) > 0
		||  instream.available () != 0;
	}

	public void close () throws IOException
	{
	    if (instream != null) {
		instream.close ();
		start = finish = 0;
		buffer = null;
		instream = null;
	    }
	}
    }

    static final class Utf8Reader extends BaseReader
    {
	private char		nextChar;

	Utf8Reader (InputStream stream)
	{
	    super (stream);
	}

	public int read (char buf [], int offset, int len) throws IOException
	{
	    int i = 0, c = 0;

	    if (len <= 0)
		return 0;
	 
            if ((offset + len) > buf.length || offset < 0)
                throw new ArrayIndexOutOfBoundsException ();

	    if (nextChar != 0) {
		buf [offset + i++] = nextChar;
		nextChar = 0;
	    }

	    while (i < len) {
		if (finish <= start) {
		    if (instream == null) {
			c = -1;
			break;
		    }
		    start = 0;
		    finish = instream.read (buffer, 0, buffer.length);
		    if (finish <= 0) {
			this.close ();
			c = -1;
			break;
		    }
		}
		

		c = buffer [start] & 0x0ff;
		if ((c & 0x80) == 0x00) {
		    start++;
		    buf [offset + i++] = (char) c;
		    continue;
		}
		
		int		off = start;
		
		try {
		    if ((buffer [off] & 0x0E0) == 0x0C0) {
			c  = (buffer [off++] & 0x1f) << 6;
			c +=  buffer [off++] & 0x3f;


		    } else if ((buffer [off] & 0x0F0) == 0x0E0) {
			c  = (buffer [off++] & 0x0f) << 12;
			c += (buffer [off++] & 0x3f) << 6;
			c +=  buffer [off++] & 0x3f;


		    } else if ((buffer [off] & 0x0f8) == 0x0F0) {
			c  = (buffer [off++] & 0x07) << 18;
			c += (buffer [off++] & 0x3f) << 12;
			c += (buffer [off++] & 0x3f) << 6;
			c +=  buffer [off++] & 0x3f;


			if (c > 0x0010ffff)
			    throw new CharConversionException (
				"UTF-8 encoding of character 0x00"
				+ Integer.toHexString (c)
				+ " can't be converted to Unicode."
				);

			else if (c > 0xffff) {
			    c -= 0x10000;
			    nextChar = (char) (0xDC00 + (c & 0x03ff));
			    c = 0xD800 + (c >> 10);
			}
		    } else
			throw new CharConversionException (
			    "Unconvertible UTF-8 character"
			    + " beginning with 0x"
			    + Integer.toHexString (
				buffer [start] & 0xff)
			);

		} catch (ArrayIndexOutOfBoundsException e) {
		    c = 0;
		}

		if (off > finish) {
		    System.arraycopy (buffer, start,
			    buffer, 0, finish - start);
		    finish -= start;
		    start = 0;
		    off = instream.read (buffer, finish,
			    buffer.length - finish);
		    if (off < 0) {
			this.close ();
			throw new CharConversionException (
			    "Partial UTF-8 char");
		    }
		    finish += off;
		    continue;
		}

		for (start++; start < off; start++) {
		    if ((buffer [start] & 0xC0) != 0x80) {
			this.close ();
			throw new CharConversionException (
			    "Malformed UTF-8 char -- "
			    + "is an XML encoding declaration missing?"
			    );
		    }
		}

		buf [offset + i++] = (char) c;
		if (nextChar != 0 && i < len) {
		    buf [offset + i++] = nextChar;
		    nextChar = 0;
		}
	    }
	    if (i > 0)
		return i;
	    return (c == -1) ? -1 : 0;
	}
    }

    static final class AsciiReader extends BaseReader
    {
	AsciiReader (InputStream in) { super (in); }

	public int read (char buf [], int offset, int len) throws IOException
	{
	    int		i, c;

	    if (instream == null)
		return -1;

            if ((offset + len) > buf.length || offset < 0)
                throw new ArrayIndexOutOfBoundsException ();

	    for (i = 0; i < len; i++) {
		if (start >= finish) {
		    start = 0;
		    finish = instream.read (buffer, 0, buffer.length);
		    if (finish <= 0) {
			if (finish <= 0)
			    this.close ();
			break;
		    }
		}
		c = buffer [start++];
		if ((c & 0x80) != 0)
		    throw new CharConversionException (
			"Illegal ASCII character, 0x"
			+ Integer.toHexString (c & 0xff)
		    );
		buf [offset + i] = (char) c;
	    }
	    if (i == 0 && finish <= 0)
		return -1;
	    return i;
	}
    }

    static final class Iso8859_1Reader extends BaseReader
    {
	Iso8859_1Reader (InputStream in) { super (in); }

	public int read (char buf [], int offset, int len) throws IOException
	{
	    int		i;

	    if (instream == null)
		return -1;

            if ((offset + len) > buf.length || offset < 0)
                throw new ArrayIndexOutOfBoundsException ();

	    for (i = 0; i < len; i++) {
		if (start >= finish) {
		    start = 0;
		    finish = instream.read (buffer, 0, buffer.length);
		    if (finish <= 0) {
			if (finish <= 0)
			    this.close ();
			break;
		    }
		}
		buf [offset + i] = (char) (0x0ff & buffer [start++]);
	    }
	    if (i == 0 && finish <= 0)
		return -1;
	    return i;
	}
    }
}
