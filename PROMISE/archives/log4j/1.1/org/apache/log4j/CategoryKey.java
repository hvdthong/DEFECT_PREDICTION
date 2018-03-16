package org.apache.log4j;

/**
   CategoryKey is heavily used internally to accelerate hash table searches.
   @author Ceki G&uuml;lc&uuml; 
*/
class CategoryKey {

  String   name;  
  int hashCache;

  CategoryKey(String name) {
    this.name = name.intern();
    hashCache = name.hashCode();
  }

  final
  public  
  int hashCode() {
    return hashCache;
  }

  final
  public
  boolean equals(Object rArg) {
    if(this == rArg)
      return true;
    
    if(rArg != null && CategoryKey.class == rArg.getClass()) 
      return  name == ((CategoryKey)rArg ).name;
    else 
      return false;
  }
}
