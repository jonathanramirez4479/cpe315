import javax.swing.*;
import java.io.*;
// import java.lang.reflect.Array;
import java.util.*;
// import java.lang.*;
// import java.io.*;
//CHanging for github

public class lab3 {
    public static int[] memory = new int[8192]; // data memory
    public static int counter = 0; // program counter
    public static void main(String[] args) throws IOException {
        readFile.readFile_main(args[0]);

        //Determine Mode
        if(args.length == 1){
            interactiveMode();
        }
    }

    private static void interactiveMode() throws IOException {
        RegisterFile.initRF(); // init register file
        int instrMemSize = readFile.instructionsList.size();

        while (counter <= readFile.instructionsList.size()){
            System.out.print("mips> ");
            BufferedReader reader = new BufferedReader( new InputStreamReader(System.in));
            String[] command = reader.readLine().trim().split(" ");
            if(command[0].equalsIgnoreCase("h")){
                // show help
                System.out.println(
                        "h = show help\n" +
                        "d = dump register state\n" +
                        "s = single step through the program (i.e. execute 1 instruction and stop)\n" +
                        "s num = step through num instructions of the program\n" +
                        "r = run until the program ends\n" +
                        "m num1 num2 = display data memory from location num1 to num2\n" +
                        "c = clear all registers, memory, and the program counter to 0\n" +
                        "q = exit the program");
            } else if (command[0].equalsIgnoreCase("q")) {
                // quit program
                System.exit(0);
            } else if (command[0].equalsIgnoreCase("d")) {
                // dump registers (print register file)
                RegisterFile.dumpRegs();
                System.out.println();
            }
            else if(command[0].equalsIgnoreCase("s")){
                // single step through the program
                // if only s
                if(counter == readFile.instructionsList.size()){
                    System.out.println("Sorry, all instructions have been executed");
                    continue;
                }
                if(command.length == 1) {
                    Instructions currentInstr = readFile.instructionsList.get(counter);
                    OperationsMap.findOp(currentInstr);
                    System.out.println("    1 instruction(s) executed");
                    counter++;
                }
            }
            else if(command[0].equalsIgnoreCase("r")){
                // run until program stops
                if(counter == readFile.instructionsList.size()){
                    System.out.println("Sorry, all instructions have been executed");
                    continue;
                }
                while(counter < readFile.instructionsList.size()){
                    Instructions currentInstr = readFile.instructionsList.get(counter);
                    OperationsMap.findOp(currentInstr);
                    counter++;
                }
            }
            else if (command[0].equalsIgnoreCase("c")){
                // clear registers, memory and set counter to 0
                // TODO reset memory
                counter = 0;
                RegisterFile.clearRegs();
                System.out.println("Simulator reset\n");
            }
            else if(command[0].equalsIgnoreCase("m")){
                // display data memory from num1 to num2
                if(command.length != 3){
                    System.out.println("Too few or too many arguments");
                    continue;
                }
                int num1 = Integer.parseInt(command[1]);
                int num2 = Integer.parseInt(command[2]);
                for(int i = num1; i <= num2; i++){
                    System.out.println("[" + i + "] = " + memory[i]);
                }
            }
        }

    }
}