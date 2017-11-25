import java.util.*;
import java.io.*;
import java.util.concurrent.locks.*;
import javax.script.*;
import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;

class Funciones {
	public static HashMap<String,String> funciones;

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

		funciones = new HashMap<String, String>();
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
		System.out.println("\nFunciones ingresadas!");

		// Iterar peticiones
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
			if (funcion.indexOf("(") == -1 || funcion.indexOf(")") == -1){
				System.out.println("Función inválida, por favor ingrésela nuevamente.");
				continue;
			}

			// Función válida
			String value = funciones.get(letra+"(x)");
			double res = 0;
			RunFuncion calcular = new RunFuncion("Thread principal", letra+"(x)", value, num);
			calcular.start();
			// Buscar resultado
			calcular.lock.lock();
			try {
				// Esperar si no se ha obtenido
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
				ArrayList<RunFuncion> t_funciones = new ArrayList<RunFuncion>();
				// Verificar funciones dentro
				int i = 1;
				for (Map.Entry<String,String>entrada:Funciones.funciones.entrySet()) {
					String key = entrada.getKey();
					String value = entrada.getValue();
					// Si encuentra la función actual
					if (operacion.indexOf(key) != -1) {
						RunFuncion subfuncion = new RunFuncion("Thread de "+key+" "+i, key, value, numero);
						t_funciones.add(subfuncion);
						i++;
						subfuncion.start();

					}

					// Ir a buscar resultados si es que hay subfunciones
					if (t_funciones.size() != 0) {
						double sub_res = 0;
						Iterator<RunFuncion> iterador = t_funciones.iterator();
						// Iterar por subfunciones
						while (iterador.hasNext()) {
							RunFuncion f_actual = iterador.next();
							f_actual.lock.lock();
							try {
								// Esperar si no se ha obtenido
								if (!f_actual.calculado) {
									f_actual.condicion.await();
								}
								sub_res = f_actual.resultado;
							}
							catch (InterruptedException exc) {
								System.out.println("Operación interrumpida");
							}
							f_actual.lock.unlock();
							// Reemplazar resultado en la operación
							operacion = operacion.replaceAll(f_actual.funcion.substring(0,1)+"\\(x\\)", String.valueOf(sub_res));
						}
					}
				}

				// Calcular expresión
				Expression exp = new ExpressionBuilder(operacion.replace("x", numero)).build();
				lock.lock();
				// Guardar resultado
				try {
					resultado = exp.evaluate();
					calculado = true;
					// Avisar obtención
					try {
						condicion.signal();
					}
					catch (Exception e) {
						System.out.println("Error avisando"+e);
					}
				}
				// Error en Expression, probablemente division por 0
				catch (Exception e) {
					System.out.println("Error aritmético.");
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