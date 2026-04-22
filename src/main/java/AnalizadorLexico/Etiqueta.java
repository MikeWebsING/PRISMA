package AnalizadorLexico;

public class Etiqueta {
    public final static int MODULO = 256, FIN_MODULO = 257, PRINCIPAL = 258, FIN_PRINCIPAL = 259,
            FUNCION = 260, FIN_FUNCION = 261, RETORNA = 262, SI = 263, SINO = 264,
            FIN_SI = 265, PARA = 266, FIN_PARA = 267, MIENTRAS = 268, FIN_MIENTRAS = 269,
            ENTERO = 270, DECIMAL = 271, TEXTO = 272, BOOLEANO = 273, VACIO = 274,
            IMPRIME = 275, LEER = 276, Y = 277, O = 278, NO = 279, IDENTIFICADOR = 280,
            NUMERO_ENTERO = 281, NUMERO_DECIMAL = 282, CADENA = 283, VERDADERO = 284, FALSO = 285,
            IGUALDAD = 286, DIFERENTE = 287, MAYOR_IGUAL = 288, MENOR_IGUAL = 289,
            VARIABLES = 290, FIN_VARIABLES = 291, ENTONCES = 292, ERROR = 293,
            FIN_ARCHIVO = -1;

    public static String obtenerNombre(int etiqueta) {
        switch (etiqueta) {
            case MODULO: return "MODULO";
            case FIN_MODULO: return "FIN-MODULO";
            case PRINCIPAL: return "PRINCIPAL";
            case FIN_PRINCIPAL: return "FIN-PRINCIPAL";
            case FUNCION: return "FUNCION";
            case FIN_FUNCION: return "FIN-FUNCION";
            case RETORNA: return "RETORNA";
            case SI: return "SI";
            case SINO: return "SINO";
            case FIN_SI: return "FIN-SI";
            case PARA: return "PARA";
            case FIN_PARA: return "FIN-PARA";
            case MIENTRAS: return "MIENTRAS";
            case FIN_MIENTRAS: return "FIN-MIENTRAS";
            case ENTERO: return "ENTERO";
            case DECIMAL: return "DECIMAL";
            case TEXTO: return "TEXTO";
            case BOOLEANO: return "BOOLEANO";
            case VACIO: return "VACIO";
            case IMPRIME: return "IMPRIME";
            case LEER: return "LEER";
            case Y: return "Y";
            case O: return "O";
            case NO: return "NO";
            case IDENTIFICADOR: return "IDENTIFICADOR";
            case NUMERO_ENTERO: return "NUMERO_ENTERO";
            case NUMERO_DECIMAL: return "NUMERO_DECIMAL";
            case CADENA: return "CADENA";
            case VERDADERO: return "VERDADERO";
            case FALSO: return "FALSO";
            case IGUALDAD: return "IGUALDAD (==)";
            case DIFERENTE: return "DIFERENTE (!=)";
            case MAYOR_IGUAL: return "MAYOR_IGUAL (>=)";
            case MENOR_IGUAL: return "MENOR_IGUAL (<=)";
            case VARIABLES: return "VARIABLES";
            case FIN_VARIABLES: return "FIN-VARIABLES";
            case ENTONCES: return "ENTONCES";
            case FIN_ARCHIVO: return "FIN DE ARCHIVO";
            default:
                if (etiqueta < 256 && etiqueta >= 0) {
                    return "'" + (char) etiqueta + "'";
                }
                return "DESCONOCIDO (" + etiqueta + ")";
        }
    }
}