/*
Authors: Franco Abullarade, Jonathan Ramirez
Project: Lab 6
Class: CPE 315-03
*/
public class lab6 {
    public static void main(String[] args) {
        Caches.InitCaches();
        readFile.parseFile(args[0]);
        Caches.printCaches();
    }
}