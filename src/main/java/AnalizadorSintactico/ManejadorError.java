package AnalizadorSintactico;

public class ManejadorError extends RuntimeException {
    public ManejadorError(int linea, int columna, String tipo, String mensaje, String textoLinea) {
        super(formatearMensaje(linea, columna, tipo, mensaje, textoLinea));
    }

    private static String formatearMensaje(int linea, int columna, String tipo, String mensaje, String textoLinea) {
        StringBuilder sb = new StringBuilder();
        sb.append("ERROR ").append(tipo).append(" Linea ").append(linea).append(": ").append(mensaje);
        sb.append("\n    > ").append(textoLinea);
        sb.append("\n      ");
        for (int i = 1; i < columna; i++) {
            sb.append(" ");
        }
        sb.append("^");
        return sb.toString();
    }
}