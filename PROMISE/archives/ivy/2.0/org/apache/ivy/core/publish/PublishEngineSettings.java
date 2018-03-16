package org.apache.ivy.core.publish;

import org.apache.ivy.plugins.parser.ParserSettings;
import org.apache.ivy.plugins.resolver.DependencyResolver;

/** 
 * Settings specific to the publish engine
 */
public interface PublishEngineSettings extends ParserSettings {

    String substitute(String srcIvyPattern);

    DependencyResolver getResolver(String resolverName);

}
