package org.apache.xpath.compiler;

import java.util.Vector;
import java.util.Hashtable;

import org.apache.xml.utils.PrefixResolver;
import org.apache.xpath.XPathProcessorException;
import org.apache.xpath.res.XPATHErrorResources;
import org.apache.xpath.compiler.Compiler;
import org.apache.xpath.objects.XString;
import org.apache.xpath.objects.XNumber;
import org.apache.xalan.res.XSLMessages;

import javax.xml.transform.TransformerException;
import org.xml.sax.Locator;
import org.xml.sax.helpers.LocatorImpl;

import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.ErrorListener;

/**
 * <meta name="usage" content="general"/>
 * Tokenizes and parses XPath expressions. This should really be named
 * XPathParserImpl, and may be renamed in the future.
 */
public class XPathParser
{
	static public final String CONTINUE_AFTER_FATAL_ERROR="CONTINUE_AFTER_FATAL_ERROR";

  /**
   * The XPath to be processed.
   */
  private OpMap m_ops;

  /**
   * The next token in the pattern.
   */
  transient String m_token;

  /**
   * The first char in m_token, the theory being that this
   * is an optimization because we won't have to do charAt(0) as
   * often.
   */
  transient char m_tokenChar = 0;

  /**
   * The position in the token queue is tracked by m_queueMark.
   */
  int m_queueMark = 0;

  /**
   * Results from checking FilterExpr syntax
   */
  protected final static int FILTER_MATCH_FAILED     = 0;
  protected final static int FILTER_MATCH_PRIMARY    = 1;
  protected final static int FILTER_MATCH_PREDICATES = 2;

  /**
   * The parser constructor.
   */
  public XPathParser(ErrorListener errorListener, javax.xml.transform.SourceLocator sourceLocator)
  {
    m_errorListener = errorListener;
    m_sourceLocator = sourceLocator;
  }

  /**
   * The prefix resolver to map prefixes to namespaces in the OpMap.
   */
  PrefixResolver m_namespaceContext;

  /**
   * Given an string, init an XPath object for selections,
   * in order that a parse doesn't
   * have to be done each time the expression is evaluated.
   * 
   * @param compiler The compiler object.
   * @param expression A string conforming to the XPath grammar.
   * @param namespaceContext An object that is able to resolve prefixes in
   * the XPath to namespaces.
   *
   * @throws javax.xml.transform.TransformerException
   */
  public void initXPath(
          Compiler compiler, String expression, PrefixResolver namespaceContext)
            throws javax.xml.transform.TransformerException
  {

    m_ops = compiler;
    m_namespaceContext = namespaceContext;

    Lexer lexer = new Lexer(compiler, namespaceContext, this);

    lexer.tokenize(expression);

    m_ops.m_opMap[0] = OpCodes.OP_XPATH;
    m_ops.m_opMap[OpMap.MAPINDEX_LENGTH] = 2;
    
    
	try {

      nextToken();
      Expr();

      if (null != m_token)
      {
        String extraTokens = "";

        while (null != m_token)
        {
          extraTokens += "'" + m_token + "'";

          nextToken();

          if (null != m_token)
            extraTokens += ", ";
        }

        error(XPATHErrorResources.ER_EXTRA_ILLEGAL_TOKENS,
      }

    } 
    catch (org.apache.xpath.XPathProcessorException e)
    {
	  if(CONTINUE_AFTER_FATAL_ERROR.equals(e.getMessage()))
	  {
		initXPath(compiler, "/..",  namespaceContext);
	  }
	  else
		throw e;
    }

    compiler.shrink();
  }

  /**
   * Given an string, init an XPath object for pattern matches,
   * in order that a parse doesn't
   * have to be done each time the expression is evaluated.
   * @param compiler The XPath object to be initialized.
   * @param expression A String representing the XPath.
   * @param namespaceContext An object that is able to resolve prefixes in
   * the XPath to namespaces.
   *
   * @throws javax.xml.transform.TransformerException
   */
  public void initMatchPattern(
          Compiler compiler, String expression, PrefixResolver namespaceContext)
            throws javax.xml.transform.TransformerException
  {

    m_ops = compiler;
    m_namespaceContext = namespaceContext;

    Lexer lexer = new Lexer(compiler, namespaceContext, this);

    lexer.tokenize(expression);

    m_ops.m_opMap[0] = OpCodes.OP_MATCHPATTERN;
    m_ops.m_opMap[OpMap.MAPINDEX_LENGTH] = 2;

    nextToken();
    Pattern();

    if (null != m_token)
    {
      String extraTokens = "";

      while (null != m_token)
      {
        extraTokens += "'" + m_token + "'";

        nextToken();

        if (null != m_token)
          extraTokens += ", ";
      }

      error(XPATHErrorResources.ER_EXTRA_ILLEGAL_TOKENS,
    }

    m_ops.m_opMap[m_ops.m_opMap[OpMap.MAPINDEX_LENGTH]] = OpCodes.ENDOP;
    m_ops.m_opMap[OpMap.MAPINDEX_LENGTH] += 1;

    m_ops.shrink();
  }

  /** The error listener where syntax errors are to be sent.
   */
  private ErrorListener m_errorListener;
  
  /** The source location of the XPath. */
  javax.xml.transform.SourceLocator m_sourceLocator;

  /**
   * Allow an application to register an error event handler, where syntax 
   * errors will be sent.  If the error listener is not set, syntax errors 
   * will be sent to System.err.
   * 
   * @param handler Reference to error listener where syntax errors will be 
   *                sent.
   */
  public void setErrorHandler(ErrorListener handler)
  {
    m_errorListener = handler;
  }

  /**
   * Return the current error listener.
   *
   * @return The error listener, which should not normally be null, but may be.
   */
  public ErrorListener getErrorListener()
  {
    return m_errorListener;
  }

  /**
   * Check whether m_token matches the target string. 
   *
   * @param s A string reference or null.
   *
   * @return If m_token is null, returns false (or true if s is also null), or 
   * return true if the current token matches the string, else false.
   */
  final boolean tokenIs(String s)
  {
    return (m_token != null) ? (m_token.equals(s)) : (s == null);
  }

  /**
   * Check whether m_tokenChar==c. 
   *
   * @param c A character to be tested.
   *
   * @return If m_token is null, returns false, or return true if c matches 
   *         the current token.
   */
  final boolean tokenIs(char c)
  {
    return (m_token != null) ? (m_tokenChar == c) : false;
  }

  /**
   * Look ahead of the current token in order to
   * make a branching decision.
   *
   * @param c the character to be tested for.
   * @param n number of tokens to look ahead.  Must be
   * greater than 1.
   *
   * @return true if the next token matches the character argument.
   */
  final boolean lookahead(char c, int n)
  {

    int pos = (m_queueMark + n);
    boolean b;

    if ((pos <= m_ops.m_tokenQueueSize) && (pos > 0)
            && (m_ops.m_tokenQueueSize != 0))
    {
      String tok = ((String) m_ops.m_tokenQueue[pos - 1]);

      b = (tok.length() == 1) ? (tok.charAt(0) == c) : false;
    }
    else
    {
      b = false;
    }

    return b;
  }

  /**
   * Look behind the first character of the current token in order to
   * make a branching decision.
   * 
   * @param c the character to compare it to.
   * @param n number of tokens to look behind.  Must be
   * greater than 1.  Note that the look behind terminates
   * at either the beginning of the string or on a '|'
   * character.  Because of this, this method should only
   * be used for pattern matching.
   *
   * @return true if the token behind the current token matches the character 
   *         argument.
   */
  private final boolean lookbehind(char c, int n)
  {

    boolean isToken;
    int lookBehindPos = m_queueMark - (n + 1);

    if (lookBehindPos >= 0)
    {
      String lookbehind = (String) m_ops.m_tokenQueue[lookBehindPos];

      if (lookbehind.length() == 1)
      {
        char c0 = (lookbehind == null) ? '|' : lookbehind.charAt(0);

        isToken = (c0 == '|') ? false : (c0 == c);
      }
      else
      {
        isToken = false;
      }
    }
    else
    {
      isToken = false;
    }

    return isToken;
  }

  /**
   * look behind the current token in order to
   * see if there is a useable token.
   * 
   * @param n number of tokens to look behind.  Must be
   * greater than 1.  Note that the look behind terminates
   * at either the beginning of the string or on a '|'
   * character.  Because of this, this method should only
   * be used for pattern matching.
   * 
   * @return true if look behind has a token, false otherwise.
   */
  private final boolean lookbehindHasToken(int n)
  {

    boolean hasToken;

    if ((m_queueMark - n) > 0)
    {
      String lookbehind = (String) m_ops.m_tokenQueue[m_queueMark - (n - 1)];
      char c0 = (lookbehind == null) ? '|' : lookbehind.charAt(0);

      hasToken = (c0 == '|') ? false : true;
    }
    else
    {
      hasToken = false;
    }

    return hasToken;
  }

  /**
   * Look ahead of the current token in order to
   * make a branching decision.
   * 
   * @param s the string to compare it to.
   * @param n number of tokens to lookahead.  Must be
   * greater than 1.
   *
   * @return true if the token behind the current token matches the string 
   *         argument.
   */
  private final boolean lookahead(String s, int n)
  {

    boolean isToken;

    if ((m_queueMark + n) <= m_ops.m_tokenQueueSize)
    {
      String lookahead = (String) m_ops.m_tokenQueue[m_queueMark + (n - 1)];

      isToken = (lookahead != null) ? lookahead.equals(s) : (s == null);
    }
    else
    {
      isToken = (null == s);
    }

    return isToken;
  }

  /**
   * Retrieve the next token from the command and
   * store it in m_token string.
   */
  private final void nextToken()
  {

    if (m_queueMark < m_ops.m_tokenQueueSize)
    {
      m_token = (String) m_ops.m_tokenQueue[m_queueMark++];
      m_tokenChar = m_token.charAt(0);
    }
    else
    {
      m_token = null;
      m_tokenChar = 0;
    }
  }

  /**
   * Retrieve a token relative to the current token.
   * 
   * @param i Position relative to current token.
   *
   * @return The string at the given index, or null if the index is out 
   *         of range.
   */
  private final String getTokenRelative(int i)
  {

    String tok;
    int relative = m_queueMark + i;

    if ((relative > 0) && (relative < m_ops.m_tokenQueueSize))
    {
      tok = (String) m_ops.m_tokenQueue[relative];
    }
    else
    {
      tok = null;
    }

    return tok;
  }

  /**
   * Retrieve the previous token from the command and
   * store it in m_token string.
   */
  private final void prevToken()
  {

    if (m_queueMark > 0)
    {
      m_queueMark--;

      m_token = (String) m_ops.m_tokenQueue[m_queueMark];
      m_tokenChar = m_token.charAt(0);
    }
    else
    {
      m_token = null;
      m_tokenChar = 0;
    }
  }

  /**
   * Consume an expected token, throwing an exception if it
   * isn't there.
   *
   * @param expected The string to be expected.
   *
   * @throws javax.xml.transform.TransformerException
   */
  private final void consumeExpected(String expected)
          throws javax.xml.transform.TransformerException
  {

    if (tokenIs(expected))
    {
      nextToken();
    }
    else
    {
      error(XPATHErrorResources.ER_EXPECTED_BUT_FOUND, new Object[]{ expected,

		throw new XPathProcessorException(CONTINUE_AFTER_FATAL_ERROR);
	}
  }

  /**
   * Consume an expected token, throwing an exception if it
   * isn't there.
   *
   * @param expected the character to be expected.
   *
   * @throws javax.xml.transform.TransformerException
   */
  private final void consumeExpected(char expected)
          throws javax.xml.transform.TransformerException
  {

    if (tokenIs(expected))
    {
      nextToken();
    }
    else
    {
      error(XPATHErrorResources.ER_EXPECTED_BUT_FOUND,
            new Object[]{ String.valueOf(expected),

		throw new XPathProcessorException(CONTINUE_AFTER_FATAL_ERROR);
    }
  }

  /**
   * Warn the user of a problem.
   *
   * @param msg An error number that corresponds to one of the numbers found 
   *            in {@link org.apache.xpath.res.XPATHErrorResources}, which is 
   *            a key for a format string.
   * @param args An array of arguments represented in the format string, which 
   *             may be null.
   *
   * @throws TransformerException if the current ErrorListoner determines to 
   *                              throw an exception.
   */
  void warn(int msg, Object[] args) throws TransformerException
  {

    String fmsg = XSLMessages.createXPATHWarning(msg, args);
    ErrorListener ehandler = this.getErrorListener();

    if (null != ehandler)
    {
      ehandler.warning(new TransformerException(fmsg, m_sourceLocator));
    }
    else
    {
      System.err.println(fmsg);
    }
  }

  /**
   * Notify the user of an assertion error, and probably throw an
   * exception.
   *
   * @param b  If false, a runtime exception will be thrown.
   * @param msg The assertion message, which should be informative.
   * 
   * @throws RuntimeException if the b argument is false.
   */
  private void assertion(boolean b, String msg)
  {

    if (!b)
    {
      String fMsg = XSLMessages.createXPATHMessage(
        XPATHErrorResources.ER_INCORRECT_PROGRAMMER_ASSERTION,
        new Object[]{ msg });

      throw new RuntimeException(fMsg);
    }
  }

  /**
   * Notify the user of an error, and probably throw an
   * exception.
   *
   * @param msg An error number that corresponds to one of the numbers found 
   *            in {@link org.apache.xpath.res.XPATHErrorResources}, which is 
   *            a key for a format string.
   * @param args An array of arguments represented in the format string, which 
   *             may be null.
   *
   * @throws TransformerException if the current ErrorListoner determines to 
   *                              throw an exception.
   */
  void error(int msg, Object[] args) throws TransformerException
  {

    String fmsg = XSLMessages.createXPATHMessage(msg, args);
    ErrorListener ehandler = this.getErrorListener();

    TransformerException te = new TransformerException(fmsg, m_sourceLocator);
    if (null != ehandler)
    {
      ehandler.fatalError(te);
    }
    else
    {
      throw te;
    }
  }

  /**
   * Dump the remaining token queue.
   * Thanks to Craig for this.
   *
   * @return A dump of the remaining token queue, which may be appended to 
   *         an error message.
   */
  protected String dumpRemainingTokenQueue()
  {

    int q = m_queueMark;
    String returnMsg;

    if (q < m_ops.m_tokenQueueSize)
    {
      String msg = "\n Remaining tokens: (";

      while (q < m_ops.m_tokenQueueSize)
      {
        String t = (String) m_ops.m_tokenQueue[q++];

        msg += (" '" + t + "'");
      }

      returnMsg = msg + ")";
    }
    else
    {
      returnMsg = "";
    }

    return returnMsg;
  }

  /**
   * Given a string, return the corresponding function token.
   *
   * @param key A local name of a function.
   *
   * @return   The function ID, which may correspond to one of the FUNC_XXX 
   *    values found in {@link org.apache.xpath.compiler.FunctionTable}, but may 
   *    be a value installed by an external module.
   */
  final int getFunctionToken(String key)
  {

    int tok;

    try
    {
      tok = ((Integer) (Keywords.m_functions.get(key))).intValue();
    }
    catch (NullPointerException npe)
    {
      tok = -1;
    }
    catch (ClassCastException cce)
    {
      tok = -1;
    }

    return tok;
  }

  /**
   * Insert room for operation.  This will NOT set
   * the length value of the operation, but will update
   * the length value for the total expression.
   *
   * @param pos The position where the op is to be inserted.
   * @param length The length of the operation space in the op map.
   * @param op The op code to the inserted.
   */
  void insertOp(int pos, int length, int op)
  {

    int totalLen = m_ops.m_opMap[OpMap.MAPINDEX_LENGTH];

    for (int i = totalLen - 1; i >= pos; i--)
    {
      m_ops.m_opMap[i + length] = m_ops.m_opMap[i];
    }

    m_ops.m_opMap[pos] = op;
    m_ops.m_opMap[OpMap.MAPINDEX_LENGTH] = totalLen + length;
  }

  /**
   * Insert room for operation.  This WILL set
   * the length value of the operation, and will update
   * the length value for the total expression.
   *
   * @param length The length of the operation.
   * @param op The op code to the inserted.
   */
  void appendOp(int length, int op)
  {

    int totalLen = m_ops.m_opMap[OpMap.MAPINDEX_LENGTH];

    m_ops.m_opMap[totalLen] = op;
    m_ops.m_opMap[totalLen + OpMap.MAPINDEX_LENGTH] = length;
    m_ops.m_opMap[OpMap.MAPINDEX_LENGTH] = totalLen + length;
  }


  /**
   *
   *
   * Expr  ::=  OrExpr
   *
   *
   * @throws javax.xml.transform.TransformerException
   */
  protected void Expr() throws javax.xml.transform.TransformerException
  {
    OrExpr();
  }

  /**
   *
   *
   * OrExpr  ::=  AndExpr
   * | OrExpr 'or' AndExpr
   *
   *
   * @throws javax.xml.transform.TransformerException
   */
  protected void OrExpr() throws javax.xml.transform.TransformerException
  {

    int opPos = m_ops.m_opMap[OpMap.MAPINDEX_LENGTH];

    AndExpr();

    if ((null != m_token) && tokenIs("or"))
    {
      nextToken();
      insertOp(opPos, 2, OpCodes.OP_OR);
      OrExpr();

      m_ops.m_opMap[opPos + OpMap.MAPINDEX_LENGTH] =
        m_ops.m_opMap[OpMap.MAPINDEX_LENGTH] - opPos;
    }
  }

  /**
   *
   *
   * AndExpr  ::=  EqualityExpr
   * | AndExpr 'and' EqualityExpr
   *
   *
   * @throws javax.xml.transform.TransformerException
   */
  protected void AndExpr() throws javax.xml.transform.TransformerException
  {

    int opPos = m_ops.m_opMap[OpMap.MAPINDEX_LENGTH];

    EqualityExpr(-1);

    if ((null != m_token) && tokenIs("and"))
    {
      nextToken();
      insertOp(opPos, 2, OpCodes.OP_AND);
      AndExpr();

      m_ops.m_opMap[opPos + OpMap.MAPINDEX_LENGTH] =
        m_ops.m_opMap[OpMap.MAPINDEX_LENGTH] - opPos;
    }
  }

  /**
   *
   * @returns an Object which is either a String, a Number, a Boolean, or a vector
   * of nodes.
   *
   * EqualityExpr  ::=  RelationalExpr
   * | EqualityExpr '=' RelationalExpr
   *
   *
   * @param addPos Position where expression is to be added, or -1 for append.
   *
   * @return the position at the end of the equality expression.
   *
   * @throws javax.xml.transform.TransformerException
   */
  protected int EqualityExpr(int addPos) throws javax.xml.transform.TransformerException
  {

    int opPos = m_ops.m_opMap[OpMap.MAPINDEX_LENGTH];

    if (-1 == addPos)
      addPos = opPos;

    RelationalExpr(-1);

    if (null != m_token)
    {
      if (tokenIs('!') && lookahead('=', 1))
      {
        nextToken();
        nextToken();
        insertOp(addPos, 2, OpCodes.OP_NOTEQUALS);

        int opPlusLeftHandLen = m_ops.m_opMap[OpMap.MAPINDEX_LENGTH] - addPos;

        addPos = EqualityExpr(addPos);
        m_ops.m_opMap[addPos + OpMap.MAPINDEX_LENGTH] =
          m_ops.m_opMap[addPos + opPlusLeftHandLen + 1] + opPlusLeftHandLen;
        addPos += 2;
      }
      else if (tokenIs('='))
      {
        nextToken();
        insertOp(addPos, 2, OpCodes.OP_EQUALS);

        int opPlusLeftHandLen = m_ops.m_opMap[OpMap.MAPINDEX_LENGTH] - addPos;

        addPos = EqualityExpr(addPos);
        m_ops.m_opMap[addPos + OpMap.MAPINDEX_LENGTH] =
          m_ops.m_opMap[addPos + opPlusLeftHandLen + 1] + opPlusLeftHandLen;
        addPos += 2;
      }
    }

    return addPos;
  }

  /**
   * .
   * @returns an Object which is either a String, a Number, a Boolean, or a vector
   * of nodes.
   *
   * RelationalExpr  ::=  AdditiveExpr
   * | RelationalExpr '<' AdditiveExpr
   * | RelationalExpr '>' AdditiveExpr
   * | RelationalExpr '<=' AdditiveExpr
   * | RelationalExpr '>=' AdditiveExpr
   *
   *
   * @param addPos Position where expression is to be added, or -1 for append.
   *
   * @return the position at the end of the relational expression.
   *
   * @throws javax.xml.transform.TransformerException
   */
  protected int RelationalExpr(int addPos) throws javax.xml.transform.TransformerException
  {

    int opPos = m_ops.m_opMap[OpMap.MAPINDEX_LENGTH];

    if (-1 == addPos)
      addPos = opPos;

    AdditiveExpr(-1);

    if (null != m_token)
    {
      if (tokenIs('<'))
      {
        nextToken();

        if (tokenIs('='))
        {
          nextToken();
          insertOp(addPos, 2, OpCodes.OP_LTE);
        }
        else
        {
          insertOp(addPos, 2, OpCodes.OP_LT);
        }

        int opPlusLeftHandLen = m_ops.m_opMap[OpMap.MAPINDEX_LENGTH] - addPos;

        addPos = RelationalExpr(addPos);
        m_ops.m_opMap[addPos + OpMap.MAPINDEX_LENGTH] =
          m_ops.m_opMap[addPos + opPlusLeftHandLen + 1] + opPlusLeftHandLen;
        addPos += 2;
      }
      else if (tokenIs('>'))
      {
        nextToken();

        if (tokenIs('='))
        {
          nextToken();
          insertOp(addPos, 2, OpCodes.OP_GTE);
        }
        else
        {
          insertOp(addPos, 2, OpCodes.OP_GT);
        }

        int opPlusLeftHandLen = m_ops.m_opMap[OpMap.MAPINDEX_LENGTH] - addPos;

        addPos = RelationalExpr(addPos);
        m_ops.m_opMap[addPos + OpMap.MAPINDEX_LENGTH] =
          m_ops.m_opMap[addPos + opPlusLeftHandLen + 1] + opPlusLeftHandLen;
        addPos += 2;
      }
    }

    return addPos;
  }

  /**
   * This has to handle construction of the operations so that they are evaluated
   * in pre-fix order.  So, for 9+7-6, instead of |+|9|-|7|6|, this needs to be
   * evaluated as |-|+|9|7|6|.
   *
   * AdditiveExpr  ::=  MultiplicativeExpr
   * | AdditiveExpr '+' MultiplicativeExpr
   * | AdditiveExpr '-' MultiplicativeExpr
   *
   *
   * @param addPos Position where expression is to be added, or -1 for append.
   *
   * @return the position at the end of the equality expression.
   *
   * @throws javax.xml.transform.TransformerException
   */
  protected int AdditiveExpr(int addPos) throws javax.xml.transform.TransformerException
  {

    int opPos = m_ops.m_opMap[OpMap.MAPINDEX_LENGTH];

    if (-1 == addPos)
      addPos = opPos;

    MultiplicativeExpr(-1);

    if (null != m_token)
    {
      if (tokenIs('+'))
      {
        nextToken();
        insertOp(addPos, 2, OpCodes.OP_PLUS);

        int opPlusLeftHandLen = m_ops.m_opMap[OpMap.MAPINDEX_LENGTH] - addPos;

        addPos = AdditiveExpr(addPos);
        m_ops.m_opMap[addPos + OpMap.MAPINDEX_LENGTH] =
          m_ops.m_opMap[addPos + opPlusLeftHandLen + 1] + opPlusLeftHandLen;
        addPos += 2;
      }
      else if (tokenIs('-'))
      {
        nextToken();
        insertOp(addPos, 2, OpCodes.OP_MINUS);

        int opPlusLeftHandLen = m_ops.m_opMap[OpMap.MAPINDEX_LENGTH] - addPos;

        addPos = AdditiveExpr(addPos);
        m_ops.m_opMap[addPos + OpMap.MAPINDEX_LENGTH] =
          m_ops.m_opMap[addPos + opPlusLeftHandLen + 1] + opPlusLeftHandLen;
        addPos += 2;
      }
    }

    return addPos;
  }

  /**
   * This has to handle construction of the operations so that they are evaluated
   * in pre-fix order.  So, for 9+7-6, instead of |+|9|-|7|6|, this needs to be
   * evaluated as |-|+|9|7|6|.
   *
   * MultiplicativeExpr  ::=  UnaryExpr
   * | MultiplicativeExpr MultiplyOperator UnaryExpr
   * | MultiplicativeExpr 'div' UnaryExpr
   * | MultiplicativeExpr 'mod' UnaryExpr
   * | MultiplicativeExpr 'quo' UnaryExpr
   *
   * @param addPos Position where expression is to be added, or -1 for append.
   *
   * @return the position at the end of the equality expression.
   *
   * @throws javax.xml.transform.TransformerException
   */
  protected int MultiplicativeExpr(int addPos) throws javax.xml.transform.TransformerException
  {

    int opPos = m_ops.m_opMap[OpMap.MAPINDEX_LENGTH];

    if (-1 == addPos)
      addPos = opPos;

    UnaryExpr();

    if (null != m_token)
    {
      if (tokenIs('*'))
      {
        nextToken();
        insertOp(addPos, 2, OpCodes.OP_MULT);

        int opPlusLeftHandLen = m_ops.m_opMap[OpMap.MAPINDEX_LENGTH] - addPos;

        addPos = MultiplicativeExpr(addPos);
        m_ops.m_opMap[addPos + OpMap.MAPINDEX_LENGTH] =
          m_ops.m_opMap[addPos + opPlusLeftHandLen + 1] + opPlusLeftHandLen;
        addPos += 2;
      }
      else if (tokenIs("div"))
      {
        nextToken();
        insertOp(addPos, 2, OpCodes.OP_DIV);

        int opPlusLeftHandLen = m_ops.m_opMap[OpMap.MAPINDEX_LENGTH] - addPos;

        addPos = MultiplicativeExpr(addPos);
        m_ops.m_opMap[addPos + OpMap.MAPINDEX_LENGTH] =
          m_ops.m_opMap[addPos + opPlusLeftHandLen + 1] + opPlusLeftHandLen;
        addPos += 2;
      }
      else if (tokenIs("mod"))
      {
        nextToken();
        insertOp(addPos, 2, OpCodes.OP_MOD);

        int opPlusLeftHandLen = m_ops.m_opMap[OpMap.MAPINDEX_LENGTH] - addPos;

        addPos = MultiplicativeExpr(addPos);
        m_ops.m_opMap[addPos + OpMap.MAPINDEX_LENGTH] =
          m_ops.m_opMap[addPos + opPlusLeftHandLen + 1] + opPlusLeftHandLen;
        addPos += 2;
      }
      else if (tokenIs("quo"))
      {
        nextToken();
        insertOp(addPos, 2, OpCodes.OP_QUO);

        int opPlusLeftHandLen = m_ops.m_opMap[OpMap.MAPINDEX_LENGTH] - addPos;

        addPos = MultiplicativeExpr(addPos);
        m_ops.m_opMap[addPos + OpMap.MAPINDEX_LENGTH] =
          m_ops.m_opMap[addPos + opPlusLeftHandLen + 1] + opPlusLeftHandLen;
        addPos += 2;
      }
    }

    return addPos;
  }

  /**
   *
   * UnaryExpr  ::=  UnionExpr
   * | '-' UnaryExpr
   *
   *
   * @throws javax.xml.transform.TransformerException
   */
  protected void UnaryExpr() throws javax.xml.transform.TransformerException
  {

    int opPos = m_ops.m_opMap[OpMap.MAPINDEX_LENGTH];
    boolean isNeg = false;

    if (m_tokenChar == '-')
    {
      nextToken();
      appendOp(2, OpCodes.OP_NEG);

      isNeg = true;
    }

    UnionExpr();

    if (isNeg)
      m_ops.m_opMap[opPos + OpMap.MAPINDEX_LENGTH] =
        m_ops.m_opMap[OpMap.MAPINDEX_LENGTH] - opPos;
  }

  /**
   *
   * StringExpr  ::=  Expr
   *
   *
   * @throws javax.xml.transform.TransformerException
   */
  protected void StringExpr() throws javax.xml.transform.TransformerException
  {

    int opPos = m_ops.m_opMap[OpMap.MAPINDEX_LENGTH];

    appendOp(2, OpCodes.OP_STRING);
    Expr();

    m_ops.m_opMap[opPos + OpMap.MAPINDEX_LENGTH] =
      m_ops.m_opMap[OpMap.MAPINDEX_LENGTH] - opPos;
  }

  /**
   *
   *
   * StringExpr  ::=  Expr
   *
   *
   * @throws javax.xml.transform.TransformerException
   */
  protected void BooleanExpr() throws javax.xml.transform.TransformerException
  {

    int opPos = m_ops.m_opMap[OpMap.MAPINDEX_LENGTH];

    appendOp(2, OpCodes.OP_BOOL);
    Expr();

    int opLen = m_ops.m_opMap[OpMap.MAPINDEX_LENGTH] - opPos;

    if (opLen == 2)
    {
    }

    m_ops.m_opMap[opPos + OpMap.MAPINDEX_LENGTH] = opLen;
  }

  /**
   *
   *
   * NumberExpr  ::=  Expr
   *
   *
   * @throws javax.xml.transform.TransformerException
   */
  protected void NumberExpr() throws javax.xml.transform.TransformerException
  {

    int opPos = m_ops.m_opMap[OpMap.MAPINDEX_LENGTH];

    appendOp(2, OpCodes.OP_NUMBER);
    Expr();

    m_ops.m_opMap[opPos + OpMap.MAPINDEX_LENGTH] =
      m_ops.m_opMap[OpMap.MAPINDEX_LENGTH] - opPos;
  }

  /**
   * The context of the right hand side expressions is the context of the
   * left hand side expression. The results of the right hand side expressions
   * are node sets. The result of the left hand side UnionExpr is the union
   * of the results of the right hand side expressions.
   *
   *
   * UnionExpr    ::=    PathExpr
   * | UnionExpr '|' PathExpr
   *
   *
   * @throws javax.xml.transform.TransformerException
   */
  protected void UnionExpr() throws javax.xml.transform.TransformerException
  {

    int opPos = m_ops.m_opMap[OpMap.MAPINDEX_LENGTH];
    boolean continueOrLoop = true;
    boolean foundUnion = false;

    do
    {
      PathExpr();

      if (tokenIs('|'))
      {
        if (false == foundUnion)
        {
          foundUnion = true;

          insertOp(opPos, 2, OpCodes.OP_UNION);
        }

        nextToken();
      }
      else
      {
        break;
      }

    }
    while (continueOrLoop);

    m_ops.m_opMap[opPos + OpMap.MAPINDEX_LENGTH] =
      m_ops.m_opMap[OpMap.MAPINDEX_LENGTH] - opPos;
  }

  /**
   * PathExpr  ::=  LocationPath
   * | FilterExpr
   * | FilterExpr '/' RelativeLocationPath
   *
   * @throws XSLProcessorException thrown if the active ProblemListener and XPathContext decide
   * the error condition is severe enough to halt processing.
   *
   * @throws javax.xml.transform.TransformerException
   */
  protected void PathExpr() throws javax.xml.transform.TransformerException
  {

    int opPos = m_ops.m_opMap[OpMap.MAPINDEX_LENGTH];

    int filterExprMatch = FilterExpr();

    if (filterExprMatch != FILTER_MATCH_FAILED)
    {
      boolean locationPathStarted = (filterExprMatch==FILTER_MATCH_PREDICATES);

      if (tokenIs('/'))
      {
        nextToken();

        if (!locationPathStarted)
        {
          insertOp(opPos, 2, OpCodes.OP_LOCATIONPATH);

          locationPathStarted = true;
        }

        if (!RelativeLocationPath())
        {
          error(XPATHErrorResources.ER_EXPECTED_REL_LOC_PATH, null);
        }

      }

      if (locationPathStarted)
      {
        m_ops.m_opMap[m_ops.m_opMap[OpMap.MAPINDEX_LENGTH]] = OpCodes.ENDOP;
        m_ops.m_opMap[OpMap.MAPINDEX_LENGTH] += 1;
        m_ops.m_opMap[opPos + OpMap.MAPINDEX_LENGTH] =
          m_ops.m_opMap[OpMap.MAPINDEX_LENGTH] - opPos;
      }
    }
    else
    {
      LocationPath();
    }
  }

  /**
   *
   *
   * FilterExpr  ::=  PrimaryExpr
   * | FilterExpr Predicate
   *
   * @throws XSLProcessorException thrown if the active ProblemListener and XPathContext decide
   * the error condition is severe enough to halt processing.
   *
   * @return  FILTER_MATCH_PREDICATES, if this method successfully matched a
   *          FilterExpr with one or more Predicates;
   *          FILTER_MATCH_PRIMARY, if this method successfully matched a
   *          FilterExpr that was just a PrimaryExpr; or
   *          FILTER_MATCH_FAILED, if this method did not match a FilterExpr
   *
   * @throws javax.xml.transform.TransformerException
   */
  protected int FilterExpr() throws javax.xml.transform.TransformerException
  {

    int opPos = m_ops.m_opMap[OpMap.MAPINDEX_LENGTH];

    int filterMatch;

    if (PrimaryExpr())
    {
      if (tokenIs('['))
      {

        insertOp(opPos, 2, OpCodes.OP_LOCATIONPATH);

        while (tokenIs('['))
        {
          Predicate();
        }

        filterMatch = FILTER_MATCH_PREDICATES;
      }
      else
      {
        filterMatch = FILTER_MATCH_PRIMARY;
      }
    }
    else
    {
      filterMatch = FILTER_MATCH_FAILED;
    }

    return filterMatch;

    /*
     * if(tokenIs('['))
     * {
     *   Predicate();
     *   m_ops.m_opMap[opPos + OpMap.MAPINDEX_LENGTH] = m_ops.m_opMap[OpMap.MAPINDEX_LENGTH] - opPos;
     * }
     */
  }

  /**
   *
   * PrimaryExpr  ::=  VariableReference
   * | '(' Expr ')'
   * | Literal
   * | Number
   * | FunctionCall
   *
   * @return true if this method successfully matched a PrimaryExpr
   *
   * @throws javax.xml.transform.TransformerException
   *
   */
  protected boolean PrimaryExpr() throws javax.xml.transform.TransformerException
  {

    boolean matchFound;
    int opPos = m_ops.m_opMap[OpMap.MAPINDEX_LENGTH];

    if ((m_tokenChar == '\'') || (m_tokenChar == '"'))
    {
      appendOp(2, OpCodes.OP_LITERAL);
      Literal();

      m_ops.m_opMap[opPos + OpMap.MAPINDEX_LENGTH] =
        m_ops.m_opMap[OpMap.MAPINDEX_LENGTH] - opPos;

      matchFound = true;
    }
    else if (m_tokenChar == '$')
    {
      appendOp(2, OpCodes.OP_VARIABLE);
      QName();
      
      m_ops.m_opMap[opPos + OpMap.MAPINDEX_LENGTH] =
        m_ops.m_opMap[OpMap.MAPINDEX_LENGTH] - opPos;

      matchFound = true;
    }
    else if (m_tokenChar == '(')
    {
      nextToken();
      appendOp(2, OpCodes.OP_GROUP);
      Expr();
      consumeExpected(')');

      m_ops.m_opMap[opPos + OpMap.MAPINDEX_LENGTH] =
        m_ops.m_opMap[OpMap.MAPINDEX_LENGTH] - opPos;

      matchFound = true;
    }
    else if ((null != m_token) && ((('.' == m_tokenChar) && (m_token.length() > 1) && Character.isDigit(
            m_token.charAt(1))) || Character.isDigit(m_tokenChar)))
    {
      appendOp(2, OpCodes.OP_NUMBERLIT);
      Number();

      m_ops.m_opMap[opPos + OpMap.MAPINDEX_LENGTH] =
        m_ops.m_opMap[OpMap.MAPINDEX_LENGTH] - opPos;

      matchFound = true;
    }
    else if (lookahead('(', 1) || (lookahead(':', 1) && lookahead('(', 3)))
    {
      matchFound = FunctionCall();
    }
    else
    {
      matchFound = false;
    }

    return matchFound;
  }

  /**
   *
   * Argument    ::=    Expr
   *
   *
   * @throws javax.xml.transform.TransformerException
   */
  protected void Argument() throws javax.xml.transform.TransformerException
  {

    int opPos = m_ops.m_opMap[OpMap.MAPINDEX_LENGTH];

    appendOp(2, OpCodes.OP_ARGUMENT);
    Expr();

    m_ops.m_opMap[opPos + OpMap.MAPINDEX_LENGTH] =
      m_ops.m_opMap[OpMap.MAPINDEX_LENGTH] - opPos;
  }

  /**
   *
   * FunctionCall    ::=    FunctionName '(' ( Argument ( ',' Argument)*)? ')'
   *
   * @return true if, and only if, a FunctionCall was matched
   *
   * @throws javax.xml.transform.TransformerException
   */
  protected boolean FunctionCall() throws javax.xml.transform.TransformerException
  {

    int opPos = m_ops.m_opMap[OpMap.MAPINDEX_LENGTH];

    if (lookahead(':', 1))
    {
      appendOp(4, OpCodes.OP_EXTFUNCTION);

      m_ops.m_opMap[opPos + OpMap.MAPINDEX_LENGTH + 1] = m_queueMark - 1;

      nextToken();
      consumeExpected(':');

      m_ops.m_opMap[opPos + OpMap.MAPINDEX_LENGTH + 2] = m_queueMark - 1;

      nextToken();
    }
    else
    {
      int funcTok = getFunctionToken(m_token);

      if (-1 == funcTok)
      {
        error(XPATHErrorResources.ER_COULDNOT_FIND_FUNCTION,
      }

      switch (funcTok)
      {
      case OpCodes.NODETYPE_PI :
      case OpCodes.NODETYPE_COMMENT :
      case OpCodes.NODETYPE_TEXT :
      case OpCodes.NODETYPE_NODE :
        return false;
      default :
        appendOp(3, OpCodes.OP_FUNCTION);

        m_ops.m_opMap[opPos + OpMap.MAPINDEX_LENGTH + 1] = funcTok;
      }

      nextToken();
    }

    consumeExpected('(');

    while (!tokenIs(')') && m_token != null)
    {
      if (tokenIs(','))
      {
      }

      Argument();

      if (!tokenIs(')'))
      {
        consumeExpected(',');

        if (tokenIs(')'))
        {
          error(XPATHErrorResources.ER_FOUND_COMMA_BUT_NO_FOLLOWING_ARG,
        }
      }
    }

    consumeExpected(')');

    m_ops.m_opMap[m_ops.m_opMap[OpMap.MAPINDEX_LENGTH]] = OpCodes.ENDOP;
    m_ops.m_opMap[OpMap.MAPINDEX_LENGTH] += 1;
    m_ops.m_opMap[opPos + OpMap.MAPINDEX_LENGTH] =
      m_ops.m_opMap[OpMap.MAPINDEX_LENGTH] - opPos;

    return true;
  }


  /**
   *
   * LocationPath ::= RelativeLocationPath
   * | AbsoluteLocationPath
   *
   *
   * @throws javax.xml.transform.TransformerException
   */
  protected void LocationPath() throws javax.xml.transform.TransformerException
  {

    int opPos = m_ops.m_opMap[OpMap.MAPINDEX_LENGTH];

    appendOp(2, OpCodes.OP_LOCATIONPATH);

    boolean seenSlash = tokenIs('/');

    if (seenSlash)
    {
      appendOp(4, OpCodes.FROM_ROOT);

      m_ops.m_opMap[m_ops.m_opMap[OpMap.MAPINDEX_LENGTH] - 2] = 4;
      m_ops.m_opMap[m_ops.m_opMap[OpMap.MAPINDEX_LENGTH] - 1] =
        OpCodes.NODETYPE_ROOT;

      nextToken();
    }

    if (m_token != null)
    {
      if (!RelativeLocationPath() && !seenSlash)
      {
        error(XPATHErrorResources.ER_EXPECTED_LOC_PATH, 
              new Object [] {m_token});
      }
    }

    m_ops.m_opMap[m_ops.m_opMap[OpMap.MAPINDEX_LENGTH]] = OpCodes.ENDOP;
    m_ops.m_opMap[OpMap.MAPINDEX_LENGTH] += 1;
    m_ops.m_opMap[opPos + OpMap.MAPINDEX_LENGTH] =
      m_ops.m_opMap[OpMap.MAPINDEX_LENGTH] - opPos;
  }

  /**
   *
   * RelativeLocationPath ::= Step
   * | RelativeLocationPath '/' Step
   * | AbbreviatedRelativeLocationPath
   *
   * @returns true if, and only if, a RelativeLocationPath was matched
   *
   * @throws javax.xml.transform.TransformerException
   */
  protected boolean RelativeLocationPath()
               throws javax.xml.transform.TransformerException
  {
    if (!Step())
    {
      return false;
    }

    while (tokenIs('/'))
    {
      nextToken();

      if (!Step())
      {
        error(XPATHErrorResources.ER_EXPECTED_LOC_STEP, null);
      }
    }

    return true;
  }

  /**
   *
   * Step    ::=    Basis Predicate
   * | AbbreviatedStep
   *
   * @returns false if step was empty (or only a '/'); true, otherwise
   *
   * @throws javax.xml.transform.TransformerException
   */
  protected boolean Step() throws javax.xml.transform.TransformerException
  {
    int opPos = m_ops.m_opMap[OpMap.MAPINDEX_LENGTH];

    boolean doubleSlash = tokenIs('/');

    if (doubleSlash)
    {
      nextToken();

      appendOp(2, OpCodes.FROM_DESCENDANTS_OR_SELF);


      m_ops.m_opMap[OpMap.MAPINDEX_LENGTH] += 1;
      m_ops.m_opMap[m_ops.m_opMap[OpMap.MAPINDEX_LENGTH]] =
        OpCodes.NODETYPE_NODE;
      m_ops.m_opMap[OpMap.MAPINDEX_LENGTH] += 1;

      m_ops.m_opMap[opPos + OpMap.MAPINDEX_LENGTH + 1] =
          m_ops.m_opMap[OpMap.MAPINDEX_LENGTH] - opPos;

      m_ops.m_opMap[opPos + OpMap.MAPINDEX_LENGTH] =
          m_ops.m_opMap[OpMap.MAPINDEX_LENGTH] - opPos;

      opPos = m_ops.m_opMap[OpMap.MAPINDEX_LENGTH];
    }

    if (tokenIs("."))
    {
      nextToken();

      if (tokenIs('['))
      {
      }

      appendOp(4, OpCodes.FROM_SELF);

      m_ops.m_opMap[m_ops.m_opMap[OpMap.MAPINDEX_LENGTH] - 2] = 4;
      m_ops.m_opMap[m_ops.m_opMap[OpMap.MAPINDEX_LENGTH] - 1] =
        OpCodes.NODETYPE_NODE;
    }
    else if (tokenIs(".."))
    {
      nextToken();
      appendOp(4, OpCodes.FROM_PARENT);

      m_ops.m_opMap[m_ops.m_opMap[OpMap.MAPINDEX_LENGTH] - 2] = 4;
      m_ops.m_opMap[m_ops.m_opMap[OpMap.MAPINDEX_LENGTH] - 1] =
        OpCodes.NODETYPE_NODE;
    }

    else if (tokenIs('*') || tokenIs('@') || tokenIs('_')
             || (m_token!= null && Character.isLetter(m_token.charAt(0))))
    {
      Basis();

      while (tokenIs('['))
      {
        Predicate();
      }

      m_ops.m_opMap[opPos + OpMap.MAPINDEX_LENGTH] =
        m_ops.m_opMap[OpMap.MAPINDEX_LENGTH] - opPos;
    }
    else
    {
      if (doubleSlash)
      {
        error(XPATHErrorResources.ER_EXPECTED_LOC_STEP, null);
      }

      return false;
    }

    return true;
  }

  /**
   *
   * Basis    ::=    AxisName '::' NodeTest
   * | AbbreviatedBasis
   *
   * @throws javax.xml.transform.TransformerException
   */
  protected void Basis() throws javax.xml.transform.TransformerException
  {

    int opPos = m_ops.m_opMap[OpMap.MAPINDEX_LENGTH];
    int axesType;

    if (lookahead("::", 1))
    {
      axesType = AxisName();

      nextToken();
      nextToken();
    }
    else if (tokenIs('@'))
    {
      axesType = OpCodes.FROM_ATTRIBUTES;

      appendOp(2, axesType);
      nextToken();
    }
    else
    {
      axesType = OpCodes.FROM_CHILDREN;

      appendOp(2, axesType);
    }

    m_ops.m_opMap[OpMap.MAPINDEX_LENGTH] += 1;

    NodeTest(axesType);

    m_ops.m_opMap[opPos + OpMap.MAPINDEX_LENGTH + 1] =
      m_ops.m_opMap[OpMap.MAPINDEX_LENGTH] - opPos;
  }

  /**
   *
   * Basis    ::=    AxisName '::' NodeTest
   * | AbbreviatedBasis
   *
   * @return FROM_XXX axes type, found in {@link org.apache.xpath.compiler.Keywords}.
   *
   * @throws javax.xml.transform.TransformerException
   */
  protected int AxisName() throws javax.xml.transform.TransformerException
  {

    Object val = Keywords.m_axisnames.get(m_token);

    if (null == val)
    {
      error(XPATHErrorResources.ER_ILLEGAL_AXIS_NAME,
    }

    int axesType = ((Integer) val).intValue();

    appendOp(2, axesType);

    return axesType;
  }

  /**
   *
   * NodeTest    ::=    WildcardName
   * | NodeType '(' ')'
   * | 'processing-instruction' '(' Literal ')'
   *
   * @param axesType FROM_XXX axes type, found in {@link org.apache.xpath.compiler.Keywords}.
   *
   * @throws javax.xml.transform.TransformerException
   */
  protected void NodeTest(int axesType) throws javax.xml.transform.TransformerException
  {

    if (lookahead('(', 1))
    {
      Object nodeTestOp = Keywords.m_nodetypes.get(m_token);

      if (null == nodeTestOp)
      {
        error(XPATHErrorResources.ER_UNKNOWN_NODETYPE,
      }
      else
      {
        nextToken();

        int nt = ((Integer) nodeTestOp).intValue();

        m_ops.m_opMap[m_ops.m_opMap[OpMap.MAPINDEX_LENGTH]] = nt;
        m_ops.m_opMap[OpMap.MAPINDEX_LENGTH] += 1;

        consumeExpected('(');

        if (OpCodes.NODETYPE_PI == nt)
        {
          if (!tokenIs(')'))
          {
            Literal();
          }
        }

        consumeExpected(')');
      }
    }
    else
    {

      m_ops.m_opMap[m_ops.m_opMap[OpMap.MAPINDEX_LENGTH]] = OpCodes.NODENAME;
      m_ops.m_opMap[OpMap.MAPINDEX_LENGTH] += 1;

      if (lookahead(':', 1))
      {
        if (tokenIs('*'))
        {
          m_ops.m_opMap[m_ops.m_opMap[OpMap.MAPINDEX_LENGTH]] =
            OpCodes.ELEMWILDCARD;
        }
        else
        {
          m_ops.m_opMap[m_ops.m_opMap[OpMap.MAPINDEX_LENGTH]] = m_queueMark
                  - 1;

          if (!Character.isLetter(m_tokenChar) && !tokenIs('_'))
          {
            error(XPATHErrorResources.ER_EXPECTED_NODE_TEST, null);
          }
        }

        nextToken();
        consumeExpected(':');
      }
      else
      {
        m_ops.m_opMap[m_ops.m_opMap[OpMap.MAPINDEX_LENGTH]] = OpCodes.EMPTY;
      }

      m_ops.m_opMap[OpMap.MAPINDEX_LENGTH] += 1;

      if (tokenIs('*'))
      {
        m_ops.m_opMap[m_ops.m_opMap[OpMap.MAPINDEX_LENGTH]] =
          OpCodes.ELEMWILDCARD;
      }
      else
      {
        if (OpCodes.FROM_NAMESPACE == axesType)
        {
          String prefix = (String) this.m_ops.m_tokenQueue[m_queueMark - 1];
          String namespace =
            ((PrefixResolver) m_namespaceContext).getNamespaceForPrefix(
              prefix);

          this.m_ops.m_tokenQueue[m_queueMark - 1] = namespace;
        }

        m_ops.m_opMap[m_ops.m_opMap[OpMap.MAPINDEX_LENGTH]] = m_queueMark - 1;

        if (!Character.isLetter(m_tokenChar) && !tokenIs('_'))
        {
          error(XPATHErrorResources.ER_EXPECTED_NODE_TEST, null);
        }
      }

      m_ops.m_opMap[OpMap.MAPINDEX_LENGTH] += 1;

      nextToken();
    }
  }

  /**
   *
   * Predicate ::= '[' PredicateExpr ']'
   *
   *
   * @throws javax.xml.transform.TransformerException
   */
  protected void Predicate() throws javax.xml.transform.TransformerException
  {

    if (tokenIs('['))
    {
      nextToken();
      PredicateExpr();
      consumeExpected(']');
    }
  }

  /**
   *
   * PredicateExpr ::= Expr
   *
   *
   * @throws javax.xml.transform.TransformerException
   */
  protected void PredicateExpr() throws javax.xml.transform.TransformerException
  {

    int opPos = m_ops.m_opMap[OpMap.MAPINDEX_LENGTH];

    appendOp(2, OpCodes.OP_PREDICATE);
    Expr();

    m_ops.m_opMap[m_ops.m_opMap[OpMap.MAPINDEX_LENGTH]] = OpCodes.ENDOP;
    m_ops.m_opMap[OpMap.MAPINDEX_LENGTH] += 1;
    m_ops.m_opMap[opPos + OpMap.MAPINDEX_LENGTH] =
      m_ops.m_opMap[OpMap.MAPINDEX_LENGTH] - opPos;
  }

  /**
   * QName ::=  (Prefix ':')? LocalPart
   * Prefix ::=  NCName
   * LocalPart ::=  NCName
   *
   * @throws javax.xml.transform.TransformerException
   */
  protected void QName() throws javax.xml.transform.TransformerException
  {
    if(lookahead(':', 1))
    {
      m_ops.m_opMap[m_ops.m_opMap[OpMap.MAPINDEX_LENGTH]] = m_queueMark - 1;
      m_ops.m_opMap[OpMap.MAPINDEX_LENGTH] += 1;

      nextToken();
      consumeExpected(':');
    }
    else
    {
      m_ops.m_opMap[m_ops.m_opMap[OpMap.MAPINDEX_LENGTH]] = OpCodes.EMPTY;
      m_ops.m_opMap[OpMap.MAPINDEX_LENGTH] += 1;
    }
    
    m_ops.m_opMap[m_ops.m_opMap[OpMap.MAPINDEX_LENGTH]] = m_queueMark - 1;
    m_ops.m_opMap[OpMap.MAPINDEX_LENGTH] += 1;

    nextToken();
  }

  /**
   * NCName ::=  (Letter | '_') (NCNameChar)
   * NCNameChar ::=  Letter | Digit | '.' | '-' | '_' | CombiningChar | Extender
   */
  protected void NCName()
  {

    m_ops.m_opMap[m_ops.m_opMap[OpMap.MAPINDEX_LENGTH]] = m_queueMark - 1;
    m_ops.m_opMap[OpMap.MAPINDEX_LENGTH] += 1;

    nextToken();
  }

  /**
   * The value of the Literal is the sequence of characters inside
   * the " or ' characters>.
   *
   * Literal  ::=  '"' [^"]* '"'
   * | "'" [^']* "'"
   *
   *
   * @throws javax.xml.transform.TransformerException
   */
  protected void Literal() throws javax.xml.transform.TransformerException
  {

    int last = m_token.length() - 1;
    char c0 = m_tokenChar;
    char cX = m_token.charAt(last);

    if (((c0 == '\"') && (cX == '\"')) || ((c0 == '\'') && (cX == '\'')))
    {

      int tokenQueuePos = m_queueMark - 1;

      m_ops.m_tokenQueue[tokenQueuePos] = null;

      Object obj = new XString(m_token.substring(1, last));

      m_ops.m_tokenQueue[tokenQueuePos] = obj;

      m_ops.m_opMap[m_ops.m_opMap[OpMap.MAPINDEX_LENGTH]] = tokenQueuePos;
      m_ops.m_opMap[OpMap.MAPINDEX_LENGTH] += 1;

      nextToken();
    }
    else
    {
      error(XPATHErrorResources.ER_PATTERN_LITERAL_NEEDS_BE_QUOTED,
    }
  }

  /**
   *
   * Number ::= [0-9]+('.'[0-9]+)? | '.'[0-9]+
   *
   *
   * @throws javax.xml.transform.TransformerException
   */
  protected void Number() throws javax.xml.transform.TransformerException
  {

    if (null != m_token)
    {

      double num;

      try
      {
        num = Double.valueOf(m_token).doubleValue();
      }
      catch (NumberFormatException nfe)
      {

        error(XPATHErrorResources.ER_COULDNOT_BE_FORMATTED_TO_NUMBER,
      }

      m_ops.m_tokenQueue[m_queueMark - 1] = new XNumber(num);
      m_ops.m_opMap[m_ops.m_opMap[OpMap.MAPINDEX_LENGTH]] = m_queueMark - 1;
      m_ops.m_opMap[OpMap.MAPINDEX_LENGTH] += 1;

      nextToken();
    }
  }


  /**
   *
   * Pattern  ::=  LocationPathPattern
   * | Pattern '|' LocationPathPattern
   *
   *
   * @throws javax.xml.transform.TransformerException
   */
  protected void Pattern() throws javax.xml.transform.TransformerException
  {

    while (true)
    {
      LocationPathPattern();

      if (tokenIs('|'))
      {
        nextToken();
      }
      else
      {
        break;
      }
    }
  }

  /**
   *
   *
   * LocationPathPattern  ::=  '/' RelativePathPattern?
   *
   *
   * @throws javax.xml.transform.TransformerException
   */
  protected void LocationPathPattern() throws javax.xml.transform.TransformerException
  {

    int opPos = m_ops.m_opMap[OpMap.MAPINDEX_LENGTH];

    final int RELATIVE_PATH_NOT_PERMITTED = 0;
    final int RELATIVE_PATH_PERMITTED     = 1;
    final int RELATIVE_PATH_REQUIRED      = 2;

    int relativePathStatus = RELATIVE_PATH_NOT_PERMITTED;

    appendOp(2, OpCodes.OP_LOCATIONPATHPATTERN);

    if (lookahead('(', 1)
            && (tokenIs(Keywords.FUNC_ID_STRING)
                || tokenIs(Keywords.FUNC_KEY_STRING)))
    {
      IdKeyPattern();

      if (tokenIs('/'))
      {
        nextToken();

        if (tokenIs('/'))
        {
          appendOp(4, OpCodes.MATCH_ANY_ANCESTOR);

          nextToken();
        }
        else
        {
          appendOp(4, OpCodes.MATCH_IMMEDIATE_ANCESTOR);
        }

        m_ops.m_opMap[m_ops.m_opMap[OpMap.MAPINDEX_LENGTH] - 2] = 4;
        m_ops.m_opMap[m_ops.m_opMap[OpMap.MAPINDEX_LENGTH] - 1] =
          OpCodes.NODETYPE_FUNCTEST;

        relativePathStatus = RELATIVE_PATH_REQUIRED;
      }
    }
    else if (tokenIs('/'))
    {
      if (lookahead('/', 1))
      {
        appendOp(4, OpCodes.MATCH_ANY_ANCESTOR);
        
        nextToken();

        relativePathStatus = RELATIVE_PATH_REQUIRED;
      }
      else
      {
        appendOp(4, OpCodes.FROM_ROOT);

        relativePathStatus = RELATIVE_PATH_PERMITTED;
      }


      m_ops.m_opMap[m_ops.m_opMap[OpMap.MAPINDEX_LENGTH] - 2] = 4;
      m_ops.m_opMap[m_ops.m_opMap[OpMap.MAPINDEX_LENGTH] - 1] =
        OpCodes.NODETYPE_ROOT;

      nextToken();
    }
    else
    {
      relativePathStatus = RELATIVE_PATH_REQUIRED;
    }

    if (relativePathStatus != RELATIVE_PATH_NOT_PERMITTED)
    {
      if (!tokenIs('|') && (null != m_token))
      {
        RelativePathPattern();
      }
      else if (relativePathStatus == RELATIVE_PATH_REQUIRED)
      {
        error(XPATHErrorResources.ER_EXPECTED_REL_PATH_PATTERN, null);
      }
    }

    m_ops.m_opMap[m_ops.m_opMap[OpMap.MAPINDEX_LENGTH]] = OpCodes.ENDOP;
    m_ops.m_opMap[OpMap.MAPINDEX_LENGTH] += 1;
    m_ops.m_opMap[opPos + OpMap.MAPINDEX_LENGTH] =
      m_ops.m_opMap[OpMap.MAPINDEX_LENGTH] - opPos;
  }

  /**
   *
   * IdKeyPattern  ::=  'id' '(' Literal ')'
   * | 'key' '(' Literal ',' Literal ')'
   * (Also handle doc())
   *
   *
   * @throws javax.xml.transform.TransformerException
   */
  protected void IdKeyPattern() throws javax.xml.transform.TransformerException
  {
    FunctionCall();
  }

  /**
   *
   * RelativePathPattern  ::=  StepPattern
   * | RelativePathPattern '/' StepPattern
   *
   * @throws javax.xml.transform.TransformerException
   */
  protected void RelativePathPattern()
              throws javax.xml.transform.TransformerException
  {

    boolean trailingSlashConsumed = StepPattern(false);

    while (tokenIs('/'))
    {
      nextToken();

      trailingSlashConsumed = StepPattern(!trailingSlashConsumed);
    }
  }

  /**
   *
   * StepPattern  ::=  AbbreviatedNodeTestStep
   *
   * @param isLeadingSlashPermitted a boolean indicating whether a slash can
   *        appear at the start of this step
   *
   * @return boolean indicating whether a slash following the step was consumed
   *
   * @throws javax.xml.transform.TransformerException
   */
  protected boolean StepPattern(boolean isLeadingSlashPermitted)
            throws javax.xml.transform.TransformerException
  {
    return AbbreviatedNodeTestStep(isLeadingSlashPermitted);
  }

  /**
   *
   * AbbreviatedNodeTestStep    ::=    '@'? NodeTest Predicate
   *
   * @param isLeadingSlashPermitted a boolean indicating whether a slash can
   *        appear at the start of this step
   *
   * @return boolean indicating whether a slash following the step was consumed
   *
   * @throws javax.xml.transform.TransformerException
   */
  protected boolean AbbreviatedNodeTestStep(boolean isLeadingSlashPermitted)
            throws javax.xml.transform.TransformerException
  {

    int opPos = m_ops.m_opMap[OpMap.MAPINDEX_LENGTH];
    int axesType;

    int matchTypePos = -1;

    if (tokenIs('@'))
    {
      axesType = OpCodes.MATCH_ATTRIBUTE;

      appendOp(2, axesType);
      nextToken();
    }
    else if (this.lookahead("::", 1))
    {
      if (tokenIs("attribute"))
      {
        axesType = OpCodes.MATCH_ATTRIBUTE;

        appendOp(2, axesType);
      }
      else if (tokenIs("child"))
      {
        matchTypePos = m_ops.m_opMap[OpMap.MAPINDEX_LENGTH];
        axesType = OpCodes.MATCH_IMMEDIATE_ANCESTOR;

        appendOp(2, axesType);
      }
      else
      {
        axesType = -1;

        this.error(XPATHErrorResources.ER_AXES_NOT_ALLOWED,
                   new Object[]{ this.m_token });
      }

      nextToken();
      nextToken();
    }
    else if (tokenIs('/'))
    {
      if (!isLeadingSlashPermitted)
      {
        error(XPATHErrorResources.ER_EXPECTED_STEP_PATTERN, null);
      }
      axesType = OpCodes.MATCH_ANY_ANCESTOR;

      appendOp(2, axesType);
      nextToken();
    }
    else
    {
      matchTypePos = m_ops.m_opMap[OpMap.MAPINDEX_LENGTH];
      axesType = OpCodes.MATCH_IMMEDIATE_ANCESTOR;

      appendOp(2, axesType);
    }

    m_ops.m_opMap[OpMap.MAPINDEX_LENGTH] += 1;

    NodeTest(axesType);

    m_ops.m_opMap[opPos + OpMap.MAPINDEX_LENGTH + 1] =
      m_ops.m_opMap[OpMap.MAPINDEX_LENGTH] - opPos;

    while (tokenIs('['))
    {
      Predicate();
    }

    boolean trailingSlashConsumed;

    if ((matchTypePos > -1) && tokenIs('/') && lookahead('/', 1))
    {
      m_ops.m_opMap[matchTypePos] = OpCodes.MATCH_ANY_ANCESTOR;

      nextToken();

      trailingSlashConsumed = true;
    }
    else
    {
      trailingSlashConsumed = false;
    }

    m_ops.m_opMap[opPos + OpMap.MAPINDEX_LENGTH] =
      m_ops.m_opMap[OpMap.MAPINDEX_LENGTH] - opPos;

    return trailingSlashConsumed;
  }
}
