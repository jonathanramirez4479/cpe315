import java.util.Arrays;
import java.util.HashMap;
import java.math.*;
import java.text.DecimalFormat;
import java.util.Objects;

public class Caches {

    public static int cacheAccesses = 0;
    public static HashMap<Integer, int[]> DM_2KB_1WB = new HashMap<>();
    public static int DM_2KB_1WB_hits = 0;


    public static HashMap<Integer, int[]> DM_2KB_2WB = new HashMap<>();

    public static int DM_2KB_2WB_hits = 0;

    public static HashMap<Integer, int[]> DM_2KB_4WB = new HashMap<>();
    public static int DM_2KB_4WB_hits = 0;

    public static HashMap<Integer, int[][]> TwoWay_2KB_1WB = new HashMap<>();
    public static int TwoWay_2KB_1WB_hits = 0;
    public static HashMap<Integer, int[]> FourWay_4KB_1WB = new HashMap<>();
    public static HashMap<Integer, int[]> FourWay_4KB_4WB = new HashMap<>();
    public static HashMap<Integer, int[]> DM_4KB_1WB = new HashMap<>();



     public static void InitCaches(){
        /* This is initializing:
         For 2^(#ofIndex) --> line#/tags to 0
         */
        // Notes:
        //      1<<N --> 2^N
        //      key = address (index)
        //      val = int[] = { line# (used for LRU), tag }

        for(int i = 0; i < (1<<9); i++){
            DM_2KB_1WB.put(i, new int[]{0, 0});
        }
        for(int i = 0; i < (1<<8); i++){
            DM_2KB_2WB.put(i, new int[]{0, 0});
            TwoWay_2KB_1WB.put(i, new int[][]{{0,0}, {0,0}});

        }
        for(int i = 0; i < (1<<7); i++){
            DM_2KB_4WB.put(i, new int[]{0, 0});
        }
    }

    public static void insertAddress(Address addr){
        cacheAccesses++;
//        System.out.println(DM_2KB_1WB.get(addr.DM_2KB_1WB_index)[1]);
//        System.out.println(addr.DM_2KB_1WB_tag);
        if(DM_2KB_1WB.get(addr.DM_2KB_1WB_index)[1] != addr.DM_2KB_1WB_tag){
            // If old tag at index != old tag --> Miss
            DM_2KB_1WB.replace(addr.DM_2KB_1WB_index, new int[] {addr.line, addr.DM_2KB_1WB_tag});
        }
        else {
            DM_2KB_1WB_hits ++;
        }
        if(DM_2KB_2WB.get(addr.DM_2KB_2WB_index)[1] != addr.DM_2KB_2WB_tag){
            // If old tag at index != old tag --> Miss
            DM_2KB_2WB.replace(addr.DM_2KB_2WB_index, new int[] {addr.line, addr.DM_2KB_2WB_tag});
        }
        else {
            DM_2KB_2WB_hits ++;
        }
        if(DM_2KB_4WB.get(addr.DM_2KB_4WB_index)[1] != addr.DM_2KB_4WB_tag){
            // If old tag at index != old tag --> Miss
            DM_2KB_4WB.replace(addr.DM_2KB_4WB_index, new int[] {addr.line, addr.DM_2KB_4WB_tag});
        }
        else {
            DM_2KB_4WB_hits ++;
        }
        boolean firstMiss = false;
        boolean secondMiss = false;
        if(TwoWay_2KB_1WB.get(addr.TwoWay_2KB_1WB_index)[0][1] != addr.TwoWay_2KB_1WB_tag) {
            firstMiss = true;
        }
        if(TwoWay_2KB_1WB.get(addr.TwoWay_2KB_1WB_index)[1][1] != addr.TwoWay_2KB_1WB_tag) {
            secondMiss = true;
        }

        if(firstMiss && secondMiss) { // if both are misses, replace LRU
            if (TwoWay_2KB_1WB.get(addr.TwoWay_2KB_1WB_index)[0][0] <=
                    TwoWay_2KB_1WB.get(addr.TwoWay_2KB_1WB_index)[1][0]) {
                // {[LRU],[MRU]} therefore keep the second
                int[] keep = TwoWay_2KB_1WB.get(addr.TwoWay_2KB_1WB_index)[1];
                TwoWay_2KB_1WB.replace(addr.TwoWay_2KB_1WB_index,
                        new int[][]{{addr.line, addr.TwoWay_2KB_1WB_tag}, keep});
            } else {
                // {[MRU],[LRU]} therefore keep the first
                int[] keep = TwoWay_2KB_1WB.get(addr.TwoWay_2KB_1WB_index)[0];
                TwoWay_2KB_1WB.replace(addr.TwoWay_2KB_1WB_index,
                        new int[][]{keep, {addr.line, addr.TwoWay_2KB_1WB_tag}});
            }
        } else if (firstMiss){ // If only the first misses
            int[] keep = TwoWay_2KB_1WB.get(addr.TwoWay_2KB_1WB_index)[1];
            TwoWay_2KB_1WB.replace(addr.TwoWay_2KB_1WB_index,
                    new int[][]{{addr.line, addr.TwoWay_2KB_1WB_tag}, keep});
        } else if (secondMiss) { // if only the second misses
            int[] keep = TwoWay_2KB_1WB.get(addr.TwoWay_2KB_1WB_index)[0];
            TwoWay_2KB_1WB.replace(addr.TwoWay_2KB_1WB_index,
                    new int[][]{keep, {addr.line, addr.TwoWay_2KB_1WB_tag}});
        }
        if(!(firstMiss || secondMiss)) TwoWay_2KB_1WB_hits ++;
    }




    public static void printCaches(){
         DecimalFormat hundredth = new DecimalFormat("0.00");
         System.out.println(
                 "Cache #1\n"+
                 "Cache Size: 2048B\tAssociativity: 1\tBlock size: 1\n" +
                 "Hits " + DM_2KB_1WB_hits + " Hit Rate " +
                     hundredth.format(Math.round((((double) DM_2KB_1WB_hits /cacheAccesses) * 100) * 100.00) / 100.00) +"%\n"+
                 "---------------------------\n"+
                 "Cache #2\n"+
                 "Cache Size: 2048B\tAssociativity: 1\tBlock size: 2\n" +
                 "Hits " + DM_2KB_2WB_hits + " Hit Rate " +
                 hundredth.format(Math.round((((double) DM_2KB_2WB_hits /cacheAccesses) * 100) * 100.0) / 100.0)+"%\n"+
                 "---------------------------\n"+
                 "Cache #3\n"+
                 "Cache Size: 2048B\tAssociativity: 1\tBlock size: 4\n" +
                 "Hits " + DM_2KB_4WB_hits + " Hit Rate " +
                 hundredth.format(Math.round((((double) DM_2KB_4WB_hits /cacheAccesses) * 100) * 100.0) / 100.0)+"%\n"+
                 "---------------------------\n"+
                 "Cache #4\n"+
                         "Cache Size: 2048B\tAssociativity: 2\tBlock size: 1\n" +
                         "Hits " + TwoWay_2KB_1WB_hits + " Hit Rate " +
                         hundredth.format(Math.round((((double) TwoWay_2KB_1WB_hits /cacheAccesses) * 100) * 100.0) / 100.0)+"%\n"+
                         "---------------------------\n"

                 );
    }
}
