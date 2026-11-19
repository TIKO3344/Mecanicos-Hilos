import java.util.ArrayList;
import java.util.Random;

public class HiloMecanico implements Runnable {
    private Mecanico mecanico;
    private ArrayList<Vehiculo> loteAutos;

    public HiloMecanico(Mecanico mecanico, ArrayList<Vehiculo> loteAutos) {
        this.mecanico = mecanico;
        this.loteAutos = loteAutos;
    }

    @Override
    public void run() {
        try {
            Random rand = new Random();
            int demora = 500 + rand.nextInt(2501);
            Thread.sleep(demora);

            int fallasLambda = 0;
            int fallasO2 = 0;
            int fallasN2O = 0;
            int fallasCO2 = 0;

            for (int i = 0; i < loteAutos.size(); i++) {
                Vehiculo v = loteAutos.get(i);
                if (v.getLambda() > 1.0) {
                    fallasLambda++;
                }
                if (v.getO2() >= 2.0) {
                    fallasO2++;
                }
                if (v.getN2o() >= 40.0) {
                    fallasN2O++;
                }
                if (v.getCo2() >= 15.0) {
                    fallasCO2++;
                }
            }

            double porcLambda = (fallasLambda * 100.0) / loteAutos.size();
            double porcO2 = (fallasO2 * 100.0) / loteAutos.size();
            double porcN2O = (fallasN2O * 100.0) / loteAutos.size();
            double porcCO2 = (fallasCO2 * 100.0) / loteAutos.size();

            String msgLambda = (porcLambda <= 70.0) ? "Pasa en Primera Revision" : "Segunda Revision Asignada";
            String msgO2 = (porcO2 <= 30.0) ? "Pasa en Primera Revision" : "Segunda Revision Asignada";
            String msgN2O = (porcN2O <= 80.0) ? "Pasa en Primera Revision" : "Segunda Revision Asignada";
            String msgCO2 = (porcCO2 <= 50.0) ? "Pasa en Primera Revision" : "Segunda Revision Asignada";

            boolean loteAprobado = (porcLambda <= 70.0) && (porcO2 <= 30.0) && (porcN2O <= 80.0) && (porcCO2 <= 50.0);

            synchronized (System.out) {
                System.out.println("+-----------------------------------------------------------------------+");
                System.out.printf("| MECANICO #%02d: %-25s | Tiempo servidor: %4d ms  |\n",
                        mecanico.getId(), mecanico.getNombre(), demora);
                System.out.println("+--------------+------------+----------+----------+----------+----------+");
                System.out.println("| Marca        | Placa      | Lambda   | O2       | N2O      | CO2      |");
                System.out.println("+--------------+------------+----------+----------+----------+----------+");

                for (int i = 0; i < loteAutos.size(); i++) {
                    Vehiculo v = loteAutos.get(i);
                    System.out.printf("| %-12s | %-10s | %8.2f | %8.2f | %8.2f | %8.2f |\n",
                            v.getMarca(), v.getPlaca(), v.getLambda(), v.getO2(), v.getN2o(), v.getCo2());
                }

                System.out.println("+--------------+------------+----------+----------+----------+----------+");
                System.out.println("| RESUMEN DE ERRORES DEL LOTE (5 VEHICULOS):                            |");
                System.out.printf("| - Lambda (> 1, tol <= 70%%) : %5.1f%% -> %-30s |\n", porcLambda, msgLambda);
                System.out.printf("| - O2 (>= 2, tol <= 30%%)    : %5.1f%% -> %-30s |\n", porcO2, msgO2);
                System.out.printf("| - N2O (>= 40, tol <= 80%%)  : %5.1f%% -> %-30s |\n", porcN2O, msgN2O);
                System.out.printf("| - CO2 (>= 15, tol <= 50%%)  : %5.1f%% -> %-30s |\n", porcCO2, msgCO2);
                System.out.println("+-----------------------------------------------------------------------+");
                if (loteAprobado) {
                    System.out.println("| ESTADO DEL LOTE: APROBADO - Pasa en Primera Revision                  |");
                } else {
                    System.out.println("| ESTADO DEL LOTE: CONDICIONADO - Requiere arreglos (Segunda Revision)  |");
                }
                System.out.println("+-----------------------------------------------------------------------+\n");
            }
        } catch (InterruptedException e) {
            System.out.println("Hilo interrumpido para: " + mecanico.getNombre());
        }
    }
}