package AnalizadorSintactico;

public class Entorno {
    private Simbolo[] tabla;
    private int contadorSimbolos = 0;
    protected Entorno anterior;

    public Entorno(Entorno entornoAnterior) {
        tabla = new Simbolo[200];
        anterior = entornoAnterior;
    }

    public void agregar(String lexema, Simbolo simbolo) {
        if (contadorSimbolos < tabla.length) {
            tabla[contadorSimbolos++] = simbolo;
        }
    }

    public Simbolo buscar(String lexema) {
        for (Entorno actual = this; actual != null; actual = actual.anterior) {
            for (int i = 0; i < actual.contadorSimbolos; i++) {
                if (actual.tabla[i].nombre.equals(lexema)) {
                    return actual.tabla[i];
                }
            }
        }
        return null;
    }
}