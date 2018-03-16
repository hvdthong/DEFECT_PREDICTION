package org.apache.synapse.config.xml;

import org.apache.axiom.om.OMElement;
import org.apache.synapse.Mediator;
import org.apache.synapse.SynapseConstants;
import org.apache.synapse.mediators.db.DBReportMediator;

import javax.xml.namespace.QName;

/**
 * Factory for {@link DBReportMediator} instances.
 * <pre>
 * &lt;dbreport&gt;
 *   &lt;connection&gt;
 *     &lt;pool&gt;
 *      (
 *       &lt;driver/&gt;
 *       &lt;url/&gt;
 *       &lt;user/&gt;
 *       &lt;password/&gt;
 *     |
 *       &lt;dsName/&gt;
 *       &lt;icClass/&gt;
 *       &lt;url/&gt;
 *       &lt;user/&gt;
 *       &lt;password/&gt;
 *     )
 *       &lt;property name="name" value="value"/&gt;*
 *     &lt;/pool&gt;
 *   &lt;/connection&gt;
 *   &lt;statement&gt;
 *     &lt;sql&gt;insert into something values(?, ?, ?, ?)&lt;/sql&gt;
 *     &lt;parameter [value="" | expression=""] type="int|string"/&gt;*
 *    &lt;/statement&gt;+
 * &lt;/dblreport&gt;
 * </pre>
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
