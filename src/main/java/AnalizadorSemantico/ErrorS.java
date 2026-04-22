package AnalizadorSemantico;

public class ErrorS {
    public int linea;
    public int columna;
    public String clave;
    public String mensaje;

    public ErrorS(int numLinea, int numColumna, String claveError, String textoMensaje) {
        this.linea = numLinea;
        this.columna = numColumna;
        this.clave = claveError;
        this.mensaje = textoMensaje;
    }
}
