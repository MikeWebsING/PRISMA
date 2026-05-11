import java.io.File;
import java.util.Scanner;

public class Debug {
    public static void main(String[] args) throws Exception {
        File archivoEntrada = new File("temporal/programa.txt");
        Scanner lector = new Scanner(archivoEntrada);
        String contenido = "";
        while (lector.hasNextLine()) {
            contenido += lector.nextLine() + "\n";
        }
        lector.close();
        System.out.println(new String(Generador.traducir(contenido.toCharArray())));
    }
}
