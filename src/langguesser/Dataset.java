package langguesser;
import java.util.HashMap;

public class Dataset {
    
    // LANGUAGE NAME
    private final String language;
    
    // OCCURRENCE HASHMAPS
    private HashMap<String, Double> letters = new HashMap();
    private HashMap<String, Double> windows = new HashMap();
    private HashMap<String, Double> firsts = new HashMap();
    
    // CONSTRUCTOR
    public Dataset(String _language, String _data) {
        
        // SET LANGUAGE NAME
        this.language = _language;
        
        // SANITIZE THE DATA
        _data = sanitize(_data);
        
        // FIND OCCURRENCES
        find_letters(_data);
        find_windows(_data);
        find_firsts(_data);
    }
    
    // SANITIZE DATA
    private String sanitize(String data) {
        
        // FORCE LOWERCASE
        data = data.toLowerCase();
        
        // NUKE SPECIAL CHARACTERS & SHRINK MULTIPLE SPACES INTO ONE
        data = data.replaceAll("[^a-z\\såäöæøüßàèéì'çšžõ]", "").replaceAll("( +)", " ");
        
        return data;
    }
    
    // FIND LETTER OCCURRENCE
    private void find_letters(String data) {
        
        // NUKE SPACES & SPLIT INTO LETTERS
        String[] letters = data.replaceAll("\\s","").split("");

        // LOOP THROUGH EACH LETTER
        for (String letter : letters) {

            // EITHER PUSH NEW OR INCREMENT EXISTING VALUE BY ONE
            this.letters.merge(letter, 1.0, Double::sum);
        }
        
        // CONVERT TO PERCENT
        this.letters = percentify(this.letters, letters.length);
    }
    
    // FIND WINDOW OCCURRENCE
    private void find_windows(String data) {
        
        // NUKE SPACES & SPLIT INTO LETTERS
        String[] letters = data.replaceAll("\\s","").split("");

        // FIND WINDOW COUNT
        Integer window_count = letters.length - 2;
        
        // LOOP UNTIL X DOESNT HAVE TWO SUCCESSORS
        for (int x = 0; x < window_count; x++) {

            // STITCH TOGETHER THE WINDOW VALUE
            String window = letters[x] + letters[x + 1] + letters[x + 2];

            // EITHER PUSH NEW OR INCREMENT EXISTING VALUE BY ONE
            this.windows.merge(window, 1.0, Double::sum);
        }
        
        // CONVERT TO PERCENT
        this.windows = percentify(this.windows, window_count);
    }
    
    // FIND FIRST LETTER OCCURRENCE
    private void find_firsts(String data) {
        
        // SPLIT INTO WORDS
        String[] words = data.split(" ");

        // LOOP THROUGH EACH WORD
        for (String word : words) {
            
            // SKIP WHEN ZERO LENGTH
            if (word.length() > 0) {

                // FIRST LETTER OF THE WORD
                String first = word.substring(0, 1);

                // EITHER PUSH NEW OR INCREMENT EXISTING VALUE BY ONE
                this.firsts.merge(first, 1.0, Double::sum);
            }
        }
        
        // CONVERT TO PERCENT
        this.firsts = percentify(this.firsts, words.length);
    }
    
    // CONVERT OCCURRENCE TO PERCENT
    private HashMap<String, Double> percentify(HashMap<String, Double> map, Integer amount) {
        
        // LOOP THROUGH THE MAP
        for (String unit : map.keySet()) {
            
            // FIGURE OUT THE PERCENT VALUE
            Double percent = map.get(unit) / amount;
            
            // REPLACE THE OLD VALUE
            map.replace(unit, percent);
        }
        
        return map;
    }
    
    // GETTERS
    public String language() { return this.language; }
    public HashMap<String, Double> letters() { return this.letters; }
    public HashMap<String, Double> windows() { return this.windows; }
    public HashMap<String, Double> firsts() { return this.firsts; }
}
