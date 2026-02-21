package AnalizadorLexico;

public class Palabra extends Token {
    public String lexema = "";

    public Palabra(String s, int etiqueta) {
        super(etiqueta);
        lexema = s;
    }

    public String toString() {
        return lexema;
    }

    public static final Palabra and = new Palabra("Y", Etiqueta.Y),
            or = new Palabra("O", Etiqueta.O),
            eq = new Palabra("==", Etiqueta.IGUALDAD),
            ne = new Palabra("!=", Etiqueta.DIFERENTE),
            le = new Palabra("<=", Etiqueta.MENOR_IGUAL),
            ge = new Palabra(">=", Etiqueta.MAYOR_IGUAL);
}