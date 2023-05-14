lab4:
	/opt/jdk-16/bin/javac *.java
run_tests:
	/opt/jdk-16/bin/java lab4 test1.asm test1Script.txt > out
	diff -w -B out ref_out1
	/opt/jdk-16/bin/java lab4 test2.asm test2Script.txt > out
	diff -w -B out ref_out2
	/opt/jdk-16/bin/java lab4 test3.asm test3Script.txt > out
	diff -w -B out ref_out3
