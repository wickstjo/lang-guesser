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
    private final HashMap<String, Object> dictionary = new HashMap();
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
        
        // FIND WORD OCCURRENCE
        find_occurrences();
        
        // FIND SCORES
        find_scores();
    }
    
    // ANALYZE LANGUAGES
    private void process(ArrayList<String> languages) {
        
        // CONTAINER FOR ALL WORDS
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
            
            // CREATE A NEW DATASET INSTANCE & PUSH TO LANG HASHMAP
            Dataset dataset = new Dataset(language, _words);
            this.languages.put(language, dataset);
        }
        
        // SPLIT ALL INTO WORDS
        String[] all_words = all.split(" ");
        
        // LOOP THROUGH AND PUSH INTO DICTIONARY HASHMAP
        for (String word : all_words) { dictionary.put(word, null); }
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
        for(String language : this.languages.keySet()) {
            
            // CREATE AN EMPTY HASHMAP
            HashMap<String, Integer> template = new HashMap();
            
            // SHORTHAND TO DATASET
            Dataset dataset = languages.get(language);
            
            // LOOP THROUGH EACH WORD
            for(String word : dataset.words()) {
                
                // INJECT IF UNDEFINED, OTHERWISE INCREMENT BY ONE
                template.merge(word, 1, Integer::sum);
            }
            
            // SET THE DATASET OCCURRENCE HASHMAP
            dataset.set_occurences(template);
        }
    }
    
    // FIND WORD SCORES
    private void find_scores() {
        
        // FIND UNIQUE WORD COUNT ACROSS ALL LANGUAGES
        Integer word_in_dictionary = this.dictionary.size();
        
        // LOOP THROUGH EACH LANGUAGE
        for(String language : this.languages.keySet()) {
            
            // CREATE AN EMPTY HASHMAP
            HashMap<String, Double> template = new HashMap();
            
            // SHORTHAND TO DATASET
            Dataset dataset = languages.get(language);
            
            // FIND WORD COUNT IN LANGUAGE DATASET
            Integer words_in_language = dataset.words().length;
            
            // LOOP THROUGH EACH UNIQUE WORD
            for (String word : dataset.occurrences().keySet()) {
                
                // DEFAULT TO ZERO
                Integer word_occurrence = 0;
                
                // IF THE WORD EXISTS, OVERWRITE ^
                if (dataset.occurrences().containsKey(word)) {
                    word_occurrence = dataset.occurrences().get(word);
                }
                
                // CALCULATE THE WORD SCORE -- FORCE DOUBLE
                double score = (double) (word_occurrence + 1) / (double) (words_in_language + word_in_dictionary);
                
                // ADD IT TO THE TEMPLATE HASHMAP
                template.put(word, score);
            }
            
            // SET THE DATASET OCCURRENCE HASHMAP
            dataset.set_scores(template);
        }
    }
    
    // ANALYZE QUERY
    public void query(String query) {
        
        // SANITIZE & SPLIT IT INTO WORDS
        query = sanitize(query);
        String[] _words = query.split(" ");
        
        // HASHMAPS
        HashMap<String, ArrayList<Double>> results = new HashMap();
        ArrayList<Double> normalizers = new ArrayList();
        
        // LOOP THROUGH THE WORDS
        for(String word : _words) {
            
            // CHECK IF THE WORD EXISTS IN DICTIONARY
            boolean exists = this.dictionary.containsKey(word);
            
            // CONTINUE IF IT DOES, OTHERWISE SKIP
            if (exists == true) {
                
                double normalizer_sum = 1;
                
                // LOOP THROUGH ALL LANGUAGES
                for(String language : this.languages.keySet()) {
                    
                    // SHORTHAND TO DATASET
                    Dataset dataset = languages.get(language);
                    
                    // DEFAULT VALUE
                    double value = 1.0;
                    
                    // CHECK IF THE WORD EXISTS IN THE LANGUAGE
                    exists = dataset.scores().containsKey(word);
                    
                    // IF IT DOES, FETCH THE SCORE
                    if (exists == true) { value = dataset.scores().get(word); }

                    // IF THE LANGUAGE HASNT BEEN ADDED TO RESULTS BEFORE, DO IT NOW
                    if (results.containsKey(language) == false) {
                        results.put(language, new ArrayList());
                    }

                    // ADD IT TO THE RESULTS ARRAYLIST
                    results.get(language).add(value);
                    
                    normalizer_sum *= value;
                }
                
                normalizers.add(normalizer_sum);
            }
        }
        
        // CALCULATE THE PRIOR & NORMALIZER
        double prior = 1.0 / (this.dictionary.size() - 1);
        double normalizer = 1.0;
        
        // MULTIPLY THE NORMALIZER VALUES WITH EACH OTHER
        for (double row : normalizers) { normalizer *= row; }
        
        // DECLARE THE WINNERS ARRAYLIST
        ArrayList<Unit> winners = new ArrayList();
        
        // LOOP THROUGH EACH LANGUAGE WITH RESULTS
        for (String language : results.keySet()) {
            
            // DEFAULT TO THE VALUE OF PRIOR
            double sum = prior;
            
            // MULTIPLY SUM BY THE KEY VALUE
            for (double value : results.get(language)) { sum *= value; }
            
            // ADD NEW COMPARISON UNIT TO THE WINNERS LIST
            winners.add(new Unit(language, sum));
        }
        
        // SORT IT IN DESCENDING ORDER
        Collections.sort(winners, new sorter());
        
        // LOOP OUT RESULTS
        for (Unit block : winners) {
            System.out.println(block.language() + " => " + block.score() / normalizer);
        }
    }
    
    // ARRAYLIST SORTER
    class sorter implements Comparator<Unit> {

        // OVERRIDE THE DEFAULT COMPARE METHOD
        @Override public int compare(Unit first, Unit second) {
            
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
