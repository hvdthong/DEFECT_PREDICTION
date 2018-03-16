package org.apache.tools.ant.taskdefs.email;

/**
 * Holds an email address.
 *
 * @since Ant 1.5
 */
public class EmailAddress {
    private String name;
    private String address;


    /** Creates an empty email address  */
    public EmailAddress() {
    }


    /**
     * Creates a new email address based on the given string
     *
     * @param email the email address (with or without <>)
     * Acceptable forms include:
     *    address
     *    <address>
     *    name <address>
     *    <address> name
     *    (name) address
     *    address (name)
     */
    public EmailAddress(String email) {
        final int minLen = 9;
        int len = email.length();

        if (len > minLen) {
            if ((email.charAt(0) == '<' || email.charAt(1) == '<')
            && (email.charAt(len - 1) == '>' || email.charAt(len - 2) == '>')) {
                this.address = trim(email, true);
                return;
            }
        }

        int paramDepth = 0;
        int start = 0;
        int end = 0;
        int nStart = 0;
        int nEnd = 0;

        for (int i = 0; i < len; i++) {
            char c = email.charAt(i);
            if (c == '(') {
                paramDepth++;
                if (start == 0) {
                    nStart = i + 1;
                }
            } else if (c == ')') {
                paramDepth--;
                if (end == 0) {
                    nEnd = i;
                }
            } else if (paramDepth == 0 && c == '<') {
                if (start == 0) {
                    nEnd = i;
                }
                start = i + 1;
            } else if (paramDepth == 0 && c == '>') {
                end = i;
                if (end != len - 1) {
                    nStart = i + 1;
                }
            }
        }

        if (end == 0) {
            end = len;
        }
        if (nEnd == 0) {
            nEnd = len;
        }

        this.address = trim(email.substring(start, end), true);
        this.name = trim(email.substring(nStart, nEnd), false);

        if (this.name.length() + this.address.length() > len) {
            this.name = null;
        }
    }

    /**
     *  A specialised trim() that trims whitespace,
     *  '(', ')', '"', '<', '>' from the start and end of strings
     */
    private String trim(String t, boolean trimAngleBrackets) {
        int start = 0;
        int end = t.length();
        boolean trim = false;
        do {
            trim = false;
            if (t.charAt(end - 1) == ')'
                || (t.charAt(end - 1) == '>' && trimAngleBrackets)
                || (t.charAt(end - 1) == '"' && t.charAt(end - 2) != '\\')
                || t.charAt(end - 1) <= '\u0020') {
                trim = true;
                end--;
            }
            if (t.charAt(start) == '('
                || (t.charAt(start) == '<' && trimAngleBrackets)
                || t.charAt(start) == '"'
                || t.charAt(start) <= '\u0020') {
                trim = true;
                start++;
            }
        } while (trim);
        return t.substring(start, end);
    }


    /**
     * Sets the personal / display name of the address
     *
     * @param name the display name
     */
    public void setName(String name) {
        this.name = name;
    }


    /**
     * Sets the email address
     *
     * @param address the actual email address (without <>)
     */
    public void setAddress(String address) {
        this.address = address;
    }


    /**
     * Constructs a string "name &lt;address&gt;" or "address"
     *
     * @return a string representation of the address
     */
    public String toString() {
        if (name == null) {
            return address;
        } else {
            return name + " <" + address + ">";
        }
    }


    /**
     * Returns the address
     *
     * @return the address part
     */
    public String getAddress() {
        return address;
    }


    /**
     * Returns the display name
     *
     * @return the display name part
     */
    public String getName() {
        return name;
    }
}

