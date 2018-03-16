package org.apache.log4j.lf5.viewer.categoryexplorer;

import javax.swing.*;
import javax.swing.event.TreeModelEvent;
import javax.swing.tree.TreePath;
import java.awt.event.MouseEvent;

/**
 * CategoryExplorerTree
 *
 * @author Michael J. Sikorsky
 * @author Robert Shaw
 * @author Brent Sprecher
 * @author Brad Marlborough
 */


public class CategoryExplorerTree extends JTree {

  protected CategoryExplorerModel _model;
  protected boolean _rootAlreadyExpanded = false;



  /**
   * Construct a CategoryExplorerTree with a specific model.
   */
  public CategoryExplorerTree(CategoryExplorerModel model) {
    super(model);

    _model = model;
    init();
  }

  /**
   * Construct a CategoryExplorerTree and create a default CategoryExplorerModel.
   */
  public CategoryExplorerTree() {
    super();

    CategoryNode rootNode = new CategoryNode("Categories");

    _model = new CategoryExplorerModel(rootNode);

    setModel(_model);

    init();
  }


  public CategoryExplorerModel getExplorerModel() {
    return (_model);
  }

  public String getToolTipText(MouseEvent e) {

    try {
      return super.getToolTipText(e);
    } catch (Exception ex) {
      return "";
    }

  }


  protected void init() {
    putClientProperty("JTree.lineStyle", "Angled");


    CategoryNodeRenderer renderer = new CategoryNodeRenderer();
    setEditable(true);
    setCellRenderer(renderer);

    CategoryNodeEditor editor = new CategoryNodeEditor(_model);

    setCellEditor(new CategoryImmediateEditor(this,
        new CategoryNodeRenderer(),
        editor));
    setShowsRootHandles(true);

    setToolTipText("");

    ensureRootExpansion();

  }

  protected void expandRootNode() {
    if (_rootAlreadyExpanded) {
      return;
    }
    _rootAlreadyExpanded = true;
    TreePath path = new TreePath(_model.getRootCategoryNode().getPath());
    expandPath(path);
  }

  protected void ensureRootExpansion() {
    _model.addTreeModelListener(new TreeModelAdapter() {
      public void treeNodesInserted(TreeModelEvent e) {
        expandRootNode();
      }
    });
  }



}






