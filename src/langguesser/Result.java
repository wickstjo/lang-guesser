package langguesser;

public class Result {
    
    // LANGUAGE NAME
    private final String language;
    
    // SUBSET SCORES
    private Double letter = 0.0;
    private Double window = 0.0;
    private Double first = 0.0;
    
    // FINAL SCORE
    private Double score = 0.0;
    
    // CONSTRUCTOR
    public Result(String _language) { this.language = _language; }
    
    // GETTERS
    public String language() { return this.language; }
    public Double letter() { return this.letter; }
    public Double window() { return this.window; }
    public Double first() { return this.first; }
    public Double score() { return this.score; }
    
    // ADD TO SUBSET SCORES
    public void add_letter(double value) { this.letter += value; }
    public void add_window(double value) { this.window += value; }
    public void add_first(double value) { this.first += value; }
    
    // SET FINAL SCORE
    public void set_score(double value) { this.score = value; }
}
