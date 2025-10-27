
package vista;

public class TipoVentana {
 public enum Ventana{
        INICIO(1),
        INFORME(2),
        REPORTE(3);
        
        private final int index;

        Ventana(int index) {
            this.index = index;
        }

        public int getIndex() {
            return index;
        }
    }
}
