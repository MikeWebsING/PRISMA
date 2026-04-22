package AnalizadorSemantico;

import AnalizadorLexico.Etiqueta;

public class Semantico {
    private SimboloS[] tablaSimbolos;
    private int contadorSimbolos = 0;
    
    private String[] listaErrores;
    private int contadorErrores = 0;

    public Semantico() {
        tablaSimbolos = new SimboloS[500];
        listaErrores = new String[500];
    }

    public void registrarVariable(int linea, int columna, String nombre, String tipo, boolean inicializado) {
        if (buscarSimbolo(nombre) != null) {
            registrarError(linea, columna, "E2", "Variable '" + nombre + "' ya declarada.");
        } else {
            if (contadorSimbolos < tablaSimbolos.length) {
                tablaSimbolos[contadorSimbolos++] = new SimboloS(nombre, tipo, inicializado);
            }
        }
    }

    public SimboloS buscarSimbolo(String nombre) {
        for (int i = 0; i < contadorSimbolos; i++) {
            if (tablaSimbolos[i].nombre.equals(nombre)) {
                return tablaSimbolos[i];
            }
        }
        return null;
    }

    public void realizarDeclaracion(String nombre, int etiquetaTipo, int linea, int columna) {
        registrarVariable(linea, columna, nombre, Etiqueta.obtenerNombre(etiquetaTipo), false);
    }

    public void realizarAsignacion(String nombre, int linea, int columna) {
        SimboloS simbolo = buscarSimbolo(nombre);
        if (simbolo == null) {
            registrarError(linea, columna, "E1", "Variable '" + nombre + "' no declarada.");
        } else {
            simbolo.inicializado = true;
        }
    }

    public void realizarLectura(String nombre, int linea, int columna) {
        SimboloS simbolo = buscarSimbolo(nombre);
        if (simbolo == null) {
            registrarError(linea, columna, "E1", "Variable '" + nombre + "' no declarada.");
        } else {
            simbolo.inicializado = true;
        }
    }

    public void registrarError(int linea, int columna, String clave, String mensaje) {
        if (contadorErrores < listaErrores.length) {
            listaErrores[contadorErrores++] = "[" + clave + "] Linea " + linea + ", Col " + columna + ": " + mensaje;
        }
    }

    public String[] obtenerMensajesDeError() {
        String[] copiaErrores = new String[contadorErrores];
        for (int i = 0; i < contadorErrores; i++) {
            copiaErrores[i] = listaErrores[i];
        }
        return copiaErrores;
    }
}
