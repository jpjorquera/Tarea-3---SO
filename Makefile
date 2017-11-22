JFLAGS = -d
JC = javac
JVM = java 
OUT = ./out/

default:
	$(JC) $(JFLAGS) $(OUT) ./src/matrices/Matrices.java
	$(JC) $(JFLAGS) $(OUT) ./src/granja/Granja.java
	$(JC) $(JFLAGS) $(OUT) ./src/funciones/Funciones.java

clean:
	$(RM) $(OUT)*.class

Funciones:
	$(JVM) -cp $(OUT) Funciones

funciones:
	$(JVM) -cp $(OUT) Funciones

Matrices:
	$(JVM) -cp $(OUT) Matrices

matrices:
	$(JVM) -cp $(OUT) Matrices

Granja:
	$(JVM) -cp $(OUT) Granja

granja:
	$(JVM) -cp $(OUT) Granja