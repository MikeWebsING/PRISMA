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

.MODEL SMALL
.CODE
CargarVars proc
  mov al, [si+2]
  mov bl, [di+2]
  ret
CargarVars endp

Guardar proc
  mov [si+2], al
  ret
Guardar endp

SaltarLinea proc
  mov ah, 02h
  mov dl, 13
  int 21h
  mov dl, 10
  int 21h
  ret
SaltarLinea endp

MostrarNum proc
  push ax
  push bx
  push cx
  push dx
  mov cx, 0
  mov bx, 10
DivLoop:
  mov dx, 0
  div bx
  push dx
  inc cx
  cmp ax, 0
  jne DivLoop
PrintLoop:
  pop dx
  add dl, 30h
  mov ah, 02h
  int 21h
  loop PrintLoop
  pop dx
  pop cx
  pop bx
  pop ax
  ret
MostrarNum endp

LeerNum proc
  mov si, dx
  mov cl, [si+1]
  mov ch, 0
  add si, 2
  mov ax, 0
  mov bx, 10
ParseBucle:
  mov dl, [si]
  sub dl, 30h
  mov dh, 0
  push dx
  mul bx
  pop dx
  add ax, dx
  inc si
  loop ParseBucle
  ret
LeerNum endp

ImprimirVariable proc
  mov si, dx
  mov al, [si+2]
  mov ah, 0
  call MostrarNum
  call SaltarLinea
  ret
ImprimirVariable endp

Inicio:
  mov Ax, @Data
  mov Ds, Ax
  CLS

  IMPRIME _cadena0
  mov dx, offset a
  LEER a
  call LeerNum
  mov si, offset a
  mov [si+2], al
  call SaltarLinea
  IMPRIME _cadena1
  mov dx, offset b
  LEER b
  call LeerNum
  mov si, offset b
  mov [si+2], al
  call SaltarLinea
  mov si, offset a
  mov di, offset b
  call CargarVars
  add al, bl
  mov si, offset c
  call Guardar
  IMPRIME _cadena2
  mov dx, offset c
  call ImprimirVariable
  mov si, offset a
  mov di, offset b
  call CargarVars
  sub al, bl
  mov si, offset c
  call Guardar
  IMPRIME _cadena3
  mov dx, offset c
  call ImprimirVariable
  mov si, offset a
  mov di, offset b
  call CargarVars
  mul bl
  mov si, offset c
  call Guardar
  IMPRIME _cadena4
  mov dx, offset c
  call ImprimirVariable
  mov si, offset a
  mov di, offset b
  call CargarVars
  mov ah, 0
  div bl
  mov si, offset c
  call Guardar
  IMPRIME _cadena5
  mov dx, offset c
  call ImprimirVariable
  mov si, offset a
  mov di, offset b
  call CargarVars
  mov ah, 0
  div bl
  mov al, ah
  mov si, offset r
  call Guardar
  IMPRIME _cadena6
  mov dx, offset r
  call ImprimirVariable
mov Ax, 4C00h
int 21h

.DATA
a db 10, ?, 10 dup (24h)
b db 10, ?, 10 dup (24h)
c db 10, ?, 10 dup (24h)
r db 10, ?, 10 dup (24h)
_cadena0 db "VALOR DE A: ", '$'
_cadena1 db "VALOR DE B: ", '$'
_cadena2 db "LA SUMA ES: ", '$'
_cadena3 db "LA RESTA ES: ", '$'
_cadena4 db "LA MULTIPLICACION ES: ", '$'
_cadena5 db "LA DIVISION ES: ", '$'
_cadena6 db "EL RESIDUO ES: ", '$'
.STACK
END Inicio
