import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.*;
import edu.stanford.nlp.util.CoreMap;

import java.io.File;
import java.util.*;

public class Driver {
    public static void main(String[] args){
        // creates a StanfordCoreNLP object, with POS tagging, lemmatization, NER, parsing, and coreference resolution
        Properties props = new Properties();
        props.setProperty("annotators", "tokenize, ssplit, pos, lemma, ner, parse, dcoref");
        StanfordCoreNLP pipeline = new StanfordCoreNLP(props);



        File[] dir = new File("~/Documents/epub").listFiles();
        for (File folders : dir) {
            File[] folder = new File(folders.getAbsolutePath()).listFiles();
            boolean found = false;
            for(File epub : folder) {
                if (epub.getName().equals("content.opf")) {
                    found = true;
                    File content = new File(epub.getAbsolutePath());

                }
            }
            if(found){
            for(File epub : folder) {


                    String ext = epub.getAbsolutePath().substring(epub.getAbsolutePath().lastIndexOf('.') + 1);

                // read some text in the text variable
                String text = "Tacos are amazing, and you suck at listening.";

                // create an empty Annotation just with the given text
                Annotation document = new Annotation(text);

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


                        switch (pos) {
                            case "CC": //coordinating conjunction
                                break;
                            case "CD": //cardinal number
                                break;
                            case "DT": //determiner
                                break;
                            case "EX": //existensial "there"
                                break;
                            case "FW": //foreign word
                                System.out.println(String.format("foreign word: %S\n", word));
                                break;
                            case "IN": //Preposition or subordinating conjunction
                                break;
                            case "JJ":  //adjective
                            case "JJR": //Adjective, comparative
                            case "JJS": //Adjective, superlative
                            case "LS":  //List item marker
                            case "MD":  //Modal
                            case "NN":    //Noun, singular or mass
                            case "NNS":    //Noun, plural
                            case "NNP":    //Proper noun, singular
                            case "NNPS":    //Proper noun, plural
                            case "PDT":    //Predeterminer
                            case "POS":    //Possessive ending
                            case "PRP":    //Personal pronoun
                            case "PRP$":    //Possessive pronoun
                            case "RB":    //Adverb
                            case "RBR":    //Adverb, comparative
                            case "RBS":    //Adverb, superlative
                            case "RP":    //Particle
                            case "SYM":    //Symbol
                            case "TO":    //to
                            case "UH":    //Interjection
                            case "VB":    //Verb, base form
                            case "VBD":    //Verb, past tense
                            case "VBG":    //Verb, gerund or present participle
                            case "VBN":    //Verb, past participle
                            case "VBP":    //Verb, non-3rd person singular present
                            case "VBZ":    //Verb, 3rd person singular present
                            case "WDT":    //Wh-determiner
                            case "WP":    //Wh-pronoun
                            case "WP$":    //Possessive wh-pronoun
                            case "WRB":    //Wh-adverb
                        }
                    }
                }


                }
            }
        }

    }
}
