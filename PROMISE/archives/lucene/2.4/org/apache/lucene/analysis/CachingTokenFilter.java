import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * This class can be used if the Tokens of a TokenStream
 * are intended to be consumed more than once. It caches
 * all Tokens locally in a List.
 * 
 * CachingTokenFilter implements the optional method
 * {@link TokenStream#reset()}, which repositions the
 * stream to the first Token. 
 *
 */
public class CachingTokenFilter extends TokenFilter {
  private List cache;
  private Iterator iterator;
  
  public CachingTokenFilter(TokenStream input) {
    super(input);
  }
  
  public Token next(final Token reusableToken) throws IOException {
    assert reusableToken != null;
    if (cache == null) {
      cache = new LinkedList();
      fillCache(reusableToken);
      iterator = cache.iterator();
    }
    
    if (!iterator.hasNext()) {
      return null;
    }
    Token nextToken = (Token) iterator.next();
    return (Token) nextToken.clone();
  }
  
  public void reset() throws IOException {
    if(cache != null) {
    	iterator = cache.iterator();
    }
  }
  
  private void fillCache(final Token reusableToken) throws IOException {
    for (Token nextToken = input.next(reusableToken); nextToken != null; nextToken = input.next(reusableToken)) {
      cache.add(nextToken.clone());
    }
  }

}
