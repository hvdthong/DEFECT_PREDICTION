package org.apache.tools.ant.types.optional.image;

import javax.media.jai.PlanarImage;

/**
 * Interface which represents an Operation which is "drawable", such
 * as a Rectangle, Circle or Text.  The Operation is responsible for
 * creating its own image buffer and drawing itself into it, then
 * wrapping and returning it as a PlanarImage.  This allows multible
 * "drawable" objects to be nested.
 *
 * @see org.apache.tools.ant.taskdefs.optional.image.Image
 */
public interface DrawOperation {
    /**
     * Abstract method which is intended to create an image buffer
     * and return it so it can be drawn into another object.  Use
     * an Alpha channel for a "transparent" background.
     * @return a planar image
     */
    PlanarImage executeDrawOperation();
}
