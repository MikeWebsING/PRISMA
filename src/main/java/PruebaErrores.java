import AnalizadorLexico.*;
import AnalizadorSintactico.*;
import java.io.*;

public class PruebaErrores {
    public static void main(String[] argumentos) {
        String codigoPrueba = "MODULO m1\nVARIABLES\nFIN-VARIABLES\nPRINCIPAL\ny = 10\nFIN-PRINCIPAL\nFIN-MODULO";
        
        try {
            InputStream flujoEntrada = new ByteArrayInputStream(codigoPrueba.getBytes());
            System.setIn(flujoEntrada);

            AnalizadorLexico lexico = new AnalizadorLexico();
            lexico.definirLineasFuente(codigoPrueba);
            AnalizadorSintactico sintactico = new AnalizadorSintactico(lexico);

            sintactico.iniciarAnalisis();
            System.out.println("Analisis completado sin errores.");

        } catch (ManejadorError error) {
            System.out.println("Prueba exitosa - Error capturado:");
            System.out.println(error.getMessage());
        } catch (Exception excepcion) {
            System.out.println("Falla inesperada: " + excepcion.getMessage());
        }
    }
}
