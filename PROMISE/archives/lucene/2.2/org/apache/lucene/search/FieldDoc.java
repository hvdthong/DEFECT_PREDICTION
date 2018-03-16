public class FieldDoc
extends ScoreDoc {

	/** Expert: The values which are used to sort the referenced document.
	 * The order of these will match the original sort criteria given by a
	 * Sort object.  Each Object will be either an Integer, Float or String,
	 * depending on the type of values in the terms of the original field.
	 * @see Sort
	 * @see Searcher#search(Query,Filter,int,Sort)
	 */
	public Comparable[] fields;

	/** Expert: Creates one of these objects with empty sort information. */
	public FieldDoc (int doc, float score) {
		super (doc, score);
	}

	/** Expert: Creates one of these objects with the given sort information. */
	public FieldDoc (int doc, float score, Comparable[] fields) {
		super (doc, score);
		this.fields = fields;
	}
}
