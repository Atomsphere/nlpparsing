import java.util.List;

public class sentenceObject {

    List<String> sentence;

    public void push(String word){
        sentence.add(word);
    }

    public boolean isSentence(){
        boolean subject = false;
        boolean verb = false;

        for(String i : sentence){
            switch(i){
                case "NN":
                case "NNS":
                case "NNP":
                case "NNPS":
                case "PRP":
                case "PRP$":
                    subject = true;
                    break;
                case "VB":
                case "VBD":
                case "VBG":
                case "VBN":
                case "VBP":
                case "VBZ":
                    verb = true;
                    break;
            }
        }

        return subject && verb;
    }
}
