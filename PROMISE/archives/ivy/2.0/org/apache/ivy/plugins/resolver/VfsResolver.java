package org.apache.ivy.plugins.resolver;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.ivy.plugins.repository.vfs.VfsRepository;

/**
 *
 */
public class VfsResolver extends RepositoryResolver {

    private static final int PASSWORD_GROUP = 2;

    public VfsResolver() {
        setRepository(new VfsRepository());
    }

    public String getTypeName() {
        return "vfs";
    }

    public String hidePassword(String name) {
        return prepareForDisplay(name);
    }

    public static String prepareForDisplay(String name) {
        StringBuffer s = new StringBuffer(name);
        Matcher m = URL_PATTERN.matcher(s);
        if (m.matches()) {
            final String password = m.group(PASSWORD_GROUP);
            final int passwordposi = s.indexOf(password);
            StringBuffer stars = new StringBuffer(password);
            for (int posi = 0; posi < password.length(); posi++) {
                stars.setCharAt(posi, '*');
            }
            String replacement = stars.toString();
            s = s.replace(passwordposi, passwordposi + password.length(), replacement);
        }
        return s.toString();

    }
}
