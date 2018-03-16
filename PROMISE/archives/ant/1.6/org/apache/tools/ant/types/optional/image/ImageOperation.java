package org.apache.tools.ant.types.optional.image;

import org.apache.tools.ant.types.DataType;
import java.util.Vector;

/**
 *
 * @see org.apache.tools.ant.tasks.optional.image.Image
 */
public abstract class ImageOperation extends DataType {
    protected Vector instructions = new Vector();

    public void addRotate(Rotate instr) {
        instructions.add(instr);
    }

    public void addDraw(Draw instr) {
        instructions.add(instr);
    }

    public void addRectangle(Rectangle instr) {
        instructions.add(instr);
    }

    public void addText(Text instr) {
        instructions.add(instr);
    }

    public void addScale(Scale instr) {
        instructions.add(instr);
    }
}
