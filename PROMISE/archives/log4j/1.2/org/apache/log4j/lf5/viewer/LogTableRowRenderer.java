package org.apache.log4j.lf5.viewer;

import org.apache.log4j.lf5.LogLevel;
import org.apache.log4j.lf5.LogRecord;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;

/**
 * LogTableRowRenderer
 *
 * @author Michael J. Sikorsky
 * @author Robert Shaw
 * @author Brad Marlborough
 */


public class LogTableRowRenderer extends DefaultTableCellRenderer {

  protected boolean _highlightFatal = true;
  protected Color _color = new Color(230, 230, 230);




  public Component getTableCellRendererComponent(JTable table,
      Object value,
      boolean isSelected,
      boolean hasFocus,
      int row,
      int col) {

    if ((row % 2) == 0) {
      setBackground(_color);
    } else {
      setBackground(Color.white);
    }

    FilteredLogTableModel model = (FilteredLogTableModel) table.getModel();
    LogRecord record = model.getFilteredRecord(row);

    setForeground(getLogLevelColor(record.getLevel()));

    return (super.getTableCellRendererComponent(table,
        value,
        isSelected,
        hasFocus,
        row, col));
  }


  protected Color getLogLevelColor(LogLevel level) {
    return (Color) LogLevel.getLogLevelColorMap().get(level);
  }



}






