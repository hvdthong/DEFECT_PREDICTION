package org.apache.tools.ant.taskdefs.optional.ssh;

import com.jcraft.jsch.UserInfo;

/**
 * @author rhanderson
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
     * @see com.jcraft.jsch.UserInfo#getName()
     */
    public String getName() {
        return name;
    }

    /**
     * @see com.jcraft.jsch.UserInfo#getPassphrase(String)
     */
    public String getPassphrase(String message) {
        return passphrase;
    }

    /**
     * @see com.jcraft.jsch.UserInfo#getPassword()
     */
    public String getPassword() {
        return password;
    }

    /**
     * @see com.jcraft.jsch.UserInfo#prompt(String)
     */
    public boolean prompt(String str) {
        return false;
    }

    /**
     * @see com.jcraft.jsch.UserInfo#retry()
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

    /**
     * @see com.jcraft.jsch.UserInfo#promptPassphrase(String)
     */
    public boolean promptPassphrase(String message) {
        return true;
    }

    /**
     * @see com.jcraft.jsch.UserInfo#promptPassword(String)
     */
    public boolean promptPassword(String passwordPrompt) {
        if (firstTime) {
            firstTime = false;
            return true;
        }
        return firstTime;
    }

    /**
     * @see com.jcraft.jsch.UserInfo#promptYesNo(String)
     */
    public boolean promptYesNo(String message) {
        return trustAllCertificates;
    }

    /**
     * @see com.jcraft.jsch.UserInfo#showMessage(String)
     */
    public void showMessage(String message) {
    }

}
