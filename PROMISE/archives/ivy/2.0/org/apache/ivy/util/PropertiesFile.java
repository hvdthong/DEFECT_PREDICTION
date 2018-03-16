package org.apache.ivy.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * A simple Properties extension easing the loading and saving of data
 */
public class PropertiesFile extends Properties {
    private File file;

    private String header;

    public PropertiesFile(File file, String header) {
        this.file = file;
        this.header = header;
        if (file.exists()) {
            FileInputStream fis = null;
            try {
                fis = new FileInputStream(file);
                load(fis);
            } catch (Exception ex) {
                Message.warn("exception occured while reading properties file " + file + ": "
                        + ex.getMessage());
            }
            try {
                if (fis != null) {
                    fis.close();
                }
            } catch (IOException e) {
            }
        }
    }

    public void save() {
        FileOutputStream fos = null;
        try {
            if (file.getParentFile() != null && !file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }
            fos = new FileOutputStream(file);
            store(fos, header);
        } catch (Exception ex) {
            Message.warn("exception occured while writing properties file " + file + ": "
                    + ex.getMessage());
        }
        try {
            if (fos != null) {
                fos.close();
            }
        } catch (IOException e) {
        }
    }

}
