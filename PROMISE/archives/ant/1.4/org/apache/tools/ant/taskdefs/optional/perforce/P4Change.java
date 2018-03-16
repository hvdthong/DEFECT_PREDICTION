package org.apache.tools.ant.taskdefs.optional.perforce;

import java.io.*;

import org.apache.tools.ant.*;

/** P4Change - grab a new changelist from Perforce.
 *
 * P4Change creates a new changelist in perforce. P4Change sets the property
 * ${p4.change} with the new changelist number. This should then be passed into
 * p4edit and p4submit.
 *
 * @see P4Edit
 * @see P4Submit
 * @author <A HREF="mailto:leslie.hughes@rubus.com">Les Hughes</A>
 *
 */
public class P4Change extends P4Base {

    protected String emptyChangeList = null;
    protected String description = "AutoSubmit By Ant";

    public void execute() throws BuildException {

        if(emptyChangeList == null) emptyChangeList = getEmptyChangeList();
        final Project myProj = project;

        P4Handler handler = new P4HandlerAdapter() {
                public void process(String line) {
                    if (util.match("/Change/", line)) {
                    
                                
                        int changenumber = Integer.parseInt(line);
                        log("Change Number is "+changenumber, Project.MSG_INFO);
                        myProj.setProperty("p4.change", ""+changenumber);

                    } else if(util.match("/error/", line)) {
                        throw new BuildException("Perforce Error, check client settings and/or server");
                    }
                                
                }};

        handler.setOutput(emptyChangeList);

        execP4Command("change -i", handler);
    }


    public String getEmptyChangeList() throws BuildException {
        final StringBuffer stringbuf = new StringBuffer();
        
        execP4Command("change -o", new P4HandlerAdapter() {
                public void process(String line) {
                    if(!util.match("/^#/",line)){
                        if(util.match("/error/", line)) {
                                
                            log("Client Error", Project.MSG_VERBOSE);
                            throw new BuildException("Perforce Error, check client settings and/or server");
                                    
                        } else if(util.match("/<enter description here>/",line)) {

                            line = util.substitute("s/<enter description here>/" + description + "/", line);
                                        
                            return;
                        }
                                    
                        stringbuf.append(line);
                        stringbuf.append("\n");
                                
                    }
                }});
                
        return stringbuf.toString();
    }

    /* Set Description Variable. */
    public void setDescription(String desc){
        this.description = desc;
    }

