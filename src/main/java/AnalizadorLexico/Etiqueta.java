package AnalizadorLexico;

public class Etiqueta {
    public final static int MODULO = 256, FIN_MODULO = 257, PRINCIPAL = 258, FIN_PRINCIPAL = 259,
            FUNCION = 260, FIN_FUNCION = 261, RETORNA = 262, SI = 263, SINO = 264,
            FIN_SI = 265, PARA = 266, FIN_PARA = 267, MIENTRAS = 268, FIN_MIENTRAS = 269,
            ENTERO = 270, DECIMAL = 271, TEXTO = 272, BOOLEANO = 273, VACIO = 274,
            IMPRIME = 275, LEER = 276, Y = 277, O = 278, NO = 279, ID = 280,
            NUM_INT = 281, NUM_DEC = 282, CADENA = 283, V = 284, F = 285,
            IGUALDAD = 286, DIFERENTE = 287, MAYOR_IGUAL = 288, MENOR_IGUAL = 289,
            VARIABLES = 290, FIN_VARIABLES = 291, ENTONCES = 292, ERROR = 293,
            EOF = -1;
}