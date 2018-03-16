package org.apache.xalan.processor;

import org.apache.xalan.res.XSLTErrorResources;

/**
 * This class processes parse events for an xsl:import element.
 * 
 * @xsl.usage internal
 */
public class ProcessorImport extends ProcessorInclude
{
    static final long serialVersionUID = -8247537698214245237L;

  /**
   * Get the stylesheet type associated with an imported stylesheet
   *
   * @return the type of the stylesheet
   */
  protected int getStylesheetType()
  {
    return StylesheetHandler.STYPE_IMPORT;
  }

  /**
   * Get the error number associated with this type of stylesheet importing itself
   *
   * @return the appropriate error number
   */
  protected String getStylesheetInclErr()
  {
    return XSLTErrorResources.ER_IMPORTING_ITSELF;
  }

}
