
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
    
    //Esto sirve solo colocarlo en la parte de control
    
    //JOptionPane.showMessageDialog(null, "Iniciando proceso AHK", "Info", JOptionPane.INFORMATION_MESSAGE);
    /*
    try {
        // Usar ProcessBuilder para mejor control
        ProcessBuilder pb = new ProcessBuilder("C:\\Users\\Saúl\\Desktop\\Automata\\Share.exe");
        pb.redirectErrorStream(true);
        
        Process process = pb.start();
        
        // Leer output en hilo separado para evitar bloqueos
        new Thread(() -> {
            try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(process.getInputStream()))) {
                
                String line;
                while ((line = reader.readLine()) != null) {
                    System.out.println("AHK: " + line);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
        
        // Esperar con timeout
        if (process.waitFor(60, TimeUnit.SECONDS)) {
            System.out.println("Proceso completado exitosamente");
        } else {
            process.destroyForcibly();
            System.out.println("Proceso terminado por timeout");
        }
        
    } catch (IOException | InterruptedException e) {
        JOptionPane.showMessageDialog(null, "Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        e.printStackTrace();
    }
}
    */
}
}
