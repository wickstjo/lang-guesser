package langguesser;
import java.io.BufferedReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;

public class Backend {

    // LANGUAGE DATASETS
    private final HashMap<String, Dataset> languages = new HashMap();
    
    // CONSTRUCTOR
    public Backend() {
        
        // LIST OF LANGUAGES
        ArrayList<String> init = new ArrayList();
            init.add("swedish");
            init.add("finnish");
            init.add("norwegian");
            init.add("italian");
            init.add("french");
            init.add("english");
            init.add("estonian");
            init.add("german");
        
        // FETCH & PROCESS THE DATASETS
        process(init);
    }
    
    // PROCESS THE LANGUAGES
    private void process(ArrayList<String> languages) {
        
        // LOOP THROUGH EACH LANGUAGE
        for (String language : languages) {
            
            // LOAD FILE CONTENT
            String content = load(language);
            
            // CREATE A NEW DATASET & PUSH IT
            Dataset dataset = new Dataset(language, content);
            this.languages.put(language, dataset);
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
    
    // FIND QUERY LANGUAGE
    public void query(String data) {
        
        // CREATE NEW DATASET
        Dataset query = new Dataset("query", data);
        
        // DO THE COMPUTATION HERE
        System.out.println("QUERY OK!");
    }
}
