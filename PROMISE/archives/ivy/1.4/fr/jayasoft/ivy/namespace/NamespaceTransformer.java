package fr.jayasoft.ivy.namespace;

import fr.jayasoft.ivy.ModuleRevisionId;

public interface NamespaceTransformer {
    public ModuleRevisionId transform(ModuleRevisionId mrid);

    public boolean isIdentity();
}
