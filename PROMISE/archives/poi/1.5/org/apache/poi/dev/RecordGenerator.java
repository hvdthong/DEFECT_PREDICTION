package org.apache.poi.dev;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;

public class RecordGenerator
{
    public static void main(String[] args)
            throws Exception
    {
        Class.forName("org.apache.poi.generator.FieldIterator");

        if (args.length != 4)
        {
            System.out.println("Usage:");
            System.out.println("  java org.apache.poi.hssf.util.RecordGenerator RECORD_DEFINTIONS RECORD_STYLES DEST_SRC_PATH TEST_SRC_PATH");
        } else
        {
            generateRecords(args[0], args[1], args[2], args[3]);
        }
    }

    private static void generateRecords(String defintionsDir, String recordStyleDir, String destSrcPathDir, String testSrcPathDir)
            throws Exception
    {
        File definitionsFile = new File(defintionsDir);

        for (int i = 0; i < definitionsFile.listFiles().length; i++)
        {
            File file = definitionsFile.listFiles()[i];
            if (file.isFile() &&
                    (file.getName().endsWith("_record.xml") ||
                    file.getName().endsWith("_type.xml")
                    )
            )
            {
                DocumentBuilderFactory factory =
                        DocumentBuilderFactory.newInstance();
                DocumentBuilder builder = factory.newDocumentBuilder();
                Document document = builder.parse(file);
                Element record = document.getDocumentElement();
                String extendstg = record.getElementsByTagName("extends").item(0).getFirstChild().getNodeValue();
                String suffix = record.getElementsByTagName("suffix").item(0).getFirstChild().getNodeValue();
                String recordName = record.getAttributes().getNamedItem("name").getNodeValue();
                String packageName = record.getAttributes().getNamedItem("package").getNodeValue();
                packageName = packageName.replace('.', '/');

                String destinationPath = destSrcPathDir + "/" + packageName;
                File destinationPathFile = new File(destinationPath);
                destinationPathFile.mkdirs();
                String destinationFilepath = destinationPath + "/" + recordName + suffix + ".java";
                String args[] = new String[]{"-in", file.getAbsolutePath(), "-xsl", recordStyleDir + "/" + extendstg.toLowerCase() + ".xsl",
                                             "-out", destinationFilepath,
                                             "-TEXT"};

                org.apache.xalan.xslt.Process.main(args);
                System.out.println("Generated " + suffix + ": " + destinationFilepath);

                destinationPath = testSrcPathDir + "/" + packageName;
                destinationPathFile = new File(destinationPath);
                destinationPathFile.mkdirs();
                destinationFilepath = destinationPath + "/Test" + recordName + suffix + ".java";
                if (new File(destinationFilepath).exists() == false)
                {
                    String temp = (recordStyleDir + "/" + extendstg.toLowerCase() + "_test.xsl");
                    args = new String[]{"-in", file.getAbsolutePath(), "-xsl",
                                        temp,
                                        "-out", destinationFilepath,
                                        "-TEXT"};
                    org.apache.xalan.xslt.Process.main(args);
                    System.out.println("Generated test: " + destinationFilepath);
                } else
                {
                    System.out.println("Skipped test generation: " + destinationFilepath);
                }
            }
        }
    }
}
