# Mod Program
# 
#   CPE 315

# Names: Jonathan Ramirez, Franco Abullarade
# Section: 03
# Description: This program mods two integer inputs.
# import java.lang.Math;

# public class mod {
#     /*
#      * 1.  Write the following fast "mod" function.
#      * This function uses no modulus operator, multiplication, or division - it uses only basic arithmetic/logical operations (add, sub, and...).
#      * The function takes two integers as inputs - a number (num), and a divisor (div).  You are guaranteed that div is a power of 2. You want the remainder of num / div.
#      * For example, if num=22 (00010110 in binary) and div = 4 (100) would return 2 (10).
#      * Your algorithm should *not* repeatedly subtract (or add) div from num. Name your file mod.asm.  Program 1 only needs to work with positive numbers
#      */

   
#     public static void main(String[] args) {
#         int num = Integer.parseInt(args[0]); 
#         int div = Integer.parseInt(args[1]); //must be power of 2
        
#         System.out.println(num & div - 1);
#         System.out.println(num % div);
#         System.out.println((num & div - 1)==(num % div));
#     }



# }

# declare global so programmer can see actual addresses.
.globl welcome
.globl prompt
.globl remainder

#  Data Area (this area contains strings to be displayed during the program)
.data

welcome:
	.asciiz " This program mods two numbers \n\n"

prompt:
	.asciiz " Enter an integer: "

remainder: 
	.asciiz " \n remainder = "

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

	#num1
	# Display prompt
	ori     $v0, $0, 4
				
	# This is the starting address of the prompt (notice the
	# different address from the welcome message)
	lui     $a0, 0x1001
	ori     $a0, $a0,0x22
	syscall
	
	# Read 1st integer from the user (5 is loaded into $v0, then a syscall)
	ori     $v0, $0, 5
	syscall
	
	# init s0 to 0 and set it to num1
	ori     $s0, $0, 0	
	addu    $s0, $v0, $s0

	#num2
	# Display prompt (4 is loaded into $v0 to display)
	ori     $v0, $0, 4			
	lui     $a0, 0x1001
	ori     $a0, $a0,0x22
	syscall

	# Read 2nd integer 
	ori	$v0, $0, 5			
	syscall
	# $v0 now has the value of the second integer
	
	# init s1 to 0 and set it to num2
	ori $s1, $0, 0 # set s1 to 0
	addu    $s1, $v0, $0
	
	#OPERATION
	addi $t1, $s1, -1
	and $t0, $s0, $t1 
	
	# Display the sum text
	ori     $v0, $0, 4			
	lui     $a0, 0x1001
	ori     $a0, $a0,0x36
	syscall
	
	# Display the sum
	# load 1 into $v0 to display an integer
	ori  $v0, $0, 1			
	#REPLACE 
	add 	$a0, $0, $t0 
	#
	syscall
	
	# Newline
	ori $v0, $0, 4
	lui $a0, 0x1001
	ori $a0, $a0, 0x46
	syscall
	
	# Exit (load 10 into $v0)
	ori     $v0, $0, 10
	syscall
