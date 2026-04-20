package AnalizadorLexico;

public class Numero extends Token {
    public final int valor;

    public Numero(int v) {
        super(Etiqueta.NUM_INT);
        valor = v;
    }

    public Numero(int v, int linea, int columna) {
        super(Etiqueta.NUM_INT, linea, columna);
        valor = v;
    }

    public String toString() {
        return "" + valor;
    }
}
