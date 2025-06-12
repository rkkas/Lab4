import java.util.*;

public class Hospital {
    private Map<String, Paciente> pacientesTotales;
    private PriorityQueue<Paciente> colaAtencion;
    private Map<String, AreaAtencion> areasAtencion;
    private List<Paciente> pacientesAtendidos;

    public Hospital() {
        this.pacientesTotales = new HashMap<>();
        this.colaAtencion = new PriorityQueue<>(
            Comparator.comparingInt(Paciente::getCategoria)
                      .thenComparingLong(Paciente::tiempoEsperaActual).reversed()
        );
        this.areasAtencion = new HashMap<>();
        this.pacientesAtendidos = new ArrayList<>();
        inicializarAreas();
    }

    private void inicializarAreas() {
        areasAtencion.put("SAPU", new AreaAtencion("SAPU", 100));
        areasAtencion.put("urgencia_adulto", new AreaAtencion("urgencia_adulto", 100));
        areasAtencion.put("infantil", new AreaAtencion("infantil", 100));
    }

    public void registrarPaciente(Paciente p) {
        pacientesTotales.put(p.getId(), p);
        colaAtencion.add(p);
        areasAtencion.get(p.getArea()).ingresarPaciente(p);
    }

    public Paciente atenderSiguiente() {
        Paciente paciente = colaAtencion.poll();
        if (paciente != null) {
            paciente.setEstado("atendido");
            pacientesAtendidos.add(paciente);
        }
        return paciente;
    }

    public void atenderSiguienteConEstadisticas(long minuto, SimuladorUrgencia simulador) {
        Paciente paciente = atenderSiguiente();
        if (paciente != null) {
            simulador.registrarEstadisticas(paciente, minuto);
        }
    }

    public void atenderPacienteCategoriaBaja(long minuto, SimuladorUrgencia simulador) {
        List<Paciente> lista = obtenerPacientesPorCategoria(5);
        if (!lista.isEmpty()) {
            Paciente paciente = lista.get(0);
            colaAtencion.remove(paciente);
            paciente.setEstado("atendido");
            pacientesAtendidos.add(paciente);
            simulador.registrarEstadisticas(paciente, minuto);
        }
    }

    public void reasignarCategoria(String id, int nuevaCategoria) {
        Paciente p = pacientesTotales.get(id);
        if (p != null) {
            p.setCategoria(nuevaCategoria);
            p.registrarCambio("Cambio de categoría a " + nuevaCategoria);
        }
    }

    public List<Paciente> obtenerPacientesPorCategoria(int categoria) {
        List<Paciente> pacientes = new ArrayList<>();
        for (Paciente p : colaAtencion) {
            if (p.getCategoria() == categoria) {
                pacientes.add(p);
            }
        }
        return pacientes;
    }

    public PriorityQueue<Paciente> getColaAtencion() {
        return colaAtencion;
    }
    
    public Map<String, Paciente> getPacientesTotales() {
    return pacientesTotales;
}

    public List<Paciente> getPacientesAtendidos() {
        return pacientesAtendidos;
    }

    public AreaAtencion obtenerArea(String nombre) {
        return areasAtencion.get(nombre);
    }
}
