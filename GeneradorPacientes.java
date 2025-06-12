import java.io.*;
import java.util.*;

public class GeneradorPacientes {
    public static List<Paciente> generarPacientes(int cantidad, long timestampInicio) {
        List<Paciente> pacientes = new ArrayList<>();
        Random random = new Random();
        String[] areas = {"SAPU", "urgencia_adulto", "infantil"};
        double[] probabilidades = {0.10, 0.15, 0.18, 0.27, 0.30};

        for (int i = 0; i < cantidad; i++) {
            String nombre = "Paciente" + i;
            String apellido = "Apellido" + i;
            String id = "ID" + i;

            double p = random.nextDouble();
            double acumulado = 0;
            int categoria = 5;
            for (int j = 0; j < probabilidades.length; j++) {
                acumulado += probabilidades[j];
                if (p <= acumulado) {
                    categoria = j + 1;
                    break;
                }
            }

            long tiempoLlegada = timestampInicio + (i * 600);
            String area = areas[random.nextInt(areas.length)];
            pacientes.add(new Paciente(nombre, apellido, id, categoria, tiempoLlegada, area));
        }
        return pacientes;
    }

    public static void guardarPacientesEnArchivo(List<Paciente> pacientes, String nombreArchivo) throws IOException {
        PrintWriter writer = new PrintWriter(new FileWriter(nombreArchivo));
        for (Paciente p : pacientes) {
            writer.println(p.getId() + "," +
                           p.getNombreCompleto() + "," +
                           p.getCategoria() + "," +
                           p.getArea() + "," +
                           p.getEstado());
        }
        writer.close();
    }
}
