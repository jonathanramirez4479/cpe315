public class OperationsMap {
    // Map of operations for our simulator
    public static void doOp(Instructions instr){
        // first we want a function to decide which operation to perform
        instr.checkOperands();
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
        else if(instr.instruction.equals("lw")){
            LW(instr);
        }
        else if(instr.instruction.equals("sw")){
            SW(instr);
        }
        else if(instr.instruction.equals("bne")){
            BNE(instr);
        }
        else if(instr.instruction.equals("beq")){
            BEQ(instr);
        } else if (instr.instruction.equals("j")) {
            J(instr);
        } else if (instr.instruction.equals("jr")) {
            JR(instr);
        } else if (instr.instruction.equals("slt")) {
            SLT(instr);
        } else if (instr.instruction.equals("jal")) {
            JAL(instr);

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
        Integer sa = Integer.parseInt(instr.operands.get(2)); //This will be a constant
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
    public static void SLT(Instructions instr)
    {
        // slt rd, rs, rt
        // if rs < rt: rd = 1
        // else rd = 0
        Integer rs = RegisterFile.RF.get(instr.operands.get(1));
        Integer rt = RegisterFile.RF.get(instr.operands.get(2));
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
//        lab4.counter = RegisterFile.RF.get(instr.operands.get(0))-1;
        instr.jump_loc = RegisterFile.RF.get(instr.operands.get(0))-1;
    }
    public static void ADDI(Instructions instr){
        // addi rt, rs, imm
        // rt = rs + imm
        Integer rs = RegisterFile.RF.get(instr.operands.get(1));
        Integer imm = Integer.parseInt(instr.operands.get(2));
        // reassign rt with sum
        Integer rt = rs + imm;
        RegisterFile.RF.replace(instr.operands.get(0), rt); // replace reg value with new rt
    }
    public  static void BEQ(Instructions instr){
        // bne rs, rt, offset
        // if(rt == rs) go to offset
        Integer rs = RegisterFile.RF.get(instr.operands.get(0));
        Integer rt = RegisterFile.RF.get(instr.operands.get(1));
        Integer offset = readFile.labels.get(instr.operands.get(2));
        if(rt.equals(rs)){

            instr.branch_taken = true;
            instr.branch_loc = offset - 1;
//            lab4.counter = offset-1; // set program counter to line of label (account for offset of next iteration)
        }
    }
    public static void BNE(Instructions instr){
        // bne rs, rt, offset
        // if(rt != rs) go to offset
        Integer rs = RegisterFile.RF.get(instr.operands.get(0));
        Integer rt = RegisterFile.RF.get(instr.operands.get(1));
        Integer offset = readFile.labels.get(instr.operands.get(2));
        if(!rt.equals(rs)){
            instr.branch_taken = true;
            instr.branch_loc = offset -1;
//            lab4.counter = offset-1; // set program counter to line of label (account for offset of next iteration)
        }
    }
    public static void LW(Instructions instr){
        // lw rt, imm(rs)
        // rt = memory[immm + rs]
        Integer imm = Integer.parseInt(instr.operands.get(1));
        Integer rs = RegisterFile.RF.get(instr.operands.get(2));
        String rt = instr.operands.get(0);
        Integer rt_val = lab4.memory[imm + rs]; // access memory
        RegisterFile.RF.replace(rt, rt_val); // store new rt reg value
        // FOR SIMULATOR - CHECK USE - AFTER LOAD Condition
        Instructions nextInstr = readFile.instructionsList.get(lab4.counter + 1);
        if(nextInstr.instruction.equalsIgnoreCase("add") ||
            nextInstr.instruction.equalsIgnoreCase("addi") ||
            nextInstr.instruction.equalsIgnoreCase("and") ||
            nextInstr.instruction.equalsIgnoreCase("or") ||
            nextInstr.instruction.equalsIgnoreCase("sub") ||
            nextInstr.instruction.equalsIgnoreCase("slt")){
        // If lw rt ==  next line rs or rt, we need 1 cycle stall!
            if(nextInstr.operands.contains(rt)){
                instr.lw_stall = true;
            }

        }
    }
    public static void SW(Instructions instr){
        // sw rt, imm(rs)
        // memory[imm + rs] = rt
        Integer rt = RegisterFile.RF.get(instr.operands.get(0));
        Integer imm = Integer.parseInt(instr.operands.get(1));
        Integer rs = RegisterFile.RF.get(instr.operands.get(2));
        lab4.memory[imm + rs] = rt; // store rt in memory
    }
    public static void J(Instructions instr){
        // Sets PC to corresponding label
//        lab4.counter = readFile.labels.get(instr.operands.get(0));
         instr.jump_loc =  readFile.labels.get(instr.operands.get(0));
    }
    public static void JAL(Instructions instr){
        // Sets $ra to current count, then replaces PC to label
        // jal target
        int newLocation = readFile.labels.get(instr.operands.get(0)); // get jump location
        RegisterFile.RF.replace("$ra", lab4.counter+1); // set $ra to current line
//        lab4.counter = newLocation; //set new location
        instr.jump_loc = newLocation;
    }
}