/*
 * Names: Jonathan Ramirez, Franco Abullarade
 * Section: 03
 * Description: Lab 3 MIPS emulator
 */
import java.io.*;
// import java.lang.reflect.Array;
import java.util.*;
// import java.lang.*;
// import java.io.*;
//CHanging for github

public class lab4 {
    public static int[] memory = new int[8192]; // data memory
    public static String[] pipeReg = new String[4]; // pipeline registers
    public static int counter = 0; // program counter
    public static int simCounter = 0; // cpu simulator counter
    public static int cycleCount = 0; // cycle count for the program
    public static void main(String[] args) throws IOException {
        pipeReg[0] = "empty";
        pipeReg[1] = "empty";
        pipeReg[2] = "empty";
        pipeReg[3] = "empty";
        readFile.readFile_main(args[0]);

        //Determine Mode
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
                for(int i = 0; i < param.length; i++){
                    System.out.print(param[i] + " ");
                }
                System.out.println();
                lab3Functions(param);
            }
        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
        }
    }

    private static void interactiveMode() throws IOException {


        RegisterFile.initRF(); // init register file

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
                            "\nh = show help\n" +
                            "d = dump register state\n" +
                            "p = show pipeline registers\n" +
                            "s = single step through the program (i.e. simulate 1 cycle and stop)\n" +
                            "s num = step through num clock cycles\n" +
                            "r = run until the program ends and display the timing summary\n" +
                            "m num1 num2 = display data memory from location num1 to num2\n" +
                            "c = clear all registers, memory, and the program counter to 0\n" +
                            "q = exit the program\n");
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
                Instructions currentInstr = readFile.instructionsList.get(counter);
                OperationsMap.findOp(currentInstr);
                instrToPipe(currentInstr.instruction);
                showPipe();
                if(!currentInstr.getType().equals("J")) {
                    counter++;
                }
            }else if (params.length == 2){
                int i = Integer.parseInt((params[1]));
                while(i > 0 && counter < readFile.instructionsList.size()){
                    Instructions currentInstr = readFile.instructionsList.get(counter);
                    OperationsMap.findOp(currentInstr);
                    if(!currentInstr.getType().equals("J")) {
                        counter++;
                    }
                    i--;
                }

                showPipe();
            }
        }
        else if(params[0].trim().equalsIgnoreCase("r")){
            // run until program stops
            while(counter < readFile.instructionsList.size())
            {
                Instructions currentInstr = readFile.instructionsList.get(counter);
                OperationsMap.findOp(currentInstr);
                instrToPipe(currentInstr.instruction);
                if(!currentInstr.getType().equals("J"))
                {
                    counter++;
                }
            }
            // program finished - add the remaining instructions (in the pipe) to the cycle count
            cycleCount += pipeReg.length;
            System.out.println("cycle count: " + cycleCount);
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

    public static void showPipe()
    {
            System.out.println("pc      if/id   id/exe  exe/mem mem/wb");
            System.out.println(simCounter + "      " + pipeReg[0] + "   " + pipeReg[1] + "    " +
                    pipeReg[2] + "   " + pipeReg[3]);
    }
    public static void instrToPipe(String newInstr)
    {
        // newInstr is the instruction waiting at the pc
        /*
        pipeReg[0] -> if/id
        pipeReg[1] -> id/exe
        pipeReg[2] -> exe/mem
        pipeReg[3] -> mem/wb
        1) use-after-load
        Notes:
        lw rt, imm(rs)
            a. check if lw is in register pipeline id/exe
            b. create Instruction var's for current and previous instruction
                i.e:  if/id and id/exe
            c. create strings for register names and compare
            d. check if current instr. has reg's that use rd from previous lw
                d1. if so, then stall; else, normal cycle
         */
        if(pipeReg[1].equals("lw")) // check if id/exe has lw
        {
            Instructions curr_instr = readFile.instructionsList.get(simCounter-1); // current instr (after load)
            Instructions lw_instr = readFile.instructionsList.get(simCounter-2); // prev instr (lw)
            // NOTE: will only support: add, and, or, sub, slt (because these are the ones being tested)
            // op rd rs rt
            String lw_rt = lw_instr.operands.get(0);
            String instr_rs = curr_instr.operands.get(1);
            String instr_rt = curr_instr.operands.get(2);

            if(lw_rt.equalsIgnoreCase(instr_rs) || lw_rt.equalsIgnoreCase(instr_rt))
            {
                // STALL
                pipeReg[3] = pipeReg[2];
                pipeReg[2] = lw_instr.instruction;
                pipeReg[1] = "stall";
                counter = simCounter-1;
                cycleCount++;
            }
            else
                normalInsert(newInstr);
        }
        else
            normalInsert(newInstr);
    }
    public static void normalInsert(String newInstr)
    {
        String[] firstThree = new String[pipeReg.length - 1];
        firstThree[0] = pipeReg[0];
        firstThree[1] = pipeReg[1];
        firstThree[2] = pipeReg[2];

        pipeReg[0] = newInstr;
        pipeReg[1] = firstThree[0];
        pipeReg[2] = firstThree[1];
        pipeReg[3] = firstThree[2];

        simCounter++;
        cycleCount++;
    }
}