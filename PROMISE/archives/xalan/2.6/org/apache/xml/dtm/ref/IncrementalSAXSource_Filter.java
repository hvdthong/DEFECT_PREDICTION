package org.apache.xml.dtm.ref;

import java.io.IOException;

import org.apache.xml.res.XMLErrorResources;
import org.apache.xml.res.XMLMessages;
import org.apache.xml.utils.ThreadControllerWrapper;

import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.DTDHandler;
import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.SAXNotRecognizedException;
import org.xml.sax.SAXNotSupportedException;
import org.xml.sax.SAXParseException;
import org.xml.sax.XMLReader;
import org.xml.sax.ext.LexicalHandler;

/** <p>IncrementalSAXSource_Filter implements IncrementalSAXSource, using a
 * standard SAX2 event source as its input and parcelling out those
 * events gradually in reponse to deliverMoreNodes() requests.  Output from the
 * filter will be passed along to a SAX handler registered as our
 * listener, but those callbacks will pass through a counting stage
 * which periodically yields control back to the controller coroutine.
 * </p>
 *
 * <p>%REVIEW%: This filter is not currenly intended to be reusable
 * for parsing additional streams/documents. We may want to consider
 * making it resettable at some point in the future. But it's a 
 * small object, so that'd be mostly a convenience issue; the cost
 * of allocating each time is trivial compared to the cost of processing
 * any nontrival stream.</p>
 *
 * <p>For a brief usage example, see the unit-test main() method.</p>
 *
 * <p>This is a simplification of the old CoroutineSAXParser, focusing
 * specifically on filtering. The resulting controller protocol is _far_
 * simpler and less error-prone; the only controller operation is deliverMoreNodes(),
 * and the only requirement is that deliverMoreNodes(false) be called if you want to
 * discard the rest of the stream and the previous deliverMoreNodes() didn't return
 * false.
 * */
public class IncrementalSAXSource_Filter
implements IncrementalSAXSource, ContentHandler, DTDHandler, LexicalHandler, ErrorHandler, Runnable
{

  private CoroutineManager fCoroutineManager = null;
  private int fControllerCoroutineID = -1;
  private int fSourceCoroutineID = -1;

  private int eventcounter;
  private int frequency=5;

  private boolean fNoMoreEvents=false;

  private XMLReader fXMLReader=null;
  private InputSource fXMLReaderInputSource=null;


  public IncrementalSAXSource_Filter() {
    this.init( new CoroutineManager(), -1, -1);
  }
  
  /** Create a IncrementalSAXSource_Filter which is not yet bound to a specific
   * SAX event source.
   * */
  public IncrementalSAXSource_Filter(CoroutineManager co, int controllerCoroutineID)
  {
    this.init( co, controllerCoroutineID, -1 );
  }

  static public IncrementalSAXSource createIncrementalSAXSource(CoroutineManager co, int controllerCoroutineID) {
    return new IncrementalSAXSource_Filter(co, controllerCoroutineID);
  }


  public void init( CoroutineManager co, int controllerCoroutineID,
                    int sourceCoroutineID)
  {
    if(co==null)
      co = new CoroutineManager();
    fCoroutineManager = co;
    fControllerCoroutineID = co.co_joinCoroutineSet(controllerCoroutineID);
    fSourceCoroutineID = co.co_joinCoroutineSet(sourceCoroutineID);
    if (fControllerCoroutineID == -1 || fSourceCoroutineID == -1)

    fNoMoreEvents=false;
    eventcounter=frequency;
  }
    
  /** Bind our input streams to an XMLReader.
   *
   * Just a convenience routine; obviously you can explicitly register
   * this as a listener with the same effect.
   * */
  public void setXMLReader(XMLReader eventsource)
  {
    fXMLReader=eventsource;
    eventsource.setContentHandler(this);
    eventsource.setDTDHandler(this);

    try 
    {
      eventsource.
                    this);
    }
    catch(SAXNotRecognizedException e)
    {
    }
    catch(SAXNotSupportedException e)
    {
    }

  }

  public void setContentHandler(ContentHandler handler)
  {
    clientContentHandler=handler;
  }
  public void setDTDHandler(DTDHandler handler)
  {
    clientDTDHandler=handler;
  }
  public void setLexicalHandler(LexicalHandler handler)
  {
    clientLexicalHandler=handler;
  }
  public void setErrHandler(ErrorHandler handler)
  {
    clientErrorHandler=handler;
  }

  public void setReturnFrequency(int events)
  {
    if(events<1) events=1;
    frequency=eventcounter=events;
  }
  
  public void characters(char[] ch, int start, int length)
       throws org.xml.sax.SAXException
  {
    if(--eventcounter<=0)
      {
        co_yield(true);
        eventcounter=frequency;
      }
    if(clientContentHandler!=null)
      clientContentHandler.characters(ch,start,length);
  }
  public void endDocument() 
       throws org.xml.sax.SAXException
  {
    if(clientContentHandler!=null)
      clientContentHandler.endDocument();

    eventcounter=0;     
    co_yield(false);
  }
  public void endElement(java.lang.String namespaceURI, java.lang.String localName,
      java.lang.String qName) 
       throws org.xml.sax.SAXException
  {
    if(--eventcounter<=0)
      {
        co_yield(true);
        eventcounter=frequency;
      }
    if(clientContentHandler!=null)
      clientContentHandler.endElement(namespaceURI,localName,qName);
  }
  public void endPrefixMapping(java.lang.String prefix) 
       throws org.xml.sax.SAXException
  {
    if(--eventcounter<=0)
      {
        co_yield(true);
        eventcounter=frequency;
      }
    if(clientContentHandler!=null)
      clientContentHandler.endPrefixMapping(prefix);
  }
  public void ignorableWhitespace(char[] ch, int start, int length) 
       throws org.xml.sax.SAXException
  {
    if(--eventcounter<=0)
      {
        co_yield(true);
        eventcounter=frequency;
      }
    if(clientContentHandler!=null)
      clientContentHandler.ignorableWhitespace(ch,start,length);
  }
  public void processingInstruction(java.lang.String target, java.lang.String data) 
       throws org.xml.sax.SAXException
  {
    if(--eventcounter<=0)
      {
        co_yield(true);
        eventcounter=frequency;
      }
    if(clientContentHandler!=null)
      clientContentHandler.processingInstruction(target,data);
  }
  public void setDocumentLocator(Locator locator) 
  {
    if(--eventcounter<=0)
      {
        eventcounter=frequency;
      }
    if(clientContentHandler!=null)
      clientContentHandler.setDocumentLocator(locator);
  }
  public void skippedEntity(java.lang.String name) 
       throws org.xml.sax.SAXException
  {
    if(--eventcounter<=0)
      {
        co_yield(true);
        eventcounter=frequency;
      }
    if(clientContentHandler!=null)
      clientContentHandler.skippedEntity(name);
  }
  public void startDocument() 
       throws org.xml.sax.SAXException
  {
    co_entry_pause();

    if(--eventcounter<=0)
      {
        co_yield(true);
        eventcounter=frequency;
      }
    if(clientContentHandler!=null)
      clientContentHandler.startDocument();
  }
  public void startElement(java.lang.String namespaceURI, java.lang.String localName,
      java.lang.String qName, Attributes atts) 
       throws org.xml.sax.SAXException
  {
    if(--eventcounter<=0)
      {
        co_yield(true);
        eventcounter=frequency;
      }
    if(clientContentHandler!=null)
      clientContentHandler.startElement(namespaceURI, localName, qName, atts);
  }
  public void startPrefixMapping(java.lang.String prefix, java.lang.String uri) 
       throws org.xml.sax.SAXException
  {
    if(--eventcounter<=0)
      {
        co_yield(true);
        eventcounter=frequency;
      }
    if(clientContentHandler!=null)
      clientContentHandler.startPrefixMapping(prefix,uri);
  }

  public void comment(char[] ch, int start, int length) 
       throws org.xml.sax.SAXException
  {
    if(null!=clientLexicalHandler)
      clientLexicalHandler.comment(ch,start,length);
  }
  public void endCDATA() 
       throws org.xml.sax.SAXException
  {
    if(null!=clientLexicalHandler)
      clientLexicalHandler.endCDATA();
  }
  public void endDTD() 
       throws org.xml.sax.SAXException
  {
    if(null!=clientLexicalHandler)
      clientLexicalHandler.endDTD();
  }
  public void endEntity(java.lang.String name) 
       throws org.xml.sax.SAXException
  {
    if(null!=clientLexicalHandler)
      clientLexicalHandler.endEntity(name);
  }
  public void startCDATA() 
       throws org.xml.sax.SAXException
  {
    if(null!=clientLexicalHandler)
      clientLexicalHandler.startCDATA();
  }
  public void startDTD(java.lang.String name, java.lang.String publicId,
      java.lang.String systemId) 
       throws org.xml.sax.SAXException
  {
    if(null!=clientLexicalHandler)
      clientLexicalHandler. startDTD(name, publicId, systemId);
  }
  public void startEntity(java.lang.String name) 
       throws org.xml.sax.SAXException
  {
    if(null!=clientLexicalHandler)
      clientLexicalHandler.startEntity(name);
  }

  
  public void notationDecl(String a, String b, String c) throws SAXException
  {
  	if(null!=clientDTDHandler)
	  	clientDTDHandler.notationDecl(a,b,c);
  }
  public void unparsedEntityDecl(String a, String b, String c, String d)  throws SAXException
  {
  	if(null!=clientDTDHandler)
	  	clientDTDHandler.unparsedEntityDecl(a,b,c,d);
  }
  
  public void error(SAXParseException exception) throws SAXException
  {
    if(null!=clientErrorHandler)
      clientErrorHandler.error(exception);
  }
  
  public void fatalError(SAXParseException exception) throws SAXException
  {
    if(null!=clientErrorHandler)
      clientErrorHandler.error(exception);

    eventcounter=0;     
    co_yield(false);

  }
  
  public void warning(SAXParseException exception) throws SAXException
  {
    if(null!=clientErrorHandler)
      clientErrorHandler.error(exception);
  }
  


  public int getSourceCoroutineID() {
    return fSourceCoroutineID;
  }
  public int getControllerCoroutineID() {
    return fControllerCoroutineID;
  }

  /** @return the CoroutineManager this CoroutineFilter object is bound to.
   * If you're using the do...() methods, applications should only
   * need to talk to the CoroutineManager once, to obtain the
   * application's Coroutine ID.
   * */
  public CoroutineManager getCoroutineManager()
  {
    return fCoroutineManager;
  }

  /** <p>In the SAX delegation code, I've inlined the count-down in
   * the hope of encouraging compilers to deliver better
   * performance. However, if we subclass (eg to directly connect the
   * output to a DTM builder), that would require calling super in
   * order to run that logic... which seems inelegant.  Hence this
   * routine for the convenience of subclasses: every [frequency]
   * invocations, issue a co_yield.</p>
   *
   * @param moreExepected Should always be true unless this is being called
   * at the end of endDocument() handling.
   * */
  protected void count_and_yield(boolean moreExpected) throws SAXException
  {
    if(!moreExpected) eventcounter=0;
    
    if(--eventcounter<=0)
      {
        co_yield(true);
        eventcounter=frequency;
      }
  }

  /**
   * co_entry_pause is called in startDocument() before anything else
   * happens. It causes the filter to wait for a "go ahead" request
   * from the controller before delivering any events. Note that
   * the very first thing the controller tells us may be "I don't
   * need events after all"!
   */
  private void co_entry_pause() throws SAXException
  {
    if(fCoroutineManager==null)
    {
      init(null,-1,-1);
    }

    try
    {
      Object arg=fCoroutineManager.co_entry_pause(fSourceCoroutineID);
      if(arg==Boolean.FALSE)
        co_yield(false);
    }
    catch(NoSuchMethodException e)
    {
      if(DEBUG) e.printStackTrace();
      throw new SAXException(e);
    }
  }

  /**
   * Co_Yield handles coroutine interactions while a parse is in progress.
   *
   * When moreRemains==true, we are pausing after delivering events, to
   * ask if more are needed. We will resume the controller thread with 
   *   co_resume(Boolean.TRUE, ...)
   * When control is passed back it may indicate
   *      Boolean.TRUE    indication to continue delivering events
   *      Boolean.FALSE   indication to discontinue events and shut down.
   * 
   * When moreRemains==false, we shut down immediately without asking the
   * controller's permission. Normally this means end of document has been
   * reached.
   *
   * Shutting down a IncrementalSAXSource_Filter requires terminating the incoming
   * SAX event stream. If we are in control of that stream (if it came
   * from an XMLReader passed to our startReader() method), we can do so
   * very quickly by throwing a reserved exception to it. If the stream is
   * coming from another source, we can't do that because its caller may
   * not be prepared for this "normal abnormal exit", and instead we put
   * ourselves in a "spin" mode where events are discarded.
   */
  private void co_yield(boolean moreRemains) throws SAXException
  {
    if(fNoMoreEvents)
      return;

    {
      Object arg=Boolean.FALSE;
      if(moreRemains)
      {
        arg = fCoroutineManager.co_resume(Boolean.TRUE, fSourceCoroutineID,
                                          fControllerCoroutineID);
        
      }

      if(arg==Boolean.FALSE)
      {
        fNoMoreEvents=true;
        
        
        fCoroutineManager.co_exit_to(Boolean.FALSE, fSourceCoroutineID,
                                     fControllerCoroutineID);
      }
    }
    catch(NoSuchMethodException e)
    {
      fNoMoreEvents=true;
      fCoroutineManager.co_exit(fSourceCoroutineID);
      throw new SAXException(e);
    }
  }


  /** Launch a thread that will run an XMLReader's parse() operation within
   *  a thread, feeding events to this IncrementalSAXSource_Filter. Mostly a convenience
   *  routine, but has the advantage that -- since we invoked parse() --
   *  we can halt parsing quickly via a StopException rather than waiting
   *  for the SAX stream to end by itself.
   *
   * @throws SAXException is parse thread is already in progress
   * or parsing can not be started.
   * */
  public void startParse(InputSource source) throws SAXException
  {
    if(fNoMoreEvents)
    if(fXMLReader==null)

    fXMLReaderInputSource=source;
    
    ThreadControllerWrapper.runThread(this, -1);
  }
  
  /* Thread logic to support startParseThread()
   */
  public void run()
  {
    if(fXMLReader==null) return;

    if(DEBUG)System.out.println("IncrementalSAXSource_Filter parse thread launched");

    Object arg=Boolean.FALSE;

    try
    {
      fXMLReader.parse(fXMLReaderInputSource);
    }
    catch(IOException ex)
    {
      arg=ex;
    }
    catch(StopException ex)
    {
      if(DEBUG)System.out.println("Active IncrementalSAXSource_Filter normal stop exception");
    }
    catch (SAXException ex)
    {
      Exception inner=ex.getException();
      if(inner instanceof StopException){
        if(DEBUG)System.out.println("Active IncrementalSAXSource_Filter normal stop exception");
      }
      else
      {
        if(DEBUG)
        {
          System.out.println("Active IncrementalSAXSource_Filter UNEXPECTED SAX exception: "+inner);
          inner.printStackTrace();
        }                
        arg=ex;
      }
    
    fXMLReader=null;

    try
    {
      fNoMoreEvents=true;
      fCoroutineManager.co_exit_to(arg, fSourceCoroutineID,
                                   fControllerCoroutineID);
    }
    catch(java.lang.NoSuchMethodException e)
    {
      e.printStackTrace(System.err);
      fCoroutineManager.co_exit(fSourceCoroutineID);
    }
  }

  /** Used to quickly terminate parse when running under a
      startParse() thread. Only its type is important. */
  class StopException extends RuntimeException
  {
  }

  /** deliverMoreNodes() is a simple API which tells the coroutine
   * parser that we need more nodes.  This is intended to be called
   * from one of our partner routines, and serves to encapsulate the
   * details of how incremental parsing has been achieved.
   *
   * @param parsemore If true, tells the incremental filter to generate
   * another chunk of output. If false, tells the filter that we're
   * satisfied and it can terminate parsing of this document.
   *
   * @return Boolean.TRUE if there may be more events available by invoking
   * deliverMoreNodes() again. Boolean.FALSE if parsing has run to completion (or been
   * terminated by deliverMoreNodes(false). Or an exception object if something
   * malfunctioned. %REVIEW% We _could_ actually throw the exception, but
   * that would require runinng deliverMoreNodes() in a try/catch... and for many
   * applications, exception will be simply be treated as "not TRUE" in
   * any case.
   * */
  public Object deliverMoreNodes(boolean parsemore)
  {
    if(fNoMoreEvents)
      return Boolean.FALSE;

    try 
    {
      Object result =
        fCoroutineManager.co_resume(parsemore?Boolean.TRUE:Boolean.FALSE,
                                    fControllerCoroutineID, fSourceCoroutineID);
      if(result==Boolean.FALSE)
        fCoroutineManager.co_exit(fControllerCoroutineID);

      return result;
    }
      
    catch(NoSuchMethodException e)
      {
        return e;
      }
  }
  

  /** Simple unit test. Attempt coroutine parsing of document indicated
   * by first argument (as a URI), report progress.
   */
    /*
  public static void main(String args[])
  {
    System.out.println("Starting...");

    org.xml.sax.XMLReader theSAXParser=
      new org.apache.xerces.parsers.SAXParser();
  

    for(int arg=0;arg<args.length;++arg)
    {
      IncrementalSAXSource_Filter filter=
        new IncrementalSAXSource_Filter();
      org.apache.xml.serialize.XMLSerializer trace;
      trace=new org.apache.xml.serialize.XMLSerializer(System.out,null);
      filter.setContentHandler(trace);
      filter.setLexicalHandler(trace);

      try
      {
        InputSource source = new InputSource(args[arg]);
        Object result=null;
        boolean more=true;


        filter.setXMLReader(theSAXParser);
        filter.startParse(source);
      
        for(result = filter.deliverMoreNodes(more);
            (result instanceof Boolean && ((Boolean)result)==Boolean.TRUE);
            result = filter.deliverMoreNodes(more))
        {
          System.out.println("\nSome parsing successful, trying more.\n");
          
          if(arg+1<args.length && "!".equals(args[arg+1]))
          {
            ++arg;
            more=false;
          }
          
        }
      
        if (result instanceof Boolean && ((Boolean)result)==Boolean.FALSE)
        {
          System.out.println("\nFilter ended (EOF or on request).\n");
        }
        else if (result == null) {
          System.out.println("\nUNEXPECTED: Filter says shut down prematurely.\n");
        }
        else if (result instanceof Exception) {
          System.out.println("\nFilter threw exception:");
          ((Exception)result).printStackTrace();
        }
      
      }
      catch(SAXException e)
      {
        e.printStackTrace();
      }
  }
    */  
