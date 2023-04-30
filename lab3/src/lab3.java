import javax.swing.*;
import java.io.*;
// import java.lang.reflect.Array;
import java.util.*;
// import java.lang.*;
// import java.io.*;
//CHanging for github

public class lab3 {

    public static void main(String[] args) throws IOException {
        readFile.readFile_main(args[0]);

        //Determine Mode
        if(args.length == 1){
            interactiveMode();
        }

    }

    private static void interactiveMode() throws IOException {
        while (true){
            System.out.print("mips> ");
            BufferedReader reader = new BufferedReader( new InputStreamReader(System.in));
            String command = reader.readLine();
            if(command.trim().equalsIgnoreCase("h")){
                System.out.println(
                        "h = show help\n" +
                        "d = dump register state\n" +
                        "s = single step through the program (i.e. execute 1 instruction and stop)\n" +
                        "s num = step through num instructions of the program\n" +
                        "r = run until the program ends\n" +
                        "m num1 num2 = display data memory from location num1 to num2\n" +
                        "c = clear all registers, memory, and the program counter to 0\n" +
                        "q = exit the program");
            } else if (command.trim().equalsIgnoreCase("q")) {
                System.exit(0);
            } else if (command.trim().equalsIgnoreCase("d")) {

            }
        }
    }
}