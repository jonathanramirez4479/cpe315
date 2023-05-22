lw $a0, 0($a1)
addi $t0, $a0, 1 # stall

lw $a1, 0($a0)
add $t1, $a1, $t0	# stall