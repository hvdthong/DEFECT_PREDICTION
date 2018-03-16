package org.apache.ivy.plugins.namespace;

import org.apache.ivy.core.module.id.ModuleRevisionId;

public interface NamespaceTransformer {
    public ModuleRevisionId transform(ModuleRevisionId mrid);

    public boolean isIdentity();
}
