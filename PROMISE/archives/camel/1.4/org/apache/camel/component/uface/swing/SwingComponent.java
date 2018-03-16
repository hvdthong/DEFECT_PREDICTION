package org.apache.camel.component.uface.swing;

import java.util.Map;

import org.apache.camel.Endpoint;
import org.apache.camel.component.uface.UFaceComponent;
import org.ufacekit.ui.swing.databinding.swing.SwingRealm;

/**
 * @version $Revision: 642879 $
 */
public class SwingComponent extends UFaceComponent {
    public SwingComponent() {
        SwingRealm.createDefault();
    }

    protected Endpoint createEndpoint(String uri, String remaining, Map parameters) throws Exception {
        return super.createEndpoint(uri, remaining, parameters);
    }
}
