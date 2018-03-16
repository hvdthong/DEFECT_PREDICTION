import java.io.ObjectStreamException;
import java.io.Serializable;
import java.io.StreamCorruptedException;
import java.util.HashMap;
import java.util.Map;

/**
 * A serializable Enum class.
 */
public abstract class Parameter implements Serializable
{
  static Map allParameters = new HashMap();
  
  private String name;
  
  private Parameter() {
  }
  
  protected Parameter(String name) {
    this.name = name;
    String key = makeKey(name);
    
    if(allParameters.containsKey(key))
      throw new IllegalArgumentException("Parameter name " + key + " already used!");
    
    allParameters.put(key, this);
  }
  
  private String makeKey(String name){
    return getClass() + " " + name;
  }
  
  public String toString() {
    return name;
  }
  
  /**
   * Resolves the deserialized instance to the local reference for accurate
   * equals() and == comparisons.
   * 
   * @return a reference to Parameter as resolved in the local VM
   * @throws ObjectStreamException
   */
  protected Object readResolve() throws ObjectStreamException {
    Object par = allParameters.get(makeKey(name));
    
    if(par == null)
      throw new StreamCorruptedException("Unknown parameter value: " + name);
      
    return par;
  }
  
 }
