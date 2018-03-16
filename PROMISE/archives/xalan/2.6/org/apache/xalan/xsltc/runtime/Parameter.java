package org.apache.xalan.xsltc.runtime;

/**
 * @author Jacek Ambroziak
 * @author Santiago Pericas-Geertsen
 * @author Morten Jorgensen
 */
public class Parameter {

    public String  _name;
    public Object  _value;
    public boolean _isDefault;

    public Parameter(String name, Object value) {
	_name = name;
	_value = value;
	_isDefault = true;
    }

    public Parameter(String name, Object value, boolean isDefault) {
	_name = name;
	_value = value;
	_isDefault = isDefault;
    }
}
