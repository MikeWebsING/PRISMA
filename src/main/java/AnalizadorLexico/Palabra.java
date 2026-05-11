package AnalizadorLexico;

public class Palabra extends SimboloLexico {
    private char[] lexema;

    public Palabra(char[] lexema, int etiqueta) {
        super(etiqueta);
        this.lexema = lexema;
    }

    public Palabra(char[] lexema, int etiqueta, int linea, int columna) {
        super(etiqueta, linea, columna);
        this.lexema = lexema;
    }

    public char[] getLexema() {
        return this.lexema;
    }

    public void setLexema(char[] lexema) {
        this.lexema = lexema;
    }

    public boolean esIgualA(char[] comparacion) {
        if (this.lexema.length != comparacion.length) {
            return false;
        }
        for (int i = 0; i < this.lexema.length; i++) {
            if (this.lexema[i] != comparacion[i]) {
                return false;
            }
        }
        return true;
    }

    public static final Palabra yLogico = new Palabra(new char[]{'Y'}, Etiqueta.Y);
    public static final Palabra oLogico = new Palabra(new char[]{'O'}, Etiqueta.O);
    public static final Palabra compararIgualdad = new Palabra(new char[]{'=','='}, Etiqueta.IGUALDAD);
    public static final Palabra compararDiferencia = new Palabra(new char[]{'!','='}, Etiqueta.DIFERENTE);
    public static final Palabra compararMenorIgual = new Palabra(new char[]{'<','='}, Etiqueta.MENOR_IGUAL);
    public static final Palabra compararMayorIgual = new Palabra(new char[]{'>','='}, Etiqueta.MAYOR_IGUAL);
}