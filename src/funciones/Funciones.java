import java.util.*;
import java.io.*;
import java.util.concurrent.locks.*;
import javax.script.*;
import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;

class Funciones {
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

		String evaluar = "5+3*2+1+2*5+1";
		/*ScriptEngineManager manager = new ScriptEngineManager();
		ScriptEngine engine = manager.getEngineByName("js");
		try {
			Object result = engine.eval(evaluar);
			System.out.println("resultado = "+result);
		}
		catch (ScriptException e) {
			System.out.println("Error haciendo la operacion");
			System.exit(-1);
		}*/

		Expression e = new ExpressionBuilder(evaluar).build();
		double result = e.evaluate();
		System.out.println("resultado = "+(int) result);

		//////////////////////////////////////////*/

		System.out.println("\nFunciones ingresadas!");
		while (true) {
			// Recibir input
			System.out.println("\nIngrese operación: (o escriba 'q' para salir)");
			Scanner reader = new Scanner(System.in);
			String funcion = reader.nextLine().trim();

			// Salir
			if (funcion.compareTo("q") == 0) {
				reader.close();
				break;
			}

			// Buscar función recibida
			String letra = funcion.substring(0,1);
			if (!funciones.containsKey(letra+"(x)")) {
				System.out.println("Función inválida, por favor ingrésela nuevamente.");
				continue;
			}
			String num = funcion.substring(2, funcion.length()-1);
			if (!isNumeric(num)) {
				System.out.println("Función inválida, por favor ingrésela nuevamente.");
				continue;
			}

			// Función válida
			String value = funciones.get(letra+"(x)");
			System.out.println("value: "+value);
			System.out.println("num: "+num);
			double res = 0;
			RunFuncion calcular = new RunFuncion("Thread principal", letra+"(x)", value, num);
			calcular.start();
			calcular.lock.lock();
			try {
				if (!calcular.calculado) {
					calcular.condicion.await();
				}
				res = calcular.resultado;
			}
			catch (InterruptedException exc) {
				System.out.println("Operación interrumpida");
			}
			calcular.lock.unlock();

			// Limpiar entero
			if ((res % ((int) res)) == 0 || (((int) res) == 0)) {
				System.out.println("El resultado de "+funcion+" es "+(int) calcular.resultado);
			}
			else {
				System.out.println("El resultado de "+funcion+" es "+calcular.resultado);
			}


		}

		System.out.println("Saliendo.");


		



		

	}

	public static boolean isNumeric(String str) {
  		try  {  
    		double d = Double.parseDouble(str);  
  		}  
  		catch(NumberFormatException nfe)  {  
    		return false;  
  		}  
  		return true;  
	}
}

class RunFuncion implements Runnable {
	private Thread t;
   	private String threadName;
   	private volatile boolean exit;
   	private String funcion;
   	private String operacion;
   	public double resultado;
   	private String numero;
   	public boolean calculado;
   	public Lock lock = new ReentrantLock();
   	public Condition condicion = lock.newCondition();

   	// Constructor con condiciones iniciales
   	RunFuncion(String name, String func, String op, String num) {
   		threadName = name;
   		exit = false;
   		resultado = 0;
   		funcion = func;
   		operacion = op;
   		numero = num;
   		calculado = false;
   	}

   	// Realizar operaciones
	public void run() {
		// Salir cuando se indique
		while (!exit) {
			// Si no ha realizado los cálculos
			if (!calculado) {
				Expression exp = new ExpressionBuilder(operacion.replace("x", numero)).build();
				lock.lock();
				resultado = exp.evaluate();
				calculado = true;
				try {
					condicion.signal();
				}
				catch (Exception e) {
					System.out.println(e);
				}
				lock.unlock();
				this.stop();
			}
			// Si terminó operación y hay que esperar
			else {
				try {
					Thread.sleep(50);
				}
				catch (InterruptedException e) {}
			}
		}
	}

	// Correr el thread
	public void start() {
      if (t == null) {
         t = new Thread (this, threadName);
         t.start ();
      }
   	}

   	// Detener el thread
   	public void stop() {
   		exit = true;
   	}
}