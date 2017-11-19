import java.io.*;

public class Matrices {
	public static void main(String[] args) throws IOException {
		// Intentar leer archivo
		BufferedReader archivo = null;
		try {
			archivo = new BufferedReader(new FileReader("../../matrices.txt"));
		}
		catch (Exception e) {
			System.out.println("Archivo de matrices no encontrado \n");
			System.exit(-1);
		}
		// Sacar tamaño de matrices
		String line = archivo.readLine();
		String[] sizes = line.split(" ");
		String[] sizes1 = sizes[0].split("x");
		String[] sizes2 = sizes[1].split("x");
		archivo.readLine();

		// Leer lineas de las matrices
		int[][] matriz1 = new int[Integer.parseInt(sizes1[0])][Integer.parseInt(sizes1[1])];
		int[][] matriz2 = new int[Integer.parseInt(sizes2[0])][Integer.parseInt(sizes2[1])];
		for (int i=0; i<Integer.parseInt(sizes1[0]); i++) {
			String[] fila = archivo.readLine().split(" ");
			for (int j=0; j<Integer.parseInt(sizes1[1]); j++) {
				matriz1[i][j] = Integer.parseInt(fila[j]);
			}
		}
		archivo.readLine();
		for (int i=0; i<Integer.parseInt(sizes2[0]); i++) {
			String[] fila = archivo.readLine().split(" ");
			for (int j=0; j<Integer.parseInt(sizes2[1]); j++) {
				matriz2[i][j] = Integer.parseInt(fila[j]);
			}
		}
		int cantidad = Integer.parseInt(sizes1[1]);
		int elemento = multiplicar(matriz1, matriz2, cantidad, 2, 4);
		System.out.println("elem = "+elemento);
		archivo.close();






		System.exit(0);

		RunMatrices R1 = new RunMatrices( "Thread-1");
      	R1.start();
      
      	RunMatrices R2 = new RunMatrices( "Thread-2");
      	R2.start();
	}

	public static int multiplicar(int[][] matriz1, int[][] matriz2, int cant, int n_fila, int n_columna) {
		int sum = 0;
		int elem1 = 0;
		int elem2 = 0;
		n_fila--;
		n_columna--;
		for (int i=0; i<cant; i++) {
			sum += matriz1[n_fila][i] * matriz2[i][n_columna];
		}
		return sum;
	}


}

class RunMatrices implements Runnable {
	// Constructor
	private Thread t;
   	private String threadName;

   	RunMatrices(String name) {
      	threadName = name;
      	System.out.println("Creating " +  threadName);
   	}

   	// Metodos
	public void run() {
		System.out.println("Running " +  threadName);
		try {
         for(int i = 4; i > 0; i--) {
            System.out.println("Thread: " + threadName + ", " + i);
            // Let the thread sleep for a while.
            Thread.sleep(50);
         }
      	} catch (InterruptedException e) {
         System.out.println("Thread " +  threadName + " interrupted. \n");
      	}
      	System.out.println("Thread " +  threadName + " exiting. \n");
	}

	public void start () {
      System.out.println("Starting " +  threadName + "\n");
      if (t == null) {
         t = new Thread (this, threadName);
         t.start ();
      }
   }
}