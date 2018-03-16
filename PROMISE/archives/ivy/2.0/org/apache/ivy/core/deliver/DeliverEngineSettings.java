package org.apache.ivy.core.deliver;

import org.apache.ivy.plugins.parser.ParserSettings;

public interface DeliverEngineSettings extends ParserSettings {

    String substitute(String destIvyPattern);

}
