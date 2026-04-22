package AnalizadorLexico;

public class SimboloLexico {
    public final int etiqueta;
    public int linea;
    public int columna;

    public SimboloLexico(int tipo) {
        etiqueta = tipo;
    }

    public SimboloLexico(int tipo, int numLinea, int numColumna) {
        this.etiqueta = tipo;
        this.linea = numLinea;
        this.columna = numColumna;
    }

    public String toString() {
        if (etiqueta < 256) {
            return "" + (char) etiqueta;
        }
        return "SimboloLexico[" + etiqueta + "]";
    }
}
