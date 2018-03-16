package org.apache.camel.component.atom;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import org.apache.abdera.Abdera;
import org.apache.abdera.model.Document;
import org.apache.abdera.model.Feed;
import org.apache.abdera.parser.ParseException;
import org.apache.abdera.parser.Parser;

/**
 * Atom utilities.
 */
public final class AtomUtils {

    private AtomUtils() {
    }

    /**
     * Gets the Atom parser.
     */
    public static Parser getAtomParser() {
        return Abdera.getInstance().getParser();
    }

    /**
     * Parses the given uri and returns the response as a atom feed document.
     *
     * @param uri the uri for the atom feed.
     * @return  the document
     * @throws IOException is thrown if error reading from the uri
     * @throws ParseException is thrown if the parsing failed
     */
    public static Document<Feed> parseDocument(String uri) throws IOException, ParseException {
        InputStream in = new URL(uri).openStream();
        return getAtomParser().parse(in);
    }

}
