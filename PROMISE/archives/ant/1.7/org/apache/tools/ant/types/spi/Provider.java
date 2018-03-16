package org.apache.tools.ant.types.spi;

import org.apache.tools.ant.ProjectComponent;
import org.apache.tools.ant.BuildException;

/**
 * ANT Jar-Task SPI extension
 * This class corresponds to the nested element
 * &lt;provider type="type"&gt; in the &lt;service type=""&gt;
 * nested element of the jar task.
 */
public class Provider extends ProjectComponent {
    private String type;

    /**
     * @return the class name for
     */
    public String getClassName() {
        return type;
    }

    /**
     * Set the provider classname.
     * @param type the value to set.
     */
    public void setClassName(String type) {
        this.type = type;
    }

    /**
     * Check if the component has been configured correctly.
     */
    public void check() {
        if (type == null) {
            throw new BuildException(
                "classname attribute must be set for provider element",
                getLocation());
        }
        if (type.length() == 0) {
            throw new BuildException(
                "Invalid empty classname", getLocation());
        }
    }
}
