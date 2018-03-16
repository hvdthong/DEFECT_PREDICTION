package org.apache.poi.hssf.eventmodel;

import org.apache.poi.hssf.model.Model;

/**
 * ModelFactoryListener is registered with the 
 * ModelFactory.  It receives Models.
 * 
 * @author Andrew C. Oliver acoliver@apache.org
 */
public interface ModelFactoryListener
{
    /**
     * Process a model.  Called by the ModelFactory
     * @param model to be processed
     * @return abortable - currently ignored (may be implemented in the future)
     */
    public boolean process(Model model);
}
