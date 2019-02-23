package langguesser;

public class Comparison {
    
    // LANGUAGE
    private final String language;
    
    // SCORES
    private final double letters;
    private final double windows;
    private final double firsts;
    private final double average;
    
    // CONSTRUCTOR
    public Comparison(String language, Dataset first, Dataset second) {
        
        // SET NAME
        this.language = language;
        
        // SET SCORES
        this.letters = first.letter_score() - second.letter_score();
        this.windows = first.window_score() - second.window_score();
        this.firsts = first.first_score() - second.first_score();
        
        // SET AVERAGE
        this.average = (this.letters + this.windows + this.firsts) / 3;
    }
    
    // GETTERS
    public double letter_score() { return this.letters; }
    public double window_score() { return this.windows; }
    public double first_score() { return this.firsts; }
    public double average() { return this.average; }
    public String language() { return this.language; }
}
