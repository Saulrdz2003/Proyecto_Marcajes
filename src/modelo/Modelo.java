package modelo;

import control.*; // Dependencia al Control (la dejamos de momento)
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Modelo implements Serializable {
    private static final long serialVersionUID = 1L;

    // Referencia al controlador
    private transient Control controlador;

    // Listas principales del modelo
    private List<Horario> horarios;
    private List<Falta> faltas;
    private List<Trabajador> trabajador;
    private Historial historial;

    public Modelo() {
        this.horarios = new ArrayList<>();
        this.faltas = new ArrayList<>();
        this.trabajador = new ArrayList<>();
    }

    // --- Métodos para el controlador ---
    public Control getControlador() {
        return controlador;
    }

    public void setControlador(Control controlador) {
        this.controlador = controlador;
    }

    public List<Horario> getHorarios() {
        return horarios;
    }

    public void agregarHorarios(Horario t) {
        horarios.add(t);
    }

    public List<Falta> getFaltas() {
        return faltas;
    }

    public void agregarFalta(Falta f) {
        faltas.add(f);
    }

    public List<Trabajador> getTrabajador(){
     return trabajador;   
    }
    
    public Historial getHistorial() {
    return historial;
}

    public void setHistorial(Historial historial) {
        this.historial = historial;
    }
    
    public void agregarTrabajador(Trabajador h){
        trabajador.add(h);
    }
    
    public void limpiarFaltas() {
        faltas.clear();
    }
    
    public void limpiarLLegadas(){
        trabajador.clear();
    }
    
    public String[] cualidades(long codigo) {
            for (Horario horario : this.getHorarios()) {
                if (horario.getCodigo() == codigo) {
                    return new String[]{
                        horario.getNombre(),
                        String.valueOf(horario.getHoraEntrada()),
                        String.valueOf(horario.getHoraSalida()),
                        String.valueOf(horario.getHoraAlmuerzo()),
                        String.valueOf(horario.getFinAlmuerzo())
                    };
                }
            }
        return null;
    }
    
    public void imprimirHorarios() {
        for(Horario i : this.horarios) {
                System.out.println(i);
        }
    }
    
    public void imprimirMarcajes() {
        for(Trabajador i : this.trabajador) {
                System.out.println(i);
        }
    }
    
    public void imprimirFaltas(){
        for(Falta f : this.faltas){
            System.out.println(f);
        }
    }
    
    public void imprimirUbicacion(){
        System.out.println(historial);
    }
}
