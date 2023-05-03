lab3:
	/opt/jdk-16/bin/javac *.java
run_tests:
	/opt/jdk-16/bin/java lab3 newtest1.asm test1Script.txt > out
	diff -w -B out ref_out
