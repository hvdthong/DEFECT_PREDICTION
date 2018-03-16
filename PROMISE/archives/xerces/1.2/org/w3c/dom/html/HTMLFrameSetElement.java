package org.w3c.dom.html;

/**
 *  Create a grid of frames. See the  FRAMESET element definition in HTML 4.0.
 */
public interface HTMLFrameSetElement extends HTMLElement {
    /**
     *  The number of columns of frames in the frameset. See the  cols 
     * attribute definition in HTML 4.0.
     */
    public String getCols();
    public void setCols(String cols);

    /**
     *  The number of rows of frames in the frameset. See the  rows attribute 
     * definition in HTML 4.0.
     */
    public String getRows();
    public void setRows(String rows);

}

