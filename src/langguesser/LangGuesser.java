package langguesser;

public class LangGuesser {
    public static void main(String[] args) {
        
        Backend backend = new Backend();
        
        String data = "Nach seiner Rückkehr in die USA wurde Hempl 1889 Assistant Professor of English an der University of Michigan. 1893 wurde er zum Junior Professor ernannt und 1897 zum Professor of English and General Linguistics";
        backend.query(data);
    }
}