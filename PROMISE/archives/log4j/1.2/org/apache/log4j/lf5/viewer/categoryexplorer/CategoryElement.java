package org.apache.log4j.lf5.viewer.categoryexplorer;

/**
 * CategoryElement represents a single element or part of a Category.
 *
 * @author Michael J. Sikorsky
 * @author Robert Shaw
 */


public class CategoryElement {

  protected String _categoryTitle;



  public CategoryElement() {
    super();
  }

  public CategoryElement(String title) {
    _categoryTitle = title;
  }


  public String getTitle() {
    return (_categoryTitle);
  }

  public void setTitle(String title) {
    _categoryTitle = title;
  }




}






