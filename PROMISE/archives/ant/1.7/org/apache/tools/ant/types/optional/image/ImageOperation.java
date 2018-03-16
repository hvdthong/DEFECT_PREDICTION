package org.apache.tools.ant.types.optional.image;

import org.apache.tools.ant.types.DataType;
import java.util.Vector;

/**
 *
 * @see org.apache.tools.ant.taskdefs.optional.image.Image
 */
public abstract class ImageOperation extends DataType {
    protected Vector instructions = new Vector();

    /**
     * Add a rotate to the operation.
     * @param instr the rotate to add.
     */
    public void addRotate(Rotate instr) {
        instructions.add(instr);
    }

    /**
     * Add a draw to the operation.
     * @param instr the draw to add.
     */
    public void addDraw(Draw instr) {
        instructions.add(instr);
    }

    /**
     * Add a rectangle to the operation.
     * @param instr the rectangle to add.
     */
    public void addRectangle(Rectangle instr) {
        instructions.add(instr);
    }

    /**
     * Add text to the operation.
     * @param instr the text to add.
     */
    public void addText(Text instr) {
        instructions.add(instr);
    }

    /**
     * Add a scale to the operation.
     * @param instr the scale to add.
     */
    public void addScale(Scale instr) {
        instructions.add(instr);
    }
}
