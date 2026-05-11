import java.io.File;
import java.io.FileWriter;
import java.util.Scanner;

public class Generador {
    private static int contadorCadenas = 0;
    private static int contadorEtiquetas = 0;

    public static void main(String[] argumentos) {
        try {
            File archivoEntrada = new File("programa_por_defecto.txt");
            Scanner lector = new Scanner(archivoEntrada);
            String contenidoTemporal = "";
            while (lector.hasNextLine()) {
                contenidoTemporal += lector.nextLine() + "\n";
            }
            lector.close();

            char[] resultadoAsm = traducir(contenidoTemporal.toCharArray());

            File carpetaTemporal = new File("temporal");
            if (!carpetaTemporal.exists()) {
                carpetaTemporal.mkdir();
            }

            FileWriter escritor = new FileWriter("temporal/programa.asm");
            escritor.write(resultadoAsm);
            escritor.close();
        } catch (Exception excepcion) {
        }
    }

    public static char[] traducir(char[] codigo) {
        String procCode = 
            "IMPRIME Macro Mensaje\n" +
            "    Mov Ah, 09h\n" +
            "    mov dx, offset Mensaje\n" +
            "    int 21h\n" +
            "EndM\n\n" +
            "LEER Macro Entrada\n" +
            "  mov Ah, 0Ah\n" +
            "  mov Dx, offset Entrada\n" +
            "  int 21h\n" +
            "EndM\n\n" +
            "CLS Macro\n" +
            "    Mov Ah, 0Fh\n" +
            "    int 10h\n" +
            "    Mov Ah, 0h\n" +
            "    int 10h\n" +
            "EndM\n\n" +
            ".MODEL SMALL\n" +
            ".CODE\n" +
            "CargarVars proc\n" +
            "  mov al, [si+2]\n" +
            "  mov bl, [di+2]\n" +
            "  ret\n" +
            "CargarVars endp\n\n" +
            "Guardar proc\n" +
            "  mov [si+2], al\n" +
            "  ret\n" +
            "Guardar endp\n\n" +
            "SaltarLinea proc\n" +
            "  mov ah, 02h\n" +
            "  mov dl, 13\n" +
            "  int 21h\n" +
            "  mov dl, 10\n" +
            "  int 21h\n" +
            "  ret\n" +
            "SaltarLinea endp\n\n" +
            "MostrarNum proc\n" +
            "  push ax\n" +
            "  push bx\n" +
            "  push cx\n" +
            "  push dx\n" +
            "  mov cx, 0\n" +
            "  mov bx, 10\n" +
            "DivLoop:\n" +
            "  mov dx, 0\n" +
            "  div bx\n" +
            "  push dx\n" +
            "  inc cx\n" +
            "  cmp ax, 0\n" +
            "  jne DivLoop\n" +
            "PrintLoop:\n" +
            "  pop dx\n" +
            "  add dl, 30h\n" +
            "  mov ah, 02h\n" +
            "  int 21h\n" +
            "  loop PrintLoop\n" +
            "  pop dx\n" +
            "  pop cx\n" +
            "  pop bx\n" +
            "  pop ax\n" +
            "  ret\n" +
            "MostrarNum endp\n\n" +
            "LeerNum proc\n" +
            "  mov si, dx\n" +
            "  mov cl, [si+1]\n" +
            "  mov ch, 0\n" +
            "  add si, 2\n" +
            "  mov ax, 0\n" +
            "  mov bx, 10\n" +
            "ParseBucle:\n" +
            "  mov dl, [si]\n" +
            "  sub dl, 30h\n" +
            "  mov dh, 0\n" +
            "  push dx\n" +
            "  mul bx\n" +
            "  pop dx\n" +
            "  add ax, dx\n" +
            "  inc si\n" +
            "  loop ParseBucle\n" +
            "  ret\n" +
            "LeerNum endp\n\n" +
            "ImprimirVariable proc\n" +
            "  mov si, dx\n" +
            "  mov al, [si+2]\n" +
            "  mov ah, 0\n" +
            "  call MostrarNum\n" +
            "  call SaltarLinea\n" +
            "  ret\n" +
            "ImprimirVariable endp\n\n" +
            "Inicio:\n" +
            "  mov Ax, @Data\n" +
            "  mov Ds, Ax\n" +
            "  CLS\n\n";

        char[] ensambladorInicio = procCode.toCharArray();
        char[] ensambladorFin = {'m','o','v',' ','A','x',',',' ','4','C','0','0','h','\n','i','n','t',' ','2','1','h','\n','\n'};
        char[] ensambladorDatosInicio = {'.','D','A','T','A','\n'};
        char[] ensambladorPila = {'.','S','T','A','C','K','\n','E','N','D',' ','I','n','i','c','i','o','\n'};
        
        char[] cuerpo = new char[0];
        char[] datos = new char[0];
        char[][] pilaEtiquetas = new char[100][];
        int cimaEtiquetas = -1;
        char[][] pilaIncrementos = new char[100][];
        int cimaIncrementos = -1;
        
        int longitud = codigo.length;
        int indice = 0;
        boolean enVariables = false;
        boolean enPrincipal = false;

        char[] palabraVariables = "VARIABLES".toCharArray();
        char[] palabraFinVariables = "FIN-VARIABLES".toCharArray();
        char[] palabraPrincipal = "PRINCIPAL".toCharArray();
        char[] palabraFinPrincipal = "FIN-PRINCIPAL".toCharArray();
        char[] palabraEntero = "ENTERO".toCharArray();
        char[] palabraImprime = "IMPRIME".toCharArray();
        char[] palabraLeer = "LEER".toCharArray();
        char[] palabraSi = "SI".toCharArray();
        char[] palabraFinSi = "FIN-SI".toCharArray();
        char[] palabraMientras = "MIENTRAS".toCharArray();
        char[] palabraFinMientras = "FIN-MIENTRAS".toCharArray();
        char[] palabraPara = "PARA".toCharArray();
        char[] palabraFinPara = "FIN-PARA".toCharArray();

        while (indice < longitud) {
            while (indice < longitud && (codigo[indice] == ' ' || codigo[indice] == '\t' || codigo[indice] == '\r')) {
                indice++;
            }
            int inicioLinea = indice;
            while (indice < longitud && codigo[indice] != '\n') {
                indice++;
            }
            int finLinea = indice;
            while (finLinea > inicioLinea && (codigo[finLinea - 1] == ' ' || codigo[finLinea - 1] == '\t' || codigo[finLinea - 1] == '\r')) {
                finLinea--;
            }
            int longitudLinea = finLinea - inicioLinea;
            if (longitudLinea <= 0) {
                if (indice < longitud) indice++;
                continue;
            }

            if (coincideExactamente(codigo, inicioLinea, longitudLinea, palabraVariables)) {
                enVariables = true;
            } else if (coincideExactamente(codigo, inicioLinea, longitudLinea, palabraFinVariables)) {
                enVariables = false;
            } else if (coincideExactamente(codigo, inicioLinea, longitudLinea, palabraPrincipal)) {
                enPrincipal = true;
            } else if (coincideExactamente(codigo, inicioLinea, longitudLinea, palabraFinPrincipal)) {
                enPrincipal = false;
            } else if (enVariables) {
                if (empiezaCon(codigo, inicioLinea, longitudLinea, palabraEntero)) {
                    int inicioNombre = inicioLinea + 6;
                    while (inicioNombre < finLinea && (codigo[inicioNombre] == ' ' || codigo[inicioNombre] == '\t')) inicioNombre++;
                    char[] nombreVariable = extraerSubcadena(codigo, inicioNombre, finLinea);
                    char[] declVariables = " db 10, ?, 10 dup (24h)\n".toCharArray();
                    datos = concatenar(datos, nombreVariable);
                    datos = concatenar(datos, declVariables);
                }
            } else if (enPrincipal) {
                if (empiezaCon(codigo, inicioLinea, longitudLinea, palabraImprime)) {
                    int inicioId = encontrarCaracter(codigo, inicioLinea, finLinea, '(') + 1;
                    int finId = encontrarCaracterDesdeAtras(codigo, inicioLinea, finLinea, ')');
                    while (inicioId < finId && (codigo[inicioId] == ' ' || codigo[inicioId] == '\t')) inicioId++;
                    while (finId > inicioId && (codigo[finId - 1] == ' ' || codigo[finId - 1] == '\t')) finId--;
                    char[] argumento = extraerSubcadena(codigo, inicioId, finId);
                    
                    if (esCadena(argumento)) {
                        char[] nombreCadena = {'_','c','a','d','e','n','a','0'};
                        nombreCadena[7] = (char)('0' + (contadorCadenas % 10));
                        contadorCadenas++;
                        char[] declCadena = " db ".toCharArray();
                        char[] finDecl = ", '$'\n".toCharArray();
                        datos = concatenar(datos, nombreCadena);
                        datos = concatenar(datos, declCadena);
                        datos = concatenar(datos, argumento);
                        datos = concatenar(datos, finDecl);
                        cuerpo = concatenar(cuerpo, "  IMPRIME ".toCharArray());
                        cuerpo = concatenar(cuerpo, nombreCadena);
                        cuerpo = concatenar(cuerpo, "\n".toCharArray());
                    } else {
                        cuerpo = concatenar(cuerpo, "  mov dx, offset ".toCharArray());
                        cuerpo = concatenar(cuerpo, argumento);
                        cuerpo = concatenar(cuerpo, "\n  call ImprimirVariable\n".toCharArray());
                    }
                } else if (empiezaCon(codigo, inicioLinea, longitudLinea, palabraLeer)) {
                    int inicioId = encontrarCaracter(codigo, inicioLinea, finLinea, '(') + 1;
                    int finId = encontrarCaracterDesdeAtras(codigo, inicioLinea, finLinea, ')');
                    while (inicioId < finId && (codigo[inicioId] == ' ' || codigo[inicioId] == '\t')) inicioId++;
                    while (finId > inicioId && (codigo[finId - 1] == ' ' || codigo[finId - 1] == '\t')) finId--;
                    char[] id = extraerSubcadena(codigo, inicioId, finId);
                    cuerpo = concatenar(cuerpo, "  mov dx, offset ".toCharArray());
                    cuerpo = concatenar(cuerpo, id);
                    cuerpo = concatenar(cuerpo, "\n  LEER ".toCharArray());
                    cuerpo = concatenar(cuerpo, id);
                    cuerpo = concatenar(cuerpo, "\n  call LeerNum\n".toCharArray());
                    cuerpo = concatenar(cuerpo, "  mov si, offset ".toCharArray());
                    cuerpo = concatenar(cuerpo, id);
                    cuerpo = concatenar(cuerpo, "\n  mov [si+2], al\n  call SaltarLinea\n".toCharArray());
                } else if (empiezaCon(codigo, inicioLinea, longitudLinea, palabraSi)) {
                    int inicioId = encontrarCaracter(codigo, inicioLinea, finLinea, '(') + 1;
                    int finId = encontrarCaracterDesdeAtras(codigo, inicioLinea, finLinea, ')');
                    char[] condicion = extraerSubcadena(codigo, inicioId, finId);
                    char[] etiquetaFin = generarNombreEtiqueta();
                    cimaEtiquetas++;
                    pilaEtiquetas[cimaEtiquetas] = etiquetaFin;
                    cuerpo = concatenar(cuerpo, generarComparacion(condicion, etiquetaFin));
                } else if (empiezaCon(codigo, inicioLinea, longitudLinea, palabraFinSi)) {
                    char[] etiquetaFin = pilaEtiquetas[cimaEtiquetas];
                    cimaEtiquetas--;
                    cuerpo = concatenar(cuerpo, etiquetaFin);
                    cuerpo = concatenar(cuerpo, ":\n".toCharArray());
                } else if (empiezaCon(codigo, inicioLinea, longitudLinea, palabraMientras)) {
                    int inicioId = encontrarCaracter(codigo, inicioLinea, finLinea, '(') + 1;
                    int finId = encontrarCaracterDesdeAtras(codigo, inicioLinea, finLinea, ')');
                    char[] condicion = extraerSubcadena(codigo, inicioId, finId);
                    char[] etiquetaInicio = generarNombreEtiqueta();
                    char[] etiquetaFin = generarNombreEtiqueta();
                    cimaEtiquetas++; pilaEtiquetas[cimaEtiquetas] = etiquetaInicio;
                    cimaEtiquetas++; pilaEtiquetas[cimaEtiquetas] = etiquetaFin;
                    cuerpo = concatenar(cuerpo, etiquetaInicio);
                    cuerpo = concatenar(cuerpo, ":\n".toCharArray());
                    cuerpo = concatenar(cuerpo, generarComparacion(condicion, etiquetaFin));
                } else if (empiezaCon(codigo, inicioLinea, longitudLinea, palabraFinMientras)) {
                    char[] etiquetaFin = pilaEtiquetas[cimaEtiquetas]; cimaEtiquetas--;
                    char[] etiquetaInicio = pilaEtiquetas[cimaEtiquetas]; cimaEtiquetas--;
                    cuerpo = concatenar(cuerpo, "  jmp ".toCharArray());
                    cuerpo = concatenar(cuerpo, etiquetaInicio);
                    cuerpo = concatenar(cuerpo, "\n".toCharArray());
                    cuerpo = concatenar(cuerpo, etiquetaFin);
                    cuerpo = concatenar(cuerpo, ":\n".toCharArray());
                } else if (empiezaCon(codigo, inicioLinea, longitudLinea, palabraPara)) {
                    int inicioId = encontrarCaracter(codigo, inicioLinea, finLinea, '(') + 1;
                    int finId = encontrarCaracterDesdeAtras(codigo, inicioLinea, finLinea, ')');
                    int coma1 = encontrarCaracter(codigo, inicioId, finId, ',');
                    int coma2 = encontrarCaracter(codigo, coma1 + 1, finId, ',');
                    char[] inicializacion = extraerSubcadena(codigo, inicioId, coma1);
                    char[] condicion = extraerSubcadena(codigo, coma1 + 1, coma2);
                    char[] incremento = extraerSubcadena(codigo, coma2 + 1, finId);
                    cuerpo = concatenar(cuerpo, traducirLinea(inicializacion));
                    char[] etiquetaInicio = generarNombreEtiqueta();
                    char[] etiquetaFin = generarNombreEtiqueta();
                    cimaEtiquetas++; pilaEtiquetas[cimaEtiquetas] = etiquetaInicio;
                    cimaEtiquetas++; pilaEtiquetas[cimaEtiquetas] = etiquetaFin;
                    cimaIncrementos++; pilaIncrementos[cimaIncrementos] = incremento;
                    cuerpo = concatenar(cuerpo, etiquetaInicio);
                    cuerpo = concatenar(cuerpo, ":\n".toCharArray());
                    cuerpo = concatenar(cuerpo, generarComparacion(condicion, etiquetaFin));
                } else if (empiezaCon(codigo, inicioLinea, longitudLinea, palabraFinPara)) {
                    char[] etiquetaFin = pilaEtiquetas[cimaEtiquetas]; cimaEtiquetas--;
                    char[] etiquetaInicio = pilaEtiquetas[cimaEtiquetas]; cimaEtiquetas--;
                    char[] incremento = pilaIncrementos[cimaIncrementos]; cimaIncrementos--;
                    cuerpo = concatenar(cuerpo, traducirLinea(incremento));
                    cuerpo = concatenar(cuerpo, "  jmp ".toCharArray());
                    cuerpo = concatenar(cuerpo, etiquetaInicio);
                    cuerpo = concatenar(cuerpo, "\n".toCharArray());
                    cuerpo = concatenar(cuerpo, etiquetaFin);
                    cuerpo = concatenar(cuerpo, ":\n".toCharArray());
                } else {
                    cuerpo = concatenar(cuerpo, traducirLinea(extraerSubcadena(codigo, inicioLinea, finLinea)));
                }
            }
            if (indice < longitud) indice++;
        }

        char[] resultado = concatenar(ensambladorInicio, cuerpo);
        resultado = concatenar(resultado, ensambladorFin);
        resultado = concatenar(resultado, ensambladorDatosInicio);
        resultado = concatenar(resultado, datos);
        resultado = concatenar(resultado, ensambladorPila);
        return resultado;
    }

    public static char[] concatenar(char[] arregloUno, char[] arregloDos) {
        char[] resultado = new char[arregloUno.length + arregloDos.length];
        for (int i = 0; i < arregloUno.length; i++) resultado[i] = arregloUno[i];
        for (int i = 0; i < arregloDos.length; i++) resultado[arregloUno.length + i] = arregloDos[i];
        return resultado;
    }

    public static boolean coincideExactamente(char[] fuente, int inicio, int longitudLinea, char[] busqueda) {
        if (longitudLinea != busqueda.length) return false;
        for (int i = 0; i < longitudLinea; i++) if (fuente[inicio + i] != busqueda[i]) return false;
        return true;
    }

    public static boolean empiezaCon(char[] fuente, int inicio, int longitudLinea, char[] busqueda) {
        if (longitudLinea < busqueda.length) return false;
        for (int i = 0; i < busqueda.length; i++) if (fuente[inicio + i] != busqueda[i]) return false;
        return true;
    }

    public static int encontrarCaracter(char[] fuente, int inicio, int fin, char busqueda) {
        for (int i = inicio; i < fin; i++) if (fuente[i] == busqueda) return i;
        return -1;
    }

    public static int encontrarCaracterDesdeAtras(char[] fuente, int inicio, int fin, char busqueda) {
        for (int i = fin - 1; i >= inicio; i--) if (fuente[i] == busqueda) return i;
        return -1;
    }

    public static char[] extraerSubcadena(char[] fuente, int inicio, int fin) {
        int tamano = fin - inicio;
        if (tamano <= 0) return new char[0];
        char[] resultado = new char[tamano];
        for (int i = 0; i < tamano; i++) resultado[i] = fuente[inicio + i];
        return resultado;
    }

    public static boolean esNumero(char[] s) {
        if (s.length == 0) return false;
        for (int i = 0; i < s.length; i++) if (s[i] < '0' || s[i] > '9') return false;
        return true;
    }

    public static boolean esCadena(char[] s) {
        return s.length >= 2 && s[0] == '"' && s[s.length - 1] == '"';
    }

    public static char[] generarAsignacion(char[] destino, char[] valor) {
        char[] res = new char[0];
        res = concatenar(res, "  mov si, offset ".toCharArray());
        res = concatenar(res, destino);
        if (esNumero(valor)) {
            res = concatenar(res, "+2\n  mov byte ptr [si], ".toCharArray());
            res = concatenar(res, valor);
            res = concatenar(res, "\n".toCharArray());
        } else {
            res = concatenar(res, "+2\n  mov di, offset ".toCharArray());
            res = concatenar(res, valor);
            res = concatenar(res, "\n  mov al, byte ptr [di+2]\n".toCharArray());
            res = concatenar(res, "  mov byte ptr [si], al\n".toCharArray());
        }
        return res;
    }

    public static char[] generarAritmetica(char[] destino, char[] var1, char[] var2, char operador) {
        char[] res = new char[0];
        if (!esNumero(var1) && !esNumero(var2)) {
            res = concatenar(res, "  mov si, offset ".toCharArray());
            res = concatenar(res, var1);
            res = concatenar(res, "\n  mov di, offset ".toCharArray());
            res = concatenar(res, var2);
            res = concatenar(res, "\n  call CargarVars\n".toCharArray());
        } else {
            if (esNumero(var1)) {
                res = concatenar(res, "  mov al, ".toCharArray());
                res = concatenar(res, var1);
                res = concatenar(res, "\n".toCharArray());
            } else {
                res = concatenar(res, "  mov si, offset ".toCharArray());
                res = concatenar(res, var1);
                res = concatenar(res, "\n  mov al, [si+2]\n".toCharArray());
            }
            if (esNumero(var2)) {
                res = concatenar(res, "  mov bl, ".toCharArray());
                res = concatenar(res, var2);
                res = concatenar(res, "\n".toCharArray());
            } else {
                res = concatenar(res, "  mov di, offset ".toCharArray());
                res = concatenar(res, var2);
                res = concatenar(res, "\n  mov bl, [di+2]\n".toCharArray());
            }
        }

        if (operador == '+') res = concatenar(res, "  add al, bl\n".toCharArray());
        else if (operador == '-') res = concatenar(res, "  sub al, bl\n".toCharArray());
        else if (operador == '*') res = concatenar(res, "  mul bl\n".toCharArray());
        else if (operador == '/' || operador == '%') {
            res = concatenar(res, "  mov ah, 0\n  div bl\n".toCharArray());
            if (operador == '%') res = concatenar(res, "  mov al, ah\n".toCharArray());
        }
        
        res = concatenar(res, "  mov si, offset ".toCharArray());
        res = concatenar(res, destino);
        res = concatenar(res, "\n  call Guardar\n".toCharArray());
        return res;
    }

    public static char[] generarNombreEtiqueta() {
        char[] nombre = {'E','t','i','q','u','e','t','a','0','0'};
        int n = contadorEtiquetas++;
        nombre[8] = (char)('0' + (n / 10));
        nombre[9] = (char)('0' + (n % 10));
        return nombre;
    }

    public static char[] generarComparacion(char[] condicion, char[] etiquetaSalto) {
        int idxOp = -1; char[] op = new char[0];
        char[][] ops = {{'=','='}, {'!','='}, {'>','='}, {'<','='}, {'>'}, {'<'}};
        for (int i = 0; i < ops.length; i++) {
            int pos = encontrarSubcadena(condicion, ops[i]);
            if (pos != -1) { idxOp = pos; op = ops[i]; break; }
        }
        if (idxOp == -1) return new char[0];
        char[] var1 = limpiarEspacios(extraerSubcadena(condicion, 0, idxOp));
        char[] var2 = limpiarEspacios(extraerSubcadena(condicion, idxOp + op.length, condicion.length));
        
        char[] res = new char[0];
        if (!esNumero(var1) && !esNumero(var2)) {
            res = concatenar(res, "  mov si, offset ".toCharArray());
            res = concatenar(res, var1);
            res = concatenar(res, "\n  mov di, offset ".toCharArray());
            res = concatenar(res, var2);
            res = concatenar(res, "\n  call CargarVars\n".toCharArray());
        } else {
            if (esNumero(var1)) res = concatenar(res, concatenar("  mov al, ".toCharArray(), concatenar(var1, "\n".toCharArray())));
            else res = concatenar(res, concatenar("  mov si, offset ".toCharArray(), concatenar(var1, "\n  mov al, [si+2]\n".toCharArray())));
            
            if (esNumero(var2)) res = concatenar(res, concatenar("  mov bl, ".toCharArray(), concatenar(var2, "\n".toCharArray())));
            else res = concatenar(res, concatenar("  mov di, offset ".toCharArray(), concatenar(var2, "\n  mov bl, [di+2]\n".toCharArray())));
        }
        
        res = concatenar(res, "  cmp al, bl\n".toCharArray());
        char[] jmp;
        if (op.length == 2) {
            if (op[0] == '=' && op[1] == '=') jmp = "jne ".toCharArray();
            else if (op[0] == '!' && op[1] == '=') jmp = "je ".toCharArray();
            else if (op[0] == '>' && op[1] == '=') jmp = "jl ".toCharArray();
            else jmp = "jg ".toCharArray();
        } else {
            if (op[0] == '>') jmp = "jle ".toCharArray();
            else jmp = "jge ".toCharArray();
        }
        res = concatenar(res, "  ".toCharArray());
        res = concatenar(res, jmp);
        res = concatenar(res, etiquetaSalto);
        res = concatenar(res, "\n".toCharArray());
        return res;
    }

    public static int encontrarSubcadena(char[] fuente, char[] busqueda) {
        for (int i = 0; i <= fuente.length - busqueda.length; i++) {
            boolean coincide = true;
            for (int j = 0; j < busqueda.length; j++) if (fuente[i + j] != busqueda[j]) { coincide = false; break; }
            if (coincide) return i;
        }
        return -1;
    }

    public static char[] limpiarEspacios(char[] s) {
        int i = 0; while (i < s.length && (s[i] == ' ' || s[i] == '\t')) i++;
        int f = s.length; while (f > i && (s[f - 1] == ' ' || s[f - 1] == '\t')) f--;
        return extraerSubcadena(s, i, f);
    }

    public static char[] traducirLinea(char[] linea) {
        int idx = encontrarCaracter(linea, 0, linea.length, '=');
        if (idx != -1) {
            char[] dest = limpiarEspacios(extraerSubcadena(linea, 0, idx));
            char[] der = limpiarEspacios(extraerSubcadena(linea, idx + 1, linea.length));
            char[] ops = {'+','-','*','/','%'};
            int idxOp = -1; char op = ' ';
            for (char o : ops) { int p = encontrarCaracter(der, 0, der.length, o); if (p != -1) { idxOp = p; op = o; break; } }
            if (idxOp != -1) {
                return generarAritmetica(dest, limpiarEspacios(extraerSubcadena(der, 0, idxOp)), limpiarEspacios(extraerSubcadena(der, idxOp + 1, der.length)), op);
            } else return generarAsignacion(dest, der);
        }
        return new char[0];
    }
}
