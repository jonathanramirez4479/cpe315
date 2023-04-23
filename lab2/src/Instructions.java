import java.util.ArrayList;
import java.util.Arrays;

public class Instructions {
    public String instruction;
    public ArrayList<String> operands;
    public int line;

    public String binary;
    private String type;

    public Instructions(String instruction, ArrayList<String> operands,
                        int line) {
        this.instruction = instruction;
        this.operands = operands;
        this.line = line;
        this.type = this.setType();
    }

    public String getInstruction() {
        return instruction;
    }

    public void setInstruction(String instruction) {
        this.instruction = instruction;
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
            return "J";
        } else {
            throw new IllegalArgumentException("ERROR: " + instruction + " -> Instruction Not Compatible");
        }
    }


    private void R_TypeConversion() {
    /*
       R-format instructions: 2 input regs, 1 output
       Op[6] rs[5] rt[5] rd[5] shamt[5]=00000 funct[6]
     */
        String Op = "000000 ";
        String shamt = "00000 ";


        if (instruction.equals("jr")) {
            String rs = Init.registers.get(operands.get(0)) + " ";
            String rt = "00000 ";
            String rd = "00000 ";
            String funct = Init.arithmeticInstr.get(instruction) + " ";
        } else {
            String rs = Init.registers.get(operands.get(0)) + " ";
            String rt = Init.registers.get(operands.get(1)) + " ";
            String rd = Init.registers.get(operands.get(2)) + " ";
            if(instruction.equals("sll")){
                rd = "00000 ";
                shamt = Integer.toBinaryString(Integer.parseInt(operands.get(2)));
                shamt = String.format("%5s", Integer.toBinaryString(Integer.parseInt(operands.get(2)) & 0xFFFF)).replace(' ', '0');
                shamt = shamt + " ";
            }
            String funct = Init.arithmeticInstr.get(instruction) + " ";
            this.binary = Op + rs + rt + rd + shamt + funct;
        }
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
            rt = Init.registers.get(operands.get(2)) + " ";
            Im = Integer.parseInt(operands.get(1));
        } else if ((instruction.equals("bne"))  || (instruction.equals("beq"))){
            rt = Init.registers.get(operands.get(1)) + " ";
            String label = operands.get(2);
            Im = ReadFile.labels.get(label);

        } else {
            rt = Init.registers.get(operands.get(1)) + " ";
            Im = Integer.parseInt(operands.get(2));
        }
        String binaryIm = Integer.toBinaryString(Im);
        binaryIm = String.format("%16s", Integer.toBinaryString(Im & 0xFFFF)).replace(' ', '0');
//        if(binaryIm.length() > 16){
//            throw new IllegalArgumentException("ERROR: " + Im + " -> Immediate Too large! Exceeds 16 bits");
//        }

        this.binary = Op + rs + rt + binaryIm;
    }

    private void J_TypeConversion(){
        //j and jal

    }



}
