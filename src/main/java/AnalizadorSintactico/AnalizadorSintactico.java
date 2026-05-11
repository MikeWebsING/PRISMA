package AnalizadorSintactico;

import AnalizadorLexico.*;
import AnalizadorSemantico.Semantico;
import AnalizadorSemantico.SimboloS;
import java.io.*;

public class AnalizadorSintactico {
    private Semantico semantico;
    private AnalizadorLexico lexico;
    private SimboloLexico preanalisis;
    private char[] tipoFuncionGlobal = {'V','A','C','I','O'};
    private final char[] BOOLEANO = {'B','O','O','L','E','A','N','O'};
    private final char[] ENTERO = {'E','N','T','E','R','O'};
    private final char[] DECIMAL = {'D','E','C','I','M','A','L'};
    private final char[] TEXTO = {'T','E','X','T','O'};
    private final char[] DESCONOCIDO = {'D','E','S','C','O','N','O','C','I','D','O'};

    public AnalizadorSintactico(AnalizadorLexico analizadorLexico) throws IOException {
        lexico = analizadorLexico;
        semantico = new Semantico();
        avanzar();
    }

    private void avanzar() throws IOException {
        preanalisis = lexico.obtenerSiguienteToken();
        if (preanalisis.etiqueta == Etiqueta.ERROR) {
            char[] lex = ((Palabra) preanalisis).getLexema();
            if (lex == null) lex = new char[]{(char) preanalisis.etiqueta};
            throw new ManejadorError(preanalisis.linea, preanalisis.columna, new char[]{'L','E','X','I','C','O'}, 
                                     lex, lexico.obtenerTextoLinea(preanalisis.linea));
        }
    }

    private char[] concatenar(char[]... partes) {
        int tamano = 0;
        for (int i = 0; i < partes.length; i++) {
            if (partes[i] != null) tamano += partes[i].length;
        }
        char[] resultado = new char[tamano];
        int indice = 0;
        for (int i = 0; i < partes.length; i++) {
            if (partes[i] != null) {
                for (int j = 0; j < partes[i].length; j++) {
                    resultado[indice++] = partes[i][j];
                }
            }
        }
        return resultado;
    }

    private void emparejar(int etiquetaEsperada) throws IOException {
        if (preanalisis.etiqueta == etiquetaEsperada) {
            avanzar();
        } else {
            char[] nombreEsp = Etiqueta.obtener(etiquetaEsperada);
            char[] lexema;
            if (preanalisis instanceof Palabra) {
                lexema = ((Palabra) preanalisis).getLexema();
            } else {
                lexema = new char[]{(char) preanalisis.etiqueta};
            }
            if (lexema == null) lexema = new char[0];
            char[] mensaje = concatenar(new char[]{'S','e',' ','e','s','p','e','r','a','b','a',':',' '}, nombreEsp, new char[]{' ','p','e','r','o',' ','s','e',' ','e','n','c','o','n','t','r','o',':',' ','\''}, lexema, new char[]{'\''});
            throw new ManejadorError(preanalisis.linea, preanalisis.columna, new char[]{'S','I','N','T','A','C','T','I','C','O'},
                    mensaje, lexico.obtenerTextoLinea(preanalisis.linea));
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
            char[] tipoRetorno = Etiqueta.obtener(etiquetaTipoRetorno);
            procesarTipo();
            char[] nombreFuncion = ((Palabra) preanalisis).getLexema();
            emparejar(Etiqueta.IDENTIFICADOR);
            emparejar('(');
            char[][] firma = new char[0][0];
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

    private char[][] procesarParametros() throws IOException {
        int tipoParametro = preanalisis.etiqueta;
        char[] nombreTipo = Etiqueta.obtener(tipoParametro);
        procesarTipo();
        char[] idParametro = ((Palabra) preanalisis).getLexema();
        int linP = preanalisis.linea;
        int colP = preanalisis.columna;
        emparejar(Etiqueta.IDENTIFICADOR);
        semantico.realizarDeclaracion(idParametro, tipoParametro, linP, colP);
        semantico.realizarAsignacion(idParametro, linP, colP);
        if (preanalisis.etiqueta == ',') {
            emparejar(',');
            char[][] resto = procesarParametros();
            char[][] firmaCompleta = new char[resto.length + 1][];
            firmaCompleta[0] = nombreTipo;
            for (int i = 0; i < resto.length; i++) {
                firmaCompleta[i + 1] = resto[i];
            }
            return firmaCompleta;
        }
        return new char[][]{nombreTipo};
    }

    private void declararVariable() throws IOException {
        int tipoV = preanalisis.etiqueta;
        procesarTipo();
        if (!(preanalisis instanceof Palabra)) {
            throw new ManejadorError(preanalisis.linea, preanalisis.columna, new char[]{'S','I','N','T','A','C','T','I','C','O'}, 
                                     new char[]{'I','d','e','n','t','i','f','i','c','a','d','o','r',' ','n','o',' ','v','a','l','i','d','o'}, lexico.obtenerTextoLinea(preanalisis.linea));
        }
        char[] nombreV = ((Palabra) preanalisis).getLexema();
        int linV = preanalisis.linea;
        int colV = preanalisis.columna;
        emparejar(Etiqueta.IDENTIFICADOR);
        semantico.realizarDeclaracion(nombreV, tipoV, linV, colV);
        if (preanalisis.etiqueta == '=') {
            emparejar('=');
            char[] tipoValor = expresionLogica();
            semantico.verificarCompatibilidad(nombreV, tipoValor, linV, colV);
            semantico.realizarAsignacion(nombreV, linV, colV);
        }
    }

    private void procesarTipo() throws IOException {
        if (esUnTipoDato(preanalisis.etiqueta)) {
            avanzar();
        } else {
            throw new ManejadorError(preanalisis.linea, preanalisis.columna, new char[]{'S','I','N','T','A','C','T','I','C','O'}, 
                                     new char[]{'T','i','p','o',' ','d','e',' ','d','a','t','o',' ','i','n','v','a','l','i','d','o'}, 
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
            char[] lexema;
            if (preanalisis instanceof Palabra) {
                lexema = ((Palabra) preanalisis).getLexema();
            } else {
                lexema = new char[]{(char) preanalisis.etiqueta};
            }
            if (lexema == null) lexema = new char[0];
            char[] mensaje = concatenar(new char[]{'I','n','s','t','r','u','c','c','i','o','n',' ','d','e','s','c','o','n','o','c','i','d','a',':',' ','\''}, lexema, new char[]{'\''});
            throw new ManejadorError(preanalisis.linea, preanalisis.columna, new char[]{'S','I','N','T','A','C','T','I','C','O'}, 
                                     mensaje, 
                                     lexico.obtenerTextoLinea(preanalisis.linea));
        }
    }

    private void gestionAsignacionLlamada() throws IOException {
        char[] idNombre = ((Palabra) preanalisis).getLexema();
        int lin = preanalisis.linea;
        int col = preanalisis.columna;
        emparejar(Etiqueta.IDENTIFICADOR);
        if (preanalisis.etiqueta == '=') {
            emparejar('=');
            char[] tipoValor = expresionLogica();
            semantico.verificarCompatibilidad(idNombre, tipoValor, lin, col);
            semantico.realizarAsignacion(idNombre, lin, col);
        } else if (preanalisis.etiqueta == '(') {
            emparejar('(');
            char[][] firmaArg = procesarArgumentos();
            emparejar(')');
            semantico.validarLlamada(idNombre, firmaArg, lin, col);
        }
    }

    private void asignacionDirecta() throws IOException {
        char[] idVariable = ((Palabra) preanalisis).getLexema();
        int lin = preanalisis.linea;
        int col = preanalisis.columna;
        emparejar(Etiqueta.IDENTIFICADOR);
        emparejar('=');
        char[] tipoValor = expresionLogica();
        semantico.verificarCompatibilidad(idVariable, tipoValor, lin, col);
        semantico.realizarAsignacion(idVariable, lin, col);
    }

    private void procesoRetorna() throws IOException {
        int lin = preanalisis.linea;
        int col = preanalisis.columna;
        emparejar(Etiqueta.RETORNA);
        char[] tipoRet = expresionLogica();
        semantico.validarRetorno(tipoRet, lin, col, tipoFuncionGlobal);
    }

    private void procesoSi() throws IOException {
        int lin = preanalisis.linea;
        int col = preanalisis.columna;
        emparejar(Etiqueta.SI);
        emparejar('(');
        char[] tipoCond = expresionLogica();
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
        char[] tipoCond = expresionLogica();
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
        char[] idVariable = ((Palabra) preanalisis).getLexema();
        int lin = preanalisis.linea;
        int col = preanalisis.columna;
        emparejar(Etiqueta.IDENTIFICADOR);
        emparejar(')');
        semantico.realizarLectura(idVariable, lin, col);
    }

    private void procesoPara() throws IOException {
        emparejar(Etiqueta.PARA);
        emparejar('(');
        char[] idContador = ((Palabra) preanalisis).getLexema();
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

    private char[] expresionLogica() throws IOException {
        int lin = preanalisis.linea;
        int col = preanalisis.columna;
        char[] tipo = terminoLogico();
        while (preanalisis.etiqueta == Etiqueta.O) {
            semantico.validarOperando(tipo, new char[]{'L','O','G','I','C','O'}, lin, col, new char[]{'O'});
            emparejar(Etiqueta.O);
            char[] tipo2 = terminoLogico();
            semantico.validarOperando(tipo2, new char[]{'L','O','G','I','C','O'}, lin, col, new char[]{'O'});
            tipo = BOOLEANO;
        }
        return tipo;
    }

    private char[] terminoLogico() throws IOException {
        int lin = preanalisis.linea;
        int col = preanalisis.columna;
        char[] tipo = factorLogico();
        while (preanalisis.etiqueta == Etiqueta.Y) {
            semantico.validarOperando(tipo, new char[]{'L','O','G','I','C','O'}, lin, col, new char[]{'Y'});
            emparejar(Etiqueta.Y);
            char[] tipo2 = factorLogico();
            semantico.validarOperando(tipo2, new char[]{'L','O','G','I','C','O'}, lin, col, new char[]{'Y'});
            tipo = BOOLEANO;
        }
        return tipo;
    }

    private char[] factorLogico() throws IOException {
        if (preanalisis.etiqueta == Etiqueta.NO) {
            emparejar(Etiqueta.NO);
            return factorLogico();
        } else {
            return expresionRelacional();
        }
    }

    private char[] expresionRelacional() throws IOException {
        int lin = preanalisis.linea;
        int col = preanalisis.columna;
        char[] tipo = expresionAritmetica();
        while (preanalisis.etiqueta == '<' || preanalisis.etiqueta == '>' ||
                preanalisis.etiqueta == Etiqueta.MAYOR_IGUAL || preanalisis.etiqueta == Etiqueta.MENOR_IGUAL ||
                preanalisis.etiqueta == Etiqueta.IGUALDAD || preanalisis.etiqueta == Etiqueta.DIFERENTE) {
            char[] op = Etiqueta.obtener(preanalisis.etiqueta);
            semantico.validarOperando(tipo, new char[]{'A','R','I','T','M','E','T','I','C','O'}, lin, col, op);
            avanzar();
            char[] tipo2 = expresionAritmetica();
            semantico.validarOperando(tipo2, new char[]{'A','R','I','T','M','E','T','I','C','O'}, lin, col, op);
            semantico.verificarCompatibilidadBinaria(tipo, tipo2, lin, col, op);
            tipo = BOOLEANO;
        }
        return tipo;
    }

    private char[] expresionAritmetica() throws IOException {
        int lin = preanalisis.linea;
        int col = preanalisis.columna;
        char[] tipo = terminoAritmetico();
        while (preanalisis.etiqueta == '+' || preanalisis.etiqueta == '-') {
            char[] op = (preanalisis.etiqueta == '+') ? new char[]{'+'} : new char[]{'-'};
            semantico.validarOperando(tipo, new char[]{'A','R','I','T','M','E','T','I','C','O'}, lin, col, op);
            avanzar();
            char[] tipo2 = terminoAritmetico();
            semantico.validarOperando(tipo2, new char[]{'A','R','I','T','M','E','T','I','C','O'}, lin, col, op);
            semantico.verificarCompatibilidadBinaria(tipo, tipo2, lin, col, op);
            tipo = semantico.obtenerTipoResultante(tipo, tipo2);
        }
        return tipo;
    }

    private char[] terminoAritmetico() throws IOException {
        int linT = preanalisis.linea;
        int colT = preanalisis.columna;
        char[] tipo = expresionUnaria();
        while (preanalisis.etiqueta == '*' || preanalisis.etiqueta == '/' || preanalisis.etiqueta == '%') {
            int opEt = preanalisis.etiqueta;
            char[] opLex = (opEt == '/') ? new char[]{'D','I','V','I','S','I','O','N'} : (opEt == '%') ? new char[]{'M','O','D','U','L','O'} : new char[]{'M','U','L','T','I','P','L','I','C','A','C','I','O','N'};
            int linOp = preanalisis.linea;
            int colOp = preanalisis.columna;
            semantico.validarOperando(tipo, new char[]{'A','R','I','T','M','E','T','I','C','O'}, linT, colT, opLex);
            avanzar();
            if (preanalisis.etiqueta == Etiqueta.NUMERO_ENTERO || preanalisis.etiqueta == Etiqueta.NUMERO_DECIMAL) {
                double val = ((Numero) preanalisis).getValor();
                semantico.validarDivisionPorCero(linOp, colOp, opLex, val);
            }
            char[] tipo2 = expresionUnaria();
            semantico.validarOperando(tipo2, new char[]{'A','R','I','T','M','E','T','I','C','O'}, linT, colT, opLex);
            semantico.verificarCompatibilidadBinaria(tipo, tipo2, linOp, colOp, opLex);
            tipo = semantico.obtenerTipoResultante(tipo, tipo2);
        }
        return tipo;
    }

    private char[] expresionUnaria() throws IOException {
        if (preanalisis.etiqueta == '-') {
            avanzar();
            return expresionUnaria();
        } else {
            return factorAritmetico();
        }
    }

    private char[] factorAritmetico() throws IOException {
        int e = preanalisis.etiqueta;
        if (e == '(') {
            emparejar('(');
            char[] tipo = expresionLogica();
            emparejar(')');
            return tipo;
        } else if (e == Etiqueta.IDENTIFICADOR) {
            char[] nombreId = ((Palabra) preanalisis).getLexema();
            int linId = preanalisis.linea;
            int colId = preanalisis.columna;
            emparejar(Etiqueta.IDENTIFICADOR);
            if (preanalisis.etiqueta == '(') {
                emparejar('(');
                char[][] firmaArg = procesarArgumentos();
                emparejar(')');
                SimboloS sim = semantico.buscarSimbolo(nombreId);
                semantico.validarLlamada(nombreId, firmaArg, linId, colId);
                return (sim != null) ? sim.tipo : DESCONOCIDO;
            } else {
                SimboloS sim = semantico.buscarSimbolo(nombreId);
                semantico.validarUsoVariable(nombreId, linId, colId);
                return (sim != null) ? sim.tipo : DESCONOCIDO;
            }
        } else if (e == Etiqueta.NUMERO_ENTERO) {
            avanzar();
            return ENTERO;
        } else if (e == Etiqueta.NUMERO_DECIMAL) {
            avanzar();
            return DECIMAL;
        } else if (e == Etiqueta.CADENA) {
            avanzar();
            return TEXTO;
        } else if (e == Etiqueta.VERDADERO) {
            avanzar();
            return BOOLEANO;
        } else if (e == Etiqueta.FALSO) {
            avanzar();
            return BOOLEANO;
        } else {
            char[] lexema;
            if (preanalisis instanceof Palabra) {
                lexema = ((Palabra) preanalisis).getLexema();
            } else {
                lexema = new char[]{(char) preanalisis.etiqueta};
            }
            if (lexema == null) lexema = new char[0];
            char[] mensaje = concatenar(new char[]{'S','e',' ','e','s','p','e','r','a','b','a',' ','u','n',' ','o','p','e','r','a','n','d','o',' ','p','e','r','o',' ','s','e',' ','e','n','c','o','n','t','r','o',':',' ','\''}, lexema, new char[]{'\''});
            throw new ManejadorError(preanalisis.linea, preanalisis.columna, new char[]{'S','I','N','T','A','C','T','I','C','O'}, 
                                     mensaje, lexico.obtenerTextoLinea(preanalisis.linea));
        }
    }

    private char[][] procesarArgumentos() throws IOException {
        if (preanalisis.etiqueta != ')') {
            char[] tipo = expresionLogica();
            char[][] tiposArgs = new char[1][];
            tiposArgs[0] = tipo;
            int contador = 1;
            while (preanalisis.etiqueta == ',') {
                emparejar(',');
                char[] tipoSiguiente = expresionLogica();
                char[][] nuevoTiposArgs = new char[contador + 1][];
                for (int i = 0; i < contador; i++) nuevoTiposArgs[i] = tiposArgs[i];
                nuevoTiposArgs[contador] = tipoSiguiente;
                tiposArgs = nuevoTiposArgs;
                contador++;
            }
            return tiposArgs;
        }
        return new char[0][0];
    }
}