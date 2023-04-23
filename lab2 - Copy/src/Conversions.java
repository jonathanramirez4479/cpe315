public class Conversions {
    private static String binary_instr = "";
    public static String arithmetic(Instructions instr){
        // input: op, rd, rs, rt
        // rd = instr.operands[0]
        // rs = instr.operands[1]
        // rt = instr.operands[2]
        // output: opcode, rs, rt, rd, shamt, funct
        // key=instruction; value=funct
        String funct = instr.instruction;

        String rs = instr.operands.get(1);
        rs = Init.registers.get(rs);

        String rt = instr.operands.get(2);
        rt = Init.registers.get(rt);

        String rd = instr.operands.get(0);
        rd = Init.registers.get(rd);

        binary_instr = "000000 " + rs + " " + rt + " " + rd + " 00000 "
                + Init.arithmeticInstr.get(funct);
        return binary_instr;
    }

    public static String addi(Instructions instr){
        // input: op, rt, rs, immediate(16-bit)
        // output: op, rs, rt, immediate(16-bit)
        // rt = instr.operands[0]
        // rs = instr.operands[1]
        // immediate = instr.operands[2]
        String op = instr.instruction;
        op = "001000";

        String rs = instr.operands.get(1);
        rs = Init.registers.get(rs);

        String rt = instr.operands.get(0);
        rt = Init.registers.get(rt);

        // convert immediate string to int
        int temp = Integer.parseInt(instr.operands.get(2));
        String immediate = Integer.toBinaryString(temp);

        // pad immediate if necessary
        while(immediate.length() < 16)
            immediate = "0" + immediate;

        binary_instr = op + " " + rs + " " + rt + " " + immediate;
        return binary_instr;
    }

    public static String shift(Instructions instr){
        // input: funct, rd, rt, sa
        // output: op, rs, rt, rd, shamt, funct
        // rs = 00000
        // rd = instr.operands[0]
        // rt = instr.operands[1]
        // sa = instr.operands[2]
        String op = "000000";

        String rd = instr.operands.get(0);
        rd = Init.registers.get(rd);

        String rt = instr.operands.get(1);
        rt = Init.registers.get(rt);

        String rs = "00000";
        int temp = Integer.parseInt(instr.operands.get(2));
        String sa = Integer.toBinaryString(temp);
        String funct = "000000";

        while(sa.length() < 5)
            sa = "0" + sa;

        binary_instr = op + " " + rs + " " + rt + " " + rd + " " + sa + " "
                + funct;
        return binary_instr;
    }
}
