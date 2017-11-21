import java.io.*;

public class Matrices {
	public static void main(String[] args) throws IOException {
		// Intentar leer archivo
		BufferedReader archivo = null;
		try {
			archivo = new BufferedReader(new FileReader("./matrices.txt"));
		}
		catch (Exception e) {
			System.out.println("Error al abrir el archivo");
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
		archivo.close();
		int cantidad = Integer.parseInt(sizes1[1]);

		// Hacer cálculos para cada matriz en cada thread
		int contador = 1;
		int[][] M_resultante = new int[Integer.parseInt(sizes1[0])][Integer.parseInt(sizes2[1])];
		int actual = 0;
		for (int i=0; i<Integer.parseInt(sizes1[0]); i++) {
			for (int j=0; j<Integer.parseInt(sizes2[1]); j++) {
				RunMatrices hilo = new RunMatrices("hilo "+ (contador++));
				hilo.start();
				actual = hilo.multiplicar(matriz1, matriz2, cantidad, (i+1), (j+1));
				hilo.stop();
				M_resultante[i][j] = actual;
			}
		}

		// Escribir en archivo
		PrintWriter writer = null;
		try {
			writer = new PrintWriter("./matriz_resultante.txt");
			writer.println(Integer.parseInt(sizes1[0])+"x"+Integer.parseInt(sizes2[1])+"\n");
			// Recorrer matriz numérica
			for (int i=0; i<Integer.parseInt(sizes1[0]); i++) {
				for (int j=0; j<Integer.parseInt(sizes2[1]); j++) {
					writer.print(M_resultante[i][j]+" ");
				}
				writer.println();
			}
		}
		catch (IOException e) {
			System.out.println("Error al crear archivo");
			System.exit(1);
		}
		finally {
			try {
				writer.close();
			} 
			catch (Exception ex) {
				System.out.println("Error al cerrar el archivo");
			}
		}
	}
}

class RunMatrices implements Runnable {
	// Constructor
	private Thread t;
   	private String threadName;
   	private volatile boolean exit = false;

   	RunMatrices(String name) {
      	threadName = name;
   	}

   	// Metodos
	public void run(){
		while (!exit) {
			try {
				Thread.sleep(50);
			}
			catch (Exception e) {
				t.interrupt();
			}
		}
	}

	public void start() {
      if (t == null) {
         t = new Thread (this, threadName);
         t.start ();
      }
   	}

   	public void stop() {
   		exit = true;
   	}

   // Método para multiplicar matrices, especificando qué fila, columna y el largo de ambas (para ser multiplicable)
   	public int multiplicar(int[][] matriz1, int[][] matriz2, int cant, int n_fila, int n_columna) {
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