package org.apache.poi.hssf.usermodel;

/**
 * Represents a escher picture.  Eg. A GIF, JPEG etc...
 *
 * @author Glen Stampoultzis
 * @version $Id: HSSFPicture.java 496526 2007-01-15 22:46:35Z markt $
 */
public class HSSFPicture
        extends HSSFSimpleShape
{

    int pictureIndex;

    /**
     * Constructs a picture object.
     */
    HSSFPicture( HSSFShape parent, HSSFAnchor anchor )
    {
        super( parent, anchor );
        setShapeType(OBJECT_TYPE_PICTURE);
    }

    public int getPictureIndex()
    {
        return pictureIndex;
    }

    public void setPictureIndex( int pictureIndex )
    {
        this.pictureIndex = pictureIndex;
    }
}
