
package control;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import javax.swing.JOptionPane;

public class ControlArchivos extends ControlBase{
    private ArrayList<Long> codigos = new ArrayList<>();
    private ArrayList<String> nombre = new ArrayList<>();
    private ArrayList<LocalTime> horaEntrada = new ArrayList<>();
    private ArrayList<LocalTime> horaSalida = new ArrayList<>();
    private LocalDate date;
    
    boolean terminado = false;
              
    public void cargarObjetos(String contenido, int tipo) {
    try {
        String[] lineas = contenido.split("\n");
        boolean primeraLinea = true;
        
        for (int i = 0; i < lineas.length; i++) {
            String linea = lineas[i].trim();
            
            if (linea.isEmpty()) {
                continue;
            }
            
            // Saltar encabezado si es la primera línea
            if (primeraLinea) {
                primeraLinea = false;
                continue;
            }
            
            String[] partes = linea.split(",");
                  
            try {
                // Extraer datos
                // --- Dentro del try que extrae datos ---
                long code = Long.parseLong(partes[1].trim());
                String name = partes[3].trim();

                // Inicializamos variables de hora
                LocalTime timeEntrance = null;
                LocalTime timeExit = null;

                // Definir formatos posibles
                DateTimeFormatter[] formatters = new DateTimeFormatter[]{
                    DateTimeFormatter.ofPattern("H:mm"),
                    DateTimeFormatter.ofPattern("HH:mm"),
                    DateTimeFormatter.ofPattern("H:mm:ss"),
                    DateTimeFormatter.ofPattern("HH:mm:ss")
                };

                // Función auxiliar para parsear hora con varios formatos
                timeEntrance = parseHoraMultiple(partes[6].trim(), formatters, i, "entrada");
                timeExit = parseHoraMultiple(partes[7].trim(), formatters, i, "salida");

                codigos.add(code);
                nombre.add(name);
                horaEntrada.add(timeEntrance);
                horaSalida.add(timeExit);
                terminado = true;

                // Imprimir para verificar
                System.out.println("Empleado: " + name + " | Codigo: " + code +
                                   " | Entrada: " + timeEntrance + " | Salida: " + timeExit);
   
            } catch (NumberFormatException e) {
                System.err.println("Error en línea " + i + ": " + linea);
            }
        }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Error al procesar archivo: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
    
    //text 8:00 could not be parsed at index 0
        
        if (terminado) {
            for (int i = 0; i < codigos.size(); i++) {
                Long temporal = codigos.get(i);
                String nombreTemp = nombre.get(i);
                LocalTime[] Horas = new LocalTime[4];

                Horas[0] = horaEntrada.get(i);
                Horas[1] = horaSalida.get(i);
                
                boolean eliminado = false;
                // Recorre hacia adelante para no romper el bucle exterior
                for (int o = codigos.size() - 1; o > i; o--) {
                    if (Objects.equals(codigos.get(o), temporal)) {
                        
                        Horas[2] = horaEntrada.get(o);
                        Horas[3] = horaSalida.get(o);
                        
                        nombre.remove(o);
                        codigos.remove(o);
                        horaEntrada.remove(o);
                        horaSalida.remove(o);
                        eliminado = true;
                        break;
                    }
                }
                    
                if (eliminado) {
                    Arrays.sort(Horas);
                    this.getControlPrincipal().registrarHorarios(temporal,nombreTemp, Horas[0], Horas[3], Horas[1], Horas[2]);
                    
                } else {
                    Horas[2] = LocalTime.of(23, 50);
                    Horas[3] = LocalTime.of(23, 51);
                            
                    Arrays.sort(Horas);
                    this.getControlPrincipal().registrarHorarios(temporal,nombreTemp, Horas[0], Horas[1], null, null);

                }
            }
        }

        
    }
    
    public void cargarHoras(String contenido) {
        try {
            String[] lineas = contenido.split("\\r?\\n");
            boolean primeraLinea = true;

            for (int i = 0; i < lineas.length; i++) {
                String linea = lineas[i].trim();
                if (linea.isEmpty()) continue;

                // Saltar encabezado
                if (primeraLinea) {
                    primeraLinea = false;
                    continue;
                }

                // Separación segura de columnas CSV
                String[] partes = linea.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");
                if (partes.length < 4) {
                    System.err.println("⚠️ Línea incompleta en " + i + ": " + linea);
                    continue;
                }

                try {
                    String nombreEmpleado = partes[1].trim().replace("\"", "");
                    long codigo = Long.parseLong(partes[2].trim().replace("\"", ""));
                    String fechaHoraStr = partes[3].trim().replace("\"", "");

                    // Separar fecha y hora si vienen en el mismo campo
                    String[] partesFechaHora = fechaHoraStr.split(" ");
                    String fechaStr = partesFechaHora[0];
                    String horaStr = (partesFechaHora.length > 1) ? partesFechaHora[1] : "00:00";

                    // Parseo flexible de fecha
                    try {
                        date = LocalDate.parse(fechaStr, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                    } catch (Exception e1) {
                        try {
                            date = LocalDate.parse(fechaStr, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
                        } catch (Exception e2) {
                            System.err.println("Formato de fecha desconocido en línea " + i + ": " + fechaStr);
                            continue;
                        }
                    }

                    // Parseo flexible de hora
                    LocalTime hora = parseHoraFlexible(horaStr, i);
                    if (hora == null) continue;

                    codigos.add(codigo);
                    nombre.add(nombreEmpleado);
                    horaEntrada.add(hora);

                    terminado = true;
                    System.out.println("✔ " + nombreEmpleado + " | No: " + codigo + " | " + hora);

                } catch (Exception e) {
                    System.err.println("Error procesando línea " + i + ": " + e.getMessage());
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error al procesar archivo: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }

        // Procesamiento de horas agrupadas
        if (terminado) {
            Map<Long, List<LocalTime>> agrupados = new HashMap<>();
            Map<Long, String> nombres = new HashMap<>();

            for (int i = 0; i < codigos.size(); i++) {
                Long codigo = codigos.get(i);
                String nombreTemp = nombre.get(i);
                LocalTime hora = horaEntrada.get(i);

                nombres.putIfAbsent(codigo, nombreTemp);
                agrupados.computeIfAbsent(codigo, k -> new ArrayList<>()).add(hora);
            }

            for (Map.Entry<Long, List<LocalTime>> entry : agrupados.entrySet()) {
                Long codigo = entry.getKey();
                String nombreTemp = nombres.get(codigo);
                List<LocalTime> horasList = entry.getValue();

                horasList.sort(Comparator.naturalOrder());

                LocalTime[] Horas = new LocalTime[4];
                for (int i = 0; i < Math.min(4, horasList.size()); i++) {
                    Horas[i] = horasList.get(i);
                }

                System.out.println("✔ " + nombreTemp + " | No: " + codigo + " | "
                        + (Horas[0] != null ? Horas[0] : "—") + " / "
                        + (Horas[1] != null ? Horas[1] : "—") + " / "
                        + (Horas[2] != null ? Horas[2] : "—") + " / "
                        + (Horas[3] != null ? Horas[3] : "—"));

                this.getControlPrincipal().Archivador(
                        codigo,
                        nombreTemp,
                        Horas[0],
                        Horas[1],
                        Horas[2],
                        Horas[3],
                        date
                );
            }
        }

        this.getControlPrincipal().comparar();
        date = null;
    }
    
    
    private LocalTime parseHoraMultiple(String horaStr, DateTimeFormatter[] formatters, int linea, String tipo) {
        for (DateTimeFormatter f : formatters) {
            try {
                return LocalTime.parse(horaStr, f);
            } catch (Exception e) {
                // Ignorar y probar siguiente formato
            }
        }
        System.err.println("No se pudo parsear la hora de " + tipo + " en línea " + linea + ": " + horaStr);
        return null; // si no se pudo parsear
    }
    
    private LocalTime parseHoraFlexible(String horaStr, int linea) {
        DateTimeFormatter[] formatters = new DateTimeFormatter[] {
            DateTimeFormatter.ofPattern("H:mm"),
            DateTimeFormatter.ofPattern("HH:mm"),
            DateTimeFormatter.ofPattern("H:mm:ss"),
            DateTimeFormatter.ofPattern("HH:mm:ss")
        };

        for (DateTimeFormatter f : formatters) {
            try {
                return LocalTime.parse(horaStr, f);
            } catch (Exception ignored) {}
        }

        System.err.println("No se pudo parsear la hora en línea " + linea + ": " + horaStr);
        return null; // si no se pudo parsear
    }


    
    }
