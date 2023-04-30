import java.util.HashMap;

public class RegisterFile {
    private static HashMap<String, Integer> RF = new HashMap<>();
    public void initRF(){
        RF.put("$zero", 0);
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

    public void dumpRegs(){
        System.out.println( "pc = " + 0 + "\n" + //TODO change PC val
                            "$0 = " + RF.get("$zero") + "          " +
                            "$v0 = " + RF.get("$v0")  + "          " +
                            "$v1 = " + RF.get("$v1")  + "          " +
                            "$a1 = " + RF.get("$a0")  + "          \n" +

                            "$a1 = " + RF.get("$a1") + "          " +
                            "$a2 = " + RF.get("$a2")  + "          " +
                            "$a3 = " + RF.get("$a3")  + "          " +
                            "$t0 = " + RF.get("$t0")  + "          \n" +

                            "$t1 = " + RF.get("$t1") + "          " +
                            "$v0 = " + RF.get("$v0")  + "          " +
                            "$v1 = " + RF.get("$v1")  + "          " +
                            "$a1 = " + RF.get("$a0")  + "          \n"


        );
    }

}
