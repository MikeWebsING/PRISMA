package AnalizadorLexico;

import java.io.*;

public class PruebaPrisma {
    public static void main(String[] args) {
        String nombreArchivo = "entrada.txt";
        File archivo = new File(nombreArchivo);

        if (!archivo.exists()) {
            return;
        }

        try {
            InputStream originalIn = System.in;
            System.setIn(new FileInputStream(archivo));

            AnalizadorLexico lexer = new AnalizadorLexico();

            while (true) {
                Token t = lexer.escanear();
                if (t.etiqueta == Etiqueta.EOF)
                    break;

                if (t.etiqueta == Etiqueta.ERROR) {
                    System.out.println("Error!! Token no Valido " + lexer.linea);
                }
            }

            System.setIn(originalIn);

        } catch (Exception e) {
        }
    }
}
