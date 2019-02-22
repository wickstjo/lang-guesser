package langguesser;

public class LangGuesser {
    public static void main(String[] args) {
        
        Backend backend = new Backend();
        
        String data = "New York City borough of Queens and received an economics degree from";
        backend.query(data);
    }
}
