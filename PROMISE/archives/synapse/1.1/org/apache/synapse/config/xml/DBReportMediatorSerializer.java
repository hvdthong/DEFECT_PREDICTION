package org.apache.synapse.config.xml;

import org.apache.axiom.om.OMElement;
import org.apache.synapse.Mediator;
import org.apache.synapse.mediators.db.DBReportMediator;

/**
 * <dbreport>
 *   <connection>
 *     <jdbc>
 *       <driver/>
 *       <url/>
 *       <user/>
 *       <password/>
 *     </jdbc>
 *   </connection>
 *   <insert>
 *     <sql>insert into table values (?, ?, ..)</sql>
 *     <parameter [property="" | xpath=""] type="int|string"/>*
 *   </insert>+
 * </dbreport>
 */
public class DBReportMediatorSerializer extends AbstractDBMediatorSerializer {

    public OMElement serializeMediator(OMElement parent, Mediator m) {

        if (!(m instanceof DBReportMediator)) {
            handleException("Unsupported mediator passed in for serialization : " + m.getType());
        }

        DBReportMediator mediator = (DBReportMediator) m;
        OMElement dbReport = fac.createOMElement("dbreport", synNS);
        saveTracingState(dbReport,mediator);
        serializeDBInformation(mediator, dbReport);

        if (parent != null) {
            parent.addChild(dbReport);
        }
        return dbReport;
    }

    public String getMediatorClassName() {
        return DBReportMediator.class.getName();
    }
}
