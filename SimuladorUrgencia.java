import java.util.*;

public class SimuladorUrgencia {
    private Hospital hospital;
    private List<Paciente> pacientes;
    private long tiempoSimulacion = 24 * 60; // minutos
    private long timestampInicio;

    private Map<Integer, List<Long>> tiemposEsperaPorCategoria = new HashMap<>();
    private Map<Integer, Integer> pacientesExcedidos = new HashMap<>();
    private Map<Integer, Long> maximoTiempoEspera = new HashMap<>();
    private int totalAtendidos = 0;

    public SimuladorUrgencia(Hospital hospital, List<Paciente> pacientes, long timestampInicio) {
        this.hospital = hospital;
        this.pacientes = pacientes;
        this.timestampInicio = timestampInicio;
        inicializarEstructuras();
    }

    private void inicializarEstructuras() {
        for (int i = 1; i <= 5; i++) {
            tiemposEsperaPorCategoria.put(i, new ArrayList<>());
            pacientesExcedidos.put(i, 0);
            maximoTiempoEspera.put(i, 0L);
        }
    }

public void simular() {
    int indexPaciente = 0;
    int pacientesNuevos = 0;
    for (long minuto = 0; minuto < tiempoSimulacion; minuto++) {
        if (minuto % 10 == 0 && indexPaciente < pacientes.size()) {
            hospital.registrarPaciente(pacientes.get(indexPaciente));
            pacientesNuevos++;
            indexPaciente++;
        }

        atenderPacientesCategoria1Inmediato(minuto);

        atenderPacientesExcedidos(minuto);

        if (minuto % 15 == 0) {
            hospital.atenderSiguienteConEstadisticas(minuto, this);
        }

        if (pacientesNuevos >= 3) {
            hospital.atenderSiguienteConEstadisticas(minuto, this);
            hospital.atenderSiguienteConEstadisticas(minuto, this);
            pacientesNuevos = 0;
        }

        if (minuto % 60 == 0) {
            hospital.atenderPacienteCategoriaBaja(minuto, this);
        }
    }
}

private void atenderPacientesCategoria1Inmediato(long minutoActual) {
    List<Paciente> categoria1 = new ArrayList<>();
    for (Paciente p : hospital.getColaAtencion()) {
        if (p.getCategoria() == 1) {
            categoria1.add(p);
        }
    }
    for (Paciente p : categoria1) {
        hospital.getColaAtencion().remove(p);
        p.setEstado("atendido");
        hospital.getPacientesAtendidos().add(p);
        registrarEstadisticas(p, minutoActual);
    }
}


    public void registrarEstadisticas(Paciente paciente, long minutoActual) {
        totalAtendidos++;
        long espera = minutoActual - ((paciente.getTiempoLlegada() - timestampInicio) / 60);
        int cat = paciente.getCategoria();
        tiemposEsperaPorCategoria.get(cat).add(espera);
        if (espera > maximoTiempoEspera.get(cat)) {
            maximoTiempoEspera.put(cat, espera);
        }
        if ((cat == 1 && espera > 0) ||
            (cat == 2 && espera > 30) ||
            (cat == 3 && espera > 90) ||
            (cat == 4 && espera > 180)) {
            pacientesExcedidos.put(cat, pacientesExcedidos.get(cat) + 1);
        }
    }

    private void atenderPacientesExcedidos(long minutoActual) {
        List<Paciente> excedidos = new ArrayList<>();
        for (Paciente p : hospital.getColaAtencion()) {
            long espera = minutoActual - ((p.getTiempoLlegada() - timestampInicio) / 60);
            int cat = p.getCategoria();
            if ((cat == 1 && espera > 0) ||
                (cat == 2 && espera > 30) ||
                (cat == 3 && espera > 90) ||
                (cat == 4 && espera > 180)) {
                excedidos.add(p);
            }
        }
        for (Paciente p : excedidos) {
            hospital.getColaAtencion().remove(p);
            p.setEstado("atendido");
            hospital.getPacientesAtendidos().add(p);
            registrarEstadisticas(p, minutoActual);
        }
    }

    public int getTotalAtendidos() {
        return totalAtendidos;
    }

    public Map<Integer, Double> getPromedioEsperaPorCategoria() {
        Map<Integer, Double> promedios = new HashMap<>();
        for (int c : tiemposEsperaPorCategoria.keySet()) {
            List<Long> tiempos = tiemposEsperaPorCategoria.get(c);
            if (!tiempos.isEmpty()) {
                long suma = 0;
                for (long t : tiempos) suma += t;
                promedios.put(c, suma / (double) tiempos.size());
            } else {
                promedios.put(c, 0.0);
            }
        }
        return promedios;
    }

    public Map<Integer, Long> getMaximoEsperaPorCategoria() {
        return maximoTiempoEspera;
    }

    public Map<Integer, Integer> getPacientesExcedidos() {
        return pacientesExcedidos;
    }
}
