package org.w3c.dom;

/**
 * <code>EntityReference</code> objects may be inserted into the structure 
 * model when an entity reference is in the source document, or when the 
 * user wishes to insert an entity reference. Note that character references 
 * and references to predefined entities are considered to be expanded by 
 * the HTML or XML processor so that characters are represented by their 
 * Unicode equivalent rather than by an entity reference. Moreover, the XML 
 * processor may completely expand references to entities while building the 
 * structure model, instead of providing <code>EntityReference</code> 
 * objects. If it does provide such objects, then for a given 
 * <code>EntityReference</code> node, it may be that there is no 
 * <code>Entity</code> node representing the referenced entity. If such an 
 * <code>Entity</code> exists, then the subtree of the 
 * <code>EntityReference</code> node is in general a copy of the 
 * <code>Entity</code> node subtree. However, this may not be true when an 
 * entity contains an unbound namespace prefix. In such a case, because the 
 * namespace prefix resolution depends on where the entity reference is, the 
 * descendants of the <code>EntityReference</code> node may be bound to 
 * different namespace URIs.
 * <p>As for <code>Entity</code> nodes, <code>EntityReference</code> nodes and 
 * all their descendants are readonly.
 */
public interface EntityReference extends Node {
}
