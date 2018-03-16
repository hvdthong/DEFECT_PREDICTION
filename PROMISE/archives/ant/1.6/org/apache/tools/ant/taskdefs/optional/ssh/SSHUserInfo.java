package org.apache.tools.ant.taskdefs.optional.ssh;

import com.jcraft.jsch.UserInfo;

/**
 */
public class SSHUserInfo implements UserInfo {

    private String name;
    private String password = null;
    private String keyfile;
    private String passphrase = null;
    private boolean firstTime = true;
    private boolean trustAllCertificates;

    public SSHUserInfo() {
        super();
        this.trustAllCertificates = false;
    }

    public SSHUserInfo(String password, boolean trustAllCertificates) {
        super();
        this.password = password;
        this.trustAllCertificates = trustAllCertificates;
    }

    /**
     * Gets the user name.
     * @return the user name
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the pass phrase of the user.
     * @param message a message
     * @return the passphrase
     */
    public String getPassphrase(String message) {
        return passphrase;
    }

    /**
     * Gets the user's password.
     * @return the user's password
     */
    public String getPassword() {
        return password;
    }

    /**
     * Prompts a string.
     * @param str the string
     * @return whether the string was prompted
     */
    public boolean prompt(String str) {
        return false;
    }

    /**
     * Indicates whether a retry was done.
     * @return whether a retry was done
     */
    public boolean retry() {
        return false;
    }

    /**
     * Sets the name.
     * @param name The name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Sets the passphrase.
     * @param passphrase The passphrase to set
     */
    public void setPassphrase(String passphrase) {
        this.passphrase = passphrase;
    }

    /**
     * Sets the password.
     * @param password The password to set
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Sets the trust.
     * @param trust whether to trust or not.
     */
    public void setTrust(boolean trust) {
        this.trustAllCertificates = trust;
    }

    /**
     * @return whether to trust or not.
     */
    public boolean getTrust() {
        return this.trustAllCertificates;
    }

    /**
     * Returns the passphrase.
     * @return String
     */
    public String getPassphrase() {
        return passphrase;
    }

    /**
     * Returns the keyfile.
     * @return String
     */
    public String getKeyfile() {
        return keyfile;
    }

    /**
     * Sets the keyfile.
     * @param keyfile The keyfile to set
     */
    public void setKeyfile(String keyfile) {
        this.keyfile = keyfile;
    }

    public boolean promptPassphrase(String message) {
        return true;
    }

    public boolean promptPassword(String passwordPrompt) {
        if (firstTime) {
            firstTime = false;
            return true;
        }
        return firstTime;
    }

    public boolean promptYesNo(String message) {
        return trustAllCertificates;
    }

    public void showMessage(String message) {
    }

}
