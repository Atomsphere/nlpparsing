import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.*;
import edu.stanford.nlp.util.CoreMap;
import org.jsoup.Jsoup;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.ParserConfigurationException;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;


public class Driver {
    public static void main(String[] args) throws ParserConfigurationException, IOException, SAXException {
        // creates a StanfordCoreNLP object, with POS tagging, lemmatization, NER, parsing, and coreference resolution
        Properties props = new Properties();
        props.setProperty("annotators", "tokenize, ssplit, pos, lemma, ner, parse, dcoref");
        props.put("threads", "4");
        StanfordCoreNLP pipeline = new StanfordCoreNLP(props);

        String isbn = null;

        //FileWriter file = new FileWriter("train.txt", true);
        //BufferedWriter writer = new BufferedWriter(file);

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
                        //System.out.println("I made it");
                        //if(nList.item(i).getNodeVa().equals("ISBN")){
                        isbn = nList.item(i).getTextContent();
                        if (isbn.length() == 13) {
                            found = true;
                        }
                    }
                }
            }

            if (found) {
                double wordcount = 0;
                double cc = 0;
                double exitensial = 0;
                double fw = 0;
                double prep = 0;
                double adj = 0;
                double adjc = 0;
                double adjs = 0;
                double modal = 0;
                double noun = 0;
                double nounProper = 0;
                double predet = 0;
                double pronoun = 0;
                double adverb = 0;
                double advercom = 0;
                double adverbsup = 0;
                double particle = 0;
                double interjection = 0;
                double verb = 0;
                double verbpast = 0;
                double verbg = 0;
                double verbthird = 0;
                double determiner = 0;
                double whpronoun = 0;
                double poswhpronoun = 0;
                double whadverb = 0;
                double adverbcom = 0;
                double verbfirst = 0;
                Long fileSize = 0L;
                String contents = null;
                File temp = null;
                boolean flag = false;

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

                    System.out.println("Processing " + temp.getAbsoluteFile() + "\n");

                    String html = new String(Files.readAllBytes(Paths.get(temp.getAbsolutePath())), StandardCharsets.UTF_8);

                    org.jsoup.nodes.Document doc = Jsoup.parse(html);
                    contents = doc.body().text();


                    //Files.lines(Path.get(epub.getAbsolutePath()), StandardCharsets.UTF_8).forEach(System.out::println);

                    //System.out.println(contents);
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

                            //System.out.println(String.format("Print: word: [%s] pos: [%s] ne: [%s]", word, pos, ne));
                            /*Control structure for collecting stats
                             * commented cases are currently being ignored*/
                            if (!pos.equals(".") && !pos.equals(",") && !pos.equals(";") && !pos.equals(":") &&
                                    !pos.equals("\"") && !pos.equals("(") && !pos.equals(")") && !word.substring(0, 0).equals("<")
                                    && !word.equals("?") && !word.equals("!"))
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
                                //    System.out.println(String.format("foreign word: %S\n", word));
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


                    if (wordcount > 0) {
                        isbn += " ";
                        isbn += wordcount;
                        isbn += " ";
                        isbn += cc / wordcount;
                        isbn += " ";
                        isbn += fw / wordcount;
                        isbn += " ";
                        isbn += prep / wordcount;
                        isbn += " ";
                        isbn += adj / wordcount;
                        isbn += " ";
                        if (adj > 0) {
                            isbn += adjc / adj;
                            isbn += " ";
                            isbn += adjs / adj;
                        } else {
                            isbn += 0;
                            isbn += " ";
                            isbn += 0;
                        }
                        isbn += " ";
                        isbn += modal / wordcount;
                        isbn += " ";
                        isbn += noun / wordcount;
                        isbn += " ";
                        isbn += nounProper / wordcount;
                        isbn += " ";
                        isbn += predet / wordcount;
                        isbn += " ";
                        isbn += pronoun / wordcount;
                        isbn += " ";
                        isbn += adverb / wordcount;
                        isbn += " ";
                        if (adverb > 0) {
                            isbn += adverbcom / adverb;
                            isbn += " ";
                            isbn += adverbsup / adverb;
                            isbn += " ";
                            isbn += whadverb / adverb;
                        } else {
                            isbn += 0;
                            isbn += " ";
                            isbn += 0;
                            isbn += " ";
                            isbn += 0;
                        }
                        isbn += " ";
                        isbn += particle / wordcount;
                        isbn += " ";
                        isbn += interjection / wordcount;
                        isbn += " ";
                        isbn += verb / wordcount;
                        isbn += " ";
                        if (verb > 0) {
                            isbn += verbfirst / verb;
                            isbn += " ";
                            isbn += verbg / verb;
                            isbn += " ";
                            isbn += verbpast / verb;
                            isbn += " ";
                            isbn += verbthird / verb;
                        } else {
                            isbn += 0;
                            isbn += " ";
                            isbn += 0;
                            isbn += " ";
                            isbn += 0;
                            isbn += " ";
                            isbn += 0;
                        }
                        isbn += " ";
                        isbn += determiner / wordcount;
                        isbn += " ";
                        isbn += whpronoun / wordcount;
                        isbn += " ";
                        isbn += poswhpronoun / wordcount;
                    } else {
                        isbn += " ";
                        isbn += 0;
                    }
                    PrintWriter file = null;
                    try {
                        file = new PrintWriter(new FileOutputStream("train.txt", true));
                        file.println(isbn);
                    } catch (IOException e) {
                        e.printStackTrace();
                    } finally {
                        file.close();
                    }

                    System.out.println("Finished with " + temp.getAbsoluteFile() + "\n");
                }
            }
        }
    }

}

