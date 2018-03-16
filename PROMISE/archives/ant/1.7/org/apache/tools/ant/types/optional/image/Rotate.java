package org.apache.tools.ant.types.optional.image;

import javax.media.jai.PlanarImage;
import javax.media.jai.InterpolationNearest;
import javax.media.jai.JAI;
import java.awt.image.renderable.ParameterBlock;
import java.awt.image.BufferedImage;
import java.awt.Graphics2D;

/**
 * ImageOperation to rotate an image by a certain degree
 *
 * @see org.apache.tools.ant.taskdefs.optional.image.Image
 */
public class Rotate extends TransformOperation implements DrawOperation {
    private static final float HALF_CIRCLE = 180.0F;

    protected float angle = 0.0F;

    /**
     * Sets the angle of rotation in degrees.
     * @param ang The angle at which to rotate the image
     */
    public void setAngle(String ang) {
        angle = Float.parseFloat(ang);
    }


    /**
     * Rotate an image.
     * @param image the image to rotate.
     * @return the rotated image.
     */
    public PlanarImage performRotate(PlanarImage image) {
        float tAngle = (float) (angle * (Math.PI / HALF_CIRCLE));
        ParameterBlock pb = new ParameterBlock();
        pb.addSource(image);
        pb.add(0.0F);
        pb.add(0.0F);
        pb.add(tAngle);
        pb.add(new InterpolationNearest());
        return JAI.create("Rotate", pb, null);
    }


    /**
     * Performs the image rotation when being handled as a TransformOperation.
     * @param image The image to perform the transformation on.
     * @return the transformed image.
     */
    public PlanarImage executeTransformOperation(PlanarImage image) {
        BufferedImage bi = null;
        Graphics2D graphics = null;
        for (int i = 0; i < instructions.size(); i++) {
            ImageOperation instr = ((ImageOperation) instructions.elementAt(i));
            if (instr instanceof DrawOperation) {
                System.out.println("Execing Draws");
                PlanarImage op = ((DrawOperation) instr).executeDrawOperation();
                image = performRotate(op);
                return image;
            } else if (instr instanceof TransformOperation) {
                bi = image.getAsBufferedImage();
                graphics = (Graphics2D) bi.getGraphics();
                System.out.println("Execing Transforms");
                image = ((TransformOperation) instr)
                    .executeTransformOperation(PlanarImage.wrapRenderedImage(bi));
                bi = image.getAsBufferedImage();
            }
        }
        System.out.println("Execing as TransformOperation");
        image = performRotate(image);
        System.out.println(image);
        return image;
    }

    /**
     *  Performs the image rotation when being handled as a DrawOperation.
     *  It absolutely requires that there be a DrawOperation nested beneath it,
     *  but only the FIRST DrawOperation will be handled since it can only return
     *  ONE image.
     * @return the image.
     */
    public PlanarImage executeDrawOperation() {
        for (int i = 0; i < instructions.size(); i++) {
            ImageOperation instr = ((ImageOperation) instructions.elementAt(i));
            if (instr instanceof DrawOperation) {
                PlanarImage op = ((DrawOperation) instr).executeDrawOperation();
                op = performRotate(op);
                return op;
            }
        }
        return null;
    }

}
