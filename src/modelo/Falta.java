
package modelo;

import java.io.Serializable;

public class Falta  implements Serializable{
    private static final long serialVersionUID = 1L;
    private long codigo;
    private String diaFalta;
    private MotivoFalta motivo;
    private int tiempoHolgura; // en minutos (ejemplo: llegó 15 min tarde)

    public enum MotivoFalta {
        NO_MARCA_ENTRADA,
        NO_MARCA_SALIDA,
        NO_MARCA_SALIDA_ALMUERZO,
        NO_MARCA_ENTRADA_ALMUERZO,
        SALIDA_TEMPRANA,
        ENTRADA_TARDE,
        ENTRADA_TARDE_DEL_ALMUERZO,
        SALIDA_TEMPRANA_AL_ALMUERZO,
        FALTA_COMPLETA;

    }
    
    public Falta(long codigo, String diaFalta, MotivoFalta motivo, int tiempoHolgura) {
        this.diaFalta = diaFalta;
        this.motivo = motivo;
        this.codigo = codigo;
        this.tiempoHolgura = tiempoHolgura;
    }

    public String getDiaFalta() {
        return diaFalta;
    }

    public MotivoFalta getMotivo() {
        return motivo;
    }

    public int getTiempoHolgura() {
        return tiempoHolgura;
    }
    
    public long getCodigo(){
        return codigo;
    }

    public void setDiaFalta(String diaFalta) {
        this.diaFalta = diaFalta;
    }

    public void setMotivo(MotivoFalta motivo) {
        this.motivo = motivo;
    }

    public void setTiempoHolgura(int tiempoHolgura) {
        this.tiempoHolgura = tiempoHolgura;
    }
    
    public void setCodigo(long codigo){
        this.codigo = codigo;
    }

    @Override
    public String toString() {
        return "Falta{"+ "Codigo=" + codigo + ", diaFalta=" + diaFalta + ", motivo=" + motivo + ", tiempoHolgura=" + tiempoHolgura + '}';
    }
    
    
    
    
}
