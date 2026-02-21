package AnalizadorSintactico;

import AnalizadorLexico.*;
import java.io.*;

public class AnalizadorSintactico {
    private AnalizadorLexico lexico;
    private Token preanalisis;
    private Entorno superior = null;

    public AnalizadorSintactico(AnalizadorLexico l) throws IOException {
        lexico = l;
        moverse();
    }

    private void moverse() throws IOException {
        preanalisis = lexico.escanear();
    }

    private void coincidir(int etiqueta) throws IOException {
        if (preanalisis.etiqueta == etiqueta) {
            moverse();
        } else {
            throw new ManejadorError(lexico.linea,
                    "Se esperaba el token con codigo: " + etiqueta + " (Recibido: " + preanalisis.etiqueta + ")");
        }
    }

    public void analizar() throws IOException {
        superior = new Entorno(null);
        programa();
    }

    private void programa() throws IOException {
        coincidir(Etiqueta.MODULO);
        coincidir(Etiqueta.ID);
        coincidir(Etiqueta.VARIABLES);
        decls_globales();
        coincidir(Etiqueta.FIN_VARIABLES);
        decls_func();
        coincidir(Etiqueta.PRINCIPAL);

        Entorno entornoAnterior = superior;
        superior = new Entorno(superior);

        bloque();

        coincidir(Etiqueta.FIN_PRINCIPAL);
        superior = entornoAnterior;
        coincidir(Etiqueta.FIN_MODULO);
    }

    private void decls_globales() throws IOException {
        while (esTipo(preanalisis.etiqueta)) {
            declaracion_variable();
        }
    }

    private void decls_func() throws IOException {
        while (preanalisis.etiqueta == Etiqueta.FUNCION) {
            coincidir(Etiqueta.FUNCION);
            int tipoFunc = preanalisis.etiqueta;
            tipo();
            String nombreFunc = ((Palabra) preanalisis).lexema;
            coincidir(Etiqueta.ID);

            superior.poner(nombreFunc, new Simbolo(tipoFunc, nombreFunc));

            Entorno entornoAnterior = superior;
            superior = new Entorno(superior);

            coincidir('(');
            if (esTipo(preanalisis.etiqueta))
                parametros();
            coincidir(')');

            bloque();

            if (preanalisis.etiqueta == Etiqueta.RETORNA) {
                coincidir(Etiqueta.RETORNA);
                expresion_logica();
            }

            coincidir(Etiqueta.FIN_FUNCION);
            superior = entornoAnterior;
        }
    }

    private void parametros() throws IOException {
        int t = preanalisis.etiqueta;
        tipo();
        String id = ((Palabra) preanalisis).lexema;
        coincidir(Etiqueta.ID);
        superior.poner(id, new Simbolo(t, id));

        if (preanalisis.etiqueta == ',') {
            coincidir(',');
            parametros();
        }
    }

    private void declaracion_variable() throws IOException {
        int tipoVar = preanalisis.etiqueta;
        tipo();

        if (!(preanalisis instanceof Palabra)) {
            throw new ManejadorError(lexico.linea, "Se esperaba un identificador válido.");
        }

        String nombreVar = ((Palabra) preanalisis).lexema;
        coincidir(Etiqueta.ID);

        superior.poner(nombreVar, new Simbolo(tipoVar, nombreVar));

        if (preanalisis.etiqueta == '=') {
            coincidir('=');
            expresion_logica();
        }
    }

    private void tipo() throws IOException {
        if (esTipo(preanalisis.etiqueta)) {
            moverse();
        } else {
            throw new ManejadorError(lexico.linea, "Tipo de dato no esperado.");
        }
    }

    private boolean esTipo(int e) {
        return (e == Etiqueta.ENTERO || e == Etiqueta.DECIMAL ||
                e == Etiqueta.TEXTO || e == Etiqueta.BOOLEANO || e == Etiqueta.VACIO);
    }

    private void bloque() throws IOException {
        while (preanalisis.etiqueta != Etiqueta.FIN_PRINCIPAL &&
                preanalisis.etiqueta != Etiqueta.FIN_SI &&
                preanalisis.etiqueta != Etiqueta.FIN_PARA &&
                preanalisis.etiqueta != Etiqueta.FIN_MIENTRAS &&
                preanalisis.etiqueta != Etiqueta.SINO &&
                preanalisis.etiqueta != Etiqueta.FIN_FUNCION &&
                preanalisis.etiqueta != Etiqueta.RETORNA &&
                preanalisis.etiqueta != Etiqueta.EOF) {
            instruccion();
        }
    }

    private void instruccion() throws IOException {
        switch (preanalisis.etiqueta) {
            case Etiqueta.SI:
                condicional_si();
                break;
            case Etiqueta.PARA:
                ciclo_para();
                break;
            case Etiqueta.MIENTRAS:
                ciclo_mientras();
                break;
            case Etiqueta.IMPRIME:
                imprimir();
                break;
            case Etiqueta.LEER:
                leer_func();
                break;
            case Etiqueta.RETORNA:
                coincidir(Etiqueta.RETORNA);
                expresion_logica();
                break;
            case Etiqueta.ID:
                asignacion_o_llamada();
                break;
            default:
                throw new ManejadorError(lexico.linea, "Instruccion no reconocida cerca de: " + preanalisis);
        }
    }

    private void asignacion_o_llamada() throws IOException {
        String id = ((Palabra) preanalisis).lexema;
        if (superior.obtener(id) == null) {
            throw new ManejadorError(lexico.linea, "El identificador '" + id + "' no ha sido declarado.");
        }

        coincidir(Etiqueta.ID);
        if (preanalisis.etiqueta == '=') {
            coincidir('=');
            expresion_logica();
        } else if (preanalisis.etiqueta == '(') {
            coincidir('(');
            argumentos();
            coincidir(')');
        }
    }

    private void asignacion_simple() throws IOException {
        String id = ((Palabra) preanalisis).lexema;
        if (superior.obtener(id) == null) {
            throw new ManejadorError(lexico.linea, "El identificador '" + id + "' no ha sido declarado.");
        }
        coincidir(Etiqueta.ID);
        coincidir('=');
        expresion_logica();
    }

    private void condicional_si() throws IOException {
        coincidir(Etiqueta.SI);
        coincidir('(');
        expresion_logica();
        coincidir(')');
        coincidir(Etiqueta.ENTONCES);

        Entorno previo = superior;
        superior = new Entorno(superior);
        bloque();
        superior = previo;

        if (preanalisis.etiqueta == Etiqueta.SINO) {
            coincidir(Etiqueta.SINO);
            superior = new Entorno(superior);
            bloque();
            superior = previo;
        }
        coincidir(Etiqueta.FIN_SI);
    }

    private void ciclo_mientras() throws IOException {
        coincidir(Etiqueta.MIENTRAS);
        coincidir('(');
        expresion_logica();
        coincidir(')');

        Entorno previo = superior;
        superior = new Entorno(superior);
        bloque();
        superior = previo;

        coincidir(Etiqueta.FIN_MIENTRAS);
    }

    private void imprimir() throws IOException {
        coincidir(Etiqueta.IMPRIME);
        coincidir('(');
        expresion_logica();
        coincidir(')');
    }

    private void leer_func() throws IOException {
        coincidir(Etiqueta.LEER);
        coincidir('(');
        String id = ((Palabra) preanalisis).lexema;
        if (superior.obtener(id) == null) {
            throw new ManejadorError(lexico.linea, "El identificador '" + id + "' no ha sido declarado.");
        }
        coincidir(Etiqueta.ID);
        coincidir(')');
    }

    private void ciclo_para() throws IOException {
        coincidir(Etiqueta.PARA);
        coincidir('(');
        asignacion_simple();
        coincidir(',');
        expresion_logica();
        coincidir(',');
        asignacion_simple();
        coincidir(')');

        Entorno previo = superior;
        superior = new Entorno(superior);
        bloque();
        superior = previo;

        coincidir(Etiqueta.FIN_PARA);
    }

    private void expresion_logica() throws IOException {
        termino_logico();
        while (preanalisis.etiqueta == Etiqueta.O) {
            coincidir(Etiqueta.O);
            termino_logico();
        }
    }

    private void termino_logico() throws IOException {
        factor_logico();
        while (preanalisis.etiqueta == Etiqueta.Y) {
            coincidir(Etiqueta.Y);
            factor_logico();
        }
    }

    private void factor_logico() throws IOException {
        if (preanalisis.etiqueta == '!') {
            coincidir('!');
            factor_logico();
        } else {
            expresion_relacional();
        }
    }

    private void expresion_relacional() throws IOException {
        expresion_aritmetica();
        if (preanalisis.etiqueta == '<' || preanalisis.etiqueta == '>' ||
                preanalisis.etiqueta == Etiqueta.IGUALDAD || preanalisis.etiqueta == Etiqueta.DIFERENTE) {
            moverse();
            expresion_aritmetica();
        }
    }

    private void expresion_aritmetica() throws IOException {
        termino_aritmetico();
        while (preanalisis.etiqueta == '+' || preanalisis.etiqueta == '-') {
            moverse();
            termino_aritmetico();
        }
    }

    private void termino_aritmetico() throws IOException {
        factor_aritmetico();
        while (preanalisis.etiqueta == '*' || preanalisis.etiqueta == '/' || preanalisis.etiqueta == '%') {
            moverse();
            factor_aritmetico();
        }
    }

    private void factor_aritmetico() throws IOException {
        switch (preanalisis.etiqueta) {
            case '(':
                coincidir('(');
                expresion_logica();
                coincidir(')');
                break;
            case Etiqueta.ID:
                String id = ((Palabra) preanalisis).lexema;
                if (superior.obtener(id) == null) {
                    throw new ManejadorError(lexico.linea, "La variable '" + id + "' no existe en este ambito.");
                }
                coincidir(Etiqueta.ID);
                if (preanalisis.etiqueta == '(') {
                    coincidir('(');
                    argumentos();
                    coincidir(')');
                }
                break;
            case Etiqueta.NUM_INT:
                moverse();
                break;
            case Etiqueta.NUM_DEC:
                moverse();
                break;
            case Etiqueta.CADENA:
                moverse();
                break;
            case Etiqueta.V:
                moverse();
                break;
            case Etiqueta.F:
                moverse();
                break;
            default:
                throw new ManejadorError(lexico.linea, "Error de expresion cerca de: " + preanalisis);
        }
    }

    private void argumentos() throws IOException {
        if (preanalisis.etiqueta != ')') {
            expresion_logica();
            while (preanalisis.etiqueta == ',') {
                coincidir(',');
                expresion_logica();
            }
        }
    }
}