 
 IMPRIME Macro Mensaje
    Mov Ah, 09h
    mov dx, offset Mensaje
    int 21h  
 EndM  

 LEER Macro Entrada
    mov Ah, 0Ah 
    mov Dx, offset Entrada
    int 21h    
 EndM 

 CLS Macro
    Mov Ah, 0Fh
    int 10h
    Mov Ah, 0h
    int 10h
 EndM 
 
 POTENCIA Macro Base, Exp  
    Cmp Exp, 0
    jne Calcular
    Mov R+2, 1
    jmp FinM
    
    Calcular:
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
 
 .CODE

 Inicio:

 mov Ax, @Data
 mov Ds, Ax
               
 CLS

 IMPRIME Mge1
 LEER num


 xor Cx, Cx
 mov Cl, [num + 1] ;Cantidad de bytes leidos
 mov sum, 0
 mov Si, offset num + 2
 mov E+2, 0
 mov B+2, 10     
 
 Eti: 
 POTENCIA B+2, E+2
  
 xor Ax, Ax 
 Mov Al, [Si] 
 sub Al, 30H
 Mul R+2
 Add sum, Al
 Add E+2, 1
 Inc Si
 loop Eti
 IMPRIME sum

 
 Fin:
 mov ax, 4C00h
 int 21h

 .DATA  
                                                                    
 Salto db 10,13,24h

 Mge1 db "Valor de NUM: $"
 num db 8, ? 8 dup (24h)
 R db 8, ? 8 dup (24h)
 B db 8, ? 8 dup (24h)
 E db 8, ? 8 dup (24h)
 sum db 8, ? 8 dup (24h)
 Dig db 8, ? 8 dup (24h)

 .STACK
 END Inicio