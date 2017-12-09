import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.util.CoreMap;
import org.jsoup.Jsoup;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.locks.ReentrantLock;

import static sun.misc.Version.println;

public class Page implements Runnable {
    Properties props;
    //props.put("threads", "8");
    StanfordCoreNLP pipeline;

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

    String isbn;
    String contents = null;
    File file;
    String rating;
    String count;
    int round;



    public Page(String isbn, File file, String rating, String count, int round){
        this.file = file;
        this.isbn = isbn;
        props = new Properties();
        props.setProperty("annotators", "tokenize, ssplit, pos, lemma, ner, parse, dcoref");
        pipeline = new StanfordCoreNLP(props);
        this.rating = rating;
        this.count = count;
        this.round = round;

    }

    public void run() {
        System.out.println(round + ": Processing " + file.getAbsoluteFile() + "\n");

        String html = null;
        try {
            html = new String(Files.readAllBytes(Paths.get(file.getAbsolutePath())), StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
        }

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
            isbn += rating;
            isbn += " ";
            isbn += count;
            isbn += " ";
            isbn += wordcount;
            isbn += " ";
            isbn += cc / wordcount;
            isbn += " ";
            isbn += exitensial;
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
        printResults();
        System.out.println(round + ": Done processing " + file.getAbsoluteFile() + "\n");
    }

    public void printResults(){
        ReentrantLock aLock = new ReentrantLock();
        aLock.lock();
        try
        {
            PrintWriter file = null;
            file = new PrintWriter(new FileOutputStream("train.txt", true));
            file.println(isbn);
            file.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally
        {
            aLock.unlock();
        }


    }
}
