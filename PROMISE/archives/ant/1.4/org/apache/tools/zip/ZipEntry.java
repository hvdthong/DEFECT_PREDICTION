package org.apache.tools.zip;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Vector;
import java.util.zip.ZipException;

/**
 * Extension that adds better handling of extra fields and provides
 * access to the internal and external file attributes.
 *
 * @author <a href="stefan.bodewig@epost.de">Stefan Bodewig</a>
 * @version $Revision: 268962 $
 */
public class ZipEntry extends java.util.zip.ZipEntry {

    private int internalAttributes = 0;
    private long externalAttributes = 0;
    private Vector extraFields = new Vector();

    /**
     * Creates a new zip entry with the specified name.
     *
     * @since 1.1
     */
    public ZipEntry(String name) {
        super(name);
    }

    /**
     * Creates a new zip entry with fields taken from the specified zip entry.
     *
     * @since 1.1
     */
    public ZipEntry(java.util.zip.ZipEntry entry) throws ZipException {
        /*
         * REVISIT: call super(entry) instead of this stuff in Ant2,
         *          "copy constructor" has not been available in JDK 1.1
         */
        super(entry.getName());

        setComment(entry.getComment());
        setMethod(entry.getMethod());
        setTime(entry.getTime());

        long size = entry.getSize();
        if (size > 0) {
            setSize(size);
        }
        long cSize = entry.getCompressedSize();
        if (cSize > 0) {
            setComprSize(cSize);
        }
        long crc = entry.getCrc();
        if (crc > 0) {
            setCrc(crc);
        }
        
        byte[] extra = entry.getExtra();
        if (extra != null) {
            setExtraFields(ExtraFieldUtils.parse(extra));
        } else {
            setExtra();
        }
    }

    /**
     * Creates a new zip entry with fields taken from the specified zip entry.
     *
     * @since 1.1
     */
    public ZipEntry(ZipEntry entry) throws ZipException {
        this((java.util.zip.ZipEntry) entry);
        setInternalAttributes(entry.getInternalAttributes());
        setExternalAttributes(entry.getExternalAttributes());
        setExtraFields(entry.getExtraFields());
    }

    /**
     * Overwrite clone
     *
     * @since 1.1
     */
    public Object clone() {
        ZipEntry e = null;
        try {
            e = new ZipEntry((java.util.zip.ZipEntry) super.clone());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        e.setInternalAttributes(getInternalAttributes());
        e.setExternalAttributes(getExternalAttributes());
        e.setExtraFields(getExtraFields());
        return e;
    }

    /**
     * Retrieves the internal file attributes.
     *
     * @since 1.1
     */
    public int getInternalAttributes() {
        return internalAttributes;
    }

    /**
     * Sets the internal file attributes.
     *
     * @since 1.1
     */
    public void setInternalAttributes(int value) {
        internalAttributes = value;
    }

    /**
     * Retrieves the external file attributes.
     *
     * @since 1.1
     */
    public long getExternalAttributes() {
        return externalAttributes;
    }

    /**
     * Sets the external file attributes.
     *
     * @since 1.1
     */
    public void setExternalAttributes(long value) {
        externalAttributes = value;
    }

    /**
     * Replaces all currently attached extra fields with the new array.
     *
     * @since 1.1
     */
    public void setExtraFields(ZipExtraField[] fields) {
        extraFields.removeAllElements();
        for (int i=0; i<fields.length; i++) {
            extraFields.addElement(fields[i]);
        }
        setExtra();
    }

    /**
     * Retrieves extra fields.
     *
     * @since 1.1
     */
    public ZipExtraField[] getExtraFields() {
        ZipExtraField[] result = new ZipExtraField[extraFields.size()];
        extraFields.copyInto(result);
        return result;
    }

    /**
     * Adds an extra fields - replacing an already present extra field
     * of the same type.
     *
     * @since 1.1
     */
    public void addExtraField(ZipExtraField ze) {
        ZipShort type = ze.getHeaderId();
        boolean done = false;
        for (int i=0; !done && i<extraFields.size(); i++) {
            if (((ZipExtraField) extraFields.elementAt(i)).getHeaderId().equals(type)) {
                extraFields.setElementAt(ze, i);
                done = true;
            }
        }
        if (!done) {
            extraFields.addElement(ze);
        }
        setExtra();
    }

    /**
     * Remove an extra fields.
     *
     * @since 1.1
     */
    public void removeExtraField(ZipShort type) {
        boolean done = false;
        for (int i=0; !done && i<extraFields.size(); i++) {
            if (((ZipExtraField) extraFields.elementAt(i)).getHeaderId().equals(type)) {
                extraFields.removeElementAt(i);
                done = true;
            }
        }
        if (!done) {
            throw new java.util.NoSuchElementException();
        }
        setExtra();
    }

    /**
     * Throws an Exception if extra data cannot be parsed into extra fields.
     *
     * @since 1.1
     */
    public void setExtra(byte[] extra) throws RuntimeException {
        try {
            setExtraFields(ExtraFieldUtils.parse(extra));
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    /**
     * Unfortunately {@link java.util.zip.ZipOutputStream
     * java.util.zip.ZipOutputStream} seems to access the extra data
     * directly, so overriding getExtra doesn't help - we need to
     * modify super's data directly.
     *
     * @since 1.1
     */
    protected void setExtra() {
        super.setExtra(ExtraFieldUtils.mergeLocalFileDataData(getExtraFields()));
    }

    /**
     * Retrieves the extra data for the local file data.
     *
     * @since 1.1
     */
    public byte[] getLocalFileDataExtra() {
        byte[] extra = getExtra();
        return extra != null ? extra : new byte[0];
    }

    /**
     * Retrieves the extra data for the central directory.
     *
     * @since 1.1
     */
    public byte[] getCentralDirectoryExtra() {
        return ExtraFieldUtils.mergeCentralDirectoryData(getExtraFields());
    }

    /**
     * Helper for JDK 1.1 <-> 1.2 incompatibility.
     *
     * @since 1.2
     */
    private Long compressedSize = null;

    /**
     * Make this class work in JDK 1.1 like a 1.2 class.
     *
     * <p>This either stores the size for later usage or invokes
     * setCompressedSize via reflection.</p>
     *
     * @since 1.2
     */
    public void setComprSize(long size) {
        if (haveSetCompressedSize()) {
            performSetCompressedSize(this, size);
        } else {
            compressedSize = new Long(size);
        }
    }

    /**
     * Override to make this class work in JDK 1.1 like a 1.2 class.
     *
     * @since 1.2
     */
    public long getCompressedSize() {
        if (compressedSize != null) {
            return compressedSize.longValue();
        }
        return super.getCompressedSize();
    }

    /**
     * Helper for JDK 1.1
     *
     * @since 1.2
     */
    private static Method setCompressedSizeMethod = null;
    /**
     * Helper for JDK 1.1
     *
     * @since 1.2
     */
    private static Object lockReflection = new Object();
    /**
     * Helper for JDK 1.1
     *
     * @since 1.2
     */
    private static boolean triedToGetMethod = false;

    /**
     * Are we running JDK 1.2 or higher?
     *
     * @since 1.2
     */
    private static boolean haveSetCompressedSize() {
        checkSCS();
        return setCompressedSizeMethod != null;
    }

    /**
     * Invoke setCompressedSize via reflection.
     *
     * @since 1.2
     */
    private static void performSetCompressedSize(ZipEntry ze, long size) {
        Long[] s = {new Long(size)};
        try {
            setCompressedSizeMethod.invoke(ze, s);
        } catch (InvocationTargetException ite) {
            Throwable nested = ite.getTargetException();
            throw new RuntimeException("Exception setting the compressed size "
                                       + "of " + ze + ": "
                                       + nested.getMessage());
        } catch (Throwable other) {
            throw new RuntimeException("Exception setting the compressed size "
                                       + "of " + ze + ": "
                                       + other.getMessage());
        }
    }

    /**
     * Try to get a handle to the setCompressedSize method.
     *
     * @since 1.2
     */
    private static void checkSCS() {
        if (!triedToGetMethod) {
            synchronized (lockReflection) {
                triedToGetMethod = true;
                try {
                    setCompressedSizeMethod = 
                        java.util.zip.ZipEntry.class.getMethod("setCompressedSize", 
                                                               new Class[] {Long.TYPE});
                } catch (NoSuchMethodException nse) {
                }
            }
        }
    }

}
