
package modelo;

import java.io.Serializable;
import java.time.LocalTime;


public class Trabajador implements Serializable {
    private static final long serialVersionUID = 1L;    private Long CUI;
    private String Nombre;
    private String dia;
    private LocalTime entrada;
    private LocalTime salida;
    private LocalTime almIni;
    private LocalTime almFin;

    public Trabajador(Long CUI, String Nombre, LocalTime entrada, LocalTime almIni, LocalTime almFin, LocalTime salida, String dia) {
        this.CUI = CUI;
        this.Nombre = Nombre;
        this.dia = dia; 
        this.entrada = entrada;
        this.salida = salida;
        this.almIni = almIni;
        this.almFin = almFin;
    }

    public Long getCUI() {
        return CUI;
    }

    public String getNombre() {
        return Nombre;
    }

    public String getDia() {
        return dia;
    }

    public LocalTime getEntrada() {
        return entrada;
    }

    public LocalTime getSalida() {
        return salida;
    }

    public LocalTime getAlmIni() {
        return almIni;
    }

    public LocalTime getAlmFin() {
        return almFin;
    }

    public void setDia(String dia) {
        this.dia = dia;
    }

    public void setEntrada(LocalTime entrada) {
        this.entrada = entrada;
    }

    public void setSalida(LocalTime salida) {
        this.salida = salida;
    }

    public void setAlmIni(LocalTime almIni) {
        this.almIni = almIni;
    }

    public void setAlmFin(LocalTime almFin) {
        this.almFin = almFin;
    }
    
    

    public void setCUI(Long CUI) {
        this.CUI = CUI;
    }

    public void setNombre(String Nombre) {
        this.Nombre = Nombre;
    }

    @Override
    public String toString() {
        return "Trabajador{" + "CUI=" + CUI + ", Nombre=" + Nombre + ", dia=" + dia + ", entrada=" + entrada + ", salida=" + salida + ", almIni=" + almIni + ", almFin=" + almFin + '}';
    }

}
