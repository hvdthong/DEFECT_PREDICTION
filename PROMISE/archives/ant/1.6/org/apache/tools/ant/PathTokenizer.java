package org.apache.tools.ant;

import java.io.File;
import java.util.NoSuchElementException;
import java.util.StringTokenizer;
import org.apache.tools.ant.taskdefs.condition.Os;

/**
 * A Path tokenizer takes a path and returns the components that make up
 * that path.
 *
 * The path can use path separators of either ':' or ';' and file separators
 * of either '/' or '\'.
 *
 */
public class PathTokenizer {
    /**
     * A tokenizer to break the string up based on the ':' or ';' separators.
     */
    private StringTokenizer tokenizer;

    /**
     * A String which stores any path components which have been read ahead
     * due to DOS filesystem compensation.
     */
    private String lookahead = null;

    /**
     * A boolean that determines if we are running on Novell NetWare, which
     * exhibits slightly different path name characteristics (multi-character
     * volume / drive names)
     */
    private boolean onNetWare = Os.isFamily("netware");

    /**
     * Flag to indicate whether or not we are running on a platform with a
     * DOS style filesystem
     */
    private boolean dosStyleFilesystem;

    /**
     * Constructs a path tokenizer for the specified path.
     *
     * @param path The path to tokenize. Must not be <code>null</code>.
     */
    public PathTokenizer(String path) {
        if (onNetWare) {
            tokenizer = new StringTokenizer(path, ":;", true);
        } else {
            tokenizer = new StringTokenizer(path, ":;", false);
        }
        dosStyleFilesystem = File.pathSeparatorChar == ';';
    }

    /**
     * Tests if there are more path elements available from this tokenizer's
     * path. If this method returns <code>true</code>, then a subsequent call
     * to nextToken will successfully return a token.
     *
     * @return <code>true</code> if and only if there is at least one token
     * in the string after the current position; <code>false</code> otherwise.
     */
    public boolean hasMoreTokens() {
        if (lookahead != null) {
            return true;
        }

        return tokenizer.hasMoreTokens();
    }

    /**
     * Returns the next path element from this tokenizer.
     *
     * @return the next path element from this tokenizer.
     *
     * @exception NoSuchElementException if there are no more elements in this
     *            tokenizer's path.
     */
    public String nextToken() throws NoSuchElementException {
        String token = null;
        if (lookahead != null) {
            token = lookahead;
            lookahead = null;
        } else {
            token = tokenizer.nextToken().trim();
        }

        if (!onNetWare) {
            if (token.length() == 1 && Character.isLetter(token.charAt(0))
                                    && dosStyleFilesystem
                                    && tokenizer.hasMoreTokens()) {
                String nextToken = tokenizer.nextToken().trim();
                if (nextToken.startsWith("\\") || nextToken.startsWith("/")) {
                    token += ":" + nextToken;
                } else {
                    lookahead = nextToken;
                }
            }
        } else {
            if (token.equals(File.pathSeparator) || token.equals(":")) {
                token = tokenizer.nextToken().trim();
            }

            if (tokenizer.hasMoreTokens()) {
                String nextToken = tokenizer.nextToken().trim();

                if (!nextToken.equals(File.pathSeparator)) {
                    if (nextToken.equals(":")) {
                        if (!token.startsWith("/") && !token.startsWith("\\")
                            && !token.startsWith(".")
                            && !token.startsWith("..")) {
                            String oneMore = tokenizer.nextToken().trim();
                            if (!oneMore.equals(File.pathSeparator)) {
                                token += ":" + oneMore;
                            } else {
                                token += ":";
                                lookahead = oneMore;
                            }
                        }
                    } else {
                        lookahead = nextToken;
                    }
                }
            }
        }
        return token;
    }
}

