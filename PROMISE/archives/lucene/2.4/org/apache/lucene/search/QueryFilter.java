public class QueryFilter extends CachingWrapperFilter {

  /** Constructs a filter which only matches documents matching
   * <code>query</code>.
   */
  public QueryFilter(Query query) {
    super(new QueryWrapperFilter(query));
  }

  public boolean equals(Object o) {
    return super.equals((QueryFilter)o);
  }

  public int hashCode() {
    return super.hashCode() ^ 0x923F64B9;  
  }
}
