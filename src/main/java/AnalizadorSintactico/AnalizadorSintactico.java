package AnalizadorSintactico;

import AnalizadorLexico.*;
import AnalizadorSemantico.Semantico;
import AnalizadorSemantico.SimboloS;
import java.io.*;

public class AnalizadorSintactico {
    private Semantico semantico;
    private AnalizadorLexico lexico;
    private SimboloLexico preanalisis;
    private String tipoFuncionGlobal = "VACIO";

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
        bloqueInstrucciones();
        emparejar(Etiqueta.FIN_PRINCIPAL);
        emparejar(Etiqueta.FIN_MODULO);
    }

    private void declaracionesGlobales() throws IOException {
        while (esUnTipoDato(preanalisis.etiqueta)) {
            declararVariable();
        }
    }

    private void declaracionesFunciones() throws IOException {
        while (preanalisis.etiqueta == Etiqueta.FUNCION) {
            int linF = preanalisis.linea;
            int colF = preanalisis.columna;
            emparejar(Etiqueta.FUNCION);
            int etiquetaTipoRetorno = preanalisis.etiqueta;
            String tipoRetorno = Etiqueta.obtenerNombre(etiquetaTipoRetorno);
            procesarTipo();
            String nombreFuncion = ((Palabra) preanalisis).lexema;
            emparejar(Etiqueta.IDENTIFICADOR);
            emparejar('(');
            String firma = "";
            if (esUnTipoDato(preanalisis.etiqueta)) {
                firma = procesarParametros();
            }
            emparejar(')');
            semantico.registrarFuncion(linF, colF, nombreFuncion, tipoRetorno, firma);
            semantico.setDentroDeFuncion(true, tipoRetorno);
            tipoFuncionGlobal = tipoRetorno;
            semantico.iniciarFuncion();
            bloqueInstrucciones();
            semantico.finalizarFuncion(nombreFuncion, tipoRetorno, preanalisis.linea, preanalisis.columna);
            semantico.setDentroDeFuncion(false, null);
            emparejar(Etiqueta.FIN_FUNCION);
        }
    }

    private String procesarParametros() throws IOException {
        int tipoParametro = preanalisis.etiqueta;
        String nombreTipo = Etiqueta.obtenerNombre(tipoParametro);
        procesarTipo();
        String idParametro = ((Palabra) preanalisis).lexema;
        int linP = preanalisis.linea;
        int colP = preanalisis.columna;
        emparejar(Etiqueta.IDENTIFICADOR);
        semantico.realizarDeclaracion(idParametro, tipoParametro, linP, colP);
        semantico.realizarAsignacion(idParametro, linP, colP);
        if (preanalisis.etiqueta == ',') {
            emparejar(',');
            return nombreTipo + "," + procesarParametros();
        }
        return nombreTipo;
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
        semantico.realizarDeclaracion(nombreV, tipoV, linV, colV);
        if (preanalisis.etiqueta == '=') {
            emparejar('=');
            String tipoValor = expresionLogica();
            semantico.verificarCompatibilidad(nombreV, tipoValor, linV, colV);
            semantico.realizarAsignacion(nombreV, linV, colV);
        }
    }

    private void procesarTipo() throws IOException {
        if (esUnTipoDato(preanalisis.etiqueta)) {
            avanzar();
        } else {
            throw new ManejadorError(preanalisis.linea, preanalisis.columna, "SINTACTICO", 
                                     "Tipo de dato invalido.", 
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
            procesoRetorna();
        } else if (e == Etiqueta.IDENTIFICADOR) {
            gestionAsignacionLlamada();
        } else {
            throw new ManejadorError(preanalisis.linea, preanalisis.columna, "SINTACTICO", 
                                     "Instruccion desconocida: '" + preanalisis.toString() + "'", 
                                     lexico.obtenerTextoLinea(preanalisis.linea));
        }
    }

    private void gestionAsignacionLlamada() throws IOException {
        String idNombre = ((Palabra) preanalisis).lexema;
        int lin = preanalisis.linea;
        int col = preanalisis.columna;
        emparejar(Etiqueta.IDENTIFICADOR);
        if (preanalisis.etiqueta == '=') {
            emparejar('=');
            String tipoValor = expresionLogica();
            semantico.verificarCompatibilidad(idNombre, tipoValor, lin, col);
            semantico.realizarAsignacion(idNombre, lin, col);
        } else if (preanalisis.etiqueta == '(') {
            emparejar('(');
            String firmaArg = procesarArgumentos();
            emparejar(')');
            semantico.validarLlamada(idNombre, firmaArg, lin, col);
        }
    }

    private void asignacionDirecta() throws IOException {
        String idVariable = ((Palabra) preanalisis).lexema;
        int lin = preanalisis.linea;
        int col = preanalisis.columna;
        emparejar(Etiqueta.IDENTIFICADOR);
        emparejar('=');
        String tipoValor = expresionLogica();
        semantico.verificarCompatibilidad(idVariable, tipoValor, lin, col);
        semantico.realizarAsignacion(idVariable, lin, col);
    }

    private void procesoRetorna() throws IOException {
        int lin = preanalisis.linea;
        int col = preanalisis.columna;
        emparejar(Etiqueta.RETORNA);
        String tipoRet = expresionLogica();
        semantico.validarRetorno(tipoRet, lin, col, tipoFuncionGlobal);
    }

    private void procesoSi() throws IOException {
        int lin = preanalisis.linea;
        int col = preanalisis.columna;
        emparejar(Etiqueta.SI);
        emparejar('(');
        String tipoCond = expresionLogica();
        semantico.validarTipoCondicion(tipoCond, lin, col);
        emparejar(')');
        emparejar(Etiqueta.ENTONCES);
        bloqueInstrucciones();
        if (preanalisis.etiqueta == Etiqueta.SINO) {
            emparejar(Etiqueta.SINO);
            bloqueInstrucciones();
        }
        emparejar(Etiqueta.FIN_SI);
    }

    private void procesoMientras() throws IOException {
        int lin = preanalisis.linea;
        int col = preanalisis.columna;
        emparejar(Etiqueta.MIENTRAS);
        emparejar('(');
        String tipoCond = expresionLogica();
        semantico.validarTipoCondicion(tipoCond, lin, col);
        emparejar(')');
        bloqueInstrucciones();
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
        String idContador = ((Palabra) preanalisis).lexema;
        int linC = preanalisis.linea;
        int colC = preanalisis.columna;
        asignacionDirecta();
        semantico.validarContadorPara(idContador, linC, colC);
        emparejar(',');
        expresionLogica();
        emparejar(',');
        asignacionDirecta();
        emparejar(')');
        bloqueInstrucciones();
        emparejar(Etiqueta.FIN_PARA);
    }

    private String expresionLogica() throws IOException {
        int lin = preanalisis.linea;
        int col = preanalisis.columna;
        String tipo = terminoLogico();
        while (preanalisis.etiqueta == Etiqueta.O) {
            semantico.validarOperando(tipo, "LOGICO", lin, col, "O");
            emparejar(Etiqueta.O);
            String tipo2 = terminoLogico();
            semantico.validarOperando(tipo2, "LOGICO", lin, col, "O");
            tipo = "BOOLEANO";
        }
        return tipo;
    }

    private String terminoLogico() throws IOException {
        int lin = preanalisis.linea;
        int col = preanalisis.columna;
        String tipo = factorLogico();
        while (preanalisis.etiqueta == Etiqueta.Y) {
            semantico.validarOperando(tipo, "LOGICO", lin, col, "Y");
            emparejar(Etiqueta.Y);
            String tipo2 = factorLogico();
            semantico.validarOperando(tipo2, "LOGICO", lin, col, "Y");
            tipo = "BOOLEANO";
        }
        return tipo;
    }

    private String factorLogico() throws IOException {
        if (preanalisis.etiqueta == Etiqueta.NO) {
            emparejar(Etiqueta.NO);
            return factorLogico();
        } else {
            return expresionRelacional();
        }
    }

    private String expresionRelacional() throws IOException {
        int lin = preanalisis.linea;
        int col = preanalisis.columna;
        String tipo = expresionAritmetica();
        while (preanalisis.etiqueta == '<' || preanalisis.etiqueta == '>' ||
                preanalisis.etiqueta == Etiqueta.MAYOR_IGUAL || preanalisis.etiqueta == Etiqueta.MENOR_IGUAL ||
                preanalisis.etiqueta == Etiqueta.IGUALDAD || preanalisis.etiqueta == Etiqueta.DIFERENTE) {
            String op = Etiqueta.obtenerNombre(preanalisis.etiqueta);
            semantico.validarOperando(tipo, "ARITMETICO", lin, col, op);
            avanzar();
            String tipo2 = expresionAritmetica();
            semantico.validarOperando(tipo2, "ARITMETICO", lin, col, op);
            semantico.verificarCompatibilidadBinaria(tipo, tipo2, lin, col, op);
            tipo = "BOOLEANO";
        }
        return tipo;
    }

    private String expresionAritmetica() throws IOException {
        int lin = preanalisis.linea;
        int col = preanalisis.columna;
        String tipo = terminoAritmetico();
        while (preanalisis.etiqueta == '+' || preanalisis.etiqueta == '-') {
            String op = (preanalisis.etiqueta == '+') ? "+" : "-";
            semantico.validarOperando(tipo, "ARITMETICO", lin, col, op);
            avanzar();
            String tipo2 = terminoAritmetico();
            semantico.validarOperando(tipo2, "ARITMETICO", lin, col, op);
            semantico.verificarCompatibilidadBinaria(tipo, tipo2, lin, col, op);
            tipo = semantico.obtenerTipoResultante(tipo, tipo2);
        }
        return tipo;
    }

    private String terminoAritmetico() throws IOException {
        int linT = preanalisis.linea;
        int colT = preanalisis.columna;
        String tipo = expresionUnaria();
        while (preanalisis.etiqueta == '*' || preanalisis.etiqueta == '/' || preanalisis.etiqueta == '%') {
            int opEt = preanalisis.etiqueta;
            String opLex = (opEt == '/') ? "DIVISION" : (opEt == '%') ? "MODULO" : "MULTIPLICACION";
            int linOp = preanalisis.linea;
            int colOp = preanalisis.columna;
            semantico.validarOperando(tipo, "ARITMETICO", linT, colT, opLex);
            avanzar();
            if (preanalisis.etiqueta == Etiqueta.NUMERO_ENTERO || preanalisis.etiqueta == Etiqueta.NUMERO_DECIMAL) {
                double val = ((Numero) preanalisis).valor;
                semantico.validarDivisionPorCero(linOp, colOp, opLex, val);
            }
            String tipo2 = expresionUnaria();
            semantico.validarOperando(tipo2, "ARITMETICO", linT, colT, opLex);
            semantico.verificarCompatibilidadBinaria(tipo, tipo2, linOp, colOp, opLex);
            tipo = semantico.obtenerTipoResultante(tipo, tipo2);
        }
        return tipo;
    }

    private String expresionUnaria() throws IOException {
        if (preanalisis.etiqueta == '-') {
            avanzar();
            return expresionUnaria();
        } else {
            return factorAritmetico();
        }
    }

    private String factorAritmetico() throws IOException {
        int e = preanalisis.etiqueta;
        if (e == '(') {
            emparejar('(');
            String tipo = expresionLogica();
            emparejar(')');
            return tipo;
        } else if (e == Etiqueta.IDENTIFICADOR) {
            String nombreId = ((Palabra) preanalisis).lexema;
            int linId = preanalisis.linea;
            int colId = preanalisis.columna;
            emparejar(Etiqueta.IDENTIFICADOR);
            if (preanalisis.etiqueta == '(') {
                emparejar('(');
                String firmaArg = procesarArgumentos();
                emparejar(')');
                SimboloS sim = semantico.buscarSimbolo(nombreId);
                semantico.validarLlamada(nombreId, firmaArg, linId, colId);
                return (sim != null) ? sim.tipo : "DESCONOCIDO";
            } else {
                SimboloS sim = semantico.buscarSimbolo(nombreId);
                semantico.validarUsoVariable(nombreId, linId, colId);
                return (sim != null) ? sim.tipo : "DESCONOCIDO";
            }
        } else if (e == Etiqueta.NUMERO_ENTERO) {
            avanzar();
            return "ENTERO";
        } else if (e == Etiqueta.NUMERO_DECIMAL) {
            avanzar();
            return "DECIMAL";
        } else if (e == Etiqueta.CADENA) {
            avanzar();
            return "TEXTO";
        } else if (e == Etiqueta.VERDADERO) {
            avanzar();
            return "BOOLEANO";
        } else if (e == Etiqueta.FALSO) {
            avanzar();
            return "BOOLEANO";
        } else {
            throw new ManejadorError(preanalisis.linea, preanalisis.columna, "SINTACTICO", 
                                     "Se esperaba un operando pero se encontro: '" + preanalisis.toString() + "'", 
                                     lexico.obtenerTextoLinea(preanalisis.linea));
        }
    }

    private String procesarArgumentos() throws IOException {
        String firma = "";
        if (preanalisis.etiqueta != ')') {
            firma = expresionLogica();
            while (preanalisis.etiqueta == ',') {
                emparejar(',');
                firma += "," + expresionLogica();
            }
        }
        return firma;
    }
}