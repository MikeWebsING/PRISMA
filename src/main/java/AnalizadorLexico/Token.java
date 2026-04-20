package AnalizadorLexico;

public class Token {
    public final int etiqueta;
    public int linea;
    public int columna;

    public Token(int t) {
        etiqueta = t;
    }

    public Token(int t, int linea, int columna) {
        this.etiqueta = t;
        this.linea = linea;
        this.columna = columna;
    }

    public String toString() {
        if (etiqueta < 256)
            return "" + (char) etiqueta;
        return "Token[" + etiqueta + "]";
    }
}
