package org.apache.xalan.lib.sql;

import java.sql.SQLException;

import org.apache.xml.dtm.DTM;
import org.apache.xml.dtm.DTMManager;

/**
 *
 * A base class that will convert an exception into an XML stream
 * that can be returned in place of the standard result. The XML
 * format returned is a follows.
 *
 * <ext-error>
 *  <message> The Message for a generic error </message>
 *  <sql-error>
 *    <message> SQL Message from the Exception thrown </message>
 *    <code> SQL Error Code </stack>
 *  </sql-error>
 * <ext-error>
 *
 */

/**
 * The SQL Document is the main controlling class the executesa SQL Query
 */
public class SQLErrorDocument extends DTMDocument
{
  /**
   */
  private static final String S_EXT_ERROR = "ext-error";
  /**
   */
  private static final String S_SQL_ERROR = "sql-error";
  /**
   */
  private static final String S_MESSAGE = "message";
  /**
   */
  private static final String S_CODE = "code";

  /**
   */
  private int m_ErrorExt_TypeID = DTM.NULL;
  /**
   */
  private int m_Message_TypeID = DTM.NULL;
  /**
   */
  private int m_Code_TypeID = DTM.NULL;

  /**
   */
  private int m_SQLError_TypeID = DTM.NULL;

  /**
   */
  private int m_rootID = DTM.NULL;
  /**
   */
  private int m_extErrorID = DTM.NULL;
  /**
   */
  private int m_MainMessageID = DTM.NULL;

  /**
   * Build up an SQLErrorDocument that includes the basic error information
   * along with the Extended SQL Error information.
   * @param mgr
   * @param ident
   * @param error
   */
  public SQLErrorDocument( DTMManager mgr, int ident, SQLException error )
  {
    super(mgr, ident);

    createExpandedNameTable();
    buildBasicStructure(error);

    int sqlError = addElement(2, m_SQLError_TypeID, m_extErrorID, m_MainMessageID);
    int element = DTM.NULL;

    element = addElementWithData(
      new Integer(error.getErrorCode()), 3,
      m_Code_TypeID, sqlError, element);

    element = addElementWithData(
      error.getLocalizedMessage(), 3,
      m_Message_TypeID, sqlError, element);

  }


  /**
   * Build up an Error Exception with just the Standard Error Information
   * @param mgr
   * @param ident
   * @param error
   */
  public SQLErrorDocument( DTMManager mgr, int ident, Exception error )
  {
    super(mgr, ident);
    createExpandedNameTable();
    buildBasicStructure(error);
  }

  /**
   * Build up the basic structure that is common for each error.
   * @param e
   * @return
   */
  private void buildBasicStructure( Exception e )
  {
    m_rootID = addElement(0, m_Document_TypeID, DTM.NULL, DTM.NULL);
    m_extErrorID = addElement(1, m_ErrorExt_TypeID, m_rootID, DTM.NULL);
    m_MainMessageID = addElementWithData
      (e.getLocalizedMessage(), 2, m_Message_TypeID, m_extErrorID, DTM.NULL);
  }

  /**
   * Populate the Expanded Name Table with the Node that we will use.
   * Keep a reference of each of the types for access speed.
   * @return
   */
  protected void createExpandedNameTable( )
  {

    super.createExpandedNameTable();

    m_ErrorExt_TypeID =
      m_expandedNameTable.getExpandedTypeID(S_NAMESPACE, S_EXT_ERROR, DTM.ELEMENT_NODE);

    m_SQLError_TypeID =
      m_expandedNameTable.getExpandedTypeID(S_NAMESPACE, S_SQL_ERROR, DTM.ELEMENT_NODE);

    m_Message_TypeID =
      m_expandedNameTable.getExpandedTypeID(S_NAMESPACE, S_MESSAGE, DTM.ELEMENT_NODE);

    m_Code_TypeID =
      m_expandedNameTable.getExpandedTypeID(S_NAMESPACE, S_CODE, DTM.ELEMENT_NODE);
  }

}
