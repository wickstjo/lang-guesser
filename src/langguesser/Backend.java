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
    private final HashMap<String, ArrayList<Double>> results = new HashMap();
    
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
    public void query(String query) {
        
        // SANITIZE & SPLIT INTO WORDS
        query = sanitize(query);
        String[] _words = query.split(" ");
        
        // LOOP THROUGH THE WORDS
        for(String word : _words) {
            
            // CHECK IF THE WORD EXISTS IN ANY OF THE DATASETS
            boolean exists = this.occurrences.get("all").containsKey(word);
            
            // CONTINUE IF IT DOES, OTHERWISE SKIP
            if (exists == true) {
                
                // LOOP THROUGH ALL LANGUAGES
                for(String language : this.scores.keySet()) {
                    
                    // CHECK IF THE WORD EXISTS IN THE LANGUAGE
                    exists = this.scores.get(language).containsKey(word);
                    
                    // CONTINUE IF IT DOES & LANG ISNT ALL
                    if (exists == true && language != "all") {
                        
                        // FETCH THE VALUE
                        double value = this.scores.get(language).get(word);
                        
                        // IF THE LANGUAGE HASNT BEEN ADDED BEFORE, DO IT NOW
                        if (this.results.containsKey(language) == false) {
                            
                            // CREATE AN EMPTY ARRAYLIST & PUSH LANG
                            ArrayList<Double> empty = new ArrayList();
                            this.results.put(language, empty);
                        }
                        
                        // ADD IT TO THE RESULTS ARRAYLIST
                        this.results.get(language).add(value);
                    }
                }
            }
        }
        
        // CALCULATE THE PRIOR VALUE
        double prior = 1.0 / (this.occurrences.size() - 1);
        
        // DECLARE THE WINNERS ARRAYLIST
        ArrayList<Dataset> winners = new ArrayList();
        
        // LOOP THROUGH EACH LANGUAGE WITH HITS
        for (String language : this.results.keySet()) {
            
            // DEFAULT TO THE VALUE OF PRIOR
            double sum = prior;
            
            // MULTIPLY SUM BY THE KEY VALUE
            for (double value : this.results.get(language)) { sum *= value; }
            
            // ADD A NEW DATASET INSTANCE TO THE WINNERS ARRAYLIST
            winners.add(new Dataset(language, sum));
        }
        
        // SORT IN DESCENDING ORDER
        Collections.sort(winners, new sorter());
        
        // LOOP OUT RESULTS
        for (Dataset block : winners) {
            System.out.println(block.language() + " => " + block.score());
        }
    }
    
    // ARRAYLIST SORTER
    class sorter implements Comparator<Dataset> {

        // OVERRIDE THE DEFAULT COMPARE METHOD
        @Override public int compare(Dataset first, Dataset second) {
            
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
