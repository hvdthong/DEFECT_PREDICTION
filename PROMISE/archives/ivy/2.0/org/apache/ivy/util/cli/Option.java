package org.apache.ivy.util.cli;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

public class Option {
    private String name;
    private String[] args;
    private String description;
    private boolean required;
    private boolean countArgs;
    private boolean deprecated;
    
    Option(String name, String[] args, String description, 
            boolean required, boolean countArgs, boolean deprecated) {
        this.name = name;
        this.args = args;
        this.description = description;
        this.required = required;
        this.countArgs = countArgs;
        this.deprecated = deprecated;
        if (required) {
            throw new UnsupportedOperationException("required option not supported yet");
        }
    }

    public String getName() {
        return name;
    }
    public String[] getArgs() {
        return args;
    }
    public String getDescription() {
        return description;
    }
    public boolean isRequired() {
        return required;
    }
    public boolean isCountArgs() {
        return countArgs;
    }
    public boolean isDeprecated() {
        return deprecated;
    }

    String[] parse(ListIterator iterator) throws ParseException {
        if (isCountArgs()) {
            String[] values = new String[args.length];
            for (int i = 0; i < values.length; i++) {
                if (!iterator.hasNext()) {
                    missingArgument(i);
                }
                values[i] = (String) iterator.next();
                if (values[i].startsWith("-")) {
                    missingArgument(i);
                }
            }
            return values;
        } else {
            List values = new ArrayList();
            while (iterator.hasNext()) {
                String value = (String) iterator.next();
                if (value.startsWith("-")) {
                    iterator.previous();
                    break;
                }
                values.add(value);
            }
            return (String[]) values.toArray(new String[values.size()]);
        }
    }

    private void missingArgument(int i) throws ParseException {
        if (i == 0) {
            throw new ParseException("no argument for: " + name);
        } else {
            throw new ParseException("missing argument for: " + name 
                + ". Expected: " + getArgsSpec());
        }
    }

    public String getSpec() {
        return "-" + name + " " + getArgsSpec();
    }

    private String getArgsSpec() {
        if (args.length == 0) {
            return "";
        }
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < args.length; i++) {
            sb.append("<").append(args[i]).append("> ");
        }
        return sb.toString();
    }
}
