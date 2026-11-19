import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Random;

public class HiloArchivos extends Thread {
    private ArrayList<Mecanico> mecanicos = new ArrayList<>();
    private ArrayList<Vehiculo> vehiculos = new ArrayList<>();

    public HiloArchivos() {
        super("Hilo-Archivos");
    }

    @Override
    public void run() {
        generarArchivosSiNoExisten();
        leerMecanicos();
        leerVehiculos();
    }

    public void generarArchivosSiNoExisten() {
        String[] nombres = {
            "Carlos Perez", "Luis Gomez", "Andres Morales", "Jorge Paredes", "Diego Castro",
            "Mateo Salazar", "Fernando Vera", "Ricardo Ortiz", "Pablo Mendoza", "David Ruiz"
        };

        String[] marcas = {
            "Chevrolet", "Kia", "Hyundai", "Toyota", "Nissan",
            "Mazda", "Ford", "Volkswagen", "Suzuki", "Honda"
        };

        Random rand = new Random();

        try {
            File fMec = new File("mecanicos.txt");
            if (!fMec.exists()) {
                FileWriter fwMec = new FileWriter(fMec, false);
                for (int i = 0; i < nombres.length; i++) {
                    fwMec.write((i + 1) + "," + nombres[i] + "\n");
                }
                fwMec.close();
            }

            File fVeh = new File("vehiculos.txt");
            if (!fVeh.exists()) {
                FileWriter fwVeh = new FileWriter(fVeh, false);
                for (int i = 0; i < 50; i++) {
                    String marca = marcas[i % 10];
                    int numPlaca = 1000 + rand.nextInt(9000);
                    char l1 = (char) ('A' + rand.nextInt(26));
                    char l2 = (char) ('A' + rand.nextInt(26));
                    String placa = "P" + l1 + l2 + "-" + numPlaca;
                    fwVeh.write(marca + "," + placa + "\n");
                }
                fwVeh.close();
            }
        } catch (Exception e) {
            System.out.println("Error al generar archivos: " + e.getMessage());
        }
    }

    public void agregarMecanicoArchivo(String nombre) {
        generarArchivosSiNoExisten();
        leerMecanicos();
        int nuevoId = mecanicos.size() + 1;
        try {
            FileWriter fw = new FileWriter("mecanicos.txt", true);
            fw.write(nuevoId + "," + nombre + "\n");
            fw.close();
            System.out.println("Mecanico guardado en mecanicos.txt con ID #" + nuevoId);
        } catch (Exception e) {
            System.out.println("Error al guardar mecanico: " + e.getMessage());
        }
    }

    public void agregarVehiculoArchivo(String marca, String placa) {
        generarArchivosSiNoExisten();
        try {
            FileWriter fw = new FileWriter("vehiculos.txt", true);
            fw.write(marca + "," + placa.toUpperCase() + "\n");
            fw.close();
            System.out.println("Vehiculo guardado en vehiculos.txt correctamente.");
        } catch (Exception e) {
            System.out.println("Error al guardar vehiculo: " + e.getMessage());
        }
    }
    private void leerMecanicos() {
        mecanicos.clear();
        try {
            File archivo = new File("mecanicos.txt");
            if (archivo.exists()) {
                BufferedReader br = new BufferedReader(new FileReader(archivo));
                String linea;
                while ((linea = br.readLine()) != null) {
                    String[] datos = linea.split(",");
                    if (datos.length >= 2) {
                        mecanicos.add(new Mecanico(Integer.parseInt(datos[0]), datos[1]));
                    }
                }
                br.close();
            }
        } catch (Exception e) {
            System.out.println("Error al leer mecanicos.txt: " + e.getMessage());
        }
    }

    private void leerVehiculos() {
        vehiculos.clear();
        Random rand = new Random();
        try {
            File archivo = new File("vehiculos.txt");
            if (archivo.exists()) {
                BufferedReader br = new BufferedReader(new FileReader(archivo));
                String linea;
                while ((linea = br.readLine()) != null) {
                    String[] datos = linea.split(",");
                    if (datos.length >= 2) {
                        double lambda = Math.round((0.5 + (1.5 * rand.nextDouble())) * 100.0) / 100.0;
                        double o2 = Math.round((0.5 + (3.0 * rand.nextDouble())) * 100.0) / 100.0;
                        double n2o = Math.round((20.0 + (40.0 * rand.nextDouble())) * 100.0) / 100.0;
                        double co2 = Math.round((8.0 + (15.0 * rand.nextDouble())) * 100.0) / 100.0;
                        vehiculos.add(new Vehiculo(datos[0], datos[1], lambda, o2, n2o, co2));
                    }
                }
                br.close();
            }
        } catch (Exception e) {
            System.out.println("Error al leer vehiculos.txt: " + e.getMessage());
        }
    }

    public ArrayList<Mecanico> getMecanicos() {
        return mecanicos;
    }

    public ArrayList<Vehiculo> getVehiculos() {
        return vehiculos;
    }
}
   
          