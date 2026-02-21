package AnalizadorSintactico;

import java.util.Hashtable;

public class Entorno {
    private Hashtable<String, Simbolo> tabla;
    protected Entorno ant;

    public Entorno(Entorno n) {
        tabla = new Hashtable<>();
        ant = n;
    }

    public void poner(String s, Simbolo sym) {
        tabla.put(s, sym);
    }

    public Simbolo obtener(String s) {
        for (Entorno e = this; e != null; e = e.ant) {
            Simbolo encontrado = e.tabla.get(s);
            if (encontrado != null)
                return encontrado;
        }
        return null;
    }
}