package org.apache.ivy.tools.analyser;

import org.apache.ivy.core.module.descriptor.ModuleDescriptor;

public interface DependencyAnalyser {
    public ModuleDescriptor[] analyze(JarModule[] modules);
}
