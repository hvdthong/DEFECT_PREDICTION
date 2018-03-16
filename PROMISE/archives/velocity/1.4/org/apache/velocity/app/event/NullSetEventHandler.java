public interface NullSetEventHandler extends EventHandler
{
    /**
     *  Called when the RHS of a #set() is null, which will result
     *  in a null LHS.
     *
     *  @param lhs  reference literal of left-hand-side of set statement
     *  @param rhs  reference literal of right-hand-side of set statement
     *  @return true if log message should be written, false otherwise
     */
    public boolean shouldLogOnNullSet( String lhs, String rhs );
}
