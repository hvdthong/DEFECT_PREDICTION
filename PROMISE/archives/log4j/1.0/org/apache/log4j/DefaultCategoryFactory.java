package org.apache.log4j;

import org.apache.log4j.spi.CategoryFactory;

class DefaultCategoryFactory implements CategoryFactory {
    
  DefaultCategoryFactory() {
  }    
    
  public
  Category makeNewCategoryInstance(String name) {
    return new Category(name);
  }    
}
