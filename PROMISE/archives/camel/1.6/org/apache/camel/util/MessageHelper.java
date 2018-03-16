package org.apache.camel.util;

import org.apache.camel.Message;
import org.apache.camel.NoTypeConversionAvailableException;
import org.apache.camel.converter.stream.StreamCache;

/**
 * Some helper methods when working with {@link org.apache.camel.Message}.
 *
 * @version $Revision: 740251 $
 */
public final class MessageHelper {

    /**
     * Utility classes should not have a public constructor.
     */
    private MessageHelper() {
    }

    /**
     * Extracts the given body and returns it as a String, that
     * can be used for logging etc.
     * <p/>
     * Will handle stream based bodies wrapped in StreamCache.
     *
     * @param message  the message with the body
     * @return the body as String, can return <tt>null</null> if no body
     */
    public static String extractBodyAsString(Message message) {
        if (message == null) {
            return null;
        }

        StreamCache newBody = null;
        try {
            newBody = message.getBody(StreamCache.class);
            if (newBody != null) {
                message.setBody(newBody);
            }
        } catch (NoTypeConversionAvailableException ex) {
        }

        Object answer;
        try {
            answer = message.getBody(String.class);
        } catch (NoTypeConversionAvailableException ex) {
            answer = message.getBody();
        }

        if (newBody != null) {
            newBody.reset();
        }

        return answer != null ? answer.toString() : null;
    }

    /**
     * Gets the given body class type name as a String.
     * <p/>
     * Will skip java.lang. for the build in Java types.
     *
     * @param message  the message with the body
     * @return the body typename as String, can return <tt>null</null> if no body
     */
    public static String getBodyTypeName(Message message) {
        if (message == null) {
            return null;
        }
        String answer = ObjectHelper.classCanonicalName(message.getBody());
        if (answer != null && answer.startsWith("java.lang.")) {
            return answer.substring(10);
        }
        return answer;
    }
    
    /**
     * If the message body contains a {@link StreamCache} instance, reset the cache to 
     * enable reading from it again.
     * 
     * @param message the message for which to reset the body
     */
    public static void resetStreamCache(Message message) {
        if (message == null) {
            return;
        }
        if (message.getBody() instanceof StreamCache) {
            ((StreamCache) message.getBody()).reset();
        }
    }
}
