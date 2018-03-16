package org.apache.log4j.lf5.viewer.categoryexplorer;

import javax.swing.*;
import javax.swing.tree.DefaultTreeCellEditor;
import javax.swing.tree.TreePath;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.EventObject;

/**
 * CategoryImmediateEditor
 *
 * @author Michael J. Sikorsky
 * @author Robert Shaw
 */


public class CategoryImmediateEditor extends DefaultTreeCellEditor {

  private CategoryNodeRenderer renderer;
  protected Icon editingIcon = null;


  public CategoryImmediateEditor(JTree tree,
      CategoryNodeRenderer renderer,
      CategoryNodeEditor editor) {
    super(tree, renderer, editor);
    this.renderer = renderer;
    renderer.setIcon(null);
    renderer.setLeafIcon(null);
    renderer.setOpenIcon(null);
    renderer.setClosedIcon(null);

    super.editingIcon = null;
  }

  public boolean shouldSelectCell(EventObject e) {

    if (e instanceof MouseEvent) {
      MouseEvent me = (MouseEvent) e;
      TreePath path = tree.getPathForLocation(me.getX(),
          me.getY());
      CategoryNode node = (CategoryNode)
          path.getLastPathComponent();

      rv = node.isLeaf() /*|| !inCheckBoxHitRegion(me)*/;
    }
    return rv;
  }

  public boolean inCheckBoxHitRegion(MouseEvent e) {
    TreePath path = tree.getPathForLocation(e.getX(),
        e.getY());
    if (path == null) {
      return false;
    }
    CategoryNode node = (CategoryNode) path.getLastPathComponent();
    boolean rv = false;

    if (true) {

      Rectangle bounds = tree.getRowBounds(lastRow);
      Dimension checkBoxOffset =
          renderer.getCheckBoxOffset();

      bounds.translate(offset + checkBoxOffset.width,
          checkBoxOffset.height);

      rv = bounds.contains(e.getPoint());
    }
    return true;
  }


  protected boolean canEditImmediately(EventObject e) {
    boolean rv = false;

    if (e instanceof MouseEvent) {
      MouseEvent me = (MouseEvent) e;
      rv = inCheckBoxHitRegion(me);
    }

    return rv;
  }

  protected void determineOffset(JTree tree, Object value,
      boolean isSelected, boolean expanded,
      boolean leaf, int row) {
    offset = 0;
  }



}






