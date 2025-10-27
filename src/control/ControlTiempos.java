
package control;

import java.time.LocalDate;
import java.util.List;
import modelo.*;


public class ControlTiempos extends ControlBase{
    
    public void generarInformesPrimerosDias() {
        LocalDate hoy = LocalDate.now();
        int diaDelMes = hoy.getDayOfMonth();
        boolean terminado = false;

        // Consideramos los primeros 3 días del mes
        if (diaDelMes >= 1 && diaDelMes <= 3) {
            List<Horario> horarios = this.getModelo().getHorarios();

            for (Horario h : horarios) {
                long codigo = h.getCodigo();
                String nombre = h.getNombre();
                String diaStr = hoy.toString(); // yyyy-MM-dd
                
                String rutaSalida = this.getModelo().getHistorial().getUbicacionCartas();

                try {
                    // Llamada al método que genera el PDF
                    this.getControlPrincipal().getControlPDF().crearInforme(rutaSalida, nombre, diaStr, codigo);
                    System.out.println("✔ Informe generado para: " + nombre + " | " + diaStr);
                    terminado = true;
                } catch (Exception e) {
                    System.err.println("❌ Error generando informe para " + nombre + ": " + e.getMessage());
                    e.printStackTrace();
                }
            }
        } else {
            System.out.println("Hoy no es uno de los primeros días del mes, no se generarán informes automáticamente.");
        }
        
        if(terminado = true){
            this.getModelo().getTrabajador().clear();
            this.getModelo().getFaltas().clear();
        }
        
    }

}
