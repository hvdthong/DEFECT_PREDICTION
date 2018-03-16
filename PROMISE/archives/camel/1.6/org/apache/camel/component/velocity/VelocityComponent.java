package org.apache.camel.component.velocity;

import java.util.Map;

import org.apache.camel.Endpoint;
import org.apache.camel.impl.DefaultComponent;
import org.apache.velocity.app.VelocityEngine;

/**
 * @version $Revision: 641680 $
 */
public class VelocityComponent extends DefaultComponent {
    private VelocityEngine velocityEngine = new VelocityEngine();

    public VelocityEngine getVelocityEngine() {
        return velocityEngine;
    }

    public void setVelocityEngine(VelocityEngine velocityEngine) {
        this.velocityEngine = velocityEngine;
    }

    protected Endpoint createEndpoint(String uri, String remaining, Map parameters) throws Exception {
        return new VelocityEndpoint(uri, this, remaining, parameters);
    }
}
