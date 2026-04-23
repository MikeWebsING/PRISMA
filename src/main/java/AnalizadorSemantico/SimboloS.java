package AnalizadorSemantico;

public class SimboloS {
    public String nombre;
    public String tipo;
    public boolean inicializado;
    public boolean esFuncion;
    public String firma;
    public int linea;
    public int columna;
    public Object valor;

    public SimboloS(String nombre, String tipo, boolean inicializado, int linea, int columna) {
        this.nombre = nombre;
        this.tipo = tipo;
        this.inicializado = inicializado;
        this.linea = linea;
        this.columna = columna;
        this.esFuncion = false;
        this.firma = "";
    }
}
