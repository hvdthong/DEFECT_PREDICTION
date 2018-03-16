package org.apache.xpath;

/**
 * Class to let us know when it's time to do
 * a search from the parent because of indexing.
 * @xsl.usage internal
 */
public class FoundIndex extends RuntimeException
{

  /**
   * Constructor FoundIndex
   *
   */
  public FoundIndex(){}
}
