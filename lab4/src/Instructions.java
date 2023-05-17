import java.util.ArrayList;
import java.util.Arrays;

//CHanging for github
public class Instructions {
    public String instruction;
    public ArrayList<String> operands;
    public int line;
    public String binary;
    private String type;

    public boolean branch_taken;

    int jump_loc;


    public Instructions(String instruction, ArrayList<String> operands, int line) {
        this.instruction = instruction;
        this.operands = operands;
        this.line = line;
        this.type = this.setType();
        this.branch_taken = false; // default to false
        this.jump_loc = 0;
    }

    public String getInstruction() {
        return instruction;
    }

    public void setInstruction(String instruction) {
        this.instruction = instruction;
    }

    @Override
    public String toString() {
        return this.instruction + " " + this.line;
    }

    public String getType() {
        return this.type;
    }

    public ArrayList<String> getOperands() {
        return operands;
    }

    public void setOperands(ArrayList<String> operands) {
        this.operands = operands;
    }
    private String setType() {
        String[] rType = {"and", "or", "add", "sll", "sub", "slt", "jr"};
        String[] iType = {"addi", "beq", "bne", "lw", "sw"};
        String[] jType = {"j", "jal"};
        if (Arrays.asList(rType).contains(instruction)) {
            R_TypeConversion();
            return "R";
        } else if (Arrays.asList(iType).contains(instruction)) {
            I_TypeConversion();
            return "I";
        } else if (Arrays.asList(jType).contains(instruction)) {
            J_TypeConversion();
            return "J";
        } else {
            return "Invalid";
        }
    }

    public void checkOperands(){
        if (operands.contains("$zero")) { //include check to not iterate every time
            for (int i = 0; i < operands.size(); i++) {
                if (operands.get(i).equalsIgnoreCase("$zero")) {
                    operands.set(i, "$0");
                }
            }
        }
    }

    private void R_TypeConversion() {
    /*
       R-format instructions: 2 input regs, 1 output
       Op[6] rs[5] rt[5] rd[5] shamt[5]=00000 funct[6]
     */
        String Op = "000000 ";
        String shamt = "00000 ";
        String rs, rt, rd, funct;

        if (instruction.equals("jr")) {
            rs = Init.registers.get(operands.get(0)) + " ";
            rt = "00000";
            rd = "00000";
        } else {
            rd = Init.registers.get(operands.get(0)) + " ";
            rs = Init.registers.get(operands.get(1)) + " ";
            rt = Init.registers.get(operands.get(2)) + " ";
            if(instruction.equals("sll")){
                rd = Init.registers.get(operands.get(0)) + " ";
                rs = "00000 ";
                rt = Init.registers.get(operands.get(1)) + " ";
                shamt = Integer.toBinaryString(Integer.parseInt(operands.get(2)));
                shamt = String.format("%5s", Integer.toBinaryString(Integer.parseInt(operands.get(2)) & 0x1F)).replace(' ', '0');
                shamt = shamt + " ";
            }
        }
        funct = Init.arithmeticInstr.get(instruction);
        this.binary = Op + rs + rt + rd + shamt + funct;

    }

    private void I_TypeConversion() {
    /*
       R-format instructions: 2 input regs, 1 output
       Op[6] rs[5] rt[5] immediate[16]
    */
//        String Op = Init.ITypeOpCodes.get()
        String Op = Init.ITypeOpCodes.get(instruction) + " ";
        String rs = Init.registers.get(operands.get(0)) + " ";
        String rt;
        int Im;

        if(instruction.equals("sw") || instruction.equals("lw")){
            rs = Init.registers.get(operands.get(2)) + " ";
            rt = Init.registers.get(operands.get(0)) + " ";
            Im = Integer.parseInt(operands.get(1));

        } else if ((instruction.equals("bne"))  || (instruction.equals("beq"))){ //BNE and BEQ
            rt = Init.registers.get(operands.get(1)) + " ";
            String label = operands.get(2);
            Im = readFile.labels.get(label) - this.line - 1;

        } else {
            rt = Init.registers.get(operands.get(1)) + " ";
            Im = Integer.parseInt(operands.get(2));
        }
        String binaryIm = Integer.toBinaryString(Im);
        binaryIm = String.format("%16s", Integer.toBinaryString(Im & 0xFFFF)).replace(' ', '0');
        if(binaryIm.length() > 16){
            throw new IllegalArgumentException("ERROR: " + Im + " -> Immediate Too large! Exceeds 16 bits");
        }

        this.binary = Op + rs + rt + binaryIm;
    }

    private void J_TypeConversion(){
        //j and jal
        //Op[6] Target Address[26]
        String Op = Init.jumpInstr.get(instruction) + " ";
        String tgt = operands.get(0);
        int tgt_bin = readFile.labels.get(tgt);
        String tgt_bin_f = String.format("%26s", Integer.toBinaryString(tgt_bin & 0x3FFFFFF)).replace(' ', '0');
        this.binary = Op + tgt_bin_f;

    }
}
