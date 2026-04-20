package AnalizadorLexico;

import java.io.*;
import java.util.*;

public class AnalizadorLexico {
    public int linea = 1;
    public int columna = 0; // Se incrementa al leer
    char preanalisis = ' ';
    Hashtable<String, Palabra> palabras = new Hashtable<>();
    private String[] lineasFuente = new String[0];

    public void setLineasFuente(String codigo) {
        if (codigo != null) {
            this.lineasFuente = codigo.split("\\r?\\n");
        }
    }

    public String obtenerTextoLinea(int n) {
        if (n > 0 && n <= lineasFuente.length) {
            return lineasFuente[n - 1];
        }
        return "";
    }

    void reservar(Palabra p) {
        palabras.put(p.lexema, p);
    }

    public AnalizadorLexico() {
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
        reservar(new Palabra("V", Etiqueta.V));
        reservar(new Palabra("F", Etiqueta.F));
        reservar(new Palabra("VARIABLES", Etiqueta.VARIABLES));
        reservar(new Palabra("FIN-VARIABLES", Etiqueta.FIN_VARIABLES));
        reservar(new Palabra("ENTONCES", Etiqueta.ENTONCES));
    }

    void leer() throws IOException {
        preanalisis = (char) System.in.read();
        columna++;
    }

    boolean leer(char c) throws IOException {
        leer();
        if (preanalisis != c)
            return false;
        preanalisis = ' ';
        return true;
    }

    public Token escanear() throws IOException {
        for (;; leer()) {
            if (preanalisis == ' ' || preanalisis == '\t' || preanalisis == '\r')
                continue;
            else if (preanalisis == '\n') {
                linea = linea + 1;
                columna = 0; // Se incrementará a 1 en el siguiente leer()
            } else
                break;
        }

        if (preanalisis == (char) -1)
            return new Token(Etiqueta.EOF, linea, columna);

        int colInicio = columna;

        switch (preanalisis) {
            case '=':
                if (leer('='))
                    return new Palabra("==", Etiqueta.IGUALDAD, linea, colInicio);
                else
                    return new Token('=', linea, colInicio);
            case '!':
                if (leer('='))
                    return new Palabra("!=", Etiqueta.DIFERENTE, linea, colInicio);
                else
                    return new Token('!', linea, colInicio);
            case '<':
                if (leer('='))
                    return new Palabra("<=", Etiqueta.MENOR_IGUAL, linea, colInicio);
                else
                    return new Token('<', linea, colInicio);
            case '>':
                if (leer('='))
                    return new Palabra(">=", Etiqueta.MAYOR_IGUAL, linea, colInicio);
                else
                    return new Token('>', linea, colInicio);
        }

        if (Character.isDigit(preanalisis)) {
            int v = 0;
            do {
                v = 10 * v + Character.digit(preanalisis, 10);
                leer();
            } while (Character.isDigit(preanalisis));
            if (preanalisis != '.')
                return new Numero(v, linea, colInicio);
            float x = v;
            float d = 10;
            for (;;) {
                leer();
                if (!Character.isDigit(preanalisis))
                    break;
                x = x + Character.digit(preanalisis, 10) / d;
                d = d * 10;
            }
            return new Real(x, linea, colInicio);
        }

        if (Character.isLetter(preanalisis) || preanalisis == '_') {
            StringBuffer b = new StringBuffer();
            do {
                b.append(preanalisis);
                leer();
            } while (Character.isLetterOrDigit(preanalisis) || preanalisis == '-' || preanalisis == '_');
            String s = b.toString();
            Palabra p = palabras.get(s);
            if (p != null) {
                // Clonamos para ponerle la posición actual sin afectar la reserva global
                return new Palabra(p.lexema, p.etiqueta, linea, colInicio);
            }

            if (s.matches("^[A-Z][A-Z0-9_-]*$")) {
                String errorMsg = "Identificador invalido: Mayusculas reservadas para palabras clave ('" + s + "')";
                return new Palabra(errorMsg, Etiqueta.ERROR, linea, colInicio);
            }

            p = new Palabra(s, Etiqueta.ID, linea, colInicio);
            // No guardar en 'palabras' de reserva para no llenar la tabla de IDs locales con posiciones fijas
            return p;
        }

        if (preanalisis == '"') {
            StringBuffer b = new StringBuffer();
            while (true) {
                leer();
                if (preanalisis == '"' || preanalisis == (char) -1)
                    break;
                b.append(preanalisis);
            }
            leer();
            return new Palabra(b.toString(), Etiqueta.CADENA, linea, colInicio);
        }

        if (esValido(preanalisis)) {
            Token t = new Token(preanalisis, linea, colInicio);
            preanalisis = ' ';
            return t;
        } else {
            String errorMsg = "Caracter no permitido: '" + preanalisis + "'";
            Token t = new Palabra(errorMsg, Etiqueta.ERROR, linea, colInicio);
            preanalisis = ' ';
            return t;
        }
    }

    private boolean esValido(char c) {
        return String.valueOf(c).matches("[a-zA-Z0-9+\\-*/%=><!(),\"_]");
    }
}