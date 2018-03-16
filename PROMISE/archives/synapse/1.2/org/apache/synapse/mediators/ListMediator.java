package org.apache.synapse.mediators;

import org.apache.synapse.Mediator;

import java.util.List;

/**
 * The List mediator executes a given sequence/list of child mediators
 */
public interface ListMediator extends Mediator {

    /**
     * Appends the specified mediator to the end of this mediator's (children) list
     * @param m the mediator to be added
     * @return true (as per the general contract of the Collection.add method)
     */
    public boolean addChild(Mediator m);

    /**
     * Appends all of the mediators in the specified collection to the end of this mediator's (children)
     * list, in the order that they are returned by the specified collection's iterator
     * @param c the list of mediators to be added
     * @return true if this list changed as a result of the call
     */
    public boolean addAll(List<Mediator> c);

    /**
     * Returns the mediator at the specified position
     * @param pos index of mediator to return
     * @return the mediator at the specified position in this list
     */
    public Mediator getChild(int pos);

    /**
     * Removes the first occurrence in this list of the specified mediator
     * @param m mediator to be removed from this list, if present
     * @return true if this list contained the specified mediator
     */
    public boolean removeChild(Mediator m);

    /**
     * Removes the mediator at the specified position in this list
     * @param pos the index of the mediator to remove
     * @return the mediator previously at the specified position
     */
    public Mediator removeChild(int pos);

    /**
     * Return the list of mediators of this List mediator instance
     * @return the child/sub mediator list
     */
    public List getList();
}
