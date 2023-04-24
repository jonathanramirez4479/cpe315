# Reverse Binary Program
# 
#   CPE 315
# Names: Jonathan Ramirez, Franco Abullarade
# Section: 03
# Description: This program reverses an inputted binary value

# public class reverse {
# /*
#  * 2.  Write a program which prints the number that represents reverse-ordered binary of the input number.
#  * Reverse the full 32-bit number (include leading 0's). This means the your program will print the 32-bit number that
#  *    is generated if the 32-bit input number's bits are written in reverse order (MSB becomes LSB and so on).
#  * Name your file reverse.asm. Program 2 only needs to work with positive numbers as input.
#  */
#     public static void main(String[] args) {
#         final int regLength = 32;
#         int num;
#         if(args.length == 0){
#             num = 9;}
#         else{
#             num = Integer.parseInt(args[0]);}
            
#         int reversed = 0;
#         for(int i = regLength - 1; i >= 0; i--){      // 31 -> 0 
#             int LSD = num & 1;                        // Masking LSD
#             int LSDShifted = LSD << i;                // Shifting the LSD Left based on current i
#             reversed |= LSDShifted;                   // Set reversed to shifted LSD
#             num = num >> 1;                           // Shift original number right one before looping 
#             }
#         System.out.println(Integer.toBinaryString(reversed));
#         System.out.println(reversed);
#     }
# }


# declare global so programmer can see actual addresses.
.globl welcome
.globl input
.globl output


#  Data Area (this area contains strings to be displayed during the program)
.data

welcome:
	.asciiz " This program reverses binary input \n\n" #0x1001

input:
	.asciiz " Enter a 32-bit integer: " #0x27

output: 
	.asciiz " \n Dec. value of reversed binary = "

newline:
	.asciiz "\n"

#Text Area (i.e. instructions)
.text

main:

	# Display the welcome message (load 4 into $v0 to display)
	ori     $v0, $0, 4			

	# This generates the starting address for the welcome message.
	# (assumes the register first contains 0).
	lui     $a0, 0x1001
	syscall

	# Display prompt
	ori     $v0, $0, 4			
	
	# This is the starting address of the prompt (notice the
	# different address from the welcome message)
	lui     $a0, 0x1001
	ori     $a0, $a0,0x27
	syscall

	# Read 1st integer from the user (5 is loaded into $v0, then a syscall)
	ori     $v0, $0, 5
	syscall
	
	
	#Start of Operation
	or $t4, $v0, $0 	# t4 = input value
	ori $s2, $0, 31 	# init s2 (DIGIT COUNTER) to 31
	or $s0, $0, $0		# init final reversed binary to zero (OUTPUT)
	
LOOP:
	and $t3, $t3, $0 	# CLEAR LSD Reg 
	andi $t3, $t4, 1	# storing LSD t3
	or $s3, $0, $0 		# init s3 to 0 (LOGICAL LEFT SHIFT COUNTER)
	beq $t3, $0, SHIFTEND 	# if LSD no need to shift bc output set to 0
SHIFTLOOP:
	sll $t3, $t3, 1		# shift LSD left 
	addi $s3, $s3, 1	# increase count by 1
	beq $s3, $s2, SHIFTEND	# check if were done. Checks if SHIFT COUNTER (s3) = Digit Counter (s2)
	j SHIFTLOOP
	
SHIFTEND:
	or $s0, $s0, $t3 	# Mask the shifted LSD to output
	addi $s2, $s2, -1	# decrease DIGIT COUNTER by 1
	beq $s2 , $0, END
	srl $t4, $t4, 1		# shift original number to refresh LSD
	j LOOP

END:
	# Display the output text
	ori     $v0, $0, 4
	lui     $a0, 0x1001
	ori		$a0, $a0, 0x41
	syscall
	
	# Display the output
	# load 1 into $v0 to display an integer
	ori     $v0, $0, 1			
	add 	$a0, $s0, $0
	syscall
	
	# Newline
	ori $v0, $0, 4
	lui $a0, 0x1001
	ori $a0, $a0, 0x65
	syscall
	
	# Exit (load 10 into $v0)
	ori     $v0, $0, 10
	syscall
