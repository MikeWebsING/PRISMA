package AnalizadorSemantico;

public class SimboloS {
    public String nombre;
    public String tipo;
    public boolean inicializado;
    public Object valor;

    public SimboloS(String nombreIdentificador, String tipoDato, boolean esInicializado) {
        this.nombre = nombreIdentificador;
        this.tipo = tipoDato;
        this.inicializado = esInicializado;
    }
}
