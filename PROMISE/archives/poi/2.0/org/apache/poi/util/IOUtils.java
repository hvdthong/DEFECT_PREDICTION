package org.apache.poi.util;

import java.io.IOException;
import java.io.InputStream;

public class IOUtils
{
    private IOUtils()
    {
    }

    /**
     * Helper method, just calls <tt>readFully(in, b, 0, b.length)</tt>
     */
    public static int readFully(InputStream in, byte[] b)
    throws IOException
    {
        return readFully(in, b, 0, b.length);
    }

    /**
     * Same as the normal <tt>in.read(b, off, len)</tt>, but tries to ensure that
     * the entire len number of bytes is read.
     * <p>
     * If the end of file is reached before any bytes are read, returns -1.
     * Otherwise, returns the number of bytes read.
     */
    public static int readFully(InputStream in, byte[] b, int off, int len)
    throws IOException
    {
        int total = 0;
        for (;;) {
            int got = in.read(b, off + total, len - total);
            if (got < 0) {
                return (total == 0) ? -1 : total;
            } else {
                total += got;
                if (total == len)
                    return total;
            }
        }
    }
}

