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

    // ASK FOR USER INPUT
    public void query() {
        
        // ASK THE QUESTION
        log("TEXT TO ANALYZE:");
        String data = scan.nextLine();
        
        // COMPUTE THE QUERY & FETCH THE RESULTS
        ArrayList<Result> results = backend.query(data);
        
        // PRESENT RESULTS
        summary(results);
    }
    
    // PRESENT RESULTS
    private void summary(ArrayList<Result> results) {
        
        // FORMAT & INDEX COUNTER
        String format = "%1s%15s%10s%10s%10s%10s";
        int index = 1;
        
        // HEADERS
        log("");
        System.out.format(format, "#", "LANGUAGE", "LETTER", "WINDOW", "FIRST", "SCORE");
        log("");
        
        // SHOW THE RESULTS -- ROUND TO 5 DECIMALS
        for (Result result : results) {
            
            System.out.format(format, index, result.language(), round(result.letter(), 5), round(result.window(), 5), round(result.first(), 5), round(result.score(), 5));
            System.out.println();
            
            // INCREMENT INDEX
            index++;
        }
    }
    
    // https://stackoverflow.com/questions/2808535/round-a-double-to-2-decimal-places
    private static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }
}
