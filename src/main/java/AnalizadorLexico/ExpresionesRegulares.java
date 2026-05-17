package AnalizadorLexico;

public class ExpresionesRegulares {
    public static final String ID = "[a-z_][a-zA-Z0-9_]*";
    public static final String NUM_INT = "[0-9]+";
    public static final String NUM_DEC = "[0-9]+\\.[0-9]+";
    public static final String CADENA = "\"[^\"]*\"";
    public static final String VALORES_LOGICOS = "(V|F)";
    public static final String RESERVADAS = "MODULO|VARIABLES|PRINCIPAL|SI|ENTONCES|SINO|PARA|MIENTRAS|FUNCION|ENTERO|DECIMAL|TEXTO|BOOLEANO|VACIO|IMPRIME|LEER|RETORNA|Y|O|NO";

    public static final String ESTRUCTURA_MODULO = "^MODULO\\s+" + ID + "\\s+VARIABLES\\s+.*\\s+FIN-VARIABLES\\s+.*\\s+PRINCIPAL\\s+.*\\s+FIN-PRINCIPAL\\s+FIN-MODULO$";
    public static final String TIPOS_DATOS = "^(ENTERO|DECIMAL|TEXTO|BOOLEANO|VACIO)$";
    public static final String DECLARACION_VAR = "^(ENTERO|DECIMAL|TEXTO|BOOLEANO|VACIO)\\s+" + ID + "(\\s*=\\s*.+)?$";
    public static final String DEFINICION_FUNCION = "^FUNCION\\s+(ENTERO|DECIMAL|TEXTO|BOOLEANO|VACIO)\\s+" + ID + "\\s*\\(.*\\)\\s+.*\\s+FIN-FUNCION$";
    public static final String ESTRUCTURA_SI = "^SI\\s*\\(.*\\)\\s*ENTONCES\\s+.*\\s+(SINO\\s+.*)?\\s+FIN-SI$";
    public static final String ESTRUCTURA_MIENTRAS = "^MIENTRAS\\s*\\(.*\\)\\s+.*\\s+FIN-MIENTRAS$";
    public static final String ESTRUCTURA_PARA = "^PARA\\s*\\(\\s*" + ID + "\\s*=\\s*.*\\s*,\\s*.*\\s*,\\s*" + ID + "\\s*=\\s*.*\\s*\\)\\s+.*\\s+FIN-PARA$";
    public static final String EXPRESION_OR = ".*(\\s+O\\s+.*)*";
    public static final String EXPRESION_SUMA_RESTA = ".*(\\s*(\\+|\\-)\\s*.*)*";
    public static final String PROPOSICION_IMPRIME = "^IMPRIME\\s*\\(.*\\)$";
    public static final String PROPOSICION_LEER = "^LEER\\s*\\(" + ID + "\\)$";

    public static final String PATRON_MODULO = "^MODULO\\s+" + ID + "$";
    public static final String PATRON_VARIABLES = "^VARIABLES$";
    public static final String PATRON_FIN_VARIABLES = "^FIN-VARIABLES$";
    public static final String PATRON_IMPRIME = "^IMPRIME\\s*\\(.*\\)$";
    public static final String PATRON_LEER = "^LEER\\s*\\(" + ID + "\\)$";
    public static final String PATRON_SI = "^SI\\s*\\(.*\\)\\s*ENTONCES$";
    public static final String PATRON_FIN_SI = "^FIN-SI$";
    public static final String PATRON_MIENTRAS = "^MIENTRAS\\s*\\(.*\\)$";
    public static final String PATRON_FIN_MIENTRAS = "^FIN-MIENTRAS$";
    public static final String PATRON_PARA = "^PARA\\s*\\(.*\\)$";
    public static final String PATRON_FIN_PARA = "^FIN-PARA$";
    public static final String PATRON_PRINCIPAL = "^PRINCIPAL$";
    public static final String PATRON_FIN_PRINCIPAL = "^FIN-PRINCIPAL$";
    public static final String PATRON_FIN_MODULO = "^FIN-MODULO$";
    public static final String PATRON_FUNCION = "^FUNCION\\s+(ENTERO|DECIMAL|TEXTO|BOOLEANO|VACIO)\\s+" + ID + "\\s*\\(.*\\)$";
    public static final String PATRON_FIN_FUNCION = "^FIN-FUNCION$";
    public static final String PATRON_RETORNA = "^RETORNA\\s+.*$";
}