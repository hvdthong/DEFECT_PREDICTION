package org.apache.xpath;

/**
 * <meta name="usage" content="internal"/>
 * Class to let us know when it's time to do
 * a search from the parent because of indexing.
 */
public class FoundIndex extends RuntimeException
{

  /**
   * Constructor FoundIndex
   *
   */
  public FoundIndex(){}
}
