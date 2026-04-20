package compilador;

/**
 *Juan Manuel Lambaren Torres
 * Compilador
 * lexico
 * Semantico
 * Sintactico
 */
public class Compilador {
    public static void main(String[] args) {
        
        System.out.println("\n--Analizador Lexico---\n");
        
        AnalizadorLexico lexico = new AnalizadorLexico();
        lexico.analizar("archivo.txt");

        System.out.println("\n--- Analizador Sintactico ---\n");

        AnalicadorSintactico sintactico = new AnalicadorSintactico();
        sintactico.analizar("archivo.txt");
    }
    
}
