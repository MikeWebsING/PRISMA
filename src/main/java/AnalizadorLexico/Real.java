package AnalizadorLexico;

public class Real extends SimboloLexico {
    public final float valor;

    public Real(float v, int linea, int columna) {
        super(Etiqueta.NUMERO_DECIMAL, linea, columna);
        valor = v;
    }

    public String toString() {
        return "" + valor;
    }
}
