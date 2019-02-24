package langguesser;
import java.util.HashMap;

public class Dataset {
    
    // LANGUAGE NAME
    private final String language;
    
    // OCCURRENCE HASHMAPS
    private final HashMap<String, Integer> letters = new HashMap();
    private final HashMap<String, Integer> windows = new HashMap();
    private final HashMap<String, Integer> firsts = new HashMap();
    
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
            this.letters.merge(letter, 1, Integer::sum);
        }
    }
    
    // FIND WINDOW OCCURRENCE
    private void find_windows(String data) {
        
        // NUKE SPACES & SPLIT INTO LETTERS
        String[] letters = data.replaceAll("\\s","").split("");

        // LOOP UNTIL X DOESNT HAVE TWO SUCCESSORS
        for (int x = 0; x < letters.length - 2; x++) {

            // STITCH TOGETHER THE WINDOW VALUE
            String window = letters[x] + letters[x + 1] + letters[x + 2];

            // EITHER PUSH NEW OR INCREMENT EXISTING VALUE BY ONE
            this.windows.merge(window, 1, Integer::sum);
        }
    }
    
    // FIND FIRST LETTER OCCURRENCE
    private void find_firsts(String data) {
        
        // SPLIT INTO WORDS
        String[] words = data.split(" ");

        // LOOP THROUGH EACH WORD
        for (String word : words) {

            // FIRST LETTER OF THE WORD
            String first = word.substring(0, 1);

            // EITHER PUSH NEW OR INCREMENT EXISTING VALUE BY ONE
            this.firsts.merge(first, 1, Integer::sum);
        }
    }
    
    // GETTERS
    public String language() { return this.language; }
    public HashMap<String, Integer> letters() { return this.letters; }
    public HashMap<String, Integer> windows() { return this.windows; }
    public HashMap<String, Integer> firsts() { return this.firsts; }
}
