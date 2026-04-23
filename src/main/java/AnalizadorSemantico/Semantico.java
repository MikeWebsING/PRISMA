package AnalizadorSemantico;

import AnalizadorLexico.Etiqueta;

public class Semantico {
    private SimboloS[] tablaSimbolos;
    private int contadorSimbolos = 0;
    
    private String[] listaErrores;
    private int contadorErrores = 0;
    private boolean retornoDetectado = false;
    private String tipoFuncionActual = null;
    private boolean dentroDeFuncion = false;
    private static final String[] PALABRAS_RESERVADAS = {
        "MODULO", "VARIABLES", "FIN-VARIABLES", "PRINCIPAL", "FIN-PRINCIPAL",
        "FUNCION", "FIN-FUNCION", "RETORNA", "SI", "ENTONCES", "SINO", "FIN-SI",
        "PARA", "FIN-PARA", "MIENTRAS", "FIN-MIENTRAS", "ENTERO", "DECIMAL",
        "TEXTO", "BOOLEANO", "VACIO", "IMPRIME", "LEER", "Y", "O", "NO", "V", "F"
    };

    public Semantico() {
        tablaSimbolos = new SimboloS[1000];
        listaErrores = new String[200];
    }

    public void setDentroDeFuncion(boolean estado, String tipo) {
        this.dentroDeFuncion = estado;
        this.tipoFuncionActual = tipo;
    }

    public void registrarVariable(int linea, int columna, String nombre, String tipo, boolean inicializado) {
        for (int i = 0; i < PALABRAS_RESERVADAS.length; i++) {
            if (PALABRAS_RESERVADAS[i].equalsIgnoreCase(nombre)) {
                registrarError(linea, columna, "E10", "Uso de palabra reservada '" + nombre + "' como identificador.");
                return;
            }
        }
        if (buscarSimbolo(nombre) != null) {
            registrarError(linea, columna, "E2", "El identificador '" + nombre + "' ya ha sido declarado previamente.");
        } else {
            if (contadorSimbolos < tablaSimbolos.length) {
                tablaSimbolos[contadorSimbolos++] = new SimboloS(nombre, tipo, inicializado, linea, columna);
            }
        }
    }

    public void registrarFuncion(int linea, int columna, String nombre, String tipoRetorno, String firma) {
        for (int i = 0; i < PALABRAS_RESERVADAS.length; i++) {
            if (PALABRAS_RESERVADAS[i].equalsIgnoreCase(nombre)) {
                registrarError(linea, columna, "E10", "Uso de palabra reservada '" + nombre + "' como nombre de funcion.");
                return;
            }
        }
        if (buscarSimbolo(nombre) != null) {
            registrarError(linea, columna, "E12", "La funcion '" + nombre + "' ya ha sido definida.");
        } else {
            if (contadorSimbolos < tablaSimbolos.length) {
                SimboloS s = new SimboloS(nombre, tipoRetorno, true, linea, columna);
                s.esFuncion = true;
                s.firma = firma;
                tablaSimbolos[contadorSimbolos++] = s;
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

    public void validarUsoVariable(String nombre, int linea, int columna) {
        SimboloS simbolo = buscarSimbolo(nombre);
        if (simbolo == null) {
            registrarError(linea, columna, "E1", "Variable '" + nombre + "' no declarada.");
        } else if (simbolo.esFuncion) {
            registrarError(linea, columna, "E1", "Se intenta usar la funcion '" + nombre + "' como una variable.");
        } else if (!simbolo.inicializado) {
            registrarError(linea, columna, "E13", "La variable '" + nombre + "' se usa sin haber sido inicializada.");
        }
    }

    public void validarDivisionPorCero(int linea, int columna, String operador, double valor) {
        if ((operador.equals("/") || operador.equals("%") || operador.equals("DIVISION") || operador.equals("MODULO")) && valor == 0) {
            registrarError(linea, columna, "E11", "Division por cero detectada en operacion '" + operador + "'.");
        }
    }

    public void validarTipoCondicion(String tipo, int linea, int columna) {
        if (tipo.equals("DESCONOCIDO")) return;
        if (!tipo.equals("BOOLEANO")) {
            registrarError(linea, columna, "E4", "La condicion debe ser BOOLEANO. Se encontro: " + tipo);
        }
    }

    public void verificarCompatibilidad(String nombre, String tipoOrigen, int linea, int columna) {
        SimboloS simbolo = buscarSimbolo(nombre);
        if (simbolo == null) {
            registrarError(linea, columna, "E1", "Variable '" + nombre + "' no declarada.");
            return;
        }
        if (simbolo.esFuncion) {
            registrarError(linea, columna, "E1", "El identificador '" + nombre + "' es una funcion y no puede recibir asignaciones.");
            return;
        }
        if (!tipoOrigen.equals("DESCONOCIDO")) {
            if (!simbolo.tipo.equals(tipoOrigen)) {
                registrarError(linea, columna, "E3", "Incompatibilidad de tipos: No se puede asignar " + tipoOrigen + " a variable " + simbolo.tipo);
            }
        }
    }

    public void validarOperando(String tipoActual, String categoriaEsperada, int linea, int columna, String operador) {
        if (tipoActual.equals("DESCONOCIDO")) return;

        if (categoriaEsperada.equals("ARITMETICO")) {
            if (!tipoActual.equals("ENTERO") && !tipoActual.equals("DECIMAL")) {
                registrarError(linea, columna, "E3", "El operando de '" + operador + "' debe ser numerico. Se recibio: " + tipoActual);
            }
        } else if (categoriaEsperada.equals("LOGICO")) {
            if (!tipoActual.equals("BOOLEANO")) {
                registrarError(linea, columna, "E3", "El operando de '" + operador + "' debe ser booleano. Se recibio: " + tipoActual);
            }
        }
    }

    public void verificarCompatibilidadBinaria(String t1, String t2, int lin, int col, String op) {
        if (t1.equals("DESCONOCIDO") || t2.equals("DESCONOCIDO")) return;
        if (!t1.equals(t2)) {
            registrarError(lin, col, "E3", "Tipos incompatibles para '" + op + "': " + t1 + " y " + t2);
        }
    }

    public String obtenerTipoResultante(String t1, String t2) {
        if (t1.equals(t2)) return t1;
        return "DESCONOCIDO";
    }

    public void validarContadorPara(String nombre, int linea, int columna) {
        SimboloS sim = buscarSimbolo(nombre);
        if (sim != null && !sim.tipo.equals("ENTERO") && !sim.tipo.equals("DECIMAL")) {
            registrarError(linea, columna, "E9", "La variable de control '" + nombre + "' debe ser numerica.");
        }
    }

    public void validarLlamada(String nombre, String firmaArgumentos, int linea, int columna) {
        SimboloS sim = buscarSimbolo(nombre);
        if (sim == null) {
            registrarError(linea, columna, "E5", "Funcion '" + nombre + "' no definida.");
            return;
        }
        if (!sim.esFuncion) {
            registrarError(linea, columna, "E1", "'" + nombre + "' no es una funcion.");
            return;
        }
        
        String[] tiposArgs = fragmentarFirma(firmaArgumentos);
        String[] paramsEsperados = fragmentarFirma(sim.firma);
        
        if (paramsEsperados.length != tiposArgs.length) {
            registrarError(linea, columna, "E6", "Numero de argumentos incorrecto para '" + nombre + "'. Esperados: " + paramsEsperados.length + ", recibidos: " + tiposArgs.length);
            return;
        }
        
        for (int i = 0; i < paramsEsperados.length; i++) {
            if (!paramsEsperados[i].equals(tiposArgs[i])) {
                registrarError(linea, columna, "E7", "Tipo de argumento " + (i + 1) + " invalido para '" + nombre + "'. Se esperaba " + paramsEsperados[i] + " pero se recibio " + tiposArgs[i]);
            }
        }
    }

    private String[] fragmentarFirma(String firma) {
        if (firma == null || firma.isEmpty()) return new String[0];
        int comas = 0;
        for (int i = 0; i < firma.length(); i++) if (firma.charAt(i) == ',') comas++;
        String[] partes = new String[comas + 1];
        int inicio = 0;
        int idx = 0;
        for (int i = 0; i < firma.length(); i++) {
            if (firma.charAt(i) == ',') {
                partes[idx++] = firma.substring(inicio, i);
                inicio = i + 1;
            }
        }
        partes[idx] = firma.substring(inicio);
        return partes;
    }

    public void realizarDeclaracion(String nombre, int etiquetaTipo, int linea, int columna) {
        String nombreTipo = Etiqueta.obtenerNombre(etiquetaTipo);
        if (nombreTipo.equals("VACIO")) {
            registrarError(linea, columna, "E14", "No se permiten variables de tipo VACIO.");
            return;
        }
        registrarVariable(linea, columna, nombre, nombreTipo, false);
    }

    public void realizarAsignacion(String nombre, int linea, int columna) {
        SimboloS simbolo = buscarSimbolo(nombre);
        if (simbolo == null) {
            registrarError(linea, columna, "E1", "Variable '" + nombre + "' no declarada.");
        } else if (simbolo.esFuncion) {
            registrarError(linea, columna, "E1", "No se puede asignar un valor a la funcion '" + nombre + "'.");
        } else {
            simbolo.inicializado = true;
        }
    }

    public void realizarLectura(String nombre, int linea, int columna) {
        SimboloS simbolo = buscarSimbolo(nombre);
        if (simbolo == null) {
            registrarError(linea, columna, "E1", "Variable '" + nombre + "' no declarada.");
        } else if (simbolo.esFuncion) {
            registrarError(linea, columna, "E1", "No se puede leer un valor hacia el identificador de funcion '" + nombre + "'.");
        } else {
            simbolo.inicializado = true;
        }
    }

    public void validarRetorno(String tipoExpresion, int linea, int columna, String tipoFuncion) {
        retornoDetectado = true;
        if (tipoFuncion.equals("VACIO")) {
            registrarError(linea, columna, "E15", "Sentencia RETORNA no permitida en funciones VACIO.");
            return;
        }
        if (!tipoExpresion.equals(tipoFuncion)) {
            registrarError(linea, columna, "E8", "El tipo retornado (" + tipoExpresion + ") no coincide con la funcion (" + tipoFuncion + ").");
        }
    }

    public void iniciarFuncion() {
        retornoDetectado = false;
    }

    public void finalizarFuncion(String nombre, String tipoFuncion, int linea, int columna) {
        if (!tipoFuncion.equals("VACIO") && !retornoDetectado) {
            registrarError(linea, columna, "E8", "La funcion '" + nombre + "' debe retornar un valor de tipo " + tipoFuncion);
        }
    }

    public void registrarError(int linea, int columna, String clave, String mensaje) {
        if (contadorErrores < listaErrores.length) {
            listaErrores[contadorErrores++] = "[" + clave + "] Linea " + linea + ", Col " + columna + ": " + mensaje;
        }
    }

    public String[] obtenerMensajesDeError() {
        String[] copia = new String[contadorErrores];
        for (int i = 0; i < contadorErrores; i++) copia[i] = listaErrores[i];
        return copia;
    }
}
