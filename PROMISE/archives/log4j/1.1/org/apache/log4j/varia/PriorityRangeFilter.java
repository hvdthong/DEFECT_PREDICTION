package org.apache.log4j.varia;

import org.apache.log4j.Priority;
import org.apache.log4j.spi.Filter;
import org.apache.log4j.spi.LoggingEvent;
import org.apache.log4j.helpers.LogLog;
import org.apache.log4j.helpers.OptionConverter;

/**
   This is a very simple filter based on priority matching, which can be
   used to reject messages with priorities outside a certain range.
   
   <p>The filter admits three options <b>PriorityMin</b>, <b>PriorityMax</b>
   and <b>AcceptOnMatch</b>.

   <p>If the priority of the {@link LoggingEvent} is not between Min and Max
   (inclusive), then {@link Filter#DENY} is returned.
   
   <p> If the Logging event priority is within the specified range, then if
   <b>AcceptOnMatch</b> is true, {@link Filter#ACCEPT} is returned, and if
   <b>AcceptOnMatch</b> is false, {@link Filter#NEUTRAL} is returned.
   
   <p>If <code>PriorityMin</code>w is not defined, then there is no
   minimum acceptable priority (ie a priority is never rejected for
   being too "low"/unimportant).  If <code>PriorityMax</code> is not
   defined, then there is no maximum acceptable priority (ie a
   priority is never rejected for beeing too "high"/important).

   <p>Refer to the {@link
   org.apache.log4j.AppenderSkeleton#setThreshold setThreshold} method
   available to <code>all</code> appenders extending {@link
   org.apache.log4j.AppenderSkeleton} for a more convenient way to
   filter out events by priority.

   @author Simon Kitching
   @author based on code by Ceki G&uuml;lc&uuml; 
*/
public class PriorityRangeFilter extends Filter {

  /**
     Do we return ACCEPT when a match occurs. Default is
     <code>false</code>, so that later filters get run by default  */
  boolean acceptOnMatch = false;

  Priority priorityMin;
  Priority priorityMax;

 
  /**
     Return the decision of this filter.
   */
  public
  int decide(LoggingEvent event) {
    if(this.priorityMin != null) {
      if (event.priority.isGreaterOrEqual(priorityMin) == false) {
        return Filter.DENY;
      }
    }

    if(this.priorityMax != null) {
      if (event.priority.toInt() > priorityMax.toInt()) {
        return Filter.DENY;
      }
    }

    if (acceptOnMatch) {
      return Filter.ACCEPT;
    }
    else {
      return Filter.NEUTRAL;
    }
  }

 /**
     Get the value of the <code>PriorityMax</code> option.  */
  public
  Priority getPriorityMax() {
    return priorityMax;
  }


  /**
     Get the value of the <code>PriorityMin</code> option.  */
  public
  Priority getPriorityMin() {
    return priorityMin;
  }

  /**
     Get the value of the <code>AcceptOnMatch</code> option.
   */
  public
  boolean getAcceptOnMatch() {
    return acceptOnMatch;
  }

  /**
     Set the <code>PriorityMax</code> option.
   */
  public
  void setPriorityMax(Priority priorityMax) {
    this.priorityMax =  priorityMax;
  }

  /**
     Set the <code>PriorityMin</code> option.
   */
  public
  void setPriorityMin(Priority priorityMin) {
    this.priorityMin =  priorityMin;
  }

  /**
     Set the <code>AcceptOnMatch</code> option.
   */  
  public 
  void setAcceptOnMatch(boolean acceptOnMatch) {
    this.acceptOnMatch = acceptOnMatch;
  }


  /**
     @deprecated We now use JavaBeans introspection to configure
     components. 
   */
  public
  String[] getOptionStrings() {
    return new String[] {
      PRIORITY_MIN_OPTION,
      PRIORITY_MAX_OPTION,
      ACCEPT_ON_MATCH_OPTION};
  }

  /**
     @deprecated We now use JavaBeans introspection to configure
     components. 
   */
  public
  void setOption(String key, String value) {
    if(key.equalsIgnoreCase(PRIORITY_MIN_OPTION)) {
      priorityMin = OptionConverter.toPriority(value, null);
    }
    else if (key.equalsIgnoreCase(PRIORITY_MAX_OPTION)) {
      priorityMax = OptionConverter.toPriority(value, null);
    }
    else if (key.equalsIgnoreCase(ACCEPT_ON_MATCH_OPTION)) {
      acceptOnMatch = OptionConverter.toBoolean(value, acceptOnMatch);
    }
  }

  /** See class comments  */
  public static final String PRIORITY_MIN_OPTION = "PriorityMin";

  /** See class comments  */
  public static final String PRIORITY_MAX_OPTION = "PriorityMax";

  /** See class comments  */
  public static final String ACCEPT_ON_MATCH_OPTION = "AcceptOnMatch";
}

