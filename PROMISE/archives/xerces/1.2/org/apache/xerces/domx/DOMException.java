 
package org.apache.xerces.domx; 

/**
 * DOM operations only raise exceptions in "exceptional" circumstances, i.e.,
 * when an operation is impossible to perform (either for logical reasons,
 * because data is lost, or  because the implementation has become unstable).
 * In general, DOM methods return specific error values in ordinary
 * processing situation, such as out-of-bound errors when using
 * <code>NodeList</code>.
 * <p>Implementations may raise other exceptions under other circumstances.
 * For example, implementations may raise an implementation-dependent
 * exception if a <code>null</code> argument is passed.
 * <p>Some languages and object systems do not support the concept of
 * exceptions. For such systems, error conditions may be indicated using
 * native error reporting mechanisms. For some bindings, for example, methods
 * may return error codes similar to those listed in the corresponding method
 * descriptions.
 */
public abstract class DOMException extends org.w3c.dom.DOMException {
  public static final short           UNSPECIFIED_EVENT_TYPE= 100;
  public static final short           UNSUPPORTED_EVENT_TYPE= 101;

 
  public DOMException(short code, String message) {
         super(code,message);
  }
}
