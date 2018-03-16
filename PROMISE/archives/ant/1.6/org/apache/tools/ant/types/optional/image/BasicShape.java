package org.apache.tools.ant.types.optional.image;

public abstract class BasicShape extends ImageOperation implements DrawOperation {
    protected int stroke_width = 0;
    protected String fill = "transparent";
    protected String stroke = "black";


    public void setFill(String col) {
        fill = col;
    }

    public void setStroke(String col) {
        stroke = col;
    }

    public void setStrokewidth(int width) {
        stroke_width = width;
    }
}
