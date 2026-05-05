.MODEL SMALL
.STACK 100h

.CODE
Inicio: 
    mov Ax, @Data 
    mov Ds, Ax 

; -------------------------------------------
; PROCEDIMIENTO: LeerNumero
; -------------------------------------------
LeerNumero proc
    mov ah, 01h
    int 21h
    sub al, '0'
    ret
LeerNumero endp

; -------------------------------------------
; PROCEDIMIENTO: Sumar
; -------------------------------------------
Sumar proc
    xor ah, ah
    xor bh, bh
    add ax, bx
    ret
Sumar endp

; -------------------------------------------
; PROCEDIMIENTO: Restar
; -------------------------------------------
Restar proc
    xor ah, ah
    xor bh, bh
    sub ax, bx
    ret
Restar endp

; -------------------------------------------
; PROCEDIMIENTO: ImprimirNumero
; -------------------------------------------
ImprimirNumero proc
    mov bx, 10
    xor dx, dx
    div bx 

    add al, 30h
    mov dl, al
    mov ah, 02h
    int 21h

    mov al, ah
    add al, 30h
    mov dl, al
    mov ah, 02h
    int 21h

    mov dl, 13
    mov ah, 02h
    int 21h
    mov dl, 10
    mov ah, 02h
    int 21h
    ret
ImprimirNumero endp

; --- PROGRAMA PRINCIPAL ---
    mov dx, offset msg1
    mov ah, 09h
    int 21h
    call LeerNumero
    mov num1, al

    mov dx, offset msg2
    mov ah, 09h
    int 21h
    call LeerNumero
    mov num2, al

; --- SUMA ---
    mov al, num1 
    mov bl, num2
    call Sumar
    mov resultado, ax

    mov dx, offset msgSuma
    mov ah, 09h
    int 21h
    mov ax, resultado
    call ImprimirNumero

; --- RESTA ---
    mov al, num1 
    mov bl, num2
    call Restar
    mov resultado, ax

    mov dx, offset msgResta
    mov ah, 09h
    int 21h
    mov ax, resultado
    call ImprimirNumero

    mov ax, 4C00h
    int 21h

.DATA
    msg1      db 'Ingrese el primer numero: $'
    msg2      db 'Ingrese el segundo numero: $'
    msgSuma   db 'La suma es: $'
    msgResta  db 'La resta es: $'
    num1      db ?
    num2      db ?
    resultado dw ?

END Inicio