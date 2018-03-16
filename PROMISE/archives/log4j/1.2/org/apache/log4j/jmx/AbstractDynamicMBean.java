package org.apache.log4j.jmx;

import java.util.Iterator;
import javax.management.DynamicMBean;
import javax.management.AttributeList;
import javax.management.Attribute;
import javax.management.RuntimeOperationsException;
import javax.management.MBeanRegistration;
import javax.management.MBeanServer;
import javax.management.ObjectName;

import org.apache.log4j.Logger;

public abstract class AbstractDynamicMBean implements DynamicMBean,
                                                      MBeanRegistration {

  String dClassName;
  MBeanServer server;

  /**
   * Enables the to get the values of several attributes of the Dynamic MBean.
   */
  public
  AttributeList getAttributes(String[] attributeNames) {

    if (attributeNames == null) {
      throw new RuntimeOperationsException(
			   new IllegalArgumentException("attributeNames[] cannot be null"),
			   "Cannot invoke a getter of " + dClassName);
    }

    AttributeList resultList = new AttributeList();

    if (attributeNames.length == 0)
      return resultList;

    for (int i=0 ; i<attributeNames.length ; i++){
      try {
	Object value = getAttribute((String) attributeNames[i]);
	resultList.add(new Attribute(attributeNames[i],value));
      } catch (Exception e) {
	e.printStackTrace();
      }
    }
    return(resultList);
  }

  /**
   * Sets the values of several attributes of the Dynamic MBean, and returns the
   * list of attributes that have been set.
   */
  public AttributeList setAttributes(AttributeList attributes) {

    if (attributes == null) {
      throw new RuntimeOperationsException(
                    new IllegalArgumentException("AttributeList attributes cannot be null"),
		    "Cannot invoke a setter of " + dClassName);
    }
    AttributeList resultList = new AttributeList();

    if (attributes.isEmpty())
      return resultList;

    for (Iterator i = attributes.iterator(); i.hasNext();) {
      Attribute attr = (Attribute) i.next();
      try {
	setAttribute(attr);
	String name = attr.getName();
	Object value = getAttribute(name);
	resultList.add(new Attribute(name,value));
      } catch(Exception e) {
	e.printStackTrace();
      }
    }
    return(resultList);
  }

  protected
  abstract
  Logger getLogger();

  public
  void postDeregister() {
    getLogger().debug("postDeregister is called.");
  }

  public
  void postRegister(java.lang.Boolean registrationDone) {
  }



  public
  void preDeregister() {
    getLogger().debug("preDeregister called.");
  }

  public
  ObjectName preRegister(MBeanServer server, ObjectName name) {
    getLogger().debug("preRegister called. Server="+server+ ", name="+name);
    this.server = server;
    return name;
  }



}
