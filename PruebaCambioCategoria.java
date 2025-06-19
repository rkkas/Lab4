import java.util.List;

public class PruebaCambioCategoria {
    public static void main(String[] args) {
        long timestampInicio = System.currentTimeMillis() / 1000L;
        List<Paciente> pacientes = GeneradorPacientes.generarPacientes(50, timestampInicio);

        Paciente paciente = pacientes.get(10);
        paciente.setCategoria(3);

        Hospital hospital = new Hospital();
        hospital.registrarPaciente(paciente);

        System.out.println("Antes del cambio de categoría:");
        System.out.printf("ID: %s, Nombre: %s, Categoría: C%d%n",
                paciente.getId(),
                paciente.getNombreCompleto(),
                paciente.getCategoria());

        hospital.reasignarCategoria(paciente.getId(), 1);

        System.out.println("\nDespués del cambio de categoría:");
        System.out.printf("ID: %s, Nombre: %s, Categoría: C%d%n",
                paciente.getId(),
                paciente.getNombreCompleto(),
                paciente.getCategoria());

        System.out.println("\nHistorial de cambios del paciente:");
        while (!paciente.getHistorialCambios().isEmpty()) {
            System.out.println(paciente.obtenerUltimoCambio());
        }
    }
}
