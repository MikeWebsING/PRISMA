package AnalizadorLexico;

public class Numero extends SimboloLexico {
    public final int valor;

    public Numero(int v, int linea, int columna) {
        super(Etiqueta.NUMERO_ENTERO, linea, columna);
        valor = v;
    }

    public String toString() {
        return "" + valor;
    }
}
