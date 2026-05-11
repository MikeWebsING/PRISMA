package AnalizadorLexico;

public class Etiqueta {
    public final static int
            MODULO = 256, FIN_MODULO = 257, PRINCIPAL = 258, FIN_PRINCIPAL = 259,
            FUNCION = 260, FIN_FUNCION = 261, RETORNA = 262, SI = 263, SINO = 264,
            FIN_SI = 265, PARA = 266, FIN_PARA = 267, MIENTRAS = 268, FIN_MIENTRAS = 269,
            ENTERO = 270, DECIMAL = 271, TEXTO = 272, BOOLEANO = 273, VACIO = 274,
            IMPRIME = 275, LEER = 276, Y = 277, O = 278, NO = 279, IDENTIFICADOR = 280,
            NUMERO_ENTERO = 281, NUMERO_DECIMAL = 282, CADENA = 283, VERDADERO = 284, FALSO = 285,
            IGUALDAD = 286, DIFERENTE = 287, MAYOR_IGUAL = 288, MENOR_IGUAL = 289,
            VARIABLES = 290, FIN_VARIABLES = 291, ENTONCES = 292, CLS = 293,
            ERROR = 294, FIN_ARCHIVO = -1;

    public final static char[] TXT_MODULO = {'M','O','D','U','L','O'};
    public final static char[] TXT_FIN_MODULO = {'F','I','N','-','M','O','D','U','L','O'};
    public final static char[] TXT_PRINCIPAL = {'P','R','I','N','C','I','P','A','L'};
    public final static char[] TXT_FIN_PRINCIPAL = {'F','I','N','-','P','R','I','N','C','I','P','A','L'};
    public final static char[] TXT_FUNCION = {'F','U','N','C','I','O','N'};
    public final static char[] TXT_FIN_FUNCION = {'F','I','N','-','F','U','N','C','I','O','N'};
    public final static char[] TXT_RETORNA = {'R','E','T','O','R','N','A'};
    public final static char[] TXT_SI = {'S','I'};
    public final static char[] TXT_SINO = {'S','I','N','O'};
    public final static char[] TXT_FIN_SI = {'F','I','N','-','S','I'};
    public final static char[] TXT_PARA = {'P','A','R','A'};
    public final static char[] TXT_FIN_PARA = {'F','I','N','-','P','A','R','A'};
    public final static char[] TXT_MIENTRAS = {'M','I','E','N','T','R','A','S'};
    public final static char[] TXT_FIN_MIENTRAS = {'F','I','N','-','M','I','E','N','T','R','A','S'};
    public final static char[] TXT_ENTERO = {'E','N','T','E','R','O'};
    public final static char[] TXT_DECIMAL = {'D','E','C','I','M','A','L'};
    public final static char[] TXT_TEXTO = {'T','E','X','T','O'};
    public final static char[] TXT_BOOLEANO = {'B','O','O','L','E','A','N','O'};
    public final static char[] TXT_VACIO = {'V','A','C','I','O'};
    public final static char[] TXT_IMPRIME = {'I','M','P','R','I','M','E'};
    public final static char[] TXT_LEER = {'L','E','E','R'};
    public final static char[] TXT_Y = {'Y'};
    public final static char[] TXT_O = {'O'};
    public final static char[] TXT_NO = {'N','O'};
    public final static char[] TXT_VERDADERO = {'V','E','R','D','A','D','E','R','O'};
    public final static char[] TXT_FALSO = {'F','A','L','S','O'};
    public final static char[] TXT_VARIABLES = {'V','A','R','I','A','B','L','E','S'};
    public final static char[] TXT_FIN_VARIABLES = {'F','I','N','-','V','A','R','I','A','B','L','E','S'};
    public final static char[] TXT_ENTONCES = {'E','N','T','O','N','C','E','S'};
    public final static char[] TXT_CLS = {'C','L','S'};

    public static char[] obtener(int etiqueta) {
        switch (etiqueta) {
            case MODULO: return TXT_MODULO;
            case FIN_MODULO: return TXT_FIN_MODULO;
            case PRINCIPAL: return TXT_PRINCIPAL;
            case FIN_PRINCIPAL: return TXT_FIN_PRINCIPAL;
            case FUNCION: return TXT_FUNCION;
            case FIN_FUNCION: return TXT_FIN_FUNCION;
            case RETORNA: return TXT_RETORNA;
            case SI: return TXT_SI;
            case SINO: return TXT_SINO;
            case FIN_SI: return TXT_FIN_SI;
            case PARA: return TXT_PARA;
            case FIN_PARA: return TXT_FIN_PARA;
            case MIENTRAS: return TXT_MIENTRAS;
            case FIN_MIENTRAS: return TXT_FIN_MIENTRAS;
            case ENTERO: return TXT_ENTERO;
            case DECIMAL: return TXT_DECIMAL;
            case TEXTO: return TXT_TEXTO;
            case BOOLEANO: return TXT_BOOLEANO;
            case VACIO: return TXT_VACIO;
            case IMPRIME: return TXT_IMPRIME;
            case LEER: return TXT_LEER;
            case Y: return TXT_Y;
            case O: return TXT_O;
            case NO: return TXT_NO;
            case VERDADERO: return TXT_VERDADERO;
            case FALSO: return TXT_FALSO;
            case VARIABLES: return TXT_VARIABLES;
            case FIN_VARIABLES: return TXT_FIN_VARIABLES;
            case ENTONCES: return TXT_ENTONCES;
            case CLS: return TXT_CLS;
            case IDENTIFICADOR: return new char[]{'I','D'};
            case NUMERO_ENTERO: return new char[]{'N','U','M'};
            case FIN_ARCHIVO: return new char[]{'F','I','N','-','A','R','C','H','I','V','O'};
            default:
                if (etiqueta < 256 && etiqueta >= 0) {
                    return new char[]{ (char) etiqueta };
                }
                return new char[]{'?'};
        }
    }
}