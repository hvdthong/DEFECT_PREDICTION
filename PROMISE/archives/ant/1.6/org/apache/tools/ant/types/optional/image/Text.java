package org.apache.tools.ant.types.optional.image;

import javax.media.jai.PlanarImage;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;

/**
 *
 * @see org.apache.tools.ant.taskdefs.optional.image.Image
 */
public class Text extends ImageOperation implements DrawOperation {
    private String str_text = "";
    private String font = "Arial";
    private int point = 10;
    private boolean bold = false;
    private boolean italic = false;
    private String color = "black";

    public void setString(String str) {
        str_text = str;
    }

    public void setFont(String f) {
        font = f;
    }

    public void setPoint(String p) {
        point = Integer.parseInt(p);
    }

    public void setColor(String c) {
        color = c;
    }

    /**
     * @todo is this used?
     */
    public void setBold(boolean state) {
        bold = state;
    }

    /**
     * @todo is this used?
     */
    public void setItalic(boolean state) {
        italic = state;
    }

    public PlanarImage executeDrawOperation() {
        log("\tCreating Text \"" + str_text + "\"");

        Color couloir = ColorMapper.getColorByName(color);
        int width = 1;
        int height = 1;

        BufferedImage bi = new BufferedImage(width, height, BufferedImage.TYPE_4BYTE_ABGR_PRE);
        Graphics2D graphics = (Graphics2D) bi.getGraphics();
        graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        graphics.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
        Font f = new Font(font, Font.PLAIN, point);
        FontMetrics fmetrics = graphics.getFontMetrics(f);
        height = fmetrics.getMaxAscent() + fmetrics.getMaxDescent();
        width = fmetrics.stringWidth(str_text);


        bi = new BufferedImage(width, height, BufferedImage.TYPE_4BYTE_ABGR_PRE);
        graphics = (Graphics2D) bi.getGraphics();

        graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        graphics.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);

        graphics.setFont(f);
        graphics.setColor(couloir);
        graphics.drawString(str_text, 0, height - fmetrics.getMaxDescent());
        PlanarImage image = PlanarImage.wrapRenderedImage(bi);
        return image;
    }
}
