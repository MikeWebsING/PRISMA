package AnalizadorSintactico;

public class ManejadorError extends RuntimeException {
    public ManejadorError(int linea, String mensaje) {
        super("Error en linea " + linea + ": " + mensaje);
    }
}