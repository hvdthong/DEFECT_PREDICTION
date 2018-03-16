package org.apache.tools.ant.types;

/**
 * A parameter is composed of a name, type and value.
 *
 */
public final class Parameter {
    private String name = null;
    private String type = null;
    private String value = null;

    /**
     * Set the name attribute.
     *
     * @param name a <code>String</code> value
     */
    public void setName(final String name) {
        this.name = name;
    }

    /**
     * Set the type attribute.
     *
     * @param type a <code>String</code> value
     */
    public void setType(final String type) {
        this.type = type;
    }

    /**
     * Set the value attribute.
     *
     * @param value a <code>String</code> value
     */
    public void setValue(final String value) {
        this.value = value;
    }

    /**
     * Get the name attribute.
     *
     * @return a <code>String</code> value
     */
    public String getName() {
        return name;
    }

    /**
     * Get the type attribute.
     *
     * @return a <code>String</code> value
     */
    public String getType() {
        return type;
    }

    /**
     * Get the value attribute.
     *
     * @return a <code>String</code> value
     */
    public String getValue() {
        return value;
    }
}
