package langguesser;
import java.util.HashMap;

public class Analysis {
    
    // DATASET
    private final String data;
   
    // SCORE PLACEHOLDERS
    private double letters = 0;
    private double windows = 0;
    private double firsts = 0;
    
    // CONSTRUCTOR
    public Analysis(String _data) {
        
        // FORCE LOWERCASE & SET
         this.data = _data.toLowerCase();
        
        // FIND THE SCORES
        letters();
        windows();
        firsts();
    }
    
    // FIND LETTER OCCURRANCE
    private void letters() {
            
        // DECLARE CONTAINER
        HashMap<String, Double> container = new HashMap();
        
        // NUKE WHITESPACE
        String data = this.data.replaceAll("\\s+","");
        
        // SPLIT THE WORD INTO LETTERS
        String[] letters = data.split("");

        // LOOP THROUGH THE LETTERS
        for (String letter : letters) {
            
            // INJECT IF UNDEFINED, OTHERWISE INCREMENT VALUE
            container.merge(letter, 1.0, Double::sum); 
        }
        
        // MORPH VALUES TO PERCENTAGES
        this.letters = find_score(container, letters.length);
    }
    
    // FIND WINDOW OCCURRANCE
    private void windows() {
        
        // DECLARE CONTAINER
        HashMap<String, Double> container = new HashMap();
        
        // NUKE WHITESPACE
        String data = this.data.replaceAll("\\s+","");
        
        // SPLIT THE WORD INTO LETTERS
        String[] letters = data.split("");
        
        // FIND WINDOW COUNT
        Integer count = letters.length - 2;
        
        // LOOP THROUGH EACH LETTER WITH TWO SUCCESSORS
        for (int x = 0; x < count; x++) {
            
            // CALCULATE THE WINDOW VALUE
            String value = letters[x] + letters[x + 1] + letters[x + 2];
            
            // INJECT IF UNDEFINED, OTHERWISE INCREMENT VALUE
            container.merge(value, 1.0, Double::sum); 
        }
        
        // MORPH VALUES TO PERCENTAGES
        this.windows = find_score(container, count);
    }
    
    // FIND FIRST LETTERS IN SENTENCES
    private void firsts() {
        
        // DECLARE CONTAINER
        HashMap<String, Double> container = new HashMap();
        
        // SPLIT BY SPACE
        String[] words = this.data.split(" ");
        
        // FIND WORD COUNT
        Integer count = words.length;
        
        // LOOP THROUGH EACH SENTENCE
        for (String word : words) {
            
            // FIND THE FIRST LETTER
            String first_letter = word.substring(0, 1);
            
            // INJECT IF UNDEFINED, OTHERWISE INCREMENT VALUE
            container.merge(first_letter, 1.0, Double::sum);
        }

        // MORPH VALUES TO PERCENTAGES
        this.firsts = find_score(container, count);
    }
    
    // CONVERT OCCURRENCE VALUE TO PERCENT
    private double find_score(HashMap<String, Double> map, Integer length) {
        
        // DECLARE SUM
        double sum = 0;
        
        // LOOP THROUGH EACH
        for (String key : map.keySet()) {
            
            // FIND PERCENTAGE
            double percent = map.get(key) / length;
            
            // ADD IT TO THE EXISTING SUM
            sum += percent;
        }
        
        // RETURN THE AVERAGE VALUE
        return sum / map.size();
    }
    
    // GETTERS
    public double letter_score() { return this.letters; }
    public double window_score() { return this.windows; }
    public double firsts_score() { return this.firsts; }
}
