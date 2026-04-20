package compilador;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class AnalicadorSintactico {
    //Expresiones regulares
    private static final String DECLARACION = "^[A-Z]+\\s*:\\s*(ENT|FLOT|CAD)\\s*;$";
    private static final String CAPTURAR = "^CAPTURAR\\s*\\(\\s*[A-Z]+\\s*\\)\\s*;$";
    private static final String MOSTRAR = "^MOSTRAR\\s*\\(\\s*([A-Z]+|[0-9]+|[0-9]+,[0-9]+|\"[^\"]*\")\\s*\\)\\s*;$";
    private static final String OPERACION = "^[A-Z]+\\s*=\\s*([A-Z]+|[0-9]+|[0-9]+,[0-9]+)\\s*(\\+|\\-|\\*|\\/|\\\\|\\^)\\s*([A-Z]+|[0-9]+|[0-9]+,[0-9]+)\\s*;$";
    private static final String COND = "^\\(\\s*.+\\s*(>=|<=|==|<>|>|<)\\s*.+\\s*\\)$";
    private static final String SI = "^SI\\s*\\(.+\\)\\s*ENTONCES$";
    private static final String FIN_SI = "^FIN_SI:?$";
    private static final String SINO = "^SINO$";
    private static final String PARA = "^PARA\\s+[A-Z]+\\s+INICIA_EN\\s+.+\\s+TERMINA_EN\\s+.+\\s+HACER$";
    private static final String FIN_PARA = "^FIN_PARA:?$";
    private static final String MIENTRAS = "^MIENTRAS\\s*\\(.+\\)\\s*HACER$";
    private static final String FIN_MIENTRAS = "^FIN_MIENTRAS$";
    private static final String REPETIR = "^REPETIR$";
    private static final String HASTA = "^HASTA\\s*\\(.+\\)$";

    //validamos con matches 
    private boolean validar(String regex, String linea) {
        return linea.matches(regex);
    }
    //Analizador sintactico
    public void analizar(String nombreArchivo) {
        try {
            File archivo = new File(nombreArchivo);
            Scanner leer = new Scanner(archivo);

            int linea = 1;
            boolean inicio = false;
            boolean fin = false;
            boolean ini_decl=false;
            boolean fin_decl=false;

            while (leer.hasNextLine()) {

                String texto = leer.nextLine().trim();

                if (texto.isEmpty()) {
                    linea++;
                    continue;
                }
                //Validacion de Inicio y fin 
                if (texto.equals("INICIO:")) {
                    inicio = true;
                }

                else if (texto.equals("FIN:")) {
                    fin = true;
                }
                //validacion INI_DECL: y FIN_DECL: 
                else if (texto.equals("INI_DECL:")) {
                    ini_decl=true;
                }
                
                else if (texto.equals("FIN_DECL:")) {
                    fin_decl=true;
                }
                
               

                // =====================
                // VALIDACIONES
                // =====================
                else if (validar(DECLARACION, texto)) {}

                else if (validar(CAPTURAR, texto)) {}

                else if (validar(MOSTRAR, texto)) {}

                else if (validar(OPERACION, texto)) {}

                else if (validar(COND, texto)) {}

                else if (validar(SI, texto)) {}

                else if (validar(SINO, texto)) {}

                else if (validar(FIN_SI, texto)) {}

                else if (validar(PARA, texto)) {}

                else if (validar(FIN_PARA, texto)) {}

                else if (validar(MIENTRAS, texto)) {}

                else if (validar(FIN_MIENTRAS, texto)) {}

                else if (validar(REPETIR, texto)) {}

                else if (validar(HASTA, texto)) {}

                else {
                    System.out.println("Error sintactico en linea " + linea + ": " + texto);
                }
                linea++;
            }

            leer.close();

            //validamos si falta Inicio y fin del programa
            if (!inicio) {
                System.out.println("Error: falta INICIO:");
            }
            if (!fin) {
                System.out.println("Error: falta FIN:");
            }

        } catch (FileNotFoundException e) {
            System.out.println("Archivo no encontrado");
        }
    }
}
