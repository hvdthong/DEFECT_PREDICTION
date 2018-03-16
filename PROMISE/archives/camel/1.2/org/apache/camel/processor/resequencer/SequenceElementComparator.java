package org.apache.camel.processor.resequencer;

import java.util.Comparator;

/**
 * A strategy for comparing elements of a sequence.
 * 
 * @author Martin Krasser
 * 
 * @version $Revision
 */
public interface SequenceElementComparator<E> extends Comparator<E> {

    /**
     * Returns <code>true</code> if <code>o1</code> is an immediate predecessor
     * of <code>o2</code>.
     * 
     * @param o1 a sequence element.
     * @param o2 a sequence element.
     */
    boolean predecessor(E o1, E o2);
    
    /**
     * Returns <code>true</code> if <code>o1</code> is an immediate successor
     * of <code>o2</code>.
     * 
     * @param o1 a sequence element.
     * @param o2 a sequence element.
     */
    boolean successor(E o1, E o2);
    
}
