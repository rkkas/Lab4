import java.util.*;

public class AreaAtencion {
    private String nombre;
    private PriorityQueue<Paciente> pacientesHeap;
    private int capacidadMaxima;

    public AreaAtencion(String nombre, int capacidadMaxima) {
        this.nombre = nombre;
        this.capacidadMaxima = capacidadMaxima;
        this.pacientesHeap = new PriorityQueue<>(
            Comparator.comparingInt(Paciente::getCategoria)
                      .thenComparingLong(Paciente::tiempoEsperaActual).reversed()
        );
    }

    public void ingresarPaciente(Paciente p) {
        if (!estaSaturada()) {
            pacientesHeap.add(p);
        } else {
            System.out.println("Área saturada: " + nombre);
        }
    }

    public Paciente atenderPaciente() {
        return pacientesHeap.poll();
    }

    public boolean estaSaturada() {
        return pacientesHeap.size() >= capacidadMaxima;
    }

    public List<Paciente> obtenerPacientesPorHeapSort() {
        List<Paciente> lista = new ArrayList<>(pacientesHeap);
        return HeapSortHospital.heapSort(lista);
    }
}
