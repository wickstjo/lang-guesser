package langguesser;
import java.util.ArrayList;

public class Result {
    
    // LANGUAGE NAME
    private final String language;
    
    // ARRAYLISTS
    private ArrayList<Double> letters = new ArrayList();
    private ArrayList<Double> windows = new ArrayList();
    private ArrayList<Double> firsts = new ArrayList();
    
    // CONSTRUCTOR
    public Result(String _language) { this.language = _language; }
    
    
    
    // GETTERS
    public ArrayList<Double> letters() { return this.letters; }
    public ArrayList<Double> windows() { return this.windows; }
    public ArrayList<Double> firsts() { return this.firsts; }
}
