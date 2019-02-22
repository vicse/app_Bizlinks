package Classes;
import java.io.*;
import javax.swing.*;
import jxl.write.*;
import jxl.*;


public class ExportarExcel {
private final File file;// archivo que manejaremos
private final JTable table; //la tabla que recibira
private final String nombreTab; //nombre del archivo
//constructor ( inicializa las variables)
    public ExportarExcel(JTable table,File file,String nombreTab){
        this.file=file;
        this.table=table;
        this.nombreTab=nombreTab;
    }
//metodo para exportar
    public boolean export(){
        try{
        System.out.print("iniciando proceso de exportar"); 
            //Representa nuestro archivo en excel y necesita un OutputStream para saber donde va locoar los datos
            try ( //hacemos referencia al archivo deseado
                DataOutputStream out = new DataOutputStream(new FileOutputStream(file))) {
                //Representa nuestro archivo en excel y necesita un OutputStream para saber donde va locoar los datos
                WritableWorkbook w = Workbook.createWorkbook(out);
                System.out.print("colocando nombre");
                //Coloca el nombre del "tab"(el nombre del arcchivo en el archivo y tambien en la hoja de excel)
                WritableSheet s = w.createSheet(nombreTab, 0);
                
                WritableFont headerCellFont = new WritableFont(WritableFont.ARIAL, 8, WritableFont.BOLD, true);
                //create format for header cells
                WritableCellFormat headerCellFormat = new WritableCellFormat(headerCellFont);
                
                 // Solo colocamos los encabezados
                
                Label headerCell1 = new Label(0, 0, "N° de Tarea", headerCellFormat);
                Label headerCell2 = new Label(1, 0, "Fecha de la actividad", headerCellFormat);
		Label headerCell3 = new Label(2, 0, "Tipo de dia", headerCellFormat);
                Label headerCell4 = new Label(3, 0, "Trabajador", headerCellFormat);
                Label headerCell5 = new Label(4, 0, "Tipo actividad", headerCellFormat);
                Label headerCell6 = new Label(5, 0, "La actividad esta asociada para un cliente específico", headerCellFormat);                
                    Label headerCell7 = new Label(6, 0, "Razon social del cliente", headerCellFormat);
                Label headerCell8 = new Label(7, 0, "Producto", headerCellFormat);
                Label headerCell9 = new Label(8, 0, "Detalle del nuevo producto", headerCellFormat);
                Label headerCell10 = new Label(9, 0, "Detalle de la actividad", headerCellFormat);
                Label headerCell11 = new Label(10, 0, "N° de Horas", headerCellFormat);
               
                /*Label headerCell12 = new Label(11, 0, "ETAPA", headerCellFormat);
                Label headerCell13 = new Label(12, 0, "ESTADO", headerCellFormat);
                Label headerCell14 = new Label(13, 0, "PRIORIDAD", headerCellFormat);
                Label headerCell15 = new Label(14, 0, "SOLICITADO POR", headerCellFormat);*/
                                                              
                s.addCell(headerCell1);
                s.addCell(headerCell2);
                s.addCell(headerCell3);
                s.addCell(headerCell4);
                s.addCell(headerCell5);
                s.addCell(headerCell6);
                s.addCell(headerCell7);
                s.addCell(headerCell8);
                s.addCell(headerCell9);
                s.addCell(headerCell10);
                s.addCell(headerCell11);
                /*s.addCell(headerCell12);
                s.addCell(headerCell13);
                s.addCell(headerCell14);
                s.addCell(headerCell15); */
                
                //ocupamos dos ciclos para recorrer nuestra tabla y escribir en las celdas de excel
                System.out.print("recorriendo la tabla");
                for(int i=1;i< table.getRowCount();i++){
                    //Se indica el número de la columna final a exportar
                    for(int j=0;j<11;j++){
                        Object objeto=table.getValueAt(i,j);
                        s.addCell(new Label(j, i, String.valueOf(objeto)));
                    }
                }
                
                //String[] tittles = new String[]{"ID","ENTREGA","TIPO_DIA", "ASIGNADO", "TIPO_ACTIVIDAD","ACTIVIDAD_ASOCIADA","RAZON_SOCIAL_CLIENTE", "PRODUCTO","DETALLE_NVO_PRODUCTO", "DETALLE_ACTIVIDAD","N° DE HORAS","ETAPA", "ESTADO", "PRIORIDAD","SOLICITADO POR"};
                
                //for(int i=0; i< tittles.length ; i++){
                //    String tits = tittles[i];
                //}
                
                //s.setColumnView(0, 0);
                
                          
                //escribimos en el archivo
                w.write();
                System.out.print("Cerramos el WritableWorkbook y DataOutputStream");
                JOptionPane.showMessageDialog(null, "Datos exportados satisfactoriamente");
                w.close();
            }
        return true;
        }catch(IOException | WriteException ex){}
              System.out.print("ocurrio un error");
              JOptionPane.showMessageDialog(null, "Ocurrio un problema al exportar datos");
            return false;
    }
}
