package org.apache.camel.processor.idempotent;

/**
 * Access to a repository of Message IDs to implement the
 *
 * @version $Revision: 630568 $
 */
public interface MessageIdRepository {

    /**
     * Returns true if this messageId has been processed before
     * otherwise this messageId is added to the repository and false is returned.
     *
     * @param messageId the String ID of the message
     * @return true if the message has been processed succesfully before otherwise false
     */
    boolean contains(String messageId);
}
