package org.apache.xalan.lib.sql;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.xml.dtm.DTM;
import org.apache.xml.dtm.DTMManager;

/**
 * The SQL Document is the main controlling class the executesa SQL Query
 */
public class SQLDocument extends DTMDocument
{

  /**
   */
  private boolean DEBUG = false;

  /**
   */


  /**
   */
  private static final String S_SQL = "sql";

  /**
   */
  private static final String S_ROW_SET = "row-set";

  /**
   */
  private static final String S_METADATA = "metadata";

  /**
   */
  private static final String S_COLUMN_HEADER = "column-header";

  /**
   */
  private static final String S_ROW = "row";

  /**
   */
  private static final String S_COL = "col";

  /**
   */
  private static final String S_CATALOGUE_NAME = "catalogue-name";
  /**
   */
  private static final String S_DISPLAY_SIZE = "column-display-size";
  /**
   */
  private static final String S_COLUMN_LABEL = "column-label";
  /**
   */
  private static final String S_COLUMN_NAME = "column-name";
  /**
   */
  private static final String S_COLUMN_TYPE = "column-type";
  /**
   */
  private static final String S_COLUMN_TYPENAME = "column-typename";
  /**
   */
  private static final String S_PRECISION = "precision";
  /**
   */
  private static final String S_SCALE = "scale";
  /**
   */
  private static final String S_SCHEMA_NAME = "schema-name";
  /**
   */
  private static final String S_TABLE_NAME = "table-name";
  /**
   */
  private static final String S_CASESENSITIVE = "case-sensitive";
  /**
   */
  private static final String S_DEFINITELYWRITABLE = "definitely-writable";
  /**
   */
  private static final String S_ISNULLABLE = "nullable";
  /**
   */
  private static final String S_ISSIGNED = "signed";
  /**
   */
  private static final String S_ISWRITEABLE = "writable";
  /**
   */
  private static final String S_ISSEARCHABLE = "searchable";

  /**
   */
  private int m_SQL_TypeID = 0;
  /**
   */
  private int m_MetaData_TypeID = 0;
  /**
   */
  private int m_ColumnHeader_TypeID = 0;
  /**
   */
  private int m_RowSet_TypeID = 0;
  /**
   */
  private int m_Row_TypeID = 0;
  /**
   */
  private int m_Col_TypeID = 0;

  /**
   */
  private int m_ColAttrib_CATALOGUE_NAME_TypeID = 0;
  /**
   */
  private int m_ColAttrib_DISPLAY_SIZE_TypeID = 0;
  /**
   */
  private int m_ColAttrib_COLUMN_LABEL_TypeID = 0;
  /**
   */
  private int m_ColAttrib_COLUMN_NAME_TypeID = 0;
  /**
   */
  private int m_ColAttrib_COLUMN_TYPE_TypeID = 0;
  /**
   */
  private int m_ColAttrib_COLUMN_TYPENAME_TypeID = 0;
  /**
   */
  private int m_ColAttrib_PRECISION_TypeID = 0;
  /**
   */
  private int m_ColAttrib_SCALE_TypeID = 0;
  /**
   */
  private int m_ColAttrib_SCHEMA_NAME_TypeID = 0;
  /**
   */
  private int m_ColAttrib_TABLE_NAME_TypeID = 0;
  /**
   */
  private int m_ColAttrib_CASESENSITIVE_TypeID = 0;
  /**
   */
  private int m_ColAttrib_DEFINITELYWRITABLE_TypeID = 0;
  /**
   */
  private int m_ColAttrib_ISNULLABLE_TypeID = 0;
  /**
   */
  private int m_ColAttrib_ISSIGNED_TypeID = 0;
  /**
   */
  private int m_ColAttrib_ISWRITEABLE_TypeID = 0;
  /**
   */
  private int m_ColAttrib_ISSEARCHABLE_TypeID = 0;

  /**
   * The DBMS Connection used to produce this SQL Document.
   * Will be used to clear free up the database resources on
   * close.
   */
  private Connection m_Connection = null;

  /**
   * The Statement used to extract the data from the Database connection.
   * We really don't need the connection, but it is NOT defined from
   * JDBC Driver to driver what happens to the ResultSet if the statment
   * is closed prior to reading all the data needed. So as long as we are
   * using the ResultSet, we will track the Statement used to produce it.
   */
  private Statement m_Statement = null;

  /**
   * The conduit to our data that will be used to fill the document.
   */
  private ResultSet m_ResultSet = null;

  /**
   * The Connection Pool that originally produced the connection.
   */
  private ConnectionPool m_ConnectionPool = null;


  /**
   * As the column header array is built, keep the node index
   * for each Column.
   * The primary use of this is to locate the first attribute for
   * each column in each row as we add records.
   */
  private int[] m_ColHeadersIdx;

  /**
   * An indicator on how many columns are in this query
   */
  private int m_ColCount;

  /**
   * The Index of the MetaData Node. Currently the MetaData Node contains the
   *
   */
  private int m_MetaDataIdx = DTM.NULL;

  /**
   * The index of the Row Set node. This is the sibling directly after
   * the last Column Header.
   */
  private int m_RowSetIdx = DTM.NULL;

  /**
   */
  private int m_SQLIdx = DTM.NULL;

  /**
   * Demark the first row element where we started adding rows into the
   * Document.
   */
  private int m_FirstRowIdx = DTM.NULL;

  /**
   * Keep track of the Last row inserted into the DTM from the ResultSet.
   * This will be used as the index of the parent Row Element when adding
   * a row.
   */
  private int m_LastRowIdx = DTM.NULL;

  /**
   * Streaming Mode Control, In Streaming mode we reduce the memory
   * footprint since we only use a single row instance.
   */
  private boolean m_StreamingMode = true;

  /**
   * @param mgr
   * @param ident
   * @param pool
   * @param con
   * @param stmt
   * @param data
   * @param streamingMode
   * @throws SQLException
   */
  public SQLDocument( DTMManager mgr, int ident, ConnectionPool pool, Connection con, Statement stmt, ResultSet data, boolean streamingMode )throws SQLException
  {
    super(mgr, ident);

    m_Connection = con;
    m_Statement  = stmt;
    m_ResultSet  = data;
    m_ConnectionPool = pool;
    m_StreamingMode = streamingMode;

    createExpandedNameTable();
    extractSQLMetaData(m_ResultSet.getMetaData());

    addRowToDTMFromResultSet();

  }


  /**
   * Extract the Meta Data and build the Column Attribute List.
   * @param meta
   * @return
   */
  private void extractSQLMetaData( ResultSetMetaData meta )
  {

    m_DocumentIdx = addElement(0, m_Document_TypeID, DTM.NULL, DTM.NULL);

    m_SQLIdx = addElement(1, m_SQL_TypeID,  m_DocumentIdx, DTM.NULL);

    m_MetaDataIdx = addElement(1, m_MetaData_TypeID,  m_SQLIdx, DTM.NULL);

    try
    {
      m_ColCount = meta.getColumnCount();
      m_ColHeadersIdx = new int[m_ColCount];
    }
    catch(Exception e)
    {
      error("ERROR Extracting Metadata");
    }

    int lastColHeaderIdx = DTM.NULL;

    int i = 1;
    for (i=1; i<= m_ColCount; i++)
    {
      m_ColHeadersIdx[i-1] =
        addElement(2,m_ColumnHeader_TypeID, m_MetaDataIdx, lastColHeaderIdx);

      lastColHeaderIdx = m_ColHeadersIdx[i-1];

      try
      {
        addAttributeToNode(
          meta.getColumnName(i),
          m_ColAttrib_COLUMN_NAME_TypeID, lastColHeaderIdx);
      }
      catch(Exception e)
      {
        addAttributeToNode(
          S_ATTRIB_NOT_SUPPORTED,
          m_ColAttrib_COLUMN_NAME_TypeID, lastColHeaderIdx);
      }

      try
      {
        addAttributeToNode(
          meta.getColumnLabel(i),
          m_ColAttrib_COLUMN_LABEL_TypeID, lastColHeaderIdx);
      }
      catch(Exception e)
      {
        addAttributeToNode(
          S_ATTRIB_NOT_SUPPORTED,
          m_ColAttrib_COLUMN_LABEL_TypeID, lastColHeaderIdx);
      }

      try
      {
        addAttributeToNode(
          meta.getCatalogName(i),
          m_ColAttrib_CATALOGUE_NAME_TypeID, lastColHeaderIdx);
      }
      catch(Exception e)
      {
        addAttributeToNode(
          S_ATTRIB_NOT_SUPPORTED,
          m_ColAttrib_CATALOGUE_NAME_TypeID, lastColHeaderIdx);
      }

      try
      {
        addAttributeToNode(
          new Integer(meta.getColumnDisplaySize(i)),
          m_ColAttrib_DISPLAY_SIZE_TypeID, lastColHeaderIdx);
      }
      catch(Exception e)
      {
        addAttributeToNode(
          S_ATTRIB_NOT_SUPPORTED,
          m_ColAttrib_DISPLAY_SIZE_TypeID, lastColHeaderIdx);
      }

      try
      {
        addAttributeToNode(
          new Integer(meta.getColumnType(i)),
          m_ColAttrib_COLUMN_TYPE_TypeID, lastColHeaderIdx);
      }
      catch(Exception e)
      {
        addAttributeToNode(
          S_ATTRIB_NOT_SUPPORTED,
          m_ColAttrib_COLUMN_TYPE_TypeID, lastColHeaderIdx);
      }

      try
      {
        addAttributeToNode(
          meta.getColumnTypeName(i),
          m_ColAttrib_COLUMN_TYPENAME_TypeID, lastColHeaderIdx);
      }
      catch(Exception e)
      {
        addAttributeToNode(
          S_ATTRIB_NOT_SUPPORTED,
          m_ColAttrib_COLUMN_TYPENAME_TypeID, lastColHeaderIdx);
      }

      try
      {
        addAttributeToNode(
          new Integer(meta.getPrecision(i)),
          m_ColAttrib_PRECISION_TypeID, lastColHeaderIdx);
      }
      catch(Exception e)
      {
        addAttributeToNode(
          S_ATTRIB_NOT_SUPPORTED,
          m_ColAttrib_PRECISION_TypeID, lastColHeaderIdx);
      }
      try
      {
        addAttributeToNode(
          new Integer(meta.getScale(i)),
          m_ColAttrib_SCALE_TypeID, lastColHeaderIdx);
      }
      catch(Exception e)
      {
        addAttributeToNode(
          S_ATTRIB_NOT_SUPPORTED,
          m_ColAttrib_SCALE_TypeID, lastColHeaderIdx);
      }

      try
      {
        addAttributeToNode(
          meta.getSchemaName(i),
          m_ColAttrib_SCHEMA_NAME_TypeID, lastColHeaderIdx);
      }
      catch(Exception e)
      {
        addAttributeToNode(
          S_ATTRIB_NOT_SUPPORTED,
          m_ColAttrib_SCHEMA_NAME_TypeID, lastColHeaderIdx);
      }
      try
      {
        addAttributeToNode(
          meta.getTableName(i),
          m_ColAttrib_TABLE_NAME_TypeID, lastColHeaderIdx);
      }
      catch(Exception e)
      {
        addAttributeToNode(
          S_ATTRIB_NOT_SUPPORTED,
          m_ColAttrib_TABLE_NAME_TypeID, lastColHeaderIdx);
      }

      try
      {
        addAttributeToNode(
          meta.isCaseSensitive(i) ? S_ISTRUE : S_ISFALSE,
          m_ColAttrib_CASESENSITIVE_TypeID, lastColHeaderIdx);
      }
      catch(Exception e)
      {
        addAttributeToNode(
          S_ATTRIB_NOT_SUPPORTED,
          m_ColAttrib_CASESENSITIVE_TypeID, lastColHeaderIdx);
      }

      try
      {
        addAttributeToNode(
          meta.isDefinitelyWritable(i) ? S_ISTRUE : S_ISFALSE,
          m_ColAttrib_DEFINITELYWRITABLE_TypeID, lastColHeaderIdx);
      }
      catch(Exception e)
      {
        addAttributeToNode(
          S_ATTRIB_NOT_SUPPORTED,
          m_ColAttrib_DEFINITELYWRITABLE_TypeID, lastColHeaderIdx);
      }

      try
      {
        addAttributeToNode(
          meta.isNullable(i) != 0 ? S_ISTRUE : S_ISFALSE,
          m_ColAttrib_ISNULLABLE_TypeID, lastColHeaderIdx);
      }
      catch(Exception e)
      {
        addAttributeToNode(
          S_ATTRIB_NOT_SUPPORTED,
          m_ColAttrib_ISNULLABLE_TypeID, lastColHeaderIdx);
      }

      try
      {
        addAttributeToNode(
          meta.isSigned(i) ? S_ISTRUE : S_ISFALSE,
          m_ColAttrib_ISSIGNED_TypeID, lastColHeaderIdx);
      }
      catch(Exception e)
      {
        addAttributeToNode(
          S_ATTRIB_NOT_SUPPORTED,
          m_ColAttrib_ISSIGNED_TypeID, lastColHeaderIdx);
      }

      try
      {
        addAttributeToNode(
          meta.isWritable(i) == true ? S_ISTRUE : S_ISFALSE,
          m_ColAttrib_ISWRITEABLE_TypeID, lastColHeaderIdx);
      }
      catch(Exception e)
      {
        addAttributeToNode(
          S_ATTRIB_NOT_SUPPORTED,
          m_ColAttrib_ISWRITEABLE_TypeID, lastColHeaderIdx);
      }

      try
      {
        addAttributeToNode(
          meta.isSearchable(i) == true ? S_ISTRUE : S_ISFALSE,
          m_ColAttrib_ISSEARCHABLE_TypeID, lastColHeaderIdx);
      }
      catch(Exception e)
      {
        addAttributeToNode(
          S_ATTRIB_NOT_SUPPORTED,
          m_ColAttrib_ISSEARCHABLE_TypeID, lastColHeaderIdx);
      }

    }

  }

  /**
   * Populate the Expanded Name Table with the Node that we will use.
   * Keep a reference of each of the types for access speed.
   * @return
   */
  protected void createExpandedNameTable( )
  {
    super.createExpandedNameTable();

    m_SQL_TypeID =
      m_expandedNameTable.getExpandedTypeID(S_NAMESPACE, S_SQL, DTM.ELEMENT_NODE);

    m_MetaData_TypeID =
      m_expandedNameTable.getExpandedTypeID(S_NAMESPACE, S_METADATA, DTM.ELEMENT_NODE);

    m_ColumnHeader_TypeID =
      m_expandedNameTable.getExpandedTypeID(S_NAMESPACE, S_COLUMN_HEADER, DTM.ELEMENT_NODE);
    m_RowSet_TypeID =
      m_expandedNameTable.getExpandedTypeID(S_NAMESPACE, S_ROW_SET, DTM.ELEMENT_NODE);
    m_Row_TypeID =
      m_expandedNameTable.getExpandedTypeID(S_NAMESPACE, S_ROW, DTM.ELEMENT_NODE);
    m_Col_TypeID =
      m_expandedNameTable.getExpandedTypeID(S_NAMESPACE, S_COL, DTM.ELEMENT_NODE);


    m_ColAttrib_CATALOGUE_NAME_TypeID =
      m_expandedNameTable.getExpandedTypeID(S_NAMESPACE, S_CATALOGUE_NAME, DTM.ATTRIBUTE_NODE);
    m_ColAttrib_DISPLAY_SIZE_TypeID =
      m_expandedNameTable.getExpandedTypeID(S_NAMESPACE, S_DISPLAY_SIZE, DTM.ATTRIBUTE_NODE);
    m_ColAttrib_COLUMN_LABEL_TypeID =
      m_expandedNameTable.getExpandedTypeID(S_NAMESPACE, S_COLUMN_LABEL, DTM.ATTRIBUTE_NODE);
    m_ColAttrib_COLUMN_NAME_TypeID =
      m_expandedNameTable.getExpandedTypeID(S_NAMESPACE, S_COLUMN_NAME, DTM.ATTRIBUTE_NODE);
    m_ColAttrib_COLUMN_TYPE_TypeID =
      m_expandedNameTable.getExpandedTypeID(S_NAMESPACE, S_COLUMN_TYPE, DTM.ATTRIBUTE_NODE);
    m_ColAttrib_COLUMN_TYPENAME_TypeID =
      m_expandedNameTable.getExpandedTypeID(S_NAMESPACE, S_COLUMN_TYPENAME, DTM.ATTRIBUTE_NODE);
    m_ColAttrib_PRECISION_TypeID =
      m_expandedNameTable.getExpandedTypeID(S_NAMESPACE, S_PRECISION, DTM.ATTRIBUTE_NODE);
    m_ColAttrib_SCALE_TypeID =
      m_expandedNameTable.getExpandedTypeID(S_NAMESPACE, S_SCALE, DTM.ATTRIBUTE_NODE);
    m_ColAttrib_SCHEMA_NAME_TypeID =
      m_expandedNameTable.getExpandedTypeID(S_NAMESPACE, S_SCHEMA_NAME, DTM.ATTRIBUTE_NODE);
    m_ColAttrib_TABLE_NAME_TypeID =
      m_expandedNameTable.getExpandedTypeID(S_NAMESPACE, S_TABLE_NAME, DTM.ATTRIBUTE_NODE);
    m_ColAttrib_CASESENSITIVE_TypeID =
      m_expandedNameTable.getExpandedTypeID(S_NAMESPACE, S_CASESENSITIVE, DTM.ATTRIBUTE_NODE);
    m_ColAttrib_DEFINITELYWRITABLE_TypeID =
      m_expandedNameTable.getExpandedTypeID(S_NAMESPACE, S_DEFINITELYWRITABLE, DTM.ATTRIBUTE_NODE);
    m_ColAttrib_ISNULLABLE_TypeID =
      m_expandedNameTable.getExpandedTypeID(S_NAMESPACE, S_ISNULLABLE, DTM.ATTRIBUTE_NODE);
    m_ColAttrib_ISSIGNED_TypeID =
      m_expandedNameTable.getExpandedTypeID(S_NAMESPACE, S_ISSIGNED, DTM.ATTRIBUTE_NODE);
    m_ColAttrib_ISWRITEABLE_TypeID =
      m_expandedNameTable.getExpandedTypeID(S_NAMESPACE, S_ISWRITEABLE, DTM.ATTRIBUTE_NODE);
    m_ColAttrib_ISSEARCHABLE_TypeID =
      m_expandedNameTable.getExpandedTypeID(S_NAMESPACE, S_ISSEARCHABLE, DTM.ATTRIBUTE_NODE);
  }


  /**
   * Pull a record from the result set and map it to a DTM based ROW
   * If we are in Streaming mode, then only create a single row and
   * keep copying the data into the same row. This will keep the memory
   * footprint constint independant of the RecordSet Size. If we are not
   * in Streaming mode then create ROWS for the whole tree.
   * @return
   */
  private boolean addRowToDTMFromResultSet( )
  {
    try
    {


      if (m_RowSetIdx == DTM.NULL)
      {
        m_RowSetIdx = addElement(1, m_RowSet_TypeID,  m_SQLIdx, m_MetaDataIdx);
      }

      if ( ! m_ResultSet.next())
      {

        if (m_StreamingMode && (m_LastRowIdx != DTM.NULL))
        {
          m_nextsib.setElementAt(DTM.NULL, m_LastRowIdx);
        }

        return false;
      }

      if (m_FirstRowIdx == DTM.NULL)
      {
        m_FirstRowIdx =
          addElement(2, m_Row_TypeID, m_RowSetIdx, DTM.NULL);
        m_LastRowIdx = m_FirstRowIdx;

        if (m_StreamingMode)
        {
          m_nextsib.setElementAt(m_LastRowIdx, m_LastRowIdx);
        }

      }
      else
      {
        if (! m_StreamingMode)
        {
          m_LastRowIdx = addElement(3, m_Row_TypeID, m_RowSetIdx, m_LastRowIdx);
        }
      }

      int colID = _firstch(m_LastRowIdx);

      int pcolID = DTM.NULL;

      for (int i=1; i<= m_ColCount; i++)
      {
        Object o = m_ResultSet.getObject(i);

        if (colID == DTM.NULL)
        {
          pcolID = addElementWithData(o,3,m_Col_TypeID, m_LastRowIdx, pcolID);
          cloneAttributeFromNode(pcolID, m_ColHeadersIdx[i-1]);
        }
        else
        {
          int dataIdent = _firstch(colID);
          if (dataIdent == DTM.NULL)
          {
            error("Streaming Mode, Data Error");
          }
          else
          {
            m_ObjectArray.setAt(dataIdent, o);
          }

        if (colID != DTM.NULL)
        {
          colID = _nextsib(colID);
        }

    }
    catch(Exception e)
    {
      if (DEBUG)
      {
        System.out.println(
          "SQL Error Fetching next row [" + e.getLocalizedMessage() + "]");
      }

      error("SQL Error Fetching next row [" + e.getLocalizedMessage() + "]");
    }

    return true;
  }


  /**
   * Clean up our ties to the database but this does not necessarly
   * clean up the document.
   * @return
   */
  public void close( )
  {
    if (DEBUG) System.out.println("close()");

    try { if (null != m_ResultSet) m_ResultSet.close(); }
    catch(Exception e) { }
    try { if (null != m_Statement) m_Statement.close(); }
    catch(Exception e) { }
    try {
      if (null != m_Connection)
        m_ConnectionPool.releaseConnection(m_Connection);
    } catch(Exception e) { }
  }

  /**
   * When an error occurs, the XConnection will call this method
   * do that we can deal with the Connection properly
   * @return
   */
  public void closeOnError( )
  {
    if (DEBUG) System.out.println("close()");

    try  { if (null != m_ResultSet) m_ResultSet.close();   }
    catch(Exception e) { }
    try  { if (null != m_Statement) m_Statement.close();
    } catch(Exception e) { }
    try {
      if (null != m_Connection)
        m_ConnectionPool.releaseConnectionOnError(m_Connection);
    } catch(Exception e) { }
  }



  /**
   * @return
   */
  protected boolean nextNode( )
  {
    if (DEBUG) System.out.println("nextNode()");
    try
    {
      return false;
    }
    catch(Exception e)
    {
      return false;
    }
  }

  /**
   * @param identity
   * @return
   */
  protected int _nextsib( int identity )
  {

    int id = _exptype(identity);
    if (
      ( id == m_Row_TypeID) &&
      (identity >= m_LastRowIdx))
    {
      if (DEBUG) System.out.println("reading from the ResultSet");
      addRowToDTMFromResultSet();
    }

    return super._nextsib(identity);
  }

  public void documentRegistration()
  {
    if (DEBUG) System.out.println("Document Registration");
  }

  public void documentRelease()
  {
    if (DEBUG) System.out.println("Document Release");
  }


}
