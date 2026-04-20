package compilador;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

class AnalizadorLexico {
    
    static String[] reservadas = {
            "INICIO", "FIN", "INI_DECL", "FIN_DECL",
            "CAPTURAR", "MOSTRAR",
            "SI", "ENTONCES", "FIN_SI",
            "SINO", "SINO_FIN",
            "PARA", "INICIA_EN", "TERMINA_EN", "HACER", "FIN_PARA",
            "MIENTRAS", "REPETIR",
            "ENT", "FLOT", "CAD",
            "AND", "OR"
    };

    public void analizar(String nombreArchivo) {

        try {

            File archivo = new File(nombreArchivo);
            Scanner leer = new Scanner(archivo);

            int linea = 1;

            while (leer.hasNextLine()) {

                String texto = leer.nextLine();

                while (texto.contains("\"")) {

                    int inicio = texto.indexOf("\"");
                    int fin = texto.indexOf("\"", inicio + 1);

                    if (fin != -1) {
                        String cadena = texto.substring(inicio, fin + 1);
                        texto = texto.replace(cadena, " ");
                    } else {
                        System.out.println("Error: comillas sin cerrar en linea " + linea);
                        break;
                    }
                }

                for (int i = 0; i < texto.length(); i++) {

                    char c = texto.charAt(i);

                    if (!Character.isLetterOrDigit(c) &&
                            "+-*/\\^><=():; _".indexOf(c) == -1 &&
                            !Character.isWhitespace(c)) {

                        System.out.println("Caracter no valido '" + c + "' en linea " + linea);
                    }
                }

                String[] tokens = texto.split("(?=[()+\\-*/\\\\^><=;:])|\\s+|(?<=[()+\\-*/\\\\^><=;:])");

                for (String token : tokens) {

                    if (token.isEmpty())
                        continue;

                    analizarToken(token, linea);
                }

                linea++;
            }

            leer.close();

        } catch (FileNotFoundException e) {

            System.out.println("Archivo no encontrado");

        }
    }

    public void analizarToken(String token, int linea) {

        for (String r : reservadas) {
            if (token.equals(r)) {
                return;
            }
        }

        if (token.matches("[0-9]+")) {
            return;
        }

        if (token.matches("[A-Z0-9]+")) {
            return;
        }

        if (token.equals("+") || token.equals("-") ||
                token.equals("*") || token.equals("/") ||
                token.equals("\\") || token.equals("^") ||
                token.equals(">") || token.equals("<") ||
                token.equals("=")) {
            return;
        }

        if (token.equals("(") || token.equals(")") ||
                token.equals(":") || token.equals(";")) {
            return;
        }

        System.out.println("Token no valido '" + token + "' en linea " + linea);
    }
    
    
}
