import java.io.File;  // Import the File class
import java.io.FileNotFoundException;  // Import this class to handle errors
// import java.lang.reflect.Array;
import java.util.*;
// import java.lang.*;
// import java.io.*;
//CHanging for github

public class lab3 {
    public static HashMap<String, Integer> labels =
            new HashMap<String, Integer>();
        
    private static ArrayList<Instructions> instructionsList =
            new ArrayList<Instructions>();

    public static HashMap<String, Integer> getLabels(){
        return labels;
    }

    public static void main(String[] args) {
        Init.initInstructions(); // init supported instructions
        Init.initRegisters(); // init available registers
        first_parse(args[0]); //Filename -> args[0]
        second_parse(args[0]);
        System.exit(0);
    }
    public static void first_parse(String filename){
        try{
            File myObj = new File(filename);
            Scanner myReader = new Scanner(myObj);
            int prog_counter = 0;
            int flag = 0;
            while(myReader.hasNextLine()){
                String data = myReader.nextLine();
                // remove beginning and trailing whitespace
                data = data.trim();
                // exclude comments
                if (data.contains(Character.toString('#'))){
                    data = data.substring(0, data.indexOf('#'));
                }
                if(data.isEmpty()){
                    continue;
                }
                // check for labels
                if(data.contains(Character.toString(':'))){
                    labels.put(data.substring(0, data.indexOf(':')),
                            prog_counter);
                    String[] temp_data = data.split(" ");
                    // if the label is the only item in the line
                    //      make sure to account for it with flag
                    if(temp_data.length == 1)
                    {
                        flag = 1;
                        prog_counter++;
                        continue;
                    }
                }
                // if flag = 1, then the previous line was a label
                //      then don't increment counter
                if(flag == 1){
                    flag = 0;
                    continue;
                }
                prog_counter++;
            }
            myReader.close();

        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }
    public static void second_parse(String filename){
        try {
            int line = 0;
            File myObj = new File(filename);
            Scanner myReader = new Scanner(myObj);
            // read each line and trim to place in array
            while (myReader.hasNextLine()) {
                String data = myReader.nextLine();
                data = data.trim(); // remove beginning and trailing whitespace

                //exclude comments
                if (data.contains(Character.toString('#'))) {
                    data = data.substring(0, data.indexOf('#'));
                }
                if (data.isEmpty()) {
                    continue;
                }
                // replace all extra characters to simplify element
                //   collection
                data = data.replace(",", " ")
                        .replace("$", " $")
                        .replace("\t", " ")
                        .replace("(", " ")
                        .replace(")", " ")
                        .replace(":", " ");

                // split into temp array
                ArrayList<String> tempDataArr = new ArrayList<String>(Arrays.
                        asList(data.split(" ")));
                tempDataArr.removeAll(Arrays.asList(""));

                // check for labels and remove
                if(labels.containsKey(tempDataArr.get(0))){
                    tempDataArr.removeAll(Arrays.asList(tempDataArr.
                            get(0)));
                }

                // check if line is empty after removing label
                if(tempDataArr.isEmpty()){
                    continue;
                }

                // variable to hold the operation
                String operation = tempDataArr.get(0);
                // list to hold the operands
                ArrayList<String> operands = new ArrayList<String>();
                // add the operands from tempDataArr to operands list
                for(int i = 1; i < tempDataArr.size(); i++){
                    operands.add(tempDataArr.get(i));
                }

                // create a new instruction object and add it to
                //     instructionsList
                Instructions newInstr = new Instructions(operation, operands, line);
                if(!(newInstr.getType().equals("Invalid"))){
                    instructionsList.add(newInstr);
                }
                else{
                    Init.InvalidInstr.add(newInstr);
                    break;
                }
                line++;
            }

            //PRINT TO TERMINAL
            for(Instructions instr: instructionsList){
                System.out.println(instr.binary);
            }
            if(!Init.InvalidInstr.isEmpty()){
                for (Instructions invalid_instr:Init.InvalidInstr) {
                    System.out.println("invalid instruction: " + invalid_instr.instruction);
                }
            }

            // while loop exited
            myReader.close();

        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }


}
