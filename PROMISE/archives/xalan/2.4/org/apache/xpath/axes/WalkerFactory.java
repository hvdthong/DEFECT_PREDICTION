package org.apache.xpath.axes;

import org.apache.xpath.compiler.OpCodes;
import org.apache.xpath.compiler.Compiler;
import org.apache.xpath.compiler.FunctionTable;
import org.apache.xpath.patterns.NodeTest;
import org.apache.xpath.patterns.StepPattern;
import org.apache.xpath.patterns.ContextMatchStepPattern;
import org.apache.xpath.patterns.FunctionPattern;
import org.apache.xpath.Expression;
import org.apache.xpath.objects.XNumber;
import org.apache.xalan.res.XSLMessages;
import org.apache.xpath.res.XPATHErrorResources;

import org.apache.xml.dtm.DTMFilter;
import org.apache.xml.dtm.DTMIterator;
import org.apache.xml.dtm.Axis;

/**
 * This class is both a factory for XPath location path expressions,
 * which are built from the opcode map output, and an analysis engine
 * for the location path expressions in order to provide optimization hints.
 */
public class WalkerFactory
{

  /**
   * <meta name="usage" content="advanced"/>
   * This method is for building an array of possible levels
   * where the target element(s) could be found for a match.
   * @param xpath The xpath that is executing.
   * @param context The current source tree context node.
   *
   * @param lpi The owning location path iterator.
   * @param compiler non-null reference to compiler object that has processed
   *                 the XPath operations into an opcode map.
   * @param stepOpCodePos The opcode position for the step.
   *
   * @return non-null AxesWalker derivative.
   *
   * @throws javax.xml.transform.TransformerException
   */
  static AxesWalker loadOneWalker(
          WalkingIterator lpi, Compiler compiler, int stepOpCodePos)
            throws javax.xml.transform.TransformerException
  {

    AxesWalker firstWalker = null;
    int stepType = compiler.getOpMap()[stepOpCodePos];

    if (stepType != OpCodes.ENDOP)
    {

      firstWalker = createDefaultWalker(compiler, stepType, lpi, 0);

      firstWalker.init(compiler, stepOpCodePos, stepType);
    }

    return firstWalker;
  }

  /**
   * <meta name="usage" content="advanced"/>
   * This method is for building an array of possible levels
   * where the target element(s) could be found for a match.
   * @param xpath The xpath that is executing.
   * @param context The current source tree context node.
   *
   * @param lpi The owning location path iterator object.
   * @param compiler non-null reference to compiler object that has processed
   *                 the XPath operations into an opcode map.
   * @param stepOpCodePos The opcode position for the step.
   * @param stepIndex The top-level step index withing the iterator.
   *
   * @return non-null AxesWalker derivative.
   *
   * @throws javax.xml.transform.TransformerException
   */
  static AxesWalker loadWalkers(
          WalkingIterator lpi, Compiler compiler, int stepOpCodePos, int stepIndex)
            throws javax.xml.transform.TransformerException
  {

    int stepType;
    AxesWalker firstWalker = null;
    AxesWalker walker, prevWalker = null;
    int ops[] = compiler.getOpMap();
    int analysis = analyze(compiler, stepOpCodePos, stepIndex);

    while (OpCodes.ENDOP != (stepType = ops[stepOpCodePos]))
    {
      walker = createDefaultWalker(compiler, stepOpCodePos, lpi, analysis);

      walker.init(compiler, stepOpCodePos, stepType);
      walker.exprSetParent(lpi);

      if (null == firstWalker)
      {
        firstWalker = walker;
      }
      else
      {
        prevWalker.setNextWalker(walker);
        walker.setPrevWalker(prevWalker);
      }

      prevWalker = walker;
      stepOpCodePos = compiler.getNextStepPos(stepOpCodePos);

      if (stepOpCodePos < 0)
        break;
    }

    return firstWalker;
  }
  
  public static boolean isSet(int analysis, int bits)
  {
    return (0 != (analysis & bits));
  }
  
  public static void diagnoseIterator(String name, int analysis, Compiler compiler)
  {
    System.out.println(compiler.toString()+", "+name+", "
                             + Integer.toBinaryString(analysis) + ", "
                             + getAnalysisString(analysis));
  }

  /**
   * Create a new LocPathIterator iterator.  The exact type of iterator
   * returned is based on an analysis of the XPath operations.
   *
   * @param compiler non-null reference to compiler object that has processed
   *                 the XPath operations into an opcode map.
   * @param opPos The position of the operation code for this itterator.
   *
   * @return non-null reference to a LocPathIterator or derivative.
   *
   * @throws javax.xml.transform.TransformerException
   */
  public static DTMIterator newDTMIterator(
          Compiler compiler, int opPos,
          boolean isTopLevel)
            throws javax.xml.transform.TransformerException
  {

    int firstStepPos = compiler.getFirstChildPos(opPos);
    int analysis = analyze(compiler, firstStepPos, 0);
    boolean isOneStep = isOneStep(analysis);
    DTMIterator iter;

    if (isOneStep && walksSelfOnly(analysis) && 
        isWild(analysis) && !hasPredicate(analysis))
    {
      if (DEBUG_ITERATOR_CREATION)
        diagnoseIterator("SelfIteratorNoPredicate", analysis, compiler);

      iter = new SelfIteratorNoPredicate(compiler, opPos, analysis);
    }
    else if (walksChildrenOnly(analysis) && isOneStep)
    {

      if (isWild(analysis) && !hasPredicate(analysis))
      {
        if (DEBUG_ITERATOR_CREATION)
          diagnoseIterator("ChildIterator", analysis, compiler);

        iter = new ChildIterator(compiler, opPos, analysis);
      }
      else
      {
        if (DEBUG_ITERATOR_CREATION)
          diagnoseIterator("ChildTestIterator", analysis, compiler);

        iter = new ChildTestIterator(compiler, opPos, analysis);
      }
    }
    else if (isOneStep && walksAttributes(analysis))
    {
      if (DEBUG_ITERATOR_CREATION)
        diagnoseIterator("AttributeIterator", analysis, compiler);

      iter = new AttributeIterator(compiler, opPos, analysis);
    }
    else if(isOneStep && !walksFilteredList(analysis))
    {
      if( !walksNamespaces(analysis) 
      && (walksInDocOrder(analysis) || isSet(analysis, BIT_PARENT)))
      {
        if (false || DEBUG_ITERATOR_CREATION)
          diagnoseIterator("OneStepIteratorForward", analysis, compiler);
  
        iter = new OneStepIteratorForward(compiler, opPos, analysis);
      }
      else
      {
        if (false || DEBUG_ITERATOR_CREATION)
          diagnoseIterator("OneStepIterator", analysis, compiler);
  
        iter = new OneStepIterator(compiler, opPos, analysis);
      }
    }

    else if (isOptimizableForDescendantIterator(compiler, firstStepPos, 0)
             )
    {
      if (DEBUG_ITERATOR_CREATION)
        diagnoseIterator("DescendantIterator", analysis, compiler);

      iter = new DescendantIterator(compiler, opPos, analysis);
    }
    else
    { 
      if(isNaturalDocOrder(compiler, firstStepPos, 0, analysis))
      {
        if (false || DEBUG_ITERATOR_CREATION)
        {
          diagnoseIterator("WalkingIterator", analysis, compiler);
        }
  
        iter = new WalkingIterator(compiler, opPos, analysis, true);
      }
      else
      {
        if (DEBUG_ITERATOR_CREATION)
          diagnoseIterator("WalkingIteratorSorted", analysis, compiler);

        iter = new WalkingIteratorSorted(compiler, opPos, analysis, true);
      }
    }
    if(iter instanceof LocPathIterator)
      ((LocPathIterator)iter).setIsTopLevel(isTopLevel);
      
    return iter;
  }
  
  /**
   * Special purpose function to see if we can optimize the pattern for 
   * a DescendantIterator.
   *
   * @param compiler non-null reference to compiler object that has processed
   *                 the XPath operations into an opcode map.
   * @param stepOpCodePos The opcode position for the step.
   * @param stepIndex The top-level step index withing the iterator.
   *
   * @return 32 bits as an integer that give information about the location
   * path as a whole.
   *
   * @throws javax.xml.transform.TransformerException
   */
  public static int getAxisFromStep(
          Compiler compiler, int stepOpCodePos)
            throws javax.xml.transform.TransformerException
  {

    int ops[] = compiler.getOpMap();
    int stepType = ops[stepOpCodePos];

    switch (stepType)
    {
    case OpCodes.FROM_FOLLOWING :
      return Axis.FOLLOWING;
    case OpCodes.FROM_FOLLOWING_SIBLINGS :
      return Axis.FOLLOWINGSIBLING;
    case OpCodes.FROM_PRECEDING :
      return Axis.PRECEDING;
    case OpCodes.FROM_PRECEDING_SIBLINGS :
      return Axis.PRECEDINGSIBLING;
    case OpCodes.FROM_PARENT :
      return Axis.PARENT;
    case OpCodes.FROM_NAMESPACE :
      return Axis.NAMESPACE;
    case OpCodes.FROM_ANCESTORS :
      return Axis.ANCESTOR;
    case OpCodes.FROM_ANCESTORS_OR_SELF :
      return Axis.ANCESTORORSELF;
    case OpCodes.FROM_ATTRIBUTES :
      return Axis.ATTRIBUTE;
    case OpCodes.FROM_ROOT :
      return Axis.ROOT;
    case OpCodes.FROM_CHILDREN :
      return Axis.CHILD;
    case OpCodes.FROM_DESCENDANTS_OR_SELF :
      return Axis.DESCENDANTORSELF;
    case OpCodes.FROM_DESCENDANTS :
      return Axis.DESCENDANT;
    case OpCodes.FROM_SELF :
      return Axis.SELF;
    case OpCodes.OP_EXTFUNCTION :
    case OpCodes.OP_FUNCTION :
    case OpCodes.OP_GROUP :
    case OpCodes.OP_VARIABLE :
      return Axis.FILTEREDLIST;
    }

   }
    
    /**
     * Get a corresponding BIT_XXX from an axis.
     * @param axis One of Axis.ANCESTOR, etc.
     * @return One of BIT_ANCESTOR, etc.
     */
    static public int getAnalysisBitFromAxes(int axis)
    {
        {
        case Axis.ANCESTOR :
          return BIT_ANCESTOR;
        case Axis.ANCESTORORSELF :
          return BIT_ANCESTOR_OR_SELF;
        case Axis.ATTRIBUTE :
          return BIT_ATTRIBUTE;
        case Axis.CHILD :
          return BIT_CHILD;
        case Axis.DESCENDANT :
          return BIT_DESCENDANT;
        case Axis.DESCENDANTORSELF :
          return BIT_DESCENDANT_OR_SELF;
        case Axis.FOLLOWING :
          return BIT_FOLLOWING;
        case Axis.FOLLOWINGSIBLING :
          return BIT_FOLLOWING_SIBLING;
        case Axis.NAMESPACE :
        case Axis.NAMESPACEDECLS :
          return BIT_NAMESPACE;
        case Axis.PARENT :
          return BIT_PARENT;
        case Axis.PRECEDING :
          return BIT_PRECEDING;
        case Axis.PRECEDINGSIBLING :
          return BIT_PRECEDING_SIBLING;
        case Axis.SELF :
          return BIT_SELF;
        case Axis.ALLFROMNODE :
          return BIT_DESCENDANT_OR_SELF;
        case Axis.DESCENDANTSFROMROOT :
        case Axis.ALL :
        case Axis.DESCENDANTSORSELFFROMROOT :
          return BIT_ANY_DESCENDANT_FROM_ROOT;
        case Axis.ROOT :
          return BIT_ROOT;
        case Axis.FILTEREDLIST :
          return BIT_FILTER;
        default :
          return BIT_FILTER;
      }
    }
  
  static boolean functionProximateOrContainsProximate(Compiler compiler, 
                                                      int opPos)
  {
    int endFunc = opPos + compiler.getOp(opPos + 1) - 1;
    opPos = compiler.getFirstChildPos(opPos);
    int funcID = compiler.getOp(opPos);
    switch(funcID)
    {
      case FunctionTable.FUNC_LAST:
      case FunctionTable.FUNC_POSITION:
        return true;
      default:
        opPos++;
        int i = 0;
        for (int p = opPos; p < endFunc; p = compiler.getNextOpPos(p), i++)
        {
          int innerExprOpPos = p+2;
          int argOp = compiler.getOp(innerExprOpPos);
          boolean prox = isProximateInnerExpr(compiler, innerExprOpPos);
          if(prox)
            return true;
        }

    }
    return false;
  }
  
  static boolean isProximateInnerExpr(Compiler compiler, int opPos)
  {
    int op = compiler.getOp(opPos);
    int innerExprOpPos = opPos+2;
    switch(op)
    {
      case OpCodes.OP_ARGUMENT:
        if(isProximateInnerExpr(compiler, innerExprOpPos))
          return true;
        break;
      case OpCodes.OP_VARIABLE:
      case OpCodes.OP_NUMBERLIT:
      case OpCodes.OP_LITERAL:
      case OpCodes.OP_LOCATIONPATH:
      case OpCodes.OP_FUNCTION:
        boolean isProx = functionProximateOrContainsProximate(compiler, opPos);
        if(isProx)
          return true;
        break;
      case OpCodes.OP_GT:
      case OpCodes.OP_GTE:
      case OpCodes.OP_LT:
      case OpCodes.OP_LTE:
      case OpCodes.OP_EQUALS:
        int leftPos = compiler.getFirstChildPos(op);
        int rightPos = compiler.getNextOpPos(leftPos);
        isProx = isProximateInnerExpr(compiler, leftPos);
        if(isProx)
          return true;
        isProx = isProximateInnerExpr(compiler, rightPos);
        if(isProx)
          return true;
        break;
      default:
    }
    return false;
  }
    
  /**
   * Tell if the predicates need to have proximity knowledge.
   */
  public static boolean mightBeProximate(Compiler compiler, int opPos, int stepType)
          throws javax.xml.transform.TransformerException
  {

    boolean mightBeProximate = false;
    int argLen;

    switch (stepType)
    {
    case OpCodes.OP_VARIABLE :
    case OpCodes.OP_EXTFUNCTION :
    case OpCodes.OP_FUNCTION :
    case OpCodes.OP_GROUP :
      argLen = compiler.getArgLength(opPos);
      break;
    default :
      argLen = compiler.getArgLengthOfStep(opPos);
    }

    int predPos = compiler.getFirstPredicateOpPos(opPos);
    int count = 0;

    while (OpCodes.OP_PREDICATE == compiler.getOp(predPos))
    {
      count++;
      
      int innerExprOpPos = predPos+2;
      int predOp = compiler.getOp(innerExprOpPos);

      switch(predOp)
      {
        case OpCodes.OP_VARIABLE:
        case OpCodes.OP_LOCATIONPATH:
          break;
        case OpCodes.OP_NUMBER:
        case OpCodes.OP_NUMBERLIT:
        case OpCodes.OP_FUNCTION:
          boolean isProx 
            = functionProximateOrContainsProximate(compiler, innerExprOpPos);
          if(isProx)
            return true;
          break;
        case OpCodes.OP_GT:
        case OpCodes.OP_GTE:
        case OpCodes.OP_LT:
        case OpCodes.OP_LTE:
        case OpCodes.OP_EQUALS:
          int leftPos = compiler.getFirstChildPos(innerExprOpPos);
          int rightPos = compiler.getNextOpPos(leftPos);
          isProx = isProximateInnerExpr(compiler, leftPos);
          if(isProx)
            return true;
          isProx = isProximateInnerExpr(compiler, rightPos);
          if(isProx)
            return true;
          break;
        default:
      }

      predPos = compiler.getNextOpPos(predPos);
    }

    return mightBeProximate;
  }
  
  /**
   * Special purpose function to see if we can optimize the pattern for 
   * a DescendantIterator.
   *
   * @param compiler non-null reference to compiler object that has processed
   *                 the XPath operations into an opcode map.
   * @param stepOpCodePos The opcode position for the step.
   * @param stepIndex The top-level step index withing the iterator.
   *
   * @return 32 bits as an integer that give information about the location
   * path as a whole.
   *
   * @throws javax.xml.transform.TransformerException
   */
  private static boolean isOptimizableForDescendantIterator(
          Compiler compiler, int stepOpCodePos, int stepIndex)
            throws javax.xml.transform.TransformerException
  {

    int stepType;
    int ops[] = compiler.getOpMap();
    int stepCount = 0;
    boolean foundDorDS = false;
    boolean foundSelf = false;
    boolean foundDS = false;
    
    int nodeTestType = OpCodes.NODETYPE_NODE;
    
    while (OpCodes.ENDOP != (stepType = ops[stepOpCodePos]))
    {
      if(nodeTestType != OpCodes.NODETYPE_NODE && nodeTestType != OpCodes.NODETYPE_ROOT)
        return false;
        
      stepCount++;
      if(stepCount > 3)
        return false;
        
      boolean mightBeProximate = mightBeProximate(compiler, stepOpCodePos, stepType);
      if(mightBeProximate)
        return false;

      switch (stepType)
      {
      case OpCodes.FROM_FOLLOWING :
      case OpCodes.FROM_FOLLOWING_SIBLINGS :
      case OpCodes.FROM_PRECEDING :
      case OpCodes.FROM_PRECEDING_SIBLINGS :
      case OpCodes.FROM_PARENT :
      case OpCodes.OP_VARIABLE :
      case OpCodes.OP_EXTFUNCTION :
      case OpCodes.OP_FUNCTION :
      case OpCodes.OP_GROUP :
      case OpCodes.FROM_NAMESPACE :
      case OpCodes.FROM_ANCESTORS :
      case OpCodes.FROM_ANCESTORS_OR_SELF :
      case OpCodes.FROM_ATTRIBUTES :
      case OpCodes.MATCH_ATTRIBUTE :
      case OpCodes.MATCH_ANY_ANCESTOR :
      case OpCodes.MATCH_IMMEDIATE_ANCESTOR :
        return false;
      case OpCodes.FROM_ROOT :
        if(1 != stepCount)
          return false;
        break;
      case OpCodes.FROM_CHILDREN :
        if(!foundDS && !(foundDorDS && foundSelf))
          return false;
        break;
      case OpCodes.FROM_DESCENDANTS_OR_SELF :
        foundDS = true;
      case OpCodes.FROM_DESCENDANTS :
        if(3 == stepCount)
          return false;
        foundDorDS = true;
        break;
      case OpCodes.FROM_SELF :
        if(1 != stepCount)
          return false;
        foundSelf = true;
        break;
      default :
      }
      
      nodeTestType = compiler.getStepTestType(stepOpCodePos);

      int nextStepOpCodePos = compiler.getNextStepPos(stepOpCodePos);

      if (nextStepOpCodePos < 0)
        break;
        
      if(OpCodes.ENDOP != ops[nextStepOpCodePos])
      {
        if(compiler.countPredicates(stepOpCodePos) > 0)
        {
          return false;
        }
      }
      
      stepOpCodePos = nextStepOpCodePos;
    }

    return true;
  }

  /**
   * Analyze the location path and return 32 bits that give information about
   * the location path as a whole.  See the BIT_XXX constants for meaning about
   * each of the bits.
   *
   * @param compiler non-null reference to compiler object that has processed
   *                 the XPath operations into an opcode map.
   * @param stepOpCodePos The opcode position for the step.
   * @param stepIndex The top-level step index withing the iterator.
   *
   * @return 32 bits as an integer that give information about the location
   * path as a whole.
   *
   * @throws javax.xml.transform.TransformerException
   */
  private static int analyze(
          Compiler compiler, int stepOpCodePos, int stepIndex)
            throws javax.xml.transform.TransformerException
  {

    int stepType;
    int ops[] = compiler.getOpMap();
    int stepCount = 0;

    while (OpCodes.ENDOP != (stepType = ops[stepOpCodePos]))
    {
      stepCount++;

      boolean predAnalysis = analyzePredicate(compiler, stepOpCodePos,
                                              stepType);

      if (predAnalysis)
        analysisResult |= BIT_PREDICATE;

      switch (stepType)
      {
      case OpCodes.OP_VARIABLE :
      case OpCodes.OP_EXTFUNCTION :
      case OpCodes.OP_FUNCTION :
      case OpCodes.OP_GROUP :
        analysisResult |= BIT_FILTER;
        break;
      case OpCodes.FROM_ROOT :
        analysisResult |= BIT_ROOT;
        break;
      case OpCodes.FROM_ANCESTORS :
        analysisResult |= BIT_ANCESTOR;
        break;
      case OpCodes.FROM_ANCESTORS_OR_SELF :
        analysisResult |= BIT_ANCESTOR_OR_SELF;
        break;
      case OpCodes.FROM_ATTRIBUTES :
        analysisResult |= BIT_ATTRIBUTE;
        break;
      case OpCodes.FROM_NAMESPACE :
        analysisResult |= BIT_NAMESPACE;
        break;
      case OpCodes.FROM_CHILDREN :
        analysisResult |= BIT_CHILD;
        break;
      case OpCodes.FROM_DESCENDANTS :
        analysisResult |= BIT_DESCENDANT;
        break;
      case OpCodes.FROM_DESCENDANTS_OR_SELF :

        if (2 == stepCount && BIT_ROOT == analysisResult)
        {
          analysisResult |= BIT_ANY_DESCENDANT_FROM_ROOT;
        }

        analysisResult |= BIT_DESCENDANT_OR_SELF;
        break;
      case OpCodes.FROM_FOLLOWING :
        analysisResult |= BIT_FOLLOWING;
        break;
      case OpCodes.FROM_FOLLOWING_SIBLINGS :
        analysisResult |= BIT_FOLLOWING_SIBLING;
        break;
      case OpCodes.FROM_PRECEDING :
        analysisResult |= BIT_PRECEDING;
        break;
      case OpCodes.FROM_PRECEDING_SIBLINGS :
        analysisResult |= BIT_PRECEDING_SIBLING;
        break;
      case OpCodes.FROM_PARENT :
        analysisResult |= BIT_PARENT;
        break;
      case OpCodes.FROM_SELF :
        analysisResult |= BIT_SELF;
        break;
      case OpCodes.MATCH_ATTRIBUTE :
        analysisResult |= (BIT_MATCH_PATTERN | BIT_ATTRIBUTE);
        break;
      case OpCodes.MATCH_ANY_ANCESTOR :
        analysisResult |= (BIT_MATCH_PATTERN | BIT_ANCESTOR);
        break;
      case OpCodes.MATCH_IMMEDIATE_ANCESTOR :
        analysisResult |= (BIT_MATCH_PATTERN | BIT_PARENT);
        break;
      default :
      }

      {
        analysisResult |= BIT_NODETEST_ANY;
      }

      stepOpCodePos = compiler.getNextStepPos(stepOpCodePos);

      if (stepOpCodePos < 0)
        break;
    }

    analysisResult |= (stepCount & BITS_COUNT);

    return analysisResult;
  }
  
  /**
   * Tell if the given axis goes downword.  Bogus name, if you can think of 
   * a better one, please do tell.  This really has to do with inverting 
   * attribute axis.
   * @param axis One of Axis.XXX.
   * @return true if the axis is not a child axis and does not go up from 
   * the axis root.
   */
  public static boolean isDownwardAxisOfMany(int axis)
  {
    return ((Axis.DESCENDANTORSELF == axis) ||
          (Axis.DESCENDANT == axis) 
          || (Axis.FOLLOWING == axis) 
          || (Axis.PRECEDING == axis) 
          );
  }

  /**
   * as a generalized match pattern.  What this means is that the LocationPath
   * is read backwards, as a test on a given node, to see if it matches the
   * criteria of the selection, and ends up at the context node.  Essentially,
   * this is a backwards query from a given node, to find the context node.
   * <p>So, the selection "foo/daz[2]" is, in non-abreviated expanded syntax,
   * "self::node()/following-sibling::foo/child::daz[position()=2]".
   * Taking this as a match pattern for a probable node, it works out to
   * "self::daz/parent::foo[child::daz[position()=2 and isPrevStepNode()]
   * precedingSibling::node()[isContextNodeOfLocationPath()]", adding magic
   * isPrevStepNode and isContextNodeOfLocationPath operations.  Predicates in
   * the location path have to be executed by the following step,
   * because they have to know the context of their execution.
   *
   * @param mpi The MatchPatternIterator to which the steps will be attached.
   * @param compiler The compiler that holds the syntax tree/op map to
   * construct from.
   * @param stepOpCodePos The current op code position within the opmap.
   * @param stepIndex The top-level step index withing the iterator.
   *
   * @return A StepPattern object, which may contain relative StepPatterns.
   *
   * @throws javax.xml.transform.TransformerException
   */
  static StepPattern loadSteps(
          MatchPatternIterator mpi, Compiler compiler, int stepOpCodePos, 
                                                       int stepIndex)
            throws javax.xml.transform.TransformerException
  {
    if (DEBUG_PATTERN_CREATION)
    {
      System.out.println("================");
      System.out.println("loadSteps for: "+compiler.getPatternString());
    }
    int stepType;
    StepPattern step = null;
    StepPattern firstStep = null, prevStep = null;
    int ops[] = compiler.getOpMap();
    int analysis = analyze(compiler, stepOpCodePos, stepIndex);

    while (OpCodes.ENDOP != (stepType = ops[stepOpCodePos]))
    {
      step = createDefaultStepPattern(compiler, stepOpCodePos, mpi, analysis,
                                      firstStep, prevStep);

      if (null == firstStep)
      {
        firstStep = step;
      }
      else
      {

        step.setRelativePathPattern(prevStep);
      }

      prevStep = step;
      stepOpCodePos = compiler.getNextStepPos(stepOpCodePos);

      if (stepOpCodePos < 0)
        break;
    }
    
    int axis = Axis.SELF;
    int paxis = Axis.SELF;
    StepPattern tail = step;
    for (StepPattern pat = step; null != pat; 
         pat = pat.getRelativePathPattern()) 
    {
      int nextAxis = pat.getAxis();
      pat.setAxis(axis);
      
      
      int whatToShow = pat.getWhatToShow();
      if(whatToShow == DTMFilter.SHOW_ATTRIBUTE || 
         whatToShow == DTMFilter.SHOW_NAMESPACE)
      {
        int newAxis = (whatToShow == DTMFilter.SHOW_ATTRIBUTE) ? 
                       Axis.ATTRIBUTE : Axis.NAMESPACE;
        if(isDownwardAxisOfMany(axis))
        {
          StepPattern attrPat = new StepPattern(whatToShow, 
                                    pat.getNamespace(),
                                    pat.getLocalName(),
          XNumber score = pat.getStaticScore();
          pat.setNamespace(null);
          pat.setLocalName(NodeTest.WILD);
          attrPat.setPredicates(pat.getPredicates());
          pat.setPredicates(null);
          pat.setWhatToShow(DTMFilter.SHOW_ELEMENT);
          StepPattern rel = pat.getRelativePathPattern();
          pat.setRelativePathPattern(attrPat);
          attrPat.setRelativePathPattern(rel);
          attrPat.setStaticScore(score);
          
          if(Axis.PRECEDING == pat.getAxis())
            pat.setAxis(Axis.PRECEDINGANDANCESTOR);
            
          else if(Axis.DESCENDANT == pat.getAxis())
            pat.setAxis(Axis.DESCENDANTORSELF);
          
          pat = attrPat;
        }
        else if(Axis.CHILD == pat.getAxis())
        {
          pat.setAxis(Axis.ATTRIBUTE);
        }
      }
      axis = nextAxis;
      tail = pat;
    }
    
    if(axis < Axis.ALL)
    {
      StepPattern selfPattern = new ContextMatchStepPattern(axis, paxis);
      XNumber score = tail.getStaticScore();
      tail.setRelativePathPattern(selfPattern);
      tail.setStaticScore(score);
      selfPattern.setStaticScore(score);
    }        

    if (DEBUG_PATTERN_CREATION)
    {
      System.out.println("Done loading steps: "+step.toString());
            
      System.out.println("");
    }
  }

  /**
   * Create a StepPattern that is contained within a LocationPath.
   *
   *
   * @param compiler The compiler that holds the syntax tree/op map to
   * construct from.
   * @param stepOpCodePos The current op code position within the opmap.
   * @param mpi The MatchPatternIterator to which the steps will be attached.
   * @param analysis 32 bits of analysis, from which the type of AxesWalker
   *                 may be influenced.
   * @param tail The step that is the first step analyzed, but the last 
   *                  step in the relative match linked list, i.e. the tail.
   *                  May be null.
   * @param head The step that is the current head of the relative 
   *                 match step linked list.
   *                 May be null.
   *
   * @return the head of the list.
   *
   * @throws javax.xml.transform.TransformerException
   */
  private static StepPattern createDefaultStepPattern(
          Compiler compiler, int opPos, MatchPatternIterator mpi, 
          int analysis, StepPattern tail, StepPattern head)
            throws javax.xml.transform.TransformerException
  {

    int stepType = compiler.getOp(opPos);
    boolean simpleInit = false;
    int totalNumberWalkers = (analysis & BITS_COUNT);
    boolean prevIsOneStepDown = true;
    int firstStepPos = compiler.getFirstChildPos(opPos);
    
    int whatToShow = compiler.getWhatToShow(opPos);
    StepPattern ai = null;
    int axis, predicateAxis;
    
    switch (stepType)
    {
    case OpCodes.OP_VARIABLE :
    case OpCodes.OP_EXTFUNCTION :
    case OpCodes.OP_FUNCTION :
    case OpCodes.OP_GROUP :
      prevIsOneStepDown = false;

      Expression expr;

      switch (stepType)
      {
      case OpCodes.OP_VARIABLE :
      case OpCodes.OP_EXTFUNCTION :
      case OpCodes.OP_FUNCTION :
      case OpCodes.OP_GROUP :
        expr = compiler.compile(opPos);
        break;
      default :
        expr = compiler.compile(opPos + 2);
      }

      axis = Axis.FILTEREDLIST;
      predicateAxis = Axis.FILTEREDLIST;
      ai = new FunctionPattern(expr, axis, predicateAxis);
      simpleInit = true;
      break;
    case OpCodes.FROM_ROOT :
      whatToShow = DTMFilter.SHOW_DOCUMENT
                   | DTMFilter.SHOW_DOCUMENT_FRAGMENT;

      axis = Axis.ROOT;
      predicateAxis = Axis.ROOT;
      ai = new StepPattern(DTMFilter.SHOW_DOCUMENT | 
                                DTMFilter.SHOW_DOCUMENT_FRAGMENT,
                                axis, predicateAxis);
      break;
    case OpCodes.FROM_ATTRIBUTES :
      whatToShow = DTMFilter.SHOW_ATTRIBUTE;
      axis = Axis.PARENT;
      predicateAxis = Axis.ATTRIBUTE;
      break;
    case OpCodes.FROM_NAMESPACE :
      whatToShow = DTMFilter.SHOW_NAMESPACE;
      axis = Axis.PARENT;
      predicateAxis = Axis.NAMESPACE;
      break;
    case OpCodes.FROM_ANCESTORS :
      axis = Axis.DESCENDANT;
      predicateAxis = Axis.ANCESTOR;
      break;
    case OpCodes.FROM_CHILDREN :
      axis = Axis.PARENT;
      predicateAxis = Axis.CHILD;
      break;
    case OpCodes.FROM_ANCESTORS_OR_SELF :
      axis = Axis.DESCENDANTORSELF;
      predicateAxis = Axis.ANCESTORORSELF;
      break;
    case OpCodes.FROM_SELF :
      axis = Axis.SELF;
      predicateAxis = Axis.SELF;
      break;
    case OpCodes.FROM_PARENT :
      axis = Axis.CHILD;
      predicateAxis = Axis.PARENT;
      break;
    case OpCodes.FROM_PRECEDING_SIBLINGS :
      axis = Axis.FOLLOWINGSIBLING;
      predicateAxis = Axis.PRECEDINGSIBLING;
      break;
    case OpCodes.FROM_PRECEDING :
      axis = Axis.FOLLOWING;
      predicateAxis = Axis.PRECEDING;
      break;
    case OpCodes.FROM_FOLLOWING_SIBLINGS :
      axis = Axis.PRECEDINGSIBLING;
      predicateAxis = Axis.FOLLOWINGSIBLING;
      break;
    case OpCodes.FROM_FOLLOWING :
      axis = Axis.PRECEDING;
      predicateAxis = Axis.FOLLOWING;
      break;
    case OpCodes.FROM_DESCENDANTS_OR_SELF :
      axis = Axis.ANCESTORORSELF;
      predicateAxis = Axis.DESCENDANTORSELF;
      break;
    case OpCodes.FROM_DESCENDANTS :
      axis = Axis.ANCESTOR;
      predicateAxis = Axis.DESCENDANT;
      break;
    default :
    }
    if(null == ai)
    {
      ai = new StepPattern(whatToShow, compiler.getStepNS(opPos),
                                compiler.getStepLocalName(opPos),
                                axis, predicateAxis);
    }
   
    if (false || DEBUG_PATTERN_CREATION)
    {
      System.out.print("new step: "+ ai);
      System.out.print(", axis: " + Axis.names[ai.getAxis()]);
      System.out.print(", predAxis: " + Axis.names[ai.getAxis()]);
      System.out.print(", what: ");
      System.out.print("    ");
      ai.debugWhatToShow(ai.getWhatToShow());
    }

    int argLen = compiler.getFirstPredicateOpPos(opPos);

    ai.setPredicates(compiler.getCompiledPredicates(argLen));

    return ai;
  }

  /**
   * Analyze a step and give information about it's predicates.  Right now this
   * just returns true or false if the step has a predicate.
   *
   * @param compiler non-null reference to compiler object that has processed
   *                 the XPath operations into an opcode map.
   * @param opPos The opcode position for the step.
   * @param stepType The type of step, one of OP_GROUP, etc.
   *
   * @return true if step has a predicate.
   *
   * @throws javax.xml.transform.TransformerException
   */
  static boolean analyzePredicate(Compiler compiler, int opPos, int stepType)
          throws javax.xml.transform.TransformerException
  {

    int argLen;

    switch (stepType)
    {
    case OpCodes.OP_VARIABLE :
    case OpCodes.OP_EXTFUNCTION :
    case OpCodes.OP_FUNCTION :
    case OpCodes.OP_GROUP :
      argLen = compiler.getArgLength(opPos);
      break;
    default :
      argLen = compiler.getArgLengthOfStep(opPos);
    }

    int pos = compiler.getFirstPredicateOpPos(opPos);
    int nPredicates = compiler.countPredicates(pos);

    return (nPredicates > 0) ? true : false;
  }

  /**
   * Create the proper Walker from the axes type.
   *
   * @param compiler non-null reference to compiler object that has processed
   *                 the XPath operations into an opcode map.
   * @param opPos The opcode position for the step.
   * @param lpi The owning location path iterator.
   * @param analysis 32 bits of analysis, from which the type of AxesWalker
   *                 may be influenced.
   *
   * @return non-null reference to AxesWalker derivative.
   * @throws RuntimeException if the input is bad.
   */
  private static AxesWalker createDefaultWalker(Compiler compiler, int opPos,
          WalkingIterator lpi, int analysis)
  {

    AxesWalker ai = null;
    int stepType = compiler.getOp(opPos);

    /*
    System.out.println("0: "+compiler.getOp(opPos));
    System.out.println("1: "+compiler.getOp(opPos+1));
    System.out.println("2: "+compiler.getOp(opPos+2));
    System.out.println("3: "+compiler.getOp(opPos+3));
    System.out.println("4: "+compiler.getOp(opPos+4));
    System.out.println("5: "+compiler.getOp(opPos+5));
    */
    boolean simpleInit = false;
    int totalNumberWalkers = (analysis & BITS_COUNT);
    boolean prevIsOneStepDown = true;

    switch (stepType)
    {
    case OpCodes.OP_VARIABLE :
    case OpCodes.OP_EXTFUNCTION :
    case OpCodes.OP_FUNCTION :
    case OpCodes.OP_GROUP :
      prevIsOneStepDown = false;

      if (DEBUG_WALKER_CREATION)
        System.out.println("new walker:  FilterExprWalker: " + analysis
                           + ", " + compiler.toString());

      ai = new FilterExprWalker(lpi);
      simpleInit = true;
      break;
    case OpCodes.FROM_ROOT :
      ai = new AxesWalker(lpi, Axis.ROOT);
      break;
    case OpCodes.FROM_ANCESTORS :
      prevIsOneStepDown = false;
      ai = new ReverseAxesWalker(lpi, Axis.ANCESTOR);
      break;
    case OpCodes.FROM_ANCESTORS_OR_SELF :
      prevIsOneStepDown = false;
      ai = new ReverseAxesWalker(lpi, Axis.ANCESTORORSELF);
      break;
    case OpCodes.FROM_ATTRIBUTES :
      ai = new AxesWalker(lpi, Axis.ATTRIBUTE);
      break;
    case OpCodes.FROM_NAMESPACE :
      ai = new AxesWalker(lpi, Axis.NAMESPACE);
      break;
    case OpCodes.FROM_CHILDREN :
      ai = new AxesWalker(lpi, Axis.CHILD);
      break;
    case OpCodes.FROM_DESCENDANTS :
      prevIsOneStepDown = false;
      ai = new AxesWalker(lpi, Axis.DESCENDANT);
      break;
    case OpCodes.FROM_DESCENDANTS_OR_SELF :
      prevIsOneStepDown = false;
      ai = new AxesWalker(lpi, Axis.DESCENDANTORSELF);
      break;
    case OpCodes.FROM_FOLLOWING :
      prevIsOneStepDown = false;
      ai = new AxesWalker(lpi, Axis.FOLLOWING);
      break;
    case OpCodes.FROM_FOLLOWING_SIBLINGS :
      prevIsOneStepDown = false;
      ai = new AxesWalker(lpi, Axis.FOLLOWINGSIBLING);
      break;
    case OpCodes.FROM_PRECEDING :
      prevIsOneStepDown = false;
      ai = new ReverseAxesWalker(lpi, Axis.PRECEDING);
      break;
    case OpCodes.FROM_PRECEDING_SIBLINGS :
      prevIsOneStepDown = false;
      ai = new ReverseAxesWalker(lpi, Axis.PRECEDINGSIBLING);
      break;
    case OpCodes.FROM_PARENT :
      prevIsOneStepDown = false;
      ai = new ReverseAxesWalker(lpi, Axis.PARENT);
      break;
    case OpCodes.FROM_SELF :
      ai = new AxesWalker(lpi, Axis.SELF);
      break;
    default :
    }

    if (simpleInit)
    {
      ai.initNodeTest(DTMFilter.SHOW_ALL);
    }
    else
    {
      int whatToShow = compiler.getWhatToShow(opPos);

      /*
      System.out.print("construct: ");
      NodeTest.debugWhatToShow(whatToShow);
      System.out.println("or stuff: "+(whatToShow & (DTMFilter.SHOW_ATTRIBUTE
                             | DTMFilter.SHOW_ELEMENT
                             | DTMFilter.SHOW_PROCESSING_INSTRUCTION)));
      */
      if ((0 == (whatToShow
                 & (DTMFilter.SHOW_ATTRIBUTE | DTMFilter.SHOW_NAMESPACE | DTMFilter.SHOW_ELEMENT
                    | DTMFilter.SHOW_PROCESSING_INSTRUCTION))) || (whatToShow == DTMFilter.SHOW_ALL))
        ai.initNodeTest(whatToShow);
      else
      {
        ai.initNodeTest(whatToShow, compiler.getStepNS(opPos),
                        compiler.getStepLocalName(opPos));
      }
    }

    return ai;
  }
  
  public static String getAnalysisString(int analysis)
  {
    StringBuffer buf = new StringBuffer();
    buf.append("count: "+getStepCount(analysis)+" ");
    if((analysis & BIT_NODETEST_ANY) != 0)
    {
      buf.append("NTANY|");
    }
    if((analysis & BIT_PREDICATE) != 0)
    {
      buf.append("PRED|");
    }
    if((analysis & BIT_ANCESTOR) != 0)
    {
      buf.append("ANC|");
    }
    if((analysis & BIT_ANCESTOR_OR_SELF) != 0)
    {
      buf.append("ANCOS|");
    }
    if((analysis & BIT_ATTRIBUTE) != 0)
    {
      buf.append("ATTR|");
    }
    if((analysis & BIT_CHILD) != 0)
    {
      buf.append("CH|");
    }
    if((analysis & BIT_DESCENDANT) != 0)
    {
      buf.append("DESC|");
    }
    if((analysis & BIT_DESCENDANT_OR_SELF) != 0)
    {
      buf.append("DESCOS|");
    }
    if((analysis & BIT_FOLLOWING) != 0)
    {
      buf.append("FOL|");
    }
    if((analysis & BIT_FOLLOWING_SIBLING) != 0)
    {
      buf.append("FOLS|");
    }
    if((analysis & BIT_NAMESPACE) != 0)
    {
      buf.append("NS|");
    }
    if((analysis & BIT_PARENT) != 0)
    {
      buf.append("P|");
    }
    if((analysis & BIT_PRECEDING) != 0)
    {
      buf.append("PREC|");
    }
    if((analysis & BIT_PRECEDING_SIBLING) != 0)
    {
      buf.append("PRECS|");
    }
    if((analysis & BIT_SELF) != 0)
    {
      buf.append(".|");
    }
    if((analysis & BIT_FILTER) != 0)
    {
      buf.append("FLT|");
    }
    if((analysis & BIT_ROOT) != 0)
    {
      buf.append("R|");
    }
    return buf.toString();
  }

  /** Set to true for diagnostics about walker creation */
  static final boolean DEBUG_PATTERN_CREATION = false;

  /** Set to true for diagnostics about walker creation */
  static final boolean DEBUG_WALKER_CREATION = false;

  /** Set to true for diagnostics about iterator creation */
  static final boolean DEBUG_ITERATOR_CREATION = false;
  
  public static boolean hasPredicate(int analysis)
  {
    return (0 != (analysis & BIT_PREDICATE));
  }

  public static boolean isWild(int analysis)
  {
    return (0 != (analysis & BIT_NODETEST_ANY));
  }

  public static boolean walksAncestors(int analysis)
  {
    return isSet(analysis, BIT_ANCESTOR | BIT_ANCESTOR_OR_SELF);
  }
  
  public static boolean walksAttributes(int analysis)
  {
    return (0 != (analysis & BIT_ATTRIBUTE));
  }

  public static boolean walksNamespaces(int analysis)
  {
    return (0 != (analysis & BIT_NAMESPACE));
  }  

  public static boolean walksChildren(int analysis)
  {
    return (0 != (analysis & BIT_CHILD));
  }

  public static boolean walksDescendants(int analysis)
  {
    return isSet(analysis, BIT_DESCENDANT | BIT_DESCENDANT_OR_SELF);
  }

  public static boolean walksSubtree(int analysis)
  {
    return isSet(analysis, BIT_DESCENDANT | BIT_DESCENDANT_OR_SELF | BIT_CHILD);
  }
  
  public static boolean walksSubtreeOnlyMaybeAbsolute(int analysis)
  {
    return walksSubtree(analysis)
           && !walksExtraNodes(analysis) 
           && !walksUp(analysis) 
           && !walksSideways(analysis) 
           ;
  }
  
  public static boolean walksSubtreeOnly(int analysis)
  {
    return walksSubtreeOnlyMaybeAbsolute(analysis) 
           && !isAbsolute(analysis) 
           ;
  }

  public static boolean walksFilteredList(int analysis)
  {
    return isSet(analysis, BIT_FILTER);
  }
  
  public static boolean walksSubtreeOnlyFromRootOrContext(int analysis)
  {
    return walksSubtree(analysis)
           && !walksExtraNodes(analysis) 
           && !walksUp(analysis) 
           && !walksSideways(analysis) 
           && !isSet(analysis, BIT_FILTER) 
           ;
  }

  public static boolean walksInDocOrder(int analysis)
  {
    return (walksSubtreeOnlyMaybeAbsolute(analysis)
           || walksExtraNodesOnly(analysis)
           || walksFollowingOnlyMaybeAbsolute(analysis)) 
           && !isSet(analysis, BIT_FILTER) 
           ;
  }
  
  public static boolean walksFollowingOnlyMaybeAbsolute(int analysis)
  {
    return isSet(analysis, BIT_SELF | BIT_FOLLOWING_SIBLING | BIT_FOLLOWING)
           && !walksSubtree(analysis) 
           && !walksUp(analysis) 
           && !walksSideways(analysis) 
           ;
  }
  
  public static boolean walksUp(int analysis)
  {
    return isSet(analysis, BIT_PARENT | BIT_ANCESTOR | BIT_ANCESTOR_OR_SELF);
  }
  
  public static boolean walksSideways(int analysis)
  {
    return isSet(analysis, BIT_FOLLOWING | BIT_FOLLOWING_SIBLING | 
                           BIT_PRECEDING | BIT_PRECEDING_SIBLING);
  }
  
  public static boolean walksExtraNodes(int analysis)
  {
    return isSet(analysis, BIT_NAMESPACE | BIT_ATTRIBUTE);
  }

  public static boolean walksExtraNodesOnly(int analysis)
  {
    return walksExtraNodes(analysis)
           && !isSet(analysis, BIT_SELF) 
           && !walksSubtree(analysis) 
           && !walksUp(analysis) 
           && !walksSideways(analysis) 
           && !isAbsolute(analysis) 
           ;
  }

  public static boolean isAbsolute(int analysis)
  {
    return isSet(analysis, BIT_ROOT | BIT_FILTER);
  }
  
  public static boolean walksChildrenOnly(int analysis)
  {
    return walksChildren(analysis)
           && !isSet(analysis, BIT_SELF)
           && !walksExtraNodes(analysis)
           && !walksDescendants(analysis) 
           && !walksUp(analysis) 
           && !walksSideways(analysis) 
           && (!isAbsolute(analysis) || isSet(analysis, BIT_ROOT))
           ;
  }
  
  public static boolean walksChildrenAndExtraAndSelfOnly(int analysis)
  {
    return walksChildren(analysis)
           && !walksDescendants(analysis) 
           && !walksUp(analysis) 
           && !walksSideways(analysis) 
           && (!isAbsolute(analysis) || isSet(analysis, BIT_ROOT))
           ;
  }
  
  public static boolean walksDescendantsAndExtraAndSelfOnly(int analysis)
  {
    return !walksChildren(analysis)
           && walksDescendants(analysis) 
           && !walksUp(analysis) 
           && !walksSideways(analysis) 
           && (!isAbsolute(analysis) || isSet(analysis, BIT_ROOT))
           ;
  }
  
  public static boolean walksSelfOnly(int analysis)
  {
    return isSet(analysis, BIT_SELF) 
           && !walksSubtree(analysis) 
           && !walksUp(analysis) 
           && !walksSideways(analysis) 
           && !isAbsolute(analysis) 
           ;
  }

  
  public static boolean walksUpOnly(int analysis)
  {
    return !walksSubtree(analysis) 
           && walksUp(analysis) 
           && !walksSideways(analysis) 
           && !isAbsolute(analysis) 
           ;
  }
  
  public static boolean walksDownOnly(int analysis)
  {
    return walksSubtree(analysis) 
           && !walksUp(analysis) 
           && !walksSideways(analysis) 
           && !isAbsolute(analysis) 
           ;
  }

  public static boolean walksDownExtraOnly(int analysis)
  {
    return walksSubtree(analysis) &&  walksExtraNodes(analysis)
           && !walksUp(analysis) 
           && !walksSideways(analysis) 
           && !isAbsolute(analysis) 
           ;
  }
  
  public static boolean canSkipSubtrees(int analysis)
  {
    return isSet(analysis, BIT_CHILD) | walksSideways(analysis);
  }
  
  public static boolean canCrissCross(int analysis)
  {
    if(walksSelfOnly(analysis))
      return false;
    else if(walksDownOnly(analysis) && !canSkipSubtrees(analysis))
      return false;
    else if(walksChildrenAndExtraAndSelfOnly(analysis))
      return false;
    else if(walksDescendantsAndExtraAndSelfOnly(analysis))
      return false;
    else if(walksUpOnly(analysis))
      return false;
    else if(walksExtraNodesOnly(analysis))
      return false;
    else if(walksSubtree(analysis) 
           && (walksSideways(analysis) 
            || walksUp(analysis) 
            || canSkipSubtrees(analysis)))
      return true;
    else
      return false;
  }
  
  /**
   * Tell if the pattern can be 'walked' with the iteration steps in natural 
   * document order, without duplicates.
   *
   * @param analysis The general analysis of the pattern.
   *
   * @return true if the walk can be done in natural order.
   *
   * @throws javax.xml.transform.TransformerException
   */
  static public boolean isNaturalDocOrder(int analysis)
  {
    if(canCrissCross(analysis) || isSet(analysis, BIT_NAMESPACE) ||
       walksFilteredList(analysis))
      return false;
      
    if(walksInDocOrder(analysis))
      return true;
      
    return false;
  }
  
  /**
   * Tell if the pattern can be 'walked' with the iteration steps in natural 
   * document order, without duplicates.
   *
   * @param compiler non-null reference to compiler object that has processed
   *                 the XPath operations into an opcode map.
   * @param stepOpCodePos The opcode position for the step.
   * @param stepIndex The top-level step index withing the iterator.
   * @param analysis The general analysis of the pattern.
   *
   * @return true if the walk can be done in natural order.
   *
   * @throws javax.xml.transform.TransformerException
   */
  private static boolean isNaturalDocOrder(
          Compiler compiler, int stepOpCodePos, int stepIndex, int analysis)
            throws javax.xml.transform.TransformerException
  {
    if(canCrissCross(analysis))
      return false;
      
    if(isSet(analysis, BIT_NAMESPACE))
      return false;
      
    if(isSet(analysis, BIT_FOLLOWING | BIT_FOLLOWING_SIBLING) && 
       isSet(analysis, BIT_PRECEDING | BIT_PRECEDING_SIBLING))
      return  false;
      
    
    int stepType;
    int ops[] = compiler.getOpMap();
    int stepCount = 0;
    boolean foundWildAttribute = false;
    
    int potentialDuplicateMakingStepCount = 0;
    
    while (OpCodes.ENDOP != (stepType = ops[stepOpCodePos]))
    {        
      stepCount++;
        
      switch (stepType)
      {
      case OpCodes.FROM_ATTRIBUTES :
      case OpCodes.MATCH_ATTRIBUTE :
          return false;
        
        
        String localName = compiler.getStepLocalName(stepOpCodePos);
        if(localName.equals("*"))
        {
          foundWildAttribute = true;
        }
        break;
      case OpCodes.FROM_FOLLOWING :
      case OpCodes.FROM_FOLLOWING_SIBLINGS :
      case OpCodes.FROM_PRECEDING :
      case OpCodes.FROM_PRECEDING_SIBLINGS :
      case OpCodes.FROM_PARENT :
      case OpCodes.OP_VARIABLE :
      case OpCodes.OP_EXTFUNCTION :
      case OpCodes.OP_FUNCTION :
      case OpCodes.OP_GROUP :
      case OpCodes.FROM_NAMESPACE :
      case OpCodes.FROM_ANCESTORS :
      case OpCodes.FROM_ANCESTORS_OR_SELF :      
      case OpCodes.MATCH_ANY_ANCESTOR :
      case OpCodes.MATCH_IMMEDIATE_ANCESTOR :
      case OpCodes.FROM_DESCENDANTS_OR_SELF :
      case OpCodes.FROM_DESCENDANTS :
        if(potentialDuplicateMakingStepCount > 0)
            return false;
        potentialDuplicateMakingStepCount++;
      case OpCodes.FROM_ROOT :
      case OpCodes.FROM_CHILDREN :
      case OpCodes.FROM_SELF :
        if(foundWildAttribute)
          return false;
        break;
      default :
      }

      int nextStepOpCodePos = compiler.getNextStepPos(stepOpCodePos);

      if (nextStepOpCodePos < 0)
        break;
              
      stepOpCodePos = nextStepOpCodePos;
    }

    return true;
  }
  
  public static boolean isOneStep(int analysis)
  {
    return (analysis & BITS_COUNT) == 0x00000001;
  }

  public static int getStepCount(int analysis)
  {
    return (analysis & BITS_COUNT);
  }

  /**
   * First 8 bits are the number of top-level location steps.  Hopefully
   *  there will never be more that 255 location steps!!!
   */
  public static final int BITS_COUNT = 0x000000FF;

  /** 4 bits are reserved for future use. */
  public static final int BITS_RESERVED = 0x00000F00;

  /** Bit is on if the expression contains a top-level predicate. */
  public static final int BIT_PREDICATE = (0x00001000);

  /** Bit is on if any of the walkers contain an ancestor step. */
  public static final int BIT_ANCESTOR = (0x00001000 << 1);

  /** Bit is on if any of the walkers contain an ancestor-or-self step. */
  public static final int BIT_ANCESTOR_OR_SELF = (0x00001000 << 2);

  /** Bit is on if any of the walkers contain an attribute step. */
  public static final int BIT_ATTRIBUTE = (0x00001000 << 3);

  /** Bit is on if any of the walkers contain a child step. */
  public static final int BIT_CHILD = (0x00001000 << 4);

  /** Bit is on if any of the walkers contain a descendant step. */
  public static final int BIT_DESCENDANT = (0x00001000 << 5);

  /** Bit is on if any of the walkers contain a descendant-or-self step. */
  public static final int BIT_DESCENDANT_OR_SELF = (0x00001000 << 6);

  /** Bit is on if any of the walkers contain a following step. */
  public static final int BIT_FOLLOWING = (0x00001000 << 7);

  /** Bit is on if any of the walkers contain a following-sibiling step. */
  public static final int BIT_FOLLOWING_SIBLING = (0x00001000 << 8);

  /** Bit is on if any of the walkers contain a namespace step. */
  public static final int BIT_NAMESPACE = (0x00001000 << 9);

  /** Bit is on if any of the walkers contain a parent step. */
  public static final int BIT_PARENT = (0x00001000 << 10);

  /** Bit is on if any of the walkers contain a preceding step. */
  public static final int BIT_PRECEDING = (0x00001000 << 11);

  /** Bit is on if any of the walkers contain a preceding-sibling step. */
  public static final int BIT_PRECEDING_SIBLING = (0x00001000 << 12);

  /** Bit is on if any of the walkers contain a self step. */
  public static final int BIT_SELF = (0x00001000 << 13);

  /**
   * Bit is on if any of the walkers contain a filter (i.e. id(), extension
   *  function, etc.) step.
   */
  public static final int BIT_FILTER = (0x00001000 << 14);

  /** Bit is on if any of the walkers contain a root step. */
  public static final int BIT_ROOT = (0x00001000 << 15);

  /**
   * If any of these bits are on, the expression may likely traverse outside
   *  the given subtree.
   */
                                                                | BIT_PRECEDING_SIBLING
                                                                | BIT_PRECEDING
                                                                | BIT_FOLLOWING_SIBLING
                                                                | BIT_FOLLOWING
                                                                | BIT_ANCESTOR_OR_SELF
                                                                | BIT_ANCESTOR
                                                                | BIT_FILTER
                                                                | BIT_ROOT);

  /**
   * Bit is on if any of the walkers can go backwards in document
   *  order from the context node.
   */
  public static final int BIT_BACKWARDS_SELF = (0x00001000 << 16);

  public static final int BIT_ANY_DESCENDANT_FROM_ROOT = (0x00001000 << 17);

  /**
   * Bit is on if any of the walkers contain an node() test.  This is
   *  really only useful if the count is 1.
   */
  public static final int BIT_NODETEST_ANY = (0x00001000 << 18);


  /** Bit is on if the expression is a match pattern. */
  public static final int BIT_MATCH_PATTERN = (0x00001000 << 19);
}