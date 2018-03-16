package org.apache.tools.ant.taskdefs.email;

/**
 * Holds an email address.
 *
 * @author roxspring@yahoo.com Rob Oxspring
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
     * @param address the email address
     */
    public EmailAddress(String address) {
        this.address = address;
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
     * @param address the actual email address
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

