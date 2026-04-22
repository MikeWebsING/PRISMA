package AnalizadorLexico;

public class Palabra extends SimboloLexico {
    public String lexema = "";

    public Palabra(String texto, int etiqueta) {
        super(etiqueta);
        lexema = texto;
    }

    public Palabra(String texto, int etiqueta, int linea, int columna) {
        super(etiqueta, linea, columna);
        lexema = texto;
    }

    public String toString() {
        return lexema;
    }

    public static final Palabra yLogico = new Palabra("Y", Etiqueta.Y),
            oLogico = new Palabra("O", Etiqueta.O),
            compararIgualdad = new Palabra("==", Etiqueta.IGUALDAD),
            compararDiferencia = new Palabra("!=", Etiqueta.DIFERENTE),
            compararMenorIgual = new Palabra("<=", Etiqueta.MENOR_IGUAL),
            compararMayorIgual = new Palabra(">=", Etiqueta.MAYOR_IGUAL);
}