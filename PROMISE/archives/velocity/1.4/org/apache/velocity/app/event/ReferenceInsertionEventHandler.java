public interface  ReferenceInsertionEventHandler extends EventHandler
{
    /**
     * A call-back which is executed during Velocity merge before a
     * reference value is inserted into the output stream.
     *
     * @param reference Reference from template about to be inserted.
     * @param value Value about to be inserted (after its
     * <code>toString()</code> method is called).
     * @return Object on which <code>toString()</code> should be
     * called for output.
     */
    public Object referenceInsert( String reference, Object value  );
}
