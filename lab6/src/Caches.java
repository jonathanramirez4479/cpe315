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
    public static HashMap<Integer, int[][]> FourWay_2KB_1WB = new HashMap<>();
    public static int FourWay_2KB_1WB_hits = 0;
    public static HashMap<Integer, int[][]> FourWay_2KB_4WB = new HashMap<>();
    public static int FourWay_2KB_4WB_hits = 0;
    public static HashMap<Integer, int[]> DM_4KB_1WB = new HashMap<>();
    public static int DM_4KB_1WB_hits = 0;


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
            FourWay_2KB_1WB.put(i, new int[][]{ {0, 0}, {0, 0}, {0, 0}, {0, 0} } );
        }

        for(int i = 0; i < (1 << 10); i++){
            DM_4KB_1WB.put(i, new int[]{0,0});
         }

        for(int i = 0; i < (1 << 5); i++)
            FourWay_2KB_4WB.put(i, new int[][]{ {0, 0}, {0, 0}, {0, 0}, {0, 0} });
    }

    public static void insertAddress(Address addr) {
        cacheAccesses++;
//        System.out.println(DM_2KB_1WB.get(addr.DM_2KB_1WB_index)[1]);
//        System.out.println(addr.DM_2KB_1WB_tag);
        if (DM_2KB_1WB.get(addr.DM_2KB_1WB_index)[1] != addr.DM_2KB_1WB_tag) {
            // If old tag at index != old tag --> Miss
            DM_2KB_1WB.replace(addr.DM_2KB_1WB_index, new int[]{addr.line, addr.DM_2KB_1WB_tag});
        } else {
            DM_2KB_1WB_hits++;
        }
        if (DM_2KB_2WB.get(addr.DM_2KB_2WB_index)[1] != addr.DM_2KB_2WB_tag) {
            // If old tag at index != old tag --> Miss
            DM_2KB_2WB.replace(addr.DM_2KB_2WB_index, new int[]{addr.line, addr.DM_2KB_2WB_tag});
        } else {
            DM_2KB_2WB_hits++;
        }
        if (DM_2KB_4WB.get(addr.DM_2KB_4WB_index)[1] != addr.DM_2KB_4WB_tag) {
            // If old tag at index != old tag --> Miss
            DM_2KB_4WB.replace(addr.DM_2KB_4WB_index, new int[]{addr.line, addr.DM_2KB_4WB_tag});
        } else {
            DM_2KB_4WB_hits++;
        }

        Insert_TwoWay_2KB_1WB(addr);
        Insert_FourWay_2KB_1WB(addr);
        Insert_FourWay_2KB_4WB(addr);

        if (DM_4KB_1WB.get(addr.DM_4KB_1WB_index)[1] != addr.DM_4KB_1WB_tag) {
            // If old tag at index != old tag --> Miss
            DM_4KB_1WB.replace(addr.DM_4KB_1WB_index, new int[]{addr.line, addr.DM_4KB_1WB_tag});
        } else {
            DM_4KB_1WB_hits++;
        }

    }

    private static void Insert_TwoWay_2KB_1WB(Address addr){
        //###########################################################################################
        boolean firstMiss = false;
        boolean secondMiss = false;
        // { {line number, tag}, {line number, tag} }
        if (TwoWay_2KB_1WB.get(addr.TwoWay_2KB_1WB_index)[0][1] != addr.TwoWay_2KB_1WB_tag) {
            firstMiss = true; // check if first cache misses
        } else { // Hits First Cache
            TwoWay_2KB_1WB.get(addr.TwoWay_2KB_1WB_index)[0][0] = addr.line; // update line #
            TwoWay_2KB_1WB_hits ++;
            return;

        }
        if (TwoWay_2KB_1WB.get(addr.TwoWay_2KB_1WB_index)[1][1] != addr.TwoWay_2KB_1WB_tag) {
            secondMiss = true; // check if second cache misses
        } else { // Hits Second Cache
            TwoWay_2KB_1WB.get(addr.TwoWay_2KB_1WB_index)[1][0] = addr.line; // update line #
            TwoWay_2KB_1WB_hits ++;
            return;
        }
        // Determine which cache is LRU
        int cache1_Line = TwoWay_2KB_1WB.get(addr.TwoWay_2KB_1WB_index)[0][0];
        int cache2_Line = TwoWay_2KB_1WB.get(addr.TwoWay_2KB_1WB_index)[1][0];
        if(cache1_Line > cache2_Line){
            // cache1 used more recently, therefore replace cache2
            int[] keep_arr = TwoWay_2KB_1WB.get(addr.TwoWay_2KB_1WB_index)[0];
            TwoWay_2KB_1WB.replace(addr.TwoWay_2KB_1WB_index, new int[][]
                    {keep_arr, {addr.line, addr.TwoWay_2KB_1WB_tag}});
        }
        else {
            // cache2 more recently used, therefore replace cache 1
            // cache1 used more recently, therefore replace cache2
            int[] keep_arr = TwoWay_2KB_1WB.get(addr.TwoWay_2KB_1WB_index)[1];
            TwoWay_2KB_1WB.replace(addr.TwoWay_2KB_1WB_index, new int[][]
                    {{addr.line, addr.TwoWay_2KB_1WB_tag}, keep_arr});
        }
    }

    private static void Insert_FourWay_2KB_1WB(Address addr)
    {
        // { {line number, tag}, {line number, tag}, {line number, tag}, {line number, tag} }
        if(FourWay_2KB_1WB.get(addr.FourWay_2KB_1WB_index)[0][1] == addr.FourWay_2KB_1WB_tag)
        {
            FourWay_2KB_1WB.get(addr.FourWay_2KB_1WB_index)[0][0] = addr.line;
            FourWay_2KB_1WB_hits++;
            return;
        }
        if(FourWay_2KB_1WB.get(addr.FourWay_2KB_1WB_index)[1][1] == addr.FourWay_2KB_1WB_tag)
        {
            FourWay_2KB_1WB.get(addr.FourWay_2KB_1WB_index)[1][0] = addr.line;
            FourWay_2KB_1WB_hits++;
            return;
        }
        if(FourWay_2KB_1WB.get(addr.FourWay_2KB_1WB_index)[2][1] == addr.FourWay_2KB_1WB_tag)
        {
            FourWay_2KB_1WB.get(addr.FourWay_2KB_1WB_index)[2][0] = addr.line;
            FourWay_2KB_1WB_hits++;
            return;
        }
        if(FourWay_2KB_1WB.get(addr.FourWay_2KB_1WB_index)[3][1] == addr.FourWay_2KB_1WB_tag)
        {
            FourWay_2KB_1WB.get(addr.FourWay_2KB_1WB_index)[3][0] = addr.line;
            FourWay_2KB_1WB_hits++;
            return;
        }
        // at this point, all were misses
        // Determine which cache was LRU
        int cache1_Line = FourWay_2KB_1WB.get(addr.FourWay_2KB_1WB_index)[0][0];
        int cache2_Line = FourWay_2KB_1WB.get(addr.FourWay_2KB_1WB_index)[1][0];
        int cache3_Line = FourWay_2KB_1WB.get(addr.FourWay_2KB_1WB_index)[2][0];
        int cache4_Line = FourWay_2KB_1WB.get(addr.FourWay_2KB_1WB_index)[3][0];

        int[] cache_lines = {cache1_Line, cache2_Line, cache3_Line, cache4_Line};
        int min = cache1_Line;
        for(int line : cache_lines) // get min (LRU)
        {
            if(line < min)
                min = line;
        }
        // check for LRU, and replace corresponding cache:
        if(min == cache1_Line)
            FourWay_2KB_1WB.get(addr.FourWay_2KB_1WB_index)[0] = new int[]{addr.line, addr.FourWay_2KB_1WB_tag};
        else if(min == cache2_Line)
            FourWay_2KB_1WB.get(addr.FourWay_2KB_1WB_index)[1] = new int[]{addr.line, addr.FourWay_2KB_1WB_tag};
        else if(min == cache3_Line)
            FourWay_2KB_1WB.get(addr.FourWay_2KB_1WB_index)[2] = new int[]{addr.line, addr.FourWay_2KB_1WB_tag};
        else if(min == cache4_Line)
            FourWay_2KB_1WB.get(addr.FourWay_2KB_1WB_index)[3] = new int[]{addr.line, addr.FourWay_2KB_1WB_tag};
    }

    private static void Insert_FourWay_2KB_4WB(Address addr)
    {
        // { {line number, tag}, {line number, tag}, {line number, tag}, {line number, tag} }
        if(FourWay_2KB_4WB.get(addr.FourWay_2KB_4WB_index)[0][1] == addr.FourWay_2KB_4WB_tag)
        {
            FourWay_2KB_4WB.get(addr.FourWay_2KB_4WB_index)[0][0] = addr.line;
            FourWay_2KB_4WB_hits++;
            return;
        }
        if(FourWay_2KB_4WB.get(addr.FourWay_2KB_4WB_index)[1][1] == addr.FourWay_2KB_4WB_tag)
        {
            FourWay_2KB_4WB.get(addr.FourWay_2KB_4WB_index)[1][0] = addr.line;
            FourWay_2KB_4WB_hits++;
            return;
        }
        if(FourWay_2KB_4WB.get(addr.FourWay_2KB_4WB_index)[2][1] == addr.FourWay_2KB_4WB_tag)
        {
            FourWay_2KB_4WB.get(addr.FourWay_2KB_4WB_index)[2][0] = addr.line;
            FourWay_2KB_4WB_hits++;
            return;
        }
        if(FourWay_2KB_4WB.get(addr.FourWay_2KB_4WB_index)[3][1] == addr.FourWay_2KB_4WB_tag)
        {
            FourWay_2KB_4WB.get(addr.FourWay_2KB_4WB_index)[3][0] = addr.line;
            FourWay_2KB_4WB_hits++;
            return;
        }
        // at this point, all were misses
        // Determine which cache was LRU
        int cache1_Line = FourWay_2KB_4WB.get(addr.FourWay_2KB_4WB_index)[0][0];
        int cache2_Line = FourWay_2KB_4WB.get(addr.FourWay_2KB_4WB_index)[1][0];
        int cache3_Line = FourWay_2KB_4WB.get(addr.FourWay_2KB_4WB_index)[2][0];
        int cache4_Line = FourWay_2KB_4WB.get(addr.FourWay_2KB_4WB_index)[3][0];

        int[] cache_lines = {cache1_Line, cache2_Line, cache3_Line, cache4_Line};
        int min = cache1_Line;
        for(int line : cache_lines) // get min (LRU)
        {
            if(line < min)
                min = line;
        }
        // check for LRU, and replace corresponding cache:
        if(min == cache1_Line)
            FourWay_2KB_4WB.get(addr.FourWay_2KB_4WB_index)[0] = new int[]{addr.line, addr.FourWay_2KB_4WB_tag};
        else if(min == cache2_Line)
            FourWay_2KB_4WB.get(addr.FourWay_2KB_4WB_index)[1] = new int[]{addr.line, addr.FourWay_2KB_4WB_tag};
        else if(min == cache3_Line)
            FourWay_2KB_4WB.get(addr.FourWay_2KB_4WB_index)[2] = new int[]{addr.line, addr.FourWay_2KB_4WB_tag};
        else if(min == cache4_Line)
            FourWay_2KB_4WB.get(addr.FourWay_2KB_4WB_index)[3] = new int[]{addr.line, addr.FourWay_2KB_4WB_tag};
    }

    public static void printCaches(){
         DecimalFormat hundredth = new DecimalFormat("0.00");
         System.out.println(
                 "Cache #1\n"+
                 "Cache Size: 2048B\tAssociativity: 1\tBlock size: 1\n" +
                 "Hits: " + DM_2KB_1WB_hits + " Hit Rate: " +
                     hundredth.format(Math.round((((double) DM_2KB_1WB_hits /cacheAccesses) * 100) * 100.00) / 100.00) +"%\n"+
                 "---------------------------\n"+
                 "Cache #2\n"+
                 "Cache Size: 2048B\tAssociativity: 1\tBlock size: 2\n" +
                 "Hits: " + DM_2KB_2WB_hits + " Hit Rate: " +
                 hundredth.format(Math.round((((double) DM_2KB_2WB_hits /cacheAccesses) * 100) * 100.0) / 100.0)+"%\n"+
                 "---------------------------\n"+
                 "Cache #3\n"+
                 "Cache Size: 2048B\tAssociativity: 1\tBlock size: 4\n" +
                 "Hits: " + DM_2KB_4WB_hits + " Hit Rate: " +
                 hundredth.format(Math.round((((double) DM_2KB_4WB_hits /cacheAccesses) * 100) * 100.0) / 100.0)+"%\n"+
                 "---------------------------\n"+
                 "Cache #4\n"+
                         "Cache Size: 2048B\tAssociativity: 2\tBlock size: 1\n" +
                         "Hits " + TwoWay_2KB_1WB_hits + " Hit Rate " +
                         hundredth.format(Math.round((((double) TwoWay_2KB_1WB_hits /cacheAccesses) * 100) * 100.0) / 100.0)+"%\n"+
                 "---------------------------\n"+
                 "Cache #5\n"+
                 "Cache Size: 2048B\tAssociativity: 4\tBlock size: 1\n" +
                 "Hits: " + FourWay_2KB_1WB_hits + " Hit Rate: " +
                 hundredth.format(Math.round((((double) FourWay_2KB_1WB_hits /cacheAccesses) * 100) * 100.0) / 100.0)+"%\n"+
                 "---------------------------\n"+
                 "Cache #6\n"+
                 "Cache Size: 2048B\tAssociativity: 4\tBlock size: 4\n" +
                 "Hits: " + FourWay_2KB_4WB_hits + " Hit Rate: " +
                 hundredth.format(Math.round((((double) FourWay_2KB_4WB_hits /cacheAccesses) * 100) * 100.0) / 100.0)+"%\n"+
                 "---------------------------\n"+

                 "Cache #7\n"+
                 "Cache Size: 4096B\tAssociativity: 1\tBlock size: 1\n" +
                 "Hits: " + DM_4KB_1WB_hits + " Hit Rate: " +
                 hundredth.format(Math.round((((double) DM_4KB_1WB_hits /cacheAccesses) * 100) * 100.0) / 100.0)+"%\n"+
                 "---------------------------\n"

                 );
    }
}
