package langguesser;

public class Unit {
    
    // LANGUAGE
    private final String language;
    private final double score;
    
    // CONSTRUCTOR
    public Unit(String _language, double _score) {
        
        // SET NAME
        this.language = _language;
        this.score = _score;
    }
    
    // GETTERS
    public String language() { return this.language; }
    public double score() { return this.score; }
}
