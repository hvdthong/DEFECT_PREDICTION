package org.apache.log4j.lf5.viewer;

import javax.swing.table.DefaultTableModel;

/**
 * LogTableModel
 *
 * @author Michael J. Sikorsky
 * @author Robert Shaw
 */


public class LogTableModel extends DefaultTableModel {




  public LogTableModel(Object[] colNames, int numRows) {
    super(colNames, numRows);
  }


  public boolean isCellEditable(int row, int column) {
    return (false);
  }



}






