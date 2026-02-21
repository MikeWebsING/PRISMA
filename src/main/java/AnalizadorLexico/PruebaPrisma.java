package AnalizadorLexico;

import java.io.*;
import java.util.*;

public class PruebaPrisma {
    public static void main(String[] args) {
        String nombreArchivo = "entrada.txt";
        File archivo = new File(nombreArchivo);

        if (!archivo.exists()) {
            System.err.println("Error: No se encontro el archivo '" + nombreArchivo + "' en la raiz del proyecto.");
            return;
        }

        try {
            // Redirigir System.in para leer desde el archivo
            InputStream originalIn = System.in;
            System.setIn(new FileInputStream(archivo));

            AnalizadorLexico lexer = new AnalizadorLexico();
            List<String> resultados = new ArrayList<>();

            System.out.println("--- Analizando archivo: " + nombreArchivo + " ---");

            while (true) {
                Token t = lexer.escanear();
                if (t.etiqueta == Etiqueta.EOF)
                    break;

                String salida;
                if (t.etiqueta < 256) {
                    salida = "Linea " + lexer.linea + " | Token: " + (char) t.etiqueta + " (Simbolo)";
                } else {
                    salida = "Linea " + lexer.linea + " | Token: " + t.toString() + " (Etiqueta: " + t.etiqueta + ")";
                }
                resultados.add(salida);
            }

            // Restaurar System.in (opcional pero buena practica)
            System.setIn(originalIn);

            System.out.println("\n--- RESULTADOS DEL ANALISIS ---");
            for (String res : resultados) {
                System.out.println(res);
            }
            System.out.println("\nAnalisis finalizado exitosamente.");
            System.out.println("Total de tokens encontrados: " + resultados.size());

        } catch (FileNotFoundException e) {
            System.err.println("Error: Archivo no encontrado.");
        } catch (IOException e) {
            System.err.println("Error de lectura: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Error critico: " + e.getMessage());
        }
    }
}