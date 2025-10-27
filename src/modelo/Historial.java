
package modelo;

import java.io.Serializable;

public class Historial implements Serializable{
    private static final long serialVersionUID = 1L;
    private String ubicacionTrabajadores;
    private String ubicacionCartas;
    private String fechaReporte;

    public void setFechaReporte(String fechaReporte) {
        this.fechaReporte = fechaReporte;
    }

    public String getFechaReporte() {
        return fechaReporte;
    }

    public Historial(String ubicacionTrabajadores, String ubicacionCartas) {
        this.ubicacionTrabajadores = ubicacionTrabajadores;
        this.ubicacionCartas = ubicacionCartas;
    }

    public String getUbicacionTrabajadores() {
        return ubicacionTrabajadores;
    }

    public String getUbicacionCartas() {
        return ubicacionCartas;
    }

    public void setUbicacionTrabajadores(String ubicacionTrabajadores) {
        this.ubicacionTrabajadores = ubicacionTrabajadores;
    }

    public void setUbicacionCartas(String ubicacionCartas) {
        this.ubicacionCartas = ubicacionCartas;
    }

    @Override
    public String toString() {
        return "Historial{" + "ubicacionTrabajadores=" + ubicacionTrabajadores + ", ubicacionCartas=" + ubicacionCartas + '}';
    }

    
}
