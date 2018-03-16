package org.apache.log4j.lf5.viewer.categoryexplorer;

import java.util.LinkedList;
import java.util.StringTokenizer;

/**
 * CategoryPath is a collection of CategoryItems which represent a
 * path of categories.
 *
 * @author Michael J. Sikorsky
 * @author Robert Shaw
 */


public class CategoryPath {

  protected LinkedList _categoryElements = new LinkedList();



  public CategoryPath() {
    super();
  }

  /**
   * Construct a CategoryPath.  If the category is null, it defaults to "Debug".
   */
  public CategoryPath(String category) {
    String processedCategory = category;

    if (processedCategory == null) {
      processedCategory = "Debug";
    }

    processedCategory.replace('/', '.');
    processedCategory = processedCategory.replace('\\', '.');

    StringTokenizer st = new StringTokenizer(processedCategory, ".");
    while (st.hasMoreTokens()) {
      String element = st.nextToken();
      addCategoryElement(new CategoryElement(element));
    }
  }


  /**
   * returns the number of CategoryElements.
   */
  public int size() {
    int count = _categoryElements.size();

    return (count);
  }

  public boolean isEmpty() {
    boolean empty = false;

    if (_categoryElements.size() == 0) {
      empty = true;
    }

    return (empty);
  }


  /**
   * Removes all categoryElements.
   */
  public void removeAllCategoryElements() {
    _categoryElements.clear();
  }

  /**
   * Adds the specified categoryElement to the end of the categoryElement set.
   */
  public void addCategoryElement(CategoryElement categoryElement) {
    _categoryElements.addLast(categoryElement);
  }

  /**
   * Returns the CategoryElement at the specified index.
   */
  public CategoryElement categoryElementAt(int index) {
    return ((CategoryElement) _categoryElements.get(index));
  }


  public String toString() {
    StringBuffer out = new StringBuffer(100);

    out.append("\n");
    out.append("===========================\n");
    out.append("CategoryPath:                   \n");
    out.append("---------------------------\n");

    out.append("\nCategoryPath:\n\t");

    if (this.size() > 0) {
      for (int i = 0; i < this.size(); i++) {
        out.append(this.categoryElementAt(i).toString());
        out.append("\n\t");
      }
    } else {
      out.append("<<NONE>>");
    }

    out.append("\n");
    out.append("===========================\n");

    return (out.toString());
  }




}
