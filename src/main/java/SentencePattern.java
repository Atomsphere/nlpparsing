/**
 * PoC stuff.
 * Capturing sentence pattern
 * Parse -> sentence -> pattern -> pattern of patterns
 * need to work out paragraphs still.
 */

import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.util.CoreMap;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;


public class SentencePattern implements Runnable {
    StanfordCoreNLP pipeline;
    Properties props;
    File file;
    List<sentenceObject> macroPattern = new ArrayList<>();

    public SentencePattern(File file, StanfordCoreNLP pipeline){
        this.pipeline = pipeline;
        this.file = file;
        props = new Properties();
        props.setProperty("annotators", "tokenize, ssplit, pos, lemma, ner, parse, dcoref");//work on this
    }

    /**
     * We'll be excluding anything that isn't a sentence. ie. i don't want "chapter 1" as an input for now.
     * Dirty implementation
     * @param sentence
     */
    boolean isSentence(List<String> sentence){
        return true;
    }

    public void run(){

        // create an empty Annotation just with the given text
        Annotation document = new Annotation(file.getAbsolutePath());

        // run all Annotators on this text
        pipeline.annotate(document);

        // these are all the sentences in this document
// a CoreMap is essentially a Map that uses class objects as keys and has values with custom types
        List<CoreMap> sentences = document.get(CoreAnnotations.SentencesAnnotation.class);



        for (CoreMap sentence : sentences) {
            // traversing the words in the current sentence
            // a CoreLabel is a CoreMap with additional token-specific methods

            sentenceObject microPattern = new sentenceObject();
            for (CoreLabel token : sentence.get(CoreAnnotations.TokensAnnotation.class)) {
                // this is the text of the token
                String word = token.get(CoreAnnotations.TextAnnotation.class);
                // this is the POS tag of the token
                String pos = token.get(CoreAnnotations.PartOfSpeechAnnotation.class);

                microPattern.push(pos);
            }
            if(microPattern.isSentence()){
                macroPattern.add(microPattern);
            }
        }
    }
}
