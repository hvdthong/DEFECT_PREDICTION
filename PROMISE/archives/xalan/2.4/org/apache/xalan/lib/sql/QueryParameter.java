package org.apache.xalan.lib.sql;

/**
 * Title:        <p>
 * Description:  <p>
 * Copyright:    Copyright (c) <p>
 * Company:      <p>
 * @author
 * @version 1.0
 */
public class QueryParameter         
{

  /**
   */
  private String value;
  /**
   */
  private String type;

  /**
   * @param v
   * @param t
   */
  public QueryParameter( String v, String t )
  {
    value = v;
    type = t;
  }

  /**
   * @return
   */
  public String getValue( ) {
    return value;
  }

  /**
   * @param newValue
   * @return
   */
  public void setValue( String newValue ) {
    value = newValue;
  }

  /**
   * @param newType
   * @return
   */
  public void setType( String newType ) {
    type = newType;
  }

  /**
   * @return
   */
  public String getType( ) {
    return type;
  }
}
