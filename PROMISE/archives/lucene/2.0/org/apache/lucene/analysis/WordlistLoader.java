import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.io.BufferedReader;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;

/**
 * Loader for text files that represent a list of stopwords.
 *
 * @author Gerhard Schwarz
 * @version $Id: WordlistLoader.java 387550 2006-03-21 15:36:32Z yonik $
 */
public class WordlistLoader {

  /**
   * Loads a text file and adds every line as an entry to a HashSet (omitting
   * leading and trailing whitespace). Every line of the file should contain only
   * one word. The words need to be in lowercase if you make use of an
   * Analyzer which uses LowerCaseFilter (like StandardAnalyzer).
   *
   * @param wordfile File containing the wordlist
   * @return A HashSet with the file's words
   */
  public static HashSet getWordSet(File wordfile) throws IOException {
    HashSet result = new HashSet();
    FileReader reader = null;
    try {
      reader = new FileReader(wordfile);
      result = getWordSet(reader);
    }
    finally {
      if (reader != null)
        reader.close();
    }
    return result;
  }

  /**
   * Reads lines from a Reader and adds every line as an entry to a HashSet (omitting
   * leading and trailing whitespace). Every line of the Reader should contain only
   * one word. The words need to be in lowercase if you make use of an
   * Analyzer which uses LowerCaseFilter (like StandardAnalyzer).
   *
   * @param reader Reader containing the wordlist
   * @return A HashSet with the reader's words
   */
  public static HashSet getWordSet(Reader reader) throws IOException {
    HashSet result = new HashSet();
    BufferedReader br = null;
    try {
      if (reader instanceof BufferedReader) {
        br = (BufferedReader) reader;
      } else {
        br = new BufferedReader(reader);
      }
      String word = null;
      while ((word = br.readLine()) != null) {
        result.add(word.trim());
      }
    }
    finally {
      if (br != null)
        br.close();
    }
    return result;
  }


  /**
   * Builds a wordlist table, using words as both keys and values
   * for backward compatibility.
   *
   * @param wordSet   stopword set
   */
  private static Hashtable makeWordTable(HashSet wordSet) {
    Hashtable table = new Hashtable();
    for (Iterator iter = wordSet.iterator(); iter.hasNext();) {
      String word = (String)iter.next();
      table.put(word, word);
    }
    return table;
  }
}
