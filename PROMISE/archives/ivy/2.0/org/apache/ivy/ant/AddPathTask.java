package org.apache.ivy.ant;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.Task;
import org.apache.tools.ant.types.DirSet;
import org.apache.tools.ant.types.FileList;
import org.apache.tools.ant.types.FileSet;
import org.apache.tools.ant.types.Path;
import org.apache.tools.ant.types.Path.PathElement;

/**
 * This task is not directly related to ivy, but is useful in some modular build systems. The idea
 * is to be able to contribute new sub path elements to an existing path.
 */
public class AddPathTask extends Task {
    private String toPath;

    private boolean first = false;

    private Path toAdd;

    public String getTopath() {
        return toPath;
    }

    public void setTopath(String toPath) {
        this.toPath = toPath;
    }

    public void setProject(Project project) {
        super.setProject(project);
        toAdd = new Path(project);
    }

    public void execute() throws BuildException {
        Object element = getProject().getReference(toPath);
        if (element == null) {
            throw new BuildException("destination path not found: " + toPath);
        }
        if (!(element instanceof Path)) {
            throw new BuildException("destination path is not a path: " + element.getClass());
        }
        Path dest = (Path) element;
        if (first) {
            toAdd.append(dest);
            getProject().addReference(toPath, toAdd);
        } else {
            dest.append(toAdd);
        }
    }

    public void add(Path path) throws BuildException {
        toAdd.add(path);
    }

    public void addDirset(DirSet dset) throws BuildException {
        toAdd.addDirset(dset);
    }

    public void addFilelist(FileList fl) throws BuildException {
        toAdd.addFilelist(fl);
    }

    public void addFileset(FileSet fs) throws BuildException {
        toAdd.addFileset(fs);
    }

    public Path createPath() throws BuildException {
        return toAdd.createPath();
    }

    public PathElement createPathElement() throws BuildException {
        return toAdd.createPathElement();
    }

    public boolean isFirst() {
        return first;
    }

    public void setFirst(boolean first) {
        this.first = first;
    }
}
