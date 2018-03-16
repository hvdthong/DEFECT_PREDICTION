package org.apache.camel.component.validator.jing;

import java.util.Map;

/**
 * A component for validating the XML payload using
 *
 * @version $Revision: 1.1 $
 */
public class RelaxNGCompactSyntaxComponent extends JingComponent {
    protected void configureValidator(JingValidator validator, String uri, String remaining, Map parameters) throws Exception {
        validator.setCompactSyntax(true);
        super.configureValidator(validator, uri, remaining, parameters);
    }
}
