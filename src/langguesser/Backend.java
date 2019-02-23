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
    
    // HASHMAPS
    private final HashMap<String, String[]> words = new HashMap();
    private final HashMap<String, HashMap<String, Integer>> occurrences = new HashMap();
    private final HashMap<String, HashMap<String, Double>> scores = new HashMap();
    
    // CONSTRUCTOR
    public Backend() {
        
        // DECLARE & FILL LANGUAGE CONTAINER
        ArrayList<String> languages = new ArrayList();
        
        languages.add("swedish");
        languages.add("finnish");
        languages.add("norwegian");
        languages.add("italian");
        languages.add("french");
        languages.add("english");
        languages.add("estonian");
        languages.add("german");
        
        // FETCH & PROCESS THE DATASETS
        process(languages);
        
        // FIND WORD OCCURRENCE
        find_occurrences();
        
        // FIND SCORES
        find_scores();
    }
    
    // ANALYZE LANGUAGES
    private void process(ArrayList<String> languages) {
        
        // CONTAINER FOR ALL TEXT
        String all = "";
        
        // LOOP THROUGH EACH LANGUAGE
        for (String language : languages) {
            
            // LOAD FILE CONTENT & SANITIZE IT
            String content = load(language);
            content = sanitize(content);
            
            // CONCAT CONTENT TO ALL
            all += content;
            
            // SPLIT IT INTO WORDS
            String[] _words = content.split(" ");
            
            // PUSH TO HASHMAP
            this.words.put(language, _words);
        }
        
        // SPLIT ALL CONTENT INTO WORDS & PUSH IT TOO
        String[] all_words = all.split(" ");
        this.words.put("all", all_words);
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
    
    // SANITIZE DATA
    private String sanitize(String data) {
        
        // FORCE LOWERCASE
        data = data.toLowerCase();
        
        // NUKE SPECIAL CHARACTERS
        data = data.replaceAll("[^a-z\\såäöæøüßàèéì'çšžõ]", "");
        
        // SHRINK MULTIPLE SPACES INTO ONE
        data = data.replaceAll("( +)", " ");
        
        return data;
    }
    
    // FIND WORD OCCURRENCE FOR EACH DATASET
    private void find_occurrences() {

        // LOOP THROUGH EACH LANGUAGE
        for(String language : this.words.keySet()) {
            
            // CREATE AN EMPTY HASHMAP
            HashMap<String, Integer> template = new HashMap();
            
            // LOOP THROUGH EACH WORD
            for(String word : this.words.get(language)) {
                
                // INJECT IF UNDEFINED, OTHERWISE INCREMENT BY ONE
                template.merge(word, 1, Integer::sum);
            }
            
            // PUSH IT TO THE OCCURRENCE HASHMAP
            this.occurrences.put(language, template);
        }
    }
    
    // FIND WORD SCORES
    private void find_scores() {
        
        // FIND UNIQUE WORD COUNT ACROSS ALL LANGUAGE DATASETS
        Integer word_in_dictionary = this.occurrences.get("all").size();
        
        // LOOP THROUGH EACH LANGUAGE
        for(String language : this.occurrences.keySet()) {
            
            // CREATE AN EMPTY HASHMAP
            HashMap<String, Double> template = new HashMap();
            
            // FIND WORD COUNT IN LANGUAGE DATASET
            Integer words_in_language = this.words.get(language).length;
            
            // LOOP THROUGH EACH WORD
            for (String word : this.occurrences.get(language).keySet()) {
                
                // DEFAULT TO ZERO
                Integer word_occurrence = 0;
                
                // IF THE WORD EXISTS, FETCH IT & UPDATE ^
                if (this.occurrences.get(language).containsKey(word)) {
                    word_occurrence = this.occurrences.get(language).get(word);
                }
                
                // CALCULATE THE WORD SCORE -- FORCE DOUBLE CONVERSION
                double score = (double) (word_occurrence + 1) / (double) (words_in_language + word_in_dictionary);
                
                // ADD IT TO THE TEMPLATE HASHMAP
                template.put(word, score);
            }
            
            // PUSH TO THE SCORES HASHMAP
            this.scores.put(language, template);
        }
    }
    
    // ANALYZE QUERY
    public void query(String _query) {
        
        // FORCE LOWERCASE
        Dataset query = new Dataset(_query);
        
//        // LOOP THROUGH EACH LANGUAGE
//        for(String language : this.data.keySet()) {
//            
//            // CREATE NEW COMPARISON & PUSH IT TO SCORES
//            Comparison results = new Comparison(language, query, this.data.get(language));
//            scores.add(results);
//        }
//        
//        // SORT THE SCORES ARRAYLIST
//        Collections.sort(scores, new sorter());
//        
//        for(Comparison key : scores) {
//            System.out.println(key.language() + " => " + key.average());
//        }
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
