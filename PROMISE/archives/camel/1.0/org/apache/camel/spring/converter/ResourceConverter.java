package org.apache.camel.spring.converter;

import org.apache.camel.Converter;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

/**
 * Some Spring based
 *
 * @version $Revision: 524222 $
 */
@Converter
public class ResourceConverter {
    @Converter
    public static InputStream toInputStream(Resource resource) throws IOException {
        return resource.getInputStream();
    }

    @Converter
    public static File toFile(Resource resource) throws IOException {
        return resource.getFile();
    }

    @Converter
    public static URL toUrl(Resource resource) throws IOException {
        return resource.getURL();
    }

    @Converter
    public static UrlResource toResource(String uri) throws IOException {
        return new UrlResource(uri);
    }

    @Converter
    public static UrlResource toResource(URL uri) throws IOException {
        return new UrlResource(uri);
    }

    @Converter
    public static FileSystemResource toResource(File file) throws IOException {
        return new FileSystemResource(file);
    }

    @Converter
    public static ByteArrayResource toResource(byte[] data) throws IOException {
        return new ByteArrayResource(data);
    }
}
