import java.util.HashMap;
import java.util.ArrayList;
//changing for github
public class RegisterFile {

    public static HashMap<String, Integer> RF = new HashMap<>();
    public static void initRF(){
        // reg, reg_val
        RF.put("$0", 0);
        RF.put("$v0", 0);
        RF.put("$v1", 0);
        RF.put("$a0", 0);
        RF.put("$a1", 0);
        RF.put("$a2", 0);
        RF.put("$a3", 0);
        RF.put("$t0", 0);
        RF.put("$t1", 0);
        RF.put("$t2", 0);
        RF.put("$t3", 0);
        RF.put("$t4", 0);
        RF.put("$t5", 0);
        RF.put("$t6", 0);
        RF.put("$t7", 0);
        RF.put("$s0", 0);
        RF.put("$s1", 0);
        RF.put("$s2", 0);
        RF.put("$s3", 0);
        RF.put("$s4", 0);
        RF.put("$s5", 0);
        RF.put("$s6", 0);
        RF.put("$s7", 0);
        RF.put("$t8", 0);
        RF.put("$t9", 0);
        RF.put("$sp", 0);
        RF.put("$ra", 0);
    }

    public static void dumpRegs(){
        System.out.println( "\npc = " + lab5.counter + "\n" +
                            "$0 = " + RF.get("$0") + "           " +
                            "$v0 = " + RF.get("$v0")  + "          " +
                            "$v1 = " + RF.get("$v1")  + "          " +
                            "$a0 = " + RF.get("$a0")  + "          \n" +

                            "$a1 = " + RF.get("$a1") + "          " +
                            "$a2 = " + RF.get("$a2")  + "          " +
                            "$a3 = " + RF.get("$a3")  + "          " +
                            "$t0 = " + RF.get("$t0")  + "          \n" +

                            "$t1 = " + RF.get("$t1") + "          " +
                            "$t2 = " + RF.get("$t2")  + "          " +
                            "$t3 = " + RF.get("$t3")  + "          " +
                            "$t4 = " + RF.get("$t4")  + "          \n" +

                            "$t5 = " + RF.get("$t5") + "          " +
                            "$t6 = " + RF.get("$t6") + "          " +
                            "$t7 = " + RF.get("$t7") + "          " +
                            "$s0 = " + RF.get("$s0") + "          \n" +

                            "$s1 = " + RF.get("$s1") + "          " +
                            "$s2 = " + RF.get("$s2") + "          " +
                            "$s3 = " + RF.get("$s3") + "          " +
                            "$s4 = " + RF.get("$s4") + "          \n" +

                            "$s5 = " + RF.get("$s5") + "          " +
                            "$s6 = " + RF.get("$s6") + "          " +
                            "$s7 = " + RF.get("$s7") + "          " +
                            "$t8 = " + RF.get("$t8") + "          \n" +

                            "$t9 = " + RF.get("$t9") + "          " +
                            "$sp = " + RF.get("$sp") + "          " +
                            "$ra = " + RF.get("$ra") + "          \n"
        );
    }
    public static void clearRegs(){
        initRF();
    }

}
