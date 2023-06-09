
public class lab6 {
    public static void main(String[] args) {
        Caches.InitCaches();
        readFile.parseFile(args[0]);
        Caches.printCaches();
    }
}