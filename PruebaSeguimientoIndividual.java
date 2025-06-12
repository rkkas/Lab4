import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class PruebaSeguimientoIndividual {
    public static void main(String[] args) {
        long timestampInicio = System.currentTimeMillis() / 1000L;
        List<Paciente> pacientes = GeneradorPacientes.generarPacientes(50, timestampInicio);

        // Forzamos el primer paciente a categoría C4 para el seguimiento
        Paciente pacienteC4 = pacientes.get(0);
        pacienteC4.setCategoria(4);

        Hospital hospital = new Hospital();
        SimuladorUrgencia simulador = new SimuladorUrgencia(hospital, pacientes, timestampInicio);
        simulador.simular();

        // Calcular tiempo de llegada simulada
        long tiempoLlegadaSegundos = pacienteC4.getTiempoLlegada() - timestampInicio;
        long horaLlegadaSimulada = tiempoLlegadaSegundos / 60;
        int hora = (int) (horaLlegadaSimulada / 60);
        int minuto = (int) (horaLlegadaSimulada % 60);

        // Mostrar detalles del paciente
        System.out.println("=== Seguimiento Paciente C4 ===");
        System.out.println("ID: " + pacienteC4.getId());
        System.out.println("Nombre: " + pacienteC4.getNombreCompleto());
        System.out.printf("Categoría: C%d%n", pacienteC4.getCategoria());
        System.out.printf("Hora simulada de llegada: %02d:%02d%n", hora, minuto);

        // Calcular tiempo de espera
        long tiempoEspera = simulador.getPromedioEsperaPorCategoria().get(4).longValue();
        long tiempoAtencionSimulada = horaLlegadaSimulada + tiempoEspera;
        int horaAtencion = (int) (tiempoAtencionSimulada / 60);
        int minutoAtencion = (int) (tiempoAtencionSimulada % 60);

        System.out.printf("Hora simulada de atención: %02d:%02d%n", horaAtencion, minutoAtencion);
        System.out.println("Tiempo de espera: " + tiempoEspera + " minutos");
    }
}
