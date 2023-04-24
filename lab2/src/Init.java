import java.util.HashMap;
import java.util.LinkedList;
//CHanging for github

public class Init {
    // Init class to initialize all supported instructions and registers
    //  by mapping their string name to their respective binary values
    public static HashMap<String, String> arithmeticInstr = new HashMap<>();
    public static HashMap<String, String> comparisonInstr = new HashMap<>();
    public static HashMap<String, String> memoryInstr = new HashMap<>();
    public static HashMap<String, String> jumpInstr = new HashMap<>();
    public static HashMap<String, String> registers = new HashMap<>();

    public static HashMap<String, String> ITypeOpCodes = new HashMap<>();

    public static LinkedList<Instructions> InvalidInstr = new LinkedList<>();

    public static void initRegisters(){
        // Init all available registers
        registers.put("$zero", "00000");
        registers.put("$0", "00000");
        registers.put("$v0", "00010");
        registers.put("$v1", "00011");
        registers.put("$a0", "00100");
        registers.put("$a1", "00101");
        registers.put("$a2", "00110");
        registers.put("$a3", "00111");
        registers.put("$t0", "01000");
        registers.put("$t1", "01001");
        registers.put("$t2", "01010");
        registers.put("$t3", "01011");
        registers.put("$t4", "01100");
        registers.put("$t5", "01101");
        registers.put("$t6", "01110");
        registers.put("$t7", "01111");
        registers.put("$s0", "10000");
        registers.put("$s1", "10001");
        registers.put("$s2", "10010");
        registers.put("$s3", "10011");
        registers.put("$s4", "10100");
        registers.put("$s5", "10101");
        registers.put("$s6", "10110");
        registers.put("$s7", "10111");
        registers.put("$t8", "11000");
        registers.put("$t9", "11001");
        registers.put("$sp", "11101");
        registers.put("$ra", "11111");
    }
    public static void initInstructions(){
        // Init all the supported instructions

        // Arithmetic instructions (except immediate)
        // input: funct, rd, rs, rt
        // output: op, rs, rt, rd, shamt, funct
        // key=instruction; value=funct
        arithmeticInstr.put("and", "100100");
        arithmeticInstr.put("or", "100101");
        arithmeticInstr.put("add", "100000");
        arithmeticInstr.put("sub", "100010");
        arithmeticInstr.put("slt", "101010");
        arithmeticInstr.put("sll", "000000");
        arithmeticInstr.put("jr",  "001000");

        // Immediate instruction
        // input: op, rt, rs, immediate(16-bit)
        // output: op, rs, rt, immediate(16-bit)


        // Shift Instruction
        // input: funct, rd, rt, sa
        // output: op, rs, rt, rd, shamt, funct
        ITypeOpCodes.put("addi", "001000");
        ITypeOpCodes.put("beq", "000100");
        ITypeOpCodes.put("bne", "000101");
        ITypeOpCodes.put("lw", "100011");
        ITypeOpCodes.put("sw", "101011");

        // Comparison Instructions
        // input: op, rs, rt, offset(16-bit)
        // output: op, rs, rt, offset(16-bit)
        // key=instruction; value=opcode
        comparisonInstr.put("beq", "000100");
        comparisonInstr.put("bne", "000101");

        // Memory instructions
        // input: op, rt, offset(rs)
        // output: op, rs, rt, offset
        // key=instruction; value=opcode
        memoryInstr.put("lw", "100011");
        memoryInstr.put("sw", "101011");

        // jump instructions
        // input: op, target
        // output: op, address
        // key=instr; value=opcode
        jumpInstr.put("j", "000010");
        jumpInstr.put("jal", "000011");

        // jump reg
        // input: op, rs
        //output: op, rs, rt, rd, shamt, funct
    }
}
