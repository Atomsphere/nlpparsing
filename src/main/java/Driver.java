import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.ParserConfigurationException;

import java.io.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class Driver {
    public static void main(String[] args) throws ParserConfigurationException, IOException, SAXException {
        // creates a StanfordCoreNLP object, with POS tagging, lemmatization, NER, parsing, and coreference resolution

        ExecutorService threadpool = Executors.newFixedThreadPool(8);

        String isbn = null;

        File[] dir = new File("/home/mark/Documents/epub").listFiles();
        for (File folders : dir) {
            File[] folder = new File(folders.getAbsolutePath()).listFiles();
            boolean found = false;
            for (File epub : folder) {
                if (epub.getName().equals("content.opf")) {
                    File fXmlFile = new File(epub.getAbsolutePath());
                    DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
                    DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
                    Document opf = dBuilder.parse(fXmlFile);

                    opf.getDocumentElement().normalize();

                    NodeList nList = opf.getElementsByTagName("dc:identifier");


                    for (int i = 0; i < nList.getLength(); ++i) {
                        isbn = nList.item(i).getTextContent();
                        if (isbn.length() == 13) {
                            found = true;
                            break;
                        }
                    }
                }
            }

            if (found) {
                boolean flag = false;
                Long fileSize = 0L;
                File temp = null;

                for (File epub : folder) {
                    String ext = epub.getAbsolutePath().substring(epub.getAbsolutePath().lastIndexOf('.') + 1);

                    if (ext.equals("html")) {
                        if (fileSize < epub.getAbsoluteFile().length()) {
                            fileSize = epub.getAbsoluteFile().length();
                            temp = new File(epub.getAbsolutePath());
                            flag = true;
                        }
                    }
                }
                if (flag) {
                    Runnable r = new Page(isbn, temp);
                    //new Thread(r).start();

                    threadpool.submit(r);
                }
            }
        }
    }
}


//}