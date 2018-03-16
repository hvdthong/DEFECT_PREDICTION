package org.apache.wml;

/**
 * <p>The interface is modeled after DOM1 Spec for HTML from W3C.
 * The DTD used in this DOM model is from 
 *
 * <p>'card' element is the basic display unit of WML. A WML decks
 * contains a collection of cards.
 * (Section 11.5, WAP WML Version 16-Jun-1999)</p>
 *
 * @version $Id: WMLCardElement.java 315461 2000-04-23 18:07:48Z david $
 * @author <a href="mailto:david@topware.com.tw">David Li</a>
 */

public interface WMLCardElement extends WMLElement {

    /**
     * 'onenterbackward' specifies the event to occur when a user
     * agent into a card using a 'go' task
     * (Section 11.5.1, WAP WML Version 16-Jun-1999)
     */
    public void setOnEnterBackward(String href);
    public String getOnEnterBackward();

    /**
     * 'onenterforward' specifies the event to occur when a user
     * agent into a card using a 'prev' task
     * (Section 11.5.1, WAP WML Version 16-Jun-1999)
     */
    public void setOnEnterForward(String href);
    public String getOnEnterForward();

    /**
     * 'onenterbackward' specifies the event to occur when a timer expires
     * (Section 11.5.1, WAP WML Version 16-Jun-1999)
     */
    public void setOnTimer(String href);
    public String getOnTimer();

    /**
     * 'title' specifies a advisory info about the card
     * (Section 11.5.2, WAP WML Version 16-Jun-1999)
     */
    public void setTitle(String newValue);
    public String getTitle();

    /**
     * 'newcontext' specifies whether a browser context should be
     * re-initialized upon entering the card. Default to be false.
     * (Section 11.5.2, WAP WML Version 16-Jun-1999)
     */
    public void setNewContext(boolean newValue);
    public boolean getNewContext();
    
    /**
     *  'ordered' attribute specifies a hit to user agent about the
     *  organization of the card's content 
     * (Section 11.5.2, WAP WML Version 16-Jun-1999)
     */
    public void setOrdered(boolean newValue);
    public boolean getOrdered();

    /**
     * 'xml:lang' specifics the natural or formal language in which
     * the document is written.  
     * (Section 8.8, WAP WML Version 16-Jun-1999) 
     */
    public void setXmlLang(String newValue);
    public String getXmlLang();
}
