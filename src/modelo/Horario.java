
package modelo;

import java.io.Serializable;
import java.time.LocalTime;

public class Horario implements Serializable{
    private static final long serialVersionUID = 1L;
    private long codigo;
    private String nombre; 
    private LocalTime horaEntrada;
    private LocalTime horaSalida;
    private LocalTime horaAlmuerzo;
    private LocalTime finAlmuerzo;

    public Horario(long codigo, String nombre, LocalTime horaEntrada, LocalTime horaSalida, LocalTime horaAlmuerzo, LocalTime finAlmuerzo) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.horaEntrada = horaEntrada;
        this.horaSalida = horaSalida;
        this.horaAlmuerzo = horaAlmuerzo;
        this.finAlmuerzo = finAlmuerzo;
    }

    public LocalTime getHoraEntrada() {
        return horaEntrada;
    }

    public LocalTime getHoraSalida() {
        return horaSalida;
    }

    public LocalTime getHoraAlmuerzo() {
        return horaAlmuerzo;
    }

    public LocalTime getFinAlmuerzo() {
        return finAlmuerzo;
    }

    public long getCodigo() {
        return codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setCodigo(long codigo) {
        this.codigo = codigo;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    

    public void setHoraEntrada(LocalTime horaEntrada) {
        this.horaEntrada = horaEntrada;
    }

    public void setHoraSalida(LocalTime horaSalida) {
        this.horaSalida = horaSalida;
    }

    public void setHoraAlmuerzo(LocalTime horaAlmuerzo) {
        this.horaAlmuerzo = horaAlmuerzo;
    }

    public void setFinAlmuerzo(LocalTime finAlmuerzo) {
        this.finAlmuerzo = finAlmuerzo;
    }

    @Override
    public String toString() {
        return "Horario{" + "codigo=" + codigo + ", Nombre=" + nombre + "horaEntrada=" + horaEntrada + ", horaSalida=" + horaSalida + ", horaAlmuerzo=" + horaAlmuerzo + ", finAlmuerzo=" + finAlmuerzo + '}';
    }
    
}
