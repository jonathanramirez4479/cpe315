// import java.lang.reflect.Array;
import java.util.ArrayList;

public class Instructions {
    public String instruction;
    public ArrayList<String> operands;
    public int line;

    public Instructions(String instruction, ArrayList<String> operands, 
                        int line){
        this.instruction = instruction;
        this.operands = operands;
        this.line = line;
    }

    public String getInstruction(){
        return instruction;
    }

    public void setInstruction(String instruction){
        this.instruction = instruction;
    }

    public ArrayList<String> getOperands(){
        return operands;
    }

    public void setOperands(ArrayList<String> operands){
        this.operands = operands;
    }
}
