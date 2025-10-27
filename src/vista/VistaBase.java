package vista;

import control.Control;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;
import modelo.Modelo;

public abstract class VistaBase extends JFrame{

    protected Control controlador;
    protected Modelo modelo;
    
    public void setControlador(Control controlador) {
        this.controlador = controlador;
    }

    public Control getControlador() {
        return controlador;
    }

    public Modelo getModelo() {
        return modelo;
    }

    public void setModelo(Modelo modelo) {
        this.modelo = modelo;
    }

    //carga de archivos 
    public void cargarArchivo(int tipo,String extension) { 
        JFileChooser sel = new JFileChooser(); 
        sel.setDialogTitle("Seleccionar archivo " + extension);
        
        sel.setFileFilter(new FileNameExtensionFilter("Archivos " + extension + " (*." + extension +")", extension));

        int resultado = sel.showOpenDialog(this);
        if(tipo == 0){
                if (resultado == JFileChooser.APPROVE_OPTION) {
            File archivo = sel.getSelectedFile();
            if (archivo != null) {
                try {
                    String contenido = "";
                    BufferedReader fa2 = new BufferedReader(new FileReader(archivo));
                    String linea;
                    while ((linea = fa2.readLine()) != null) {
                        contenido += linea + "\n";
                    }
                    System.out.println(contenido);
                    fa2.close();
                    
                    // LÍNEA CORREGIDA - Elige la opción correcta según tu estructura:
                    this.getControlador().getControlArchivos().cargarObjetos(contenido, tipo);
                   
                } catch (IOException e) {
                    JOptionPane.showMessageDialog(this, "Error al leer el archivo: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        } else {
                    JOptionPane.showMessageDialog(this, "No se seleccionó ningún archivo.", "Aviso", JOptionPane.WARNING_MESSAGE);
                }
        }else{
         
            if (resultado == JFileChooser.APPROVE_OPTION) {
            File archivo = sel.getSelectedFile();
            if (archivo != null) {
                try {
                    String contenido = "";
                    BufferedReader fa2 = new BufferedReader(new FileReader(archivo));
                    String linea;
                    while ((linea = fa2.readLine()) != null) {
                        contenido += linea + "\n";
                    }
                    System.out.println(contenido);
                    fa2.close();
                    
                    // LÍNEA CORREGIDA - Elige la opción correcta según tu estructura:
                    this.getControlador().getControlArchivos().cargarHoras(contenido);
                   
                } catch (IOException e) {
                    JOptionPane.showMessageDialog(this, "Error al leer el archivo: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        } else {
                    JOptionPane.showMessageDialog(this, "No se seleccionó ningún archivo.", "Aviso", JOptionPane.WARNING_MESSAGE);
                }
            
        }      
        
    }
    
}