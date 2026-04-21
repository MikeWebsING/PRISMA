package AnalizadorSemantico;
import java.util.ArrayList;
import AnalizadorLexico.Etiqueta;

public class Semantico {
    // Estructura basica (ArrayList por regla de no usar Hashtable)
    public ArrayList<SimboloS> tablaSimbolos;
    public ArrayList<String> errores;

    public Semantico() {
        tablaSimbolos = new ArrayList<>();
        errores = new ArrayList<>();
    }

    public void regVar(int l, int c, String n, String t, boolean i) {
        if (buscarSimbolo(n) != null) {
            err(l, c, "E2", "Variable '" + n + "' ya declarada.");
        } else {
            tablaSimbolos.add(new SimboloS(n, t, i));
        }
    }

    public SimboloS buscarSimbolo(String n) {
        for (SimboloS s : tablaSimbolos) {
            if (s.n.equals(n)) return s;
        }
        return null;
    }

    public void declaracion(String n, int t, int l, int c) {
        regVar(l, c, n, Etiqueta.obtenerNombre(t), false);
    }

    public void asignacion(String n, int l, int c) {
        SimboloS s = buscarSimbolo(n);
        if (s == null) {
            err(l, c, "E1", "Variable '" + n + "' no declarada.");
        } else {
            s.i = true;
        }
    }

    public void leer(String n, int l, int c) {
        SimboloS s = buscarSimbolo(n);
        if (s == null) {
            err(l, c, "E1", "Variable '" + n + "' no declarada.");
        } else {
            s.i = true;
        }
    }

    public void imprime() {
        // Solo para analisis, no hace nada en ejecucion
    }

    public void err(int l, int c, String k, String m) {
        errores.add("[" + k + "] Linea " + l + ", Col " + c + ": " + m);
    }

    public ArrayList<String> obtenerErroresMsg() {
        return errores;
    }
}
