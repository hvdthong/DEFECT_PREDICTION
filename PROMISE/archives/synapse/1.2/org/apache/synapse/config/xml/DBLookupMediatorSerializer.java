package org.apache.synapse.config.xml;

import org.apache.axiom.om.OMElement;
import org.apache.synapse.Mediator;
import org.apache.synapse.mediators.db.DBLookupMediator;

/**
 * Serializer for {@link DBLookupMediator} instances.
 * 
 * @see DBLookupMediatorSerializer
 */
public class DBLookupMediatorSerializer extends AbstractDBMediatorSerializer {

    public OMElement serializeMediator(OMElement parent, Mediator m) {

        if (!(m instanceof DBLookupMediator)) {
            handleException("Unsupported mediator passed in for serialization : " + m.getType());
        }

        DBLookupMediator mediator = (DBLookupMediator) m;
        OMElement dbLookup = fac.createOMElement("dblookup", synNS);
        saveTracingState(dbLookup,mediator);
        serializeDBInformation(mediator, dbLookup);

        if (parent != null) {
            parent.addChild(dbLookup);
        }
        return dbLookup;
    }

    public String getMediatorClassName() {
        return DBLookupMediator.class.getName();
    }
}
