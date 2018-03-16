package org.apache.log4j.helpers;

import org.apache.log4j.spi.LoggingEvent;
import org.apache.log4j.spi.LocationInfo;

/**
   Utility class for transforming strings.
 */
public class Transform {

  /**
   * This method takes a string which may contain HTML tags (ie, <b>, <table>,
   * etc) and converts the '<' and '>' characters to their HTML escape
   * sequences.
   *
   * @param input The text to be converted.
   * @return The input string with the characters '<' and '>' replaced with
   *  &lt; and &gt; respectively.
   */
  static 
  public 
  String escapeTags(String input) {

    if( input == null || input.length() == 0 ) {
      return input;
    }


    StringBuffer buf = new StringBuffer(input.length() + 6);
    char ch = ' ';

    int len = input.length();
    for(int i=0; i < len; i++) {
      ch = input.charAt(i);
      if(ch == '<') {
	buf.append("&lt;");
      } else if(ch == '>') {
	buf.append("&gt;");
      } else {
	buf.append(ch);
      }
    }
    return buf.toString();
  }
}
