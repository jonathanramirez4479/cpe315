import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class BranchPredictor {

    public static int totalBranches = 0;
    public int[] GHR;

    public int GHR_size;

    public int[] counterTable;
    public BranchPredictor(String[] args){
        this.GHR_size = get_GHR_size(args);
        this.GHR = new int[GHR_size];
        this.counterTable = new int[(int)Math.pow(2, GHR_size)];
        initPredictor();
    }

    private void initPredictor(){
        for (int i :GHR) {
            GHR[i] = 0; // per spec initialize GHR to zero
        }
        for (int j: counterTable) {
            counterTable[j] = 0; // per spec init 2 bit counter table to 0
        }
    }
    private int get_GHR_size(String[] args){
        // use command line args to determine size
        if(args.length == 2){
            return 2; // default GHR size
        }
        else{
            return Integer.parseInt(args[2]);
        }
    }

    private int turnGHRintoBinary(){
        // Turns GHR into index for counter
        int[] reversed = GHR;
        for (int i = 0; i < reversed.length / 2; i++) { // reverse array
            int temp = reversed[i];
            reversed[i] = reversed[reversed.length - 1 - i];
            reversed[reversed.length - 1 - i] = temp;
        }
        StringBuilder binary = new StringBuilder();
        for(int digit: reversed){
            binary.append(digit);
        }
        String binaryStr = binary.toString();
        int binaryInt = Integer.parseInt(binaryStr, 2);
        return binaryInt;
    }

    public void checkPrediction(Instructions instr){
        totalBranches ++;
        int counterIndex = turnGHRintoBinary();
        // Note:
        // Not Taken = 0, Taken = 1.
        boolean prediction_BranchTaken = counterTable[counterIndex] != 0; // T if 1, F if 0
        boolean actual_BranchTaken = instr.branch_taken;
        //if prediction = not taken & actual is taken
        // TODO: What is bring incremented? Are Strongly/Weakly implemented, \
        //  if so, line 59 needs to be updated to handle each number is State machine (lecture8_3pf2.pdf slide 6)
    }
}
