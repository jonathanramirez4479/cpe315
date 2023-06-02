
public class lab6 {
    public static void main(String[] args) {
        Caches.InitCaches();
        readFile.parseFile("src/mem_stream.1");
        Caches.printCaches();
    }
}