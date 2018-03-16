package org.apache.tools.ant.taskdefs.optional.vss;

/**
 *  Holds all the constants for the VSS tasks.
 *
 */
public interface MSVSSConstants {
    /**  Constant for the thing to execute  */
    String SS_EXE = "ss";
    /** Dollar Sigh to prefix the project path */
    String PROJECT_PREFIX = "$";

    /**  The 'CP' command  */
    String COMMAND_CP = "CP";
    /**  The 'Add' command  */
    String COMMAND_ADD = "Add";
    /**  The 'Get' command  */
    String COMMAND_GET = "Get";
    /**  The 'Checkout' command  */
    String COMMAND_CHECKOUT = "Checkout";
    /**  The 'Checkin' command  */
    String COMMAND_CHECKIN = "Checkin";
    /**  The 'Label' command  */
    String COMMAND_LABEL = "Label";
    /**  The 'History' command  */
    String COMMAND_HISTORY = "History";
    /**  The 'Create' command  */
    String COMMAND_CREATE = "Create";

    /**  The brief style flag  */
    String STYLE_BRIEF = "brief";
    /**  The codediff style flag  */
    String STYLE_CODEDIFF = "codediff";
    /**  The nofile style flag  */
    String STYLE_NOFILE = "nofile";
    /**  The default style flag  */
    String STYLE_DEFAULT = "default";

    /**  The text for  current (default) timestamp */
    String TIME_CURRENT = "current";
    /**  The text for  modified timestamp */
    String TIME_MODIFIED = "modified";
    /**  The text for  updated timestamp */
    String TIME_UPDATED = "updated";

    /**  The text for replacing writable files   */
    String WRITABLE_REPLACE = "replace";
    /**  The text for skiping writable files  */
    String WRITABLE_SKIP = "skip";
    /**  The text for failing on writable files  */
    String WRITABLE_FAIL = "fail";

    /** -Y flag */
    String FLAG_LOGIN = "-Y";
    /** -GL flag */
    String FLAG_OVERRIDE_WORKING_DIR = "-GL";
    /** -I- flag */
    String FLAG_AUTORESPONSE_DEF = "-I-";
    /** -I-Y flag */
    String FLAG_AUTORESPONSE_YES = "-I-Y";
    /** -I-N flag */
    String FLAG_AUTORESPONSE_NO = "-I-N";
    /** -R flag */
    String FLAG_RECURSION = "-R";
    /** -V flag */
    String FLAG_VERSION = "-V";
    /** -Vd flag */
    String FLAG_VERSION_DATE = "-Vd";
    /** -VL flag */
    String FLAG_VERSION_LABEL = "-VL";
    /** -W flag */
    String FLAG_WRITABLE = "-W";
    /** -N flag */
    String VALUE_NO = "-N";
    /** -Y flag */
    String VALUE_YES = "-Y";
    /** -O- flag */
    String FLAG_QUIET = "-O-";
    /** -C flag */
    String FLAG_COMMENT = "-C";
    /** -L flag */
    String FLAG_LABEL = "-L";
    /** ~d flag */
    String VALUE_FROMDATE = "~d";
    /** ~L flag */
    String VALUE_FROMLABEL = "~L";
    /** -O flag */
    String FLAG_OUTPUT = "-O";
    /** -U flag */
    String FLAG_USER = "-U";
    /** -F- flag */
    String FLAG_NO_FILE = "-F-";
    /** -B flag */
    String FLAG_BRIEF = "-B";
    /** -D flag */
    String FLAG_CODEDIFF = "-D";
    /** -GTC flag */
    String FLAG_FILETIME_DEF = "-GTC";
    /** -GTM flag */
    String FLAG_FILETIME_MODIFIED = "-GTM";
    /** -GTU flag */
    String FLAG_FILETIME_UPDATED = "-GTU";
    /** -GWR flag */
    String FLAG_REPLACE_WRITABLE = "-GWR";
    /** -GWS flag */
    String FLAG_SKIP_WRITABLE = "-GWS";
    /** -G- flag */
    String FLAG_NO_GET = "-G-";
}
