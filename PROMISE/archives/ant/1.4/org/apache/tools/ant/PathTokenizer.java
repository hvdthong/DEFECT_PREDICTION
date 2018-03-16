package org.apache.tools.ant;

import java.util.*;
import java.io.*;

/**
 * A Path tokenizer takes a path and returns the components that make up
 * that path.
 *
 * The path can use path separators of either ':' or ';' and file separators
 * of either '/' or '\'
 *
 * @author Conor MacNeill (conor@ieee.org)
 *
 */ 
public class PathTokenizer {
    /**
     * A tokenizer to break the string up based on the ':' or ';' separators.
     */
    private StringTokenizer tokenizer;
    
    /**
     * A String which stores any path components which have been read ahead.
     */
    private String lookahead = null;

    /**
     * Flag to indicate whether we are running on a platform with a DOS style
     * filesystem
     */
    private boolean dosStyleFilesystem;

    public PathTokenizer(String path) {
       tokenizer = new StringTokenizer(path, ":;", false);
       dosStyleFilesystem = File.pathSeparatorChar == ';'; 
    }

    public boolean hasMoreTokens() {
        if (lookahead != null) {
            return true;
        }
        
        return tokenizer.hasMoreTokens();
    }
    
    public String nextToken() throws NoSuchElementException {
        String token = null;
        if (lookahead != null) {
            token = lookahead;
            lookahead = null;
        }
        else {
            token = tokenizer.nextToken().trim();
        }            
            
        if (token.length() == 1 && Character.isLetter(token.charAt(0))
                                && dosStyleFilesystem
                                && tokenizer.hasMoreTokens()) {
            String nextToken = tokenizer.nextToken().trim();
            if (nextToken.startsWith("\\") || nextToken.startsWith("/")) {
                token += ":" + nextToken;
            }
            else {
                lookahead = nextToken;
            }
        }
           
        return token;
    }
}
          
