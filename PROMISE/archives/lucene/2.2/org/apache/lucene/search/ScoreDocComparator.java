public interface ScoreDocComparator {

	/** Special comparator for sorting hits according to computed relevance (document score). */
	static final ScoreDocComparator RELEVANCE = new ScoreDocComparator() {
		public int compare (ScoreDoc i, ScoreDoc j) {
			if (i.score > j.score) return -1;
			if (i.score < j.score) return 1;
			return 0;
		}
		public Comparable sortValue (ScoreDoc i) {
			return new Float (i.score);
		}
		public int sortType() {
			return SortField.SCORE;
		}
	};

	/** Special comparator for sorting hits according to index order (document number). */
	static final ScoreDocComparator INDEXORDER = new ScoreDocComparator() {
		public int compare (ScoreDoc i, ScoreDoc j) {
			if (i.doc < j.doc) return -1;
			if (i.doc > j.doc) return 1;
			return 0;
		}
		public Comparable sortValue (ScoreDoc i) {
			return new Integer (i.doc);
		}
		public int sortType() {
			return SortField.DOC;
		}
	};

	/**
	 * Compares two ScoreDoc objects and returns a result indicating their
	 * sort order.
	 * @param i First ScoreDoc
	 * @param j Second ScoreDoc
	 * @return a negative integer if <code>i</code> should come before <code>j</code><br>
     *         a positive integer if <code>i</code> should come after <code>j</code><br>
     *         <code>0</code> if they are equal
	 * @see java.util.Comparator
	 */
	int compare (ScoreDoc i, ScoreDoc j);

	/**
	 * Returns the value used to sort the given document.  The
	 * object returned must implement the java.io.Serializable
	 * interface.  This is used by multisearchers to determine how
     * to collate results from their searchers.
	 * @see FieldDoc
	 * @param i Document
	 * @return Serializable object
	 */
	Comparable sortValue (ScoreDoc i);

	/**
	 * Returns the type of sort.  Should return <code>SortField.SCORE</code>,
     * <code>SortField.DOC</code>, <code>SortField.STRING</code>,
     * <code>SortField.INTEGER</code>, <code>SortField.FLOAT</code> or
     * <code>SortField.CUSTOM</code>.  It is not valid to return
     * <code>SortField.AUTO</code>.
     * This is used by multisearchers to determine how to collate results
     * from their searchers.
	 * @return One of the constants in SortField.
	 * @see SortField
	 */
	int sortType();
}
