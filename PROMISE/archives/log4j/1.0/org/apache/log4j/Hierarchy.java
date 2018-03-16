package org.apache.log4j;


import java.util.Hashtable;
import java.util.Enumeration;

import org.apache.log4j.spi.RootCategory;
import org.apache.log4j.spi.CategoryFactory;
import org.apache.log4j.or.RendererMap;
import org.apache.log4j.or.ObjectRenderer;

/**
   This class is specialized in retreiving categories by name and
   also maintaining the category hierarchy.

   <p><em>The casual user should never have to deal with this class
   firectly.</em> In fact, up until version 0.9.0, this class had
   default package access. 

   <p>The structure of the category hierachy is maintained by the
   {@link #getInstance} method. The hierrachy is such that children
   link to their parent but parents do not have any pointers to their
   children. Moreover, categories can be instantiated in any order, in
   particular decendant before ancestor.

   <p>In case a decendant is created before a particular ancestor,
   then it creates a provision node for the ancestor and adds itself
   to the provision node. Other decendants of the same ancestor add
   themselves to the previously created provision node.

   <p>See the code below for further details.
   
   @author Ceki G&uuml;lc&uuml; 

*/
public class Hierarchy {
  
  static 
  private
  CategoryFactory defaultFactory = new DefaultCategoryFactory();

  Hashtable ht;
  Category root;
  RendererMap rendererMap; 

  /**
     Create a new Category hierarchy.

     @param root The root of the new hierarchy.

   */
  public
  Hierarchy(Category root) {
    ht = new Hashtable();
    this.root = root;
    this.root.myContext  = this;      
    rendererMap = new RendererMap();
  }

  /**
     Add an object renderer for a specific class.       
   */
  public
  void addRenderer(Class classToRender, ObjectRenderer or) {
    rendererMap.put(classToRender, or);
  }
  

  /**
     This call will clear all category definitions from the internal
     hashtable. Invoking this method will irrevocably mess up the
     category hiearchy.
     
     <p>You should <em>really</em> know what you are doing before
     invoking this method.

     @since 0.9.0 */
  public
  void clear() {
    ht.clear();
  }

  /**
     Check if the named category exists in the hirarchy. If so return
     its reference, otherwise returns <code>null</code>.
     
     @param name The name of the category to search for.
     
  */
  public
  Category exists(String name) {    
    Object o = ht.get(new CategoryKey(name));
    if(o instanceof Category) {
      return (Category) o;
    } else {
      return null;
    }
  }


  /**
     Return a new category instance named as the first parameter using
     the default factory. 
     
     <p>If a category of that name already exists, then it will be
     returned.  Otherwise, a new category will be instantiated and
     lthen inked with its existing ancestors as well as children.
     
     @param name The name of the category to retreive.

 */
  public
  Category getInstance(String name) {
    return getInstance(name, defaultFactory);
  }

 /**
     Return a new category instance named as the first parameter using
     <code>factory</code>.
     
     <p>If a category of that name already exists, then it will be
     returned.  Otherwise, a new category will be instantiated by the
     <code>factory</code> parameter and linked with its existing
     ancestors as well as children.
     
     @param name The name of the category to retreive.
     @param factory The factory that will make the new category instance.

 */
  public
  Category getInstance(String name, CategoryFactory factory) {
    CategoryKey key = new CategoryKey(name);    
    Category category;
    
    synchronized(ht) {
      Object o = ht.get(key);
      if(o == null) {
	category = factory.makeNewCategoryInstance(name);
	category.setHierarchy(this);
	ht.put(key, category);      
	updateParents(category);
	return category;
      }
      else if(o instanceof Category) {
	return (Category) o;
      }
      else if (o instanceof ProvisionNode) {
	category = factory.makeNewCategoryInstance(name);
	ht.put(key, category);
	updateChildren((ProvisionNode) o, category);
	updateParents(category);	
	return category;
      }
      else {
      }
    }
  }

  /**
     Get the renderer map for this hierarchy.
  */
  public
  RendererMap getRendererMap() {
    return rendererMap;
  }


  /**
     Get the root of this hierarchy.
     
     @since 0.9.0
   */
  public
  Category getRoot() {
    return root;
  }

  /**
     This method loops through all the *potential* parents of
     'cat'. There 3 possible cases:

     1) No entry for the potential parent of 'cat' exists

        We create a ProvisionNode for this potential parent and insert
        'cat' in that provision node.

     2) There entry is of type Category for the potential parent.

        The entry is 'cat's nearest existing parent. We update cat's
        parent field with this entry. We also break from the loop
        because updating our parent's parent is our parent's
        responsibility.
	 
     3) There entry is of type ProvisionNode for this potential parent.

        We add 'cat' to the list of children for this potential parent.
   */
  final
  private
  void updateParents(Category cat) {
    String name = cat.name;
    int length = name.length();
    boolean parentFound = false;
    
    
    for(int i = name.lastIndexOf('.', length-1); i >= 0; 
	                                 i = name.lastIndexOf('.', i-1))  {
      String substr = name.substring(0, i);

      Object o = ht.get(key);
      if(o == null) {
	ProvisionNode pn = new ProvisionNode(cat);
	ht.put(key, pn);
      }
      else if(o instanceof Category) {
	parentFound = true;
	cat.parent = (Category) o;
	break;	
      }
      else if(o instanceof ProvisionNode) {
	((ProvisionNode) o).addElement(cat);
      }
      else {
	Exception e = new IllegalStateException("unexpected object type " + 
					o.getClass() + " in ht.");
	e.printStackTrace();			   
      }
    }
    if(!parentFound) 
      cat.parent = root;
  }

  /** 
      We update the links for all the children that placed themselves
      in the provision node 'pn'. The second argument 'cat' is a
      reference for the newly created Category, parent of all the
      children in 'pn'

      We loop on all the children 'c' in 'pn':

         If the child 'c' has been already linked to a child of
         'cat' then there is no need to update 'c'.

	 Otherwise, we loop until we find the nearest parent of 'c'
	 (not excluding 'c') below 'cat' and nearest to 'cat'.

	 Say 'x' is this category. We set cat's parent field to x's
	 parent and set x's parent field to cat.

  */
  final
  private
  void updateChildren(ProvisionNode pn, Category cat) {
    final int last = pn.size();

    childLoop:
    for(int i = 0; i < last; i++) {
      Category c = (Category) pn.elementAt(i);


      if(c.parent != null && c.parent.name.startsWith(cat.name)) {
	continue childLoop;
      }

      while(c.parent != null && c.parent.name.startsWith(cat.name)) {
	c = c.parent;
      }
      cat.parent = c.parent;

      c.parent = cat;      
    }
  }    

  /**
     Shutting down a hiearchy will <em>safely</em> close and remove
     all appenders in all the categories including root.
     
     <p>Some appenders such as {@link org.apache.log4j.net.SocketAppender}
     and {@link AsyncAppender} need to be closed before the
     application exists. Otherwise, pending logging events might be
     lost.

     <p>The <code>shutdown</code> method is careful to close nested
     appenders before closing regular appenders. This is allows
     configurations where a regular appender is attached to a category
     and again to a nested appender.
     

     @since 1.0 */
  public 
  void shutdown() {
    Category root = getRoot();    

    root.closeNestedAppenders();

    synchronized(ht) {
      Enumeration cats = Category.getCurrentCategories();
      while(cats.hasMoreElements()) {
	Category c = (Category) cats.nextElement();
	c.closeNestedAppenders();
      }

      root.removeAllAppenders();
      cats = Category.getCurrentCategories();
      while(cats.hasMoreElements()) {
	Category c = (Category) cats.nextElement();
	c.removeAllAppenders();
      }      
    }
  }
}


