package org.apache.tools.ant.taskdefs.cvslib;

import org.apache.tools.ant.BuildException;

/**
 * Represents a CVS user with a userID and a full name.
 *
 */
public class CvsUser {
    /** The user's Id */
    private String userID;
    /** The user's full name */
    private String displayName;


    /**
     * Set the user's fullname
     *
     * @param displayName the user's full name
     */
    public void setDisplayname(final String displayName) {
        this.displayName = displayName;
    }


    /**
     * Set the user's id
     *
     * @param userID the user's new id value.
     */
    public void setUserid(final String userID) {
        this.userID = userID;
    }


    /**
     * Get the user's id.
     *
     * @return The userID value
     */
    public String getUserID() {
        return userID;
    }


    /**
     * Get the user's full name
     *
     * @return the user's full name
     */
    public String getDisplayname() {
        return displayName;
    }


    /**
     * Validate that this object is configured.
     *
     * @exception BuildException if the instance has not be correctly
     *            configured.
     */
    public void validate() throws BuildException {
        if (null == userID) {
            final String message = "Username attribute must be set.";

            throw new BuildException(message);
        }
        if (null == displayName) {
            final String message =
                "Displayname attribute must be set for userID " + userID;

            throw new BuildException(message);
        }
    }
}

