package AnalizadorSintactico;

public class ManejadorError extends RuntimeException {
    private char[] mensajeFinal;

    public ManejadorError(int linea, int columna, char[] tipo, char[] mensaje, char[] textoLinea) {
        this.mensajeFinal = formatearMensaje(linea, columna, tipo, mensaje, textoLinea);
    }

    @Override
    public String getMessage() {
        return new String(mensajeFinal);
    }

    private static char[] formatearMensaje(int linea, int columna, char[] tipo, char[] mensaje, char[] textoLinea) {
        char[] prefijoError = {'E','R','R','O','R',' '};
        char[] prefijoLinea = {' ','L','i','n','e','a',' '};
        char[] charsLinea = intAChars(linea);
        char[] separador = {':',' '};
        char[] saltoLinea1 = {'\n',' ',' ',' ',' ','>',' '};
        char[] saltoLinea2 = {'\n',' ',' ',' ',' ',' ',' '};
        
        char[] espaciosColumna = new char[columna > 0 ? columna - 1 : 0];
        for (int i = 0; i < espaciosColumna.length; i++) {
            espaciosColumna[i] = ' ';
        }
        char[] indicador = {'^'};

        return concatenar(prefijoError, tipo, prefijoLinea, charsLinea, separador, mensaje, saltoLinea1, textoLinea, saltoLinea2, espaciosColumna, indicador);
    }

    private static char[] intAChars(int numero) {
        if (numero == 0) return new char[]{'0'};
        int temporal = numero;
        int digitos = 0;
        while (temporal > 0) {
            digitos++;
            temporal /= 10;
        }
        char[] resultado = new char[digitos];
        temporal = numero;
        for (int i = digitos - 1; i >= 0; i--) {
            resultado[i] = (char) ((temporal % 10) + '0');
            temporal /= 10;
        }
        return resultado;
    }

    private static char[] concatenar(char[]... partes) {
        int tamano = 0;
        for (int i = 0; i < partes.length; i++) {
            if (partes[i] != null) tamano += partes[i].length;
        }
        char[] resultado = new char[tamano];
        int indice = 0;
        for (int i = 0; i < partes.length; i++) {
            if (partes[i] != null) {
                for (int j = 0; j < partes[i].length; j++) {
                    resultado[indice++] = partes[i][j];
                }
            }
        }
        return resultado;
    }
}