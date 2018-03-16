package org.apache.ivy.ant;

import java.io.File;
import java.net.MalformedURLException;
import java.text.ParseException;

import org.apache.ivy.core.module.descriptor.ModuleDescriptor;
import org.apache.ivy.plugins.parser.m2.PomModuleDescriptorParser;
import org.apache.ivy.plugins.repository.url.URLResource;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Project;

/**
 * Convert a pom to an ivy file
 */
public class IvyConvertPom extends IvyTask {
    private File pomFile = null;

    private File ivyFile = null;

    public File getPomFile() {
        return pomFile;
    }

    public void setPomFile(File file) {
        pomFile = file;
    }

    public File getIvyFile() {
        return ivyFile;
    }

    public void setIvyFile(File ivyFile) {
        this.ivyFile = ivyFile;
    }

    public void doExecute() throws BuildException {
        try {
            if (pomFile == null) {
                throw new BuildException("source pom file is required for convertpom task");
            }
            if (ivyFile == null) {
                throw new BuildException("destination ivy file is required for convertpom task");
            }
            ModuleDescriptor md = PomModuleDescriptorParser.getInstance().parseDescriptor(
                getSettings(), pomFile.toURI().toURL(), false);
            PomModuleDescriptorParser.getInstance().toIvyFile(pomFile.toURI().toURL().openStream(),
                new URLResource(pomFile.toURI().toURL()), getIvyFile(), md);
        } catch (MalformedURLException e) {
            throw new BuildException("unable to convert given pom file to url: " + pomFile + ": "
                    + e, e);
        } catch (ParseException e) {
            log(e.getMessage(), Project.MSG_ERR);
            throw new BuildException("syntax errors in pom file " + pomFile + ": " + e, e);
        } catch (Exception e) {
            throw new BuildException("impossible convert given pom file to ivy file: " + e
                    + " from=" + pomFile + " to=" + ivyFile, e);
        }
    }
}
