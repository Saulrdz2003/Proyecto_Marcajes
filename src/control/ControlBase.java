
package control;

import javax.swing.JFrame;
import modelo.Modelo;
import vista.Vista;


public abstract class ControlBase extends JFrame{
    private Modelo modelo;
    private Vista vista;
    private Control controlPrincipal;
    
    
    public Modelo getModelo() {
        return modelo;
    }

    public Vista getVista() {
        return vista;
    }

    public Control getControlPrincipal() {
        return controlPrincipal;
    }

    public void setModelo(Modelo modelo) {
        this.modelo = modelo;
    }

    public void setVista(Vista vista) {
        this.vista = vista;
    }

    public void setControlPrincipal(Control controlPrincipal) {
        this.controlPrincipal = controlPrincipal;
    }
    
    public void dormir(int ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
