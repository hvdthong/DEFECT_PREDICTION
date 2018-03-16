package org.apache.synapse.config.xml;

import org.apache.axiom.om.OMAttribute;
import org.apache.axiom.om.OMElement;
import org.apache.synapse.Mediator;
import org.apache.synapse.SynapseConstants;
import org.apache.synapse.mediators.filters.FilterMediator;
import org.jaxen.JaxenException;

import javax.xml.namespace.QName;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

/**
 * <p>Creates a filter mediator instance with the default behavior</p>
 *
 * <pre>
 * &lt;filter (source="xpath" regex="string") | xpath="xpath"&gt;
 *   mediator+
 * &lt;/filter&gt;
 * </pre>
 *
 * <p>or if the filter medaitor needs to support the else behavior as well (i.e. a set of mediators
 * to be executed when the filter evaluates to false).</p>
 *
 * <pre>
 * &lt;filter (source="xpath" regex="string") | xpath="xpath"&gt;
 *   &lt;then [sequence="string"]&gt;
 *      mediator+
 *   &lt;/then&gt;
 *   &lt;else [sequence="string"]&gt;
 *      mediator+
 *   &lt;/else&gt;
 * &lt;/filter&gt;
 * </pre>
 */
public class FilterMediatorFactory extends AbstractListMediatorFactory {

    private static final QName FILTER_Q = new QName(SynapseConstants.SYNAPSE_NAMESPACE, "filter");
    private static final QName THEN_Q = new QName(SynapseConstants.SYNAPSE_NAMESPACE, "then");
    private static final QName ELSE_Q = new QName(SynapseConstants.SYNAPSE_NAMESPACE, "else");

    public Mediator createMediator(OMElement elem) {
        
        FilterMediator filter = new FilterMediator();

        OMAttribute attXpath  = elem.getAttribute(ATT_XPATH);
        OMAttribute attSource = elem.getAttribute(ATT_SOURCE);
        OMAttribute attRegex  = elem.getAttribute(ATT_REGEX);

        if (attXpath != null) {
            if (attXpath.getAttributeValue() != null &&
                attXpath.getAttributeValue().trim().length() == 0) {

                handleException("Invalid attribute value specified for xpath");

            } else {

                try {
                    filter.setXpath(SynapseXPathFactory.getSynapseXPath(elem, ATT_XPATH));
                } catch (JaxenException e) {
                    handleException("Invalid XPath expression for attribute xpath : "
                        + attXpath.getAttributeValue(), e);
                }
                
            }

        } else if (attSource != null && attRegex != null) {

            if ((attSource.getAttributeValue() != null &&
                attSource.getAttributeValue().trim().length() == 0) || (attRegex.getAttributeValue()
                != null && attRegex.getAttributeValue().trim().length() == 0) ){

                handleException("Invalid attribute values for source and/or regex specified");

            } else {
                
                try {
                    filter.setSource(SynapseXPathFactory.getSynapseXPath(elem, ATT_SOURCE));
                } catch (JaxenException e) {

                    handleException("Invalid XPath expression for attribute source : "
                        + attSource.getAttributeValue(), e);
                }

                try {
                    filter.setRegex(Pattern.compile(attRegex.getAttributeValue()));
                } catch (PatternSyntaxException pse) {

                    handleException("Invalid Regular Expression for attribute regex : "
                        + attRegex.getAttributeValue(), pse);
                }
            }

        } else {

            handleException("An xpath or (source, regex) attributes are required for a filter");
        }

        processTraceState(filter,elem);

        OMElement thenElem = elem.getFirstChildWithName(THEN_Q);

        if (thenElem != null) {

            filter.setThenElementPresent(true);
            OMAttribute sequenceAttr = thenElem.getAttribute(ATT_SEQUENCE);

            if (sequenceAttr != null && sequenceAttr.getAttributeValue() != null) {

                filter.setThenKey(sequenceAttr.getAttributeValue());

            } else {
                addChildren(thenElem, filter);
            }

            OMElement elseElem = elem.getFirstChildWithName(ELSE_Q);

            if (elseElem != null) {

                sequenceAttr = elseElem.getAttribute(ATT_SEQUENCE);

                if (sequenceAttr != null && sequenceAttr.getAttributeValue() != null) {

                    filter.setElseKey(sequenceAttr.getAttributeValue());

                } else {

                    AnonymousListMediator listMediator =
                        AnonymousListMediatorFactory.createAnonymousListMediator(elseElem);
                    filter.setElseMediator(listMediator);
                }
            }

        } else {

            filter.setThenElementPresent(false);
            addChildren(elem, filter);
        }

        return filter;
    }

    public QName getTagQName() {
        return FILTER_Q;
    }
}
