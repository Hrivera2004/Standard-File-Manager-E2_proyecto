/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package estru2_proyecto;

/**
 *
 * @author Asus
 */
import java.awt.Desktop;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import javax.swing.JFileChooser;
import javax.swing.JTable;
import javax.swing.filechooser.FileNameExtensionFilter;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;

public class Exportar_Excel {

    public void exportar(JTable t) throws IOException {
        JFileChooser chooser = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Archivos de Excel", "xls");
        chooser.setFileFilter(filter);
        chooser.setDialogTitle("Guardar archivo");
        chooser.setAcceptAllFileFilterUsed(false);

        if (chooser.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
            String ruta = chooser.getSelectedFile().toString().concat(".xls");

            try {
                File archivoXLS = new File(ruta);
                if (archivoXLS.exists()) {
                    archivoXLS.delete();
                }
                archivoXLS.createNewFile();

                Workbook libro = new HSSFWorkbook();
                FileOutputStream archivo = new FileOutputStream(archivoXLS);
                Sheet hoja = libro.createSheet("Mi hoja de trabajo 1");
                hoja.setDisplayGridlines(true);

                // Crear fila de encabezados
                Row filaEncabezados = hoja.createRow(0);
                for (int c = 0; c < t.getColumnCount(); c++) {
                    Cell celda = filaEncabezados.createCell(c);
                    celda.setCellValue(t.getColumnName(c));
                }

                // Crear filas con datos
                for (int f = 0; f < t.getRowCount(); f++) {
                    Row fila = hoja.createRow(f + 1); // Comenzar en la segunda fila
                    for (int c = 0; c < t.getColumnCount(); c++) {
                        Cell celda = fila.createCell(c);
                        Object valor = t.getValueAt(f, c);

                        if (valor instanceof Number) {
                            celda.setCellValue(((Number) valor).doubleValue());
                        } else if (valor != null) {
                            celda.setCellValue(valor.toString());
                        } else {
                            celda.setCellValue("");
                        }
                    }
                }

                // Escribir el archivo Excel
                libro.write(archivo);
                archivo.close();

                // Abrir el archivo generado
                Desktop.getDesktop().open(archivoXLS);

                System.out.println("Archivo generado correctamente en: " + ruta);
            } catch (IOException e) {
                e.printStackTrace();
                throw e;
            }
        }
    }
}
