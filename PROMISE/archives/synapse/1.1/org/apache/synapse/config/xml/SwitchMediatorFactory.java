package org.apache.synapse.config.xml;

import org.apache.axiom.om.OMAttribute;
import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.xpath.AXIOMXPath;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.synapse.SynapseException;
import org.apache.synapse.Mediator;
import org.apache.synapse.mediators.filters.SwitchMediator;
import org.jaxen.JaxenException;

import javax.xml.namespace.QName;
import java.util.Iterator;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

/**
 * Constructs a Switch mediator instance from the given XML configuration
 * <p/>
 * <pre>
 * &lt;switch source="xpath"&gt;
 *   &lt;case regex="string"&gt;
 *     mediator+
 *   &lt;/case&gt;+
 *   &lt;default&gt;
 *     mediator+
 *   &lt;/default&gt;?
 * &lt;/switch&gt;
 * </pre>
 */
public class SwitchMediatorFactory extends AbstractMediatorFactory {

    private static final QName SWITCH_Q = new QName(XMLConfigConstants.SYNAPSE_NAMESPACE, "switch");
    private static final QName CASE_Q = new QName(XMLConfigConstants.SYNAPSE_NAMESPACE, "case");
    private static final QName DEFAULT_Q = new QName(XMLConfigConstants.SYNAPSE_NAMESPACE, "default");

    public Mediator createMediator(OMElement elem) {

        SwitchMediator switchMediator = new SwitchMediator();
        OMAttribute source = elem.getAttribute(ATT_SOURCE);
        if (source == null) {
            String msg = "A 'source' XPath attribute is required for a switch mediator";
            log.error(msg);
            throw new SynapseException(msg);
        } else {
            try {
                AXIOMXPath sourceXPath = new AXIOMXPath(source.getAttributeValue());
                org.apache.synapse.config.xml.OMElementUtils.addNameSpaces(sourceXPath, elem, log);
                switchMediator.setSource(sourceXPath);

            } catch (JaxenException e) {
                String msg = "Invalid XPath for attribute 'source' : " + source.getAttributeValue();
                log.error(msg);
                throw new SynapseException(msg);
            }
        }
        processTraceState(switchMediator, elem);
        Iterator iter = elem.getChildrenWithName(CASE_Q);
        while (iter.hasNext()) {
            OMElement caseElem = (OMElement) iter.next();
            SwitchCase aCase = new SwitchCase();
            OMAttribute regex = caseElem.getAttribute(ATT_REGEX);
            if (regex == null) {
                String msg = "The 'regex' attribute is required for a switch case definition";
                log.error(msg);
                throw new SynapseException(msg);
            }
            try {
                aCase.setRegex(Pattern.compile(regex.getAttributeValue()));
            } catch (PatternSyntaxException pse) {
                String msg = "Invalid Regular Expression for attribute 'regex' : " + regex.getAttributeValue();
                log.error(msg);
                throw new SynapseException(msg);
            }
            aCase.setCaseMediator(AnonymousListMediatorFactory.createAnonymousListMediator(caseElem));
            switchMediator.addCase(aCase);
        }
        iter = elem.getChildrenWithName(DEFAULT_Q);
        while (iter.hasNext()) {
            SwitchCase aCase = new SwitchCase();
            aCase.setCaseMediator(AnonymousListMediatorFactory.createAnonymousListMediator((OMElement) iter.next()));
            switchMediator.setDefaultCase(aCase);
        }
        return switchMediator;
    }

    public QName getTagQName() {
        return SWITCH_Q;
    }
}
