public interface XSLTVisitable
{
	/**
	 * This will traverse the heararchy, calling the visitor for 
	 * each member.  If the called visitor method returns 
	 * false, the subtree should not be called.
	 * 
	 * @param visitor The visitor whose appropriate method will be called.
	 */
	public void callVisitors(XSLTVisitor visitor);
}

