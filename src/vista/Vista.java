
package vista;

import control.Control;

public class Vista extends VistaBase{
    private Informe informe;
    private Inicio inicio;
    private Reportes reportes;
    private Control control;
    
    public Control getControl() {
        return control;
    }
     
    public Informe getInforme(){
         return informe;
     }

    public Inicio getInicio(){
         return inicio;
    }
    
    public Reportes getReportes(){
        return reportes;
    }
          
    public void setInforme(Informe informe) {
        this.informe = informe;
    }
    
    public void setReportes(Reportes reportes){
        this.reportes = reportes;
    }

    public void setInicio (Inicio inicio) {
        this.inicio = inicio;
    }
    
     public void setControl(Control control) {
        this.control = control;
    }
}
