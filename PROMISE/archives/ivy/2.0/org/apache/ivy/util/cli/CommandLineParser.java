package org.apache.ivy.util.cli;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.ivy.util.StringUtils;

public class CommandLineParser {
    private static final int MIN_DESC_WIDTH = 40;
    private static final int MAX_SPEC_WIDTH = 30;
    private Map/*<String, Option>*/ options = new LinkedHashMap();
    private Map/*<String, List<Option>>*/ categories = new LinkedHashMap();
    
    public CommandLineParser() {
    }

    public CommandLineParser addCategory(String category) {
        categories.put(category, new ArrayList());
        return this;
    }
    
    public CommandLineParser addOption(Option option) {
        options.put(option.getName(), option);
        if (!categories.isEmpty()) {
            ((List) categories.values().toArray()[categories.values().size() - 1])
                .add(option);
        }
        return this;
    }
    
    public CommandLine parse(String[] args) throws ParseException {
        CommandLine line = new CommandLine();
        
        int index = args.length;
        ListIterator iterator = Arrays.asList(args).listIterator();
        while (iterator.hasNext()) {
            String arg = (String) iterator.next();
            if ("--".equals(arg)) {
                index = iterator.nextIndex();
                break;
            }

            if (!arg.startsWith("-")) {
                index = iterator.previousIndex();
                break;
            }
            
            Option option = (Option) options.get(arg.substring(1));
            if (option == null) {
                throw new ParseException("Unrecognized option: " + arg);
            }
            line.addOptionValues(arg.substring(1), option.parse(iterator));
        }
        
        String[] leftOverArgs = new String[args.length - index];
        System.arraycopy(args, index, leftOverArgs, 0, leftOverArgs.length);
        line.setLeftOverArgs(leftOverArgs);

        return line;
    }

    public void printHelp(PrintWriter pw, int width, String command, boolean showDeprecated) {
        pw.println("usage: " + command);
        int specWidth = 0;
        for (Iterator iterator = options.values().iterator(); iterator.hasNext();) {
            Option option = (Option) iterator.next();
            if (option.isDeprecated() && !showDeprecated) {
                continue;
            }
            specWidth = Math.min(MAX_SPEC_WIDTH, 
                Math.max(specWidth, option.getSpec().length()));
        }

        for (Iterator iterator = categories.entrySet().iterator(); iterator.hasNext();) {
            Entry entry = (Entry) iterator.next();
            String category = (String) entry.getKey();
            pw.println("==== " + category);
            List/*<Option>*/ options = (List) entry.getValue();
            for (Iterator it = options.iterator(); it.hasNext();) {
                Option option = (Option) it.next();
                if (option.isDeprecated() && !showDeprecated) {
                    continue;
                }
                String spec = option.getSpec();
                pw.print(" " + spec);
                int specLength = spec.length() + 1;
                pw.print(StringUtils.repeat(" ", specWidth - specLength));
                
                StringBuffer desc = new StringBuffer(
                    (option.isDeprecated() ? "DEPRECATED: " : "") + option.getDescription());
                int count = Math.min(desc.length(), width - Math.max(specLength, specWidth));
                if (count > MIN_DESC_WIDTH || desc.length() + specLength < width) {
                    pw.print(desc.substring(0, count));
                    desc.delete(0, count);
                }
                pw.println();
                
                while (desc.length() > 0) {
                    pw.print(StringUtils.repeat(" ", specWidth));
                    count = Math.min(desc.length(), width - specWidth);
                    pw.println(desc.substring(0, count));
                    desc.delete(0, count);
                }
            }
            pw.println();
        }
    }
}
