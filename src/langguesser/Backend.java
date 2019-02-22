package langguesser;
import java.io.BufferedReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;

public class Backend {
    
    // ANALYZED DATASET & TEMPLATE HASHMAPS
    private final HashMap<String, Dataset> data = new HashMap();
    private final HashMap<String, Double> template = new HashMap();
    
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
            
            // PUSH LANGUAGE TO TEMPLATE HASHMAP
            template.put(language, 1.0);
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
        } catch (Exception ex) { log(ex); }
        
        return content;
    }
    
    // ANALYZE QUERY
    public void query(String _query) {
        
        // FORCE LOWERCASE
        _query = _query.toLowerCase();
        
        // CLONE A TEMPLATE HASHMAP
        HashMap<String, Double> temp = this.template;
        
        // DECLARE NORMALIZER
        double normalizer = 1;
        
        String query = _query;
        
        // NUKE SPACES & SPLIT INTO LETTERS
        query = query.replaceAll("\\s+","");
        String[] letters = query.split("");

        // LOOP THROUGH THE LETTERS
        for (String letter : letters) {
            
            double norm = 1;
            
            for (String language : this.data.keySet()) {
                
                // FETCH LETTER VALUE
                double value = this.data.get(language).get_letters().get(letter);
                
                norm *= value;
                
                double new_value = temp.get(language) * value;
                temp.replace(language, new_value);
            }
            
            log(norm);
            normalizer *= norm;
            log(normalizer);
        }
        
        for (String key : temp.keySet()) {
            System.out.println(normalizer);
            System.out.println(key + " => " + temp.get(key) * normalizer);
        }
    }
    
    private void log(Object content) { System.out.println(content); }
    private void loop(HashMap<String, Double> map) {
        for (String key : map.keySet()) {
            log(key + " => " + map.get(key));
        }
    }
}
