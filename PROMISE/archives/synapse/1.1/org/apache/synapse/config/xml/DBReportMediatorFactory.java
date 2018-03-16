package org.apache.synapse.config.xml;

import org.apache.axiom.om.OMElement;
import org.apache.synapse.Mediator;
import org.apache.synapse.SynapseConstants;
import org.apache.synapse.mediators.db.DBReportMediator;

import javax.xml.namespace.QName;

/**
 * <dbreport>
 *   <connection>
 *     <pool>
 *       (
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
 *     <sql>insert into table values (?, ?, ..)</sql>
 *     <parameter [value="" | expression=""] type="int|string"/>*
 *   </statement>+
 * </dbreport>
 */
public class DBReportMediatorFactory extends AbstractDBMediatorFactory {

    private static final QName DBREPORT_Q =
        new QName(SynapseConstants.SYNAPSE_NAMESPACE, "dbreport");

    public Mediator createMediator(OMElement elem) {
        DBReportMediator mediator = new DBReportMediator();
        buildDataSource(elem, mediator);
        processStatements(elem, mediator);
        return mediator;
    }

    public QName getTagQName() {
        return DBREPORT_Q;
    }
}
