package org.apache.xml.dtm.ref;

import org.apache.xml.dtm.*;

import javax.xml.transform.Source;

import org.apache.xml.utils.XMLStringFactory;

import org.apache.xalan.res.XSLTErrorResources;
import org.apache.xalan.res.XSLMessages;


/**
 * This class implements the traversers for DTMDefaultBase.
 *
 * PLEASE NOTE that the public interface for all traversers should be
 * in terms of DTM Node Handles... but they may use the internal node
 * identity indices within their logic, for efficiency's sake. Be very
 * careful to avoid confusing these when maintaining this code.
 * */
public abstract class DTMDefaultBaseTraversers extends DTMDefaultBase
{

  /**
   * Construct a DTMDefaultBaseTraversers object from a DOM node.
   *
   * @param mgr The DTMManager who owns this DTM.
   * @param domSource the DOM source that this DTM will wrap.
   * @param source The object that is used to specify the construction source.
   * @param dtmIdentity The DTM identity ID for this DTM.
   * @param whiteSpaceFilter The white space filter for this DTM, which may
   *                         be null.
   * @param xstringfactory The factory to use for creating XMLStrings.
   * @param doIndexing true if the caller considers it worth it to use
   *                   indexing schemes.
   */
  public DTMDefaultBaseTraversers(DTMManager mgr, Source source,
                                  int dtmIdentity,
                                  DTMWSFilter whiteSpaceFilter,
                                  XMLStringFactory xstringfactory,
                                  boolean doIndexing)
  {
    super(mgr, source, dtmIdentity, whiteSpaceFilter, xstringfactory,
          doIndexing);
  }

  /**
   * This returns a stateless "traverser", that can navigate
   * over an XPath axis, though perhaps not in document order.
   *
   * @param axis One of Axes.ANCESTORORSELF, etc.
   *
   * @return A DTMAxisTraverser, or null if the given axis isn't supported.
   */
  public DTMAxisTraverser getAxisTraverser(final int axis)
  {

    DTMAxisTraverser traverser;

    {
      m_traversers = new DTMAxisTraverser[Axis.names.length];
      traverser = null;
    }
    else
    {

      if (traverser != null)
        return traverser;
    }

    {
    case Axis.ANCESTOR :
      traverser = new AncestorTraverser();
      break;
    case Axis.ANCESTORORSELF :
      traverser = new AncestorOrSelfTraverser();
      break;
    case Axis.ATTRIBUTE :
      traverser = new AttributeTraverser();
      break;
    case Axis.CHILD :
      traverser = new ChildTraverser();
      break;
    case Axis.DESCENDANT :
      traverser = new DescendantTraverser();
      break;
    case Axis.DESCENDANTORSELF :
      traverser = new DescendantOrSelfTraverser();
      break;
    case Axis.FOLLOWING :
      traverser = new FollowingTraverser();
      break;
    case Axis.FOLLOWINGSIBLING :
      traverser = new FollowingSiblingTraverser();
      break;
    case Axis.NAMESPACE :
      traverser = new NamespaceTraverser();
      break;
    case Axis.NAMESPACEDECLS :
      traverser = new NamespaceDeclsTraverser();
      break;
    case Axis.PARENT :
      traverser = new ParentTraverser();
      break;
    case Axis.PRECEDING :
      traverser = new PrecedingTraverser();
      break;
    case Axis.PRECEDINGSIBLING :
      traverser = new PrecedingSiblingTraverser();
      break;
    case Axis.SELF :
      traverser = new SelfTraverser();
      break;
    case Axis.ALL :
      traverser = new AllFromRootTraverser();
      break;
    case Axis.ALLFROMNODE :
      traverser = new AllFromNodeTraverser();
      break;
    case Axis.PRECEDINGANDANCESTOR :
      traverser = new PrecedingAndAncestorTraverser();
      break;
    case Axis.DESCENDANTSFROMROOT :
      traverser = new DescendantFromRootTraverser();
      break;
    case Axis.DESCENDANTSORSELFFROMROOT :
      traverser = new DescendantOrSelfFromRootTraverser();
      break;
    case Axis.ROOT :
      traverser = new RootTraverser();
      break;
    case Axis.FILTEREDLIST :
    default :
    }

    if (null == traverser)
      throw new DTMException(XSLMessages.createMessage(XSLTErrorResources.ER_AXIS_TRAVERSER_NOT_SUPPORTED, new Object[]{Axis.names[axis]}));

    m_traversers[axis] = traverser;

    return traverser;
  }

  /**
   * Implements traversal of the Ancestor access, in reverse document order.
   */
  private class AncestorTraverser extends DTMAxisTraverser
  {

    /**
     * Traverse to the next node after the current node.
     *
     * @param context The context node if this iteration.
     * @param current The current node of the iteration.
     *
     * @return the next node in the iteration, or DTM.NULL.
     */
    public int next(int context, int current)
    {
			return getParent(current);
    }

    /**
     * Traverse to the next node after the current node that is matched
     * by the expanded type ID.
     *
     * @param context The context node of this iteration.
     * @param current The current node of the iteration.
     * @param expandedTypeID The expanded type ID that must match.
     *
     * @return the next node in the iteration, or DTM.NULL.
     */
    public int next(int context, int current, int expandedTypeID)
    {
      current = makeNodeIdentity(current);

      while (DTM.NULL != (current = m_parent.elementAt(current)))
      {
        if (m_exptype.elementAt(current) == expandedTypeID)
          return makeNodeHandle(current);
      }

      return NULL;
    }
  }

  /**
   * Implements traversal of the Ancestor access, in reverse document order.
   */
  private class AncestorOrSelfTraverser extends AncestorTraverser
  {

    /**
     * By the nature of the stateless traversal, the context node can not be
     * returned or the iteration will go into an infinate loop.  To see if
     * the self node should be processed, use this function.
     *
     * @param context The context node of this traversal.
     *
     * @return the first node in the traversal.
     */
    public int first(int context)
    {
      return context;
    }

    /**
     * By the nature of the stateless traversal, the context node can not be
     * returned or the iteration will go into an infinate loop.  To see if
     * the self node should be processed, use this function.  If the context
     * node does not match the expanded type ID, this function will return
     * false.
     *
     * @param context The context node of this traversal.
     * @param expandedTypeID The expanded type ID that must match.
     *
     * @return the first node in the traversal.
     */
    public int first(int context, int expandedTypeID)
    {
			return (getExpandedTypeID(context) == expandedTypeID)
             ? context : next(context, context, expandedTypeID);
    }
  }

  /**
   * Implements traversal of the Attribute access
   */
  private class AttributeTraverser extends DTMAxisTraverser
  {

    /**
     * Traverse to the next node after the current node.
     *
     * @param context The context node of this iteration.
     * @param current The current node of the iteration.
     *
     * @return the next node in the iteration, or DTM.NULL.
     */
    public int next(int context, int current)
    {
      return (context == current)
             ? getFirstAttribute(context) : getNextAttribute(current);
    }

    /**
     * Traverse to the next node after the current node that is matched
     * by the expanded type ID.
     *
     * @param context The context node of this iteration.
     * @param current The current node of the iteration.
     * @param expandedTypeID The expanded type ID that must match.
     *
     * @return the next node in the iteration, or DTM.NULL.
     */
    public int next(int context, int current, int expandedTypeID)
    {

      current = (context == current)
                ? getFirstAttribute(context) : getNextAttribute(current);

      do
      {
        if (getExpandedTypeID(current) == expandedTypeID)
          return current;
      }
      while (DTM.NULL != (current = getNextAttribute(current)));

      return NULL;
    }
  }

  /**
   * Implements traversal of the Ancestor access, in reverse document order.
   */
  private class ChildTraverser extends DTMAxisTraverser
  {
    
    /**
     * Get the next indexed node that matches the expanded type ID.  Before 
     * calling this function, one should first call 
     * {@link #isIndexed(int) isIndexed} to make sure that the index can 
     * contain nodes that match the given expanded type ID.
     *
     * @param axisRoot The root identity of the axis.
     * @param nextPotential The node found must match or occur after this node.
     * @param expandedTypeID The expanded type ID for the request.
     *
     * @return The node ID or NULL if not found.
     */
    protected int getNextIndexed(int axisRoot, int nextPotential,
                                 int expandedTypeID)
    {

      int nsIndex = m_expandedNameTable.getNamespaceID(expandedTypeID);
      int lnIndex = m_expandedNameTable.getLocalNameID(expandedTypeID);

      for (; ; ) 
      {
        int nextID = findElementFromIndex(nsIndex, lnIndex, nextPotential);

        if (NOTPROCESSED != nextID)
        {
          int parentID = m_parent.elementAt(nextID);
          
          if(parentID == axisRoot)
            return nextID;
          
          if(parentID < axisRoot)
              return NULL;
          
          do
          {
            parentID = m_parent.elementAt(parentID);
            if(parentID < axisRoot)
              return NULL;
          }
            while(parentID > axisRoot);
          
          nextPotential = nextID+1;
          continue;
        }

        nextNode();
        
        if(!(m_nextsib.elementAt(axisRoot) == NOTPROCESSED))
          break;
      }

      return DTM.NULL;
    }
        
    /**
     * By the nature of the stateless traversal, the context node can not be
     * returned or the iteration will go into an infinate loop.  So to traverse 
     * an axis, the first function must be used to get the first node.
     *
     * <p>This method needs to be overloaded only by those axis that process
     * the self node. <\p>
     *
     * @param context The context node of this traversal. This is the point
     * that the traversal starts from.
     * @return the first node in the traversal.
     */
    public int first(int context)
    {
      return getFirstChild(context);
    }
  
    /**
     * By the nature of the stateless traversal, the context node can not be
     * returned or the iteration will go into an infinate loop.  So to traverse 
     * an axis, the first function must be used to get the first node.
     *
     * <p>This method needs to be overloaded only by those axis that process
     * the self node. <\p>
     *
     * @param context The context node of this traversal. This is the point
     * of origin for the traversal -- its "root node" or starting point.
     * @param expandedTypeID The expanded type ID that must match.
     *
     * @return the first node in the traversal.
     */
    public int first(int context, int expandedTypeID)
    {
      if(true)
      {
        int identity = makeNodeIdentity(context);
        
        int firstMatch = getNextIndexed(identity, _firstch(identity),
                                 expandedTypeID);
       
        return makeNodeHandle(firstMatch);
      }
      else
      {
        for (int current = _firstch(makeNodeIdentity(context)); 
             DTM.NULL != current; 
             current = _nextsib(current)) 
        {
          if (m_exptype.elementAt(current) == expandedTypeID)
              return makeNodeHandle(current);
        }
        return NULL;
      }
    }

    /**
     * Traverse to the next node after the current node.
     *
     * @param context The context node of this iteration.
     * @param current The current node of the iteration.
     *
     * @return the next node in the iteration, or DTM.NULL.
     */
    public int next(int context, int current)
    {
      return getNextSibling(current);
    }

    /**
     * Traverse to the next node after the current node that is matched
     * by the expanded type ID.
     *
     * @param context The context node of this iteration.
     * @param current The current node of the iteration.
     * @param expandedTypeID The expanded type ID that must match.
     *
     * @return the next node in the iteration, or DTM.NULL.
     */
    public int next(int context, int current, int expandedTypeID)
    {
      for (current = _nextsib(makeNodeIdentity(current)); 
           DTM.NULL != current; 
           current = _nextsib(current)) 
      {
        if (m_exptype.elementAt(current) == expandedTypeID)
            return makeNodeHandle(current);
      }
      
      return NULL;
    }
  }

  /**
   * Super class for derived classes that want a convenient way to access 
   * the indexing mechanism.
   */
  private abstract class IndexedDTMAxisTraverser extends DTMAxisTraverser
  {

    /**
     * Tell if the indexing is on and the given expanded type ID matches 
     * what is in the indexes.  Derived classes should call this before 
     * calling {@link #getNextIndexed(int, int, int) getNextIndexed} method.
     *
     * @param expandedTypeID The expanded type ID being requested.
     *
     * @return true if it is OK to call the 
     *         {@link #getNextIndexed(int, int, int) getNextIndexed} method.
     */
    protected final boolean isIndexed(int expandedTypeID)
    {
      return (m_indexing
              && ExpandedNameTable.ELEMENT
                 == m_expandedNameTable.getType(expandedTypeID)); 
    }

    /**
     * Tell if a node is outside the axis being traversed.  This method must be 
     * implemented by derived classes, and must be robust enough to handle any 
     * node that occurs after the axis root.
     *
     * @param axisRoot The root identity of the axis.
     * @param identity The node in question.
     *
     * @return true if the given node falls outside the axis being traversed.
     */
    protected abstract boolean isAfterAxis(int axisRoot, int identity);

    /**
     * Tell if the axis has been fully processed to tell if a the wait for 
     * an arriving node should terminate.  This method must be implemented 
     * be a derived class.
     *
     * @param axisRoot The root identity of the axis.
     *
     * @return true if the axis has been fully processed.
     */
    protected abstract boolean axisHasBeenProcessed(int axisRoot);

    /**
     * Get the next indexed node that matches the expanded type ID.  Before 
     * calling this function, one should first call 
     * {@link #isIndexed(int) isIndexed} to make sure that the index can 
     * contain nodes that match the given expanded type ID.
     *
     * @param axisRoot The root identity of the axis.
     * @param nextPotential The node found must match or occur after this node.
     * @param expandedTypeID The expanded type ID for the request.
     *
     * @return The node ID or NULL if not found.
     */
    protected int getNextIndexed(int axisRoot, int nextPotential,
                                 int expandedTypeID)
    {

      int nsIndex = m_expandedNameTable.getNamespaceID(expandedTypeID);
      int lnIndex = m_expandedNameTable.getLocalNameID(expandedTypeID);

      while(true)
      {
        int next = findElementFromIndex(nsIndex, lnIndex, nextPotential);

        if (NOTPROCESSED != next)
        {
          if (isAfterAxis(axisRoot, next))
            return NULL;

          return next;
        }
        else if(axisHasBeenProcessed(axisRoot))
          break;

        nextNode();
      }

      return DTM.NULL;
    }
  }

  /**
   * Implements traversal of the Ancestor access, in reverse document order.
   */
  private class DescendantTraverser extends IndexedDTMAxisTraverser
  {
    /**
     * Get the first potential identity that can be returned.  This should 
     * be overridded by classes that need to return the self node.
     *
     * @param identity The node identity of the root context of the traversal.
     *
     * @return The first potential node that can be in the traversal.
     */
    protected int getFirstPotential(int identity)
    {
      return identity + 1;
    }
    
    /**
     * Tell if the axis has been fully processed to tell if a the wait for 
     * an arriving node should terminate.
     *
     * @param axisRoot The root identity of the axis.
     *
     * @return true if the axis has been fully processed.
     */
    protected boolean axisHasBeenProcessed(int axisRoot)
    {
      return !(m_nextsib.elementAt(axisRoot) == NOTPROCESSED);
    }
    
    /**
     * Get the subtree root identity from the handle that was passed in by 
     * the caller.  Derived classes may override this to change the root 
     * context of the traversal.
     * 
     * @param handle handle to the root context.
     * @return identity of the root of the subtree.
     */
    protected int getSubtreeRoot(int handle)
    {
      return makeNodeIdentity(handle);
    }

    /**
     * Tell if this node identity is a descendant.  Assumes that
     * the node info for the element has already been obtained.
     *
     * %REVIEW% This is really parentFollowsRootInDocumentOrder ...
     * which fails if the parent starts after the root ends.
     * May be sufficient for this class's logic, but misleadingly named!
     *
     * @param subtreeRootIdentity The root context of the subtree in question.
     * @param identity The index number of the node in question.
     * @return true if the index is a descendant of _startNode.
     */
    protected boolean isDescendant(int subtreeRootIdentity, int identity)
    {
      return _parent(identity) >= subtreeRootIdentity;
    }

    /**
     * Tell if a node is outside the axis being traversed.  This method must be 
     * implemented by derived classes, and must be robust enough to handle any 
     * node that occurs after the axis root.
     *
     * @param axisRoot The root identity of the axis.
     * @param identity The node in question.
     *
     * @return true if the given node falls outside the axis being traversed.
     */
    protected boolean isAfterAxis(int axisRoot, int identity)
    {   
      do
      {
        if(identity == axisRoot)
          return false;
        identity = m_parent.elementAt(identity);
      }
        while(identity >= axisRoot);
        
      return true;
    }

    /**
     * By the nature of the stateless traversal, the context node can not be
     * returned or the iteration will go into an infinate loop.  So to traverse
     * an axis, the first function must be used to get the first node.
     *
     * <p>This method needs to be overloaded only by those axis that process
     * the self node. <\p>
     *
     * @param context The context node of this traversal. This is the point
     * of origin for the traversal -- its "root node" or starting point.
     * @param expandedTypeID The expanded type ID that must match.
     *
     * @return the first node in the traversal.
     */
    public int first(int context, int expandedTypeID)
    {

      if (isIndexed(expandedTypeID))
      {
        int identity = getSubtreeRoot(context);
        int firstPotential = getFirstPotential(identity);

        return makeNodeHandle(getNextIndexed(identity, firstPotential, expandedTypeID));
      }

      return next(context, context, expandedTypeID);
    }

    /**
     * Traverse to the next node after the current node.
     *
     * @param context The context node of this iteration.
     * @param current The current node of the iteration.
     *
     * @return the next node in the iteration, or DTM.NULL.
     */
    public int next(int context, int current)
    {

      int subtreeRootIdent = getSubtreeRoot(context);

      for (current = makeNodeIdentity(current) + 1; ; current++)
      {

        if (!isDescendant(subtreeRootIdent, current))
          return NULL;

        if (ATTRIBUTE_NODE == type || NAMESPACE_NODE == type)
          continue;

      }
    }

    /**
     * Traverse to the next node after the current node that is matched
     * by the expanded type ID.
     *
     * @param context The context node of this iteration.
     * @param current The current node of the iteration.
     * @param expandedTypeID The expanded type ID that must match.
     *
     * @return the next node in the iteration, or DTM.NULL.
     */
    public int next(int context, int current, int expandedTypeID)
    {

      int subtreeRootIdent = getSubtreeRoot(context);

      current = makeNodeIdentity(current) + 1;

      if (isIndexed(expandedTypeID))
      {
        return makeNodeHandle(getNextIndexed(subtreeRootIdent, current, expandedTypeID));
      }

      for (; ; current++)
      {

        if (!isDescendant(subtreeRootIdent, current))
          return NULL;

        if (exptype != expandedTypeID)
          continue;

      }
    }
  }

  /**
   * Implements traversal of the Ancestor access, in reverse document order.
   */
  private class DescendantOrSelfTraverser extends DescendantTraverser
  {

    /**
     * Get the first potential identity that can be returned, which is the 
     * axis context, in this case.
     *
     * @param identity The node identity of the root context of the traversal.
     *
     * @return The axis context.
     */
    protected int getFirstPotential(int identity)
    {
      return identity;
    }

    /**
     * By the nature of the stateless traversal, the context node can not be
     * returned or the iteration will go into an infinate loop.  To see if
     * the self node should be processed, use this function.
     *
     * @param context The context node of this traversal.
     *
     * @return the first node in the traversal.
     */
    public int first(int context)
    {
      return context;
    }
  }

  /**
   * Implements traversal of the entire subtree, including the root node.
   */
  private class AllFromNodeTraverser extends DescendantOrSelfTraverser
  {

    /**
     * Traverse to the next node after the current node.
     *
     * @param context The context node of this iteration.
     * @param current The current node of the iteration.
     *
     * @return the next node in the iteration, or DTM.NULL.
     */
    public int next(int context, int current)
    {

      int subtreeRootIdent = makeNodeIdentity(context);

      for (current = makeNodeIdentity(current) + 1; ; current++)
      {

        if (!isDescendant(subtreeRootIdent, current))
          return NULL;

      }
    }
  }

  /**
   * Implements traversal of the following access, in document order.
   */
  private class FollowingTraverser extends DescendantTraverser
  {

    /**
     * Get the first of the following.
     *
     * @param context The context node of this traversal. This is the point
     * that the traversal starts from.
     * @return the first node in the traversal.
     */
    public int first(int context)
    {
			context=makeNodeIdentity(context);

      int first;
      int type = _type(context);

      if ((DTM.ATTRIBUTE_NODE == type) || (DTM.NAMESPACE_NODE == type))
      {
        context = _parent(context);
        first = _firstch(context);

        if (NULL != first)
          return makeNodeHandle(first);
      }

      do
      {
        first = _nextsib(context);

        if (NULL == first)
          context = _parent(context);
      }
      while (NULL == first && NULL != context);

      return makeNodeHandle(first);
    }

    /**
     * Get the first of the following.
     *
     * @param context The context node of this traversal. This is the point
     * of origin for the traversal -- its "root node" or starting point.
     * @param expandedTypeID The expanded type ID that must match.
     *
     * @return the first node in the traversal.
     */
    public int first(int context, int expandedTypeID)
    {
      int first;
      int type = getNodeType(context);

      if ((DTM.ATTRIBUTE_NODE == type) || (DTM.NAMESPACE_NODE == type))
      {
        context = getParent(context);
        first = getFirstChild(context);

        if (NULL != first)
        {
          if (getExpandedTypeID(first) == expandedTypeID)
            return first;
          else
            return next(context, first, expandedTypeID);
        }
      }

      do
      {
        first = getNextSibling(context);

        if (NULL == first)
          context = getParent(context);
        else
        {
          if (getExpandedTypeID(first) == expandedTypeID)
            return first;
          else
            return next(context, first, expandedTypeID);
        }
      }
      while (NULL == first && NULL != context);

      return first;
    }

    /**
     * Traverse to the next node after the current node.
     *
     * @param context The context node of this iteration.
     * @param current The current node of the iteration.
     *
     * @return the next node in the iteration, or DTM.NULL.
     */
    public int next(int context, int current)
    {
			current=makeNodeIdentity(current);

      while (true)
      {


        if (NULL == type)
          return NULL;

        if (ATTRIBUTE_NODE == type || NAMESPACE_NODE == type)
          continue;

      }
    }

    /**
     * Traverse to the next node after the current node that is matched
     * by the expanded type ID.
     *
     * @param context The context node of this iteration.
     * @param current The current node of the iteration.
     * @param expandedTypeID The expanded type ID that must match.
     *
     * @return the next node in the iteration, or DTM.NULL.
     */
    public int next(int context, int current, int expandedTypeID)
    {
			current=makeNodeIdentity(current);

      while (true)
      {
        current++;


        if (NULL == etype)
          return NULL;

        if (etype != expandedTypeID)
          continue;

      }
    }
  }

  /**
   * Implements traversal of the Ancestor access, in reverse document order.
   */
  private class FollowingSiblingTraverser extends DTMAxisTraverser
  {

    /**
     * Traverse to the next node after the current node.
     *
     * @param context The context node of this iteration.
     * @param current The current node of the iteration.
     *
     * @return the next node in the iteration, or DTM.NULL.
     */
    public int next(int context, int current)
    {
      return getNextSibling(current);
    }

    /**
     * Traverse to the next node after the current node that is matched
     * by the expanded type ID.
     *
     * @param context The context node of this iteration.
     * @param current The current node of the iteration.
     * @param expandedTypeID The expanded type ID that must match.
     *
     * @return the next node in the iteration, or DTM.NULL.
     */
    public int next(int context, int current, int expandedTypeID)
    {

      while (DTM.NULL != (current = getNextSibling(current)))
      {
        if (getExpandedTypeID(current) == expandedTypeID)
          return current;
      }

      return NULL;
    }
  }

  /**
   * Implements traversal of the Ancestor access, in reverse document order.
   */
  private class NamespaceDeclsTraverser extends DTMAxisTraverser
  {

    /**
     * Traverse to the next node after the current node.
     *
     * @param context The context node of this iteration.
     * @param current The current node of the iteration.
     *
     * @return the next node in the iteration, or DTM.NULL.
     */
    public int next(int context, int current)
    {

      return (context == current)
             ? getFirstNamespaceNode(context, false)
             : getNextNamespaceNode(context, current, false);
    }

    /**
     * Traverse to the next node after the current node that is matched
     * by the expanded type ID.
     *
     * @param context The context node of this iteration.
     * @param current The current node of the iteration.
     * @param expandedTypeID The expanded type ID that must match.
     *
     * @return the next node in the iteration, or DTM.NULL.
     */
    public int next(int context, int current, int expandedTypeID)
    {

      current = (context == current)
                ? getFirstNamespaceNode(context, false)
                : getNextNamespaceNode(context, current, false);

      do
      {
        if (getExpandedTypeID(current) == expandedTypeID)
          return current;
      }
      while (DTM.NULL
             != (current = getNextNamespaceNode(context, current, false)));

      return NULL;
    }
  }

  /**
   * Implements traversal of the Ancestor access, in reverse document order.
   */
  private class NamespaceTraverser extends DTMAxisTraverser
  {

    /**
     * Traverse to the next node after the current node.
     *
     * @param context The context node of this iteration.
     * @param current The current node of the iteration.
     *
     * @return the next node in the iteration, or DTM.NULL.
     */
    public int next(int context, int current)
    {

      return (context == current)
             ? getFirstNamespaceNode(context, true)
             : getNextNamespaceNode(context, current, true);
    }

    /**
     * Traverse to the next node after the current node that is matched
     * by the expanded type ID.
     *
     * @param context The context node of this iteration.
     * @param current The current node of the iteration.
     * @param expandedTypeID The expanded type ID that must match.
     *
     * @return the next node in the iteration, or DTM.NULL.
     */
    public int next(int context, int current, int expandedTypeID)
    {

      current = (context == current)
                ? getFirstNamespaceNode(context, true)
                : getNextNamespaceNode(context, current, true);

      do
      {
        if (getExpandedTypeID(current) == expandedTypeID)
          return current;
      }
      while (DTM.NULL
             != (current = getNextNamespaceNode(context, current, true)));

      return NULL;
    }
  }

  /**
   * Implements traversal of the Ancestor access, in reverse document order.
   */
  private class ParentTraverser extends DTMAxisTraverser
  {
    /**
     * By the nature of the stateless traversal, the context node can not be
     * returned or the iteration will go into an infinate loop.  So to traverse 
     * an axis, the first function must be used to get the first node.
     *
     * <p>This method needs to be overloaded only by those axis that process
     * the self node. <\p>
     *
     * @param context The context node of this traversal. This is the point
     * that the traversal starts from.
     * @return the first node in the traversal.
     */
    public int first(int context)
    {
      return getParent(context);
    }
  
    /**
     * By the nature of the stateless traversal, the context node can not be
     * returned or the iteration will go into an infinate loop.  So to traverse 
     * an axis, the first function must be used to get the first node.
     *
     * <p>This method needs to be overloaded only by those axis that process
     * the self node. <\p>
     *
     * @param context The context node of this traversal. This is the point
     * of origin for the traversal -- its "root node" or starting point.
     * @param expandedTypeID The expanded type ID that must match.
     *
     * @return the first node in the traversal.
     */
    public int first(int current, int expandedTypeID)
    {
      current = makeNodeIdentity(current);

      while (NULL != (current = m_parent.elementAt(current)))
      {
        if (m_exptype.elementAt(current) == expandedTypeID)
          return makeNodeHandle(current);
      }

      return NULL;
    }


    /**
     * Traverse to the next node after the current node.
     *
     * @param context The context node of this iteration.
     * @param current The current node of the iteration.
     *
     * @return the next node in the iteration, or DTM.NULL.
     */
    public int next(int context, int current)
    {

      return NULL;
    }
    


    /**
     * Traverse to the next node after the current node that is matched
     * by the expanded type ID.
     *
     * @param context The context node of this iteration.
     * @param current The current node of the iteration.
     * @param expandedTypeID The expanded type ID that must match.
     *
     * @return the next node in the iteration, or DTM.NULL.
     */
    public int next(int context, int current, int expandedTypeID)
    {

      return NULL;
    }
  }

  /**
   * Implements traversal of the Ancestor access, in reverse document order.
   */
  private class PrecedingTraverser extends DTMAxisTraverser
  {

    /**
     * Tell if the current identity is an ancestor of the context identity.
     * This is an expensive operation, made worse by the stateless traversal.
     * But the preceding axis is used fairly infrequently.
     *
     * @param contextIdent The context node of the axis traversal.
     * @param currentIdent The node in question.
     * @return true if the currentIdent node is an ancestor of contextIdent.
     */
    protected boolean isAncestor(int contextIdent, int currentIdent)
    {
      for (contextIdent = m_parent.elementAt(contextIdent); DTM.NULL != contextIdent;
              contextIdent = m_parent.elementAt(contextIdent))
      {
        if (contextIdent == currentIdent)
          return true;
      }

      return false;
    }

    /**
     * Traverse to the next node after the current node.
     *
     * @param context The context node of this iteration.
     * @param current The current node of the iteration.
     *
     * @return the next node in the iteration, or DTM.NULL.
     */
    public int next(int context, int current)
    {
      int subtreeRootIdent = makeNodeIdentity(context);

      for (current = makeNodeIdentity(current) - 1; current >= 0; current--)
      {
        short type = _type(current);

        if (ATTRIBUTE_NODE == type || NAMESPACE_NODE == type
                || isAncestor(subtreeRootIdent, current))
          continue;

      }

      return NULL;
    }

    /**
     * Traverse to the next node after the current node that is matched
     * by the expanded type ID.
     *
     * @param context The context node of this iteration.
     * @param current The current node of the iteration.
     * @param expandedTypeID The expanded type ID that must match.
     *
     * @return the next node in the iteration, or DTM.NULL.
     */
    public int next(int context, int current, int expandedTypeID)
    {
      int subtreeRootIdent = makeNodeIdentity(context);

      for (current = makeNodeIdentity(current) - 1; current >= 0; current--)
      {
        int exptype = m_exptype.elementAt(current);

        if (exptype != expandedTypeID
                || isAncestor(subtreeRootIdent, current))
          continue;

      }

      return NULL;
    }
  }

  /**
   * Implements traversal of the Ancestor and the Preceding axis,
   * in reverse document order.
   */
  private class PrecedingAndAncestorTraverser extends DTMAxisTraverser
  {

    /**
     * Traverse to the next node after the current node.
     *
     * @param context The context node of this iteration.
     * @param current The current node of the iteration.
     *
     * @return the next node in the iteration, or DTM.NULL.
     */
    public int next(int context, int current)
    {
      int subtreeRootIdent = makeNodeIdentity(context );

      for (current = makeNodeIdentity(current) - 1; current >= 0; current--)
      {
        short type = _type(current);

        if (ATTRIBUTE_NODE == type || NAMESPACE_NODE == type)
          continue;

      }

      return NULL;
    }

    /**
     * Traverse to the next node after the current node that is matched
     * by the expanded type ID.
     *
     * @param context The context node of this iteration.
     * @param current The current node of the iteration.
     * @param expandedTypeID The expanded type ID that must match.
     *
     * @return the next node in the iteration, or DTM.NULL.
     */
    public int next(int context, int current, int expandedTypeID)
    {
      int subtreeRootIdent = makeNodeIdentity(context);

      for (current = makeNodeIdentity(current) - 1; current >= 0; current--)
      {
        int exptype = m_exptype.elementAt(current);

        if (exptype != expandedTypeID)
          continue;

      }

      return NULL;
    }
  }

  /**
   * Implements traversal of the Ancestor access, in reverse document order.
   */
  private class PrecedingSiblingTraverser extends DTMAxisTraverser
  {

    /**
     * Traverse to the next node after the current node.
     *
     * @param context The context node of this iteration.
     * @param current The current node of the iteration.
     *
     * @return the next node in the iteration, or DTM.NULL.
     */
    public int next(int context, int current)
    {
      return getPreviousSibling(current);
    }

    /**
     * Traverse to the next node after the current node that is matched
     * by the expanded type ID.
     *
     * @param context The context node of this iteration.
     * @param current The current node of the iteration.
     * @param expandedTypeID The expanded type ID that must match.
     *
     * @return the next node in the iteration, or DTM.NULL.
     */
    public int next(int context, int current, int expandedTypeID)
    {

      while (DTM.NULL != (current = getPreviousSibling(current)))
      {
        if (getExpandedTypeID(current) == expandedTypeID)
          return current;
      }

      return NULL;
    }
  }

  /**
   * Implements traversal of the Self axis.
   */
  private class SelfTraverser extends DTMAxisTraverser
  {

    /**
     * By the nature of the stateless traversal, the context node can not be
     * returned or the iteration will go into an infinate loop.  To see if
     * the self node should be processed, use this function.
     *
     * @param context The context node of this traversal.
     *
     * @return the first node in the traversal.
     */
    public int first(int context)
    {
      return context;
    }

    /**
     * By the nature of the stateless traversal, the context node can not be
     * returned or the iteration will go into an infinate loop.  To see if
     * the self node should be processed, use this function.  If the context
     * node does not match the expanded type ID, this function will return
     * false.
     *
     * @param context The context node of this traversal.
     * @param expandedTypeID The expanded type ID that must match.
     *
     * @return the first node in the traversal.
     */
    public int first(int context, int expandedTypeID)
    {
      return (getExpandedTypeID(context) == expandedTypeID) ? context : NULL;
    }

    /**
     * Traverse to the next node after the current node.
     *
     * @param context The context node of this iteration.
     * @param current The current node of the iteration.
     *
     * @return Always return NULL for this axis.
     */
    public int next(int context, int current)
    {
      return NULL;
    }

    /**
     * Traverse to the next node after the current node that is matched
     * by the expanded type ID.
     *
     * @param context The context node of this iteration.
     * @param current The current node of the iteration.
     * @param expandedTypeID The expanded type ID that must match.
     *
     * @return the next node in the iteration, or DTM.NULL.
     */
    public int next(int context, int current, int expandedTypeID)
    {
      return NULL;
    }
  }

  /**
   * Implements traversal of the Ancestor access, in reverse document order.
   */
  private class AllFromRootTraverser extends AllFromNodeTraverser
  {

    /**
     * Return the root.
     *
     * @param context The context node of this traversal.
     *
     * @return the first node in the traversal.
     */
    public int first(int context)
    {
      return getDocumentRoot(context);
    }

    /**
     * Return the root if it matches the expanded type ID.
     *
     * @param context The context node of this traversal.
     * @param expandedTypeID The expanded type ID that must match.
     *
     * @return the first node in the traversal.
     */
    public int first(int context, int expandedTypeID)
    {
      return (getExpandedTypeID(getDocumentRoot(context)) == expandedTypeID)
             ? context : next(context, context, expandedTypeID);
    }

    /**
     * Traverse to the next node after the current node.
     *
     * @param context The context node of this iteration.
     * @param current The current node of the iteration.
     *
     * @return the next node in the iteration, or DTM.NULL.
     */
    public int next(int context, int current)
    {
      int subtreeRootIdent = makeNodeIdentity(context);

      for (current = makeNodeIdentity(current) + 1; ; current++)
      {
        if (type == NULL)
          return NULL;

      }
    }

    /**
     * Traverse to the next node after the current node that is matched
     * by the expanded type ID.
     *
     * @param context The context node of this iteration.
     * @param current The current node of the iteration.
     * @param expandedTypeID The expanded type ID that must match.
     *
     * @return the next node in the iteration, or DTM.NULL.
     */
    public int next(int context, int current, int expandedTypeID)
    {
      int subtreeRootIdent = makeNodeIdentity(context);

      for (current = makeNodeIdentity(current) + 1; ; current++)
      {

        if (exptype == NULL)
          return NULL;

        if (exptype != expandedTypeID)
          continue;

      }
    }
  }

  /**
   * Implements traversal of the Self axis.
   */
  private class RootTraverser extends AllFromRootTraverser
  {

    /**
     * Traverse to the next node after the current node.
     *
     * @param context The context node of this iteration.
     * @param current The current node of the iteration.
     *
     * @return Always return NULL for this axis.
     */
    public int next(int context, int current)
    {
      return NULL;
    }

    /**
     * Traverse to the next node after the current node that is matched
     * by the expanded type ID.
     *
     * @param context The context node of this iteration.
     * @param current The current node of the iteration.
     * @param expandedTypeID The expanded type ID that must match.
     *
     * @return the next node in the iteration, or DTM.NULL.
     */
    public int next(int context, int current, int expandedTypeID)
    {
      return NULL;
    }
  }

  /**
   * A non-xpath axis, returns all nodes that aren't namespaces or attributes,
   * from and including the root.
   */
  private class DescendantOrSelfFromRootTraverser extends DescendantTraverser
  {

    /**
     * Get the first potential identity that can be returned, which is the axis 
     * root context in this case.
     *
     * @param identity The node identity of the root context of the traversal.
     *
     * @return The identity argument.
     */
    protected int getFirstPotential(int identity)
    {
      return identity;
    }

    /**
     * Get the first potential identity that can be returned.
     * @param handle handle to the root context.
     * @return identity of the root of the subtree.
     */
    protected int getSubtreeRoot(int handle)
    {
      return makeNodeIdentity(getDocument());
    }

    /**
     * Return the root.
     *
     * @param context The context node of this traversal.
     *
     * @return the first node in the traversal.
     */
    public int first(int context)
    {
      return getDocumentRoot(context);
    }
    
    /**
     * By the nature of the stateless traversal, the context node can not be
     * returned or the iteration will go into an infinate loop.  So to traverse
     * an axis, the first function must be used to get the first node.
     *
     * <p>This method needs to be overloaded only by those axis that process
     * the self node. <\p>
     *
     * @param context The context node of this traversal. This is the point
     * of origin for the traversal -- its "root node" or starting point.
     * @param expandedTypeID The expanded type ID that must match.
     *
     * @return the first node in the traversal.
     */
    public int first(int context, int expandedTypeID)
    {
      if (isIndexed(expandedTypeID))
      {
        int identity = 0;
        int firstPotential = getFirstPotential(identity);

        return makeNodeHandle(getNextIndexed(identity, firstPotential, expandedTypeID));
      }

      int root = first(context); 
      return next(root, root, expandedTypeID);
    }
  }
  
  /**
   * A non-xpath axis, returns all nodes that aren't namespaces or attributes,
   * from but not including the root.
   */
  private class DescendantFromRootTraverser extends DescendantTraverser
  {

    /**
     * Get the first potential identity that can be returned, which is the axis 
     * root context in this case.
     *
     * @param identity The node identity of the root context of the traversal.
     *
     * @return The identity argument.
     */
    protected int getFirstPotential(int identity)
    {
      return _firstch(0);
    }

    /**
     * Get the first potential identity that can be returned.
     * @param handle handle to the root context.
     * @return identity of the root of the subtree.
     */
    protected int getSubtreeRoot(int handle)
    {
      return 0;
    }

    /**
     * Return the root.
     *
     * @param context The context node of this traversal.
     *
     * @return the first node in the traversal.
     */
    public int first(int context)
    {
      return makeNodeHandle(_firstch(0));
    }
    
    /**
     * By the nature of the stateless traversal, the context node can not be
     * returned or the iteration will go into an infinate loop.  So to traverse
     * an axis, the first function must be used to get the first node.
     *
     * <p>This method needs to be overloaded only by those axis that process
     * the self node. <\p>
     *
     * @param context The context node of this traversal. This is the point
     * of origin for the traversal -- its "root node" or starting point.
     * @param expandedTypeID The expanded type ID that must match.
     *
     * @return the first node in the traversal.
     */
    public int first(int context, int expandedTypeID)
    {
      if (isIndexed(expandedTypeID))
      {
        int identity = 0; 
        int firstPotential = getFirstPotential(identity);

        return makeNodeHandle(getNextIndexed(identity, firstPotential, expandedTypeID));
      }

      int root = getDocumentRoot(context); 
      return next(root, root, expandedTypeID);
    }
    
  }

}
