public class SortedTermVectorMapper extends TermVectorMapper{


  private SortedSet currentSet;
  private Map termToTVE = new HashMap();
  private boolean storeOffsets;
  private boolean storePositions;
  /**
   * Stand-in name for the field in {@link TermVectorEntry}.
   */
  public static final String ALL = "_ALL_";

  /**
   *
   * @param comparator A Comparator for sorting {@link TermVectorEntry}s
   */
  public SortedTermVectorMapper(Comparator comparator) {
    this(false, false, comparator);
  }


  public SortedTermVectorMapper(boolean ignoringPositions, boolean ignoringOffsets, Comparator comparator) {
    super(ignoringPositions, ignoringOffsets);
    currentSet = new TreeSet(comparator);
  }

  /**
   *
   * @param term The term to map
   * @param frequency The frequency of the term
   * @param offsets Offset information, may be null
   * @param positions Position information, may be null
   */
  public void map(String term, int frequency, TermVectorOffsetInfo[] offsets, int[] positions) {
    TermVectorEntry entry = (TermVectorEntry) termToTVE.get(term);
    if (entry == null) {
      entry = new TermVectorEntry(ALL, term, frequency, 
              storeOffsets == true ? offsets : null,
              storePositions == true ? positions : null);
      termToTVE.put(term, entry);
      currentSet.add(entry);
    } else {
      entry.setFrequency(entry.getFrequency() + frequency);
      if (storeOffsets)
      {
        TermVectorOffsetInfo [] existingOffsets = entry.getOffsets();
        if (existingOffsets != null && offsets != null && offsets.length > 0)
        {
          TermVectorOffsetInfo [] newOffsets = new TermVectorOffsetInfo[existingOffsets.length + offsets.length];
          System.arraycopy(existingOffsets, 0, newOffsets, 0, existingOffsets.length);
          System.arraycopy(offsets, 0, newOffsets, existingOffsets.length, offsets.length);
          entry.setOffsets(newOffsets);
        }
        else if (existingOffsets == null && offsets != null && offsets.length > 0)
        {
          entry.setOffsets(offsets);
        }
      }
      if (storePositions)
      {
        int [] existingPositions = entry.getPositions();
        if (existingPositions != null && positions != null && positions.length > 0)
        {
          int [] newPositions = new int[existingPositions.length + positions.length];
          System.arraycopy(existingPositions, 0, newPositions, 0, existingPositions.length);
          System.arraycopy(positions, 0, newPositions, existingPositions.length, positions.length);
          entry.setPositions(newPositions);
        }
        else if (existingPositions == null && positions != null && positions.length > 0)
        {
          entry.setPositions(positions);
        }
      }
    }


  }

  public void setExpectations(String field, int numTerms, boolean storeOffsets, boolean storePositions) {

    this.storeOffsets = storeOffsets;
    this.storePositions = storePositions;
  }

  /**
   * The TermVectorEntrySet.  A SortedSet of {@link TermVectorEntry} objects.  Sort is by the comparator passed into the constructor.
   *<br/>
   * This set will be empty until after the mapping process takes place.
   *
   * @return The SortedSet of {@link TermVectorEntry}.
   */
  public SortedSet getTermVectorEntrySet()
  {
    return currentSet;
  }

}
