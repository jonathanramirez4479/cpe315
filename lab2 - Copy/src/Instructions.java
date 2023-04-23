import java.lang.reflect.Array;
import java.util.ArrayList;

public class Instructions {
    public String instruction;
    public ArrayList<String> operands;

    public Instructions(String instruction, ArrayList<String> operands){
        this.instruction = instruction;
        this.operands = operands;
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
