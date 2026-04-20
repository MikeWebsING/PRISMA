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