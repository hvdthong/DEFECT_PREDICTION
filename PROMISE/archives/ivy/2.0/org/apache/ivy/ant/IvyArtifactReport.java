package org.apache.ivy.ant;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.sax.SAXTransformerFactory;
import javax.xml.transform.sax.TransformerHandler;
import javax.xml.transform.stream.StreamResult;

import org.apache.ivy.core.cache.ArtifactOrigin;
import org.apache.ivy.core.cache.RepositoryCacheManager;
import org.apache.ivy.core.module.descriptor.Artifact;
import org.apache.ivy.core.module.descriptor.ModuleDescriptor;
import org.apache.ivy.core.module.id.ModuleRevisionId;
import org.apache.ivy.core.report.ArtifactDownloadReport;
import org.apache.ivy.core.resolve.IvyNode;
import org.apache.ivy.core.resolve.ResolveOptions;
import org.apache.ivy.core.resolve.ResolvedModuleRevision;
import org.apache.ivy.core.retrieve.RetrieveOptions;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Project;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

/**
 * Generates a report of all artifacts involved during the last resolve.
 */
public class IvyArtifactReport extends IvyPostResolveTask {
    private File tofile;

    private String pattern;

    public File getTofile() {
        return tofile;
    }

    public void setTofile(File aFile) {
        tofile = aFile;
    }

    public String getPattern() {
        return pattern;
    }

    public void setPattern(String aPattern) {
        pattern = aPattern;
    }

    public void doExecute() throws BuildException {
        prepareAndCheck();
        if (tofile == null) {
            throw new BuildException(
                    "no destination file name: please provide it through parameter 'tofile'");
        }

        pattern = getProperty(pattern, getSettings(), "ivy.retrieve.pattern");

        try {
            String[] confs = splitConfs(getConf());
            ModuleDescriptor md = null;
            if (getResolveId() != null) {
                md = (ModuleDescriptor) getResolvedDescriptor(getResolveId());
            } else {
                md = (ModuleDescriptor) getResolvedDescriptor(getOrganisation(), getModule()
                        , false);
            }
            IvyNode[] dependencies = getIvyInstance().getResolveEngine().getDependencies(
                md,
                new ResolveOptions().setConfs(confs).setResolveId(
                    getResolveId()).setValidate(doValidate(getSettings())), null);

            Map artifactsToCopy = getIvyInstance().getRetrieveEngine().determineArtifactsToCopy(
                ModuleRevisionId.newInstance(getOrganisation(), getModule(), getRevision()),
                pattern,
                new RetrieveOptions().setConfs(confs).setResolveId(getResolveId()));

            Map moduleRevToArtifactsMap = new HashMap();
            for (Iterator iter = artifactsToCopy.keySet().iterator(); iter.hasNext();) {
                ArtifactDownloadReport artifact = (ArtifactDownloadReport) iter.next();
                Set moduleRevArtifacts = (Set) moduleRevToArtifactsMap.get(artifact.getArtifact()
                        .getModuleRevisionId());
                if (moduleRevArtifacts == null) {
                    moduleRevArtifacts = new HashSet();
                    moduleRevToArtifactsMap.put(
                        artifact.getArtifact().getModuleRevisionId(), moduleRevArtifacts);
                }
                moduleRevArtifacts.add(artifact);
            }

            generateXml(dependencies, moduleRevToArtifactsMap, artifactsToCopy);
        } catch (ParseException e) {
            log(e.getMessage(), Project.MSG_ERR);
            throw new BuildException("syntax errors in ivy file: " + e, e);
        } catch (IOException e) {
            throw new BuildException("impossible to generate report: " + e, e);
        }
    }

    private void generateXml(IvyNode[] dependencies,
            Map moduleRevToArtifactsMap, Map artifactsToCopy) {
        try {
            FileOutputStream fileOuputStream = new FileOutputStream(tofile);
            try {
                TransformerHandler saxHandler = createTransformerHandler(fileOuputStream);

                saxHandler.startDocument();
                saxHandler.startElement(null, "modules", "modules", new AttributesImpl());

                for (int i = 0; i < dependencies.length; i++) {
                    IvyNode dependency = dependencies[i];
                    if (dependency.getModuleRevision() == null 
                            || dependency.isCompletelyEvicted()) {
                        continue;
                    }

                    startModule(saxHandler, dependency);

                    Set artifactsOfModuleRev = (Set) moduleRevToArtifactsMap.get(dependency
                            .getModuleRevision().getId());
                    if (artifactsOfModuleRev != null) {
                        for (Iterator iter = artifactsOfModuleRev.iterator(); iter.hasNext();) {
                            ArtifactDownloadReport artifact = (ArtifactDownloadReport) iter.next();
                            
                            RepositoryCacheManager cache = dependency.getModuleRevision()
                                .getArtifactResolver().getRepositoryCacheManager();

                            startArtifact(saxHandler, artifact.getArtifact());

                            writeOriginLocationIfPresent(cache, saxHandler, artifact);

                            writeCacheLocation(cache, saxHandler, artifact);

                            Set artifactDestPaths = (Set) artifactsToCopy.get(artifact);
                            for (Iterator iterator = artifactDestPaths.iterator(); iterator
                                    .hasNext();) {
                                String artifactDestPath = (String) iterator.next();
                                writeRetrieveLocation(saxHandler, artifactDestPath);
                            }
                            saxHandler.endElement(null, "artifact", "artifact");
                        }
                    }
                    saxHandler.endElement(null, "module", "module");
                }
                saxHandler.endElement(null, "modules", "modules");
                saxHandler.endDocument();
            } finally {
                fileOuputStream.close();
            }
        } catch (SAXException e) {
            throw new BuildException("impossible to generate report", e);
        } catch (TransformerConfigurationException e) {
            throw new BuildException("impossible to generate report", e);
        } catch (IOException e) {
            throw new BuildException("impossible to generate report", e);
        }
    }

    private TransformerHandler createTransformerHandler(FileOutputStream fileOuputStream)
            throws TransformerFactoryConfigurationError, TransformerConfigurationException,
            SAXException {
        SAXTransformerFactory transformerFact = (SAXTransformerFactory) SAXTransformerFactory
                .newInstance();
        TransformerHandler saxHandler = transformerFact.newTransformerHandler();
        saxHandler.getTransformer().setOutputProperty(OutputKeys.ENCODING, "UTF-8");
        saxHandler.getTransformer().setOutputProperty(OutputKeys.INDENT, "yes");
        saxHandler.setResult(new StreamResult(fileOuputStream));
        return saxHandler;
    }

    private void startModule(TransformerHandler saxHandler, IvyNode dependency) 
            throws SAXException {
        AttributesImpl moduleAttrs = new AttributesImpl();
        moduleAttrs.addAttribute(null, "organisation", "organisation", "CDATA", dependency
                .getModuleId().getOrganisation());
        moduleAttrs.addAttribute(null, "name", "name", "CDATA", dependency.getModuleId().getName());
        ResolvedModuleRevision moduleRevision = dependency.getModuleRevision();
        moduleAttrs.addAttribute(null, "rev", "rev", "CDATA", moduleRevision
                .getId().getRevision());
        moduleAttrs.addAttribute(null, "status", "status", "CDATA", moduleRevision
                .getDescriptor().getStatus());
        saxHandler.startElement(null, "module", "module", moduleAttrs);
    }

    private void startArtifact(TransformerHandler saxHandler, Artifact artifact)
            throws SAXException {
        AttributesImpl artifactAttrs = new AttributesImpl();
        artifactAttrs.addAttribute(null, "name", "name", "CDATA", artifact.getName());
        artifactAttrs.addAttribute(null, "ext", "ext", "CDATA", artifact.getExt());
        artifactAttrs.addAttribute(null, "type", "type", "CDATA", artifact.getType());
        saxHandler.startElement(null, "artifact", "artifact", artifactAttrs);
    }

    private void writeOriginLocationIfPresent(
            RepositoryCacheManager cache, TransformerHandler saxHandler, 
            ArtifactDownloadReport artifact) 
            throws IOException, SAXException {
        ArtifactOrigin origin = artifact.getArtifactOrigin();
        if (!ArtifactOrigin.isUnknown(origin)) {
            String originName = origin.getLocation();
            boolean isOriginLocal = origin.isLocal();

            String originLocation;
            AttributesImpl originLocationAttrs = new AttributesImpl();
            if (isOriginLocal) {
                originLocationAttrs.addAttribute(null, "is-local", "is-local", "CDATA", "true");
                originLocation = originName.replace('\\', '/');
            } else {
                originLocationAttrs.addAttribute(null, "is-local", "is-local", "CDATA", "false");
                originLocation = originName;
            }
            saxHandler
                    .startElement(null, "origin-location", "origin-location", originLocationAttrs);
            char[] originLocationAsChars = originLocation.toCharArray();
            saxHandler.characters(originLocationAsChars, 0, originLocationAsChars.length);
            saxHandler.endElement(null, "origin-location", "origin-location");
        }
    }

    private void writeCacheLocation(RepositoryCacheManager cache, TransformerHandler saxHandler,
            ArtifactDownloadReport artifact) throws SAXException {
        File archiveInCache = artifact.getLocalFile();

        saxHandler.startElement(null, "cache-location", "cache-location", new AttributesImpl());
        char[] archiveInCacheAsChars = archiveInCache.getPath().replace('\\', '/').toCharArray();
        saxHandler.characters(archiveInCacheAsChars, 0, archiveInCacheAsChars.length);
        saxHandler.endElement(null, "cache-location", "cache-location");
    }

    private void writeRetrieveLocation(TransformerHandler saxHandler, String artifactDestPath)
            throws SAXException {
        artifactDestPath = removeLeadingPath(getProject().getBaseDir(), new File(artifactDestPath));

        saxHandler.startElement(null, "retrieve-location", "retrieve-location",
            new AttributesImpl());
        char[] artifactDestPathAsChars = artifactDestPath.replace('\\', '/').toCharArray();
        saxHandler.characters(artifactDestPathAsChars, 0, artifactDestPathAsChars.length);
        saxHandler.endElement(null, "retrieve-location", "retrieve-location");
    }

    public String removeLeadingPath(File leading, File path) {
        String l = leading.getAbsolutePath();
        String p = path.getAbsolutePath();
        if (l.equals(p)) {
            return "";
        }

        if (!l.endsWith(File.separator)) {
            l += File.separator;
        }
        return (p.startsWith(l)) ? p.substring(l.length()) : p;
    }

}