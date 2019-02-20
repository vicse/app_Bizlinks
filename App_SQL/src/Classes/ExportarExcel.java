package Classes;
import java.io.*;
import javax.swing.*;
import jxl.write.*;
import jxl.*;
import org.apache.poi.ss.usermodel.WorkbookFactory;


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
                
                String [] titulos = {"ID","ENTREGA","TIPO_DIA", "ASIGNADO", "TIPO_ACTIVIDAD","ACTIVIDAD_ASOCIADA","RAZON_SOCIAL_CLIENTE", "PRODUCTO","DETALLE_NVO_PRODUCTO", "DETALLE_ACTIVIDAD","N° DE HORAS","ETAPA", "ESTADO", "PRIORIDAD","SOLICITADO POR"};
                
                //ocupamos dos ciclos para recorrer nuestra tabla y escribir en las celdas de excel
                System.out.print("recorriendo la tabla");
                for(int i=0;i< table.getRowCount();i++){
                    for(int j=0;j<table.getColumnCount();j++){
                        Object objeto=table.getValueAt(i,j);
                        s.addCell(new Label(j, i, String.valueOf(objeto)));
                    }
                }
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
