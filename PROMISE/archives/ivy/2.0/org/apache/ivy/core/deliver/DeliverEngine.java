package org.apache.ivy.core.deliver;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.apache.ivy.core.IvyPatternHelper;
import org.apache.ivy.core.cache.ResolutionCacheManager;
import org.apache.ivy.core.module.descriptor.DependencyDescriptor;
import org.apache.ivy.core.module.descriptor.ModuleDescriptor;
import org.apache.ivy.core.module.id.ModuleRevisionId;
import org.apache.ivy.plugins.parser.xml.UpdateOptions;
import org.apache.ivy.plugins.parser.xml.XmlModuleDescriptorParser;
import org.apache.ivy.plugins.parser.xml.XmlModuleDescriptorUpdater;
import org.apache.ivy.plugins.report.XmlReportParser;
import org.apache.ivy.util.ConfigurationUtils;
import org.apache.ivy.util.Message;
import org.xml.sax.SAXException;

public class DeliverEngine {
    private DeliverEngineSettings settings;

    public DeliverEngine(DeliverEngineSettings settings) {
        this.settings = settings;
    }

    /**
     * Delivers a resolved ivy file based upon last resolve call status. If resolve report file
     * cannot be found in cache, then it throws an IllegalStateException (maybe resolve has not been
     * called before ?).
     * 
     * @param revision
     *            the revision to which the module should be delivered
     * @param destIvyPattern
     *            the pattern to which the delivered ivy file should be written
     * @param options
     *            the options with which deliver should be done
     */
    public void deliver(String revision, String destIvyPattern, DeliverOptions options)
            throws IOException, ParseException {
        String resolveId = options.getResolveId();
        if (resolveId == null) {
            throw new IllegalArgumentException("A resolveId must be specified for delivering.");
        }
        File[] files = getCache().getConfigurationResolveReportsInCache(resolveId);
        if (files.length == 0) {
            throw new IllegalStateException("No previous resolve found for id '" + resolveId
                    + "' Please resolve dependencies before delivering.");
        }
        XmlReportParser parser = new XmlReportParser();
        parser.parse(files[0]);
        ModuleRevisionId mrid = parser.getResolvedModule();
        deliver(mrid, revision, destIvyPattern, options);
    }

    private ResolutionCacheManager getCache() {
        return settings.getResolutionCacheManager();
    }

    /**
     * Delivers a resolved ivy file based upon last resolve call status. If resolve report file
     * cannot be found in cache, then it throws an IllegalStateException (maybe resolve has not been
     * called before ?).
     * 
     * @param mrid
     *            the module revision id of the module to deliver
     * @param revision
     *            the revision to which the module should be delivered
     * @param destIvyPattern
     *            the pattern to which the delivered ivy file should be written
     * @param options
     *            the options with which deliver should be done
     */
    public void deliver(ModuleRevisionId mrid, String revision, String destIvyPattern,
            DeliverOptions options) throws IOException, ParseException {
        Message.info(":: delivering :: " + mrid + " :: " + revision + " :: " + options.getStatus()
                + " :: " + options.getPubdate());
        Message.verbose("\toptions = " + options);
        long start = System.currentTimeMillis();
        destIvyPattern = settings.substitute(destIvyPattern);

        File ivyFile = getCache().getResolvedIvyFileInCache(mrid);
        if (!ivyFile.exists()) {
            throw new IllegalStateException("ivy file not found in cache for " + mrid
                    + ": please resolve dependencies before delivering (" + ivyFile + ")");
        }
        ModuleDescriptor md = null;
        URL ivyFileURL = null;
        try {
            ivyFileURL = ivyFile.toURI().toURL();
            md = XmlModuleDescriptorParser.getInstance().parseDescriptor(settings, ivyFileURL,
                options.isValidate());
            md.setResolvedModuleRevisionId(ModuleRevisionId.newInstance(mrid, 
                options.getPubBranch() == null ? mrid.getBranch() : options.getPubBranch(), 
                revision));
            md.setResolvedPublicationDate(options.getPubdate());
        } catch (MalformedURLException e) {
            throw new RuntimeException("malformed url obtained for file " + ivyFile, e);
        } catch (ParseException e) {
            throw new RuntimeException("bad ivy file in cache for " + mrid
                    + ": please clean and resolve again", e);
        }

        File ivyProperties = getCache().getResolvedIvyPropertiesInCache(mrid);
        if (!ivyProperties.exists()) {
            throw new IllegalStateException("ivy properties not found in cache for " + mrid
                    + ": please resolve dependencies before delivering (" + ivyFile + ")");
        }
        Properties props = new Properties();
        FileInputStream in = new FileInputStream(ivyProperties);
        props.load(in);
        in.close();

        for (Iterator iter = props.keySet().iterator(); iter.hasNext();) {
            String depMridStr = (String) iter.next();
            String[] parts = props.getProperty(depMridStr).split(" ");
            ModuleRevisionId decodedMrid = ModuleRevisionId.decode(depMridStr);
            if (options.isResolveDynamicRevisions()) {
                resolvedRevisions.put(decodedMrid, parts[0]);
            }
            dependenciesStatus.put(decodedMrid, parts[1]);
        }

        DependencyDescriptor[] dependencies = md.getDependencies();
        for (int i = 0; i < dependencies.length; i++) {
            String rev = (String) resolvedRevisions.get(dependencies[i].getDependencyRevisionId());
            if (rev == null) {
                rev = dependencies[i].getDependencyRevisionId().getRevision();
            }
            String depStatus = (String) dependenciesStatus.get(dependencies[i]
                    .getDependencyRevisionId());
            resolvedDependencies.put(dependencies[i].getDependencyRevisionId(), options
                    .getPdrResolver().resolve(
                        md,
                        options.getStatus(),
                        ModuleRevisionId
                                .newInstance(dependencies[i].getDependencyRevisionId(), rev),
                        depStatus));
        }

        File publishedIvy = settings.resolveFile(
                    IvyPatternHelper.substitute(destIvyPattern, md.getResolvedModuleRevisionId()));
        Message.info("\tdelivering ivy file to " + publishedIvy);

        String[] confs = ConfigurationUtils.replaceWildcards(options.getConfs(), md);
        Set confsToRemove = new HashSet(Arrays.asList(md.getConfigurationsNames()));
        confsToRemove.removeAll(Arrays.asList(confs));

        try {
            XmlModuleDescriptorUpdater.update(ivyFileURL, publishedIvy,
                    new UpdateOptions()
                        .setSettings(settings)
                        .setResolvedRevisions(resolvedDependencies)
                        .setStatus(options.getStatus())
                        .setRevision(revision)
                        .setBranch(options.getPubBranch())
                        .setPubdate(options.getPubdate())
                        .setConfsToExclude((String[]) confsToRemove
                            .toArray(new String[confsToRemove.size()])));
        } catch (SAXException ex) {
            throw new RuntimeException("bad ivy file in cache for " + mrid
                    + ": please clean and resolve again", ex);
        }

        Message.verbose("\tdeliver done (" + (System.currentTimeMillis() - start) + "ms)");
    }
}
