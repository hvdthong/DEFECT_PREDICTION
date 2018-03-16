package fr.jayasoft.ivy.resolver;

import java.io.IOException;
import java.net.URL;
import java.util.List;

public interface URLLister {
    /**
     * Indicates if this lister is able to list urls with the given pattern.
     * In general, only protocol is used.
     * @param pattern
     * @return
     */
    boolean accept(String pattern);
    
    List listAll(URL url) throws IOException;
}
