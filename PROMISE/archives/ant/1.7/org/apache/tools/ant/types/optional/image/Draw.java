package org.apache.tools.ant.types.optional.image;

import javax.media.jai.PlanarImage;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

/**
 *
 * @see org.apache.tools.ant.taskdefs.optional.image.Image
 */
public class Draw extends TransformOperation {
    protected int xloc = 0;
    protected int yloc = 0;

    /**
     * Set the X location.
     * @param x the value to use.
     */
    public void setXloc(int x) {
        xloc = x;
    }

    /**
     * Set the Y location.
     * @param y the value to use.
     */
    public void setYloc(int y) {
        yloc = y;
    }

    /** {@inheritDoc}. */
    public void addRectangle(Rectangle rect) {
        instructions.add(rect);
    }

    /** {@inheritDoc}. */
    public void addText(Text text) {
        instructions.add(text);
    }

    /**
     * Add an ellipse.
     * @param elip the ellipse to add.
     */
    public void addEllipse(Ellipse elip) {
        instructions.add(elip);
    }

    /**
     * Add an arc.
     * @param arc the arc to add.
     */
    public void addArc(Arc arc) {
        instructions.add(arc);
    }

    /** {@inheritDoc}. */
    public PlanarImage executeTransformOperation(PlanarImage image) {
        BufferedImage bi = image.getAsBufferedImage();
        Graphics2D graphics = (Graphics2D) bi.getGraphics();

        for (int i = 0; i < instructions.size(); i++) {
            ImageOperation instr = ((ImageOperation) instructions.elementAt(i));
            if (instr instanceof DrawOperation) {
                PlanarImage op = ((DrawOperation) instr).executeDrawOperation();
                log("\tDrawing to x=" + xloc + " y=" + yloc);
                graphics.drawImage(op.getAsBufferedImage(), null, xloc, yloc);
            } else if (instr instanceof TransformOperation) {
                PlanarImage op
                    = ((TransformOperation) instr).executeTransformOperation(null);
                BufferedImage child = op.getAsBufferedImage();
                log("\tDrawing to x=" + xloc + " y=" + yloc);
                graphics.drawImage(child, null, xloc, yloc);
                PlanarImage.wrapRenderedImage(bi);
            }
        }
        image = PlanarImage.wrapRenderedImage(bi);

        return image;
    }
}
