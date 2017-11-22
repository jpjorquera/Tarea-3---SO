import java.util.Scanner;

class Granja {
	public static void main(String[] args) {
		System.out.println("Bienvenido a la Granja Radioactiva!");
		Boolean salir = false;
		Scanner reader = null;
		Cultivo cultivo = null;
		int id_actual = 1;
		while (!salir) {
			// Pedir acción
			System.out.println("Ingrese alguna de las siguientes opciones:");
			System.out.println("1.- Cultivar");
			System.out.println("2.- Ver Cultivos");
			System.out.println("3.- Comprar Cultivos");
			reader = new Scanner(System.in);
			int opcion;
			// Validar opción
			try {
				opcion = reader.nextInt();
			}
			catch (Exception e) {
				System.out.println("Opción inválida, por favor inténtelo nuevamente.");
				continue;
			}
			if (opcion>3 || opcion<1) {
				System.out.println("Opción inválida, por favor inténtelo nuevamente.");
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
						System.out.println("Tiempo inválido, por favor inténtelo nuevamente.");
						continue;
					}
					if (tiempo <= 1) {
						System.out.println("Por favor, ingrese un tiempo entero positivo.");
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
						System.out.println("Costo por Kilo inválido, por favor inténtelo nuevamente.");
						continue;
					}
					if (costo <= 0) {
						System.out.println("Por favor, ingrese un costo positivo.");
						continue;
					}
					valido = true;
				}
				System.out.println(name+" ingresado(a) con éxito! \n");
				cultivo = new Cultivo("Thread "+id_actual, name, tiempo, costo, (id_actual++));
				cultivo.start();
			}

			// Opción ver cultivos
			else if (opcion == 2) {
				if (id_actual == 1) {
					System.out.println("No hay cultivos!\n");
				}
				else {
					System.out.println("Los cultivos que hay son:");
					for (int i=1; i<id_actual; i++) {
						if (cultivo.id == i) {
							System.out.println(i+".- "+cultivo.nombreCultivo+" ("+
								cultivo.peso+" Kg, $"+(cultivo.peso*cultivo.costoKilo)+") que crecerá en "+
								cultivo.t_restante+" Segundo(s)");
						}
					}
				}
			}







		}
		reader.close();
	}
	public static void Cultivar() {
		
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