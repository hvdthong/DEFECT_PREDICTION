package org.apache.camel.component.uface.swing;

import java.awt.*;

import javax.swing.*;

import org.apache.camel.component.uface.UFaceBrowser;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.camel.spring.Main;
import org.ufacekit.ui.UIComposite;
import org.ufacekit.ui.swing.SwingComposite;
import org.ufacekit.ui.swing.SwingFactory;
import org.ufacekit.ui.swing.databinding.swing.SwingRealm;

/**
 * @version $Revision: 642879 $
 */
public class SwingBrowser extends UFaceBrowser {
    static {
        SwingRealm.createDefault();
    }

    public SwingBrowser(DefaultCamelContext camelContext) {
        super(camelContext);
    }

    public void run() {
        JFrame frame = new JFrame();
        frame.setTitle("Camel Browser");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Container container = frame.getContentPane();
        UIComposite composite = new SwingComposite(container, new SwingFactory().newFillLayout());

        createBrowserUI(composite);

        frame.pack();
        frame.setSize(650, 500);
        frame.setVisible(true);
    }
}
