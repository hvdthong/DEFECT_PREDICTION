package org.apache.tools.ant.taskdefs.optional.sos;

/**
 * Interface to hold constants used by the SOS tasks
 *
 */
public interface SOSCmd {
    String COMMAND_SOS_EXE = "soscmd";
    String COMMAND_GET_FILE = "GetFile";
    String COMMAND_GET_PROJECT = "GetProject";
    String COMMAND_CHECKOUT_FILE = "CheckOutFile";
    String COMMAND_CHECKOUT_PROJECT = "CheckOutProject";
    String COMMAND_CHECKIN_FILE = "CheckInFile";
    String COMMAND_CHECKIN_PROJECT = "CheckInProject";
    String COMMAND_HISTORY = "GetFileHistory";
    String COMMAND_LABEL = "AddLabel";
    String PROJECT_PREFIX = "$";
    String FLAG_COMMAND = "-command";
    String FLAG_VSS_SERVER = "-database";
    String FLAG_USERNAME = "-name";
    String FLAG_PASSWORD = "-password";
    String FLAG_COMMENT = "-log";
    String FLAG_WORKING_DIR = "-workdir";
    String FLAG_RECURSION = "-recursive";
    String FLAG_VERSION = "-revision";
    String FLAG_LABEL = "-label";
    String FLAG_NO_COMPRESSION = "-nocompress";
    String FLAG_NO_CACHE = "-nocache";
    String FLAG_SOS_SERVER = "-server";
    String FLAG_SOS_HOME = "-soshome";
    String FLAG_PROJECT = "-project";
    String FLAG_FILE = "-file";
    String FLAG_VERBOSE = "-verbose";
}
