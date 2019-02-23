package langguesser;
import java.io.BufferedReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

public class Backend {
    
    // ANALYZED DATASET & TEMPLATE HASHMAPS
    private final HashMap<String, Dataset> data = new HashMap();
    private final ArrayList<Comparison> scores = new ArrayList();
    
    // CONSTRUCTOR
    public Backend() {
        
        // DECLARE LANGUAGE ARRAYLIST
        ArrayList<String> languages = new ArrayList();
        
        // FILL IT
        languages.add("swedish");
        languages.add("finnish");
        languages.add("norwegian");
        languages.add("italian");
        languages.add("french");
        languages.add("english");
        languages.add("estonian");
        languages.add("german");
        
        // ANALYZE ALL THE LANGUAGES
        analyze_all(languages);
    }
    
    // ANALYZE LANGUAGES
    private void analyze_all(ArrayList<String> languages) {
        
        // LOOP THROUGH EACH LANGUAGE
        for (String language : languages) {
            
            // LOAD FILE CONTENT & ANALYZE IT
            String content = load(language);
            Dataset dataset = new Dataset(content);
            
            // PUSH THE INSTANCE RESULTS TO THE DATA HASHMAP
            data.put(language, dataset);
        }
    }
    
    // LOAD FILE CONTENT
    private String load(String filename) {
        
        // GENERATE A PATH
        Path path = Paths.get("dataset/" + filename + ".txt");
        
        // CONTENT CONTAINER
        String content = "";
        
        // ATTEMPT OPENING THE FILE -- FORCE ISO_8859_1
        try (BufferedReader reader = Files.newBufferedReader(path, StandardCharsets.ISO_8859_1)) {
            
            // PLACEHOLDER
            String line;
            
            // WHILE THERE ARE LINES TO READ, PUSH THEM TO THE CONTAINER
            while ((line = reader.readLine()) != null) { content += line; }
            
        // ON ERROR, LOG IT
        } catch (Exception ex) { System.out.println(ex); }
        
        return content;
    }
    
    // ANALYZE QUERY
    public void query(String _query) {
        
        // FORCE LOWERCASE
        Dataset query = new Dataset(_query);
        
        // LOOP THROUGH EACH LANGUAGE
        for(String language : this.data.keySet()) {
            
            // CREATE NEW COMPARISON & PUSH IT TO SCORES
            Comparison results = new Comparison(language, query, this.data.get(language));
            scores.add(results);
        }
        
        // SORT THE SCORES ARRAYLIST
        Collections.sort(scores, new sorter());
        
        for(Comparison key : scores) {
            System.out.println(key.language() + " => " + key.average());
        }
    }
    
    // ARRAYLIST SORTER
    class sorter implements Comparator<Comparison> {

        // OVERRIDE THE DEFAULT COMPARE METHOD
        @Override public int compare(Comparison first, Comparison second) {
            
            // DEFAULT TO NOT MOVING
            Integer response = 0;
            
            // MOVE ELEMENT FORWARD
            if (first.average() > second.average()) {
                response = 1;
            
            // MOVE ELEMENT BACKWARD
            } else if (first.average() < second.average()) {
                response = -1;
            }
            
            return response;
        }
    }
}
