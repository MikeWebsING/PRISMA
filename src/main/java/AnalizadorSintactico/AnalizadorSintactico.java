package AnalizadorSintactico;

import AnalizadorLexico.*;
import AnalizadorSemantico.Semantico;
import java.io.*;

public class AnalizadorSintactico {
    private Semantico sem;
    // EXPRESIONES REGULARES PARA VALIDACION SINTACTICA
    private AnalizadorLexico lexico;
    private Token preanalisis;
    private Entorno superior = null;

    public AnalizadorSintactico(AnalizadorLexico l) throws IOException {
        lexico = l;
        sem = new Semantico();
        moverse();
    }

    private void moverse() throws IOException {
        preanalisis = lexico.escanear();
        if (preanalisis.etiqueta == Etiqueta.ERROR) {
            throw new ManejadorError(preanalisis.linea, preanalisis.columna, "LEXICO", 
                                     preanalisis.toString(), lexico.obtenerTextoLinea(preanalisis.linea));
        }
    }

    private void coincidir(int etiqueta) throws IOException {
        if (preanalisis.etiqueta == etiqueta) {
            moverse();
        } else {
            throw new ManejadorError(preanalisis.linea, preanalisis.columna, "SINTACTICO",
                    "Se esperaba: " + Etiqueta.obtenerNombre(etiqueta) + 
                    " pero se encontro la palabra: '" + preanalisis.toString() + "' (" + Etiqueta.obtenerNombre(preanalisis.etiqueta) + ")",
                    lexico.obtenerTextoLinea(preanalisis.linea));
        }
    }

    public void analizar() throws IOException {
        superior = new Entorno(null);
        programa();
    }

    public Semantico getSem() {
        return sem;
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
            throw new ManejadorError(preanalisis.linea, preanalisis.columna, "SINTACTICO", 
                                     "Se esperaba un identificador válido.", lexico.obtenerTextoLinea(preanalisis.linea));
        }

        String nombreVar = ((Palabra) preanalisis).lexema;
        coincidir(Etiqueta.ID);

        superior.poner(nombreVar, new Simbolo(tipoVar, nombreVar));
        sem.declaracion(nombreVar, tipoVar, preanalisis.linea, preanalisis.columna);

        if (preanalisis.etiqueta == '=') {
            coincidir('=');
            expresion_logica();
            sem.asignacion(nombreVar, preanalisis.linea, preanalisis.columna);
        }
    }

    private void tipo() throws IOException {
        if (esTipo(preanalisis.etiqueta)) {
            moverse();
        } else {
            throw new ManejadorError(preanalisis.linea, preanalisis.columna, "SINTACTICO", 
                                     "Tipo de dato no esperado: '" + preanalisis.toString() + "' (" + Etiqueta.obtenerNombre(preanalisis.etiqueta) + ")", 
                                     lexico.obtenerTextoLinea(preanalisis.linea));
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
                preanalisis.etiqueta != Etiqueta.EOF) {
            instruccion();
        }
    }

    private void instruccion() throws IOException {
        int e = preanalisis.etiqueta;
        if (e == Etiqueta.SI) {
            condicional_si();
        } else if (e == Etiqueta.PARA) {
            ciclo_para();
        } else if (e == Etiqueta.MIENTRAS) {
            ciclo_mientras();
        } else if (e == Etiqueta.IMPRIME) {
            imprimir();
        } else if (e == Etiqueta.LEER) {
            leer_func();
        } else if (e == Etiqueta.RETORNA) {
            coincidir(Etiqueta.RETORNA);
            expresion_logica();
        } else if (e == Etiqueta.ID) {
            asignacion_o_llamada();
        } else {
            throw new ManejadorError(preanalisis.linea, preanalisis.columna, "SINTACTICO", 
                                     "Instruccion no reconocida cerca de la palabra: '" + preanalisis.toString() + "' (" + Etiqueta.obtenerNombre(preanalisis.etiqueta) + ")", 
                                     lexico.obtenerTextoLinea(preanalisis.linea));
        }
    }

    private void asignacion_o_llamada() throws IOException {
        String id = ((Palabra) preanalisis).lexema;
        int l = preanalisis.linea;
        int c = preanalisis.columna;
        coincidir(Etiqueta.ID);
        if (preanalisis.etiqueta == '=') {
            coincidir('=');
            expresion_logica();
            sem.asignacion(id, l, c);
        } else if (preanalisis.etiqueta == '(') {
            coincidir('(');
            argumentos();
            coincidir(')');
        }
    }

    private void asignacion_simple() throws IOException {
        String id = ((Palabra) preanalisis).lexema;
        int l = preanalisis.linea;
        int c = preanalisis.columna;
        coincidir(Etiqueta.ID);
        coincidir('=');
        expresion_logica();
        sem.asignacion(id, l, c);
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
        sem.imprime();
    }

    private void leer_func() throws IOException {
        coincidir(Etiqueta.LEER);
        coincidir('(');
        String id = ((Palabra) preanalisis).lexema;
        int l = preanalisis.linea;
        int c = preanalisis.columna;
        coincidir(Etiqueta.ID);
        coincidir(')');
        sem.leer(id, l, c);
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
                preanalisis.etiqueta == Etiqueta.MAYOR_IGUAL || preanalisis.etiqueta == Etiqueta.MENOR_IGUAL ||
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
        int e = preanalisis.etiqueta;
        if (e == '(') {
            coincidir('(');
            expresion_logica();
            coincidir(')');
        } else if (e == Etiqueta.ID) {
            coincidir(Etiqueta.ID);
            if (preanalisis.etiqueta == '(') {
                coincidir('(');
                argumentos();
                coincidir(')');
            }
        } else if (e == Etiqueta.NUM_INT) {
            moverse();
        } else if (e == Etiqueta.NUM_DEC) {
            moverse();
        } else if (e == Etiqueta.CADENA) {
            moverse();
        } else if (e == Etiqueta.V) {
            moverse();
        } else if (e == Etiqueta.F) {
            moverse();
        } else {
            throw new ManejadorError(preanalisis.linea, preanalisis.columna, "SINTACTICO", 
                                     "Error de expresion cerca de la palabra: '" + preanalisis.toString() + "' (" + Etiqueta.obtenerNombre(preanalisis.etiqueta) + ")", 
                                     lexico.obtenerTextoLinea(preanalisis.linea));
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