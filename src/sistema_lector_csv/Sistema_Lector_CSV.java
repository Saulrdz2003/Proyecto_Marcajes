
package sistema_lector_csv;

import control.Control;
import control.ControlSerializacion;
import java.io.File;
import modelo.Modelo;
import vista.TipoVentana;
import vista.Vista;

public class Sistema_Lector_CSV {
    
    public static void main(String[] args) {
//verificar la desincronizción
        Modelo modelo;
        ControlSerializacion serializador = new ControlSerializacion();
        
        //creacion de la vista
        Vista vista = new Vista();

        //busqueda de .bin
        File f = new File("modelo.bin");

        if (f.exists()) {
            modelo = serializador.deserializar();
        } else {
            modelo = new Modelo();
        }
        
        Control controlador = new Control(vista, modelo);
        modelo.setControlador(controlador);
        
        controlador.crearCondicionesIniciales();
        controlador.mostrarVentana(TipoVentana.Ventana.INICIO);
        
        controlador.imprimirSerializados();
        
        //Serealización al cerrar
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            ControlSerializacion serializadorCierre = new ControlSerializacion();
            serializadorCierre.serializar(modelo); 
            System.out.println("Aplicación cerrada. Datos serializados.");
        }));
    
    
}
}
