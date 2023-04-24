# Divide Program
# 
#   CPE 315
# Names: Jonathan Ramirez, Franco Abullarade
# Section: 03
# Description: This program divides a 64-bit number by a 32-bit number by 
# 	taking the user-inputted 64-bit number as two integers (the upper 32-bit number
#	and the lower 32-bit number) as well as the 32-bit divisor.  Using logical 
# 	bit shifting, this program calculates a quotient and outputs it using two
# 	32-bit integers (upper and lower 32 bits).

#import java.util.Scanner;
#public class Divide {
#    public static void main(String[] args){
#        int bitShift = 0;
#        int bitPos = 0;
#        int upper, lower, divisor;
#        int lsd;
#        Scanner input = new Scanner(System.in);

#        System.out.println("\nThis program  divides a 64-bit number" +
#                " with a 32-bit number \n");
#        System.out.println("Enter the 64-bit number as 2(32-bit)" +
#                " numbers");
#        System.out.print("Upper 32-bit number: ");
#        upper = input.nextInt();
#        System.out.print("Lower 32-bit number: ");
#        lower = input.nextInt();
#        System.out.print("\nEnter the 32-bit divisor: ");
#        divisor = input.nextInt();
#
#        for(int i = divisor; i != 1; i = i >>> 1)
#        {
#            bitPos += 1;
#        }
#
#        int prev_upper = upper;
#        int prev_lower = lower;
#
#        while(bitPos != 0){
#            lsd = upper & 1;
#            upper = upper >>> 1;
#            lower = lower >>> 1;
#            bitPos -= 1;
#            if(lsd == 1){
#                int msd = lsd << 31;
#                lower = lower | msd;
#            }
#        }
#        System.out.println(prev_upper + "," + prev_lower + " / " +
#                divisor + " = " + upper + "," + lower);
#    }
#}

# declare global so programmer can see actual addresses.
.globl welcome
.globl dividendPrompt
.globl upperBits
.globl lowerBits
.globl divisorPrompt
.globl quotientHigh
.globl quotientLow

#  Data Area (this area contains strings to be displayed during the program)
.data

welcome:
	.asciiz " This program divides a 64-bit number with a 32-bit number \n\n"

dividendPrompt:
	.asciiz " Enter the 64-bit number as 2(32-bit) numbers \n" # 0x3E
	
upperBits:
	.asciiz " Upper 32-bit number: " # 0x6E
	
lowerBits:
	.asciiz " Lower 32-bit number: " # 0x85

divisorPrompt:
	.asciiz "\n Enter the 32-bit divisor: " # 0x9C

quotientHigh: 
	.asciiz " \n quotient (high 32) = " # 0xB9
	
quotientLow:
	.asciiz " \n quotient (low 32) = " # 0xD2
	
newline:
	.asciiz "\n" # 0xEA

#Text Area (i.e. instructions)
.text

main:
	
	# Display the welcome message
	ori $v0, $0, 4
	lui $a0, 0x1001
	syscall
	
	# Display prompt
	ori $v0, $0, 4
	lui $a0, 0x1001
	ori $a0, $a0, 0x3E
	syscall
	
	# Display the prompt to input upper 32 bits
	ori $v0, $0, 4
	lui $a0, 0x1001
	ori $a0, $a0, 0x6E
	syscall
	
	# Read number that represents the upper 32 bits (of the dividend) from the user
	ori $v0, $0, 5
	syscall
	
	# Load upper 32-bit number in $s0
	or $s0, $0, $v0
	
	# Display the prompt to input the lower 32 bits
	ori $v0, $0, 4
	lui $a0, 0x1001
	ori $a0, $a0, 0x85
	syscall
	
	# Read number that represents the lower 32 bits (of the divident) from the user
	ori $v0, $0, 5
	syscall
	
	# Load lower 32-bit number in $s1
	or $s1, $0, $v0
	
	# Display the prompt to input the divisor
	ori $v0, $0, 4
	lui $a0, 0x1001
	ori $a0, $a0, 0x9C
	syscall
	
	# Read the divisor from the user
	ori $v0, $0, 5
	syscall
	
	# Load the divisor into $s2
	or $s2, $0, $v0
	
# START OF OPERATION

	# init counter=0 in $t0
	ori $t0, $0, 0
	
	# Find the bit position of 1 in divisor (will be our srl limit)
	ori $t1, $0, 1  # target = 1 ($t1)
	
findOnePos:
	beq $s2, $t1, getLSD  # if LSD == 1, stop and go to getLSD
	srl $s2, $s2, 1  # logically shift divisor right once
	addi $t0, $t0, 1  # increment counter
	j findOnePos
	
getLSD:
	beq $t0, $0, end  # if counter = 0, end
	andi $t2, $s0, 1  # $t2 = upper & 1 -> to get LSD
	srl $s0, $s0, 1  # logically shift upper 32-bit number right once
	srl $s1, $s1, 1  # logically shift lower 32-bit number right once
	beq $t2, $t1, transferOne  # if LSD = 1, go to transferOne
	addi $t0, $t0, -1  # decrement counter
	j getLSD

transferOne:
	beq $t0, $0, end  # if counter = 0, end
	sll $t2, $t2, 31  # logically shift LSD 31 times to the left (MSD)
	or $s1, $s1, $t2  # OR MSD with lower 32-bit num
	addi $t0, $t0, -1  # decrement counter
	j getLSD
	
end:

	# Display the output text
	ori     $v0, $0, 4			
	lui     $a0, 0x1001
	ori     $a0, $a0,0xB9
	syscall
	
	# Display the quotient high 32
	ori $v0, $0, 1
	add $a0, $0, $s0
	syscall
	
	# Display the output text
	ori     $v0, $0, 4			
	lui     $a0, 0x1001
	ori     $a0, $a0,0xD2
	syscall
	
	# Display the quotient low 32
	ori $v0, $0, 1
	add $a0, $0, $s1
	syscall
	
	# Newline
	ori $v0, $0, 4
	lui $a0, 0x1001
	ori $a0, $a0, 0xEA
	syscall
	
	# Exit (load 10 into $v0)
	ori     $v0, $0, 10
	syscall
