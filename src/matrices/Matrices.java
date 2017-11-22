import java.io.*;
import java.util.*;

public class Matrices {
	public static int[][] matriz1 = null;
	public static int[][] matriz2 = null;

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
		Matrices.matriz1 = new int[Integer.parseInt(sizes1[0])][Integer.parseInt(sizes1[1])];
		Matrices.matriz2 = new int[Integer.parseInt(sizes2[0])][Integer.parseInt(sizes2[1])];
		// Leer matriz 1
		for (int i=0; i<Integer.parseInt(sizes1[0]); i++) {
			String[] fila = archivo.readLine().split(" ");
			for (int j=0; j<Integer.parseInt(sizes1[1]); j++) {
				Matrices.matriz1[i][j] = Integer.parseInt(fila[j]);
			}
		}
		archivo.readLine();
		// Leer matriz 2
		for (int i=0; i<Integer.parseInt(sizes2[0]); i++) {
			String[] fila = archivo.readLine().split(" ");
			for (int j=0; j<Integer.parseInt(sizes2[1]); j++) {
				Matrices.matriz2[i][j] = Integer.parseInt(fila[j]);
			}
		}

		archivo.close();
		int cantidad = Integer.parseInt(sizes1[1]);
		int contador = 1;
		// Hacer operaciones en cada hilo
		ArrayList<RunMatrices> hilos = new ArrayList<RunMatrices>();
		for (int i=0; i<Integer.parseInt(sizes1[0]); i++) {
			for (int j=0; j<Integer.parseInt(sizes2[1]); j++) {
				RunMatrices hilo = new RunMatrices("hilo "+ (contador++), cantidad, (i+1), (j+1));
				hilo.start();
				hilos.add(hilo);
			}
		}

		// Guardar resultados en nueva matriz y detener el hilo
		int[][] M_resultante = new int[Integer.parseInt(sizes1[0])][Integer.parseInt(sizes2[1])];
		for (RunMatrices hilo_aux:hilos) {
			int fila = hilo_aux.fila;
			int col = hilo_aux.columna;
			int valor = hilo_aux.resultado;
			M_resultante[fila-1][col-1] = valor;
		}
		hilos.clear();

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

// Clase para correr threads y realizar operaciones
class RunMatrices implements Runnable {
	private Thread t;
   	private String threadName;
   	private volatile boolean exit = false;
   	private int cantidad;
   	public int fila;
   	public int columna;
   	public int resultado = 0;
   	private boolean calculado = false;

   	// Constructor básico con nombre de prueba y información necesario para multiplicar
   	//actual = hilo.multiplicar(matriz1, matriz2, cantidad, (i+1), (j+1));****************
   	RunMatrices(String name, int cant, int i, int j) {
      	threadName = name;
      	cantidad = cant;
      	fila = i;
      	columna = j;
   	}

   	// Metodo que se corre mientras el thread este funcionando
	public void run(){
		// Verificar si corresponde salir
		while (!exit) {
			if (!calculado) {
				// Realizar operacion
				resultado = this.multiplicar(Matrices.matriz1, Matrices.matriz2, cantidad, fila, columna);
				calculado = true;
				this.stop();
			}
			else {
				// Dormir si no hay acción
				try {
					Thread.sleep(50);
				}
				catch (Exception e) {
					t.interrupt();
				}
			}
		}
	}

	// Crear y correr thread
	public void start() {
      if (t == null) {
         t = new Thread (this, threadName);
         t.start ();
      }
   	}

   	// Actualizar variable para detener thread
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