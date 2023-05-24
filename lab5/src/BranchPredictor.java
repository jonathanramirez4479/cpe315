import java.util.Collections;
import java.util.LinkedList;

public class BranchPredictor {

    public static int totalBranches = 0;
    LinkedList<Integer> GHR;

    public int GHR_size;

    public int correctPrediction = 0;
    public int[] counterTable;
    public BranchPredictor(String[] args){
        this.GHR_size = get_GHR_size(args);
        this.GHR = new LinkedList<>();
        this.counterTable = new int[(int)Math.pow(2, GHR_size)];
        initPredictor();
    }

    private void initPredictor(){
        for (int i = 0; i < GHR_size; i++) {
            GHR.add(0); // per spec initialize GHR to zero
        }
        for (int j: counterTable) {
            counterTable[j] = 0; // per spec init 2 bit counter table to 0
        }
        GHR.set(0, 1);
        GHR.set(1, 0);
        System.out.println(GHR);
        System.out.println(turnGHRintoIndex());
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

    private int turnGHRintoIndex(){
        // Turns GHR into index for counter

        StringBuilder binary = new StringBuilder();
        for(int digit: GHR){
            binary.append(digit);
        }
        String binaryStr = binary.toString();
        return Integer.parseInt(binaryStr, 2);
    }

    public void checkPrediction(Instructions instr){
        totalBranches ++;
        int counterIndex = turnGHRintoIndex();
        int counter_val = counterTable[counterIndex];
        boolean predictionTaken = counter_val == 2 || counter_val == 3; // True if 10 or 11
        boolean actualTaken = instr.branch_taken;
        if(predictionTaken && actualTaken){
            correctPrediction ++;
        }
        else if (!predictionTaken && !actualTaken){
            correctPrediction ++;
        }
        if (!actualTaken) {
            if(counter_val != 0) {
                counterTable[counterIndex] --;
            }
            updateGHR(0);
        }
        else{ // Taken
            if(counter_val != 3) {
                counterTable[counterIndex] ++;
            }
            updateGHR(1);
        }
    }

    private void updateGHR(int newVal){
        //Note:
        // If actually:
        // NT -> newVal = 0;
        // T -> newVal = 1;
        // [0, 0]
        GHR.removeFirst();
        GHR.addLast(newVal);
    }

}
