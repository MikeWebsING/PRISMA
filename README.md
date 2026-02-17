<div align="center">

# TECNOLÓGICO NACIONAL DE MÉXICO
## CAMPUS OCOTLÁN

<img src="https://upload.wikimedia.org/wikipedia/commons/d/d4/Logo-TecNM-2017.png" width="200" alt="TecNM Logo">

### Ingeniería en Sistemas Computacionales
### Lenguajes y Autómatas

**Alumno:** Jose Miguel Macias Mendoza  
**N° Control:** 23630159

---

</div>

## 📝 Descripción del Proyecto

Este proyecto consiste en el desarrollo del compilador para el lenguaje **PRISMA**. El contenido y la codificación actual están basados en las metodologías y estructuras propuestas en el libro **"Compiladores: Principios, técnicas y herramientas"** (The Dragon Book) de Aho, Lam, Sethi y Ullman.

El objetivo es aplicar los conceptos fundamentales de análisis léxico, sintáctico y semántico para la construcción de un lenguaje imperativo y modular.

---

## 🚀 Guía de Ejecución

El compilador cuenta con dos componentes principales que pueden ejecutarse de forma independiente:

### 1. Analizador Sintáctico (Interfaz Gráfica)
Es la forma principal de utilizar el compilador actualmente. Abre un IDE con resaltado de sintaxis y permite validar el código PRISMA completo.

- **Clase Principal:** `AnalizadorSintactico.Principal`
- **Instrucciones:**
  1. Compila el proyecto con Maven o tu IDE preferido.
  2. Ejecuta la clase `AnalizadorSintactico.Principal`.
  3. Escribe o pega tu código en el área de texto superior.
  4. Presiona el botón **RUN** para validar la sintaxis.

### 2. Analizador Léxico (Consola)
Permite visualizar la tokenización del código en tiempo real desde la consola.

- **Clase Principal:** `AnalizadorLexico.PruebaPrisma`
- **Instrucciones:**
  1. Ejecuta la clase `AnalizadorLexico.PruebaPrisma`.
  2. Escribe el código directamente en la terminal.
  3. Al terminar, envía la señal de fin de archivo (`Ctrl+D` en Linux/Mac o `Ctrl+Z` en Windows + `Enter`).
  4. Verás la lista de tokens generada con sus respectivas etiquetas.

---

# 📘 CASO DE ESTUDIO
## Diseño Formal del Lenguaje de Programación PRISMA

### 1. Introducción
**PRISMA** es un lenguaje de programación imperativo, estructurado y modular, diseñado con fines académicos para el estudio formal de compiladores. Su diseño prioriza la claridad sintáctica, la delimitación explícita de bloques y la facilidad de implementación de analizadores descendentes (LL).

### 2. Definición Formal del Lenguaje
$L = (\Sigma, T, R)$
- **$\Sigma$:** Alfabeto.
- **$T$:** Conjunto de tokens válidos.
- **$R$:** Conjunto de reglas sintácticas (Gramática Libre de Contexto).

### 3. Alfabeto ($\Sigma$)
```text
Σ = { a-z, A-Z, 0-9, +, -, *, /, %, =, >, <, !, (, ), ,, ", _ }
```

### 4. Palabras Reservadas
```prisma
RESERVADAS = { 
    MODULO, FIN-MODULO, PRINCIPAL, FIN-PRINCIPAL, FUNCION, FIN-FUNCION, 
    RETORNA, SI, ENTONCES, SINO, FIN-SI, PARA, FIN-PARA, MIENTRAS, 
    FIN-MIENTRAS, ENTERO, DECIMAL, TEXTO, BOOLEANO, VACIO, IMPRIME, 
    LEER, Y, O, NO 
}
```

### 5. Operadores
- **Aritméticos:** `+`, `-`, `*`, `/`, `%`
- **Relacionales:** `==`, `!=`, `>`, `<`, `>=`, `<=`
- **Lógicos:** `Y`, `O`, `NO`
- **Asignación:** `=`

### 6. Tipos de Datos
```prisma
TIPOS = { ENTERO, DECIMAL, TEXTO, BOOLEANO, VACIO }
```

### 7. Literales y Definiciones Léxicas
- **Identificadores (ID):** `letra (letra | digito | '_')*`
- **Enteros (NUM_INT):** `digito+`
- **Decimales (NUM_DEC):** `digito+ '.' digito+`
- **Booleanos:** `V`, `F`
- **Cadenas (CADENA):** `" (cualquier caracter excepto ") "`

### 8. Estructura General Modificada
Todo programa sigue la estructura:

```prisma
MODULO id
   (variables globales opcionales)
   (funciones opcionales)
   
   PRINCIPAL
       (instrucciones)
   FIN-PRINCIPAL
FIN-MODULO
```

### 9. Gramática Formal Completa (G)
$G = (V, T, P, S)$

#### 9.1 Símbolo Inicial (S)
$S = \{ <programa> \}$

#### 9.2 Terminales (T)
El conjunto de terminales $T$ se define formalmente mediante los tokens léxicos implementados en `Etiqueta.java` y procesados por el `AnalizadorLexico`:

| Categoría | Tokens / Lexemas |
| :--- | :--- |
| **Palabras Reservadas** | `MODULO`, `FIN-MODULO`, `PRINCIPAL`, `FIN-PRINCIPAL`, `FUNCION`, `FIN-FUNCION`, `RETORNA`, `SI`, `SINO`, `FIN-SI`, `PARA`, `FIN-PARA`, `MIENTRAS`, `FIN-MIENTRAS`, `ENTERO`, `DECIMAL`, `TEXTO`, `BOOLEANO`, `VACIO`, `IMPRIME`, `LEER`, `Y`, `O`, `NO` |
| **Operadores Relacionales** | `==` (IGUALDAD), `!=` (DIFERENTE), `>=` (MAYOR_IGUAL), `<=` (MENOR_IGUAL), `>`, `<` |
| **Operadores Aritméticos** | `+`, `-`, `*`, `/`, `%` |
| **Símbolos y Asignación** | `=`, `(`, `)`, `,`, `!` |
| **Literales y Dinámicos** | `ID` (Identificadores), `NUM_INT` (Enteros), `NUM_DEC` (Decimales), `CADENA` (Cadenas), `V` (Verdadero), `F` (Falso) |

> **Nota:** Los terminales con valor numérico $\ge 256$ son gestionados mediante la clase `Etiqueta`, mientras que los caracteres simples utilizan su valor ASCII estándar.

#### 9.3 Reglas de Producción (P)

**Estructura Principal y Alcance Global**
1. `<programa> ::= MODULO ID <cuerpo_modulo> FIN-MODULO`
2. `<cuerpo_modulo> ::= <declaraciones_globales> <declaraciones_funcion> PRINCIPAL <bloque> FIN-PRINCIPAL`
3. `<declaraciones_globales> ::= <declaracion_variable> <declaraciones_globales> | ε`

**Funciones y Procedimientos**
4. `<declaraciones_funcion> ::= <declaracion_funcion> <declaraciones_funcion> | ε`
5. `<declaracion_funcion> ::= FUNCION <tipo> ID '(' <lista_parametros> ')' <bloque> <retorno_opcional> FIN-FUNCION`
6. `<retorno_opcional> ::= RETORNA <expresion> | ε`
7. `<lista_parametros> ::= <parametro> <mas_parametros> | ε`
8. `<parametro> ::= <tipo> ID`
9. `<mas_parametros> ::= ',' <parametro> <mas_parametros> | ε`

**Declaraciones y Tipos**
10. `<declaracion> ::= <declaracion_variable>`
11. `<declaracion_variable> ::= <tipo> ID <inicializacion>`
12. `<inicializacion> ::= '=' <expresion> | ε`
13. `<tipo> ::= ENTERO | DECIMAL | TEXTO | BOOLEANO | VACIO`

**Expresiones (Precedencia)**
14. `<expresion> ::= <expresion_o>`
15. `<expresion_o> ::= <expresion_y> <cola_o>`
16. `<cola_o> ::= O <expresion_y> <cola_o> | ε`
17. `<expresion_y> ::= <expresion_relacional> <cola_y>`
18. `<cola_y> ::= Y <expresion_relacional> <cola_y> | ε`
19. `<expresion_relacional> ::= <expresion_suma> <cola_relacional>`
20. `<cola_relacional> ::= ('==' | '!=' | '>' | '<' | '>=' | '<=') <expresion_suma> | ε`
21. `<expresion_suma> ::= <expresion_multiplicacion> <cola_suma>`
22. `<cola_suma> ::= ('+' | '-') <expresion_multiplicacion> <cola_suma> | ε`
23. `<expresion_multiplicacion> ::= <expresion_unaria> <cola_multiplicacion>`
24. `<cola_multiplicacion> ::= ('*' | '/' | '%') <expresion_unaria> <cola_multiplicacion> | ε`
25. `<expresion_unaria> ::= NO <expresion_unaria> | '-' <expresion_unaria> | <primario>`

**Términos Primarios**
26. `<primario> ::= ID <continuacion_id> | NUM_INT | NUM_DEC | CADENA | V | F | '(' <expresion> ')'`
27. `<continuacion_id> ::= '(' <lista_argumentos> ')' | ε`
28. `<lista_argumentos> ::= <expresion> <mas_argumentos> | ε`
29. `<mas_argumentos> ::= ',' <expresion> <mas_argumentos> | ε`

**Sentencias y Control de Flujo**
30. `<asignacion> ::= ID '=' <expresion>`
31. `<condicional_si> ::= SI '(' <expresion> ')' <bloque> <condicional_sino_opcional> FIN-SI`
32. `<condicional_sino_opcional> ::= SINO <bloque> | ε`
33. `<ciclo_para> ::= PARA '(' <asignacion> ',' <expresion> ',' <asignacion> ')' <bloque> FIN-PARA`
34. `<ciclo_mientras> ::= MIENTRAS '(' <expresion> ')' <bloque> FIN-MIENTRAS`

**Bloques e Instrucciones**
35. `<bloque> ::= <lista_instrucciones>`
36. `<lista_instrucciones> ::= <instruccion> <lista_instrucciones> | ε`
37. `<instruccion> ::= ID <continuacion_instruccion> | <declaracion> | <condicional_si> | <ciclo_para> | <ciclo_mientras> | IMPRIME '(' <expresion> ')' | LEER '(' ID ')' | RETORNA <expresion>`
38. `<continuacion_instruccion> ::= '=' <expresion> | '(' <lista_argumentos> ')'`
# PRISMA
