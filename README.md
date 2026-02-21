# TECNOLÓGICO NACIONAL DE MÉXICO  

## CAMPUS OCOTLÁN  

### Ingeniería en Sistemas Computacionales  
### Lenguajes y Autómatas  

**Alumno:** José Miguel Macias Mendoza  
**N° Control:** 23630159  

---

# CASO DE ESTUDIO  

## Diseño Formal del Lenguaje de Programación PRISMA  

---

# 1. ALFABETO (Σ)

Σ = { a-z, A-Z, 0-9, +, -, *, /, %, =, >, <, !, (, ), ,, ", _ }

---

# 2. LENGUAJE (L)

El lenguaje PRISMA es un lenguaje imperativo y estructurado que cumple la siguiente forma general:

MODULO ID  
VARIABLES  
(declaraciones globales)  
FIN-VARIABLES  

(funciones opcionales)  

PRINCIPAL  
(instrucciones)  
FIN-PRINCIPAL  
FIN-MODULO  

---

# 3. GRAMÁTICA (G)

G = (Σ, T, V, P, S)

### Terminales (T)

T = { MODULO, FIN-MODULO, VARIABLES, FIN-VARIABLES, PRINCIPAL,
FIN-PRINCIPAL, FUNCION, FIN-FUNCION, RETORNA, SI, ENTONCES,
SINO, FIN-SI, PARA, FIN-PARA, MIENTRAS, FIN-MIENTRAS,
ENTERO, DECIMAL, TEXTO, BOOLEANO, VACIO, IMPRIME, LEER,
Y, O, NO, +, -, *, /, %, ==, !=, >, <, >=, <=, =, (, ), ,,
ID, NUM_INT, NUM_DEC, CADENA, V, F }

### Variables No Terminales (V)

V = { <tipo>, <declaracion_variable>, <inicializacion>,
<declaraciones_globales>, <parametro>, <mas_parametros>,
<lista_parametros>, <expresion>, <expresion_o>, <cola_o>,
<expresion_y>, <cola_y>, <expresion_relacional>,
<cola_relacional>, <expresion_suma>, <cola_suma>,
<expresion_multiplicacion>, <cola_multiplicacion>,
<expresion_unaria>, <primario>, <continuacion_id>,
<lista_argumentos>, <mas_argumentos>, <asignacion>,
<condicional_si>, <condicional_sino_opcional>,
<ciclo_para>, <ciclo_mientras>, <instruccion>,
<lista_instrucciones>, <bloque>, <retorno_opcional>,
<declaracion_funcion>, <declaraciones_funcion> }

### Símbolo Inicial

S ::= MODULO ID VARIABLES <declaraciones_globales>
FIN-VARIABLES <declaraciones_funcion>
PRINCIPAL <bloque>
FIN-PRINCIPAL FIN-MODULO

---

# 4. PROPOSICIONES DE ENTRADA

LEER '(' ID ')'

---

# 5. PROPOSICIONES DE SALIDA

IMPRIME '(' <expresion> ')'

---

# 6. PROPOSICIONES DE OPERACIÓN ARITMÉTICA

<expresion_suma> ::= <expresion_multiplicacion> <cola_suma>  
<cola_suma> ::= (+ | -) <expresion_multiplicacion> <cola_suma> | ε  
<expresion_multiplicacion> ::= <expresion_unaria> <cola_multiplicacion>  
<cola_multiplicacion> ::= (* | / | %) <expresion_unaria> <cola_multiplicacion> | ε  

---

# 7. PROPOSICIONES CONDICIONALES

<condicional_si> ::= SI '(' <expresion> ')' ENTONCES <bloque>
<condicional_sino_opcional> FIN-SI  

<condicional_sino_opcional> ::= SINO <bloque> | ε  

---

# 8. CICLOS CONTADOS

<ciclo_para> ::= PARA '(' <asignacion> ',' <expresion> ',' <asignacion> ')'
<bloque> FIN-PARA  

---

# 9. ESTRUCTURAS COMPLEMENTARIAS

## Ciclos Condicionales

<ciclo_mientras> ::= MIENTRAS '(' <expresion> ')'
<bloque> FIN-MIENTRAS  

## Declaraciones Globales

<declaracion_variable> ::= <tipo> ID <inicializacion>  
<inicializacion> ::= '=' <expresion> | ε  
<declaraciones_globales> ::= <declaracion_variable> <declaraciones_globales> | ε  
<tipo> ::= ENTERO | DECIMAL | TEXTO | BOOLEANO | VACIO  

## Funciones

<declaracion_funcion> ::= FUNCION <tipo> ID '(' <lista_parametros> ')'
<bloque> <retorno_opcional> FIN-FUNCION  

<retorno_opcional> ::= RETORNA <expresion> | ε  
<lista_parametros> ::= <parametro> <mas_parametros> | ε  
<parametro> ::= <tipo> ID  
<mas_parametros> ::= ',' <parametro> <mas_parametros> | ε  

## Expresiones Lógicas

<expresion> ::= <expresion_o>  
<expresion_o> ::= <expresion_y> <cola_o>  
<cola_o> ::= O <expresion_y> <cola_o> | ε  
<expresion_y> ::= <expresion_relacional> <cola_y>  
<cola_y> ::= Y <expresion_relacional> <cola_y> | ε  
<expresion_relacional> ::= <expresion_suma> <cola_relacional>  
<cola_relacional> ::= (== | != | > | < | >= | <=) <expresion_suma> | ε  
<expresion_unaria> ::= NO <expresion_unaria> | '-' <expresion_unaria> | <primario>  
<primario> ::= ID <continuacion_id> | NUM_INT | NUM_DEC | CADENA | V | F | '(' <expresion> ')'  
<continuacion_id> ::= '(' <lista_argumentos> ')' | ε  
<lista_argumentos> ::= <expresion> <mas_argumentos> | ε  
<mas_argumentos> ::= ',' <expresion> <mas_argumentos> | ε  

## Bloques e Instrucciones

<asignacion> ::= ID '=' <expresion>  

<instruccion> ::= <asignacion>
| <condicional_si>
| <ciclo_para>
| <ciclo_mientras>
| IMPRIME '(' <expresion> ')'
| LEER '(' ID ')'
| RETORNA <expresion>
| ID '(' <lista_argumentos> ')'  

<lista_instrucciones> ::= <instruccion> <lista_instrucciones> | ε  
<bloque> ::= <lista_instrucciones>  

---

# 10. TABLA DE NO TERMINALES

| No Terminal | Descripción |
|-------------|------------|
| `<tipo>` | Tipo de dato permitido |
| `<declaracion_variable>` | Declaración de variable global |
| `<inicializacion>` | Asignación opcional inicial |
| `<declaraciones_globales>` | Conjunto de variables globales |
| `<parametro>` | Parámetro individual de función |
| `<mas_parametros>` | Parámetros adicionales |
| `<lista_parametros>` | Lista completa de parámetros |
| `<expresion>` | Expresión general |
| `<expresion_o>` | Expresión lógica con operador OR |
| `<cola_o>` | Continuación de expresión OR |
| `<expresion_y>` | Expresión lógica con operador AND |
| `<cola_y>` | Continuación de expresión AND |
| `<expresion_relacional>` | Expresión de comparación |
| `<cola_relacional>` | Continuación de expresión relacional |
| `<expresion_suma>` | Expresión de suma y resta |
| `<cola_suma>` | Continuación de suma o resta |
| `<expresion_multiplicacion>` | Multiplicación, división y módulo |
| `<cola_multiplicacion>` | Continuación de multiplicación |
| `<expresion_unaria>` | Operadores unarios |
| `<primario>` | Elemento básico de expresión |
| `<continuacion_id>` | Llamada a función opcional |
| `<lista_argumentos>` | Lista de argumentos |
| `<mas_argumentos>` | Argumentos adicionales |
| `<asignacion>` | Asignación de valor |
| `<condicional_si>` | Estructura condicional SI |
| `<condicional_sino_opcional>` | Parte opcional SINO |
| `<ciclo_para>` | Ciclo contado |
| `<ciclo_mientras>` | Ciclo condicional |
| `<instruccion>` | Instrucción individual |
| `<lista_instrucciones>` | Conjunto de instrucciones |
| `<bloque>` | Bloque de ejecución |
| `<retorno_opcional>` | Retorno opcional en función |
| `<declaracion_funcion>` | Definición de función |
| `<declaraciones_funcion>` | Conjunto de funciones |

---

# 11. GUÍA DE EJECUCIÓN

Para probar el correcto funcionamiento del compilador utilizando el **Analizador Léxico (Consola)** o el **Sintáctico (IDE)**, se sugiere utilizar el siguiente código de ejemplo que cumple con la gramática actual:

### Código de Ejemplo (Prueba PRISMA)
```prisma
MODULO MiProyecto
    VARIABLES
        ENTERO x = 10
        TEXTO saludo = "Hola Mundo"
    FIN-VARIABLES

    FUNCION VACIO Saludar()
        IMPRIME(saludo)
    FIN-FUNCION

    PRINCIPAL
        Saludar()
        SI (x > 5) ENTONCES
            IMPRIME("X es mayor a 5")
        FIN-SI
    FIN-PRINCIPAL
FIN-MODULO
```

### Instrucciones de uso (Analizador Léxico)
1. Abre el archivo `entrada.txt` en la raíz del proyecto.
2. Escribe o modifica el código PRISMA que deseas analizar (ya hay un código de ejemplo listo para usar).
3. Guarda el archivo.
4. Ejecuta la clase `AnalizadorLexico.PruebaPrisma`.
5. El sistema procesará automáticamente el archivo y mostrará los resultados en la consola.


NOTA: EL ANALIZADOR SINTACTICO AUN LE FALTAN COSAS PARA QUE FUNCIONE DEL TODO
