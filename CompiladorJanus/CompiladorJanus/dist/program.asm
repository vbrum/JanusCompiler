.data
x: .word 0
strx: .asciiz "x = "
n: .word 0
strn: .asciiz "n = "
y: .word 0
stry: .asciiz "y = "
rc: .asciiz "readCommand\n"
open: .asciiz "{"
close: .asciiz "}"
comma: .asciiz ","
offset: .word 0
temporaries: .word 24
.text
L0:
lw $t0, n
addi $t0, $t0, 5
sw $t0, n
jal L1
lw $a1 , x
la $a0, strx
jal write
lw $a1 , n
la $a0, strn
jal write
lw $a1 , y
la $a0, stry
jal write
j exit
L1:
addi $sp, $sp, -4
sw $ra, offset($sp)
lw $a0, offset
addi $a0, $a0, 4
sw $a0, offset
addi $sp, $sp, -4
lw $t0, temporaries + 0
sw $t0, offset($sp)
lw $a0, offset
addi $a0, $a0, 4
sw $a0, offset
addi $sp, $sp, -4
lw $t0, temporaries + 4
sw $t0, offset($sp)
lw $a0, offset
addi $a0, $a0, 4
sw $a0, offset
addi $sp, $sp, -4
lw $t0, temporaries + 8
sw $t0, offset($sp)
lw $a0, offset
addi $a0, $a0, 4
sw $a0, offset
addi $sp, $sp, -4
lw $t0, temporaries + 12
sw $t0, offset($sp)
lw $a0, offset
addi $a0, $a0, 4
sw $a0, offset
addi $sp, $sp, -4
lw $t0, temporaries + 16
sw $t0, offset($sp)
lw $a0, offset
addi $a0, $a0, 4
sw $a0, offset
addi $sp, $sp, -4
lw $t0, temporaries + 20
sw $t0, offset($sp)
lw $a0, offset
addi $a0, $a0, 4
sw $a0, offset
lw $t0, n
beq $t0, 0, L8
add $t0, $zero, $zero
j L9
L8:
addi $t0, $zero, 1
L9:
sw $t0, temporaries + 4
beq $t0, $zero, L2
lw $t1, x
addi $t1, $t1, 1
sw $t1, x
lw $t1, y
addi $t1, $t1, 1
sw $t1, y
j L3
L2:
lw $t1, n
addi $t1, $t1, -1
sw $t1, n
jal L1
lw $t1, x
lw $t2, y
add $t1, $t1, $t2
sw $t1, x
lw $t1, x
sw $t1, temporaries + 12
lw $t1, y
sw $t1, x
lw $t1, temporaries + 12
sw $t1, y
L3:
lw $t1, x
lw $t3, y
beq $t1, $t3, L10
add $t1, $zero, $zero
j L11
L10:
addi $t1, $zero, 1
L11:
sw $t1, temporaries + 16
lw $t0, temporaries + 4
move $t1,$t0
lw $t2, temporaries + 16
beq $t1, $t2, L12
add $t1, $zero, $zero
j L13
L12:
addi $t1, $zero, 1
L13:
sw $t1, temporaries + 20
lw $t0, temporaries + 20
la $a0, msg2
teqi $t0, 0
j L14
j exit
L14:
lw $t0, offset($sp)
sw $t0, temporaries + 0
addi $a0, $a0, -4
sw $a0, offset
addi $sp, $sp, 4
lw $t0, offset($sp)
sw $t0, temporaries + 4
addi $a0, $a0, -4
sw $a0, offset
addi $sp, $sp, 4
lw $t0, offset($sp)
sw $t0, temporaries + 8
addi $a0, $a0, -4
sw $a0, offset
addi $sp, $sp, 4
lw $t0, offset($sp)
sw $t0, temporaries + 12
addi $a0, $a0, -4
sw $a0, offset
addi $sp, $sp, 4
lw $t0, offset($sp)
sw $t0, temporaries + 16
addi $a0, $a0, -4
sw $a0, offset
addi $sp, $sp, 4
lw $t0, offset($sp)
sw $t0, temporaries + 20
addi $a0, $a0, -4
sw $a0, offset
addi $sp, $sp, 4
lw $ra, offset($sp)
lw $a0, offset
addi $a0, $a0, -4
sw $a0, offset
addi $sp, $sp, 4
jr $ra
L4:
jal L5
lw $t0, n
addi $t0, $t0, -5
sw $t0, n
j exit
L5:
addi $sp, $sp, -4
sw $ra, offset($sp)
lw $a0, offset
addi $a0, $a0, 4
sw $a0, offset
addi $sp, $sp, -4
lw $t0, temporaries + 0
sw $t0, offset($sp)
lw $a0, offset
addi $a0, $a0, 4
sw $a0, offset
addi $sp, $sp, -4
lw $t0, temporaries + 4
sw $t0, offset($sp)
lw $a0, offset
addi $a0, $a0, 4
sw $a0, offset
addi $sp, $sp, -4
lw $t0, temporaries + 8
sw $t0, offset($sp)
lw $a0, offset
addi $a0, $a0, 4
sw $a0, offset
addi $sp, $sp, -4
lw $t0, temporaries + 12
sw $t0, offset($sp)
lw $a0, offset
addi $a0, $a0, 4
sw $a0, offset
addi $sp, $sp, -4
lw $t0, temporaries + 16
sw $t0, offset($sp)
lw $a0, offset
addi $a0, $a0, 4
sw $a0, offset
addi $sp, $sp, -4
lw $t0, temporaries + 20
sw $t0, offset($sp)
lw $a0, offset
addi $a0, $a0, 4
sw $a0, offset
addi $sp, $sp, -4
lw $t0, temporaries + 24
sw $t0, offset($sp)
lw $a0, offset
addi $a0, $a0, 4
sw $a0, offset
lw $t0, x
lw $t2, y
beq $t0, $t2, L15
add $t0, $zero, $zero
j L16
L15:
addi $t0, $zero, 1
L16:
sw $t0, temporaries + 16
beq $t0, $zero, L6
lw $t1, y
addi $t1, $t1, -1
sw $t1, y
lw $t1, x
addi $t1, $t1, -1
sw $t1, x
j L7
L6:
lw $t1, x
sw $t1, temporaries + 12
lw $t1, y
sw $t1, x
lw $t1, temporaries + 12
sw $t1, y
lw $t1, x
lw $t2, y
sub $t1, $t1, $t2
sw $t1, x
jal L5
lw $t1, n
addi $t1, $t1, 1
sw $t1, n
L7:
lw $t1, n
beq $t1, 0, L17
add $t1, $zero, $zero
j L18
L17:
addi $t1, $zero, 1
L18:
sw $t1, temporaries + 4
lw $t0, temporaries + 16
move $t1,$t0
lw $t2, temporaries + 4
beq $t1, $t2, L19
add $t1, $zero, $zero
j L20
L19:
addi $t1, $zero, 1
L20:
sw $t1, temporaries + 24
lw $t0, temporaries + 24
la $a0, msg2
teqi $t0, 0
j L21
j exit
L21:
lw $t0, offset($sp)
sw $t0, temporaries + 0
addi $a0, $a0, -4
sw $a0, offset
addi $sp, $sp, 4
lw $t0, offset($sp)
sw $t0, temporaries + 4
addi $a0, $a0, -4
sw $a0, offset
addi $sp, $sp, 4
lw $t0, offset($sp)
sw $t0, temporaries + 8
addi $a0, $a0, -4
sw $a0, offset
addi $sp, $sp, 4
lw $t0, offset($sp)
sw $t0, temporaries + 12
addi $a0, $a0, -4
sw $a0, offset
addi $sp, $sp, 4
lw $t0, offset($sp)
sw $t0, temporaries + 16
addi $a0, $a0, -4
sw $a0, offset
addi $sp, $sp, 4
lw $t0, offset($sp)
sw $t0, temporaries + 20
addi $a0, $a0, -4
sw $a0, offset
addi $sp, $sp, 4
lw $t0, offset($sp)
sw $t0, temporaries + 24
addi $a0, $a0, -4
sw $a0, offset
addi $sp, $sp, 4
lw $ra, offset($sp)
lw $a0, offset
addi $a0, $a0, -4
sw $a0, offset
addi $sp, $sp, 4
jr $ra
