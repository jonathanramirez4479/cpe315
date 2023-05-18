public class OperationsMap {
    // Map of operations for our simulator
    public static void findOp(Instructions instr){
        // first we want a function to decide which operation to perform
        instr.checkOperands();
        switch (instr.instruction) {
            case "addi" -> ADDI(instr);
            case "add" -> ADD(instr);
            case "and" -> AND(instr);
            case "sub" -> SUB(instr);
            case "or" -> OR(instr);
            case "sll" -> SLL(instr);
            case "lw" -> LW(instr);
            case "sw" -> SW(instr);
            case "bne" -> BNE(instr);
            case "beq" -> BEQ(instr);
            case "j" -> J(instr);
            case "jr" -> JR(instr);
            case "slt" -> SLT(instr);
            case "jal" -> JAL(instr);
        }
    }
    public static void ADD(Instructions instr){
        // add rd, rs, rt
        // rd = rs + rt
        int rs = checkNullInteger(RegisterFile.RF.get(instr.operands.get(1)));
        int rt = checkNullInteger(RegisterFile.RF.get(instr.operands.get(2)));

        Integer rd = rs + rt;
        // replace old rd with new rd
        RegisterFile.RF.replace(instr.operands.get(0), rd);
    }
    public static void AND(Instructions instr){
        // and rd, rs, rt
        // rd = rs & rt
        int rs = checkNullInteger(RegisterFile.RF.get(instr.operands.get(1)));
        int rt = checkNullInteger(RegisterFile.RF.get(instr.operands.get(2)));
        Integer rd = rs & rt;
        // replace old rt with new rt
        RegisterFile.RF.replace(instr.operands.get(0), rd);
    }
    public static void OR(Instructions instr){
        // or rd, rs, rt
        // rd = rs | rt
        int rs = checkNullInteger(RegisterFile.RF.get(instr.operands.get(1)));
        int rt = checkNullInteger(RegisterFile.RF.get(instr.operands.get(2)));
        Integer rd = rs | rt;
        // replace old rt with new rt
        RegisterFile.RF.replace(instr.operands.get(0), rd);
    }
    public static void SLL(Instructions instr){
        // sll rd, rt, sa
        // rd = rt << sa
        int rt = checkNullInteger(RegisterFile.RF.get(instr.operands.get(1)));
        int sa = checkNullInteger(Integer.parseInt(instr.operands.get(2))); //This will be a constant
        Integer rd = rt << sa;
        RegisterFile.RF.replace(instr.operands.get(0), rd);
    }
    public static void SUB(Instructions instr){
        // sub rd, rs, rt
        // rd = rs - rt
        int rs = checkNullInteger(RegisterFile.RF.get(instr.operands.get(1)));
        int rt = checkNullInteger(RegisterFile.RF.get(instr.operands.get(2)));
        Integer rd = rs - rt;
        // replace old rt with new rt
        RegisterFile.RF.replace(instr.operands.get(0), rd);
    }
    public static void SLT(Instructions instr)
    {
        // slt rd, rs, rt
        // if rs < rt: rd = 1
        // else rd = 0
        int rs = checkNullInteger(RegisterFile.RF.get(instr.operands.get(1)));
        int rt = checkNullInteger(RegisterFile.RF.get(instr.operands.get(2)));
        int rd;
        if(rs < rt)
            rd = 1;
        else
            rd = 0;
        RegisterFile.RF.replace(instr.operands.get(0), rd);
    }
    public static void JR(Instructions instr){
        // gets content of register (operand[0]) and sets it to PC
        // jr rs
        // pc = rs
        int new_counter = checkNullInteger(RegisterFile.RF.get(instr.operands.get(0)))-1;
        instr.jump_loc = new_counter;
        lab4.counter = new_counter;
    }
    public static void ADDI(Instructions instr){
        // addi rt, rs, imm
        // rt = rs + imm
        int rs = checkNullInteger(RegisterFile.RF.get(instr.operands.get(1)));
        int imm = checkNullInteger(Integer.parseInt(instr.operands.get(2)));
        // reassign rt with sum
        Integer rt = rs + imm;
        RegisterFile.RF.replace(instr.operands.get(0), rt); // replace reg value with new rt
    }
    public  static void BEQ(Instructions instr){
        // bne rs, rt, offset
        // if(rt == rs) go to offset
        int rs = checkNullInteger(RegisterFile.RF.get(instr.operands.get(0)));
        int rt = checkNullInteger(RegisterFile.RF.get(instr.operands.get(1)));
        int offset = checkNullInteger(readFile.labels.get(instr.operands.get(2)));
        if(rt == rs){
            lab4.counter = offset-1; // set program counter to line of label (account for offset of next iteration)
            instr.branch_taken = true; // communicate branch was taken

        }
    }
    public static void BNE(Instructions instr){
        // bne rs, rt, offset
        // if(rt != rs) go to offset
        int rs = checkNullInteger(RegisterFile.RF.get(instr.operands.get(0)));
        int rt = checkNullInteger(RegisterFile.RF.get(instr.operands.get(1)));
        int offset = checkNullInteger(readFile.labels.get(instr.operands.get(2)));
        if(rt != rs){
            lab4.counter = offset-1; // set program counter to line of label (account for offset of next iteration)
            instr.branch_taken = true; // communicate branch was taken

        }
    }
    public static void LW(Instructions instr){
        // lw rt, imm(rs)
        // rt = memory[immm + rs]
        int imm = checkNullInteger(Integer.parseInt(instr.operands.get(1)));
        int rs = checkNullInteger(RegisterFile.RF.get(instr.operands.get(2)));
        Integer rt = lab4.memory[imm + rs]; // access memory
        RegisterFile.RF.replace(instr.operands.get(0), rt); // store new rt reg value
    }
    public static void SW(Instructions instr){
        // sw rt, imm(rs)
        // memory[imm + rs] = rt
        int rt = checkNullInteger(RegisterFile.RF.get(instr.operands.get(0)));
        int imm = checkNullInteger(Integer.parseInt(instr.operands.get(1)));
        int rs = checkNullInteger(RegisterFile.RF.get(instr.operands.get(2)));
        lab4.memory[imm + rs] = rt; // store rt in memory
    }
    public static void J(Instructions instr){
        // Sets PC to corresponding label
        int new_counter = checkNullInteger(readFile.labels.get(instr.operands.get(0)));
        instr.jump_loc = new_counter;
        lab4.counter = new_counter;
    }
    public static void JAL(Instructions instr){
        // Sets $ra to current count, then replaces PC to label
        // jal target
        int newLocation = checkNullInteger(readFile.labels.get(instr.operands.get(0))); // get jump location
        instr.jump_loc = newLocation;
        RegisterFile.RF.replace("$ra", lab4.counter+1); // set $ra to next instr
        lab4.counter = newLocation; //set new location
    }

    public static int checkNullInteger(Integer value)
    {
        if(value == null)
            return 0;
        else
            return value;
    }
}