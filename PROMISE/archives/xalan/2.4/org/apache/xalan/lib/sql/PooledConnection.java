package org.apache.xalan.lib.sql;

import java.sql.*;
import java.sql.Connection;

/**
 */
public class PooledConnection
{

  /**
   */
  private Connection connection = null;
  /**
   */
  private boolean inuse = false;

  /**
   * @param value
   */
  public PooledConnection( Connection value )
  {
    if ( value != null ) { connection = value; }
  }

  /**
   * Returns a reference to the JDBC Connection
   * @return Connection
   */
  public Connection getConnection( )
  {
    return connection;
  }

  /**
   * Set the status of the PooledConnection.
   *
   * @param value
   * @return
   */
  public void setInUse( boolean value )
  {
    inuse = value;
  }

  /**
   * Returns the current status of the PooledConnection.
   * @return
   */
  public boolean inUse( ) { return inuse; }

  /**
   *  Close the real JDBC Connection
   * @return
   */
  public void close( )
  {
    try
    {
      connection.close();
    }
    catch (SQLException sqle)
    {
      System.err.println(sqle.getMessage());
    }
  }
}
