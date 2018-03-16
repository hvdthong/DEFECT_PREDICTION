package org.apache.synapse.config.xml;

import org.apache.axiom.om.OMElement;
import org.apache.synapse.Mediator;
import org.apache.synapse.SynapseConstants;
import org.apache.synapse.mediators.db.DBLookupMediator;

import javax.xml.namespace.QName;

/**
 * Factory for {@link DBLookupMediator} instances.
 * <p>
 * Configuration syntax:
 * <pre>
 * &lt;dblookup&gt;
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
 *     &lt;sql&gt;select something from table where something_else = ?&lt;/sql&gt;
 *     &lt;parameter [value="" | expression=""] type="int|string"/&gt;*
 *     &lt;result name="string" column="int|string"/&gt;*
 *   &lt;/statement&gt;+
 * &lt;/dblookup&gt;
 * </pre>
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
