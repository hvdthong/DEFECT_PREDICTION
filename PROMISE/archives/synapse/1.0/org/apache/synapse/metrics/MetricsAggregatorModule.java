package org.apache.synapse.metrics;

import org.apache.axis2.AxisFault;
import org.apache.axis2.context.ConfigurationContext;
import org.apache.axis2.description.AxisDescription;
import org.apache.axis2.description.AxisModule;
import org.apache.axis2.description.Parameter;
import org.apache.axis2.engine.AxisConfiguration;
import org.apache.axis2.modules.Module;
import org.apache.neethi.Assertion;
import org.apache.neethi.Policy;
/*
 * 
 */

public class MetricsAggregatorModule implements Module {
    public void init(ConfigurationContext configContext, AxisModule module) throws AxisFault {
        AxisConfiguration axiConfiguration = configContext.getAxisConfiguration();

        Counter globalRequestCounter = new Counter();
        Parameter globalRequestCounterParameter = new Parameter();
        globalRequestCounterParameter.setName(Constants.GLOBAL_REQUEST_COUNTER);
        globalRequestCounterParameter.setValue(globalRequestCounter);
        axiConfiguration.addParameter(globalRequestCounterParameter);

    }

    public void engageNotify(AxisDescription axisDescription) throws AxisFault {}

    public boolean canSupportAssertion(Assertion assertion) {
        return false;
    }

    public void applyPolicy(Policy policy, AxisDescription axisDescription) throws AxisFault {
    }

    public void shutdown(ConfigurationContext configurationContext) throws AxisFault {}
}
