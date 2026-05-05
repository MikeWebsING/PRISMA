IMPRIME Macro Mensaje
    mov Ah, 09h
    mov Dx, offset Mensaje
    int 21h
EndM

LEER Macro Entrada
    mov Ah, 0Ah
    mov Dx, offset Entrada
    int 21h
EndM

CLS Macro
    mov Ah, 0Fh
    int 10h
    mov Ah, 0h
    int 10h
EndM

.MODEL SMALL
.CODE
Inicio:
    mov Ax, @Data
    mov Ds, Ax

    CLS
    IMPRIME Mge1
    LEER Cad1
    IMPRIME Salto
    IMPRIME Mge2
    LEER Cad2
    IMPRIME Salto

    mov Si, offset Cad1 + 1
    mov Al, [Si]
    mov Di, offset Cad2 + 1
    mov Bl, [Di]

    cmp Al, Bl
    jne DiferenteLargo

    mov Cl, Al
    xor Ch, Ch
    inc Si
    inc Di

CompararLoop:
    mov Al, [Si]
    mov Bl, [Di]
    cmp Al, Bl
    ja Gana1
    jb Gana2
    inc Si
    inc Di
    loop CompararLoop

    IMPRIME Mge3
    jmp Final

DiferenteLargo:
    mov Cl, Al
    cmp Cl, Bl
    jb MenorLargo
    mov Cl, Bl
MenorLargo:
    xor Ch, Ch
    inc Si
    inc Di

LoopDiferente:
    mov Al, [Si]
    mov Bl, [Di]
    cmp Al, Bl
    ja Gana1
    jb Gana2
    inc Si
    inc Di
    loop LoopDiferente

    mov Si, offset Cad1 + 1
    mov Al, [Si]
    mov Di, offset Cad2 + 1
    mov Bl, [Di]
    cmp Al, Bl
    ja Gana1
    jb Gana2

Gana1:
    IMPRIME Mge4
    IMPRIME Cad1 + 2
    jmp Final

Gana2:
    IMPRIME Mge4
    IMPRIME Cad2 + 2

Final:
    mov Ax, 4C00h
    int 21h

.DATA
    Salto db 10, 13, "$"
    Mge1 db "Ingresa la primera cadena: $"
    Mge2 db "Ingresa la segunda cadena: $"
    Mge3 db "las cadenas son iguales: $"
    Mge4 db "la cadena mayor es $"
    Cad1 db 50, ?, 50 dup ("$")
    Cad2 db 50, ?, 50 dup ("$")

.STACK
END Inicio
