import java.util.*;

class Granja {
	private static ArrayList<Cultivo> cultivos = new ArrayList<Cultivo>();
	public static int id_actual = 1;

	public static void main(String[] args) {
		System.out.println("\nBienvenido a la Granja Radioactiva!\n");
		Boolean salir = false;
		Scanner reader = null;
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
				Cultivo cultivo = new Cultivo("Thread "+id_actual, name, tiempo, costo, (id_actual++));
				cultivos.add(cultivo);
				cultivo.start();
			}

			// Opción ver cultivos
			else if (opcion == 2) {
				if (id_actual == 1) {
					System.out.println("No hay cultivos!\n");
				}
				else {
					System.out.println("Los cultivos que hay son:");
					Cultivo cultivo = null;
					Iterator<Cultivo> iterador = cultivos.iterator();
					for (int i=1; i<id_actual; i++) {
						cultivo = iterador.next();
						if (cultivo.id == i) {
							System.out.println(i+".- "+cultivo.nombreCultivo+" ("+
								cultivo.peso+" Kg, $"+(cultivo.peso*cultivo.costoKilo)+") que crecerá en "+
								cultivo.t_restante+" Segundo(s).");
						}
					}
					System.out.println();
				}
			}

			// Opcion comprar
			else if (opcion == 3) {

			}

			else {
				salir = true;
				System.out.println("\n¡Gracias por venir a la Granja Radioactiva! esperamos que vuelva pronto.");
			}
		}
		reader.close();
		for(Cultivo cultivo:cultivos) {  
    		cultivo.stop();
 		}  
 		cultivos.clear();
	}
	public static void retirar(int id) {
		cultivos.remove(id-1);
		for(Cultivo cultivo:cultivos) {  
    		if (cultivo.id > id) {
    			cultivo.id--;
    		}
 		}  
		id_actual--;
	}
}

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

   	// Metodos
	public void run(){
		while (!exit) {
			try {
				Thread.sleep(1000);
			}
			catch (Exception e) {};
			if (--t_restante == 0) {
				peso++;
				t_restante = tiempoCrecimiento;
			}
			if (peso == 20) {
				System.out.println("Se descompuso el(la) "+nombreCultivo+", retirando de la granja.");
				Granja.retirar(id);
				this.stop();
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
}