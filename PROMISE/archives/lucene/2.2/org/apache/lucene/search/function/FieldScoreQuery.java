public class FieldScoreQuery extends ValueSourceQuery {

  /**
   * Type of score field, indicating how field values are interpreted/parsed.  
   * <p>
   * The type selected at search search time should match the data stored in the field. 
   * Different types have different RAM requirements: 
   * <ul>
   *   <li>{@link #BYTE} consumes 1 * maxDocs bytes.</li>
   *   <li>{@link #SHORT} consumes 2 * maxDocs bytes.</li>
   *   <li>{@link #INT} consumes 4 * maxDocs bytes.</li>
   *   <li>{@link #FLOAT} consumes 8 * maxDocs bytes.</li>
   * </ul>
   */
  public static class Type {
    
    /** field values are interpreted as numeric byte values. */
    public static final Type BYTE = new Type("byte"); 

    /** field values are interpreted as numeric short values. */
    public static final Type SHORT = new Type("short"); 

    /** field values are interpreted as numeric int values. */
    public static final Type INT = new Type("int"); 

    /** field values are interpreted as numeric float values. */
    public static final Type FLOAT = new Type("float"); 

    private String typeName;
    private Type (String name) {
      this.typeName = name;
    }
    /*(non-Javadoc) @see java.lang.Object#toString() */
    public String toString() {
      return getClass().getName()+"::"+typeName;
    }
  }
  
  /**
   * Create a FieldScoreQuery - a query that scores each document as the value of the numeric input field.
   * <p>
   * The <code>type</code> param tells how to parse the field string values into a numeric score value.
   * @param field the numeric field to be used.
   * @param type the type of the field: either
   * {@link Type#BYTE}, {@link Type#SHORT}, {@link Type#INT}, or {@link Type#FLOAT}. 
   */
  public FieldScoreQuery(String field, Type type) {
    super(getValueSource(field,type));
  }

  private static ValueSource getValueSource(String field, Type type) {
    if (type == Type.BYTE) {
      return new ByteFieldSource(field);
    }
    if (type == Type.SHORT) {
      return new ShortFieldSource(field);
    }
    if (type == Type.INT) {
      return new IntFieldSource(field);
    }
    if (type == Type.FLOAT) {
      return new FloatFieldSource(field);
    }
    throw new IllegalArgumentException(type+" is not a known Field Score Query Type!");
  }

}
