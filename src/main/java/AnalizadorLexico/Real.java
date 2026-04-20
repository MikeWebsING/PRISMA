package AnalizadorLexico;

public class Real extends Token {
    public final float valor;

    public Real(float v) {
        super(Etiqueta.NUM_DEC);
        valor = v;
    }

    public Real(float v, int linea, int columna) {
        super(Etiqueta.NUM_DEC, linea, columna);
        valor = v;
    }

    public String toString() {
        return "" + valor;
    }
}
