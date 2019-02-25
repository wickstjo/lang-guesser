package langguesser;

public class LangGuesser {
    public static void main(String[] args) {
        
        // FETCH THE NECESSARY COMPONENTS
        Backend backend = new Backend();
        UI ui = new UI(backend);
        
        // PERFORM QUERY
        ui.query();
    }
}