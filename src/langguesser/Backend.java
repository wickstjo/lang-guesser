package langguesser;
import java.io.BufferedReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;

public class Backend {
    
    // ANALYZED LANGUAGES
    private HashMap<String, Analysis> data = new HashMap();
    
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
            Analysis result = new Analysis(content);
            
            // PUSH THE RESULT TO THE HASHMAP
            data.put(language, result);
        }
    }
    
    // LOAD FILE CONTENT
    private String load(String filename) {
        
        // GENERATE A PATH
        Path path = Paths.get("samples/" + filename + ".txt");
        
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
    public void query(String _data) {
        
        // ANALYZE IT & CLONE A TEMPLATE
        Analysis query = new Analysis(_data);
        
//        HashMap<String, Analysis> template = this.data;
//        
//        // LOOP THROUGH EACH ENTRY
//        for (String key : template.keySet()) {
//            
//        }

        // SOMETHING
    }
    
    private void log(Object content) { System.out.println(content); }
    
    private void loop(HashMap<String, Double> map) {
        double sum = 0;
        for (String key : map.keySet()) {
            log(key + " => " + map.get(key));
            sum += map.get(key);
        }
        
        log(sum);
    }
}
