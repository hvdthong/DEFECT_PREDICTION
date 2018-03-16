import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.HashMap;
import java.util.HashSet;

/**
 * Loader for text files that represent a list of stopwords.
 *
 * @author Gerhard Schwarz
 * @version $Id: WordlistLoader.java 472959 2006-11-09 16:21:50Z yonik $
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
   * Reads a stem dictionary. Each line contains:
   * <pre>word<b>\t</b>stem</pre>
   * (i.e. two tab seperated words)
   *
   * @return stem dictionary that overrules the stemming algorithm
   * @throws IOException 
   */
  public static HashMap getStemDict(File wordstemfile) throws IOException {
    if (wordstemfile == null)
      throw new NullPointerException("wordstemfile may not be null");
    HashMap result = new HashMap();
    BufferedReader br = null;
    FileReader fr = null;
    try {
      fr = new FileReader(wordstemfile);
      br = new BufferedReader(fr);
      String line;
      while ((line = br.readLine()) != null) {
        String[] wordstem = line.split("\t", 2);
        result.put(wordstem[0], wordstem[1]);
      }
    } finally {
      if (fr != null)
        fr.close();
      if (br != null)
        br.close();
    }
    return result;
  }

}
