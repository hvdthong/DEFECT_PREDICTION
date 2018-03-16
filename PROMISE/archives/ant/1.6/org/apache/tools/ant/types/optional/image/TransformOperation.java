package org.apache.tools.ant.types.optional.image;
import javax.media.jai.PlanarImage;

/**
 *
 * @see org.apache.tools.ant.tasks.optional.image.Image
 */
public abstract class TransformOperation extends ImageOperation {
    public abstract PlanarImage executeTransformOperation(PlanarImage img);

    public void addRectangle(Rectangle instr) {
        instructions.add(instr);
    }

}
