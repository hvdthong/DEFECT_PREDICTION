package org.apache.xalan.templates;

import java.util.Vector;

import javax.xml.transform.TransformerException;

/**
 * Represents a stylesheet that has methods that resolve includes and
 * imports.  It has methods on it that
 * return "composed" properties, which mean that:
 * <ol>
 * <li>Properties that are aggregates, like OutputProperties, will
 * be composed of properties declared in this stylsheet and all
 * included stylesheets.</li>
 * <li>Properties that aren't found, will be searched for first in
 * the includes, and, if none are located, will be searched for in
 * the imports.</li>
 * <li>Properties in that are not atomic on a stylesheet will
 * have the form getXXXComposed. Some properties, like version and id,
 * are not inherited, and so won't have getXXXComposed methods.</li>
 * </ol>
 * <p>In some cases getXXXComposed methods may calculate the composed
 * values dynamically, while in other cases they may store the composed
 * values.</p>
 */
public class StylesheetComposed extends Stylesheet
{
    static final long serialVersionUID = -3444072247410233923L;

  /**
   * Uses an XSL stylesheet document.
   * @param parent  The including or importing stylesheet.
   */
  public StylesheetComposed(Stylesheet parent)
  {
    super(parent);
  }

  /**
   * Tell if this can be cast to a StylesheetComposed, meaning, you
   * can ask questions from getXXXComposed functions.
   *
   * @return True since this is a StylesheetComposed 
   */
  public boolean isAggregatedType()
  {
    return true;
  }

  /**
   * Adds all recomposable values for this precedence level into the recomposableElements Vector
   * that was passed in as the first parameter.  All elements added to the
   * recomposableElements vector should extend ElemTemplateElement.
   * @param recomposableElements a Vector of ElemTemplateElement objects that we will add all of
   *        our recomposable objects to.
   */
  public void recompose(Vector recomposableElements) throws TransformerException
  {



    int n = getIncludeCountComposed();

    for (int i = -1; i < n; i++)
    {
      Stylesheet included = getIncludeComposed(i);


      int s = included.getOutputCount();
      for (int j = 0; j < s; j++)
      {
        recomposableElements.addElement(included.getOutput(j));
      }


      s = included.getAttributeSetCount();
      for (int j = 0; j < s; j++)
      {
        recomposableElements.addElement(included.getAttributeSet(j));
      }


      s = included.getDecimalFormatCount();
      for (int j = 0; j < s; j++)
      {
        recomposableElements.addElement(included.getDecimalFormat(j));
      }


      s = included.getKeyCount();
      for (int j = 0; j < s; j++)
      {
        recomposableElements.addElement(included.getKey(j));
      }


      s = included.getNamespaceAliasCount();
      for (int j = 0; j < s; j++)
      {
        recomposableElements.addElement(included.getNamespaceAlias(j));
      }


      s = included.getTemplateCount();
      for (int j = 0; j < s; j++)
      {
        recomposableElements.addElement(included.getTemplate(j));
      }


      s = included.getVariableOrParamCount();
      for (int j = 0; j < s; j++)
      {
        recomposableElements.addElement(included.getVariableOrParam(j));
      }


      s = included.getStripSpaceCount();
      for (int j = 0; j < s; j++)
      {
        recomposableElements.addElement(included.getStripSpace(j));
      }

      s = included.getPreserveSpaceCount();
      for (int j = 0; j < s; j++)
      {
        recomposableElements.addElement(included.getPreserveSpace(j));
      }
    }
  }

  /** Order in import chain.
   *  @serial         */
  private int m_importNumber = -1;

  /** The precedence of this stylesheet in the global import list.
   *  The lowest precedence stylesheet is 0.  A higher
   *  number has a higher precedence.
   *  @serial
   */
  private int m_importCountComposed;
  
  /* The count of imports composed for this stylesheet */
  private int m_endImportCountComposed;

  /**
   * Recalculate the precedence of this stylesheet in the global
   * import list.  The lowest precedence stylesheet is 0.  A higher
   * number has a higher precedence.
   */
  void recomposeImports()
  {

    m_importNumber = getStylesheetRoot().getImportNumber(this);

    StylesheetRoot root = getStylesheetRoot();
    int globalImportCount = root.getGlobalImportCount();

    m_importCountComposed = (globalImportCount - m_importNumber) - 1;
    
    int count = getImportCount();
    if ( count > 0)
    {
      m_endImportCountComposed += count;
      while (count > 0)
        m_endImportCountComposed += this.getImport(--count).getEndImportCountComposed();
    }
    
    count = getIncludeCountComposed();
    while (count>0)
    {
      int imports = getIncludeComposed(--count).getImportCount();
      m_endImportCountComposed += imports;
      while (imports > 0)
        m_endImportCountComposed +=getIncludeComposed(count).getImport(--imports).getEndImportCountComposed();
     
    }                                                            
  }

  /**
   * Get a stylesheet from the "import" list.
   *
   * @param i Index of stylesheet in import list 
   *
   * @return The stylesheet at the given index
   *
   * @throws ArrayIndexOutOfBoundsException
   */
  public StylesheetComposed getImportComposed(int i)
          throws ArrayIndexOutOfBoundsException
  {

    StylesheetRoot root = getStylesheetRoot();

    return root.getGlobalImport(1 + m_importNumber + i);
  }

  /**
   * Get the precedence of this stylesheet in the global import list.
   * The lowest precedence is 0.  A higher number has a higher precedence.
   *
   * @return the precedence of this stylesheet in the global import list.
   */
  public int getImportCountComposed()
  {
    return m_importCountComposed;
  }
  
  /**
   * Get the number of import in this stylesheet's composed list.
   *
   * @return the number of imports in this stylesheet's composed list.
   */
  public int getEndImportCountComposed()
  {
    return m_endImportCountComposed;
  }
  

  /**
   * The combined list of includes.
   * @serial
   */
  private transient Vector m_includesComposed;

  /**
   * Recompose the value of the composed include list.  Builds a composite
   * list of all stylesheets included by this stylesheet to any depth.
   *
   * @param including Stylesheet to recompose
   */
  void recomposeIncludes(Stylesheet including)
  {

    int n = including.getIncludeCount();

    if (n > 0)
    {
      if (null == m_includesComposed)
        m_includesComposed = new Vector();

      for (int i = 0; i < n; i++)
      {
        Stylesheet included = including.getInclude(i);
        m_includesComposed.addElement(included);
        recomposeIncludes(included);
      }
    }
  }

  /**
   * Get an "xsl:include" property.
   *
   * @param i Index of stylesheet in "include" list 
   *
   * @return The stylesheet at the given index in the "include" list 
   *
   * @throws ArrayIndexOutOfBoundsException
   */
  public Stylesheet getIncludeComposed(int i)
          throws ArrayIndexOutOfBoundsException
  {

    if (-1 == i)
      return this;

    if (null == m_includesComposed)
      throw new ArrayIndexOutOfBoundsException();

    return (Stylesheet) m_includesComposed.elementAt(i);
  }

  /**
   * Get the number of included stylesheets.
   *
   * @return the number of included stylesheets.
   */
  public int getIncludeCountComposed()
  {
    return (null != m_includesComposed) ? m_includesComposed.size() : 0;
  }

  /**
   * For compilation support, we need the option of overwriting
   * (rather than appending to) previous composition.
   * We could phase out the old API in favor of this one, but I'm
   * holding off until we've made up our minds about compilation.
   * ADDED 9/5/2000 to support compilation experiment.
   * NOTE: GLP 29-Nov-00 I've left this method in so that CompilingStylesheetHandler will compile.  However,
   *                     I'm not sure why it's needed or what it does and I've commented out the body.
   *
   * @param flushFirst Flag indicating the option of overwriting
   * (rather than appending to) previous composition.
   *
   * @throws TransformerException
   */
  public void recomposeTemplates(boolean flushFirst) throws TransformerException
  {
/***************************************  KEEP METHOD IN FOR COMPILATION
    if (flushFirst)
      m_templateList = new TemplateList(this);

    recomposeTemplates();
*****************************************/
  }
}
