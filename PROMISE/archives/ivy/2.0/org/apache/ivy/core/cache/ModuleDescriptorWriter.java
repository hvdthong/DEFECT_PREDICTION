package org.apache.ivy.core.cache;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;

import org.apache.ivy.core.module.descriptor.ModuleDescriptor;
import org.apache.ivy.plugins.resolver.util.ResolvedResource;

public interface ModuleDescriptorWriter {
    public void write(ResolvedResource originalMdResource, ModuleDescriptor md, 
            File src, File dest) throws IOException, ParseException;
}
