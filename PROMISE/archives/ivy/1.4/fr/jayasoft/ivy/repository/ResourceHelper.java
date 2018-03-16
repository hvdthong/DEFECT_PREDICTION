package fr.jayasoft.ivy.repository;

import java.io.File;
import java.net.MalformedURLException;

import fr.jayasoft.ivy.repository.file.FileResource;
import fr.jayasoft.ivy.repository.url.URLResource;

public class ResourceHelper {
    public static boolean equals(Resource res, File f) {
        if (res == null && f == null) {
            return true;
        }
        if (res == null || f == null) {
            return false;
        }
        if (res instanceof FileResource) {
            return new File(res.getName()).equals(f);
        } else if (res instanceof URLResource) {
            try {
                return f.toURL().toExternalForm().equals(res.getName());
            } catch (MalformedURLException e) {
                return false;
            }
        }
        return false;
    }
}
