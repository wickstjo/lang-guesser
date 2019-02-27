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

    // PERMANENT DATASET HASHMAP FOR LANGUAGES
    private final HashMap<String, Dataset> languages = new HashMap();
    
    // RENEWABLE QUERY RELATED HASHMAPS
    private final HashMap<String, Result> results = new HashMap();
    private final ArrayList<Result> response = new ArrayList();;
    
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
            
            // CREATE A NEW ENTRY IN LANGUAGES
            Dataset dataset = new Dataset(language, content);
            this.languages.put(language, dataset);
            
            // CREATE A NEW ENTRY IN RESULTS
            results.put(language, new Result(language));
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
    public ArrayList<Result> query(String data) {
        
        // CREATE DATASET FROM QUERY
        Dataset query = new Dataset("query", data);
        
        // COMPARE SUBSETS
        compare_letters(query);
        compare_windows(query);
        compare_firsts(query);
        find_scores();
        
        // FILL THE RESPONSE ARRAYLIST WITH COMPARISON RESULTS
        for (String language : this.results.keySet()) {
            this.response.add(this.results.get(language));
        }
        
        // SORT IT IN DESCENDING ORDER & RETURN
        Collections.sort(this.response, new sorter());
        return this.response;
    }
    
    // COMPARE QUERY LETTERS
    private void compare_letters(Dataset query) {
        
        // LOOP THROUGH EACH QUERY LETTER
        for (String letter : query.letters().keySet()) {
            
            // LOOP THROUGH EACH LANGUAGE
            for (String language : this.languages.keySet()) {
                
                // SHORTHAND
                Dataset dataset = this.languages.get(language);

                // DEFAULT VALUE
                double lang_value = 0;
                
                // IF THE LETTER EXISTS IN THE LANGUAGE, FETCH IT
                if (dataset.letters().containsKey(letter)) {
                    lang_value = dataset.letters().get(letter);
                }
                
                // CALCULATE THE ABSOLUTE DIFFERENCE
                double difference = Math.abs(lang_value - query.letters().get(letter));
                
                // ADD IT TO THE RESULT ARRAYLIST
                results.get(language).add_letter(difference);
            }
        }
    }
    
    // COMPARE QUERY LETTERS
    private void compare_windows(Dataset query) {
        
        // LOOP THROUGH EACH QUERY LETTER
        for (String window : query.windows().keySet()) {
            
            // LOOP THROUGH EACH LANGUAGE
            for (String language : this.languages.keySet()) {
                
                // SHORTHAND
                Dataset dataset = this.languages.get(language);

                // DEFAULT VALUE
                double lang_value = 0;
                
                // IF THE LETTER EXISTS IN THE LANGUAGE, FETCH IT
                if (dataset.windows().containsKey(window)) {
                    lang_value = dataset.windows().get(window);
                }
                
                // CALCULATE THE ABSOLUTE DIFFERENCE
                double difference = Math.abs(lang_value - query.windows().get(window));
                
                // ADD IT TO THE RESULT ARRAYLIST
                results.get(language).add_window(difference);
            }
        }
    }
    
    // COMPARE QUERY LETTERS
    private void compare_firsts(Dataset query) {
        
        // LOOP THROUGH EACH QUERY LETTER
        for (String first : query.firsts().keySet()) {
        
            // LOOP THROUGH EACH LANGUAGE
            for (String language : this.languages.keySet()) {
                
                // SHORTHAND
                Dataset dataset = this.languages.get(language);

                // DEFAULT VALUE
                double lang_value = 0;
                
                // IF THE LETTER EXISTS IN THE LANGUAGE, FETCH IT
                if (dataset.firsts().containsKey(first)) {
                    lang_value = dataset.firsts().get(first);
                }
                
                // CALCULATE THE ABSOLUTE DIFFERENCE
                double difference = Math.abs(lang_value - query.firsts().get(first));
                
                // ADD IT TO THE RESULT ARRAYLIST
                results.get(language).add_first(difference);
            }
        }
    }

    // FIND FINALIZED SCORES
    private void find_scores() {
        
        // LOOP THROUGH EACH QUERY LETTER
        for (String language : this.results.keySet()) {
            
            // SHORTHAND
            Result result = this.results.get(language);
            
            // FIND THE AVERAGE VALUE & SET IT
            double average = (result.letter() + result.window() + result.first()) / 3.0;
            result.set_score(average);
        }
    }
    
    // ARRAYLIST SORTER
    class sorter implements Comparator<Result> {

        // OVERRIDE THE DEFAULT COMPARE METHOD
        @Override public int compare(Result first, Result second) {
            
            // DEFAULT TO NOT MOVING
            Integer response = 0;
            
            // MOVE ELEMENT FORWARD
            if (first.score() > second.score()) {
                response = 1;
            
            // MOVE ELEMENT BACKWARD
            } else if (first.score() < second.score()) {
                response = -1;
            }
            
            return response;
        }
    }
}
