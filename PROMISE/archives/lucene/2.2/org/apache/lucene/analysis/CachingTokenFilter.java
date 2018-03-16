import java.io.IOException;
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
  private int index;
  
  public CachingTokenFilter(TokenStream input) {
    super(input);
  }
  
  public Token next() throws IOException {
    if (cache == null) {
      cache = new LinkedList();
      fillCache();
    }
    
    if (index == cache.size()) {
      return null;
    }
    
    return (Token) cache.get(index++);
  }
  
  public void reset() throws IOException {
    index = 0;
  }
  
  private void fillCache() throws IOException {
    Token token;
    while ( (token = input.next()) != null) {
      cache.add(token);
    }
  }

}
