package org.apache.tools.ant.types.resources;

import java.io.File;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Iterator of FileResources from filenames.
 * @since Ant 1.7
 */
public class FileResourceIterator implements Iterator {
    private File basedir;
    private String[] files;
    private int pos = 0;

    /**
     * Construct a new FileResourceIterator.
     */
    public FileResourceIterator() {
    }

    /**
     * Construct a new FileResourceIterator relative to the specified
     * base directory.
     * @param f the base directory of this instance.
     */
    public FileResourceIterator(File f) {
        basedir = f;
    }

    /**
     * Construct a new FileResourceIterator over the specified filenames,
     * relative to the specified base directory.
     * @param f the base directory of this instance.
     * @param s the String[] of filenames.
     */
    public FileResourceIterator(File f, String[] s) {
        this(f);
        addFiles(s);
    }

    /**
     * Add an array of filenames to this FileResourceIterator.
     * @param s the filenames to add.
     */
    public void addFiles(String[] s) {
        int start = (files == null) ? 0 : files.length;
        String[] newfiles = new String[start + s.length];
        if (start > 0) {
            System.arraycopy(files, 0, newfiles, 0, start);
        }
        files = newfiles;
        System.arraycopy(s, 0, files, start, s.length);
    }

    /**
     * Find out whether this FileResourceIterator has more elements.
     * @return whether there are more Resources to iterate over.
     */
    public boolean hasNext() {
        return pos < files.length;
    }

    /**
     * Get the next element from this FileResourceIterator.
     * @return the next Object.
     */
    public Object next() {
        return nextResource();
    }

    /**
     * Not implemented.
     */
    public void remove() {
        throw new UnsupportedOperationException();
    }

    /**
     * Convenience method to return the next resource.
     * @return the next File.
     */
    public FileResource nextResource() {
        if (!hasNext()) {
            throw new NoSuchElementException();
        }
        return new FileResource(basedir, files[pos++]);
    }

}
