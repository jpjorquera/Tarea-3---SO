JFLAGS = -d
JC = javac
JVM = java 
OUT = ./out/
LIB = './out/exp4j-0.4.8.jar'

default:
	$(JC) $(JFLAGS) $(OUT) ./src/matrices/Matrices.java
	$(JC) $(JFLAGS) $(OUT) ./src/granja/Granja.java
	$(JC) $(JFLAGS) $(OUT) -cp .:$(LIB) ./src/funciones/Funciones.java

clean:
	$(RM) $(OUT)*.class

Funciones:
	$(JVM) -cp .:$(OUT):$(LIB) Funciones

funciones:
	$(JVM) -cp .:$(OUT):$(LIB) Funciones

Matrices:
	$(JVM) -cp $(OUT) Matrices

matrices:
	$(JVM) -cp $(OUT) Matrices

Granja:
	$(JVM) -cp $(OUT) Granja

granja:
	$(JVM) -cp $(OUT) Granja