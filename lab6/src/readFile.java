import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
//changing for github
public class readFile {

    static LinkedList<Object[]> dataList = new LinkedList<>();

    public static void parseFile(String filename) {
        try {
            File myObj = new File(filename);
            Scanner myReader = new Scanner(myObj);
            int cnt = 0;
            while (myReader.hasNextLine()) {
                String data = myReader.nextLine();
                data = data.trim();
                int dataBinary = Integer.parseInt(data, 16);
                Address fileAddress = new Address(dataBinary, cnt);
                Caches.insertAddress(fileAddress);
                cnt++;
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}