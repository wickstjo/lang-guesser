package langguesser;

public class LangGuesser {
    public static void main(String[] args) {
        
        // FETCH THE BACKEND MODULE
        Backend backend = new Backend();
        
        // PERFORM QUERY
        String data  = "Lavorando allo stesso tempo nell'azienda paterna, la Elizabeth Trump & Son, di cui divenne socio dopo essersi laureato come baccelliere in economia nel 1968; tre anni più tardi rilevò in prima persona la gestione della compagnia, ribattezzandola Trump Organization. Durante la sua carriera";
        backend.query(data);
    }
}