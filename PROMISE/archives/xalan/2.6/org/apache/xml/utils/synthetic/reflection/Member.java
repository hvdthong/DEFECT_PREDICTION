package org.apache.xml.utils.synthetic.reflection;

import org.apache.xml.utils.synthetic.SynthesisException;

/**
 * Member is an interface that reflects identifying
 * information about a single member (a field or a method)
 * or a constructor.
 * <p>
 * Note that this is <strong>not</strong> currently derived from
 * java.lang.reflect.Member, due to questions about how to handle
 * declarignClass.
 *
 * @see org.apache.xml.utils.synthetic.Class
 * @xsl.usage internal
 */
public interface Member
{

  /**
   * Returns the Class object representing the class or
   * interface that declares the member or constructor
   * represented by this Member.
   *
   */
  public abstract org.apache.xml.utils.synthetic.Class getDeclaringClass();

  /**
   * Returns the Java language modifiers for the
   * member or constructor represented by this
   * Member, as an integer. The Modifier class should
   * be used to decode the modifiers in the integer.
   *
   */
  public abstract int getModifiers();

  /**
   * Returns the Class object representing the class or
   * interface that declares the member or constructor
   * represented by this Member.
   *
   * @param declaringClass
   *
   * @throws SynthesisException
   */
  public abstract void setDeclaringClass(
    org.apache.xml.utils.synthetic.Class declaringClass)
      throws SynthesisException;

  /**
   * Returns the Java language modifiers for the
   * member or constructor represented by this
   * Member, as an integer. The Modifier class should
   * be used to decode the modifiers in the integer.
   *
   * @param modifiers
   *
   * @throws SynthesisException
   */
  public abstract void setModifiers(int modifiers) throws SynthesisException;
}
