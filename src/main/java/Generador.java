import java.io.File;
import java.io.FileWriter;
import java.util.Scanner;

public class Generador {
    public static void main(String[] argumentos) {
        try {
            File archivoEntrada = new File("programa_por_defecto.txt");
            Scanner lector = new Scanner(archivoEntrada);
            String contenido = "";
            while (lector.hasNextLine()) {
                contenido += lector.nextLine() + "\n";
            }
            lector.close();

            String resultadoAsm = traducir(contenido);

            File carpetaTemporal = new File("temporal");
            if (!carpetaTemporal.exists()) {
                carpetaTemporal.mkdir();
            }

            FileWriter escritor = new FileWriter("temporal/programa.asm");
            escritor.write(resultadoAsm);
            escritor.close();
        } catch (Exception error) {
        }
    }

    public static String traducir(String codigo) {
        String[] lineas = codigo.split("\n");
        boolean enVariables = false;
        boolean enPrincipal = false;

        for (int i = 0; i < lineas.length; i++) {
            String linea = lineas[i].trim();
            if (linea.isEmpty()) {
                continue;
            }

            if (linea.equals("VARIABLES")) {
                enVariables = true;
                continue;
            }
            if (linea.equals("FIN-VARIABLES")) {
                enVariables = false;
                continue;
            }
            if (linea.equals("PRINCIPAL")) {
                enPrincipal = true;
                continue;
            }
            if (linea.equals("FIN-PRINCIPAL")) {
                enPrincipal = false;
                continue;
            }

            if (enVariables) {
                if (linea.startsWith("ENTERO")) {
                    String nombreVariable = linea.substring(7).trim();
                }
            }

            if (enPrincipal) {
                if (linea.startsWith("IMPRIME")) {
                    int inicioParentesis = linea.indexOf("(");
                    int finParentesis = linea.lastIndexOf(")");
                    String argumento = linea.substring(inicioParentesis + 1, finParentesis).trim();
                    
                    if (argumento.startsWith("\"")) {
                        String texto = argumento.substring(1, argumento.length() - 1);
                    } else {
                        String nombreVariable = argumento;
                    }
                } else if (linea.startsWith("LEER")) {
                    int inicioParentesis = linea.indexOf("(");
                    int finParentesis = linea.lastIndexOf(")");
                    String variable = linea.substring(inicioParentesis + 1, finParentesis).trim();
                } else if (linea.contains("=")) {
                    int posicionIgual = linea.indexOf("=");
                    String destino = linea.substring(0, posicionIgual).trim();
                    String expresion = linea.substring(posicionIgual + 1).trim();
                    
                    char operador = ' ';
                    if (expresion.contains("+")) {
                        operador = '+';
                    } else if (expresion.contains("-")) {
                        operador = '-';
                    } else if (expresion.contains("*")) {
                        operador = '*';
                    } else if (expresion.contains("/")) {
                        operador = '/';
                    } else if (expresion.contains("%")) {
                        operador = '%';
                    }

                    if (operador != ' ') {
                        int posicionOperador = expresion.indexOf(operador);
                        String operando1 = expresion.substring(0, posicionOperador).trim();
                        String operando2 = expresion.substring(posicionOperador + 1).trim();
                    }
                }
            }
        }

        return "";
    }
}
