package org.apache.ivy.core.cache;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.Iterator;
import java.util.LinkedHashMap;

import org.apache.ivy.core.module.descriptor.ModuleDescriptor;
import org.apache.ivy.plugins.parser.ParserSettings;
import org.apache.ivy.util.Message;

/**
 * Cache ModuleDescriptors so that when the same module is used twice (in multi-module build for 
 * instance), it is parsed only once.
 * This cache is has a limited size, and keep the most recently used entries.
 * The entry in the cache are invalidated if there is a change to one variable
 * used in the module descriptor.
 */
class ModuleDescriptorMemoryCache {

    private final int maxSize;
    private final LinkedHashMap/*<File,CacheEntry>*/ valueMap;
    
    
    /**
     * Create a cache of the given size
     * @param size
     */
    public ModuleDescriptorMemoryCache(int size) {
        this.maxSize = size;
        this.valueMap = new LinkedHashMap(size);
    }

    public ModuleDescriptor get(File ivyFile, ParserSettings ivySettings, boolean validated,
            ModuleDescriptorProvider mdProvider) throws ParseException, IOException {
        
        ModuleDescriptor descriptor = getFromCache(ivyFile, ivySettings, validated);
        if (descriptor == null) {
            descriptor = getStale(ivyFile, ivySettings, validated, mdProvider);
        }
        return descriptor;
    }

    /**
     * Get the module descriptor from the mdProvider and store it into the cache. 
     */
    public ModuleDescriptor getStale(File ivyFile, ParserSettings ivySettings, boolean validated,
            ModuleDescriptorProvider mdProvider) throws ParseException, IOException {
        ParserSettingsMonitor settingsMonitor = new ParserSettingsMonitor(ivySettings);
        ModuleDescriptor descriptor = mdProvider.provideModule(
            settingsMonitor.getMonitoredSettings() , ivyFile, validated);
        putInCache(ivyFile, settingsMonitor, validated, descriptor);
        return descriptor;
    }

    ModuleDescriptor getFromCache(File ivyFile, ParserSettings ivySettings, boolean validated) {
        if (maxSize <= 0) {
            return null;
        }
        CacheEntry entry = (CacheEntry) valueMap.get(ivyFile);
        if (entry != null) {
            if (entry.isStale(validated, ivySettings)) {
                Message.debug("Entry is found in the ModuleDescriptorCache but entry should be " 
                    + "reevaluated : " + ivyFile);
                valueMap.remove(ivyFile);
                return null;
            } else {
                valueMap.remove(ivyFile);
                valueMap.put(ivyFile, entry);
                Message.debug("Entry is found in the ModuleDescriptorCache : " + ivyFile);
                return entry.md;
            }
        } else {
            Message.debug("No entry is found in the ModuleDescriptorCache : " + ivyFile);
            return null;
        }        
    }

    
 
    void putInCache(File url, ParserSettingsMonitor ivySettingsMonitor, boolean validated, 
            ModuleDescriptor descriptor) {
        if (maxSize <= 0) {
            return;
        }
        if (valueMap.size() >= maxSize) {
            Message.debug("ModuleDescriptorCache is full, remove one entry");
            Iterator it = valueMap.values().iterator();
            it.next();
            it.remove();
        }
        valueMap.put(url, new CacheEntry(descriptor , validated, ivySettingsMonitor));
    }

    
    private static class CacheEntry {
        private final ModuleDescriptor md;
        private final boolean validated;
        private final ParserSettingsMonitor parserSettingsMonitor;

        CacheEntry(ModuleDescriptor md , boolean validated, 
                        ParserSettingsMonitor parserSettingsMonitor) {
            this.md = md;
            this.validated = validated;
            this.parserSettingsMonitor = parserSettingsMonitor;
        }
        
        boolean isStale(boolean validated, ParserSettings newParserSettings) {
            return (validated && !this.validated) 
                    || parserSettingsMonitor.hasChanged(newParserSettings);
        }
    }
    
}
