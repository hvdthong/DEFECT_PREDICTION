import org.apache.velocity.context.Context;
import org.apache.velocity.util.ContextAware;

/**
 *  Reference 'Stream insertion' event handler.  Called with object
 *  that will be inserted into stream via value.toString().
 *
 *  Please return an Object that will toString() nicely :)
 *
 * @author <a href="mailto:wglass@forio.com">Will Glass-Husain</a>
 * @author <a href="mailto:geirm@optonline.net">Geir Magnusson Jr.</a>
 * @version $Id: ReferenceInsertionEventHandler.java 470256 2006-11-02 07:20:36Z wglass $
 */
public interface  ReferenceInsertionEventHandler extends EventHandler
{
    /**
     * A call-back which is executed during Velocity merge before a reference
     * value is inserted into the output stream. All registered
     * ReferenceInsertionEventHandlers are called in sequence. If no
     * ReferenceInsertionEventHandlers are are registered then reference value
     * is inserted into the output stream as is.
     *
     * @param reference Reference from template about to be inserted.
     * @param value Value about to be inserted (after its <code>toString()</code>
     *            method is called).
     * @return Object on which <code>toString()</code> should be called for
     *         output.
     */
    public Object referenceInsert( String reference, Object value  );

    /**
     * Defines the execution strategy for referenceInsert
     */
    static class referenceInsertExecutor implements EventHandlerMethodExecutor
    {
        private Context context;
        private String reference;
        private Object value;

        referenceInsertExecutor(
                Context context, 
                String reference, 
                Object value)
        {
            this.context = context;
            this.reference = reference;
            this.value = value;
        }

        /**
         * Call the method referenceInsert()
         *  
         * @param handler call the appropriate method on this handler
         */
        public void execute(EventHandler handler)
        {
            ReferenceInsertionEventHandler eh = (ReferenceInsertionEventHandler) handler;
            
            if (eh instanceof ContextAware)
                ((ContextAware) eh).setContext(context);

            /**
             * Every successive call will alter the same value
             */
            value = ((ReferenceInsertionEventHandler) handler).referenceInsert(reference, value); 
        }

        public Object getReturnValue()
        {
            return value;
        }

        /**
         * Continue to end of event handler iteration
         * 
         * @return always returns false
         */
        public boolean isDone()
        {
            return false;
        }        
    }

}
