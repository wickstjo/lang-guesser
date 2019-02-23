package langguesser;

public class LangGuesser {
    public static void main(String[] args) {
        
        Backend backend = new Backend();
        
        String data  = "prima viene scoperto dalla polizia locale";
        backend.query(data);
    }
}