package org.apache.xerces.dom.events;

import org.apache.xerces.dom.*;
import org.w3c.dom.*;
import org.w3c.dom.events.*;

public class MutationEventImpl 
extends org.apache.xerces.dom.events.EventImpl 
implements MutationEvent
{
    Node relatedNode=null;
    String prevValue=null,newValue=null,attrName=null;
    
    public static final String DOM_SUBTREE_MODIFIED = "DOMSubtreeModified";
    public static final String DOM_NODE_INSERTED = "DOMNodeInserted";
    public static final String DOM_NODE_REMOVED = "DOMNodeRemoved";
    public static final String DOM_NODE_REMOVED_FROM_DOCUMENT = "DOMNodeRemovedFromDocument";
    public static final String DOM_NODE_INSERTED_INTO_DOCUMENT = "DOMNodeInsertedIntoDocument";
    public static final String DOM_ATTR_MODIFIED = "DOMAttrModified";
    public static final String DOM_CHARACTER_DATA_MODIFIED = "DOMCharacterDataModified";

    /** @return the name of the Attr which
        changed, for DOMAttrModified events. 
        Undefined for others.
        */
    public String getAttrName()
    {
        return attrName;
    }

    /** @return the new string value of the Attr for DOMAttrModified events, or
        of the CharacterData node for DOMCharDataModifed events.
        Undefined for others.
        */
    public String getNewValue()
    {
        return newValue;
    }

    /** @return the previous string value of the Attr for DOMAttrModified events, or
        of the CharacterData node for DOMCharDataModifed events.
        Undefined for others.
        */
    public String getPrevValue()
    {
        return prevValue;
    }

    /** @return a Node related to this event, other than the target that the
        node was dispatched to. For DOMNodeRemoved, it is the node which
        was removed. 
        No other uses are currently defined.
        */
    public Node getRelatedNode()
    {
        return relatedNode;
    }

    /** Initialize a mutation event, or overwrite the event's current
        settings with new values of the parameters. 
        */
    public void initMutationEvent(String typeArg, boolean canBubbleArg, 
        boolean cancelableArg, Node relatedNodeArg, String prevValueArg, 
        String newValueArg, String attrNameArg)
    {
        relatedNode=relatedNodeArg;
        prevValue=prevValueArg;
        newValue=newValueArg;
        attrName=attrNameArg;
        super.initEvent(typeArg,canBubbleArg,cancelableArg);
    }

}
