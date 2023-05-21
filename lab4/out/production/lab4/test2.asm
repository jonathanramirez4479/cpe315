# test2
# CPI: 1.727 Cycles: 19 Instructions: 11

j next		# 1 cycle penalty
equal1:	addi $a0, $0, 1

next:	addi $a0, $0, 100
	addi $a1, $0, 101
	beq $a0, $a1, equal1	# fall through
	addi $a0, $0, 101
	beq $a0, $a1, equal2	# taken branch
	addi $t0, $t0, 1
	addi $t0, $t0, 1
	addi $t0, $t0, 1

equal2:	add $a0, $0, $0
  add $a0, $0, $0
  add $a0, $0, $0
  add $a0, $0, $0
  add $a0, $0, $0
