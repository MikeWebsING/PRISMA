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

MOSTRAR_NUMERO Macro
    local dividir, imprimir, negativo, positivo
    push ax
    push bx
    push cx
    push dx
    cmp al, 0
    jge positivo
    push ax
    mov dl, '-'
    mov ah, 02h
    int 21h
    pop ax
    neg al
positivo:
    mov cx, 0
    mov bl, 10
dividir:
    mov ah, 0
    div bl
    push ax
    inc cx
    cmp al, 0
    jne dividir
imprimir:
    pop ax
    mov dl, ah
    add dl, 30h
    mov ah, 02h
    int 21h
    loop imprimir
    pop dx
    pop cx
    pop bx
    pop ax
EndM

.MODEL SMALL
.CODE
Inicio:
mov Ax, @Data
mov Ds, Ax
CLS
mov ax, 4C00h
int 21h
.DATA
Salto db 10,13,24h
.STACK
END Inicio