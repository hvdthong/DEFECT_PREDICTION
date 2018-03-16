package org.apache.synapse.config.xml;

import org.apache.axiom.om.OMElement;
import org.apache.synapse.Mediator;
import org.apache.synapse.mediators.filters.SwitchMediator;

import java.util.Iterator;

/**
 * Factory for {@link SwitchMediator} instances.
 * 
 * @see SwitchMediatorFactory
 */
public class SwitchMediatorSerializer extends AbstractMediatorSerializer {

    public OMElement serializeMediator(OMElement parent, Mediator m) {

        if (!(m instanceof SwitchMediator)) {
            handleException("Unsupported mediator passed in for serialization : " + m.getType());
        }

        SwitchMediator mediator = (SwitchMediator) m;
        OMElement switchMed = fac.createOMElement("switch", synNS);
        saveTracingState(switchMed, mediator);

        if (mediator.getSource() != null) {
            SynapseXPathSerializer.serializeXPath(mediator.getSource(), switchMed, "source");

        } else {
            handleException("Invalid switch mediator. Source required");
        }

        Iterator iter = mediator.getCases().iterator();
        while (iter.hasNext()) {
            OMElement caseElem = fac.createOMElement("case", synNS);
            SwitchCase aCase = ((SwitchCase) iter.next());
            if (aCase.getRegex() != null) {
                caseElem.addAttribute(fac.createOMAttribute(
                        "regex", nullNS, aCase.getRegex().pattern()));
            } else {
                handleException("Invalid switch case. Regex required");
            }
            AnonymousListMediator caseMediator = aCase.getCaseMediator();
            if (caseMediator != null) {
                new AnonymousListMediatorSerializer().serializeMediator(
                        caseElem, caseMediator);
                switchMed.addChild(caseElem);
            }
        }
        SwitchCase defaultCase = mediator.getDefaultCase();
        if (defaultCase != null) {
            OMElement caseDefaultElem = fac.createOMElement("default", synNS);
            AnonymousListMediator caseDefaultMediator = defaultCase.getCaseMediator();
            if (caseDefaultMediator != null) {
                new AnonymousListMediatorSerializer().serializeMediator(
                        caseDefaultElem, caseDefaultMediator);
                switchMed.addChild(caseDefaultElem);
            }
        }
        if (parent != null) {
            parent.addChild(switchMed);
        }
        return switchMed;
    }

    public String getMediatorClassName() {
        return SwitchMediator.class.getName();
    }
}
