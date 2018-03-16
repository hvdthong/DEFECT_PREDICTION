package org.apache.tools.ant.types;

/**
 * A parameter is composed of a name, type and value.
 *
 */
public final class Parameter {
    private String name = null;
    private String type = null;
    private String value = null;

    public final void setName(final String name) {
        this.name = name;
    }

    public final void setType(final String type) {
        this.type = type;
    }

    public final void setValue(final String value) {
        this.value = value;
    }

    public final String getName() {
        return name;
    }

    public final String getType() {
        return type;
    }

    public final String getValue() {
        return value;
    }
}
