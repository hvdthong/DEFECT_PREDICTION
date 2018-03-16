package org.apache.wml;

/**
 * <p>The interface is modeled after DOM1 Spec for HTML from W3C.
 * The DTD used in this DOM model is from 
 *
 * <p>The 'template' element declares a template for the cards in the deck.</p>
 *
 * @version $Id: WMLTemplateElement.java 315461 2000-04-23 18:07:48Z david $
 * @author <a href="mailto:david@topware.com.tw">David Li</a>
 */

public interface WMLTemplateElement extends WMLElement {

    public void setOnTimer(String newValue);
    public String getOnTimer();

    public void setOnEnterBackward(String newValue);
    public String getOnEnterBackward();

    public void setOnEnterForward(String newValue);
    public String getOnEnterForward();
}
