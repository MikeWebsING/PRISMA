package AnalizadorSintactico;

public class ManejadorError extends RuntimeException {
    public ManejadorError(int linea, int columna, String tipo, String mensaje, String textoLinea) {
        super(formatearMensaje(linea, columna, tipo, mensaje, textoLinea));
    }

    private static String formatearMensaje(int linea, int columna, String tipo, String mensaje, String textoLinea) {
        String resultado = "ERROR " + tipo + " Linea " + linea + ": " + mensaje + "\n    > " + textoLinea + "\n      ";
        for (int i = 1; i < columna; i++) {
            resultado = resultado + " ";
        }
        resultado = resultado + "^";
        return resultado;
    }
}