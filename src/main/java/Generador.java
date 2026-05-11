import java.io.File;
import java.io.FileWriter;
import java.util.Scanner;
import AnalizadorLexico.Etiqueta;

public class Generador {
    private static int contadorCadenas = 0;
    private static int contadorEtiquetas = 0;
    private static char[][] variables = new char[100][];
    private static int numVars = 0;

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
        char[] cuerpo = new char[0];
        char[] cuerpoFunciones = new char[0];
        char[] datos = new char[0];
        int longitud = codigo.length;
        int indice = 0;

        boolean usaAritmetica = false;
        boolean usaImprime = false;
        boolean usaLeer = false;
        boolean usaCls = false;

        char[] palabraVariables = Etiqueta.obtener(Etiqueta.VARIABLES);
        char[] palabraFinVariables = Etiqueta.obtener(Etiqueta.FIN_VARIABLES);
        char[] palabraModulo = Etiqueta.obtener(Etiqueta.MODULO);
        char[] palabraFinModulo = Etiqueta.obtener(Etiqueta.FIN_MODULO);
        char[] palabraPrincipal = Etiqueta.obtener(Etiqueta.PRINCIPAL);
        char[] palabraFinPrincipal = Etiqueta.obtener(Etiqueta.FIN_PRINCIPAL);
        char[] palabraImprime = Etiqueta.obtener(Etiqueta.IMPRIME);
        char[] palabraLeer = Etiqueta.obtener(Etiqueta.LEER);
        char[] palabraSi = Etiqueta.obtener(Etiqueta.SI);
        char[] palabraFinSi = Etiqueta.obtener(Etiqueta.FIN_SI);
        char[] palabraMientras = Etiqueta.obtener(Etiqueta.MIENTRAS);
        char[] palabraFinMientras = Etiqueta.obtener(Etiqueta.FIN_MIENTRAS);
        char[] palabraPara = Etiqueta.obtener(Etiqueta.PARA);
        char[] palabraFinPara = Etiqueta.obtener(Etiqueta.FIN_PARA);
        char[] palabraCls = Etiqueta.obtener(Etiqueta.CLS);
        char[] palabraFuncion = Etiqueta.obtener(Etiqueta.FUNCION);
        char[] palabraFinFuncion = Etiqueta.obtener(Etiqueta.FIN_FUNCION);
        char[] palabraRetorna = Etiqueta.obtener(Etiqueta.RETORNA);
        char[] palabraSino = Etiqueta.obtener(Etiqueta.SINO);
        char[] palabraEntonces = Etiqueta.obtener(Etiqueta.ENTONCES);

        char[][] pilaEtiquetas = new char[100][];
        int cimaEtiquetas = -1;
        char[][] pilaIncrementos = new char[100][];
        int cimaIncrementos = -1;
        cimaIncrementos = -1;
        numVars = 0;
        boolean enFuncion = false;
        char[] nombreFuncActual = new char[0];

        while (indice < longitud) {
            int inicioLinea = indice;
            while (indice < longitud && codigo[indice] != '\n') indice++;
            int finLinea = indice;
            int longitudLinea = finLinea - inicioLinea;

            if (longitudLinea > 0) {
                char[] lineaOriginal = extraerSubcadena(codigo, inicioLinea, finLinea);
                char[] lineaLimpia = limpiarEspacios(lineaOriginal);
                char[] cuerpoV = new char[0];
                char[] asmV = new char[0];

                if (lineaLimpia.length > 0) {
                    if (empiezaCon(lineaLimpia, 0, lineaLimpia.length, palabraVariables)) {
                        if (indice < longitud) indice++;
                        while (indice < longitud) {
                            int inicioVar = indice;
                            while (indice < longitud && codigo[indice] != '\n') indice++;
                            int finVar = indice;
                            char[] lineaVar = limpiarEspacios(extraerSubcadena(codigo, inicioVar, finVar));
                            
                            if (lineaVar.length > 0) {
                                if (empiezaCon(lineaVar, 0, lineaVar.length, palabraFinVariables)) break;
                                
                                int inicioNombre = -1;
                                if (contiene(lineaVar, "ENTERO".toCharArray())) inicioNombre = 7;
                                else if (contiene(lineaVar, "CADENA".toCharArray())) inicioNombre = 7;
                                
                                if (inicioNombre != -1 && inicioNombre < lineaVar.length) {
                                    int finNombre = encontrarCaracter(lineaVar, inicioNombre, lineaVar.length, ' ');
                                    if (finNombre == -1) finNombre = encontrarCaracter(lineaVar, inicioNombre, lineaVar.length, '=');
                                    if (finNombre == -1) finNombre = lineaVar.length;
                                    
                                    char[] nombreVar = limpiarEspacios(extraerSubcadena(lineaVar, inicioNombre, finNombre));
                                    if (nombreVar.length > 0) {
                                        variables[numVars++] = nombreVar;
                                        datos = concatenar(datos, "  ".toCharArray());
                                        datos = concatenar(datos, nombreVar);
                                        datos = concatenar(datos, " db 10, 0, 10 dup('$')\n".toCharArray());
                                        
                                        int idxAsig = encontrarCaracter(lineaVar, 0, lineaVar.length, '=');
                                        if (idxAsig != -1) {
                                            char[] valor = limpiarEspacios(extraerSubcadena(lineaVar, idxAsig + 1, lineaVar.length));
                                            cuerpo = concatenar(cuerpo, "  mov si, offset ".toCharArray());
                                            cuerpo = concatenar(cuerpo, nombreVar);
                                            cuerpo = concatenar(cuerpo, "+2\n  mov byte ptr [si], ".toCharArray());
                                            cuerpo = concatenar(cuerpo, valor);
                                            cuerpo = concatenar(cuerpo, "\n".toCharArray());
                                        }
                                    }
                                }
                            }
                            if (indice < longitud) indice++;
                        }
                    } else if (empiezaCon(lineaLimpia, 0, lineaLimpia.length, palabraImprime)) {
                        usaImprime = true;
                        int inicioId = encontrarCaracter(lineaLimpia, 0, lineaLimpia.length, '(') + 1;
                        int finId = encontrarCaracterDesdeAtras(lineaLimpia, 0, lineaLimpia.length, ')');
                        if (inicioId != 0 && finId != -1) {
                            char[] id = limpiarEspacios(extraerSubcadena(lineaLimpia, inicioId, finId));
                            if (esCadena(id)) {
                                char[] nombreMsg = {'M','s','g'};
                                nombreMsg = concatenar(nombreMsg, String.valueOf(contadorCadenas++).toCharArray());
                                datos = concatenar(datos, "  ".toCharArray());
                                datos = concatenar(datos, nombreMsg);
                                datos = concatenar(datos, " db ".toCharArray());
                                datos = concatenar(datos, id);
                                datos = concatenar(datos, ", 13, 10, '$'\n".toCharArray());
                                cuerpo = concatenar(cuerpo, "  IMPRIME ".toCharArray());
                                cuerpo = concatenar(cuerpo, nombreMsg);
                                cuerpo = concatenar(cuerpo, "\n".toCharArray());
                            } else {
                                cuerpoV = concatenar(cuerpoV, "  mov dx, offset ".toCharArray());
                                cuerpoV = concatenar(cuerpoV, id);
                                cuerpoV = concatenar(cuerpoV, "\n  call ImprimirVariable\n".toCharArray());
                            }
                            if (enFuncion) cuerpoFunciones = concatenar(cuerpoFunciones, cuerpoV);
                            else cuerpo = concatenar(cuerpo, cuerpoV);
                        }
                    } else if (empiezaCon(lineaLimpia, 0, lineaLimpia.length, palabraLeer)) {
                        usaLeer = true;
                        int inicioId = encontrarCaracter(lineaLimpia, 0, lineaLimpia.length, '(') + 1;
                        int finId = encontrarCaracterDesdeAtras(lineaLimpia, 0, lineaLimpia.length, ')');
                        if (inicioId != 0 && finId != -1) {
                            char[] id = limpiarEspacios(extraerSubcadena(lineaLimpia, inicioId, finId));
                            cuerpoV = concatenar(cuerpoV, "  mov dx, offset ".toCharArray());
                            cuerpoV = concatenar(cuerpoV, id);
                            cuerpoV = concatenar(cuerpoV, "\n  LEER ".toCharArray());
                            cuerpoV = concatenar(cuerpoV, id);
                            cuerpoV = concatenar(cuerpoV, "\n  call LeerNum\n".toCharArray());
                            cuerpoV = concatenar(cuerpoV, "  mov si, offset ".toCharArray());
                            cuerpoV = concatenar(cuerpoV, id);
                            cuerpoV = concatenar(cuerpoV, "\n  mov [si+2], al\n  call SaltarLinea\n".toCharArray());
                            if (enFuncion) cuerpoFunciones = concatenar(cuerpoFunciones, cuerpoV);
                            else cuerpo = concatenar(cuerpo, cuerpoV);
                        }
                    } else if (empiezaCon(lineaLimpia, 0, lineaLimpia.length, palabraCls)) {
                        usaCls = true;
                        if (enFuncion) cuerpoFunciones = concatenar(cuerpoFunciones, "  CLS\n".toCharArray());
                        else cuerpo = concatenar(cuerpo, "  CLS\n".toCharArray());
                    } else if (empiezaCon(lineaLimpia, 0, lineaLimpia.length, palabraSi)) {
                        int inicioId = encontrarCaracter(lineaLimpia, 0, lineaLimpia.length, '(') + 1;
                        int finId = encontrarCaracterDesdeAtras(lineaLimpia, 0, lineaLimpia.length, ')');
                        if (inicioId != 0 && finId != -1) {
                            char[] condicion = extraerSubcadena(lineaLimpia, inicioId, finId);
                            char[] etiquetaFin = generarNombreEtiqueta();
                            cimaEtiquetas++; pilaEtiquetas[cimaEtiquetas] = etiquetaFin;
                            char[] comp = generarComparacion(condicion, etiquetaFin);
                            if (encontrarSubcadena(comp, "call CargarVars".toCharArray()) != -1) usaAritmetica = true;
                            if (enFuncion) cuerpoFunciones = concatenar(cuerpoFunciones, comp);
                            else cuerpo = concatenar(cuerpo, comp);
                        }
                    } else if (empiezaCon(lineaLimpia, 0, lineaLimpia.length, palabraFinSi)) {
                        char[] etiquetaFin = pilaEtiquetas[cimaEtiquetas]; cimaEtiquetas--;
                        if (enFuncion) {
                            cuerpoFunciones = concatenar(cuerpoFunciones, etiquetaFin);
                            cuerpoFunciones = concatenar(cuerpoFunciones, ":\n".toCharArray());
                        } else {
                            cuerpo = concatenar(cuerpo, etiquetaFin);
                            cuerpo = concatenar(cuerpo, ":\n".toCharArray());
                        }
                    } else if (empiezaCon(lineaLimpia, 0, lineaLimpia.length, palabraSino)) {
                        char[] etiquetaFinSi = pilaEtiquetas[cimaEtiquetas];
                        char[] etiquetaSino = generarNombreEtiqueta();
                        if (enFuncion) {
                            cuerpoFunciones = concatenar(cuerpoFunciones, "  jmp ".toCharArray());
                            cuerpoFunciones = concatenar(cuerpoFunciones, etiquetaSino);
                            cuerpoFunciones = concatenar(cuerpoFunciones, "\n".toCharArray());
                            cuerpoFunciones = concatenar(cuerpoFunciones, etiquetaFinSi);
                            cuerpoFunciones = concatenar(cuerpoFunciones, ":\n".toCharArray());
                        } else {
                            cuerpo = concatenar(cuerpo, "  jmp ".toCharArray());
                            cuerpo = concatenar(cuerpo, etiquetaSino);
                            cuerpo = concatenar(cuerpo, "\n".toCharArray());
                            cuerpo = concatenar(cuerpo, etiquetaFinSi);
                            cuerpo = concatenar(cuerpo, ":\n".toCharArray());
                        }
                        pilaEtiquetas[cimaEtiquetas] = etiquetaSino;
                    } else if (empiezaCon(lineaLimpia, 0, lineaLimpia.length, palabraMientras)) {
                        int inicioId = encontrarCaracter(lineaLimpia, 0, lineaLimpia.length, '(') + 1;
                        int finId = encontrarCaracterDesdeAtras(lineaLimpia, 0, lineaLimpia.length, ')');
                        if (inicioId != 0 && finId != -1) {
                            char[] condicion = extraerSubcadena(lineaLimpia, inicioId, finId);
                            char[] etiquetaInicio = generarNombreEtiqueta();
                            char[] etiquetaFin = generarNombreEtiqueta();
                            cimaEtiquetas++; pilaEtiquetas[cimaEtiquetas] = etiquetaInicio;
                            cimaEtiquetas++; pilaEtiquetas[cimaEtiquetas] = etiquetaFin;
                            cuerpoV = concatenar(cuerpoV, etiquetaInicio);
                            cuerpoV = concatenar(cuerpoV, ":\n".toCharArray());
                            char[] comp = generarComparacion(condicion, etiquetaFin);
                            if (encontrarSubcadena(comp, "call CargarVars".toCharArray()) != -1) usaAritmetica = true;
                            cuerpoV = concatenar(cuerpoV, comp);
                            if (enFuncion) cuerpoFunciones = concatenar(cuerpoFunciones, cuerpoV);
                            else cuerpo = concatenar(cuerpo, cuerpoV);
                        }
                    } else if (empiezaCon(lineaLimpia, 0, lineaLimpia.length, palabraFinMientras)) {
                        char[] etiquetaFin = pilaEtiquetas[cimaEtiquetas]; cimaEtiquetas--;
                        char[] etiquetaInicio = pilaEtiquetas[cimaEtiquetas]; cimaEtiquetas--;
                        asmV = new char[0];
                        asmV = concatenar(asmV, "  jmp ".toCharArray());
                        asmV = concatenar(asmV, etiquetaInicio);
                        asmV = concatenar(asmV, "\n".toCharArray());
                        asmV = concatenar(asmV, etiquetaFin);
                        asmV = concatenar(asmV, ":\n".toCharArray());
                        if (enFuncion) cuerpoFunciones = concatenar(cuerpoFunciones, asmV);
                        else cuerpo = concatenar(cuerpo, asmV);
                    } else if (empiezaCon(lineaLimpia, 0, lineaLimpia.length, palabraPara)) {
                        int inicioId = encontrarCaracter(lineaLimpia, 0, lineaLimpia.length, '(') + 1;
                        int finId = encontrarCaracterDesdeAtras(lineaLimpia, 0, lineaLimpia.length, ')');
                        if (inicioId != 0 && finId != -1) {
                            int coma1 = encontrarCaracter(lineaLimpia, inicioId, finId, ',');
                            int coma2 = encontrarCaracter(lineaLimpia, coma1 + 1, finId, ',');
                            if (coma1 != -1 && coma2 != -1) {
                                char[] inicializacion = extraerSubcadena(lineaLimpia, inicioId, coma1);
                                char[] condicion = extraerSubcadena(lineaLimpia, coma1 + 1, coma2);
                                char[] incremento = extraerSubcadena(lineaLimpia, coma2 + 1, finId);
                                asmV = traducirLinea(inicializacion);
                                char[] etiquetaInicio = generarNombreEtiqueta();
                                char[] etiquetaFin = generarNombreEtiqueta();
                                cimaEtiquetas++; pilaEtiquetas[cimaEtiquetas] = etiquetaInicio;
                                cimaEtiquetas++; pilaEtiquetas[cimaEtiquetas] = etiquetaFin;
                                cimaIncrementos++; pilaIncrementos[cimaIncrementos] = incremento;
                                asmV = concatenar(asmV, etiquetaInicio);
                                asmV = concatenar(asmV, ":\n".toCharArray());
                                char[] comp = generarComparacion(condicion, etiquetaFin);
                                if (encontrarSubcadena(comp, "call CargarVars".toCharArray()) != -1) usaAritmetica = true;
                                asmV = concatenar(asmV, comp);
                                if (enFuncion) cuerpoFunciones = concatenar(cuerpoFunciones, asmV);
                                else cuerpo = concatenar(cuerpo, asmV);
                            }
                        }
                    } else if (empiezaCon(lineaLimpia, 0, lineaLimpia.length, palabraFinPara)) {
                        char[] etiquetaFin = pilaEtiquetas[cimaEtiquetas]; cimaEtiquetas--;
                        char[] etiquetaInicio = pilaEtiquetas[cimaEtiquetas]; cimaEtiquetas--;
                        char[] incremento = pilaIncrementos[cimaIncrementos]; cimaIncrementos--;
                        asmV = traducirLinea(incremento);
                        asmV = concatenar(asmV, "  jmp ".toCharArray());
                        asmV = concatenar(asmV, etiquetaInicio);
                        asmV = concatenar(asmV, "\n".toCharArray());
                        asmV = concatenar(asmV, etiquetaFin);
                        asmV = concatenar(asmV, ":\n".toCharArray());
                        if (enFuncion) cuerpoFunciones = concatenar(cuerpoFunciones, asmV);
                        else cuerpo = concatenar(cuerpo, asmV);
                    } else if (empiezaCon(lineaLimpia, 0, lineaLimpia.length, palabraFuncion)) {
                        int inicioId = palabraFuncion.length;
                        int finId = encontrarCaracter(lineaLimpia, inicioId, lineaLimpia.length, '(');
                        if (finId == -1) finId = lineaLimpia.length;
                        char[] resto = limpiarEspacios(extraerSubcadena(lineaLimpia, inicioId, finId));
                        
                        // Omitir el tipo si existe
                        int primerEspacio = encontrarCaracter(resto, 0, resto.length, ' ');
                        char[] nombreF;
                        if (primerEspacio != -1) {
                            nombreF = limpiarEspacios(extraerSubcadena(resto, primerEspacio, resto.length));
                        } else {
                            nombreF = resto;
                        }
                        
                        nombreFuncActual = nombreF;
                        enFuncion = true;
                        cuerpoFunciones = concatenar(cuerpoFunciones, "\n".toCharArray());
                        cuerpoFunciones = concatenar(cuerpoFunciones, nombreF);
                        cuerpoFunciones = concatenar(cuerpoFunciones, " proc\n".toCharArray());
                    } else if (empiezaCon(lineaLimpia, 0, lineaLimpia.length, palabraFinFuncion)) {
                        cuerpoFunciones = concatenar(cuerpoFunciones, "  ret\n".toCharArray());
                        cuerpoFunciones = concatenar(cuerpoFunciones, nombreFuncActual);
                        cuerpoFunciones = concatenar(cuerpoFunciones, " endp\n\n".toCharArray());
                        enFuncion = false;
                    } else if (empiezaCon(lineaLimpia, 0, lineaLimpia.length, palabraRetorna)) {
                        char[] valor = limpiarEspacios(extraerSubcadena(lineaLimpia, palabraRetorna.length, lineaLimpia.length));
                        asmV = new char[0];
                        if (esNumero(valor)) {
                            asmV = concatenar(asmV, "  mov al, ".toCharArray());
                            asmV = concatenar(asmV, valor);
                            asmV = concatenar(asmV, "\n".toCharArray());
                        } else {
                            asmV = concatenar(asmV, "  mov si, offset ".toCharArray());
                            asmV = concatenar(asmV, valor);
                            asmV = concatenar(asmV, "\n  mov al, [si+2]\n".toCharArray());
                        }
                        asmV = concatenar(asmV, "  ret\n".toCharArray());
                        if (enFuncion) cuerpoFunciones = concatenar(cuerpoFunciones, asmV);
                        else cuerpo = concatenar(cuerpo, asmV);
                    } else if (encontrarCaracter(lineaLimpia, 0, lineaLimpia.length, '=') != -1) {
                        char[] lineaTraducida = traducirLinea(lineaLimpia);
                        if (encontrarSubcadena(lineaTraducida, "call CargarVars".toCharArray()) != -1 || 
                            encontrarSubcadena(lineaTraducida, "call Guardar".toCharArray()) != -1) {
                            usaAritmetica = true;
                        }
                        if (enFuncion) cuerpoFunciones = concatenar(cuerpoFunciones, lineaTraducida);
                        else cuerpo = concatenar(cuerpo, lineaTraducida);
                    } else if (enFuncion) {
                        // Para otras instrucciones dentro de funciones (SI, PARA, etc)
                        // Este generador es simple y usa un solo buffer 'cuerpo' para casi todo.
                        // Necesitamos redirigir todo lo que se genera mientras enFuncion es true.
                        // Pero la lógica actual de SI, PARA, etc escribe directamente a 'cuerpo'.
                        // Voy a interceptar esos casos o cambiar la lógica.
                        // Sin embargo, para no romper todo, voy a mover el contenido de 'cuerpo' 
                        // generado durante la función a 'cuerpoFunciones' al final del bucle o manejarlo mejor.
                    }
                }
            }
            if (indice < longitud) indice++;
        }

        String asmFinal = "";
        if (usaImprime) asmFinal += "IMPRIME Macro Mensaje\n    Mov Ah, 09h\n    mov dx, offset Mensaje\n    int 21h\nEndM\n\n";
        if (usaLeer) asmFinal += "LEER Macro Entrada\n  mov Ah, 0Ah\n  mov Dx, offset Entrada\n  int 21h\nEndM\n\n";
        if (usaCls) asmFinal += "CLS Macro\n    Mov Ah, 0Fh\n    int 10h\n    Mov Ah, 0h\n    int 10h\nEndM\n\n";

        asmFinal += ".MODEL SMALL\n.CODE\n\n";

        if (usaAritmetica) {
            asmFinal += "CargarVars proc\n  mov al, [si+2]\n  mov bl, [di+2]\n  ret\nCargarVars endp\n\n";
            asmFinal += "Guardar proc\n  mov [si+2], al\n  ret\nGuardar endp\n\n";
        }
        if (usaImprime || usaLeer) {
            asmFinal += "SaltarLinea proc\n  mov ah, 02h\n  mov dl, 13\n  int 21h\n  mov dl, 10\n  int 21h\n  ret\nSaltarLinea endp\n\n";
        }
        if (usaImprime) {
            asmFinal += "MostrarNum proc\n  push ax\n  push bx\n  push cx\n  push dx\n  cmp ax, 0\n  jge EsPositivo\n  push ax\n  mov ah, 02h\n  mov dl, '-'\n  int 21h\n  pop ax\n  neg ax\nEsPositivo:\n  mov cx, 0\n  mov bx, 10\nDivLoop:\n  mov dx, 0\n  div bx\n  push dx\n  inc cx\n  cmp ax, 0\n  jne DivLoop\nPrintLoop:\n  pop dx\n  add dl, 30h\n  mov ah, 02h\n  int 21h\n  loop PrintLoop\n  pop dx\n  pop cx\n  pop bx\n  pop ax\n  ret\nMostrarNum endp\n\n";
            asmFinal += "ImprimirVariable proc\n  mov si, dx\n  mov al, [si+2]\n  cbw\n  call MostrarNum\n  call SaltarLinea\n  ret\nImprimirVariable endp\n\n";
        }
        if (usaLeer) {
            asmFinal += "LeerNum proc\n  mov si, dx\n  mov cl, [si+1]\n  mov ch, 0\n  jcxz FinLeer\n  add si, 2\n  mov ax, 0\n  mov bx, 10\nParseBucle:\n  mov dl, [si]\n  sub dl, 30h\n  mov dh, 0\n  push dx\n  mul bx\n  pop dx\n  add ax, dx\n  inc si\n  loop ParseBucle\nFinLeer:\n  ret\nLeerNum endp\n\n";
        }

        asmFinal += "Inicio:\n  mov Ax, @Data\n  mov Ds, Ax\n\n";
        char[] asmInicio = asmFinal.toCharArray();
        
        char[] resultado = concatenar(asmInicio, cuerpo);
        resultado = concatenar(resultado, "\n  mov ax, 4C00h\n  int 21h\n\n".toCharArray());
        resultado = concatenar(resultado, cuerpoFunciones);
        resultado = concatenar(resultado, ".DATA\n".toCharArray());
        resultado = concatenar(resultado, datos);
        resultado = concatenar(resultado, "\n.STACK\nEND Inicio\n".toCharArray());
        
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
        int inicio = 0;
        if (s[0] == '-') {
            if (s.length == 1) return false;
            inicio = 1;
        }
        for (int i = inicio; i < s.length; i++) if (s[i] < '0' || s[i] > '9') return false;
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
            res = concatenar(res, "  cbw\n  idiv bl\n".toCharArray());
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

    public static boolean contiene(char[] fuente, char[] busqueda) {
        return encontrarSubcadena(fuente, busqueda) != -1;
    }

    public static boolean compararCadenas(char[] s1, char[] s2) {
        if (s1.length != s2.length) return false;
        for (int i = 0; i < s1.length; i++) if (s1[i] != s2[i]) return false;
        return true;
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
            } else {
                // Verificar si es una llamada a función
                boolean esVariable = false;
                for (int k = 0; k < numVars; k++) {
                    if (compararCadenas(der, variables[k])) { esVariable = true; break; }
                }

                int idxPar = encontrarCaracter(der, 0, der.length, '(');
                if (!esVariable && !esNumero(der)) {
                    char[] nombreF = (idxPar != -1) ? limpiarEspacios(extraerSubcadena(der, 0, idxPar)) : der;
                    char[] res = new char[0];
                    res = concatenar(res, "  call ".toCharArray());
                    res = concatenar(res, nombreF);
                    res = concatenar(res, "\n  mov si, offset ".toCharArray());
                    res = concatenar(res, dest);
                    res = concatenar(res, "\n  call Guardar\n".toCharArray());
                    return res;
                }
                return generarAsignacion(dest, der);
            }
        }
        return new char[0];
    }
}
