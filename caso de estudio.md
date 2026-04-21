TECNOLÓGICO NACIONAL DE MÉXICO CAMPUS OCOTLÁN

Ingeniería en Sistemas Computacionales

Lenguajes y Autómatas

Alumno: José Miguel Macias Mendoza

N° Control: 23630159

CASO DE ESTUDIO Diseño Formal del Lenguaje de Programación PRISMA

1. ALFABETO (Σ) Σ = { a-z, A-Z, 0-9, +, -, *, /, %, =, >, <, !, (, ), ,, ", _, >=, <=, ==, != }

2. LENGUAJE (L)  
 El lenguaje PRISMA es un lenguaje imperativo y estructurado que cumple la siguiente forma general: MODULO ID VARIABLES (declaraciones globales) FIN-VARIABLES (funciones opcionales) PRINCIPAL (instrucciones) FIN-PRINCIPAL FIN-MODULO

3. GRAMÁTICA (G) G = (Σ, T, V, P, S)

Terminales (T) T = { MODULO, FIN-MODULO, VARIABLES, FIN-VARIABLES, PRINCIPAL, FIN-PRINCIPAL, FUNCION, FIN-FUNCION, RETORNA, SI, ENTONCES, SINO, FIN-SI, PARA, FIN-PARA, MIENTRAS, FIN-MIENTRAS, ENTERO, DECIMAL, TEXTO, BOOLEANO, VACIO, IMPRIME, LEER, Y, O, NO, +, -, *, /, %, ==, !=, >, <, >=, <=, =, (, ), ,, ID, NUM_INT, NUM_DEC, CADENA, V, F }

Variables No Terminales (V)  
 V = { <programa>, <tipo>, <decl_var>, <init>, <vars_globales>, <funcs>, <decl_func>, <params>, <param>, <mas_params>, <instr>, <instr_id_tail>, <instrs>, <sino_opt>, <ret_opt>, <expr>, <expr_or>, <cola_or>, <expr_and>, <cola_and>, <expr_rel>, <cola_rel>, <rel_op>, <expr_suma>, <cola_suma>, <expr_mult>, <cola_mult>, <expr_unar>, <primario>, <cont_id>, <args>, <mas_args>, <asign> }

Símbolo Inicial S ::= <programa>

<programa> ::= MODULO ID VARIABLES <vars_globales> FIN-VARIABLES <funcs> PRINCIPAL <instrs> FIN-PRINCIPAL FIN-MODULO

### PROPOSICIONES DE ENTRADA

LEER '(' ID ')'

### PROPOSICIONES DE SALIDA

IMPRIME '(' <expr> ')'

### PROPOSICIONES DE OPERACIÓN ARITMÉTICA

<expr_suma> ::= <expr_mult> <cola_suma>

<cola_suma> ::= + <expr_mult> <cola_suma> | - <expr_mult> <cola_suma> | ε

<expr_mult> ::= <expr_unar> <cola_mult>

<cola_mult> ::= * <expr_unar> <cola_mult> | / <expr_unar> <cola_mult> | % <expr_unar> <cola_mult> | ε

### PROPOSICIONES CONDICIONALES

<si> ::= SI '(' <expr> ')' ENTONCES <instrs> <sino_opt> FIN-SI

<sino_opt> ::= SINO <instrs> | ε

### 8. CICLOS CONTADOS

<para> ::= PARA '(' <asign> ',' <expr> ',' <asign> ')' <instrs> FIN-PARA

### ESTRUCTURAS COMPLEMENTARIAS

**Ciclos Condicionales**

<mientras> ::= MIENTRAS '(' <expr> ')' <instrs> FIN-MIENTRAS

**Declaraciones Globales**

<decl_var> ::= <tipo> ID <init>

<init> ::= '=' <expr> | ε

<vars_globales> ::= <decl_var> <vars_globales> | ε

<tipo> ::= ENTERO | DECIMAL | TEXTO | BOOLEANO | VACIO

**Funciones**

<decl_func> ::= FUNCION <tipo> ID '(' <params> ')' <instrs> FIN-FUNCION

<params> ::= <param> <mas_params> | ε

<param> ::= <tipo> ID

<mas_params> ::= ',' <param> <mas_params> | ε

<funcs> ::= <decl_func> <funcs> | ε

**Expresiones Lógicas y Relacionales**

<expr> ::= <expr_or>

<expr_or> ::= <expr_and> <cola_or>

<cola_or> ::= O <expr_and> <cola_or> | ε

<expr_and> ::= <expr_rel> <cola_and>

<cola_and> ::= Y <expr_rel> <cola_and> | ε

<expr_rel> ::= <expr_suma> <cola_rel>

<cola_rel> ::= <rel_op> <expr_suma> <cola_rel> | ε

<rel_op> ::= == | != | > | < | >= | <=

<expr_unar> ::= NO <expr_unar> | '-' <expr_unar> | <primario>

<primario> ::= ID <cont_id> | NUM_INT | NUM_DEC | CADENA | V | F | '(' <expr> ')'

<cont_id> ::= '(' <args> ')' | ε

<args> ::= <expr> <mas_args> | ε

<mas_args> ::= ',' <expr> <mas_args> | ε

**Bloques e Instrucciones (Factorización LL(1))**

<instr> ::= ID <instr_id_tail> | <si> | <para> | <mientras> | IMPRIME '(' <expr> ')' | LEER '(' ID ')' | RETORNA <expr>

<instr_id_tail> ::= '=' <expr> | '(' <args> ')' (Asignación o llamada a función)

<instrs> ::= <instr> <instrs> | ε

<asign> ::= ID '=' <expr>  
  
 TABLA DE TOKENS

|**SENTENCIA**|**DISPARADOR**|**TIPO DE DISPARADOR**|**NUM TOKEN**|
|---|---|---|---|
|**Estructura de Control**||||
|Inicio de Módulo|MODULO|SIMPLE|256|
|Fin de Módulo|FIN-MODULO|SIMPLE|257|
|Sección de Variables|VARIABLES|SIMPLE|290|
|Fin de Variables|FIN-VARIABLES|SIMPLE|291|
|Bloque Principal|PRINCIPAL|SIMPLE|258|
|Fin de Principal|FIN-PRINCIPAL|SIMPLE|259|
|Declaración de Función|FUNCION|SIMPLE|260|
|Fin de Función|FIN-FUNCION|SIMPLE|261|
|Retorno de Valor|RETORNA|SIMPLE|262|
|Condicional|SI|SIMPLE|263|
|Alternativa|SINO|SIMPLE|264|
|Fin de Condicional|FIN-SI|SIMPLE|265|
|Ciclo Acotado|PARA|SIMPLE|266|
|Fin de Ciclo Acotado|FIN-PARA|SIMPLE|267|
|Ciclo Condicional|MIENTRAS|SIMPLE|268|
|Fin de Ciclo Condicional|FIN-MIENTRAS|SIMPLE|269|
|Gatillo de Condición|ENTONCES|SIMPLE|292|
|**Tipos de Datos**||||
|Tipo Entero|ENTERO|SIMPLE|270|
|Tipo Decimal|DECIMAL|SIMPLE|271|
|Tipo Texto|TEXTO|SIMPLE|272|
|Tipo Booleano|BOOLEANO|SIMPLE|273|
|Tipo Vacío|VACIO|SIMPLE|274|
|**Entrada y Salida**||||
|Salida de Datos|IMPRIME|SIMPLE|275|
|Entrada de Datos|LEER|SIMPLE|276|
|**Operadores y Símbolos**||||
|Asignación|=|SIMPLE|61|
|Suma|+|SIMPLE|43|
|Resta|-|SIMPLE|45|
|Multiplicación|*|SIMPLE|42|
|División|/|SIMPLE|47|
|Residuo|%|SIMPLE|37|
|Paréntesis Apertura|(|SIMPLE|40|
|Paréntesis Cierre|)|SIMPLE|41|
|Separador|,|SIMPLE|44|
|Comparación Igualdad|==|SIMPLE|286|
|Comparación Diferente|!=|SIMPLE|287|
|Comparación Mayor o Igual|>=|SIMPLE|288|
|Comparación Menor o Igual|<=|SIMPLE|289|
|Mayor que|>|SIMPLE|62|
|Menor que|<|SIMPLE|60|
|Operación Lógica Y|Y|SIMPLE|277|
|Operación Lógica O|O|SIMPLE|278|
|Negación Lógica|NO|SIMPLE|279|
|**Valores y Dinámicos**||||
|Identificador|[a-z_][a-zA-Z0-9_]*|COMPUESTA|280|
|Número Entero|[0-9]+|COMPUESTA|281|
|Número Decimal|[0-9]+\.[0-9]+|COMPUESTA|282|
|Cadena de Texto|"[^"]*"|COMPUESTA|283|
|Literal Verdadero|V|SIMPLE|284|
|Literal Falso|F|SIMPLE|285|
|**Manejo de Errores**||||
|Error ## EXPRESIONES REGULARES EQUIVALENTES A CADA REGLA DE PRODUCCIÓN

### 1. Estructura Superior (Módulos y Bloques)
- **REGLA DE PRODUCCIÓN:** <programa> ::= MODULO ID VARIABLES <vars_globales> FIN-VARIABLES <funcs> PRINCIPAL <instrs> FIN-PRINCIPAL FIN-MODULO
- **EXPRESIÓN REGULAR:** `^MODULO\s+ID\s+VARIABLES\s+(<decl_var>)*\s+FIN-VARIABLES\s+(<decl_func>)*\s+PRINCIPAL\s+(<instr>)*\s+FIN-PRINCIPAL\s+FIN-MODULO$`

### 2. Tipos de Datos y Declaraciones
- **REGLA DE PRODUCCIÓN:** <tipo> ::= ENTERO | DECIMAL | TEXTO | BOOLEANO | VACIO
- **EXPRESIÓN REGULAR:** `^(ENTERO|DECIMAL|TEXTO|BOOLEANO|VACIO)$`
- **REGLA DE PRODUCCIÓN:** <decl_var> ::= <tipo> ID ( '=' <expr> )?
- **EXPRESIÓN REGULAR:** `^(ENTERO|DECIMAL|TEXTO|BOOLEANO|VACIO)\s+ID(\s*=\s*.+)?$`

### 3. Definición de Funciones
- **REGLA DE PRODUCCIÓN:** <decl_func> ::= FUNCION <tipo> ID '(' <params> ')' <instrs> FIN-FUNCION
- **EXPRESIÓN REGULAR:** `^FUNCION\s+(ENTERO|DECIMAL|TEXTO|BOOLEANO|VACIO)\s+ID\s*\(\s*(<params>)?\s*\)\s*(<instr>)*\s+FIN-FUNCION$`

### 4. Estructuras de Control e Iteración
- **REGLA DE PRODUCCIÓN (SI):** <si> ::= SI '(' <expr> ')' ENTONCES <instrs> ( SINO <instrs> )? FIN-SI
- **EXPRESIÓN REGULAR:** `^SI\s*\(\s*<expr>\s*\)\s*ENTONCES\s*(<instr>)*\s*(SINO\s*(<instr>)*)?\s+FIN-SI$`
- **REGLA DE PRODUCCIÓN (MIENTRAS):** <mientras> ::= MIENTRAS '(' <expr> ')' <instrs> FIN-MIENTRAS
- **EXPRESIÓN REGULAR:** `^MIENTRAS\s*\(\s*<expr>\s*\)\s*(<instr>)*\s+FIN-MIENTRAS$`
- **REGLA DE PRODUCCIÓN (PARA):** <para> ::= PARA '(' <asign> ',' <expr> ',' <asign> ')' <instrs> FIN-PARA
- **EXPRESIÓN REGULAR:** `^PARA\s*\(\s*ID\s*=\s*<expr>\s*,\s*<expr>\s*,\s*ID\s*=\s*<expr>\s*\)\s*(<instr>)*\s+FIN-PARA$`

### 5. Jerarquía de Expresiones Aritméticas y Lógicas
- **REGLA DE PRODUCCIÓN (OR):** <expr_or> ::= <expr_and> ( 'O' <expr_and> )*
- **EXPRESIÓN REGULAR:** `<expr_and>(\s+O\s+<expr_and>)*`
- **REGLA DE PRODUCCIÓN (SUMA/RESTA):** <expr_suma> ::= <expr_mult> ( ( '+' | '-' ) <expr_mult> )*
- **EXPRESIÓN REGULAR:** `<expr_mult>(\s*(\+|\-)\s*<expr_mult>)*`

### 6. Entrada/Salida
- **REGLA DE PRODUCCIÓN (IMPRIME):** IMPRIME '(' <expr> ')'
- **EXPRESIÓN REGULAR:** `^IMPRIME\s*\(\s*<expr>\s*\)$`
- **REGLA DE PRODUCCIÓN (LEER):** LEER '(' ID ')'
- **EXPRESIÓN REGULAR:** `^LEER\s*\(\s*ID\s*\)$`

---

### Resumen de Patrones de Instrucciones 

A continuación se detallan los patrones individuales para validación rápida:

- **MODULO**: `^MODULO\s+[a-z_][a-zA-Z0-9_]*$`
- **VARIABLES**: `^VARIABLES$`
- **FIN-VARIABLES**: `^FIN-VARIABLES$`
- **DECLARACION**: `^(ENTERO|DECIMAL|TEXTO|BOOLEANO|VACIO)\s+[a-z_][a-zA-Z0-9_]*(\s*=\s*.+)?$`
- **IMPRIME**: `^IMPRIME\s*\(.+\)$`
- **LEER**: `^LEER\s*\([a-z_][a-zA-Z0-9_]*\)$`
- **SI**: `^SI\s*\(.+\)\s*ENTONCES$`
- **FIN-SI**: `^FIN-SI$`
- **MIENTRAS**: `^MIENTRAS\s*\(.+\)$`
- **FIN-MIENTRAS**: `^FIN-MIENTRAS$`
- **PARA**: `^PARA\s*\(.+\)$`
- **FIN-PARA**: `^FIN-PARA$`
- **PRINCIPAL**: `^PRINCIPAL$`
- **FIN-PRINCIPAL**: `^FIN-PRINCIPAL$`
- **FIN-MODULO**: `^FIN-MODULO$`
- **FUNCION**: `^FUNCION\s+(ENTERO|DECIMAL|TEXTO|BOOLEANO|VACIO)\s+[a-z_][a-zA-Z0-9_]*\s*\(.*\)$`
- **FIN-FUNCION**: `^FIN-FUNCION$`
- **RETORNA**: `^RETORNA\s+.+$`

### Detalles de Tokens y Palabras Reservadas

|**Token / Palabra**|**Expresión Regular**|
|---|---|
|ID|`[a-z_][a-zA-Z0-9_]*`|
|NUM_INT|`[0-9]+`|
|NUM_DEC|`[0-9]+\.[0-9]+`|
|CADENA|`"[^"]*"`|
|V / F|`(V\|F)`|
|Reservadas|`MODULO, VARIABLES, PRINCIPAL, SI, ENTONCES, SINO, PARA, MIENTRAS, FUNCION, ENTERO, DECIMAL, TEXTO, BOOLEANO, VACIO, IMPRIME, LEER, RETORNA, Y, O, NO`|

---

## 4. ANALIZADOR SEMÁNTICO

El analizador semántico de PRISMA se encarga de validar la lógica del programa, asegurando que las operaciones sean coherentes con los tipos de datos y que las variables se utilicen correctamente dentro de su único alcance global.

### 4.1 Catálogo de Errores Semánticos (Numeración MANUSCRIPT)

A continuación se definen los errores que el compilador debe identificar:

| ID | Error Semántico | Descripción |
| :--- | :--- | :--- |
| **E1** | Variable no declarada | Intento de usar un identificador que no existe en la tabla de símbolos. |
| **E2** | Variable ya declarada | Intento de declarar un identificador que ya existe en el bloque `VARIABLES`. |
| **E3** | Incompatibilidad de tipos | Los tipos en una asignación u operación no coinciden y no hay coerción. |
| **E4** | Expresión no booleana | La condición en un `SI` o `MIENTRAS` no evalúa a un tipo BOOLEANO. |
| **E5** | Función no definida | Intento de llamar a una función que no ha sido declarada. |
| **E6** | Número de argumentos incorrecto | La cantidad de argumentos en la llamada no coincide con la definición. |
| **E7** | Tipos de argumentos incorrectos | Uno o más argumentos en la llamada no coinciden con el tipo esperado. |
| **E8** | Error de retorno | El tipo de la expresión en `RETORNA` no coincide con el tipo de la función. |
| **E9** | Variable de control inválida | En un ciclo `PARA`, la variable de control debe ser numérica (ENTERO/DECIMAL). |
| **E10** | Uso de palabra reservada | Intento de usar una palabra reservada como identificador. |
| **E11** | División por cero | Operación de división o residuo donde el divisor es 0 (validación estática si es literal). |
| **E12** | Función ya definida | Intento de declarar una función con un nombre ya existente. |
| **E13** | Variable no inicializada | Uso de una variable antes de que se le haya asignado un valor (si se implementa tracking). |
| **E14** | Tipo de dato inválido | Uso de un tipo no permitido en un contexto específico. |
| **E15** | Estructura fuera de lugar | Uso de una sentencia (como `RETORNA`) fuera de su bloque permitido. |

### 4.2 Reglas Semánticas (R)

Las reglas que rigen la validez del programa son:

- **R1:** Todas las variables deben declararse obligatoriamente en la sección `VARIABLES... FIN-VARIABLES`.
- **R2:** PRISMA solo maneja un **alcance global**. No existen variables locales dentro de funciones o bloques.
- **R3:** No existe la coerción de tipos. Operaciones entre `ENTERO` y `DECIMAL` deben ser validadas estrictamente (o definidas según el diseño final).
- **R4:** El bloque `PRINCIPAL` es el punto de entrada y debe existir siempre.
- **R5:** Las funciones deben definirse antes de ser llamadas (o registrarse en una primera pasada).
- **R6:** Las constantes literales (NUM_INT, NUM_DEC, CADENA, V, F) tienen un tipo intrínseco.
- **R7:** El operador de asignación `=` solo es válido si el tipo del valor coincide exactamente con el tipo del identificador.

### 4.3 Acciones Semánticas en los Árboles de Sintaxis

Esta sección ilustra cómo se integran las validaciones semánticas en el Árbol de Sintaxis Abstracta (AST) de PRISMA. Cada diagrama muestra el recorrido (inorden/postorden) y los puntos donde se disparan las acciones.

#### Árbol 1: Sentencia de Asignación (a = b + 5 * c)

Este árbol representa una operación de asignación compuesta por una suma y una multiplicación.

![Árbol 1: Asignación](file:///c:/Users/MIKE/Documents/TEC/LENGUAJES%20Y%20AUTOMATAS/PRISMA/temporal/PRISMA/CE%20mike_pagenumber_pages-to-jpg-0007.jpg)

**Acciones Semánticas (AS):**

1.  **En nodos Identificador (a, b, c):**
    *   **AS1:** Buscar el identificador en la Tabla de Símbolos. Si no se encuentra, generar **Error E1** (Variable no declarada).
    *   **AS2:** Recuperar el tipo de dato asociado al identificador para validaciones posteriores.
2.  **En nodo Número Entero (5):**
    *   **AS3:** Validar que el valor esté dentro del rango permitido para un `ENTERO` y asignar dicho tipo al nodo.
3.  **En nodo Multiplicación (*):**
    *   **AS4:** Verificar que tanto el operando izquierdo (`5`) como el derecho (`c`) sean de tipos numéricos compatibles.
    *   **AS5:** Al no existir coerción en PRISMA, si los tipos no coinciden exactamente, generar **Error E3** (Incompatibilidad de tipos).
4.  **En nodo Suma (+):**
    *   **AS6:** Validar la compatibilidad de tipos entre el identificador `b` y el resultado de la multiplicación. Si hay discrepancia, generar **Error E3**.
5.  **En nodo Raíz Asignación (=):**
    *   **AS7:** Verificar que el tipo de dato resultante de toda la expresión derecha coincida estrictamente con el tipo de dato declarado para la variable `a`. De lo contrario, generar **Error E3**.

#### Árbol 2: Condicional (SI-ENTONCES)

Este árbol describe la estructura de una sentencia de control condicional con bloques opcionales.

![Árbol 2: Condicional](file:///c:/Users/MIKE/Documents/TEC/LENGUAJES%20Y%20AUTOMATAS/PRISMA/temporal/PRISMA/CE%20mike_pagenumber_pages-to-jpg-0008.jpg)

**Acciones Semánticas (AS):**

1.  **En nodo Comparación Igualdad (==):**
    *   **AS8:** Evaluar la expresión relacional y asegurar que el resultado sea de tipo `BOOLEANO`.
2.  **En nodo Condicional (SI):**
    *   **AS9:** Validar el nodo de condición. Si el tipo resultante no es `BOOLEANO`, lanzar **Error E4** (Expresión no booleana).
3.  **En bloques ENTONCES / SINO:**
    *   **AS10:** Realizar el análisis semántico de todas las instrucciones contenidas en los bloques (en este ejemplo, la validación del nodo `IMPRIME`).
4.  **En nodo Fin de Condicional (FIN-SI):**
    *   **AS11:** Verificar el cierre correcto de la estructura y liberar o marcar el fin del contexto de control actual.

#### Árbol 3: Estructura de Ciclo (MIENTRAS)

Este árbol representa un bucle condicional que se ejecuta mientras una comparación sea verdadera.

![Árbol 3: Ciclo Mientras](file:///c:/Users/MIKE/Documents/TEC/LENGUAJES%20Y%20AUTOMATAS/PRISMA/temporal/PRISMA/CE%20mike_pagenumber_pages-to-jpg-0009.jpg)

**Acciones Semánticas (AS):**

1.  **En nodo Identificador (i):**
    *   **AS12:** Buscar el identificador `i` en la Tabla de Símbolos. Si no existe, lanzar **Error E1**.
    *   **AS13:** Obtener su tipo de dato.
2.  **En nodo Literal Verdadero (V):**
    *   **AS14:** Asignar el tipo `BOOLEANO` al nodo constante.
3.  **En nodo Comparación Igualdad (==):**
    *   **AS15:** Verificar la compatibilidad entre el tipo de `i` y el tipo `BOOLEANO` del literal `V`.
    *   **AS16:** Establecer el resultado de la comparación como tipo `BOOLEANO`.
4.  **En nodo Ciclo Condicional (MIENTRAS):**
    *   **AS17:** Validar que el resultado del nodo de comparación sea estrictamente `BOOLEANO`. En caso contrario, generar **Error E4**.
5.  **En nodo Bloque Principal (PRINCIPAL):**
    *   **AS18:** Analizar semánticamente todas las instrucciones dentro del cuerpo del bucle.

#### Árbol 4: Sentencia de Salida (IMPRIME)

Este árbol ilustra la validación de una instrucción de salida de datos a consola.

![Árbol 4: Imprime](file:///c:/Users/MIKE/Documents/TEC/LENGUAJES%20Y%20AUTOMATAS/PRISMA/temporal/PRISMA/CE%20mike_pagenumber_pages-to-jpg-0009.jpg)

**Acciones Semánticas (AS):**

1.  **En nodo Cadena de Texto:**
    *   **AS19:** Asignar el tipo de dato `TEXTO` al literal encontrado.
2.  **En nodo Salida de Datos (IMPRIME):**
    *   **AS20:** Verificar que el argumento proporcionado sea una expresión válida (en este caso un literal de texto).
    *   *Nota:* PRISMA permite imprimir tipos básicos (ENTERO, DECIMAL, TEXTO, BOOLEANO). Si el argumento fuera una función sin retorno (`VACIO`), se debería lanzar un error de tipo.
    *   **AS21:** Validar que el valor a imprimir no sea una referencia a una variable no inicializada (si se activa el seguimiento de flujo).

#### Árbol 5: Declaración de Variables

Este árbol representa el proceso de registro de una nueva variable en la tabla de símbolos junto con su inicialización.

![Árbol 5: Declaración](file:///c:/Users/MIKE/Documents/TEC/LENGUAJES%20Y%20AUTOMATAS/PRISMA/temporal/PRISMA/CE%20mike_pagenumber_pages-to-jpg-0008.jpg)

**Acciones Semánticas (AS):**

1.  **En nodo Tipo Entero (ENTERO):**
    *   **AS22:** Definir el tipo de dato que se aplicará a los identificadores que sigan en la declaración.
2.  **En nodo Identificador (x):**
    *   **AS23:** Verificar que el identificador `x` no haya sido declarado previamente en la Tabla de Símbolos. Si ya existe, lanzar **Error E2** (Variable ya declarada).
    *   **AS24:** Insertar el identificador `x` en la Tabla de Símbolos con su tipo correspondiente.
3.  **En nodo Número Entero (0):**
    *   **AS25:** Validar que el valor literal coincida con la categoría numérica.
4.  **En nodo Asignación (=):**
    *   **AS26:** Validar la compatibilidad de tipos entre la variable declarada (`x`) y el valor inicial (`0`). De lo contrario, lanzar **Error E3**.
    *   **AS27:** Marcar la variable como inicializada en la tabla de símbolos para permitir su uso posterior.

#### Árbol 6: Definición de Función

Este árbol muestra la estructura de declaración de una función sin parámetros y sin retorno (VACIO).

![Árbol 6: Función](file:///c:/Users/MIKE/Documents/TEC/LENGUAJES%20Y%20AUTOMATAS/PRISMA/temporal/PRISMA/CE%20mike_pagenumber_pages-to-jpg-0009.jpg)

**Acciones Semánticas (AS):**

1.  **En nodo Identificador (test):**
    *   **AS28:** Verificar si el nombre de la función ya existe en la Tabla de Símbolos. Si existe, lanzar **Error E12** (Función ya definida).
2.  **En nodo Tipo Vacío (VACIO):**
    *   **AS29:** Registrar el tipo de retorno de la función para validar futuras sentencias `RETORNA`.
3.  **En nodo Declaración de Función (FUNCION):**
    *   **AS30:** Insertar el nombre de la función, su tipo de retorno y su lista de parámetros en la Tabla de Símbolos.
4.  **En nodo Fin de Función (FIN-FUNCION):**
    *   **AS31:** Verificar que el flujo de ejecución sea consistente con el tipo de retorno declarado (si no fuera VACIO, requeriría un RETORNA).

#### Árbol 7: Sentencia de Entrada (LEER)

Representa la captura de datos desde una fuente externa hacia una variable.

![Árbol 7: Entrada](file:///c:/Users/MIKE/Documents/TEC/LENGUAJES%20Y%20AUTOMATAS/PRISMA/temporal/PRISMA/CE%20mike_pagenumber_pages-to-jpg-0009.jpg)

**Acciones Semánticas (AS):**

1.  **En nodo Identificador (valor):**
    *   **AS32:** Buscar el identificador `valor` en la Tabla de Símbolos. Si no está declarado, lanzar **Error E1**.
2.  **En nodo Entrada de Datos (LEER):**
    *   **AS33:** Validar que el identificador sea una variable (no una constante o nombre de función).
    *   **AS34:** Marcar la variable como "inicializada" tras la operación de lectura exitosa.

#### Árbol 8: Estructura Global del Programa (Módulo Principal)

Este árbol representa la jerarquía de más alto nivel de un programa en PRISMA, asegurando la existencia de los bloques obligatorios.

![Árbol 8: Estructura Global](file:///c:/Users/MIKE/Documents/TEC/LENGUAJES%20Y%20AUTOMATAS/PRISMA/temporal/PRISMA/CE%20mike_pagenumber_pages-to-jpg-0007.jpg)

**Acciones Semánticas (AS):**

1.  **En nodo Inicio de Módulo (MODULO):**
    *   **AS35:** Inicializar la estructura global de la Tabla de Símbolos y registrar el nombre del módulo.
2.  **En nodo Bloque Principal (PRINCIPAL):**
    *   **AS36:** Validar la existencia obligatoria de este bloque. Si no se encuentra antes del fin del módulo, lanzar un error semántico de estructura faltante.
    *   **AS37:** Iniciar el análisis secuencial de las instrucciones principales del programa.
3.  **En nodo Fin de Módulo (FIN-MODULO):**
    *   **AS38:** Verificar que todas las funciones referenciadas en el programa tengan una definición (Error E5).
    *   **AS39:** Realizar la limpieza de estructuras temporales y finalizar el proceso de análisis semántico global.

#### Árbol 9: Ciclo Acotado (PARA)

Este árbol describe la validación semántica de un bucle con contador, asegurando que la variable de control sea válida.

![Árbol 9: Ciclo Para](file:///c:/Users/MIKE/Documents/TEC/LENGUAJES%20Y%20AUTOMATAS/PRISMA/temporal/PRISMA/CE%20mike_pagenumber_pages-to-jpg-0009.jpg)

**Acciones Semánticas (AS):**

1.  **En nodo Asignación (=) [Inicialización]:**
    *   **AS40:** Verificar que la variable utilizada como control esté declarada en la Tabla de Símbolos (Error E1).
    *   **AS41:** Asegurar que la variable sea de tipo numérico (`ENTERO` o `DECIMAL`). De lo contrario, lanzar **Error E9** (Variable de control inválida).
2.  **En nodo Ciclo Acotado (PARA):**
    *   **AS42:** Validar que las expresiones de límite e incremento sean compatibles con el tipo de la variable de control (Error E3).
3.  **En nodo Bloque Principal (PRINCIPAL):**
    *   **AS43:** Analizar semánticamente las instrucciones dentro del cuerpo del bucle.
4.  **En nodo Fin de Ciclo Acotado (FIN-PARA):**
    *   **AS44:** Validar el cierre de la estructura y asegurar que la variable de control no haya sido alterada semánticamente de forma ilegal (opcional).

#### Árbol 10: Retorno de Función (RETORNA)

Este árbol representa la validación semántica de la devolución de un valor dentro de una función.

![Árbol 10: Retorna](file:///c:/Users/MIKE/Documents/TEC/LENGUAJES%20Y%20AUTOMATAS/PRISMA/temporal/PRISMA/CE%20mike_pagenumber_pages-to-jpg-0010.jpg)

**Acciones Semánticas (AS):**

1.  **En nodo Número Entero (0):**
    *   **AS45:** Evaluar el tipo de dato de la expresión resultante (en este ejemplo, `ENTERO`).
2.  **En nodo Retorno de Valor (RETORNA):**
    *   **AS46:** Verificar que la sentencia `RETORNA` se encuentre dentro del ámbito de una `FUNCION`. Si se usa en el bloque `PRINCIPAL`, lanzar **Error E15** (Estructura fuera de lugar).
    *   **AS47:** Validar que el tipo de la expresión de retorno coincida exactamente con el tipo de retorno declarado en la cabecera de la función actual. En caso de discrepancia, generar **Error E8** (Error de retorno).

#### Árbol 11: Expresiones Lógicas Complejas (Y, O)

Este árbol ilustra cómo se validan las operaciones lógicas que combinan comparaciones relacionales.

![Árbol 11: Expresiones Lógicas](file:///c:/Users/MIKE/Documents/TEC/LENGUAJES%20Y%20AUTOMATAS/PRISMA/temporal/PRISMA/CE%20mike_pagenumber_pages-to-jpg-0011.jpg)

**Acciones Semánticas (AS):**

1.  **En nodos relacionales (<, >):**
    *   **AS48:** Verificar la declaración de los identificadores (`a`, `b`) en la Tabla de Símbolos.
    *   **AS49:** Validar que los operandos sean de tipo numérico (`ENTERO`/`DECIMAL`). Si se intenta comparar tipos incompatibles, lanzar **Error E3**.
    *   **AS50:** Establecer el tipo resultante de estos nodos como `LOGICO`.
2.  **En nodo de operación lógica (Y):**
    *   **AS51:** Verificar que ambos hijos (izquierdo y derecho) sean estrictamente de tipo `LOGICO`. Si alguno de los operandos no es resultado de una comparación o variable lógica, lanzar **Error E7** (Incompatibilidad en operador lógico).
    *   **AS52:** Propagar el tipo `LOGICO` hacia el nodo superior para posibles evaluaciones anidadas.

### 5. Generación de Código Intermedio

Para PRISMA, se utilizará una estrategia de **P-CODE** (Código para Máquina de Pila) debido a su simplicidad y facilidad de mapeo desde los árboles de sintaxis mediante un recorrido en **post-orden**.

#### 5.1. Conjunto de Instrucciones (Instrucciones Base)

| Operación | Descripción |
| :--- | :--- |
| `LOD <id>` | Carga el valor de la variable `<id>` en la cima de la pila. |
| `STR <id>` | Almacena el valor de la cima de la pila en la variable `<id>`. |
| `LIT <val>` | Carga una constante literal en la cima de la pila. |
| `ADD`, `SUB`, `MUL`, `DIV` | Realiza la operación aritmética con los dos valores superiores y guarda el resultado. |
| `CMP <op>` | Compara los dos valores superiores según el operador (`<`, `>`, `<=`, `>=`, `==`, `!=`). |
| `JMP <lab>` | Salto incondicional a la etiqueta `<lab>`. |
| `JIF <lab>` | Salto condicional si la cima de la pila es FALSO. |
| `CALL <fun>` | Llama a una función definida. |
| `RET` | Retorna de una función. |
| `PRN` | Imprime el valor en la cima de la pila. |
| `RD` | Lee un dato y lo coloca en la cima de la pila. |

#### 5.2. Estrategia de Implementación
El Analizador Semántico, al validar cada nodo (AS), enviará la señal al Generador de Código para emitir la instrucción correspondiente tras procesar sus hijos. Por ejemplo, para una asignación `x = 5 + 3`:
1. `LIT 5`
2. `LIT 3`
3. `ADD`
4. `STR x`

### 6. Conclusión y Próximos Pasos

Con la finalización de la **Fase 4 (Análisis Semántico)** y la definición de la **Estrategia de Código Intermedio**, la formalización del lenguaje PRISMA está completa. 

**Estado Actual:**
*   [x] Gramática y Autómatas definidos.
*   [x] Catálogo de errores E1-E15 establecido.
*   [x] 52 Acciones Semánticas (AS) documentadas.
*   [x] Estructura de Tabla de Símbolos y Código Intermedio lista.

**Próximo Paso:** Iniciar la **Fase de Codificación** del compilador en el lenguaje objetivo, comenzando por la infraestructura de la Tabla de Símbolos y la integración de las acciones semánticas en el Parser.
