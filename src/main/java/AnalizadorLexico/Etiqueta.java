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

    public static char[] obtenerNombre(int etiqueta) {
        switch (etiqueta) {
            case MODULO: return new char[]{'M','O','D','U','L','O'};
            case FIN_MODULO: return new char[]{'F','I','N','-','M','O','D','U','L','O'};
            case PRINCIPAL: return new char[]{'P','R','I','N','C','I','P','A','L'};
            case FIN_PRINCIPAL: return new char[]{'F','I','N','-','P','R','I','N','C','I','P','A','L'};
            case FUNCION: return new char[]{'F','U','N','C','I','O','N'};
            case FIN_FUNCION: return new char[]{'F','I','N','-','F','U','N','C','I','O','N'};
            case RETORNA: return new char[]{'R','E','T','O','R','N','A'};
            case SI: return new char[]{'S','I'};
            case SINO: return new char[]{'S','I','N','O'};
            case FIN_SI: return new char[]{'F','I','N','-','S','I'};
            case PARA: return new char[]{'P','A','R','A'};
            case FIN_PARA: return new char[]{'F','I','N','-','P','A','R','A'};
            case MIENTRAS: return new char[]{'M','I','E','N','T','R','A','S'};
            case FIN_MIENTRAS: return new char[]{'F','I','N','-','M','I','E','N','T','R','A','S'};
            case ENTERO: return new char[]{'E','N','T','E','R','O'};
            case DECIMAL: return new char[]{'D','E','C','I','M','A','L'};
            case TEXTO: return new char[]{'T','E','X','T','O'};
            case BOOLEANO: return new char[]{'B','O','O','L','E','A','N','O'};
            case VACIO: return new char[]{'V','A','C','I','O'};
            case IMPRIME: return new char[]{'I','M','P','R','I','M','E'};
            case LEER: return new char[]{'L','E','E','R'};
            case Y: return new char[]{'Y'};
            case O: return new char[]{'O'};
            case NO: return new char[]{'N','O'};
            case IDENTIFICADOR: return new char[]{'I','D','E','N','T','I','F','I','C','A','D','O','R'};
            case NUMERO_ENTERO: return new char[]{'N','U','M','E','R','O','_','E','N','T','E','R','O'};
            case NUMERO_DECIMAL: return new char[]{'N','U','M','E','R','O','_','D','E','C','I','M','A','L'};
            case CADENA: return new char[]{'C','A','D','E','N','A'};
            case VERDADERO: return new char[]{'V','E','R','D','A','D','E','R','O'};
            case FALSO: return new char[]{'F','A','L','S','O'};
            case IGUALDAD: return new char[]{'I','G','U','A','L','D','A','D',' ','(','=','=',')'};
            case DIFERENTE: return new char[]{'D','I','F','E','R','E','N','T','E',' ','(','!','=',')'};
            case MAYOR_IGUAL: return new char[]{'M','A','Y','O','R','_','I','G','U','A','L',' ','(','>','=',')'};
            case MENOR_IGUAL: return new char[]{'M','E','N','O','R','_','I','G','U','A','L',' ','(','<','=',')'};
            case VARIABLES: return new char[]{'V','A','R','I','A','B','L','E','S'};
            case FIN_VARIABLES: return new char[]{'F','I','N','-','V','A','R','I','A','B','L','E','S'};
            case ENTONCES: return new char[]{'E','N','T','O','N','C','E','S'};
            case FIN_ARCHIVO: return new char[]{'F','I','N',' ','D','E',' ','A','R','C','H','I','V','O'};
            default:
                if (etiqueta < 256 && etiqueta >= 0) {
                    return new char[]{ '\'', (char) etiqueta, '\'' };
                }
                
                char[] desconocido = new char[]{'D','E','S','C','O','N','O','C','I','D','O'};
                return desconocido;
        }
    }
}