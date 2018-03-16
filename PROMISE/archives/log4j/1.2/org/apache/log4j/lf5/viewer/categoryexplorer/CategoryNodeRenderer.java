package org.apache.log4j.lf5.viewer.categoryexplorer;

import javax.swing.*;
import javax.swing.tree.DefaultTreeCellRenderer;
import java.awt.*;
import java.net.URL;

/**
 * CategoryNodeRenderer
 *
 * @author Michael J. Sikorsky
 * @author Robert Shaw
 */


public class CategoryNodeRenderer extends DefaultTreeCellRenderer {

  public static final Color FATAL_CHILDREN = new Color(189, 113, 0);

  protected JCheckBox _checkBox = new JCheckBox();
  protected JPanel _panel = new JPanel();
  protected static ImageIcon _sat = null;


  public CategoryNodeRenderer() {
    _panel.setBackground(UIManager.getColor("Tree.textBackground"));

    if (_sat == null) {
      String resource =
          "/org/apache/log4j/lf5/viewer/images/channelexplorer_satellite.gif";
      URL satURL = getClass().getResource(resource);

      _sat = new ImageIcon(satURL);
    }

    setOpaque(false);
    _checkBox.setOpaque(false);
    _panel.setOpaque(false);

    _panel.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));
    _panel.add(_checkBox);
    _panel.add(this);

    setOpenIcon(_sat);
    setClosedIcon(_sat);
    setLeafIcon(_sat);
  }

  public Component getTreeCellRendererComponent(
      JTree tree, Object value,
      boolean selected, boolean expanded,
      boolean leaf, int row,
      boolean hasFocus) {

    CategoryNode node = (CategoryNode) value;

    super.getTreeCellRendererComponent(
        tree, value, selected, expanded,
        leaf, row, hasFocus);

    if (row == 0) {
      _checkBox.setVisible(false);
    } else {
      _checkBox.setVisible(true);
      _checkBox.setSelected(node.isSelected());
    }
    String toolTip = buildToolTip(node);
    _panel.setToolTipText(toolTip);
    if (node.hasFatalChildren()) {
      this.setForeground(FATAL_CHILDREN);
    }
    if (node.hasFatalRecords()) {
      this.setForeground(Color.red);
    }

    return _panel;
  }

  public Dimension getCheckBoxOffset() {
    return new Dimension(0, 0);
  }


  protected String buildToolTip(CategoryNode node) {
    StringBuffer result = new StringBuffer();
    result.append(node.getTitle()).append(" contains a total of ");
    result.append(node.getTotalNumberOfRecords());
    result.append(" LogRecords.");
    result.append(" Right-click for more info.");
    return result.toString();
  }


}






