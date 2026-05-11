package AnalizadorLexico;

public class Real extends SimboloLexico {
    private float valor;

    public Real(float valor, int linea, int columna) {
        super(Etiqueta.NUMERO_DECIMAL, linea, columna);
        this.valor = valor;
    }

    public float getValor() {
        return this.valor;
    }

    public void setValor(float valor) {
        this.valor = valor;
    }
}
