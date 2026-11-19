import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int opcion = 0;

        do {
            System.out.println("\n=========================================================================");
            System.out.println("      SISTEMA DE REVISION VEHICULAR CORPAIRE - QUITO (MULTIHILO)");
            System.out.println("=========================================================================");
            System.out.println("1. Ingresar un nuevo mecanico");
            System.out.println("2. Ingresar un nuevo vehiculo (Marca y Placa)");
            System.out.println("3. Ejecutar revision vehicular con hilos");
            System.out.println("4. Salir");
            System.out.print("Seleccione una opcion: ");

            try {
                opcion = Integer.parseInt(sc.nextLine());
            } catch (Exception e) {
                opcion = 0;
            }

            if (opcion == 1) {
                System.out.print("Ingrese el nombre del mecanico: ");
                String nombre = sc.nextLine().trim();
                if (!nombre.isEmpty()) {
                    HiloArchivos gestor = new HiloArchivos();
                    gestor.agregarMecanicoArchivo(nombre);
                } else {
                    System.out.println("El nombre no puede estar vacio.");
                }
            } else if (opcion == 2) {
                System.out.print("Ingrese la marca del vehiculo: ");
                String marca = sc.nextLine().trim();
                System.out.print("Ingrese la placa del vehiculo (Ej: PBA-1234): ");
                String placa = sc.nextLine().trim();
                if (!marca.isEmpty() && !placa.isEmpty()) {
                    HiloArchivos gestor = new HiloArchivos();
                    gestor.agregarVehiculoArchivo(marca, placa);
                } else {
                    System.out.println("La marca y la placa son obligatorias.");
                }
            } else if (opcion == 3) {
                System.out.println("\nCargando archivos mecanicos.txt y vehiculos.txt...\n");
                HiloArchivos hiloArchivos = new HiloArchivos();
                hiloArchivos.start();

                try {
                    hiloArchivos.join();
                } catch (InterruptedException e) {
                    System.out.println("Error esperando la carga de archivos.");
                }

                ArrayList<Mecanico> mecanicos = hiloArchivos.getMecanicos();
                ArrayList<Vehiculo> vehiculos = hiloArchivos.getVehiculos();
                ArrayList<Thread> hilosMecanicos = new ArrayList<>();

                for (int i = 0; i < mecanicos.size(); i++) {
                    ArrayList<Vehiculo> lote = new ArrayList<>();
                    int inicio = i * 5;
                    for (int j = inicio; j < inicio + 5 && j < vehiculos.size(); j++) {
                        lote.add(vehiculos.get(j));
                    }

                    if (!lote.isEmpty()) {
                        HiloMecanico tarea = new HiloMecanico(mecanicos.get(i), lote);
                        Thread hilo = new Thread(tarea, "Hilo-Mecanico-" + (i + 1));
                        hilosMecanicos.add(hilo);
                        hilo.start();
                    }
                }

                for (int i = 0; i < hilosMecanicos.size(); i++) {
                    try {
                        hilosMecanicos.get(i).join();
                    } catch (InterruptedException e) {
                        System.out.println("Error esperando hilos de mecanicos.");
                    }
                }

                System.out.println("=========================================================================");
                System.out.println("      TODOS LOS LOTES DE LOS MECANICOS HAN SIDO PROCESADOS.");
                System.out.println("=========================================================================");
            } else if (opcion == 4) {
                System.out.println("Saliendo del sistema...");
            } else {
                System.out.println("Opcion invalida, intente nuevamente.");
            }
        } while (opcion != 4);

        sc.close();
    }
}