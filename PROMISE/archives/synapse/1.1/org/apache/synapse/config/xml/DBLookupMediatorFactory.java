package org.apache.synapse.config.xml;

import org.apache.axiom.om.OMElement;
import org.apache.synapse.Mediator;
import org.apache.synapse.SynapseConstants;
import org.apache.synapse.mediators.db.DBLookupMediator;

import javax.xml.namespace.QName;

/**
 * <dblookup>
 *   <connection>
 *     <pool>
 *      (
 *       <driver/>
 *       <url/>
 *       <user/>
 *       <password/>
 *     |
 *       <dsName/>
 *       <icClass/>
 *       <url/>
 *       <user/>
 *       <password/>
 *     )
 *       <property name="name" value="value"/>*
 *     </pool>
 *   </connection>
 *   <statement>
 *     <sql>select something from table where something_else = ?</sql>
 *     <parameter [value="" | expression=""] type="int|string"/>*
 *     <result name="string" column="int|string"/>*
 *   </statement>+
 * </dblookup>
 */
public class DBLookupMediatorFactory extends AbstractDBMediatorFactory {

    private static final QName DBLOOKUP_Q =
        new QName(SynapseConstants.SYNAPSE_NAMESPACE, "dblookup");

    public Mediator createMediator(OMElement elem) {

        DBLookupMediator mediator = new DBLookupMediator();
        buildDataSource(elem, mediator);
        processStatements(elem, mediator);
        return mediator;
    }

    public QName getTagQName() {
        return DBLOOKUP_Q;
    }
}
