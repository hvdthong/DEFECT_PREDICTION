package org.apache.camel.processor.resequencer;

/**
  * An interface used by the {@link ResequencerEngine#deliver()} and
  * {@link ResequencerEngine#deliverNext()} methods to send out re-ordered
  * elements.
  *
  * @author Martin Krasser
  *
  * @version $Revision: 698416 $
  */
public interface SequenceSender<E> {

    /**
     * Sends the given element.
     *
     * @param o a re-ordered element.
     * @throws Exception if delivery fails.
     */
    void sendElement(E o) throws Exception;

}
