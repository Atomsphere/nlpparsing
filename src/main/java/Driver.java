import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.*;
import edu.stanford.nlp.util.CoreMap;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.ParserConfigurationException;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class Driver {
    public static void main(String[] args) throws ParserConfigurationException, IOException, SAXException {
        // creates a StanfordCoreNLP object, with POS tagging, lemmatization, NER, parsing, and coreference resolution
        Properties props = new Properties();
        props.setProperty("annotators", "tokenize, ssplit, pos, lemma, ner, parse, dcoref");
        StanfordCoreNLP pipeline = new StanfordCoreNLP(props);

        String isbn = null;

        //FileWriter file = new FileWriter("train.txt", true);
        //BufferedWriter writer = new BufferedWriter(file);

        File[] dir = new File("/home/mark/Documents/epub").listFiles();
        for (File folders : dir) {
            File[] folder = new File(folders.getAbsolutePath()).listFiles();
            boolean found = false;
            for(File epub : folder) {
                if (epub.getName().equals("content.opf")) {
                    File fXmlFile = new File(epub.getAbsolutePath());
                    DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
                    DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
                    Document opf = dBuilder.parse(fXmlFile);

                    opf.getDocumentElement().normalize();

                    NodeList nList = opf.getElementsByTagName("dc:identifier");


                    for(int i = 0; i < nList.getLength(); ++i){
                        //System.out.println("I made it");
                        //if(nList.item(i).getNodeVa().equals("ISBN")){
                            isbn = nList.item(i).getTextContent();
                            if(isbn.length() == 13) {
                                /*PrintWriter file = null;
                                try{
                                    file = new PrintWriter(new FileOutputStream("train.txt", true));
                                    file.println(isbn + " ");
                                    }catch(IOException e) {
                                        e.printStackTrace();
                                    }finally {
                                     file.close();
                                //We'll be appending to a string and outputting at end of a loop
                                }*/

                                found = true;
                            }


                    }




                }
            }

            if(found) {
                int wordcount = 0;
                int cc = 0;
                int exitensial = 0;
                int fw = 0;
                int prep = 0;
                int adj = 0;
                int adjc = 0;
                int adjs = 0;
                int modal = 0;
                int noun = 0;
                int nounProper = 0;
                int predet = 0;
                int pronoun = 0;
                int adverb = 0;
                int advercom = 0;
                int adverbsup = 0;
                int particle = 0;
                int interjection = 0;
                int verb = 0;
                int verbpast = 0;
                int verbg = 0;
                int verbthird = 0;
                int determiner = 0;
                int whpronoun = 0;
                int poswhpronoun = 0;
                int whadverb = 0;
                int adverbcom = 0;
                int verbfirst = 0;

                for (File epub : folder) {



                    String ext = epub.getAbsolutePath().substring(epub.getAbsolutePath().lastIndexOf('.') + 1);

                    if (ext.equals(".html")) {

                        //try {


                        String contents = new String(Files.readAllBytes(Paths.get(epub.getAbsolutePath())));


                        // read some text in the text variable
                        //String text = "Tacos are amazing, and you suck at listening.";

                        // create an empty Annotation just with the given text
                        Annotation document = new Annotation(contents);

                        // run all Annotators on this text
                        pipeline.annotate(document);

                        // these are all the sentences in this document
// a CoreMap is essentially a Map that uses class objects as keys and has values with custom types
                        List<CoreMap> sentences = document.get(CoreAnnotations.SentencesAnnotation.class);

                        for (CoreMap sentence : sentences) {
                            // traversing the words in the current sentence
                            // a CoreLabel is a CoreMap with additional token-specific methods
                            for (CoreLabel token : sentence.get(CoreAnnotations.TokensAnnotation.class)) {
                                // this is the text of the token
                                String word = token.get(CoreAnnotations.TextAnnotation.class);
                                // this is the POS tag of the token
                                String pos = token.get(CoreAnnotations.PartOfSpeechAnnotation.class);
                                // this is the NER label of the token
                                String ne = token.get(CoreAnnotations.NamedEntityTagAnnotation.class);

                                System.out.println(String.format("Print: word: [%s] pos: [%s] ne: [%s]", word, pos, ne));
                                /*Control structure for collecting stats
                                * commented cases are currently being ignored*/
                                if(!pos.equals(".") && !pos.equals(",") && !pos.equals(";") && !pos.equals(":") && !pos.equals("\"") && !pos.equals("(") && !pos.equals(")"))
                                    wordcount++;
                                switch (pos) {
                                    case "CC": //coordinating conjunction
                                        cc++;
                                        break;
                                    //case "CD": //cardinal number
                                     //   break;
                                    //case "DT": //determiner
                                        //break;
                                    case "EX": //existensial "there" I want to use this, but how? exitensials/word?
                                        exitensial++;
                                        break;
                                    case "FW": //foreign word
                                        fw++;
                                        System.out.println(String.format("foreign word: %S\n", word));
                                        break;
                                    case "IN": //Preposition or subordinating conjunction
                                        prep++;
                                        break;
                                    case "JJ":  //adjective
                                        adj++;
                                        break;
                                    case "JJR": //Adjective, comparative
                                        adj++;
                                        adjc++;
                                        break;
                                    case "JJS": //Adjective, superlative
                                        adj++;
                                        adjs++;
                                        break;
                                    //case "LS":  //List item marker
                                    case "MD":  //Modal
                                        modal++;
                                        break;
                                    case "NN":    //Noun, singular or mass
                                    case "NNS":    //Noun, plural
                                        noun++;
                                        break;
                                    case "NNP":    //Proper noun, singular
                                    case "NNPS":    //Proper noun, plural
                                        noun++;
                                        nounProper++; // propernoun/noun
                                        break;
                                    case "PDT":    //Predeterminer
                                        predet++;
                                        break;
                                    //case "POS":    //Possessive ending
                                    case "PRP":    //Personal pronoun
                                    case "PRP$":    //Possessive pronoun
                                        pronoun++;
                                        break;
                                    case "RB":    //Adverb
                                        adverb++;
                                        break;
                                    case "RBR":    //Adverb, comparative
                                        adverb++;
                                        adverbcom++;
                                        break;
                                    case "RBS":    //Adverb, superlative
                                        adverb++;
                                        adverbsup++;
                                        break;
                                    case "RP":    //Particle
                                        particle++;
                                        break;
                                    //case "SYM":    //Symbol
                                    //case "TO":    //to
                                    case "UH":    //Interjection
                                        interjection++;
                                        break;
                                    case "VB":    //Verb, base form
                                        verb++;
                                        break;
                                    case "VBD":    //Verb, past tense
                                        verbpast++;
                                        verb++;
                                        break;
                                    case "VBG":    //Verb, gerund or present participle
                                        verbg++;
                                        verb++;
                                        break;
                                    case "VBN":    //Verb, past participle
                                        verb++;
                                        verbpast++;
                                        break;
                                    case "VBP":    //Verb, non-3rd person singular present
                                        verbfirst++;
                                        verb++;
                                        break;
                                    case "VBZ":    //Verb, 3rd person singular present
                                        verbthird++;
                                        verb++;
                                        break;
                                    case "WDT":    //Wh-determiner
                                        determiner++;
                                        break;
                                    case "WP":    //Wh-pronoun
                                        pronoun++;
                                        whpronoun++;
                                        break;
                                    case "WP$":    //Possessive wh-pronoun
                                        pronoun++;
                                        poswhpronoun++;
                                        break;
                                    case "WRB":    //Wh-adverb
                                        adverb++;
                                        whadverb++;
                                        break;
                                }
                            }
                        }



                    }
                }

                if(wordcount > 0){
                    isbn += " ";
                }
                //call stat building info and print statements here
            }
        }

    }
}
