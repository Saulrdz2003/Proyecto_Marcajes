
package control;

/**
     * Crea una carta en PDF con nombre, fecha y motivo.
     * @param rutaSalida Ruta del archivo PDF (por ejemplo "carta.pdf")
     * @param nombre Persona a quien va dirigida la carta
     * @param dia Fecha o día que quieres poner en la carta
     * @param motivo Motivo o contenido de la carta
     * @throws FileNotFoundException
     * @throws DocumentException
     */

import com.itextpdf.text.DocumentException;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.Timer;
import modelo.*;
import vista.*;

public class Control {
    private Vista vista;
    private Modelo modelo;
    private ControlArchivos controlArchivos;
    private ControlPDF controlPdf;
    private ControlSerializacion controlSerializacion;
    private ControlTiempos controlTiempos;
    private String fechaReporte;   
    private Timer temporizadorInactividad;
    private int segundosRestantes = 90;
    
    public Control(Vista vista, Modelo modelo) {
        this.vista = vista;
        this.modelo = modelo;
        this.controlArchivos = new ControlArchivos();
        this.controlPdf = new ControlPDF();
        this.controlTiempos = new ControlTiempos();
    }
    
    public Vista getVista() {
        return vista;
    }

    public Modelo getModelo() {
        return modelo;
    }

    public ControlArchivos getControlArchivos() {
        return controlArchivos;
    }
    
   public ControlPDF getControlPDF() {
       return controlPdf;
   }

    public ControlSerializacion getControlSerializacion() {
        return controlSerializacion;
    }

    public void setControlPdf(ControlPDF controlPdf) {
        this.controlPdf = controlPdf;
    }

    public void setControlTiempos(ControlTiempos controlTiempos) {
        this.controlTiempos = controlTiempos;
    }

    public ControlPDF getControlPdf() {
        return controlPdf;
    }

    public ControlTiempos getControlTiempos() {
        return controlTiempos;
    }
    
    public void setVista(Vista vista) {
        this.vista = vista;
    }
    
    public void setControlPDF(ControlPDF controlPDF){
        this.controlPdf = controlPDF;
    }

    public void setControlArchivos(ControlArchivos controlArchivos) {
        this.controlArchivos = controlArchivos;
    }

    public void setControlSerializacion(ControlSerializacion controlSerializacion) {
        this.controlSerializacion = controlSerializacion;
    }

    
    public void setModelo(Modelo modelo) {
        this.modelo = modelo;
    }
    //Seteo de ventanas, controlador y adquisisión de admin
    public void crearVentanas() {
        this.vista.setInforme(new Informe());
        this.vista.setInicio(new Inicio());
        this.vista.setReportes(new Reportes());
        this.vista.getInforme().setControlador(this);
        this.vista.getInicio().setControlador(this);
        this.vista.getReportes().setControlador(this);
    }
    
    public void mostrarVentana(TipoVentana.Ventana tipoVentana) {
        javax.swing.JFrame[] ventanas = {
            this.vista.getInforme(),
            this.vista.getInicio(),
            this.vista.getReportes()
        };

        for (javax.swing.JFrame ventana : ventanas) {
            ventana.setVisible(false);
        }
        
        switch (tipoVentana.getIndex()) {
                case 1 -> this.vista.getInicio().setVisible(true);
                case 2 -> this.vista.getInforme().setVisible(true);
                case 3 -> this.vista.getReportes().setVisible(true);
                    } 
        }
     

    //crea ventanas y setea los controles 
    public void crearCondicionesIniciales() {
        this.crearVentanas();
        this.controlArchivos.setControlPrincipal(this);
        this.controlPdf.setControlPrincipal(this);
        this.controlTiempos.setControlPrincipal(this);
    }
    
    public String[] traerDatos(long codigo){
       String[] respuesta = this.modelo.cualidades(codigo);
       
           return new String[]{
           respuesta[0],
           respuesta[1],
           respuesta[2],
           respuesta[3],
           respuesta[4],
           };
    }

    public void llenarLista(JComboBox<String> combo) {
        DefaultComboBoxModel<String> modeloCombo = (DefaultComboBoxModel<String>) combo.getModel();
        modeloCombo.removeAllElements();

            List<Horario> horario = this.getModelo().getHorarios(); 

            for (Horario horarios : horario) {
                modeloCombo.addElement(String.valueOf(horarios.getCodigo()));
            }
         
    }    
    
    public void registrarHorarios(long code, String nombre, LocalTime horaEntrada, LocalTime horaSalida, LocalTime horaAlm, LocalTime horaSal){
        
        Horario horario = new Horario(code, nombre, horaEntrada, horaSalida, horaAlm, horaSal); 
        this.getModelo().getHorarios().add(horario);
    }
    
    public void imprimirSerializados() {
            System.out.println("Objetos Serializados");
            this.modelo.imprimirHorarios();
            this.modelo.imprimirMarcajes();
            this.modelo.imprimirFaltas();
            this.modelo.imprimirUbicacion();
    }
    
    public static String FechaCarta(){
        LocalDate hoy = LocalDate.now();
        LocalDate fecha;

        if (hoy.getDayOfWeek() == DayOfWeek.MONDAY) {
            // Si hoy es lunes, retroceder 3 días (viernes anterior)
            fecha = hoy.minusDays(3);
        } else {
            // En cualquier otro día, retroceder 1 día
            fecha = hoy.minusDays(1);
        }

        DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        
        return fecha.format(formato);
    }

    public void Archivador(long code, String nombre, LocalTime entrada, LocalTime horaAlm, LocalTime regreso, LocalTime salida, LocalDate dia){

        String fecha = dia.toString();
        fechaReporte = dia.toString();
        
        Trabajador trabajador = new Trabajador(code, nombre, entrada, horaAlm, regreso, salida, fecha);
        this.modelo.agregarTrabajador(trabajador);
        

    }
    
    
   public void comparar() {
        for (Horario horario : this.modelo.getHorarios()) {
            Long codigoHorario = horario.getCodigo();
            String nombreEmpleado = horario.getNombre();

            // Buscar todos los registros de trabajadores con este código
            List<Trabajador> registrosEmpleado = this.modelo.getTrabajador().stream()
                .filter(t -> t.getCUI() != null && t.getCUI().equals(codigoHorario))
                .filter(t -> t.getDia() != null && t.getDia().equals(fechaReporte))
                .toList();

            if (!registrosEmpleado.isEmpty()) {
                System.out.println("\n=== PROCESANDO: " + nombreEmpleado + " (Código: " + codigoHorario + ") ===");

                // 1. Recolectar TODAS las marcas del empleado
                List<LocalTime> todasLasMarcas = new ArrayList<>();
                for (Trabajador registro : registrosEmpleado) {
                    if (registro.getEntrada() != null) todasLasMarcas.add(registro.getEntrada());
                    if (registro.getAlmIni() != null) todasLasMarcas.add(registro.getAlmIni());
                    if (registro.getAlmFin() != null) todasLasMarcas.add(registro.getAlmFin());
                    if (registro.getSalida() != null) todasLasMarcas.add(registro.getSalida());
                }

                // 2. Ordenar marcas cronológicamente
                todasLasMarcas.sort(Comparator.naturalOrder());
                System.out.println("📋 Marcas encontradas: " + todasLasMarcas);

                // 3. Configurar tolerancias
                int toleranciaEntradaSalida = 120; // ±2 horas
                int toleranciaAlmuerzo = 60;       // ±1 hora

                // 4. Usar un array como pivote en lugar de crear nuevo Trabajador
                LocalTime[] asignacionesOptimas = new LocalTime[4]; // [entrada, almIni, almFin, salida]

                // 5. Asignar inteligentemente cada marca al tipo más cercano
                List<LocalTime> marcasSinAsignar = new ArrayList<>(todasLasMarcas);

                // Para cada tipo de marca, encontrar la mejor opción disponible
                asignarMarcaPorTipo(asignacionesOptimas, horario.getHoraEntrada(), 
                                  marcasSinAsignar, toleranciaEntradaSalida, 0, "ENTRADA", nombreEmpleado);
                asignarMarcaPorTipo(asignacionesOptimas, horario.getHoraAlmuerzo(), 
                                  marcasSinAsignar, toleranciaAlmuerzo, 1, "INICIO ALMUERZO", nombreEmpleado);
                asignarMarcaPorTipo(asignacionesOptimas, horario.getFinAlmuerzo(), 
                                  marcasSinAsignar, toleranciaAlmuerzo, 2, "FIN ALMUERZO", nombreEmpleado);
                asignarMarcaPorTipo(asignacionesOptimas, horario.getHoraSalida(), 
                                  marcasSinAsignar, toleranciaEntradaSalida, 3, "SALIDA", nombreEmpleado);

                // 6. Actualizar registros originales con las asignaciones óptimas
                actualizarRegistrosConAsignaciones(registrosEmpleado, asignacionesOptimas);

                // 7. Mostrar resultados
                mostrarResumenProcesamiento(registrosEmpleado, horario, nombreEmpleado);
            }
        }
        
        reportar();
        
        escritura();
    }

    private void asignarMarcaPorTipo(LocalTime[] asignaciones, LocalTime horaEsperada, 
                                   List<LocalTime> marcasDisponibles, int tolerancia,
                                   int tipo, String nombreTipo, String nombreEmpleado) {
        if (horaEsperada == null) {
            System.out.println("⏭️  " + nombreTipo + " omitido - sin hora esperada");
            return;
        }

        LocalTime mejorMarca = null;
        long menorDiferencia = Long.MAX_VALUE;

        // Buscar la marca más cercana dentro de la tolerancia
        for (LocalTime marca : marcasDisponibles) {
            long diferencia = Math.abs(Duration.between(horaEsperada, marca).toMinutes());

            if (diferencia <= tolerancia && diferencia < menorDiferencia) {
                mejorMarca = marca;
                menorDiferencia = diferencia;
            }
        }

        // Asignar la mejor marca encontrada
        if (mejorMarca != null) {
            asignaciones[tipo] = mejorMarca;
            marcasDisponibles.remove(mejorMarca);
            System.out.println("✅ " + nombreTipo + " asignado a " + nombreEmpleado + 
                             ": " + mejorMarca + " (Esperada: " + horaEsperada + 
                             ", Diferencia: " + menorDiferencia + " min)");
        } else {
            System.out.println("❌ " + nombreTipo + " no asignado para " + nombreEmpleado + 
                             " - ninguna marca dentro de tolerancia");
        }
    }
    
    private void escritura() {
        String diaActual = FechaCarta(); // fecha actual o del reporte

        for (Trabajador t : this.modelo.getTrabajador()) {
            String ruta = this.modelo.getHistorial().getUbicacionCartas();
            String nombre = t.getNombre();
            Long cui = t.getCUI();

            // Buscar si el trabajador tiene una falta para la fecha actual
            boolean tieneFaltaHoy = this.modelo.getFaltas().stream()
                .anyMatch(f -> Objects.equals(f.getCodigo(), cui) && Objects.equals(f.getDiaFalta(), fechaReporte));

            if (tieneFaltaHoy) {
                try {
                    this.controlPdf.crearCartaPDF(ruta, nombre, diaActual, cui, fechaReporte);
                    System.out.println("📄 Carta generada por falta: " + nombre + " (" + cui + ")");
                } catch (FileNotFoundException | DocumentException ex) {
                    Logger.getLogger(Control.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        
        JOptionPane.showMessageDialog(
             null,  // padre null hace que el diálogo sea centrado en pantalla
             "Cartas creadas. El programa se cerrará.",
             "Ejecución exitosa",
             JOptionPane.WARNING_MESSAGE
         );
         System.exit(0);
        
    }
    
    public List<Object[]> obtenerFaltasPorEmpleado(long codigo) {
        List<Object[]> faltas = new ArrayList<>();

        // Suponiendo que tienes acceso a los datos del modelo
        for (Trabajador t : this.modelo.getTrabajador()) {
            if (t.getCUI() == codigo) {
                String dia = t.getDia();
                String entrada = (t.getEntrada() != null) ? t.getEntrada().toString() : "";
                String entradaAlm = (t.getAlmIni() != null) ? t.getAlmIni().toString() : "";
                String salidaAlm = (t.getAlmFin() != null) ? t.getAlmFin().toString() : "";
                String salida = (t.getSalida() != null) ? t.getSalida().toString() : "";
                faltas.add(new Object[]{dia, entrada, entradaAlm, salidaAlm, salida});
            }
        }

        return faltas;
    }


    private void actualizarRegistrosConAsignaciones(List<Trabajador> registros, LocalTime[] asignacionesOptimas) {
        // Si hay múltiples registros, usar el primero para las asignaciones
        Trabajador registroPrincipal = registros.get(0);

        // Limpiar el registro principal
        registroPrincipal.setEntrada(null);
        registroPrincipal.setAlmIni(null);
        registroPrincipal.setAlmFin(null);
        registroPrincipal.setSalida(null);

        // Aplicar las asignaciones óptimas del array
        registroPrincipal.setEntrada(asignacionesOptimas[0]); // entrada
        registroPrincipal.setAlmIni(asignacionesOptimas[1]);  // almIni
        registroPrincipal.setAlmFin(asignacionesOptimas[2]);  // almFin
        registroPrincipal.setSalida(asignacionesOptimas[3]);  // salida

        // Limpiar los registros adicionales si existen
        for (int i = 1; i < registros.size(); i++) {
            Trabajador registro = registros.get(i);
            registro.setEntrada(null);
            registro.setAlmIni(null);
            registro.setAlmFin(null);
            registro.setSalida(null);
        }
    }

    private void mostrarResumenProcesamiento(List<Trabajador> registros, Horario horario, String nombre) {
        System.out.println("\n📊 RESUMEN FINAL: " + nombre);
        System.out.println("Horario esperado → " + 
            "Entrada: " + (horario.getHoraEntrada() != null ? horario.getHoraEntrada() : "—") +
            " | Alm. Inicio: " + (horario.getHoraAlmuerzo() != null ? horario.getHoraAlmuerzo() : "—") +
            " | Alm. Fin: " + (horario.getFinAlmuerzo() != null ? horario.getFinAlmuerzo() : "—") +
            " | Salida: " + (horario.getHoraSalida() != null ? horario.getHoraSalida() : "—"));

        for (Trabajador registro : registros) {
            if (registro.getEntrada() != null || registro.getAlmIni() != null || 
                registro.getAlmFin() != null || registro.getSalida() != null) {

                System.out.println("Marcas asignadas → " + 
                    "Entrada: " + (registro.getEntrada() != null ? registro.getEntrada() : "null") +
                    " | Alm. Inicio: " + (registro.getAlmIni() != null ? registro.getAlmIni() : "null") +
                    " | Alm. Fin: " + (registro.getAlmFin() != null ? registro.getAlmFin() : "null") +
                    " | Salida: " + (registro.getSalida() != null ? registro.getSalida() : "null"));
                break;
            }
        }
        System.out.println("---");
    }
    
    private void reportar() {
        for (Horario h : this.modelo.getHorarios()) {
            LocalTime entradaContr = h.getHoraEntrada();
            LocalTime almIniContr = h.getHoraAlmuerzo();
            LocalTime almFinContr = h.getFinAlmuerzo();
            LocalTime salidaContrato = h.getHoraSalida();
            String dia = fechaReporte;
            Long codigo = h.getCodigo();

            boolean tieneRegistro = this.modelo.getTrabajador().stream()
                    .anyMatch(t -> Objects.equals(t.getCUI(), codigo));

            for (Trabajador t : this.modelo.getTrabajador()) {
                if (Objects.equals(codigo, t.getCUI()) && Objects.equals(dia, t.getDia())) {

                    LocalTime entrada = t.getEntrada();
                    LocalTime almIni = t.getAlmIni();
                    LocalTime almFin = t.getAlmFin();
                    LocalTime salida = t.getSalida();

                    // --- Verificar marcajes faltantes ---
                    if (entrada == null) {
                        Falta f = new Falta(codigo, dia, Falta.MotivoFalta.NO_MARCA_ENTRADA, 0);
                        this.modelo.agregarFalta(f);
                        System.out.println("⚠️ No marcó entrada: " + h.getNombre());
                    }
                    if (almIniContr != null && almIni == null) {
                        Falta f = new Falta(codigo, dia, Falta.MotivoFalta.NO_MARCA_SALIDA_ALMUERZO, 0);
                        this.modelo.agregarFalta(f);
                        System.out.println("⚠️ No marcó inicio de almuerzo: " + h.getNombre());
                    }
                    if (almFinContr != null && almFin == null) {
                        Falta f = new Falta(codigo, dia, Falta.MotivoFalta.NO_MARCA_ENTRADA_ALMUERZO, 0);
                        this.modelo.agregarFalta(f);
                        System.out.println("⚠️ No marcó fin de almuerzo: " + h.getNombre());
                    }
                    if (salida == null) {
                        Falta f = new Falta(codigo, dia, Falta.MotivoFalta.NO_MARCA_SALIDA, 0);
                        this.modelo.agregarFalta(f);
                        System.out.println("⚠️ No marcó salida: " + h.getNombre());
                    }

                    // --- Verificar retrasos o salidas tempranas ---
                    if (entrada != null) {
                        long holguraEntrada = Duration.between(entradaContr, entrada).toMinutes();
                        if (holguraEntrada > 15) {
                            Falta f = new Falta(codigo, dia, Falta.MotivoFalta.ENTRADA_TARDE, (int) holguraEntrada);
                            this.modelo.agregarFalta(f);
                            System.out.println("⏰ Entrada tarde de " + h.getNombre() + " (" + holguraEntrada + " min)");
                        }
                    }

                    if (almIniContr != null && almIni != null) {
                        long holguraAlmIni = Duration.between(almIni, almIniContr).toMinutes();
                        if (holguraAlmIni > 5) {
                            Falta f = new Falta(codigo, dia, Falta.MotivoFalta.SALIDA_TEMPRANA, (int) holguraAlmIni);
                            this.modelo.agregarFalta(f);
                            System.out.println("🍽️ Salió temprano al almuerzo: " + h.getNombre() + " (" + holguraAlmIni + " min)");
                        }
                    }

                    if (almFinContr != null && almFin != null) {
                        long holguraAlmFin = Duration.between(almFinContr, almFin).toMinutes();
                        if (holguraAlmFin > 15) {
                            Falta f = new Falta(codigo, dia, Falta.MotivoFalta.ENTRADA_TARDE, (int) holguraAlmFin);
                            this.modelo.agregarFalta(f);
                            System.out.println("☕ Regresó tarde del almuerzo: " + h.getNombre() + " (" + holguraAlmFin + " min)");
                        }
                    }

                    if (salida != null) {
                        long holguraSalida = Duration.between(salida, salidaContrato).toMinutes();
                        if (holguraSalida > 5) {
                            Falta f = new Falta(codigo, dia, Falta.MotivoFalta.SALIDA_TEMPRANA, (int) holguraSalida);
                            this.modelo.agregarFalta(f);
                            System.out.println("🏃 Salió antes de tiempo: " + h.getNombre() + " (" + holguraSalida + " min)");
                        }
                    }

                    // Si el contrato NO tiene hora de almuerzo
                    if (almIniContr == null && almFinContr == null) {
                        System.out.println("ℹ️ " + h.getNombre() + " no tiene hora de almuerzo registrada.");
                    }
                }
            }

            // --- Si no tiene registros ---
            if (!tieneRegistro) {
                Trabajador t = new Trabajador(codigo, h.getNombre(), null, null, null, null, FechaCarta());
                this.modelo.agregarTrabajador(t);
                Falta f = new Falta(codigo, dia, Falta.MotivoFalta.FALTA_COMPLETA, 0);
                this.modelo.agregarFalta(f);
                System.out.println("🚫 Falta total asignada a " + h.getNombre() + " (" + codigo + ")");
            }
        }
    }
    
    public void guardarHistorial(Historial historial) {
        if (this.modelo != null) {
            this.modelo.setHistorial(historial);
            System.out.println("📘 Historial guardado en modelo:");
            System.out.println(historial);
        } else {
            System.out.println("⚠️ Error: modelo no inicializado en Control.");
        }
    }

    public List<Falta> obtenerFaltasPorCodigo(long codigo) {
        return this.modelo.getFaltas().stream()
                .filter(f -> f.getCodigo() == codigo)
                .collect(Collectors.toList());
    }
    
    public void generarInformesPrimerosDias() {
        LocalDate hoy = LocalDate.now();
        boolean terminado = false;

            List<Horario> horarios = this.getModelo().getHorarios();

            for (Horario h : horarios) {
                long codigo = h.getCodigo();
                String nombre = h.getNombre();
                String diaStr = hoy.toString(); // yyyy-MM-dd
                
                String rutaSalida = this.getModelo().getHistorial().getUbicacionCartas();

                try {
                    // Llamada al método que genera el PDF
                    this.getControlPDF().crearInforme(rutaSalida, nombre, diaStr, codigo);
                    System.out.println("✔ Informe generado para: " + nombre + " | " + diaStr);
                    terminado = true;
                } catch (Exception e) {
                    System.err.println("❌ Error generando informe para " + nombre + ": " + e.getMessage());
                    e.printStackTrace();
                }
            }
        
        if(terminado == true){
            this.getModelo().getTrabajador().clear();
            this.getModelo().getFaltas().clear();
        }
        
    }
    
    public void iniciarTemporizadorInactividad() {
        if (temporizadorInactividad != null) {
            temporizadorInactividad.restart(); // Reinicia si ya existía
        } else {
            // Crear el timer de Swing si no existe
            temporizadorInactividad = new javax.swing.Timer(1000, (java.awt.event.ActionEvent e) -> {
                segundosRestantes--;
                if (segundosRestantes <= 0) {
                    temporizadorInactividad.stop();
                    ejecutarScriptAHK();
                }
            });
        }

        temporizadorInactividad.start();
    }
    
    public void agregarEscuchaDeInteraccion() {
        java.awt.Toolkit.getDefaultToolkit().addAWTEventListener(event -> {
            segundosRestantes = 90; // Reinicia contador
        }, java.awt.AWTEvent.KEY_EVENT_MASK | java.awt.AWTEvent.MOUSE_EVENT_MASK);
    }
    
    private void ejecutarScriptAHK() {
        try {
            // 1. Primero ejecutar el script AHK para renombrar
            String rutaEscritorio = System.getProperty("user.home") + File.separator + "Desktop";
            String rutaScript = rutaEscritorio + File.separator + "Automata" + File.separator + "auto_renombrar.exe";
            Process process = Runtime.getRuntime().exec("cmd /c start \"\" \"" + rutaScript + "\"");
            process.waitFor(); // Esperar a que termine el script AHK

            // 2. Esperar un poco para que el renombrado se complete
            Thread.sleep(2000);

            // 3. Obtener la ruta de destino (debes implementar este método según tu código)
            String rutaDestino = this.modelo.getHistorial().getUbicacionTrabajadores();

            // 4. Buscar el archivo renombrado (con formato fecha-actual.csv)
            String fechaActual = java.time.LocalDate.now().toString(); // yyyy-MM-dd
            String archivoRenombrado = rutaDestino + "\\" + fechaActual + ".csv";
            File archivo = new File(archivoRenombrado);

            // 5. Si existe el archivo renombrado, cargarlo y luego borrarlo
            if (archivo.exists()) {
                cargarMarcajesAutomaticamente( 0); // tipo 0 para trabajadores, ajusta según necesites

                // 6. Borrar el archivo después de cargarlo
                if (archivo.delete()) {
                    System.out.println("🟢 Archivo " + archivo.getName() + " cargado y eliminado correctamente.");
                } else {
                    System.out.println("🟡 Archivo cargado pero no se pudo eliminar: " + archivo.getName());
                }
            } else {
                System.out.println("🔴 No se encontró el archivo renombrado: " + archivoRenombrado);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void cargarMarcajesAutomaticamente(int tipo) {
        try {
            // Obtiene la ruta desde el modelo (carpeta de Marcajes en el escritorio)
            String rutaCarpeta = this.getModelo().getHistorial().getUbicacionTrabajadores();

            File carpeta = new File(rutaCarpeta);
            if (!carpeta.exists() || !carpeta.isDirectory()) {
                System.err.println("❌ La carpeta de marcajes no existe: " + rutaCarpeta);
                return;
            }

            // Busca archivos CSV dentro de la carpeta
            File[] archivos = carpeta.listFiles((dir, name) -> name.toLowerCase().endsWith(".csv"));
            if (archivos == null || archivos.length == 0) {
                System.out.println("⚠ No se encontró ningún archivo CSV en " + rutaCarpeta);
                return;
            }

            // Procesa el más reciente
            File archivo = archivos[0];
            System.out.println("📂 Procesando archivo: " + archivo.getName());

            StringBuilder contenido = new StringBuilder();
            try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
                String linea;
                while ((linea = br.readLine()) != null) {
                    contenido.append(linea).append("\n");
                }
            }

            // Dependiendo del tipo, llama el método correspondiente
            if (tipo == 0) {
                this.getControlArchivos().cargarHoras(contenido.toString());
            } else {
                this.getControlArchivos().cargarHoras(contenido.toString());
            }

            // Eliminar el archivo después de procesarlo
            if (archivo.delete()) {
                System.out.println("🗑 Archivo eliminado: " + archivo.getName());
            } else {
                System.err.println("⚠ No se pudo eliminar el archivo: " + archivo.getName());
            }

        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("❌ Error al cargar el archivo automáticamente: " + e.getMessage());
        }
    }

    
    public void enviar() throws IOException, InterruptedException{
          
            // Obtener la ruta local de las cartas
            String carpetaCartas = modelo.getHistorial().getUbicacionCartas();

            // Ruta al script AHK que has modificado
            String rutaScript = System.getProperty("user.home") + "\\Desktop\\Automata\\Compartir.exe";

            // Comando para ejecutar AHK con el parámetro de la carpeta origen
            String comando = "cmd /c start \"\" \"" + rutaScript + "\" \"" + carpetaCartas + "\"";
            Process process = Runtime.getRuntime().exec(comando);
            process.waitFor();  // esperar que termine
        }

    }

    
    
