import AnalizadorLexico.*;
import AnalizadorSintactico.*;
import java.io.*;

public class PruebaPrisma {
    public static void main(String[] argumentos) {
        String rutaArchivo = "entrada.txt";
        File archivoEntrada = new File(rutaArchivo);

        if (!archivoEntrada.exists()) {
            return;
        }

        try {
            InputStream entradaOriginal = System.in;
            System.setIn(new FileInputStream(archivoEntrada));

            AnalizadorLexico lexico = new AnalizadorLexico();

            while (true) {
                SimboloLexico simboloActual = lexico.obtenerSiguienteToken();
                if (simboloActual.etiqueta == Etiqueta.FIN_ARCHIVO) {
                    break;
                }

                if (simboloActual.etiqueta == Etiqueta.ERROR) {
                    System.out.println("Error: Simbolo no valido en linea " + lexico.linea);
                } else {
                    System.out.println("Simbolo encontrado: " + Etiqueta.obtenerNombre(simboloActual.etiqueta));
                }
            }

            System.setIn(entradaOriginal);
        } catch (Exception excepcion) {
        }
    }
}
