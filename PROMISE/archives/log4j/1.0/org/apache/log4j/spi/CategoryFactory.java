package org.apache.log4j.spi;

import org.apache.log4j.Category;

/**
   
  Implement this interface to create new instances of Category or
  a sub-class of Category.

  <p>See {@link org.apache.log4j.examples.MyCategory} for an example.

  @author Ceki G&uuml;lc&uuml;
  @since version 0.8.5
   
 */
public interface CategoryFactory {

 
  public
  Category makeNewCategoryInstance(String name);

}
