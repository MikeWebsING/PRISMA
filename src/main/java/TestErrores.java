import AnalizadorLexico.*;
import AnalizadorSintactico.*;
import java.io.*;

public class TestErrores {
    public static void main(String[] args) {
        String codigoConError = "MODULO m1\nVARIABLES\nFIN-VARIABLES\nPRINCIPAL\ny = 10\nFIN-PRINCIPAL\nFIN-MODULO";
        
        try {
            InputStream stream = new ByteArrayInputStream(codigoConError.getBytes());
            System.setIn(stream);

            AnalizadorLexico lexico = new AnalizadorLexico();
            lexico.setLineasFuente(codigoConError);
            AnalizadorSintactico sintactico = new AnalizadorSintactico(lexico);

            sintactico.analizar();
            System.out.println("Compilación exitosa (No debería pasar)");

        } catch (ManejadorError err) {
            System.out.println("TEST PASSED - Error capturado correctamente:");
            System.out.println(err.getMessage());
        } catch (Exception ex) {
            System.out.println("Error inesperado: " + ex.getMessage());
            ex.printStackTrace();
        }
    }
}
