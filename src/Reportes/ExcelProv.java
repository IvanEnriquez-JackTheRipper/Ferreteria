package Reportes;
//CREADO POR VICTOR
import java.awt.Desktop;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import Modelo.Conexion;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.util.IOUtils;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExcelProv {

    public static void reporte() {
        Workbook book = new XSSFWorkbook();
        Sheet sheet = book.createSheet("Proveedores");

        try {
            // Logo
            InputStream is = new FileInputStream("src/img/Logooo.png");
            byte[] bytes = IOUtils.toByteArray(is);
            int imgIndex = book.addPicture(bytes, Workbook.PICTURE_TYPE_PNG);
            is.close();

            CreationHelper help = book.getCreationHelper();
            Drawing draw = sheet.createDrawingPatriarch();
            ClientAnchor anchor = help.createClientAnchor();
            anchor.setCol1(0);
            anchor.setRow1(1);
            Picture pict = draw.createPicture(anchor, imgIndex);
            pict.resize(1, 3);

            // Título
            CellStyle tituloEstilo = book.createCellStyle();
            tituloEstilo.setAlignment(HorizontalAlignment.CENTER);
            tituloEstilo.setVerticalAlignment(VerticalAlignment.CENTER);
            Font fuenteTitulo = book.createFont();
            fuenteTitulo.setFontName("Arial");
            fuenteTitulo.setBold(true);
            fuenteTitulo.setFontHeightInPoints((short) 14);
            tituloEstilo.setFont(fuenteTitulo);

            Row filaTitulo = sheet.createRow(1);
            Cell celdaTitulo = filaTitulo.createCell(1);
            celdaTitulo.setCellStyle(tituloEstilo);
            celdaTitulo.setCellValue("Reporte de Proveedores");
            sheet.addMergedRegion(new CellRangeAddress(1, 2, 1, 4));

            // Cabecera
            String[] cabecera = new String[]{"RUC", "Nombre", "Teléfono", "Dirección", "Razón"};
            CellStyle headerStyle = book.createCellStyle();
            headerStyle.setFillForegroundColor(IndexedColors.LIGHT_BLUE.getIndex());
            headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            headerStyle.setBorderBottom(BorderStyle.THIN);
            headerStyle.setBorderLeft(BorderStyle.THIN);
            headerStyle.setBorderRight(BorderStyle.THIN);
            headerStyle.setBorderTop(BorderStyle.THIN);
            Font font = book.createFont();
            font.setFontName("Arial");
            font.setBold(true);
            font.setColor(IndexedColors.WHITE.getIndex());
            font.setFontHeightInPoints((short) 12);
            headerStyle.setFont(font);

            Row filaEncabezados = sheet.createRow(4);
            for (int i = 0; i < cabecera.length; i++) {
                Cell celda = filaEncabezados.createCell(i);
                celda.setCellStyle(headerStyle);
                celda.setCellValue(cabecera[i]);
            }

            // Datos de la BD
            Conexion con = new Conexion();
            Connection conn = con.getConnection();
            PreparedStatement ps = conn.prepareStatement("SELECT ruc, nombre, telefono, direccion, razon FROM proveedores");
            ResultSet rs = ps.executeQuery();

            int numFilaDatos = 5;
            CellStyle datosEstilo = book.createCellStyle();
            datosEstilo.setBorderBottom(BorderStyle.THIN);
            datosEstilo.setBorderLeft(BorderStyle.THIN);
            datosEstilo.setBorderRight(BorderStyle.THIN);
            datosEstilo.setBorderTop(BorderStyle.THIN);

            while (rs.next()) {
                Row filaDatos = sheet.createRow(numFilaDatos);
                for (int i = 0; i < cabecera.length; i++) {
                    Cell celda = filaDatos.createCell(i);
                    celda.setCellStyle(datosEstilo);
                    celda.setCellValue(rs.getString(i + 1));
                }
                numFilaDatos++;
            }

            for (int i = 0; i < cabecera.length; i++) {
                sheet.autoSizeColumn(i);
            }

            sheet.setZoom(150);
            String fileName = "proveedores";
            String home = System.getProperty("user.home");
            File file = new File(home + "/Downloads/" + fileName + ".xlsx");
            FileOutputStream fileOut = new FileOutputStream(file);
            book.write(fileOut);
            fileOut.close();
            Desktop.getDesktop().open(file);
            JOptionPane.showMessageDialog(null, "Reporte de proveedores generado");

        } catch (IOException | SQLException ex) {
            Logger.getLogger(ExcelProv.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
