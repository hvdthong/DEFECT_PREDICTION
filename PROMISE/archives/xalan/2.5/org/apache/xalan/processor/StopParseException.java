package org.apache.xalan.processor;

/**
 * <meta name="usage" content="internal"/>
 * This is a special exception that is used to stop parsing when 
 * search for an element.  For instance, when searching for xml:stylesheet 
 * PIs, it is used to stop the parse once the document element is found.
 * @see StylesheetPIHandler
 */
public class StopParseException extends org.xml.sax.SAXException
{

  /**
   * Constructor StopParseException.
   */
  StopParseException()
  {
    super("Stylesheet PIs found, stop the parse");
  }
}
