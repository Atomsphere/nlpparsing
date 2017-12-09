import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.ParserConfigurationException;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Driver {

    static ArrayList<RatingInfo> ratings = new ArrayList();

    static void createList(){
        File shortList = new File("shortlist.dat");

        String dat = null;
        try {
            dat = new String(Files.readAllBytes(Paths.get(shortList.getAbsolutePath())), StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
        }
        String lines[] = dat.split("\\r?\\n");

        for(String line : lines){
            RatingInfo rating = new RatingInfo(line);
            ratings.add(rating);
        }
    }

    static boolean inShortList(String isbn){

        for(RatingInfo rate : ratings){
            if(rate.isbn.equals(isbn))
                return true;
        }
        return false;
    }
    public static void main(String[] args) throws ParserConfigurationException, IOException, SAXException {

        ExecutorService threadpool = Executors.newFixedThreadPool(8);

        String isbn = null;

        createList();

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
                            found = inShortList(isbn);
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
                    threadpool.submit(r);
                }
            }
        }
    }
}


//}