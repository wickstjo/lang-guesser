package langguesser;
import java.util.HashMap;

public class Dataset {
    
    // LANGUAGE & RAW WORDS
    private final String language;
    private final String[] words;
    
    // HASHMAPS
    private HashMap<String, Integer> occurrences = new HashMap();
    private HashMap<String, Double> scores = new HashMap();
    
    // CONSTRUCTOR
    public Dataset(String _language, String[] _words) {
        
        // SET NAME & WORDS
        this.language = _language;
        this.words = _words;
    }
    
    // GETTERS
    public String language() { return this.language; }
    public String[] words() { return this.words; }
    public HashMap<String, Integer> occurrences() { return this.occurrences; }
    public HashMap<String, Double> scores() { return this.scores; }
    
    // SETTERS
    public void set_occurences(HashMap<String, Integer> map) { this.occurrences = map; }
    public void set_scores(HashMap<String, Double> map) { this.scores = map; }
}
