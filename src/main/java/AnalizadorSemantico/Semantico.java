package AnalizadorSemantico;

import AnalizadorLexico.Etiqueta;

public class Semantico {
    private SimboloS[] tablaSimbolos;
    private int contadorSimbolos = 0;
    
    private char[][] listaErrores;
    private int contadorErrores = 0;
    private boolean retornoDetectado = false;
    private char[] tipoFuncionActual = null;
    private boolean dentroDeFuncion = false;

    private static final char[][] PALABRAS_RESERVADAS = {
        {'M','O','D','U','L','O'}, {'V','A','R','I','A','B','L','E','S'}, {'F','I','N','-','V','A','R','I','A','B','L','E','S'},
        {'P','R','I','N','C','I','P','A','L'}, {'F','I','N','-','P','R','I','N','C','I','P','A','L'},
        {'F','U','N','C','I','O','N'}, {'F','I','N','-','F','U','N','C','I','O','N'},
        {'R','E','T','O','R','N','A'}, {'S','I'}, {'E','N','T','O','N','C','E','S'}, {'S','I','N','O'}, {'F','I','N','-','S','I'},
        {'P','A','R','A'}, {'F','I','N','-','P','A','R','A'}, {'M','I','E','N','T','R','A','S'}, {'F','I','N','-','M','I','E','N','T','R','A','S'},
        {'E','N','T','E','R','O'}, {'D','E','C','I','M','A','L'}, {'T','E','X','T','O'}, {'B','O','O','L','E','A','N','O'}, {'V','A','C','I','O'},
        {'I','M','P','R','I','M','E'}, {'L','E','E','R'}, {'Y'}, {'O'}, {'N','O'}, {'V'}, {'F'}
    };

    public Semantico() {
        tablaSimbolos = new SimboloS[1000];
        listaErrores = new char[200][];
    }

    public void setDentroDeFuncion(boolean estado, char[] tipo) {
        this.dentroDeFuncion = estado;
        this.tipoFuncionActual = tipo;
    }

    public void registrarVariable(int linea, int columna, char[] nombre, char[] tipo, boolean inicializado) {
        for (int i = 0; i < PALABRAS_RESERVADAS.length; i++) {
            if (esIgualIgnorandoMayusculas(PALABRAS_RESERVADAS[i], nombre)) {
                registrarError(linea, columna, new char[]{'E','1','0'}, concatenar(new char[]{'U','s','o',' ','d','e',' ','p','a','l','a','b','r','a',' ','r','e','s','e','r','v','a','d','a',' ','\''}, nombre, new char[]{'\''}));
                return;
            }
        }
        if (buscarSimbolo(nombre) != null) {
            registrarError(linea, columna, new char[]{'E','2'}, concatenar(new char[]{'E','l',' ','i','d','e','n','t','i','f','i','c','a','d','o','r',' ','\''}, nombre, new char[]{'\''}));
        } else {
            if (contadorSimbolos < tablaSimbolos.length) {
                tablaSimbolos[contadorSimbolos++] = new SimboloS(nombre, tipo, inicializado, linea, columna);
            }
        }
    }

    public void registrarFuncion(int linea, int columna, char[] nombre, char[] tipoRetorno, char[][] firma) {
        for (int i = 0; i < PALABRAS_RESERVADAS.length; i++) {
            if (esIgualIgnorandoMayusculas(PALABRAS_RESERVADAS[i], nombre)) {
                registrarError(linea, columna, new char[]{'E','1','0'}, concatenar(new char[]{'U','s','o',' ','d','e',' ','p','a','l','a','b','r','a',' ','r','e','s','e','r','v','a','d','a',' ','\''}, nombre, new char[]{'\''}));
                return;
            }
        }
        if (buscarSimbolo(nombre) != null) {
            registrarError(linea, columna, new char[]{'E','1','2'}, concatenar(new char[]{'L','a',' ','f','u','n','c','i','o','n',' ','\''}, nombre, new char[]{'\''}));
        } else {
            if (contadorSimbolos < tablaSimbolos.length) {
                SimboloS s = new SimboloS(nombre, tipoRetorno, true, linea, columna);
                s.esFuncion = true;
                s.firma = firma;
                tablaSimbolos[contadorSimbolos++] = s;
            }
        }
    }

    public SimboloS buscarSimbolo(char[] nombre) {
        for (int i = 0; i < contadorSimbolos; i++) {
            if (esIgual(tablaSimbolos[i].nombre, nombre)) {
                return tablaSimbolos[i];
            }
        }
        return null;
    }

    public void validarUsoVariable(char[] nombre, int linea, int columna) {
        SimboloS simbolo = buscarSimbolo(nombre);
        if (simbolo == null) {
            registrarError(linea, columna, new char[]{'E','1'}, concatenar(new char[]{'V','a','r','i','a','b','l','e',' ','\''}, nombre, new char[]{'\''}));
        } else if (simbolo.esFuncion) {
            registrarError(linea, columna, new char[]{'E','1'}, concatenar(new char[]{'S','e',' ','i','n','t','e','n','t','a',' ','u','s','a','r',' ','l','a',' ','f','u','n','c','i','o','n',' ','\''}, nombre, new char[]{'\''}));
        } else if (!simbolo.inicializado) {
            registrarError(linea, columna, new char[]{'E','1','3'}, concatenar(new char[]{'V','a','r','i','a','b','l','e',' ','s','i','n',' ','i','n','i','c','i','a','l','i','z','a','r',' ','\''}, nombre, new char[]{'\''}));
        }
    }

    public void validarDivisionPorCero(int linea, int columna, char[] operador, double valor) {
        if ((esIgual(operador, new char[]{'/'}) || esIgual(operador, new char[]{'%'}) || 
             esIgual(operador, new char[]{'D','I','V','I','S','I','O','N'}) || 
             esIgual(operador, new char[]{'M','O','D','U','L','O'})) && valor == 0) {
            registrarError(linea, columna, new char[]{'E','1','1'}, concatenar(new char[]{'D','i','v','i','s','i','o','n',' ','p','o','r',' ','c','e','r','o',' ','\''}, operador, new char[]{'\''}));
        }
    }

    public void validarTipoCondicion(char[] tipo, int linea, int columna) {
        if (esIgual(tipo, new char[]{'D','E','S','C','O','N','O','C','I','D','O'})) return;
        if (!esIgual(tipo, new char[]{'B','O','O','L','E','A','N','O'})) {
            registrarError(linea, columna, new char[]{'E','4'}, concatenar(new char[]{'L','a',' ','c','o','n','d','i','c','i','o','n',' ','d','e','b','e',' ','s','e','r',' ','B','O','O','L','E','A','N','O','.',' ','S','e',' ','e','n','c','o','n','t','r','o',':',' '}, tipo));
        }
    }

    public void verificarCompatibilidad(char[] nombre, char[] tipoOrigen, int linea, int columna) {
        SimboloS simbolo = buscarSimbolo(nombre);
        if (simbolo == null) {
            registrarError(linea, columna, new char[]{'E','1'}, concatenar(new char[]{'V','a','r','i','a','b','l','e',' ','\''}, nombre, new char[]{'\''}));
            return;
        }
        if (simbolo.esFuncion) {
            registrarError(linea, columna, new char[]{'E','1'}, concatenar(new char[]{'E','l',' ','i','d','e','n','t','i','f','i','c','a','d','o','r',' ','\''}, nombre, new char[]{'\''}));
            return;
        }
        if (!esIgual(tipoOrigen, new char[]{'D','E','S','C','O','N','O','C','I','D','O'})) {
            if (!esIgual(simbolo.tipo, tipoOrigen)) {
                registrarError(linea, columna, new char[]{'E','3'}, concatenar(new char[]{'I','n','c','o','m','p','a','t','i','b','i','l','i','d','a','d',' ','d','e',' ','t','i','p','o','s',' ','-',' '}, tipoOrigen, new char[]{' ','y',' '}, simbolo.tipo));
            }
        }
    }

    public void validarOperando(char[] tipoActual, char[] categoriaEsperada, int linea, int columna, char[] operador) {
        if (esIgual(tipoActual, new char[]{'D','E','S','C','O','N','O','C','I','D','O'})) return;

        if (esIgual(categoriaEsperada, new char[]{'A','R','I','T','M','E','T','I','C','O'})) {
            if (!esIgual(tipoActual, new char[]{'E','N','T','E','R','O'}) && !esIgual(tipoActual, new char[]{'D','E','C','I','M','A','L'})) {
                registrarError(linea, columna, new char[]{'E','3'}, concatenar(new char[]{'O','p','e','r','a','n','d','o',' ','i','n','c','o','r','r','e','c','t','o',' ','p','a','r','a',' ','\''}, operador, new char[]{'\''}));
            }
        } else if (esIgual(categoriaEsperada, new char[]{'L','O','G','I','C','O'})) {
            if (!esIgual(tipoActual, new char[]{'B','O','O','L','E','A','N','O'})) {
                registrarError(linea, columna, new char[]{'E','3'}, concatenar(new char[]{'O','p','e','r','a','n','d','o',' ','l','o','g','i','c','o',' ','i','n','c','o','r','r','e','c','t','o',' ','\''}, operador, new char[]{'\''}));
            }
        }
    }

    public void verificarCompatibilidadBinaria(char[] t1, char[] t2, int lin, int col, char[] op) {
        if (esIgual(t1, new char[]{'D','E','S','C','O','N','O','C','I','D','O'}) || esIgual(t2, new char[]{'D','E','S','C','O','N','O','C','I','D','O'})) return;
        if (!esIgual(t1, t2)) {
            registrarError(lin, col, new char[]{'E','3'}, concatenar(new char[]{'T','i','p','o','s',' ','i','n','c','o','m','p','a','t','i','b','l','e','s',' ','p','a','r','a',' ','\''}, op, new char[]{'\''}));
        }
    }

    public char[] obtenerTipoResultante(char[] t1, char[] t2) {
        if (esIgual(t1, t2)) return t1;
        return new char[]{'D','E','S','C','O','N','O','C','I','D','O'};
    }

    public void validarContadorPara(char[] nombre, int linea, int columna) {
        SimboloS sim = buscarSimbolo(nombre);
        if (sim != null && !esIgual(sim.tipo, new char[]{'E','N','T','E','R','O'}) && !esIgual(sim.tipo, new char[]{'D','E','C','I','M','A','L'})) {
            registrarError(linea, columna, new char[]{'E','9'}, concatenar(new char[]{'L','a',' ','v','a','r','i','a','b','l','e',' ','d','e',' ','c','o','n','t','r','o','l',' ','\''}, nombre, new char[]{'\''}));
        }
    }

    public void validarLlamada(char[] nombre, char[][] tiposArgs, int linea, int columna) {
        SimboloS sim = buscarSimbolo(nombre);
        if (sim == null) {
            registrarError(linea, columna, new char[]{'E','5'}, concatenar(new char[]{'F','u','n','c','i','o','n',' ','\''}, nombre, new char[]{'\''}));
            return;
        }
        if (!sim.esFuncion) {
            registrarError(linea, columna, new char[]{'E','1'}, concatenar(new char[]{'\''}, nombre, new char[]{'\''}));
            return;
        }
        
        char[][] paramsEsperados = sim.firma;
        
        if (paramsEsperados.length != tiposArgs.length) {
            registrarError(linea, columna, new char[]{'E','6'}, concatenar(new char[]{'N','u','m','e','r','o',' ','d','e',' ','a','r','g','u','m','e','n','t','o','s',' ','i','n','c','o','r','r','e','c','t','o',' ','p','a','r','a',' ','\''}, nombre, new char[]{'\''}));
            return;
        }
        
        for (int i = 0; i < paramsEsperados.length; i++) {
            if (!esIgual(paramsEsperados[i], tiposArgs[i])) {
                registrarError(linea, columna, new char[]{'E','7'}, concatenar(new char[]{'T','i','p','o',' ','d','e',' ','a','r','g','u','m','e','n','t','o',' ','i','n','v','a','l','i','d','o',' ','p','a','r','a',' ','\''}, nombre, new char[]{'\''}));
            }
        }
    }

    public void realizarDeclaracion(char[] nombre, int etiquetaTipo, int linea, int columna) {
        char[] nombreTipo = Etiqueta.obtenerNombre(etiquetaTipo);
        if (esIgual(nombreTipo, new char[]{'V','A','C','I','O'})) {
            registrarError(linea, columna, new char[]{'E','1','4'}, new char[]{'N','o',' ','s','e',' ','p','e','r','m','i','t','e','n',' ','v','a','r','i','a','b','l','e','s',' ','d','e',' ','t','i','p','o',' ','V','A','C','I','O','.'});
            return;
        }
        registrarVariable(linea, columna, nombre, nombreTipo, false);
    }

    public void realizarAsignacion(char[] nombre, int linea, int columna) {
        SimboloS simbolo = buscarSimbolo(nombre);
        if (simbolo == null) {
            registrarError(linea, columna, new char[]{'E','1'}, concatenar(new char[]{'V','a','r','i','a','b','l','e',' ','\''}, nombre, new char[]{'\''}));
        } else if (simbolo.esFuncion) {
            registrarError(linea, columna, new char[]{'E','1'}, concatenar(new char[]{'N','o',' ','s','e',' ','p','u','e','d','e',' ','a','s','i','g','n','a','r',' ','u','n',' ','v','a','l','o','r',' ','a',' ','l','a',' ','f','u','n','c','i','o','n',' ','\''}, nombre, new char[]{'\''}));
        } else {
            simbolo.inicializado = true;
        }
    }

    public void realizarLectura(char[] nombre, int linea, int columna) {
        SimboloS simbolo = buscarSimbolo(nombre);
        if (simbolo == null) {
            registrarError(linea, columna, new char[]{'E','1'}, concatenar(new char[]{'V','a','r','i','a','b','l','e',' ','\''}, nombre, new char[]{'\''}));
        } else if (simbolo.esFuncion) {
            registrarError(linea, columna, new char[]{'E','1'}, concatenar(new char[]{'N','o',' ','s','e',' ','p','u','e','d','e',' ','l','e','e','r',' ','u','n',' ','v','a','l','o','r',' ','h','a','c','i','a',' ','l','a',' ','f','u','n','c','i','o','n',' ','\''}, nombre, new char[]{'\''}));
        } else {
            simbolo.inicializado = true;
        }
    }

    public void validarRetorno(char[] tipoExpresion, int linea, int columna, char[] tipoFuncion) {
        retornoDetectado = true;
        if (esIgual(tipoFuncion, new char[]{'V','A','C','I','O'})) {
            registrarError(linea, columna, new char[]{'E','1','5'}, new char[]{'S','e','n','t','e','n','c','i','a',' ','R','E','T','O','R','N','A',' ','n','o',' ','p','e','r','m','i','t','i','d','a',' ','e','n',' ','f','u','n','c','i','o','n','e','s',' ','V','A','C','I','O','.'});
            return;
        }
        if (!esIgual(tipoExpresion, tipoFuncion)) {
            registrarError(linea, columna, new char[]{'E','8'}, concatenar(new char[]{'E','l',' ','t','i','p','o',' ','r','e','t','o','r','n','a','d','o',' ','n','o',' ','c','o','i','n','c','i','d','e',' ','c','o','n',' ','l','a',' ','f','u','n','c','i','o','n'}));
        }
    }

    public void iniciarFuncion() {
        retornoDetectado = false;
    }

    public void finalizarFuncion(char[] nombre, char[] tipoFuncion, int linea, int columna) {
        if (!esIgual(tipoFuncion, new char[]{'V','A','C','I','O'}) && !retornoDetectado) {
            registrarError(linea, columna, new char[]{'E','8'}, concatenar(new char[]{'L','a',' ','f','u','n','c','i','o','n',' ','\''}, nombre, new char[]{'\''}));
        }
    }

    public void registrarError(int linea, int columna, char[] clave, char[] mensaje) {
        if (contadorErrores < listaErrores.length) {
            char[] charsLinea = intAChars(linea);
            char[] charsCol = intAChars(columna);
            listaErrores[contadorErrores++] = concatenar(new char[]{'['}, clave, new char[]{']',' ','L','i','n','e','a',' '}, charsLinea, new char[]{',',' ','C','o','l',' '}, charsCol, new char[]{':',' '}, mensaje);
        }
    }

    public String[] obtenerMensajesDeError() {
        String[] copia = new String[contadorErrores];
        for (int i = 0; i < contadorErrores; i++) {
            copia[i] = new String(listaErrores[i]);
        }
        return copia;
    }

    public boolean esIgual(char[] a, char[] b) {
        if (a == null || b == null) return false;
        if (a.length != b.length) return false;
        for (int i = 0; i < a.length; i++) {
            if (a[i] != b[i]) return false;
        }
        return true;
    }

    public boolean esIgualIgnorandoMayusculas(char[] a, char[] b) {
        if (a == null || b == null) return false;
        if (a.length != b.length) return false;
        for (int i = 0; i < a.length; i++) {
            char ca = a[i];
            char cb = b[i];
            if (ca >= 'A' && ca <= 'Z') ca = (char)(ca + 32);
            if (cb >= 'A' && cb <= 'Z') cb = (char)(cb + 32);
            if (ca != cb) return false;
        }
        return true;
    }

    public char[] concatenar(char[]... partes) {
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

    private char[] intAChars(int numero) {
        if (numero == 0) return new char[]{'0'};
        int temp = numero;
        int digitos = 0;
        while (temp > 0) {
            digitos++;
            temp /= 10;
        }
        char[] resultado = new char[digitos];
        temp = numero;
        for (int i = digitos - 1; i >= 0; i--) {
            resultado[i] = (char) ((temp % 10) + '0');
            temp /= 10;
        }
        return resultado;
    }
}
