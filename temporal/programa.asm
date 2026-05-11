IMPRIME Macro Mensaje
    Mov Ah, 09h
    mov dx, offset Mensaje
    int 21h
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
  cmp ax, 0
  jge EsPositivo
  push ax
  mov ah, 02h
  mov dl, '-'
  int 21h
  pop ax
  neg ax
EsPositivo:
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

ImprimirVariable proc
  mov si, dx
  mov al, [si+2]
  cbw
  call MostrarNum
  call SaltarLinea
  ret
ImprimirVariable endp

Inicio:
  mov Ax, @Data
  mov Ds, Ax

  mov si, offset objetivo+2
  mov byte ptr [si], 20
  mov si, offset posicion+2
  mov byte ptr [si], -1
  mov si, offset nActual+2
  mov byte ptr [si], 0
  mov si, offset n1+2
  mov byte ptr [si], 3
  mov si, offset n2+2
  mov byte ptr [si], -2
  mov si, offset n3+2
  mov byte ptr [si], 8
  mov si, offset n4+2
  mov byte ptr [si], 15
  mov si, offset n5+2
  mov byte ptr [si], 20
  mov si, offset n6+2
  mov byte ptr [si], 7
  mov si, offset contador+2
  mov byte ptr [si], 0
  mov si, offset aux+2
  mov byte ptr [si], 0
  mov si, offset resultadoBusqueda+2
  mov byte ptr [si], -1
  mov si, offset contador+2
  mov byte ptr [si], 0
Etiqueta03:
  mov si, offset contador
  mov al, [si+2]
  mov bl, 6
  cmp al, bl
  jge Etiqueta04
  mov si, offset contador
  mov al, [si+2]
  mov bl, 0
  cmp al, bl
  jne Etiqueta05
  mov si, offset nActual+2
  mov di, offset n1
  mov al, byte ptr [di+2]
  mov byte ptr [si], al
Etiqueta05:
  mov si, offset contador
  mov al, [si+2]
  mov bl, 1
  cmp al, bl
  jne Etiqueta06
  mov si, offset nActual+2
  mov di, offset n2
  mov al, byte ptr [di+2]
  mov byte ptr [si], al
Etiqueta06:
  mov si, offset contador
  mov al, [si+2]
  mov bl, 2
  cmp al, bl
  jne Etiqueta07
  mov si, offset nActual+2
  mov di, offset n3
  mov al, byte ptr [di+2]
  mov byte ptr [si], al
Etiqueta07:
  mov si, offset contador
  mov al, [si+2]
  mov bl, 3
  cmp al, bl
  jne Etiqueta08
  mov si, offset nActual+2
  mov di, offset n4
  mov al, byte ptr [di+2]
  mov byte ptr [si], al
Etiqueta08:
  mov si, offset contador
  mov al, [si+2]
  mov bl, 4
  cmp al, bl
  jne Etiqueta09
  mov si, offset nActual+2
  mov di, offset n5
  mov al, byte ptr [di+2]
  mov byte ptr [si], al
Etiqueta09:
  mov si, offset contador
  mov al, [si+2]
  mov bl, 5
  cmp al, bl
  jne Etiqueta10
  mov si, offset nActual+2
  mov di, offset n6
  mov al, byte ptr [di+2]
  mov byte ptr [si], al
Etiqueta10:
  call procesarNumero
  mov si, offset resultadoBusqueda
  call Guardar
  mov si, offset resultadoBusqueda
  mov al, [si+2]
  mov bl, -1
  cmp al, bl
  je Etiqueta11
  mov si, offset posicion+2
  mov di, offset resultadoBusqueda
  mov al, byte ptr [di+2]
  mov byte ptr [si], al
Etiqueta11:
  mov si, offset contador
  mov al, [si+2]
  mov bl, 1
  add al, bl
  mov si, offset contador
  call Guardar
  jmp Etiqueta03
Etiqueta04:
  IMPRIME Msg0
  mov si, offset contador+2
  mov byte ptr [si], 0
Etiqueta12:
  mov si, offset contador
  mov al, [si+2]
  mov bl, 6
  cmp al, bl
  jge Etiqueta13
  mov si, offset contador
  mov al, [si+2]
  mov bl, 0
  cmp al, bl
  jne Etiqueta14
  mov dx, offset n1
  call ImprimirVariable
Etiqueta14:
  mov si, offset contador
  mov al, [si+2]
  mov bl, 1
  cmp al, bl
  jne Etiqueta15
  mov dx, offset n2
  call ImprimirVariable
Etiqueta15:
  mov si, offset contador
  mov al, [si+2]
  mov bl, 2
  cmp al, bl
  jne Etiqueta16
  mov dx, offset n3
  call ImprimirVariable
Etiqueta16:
  mov si, offset contador
  mov al, [si+2]
  mov bl, 3
  cmp al, bl
  jne Etiqueta17
  mov dx, offset n4
  call ImprimirVariable
Etiqueta17:
  mov si, offset contador
  mov al, [si+2]
  mov bl, 4
  cmp al, bl
  jne Etiqueta18
  mov dx, offset n5
  call ImprimirVariable
Etiqueta18:
  mov si, offset contador
  mov al, [si+2]
  mov bl, 5
  cmp al, bl
  jne Etiqueta19
  mov dx, offset n6
  call ImprimirVariable
Etiqueta19:
  mov si, offset contador
  mov al, [si+2]
  mov bl, 1
  add al, bl
  mov si, offset contador
  call Guardar
  jmp Etiqueta12
Etiqueta13:
  IMPRIME Msg1
  mov dx, offset posicion
  call ImprimirVariable

  mov ax, 4C00h
  int 21h


procesarNumero proc
  mov si, offset nActual
  mov al, [si+2]
  mov bl, 0
  cmp al, bl
  jle Etiqueta00
  mov si, offset nActual
  mov al, [si+2]
  mov bl, 2
  cbw
  idiv bl
  mov al, ah
  mov si, offset aux
  call Guardar
  mov si, offset aux
  mov al, [si+2]
  mov bl, 0
  cmp al, bl
  jne Etiqueta01
  mov si, offset nActual
  mov di, offset objetivo
  call CargarVars
  cmp al, bl
  jne Etiqueta02
  mov si, offset contador
  mov al, [si+2]
  ret
Etiqueta02:
Etiqueta01:
Etiqueta00:
  mov al, -1
  ret
  ret
procesarNumero endp

.DATA
  objetivo db 10, 0, 10 dup('$')
  posicion db 10, 0, 10 dup('$')
  nActual db 10, 0, 10 dup('$')
  n1 db 10, 0, 10 dup('$')
  n2 db 10, 0, 10 dup('$')
  n3 db 10, 0, 10 dup('$')
  n4 db 10, 0, 10 dup('$')
  n5 db 10, 0, 10 dup('$')
  n6 db 10, 0, 10 dup('$')
  contador db 10, 0, 10 dup('$')
  aux db 10, 0, 10 dup('$')
  resultadoBusqueda db 10, 0, 10 dup('$')
  Msg0 db "Listado de numeros:", 13, 10, '$'
  Msg1 db "Posicion encontrada:", 13, 10, '$'

.STACK
END Inicio
