package org.apache.tools.ant.taskdefs.cvslib;

import org.apache.tools.ant.BuildException;

/**
 * Represents a CVS user with a userID and a full name.
 *
 */
public class CvsUser {
    /** The user's Id */
    private String m_userID;
    /** The user's full name */
    private String m_displayName;


    /**
     * Set the user's fullname
     *
     * @param displayName the user's full name
     */
    public void setDisplayname(final String displayName) {
        m_displayName = displayName;
    }


    /**
     * Set the user's id
     *
     * @param userID the user's new id value.
     */
    public void setUserid(final String userID) {
        m_userID = userID;
    }


    /**
     * Get the user's id.
     *
     * @return The userID value
     */
    String getUserID() {
        return m_userID;
    }


    /**
     * Get the user's full name
     *
     * @return the user's full name
     */
    String getDisplayname() {
        return m_displayName;
    }


    /**
     * validate that this object is configured.
     *
     * @exception BuildException if the instance has not be correctly
     *            configured.
     */
    void validate() throws BuildException {
        if (null == m_userID) {
            final String message = "Username attribute must be set.";

            throw new BuildException(message);
        }
        if (null == m_displayName) {
            final String message =
                "Displayname attribute must be set for userID " + m_userID;

            throw new BuildException(message);
        }
    }
}

