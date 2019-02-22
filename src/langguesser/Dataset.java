package langguesser;
import java.util.HashMap;

public class Dataset {
    
    // HASHMAPS
    private HashMap<String, Double> letters = new HashMap();
    private HashMap<String, Double> windows = new HashMap();
    private HashMap<String, Double> firsts = new HashMap();
    
    // CONSTRUCTOR
    public Dataset(String data) {
        
        // FORCE LOWERCASE & SET
        data = sanitize(data);
        
        // RUN MODULES
        letters(data);
        windows(data);
        firsts(data);
    }
    
    // SANITIZE DATA
    private String sanitize(String data) {
        
        data = data.toLowerCase();
        
        // NUKE SPECIAL CHARACTERS
        data = data.replaceAll("[^a-z\\såäöæøüßàèéì'çšžõ]", "");
               
        // SHRINK MULTIPLE SPACES INTO ONE
        data = data.replaceAll("( +)", " ");
        
        return data;
    }
    
    // FIND LETTER OCCURRANCE
    private void letters(String data) {
        
        // NUKE WHITESPACE
        data = data.replaceAll("\\s+","");
        
        // SPLIT THE WORD INTO LETTERS
        String[] letters = data.split("");

        // LOOP THROUGH THE LETTERS
        for (String letter : letters) {
            
            // INJECT IF UNDEFINED, OTHERWISE INCREMENT VALUE
            this.letters.merge(letter, 1.0, Double::sum); 
        }
        
        // MORPH VALUES TO PERCENTAGES
        this.letters = percentify(this.letters, letters.length);
    }
    
    // FIND WINDOW OCCURRANCE
    private void windows(String data) {
        
        // NUKE WHITESPACE
        data = data.replaceAll("\\s+","");
        
        // SPLIT THE WORD INTO LETTERS
        String[] letters = data.split("");
        
        // FIND WINDOW COUNT
        Integer count = letters.length - 2;
        
        // LOOP THROUGH EACH LETTER WITH TWO SUCCESSORS
        for (int x = 0; x < count; x++) {
            
            // CALCULATE THE WINDOW VALUE
            String value = letters[x] + letters[x + 1] + letters[x + 2];
            
            // INJECT IF UNDEFINED, OTHERWISE INCREMENT VALUE
            this.windows.merge(value, 1.0, Double::sum); 
        }
        
        // MORPH VALUES TO PERCENTAGES
        this.windows = percentify(this.windows, count);
    }
    
    // FIND FIRST LETTERS IN SENTENCES
    private void firsts(String data) {
        
        // SPLIT BY SPACE
        String[] words = data.split(" ");
        
        // FIND WORD COUNT
        Integer count = words.length;
        
        // LOOP THROUGH EACH SENTENCE
        for (String word : words) {
            
            // FIND THE FIRST LETTER
            String first_letter = word.substring(0, 1);
            
            // INJECT IF UNDEFINED, OTHERWISE INCREMENT VALUE
            this.firsts.merge(first_letter, 1.0, Double::sum);
        }

        // MORPH VALUES TO PERCENTAGES
        this.firsts = percentify(this.firsts, count);
    }
    
    // CONVERT OCCURRENCE VALUE TO PERCENT
    private HashMap<String, Double> percentify(HashMap<String, Double> map, Integer length) {
        
        // LOOP THROUGH EACH
        for (String key : map.keySet()) {
            
            // FIND PERCENTAGE
            double percent = map.get(key) / length;
            
            // REPLACE THE OLD VALUE
            map.replace(key, percent);
        }
        
        return map;
    }
    
    // GETTERS
    public HashMap<String, Double> get_letters() { return this.letters; }
    public HashMap<String, Double> get_windows() { return this.windows; }
    public HashMap<String, Double> get_firsts() { return this.firsts; }
}
