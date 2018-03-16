package org.apache.xalan.lib.sql;

import java.sql.Connection;
import java.sql.SQLException;

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
   *
   */
  public void setInUse( boolean value )
  {
    inuse = value;
  }

  /**
   * Returns the current status of the PooledConnection.
   *
   */
  public boolean inUse( ) { return inuse; }

  /**
   *  Close the real JDBC Connection
   *
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
