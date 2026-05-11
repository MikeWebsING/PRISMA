package AnalizadorLexico;

import java.io.*;

public class AnalizadorLexico {
    private int linea = 1;
    private int columna = 0;
    private char preanalisis = ' ';
    
    private Palabra[] tablaPalabras;
    private int contadorPalabras = 0;
    
    private char[][] lineasFuente;
    private int totalLineas = 0;
    
    private char[][] erroresLexicos;
    private int contadorErrores = 0;

    public void definirLineasFuente(char[] caracteres) {
        if (caracteres != null) {
            int conteoLineas = 1;
            for (int i = 0; i < caracteres.length; i++) {
                if (caracteres[i] == '\n') conteoLineas++;
            }
            lineasFuente = new char[conteoLineas][];
            totalLineas = conteoLineas;
            
            int indiceLinea = 0;
            int inicio = 0;
            for (int i = 0; i <= caracteres.length; i++) {
                if (i == caracteres.length || caracteres[i] == '\n') {
                    int longitudLinea = i - inicio;
                    if (longitudLinea > 0 && caracteres[i - 1] == '\r') {
                        longitudLinea--;
                    }
                    char[] lineaActual = new char[longitudLinea];
                    for (int j = 0; j < longitudLinea; j++) {
                        lineaActual[j] = caracteres[inicio + j];
                    }
                    lineasFuente[indiceLinea++] = lineaActual;
                    inicio = i + 1;
                }
            }
        }
    }

    public char[] obtenerTextoLinea(int numeroLinea) {
        if (numeroLinea > 0 && numeroLinea <= totalLineas) {
            return lineasFuente[numeroLinea - 1];
        }
        return new char[0];
    }

    private void reservar(Palabra palabra) {
        tablaPalabras[contadorPalabras++] = palabra;
    }

    public AnalizadorLexico() {
        tablaPalabras = new Palabra[200];
        erroresLexicos = new char[1000][];
        
        reservar(new Palabra(new char[]{'M','O','D','U','L','O'}, Etiqueta.MODULO));
        reservar(new Palabra(new char[]{'F','I','N','-','M','O','D','U','L','O'}, Etiqueta.FIN_MODULO));
        reservar(new Palabra(new char[]{'P','R','I','N','C','I','P','A','L'}, Etiqueta.PRINCIPAL));
        reservar(new Palabra(new char[]{'F','I','N','-','P','R','I','N','C','I','P','A','L'}, Etiqueta.FIN_PRINCIPAL));
        reservar(new Palabra(new char[]{'F','U','N','C','I','O','N'}, Etiqueta.FUNCION));
        reservar(new Palabra(new char[]{'F','I','N','-','F','U','N','C','I','O','N'}, Etiqueta.FIN_FUNCION));
        reservar(new Palabra(new char[]{'R','E','T','O','R','N','A'}, Etiqueta.RETORNA));
        reservar(new Palabra(new char[]{'S','I'}, Etiqueta.SI));
        reservar(new Palabra(new char[]{'S','I','N','O'}, Etiqueta.SINO));
        reservar(new Palabra(new char[]{'F','I','N','-','S','I'}, Etiqueta.FIN_SI));
        reservar(new Palabra(new char[]{'P','A','R','A'}, Etiqueta.PARA));
        reservar(new Palabra(new char[]{'F','I','N','-','P','A','R','A'}, Etiqueta.FIN_PARA));
        reservar(new Palabra(new char[]{'M','I','E','N','T','R','A','S'}, Etiqueta.MIENTRAS));
        reservar(new Palabra(new char[]{'F','I','N','-','M','I','E','N','T','R','A','S'}, Etiqueta.FIN_MIENTRAS));
        reservar(new Palabra(new char[]{'E','N','T','E','R','O'}, Etiqueta.ENTERO));
        reservar(new Palabra(new char[]{'D','E','C','I','M','A','L'}, Etiqueta.DECIMAL));
        reservar(new Palabra(new char[]{'T','E','X','T','O'}, Etiqueta.TEXTO));
        reservar(new Palabra(new char[]{'B','O','O','L','E','A','N','O'}, Etiqueta.BOOLEANO));
        reservar(new Palabra(new char[]{'V','A','C','I','O'}, Etiqueta.VACIO));
        reservar(new Palabra(new char[]{'I','M','P','R','I','M','E'}, Etiqueta.IMPRIME));
        reservar(new Palabra(new char[]{'L','E','E','R'}, Etiqueta.LEER));
        reservar(new Palabra(new char[]{'Y'}, Etiqueta.Y));
        reservar(new Palabra(new char[]{'O'}, Etiqueta.O));
        reservar(new Palabra(new char[]{'N','O'}, Etiqueta.NO));
        reservar(new Palabra(new char[]{'V'}, Etiqueta.VERDADERO));
        reservar(new Palabra(new char[]{'F'}, Etiqueta.FALSO));
        reservar(new Palabra(new char[]{'V','A','R','I','A','B','L','E','S'}, Etiqueta.VARIABLES));
        reservar(new Palabra(new char[]{'F','I','N','-','V','A','R','I','A','B','L','E','S'}, Etiqueta.FIN_VARIABLES));
        reservar(new Palabra(new char[]{'E','N','T','O','N','C','E','S'}, Etiqueta.ENTONCES));
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
                    return new Palabra(new char[]{'=','='}, Etiqueta.IGUALDAD, linea, columnaDondeInicia);
                } else {
                    return new SimboloLexico('=', linea, columnaDondeInicia);
                }
            case '!':
                if (compararSiguiente('=')) {
                    return new Palabra(new char[]{'!','='}, Etiqueta.DIFERENTE, linea, columnaDondeInicia);
                } else {
                    return new SimboloLexico('!', linea, columnaDondeInicia);
                }
            case '<':
                if (compararSiguiente('=')) {
                    return new Palabra(new char[]{'<','='}, Etiqueta.MENOR_IGUAL, linea, columnaDondeInicia);
                } else {
                    return new SimboloLexico('<', linea, columnaDondeInicia);
                }
            case '>':
                if (compararSiguiente('=')) {
                    return new Palabra(new char[]{'>','='}, Etiqueta.MAYOR_IGUAL, linea, columnaDondeInicia);
                } else {
                    return new SimboloLexico('>', linea, columnaDondeInicia);
                }
        }

        if (esNumero(preanalisis)) {
            int acumuladorInt = 0;
            do {
                acumuladorInt = 10 * acumuladorInt + (preanalisis - '0');
                avanzar();
            } while (esNumero(preanalisis));
            
            if (preanalisis != '.') {
                return new Numero(acumuladorInt, linea, columnaDondeInicia);
            }
            
            float acumuladorFloat = acumuladorInt;
            float divisor = 10;
            boolean tieneFraccion = false;
            for (;;) {
                avanzar();
                if (!esNumero(preanalisis)) {
                    break;
                }
                tieneFraccion = true;
                acumuladorFloat = acumuladorFloat + (float) (preanalisis - '0') / divisor;
                divisor = divisor * 10;
            }
            if (!tieneFraccion) {
                registrarErrorLexico(linea, columnaDondeInicia, new char[]{'N','u','m','e','r','o',' ','d','e','c','i','m','a','l',' ','i','n','c','o','m','p','l','e','t','o'});
                return new Palabra(new char[]{'.'}, Etiqueta.ERROR, linea, columnaDondeInicia);
            }
            return new Real(acumuladorFloat, linea, columnaDondeInicia);
        }

        if (esLetra(preanalisis) || preanalisis == '_') {
            char[] bufferTemporal = new char[500];
            int longitud = 0;
            do {
                bufferTemporal[longitud++] = preanalisis;
                avanzar();
            } while (esLetraONumero(preanalisis) || preanalisis == '_');
            
            if (longitud == 3 && bufferTemporal[0] == 'F' && bufferTemporal[1] == 'I' && bufferTemporal[2] == 'N' && preanalisis == '-') {
                bufferTemporal[longitud++] = preanalisis;
                avanzar();
                while (esLetraONumero(preanalisis) || preanalisis == '_') {
                    bufferTemporal[longitud++] = preanalisis;
                    avanzar();
                }
            }
            
            char[] lexemaConstruido = new char[longitud];
            for (int i = 0; i < longitud; i++) {
                lexemaConstruido[i] = bufferTemporal[i];
            }
            
            for (int i = 0; i < contadorPalabras; i++) {
                if (tablaPalabras[i].getLexema() != null && tablaPalabras[i].esIgualA(lexemaConstruido)) {
                    return new Palabra(tablaPalabras[i].getLexema(), tablaPalabras[i].getEtiqueta(), linea, columnaDondeInicia);
                }
            }

            if (!esIdentificadorValido(lexemaConstruido)) {
                registrarErrorLexico(linea, columnaDondeInicia, new char[]{'I','d','e','n','t','i','f','i','c','a','d','o','r',' ','i','n','v','a','l','i','d','o'});
                return new Palabra(lexemaConstruido, Etiqueta.ERROR, linea, columnaDondeInicia);
            }

            return new Palabra(lexemaConstruido, Etiqueta.IDENTIFICADOR, linea, columnaDondeInicia);
        }

        if (preanalisis == '"') {
            char[] bufferTemporal = new char[2000];
            int longitud = 0;
            while (true) {
                avanzar();
                if (preanalisis == '"' || preanalisis == '\n' || preanalisis == '\r' || preanalisis == (char) -1 || preanalisis == 65535) {
                    break;
                }
                bufferTemporal[longitud++] = preanalisis;
            }
            char[] cadenaConstruida = new char[longitud];
            for (int i = 0; i < longitud; i++) {
                cadenaConstruida[i] = bufferTemporal[i];
            }
            
            if (preanalisis == '"') {
                avanzar();
                return new Palabra(cadenaConstruida, Etiqueta.CADENA, linea, columnaDondeInicia);
            } else {
                registrarErrorLexico(linea, columnaDondeInicia, new char[]{'C','a','d','e','n','a',' ','n','o',' ','c','e','r','r','a','d','a'});
                return new Palabra(cadenaConstruida, Etiqueta.ERROR, linea, columnaDondeInicia);
            }
        }

        if (esCaracterValido(preanalisis)) {
            SimboloLexico simboloSimple = new SimboloLexico(preanalisis, linea, columnaDondeInicia);
            preanalisis = ' ';
            return simboloSimple;
        } else {
            registrarErrorLexico(linea, columnaDondeInicia, concatenar(new char[]{'C','a','r','a','c','t','e','r',' ','n','o',' ','p','e','r','m','i','t','i','d','o',':',' ','\''}, new char[]{preanalisis}, new char[]{'\''}));
            SimboloLexico simboloError = new Palabra(new char[]{preanalisis}, Etiqueta.ERROR, linea, columnaDondeInicia);
            preanalisis = ' ';
            return simboloError;
        }
    }

    private void registrarErrorLexico(int lineaErr, int colErr, char[] mensaje) {
        if (contadorErrores < erroresLexicos.length) {
            char[] prefijo = concatenar(new char[]{'E','R','R','O','R',' ','L','E','X','I','C','O',' ','L','i','n','e','a',' '}, intAChars(lineaErr), new char[]{',',' ','C','o','l',' '}, intAChars(colErr), new char[]{':',' '});
            erroresLexicos[contadorErrores++] = concatenar(prefijo, mensaje);
        }
    }

    private char[] concatenar(char[]... partes) {
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

    private char[] intAChars(int numero) {
        if (numero == 0) return new char[]{'0'};
        int temp = numero;
        int digitos = 0;
        while (temp > 0) {
            digitos++;
            temp /= 10;
        }
        char[] resultado = new char[digitos];
        temp = numero;
        for (int i = digitos - 1; i >= 0; i--) {
            resultado[i] = (char) ((temp % 10) + '0');
            temp /= 10;
        }
        return resultado;
    }

    public char[][] obtenerErroresLexicos() {
        char[][] resultado = new char[contadorErrores][];
        for (int i = 0; i < contadorErrores; i++) {
            resultado[i] = erroresLexicos[i];
        }
        return resultado;
    }

    private boolean esLetra(char c) {
        return (c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z');
    }

    private boolean esNumero(char c) {
        return (c >= '0' && c <= '9');
    }

    private boolean esLetraONumero(char c) {
        return esLetra(c) || esNumero(c);
    }

    private boolean esIdentificadorValido(char[] lexema) {
        if (lexema.length == 0) return false;
        char inicio = lexema[0];
        if (!((inicio >= 'a' && inicio <= 'z') || inicio == '_')) {
            return false;
        }
        for (int i = 1; i < lexema.length; i++) {
            char c = lexema[i];
            if (!(esLetraONumero(c) || c == '_')) {
                return false;
            }
        }
        return true;
    }

    private boolean esCaracterValido(char caracter) {
        if (esLetraONumero(caracter)) return true;
        char[] validos = {'+', '-', '*', '/', '%', '=', '>', '<', '!', '(', ')', ',', '"', '_'};
        for (int i = 0; i < validos.length; i++) {
            if (caracter == validos[i]) return true;
        }
        return false;
    }
    
    public int getLinea() {
        return linea;
    }
    
    public int getColumna() {
        return columna;
    }
}