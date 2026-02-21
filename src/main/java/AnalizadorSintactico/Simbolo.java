package AnalizadorSintactico;

import AnalizadorLexico.Token;

public class Simbolo {
    public int etiqueta;
    public String nombre;

    public Simbolo(int etiqueta, String nombre) {
        this.etiqueta = etiqueta;
        this.nombre = nombre;
    }
}