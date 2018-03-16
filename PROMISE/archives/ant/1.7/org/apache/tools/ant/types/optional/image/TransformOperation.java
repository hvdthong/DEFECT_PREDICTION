package org.apache.tools.ant.types.optional.image;
import javax.media.jai.PlanarImage;

/**
 *
 * @see org.apache.tools.ant.taskdefs.optional.image.Image
 */
public abstract class TransformOperation extends ImageOperation {
    /**
     * Performs the transformations.
     * @param img The image to perform the transformation on.
     * @return the transformed image.
     */
    public abstract PlanarImage executeTransformOperation(PlanarImage img);

     /** {@inheritDoc}. */
    public void addRectangle(Rectangle instr) {
        instructions.add(instr);
    }
}
