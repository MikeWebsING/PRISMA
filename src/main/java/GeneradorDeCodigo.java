import java.io.File;
import java.io.FileWriter;
import java.util.Scanner;

public class GeneradorDeCodigo {
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
        String macros = "IMPRIME Macro Mensaje\n" +
                        "    Mov Ah, 09h\n" +
                        "    mov dx, offset Mensaje\n" +
                        "    int 21h\n" +
                        "EndM\n\n" +
                        "LEER Macro Entrada\n" +
                        "    mov Ah, 0Ah\n" +
                        "    mov Dx, offset Entrada\n" +
                        "    int 21h\n" +
                        "EndM\n\n" +
                        "CLS Macro\n" +
                        "    Mov Ah, 0Fh\n" +
                        "    int 10h\n" +
                        "    Mov Ah, 0h\n" +
                        "    int 10h\n" +
                        "EndM\n\n" +
                        "MOSTRAR_NUMERO Macro\n" +
                        "    local dividir, imprimir, negativo, positivo\n" +
                        "    push ax\n" +
                        "    push bx\n" +
                        "    push cx\n" +
                        "    push dx\n" +
                        "    cmp al, 0\n" +
                        "    jge positivo\n" +
                        "    push ax\n" +
                        "    mov dl, '-'\n" +
                        "    mov ah, 02h\n" +
                        "    int 21h\n" +
                        "    pop ax\n" +
                        "    neg al\n" +
                        "positivo:\n" +
                        "    mov cx, 0\n" +
                        "    mov bl, 10\n" +
                        "dividir:\n" +
                        "    mov ah, 0\n" +
                        "    div bl\n" +
                        "    push ax\n" +
                        "    inc cx\n" +
                        "    cmp al, 0\n" +
                        "    jne dividir\n" +
                        "imprimir:\n" +
                        "    pop ax\n" +
                        "    mov dl, ah\n" +
                        "    add dl, 30h\n" +
                        "    mov ah, 02h\n" +
                        "    int 21h\n" +
                        "    loop imprimir\n" +
                        "    pop dx\n" +
                        "    pop cx\n" +
                        "    pop bx\n" +
                        "    pop ax\n" +
                        "EndM\n\n";

        String inicio = ".MODEL SMALL\n" +
                        ".CODE\n" +
                        "Inicio:\n" +
                        "mov Ax, @Data\n" +
                        "mov Ds, Ax\n" +
                        "CLS\n";

        String cuerpo = "";
        String datos = ".DATA\nSalto db 10,13,24h\n";
        
        String[] lineas = codigo.split("\n");
        boolean enVariables = false;
        boolean enPrincipal = false;
        int contadorMensajes = 0;

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
                    datos += nombreVariable + " db ?\n";
                    datos += "BUF_" + nombreVariable + " db 5, ?, 5 dup (24h)\n";
                }
            }

            if (enPrincipal) {
                if (linea.startsWith("IMPRIME")) {
                    int inicioParen = linea.indexOf("(");
                    int finParen = linea.lastIndexOf(")");
                    String argumento = linea.substring(inicioParen + 1, finParen).trim();
                    
                    if (argumento.startsWith("\"")) {
                        contadorMensajes = contadorMensajes + 1;
                        String texto = argumento.substring(1, argumento.length() - 1);
                        datos += "MSG" + contadorMensajes + " db \"" + texto + "$\"\n";
                        cuerpo += "IMPRIME MSG" + contadorMensajes + "\n";
                    } else {
                        cuerpo += "mov al, byte ptr [" + argumento + "]\n";
                        cuerpo += "MOSTRAR_NUMERO\n";
                        cuerpo += "IMPRIME Salto\n";
                    }
                } else if (linea.startsWith("LEER")) {
                    int inicioParen = linea.indexOf("(");
                    int finParen = linea.lastIndexOf(")");
                    String variable = linea.substring(inicioParen + 1, finParen).trim();
                    cuerpo += "LEER BUF_" + variable + "\n";
                    cuerpo += "mov si, offset BUF_" + variable + "+2\n";
                    cuerpo += "mov al, [si]\n";
                    cuerpo += "sub al, 30h\n";
                    cuerpo += "mov byte ptr [" + variable + "], al\n";
                    cuerpo += "IMPRIME Salto\n";
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

                        cuerpo += "mov al, byte ptr [" + operando1 + "]\n";
                        cuerpo += "mov bl, byte ptr [" + operando2 + "]\n";

                        if (operador == '+') {
                            cuerpo += "add al, bl\n";
                        } else if (operador == '-') {
                            cuerpo += "sub al, bl\n";
                        } else if (operador == '*') {
                            cuerpo += "mul bl\n";
                        } else if (operador == '/') {
                            cuerpo += "xor ah, ah\n";
                            cuerpo += "div bl\n";
                        } else if (operador == '%') {
                            cuerpo += "xor ah, ah\n";
                            cuerpo += "div bl\n";
                            cuerpo += "mov al, ah\n";
                        }

                        cuerpo += "mov byte ptr [" + destino + "], al\n";
                    }
                }
            }
        }

        String fin = "mov ax, 4C00h\nint 21h\n";
        return macros + inicio + cuerpo + fin + datos + ".STACK\nEND Inicio";
    }
}
