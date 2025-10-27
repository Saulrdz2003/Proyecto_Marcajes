
package control;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import modelo.Modelo;

public class ControlSerializacion extends ControlBase{
private static final String ARCHIVO_MODELO = "modelo.bin";

    //creacion de archivo .bin con los datos de modelo
    public void serializar(Modelo modelo) {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(ARCHIVO_MODELO))) {
            out.writeObject(modelo);
            System.out.println("Modelo serializado correctamente.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //lectura de archivo .bin para llenar modelos
    public Modelo deserializar() {
        Modelo modelo = null;

        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(ARCHIVO_MODELO))) {
            modelo = (Modelo) in.readObject();
            System.out.println("Modelo deserializado correctamente.");
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return modelo;
    }
}
