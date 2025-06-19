import java.io.IOException;
import java.util.List;
import java.util.Map;

public class PruebaSaturacion {

    public static void main(String[] args) throws IOException {
        long timestampInicio = System.currentTimeMillis() / 1000L;

        List<Paciente> pacientes = GeneradorPacientes.generarPacientes(200, timestampInicio);
        GeneradorPacientes.guardarPacientesEnArchivo(pacientes, "Pacientes_24h.txt");

        Hospital hospital = new Hospital();
        SimuladorUrgencia simulador = new SimuladorUrgencia(hospital, pacientes, timestampInicio);

        simulador.simular();

        System.out.println("----- PRUEBA DE SATURACIÓN -----");
        System.out.println("Total de pacientes atendidos: " + simulador.getTotalAtendidos());

        System.out.println("\nPromedios de tiempo de espera por categoría:");
        for (Map.Entry<Integer, Double> entry : simulador.getPromedioEsperaPorCategoria().entrySet()) {
            System.out.printf("C%d: %.2f minutos%n", entry.getKey(), entry.getValue());
        }

        System.out.println("\nMáximos tiempos de espera por categoría:");
        for (Map.Entry<Integer, Long> entry : simulador.getMaximoEsperaPorCategoria().entrySet()) {
            System.out.printf("C%d: %d minutos%n", entry.getKey(), entry.getValue());
        }

        System.out.println("\nPacientes que excedieron el tiempo máximo por categoría:");
        for (Map.Entry<Integer, Integer> entry : simulador.getPacientesExcedidos().entrySet()) {
            System.out.printf("C%d: %d%n", entry.getKey(), entry.getValue());
        }
    }
}
