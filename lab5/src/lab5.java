/*
 * Names: Jonathan Ramirez, Franco Abullarade
 * Section: 03
 * Description: Lab 3 MIPS emulator
 */
import javax.swing.*;
import java.io.*;
// import java.lang.reflect.Array;
import java.util.*;
// import java.lang.*;
// import java.io.*;
//CHanging for github

public class lab5 {
    public static int[] memory = new int[8192]; // data memory
    public static int counter = 0; // program counter

    public static BranchPredictor branchP;

    public static void main(String[] args) throws IOException {
        readFile.readFile_main(args[0]);
        RegisterFile.initRF(); // init register file
        //Determine Mode
        if(args.length == 1){
            interactiveMode();
        }

        else{
            branchP = new BranchPredictor(args); // make new BranchPredictor obj with default GHR size
            ScriptMode(args[1]);
        }
    }

    private static void ScriptMode(String filename) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                // Process each line of the file here
                String[] param = line.split(" ");
                System.out.print("mips> ");
                for(int i = 0; i < param.length; i++){
                    System.out.print(param[i] + " ");
                }
                System.out.println();
                lab5Functions(param);
            }
        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
        }
    }

    private static void interactiveMode() throws IOException {

        while (counter <= readFile.instructionsList.size()){
            System.out.print("mips> ");
            BufferedReader reader = new BufferedReader( new InputStreamReader(System.in));
            String[] command = reader.readLine().trim().split(" ");
            lab5Functions(command);
        }

    }

    private static void lab5Functions(String[] params){
        if(params[0].equalsIgnoreCase("h")){
            // show help
            System.out.println(
                    """
                            h = show help
                            b = output the branch predictor accuracy
                            d = dump register state
                            s = single step through the program (i.e. execute 1 instruction and stop)
                            s num = step through num instructions of the program
                            r = run until the program ends
                            m num1 num2 = display data memory from location num1 to num2
                            c = clear all registers, memory, and the program counter to 0
                            q = exit the program
                            """);
        } else if (params[0].trim().equalsIgnoreCase("q")) {
            // quit program
            System.exit(0);
        } else if (params[0].trim().equalsIgnoreCase("d")) {
            // dump registers (print register file)
            RegisterFile.dumpRegs();
            System.out.println();
        }
        else if(params[0].trim().equalsIgnoreCase("s")){
            // single step through the program
            // if only s
            if(counter == readFile.instructionsList.size()){
                System.out.println("Sorry, all instructions have been executed");
                return;
            }
            if(params.length == 1) {
                newIteration();
                System.out.println("    1 instruction(s) executed ");
            }else if (params.length == 2){
                int i = Integer.parseInt((params[1]));
                while(i > 0 && counter < readFile.instructionsList.size()){
                    newIteration();
                    i--;
                }
                System.out.println("    " + params[1] +  " instruction(s) executed ");
            }
        }
        else if(params[0].trim().equalsIgnoreCase("r")){
            // run until program stops
            while(counter < readFile.instructionsList.size()){
                newIteration();
                if(counter == readFile.instructionsList.size()){
                    break;
                }
            }

        } else if (params[0].trim().equalsIgnoreCase("b")) {
            double calcPercent = ((branchP.correctPrediction) * 1.0 / BranchPredictor.totalBranches) * 100;
            double calcPercentFormatted = Math.round( calcPercent * 100.0) / 100.0;
            System.out.println("accuracy "+ calcPercentFormatted +"% ("+ branchP.correctPrediction +
                    " correct predictions, "+ BranchPredictor.totalBranches +" predictions) ");
        } else if (params[0].trim().equalsIgnoreCase("c")){
            // clear registers, memory and set counter to 0
            counter = 0;
            RegisterFile.clearRegs(); // calls initRF
            Arrays.fill(memory, 0); // clears memory
            System.out.println("        Simulator reset\n");
        }
        else if(params[0].equalsIgnoreCase("m")){
            // display data memory from num1 to num2
            if(params.length != 3){
                System.out.println("Too few or too many arguments");
                return;
            }
            int num1 = Integer.parseInt(params[1]);
            int num2 = Integer.parseInt(params[2]);
            for(int i = num1; i <= num2; i++){
                System.out.println("[" + i + "] = " + memory[i]);
            }
            System.out.println();
        }

    }

    static void newIteration(){
        Instructions currentInstr = readFile.instructionsList.get(counter);
        OperationsMap.doOp(currentInstr);
        if(!currentInstr.getType().equals("J")) {
            counter++;
        }
    }

}
