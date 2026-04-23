import AnalizadorLexico.AnalizadorLexico;
import AnalizadorSintactico.AnalizadorSintactico;
import AnalizadorSintactico.ManejadorError;
import java.io.ByteArrayInputStream;
import java.io.InputStream;

public class PruebaError {
    public static void main(String[] args) {
        String codigo = "MODULO Prueba\nVARIABLES\nENTERO i = 0\nFIN-VARIABLES\nPRINCIPAL\nMIENTRAS ( i <= )\nIMPRIME(i)\nFIN-MIENTRAS\nFIN-PRINCIPAL\nFIN-MODULO";
        try {
            InputStream flujo = new ByteArrayInputStream(codigo.getBytes());
            System.setIn(flujo);
            AnalizadorLexico lexico = new AnalizadorLexico();
            lexico.definirLineasFuente(codigo);
            AnalizadorSintactico sintactico = new AnalizadorSintactico(lexico);
            sintactico.iniciarAnalisis();
            System.out.println("No se detecto error.");
        } catch (ManejadorError e) {
            System.out.println("Error detectado: " + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
