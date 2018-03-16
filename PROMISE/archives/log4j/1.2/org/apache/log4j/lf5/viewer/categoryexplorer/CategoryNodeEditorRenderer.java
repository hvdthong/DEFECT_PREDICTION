package org.apache.log4j.lf5.viewer.categoryexplorer;

import javax.swing.*;
import java.awt.*;

/**
 * CategoryNodeEditorRenderer
 *
 * @author Michael J. Sikorsky
 * @author Robert Shaw
 */


public class CategoryNodeEditorRenderer extends CategoryNodeRenderer {





  public Component getTreeCellRendererComponent(
      JTree tree, Object value,
      boolean selected, boolean expanded,
      boolean leaf, int row,
      boolean hasFocus) {
    Component c = super.getTreeCellRendererComponent(tree,
        value, selected, expanded,
        leaf, row, hasFocus);

    return c;
  }

  public JCheckBox getCheckBox() {
    return _checkBox;
  }




}
