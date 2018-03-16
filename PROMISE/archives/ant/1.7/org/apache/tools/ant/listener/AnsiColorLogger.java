package org.apache.tools.ant.listener;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.Properties;
import org.apache.tools.ant.DefaultLogger;
import org.apache.tools.ant.Project;

/**
 * Uses ANSI Color Code Sequences to colorize messages
 * sent to the console.
 *
 * If used with the -logfile option, the output file
 * will contain all the necessary escape codes to
 * display the text in colorized mode when displayed
 * in the console using applications like cat, more,
 * etc.
 *
 * This is designed to work on terminals that support ANSI
 * color codes.  It works on XTerm, ETerm, Mindterm, etc.
 * It also works on Win9x (with ANSI.SYS loaded.)
 *
 * NOTE:
 * It doesn't work on WinNT's COMMAND.COM even with
 * ANSI.SYS loaded.
 *
 * The default colors used for differentiating
 * the message levels can be changed by editing the
 * /org/apache/tools/ant/listener/defaults.properties
 * file.
 * This file contains 5 key/value pairs:
 * AnsiColorLogger.ERROR_COLOR=2;31
 * AnsiColorLogger.WARNING_COLOR=2;35
 * AnsiColorLogger.INFO_COLOR=2;36
 * AnsiColorLogger.VERBOSE_COLOR=2;32
 * AnsiColorLogger.DEBUG_COLOR=2;34
 *
 * Another option is to pass a system variable named
 * ant.logger.defaults, with value set to the path of
 * the file that contains user defined Ansi Color
 * Codes, to the <B>java</B> command using -D option.
 *
 * To change these colors use the following chart:
 *
 *      <B>ANSI COLOR LOGGER CONFIGURATION</B>
 *
 * Format for AnsiColorLogger.*=
 *  Attribute;Foreground;Background
 *
 *  Attribute is one of the following:
 *  0 -> Reset All Attributes (return to normal mode)
 *  1 -> Bright (Usually turns on BOLD)
 *  2 -> Dim
 *  3 -> Underline
 *  5 -> link
 *  7 -> Reverse
 *  8 -> Hidden
 *
 *  Foreground is one of the following:
 *  30 -> Black
 *  31 -> Red
 *  32 -> Green
 *  33 -> Yellow
 *  34 -> Blue
 *  35 -> Magenta
 *  36 -> Cyan
 *  37 -> White
 *
 *  Background is one of the following:
 *  40 -> Black
 *  41 -> Red
 *  42 -> Green
 *  43 -> Yellow
 *  44 -> Blue
 *  45 -> Magenta
 *  46 -> Cyan
 *  47 -> White
 *
 */
public class AnsiColorLogger extends DefaultLogger {
    private static final int ATTR_DIM = 2;

    private static final int FG_RED = 31;
    private static final int FG_GREEN = 32;
    private static final int FG_BLUE = 34;
    private static final int FG_MAGENTA = 35;
    private static final int FG_CYAN = 36;


    private static final String PREFIX = "\u001b[";
    private static final String SUFFIX = "m";
    private static final char SEPARATOR = ';';
    private static final String END_COLOR = PREFIX + SUFFIX;

    private String errColor
        = PREFIX + ATTR_DIM + SEPARATOR + FG_RED + SUFFIX;
    private String warnColor
        = PREFIX + ATTR_DIM + SEPARATOR + FG_MAGENTA + SUFFIX;
    private String infoColor
        = PREFIX + ATTR_DIM + SEPARATOR + FG_CYAN + SUFFIX;
    private String verboseColor
        = PREFIX + ATTR_DIM + SEPARATOR + FG_GREEN + SUFFIX;
    private String debugColor
        = PREFIX + ATTR_DIM + SEPARATOR + FG_BLUE + SUFFIX;

    private boolean colorsSet = false;

    /**
     * Set the colors to use from a property file specified by the
     * special ant property ant.logger.defaults
     */
    private void setColors() {
        String userColorFile = System.getProperty("ant.logger.defaults");
        String systemColorFile =
            "/org/apache/tools/ant/listener/defaults.properties";

        InputStream in = null;

        try {
            Properties prop = new Properties();

            if (userColorFile != null) {
                in = new FileInputStream(userColorFile);
            } else {
                in = getClass().getResourceAsStream(systemColorFile);
            }

            if (in != null) {
                prop.load(in);
            }

            String errC = prop.getProperty("AnsiColorLogger.ERROR_COLOR");
            String warn = prop.getProperty("AnsiColorLogger.WARNING_COLOR");
            String info = prop.getProperty("AnsiColorLogger.INFO_COLOR");
            String verbose = prop.getProperty("AnsiColorLogger.VERBOSE_COLOR");
            String debug = prop.getProperty("AnsiColorLogger.DEBUG_COLOR");
            if (errC != null) {
                errColor = PREFIX + errC + SUFFIX;
            }
            if (warn != null) {
                warnColor = PREFIX + warn + SUFFIX;
            }
            if (info != null) {
                infoColor = PREFIX + info + SUFFIX;
            }
            if (verbose != null) {
                verboseColor = PREFIX + verbose + SUFFIX;
            }
            if (debug != null) {
                debugColor = PREFIX + debug + SUFFIX;
            }
        } catch (IOException ioe) {
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                }
            }
        }
    }

    /**
     * @see DefaultLogger#printMessage
     */
    /** {@inheritDoc}. */
    protected void printMessage(final String message,
                                      final PrintStream stream,
                                      final int priority) {
        if (message != null && stream != null) {
            if (!colorsSet) {
                setColors();
                colorsSet = true;
            }

            final StringBuffer msg = new StringBuffer(message);
            switch (priority) {
                case Project.MSG_ERR:
                    msg.insert(0, errColor);
                    msg.append(END_COLOR);
                    break;
                case Project.MSG_WARN:
                    msg.insert(0, warnColor);
                    msg.append(END_COLOR);
                    break;
                case Project.MSG_INFO:
                    msg.insert(0, infoColor);
                    msg.append(END_COLOR);
                    break;
                case Project.MSG_VERBOSE:
                    msg.insert(0, verboseColor);
                    msg.append(END_COLOR);
                    break;
                case Project.MSG_DEBUG:
                default:
                    msg.insert(0, debugColor);
                    msg.append(END_COLOR);
                    break;
            }
            final String strmessage = msg.toString();
            stream.println(strmessage);
        }
    }
}
