package org.apache.camel.component.uface.swing;

import org.apache.camel.CamelContext;
import org.apache.camel.impl.DefaultCamelContext;

/**
 * @version $Revision: 668391 $
 */
public class Main extends org.apache.camel.spring.Main {

    public static void main(String... args) {
        new Main().run(args);
    }

    protected void postProcesCamelContext(CamelContext camelContext) throws Exception {
        super.postProcesCamelContext(camelContext);

        SwingBrowser browser = new SwingBrowser((DefaultCamelContext) camelContext);
        browser.run();
    }

}
