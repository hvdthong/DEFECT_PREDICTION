package org.apache.synapse.config.xml;

import org.apache.axiom.om.OMAttribute;
import org.apache.axiom.om.OMElement;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.synapse.Mediator;
import org.apache.synapse.mediators.builtin.LogMediator;

import javax.xml.namespace.QName;

/**
 * Created a Log mediator that logs messages using commons-logging.
 *
 * <pre>
 * &lt;log [level="simple|headers|full|custom"]&gt;
 *      &lt;property&gt; *
 * &lt;/log&gt;
 * </pre>
 */
public class LogMediatorFactory extends AbstractMediatorFactory  {

    private static final Log log = LogFactory.getLog(LogMediatorFactory.class);

    private static final QName LOG_Q    = new QName(Constants.SYNAPSE_NAMESPACE, "log");
    private static final String SIMPLE  = "simple";
    private static final String HEADERS = "headers";
    private static final String FULL    = "full";
    private static final String CUSTOM  = "custom";

    public QName getTagQName() {
        return LOG_Q;
    }

    public Mediator createMediator(OMElement elem) {

        LogMediator logMediator = new LogMediator();

        initMediator(logMediator,elem);
        
        OMAttribute level = elem.getAttribute(new QName(Constants.NULL_NAMESPACE, "level"));
        if (level != null) {
            String levelstr = level.getAttributeValue();
            if (SIMPLE.equals(levelstr)) {
                logMediator.setLogLevel(LogMediator.SIMPLE);
            } else if (HEADERS.equals(levelstr)) {
                logMediator.setLogLevel(LogMediator.HEADERS);
            } else if (FULL.equals(levelstr)) {
                logMediator.setLogLevel(LogMediator.FULL);
            } else if (CUSTOM.equals(levelstr)) {
                logMediator.setLogLevel(LogMediator.CUSTOM);
            }
        }

        OMAttribute separator = elem.getAttribute(new QName(Constants.NULL_NAMESPACE, "separator"));
        if (separator != null) {
            logMediator.setSeparator(separator.getAttributeValue());
        }

        logMediator.addAllProperties(MediatorPropertyFactory.getMediatorProperties(elem));

        return logMediator;
    }
}
