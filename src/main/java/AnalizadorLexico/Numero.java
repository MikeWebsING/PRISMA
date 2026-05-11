package AnalizadorLexico;

public class Numero extends SimboloLexico {
    private int valor;

    public Numero(int valor, int linea, int columna) {
        super(Etiqueta.NUMERO_ENTERO, linea, columna);
        this.valor = valor;
    }

    public int getValor() {
        return this.valor;
    }

    public void setValor(int valor) {
        this.valor = valor;
    }
}
