
package control;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import com.itextpdf.text.pdf.draw.LineSeparator;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import modelo.*;
import modelo.Trabajador;

public class ControlPDF extends ControlBase {
    


    public class HeaderEvent extends PdfPageEventHelper {
        private Image logo;
        private String textoEncabezado;

        public HeaderEvent(String rutaLogo, String textoEncabezado) throws BadElementException, IOException {
            this.logo = Image.getInstance(rutaLogo);
            this.logo.scaleToFit(60, 60); // tamaño del logo
            this.textoEncabezado = textoEncabezado;
        }

        @Override
        public void onEndPage(PdfWriter writer, Document document) {
            PdfContentByte cb = writer.getDirectContent();

            float x = document.left(); // margen izquierdo
            float y = document.getPageSize().getHeight() - document.topMargin() + 10;

            // Dibujar el logo en la esquina superior izquierda
            logo.setAbsolutePosition(x, y - logo.getScaledHeight());
            try {
                cb.addImage(logo);
            } catch (DocumentException e) {
                e.printStackTrace();
            }

            // Dibujar el texto del encabezado a la derecha del logo
            ColumnText.showTextAligned(cb, Element.ALIGN_LEFT,
                    new Phrase(textoEncabezado, FontFactory.getFont(FontFactory.HELVETICA_BOLD, 14)),
                    x + logo.getScaledWidth() + 10, // un poco a la derecha del logo
                    y - logo.getScaledHeight()/2 + 10, // centrar verticalmente respecto al logo
                    0);

            // Dibujar una línea divisoria debajo del encabezado
            LineSeparator sep = new LineSeparator(1f, 100f, BaseColor.BLACK, Element.ALIGN_CENTER, -2f);
            sep.draw(cb,
                     document.left(), 
                     document.right(), 
                     document.getPageSize().getWidth(),
                     y - logo.getScaledHeight() - 5, // posición de la línea
                     0);
        }
    }


    public void crearCartaPDF(String rutaSalida, String nombre, String dia, long codigo, String reportando)
            throws FileNotFoundException, DocumentException {

        // --- Filtrar las faltas del empleado para la fecha específica ---
        List<Falta> faltasEmpleado = getControlPrincipal().getModelo().getFaltas().stream()
                .filter(f -> Objects.equals(f.getCodigo(), codigo)
                        && Objects.equals(f.getDiaFalta(), reportando))
                .toList();

        // --- Crear documento PDF ---
        String nombreArchivo = rutaSalida + "/Carta_" + nombre.replace(" ", "_") + "_" + reportando.replace("/", "-") + ".pdf";
        Document document = new Document();
        PdfWriter.getInstance(document, new FileOutputStream(nombreArchivo));
        document.open();

        // --- Fuentes ---
        Font tituloFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 15);
        Font subTituloFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 13);
        Font textoFont = FontFactory.getFont(FontFactory.HELVETICA, 12);
        Font motivoFont = FontFactory.getFont(FontFactory.HELVETICA_OBLIQUE, 12, BaseColor.RED);
        Font tablaHeaderFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12, BaseColor.WHITE);
        Font tablaFont = FontFactory.getFont(FontFactory.HELVETICA, 11);

        // --- ENCABEZADO DE LA CARTA ---
        Paragraph pTitulo = new Paragraph("FACULTAD DE INGENIERÍA", tituloFont);
        pTitulo.setAlignment(Element.ALIGN_LEFT);
        document.add(pTitulo);
        
        Paragraph sTitulo = new Paragraph("UNIVERSIDAD DE SAN CARLOS DE GUATEMALA", subTituloFont);
        pTitulo.setAlignment(Element.ALIGN_LEFT);
        document.add(sTitulo);
        
        Paragraph tTitulo = new Paragraph("SECRETARÍA ADJUNTA", tituloFont);
        pTitulo.setAlignment(Element.ALIGN_LEFT);
        document.add(tTitulo);
        
        Paragraph cTitulo = new Paragraph("Tel: 2418 9106", subTituloFont);
        pTitulo.setAlignment(Element.ALIGN_LEFT);
        document.add(cTitulo);
        
        Paragraph qTitulo = new Paragraph("Oficina 4, Edificio T-4, Primer Nivel", subTituloFont);
        pTitulo.setAlignment(Element.ALIGN_LEFT);
        document.add(qTitulo);
       
        Paragraph STitulo = new Paragraph("secretariaadjunta@ingenieria.usac.edu.gt", subTituloFont);
        pTitulo.setAlignment(Element.ALIGN_RIGHT);
        document.add(STitulo);
        document.add(new Paragraph(" "));
        
        

        Paragraph pSaludo = new Paragraph("Respetable: " + nombre, textoFont);
        document.add(pSaludo);
        document.add(new Paragraph(" "));     

        document.add(new Paragraph("Deseándole toda clase de éxitos en el desarrollo de sus actividades diarias y laborales.", textoFont));
        document.add(new Paragraph(" "));
        document.add(new Paragraph("Por este medio, adjunto el listado de faltas cometidas la fecha  " + reportando + ", generado por el reloj biométrico, Le solicitamos que, en caso de que algún registro muestre inasistencias no justificadas, se informe mediante una nota dirigida a la Secretaría Adjunta. Esta nota debe entregarse de manera física dentro de un plazo de 3 días hábiles. Además, si existe algún cambio de horario que no haya sido notificado, le pedimos que informen y adjunten una copia del Dictamen correspondiente, emitido por Recursos Humanos.", textoFont));
        document.add(new Paragraph(" "));

        // --- VERIFICAR SI EXISTEN FALTAS ---
        if (!faltasEmpleado.isEmpty()) {
            Paragraph pMotivoTitulo = new Paragraph("Detalles de las faltas registradas:", textoFont);
            pMotivoTitulo.setSpacingBefore(10);
            pMotivoTitulo.setSpacingAfter(10);
            document.add(pMotivoTitulo);

            // Crear tabla con columnas
            // Crear tabla con 4 columnas
        PdfPTable table = new PdfPTable(3);
        table.setWidthPercentage(100);
        table.setSpacingBefore(10);
        table.setSpacingAfter(10);
        table.setWidths(new float[]{ 2f, 3f, 2f}); // proporciones de cada columna

        // Encabezados
        String[] headers = {"Día", "Motivo", "Tiempo Holgura"};
        for (String h : headers) {
            PdfPCell headerCell = new PdfPCell(new Phrase(h, tablaHeaderFont));
            headerCell.setBackgroundColor(BaseColor.DARK_GRAY);
            headerCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            headerCell.setPadding(5);
            table.addCell(headerCell);
        }

        // Agregar filas por cada falta
        for (Falta f : faltasEmpleado) {
            table.addCell(new PdfPCell(new Phrase(f.getDiaFalta(), tablaFont)));
            table.addCell(new PdfPCell(new Phrase(f.getMotivo().toString().replace("_", " "), motivoFont)));

            String duracion = (f.getTiempoHolgura() != 0)
                    ? f.getTiempoHolgura() + " min"
                    : "N/A";

            table.addCell(new PdfPCell(new Phrase(duracion, tablaFont)));
        }

        document.add(table);

            // Mensaje adicional
            Paragraph aviso = new Paragraph(
                    "Sin otro particular aprovecho la oportunidad para suscribir la presente.",
                    textoFont);
            aviso.setSpacingBefore(10);
            document.add(aviso);

        } else {
            Paragraph pSinFaltas = new Paragraph(
                    "No se encontraron faltas registradas en el sistema para esta fecha.", textoFont);
            pSinFaltas.setSpacingBefore(10);
            document.add(pSinFaltas);
        }

        // --- CIERRE DE CARTA ---

        document.add(new Paragraph(" "));
        Paragraph pFirma = new Paragraph("Atentamente, ", textoFont);
        pFirma.setAlignment(Element.ALIGN_CENTER);
        document.add(pFirma);
        document.add(new Paragraph(" "));
        document.add(new Paragraph(" "));
        document.add(new Paragraph(" "));
        document.add(new Paragraph(" "));
        document.add(new Paragraph(" "));
        document.add(new Paragraph(" "));
        Paragraph p1 = new Paragraph("Lic. Francisco Méndez Alvarado");
        p1.setAlignment(Element.ALIGN_CENTER);
        document.add(p1);

        Paragraph p2 = new Paragraph("Secretario Adjunto");
        p2.setAlignment(Element.ALIGN_CENTER);
        document.add(p2);

        Paragraph p3 = new Paragraph("Facultad de Ingeniería");
        p3.setAlignment(Element.ALIGN_CENTER);
        document.add(p3);

        Paragraph p4 = new Paragraph("Universidad de San Carlos de Guatemala");
        p4.setAlignment(Element.ALIGN_CENTER);
        document.add(p4);
        
        document.close();
    }
    
 public void crearInforme(String rutaSalida, String nombre, String dia, long codigo)
        throws FileNotFoundException, DocumentException {

        List<Falta> faltasEmpleado = getControlPrincipal().getModelo().getFaltas().stream()
                .filter(f -> Objects.equals(f.getCodigo(), codigo))
                .toList();

        // Agrupar faltas por día
        Map<String, List<Falta>> faltasPorDia = faltasEmpleado.stream()
                .collect(Collectors.groupingBy(Falta::getDiaFalta));

        // Obtener marcajes del trabajador
        List<Trabajador> marcajesEmpleado = getControlPrincipal().getModelo().getTrabajador().stream()
                .filter(t -> Objects.equals(t.getCUI(), codigo))
                .sorted(Comparator.comparing(Trabajador::getDia))
                .toList();

        // Crear archivo
        String nombreArchivo = rutaSalida + "/Informe_" + nombre.replace(" ", "_") + "_" + dia.replace("/", "-") + ".pdf";
        File archivo = new File(nombreArchivo);
        archivo.getParentFile().mkdirs();

        Document document = new Document();
        PdfWriter.getInstance(document, new FileOutputStream(archivo));
        document.open();

        Font tituloFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 15);
        Font subTituloFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 13);
        Font textoFont = FontFactory.getFont(FontFactory.HELVETICA, 12);
        Font tablaHeaderFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12, BaseColor.WHITE);
        Font tablaFont = FontFactory.getFont(FontFactory.HELVETICA, 11);

        // --- Encabezado ---
        Paragraph pTitulo = new Paragraph("FACULTAD DE INGENIERÍA", tituloFont);
        pTitulo.setAlignment(Element.ALIGN_LEFT);
        document.add(pTitulo);

        document.add(new Paragraph("UNIVERSIDAD DE SAN CARLOS DE GUATEMALA", subTituloFont));
        document.add(new Paragraph("SECRETARÍA ADJUNTA", tituloFont));
        document.add(new Paragraph("Tel: 2418 9106", subTituloFont));
        document.add(new Paragraph("Oficina 4, Edificio T-4, Primer Nivel", subTituloFont));
        document.add(new Paragraph("secretariaadjunta@ingenieria.usac.edu.gt", subTituloFont));
        document.add(new Paragraph(" "));

        document.add(new Paragraph("A quien corresponda,", textoFont));
        document.add(new Paragraph(" "));
        document.add(new Paragraph("Se muestra informe de los marcajes del mes correspondiente a " + nombre + ", extendida el dia  " + dia + ", se da la siguiente tabla con la nomenclatura de los colores correspondientes a las faltas", textoFont));
        document.add(new Paragraph(" "));

        // --- Tabla de Nomenclatura de Colores ---
        PdfPTable leyenda = new PdfPTable(2);
        leyenda.setWidthPercentage(60);
        leyenda.setSpacingBefore(10);
        leyenda.setSpacingAfter(10);
        leyenda.setWidths(new float[]{1f, 3f});

        addLeyendaColor(leyenda, BaseColor.RED, "Falta completa");
        addLeyendaColor(leyenda, BaseColor.ORANGE, "No marcó entrada o salida");
        addLeyendaColor(leyenda, new BaseColor(255, 255, 153), "Entrada o salida a destiempo");
        addLeyendaColor(leyenda, BaseColor.GREEN, "Sin incidencias (presente)");

        document.add(leyenda);

        // --- Tabla principal de marcajes ---
        PdfPTable tabla = new PdfPTable(6);
        tabla.setWidthPercentage(100);
        tabla.setSpacingBefore(10);
        tabla.setWidths(new float[]{2.5f, 2.5f, 2.5f, 2.5f, 2.5f, 2f});

        String[] headers = {"Día", "Entrada", "Inicio Almuerzo", "Fin Almuerzo", "Salida", "Tiempo Holgura"};
        for (String h : headers) {
            PdfPCell headerCell = new PdfPCell(new Phrase(h, tablaHeaderFont));
            headerCell.setBackgroundColor(BaseColor.DARK_GRAY);
            headerCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            headerCell.setPadding(5);
            tabla.addCell(headerCell);
        }

        for (Trabajador t : marcajesEmpleado) {
            String diaMarcaje = t.getDia();
            List<Falta> faltasDelDia = faltasPorDia.getOrDefault(diaMarcaje, List.of());

            // Sumar tiempo de holgura
            int holguraTotal = faltasDelDia.stream().mapToInt(Falta::getTiempoHolgura).sum();

            // Determinar color de fila
            BaseColor colorFila = BaseColor.WHITE;
            for (Falta f : faltasDelDia) {
                switch (f.getMotivo()) {
                    case FALTA_COMPLETA -> colorFila = BaseColor.RED;
                    case NO_MARCA_ENTRADA, NO_MARCA_SALIDA, NO_MARCA_SALIDA_ALMUERZO, NO_MARCA_ENTRADA_ALMUERZO ->
                        colorFila = BaseColor.ORANGE;
                    case ENTRADA_TARDE, ENTRADA_TARDE_DEL_ALMUERZO, SALIDA_TEMPRANA, SALIDA_TEMPRANA_AL_ALMUERZO ->
                        colorFila = new BaseColor(255, 255, 153); // Amarillo claro
                }
            }

            // Formatear horas o mostrar "—"
            String entrada = (t.getEntrada() != null) ? t.getEntrada().toString() : "—";
            String almIni = (t.getAlmIni() != null) ? t.getAlmIni().toString() : "—";
            String almFin = (t.getAlmFin() != null) ? t.getAlmFin().toString() : "—";
            String salida = (t.getSalida() != null) ? t.getSalida().toString() : "—";

            // Añadir celdas con color de fondo
            addColoredCell(tabla, diaMarcaje, tablaFont, colorFila);
            addColoredCell(tabla, entrada, tablaFont, colorFila);
            addColoredCell(tabla, almIni, tablaFont, colorFila);
            addColoredCell(tabla, almFin, tablaFont, colorFila);
            addColoredCell(tabla, salida, tablaFont, colorFila);
            addColoredCell(tabla, holguraTotal + " min", tablaFont, colorFila);
        }

        document.add(tabla);

        document.add(new Paragraph(" "));
        document.add(new Paragraph("Sin otro particular,", textoFont));
        document.add(new Paragraph("Atentamente,", textoFont));
        document.add(new Paragraph(" "));
        Paragraph pFirma = new Paragraph(nombre, textoFont);
        pFirma.setAlignment(Element.ALIGN_RIGHT);
        document.add(pFirma);

        document.close();
    }

    // --- Métodos auxiliares ---
    private void addLeyendaColor(PdfPTable table, BaseColor color, String texto) {
        PdfPCell colorCell = new PdfPCell();
        colorCell.setBackgroundColor(color);
        colorCell.setFixedHeight(15);
        table.addCell(colorCell);
        table.addCell(new PdfPCell(new Phrase(texto)));
    }

    private void addColoredCell(PdfPTable table, String texto, Font font, BaseColor color) {
        PdfPCell cell = new PdfPCell(new Phrase(texto, font));
        cell.setBackgroundColor(color);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);
    }
    
    public void escribirInstrucciones()throws FileNotFoundException, DocumentException {
        // Obtener carpeta Descargas del usuario actual
        String rutaDescargas = System.getProperty("user.home") + "/Downloads";

        // Nombre fijo del archivo PDF
        String nombreArchivo = rutaDescargas + "/Instrucciones.pdf";
        File archivo = new File(nombreArchivo);
        archivo.getParentFile().mkdirs();

        Document document = new Document();
        PdfWriter.getInstance(document, new FileOutputStream(archivo));
        document.open();

        Font tituloFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 15);
        Font subTituloFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 13);
        Font textoFont = FontFactory.getFont(FontFactory.HELVETICA, 12);
        Font tablaHeaderFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12, BaseColor.WHITE);
        Font tablaFont = FontFactory.getFont(FontFactory.HELVETICA, 11);

        // --- Encabezado ---
        Paragraph pTitulo = new Paragraph("Manual Basico de Uso", tituloFont);
        pTitulo.setAlignment(Element.ALIGN_CENTER);
        document.add(pTitulo);

        document.add(new Paragraph("1. REQUISITOS", subTituloFont));
        document.add(new Paragraph("2. PASOS DE USO", subTituloFont));
        document.add(new Paragraph("3. ACTUALIZAR DATOS", subTituloFont));
        document.add(new Paragraph("4. POSIBLES FALLOS Y SOLUCIONES", subTituloFont));
        document.add(new Paragraph(" "));

        document.add(new Paragraph("El proximo manual dara una breve explicación del uso y requisitos necesarios para el funcionamiento del mismo", textoFont));
        document.add(new Paragraph(""));
        document.add(new Paragraph("REQUISITOS", tituloFont));
        document.add(new Paragraph(" "));
        document.add(new Paragraph("")); 
        document.add(new Paragraph("Para el debido funcionamiento del programa se necesita los siguiente:"));                                 document.add(new Paragraph("1. Una carpeta para las cartas eh informes"));
        document.add(new Paragraph("2. Una carpeta a la cual se le introduciran los marcajes de los empleados"));        
        document.add(new Paragraph("3. La carpeta donde iran los ejecutables para la automatización"));      
        document.add(new Paragraph("4. Los archivos correspondientes al programa"));      
        document.add(new Paragraph(" "));
        document.add(new Paragraph(""));
        document.add(new Paragraph("PASOS DE USO", tituloFont));
        document.add(new Paragraph(""));
        document.add(new Paragraph("Para el debido funcionamiento del programa se deberia realizar los siguientes pasos:"));
        document.add(new Paragraph("1. Si es su primera vez usando el proyecto, debe exportar datos de su reloj biometrico de la sección Reporte, asegurese de seleccionar todos los empleados de la facultad, guarde dicho archivo en cualquier lado y copie su contenido en algún editor de hojas de calculo, preferiblemente hojas de calculo de Google."));
        document.add(new Paragraph("2. Exporte su archivo de hojas de calculo a su computador asegurese de exportarlo como csv"));  
        document.add(new Paragraph("3. Abra su programa de cálculo de marcajes, seleccione la opción CARGAR, la misma le desplegara una notificación, si es su primera vez ejecutando, seleccione la opción NO, en la ventana emergente busque su archivo axportado.")); 
        document.add(new Paragraph("5. Ahora vuelva a la opción CARGAR, aqui copie y pegue las rutas donde colocara los marcajes exportado y otra donde guardara las cartas eh informes realizados."));      
        document.add(new Paragraph("6. Cierre su programa para guardar lo realizada, este paso debe realizarlo siempre que cambie datos de los empleados."));
        document.add(new Paragraph("7. Para realizar los reportes de marcajes repita el paso 1 y 2, con la distinción de exportar datos de la zona AC Reg. del Reloj Biometrico."));
        document.add(new Paragraph("8. Exporte su archivo como csv, xls o xlsx, asegurece de guardarlo en la carpeta destino seleccionada por usted donde se encontraran los marcajes."));
        document.add(new Paragraph("9. Inicie el programa de calculo de marcajes, seleccione la opción CREAR REPORTES, seleccione su archivo csv, si no quiere realizar manualmente esta opción solo deje abierto el programa por 90 segundos sin interractuar con el. IMPORTANTE *Exporte en csv si va a realizar reporte manual, de lo contrario exporte con cualquier formato ya mencionado*."));
        document.add(new Paragraph("10. Su programa realizara las cartas y las guardara en la carpeta previamente seleccionada, el programa se cerrara por si solo."));
        document.add(new Paragraph(""));       
        document.add(new Paragraph(""));
        document.add(new Paragraph("ACTUALIZAR HORARIOS", tituloFont));
        document.add(new Paragraph("")); 
        document.add(new Paragraph("Para actualizar las horas para cada empleado siga estas instrucciones:"));
        document.add(new Paragraph("1. Use el botonn actualizar, sa le pedira usuario y contraseña, Use el usuario Admin Conraseña 1234, esto para eliminar los anteriores registros para actualizar los nuevos, cierre seguidamente el programa"));
        document.add(new Paragraph("2. Repetrir los pasos 1 y 3 de la sección PASOS A SEGUIR"));
        document.add(new Paragraph("ERRORES Y SOLUCIONES", tituloFont));
        document.add(new Paragraph(""));       
        document.add(new Paragraph(""));
        document.add(new Paragraph("Crear mas de dos reportes en una sola sesión puede llevar a cabo conflicto de marcajes se recomienda solo crear un reporte a la vez"));       
        document.add(new Paragraph("existe un error al cargar los horarios para resolverlo se comienda borrar todos los registros del programa para esto, cree un informe a fin de mes, este borra la memoria de las marcas, y luego elimine los horarios con ACTUALIZAR usuario admin contraseña 1234"));
        document.add(new Paragraph("Si desea cargar o actualizar horarios NO entre a otras opciones del programa"));
        document.add(new Paragraph("La funcion de reporte automatica puede llegar a se bloqueada por antivirus, ademas asegurese que la carpeta automata este en su escritorio, junto a la carpeta donde estaran las marcas de los empleados"));

// --- Tabla de Nomenclatura de Colores ---
        
        document.close();
    }

     


}
