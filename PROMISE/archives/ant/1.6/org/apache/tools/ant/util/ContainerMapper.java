package org.apache.tools.ant.util;

import java.util.List;
import java.util.Iterator;
import java.util.ArrayList;
import java.util.Collections;
import org.apache.tools.ant.types.Mapper;

/**
 * A <code>FileNameMapper</code> that contains
 * other <CODE>FileNameMapper</CODE>s.
 * @see FileNameMapper
 */
public abstract class ContainerMapper implements FileNameMapper {

    private List mappers = new ArrayList();

    /**
     * Add a <code>Mapper</code>.
     * @param mapper the <code>Mapper</code> to add.
     */
    public void addConfiguredMapper(Mapper mapper) {
        add(mapper.getImplementation());
    }

    /**
     * Add a <code>FileNameMapper</code>.
     * @param fileNameMapper a <CODE>FileNameMapper</CODE>.
     * @throws <CODE>IllegalArgumentException</CODE> if attempting to add this
     *         <CODE>ContainerMapper</CODE> to itself, or if the specified
     *         <CODE>FileNameMapper</CODE> is itself a <CODE>ContainerMapper</CODE>
     *         that contains this <CODE>ContainerMapper</CODE>.
     */
    public synchronized void add(FileNameMapper fileNameMapper) {
        if (this == fileNameMapper
            || (fileNameMapper instanceof ContainerMapper
            && ((ContainerMapper)fileNameMapper).contains(this))) {
            throw new IllegalArgumentException(
                "Circular mapper containment condition detected");
        } else {
            mappers.add(fileNameMapper);
        }
    }

    /**
     * Return <CODE>true</CODE> if this <CODE>ContainerMapper</CODE> or any of
     * its sub-elements contains the specified <CODE>FileNameMapper</CODE>.
     * @param fileNameMapper   the <CODE>FileNameMapper</CODE> to search for.
     * @return <CODE>boolean</CODE>.
     */
    protected synchronized boolean contains(FileNameMapper fileNameMapper) {
        boolean foundit = false;
        for (Iterator iter = mappers.iterator(); iter.hasNext() && !foundit;) {
            FileNameMapper next = (FileNameMapper)(iter.next());
            foundit|= (next == fileNameMapper
                || (next instanceof ContainerMapper
                && ((ContainerMapper)next).contains(fileNameMapper)));
        }
        return foundit;
    }

    /**
     * Get the <CODE>List</CODE> of <CODE>FileNameMapper</CODE>s.
     * @return <CODE>List</CODE>.
     */
    public synchronized List getMappers() {
        return Collections.unmodifiableList(mappers);
    }

    /**
     * Empty implementation.
     */
    public void setFrom(String ignore) {
    }

    /**
     * Empty implementation.
     */
    public void setTo(String ignore) {
    }

}

