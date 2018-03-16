import java.util.ArrayList;

/** Expert: Describes the score computation for document and query. */
public class Explanation implements java.io.Serializable {

  public Explanation() {}

  public Explanation(float value, String description) {
    this.value = value;
    this.description = description;
  }

  /** The value assigned to this explanation node. */
  public float getValue() { return value; }
  /** Sets the value assigned to this explanation node. */
  public void setValue(float value) { this.value = value; }

  /** A description of this explanation node. */
  public String getDescription() { return description; }
  /** Sets the description of this explanation node. */
  public void setDescription(String description) {
    this.description = description;
  }

  /** The sub-nodes of this explanation node. */
  public Explanation[] getDetails() {
    if (details == null)
      return null;
    return (Explanation[])details.toArray(new Explanation[0]);
  }

  /** Adds a sub-node to this explanation node. */
  public void addDetail(Explanation detail) {
    if (details == null)
      details = new ArrayList();
    details.add(detail);
  }

  /** Render an explanation as text. */
  public String toString() {
    return toString(0);
  }
  private String toString(int depth) {
    StringBuffer buffer = new StringBuffer();
    for (int i = 0; i < depth; i++) {
      buffer.append("  ");
    }
    buffer.append(getValue());
    buffer.append(" = ");
    buffer.append(getDescription());
    buffer.append("\n");

    Explanation[] details = getDetails();
    if (details != null) {
      for (int i = 0 ; i < details.length; i++) {
        buffer.append(details[i].toString(depth+1));
      }
    }

    return buffer.toString();
  }


  /** Render an explanation as HTML. */
  public String toHtml() {
    StringBuffer buffer = new StringBuffer();
    buffer.append("<ul>\n");

    buffer.append("<li>");
    buffer.append(getValue());
    buffer.append(" = ");
    buffer.append(getDescription());
    buffer.append("</li>\n");

    Explanation[] details = getDetails();
    if (details != null) {
      for (int i = 0 ; i < details.length; i++) {
        buffer.append(details[i].toHtml());
      }
    }

    buffer.append("</ul>\n");

    return buffer.toString();
  }
}
