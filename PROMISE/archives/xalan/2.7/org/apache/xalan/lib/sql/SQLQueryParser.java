package org.apache.xalan.lib.sql;

import java.util.*;
import java.sql.*;
import org.apache.xpath.objects.*;
import org.apache.xalan.extensions.ExpressionContext;
import org.apache.xml.utils.QName;
import javax.xml.transform.TransformerException;



public class SQLQueryParser
{
  /**
   * If the parser used inline parser to pull out variables then
   * this will be true. The default is not to use the Inline Parser.
   */
  private boolean           m_InlineVariables  = false;

  /**
   *
   */
  private boolean           m_IsCallable = false;

  /**
   *
   */
  private String            m_OrigQuery = null;

  /**
   *
   */
  private StringBuffer      m_ParsedQuery = null;

  /**
   *
   */
  private Vector            m_Parameters = null;

  /**
   *
   */
  private boolean           m_hasOutput = false;

  /**
   *
   */
  private boolean           m_HasParameters;

  public static final int         NO_OVERRIDE = 0;
  public static final int         NO_INLINE_PARSER = 1;
  public static final int         INLINE_PARSER  = 2;

  /**
   * The SQLStatement Parser will be created as a psuedo SINGLETON per
   * XConnection. Since we are only caching the Query and its parsed results
   * we may be able to use this as a real SINGLETON. It all depends on how
   * Statement Caching will play out.
   */
  public SQLQueryParser()
  {
    init();
  }

  /**
   * Constructor, used to create a new parser entry
   */
  private SQLQueryParser(String query)
  {
    m_OrigQuery = query;
  }

  /**
   * On a per Xconnection basis, we will create a SQLStatemenetParser, from
   * this parser, individual parsers will be created. The Init method is defined
   * to initialize all the internal structures that maintains the pool of parsers.
   */
  private void init()
  {
  }

  /**
   * Produce an SQL Statement Parser based on the incomming query.
   *
   * For now we will just create a new object, in the future we may have this
   * interface cache the queries so that we can take advantage of a preparsed
   * String.
   *
   * If the Inline Parser is not enabled in the Options, no action will be
   * taken on the parser. This option can be set by the Stylesheet. If the
   * option is not set or cleared, a default value will be set determined
   * by the way variables were passed into the system.
   */
  public SQLQueryParser parse(XConnection xconn, String query, int override)
  {
    SQLQueryParser parser = new SQLQueryParser(query);

    parser.parse(xconn, override);

    return parser;
  }



  /**
   * Produce an SQL Statement Parser based on the incomming query.
   *
   * For now we will just create a new object, in the future we may have this
   * interface cache the queries so that we can take advantage of a preparsed
   * String.
   *
   * If the Inline Parser is not enabled in the Options, no action will be
   * taken on the parser. This option can be set by the Stylesheet. If the
   * option is not set or cleared, a default value will be set determined
   * by the way variables were passed into the system.
   */
  private void parse(XConnection xconn, int override)
  {

    m_InlineVariables = "true".equals(xconn.getFeature("inline-variables"));
    if (override == NO_INLINE_PARSER) m_InlineVariables = false;
    else if (override == INLINE_PARSER) m_InlineVariables = true;

    if (m_InlineVariables) inlineParser();

  }

  /**
   * If a SQL Statement does not have any parameters, then it can be executed
   * directly. Most SQL Servers use this as a performance advantage since no
   * parameters need to be parsed then bound.
   */
  public boolean hasParameters()
  {
    return m_HasParameters;
  }

  /**
   * If the Inline Parser is used, the parser will note if this stastement is
   * a plain SQL Statement or a Called Procedure. Called Procudures generally
   * have output parameters and require special handling.
   *
   * Called Procudures that are not processed with the Inline Parser will
   * still be executed but under the context of a PreparedStatement and
   * not a CallableStatement. Called Procudures that have output parameters
   * MUST be handled with the Inline Parser.
   */
  public boolean isCallable()
  {
    return m_IsCallable;
  }


  /**
   *
   */
  public Vector getParameters()
  {
    return m_Parameters;
  }

  /**
   * The XConnection will use this method to store the Parameters
   * that were supplied by the style sheet in the case where the
   * inline parser was not used
   */
  public void setParameters(Vector p)
  {
    m_HasParameters = true;
    m_Parameters = p;
  }

  /**
   * Return a copy of the parsed SQL query that will be set to the
   * Database system to execute. If the inline parser was not used,
   * then the original query will be returned.
   */
  public String getSQLQuery()
  {
    if (m_InlineVariables) return m_ParsedQuery.toString();
    else return m_OrigQuery;
  }


  /**
   * The SQL Statement Parser, when an Inline Parser is used, tracks the XSL
   * variables used to populate a statement. The data use to popoulate a
   * can also be provided. If the data is provided, it will overide the
   * populastion using XSL variables. When the Inline PArser is not used, then
   * the Data will always be provided.
   *
   */
  public void populateStatement(PreparedStatement stmt, ExpressionContext ctx)
  {

    for ( int indx = 0 ; indx < m_Parameters.size() ; indx++ )
    {
      QueryParameter parm = (QueryParameter) m_Parameters.elementAt(indx);

      try
      {

        if (m_InlineVariables)
        {
          XObject value = (XObject)ctx.getVariableOrParam(new QName(parm.getName()));

          if (value != null)
          {
            stmt.setObject(
              indx + 1,
              value.object(),
          }
          else
          {
            stmt.setNull(indx + 1, parm.getType());
          }
        }
        else
        {
          String value = parm.getValue();

          if (value != null)
          {
            stmt.setObject(
              indx + 1,
              value,
          }
          else
          {
            stmt.setNull(indx + 1, parm.getType());
          }
        }
      }
      catch (Exception tx)
      {
      }
    }

  }

  public void registerOutputParameters(CallableStatement cstmt) throws SQLException
  {
    if ( m_IsCallable && m_hasOutput )
    {
      for ( int indx = 0 ; indx < m_Parameters.size() ; indx++ )
      {
        QueryParameter parm = (QueryParameter) m_Parameters.elementAt(indx);
        if ( parm.isOutput() )
        {
          cstmt.registerOutParameter(indx + 1, parm.getType());
        }
      }
    }
 }

  /**
   *
   */
  protected void inlineParser()
  {
    QueryParameter  curParm = null;
    int	            state = 0;
    StringBuffer    tok = new StringBuffer();
    boolean         firstword = true;

    if (m_Parameters == null) m_Parameters = new Vector();

    if (m_ParsedQuery == null) m_ParsedQuery = new StringBuffer();

    for ( int idx = 0 ; idx < m_OrigQuery.length() ; idx++ )
    {
      char ch = m_OrigQuery.charAt(idx);
      switch ( state )
      {

          if ( ch == '\'' ) state = 1;
          else if ( ch == '?' ) state = 4;
          else if ( firstword && (Character.isLetterOrDigit(ch) || ch == '#') )
          {
            tok.append(ch);
            state = 3;
          }
          m_ParsedQuery.append(ch);
          break;

          if ( ch == '\'' ) state = 0;
          else if ( ch == '\\' ) state = 2;
          m_ParsedQuery.append(ch);
          break;

          state = 1;
          m_ParsedQuery.append(ch);
          break;

          if ( Character.isLetterOrDigit(ch) || ch == '#' || ch == '_' ) tok.append(ch);
          else
          {
            if ( tok.toString().equalsIgnoreCase("call") )
            {
              m_IsCallable = true;
              if ( curParm != null )
              {
                curParm.setIsOutput(true);
              }
            }
            firstword = false;
            tok = new StringBuffer();
            if ( ch == '\'' ) state = 1;
            else if ( ch == '?' ) state = 4;
            else state = 0;
          }

          m_ParsedQuery.append(ch);
          break;

          if ( ch == '[' ) state = 5;
          break;

          if ( !Character.isWhitespace(ch) && ch != '=' )
          {
            tok.append(Character.toUpperCase(ch));
          }
          else if ( tok.length() > 0 )
          {
            m_HasParameters = true;

            curParm = new QueryParameter();

            curParm.setTypeName(tok.toString());
            m_Parameters.addElement(curParm);
            tok = new StringBuffer();
            if ( ch == '=' ) state = 7;
            else state = 6;
          }
          break;

          if ( ch == '=' ) state = 7;
          break;

          if ( !Character.isWhitespace(ch) && ch != ']' ) tok.append(ch);
          else if ( tok.length() > 0 )
          {
            curParm.setName(tok.toString());
            tok = new StringBuffer();
            if ( ch == ']' )
            {
              state = 0;
            }
            else state = 8;
          }
          break;

          if ( !Character.isWhitespace(ch) && ch != ']' )
          {
            tok.append(ch);
          }
          else if ( tok.length() > 0 )
          {
            tok.setLength(3);
            if ( tok.toString().equalsIgnoreCase("OUT") )
            {
              curParm.setIsOutput(true);
              m_hasOutput = true;
            }

            tok = new StringBuffer();
            if ( ch == ']' )
            {
              state = 0;
            }
          }
          break;
      }
    }


    if ( m_IsCallable )
    {
      m_ParsedQuery.insert(0, '{');
      m_ParsedQuery.append('}');
    }

  }

}

