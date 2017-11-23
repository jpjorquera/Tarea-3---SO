import java.util.*;
import java.io.*;
import java.util.concurrent.locks.*;

class Funciones {
	public static Lock lock = new ReentrantLock();

	public static void main(String[] args) {
		int cant_funciones = 0;
		// Leer archivo
		BufferedReader archivo = null;
		try {
			archivo = new BufferedReader(new FileReader("./funciones.txt"));
		}
		catch (Exception e) {
			System.out.println("Error al abrir el archivo");
			System.exit(-1);
		}

		// Leer cantidad de funciones
		try {
			cant_funciones = Integer.parseInt(archivo.readLine());
		}
		catch (IOException e) {
			System.out.println("Error al leer el archivo");
			System.exit(-1);
		}

		// Saltar linea en blanco
		try {
			archivo.readLine();
		}
		catch (IOException e) {
			System.out.println("Error al leer el archivo");
			System.exit(-1);
		}

		HashMap<String, String> funciones = new HashMap<String, String>();
		// Leer funciones
		for (int i=0; i<cant_funciones; i++) {
			String func = "";
			try {
				func = archivo.readLine();
			}
			catch (IOException e) {
				System.out.println("Error al leer el archivo");
				System.exit(-1);
			}
			if (func == null) {
				break;
			}
			funciones.put(func.substring(0,4), func.substring(5).trim());
		}

		////////// Test del Hash //////////////////
		for (Map.Entry<String,String>entrada:funciones.entrySet()) {
			String l = entrada.getKey();
			String f = entrada.getValue();
			System.out.println(l+" = "+f);
		}
		//////////////////////////////////////////*/

		

	}
}

class RunFuncion implements Runnable {

	public void run() {
	}
}