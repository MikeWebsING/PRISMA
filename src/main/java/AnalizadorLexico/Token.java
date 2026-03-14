package AnalizadorLexico;

public class Token {
    public final int etiqueta;

    public Token(int t) {
        etiqueta = t;
    }

    public String toString() {
        if (etiqueta < 256)
            return "" + (char) etiqueta;
        return "Token[" + etiqueta + "]";
    }
}
