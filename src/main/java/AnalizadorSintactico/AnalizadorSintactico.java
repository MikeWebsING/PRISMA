package AnalizadorSintactico;

import AnalizadorLexico.*;
import AnalizadorSemantico.Semantico;
import java.io.*;

public class AnalizadorSintactico {
    private Semantico semantico;
    private AnalizadorLexico lexico;
    private SimboloLexico preanalisis;
    private Entorno entornoActual = null;

    public AnalizadorSintactico(AnalizadorLexico analizadorLexico) throws IOException {
        lexico = analizadorLexico;
        semantico = new Semantico();
        avanzar();
    }

    private void avanzar() throws IOException {
        preanalisis = lexico.obtenerSiguienteToken();
        if (preanalisis.etiqueta == Etiqueta.ERROR) {
            throw new ManejadorError(preanalisis.linea, preanalisis.columna, "LEXICO", 
                                     preanalisis.toString(), lexico.obtenerTextoLinea(preanalisis.linea));
        }
    }

    private void emparejar(int etiquetaEsperada) throws IOException {
        if (preanalisis.etiqueta == etiquetaEsperada) {
            avanzar();
        } else {
            throw new ManejadorError(preanalisis.linea, preanalisis.columna, "SINTACTICO",
                    "Se esperaba: " + Etiqueta.obtenerNombre(etiquetaEsperada) + 
                    " pero se encontro: '" + preanalisis.toString() + "'",
                    lexico.obtenerTextoLinea(preanalisis.linea));
        }
    }

    public void iniciarAnalisis() throws IOException {
        entornoActual = new Entorno(null);
        procesoPrograma();
    }

    public Semantico obtenerSemantico() {
        return semantico;
    }

    private void procesoPrograma() throws IOException {
        emparejar(Etiqueta.MODULO);
        emparejar(Etiqueta.IDENTIFICADOR);
        emparejar(Etiqueta.VARIABLES);
        declaracionesGlobales();
        emparejar(Etiqueta.FIN_VARIABLES);
        declaracionesFunciones();
        emparejar(Etiqueta.PRINCIPAL);

        Entorno entornoPrevio = entornoActual;
        entornoActual = new Entorno(entornoActual);

        bloqueInstrucciones();

        emparejar(Etiqueta.FIN_PRINCIPAL);
        entornoActual = entornoPrevio;
        emparejar(Etiqueta.FIN_MODULO);
    }

    private void declaracionesGlobales() throws IOException {
        while (esUnTipoDato(preanalisis.etiqueta)) {
            declararVariable();
        }
    }

    private void declaracionesFunciones() throws IOException {
        while (preanalisis.etiqueta == Etiqueta.FUNCION) {
            emparejar(Etiqueta.FUNCION);
            int tipoFuncion = preanalisis.etiqueta;
            procesarTipo();
            String nombreFuncion = ((Palabra) preanalisis).lexema;
            emparejar(Etiqueta.IDENTIFICADOR);

            entornoActual.agregar(nombreFuncion, new Simbolo(tipoFuncion, nombreFuncion));

            Entorno entornoPrevio = entornoActual;
            entornoActual = new Entorno(entornoActual);

            emparejar('(');
            if (esUnTipoDato(preanalisis.etiqueta)) {
                procesarParametros();
            }
            emparejar(')');

            bloqueInstrucciones();

            emparejar(Etiqueta.FIN_FUNCION);
            entornoActual = entornoPrevio;
        }
    }

    private void procesarParametros() throws IOException {
        int tipoParametro = preanalisis.etiqueta;
        procesarTipo();
        String idParametro = ((Palabra) preanalisis).lexema;
        emparejar(Etiqueta.IDENTIFICADOR);
        entornoActual.agregar(idParametro, new Simbolo(tipoParametro, idParametro));

        if (preanalisis.etiqueta == ',') {
            emparejar(',');
            procesarParametros();
        }
    }

    private void declararVariable() throws IOException {
        int tipoV = preanalisis.etiqueta;
        procesarTipo();

        if (!(preanalisis instanceof Palabra)) {
            throw new ManejadorError(preanalisis.linea, preanalisis.columna, "SINTACTICO", 
                                     "Identificador no valido.", lexico.obtenerTextoLinea(preanalisis.linea));
        }

        String nombreV = ((Palabra) preanalisis).lexema;
        int linV = preanalisis.linea;
        int colV = preanalisis.columna;
        emparejar(Etiqueta.IDENTIFICADOR);

        entornoActual.agregar(nombreV, new Simbolo(tipoV, nombreV));
        semantico.realizarDeclaracion(nombreV, tipoV, linV, colV);

        if (preanalisis.etiqueta == '=') {
            emparejar('=');
            expresionLogica();
            semantico.realizarAsignacion(nombreV, linV, colV);
        }
    }

    private void procesarTipo() throws IOException {
        if (esUnTipoDato(preanalisis.etiqueta)) {
            avanzar();
        } else {
            throw new ManejadorError(preanalisis.linea, preanalisis.columna, "SINTACTICO", 
                                     "Tipo de dato invalidado.", 
                                     lexico.obtenerTextoLinea(preanalisis.linea));
        }
    }

    private boolean esUnTipoDato(int etiqueta) {
        return (etiqueta == Etiqueta.ENTERO || etiqueta == Etiqueta.DECIMAL ||
                etiqueta == Etiqueta.TEXTO || etiqueta == Etiqueta.BOOLEANO || etiqueta == Etiqueta.VACIO);
    }

    private void bloqueInstrucciones() throws IOException {
        while (preanalisis.etiqueta != Etiqueta.FIN_PRINCIPAL &&
                preanalisis.etiqueta != Etiqueta.FIN_SI &&
                preanalisis.etiqueta != Etiqueta.FIN_PARA &&
                preanalisis.etiqueta != Etiqueta.FIN_MIENTRAS &&
                preanalisis.etiqueta != Etiqueta.SINO &&
                preanalisis.etiqueta != Etiqueta.FIN_FUNCION &&
                preanalisis.etiqueta != Etiqueta.FIN_ARCHIVO) {
            instruccionSimple();
        }
    }

    private void instruccionSimple() throws IOException {
        int e = preanalisis.etiqueta;
        if (e == Etiqueta.SI) {
            procesoSi();
        } else if (e == Etiqueta.PARA) {
            procesoPara();
        } else if (e == Etiqueta.MIENTRAS) {
            procesoMientras();
        } else if (e == Etiqueta.IMPRIME) {
            procesoImprimir();
        } else if (e == Etiqueta.LEER) {
            procesoLeer();
        } else if (e == Etiqueta.RETORNA) {
            emparejar(Etiqueta.RETORNA);
            expresionLogica();
        } else if (e == Etiqueta.IDENTIFICADOR) {
            gestionAsignacionLlamada();
        } else {
            throw new ManejadorError(preanalisis.linea, preanalisis.columna, "SINTACTICO", 
                                     "Instruccion desconocida: '" + preanalisis.toString() + "'", 
                                     lexico.obtenerTextoLinea(preanalisis.linea));
        }
    }

    private void gestionAsignacionLlamada() throws IOException {
        String idVariable = ((Palabra) preanalisis).lexema;
        int lin = preanalisis.linea;
        int col = preanalisis.columna;
        emparejar(Etiqueta.IDENTIFICADOR);
        if (preanalisis.etiqueta == '=') {
            emparejar('=');
            expresionLogica();
            semantico.realizarAsignacion(idVariable, lin, col);
        } else if (preanalisis.etiqueta == '(') {
            emparejar('(');
            procesarArgumentos();
            emparejar(')');
        }
    }

    private void asignacionDirecta() throws IOException {
        String idVariable = ((Palabra) preanalisis).lexema;
        int lin = preanalisis.linea;
        int col = preanalisis.columna;
        emparejar(Etiqueta.IDENTIFICADOR);
        emparejar('=');
        expresionLogica();
        semantico.realizarAsignacion(idVariable, lin, col);
    }

    private void procesoSi() throws IOException {
        emparejar(Etiqueta.SI);
        emparejar('(');
        expresionLogica();
        emparejar(')');
        emparejar(Etiqueta.ENTONCES);

        Entorno entornoPrevio = entornoActual;
        entornoActual = new Entorno(entornoActual);
        bloqueInstrucciones();
        entornoActual = entornoPrevio;

        if (preanalisis.etiqueta == Etiqueta.SINO) {
            emparejar(Etiqueta.SINO);
            entornoActual = new Entorno(entornoActual);
            bloqueInstrucciones();
            entornoActual = entornoPrevio;
        }
        emparejar(Etiqueta.FIN_SI);
    }

    private void procesoMientras() throws IOException {
        emparejar(Etiqueta.MIENTRAS);
        emparejar('(');
        expresionLogica();
        emparejar(')');

        Entorno entornoPrevio = entornoActual;
        entornoActual = new Entorno(entornoActual);
        bloqueInstrucciones();
        entornoActual = entornoPrevio;

        emparejar(Etiqueta.FIN_MIENTRAS);
    }

    private void procesoImprimir() throws IOException {
        emparejar(Etiqueta.IMPRIME);
        emparejar('(');
        expresionLogica();
        emparejar(')');
    }

    private void procesoLeer() throws IOException {
        emparejar(Etiqueta.LEER);
        emparejar('(');
        String idVariable = ((Palabra) preanalisis).lexema;
        int lin = preanalisis.linea;
        int col = preanalisis.columna;
        emparejar(Etiqueta.IDENTIFICADOR);
        emparejar(')');
        semantico.realizarLectura(idVariable, lin, col);
    }

    private void procesoPara() throws IOException {
        emparejar(Etiqueta.PARA);
        emparejar('(');
        asignacionDirecta();
        emparejar(',');
        expresionLogica();
        emparejar(',');
        asignacionDirecta();
        emparejar(')');

        Entorno entornoPrevio = entornoActual;
        entornoActual = new Entorno(entornoActual);
        bloqueInstrucciones();
        entornoActual = entornoPrevio;

        emparejar(Etiqueta.FIN_PARA);
    }

    private void expresionLogica() throws IOException {
        terminoLogico();
        while (preanalisis.etiqueta == Etiqueta.O) {
            emparejar(Etiqueta.O);
            terminoLogico();
        }
    }

    private void terminoLogico() throws IOException {
        factorLogico();
        while (preanalisis.etiqueta == Etiqueta.Y) {
            emparejar(Etiqueta.Y);
            factorLogico();
        }
    }

    private void factorLogico() throws IOException {
        if (preanalisis.etiqueta == Etiqueta.NO) {
            emparejar(Etiqueta.NO);
            factorLogico();
        } else {
            expresionRelacional();
        }
    }

    private void expresionRelacional() throws IOException {
        expresionAritmetica();
        while (preanalisis.etiqueta == '<' || preanalisis.etiqueta == '>' ||
                preanalisis.etiqueta == Etiqueta.MAYOR_IGUAL || preanalisis.etiqueta == Etiqueta.MENOR_IGUAL ||
                preanalisis.etiqueta == Etiqueta.IGUALDAD || preanalisis.etiqueta == Etiqueta.DIFERENTE) {
            avanzar();
            expresionAritmetica();
        }
    }

    private void expresionAritmetica() throws IOException {
        terminoAritmetico();
        while (preanalisis.etiqueta == '+' || preanalisis.etiqueta == '-') {
            avanzar();
            terminoAritmetico();
        }
    }

    private void terminoAritmetico() throws IOException {
        expresionUnaria();
        while (preanalisis.etiqueta == '*' || preanalisis.etiqueta == '/' || preanalisis.etiqueta == '%') {
            avanzar();
            expresionUnaria();
        }
    }

    private void expresionUnaria() throws IOException {
        if (preanalisis.etiqueta == '-') {
            avanzar();
            expresionUnaria();
        } else {
            factorAritmetico();
        }
    }

    private void factorAritmetico() throws IOException {
        int e = preanalisis.etiqueta;
        if (e == '(') {
            emparejar('(');
            expresionLogica();
            emparejar(')');
        } else if (e == Etiqueta.IDENTIFICADOR) {
            emparejar(Etiqueta.IDENTIFICADOR);
            if (preanalisis.etiqueta == '(') {
                emparejar('(');
                procesarArgumentos();
                emparejar(')');
            }
        } else if (e == Etiqueta.NUMERO_ENTERO) {
            avanzar();
        } else if (e == Etiqueta.NUMERO_DECIMAL) {
            avanzar();
        } else if (e == Etiqueta.CADENA) {
            avanzar();
        } else if (e == Etiqueta.VERDADERO) {
            avanzar();
        } else if (e == Etiqueta.FALSO) {
            avanzar();
        } else {
            throw new ManejadorError(preanalisis.linea, preanalisis.columna, "SINTACTICO", 
                                     "Error en expresion.", 
                                     lexico.obtenerTextoLinea(preanalisis.linea));
        }
    }

    private void procesarArgumentos() throws IOException {
        if (preanalisis.etiqueta != ')') {
            expresionLogica();
            while (preanalisis.etiqueta == ',') {
                emparejar(',');
                expresionLogica();
            }
        }
    }
}