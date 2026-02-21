package AnalizadorLexico;

public class Numero extends Token {
    public final int valor;

    public Numero(int v) {
        super(Etiqueta.NUM_INT);
        valor = v;
    }

    public String toString() {
        return "" + valor;
    }
}
