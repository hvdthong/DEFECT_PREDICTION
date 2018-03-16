package org.apache.log4j.spi;

import org.apache.log4j.*;

/**
   Listen to events occuring within a {@link
   org.apache.log4j.Hierarchy Hierarchy}.

   @author Ceki G&uuml;lc&uuml;
   @since 1.2
   
 */
public interface HierarchyEventListener {

 


  public
  void addAppenderEvent(Category cat, Appender appender);

  public
  void removeAppenderEvent(Category cat, Appender appender);


}
