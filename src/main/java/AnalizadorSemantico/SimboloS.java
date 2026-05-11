package AnalizadorSemantico;

public class SimboloS {
    public char[] nombre;
    public char[] tipo;
    public boolean inicializado;
    public boolean esFuncion;
    public char[][] firma;
    public int linea;
    public int columna;
    public Object valor;

    public SimboloS(char[] nombre, char[] tipo, boolean inicializado, int linea, int columna) {
        this.nombre = nombre;
        this.tipo = tipo;
        this.inicializado = inicializado;
        this.linea = linea;
        this.columna = columna;
        this.esFuncion = false;
        this.firma = new char[0][0];
    }
}
