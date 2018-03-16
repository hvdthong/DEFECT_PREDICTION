package org.apache.tools.ant.taskdefs.optional.perforce;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Project;
import org.apache.oro.text.perl.Perl5Util;

import java.util.ArrayList;

/**
 * FStatP4OutputHandler  - spezialied Perforce output handler
 * able to sort files recognized as managed by Perforce and files not
 * managed by Perforce in the output
 *
 */
class FStatP4OutputHandler extends P4HandlerAdapter {
    private P4Fstat parent;
    private ArrayList existing = new ArrayList();
    private ArrayList nonExisting = new ArrayList();
    private static Perl5Util util = new Perl5Util();

    public FStatP4OutputHandler(P4Fstat parent) {
        this.parent = parent;
    }

    public void process(String line) throws BuildException {
        if (util.match("/^... clientFile (.+)$/", line)) {
            String f = util.group(1);
            existing.add(f);
        } else if (util.match("/^(.+) - no such file/", line)) {
            String f = util.group(1);
            nonExisting.add(f);
        }
                   Project.MSG_VERBOSE);
    }

    public ArrayList getExisting() {
        return existing;
    }

    public ArrayList getNonExisting() {
        return nonExisting;
    }
}
