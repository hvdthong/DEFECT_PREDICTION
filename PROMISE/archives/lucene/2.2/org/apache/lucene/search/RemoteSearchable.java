import org.apache.lucene.document.Document;
import org.apache.lucene.document.FieldSelector;
import org.apache.lucene.index.Term;
import org.apache.lucene.index.CorruptIndexException;

import java.io.IOException;
import java.rmi.Naming;
import java.rmi.RMISecurityManager;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

/**
 * A remote searchable implementation.
 *
 * @version $Id: RemoteSearchable.java 514675 2007-03-05 14:28:01Z gsingers $
 */
public class RemoteSearchable
  extends UnicastRemoteObject
  implements Searchable {
  
  private Searchable local;
  
  /** Constructs and exports a remote searcher. */
  public RemoteSearchable(Searchable local) throws RemoteException {
    super();
    this.local = local;
  }


  public void search(Weight weight, Filter filter, HitCollector results)
    throws IOException {
    local.search(weight, filter, results);
  }

  public void close() throws IOException {
    local.close();
  }

  public int docFreq(Term term) throws IOException {
    return local.docFreq(term);
  }


  public int[] docFreqs(Term[] terms) throws IOException {
    return local.docFreqs(terms);
  }

  public int maxDoc() throws IOException {
    return local.maxDoc();
  }

  public TopDocs search(Weight weight, Filter filter, int n) throws IOException {
    return local.search(weight, filter, n);
  }


  public TopFieldDocs search (Weight weight, Filter filter, int n, Sort sort)
  throws IOException {
    return local.search (weight, filter, n, sort);
  }

  public Document doc(int i) throws CorruptIndexException, IOException {
    return local.doc(i);
  }

  public Document doc(int i, FieldSelector fieldSelector) throws CorruptIndexException, IOException {
	    return local.doc(i, fieldSelector);
  }
  
  public Query rewrite(Query original) throws IOException {
    return local.rewrite(original);
  }

  public Explanation explain(Weight weight, int doc) throws IOException {
    return local.explain(weight, doc);
  }

  /** Exports a searcher for the index in args[0] named
  public static void main(String args[]) throws Exception {
    String indexName = null;
    
    if (args != null && args.length == 1)
      indexName = args[0];
    
    if (indexName == null) {
      System.out.println("Usage: org.apache.lucene.search.RemoteSearchable <index>");
      return;
    }
    
    if (System.getSecurityManager() == null) {
      System.setSecurityManager(new RMISecurityManager());
    }
    
    Searchable local = new IndexSearcher(indexName);
    RemoteSearchable impl = new RemoteSearchable(local);
      
  }

}
