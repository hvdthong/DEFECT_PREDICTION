package org.apache.camel.processor.resequencer;

/**
 * A strategy for comparing {@link Element} instances. This strategy uses
 * another {@link SequenceElementComparator} instance for comparing elements
 * contained by {@link Element} instances.
 * 
 * @author Martin Krasser
 * 
 * @version $Revision
 */
class ElementComparator<E> implements SequenceElementComparator<Element<E>> {

    /**
     * A sequence element comparator this comparator delegates to.
     */
    private SequenceElementComparator<E> comparator;
    
    /**
     * Creates a new element comparator instance.
     * 
     * @param comparator a sequence element comparator this comparator delegates
     *        to.
     */
    public ElementComparator(SequenceElementComparator<E> comparator) {
        this.comparator = comparator;
    }
    
    /**
     * @see SequenceElementComparator#predecessor(java.lang.Object, java.lang.Object)
     */
    public boolean predecessor(Element<E> o1, Element<E> o2) {
        return comparator.predecessor(o1.getObject(), o2.getObject());
    }

    /**
     * @see SequenceElementComparator#successor(java.lang.Object, java.lang.Object)
     */
    public boolean successor(Element<E> o1, Element<E> o2) {
        return comparator.successor(o1.getObject(), o2.getObject());
    }

    /**
     * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
     */
    public int compare(Element<E> o1, Element<E> o2) {
        return comparator.compare(o1.getObject(), o2.getObject());
    }

}
