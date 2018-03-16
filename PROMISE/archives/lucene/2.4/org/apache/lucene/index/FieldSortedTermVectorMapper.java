public class FieldSortedTermVectorMapper extends TermVectorMapper{
  private Map fieldToTerms = new HashMap();
  private SortedSet currentSet;
  private String currentField;
  private Comparator comparator;

  /**
   *
   * @param comparator A Comparator for sorting {@link TermVectorEntry}s
   */
  public FieldSortedTermVectorMapper(Comparator comparator) {
    this(false, false, comparator);
  }


  public FieldSortedTermVectorMapper(boolean ignoringPositions, boolean ignoringOffsets, Comparator comparator) {
    super(ignoringPositions, ignoringOffsets);
    this.comparator = comparator;
  }

  public void map(String term, int frequency, TermVectorOffsetInfo[] offsets, int[] positions) {
    TermVectorEntry entry = new TermVectorEntry(currentField, term, frequency, offsets, positions);
    currentSet.add(entry);
  }

  public void setExpectations(String field, int numTerms, boolean storeOffsets, boolean storePositions) {
    currentSet = new TreeSet(comparator);
    currentField = field;
    fieldToTerms.put(field, currentSet);
  }

  /**
   * Get the mapping between fields and terms, sorted by the comparator
   *
   * @return A map between field names and {@link java.util.SortedSet}s per field.  SortedSet entries are {@link TermVectorEntry}
   */
  public Map getFieldToTerms() {
    return fieldToTerms;
  }


  public Comparator getComparator() {
    return comparator;
  }
}
