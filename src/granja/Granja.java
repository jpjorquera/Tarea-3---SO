import java.util.*;

class Granja {
	private static ArrayList<Cultivo> cultivos = new ArrayList<Cultivo>();
	public static int id_actual = 1;

	public static void main(String[] args) {
		System.out.println("\nBienvenido a la Granja Radioactiva!\n");
		Boolean salir = false;
		Scanner reader = null;
		// Iterar según acción elegida
		while (!salir) {
			// Pedir acción
			System.out.println("Ingrese alguna de las siguientes opciones:");
			System.out.println("1.- Cultivar");
			System.out.println("2.- Ver Cultivos");
			System.out.println("3.- Comprar Cultivos");
			System.out.println("4.- Salir");
			reader = new Scanner(System.in);
			int opcion;
			// Validar opción
			try {
				opcion = reader.nextInt();
			}
			catch (Exception e) {
				System.out.println("Opción inválida, por favor inténtelo nuevamente. \n");
				continue;
			}
			if (opcion>4 || opcion<1) {
				System.out.println("Opción inválida, por favor inténtelo nuevamente. \n");
				continue;
			}

			// Opción cultivar
			if (opcion == 1) {
				// Validar input a cultivar
				System.out.println("Ingresar Nombre del Cultivo:");
				String name = reader.next();
				Boolean valido = false;
				Scanner intento = null;
				int tiempo = 0;
				// Validar tiempo
				while (!valido) {
					System.out.println("Ingresar Tiempo de Crecimiento (Segundos):");
					try {
						intento = new Scanner(System.in);
						tiempo = intento.nextInt();
					}
					catch (Exception e) {
						System.out.println("Tiempo inválido, por favor inténtelo nuevamente. \n");
						continue;
					}
					if (tiempo < 10) {
						System.out.println("Por favor, ingrese un tiempo mayor o igual a 10 [s]. \n");
						continue;
					}
					valido = true;
				}
				valido = false;
				int costo = 0;
				// Validar costo por kilo
				while (!valido) {
					System.out.println("Ingresar Costo por Kilo:");
					try {
						intento = new Scanner(System.in);
						costo = reader.nextInt();
					}
					catch (Exception e) {
						System.out.println("Costo por Kilo inválido, por favor inténtelo nuevamente. \n");
						continue;
					}
					if (costo <= 0) {
						System.out.println("Por favor, ingrese un costo positivo. \n");
						continue;
					}
					valido = true;
				}
				System.out.println(name+" ingresado(a) con éxito! \n");
				// Crear thread
				Cultivo cultivo = new Cultivo("Thread "+id_actual, name, tiempo, costo, (id_actual++));
				cultivos.add(cultivo);
				cultivo.start();
			}

			// Opción ver cultivos
			else if (opcion == 2) {
				// Verificar cantidad de cultivos
				if (id_actual == 1) {
					System.out.println("No hay cultivos!\n");
				}
				else {
					System.out.println("Los cultivos que hay son:");
					Cultivo cultivo = null;
					Iterator<Cultivo> iterador = cultivos.iterator();
					// Iterar cultivos actuales
					for (int i=1; i<id_actual; i++) {
						cultivo = iterador.next();
						if (cultivo.id == i) {
							System.out.println(i+".- "+cultivo.nombreCultivo+" ("+
								cultivo.peso+" Kg, $"+(cultivo.peso*cultivo.costoKilo)+") que crecerá en "+
								cultivo.tiempoCrecimiento+" Segundos.");
						}
					}
					System.out.println();
				}
			}

			// Opcion comprar
			else if (opcion == 3) {
				Boolean valido = false;
				int eleccion = 0;
				int a_pagar = 0;
				while (!valido) {
					// Obtener ID
					System.out.println("Ingrese ID del cultivo a comprar:");
					// Validar opción
					try {
						reader = new Scanner(System.in);
						eleccion = reader.nextInt();
					}
					catch (Exception e) {
						System.out.println("ID inválido, por favor inténtelo nuevamente. \n");
						continue;
					}
					if (eleccion>=id_actual || eleccion<1) {
						System.out.println("ID inválido, por favor inténtelo nuevamente. \n");
						continue;
					}
					valido = true;
				}
				valido = false;
				while (!valido) {
					// Obtener dinero
					System.out.println("Ingrese el dinero a pagar:");
					// Validar opción
					try {
						reader = new Scanner(System.in);
						a_pagar = reader.nextInt();
					}
					catch (Exception e) {
						System.out.println("Monto inválido, por favor inténtelo nuevamente. \n");
						continue;
					}
					if (a_pagar <= 0) {
						continue;
					}
					valido = true;
				}
				Boolean estadoCompra = false;
				Iterator<Cultivo> iterador_actual = cultivos.iterator();
				int i = 0;
				// Iterar hasta encontrar el elegido
				while (iterador_actual.hasNext()) {
					Cultivo cultivo = iterador_actual.next(); 
		    		if (cultivo.id == eleccion) {
		    			int precio = cultivo.costoKilo * cultivo.peso;
		    			if (a_pagar >= precio) {
		    				// Dar vuelto
		    				if (a_pagar != precio) {
		    					System.out.println("Gracias por comprar en la Granja Radioactiva, su vuelto es de $"
		    						+(a_pagar-precio)+".");
		    				}
		    				// Dinero exacto
		    				else {
		    					System.out.println("Gracias por comprar en la Granja Radioactiva.");
		    				}
		    				estadoCompra = true;
		    			}
		    			else {
		    				System.out.println("Lo siento, el precio actual es de $"+precio+
		    					" y su dinero no es suficiente.");
		    			}
		    			System.out.println();
		    			break;
		    		}
		    		i++;
 				}
 				// Detener thread de cultivo comprado
 				if (estadoCompra) {
 					Iterator<Cultivo> iter = cultivos.iterator();
					while (iter.hasNext()) {
					    Cultivo cult_actual = iter.next();
					    if (cult_actual.id == eleccion) {
					        cult_actual.stop();
					        retirar(eleccion);
					        break;
					    }
					}
			 	}



			}
			// Salir
			else {
				salir = true;
				System.out.println("\n¡Gracias por venir a la Granja Radioactiva! esperamos que vuelva pronto.");
			}
		}
		reader.close();
		// Detener threads que sigan corriendo para salir
		Iterator<Cultivo> iter_final = cultivos.iterator();
		while (iter_final.hasNext()) {
		    Cultivo cult_actual = iter_final.next();
		    cult_actual.stop();
		} 
 		cultivos.clear();
	}

	// Método para retirar de los cultivos el indicado, no detiene el thread
	public static void retirar(int id) {
		Iterator<Cultivo> iter = cultivos.iterator();
		for (int i=0; i<id; i++) {
			Cultivo actual = iter.next();
			if (i==(id-1)) {
				iter.remove();
			}
		}

		//cultivos.remove(id-1);
		// Actualizar IDs en array
		for(Cultivo cultivo:cultivos) {  
    		if (cultivo.id > id) {
    			cultivo.id--;
    		}
 		}  
		id_actual--;
	}
}

// Clase que lleva el trabajo de threads, almacena información de cada cultivo
// y sobreescribe funciones para el manejo de threads, principalmente Run()
class Cultivo implements Runnable {
	// Constructor
	private Thread t;
   	private String threadName;
   	private volatile boolean exit = false;
   	public String nombreCultivo;
   	public int tiempoCrecimiento;
   	public int peso;
   	public int costoKilo;
   	public int id;
   	public int t_restante;

   	Cultivo(String name, String nombre, int tiempo, int costo, int codigo) {
      	threadName = name;
      	nombreCultivo = nombre;
      	tiempoCrecimiento = tiempo;
      	costoKilo = costo;
      	id = codigo;
      	t_restante = tiempo;
      	peso = 1;
   	}

   	// Acciones mientras corre el thread
	public void run(){
		// Salir cuando se indique
		while (!exit) {
			// Revisar cada segundo
			try {
				Thread.sleep(1000);
			}
			catch (Exception e) {};
			// Actualizar tiempo restante para descomponerse
			if (--t_restante == 0) {
				peso++;
				t_restante = tiempoCrecimiento;
			}
			// Si se descompuso
			if (peso == 20) {
				System.out.println("\nSe descompuso el(la) "+nombreCultivo+", retirando de la granja.\n");
				Granja.retirar(id);
				this.stop();
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