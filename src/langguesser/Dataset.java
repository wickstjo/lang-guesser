package langguesser;

public class Dataset {
    
    // LANGUAGE
    private final String language;
    private final double score;
    
    // CONSTRUCTOR
    public Dataset(String _language, double _score) {
        
        // SET NAME
        this.language = _language;
        this.score = _score;
    }
    
    // GETTERS
    public double score() { return this.score; }
    public String language() { return this.language; }
}
