package org.apache.xerces.dom;

import org.w3c.dom.Node;
import org.w3c.dom.ProcessingInstruction;

/**
 * Processing Instructions (PIs) permit documents to carry
 * processor-specific information alongside their actual content. PIs
 * are most common in XML, but they are supported in HTML as well.
 *
 * This class inherits from CharacterDataImpl to reuse its setNodeValue method.
 *
 * @version
 * @since  PR-DOM-Level-1-19980818.
 */
public class ProcessingInstructionImpl
    extends CharacterDataImpl
    implements ProcessingInstruction {


    /** Serialization version. */
    static final long serialVersionUID = 7554435174099981510L;


    protected String target;


    /** Factory constructor. */
    public ProcessingInstructionImpl(CoreDocumentImpl ownerDoc,
                                     String target, String data) {
        super(ownerDoc, data);
        this.target = target;
    }


    /**
     * A short integer indicating what type of node this is. The named
     * constants for this value are defined in the org.w3c.dom.Node interface.
     */
    public short getNodeType() {
        return Node.PROCESSING_INSTRUCTION_NODE;
    }

    /**
     * Returns the target
     */
    public String getNodeName() {
        if (needsSyncData()) {
            synchronizeData();
        }
        return target;
    }


    /**
     * A PI's "target" states what processor channel the PI's data
     * should be directed to. It is defined differently in HTML and XML.
     * <p>
     * In XML, a PI's "target" is the first (whitespace-delimited) token
     * following the "<?" token that begins the PI.
     * <p>
     * In HTML, target is always null.
     * <p>
     * Note that getNodeName is aliased to getTarget.
     */
    public String getTarget() {
        if (needsSyncData()) {
            synchronizeData();
        }
        return target;


    /**
     * A PI's data content tells the processor what we actually want it
     * to do.  It is defined slightly differently in HTML and XML.
     * <p>
     * In XML, the data begins with the non-whitespace character
     * immediately after the target -- @see getTarget().
     * <p>
     * In HTML, the data begins with the character immediately after the
     * "&lt;?" token that begins the PI.
     * <p>
     * Note that getNodeValue is aliased to getData
     */
    public String getData() {
        if (needsSyncData()) {
            synchronizeData();
        }
        return data;


    /**
     * Change the data content of this PI.
     * Note that setData is aliased to setNodeValue.
     * @see #getData().
     * @throws DOMException(NO_MODIFICATION_ALLOWED_ERR) if node is read-only.
     */
    public void setData(String data) {
        setNodeValue(data);

