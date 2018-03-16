package org.apache.tools.ant.taskdefs.optional.pvcs;

import java.io.*;
import java.util.Enumeration;
import java.util.Vector;
import java.text.*;
import java.util.Random;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.taskdefs.Execute;
import org.apache.tools.ant.taskdefs.LogOutputStream;
import org.apache.tools.ant.taskdefs.LogStreamHandler;
import org.apache.tools.ant.taskdefs.PumpStreamHandler;
import org.apache.tools.ant.taskdefs.ExecuteStreamHandler;
import org.apache.tools.ant.types.Commandline;

/**
 * A task that fetches source files from a PVCS archive
 *
 * <b>19-04-2001</b> <p>The task now has a more robust
 * parser. It allows for platform independant file paths
 * and supports file names with <i>()</i>. Thanks to Erik Husby for
 * bringing the bug to my attention.
 *
 * <b>27-04-2001</b> <p>UNC paths are now handled properly. 
 * Fix provided by Don Jeffery. He also added an <i>UpdateOnly</i> flag
 * that, when true, conditions the PVCS get using the -U option to only 
 * update those files that have a modification time (in PVCS) that is newer 
 * than the existing workfile.
 *
 * @author Thomas Christensen <tchristensen@nordija.com>
 * @author Don Jeffery <donj@apogeenet.com>
 */
public class Pvcs extends org.apache.tools.ant.Task {
    private String pvcsbin;
    private String repository;
    private String pvcsProject;
    private Vector pvcsProjects;
    private String workspace;
    private String force;
    private String promotiongroup;
    private String label;
    private boolean ignorerc;
    private boolean updateOnly;

    /**
     * Constant for the thing to execute
     */
    private static final String PCLI_EXE = "pcli";

    /**
     * Constant for the PCLI listversionedfiles recursive i a format "get" understands
     */
    private static final String PCLI_LVF_ARGS = "lvf -z -aw";

    /**
     * Constant for the thing to execute
     */
    private static final String GET_EXE = "get";


    protected int runCmd(Commandline cmd, ExecuteStreamHandler out) {
        try {
            Project aProj = getProject();
            Execute exe = new Execute(out);
            exe.setAntRun(aProj);
            exe.setWorkingDirectory(aProj.getBaseDir());
            exe.setCommandline(cmd.getCommandline());
            return exe.execute();
        }
        catch (java.io.IOException e) {
            String msg = "Failed executing: " + cmd.toString() + ". Exception: "+e.getMessage();
            throw new BuildException(msg, location);
        }
    }

    private String getExecutable(String exe) {
        StringBuffer correctedExe = new StringBuffer();
        if(getPvcsbin()!=null)
            if(pvcsbin.endsWith(File.separator))
                correctedExe.append(pvcsbin);
            else
                correctedExe.append(pvcsbin).append(File.separator);
        return correctedExe.append(exe).toString();
    }

    /**
     * @exception org.apache.tools.ant.BuildException Something is stopping the build...
     */
    public void execute() throws org.apache.tools.ant.BuildException {
        Project aProj = getProject();
        int result = 0;

        if(repository == null || repository.trim().equals("")) 
            throw new BuildException("Required argument repository not specified");

        Commandline commandLine = new Commandline();
        commandLine.setExecutable(getExecutable(PCLI_EXE));

        commandLine.createArgument().setValue("lvf");
        commandLine.createArgument().setValue("-z");
        commandLine.createArgument().setValue("-aw");
        if(getWorkspace()!=null)
            commandLine.createArgument().setValue("-sp"+getWorkspace());
        commandLine.createArgument().setValue("-pr"+getRepository());

        if(getPvcsproject() == null && getPvcsprojects().isEmpty())
            pvcsProject = "/";

        if(getPvcsproject()!=null)
            commandLine.createArgument().setValue(getPvcsproject());
        if(!getPvcsprojects().isEmpty()) {
            Enumeration e = getPvcsprojects().elements();
            while (e.hasMoreElements()) {
                String projectName = ((PvcsProject)e.nextElement()).getName();
                if (projectName == null || (projectName.trim()).equals(""))
                    throw new BuildException("name is a required attribute of pvcsproject");
                commandLine.createArgument().setValue(projectName);
            }
        }

        File tmp = null;
        File tmp2 = null;
        try {
            Random rand = new Random(System.currentTimeMillis());
            tmp = new File("pvcs_ant_"+rand.nextLong()+".log");
            tmp2 = new File("pvcs_ant_"+rand.nextLong()+".log");
            log("Executing " + commandLine.toString(), Project.MSG_VERBOSE);
            result = runCmd(commandLine, new PumpStreamHandler(new FileOutputStream(tmp), new LogOutputStream(this,Project.MSG_WARN)));
            if ( result != 0 && !ignorerc) {
                String msg = "Failed executing: " + commandLine.toString();
                throw new BuildException(msg, location);
            }

            if(!tmp.exists())
                throw new BuildException("Communication between ant and pvcs failed. No output generated from executing PVCS commandline interface \"pcli\" and \"get\"");

            log("Creating folders", Project.MSG_INFO);
            createFolders(tmp);

            massagePCLI(tmp, tmp2);

            commandLine.clearArgs();
            commandLine.setExecutable(getExecutable(GET_EXE));

            if(getForce()!=null && getForce().equals("yes"))
                commandLine.createArgument().setValue("-Y");
            else
                commandLine.createArgument().setValue("-N");

            if(getPromotiongroup()!=null)
                commandLine.createArgument().setValue("-G"+getPromotiongroup());
            else {
                if(getLabel()!=null)
                    commandLine.createArgument().setValue("-r"+getLabel());
            }

            if (updateOnly) {
                commandLine.createArgument().setValue("-U");
            }

            commandLine.createArgument().setValue("@"+tmp2.getAbsolutePath());
            log("Getting files", Project.MSG_INFO);
            log("Executing " + commandLine.toString(), Project.MSG_VERBOSE);
            result = runCmd(commandLine, new LogStreamHandler(this,Project.MSG_INFO, Project.MSG_WARN));
            if ( result != 0 && !ignorerc) {
                String msg = "Failed executing: " + commandLine.toString() + ". Return code was "+result;
                throw new BuildException(msg, location);
            }

        } catch(FileNotFoundException e) {
            String msg = "Failed executing: " + commandLine.toString() + ". Exception: "+e.getMessage();
            throw new BuildException(msg,location);
        } catch(IOException e) {
            String msg = "Failed executing: " + commandLine.toString() + ". Exception: "+e.getMessage();
            throw new BuildException(msg,location);
        } catch(ParseException e) {
            String msg = "Failed executing: " + commandLine.toString() + ". Exception: "+e.getMessage();
            throw new BuildException(msg,location);
        } finally {
            if (tmp != null) {
                tmp.delete();
            }
            if (tmp2 != null) {
                tmp2.delete();
            }
        }
    }

    /**
     * Parses the file and creates the folders specified in the output section
     */
    private void createFolders(File file) throws IOException, ParseException {
        BufferedReader in = new BufferedReader(new FileReader(file));
        MessageFormat mf = new MessageFormat("{0}-arc({1})");
        String line = in.readLine();
        while(line != null) {
            log("Considering \""+line+"\"", Project.MSG_VERBOSE);
            if(line.startsWith("\"\\") || line.startsWith("\"/")) {
                Object[] objs = mf.parse(line);
                String f = (String)objs[1];
                int index = f.lastIndexOf(File.separator);
                if (index > -1) {
                    File dir = new File(f.substring(0, index));
                    if(!dir.exists()) {
                        log("Creating "+dir.getAbsolutePath(), Project.MSG_VERBOSE);
                        if(dir.mkdirs()) {
                            log("Created "+dir.getAbsolutePath(), Project.MSG_INFO);
                        } else {
                            log("Failed to create "+dir.getAbsolutePath(), Project.MSG_INFO);
                        }
                    } else {
                        log(dir.getAbsolutePath() + " exists. Skipping", Project.MSG_VERBOSE);
                    }
                } else {
                    log("File separator problem with " + line, 
                        Project.MSG_WARN);
                }
            } else {
                log("Skipped \""+line+"\"", Project.MSG_VERBOSE);
            }
            line = in.readLine();
        }
    }

    /**
     * Simple hack to handle the PVCS command-line tools botch when handling UNC notation.
     */
    private void massagePCLI(File in, File out) throws FileNotFoundException, IOException
    {
        BufferedReader inReader = new BufferedReader(new FileReader(in));
        BufferedWriter outWriter = new BufferedWriter(new FileWriter(out));
        String s = null;
        while ((s = inReader.readLine()) != null) {
            String sNormal = s.replace('\\', '/');
            outWriter.write(sNormal);
            outWriter.newLine();
        }
        inReader.close();
        outWriter.close();
    }

    /**
     * Get network name of the PVCS repository
     * @return String
     */
    public String getRepository() {
        return repository;
    }

    /**
     * Specifies the network name of the PVCS repository
     * @param repo String
     */
    public void setRepository(String repo) {
        repository = repo;
    }

    /**
     * Get name of the project in the PVCS repository
     * @return String
     */
    public String getPvcsproject() {
        return pvcsProject;
    }

    /**
     * Specifies the name of the project in the PVCS repository
     * @param prj String
     */
    public void setPvcsproject(String prj) {
        pvcsProject = prj;
    }

    /**
     * Get name of the project in the PVCS repository
     * @return Vector
     */
    public Vector getPvcsprojects() {
        return pvcsProjects;
    }

    /**
     * Get name of the workspace to store the retrieved files
     * @return String
     */
    public String getWorkspace() {
        return workspace;
    }

    /**
     * Specifies the name of the workspace to store retrieved files
     * @param ws String
     */
    public void setWorkspace(String ws) {
        workspace = ws;
    }

    /**
     * Get name of the PVCS bin directory 
     * @return String
     */
    public String getPvcsbin() {
        return pvcsbin;
    }

    /**
     * Specifies the location of the PVCS bin directory
     * @param ws String
     */
    public void setPvcsbin(String bin) {
        pvcsbin = bin;
    }

    /**
     * Get value of force
     * @return String
     */
    public String getForce() {
        return force;
    }

    /**
     * Specifies the value of the force argument
     * @param repo String (yes/no)
     */
    public void setForce(String f) {
        if(f!=null && f.equalsIgnoreCase("yes"))
            force="yes";
        else
            force = "no";
    }

    /**
     * Get value of promotiongroup
     * @return String
     */
    public String getPromotiongroup() {
        return promotiongroup;
    }

    /**
     * Specifies the name of the promotiongroup argument
     * @param repo String 
     */
    public void setPromotiongroup(String w) {
        promotiongroup=w;
    }

    /**
     * Get value of label
     * @return String
     */
    public String getLabel() {
        return label;
    }

    /**
     * Specifies the name of the label argument
     * @param repo String 
     */
    public void setLabel(String l) {
        label=l;
    }

    /**
     * Get value of ignorereturncode
     * @return String
     */
    public boolean getIgnoreReturnCode() {
        return ignorerc;
    }

    /**
     * If set to true the return value from executing the pvcs 
     * commands are ignored.
     */
    public void setIgnoreReturnCode(boolean b) {
        ignorerc = b;
    }

    /**
     * handles &lt;pvcsproject&gt; subelements
     * @param PvcsProject
     */
    public void addPvcsproject(PvcsProject p) {
        pvcsProjects.addElement(p);
    }

    public boolean getUpdateOnly() {
        return updateOnly;
    }

    /**
     * If set to true files are gotten only if newer
     * than existing local files.
     */
    public void setUpdateOnly(boolean l) {
        updateOnly = l;
    }

    /**
     * Creates a Pvcs object
     */
    public Pvcs() {
        super();
        pvcsProject = null;
        pvcsProjects = new Vector();
        workspace = null;
        repository = null;
        pvcsbin = null;
        force=null;
        promotiongroup=null;
        label=null;
        ignorerc=false;
        updateOnly = false;
    }
}
