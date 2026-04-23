package AnalizadorLexico;

import java.io.*;

public class AnalizadorLexico {
    public int linea = 1;
    public int columna = 0;
    char preanalisis = ' ';
    
    private Palabra[] tablaPalabras;
    private int contadorPalabras = 0;
    
    private String[] lineasFuente;
    
    private String[] erroresLexicos;
    private int contadorErrores = 0;

    public void definirLineasFuente(String codigo) {
        if (codigo != null) {
            this.lineasFuente = codigo.split("\\r?\\n");
        }
    }

    public String obtenerTextoLinea(int numeroLinea) {
        if (numeroLinea > 0 && numeroLinea <= lineasFuente.length) {
            return lineasFuente[numeroLinea - 1];
        }
        return "";
    }

    private void reservar(Palabra palabra) {
        tablaPalabras[contadorPalabras++] = palabra;
    }

    public AnalizadorLexico() {
        tablaPalabras = new Palabra[200];
        erroresLexicos = new String[1000];
        
        reservar(new Palabra("MODULO", Etiqueta.MODULO));
        reservar(new Palabra("FIN-MODULO", Etiqueta.FIN_MODULO));
        reservar(new Palabra("PRINCIPAL", Etiqueta.PRINCIPAL));
        reservar(new Palabra("FIN-PRINCIPAL", Etiqueta.FIN_PRINCIPAL));
        reservar(new Palabra("FUNCION", Etiqueta.FUNCION));
        reservar(new Palabra("FIN-FUNCION", Etiqueta.FIN_FUNCION));
        reservar(new Palabra("RETORNA", Etiqueta.RETORNA));
        reservar(new Palabra("SI", Etiqueta.SI));
        reservar(new Palabra("SINO", Etiqueta.SINO));
        reservar(new Palabra("FIN-SI", Etiqueta.FIN_SI));
        reservar(new Palabra("PARA", Etiqueta.PARA));
        reservar(new Palabra("FIN-PARA", Etiqueta.FIN_PARA));
        reservar(new Palabra("MIENTRAS", Etiqueta.MIENTRAS));
        reservar(new Palabra("FIN-MIENTRAS", Etiqueta.FIN_MIENTRAS));
        reservar(new Palabra("ENTERO", Etiqueta.ENTERO));
        reservar(new Palabra("DECIMAL", Etiqueta.DECIMAL));
        reservar(new Palabra("TEXTO", Etiqueta.TEXTO));
        reservar(new Palabra("BOOLEANO", Etiqueta.BOOLEANO));
        reservar(new Palabra("VACIO", Etiqueta.VACIO));
        reservar(new Palabra("IMPRIME", Etiqueta.IMPRIME));
        reservar(new Palabra("LEER", Etiqueta.LEER));
        reservar(new Palabra("Y", Etiqueta.Y));
        reservar(new Palabra("O", Etiqueta.O));
        reservar(new Palabra("NO", Etiqueta.NO));
        reservar(new Palabra("V", Etiqueta.VERDADERO));
        reservar(new Palabra("F", Etiqueta.FALSO));
        reservar(new Palabra("VARIABLES", Etiqueta.VARIABLES));
        reservar(new Palabra("FIN-VARIABLES", Etiqueta.FIN_VARIABLES));
        reservar(new Palabra("ENTONCES", Etiqueta.ENTONCES));
    }

    private void avanzar() throws IOException {
        preanalisis = (char) System.in.read();
        columna++;
    }

    private boolean compararSiguiente(char caracterEsperado) throws IOException {
        avanzar();
        if (preanalisis != caracterEsperado) {
            return false;
        }
        preanalisis = ' ';
        return true;
    }

    public SimboloLexico obtenerSiguienteToken() throws IOException {
        for (;; avanzar()) {
            if (preanalisis == ' ' || preanalisis == '\t' || preanalisis == '\r') {
                continue;
            } else if (preanalisis == '\n') {
                linea = linea + 1;
                columna = 0;
            } else {
                break;
            }
        }

        if (preanalisis == (char) -1 || preanalisis == 65535) {
            return new SimboloLexico(Etiqueta.FIN_ARCHIVO, linea, columna);
        }

        int columnaDondeInicia = columna;

        switch (preanalisis) {
            case '=':
                if (compararSiguiente('=')) {
                    return new Palabra("==", Etiqueta.IGUALDAD, linea, columnaDondeInicia);
                } else {
                    return new SimboloLexico('=', linea, columnaDondeInicia);
                }
            case '!':
                if (compararSiguiente('=')) {
                    return new Palabra("!=", Etiqueta.DIFERENTE, linea, columnaDondeInicia);
                } else {
                    return new SimboloLexico('!', linea, columnaDondeInicia);
                }
            case '<':
                if (compararSiguiente('=')) {
                    return new Palabra("<=", Etiqueta.MENOR_IGUAL, linea, columnaDondeInicia);
                } else {
                    return new SimboloLexico('<', linea, columnaDondeInicia);
                }
            case '>':
                if (compararSiguiente('=')) {
                    return new Palabra(">=", Etiqueta.MAYOR_IGUAL, linea, columnaDondeInicia);
                } else {
                    return new SimboloLexico('>', linea, columnaDondeInicia);
                }
        }

        if (Character.isDigit(preanalisis)) {
            int acumuladorInt = 0;
            do {
                acumuladorInt = 10 * acumuladorInt + Character.digit(preanalisis, 10);
                avanzar();
            } while (Character.isDigit(preanalisis));
            
            if (preanalisis != '.') {
                return new Numero(acumuladorInt, linea, columnaDondeInicia);
            }
            
            float acumuladorFloat = acumuladorInt;
            float divisor = 10;
            for (;;) {
                avanzar();
                if (!Character.isDigit(preanalisis)) {
                    break;
                }
                acumuladorFloat = acumuladorFloat + (float) Character.digit(preanalisis, 10) / divisor;
                divisor = divisor * 10;
            }
            return new Real(acumuladorFloat, linea, columnaDondeInicia);
        }

        if (Character.isLetter(preanalisis) || preanalisis == '_') {
            String lexemaConstruido = "";
            do {
                lexemaConstruido = lexemaConstruido + preanalisis;
                avanzar();
            } while (Character.isLetterOrDigit(preanalisis) || preanalisis == '_');
            
            if (lexemaConstruido.equals("FIN") && preanalisis == '-') {
                lexemaConstruido = lexemaConstruido + preanalisis;
                avanzar();
                while (Character.isLetterOrDigit(preanalisis) || preanalisis == '_') {
                    lexemaConstruido = lexemaConstruido + preanalisis;
                    avanzar();
                }
            }
            
            for (int i = 0; i < contadorPalabras; i++) {
                if (tablaPalabras[i].lexema.equals(lexemaConstruido)) {
                    return new Palabra(tablaPalabras[i].lexema, tablaPalabras[i].etiqueta, linea, columnaDondeInicia);
                }
            }

            if (!lexemaConstruido.matches("^[a-z_][a-zA-Z0-9_]*$")) {
                registrarErrorLexico("ERROR LEXICO Linea " + linea + ", Col " + columnaDondeInicia + 
                    ": Identificador invalido: '" + lexemaConstruido + "'. Debe iniciar con minuscula o guion bajo.");
                return new Palabra(lexemaConstruido, Etiqueta.ERROR, linea, columnaDondeInicia);
            }

            return new Palabra(lexemaConstruido, Etiqueta.IDENTIFICADOR, linea, columnaDondeInicia);
        }

        if (preanalisis == '"') {
            String cadenaConstruida = "";
            while (true) {
                avanzar();
                if (preanalisis == '"' || preanalisis == (char) -1 || preanalisis == 65535) {
                    break;
                }
                cadenaConstruida = cadenaConstruida + preanalisis;
            }
            avanzar();
            return new Palabra(cadenaConstruida, Etiqueta.CADENA, linea, columnaDondeInicia);
        }

        if (esCaracterValido(preanalisis)) {
            SimboloLexico simboloSimple = new SimboloLexico(preanalisis, linea, columnaDondeInicia);
            preanalisis = ' ';
            return simboloSimple;
        } else {
            String mensajeError = "Caracter no permitido: '" + preanalisis + "'";
            SimboloLexico simboloError = new Palabra(mensajeError, Etiqueta.ERROR, linea, columnaDondeInicia);
            preanalisis = ' ';
            return simboloError;
        }
    }

    private void registrarErrorLexico(String mensaje) {
        if (contadorErrores < erroresLexicos.length) {
            erroresLexicos[contadorErrores++] = mensaje;
        }
    }

    public String[] obtenerErroresLexicos() {
        String[] resultado = new String[contadorErrores];
        for (int i = 0; i < contadorErrores; i++) {
            resultado[i] = erroresLexicos[i];
        }
        return resultado;
    }

    private boolean esCaracterValido(char caracter) {
        String cadenaCaracter = String.valueOf(caracter);
        return cadenaCaracter.matches("[a-zA-Z0-9+\\-*/%=><!(),\"_]");
    }
}