import java.awt.image.RGBImageFilter;

public class OperationsMap {
    // Map of operations for our simulator
    public static void findOp(Instructions instr){
        // first we want a function to decide which operation to perform
        if(instr.instruction.equals("addi")){
            ADDI(instr);
        }
        else if(instr.instruction.equals("add")){
            ADD(instr);
        }
        else if(instr.instruction.equals("and")){
            AND(instr);
        }
        else if(instr.instruction.equals("sub")){
            SUB(instr);
        }
        else if(instr.instruction.equals("or")){
            OR(instr);
        }
        else if(instr.instruction.equals("sll")){
            SLL(instr);
        }
    }
    public static void ADD(Instructions instr){
        // add rd, rs, rt
        // rd = rs + rt
        Integer rs = RegisterFile.RF.get(instr.operands.get(1));
        Integer rt = RegisterFile.RF.get(instr.operands.get(2));
        Integer rd = rs + rt;
        // replace old rt with new rt
        RegisterFile.RF.replace(instr.operands.get(0), rd);
    }
    public static void AND(Instructions instr){
        // and rd, rs, rt
        // rd = rs & rt
        Integer rs = RegisterFile.RF.get(instr.operands.get(1));
        Integer rt = RegisterFile.RF.get(instr.operands.get(2));
        Integer rd = rs & rt;
        // replace old rt with new rt
        RegisterFile.RF.replace(instr.operands.get(0), rd);
    }
    public static void OR(Instructions instr){
        // or rd, rs, rt
        // rd = rs | rt
        Integer rs = RegisterFile.RF.get(instr.operands.get(1));
        Integer rt = RegisterFile.RF.get(instr.operands.get(2));
        Integer rd = rs | rt;
        // replace old rt with new rt
        RegisterFile.RF.replace(instr.operands.get(0), rd);
    }
    public static void SLL(Instructions instr){
        // sll rd, rt, sa
        // rd = rt << sa
        Integer rt = RegisterFile.RF.get(instr.operands.get(1));
        Integer sa = RegisterFile.RF.get(instr.operands.get(2));
        Integer rd = rt << sa;
        RegisterFile.RF.replace(instr.operands.get(0), rd);
    }
    public static void SUB(Instructions instr){
        // sub rd, rs, rt
        // rd = rs - rt
        Integer rs = RegisterFile.RF.get(instr.operands.get(1));
        Integer rt = RegisterFile.RF.get(instr.operands.get(2));
        Integer rd = rs - rt;
        // replace old rt with new rt
        RegisterFile.RF.replace(instr.operands.get(0), rd);
    }
    public static void SLT(Instructions instr){

    }
    public static void JR(Instructions instr){

    }
    public static void ADDI(Instructions instr){
        // addi rt, rs, imm
        // rt = rs + imm
        Integer rs = RegisterFile.RF.get(instr.operands.get(1));
        Integer imm = Integer.parseInt(instr.operands.get(2));
        // reassign rt with sum
        Integer rt = rs + imm;
        RegisterFile.RF.replace(instr.operands.get(0), 10); // replace reg value with new rt
    }
    public  static void BEQ(Instructions instr){

    }
    public static void BNE(Instructions instr){

    }
    public static void LW(Instructions instr){

    }
    public static void SW(Instructions instr){

    }
    public static void J(Instructions instr){

    }
    public static void JAL(Instructions instr){

    }
}