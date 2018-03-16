package org.apache.tools.ant.types.optional.image;


/** Draw a basic shape */
public abstract class BasicShape extends ImageOperation implements DrawOperation {
    protected int stroke_width = 0;
    protected String fill = "transparent";
    protected String stroke = "black";


    /**
     * Set the fill attribute.
     * @param col the color value to use.
     */
    public void setFill(String col) {
        fill = col;
    }

    /**
     * Set the stroke attribute.
     * @param col the color value to use.
     */
    public void setStroke(String col) {
        stroke = col;
    }

    /**
     * Set the stroke width attribute.
     * @param width the value to use.
     */
    public void setStrokewidth(int width) {
        stroke_width = width;
    }
}
