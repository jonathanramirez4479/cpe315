/*
 * Names: Jonathan Ramirez, Franco Abullarade
 * Section: 03
 * Description: Lab 3 MIPS emulator
 */

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
// import java.lang.*;
// import java.io.*;
//CHanging for github

public class lab4 {
    public static int[] memory = new int[8192]; // data memory
    public static int counter = 0; // program counter

    public static int instrCount = 0;
    // Init pipeline
    static Pipeline pipe = new Pipeline();

    static int pipeCnt = 0;
    public static void main(String[] args) throws IOException {
        readFile.readFile_main(args[0]);
        //Determine Mode
        System.out.println(readFile.labels.toString());
        RegisterFile.initRF(); // init register file
        if(args.length == 1){
            interactiveMode();
        }
        else ScriptMode(args[1]);
    }

    private static void ScriptMode(String filename) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                // Process each line of the file here
                String[] param = line.split(" ");
                System.out.print("mips> ");
                for (String op : param) {
                    System.out.print(op + " ");
                }
                System.out.println();
                lab3Functions(param);
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
            lab3Functions(command);
        }

    }

    private static void lab3Functions(String[] params){
        if(params[0].equalsIgnoreCase("h")){
            // show help
            System.out.println(
                    """

                            h = show help
                            d = dump register state
                            p = show pipeline registers
                            s = step through a single clock cycle step (i.e. simulate 1 cycle and stop)
                            s num = step through num clock cycles
                            r = run until the program ends and display timing summary
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

        // MIPS EMULATOR S

        else if(params[0].trim().equalsIgnoreCase("s")){
            // single step through the program
            // if only s
            if(counter == readFile.instructionsList.size()){
                System.out.println("Sorry, all instructions have been executed");
                return;
            }
            if(params.length == 1) {
                Instructions currentInstr = readFile.instructionsList.get(counter);
                newIteration(currentInstr);
                pipe.showPipe();
            }else if (params.length == 2){
                int i = Integer.parseInt((params[1]));
                while(i > 0 && counter < readFile.instructionsList.size()){
                    Instructions currentInstr = readFile.instructionsList.get(counter);
                    newIteration(currentInstr);
                    i--;
                }
                System.out.println("    " + params[1] +  " instruction(s) executed ");
            }
        }

        else if(params[0].trim().equalsIgnoreCase("p")){
            pipe.showPipe();
        }



        else if(params[0].trim().equalsIgnoreCase("r")){
            // run until program stops
            while(counter < readFile.instructionsList.size()){
                Instructions currentInstr = readFile.instructionsList.get(counter);
                newIteration(currentInstr);


                if(counter == readFile.instructionsList.size()){
                    break;
                }
            }
//            pipe.showPipe();
            System.out.println("Program complete");
            pipeCnt +=  pipe.pipeReg.length;
            double cpi = Math.round(((double) pipeCnt/ instrCount) * 1000.0) / 1000.0;
            String formattedCpi = String.format("%.3f", cpi);
            System.out.println("CPI = " + formattedCpi + " Cycles = " + pipeCnt + " Instructions = " + instrCount);
        }
        else if (params[0].trim().equalsIgnoreCase("c")){
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

    private static void newIteration(Instructions currentInstr){
        if(!pipe.skip_lw && !pipe.skip_execution){
            OperationsMap.doOp(currentInstr);
            instrCount ++;
        }
        pipe.addInstr(currentInstr);

        if(!pipe.skip_lw) {
            counter++;
        }
        pipe.checkPipe();
        pipeCnt ++;
    }

}
