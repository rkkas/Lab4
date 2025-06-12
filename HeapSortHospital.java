import java.util.*;

public class HeapSortHospital {
    public static List<Paciente> heapSort(List<Paciente> lista) {
        PriorityQueue<Paciente> heap = new PriorityQueue<>(
            Comparator.comparingInt(Paciente::getCategoria)
                      .thenComparingLong(Paciente::tiempoEsperaActual).reversed()
        );
        heap.addAll(lista);

        List<Paciente> ordenados = new ArrayList<>();
        while (!heap.isEmpty()) {
            ordenados.add(heap.poll());
        }
        return ordenados;
    }
}
