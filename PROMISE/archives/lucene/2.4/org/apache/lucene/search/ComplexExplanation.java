public class ComplexExplanation extends Explanation {
  private Boolean match;
  
  public ComplexExplanation() {
    super();
  }

  public ComplexExplanation(boolean match, float value, String description) {
    super(value, description);
    this.match = Boolean.valueOf(match);
  }

  /**
   * The match status of this explanation node.
   * @return May be null if match status is unknown
   */
  public Boolean getMatch() { return match; }
  /**
   * Sets the match status assigned to this explanation node.
   * @param match May be null if match status is unknown
   */
  public void setMatch(Boolean match) { this.match = match; }
  /**
   * Indicates whether or not this Explanation models a good match.
   *
   * <p>
   * If the match status is explicitly set (i.e.: not null) this method
   * uses it; otherwise it defers to the superclass.
   * </p>
   * @see #getMatch
   */
  public boolean isMatch() {
    Boolean m = getMatch();
    return (null != m ? m.booleanValue() : super.isMatch());
  }

  protected String getSummary() {
    if (null == getMatch())
      return super.getSummary();
    
    return getValue() + " = "
      + (isMatch() ? "(MATCH) " : "(NON-MATCH) ")
      + getDescription();
  }
  
}
