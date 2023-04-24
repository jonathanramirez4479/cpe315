import java.util.HashMap;

public class Conversions {
    // Conversions class that holds methods to convert all possible 
    //  instructionas to binary text
    private static String binary_instr = "";

    public static String arithmeticInsString(Instructions instr){
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

    public static String addiInstString(Instructions instr){
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

    public static String shiftInstString(Instructions instr){
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

    public static String comparisonInstrString(Instructions instr){
        // input: op, rs, rt, label
        // output: op, rs, rt, label offset(16-bit)
        // rs = operands[0]
        // rt = operands[1]
        // label = operands[2]
        HashMap<String, Integer> labels = Main.getLabels();
        String op = Init.comparisonInstr.get(instr.instruction);

        String rs = instr.operands.get(0);
        rs = Init.registers.get(rs);

        String rt = instr.operands.get(1);
        rt = Init.registers.get(rt);

        String label = instr.operands.get(2); // returns label name

        int instr_line = instr.line;
        int label_line = labels.get(label);
        // calculate distance from instruction to label
        int offset = label_line - (instr_line + 1);

        // returns 32 bit for negative values, and unfixed for positive
        //   values
        String bin_offset = Integer.toBinaryString(offset);
        // if negative, then reduce to 16 bit
        if(offset < 0){
            bin_offset = bin_offset.substring(16, 32);
        }
        // else increase to 16 bit
        else if(offset >= 0){
            while(bin_offset.length() < 16)
                bin_offset = "0" + bin_offset;
        }

        binary_instr = op + " " + rs + " " + rt + " " + bin_offset;
        return binary_instr;
    }
}
