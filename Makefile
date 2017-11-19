JFLAGS = -d
JC = javac
JVM = java 
OUT = ./out/

default:
	$(JC) $(JFLAGS) $(OUT) ./src/matrices/Matrices.java

clean:
	$(RM) $(OUT)*.class

Matrices:
	$(JVM) -cp $(OUT) Matrices