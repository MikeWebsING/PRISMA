package AnalizadorLexico;

public class SimboloLexico {
    public int etiqueta;
    public int linea;
    public int columna;

    public SimboloLexico(int etiqueta) {
        this.etiqueta = etiqueta;
    }

    public SimboloLexico(int etiqueta, int linea, int columna) {
        this.etiqueta = etiqueta;
        this.linea = linea;
        this.columna = columna;
    }

    public int getEtiqueta() {
        return this.etiqueta;
    }

    public void setEtiqueta(int etiqueta) {
        this.etiqueta = etiqueta;
    }

    public int getLinea() {
        return this.linea;
    }

    public void setLinea(int linea) {
        this.linea = linea;
    }

    public int getColumna() {
        return this.columna;
    }

    public void setColumna(int columna) {
        this.columna = columna;
    }
}
