package org.apache.ivy.core.check;

import org.apache.ivy.plugins.parser.ParserSettings;
import org.apache.ivy.plugins.resolver.DependencyResolver;

public interface CheckEngineSettings extends ParserSettings {

    boolean doValidate();

    DependencyResolver getResolver(String resolvername);

}
