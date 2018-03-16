package org.apache.tools.zip;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.Hashtable;
import java.util.Vector;
import java.util.zip.CRC32;
import java.util.zip.Deflater;
import java.util.zip.ZipException;

/**
 * Reimplementation of {@link java.util.zip.ZipOutputStream
 * java.util.zip.ZipOutputStream} that does handle the extended
 * functionality of this package, especially internal/external file
 * attributes and extra fields with different layouts for local file
 * data and central directory entries.
 *
 * <p>This class will try to use {@link java.io.RandomAccessFile
 * RandomAccessFile} when you know that the output is going to go to a
 * file.</p>
 *
 * <p>If RandomAccessFile cannot be used, this implementation will use
 * a Data Descriptor to store size and CRC information for {@link
 * #DEFLATED DEFLATED} entries, this means, you don't need to
 * calculate them yourself.  Unfortunately this is not possible for
 * the {@link #STORED STORED} method, here setting the CRC and
 * uncompressed size information is required before {@link
 * #putNextEntry putNextEntry} can be called.</p>
 *
 */
public class ZipOutputStream extends FilterOutputStream {

    private static final int BYTE_MASK = 0xFF;
    private static final int SHORT = 2;
    private static final int WORD = 4;
    private static final int BUFFER_SIZE = 512;

    /**
     * Compression method for deflated entries.
     *
     * @since 1.1
     */
    public static final int DEFLATED = java.util.zip.ZipEntry.DEFLATED;

    /**
     * Default compression level for deflated entries.
     *
     * @since Ant 1.7
     */
    public static final int DEFAULT_COMPRESSION = Deflater.DEFAULT_COMPRESSION;

    /**
     * Compression method for stored entries.
     *
     * @since 1.1
     */
    public static final int STORED = java.util.zip.ZipEntry.STORED;

    /**
     * Current entry.
     *
     * @since 1.1
     */
    private ZipEntry entry;

    /**
     * The file comment.
     *
     * @since 1.1
     */
    private String comment = "";

    /**
     * Compression level for next entry.
     *
     * @since 1.1
     */
    private int level = DEFAULT_COMPRESSION;

    /**
     * Has the compression level changed when compared to the last
     * entry?
     *
     * @since 1.5
     */
    private boolean hasCompressionLevelChanged = false;

    /**
     * Default compression method for next entry.
     *
     * @since 1.1
     */
    private int method = java.util.zip.ZipEntry.DEFLATED;

    /**
     * List of ZipEntries written so far.
     *
     * @since 1.1
     */
    private Vector entries = new Vector();

    /**
     * CRC instance to avoid parsing DEFLATED data twice.
     *
     * @since 1.1
     */
    private CRC32 crc = new CRC32();

    /**
     * Count the bytes written to out.
     *
     * @since 1.1
     */
    private long written = 0;

    /**
     * Data for local header data
     *
     * @since 1.1
     */
    private long dataStart = 0;

    /**
     * Offset for CRC entry in the local file header data for the
     * current entry starts here.
     *
     * @since 1.15
     */
    private long localDataStart = 0;

    /**
     * Start of central directory.
     *
     * @since 1.1
     */
    private long cdOffset = 0;

    /**
     * Length of central directory.
     *
     * @since 1.1
     */
    private long cdLength = 0;

    /**
     * Helper, a 0 as ZipShort.
     *
     * @since 1.1
     */
    private static final byte[] ZERO = {0, 0};

    /**
     * Helper, a 0 as ZipLong.
     *
     * @since 1.1
     */
    private static final byte[] LZERO = {0, 0, 0, 0};

    /**
     * Holds the offsets of the LFH starts for each entry.
     *
     * @since 1.1
     */
    private Hashtable offsets = new Hashtable();

    /**
     * The encoding to use for filenames and the file comment.
     *
     * <p>For a list of possible values see <a
     * Defaults to the platform's default character encoding.</p>
     *
     * @since 1.3
     */
    private String encoding = null;


    /**
     * This Deflater object is used for output.
     *
     * <p>This attribute is only protected to provide a level of API
     * backwards compatibility.  This class used to extend {@link
     * java.util.zip.DeflaterOutputStream DeflaterOutputStream} up to
     * Revision 1.13.</p>
     *
     * @since 1.14
     */
    protected Deflater def = new Deflater(level, true);

    /**
     * This buffer servers as a Deflater.
     *
     * <p>This attribute is only protected to provide a level of API
     * backwards compatibility.  This class used to extend {@link
     * java.util.zip.DeflaterOutputStream DeflaterOutputStream} up to
     * Revision 1.13.</p>
     *
     * @since 1.14
     */
    protected byte[] buf = new byte[BUFFER_SIZE];


    /**
     * Optional random access output.
     *
     * @since 1.14
     */
    private RandomAccessFile raf = null;

    /**
     * Creates a new ZIP OutputStream filtering the underlying stream.
     * @param out the outputstream to zip
     * @since 1.1
     */
    public ZipOutputStream(OutputStream out) {
        super(out);
    }

    /**
     * Creates a new ZIP OutputStream writing to a File.  Will use
     * random access if possible.
     * @param file the file to zip to
     * @since 1.14
     * @throws IOException on error
     */
    public ZipOutputStream(File file) throws IOException {
        super(null);

        try {
            raf = new RandomAccessFile(file, "rw");
            raf.setLength(0);
        } catch (IOException e) {
            if (raf != null) {
                try {
                    raf.close();
                } catch (IOException inner) {
                }
                raf = null;
            }
            out = new FileOutputStream(file);
        }
    }

    /**
     * This method indicates whether this archive is writing to a seekable stream (i.e., to a random
     * access file).
     *
     * <p>For seekable streams, you don't need to calculate the CRC or
     * uncompressed size for {@link #STORED} entries before
     * invoking {@link #putNextEntry}.
     * @return true if seekable
     * @since 1.17
     */
    public boolean isSeekable() {
        return raf != null;
    }

    /**
     * The encoding to use for filenames and the file comment.
     *
     * <p>For a list of possible values see <a
     * Defaults to the platform's default character encoding.</p>
     * @param encoding the encoding value
     * @since 1.3
     */
    public void setEncoding(String encoding) {
        this.encoding = encoding;
    }

    /**
     * The encoding to use for filenames and the file comment.
     *
     * @return null if using the platform's default character encoding.
     *
     * @since 1.3
     */
    public String getEncoding() {
        return encoding;
    }

    /**
     * Finishs writing the contents and closes this as well as the
     * underlying stream.
     *
     * @since 1.1
     * @throws IOException on error
     */
    public void finish() throws IOException {
        closeEntry();
        cdOffset = written;
        for (int i = 0, entriesSize = entries.size(); i < entriesSize; i++) {
            writeCentralFileHeader((ZipEntry) entries.elementAt(i));
        }
        cdLength = written - cdOffset;
        writeCentralDirectoryEnd();
        offsets.clear();
        entries.removeAllElements();
    }

    /**
     * Writes all necessary data for this entry.
     *
     * @since 1.1
     * @throws IOException on error
     */
    public void closeEntry() throws IOException {
        if (entry == null) {
            return;
        }

        long realCrc = crc.getValue();
        crc.reset();

        if (entry.getMethod() == DEFLATED) {
            def.finish();
            while (!def.finished()) {
                deflate();
            }

            entry.setSize(adjustToLong(def.getTotalIn()));
            entry.setCompressedSize(adjustToLong(def.getTotalOut()));
            entry.setCrc(realCrc);

            def.reset();

            written += entry.getCompressedSize();
        } else if (raf == null) {
            if (entry.getCrc() != realCrc) {
                throw new ZipException("bad CRC checksum for entry "
                                       + entry.getName() + ": "
                                       + Long.toHexString(entry.getCrc())
                                       + " instead of "
                                       + Long.toHexString(realCrc));
            }

            if (entry.getSize() != written - dataStart) {
                throw new ZipException("bad size for entry "
                                       + entry.getName() + ": "
                                       + entry.getSize()
                                       + " instead of "
                                       + (written - dataStart));
            }
        } else { /* method is STORED and we used RandomAccessFile */
            long size = written - dataStart;

            entry.setSize(size);
            entry.setCompressedSize(size);
            entry.setCrc(realCrc);
        }

        if (raf != null) {
            long save = raf.getFilePointer();

            raf.seek(localDataStart);
            writeOut(ZipLong.getBytes(entry.getCrc()));
            writeOut(ZipLong.getBytes(entry.getCompressedSize()));
            writeOut(ZipLong.getBytes(entry.getSize()));
            raf.seek(save);
        }

        writeDataDescriptor(entry);
        entry = null;
    }

    /**
     * Begin writing next entry.
     * @param ze the entry to write
     * @since 1.1
     * @throws IOException on error
     */
    public void putNextEntry(ZipEntry ze) throws IOException {
        closeEntry();

        entry = ze;
        entries.addElement(entry);

            entry.setMethod(method);
        }

            entry.setTime(System.currentTimeMillis());
        }

        if (entry.getMethod() == STORED && raf == null) {
            if (entry.getSize() == -1) {
                throw new ZipException("uncompressed size is required for"
                                       + " STORED method when not writing to a"
                                       + " file");
            }
            if (entry.getCrc() == -1) {
                throw new ZipException("crc checksum is required for STORED"
                                       + " method when not writing to a file");
            }
            entry.setCompressedSize(entry.getSize());
        }

        if (entry.getMethod() == DEFLATED && hasCompressionLevelChanged) {
            def.setLevel(level);
            hasCompressionLevelChanged = false;
        }
        writeLocalFileHeader(entry);
    }

    /**
     * Set the file comment.
     * @param comment the comment
     * @since 1.1
     */
    public void setComment(String comment) {
        this.comment = comment;
    }

    /**
     * Sets the compression level for subsequent entries.
     *
     * <p>Default is Deflater.DEFAULT_COMPRESSION.</p>
     * @param level the compression level.
     * @throws IllegalArgumentException if an invalid compression level is specified.
     * @since 1.1
     */
    public void setLevel(int level) {
        if (level < Deflater.DEFAULT_COMPRESSION
            || level > Deflater.BEST_COMPRESSION) {
            throw new IllegalArgumentException(
                "Invalid compression level: " + level);
        }
        hasCompressionLevelChanged = (this.level != level);
        this.level = level;
    }

    /**
     * Sets the default compression method for subsequent entries.
     *
     * <p>Default is DEFLATED.</p>
     * @param method an <code>int</code> from java.util.zip.ZipEntry
     * @since 1.1
     */
    public void setMethod(int method) {
        this.method = method;
    }

    /**
     * Writes bytes to ZIP entry.
     * @param b the byte array to write
     * @param offset the start position to write from
     * @param length the number of bytes to write
     * @throws IOException on error
     */
    public void write(byte[] b, int offset, int length) throws IOException {
        if (entry.getMethod() == DEFLATED) {
            if (length > 0) {
                if (!def.finished()) {
                    def.setInput(b, offset, length);
                    while (!def.needsInput()) {
                        deflate();
                    }
                }
            }
        } else {
            writeOut(b, offset, length);
            written += length;
        }
        crc.update(b, offset, length);
    }

    /**
     * Writes a single byte to ZIP entry.
     *
     * <p>Delegates to the three arg method.</p>
     * @param b the byte to write
     * @since 1.14
     * @throws IOException on error
     */
    public void write(int b) throws IOException {
        byte[] buff = new byte[1];
        buff[0] = (byte) (b & BYTE_MASK);
        write(buff, 0, 1);
    }

    /**
     * Closes this output stream and releases any system resources
     * associated with the stream.
     *
     * @exception  IOException  if an I/O error occurs.
     * @since 1.14
     */
    public void close() throws IOException {
        finish();

        if (raf != null) {
            raf.close();
        }
        if (out != null) {
            out.close();
        }
    }

    /**
     * Flushes this output stream and forces any buffered output bytes
     * to be written out to the stream.
     *
     * @exception  IOException  if an I/O error occurs.
     * @since 1.14
     */
    public void flush() throws IOException {
        if (out != null) {
            out.flush();
        }
    }

    /*
     * Various ZIP constants
     */
    /**
     * local file header signature
     *
     * @since 1.1
     */
    protected static final byte[] LFH_SIG = ZipLong.getBytes(0X04034B50L);
    /**
     * data descriptor signature
     *
     * @since 1.1
     */
    protected static final byte[] DD_SIG = ZipLong.getBytes(0X08074B50L);
    /**
     * central file header signature
     *
     * @since 1.1
     */
    protected static final byte[] CFH_SIG = ZipLong.getBytes(0X02014B50L);
    /**
     * end of central dir signature
     *
     * @since 1.1
     */
    protected static final byte[] EOCD_SIG = ZipLong.getBytes(0X06054B50L);

    /**
     * Writes next block of compressed data to the output stream.
     * @throws IOException on error
     *
     * @since 1.14
     */
    protected final void deflate() throws IOException {
        int len = def.deflate(buf, 0, buf.length);
        if (len > 0) {
            writeOut(buf, 0, len);
        }
    }

    /**
     * Writes the local file header entry
     * @param ze the entry to write
     * @throws IOException on error
     *
     * @since 1.1
     */
    protected void writeLocalFileHeader(ZipEntry ze) throws IOException {
        offsets.put(ze, ZipLong.getBytes(written));

        writeOut(LFH_SIG);
        written += WORD;

        final int zipMethod = ze.getMethod();

        if (zipMethod == DEFLATED && raf == null) {
            writeOut(ZipShort.getBytes(20));

            writeOut(ZipShort.getBytes(8));
        } else {
            writeOut(ZipShort.getBytes(10));
            writeOut(ZERO);
        }
        written += WORD;

        writeOut(ZipShort.getBytes(zipMethod));
        written += SHORT;

        writeOut(toDosTime(ze.getTime()));
        written += WORD;

        localDataStart = written;
        if (zipMethod == DEFLATED || raf != null) {
            writeOut(LZERO);
            writeOut(LZERO);
            writeOut(LZERO);
        } else {
            writeOut(ZipLong.getBytes(ze.getCrc()));
            writeOut(ZipLong.getBytes(ze.getSize()));
            writeOut(ZipLong.getBytes(ze.getSize()));
        }
        written += 12;

        byte[] name = getBytes(ze.getName());
        writeOut(ZipShort.getBytes(name.length));
        written += SHORT;

        byte[] extra = ze.getLocalFileDataExtra();
        writeOut(ZipShort.getBytes(extra.length));
        written += SHORT;

        writeOut(name);
        written += name.length;

        writeOut(extra);
        written += extra.length;

        dataStart = written;
    }

    /**
     * Writes the data descriptor entry.
     * @param ze the entry to write
     * @throws IOException on error
     *
     * @since 1.1
     */
    protected void writeDataDescriptor(ZipEntry ze) throws IOException {
        if (ze.getMethod() != DEFLATED || raf != null) {
            return;
        }
        writeOut(DD_SIG);
        writeOut(ZipLong.getBytes(entry.getCrc()));
        writeOut(ZipLong.getBytes(entry.getCompressedSize()));
        writeOut(ZipLong.getBytes(entry.getSize()));
        written += 16;
    }

    /**
     * Writes the central file header entry.
     * @param ze the entry to write
     * @throws IOException on error
     *
     * @since 1.1
     */
    protected void writeCentralFileHeader(ZipEntry ze) throws IOException {
        writeOut(CFH_SIG);
        written += WORD;

        writeOut(ZipShort.getBytes((ze.getPlatform() << 8) | 20));
        written += SHORT;

        if (ze.getMethod() == DEFLATED && raf == null) {
            writeOut(ZipShort.getBytes(20));

            writeOut(ZipShort.getBytes(8));
        } else {
            writeOut(ZipShort.getBytes(10));
            writeOut(ZERO);
        }
        written += WORD;

        writeOut(ZipShort.getBytes(ze.getMethod()));
        written += SHORT;

        writeOut(toDosTime(ze.getTime()));
        written += WORD;

        writeOut(ZipLong.getBytes(ze.getCrc()));
        writeOut(ZipLong.getBytes(ze.getCompressedSize()));
        writeOut(ZipLong.getBytes(ze.getSize()));
        written += 12;

        byte[] name = getBytes(ze.getName());
        writeOut(ZipShort.getBytes(name.length));
        written += SHORT;

        byte[] extra = ze.getCentralDirectoryExtra();
        writeOut(ZipShort.getBytes(extra.length));
        written += SHORT;

        String comm = ze.getComment();
        if (comm == null) {
            comm = "";
        }
        byte[] commentB = getBytes(comm);
        writeOut(ZipShort.getBytes(commentB.length));
        written += SHORT;

        writeOut(ZERO);
        written += SHORT;

        writeOut(ZipShort.getBytes(ze.getInternalAttributes()));
        written += SHORT;

        writeOut(ZipLong.getBytes(ze.getExternalAttributes()));
        written += WORD;

        writeOut((byte[]) offsets.get(ze));
        written += WORD;

        writeOut(name);
        written += name.length;

        writeOut(extra);
        written += extra.length;

        writeOut(commentB);
        written += commentB.length;
    }

    /**
     * Writes the &quot;End of central dir record&quot;.
     * @throws IOException on error
     *
     * @since 1.1
     */
    protected void writeCentralDirectoryEnd() throws IOException {
        writeOut(EOCD_SIG);

        writeOut(ZERO);
        writeOut(ZERO);

        byte[] num = ZipShort.getBytes(entries.size());
        writeOut(num);
        writeOut(num);

        writeOut(ZipLong.getBytes(cdLength));
        writeOut(ZipLong.getBytes(cdOffset));

        byte[] data = getBytes(comment);
        writeOut(ZipShort.getBytes(data.length));
        writeOut(data);
    }

    /**
     * Smallest date/time ZIP can handle.
     *
     * @since 1.1
     */
    private static final byte[] DOS_TIME_MIN = ZipLong.getBytes(0x00002100L);

    /**
     * Convert a Date object to a DOS date/time field.
     * @param time the <code>Date</code> to convert
     * @return the date as a <code>ZipLong</code>
     * @since 1.1
     */
    protected static ZipLong toDosTime(Date time) {
        return new ZipLong(toDosTime(time.getTime()));
    }

    /**
     * Convert a Date object to a DOS date/time field.
     *
     * <p>Stolen from InfoZip's <code>fileio.c</code></p>
     * @param t number of milliseconds since the epoch
     * @return the date as a byte array
     * @since 1.26
     */
    protected static byte[] toDosTime(long t) {
        Date time = new Date(t);
        int year = time.getYear() + 1900;
        if (year < 1980) {
            return DOS_TIME_MIN;
        }
        int month = time.getMonth() + 1;
        long value =  ((year - 1980) << 25)
            |         (month << 21)
            |         (time.getDate() << 16)
            |         (time.getHours() << 11)
            |         (time.getMinutes() << 5)
            |         (time.getSeconds() >> 1);
        return ZipLong.getBytes(value);
    }

    /**
     * Retrieve the bytes for the given String in the encoding set for
     * this Stream.
     * @param name the string to get bytes from
     * @return the bytes as a byte array
     * @throws ZipException on error
     *
     * @since 1.3
     */
    protected byte[] getBytes(String name) throws ZipException {
        if (encoding == null) {
            return name.getBytes();
        } else {
            try {
                return name.getBytes(encoding);
            } catch (UnsupportedEncodingException uee) {
                throw new ZipException(uee.getMessage());
            }
        }
    }

    /**
     * Write bytes to output or random access file.
     * @param data the byte array to write
     * @throws IOException on error
     *
     * @since 1.14
     */
    protected final void writeOut(byte[] data) throws IOException {
        writeOut(data, 0, data.length);
    }

    /**
     * Write bytes to output or random access file.
     * @param data the byte array to write
     * @param offset the start position to write from
     * @param length the number of bytes to write
     * @throws IOException on error
     *
     * @since 1.14
     */
    protected final void writeOut(byte[] data, int offset, int length)
        throws IOException {
        if (raf != null) {
            raf.write(data, offset, length);
        } else {
            out.write(data, offset, length);
        }
    }

    /**
     * Assumes a negative integer really is a positive integer that
     * has wrapped around and re-creates the original value.
     * @param i the value to treat as unsigned int.
     * @return the unsigned int as a long.
     * @since 1.34
     */
    protected static long adjustToLong(int i) {
        if (i < 0) {
            return 2 * ((long) Integer.MAX_VALUE) + 2 + i;
        } else {
            return i;
        }
    }

}
