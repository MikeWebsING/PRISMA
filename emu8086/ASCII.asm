;Imprime el mensaje con el servicio 09h
IMPRIME Macro Mensaje
      Mov Ah, 09h
      mov dx, offset Mensaje
      int 21h     
EndM
        
          
LEER Macro Entrada 
    mov Ah,0Ah
    mov dx, offset Entrada
    int 21h  
EndM 

CLS Macro
    Mov Ah, 0Fh
    int 10h
    Mov Ah, 0h
    int 10h
EndM


POTENCIA Macro Base, Exp
    LOCAL Calcular1, Calcular2, Calcula, FinM
    Cmp Exp, 0
    jne Calcular1 
    Mov R+2, 1
    jmp FinM
    
    Calcular1:
    Cmp Exp, 1
    jne Calcular2 
    Mov R+2, 10
    jmp FinM 
    
    Calcular2:
    Xor Cx, Cx
    Mov Cl, Exp
    Dec Cx
    Xor Ax, Ax
    Mov Al, Base
    Calcula:
    Mul Base
    loop Calcula
    Mov R + 2, Al 
    FinM:
EndM

.MODEL SMALL
               ; Define el tamano de memoria
.CODE 
               ; indica el inicio del programa
Inicio:        ; Etiqueta de inicio obligatorio

mov Ax, @Data   ;asignamos direccion   
mov Ds, Ax      ;al sagmento de datos (DS)

;aqui va el codigo de tu programa 
;---------------------------- 
CLS 

IMPRIME Mge1
LEER num 

mov Si, offset num+2
mov Al, [Si]
cmp Al, 30h
jb EsAscii
cmp Al, 39h
ja EsAscii

EsDecimal:
xor Cx, Cx
mov Cl, [num + 1]  ; Cx contiene la cantidad de bytes leidos
mov Sum, 0
mov Si, offset num + 2
ADD Si, Cx
DEC Si
mov E+2, 0
mov B+2, 10

Eti: 
PUSH Cx
POTENCIA B+2, E+2 
POP Cx
  
Xor Ax, Ax
Mov Al, [Si] 
Sub Al, 30H
Mul R+2
Add Sum, Al 
Inc E+2
DEC Si  
loop Eti 

mov Dl, sum
mov Ah, 02h
int 21h
jmp Fin

EsAscii:
xor Ax, Ax
mov Al, [num + 2]   
mov Di, offset Dig + 2
mov Bl, 10
xor Cx, Cx

Dividir:
xor Ah, Ah
div Bl              
push Ax             
inc Cx
cmp Al, 0
jne Dividir

Juntar:
POP Ax
Add Ah,30h
mov [Di], Ah
inc Di
loop Juntar

mov byte ptr [Di], '$'
IMPRIME Dig+2


Fin:

;---------------------------- 
mov ax, 4C00h
int 21h 


.DATA
  
;Declaracion de variables 
Salto db 10,13,24h 
 
Mge1 db "Valor de NUM: $"
num db 8,?,8 dup (24h)
R db 8,?,8 dup (24h)
B db 8,?,8 dup (24h) 
E db 8,?,8 dup (24h)
Sum db 8,?,8 dup (24h)
Dig db 8,?,8 dup (24h)

.STACK
END Inicio