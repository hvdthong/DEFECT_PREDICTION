package org.apache.log4j.helpers;

import java.util.Hashtable;

/**
   <code>ThreadLocalMap</code> extends {@link InheritableThreadLocal}
   to bequeath a copy of the hashtable of the MDC of the parent
   thread.

   @author Ceki G&uuml;lc&uuml;
   @since 1.2
*/
final public class ThreadLocalMap extends InheritableThreadLocal {

  public
  final
  Object childValue(Object parentValue) {
    Hashtable ht = (Hashtable) parentValue;
    if(ht != null) {
      return ht.clone();
    } else {
      return null;
    }
  }
}
