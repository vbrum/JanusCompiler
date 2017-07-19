


exit:	
li $v0, 10
syscall

read:
li $v0, 4
la $a0, rc
syscall
li $v0, 5
syscall
jr $ra

write:
li $v0, 4
syscall
move $a0, $a1
# print integer
li  $v0, 1 
syscall
# print a newline
li $a0, 10
li $v0, 11
syscall
jr $ra

wvector:
mul $a2, $a2, 4
add $a3, $a1, $a2
li $v0, 4
syscall
la $a0, open
li $v0, 4
syscall
lw $a0, ($a1)
li $v0, 1
syscall
addi $a1,$a1, 4
ini:
beq $a1, $a3, fim
la $a0, comma
li $v0, 4
syscall
lw $a0, ($a1)
li $v0, 1
syscall
addi $a1, $a1, 4
j ini
fim:
la $a0, close
li $v0, 4
syscall
# print a newline
li $a0, 10
li $v0, 11
syscall
jr $ra


.ktext 0x80000180
move $k0, $v0
move $k1, $a0
#la $a0, msg
li $v0, 4
syscall

move $v0, $k0
move $a0, $k1
mfc0 $k0, $14
addi $k0, $k0, 8
mtc0 $k0, $14
eret
.kdata
msg:
     .asciiz "Error : Divided by zero."

msg2:
     .asciiz "Error : Incompatible truth-values."
	 
msg3:
	 .asciiz "Error : Should be true."

msg4:
	 .asciiz "Error : Should be false."
	 
