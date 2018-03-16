package org.apache.ivy.core.install;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Date;

import org.apache.ivy.core.module.descriptor.Configuration;
import org.apache.ivy.core.module.descriptor.DefaultDependencyDescriptor;
import org.apache.ivy.core.module.descriptor.DefaultModuleDescriptor;
import org.apache.ivy.core.module.descriptor.ModuleDescriptor;
import org.apache.ivy.core.module.id.ModuleId;
import org.apache.ivy.core.module.id.ModuleRevisionId;
import org.apache.ivy.core.report.ArtifactDownloadReport;
import org.apache.ivy.core.report.ResolveReport;
import org.apache.ivy.core.resolve.DownloadOptions;
import org.apache.ivy.core.resolve.IvyNode;
import org.apache.ivy.core.resolve.ResolveEngine;
import org.apache.ivy.core.resolve.ResolveOptions;
import org.apache.ivy.core.search.SearchEngine;
import org.apache.ivy.plugins.conflict.NoConflictManager;
import org.apache.ivy.plugins.matcher.ExactPatternMatcher;
import org.apache.ivy.plugins.matcher.MatcherHelper;
import org.apache.ivy.plugins.matcher.PatternMatcher;
import org.apache.ivy.plugins.resolver.DependencyResolver;
import org.apache.ivy.util.Message;

public class InstallEngine {
    private InstallEngineSettings settings;

    private ResolveEngine resolveEngine;

    private SearchEngine searchEngine;

    public InstallEngine(InstallEngineSettings settings, SearchEngine searchEngine,
            ResolveEngine resolveEngine) {
        this.settings = settings;
        this.searchEngine = searchEngine;
        this.resolveEngine = resolveEngine;
    }

    public ResolveReport install(ModuleRevisionId mrid, String from, String to, 
            InstallOptions options) throws IOException {
        DependencyResolver fromResolver = settings.getResolver(from);
        DependencyResolver toResolver = settings.getResolver(to);
        if (fromResolver == null) {
            throw new IllegalArgumentException("unknown resolver " + from
                    + ". Available resolvers are: " + settings.getResolverNames());
        }
        if (toResolver == null) {
            throw new IllegalArgumentException("unknown resolver " + to
                    + ". Available resolvers are: " + settings.getResolverNames());
        }
        PatternMatcher matcher = settings.getMatcher(options.getMatcherName());
        if (matcher == null) {
            throw new IllegalArgumentException("unknown matcher " + options.getMatcherName()
                    + ". Available matchers are: " + settings.getMatcherNames());
        }

        Message.info(":: installing " + mrid + " ::");
        DependencyResolver oldDicator = resolveEngine.getDictatorResolver();
        boolean log = settings.logNotConvertedExclusionRule();
        try {
            settings.setLogNotConvertedExclusionRule(true);
            resolveEngine.setDictatorResolver(fromResolver);

            DefaultModuleDescriptor md = new DefaultModuleDescriptor(ModuleRevisionId.newInstance(
                "apache", "ivy-install", "1.0"), settings.getStatusManager().getDefaultStatus(),
                    new Date());
            String resolveId = ResolveOptions.getDefaultResolveId(md);
            md.addConfiguration(new Configuration("default"));
            md.addConflictManager(new ModuleId(ExactPatternMatcher.ANY_EXPRESSION,
                    ExactPatternMatcher.ANY_EXPRESSION), ExactPatternMatcher.INSTANCE,
                new NoConflictManager());

            if (MatcherHelper.isExact(matcher, mrid)) {
                DefaultDependencyDescriptor dd = new DefaultDependencyDescriptor(md, mrid, false,
                        false, options.isTransitive());
                dd.addDependencyConfiguration("default", "*");
                md.addDependency(dd);
            } else {
                ModuleRevisionId[] mrids = searchEngine.listModules(fromResolver, mrid, matcher);

                for (int i = 0; i < mrids.length; i++) {
                    Message.info("\tfound " + mrids[i] + " to install: adding to the list");
                    DefaultDependencyDescriptor dd = new DefaultDependencyDescriptor(md, mrids[i],
                            false, false, options.isTransitive());
                    dd.addDependencyConfiguration("default", "*");
                    md.addDependency(dd);
                }
            }

            ResolveReport report = new ResolveReport(md, resolveId);

            Message.info(":: resolving dependencies ::");
            ResolveOptions resolveOptions = new ResolveOptions()
                                .setResolveId(resolveId)
                                .setConfs(new String[] {"default"})
                                .setValidate(options.isValidate());
            IvyNode[] dependencies = resolveEngine.getDependencies(md, resolveOptions, report);
            report.setDependencies(Arrays.asList(dependencies), options.getArtifactFilter());

            Message.info(":: downloading artifacts to cache ::");
            resolveEngine.downloadArtifacts(
                report, options.getArtifactFilter(), new DownloadOptions());
            
            Message.info(":: installing in " + to + " ::");
            for (int i = 0; i < dependencies.length; i++) {
                ModuleDescriptor depmd = dependencies[i].getDescriptor();
                if (depmd != null) {
                    ModuleRevisionId depMrid = depmd.getModuleRevisionId();
                    Message.verbose("installing " + depMrid);
                    boolean successfullyPublished = false;
                    try {
                        toResolver.beginPublishTransaction(depMrid, options.isOverwrite());
                        
                        ArtifactDownloadReport[] artifacts = 
                                report.getArtifactsReports(depMrid);
                        for (int j = 0; j < artifacts.length; j++) {
                            if (artifacts[j].getLocalFile() != null) {
                                toResolver.publish(artifacts[j].getArtifact(), 
                                    artifacts[j].getLocalFile(), options.isOverwrite());
                            }
                        }
                        
                        File localIvyFile = dependencies[i]
                                                    .getModuleRevision().getReport().getLocalFile();
                        toResolver.publish(
                            depmd.getMetadataArtifact(), localIvyFile, options.isOverwrite());
                        
                        toResolver.commitPublishTransaction();
                        successfullyPublished  = true;
                    } finally {
                        if (!successfullyPublished) {
                            toResolver.abortPublishTransaction();
                        }
                    }
                }
            }

            Message.info(":: install resolution report ::");

            resolveEngine.outputReport(
                report, settings.getResolutionCacheManager(), resolveOptions);

            return report;
        } finally {
            Message.sumupProblems();

            resolveEngine.setDictatorResolver(oldDicator);
            settings.setLogNotConvertedExclusionRule(log);
        }
    }

}
