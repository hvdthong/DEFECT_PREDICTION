package org.apache.xerces.dom;

import org.w3c.dom.*;

import org.apache.xerces.dom.events.MutationEventImpl;
import org.w3c.dom.events.*;

/**
 * CharacterData is an abstract Node that can carry character data as its
 * Value.  It provides shared behavior for Text, CData, and
 * possibly other node types. All offsets are 0-based.
 * <p>
 * This implementation includes support for DOM Level 2 Mutation Events.
 * If the static boolean NodeImpl.MUTATIONEVENTS is not set true, that support
 * is disabled and can be optimized out to reduce code size.
 *
 * Since this ProcessingInstructionImpl inherits from this class to reuse the
 * setNodeValue method, this class isn't declared as implementing the interface
 * CharacterData. This is done by relevant subclasses (TexImpl, CommentImpl).
 *
 * @version
 * @since  PR-DOM-Level-1-19980818.
 */
public abstract class CharacterDataImpl
    extends ChildNode {


    /** Serialization version. */
    static final long serialVersionUID = 7931170150428474230L;


    protected String data;

    /** Empty child nodes. */
    private static transient NodeList singletonNodeList = new NodeList() {
        public Node item(int index) { return null; }
        public int getLength() { return 0; }
    };


    /** Factory constructor. */
    protected CharacterDataImpl(DocumentImpl ownerDocument, String data) {
        super(ownerDocument);
        this.data = data;
    }


    /** Returns an empty node list. */
    public NodeList getChildNodes() {
        return singletonNodeList;
    }

    /*
     * returns the content of this node
     */
    public String getNodeValue() {
        if (needsSyncData()) {
            synchronizeData();
        }
        return data;
    }

    /** This function added so that we can distinguish whether
     *  setNodeValue has been called from some other DOM functions.
     *  or by the client.<p>
     *  This is important, because we do one type of Range fix-up, 
     *  from the high-level functions in CharacterData, and another
     *  type if the client simply calls setNodeValue(value).
     */
    void setNodeValueInternal(String value) {
        /** flag to indicate whether setNodeValue was called by the
         *  client or from the DOM.
         */
        setValueCalled(true);
        setNodeValue(value);
        setValueCalled(false);
    }
    
    /**
     * Sets the content, possibly firing related events,
     * and updating ranges (via notification to the document)
     */
    public void setNodeValue(String value) {
    	if (isReadOnly())
    		throw new DOMException(
    			DOMException.NO_MODIFICATION_ALLOWED_ERR, 
    			"DOM001 Modification not allowed");
        if (needsSyncData()) {
            synchronizeData();
        }
            
        String oldvalue = this.data;
        EnclosingAttr enclosingAttr=null;
        if(MUTATIONEVENTS)
        {
            LCount lc=LCount.lookup(MutationEventImpl.DOM_ATTR_MODIFIED);
            if(lc.captures+lc.bubbles+lc.defaults>0)
            {
                enclosingAttr=getEnclosingAttr();
            }
            
    	this.data = value;
    	if (!setValueCalled()) {
            ownerDocument().replacedText(this);
        }
    	
        if(MUTATIONEVENTS)
        {
            LCount lc =
                LCount.lookup(MutationEventImpl.DOM_CHARACTER_DATA_MODIFIED);
            if(lc.captures+lc.bubbles+lc.defaults>0)
            {
                MutationEvent me= new MutationEventImpl();
                me.initMutationEvent(
                                 MutationEventImpl.DOM_CHARACTER_DATA_MODIFIED,
                                 true,false,null,oldvalue,value,null,(short)0);
                dispatchEvent(me);
            }
            
            dispatchAggregateEvents(enclosingAttr);



    /**
     * Retrieve character data currently stored in this node.
     * 
     * @throws DOMExcpetion(DOMSTRING_SIZE_ERR) In some implementations,
     * the stored data may exceed the permitted length of strings. If so,
     * getData() will throw this DOMException advising the user to
     * instead retrieve the data in chunks via the substring() operation.  
     */
    public String getData() {
        if (needsSyncData()) {
            synchronizeData();
        }
        return data;
    }

    /** 
     * Report number of characters currently stored in this node's
     * data. It may be 0, meaning that the value is an empty string. 
     */
    public int getLength() {   
        if (needsSyncData()) {
            synchronizeData();
        }
        return data.length();
    }  

    /** 
     * Concatenate additional characters onto the end of the data
     * stored in this node. Note that this, and insert(), are the paths
     * by which a DOM could wind up accumulating more data than the
     * language's strings can easily handle. (See above discussion.)
     * 
     * @throws DOMException(NO_MODIFICATION_ALLOWED_ERR) if node is readonly.
     */
    public void appendData(String data) {

        if (isReadOnly()) {
        	throw new DOMException(
        		DOMException.NO_MODIFICATION_ALLOWED_ERR,
        		"DOM001 Modification not allowed");
        }

        if (needsSyncData()) {
            synchronizeData();
        }
        
        setNodeValue(this.data + data);


    /**
     * Remove a range of characters from the node's value. Throws a
     * DOMException if the offset is beyond the end of the
     * string. However, a deletion _count_ that exceeds the available
     * data is accepted as a delete-to-end request.
     * 
     * @throws DOMException(INDEX_SIZE_ERR) if offset is negative or
     * greater than length, or if count is negative.
     * 
     * @throws DOMException(NO_MODIFICATION_ALLOWED_ERR) if node is
     * readonly.  
     */
    public void deleteData(int offset, int count) 
        throws DOMException {

        if (isReadOnly()) {
        	throw new DOMException(
        		DOMException.NO_MODIFICATION_ALLOWED_ERR, 
        		"DOM001 Modification not allowed");
        }

        if (count < 0) {
        	throw new DOMException(DOMException.INDEX_SIZE_ERR, 
        	                           "DOM004 Index out of bounds");
        }

        if (needsSyncData()) {
            synchronizeData();
        }
        int tailLength = Math.max(data.length() - count - offset, 0);
        try {
            setNodeValueInternal(data.substring(0, offset) +
                                 (tailLength > 0 
		? data.substring(offset + count, offset + count + tailLength) 
                                  : "") );
            ownerDocument().deletedText(this, offset, count);
        }
        catch (StringIndexOutOfBoundsException e) {
        	throw new DOMException(DOMException.INDEX_SIZE_ERR, 
        	                           "DOM004 Index out of bounds");
        }


    /**
     * Insert additional characters into the data stored in this node,
     * at the offset specified.
     *
     * @throws DOMException(INDEX_SIZE_ERR) if offset is negative or
     * greater than length.
     *
     * @throws DOMException(NO_MODIFICATION_ALLOWED_ERR) if node is readonly.  
     */
    public void insertData(int offset, String data) 
        throws DOMException {

        if (isReadOnly()) {
        	throw new DOMException(
        		DOMException.NO_MODIFICATION_ALLOWED_ERR, 
        		"DOM001 Modification not allowed");
        }

        if (needsSyncData()) {
            synchronizeData();
        }
        try {
            setNodeValueInternal(
                new StringBuffer(this.data).insert(offset, data).toString()
                );
            ownerDocument().insertedText(this, offset, data.length());
        }
        catch (StringIndexOutOfBoundsException e) {
        	throw new DOMException(DOMException.INDEX_SIZE_ERR, 
        	                           "DOM004 Index out of bounds");
        }


    /**
     * Replace a series of characters at the specified (zero-based)
     * offset with a new string, NOT necessarily of the same
     * length. Convenience method, equivalent to a delete followed by an
     * insert. Throws a DOMException if the specified offset is beyond
     * the end of the existing data.
     * 
     * @param offset       The offset at which to begin replacing.
     * 
     * @param count        The number of characters to remove, 
     * interpreted as in the delete() method.
     * 
     * @param data         The new string to be inserted at offset in place of
     * the removed data. Note that the entire string will
     * be inserted -- the count parameter does not affect
     * insertion, and the new data may be longer or shorter
     * than the substring it replaces.
     * 
     * @throws DOMException(INDEX_SIZE_ERR) if offset is negative or
     * greater than length, or if count is negative.
     * 
     * @throws DOMException(NO_MODIFICATION_ALLOWED_ERR) if node is
     * readonly.  
     */
    public void replaceData(int offset, int count, String data) 
        throws DOMException {

		deleteData(offset, count);
		insertData(offset, data);


    /**
     * Store character data into this node.
     * 
     * @throws DOMException(NO_MODIFICATION_ALLOWED_ERR) if node is readonly.
     */
    public void setData(String value) 
        throws DOMException {
        setNodeValue(value);
    }

    /** 
     * Substring is more than a convenience function. In some
     * implementations of the DOM, where the stored data may exceed the
     * length that can be returned in a single string, the only way to
     * read it all is to extract it in chunks via this method.
     *
     * @param offset        Zero-based offset of first character to retrieve.
     * @param count Number of characters to retrieve. 
     *
     * If the sum of offset and count exceeds the length, all characters
     * to end of data are returned.
     *
     * @throws DOMException(INDEX_SIZE_ERR) if offset is negative or
     * greater than length, or if count is negative.
     *
     * @throws DOMException(WSTRING_SIZE_ERR) In some implementations,
     * count may exceed the permitted length of strings. If so,
     * substring() will throw this DOMException advising the user to
     * instead retrieve the data in smaller chunks.  
     */
    public String substringData(int offset, int count) 
        throws DOMException {

        if (needsSyncData()) {
            synchronizeData();
        }
        
        int length = data.length();
        if (count < 0 || offset < 0 || offset > length - 1) {
            throw new DOMException(DOMException.INDEX_SIZE_ERR, 
                                       "DOM004 Index out of bounds");
        }

        int tailIndex = Math.min(offset + count, length);

        return data.substring(offset, tailIndex);


