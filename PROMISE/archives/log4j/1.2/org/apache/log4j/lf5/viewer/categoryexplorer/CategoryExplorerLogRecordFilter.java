package org.apache.log4j.lf5.viewer.categoryexplorer;

import org.apache.log4j.lf5.LogRecord;
import org.apache.log4j.lf5.LogRecordFilter;

import java.util.Enumeration;

/**
 * An implementation of LogRecordFilter based on a CategoryExplorerModel
 *
 * @author Richard Wan
 */


public class CategoryExplorerLogRecordFilter implements LogRecordFilter {


  protected CategoryExplorerModel _model;



  public CategoryExplorerLogRecordFilter(CategoryExplorerModel model) {
    _model = model;
  }


  /**
   * @return true if the CategoryExplorer model specified at construction
   * is accepting the category of the specified LogRecord.  Has a side-effect
   * of adding the CategoryPath of the LogRecord to the explorer model
   * if the CategoryPath is new.
   */
  public boolean passes(LogRecord record) {
    CategoryPath path = new CategoryPath(record.getCategory());
    return _model.isCategoryPathActive(path);
  }

  /**
   * Resets the counters for the contained CategoryNodes to zero.
   */
  public void reset() {
    resetAllNodes();
  }


  protected void resetAllNodes() {
    Enumeration nodes = _model.getRootCategoryNode().depthFirstEnumeration();
    CategoryNode current;
    while (nodes.hasMoreElements()) {
      current = (CategoryNode) nodes.nextElement();
      current.resetNumberOfContainedRecords();
      _model.nodeChanged(current);
    }
  }

}

