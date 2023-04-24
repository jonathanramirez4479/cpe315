# Exponent Program
# 
#   CPE 315
# Names: Jonathan Ramirez, Franco Abullarade
# Section: 03
# Description: This program calculates the exponentiation of x raised to the power
# 	of y (user inputs) using a combination of jumps and loops.

#import java.util.Scanner;
#public class Exponent {
#    public static void main(String[] args) {
#        Scanner input = new Scanner(System.in);
#        System.out.println("\nThis program calculates x raised to the " +
#                "power of y");
#        System.out.print("\nEnter a value for x: ");
#        int x = input.nextInt();
#        System.out.print("Enter a value for y: ");
#        int y = input.nextInt();
#
#        int exp = exponent(x, y);
#        System.out.println("x^y = " + exp);
#    }
#    public static int exponent(int x, int y){
#        int count = x;
#        for(int i = 1; i < y; i++) {
#            int temp = count;
#            count = 0;
#            for (int j = 0; j < temp; j++) {
#                count += x;
#            }
#        }
#        return count;
#    }
#}

# declare global so programmer can see actual addresses.
.globl welcome
.globl prompt
.globl prompt2
.globl result

#  Data Area (this area contains strings to be displayed during the program)
.data

welcome:
	.asciiz " This program calculates x raised to the power of y \n\n"

prompt:
	.asciiz " Enter a value for x: "
	
prompt2:
	.asciiz " Enter a value for y: "

result: 
	.asciiz " \n x^y = "
	
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
	ori     $a0, $a0,0x37
	syscall

	# Read integer X from the user (5 is loaded into $v0, then a syscall)
	ori     $v0, $0, 5
	syscall
	
	# load X into $s0
	ori $s0, $v0, 0	
	
	# Display prompt2 (4 is loaded into $v0 to display)
	ori     $v0, $0, 4			
	lui     $a0, 0x1001
	ori     $a0, $a0,0x4E
	syscall

	# Read Y 
	ori	$v0, $0, 5			
	syscall
	
	# load Y into $s1
	ori   $s1, $v0, 0
	
	# Init sum=x in $s2
	ori $s2, $s0, 0
	
	# Init i=1 (for loop iteration) in $t0
	ori $t0, $0, 1
	
	# Start of exponentiation
loop:
	beq $t0, $s1, end  # if i=y goto end
	ori $t1, $s2, 0  # init temp=sum in $t1
	ori $t2, $0, 0  # init j=0 for addloop iteration in $t2
	ori $s2, $0, 0  # set sum to 0
	addi $t0, $t0, 1  # increment i by 1
addloop:
	beq $t2, $t1, loop  # if j=temp goto loop
	add $s2, $s2, $s0  # sum += x
	addi $t2, $t2, 1  # increment j by 1
	j addloop

end:

	# Display the output text
	ori     $v0, $0, 4			
	lui     $a0, 0x1001
	ori     $a0, $a0,0x65
	syscall
	
	# Display the result
	ori $v0, $0, 1
	add $a0, $s2, $0
	syscall
	
	# Newline
	ori $v0, $0, 4
	lui $a0, 0x1001
	ori $a0, $a0, 0x6F
	syscall

	# Exit (load 10 into $v0)
	ori     $v0, $0, 10
	syscall
