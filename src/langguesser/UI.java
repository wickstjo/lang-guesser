    package langguesser;
import java.math.BigDecimal;
import java.math.RoundingMode;
    import java.util.ArrayList;
    import java.util.Scanner;

    public class UI {

        // BACKEND MODULE
    private final Backend backend;
    
    // FETCH THE SCANNER MODULE
    private final Scanner scan = new Scanner(System.in);
    
    // CONSTRUCTOR
    public UI (Backend _backend) { this.backend = _backend; }
    
    // SHORTHANDS FOR LOGGING
    public void log(Object content) { System.out.println(content); }
    public void error(Object content) { log("\n" + "\u001B[31m" + content + "\u001B[0m" + "\n"); }
    
    // ASK FOR USER INPUT
    public void query() {
        
        // ASK THE USER FOR TEXT
        String data = question("TYPE IN TEXT:");
      
        // COMPUTE THE QUERY & FETCH THE RESULTS
        ArrayList<Result> results = backend.query(data);
        
        // SHOW THE RESULTS -- ROUND TO 5 DECIMALS
        for (Result result : results) {
            log(result.language() + " => " + round(result.score(), 5));
            log("letters => " + result.letter());
            log("window => " + result.window());
            log("first => " + result.first() + "\n");
        }
    }
    
    // ASK A QUESTION
    private String question(String _question) {
        
        // ASK THE QUESTION & SAVE THE ANSWER
        System.out.print(_question + "\n\u00A0\u00A0> ");
        String answer = scan.next();
        System.out.println("");
        
        // OTHERWISE, RETURN THE ANSWER
        return answer;
    }
    
    // https://stackoverflow.com/questions/2808535/round-a-double-to-2-decimal-places
    private static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }
}
