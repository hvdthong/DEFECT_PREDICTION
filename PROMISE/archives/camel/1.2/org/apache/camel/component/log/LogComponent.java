package org.apache.camel.component.log;

import java.util.Map;

import org.apache.camel.Endpoint;
import org.apache.camel.Exchange;
import org.apache.camel.impl.DefaultComponent;
import org.apache.camel.impl.ProcessorEndpoint;
import org.apache.camel.processor.Logger;
import org.apache.camel.processor.LoggingLevel;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @version $Revision: $
 */
public class LogComponent extends DefaultComponent<Exchange> {
    private static final Log LOG = LogFactory.getLog(LogComponent.class);

    protected Endpoint<Exchange> createEndpoint(String uri, String remaining, Map parameters) throws Exception {
        LoggingLevel level = getLoggingLevel(parameters);
        Logger logger = new Logger(remaining, level);

        return new ProcessorEndpoint(uri, this, logger);
    }

    protected LoggingLevel getLoggingLevel(Map parameters) {
        String levelText = (String) parameters.get("level");
        LoggingLevel level = null;
        if (levelText != null) {
            level = LoggingLevel.valueOf(levelText.toUpperCase());
            if (level == null) {
                LOG.warn("Could not convert level text: " + levelText + " to a valid logging level so defaulting to WARN");
            }
        }
        if (level == null) {
            level = LoggingLevel.INFO;
        }
        return level;
    }
}
