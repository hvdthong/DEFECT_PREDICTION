package org.apache.ivy.core.cache;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;

import org.apache.ivy.core.module.descriptor.ModuleDescriptor;
import org.apache.ivy.plugins.parser.ParserSettings;

interface ModuleDescriptorProvider {

    public ModuleDescriptor provideModule(ParserSettings ivySettings, File descriptorFile,
            boolean validate) throws ParseException, IOException;
}
