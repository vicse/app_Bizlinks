package Forms;

import Classes.Conexion;
import com.sun.awt.AWTUtilities;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Shape;
import java.awt.geom.RoundRectangle2D;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Date;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author luiscardenas
 */
@SuppressWarnings("serial")
public class ListarTareas extends javax.swing.JFrame {

    Conexion con = new Conexion();
    DefaultTableModel ModeloTablaConsulta = new DefaultTableModel(){
        @Override
        public boolean isCellEditable(int row, int column) {
                return false;
        }
    };
    DefaultTableCellRenderer HeadTable = new DefaultTableCellRenderer();
    public String rols;
    public static boolean cancel=false;
    public static String rol;
    private String filterPrioridad,filterEstado;
    /**
     * Constructor de la clase
     * @param r Parametro recibido de Login indicando el rol de la cuenta
     */
    public ListarTareas(String r){
        initComponents();
        FillComboEstado();
        FillComboPrioridad();
        this.rol=r;
        Priority(rol);
        this.setLocationRelativeTo(null);
        this.setIconImage(new ImageIcon(getClass().getResource("/Resources/icon_250.png")).getImage());
        impBusquedaEmpresa.setImage(new ImageIcon(getClass().getResource("/Resources/Titles.png")).getImage());
        impLista.setImage(new ImageIcon(getClass().getResource("/Resources/Titles.png")).getImage());
        ipmLogoLeft.setImage(new ImageIcon(getClass().getResource("/Resources/icon_250.png")).getImage());
        imp_main.setImage(new ImageIcon(getClass().getResource("/Resources/fond_main.png")).getImage());
        String[] columns = new String[]{"ID", "TIPO_ACTIVIDAD", "PRODUCTO", "DETALLE ACTIVIDAD","ETAPA", "ASIGNADO", "ESTADO", "PRIORIDAD","SOLICITADO","ENTREGA","ADJUNTO"};
        ModeloTablaConsulta.setColumnIdentifiers(columns);
        tbTablaConsulta.setModel(ModeloTablaConsulta);
        StyleTable();
        Shape redondear = new RoundRectangle2D.Double(0, 0, this.getBounds().width, this.getBounds().height, 6, 6);
        AWTUtilities.setWindowShape(this, redondear);       
        LoadData("dbo.tareas_emp","dbo.tipo_actividad","dbo.producto","dbo.etapa_tarea","dbo.estado_tarea","dbo.prioridad_tarea");        
    }
    
    /*--------------------------------FUNCIONES-----------------------------------------*/
    /**
     * Cargar datos de base de datos a la tabla
     * @param table Nombre de la tabla para la consulta de los datos
     * @param t2 NOmbre de la tabla para realizar JOIN
     * @param t3 NOmbre de la tabla para realizar JOIN
     * @param t4 NOmbre de la tabla para realizar JOIN
     * @param t5 NOmbre de la table para realizar JOIN
     * @param t6 NOmbre de la table para realizar JOIN
     */
    public void LoadData(String table,String t2,String t3, String t4, String t5, String t6){
        CleanData();
        try {
            PreparedStatement ps = con.getConnection().prepareStatement("Select id_tarea\n" +
            ", tipo_actividad.tipo_actividad\n" +
            ", producto.producto\n" +
            ", detalle_actividad\n" +
            ", etapa_tarea.etapa\n" +
            ", COALESCE(responsable, '')\n" +
            ", estado_tarea.estado\n" +
            ", prioridad_tarea.prioridad\n" +
            ", COALESCE(solicitado, '')\n" +
            ", fecha_entrega\n" +
            ", adjunto FROM "+table+
            " INNER JOIN "+t2+" ON "+t2+".id_tipo_actividad="+table+".nombre_tipo_actividad"+
            " INNER JOIN "+t3+" ON "+t3+".id_producto="+table+".nombre_producto"+
            " INNER JOIN "+t4+" ON "+t4+".id_etapa="+table+".nombre_etapa"+
            " INNER JOIN "+t5+" ON "+t5+".id_estado="+table+".nombre_estado"+
            " INNER JOIN "+t6+" ON "+t6+".id_prioridad_tarea="+table+".nombre_prioridad"+
            " ORDER BY id_tarea ASC");
            ResultSet r = ps.executeQuery();
            ResultSetMetaData datos = r.getMetaData();
            
            while(r.next()){

                Object[] filas = new Object[datos.getColumnCount()];

                for (int i = 0; i < datos.getColumnCount(); i++) {
                    filas[i] = r.getObject(i + 1);
                }            
                ModeloTablaConsulta.addRow(filas);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error de Conexion: "+ e.getMessage());
        }
    }
    
    //Metodo para elimiar una tarea
    public void Delete(String table,String id){
        try{
            PreparedStatement ps = con.getConnection().prepareStatement("DELETE FROM " + table + " WHERE id_tarea=" +id);
            ps.execute();
            
            JOptionPane.showMessageDialog(this, "Borrado Exitosamente", "Proceso Culminado", JOptionPane.INFORMATION_MESSAGE);
            
        }catch(SQLException e){
            JOptionPane.showMessageDialog(null, "Error de conexion:" + e.getMessage());
        }
    }
    
    
    //meetodos para rellenar los comboBox de busqueda
    private void FillComboPrioridad(){
        try{
            PreparedStatement ps = con.getConnection().prepareStatement("SELECT * FROM dbo.prioridad_tarea");
            ResultSet r = ps.executeQuery();
            
            while(r.next()){
                String prioridad = r.getString("prioridad");
                jComboBoxFprioridad.addItem(prioridad);
            }         
        }catch(SQLException e){
            JOptionPane.showMessageDialog(null, e);
        }
    }
    
    private void FillComboEstado(){
        try{
            PreparedStatement ps = con.getConnection().prepareStatement("SELECT * FROM dbo.estado_tarea");
            ResultSet r = ps.executeQuery();
            
            while(r.next()){
                String estado = r.getString("estado");
                jComboBoxFestado.addItem(estado);
            }         
        }catch(SQLException e){
            JOptionPane.showMessageDialog(null, e);
        }
    }
    
    
    
    
    /**
     * Estilos de la Tabla (Dimensiones, colores, etc)
     */
    public void StyleTable(){
        HeadTable.setBackground(new Color(171, 173, 166));
        HeadTable.setForeground(new Color(0,0,0));
        HeadTable.setPreferredSize(new Dimension(scTable.getWidth(),25));
        tbTablaConsulta.getColumnModel().getColumn(1).setMinWidth(200);
        for (int i = 0; i < tbTablaConsulta.getModel().getColumnCount(); i++) {
            tbTablaConsulta.getColumnModel().getColumn(i).setHeaderRenderer(HeadTable);
            tbTablaConsulta.getColumnModel().getColumn(i).setMinWidth(30);
//          tbTablaConsulta.getColumnModel().getColumn(i).setMaxWidth(150);
            tbTablaConsulta.getColumnModel().getColumn(i).setResizable(true);
        }
    }
    
    /**
     * Funcion para filtrar la base de datos 
     * @param column Nombre de la columna del ID
     * @param filter Cadena para filtrar los datos
     */
//    public void FilterDB(String column,String filter){
//        
//        cancel=false;
//        int load=0;  
//        try {
//            LoadData ld = new LoadData(this, rootPaneCheckingEnabled);
//            PreparedStatement ps = con.getConnection().prepareStatement("Select * FROM DATOS_EMPRESA  WHERE "+column+" = '"+filter+"'");
//            
//            ResultSet r = ps.executeQuery();
//            ResultSetMetaData dat = r.getMetaData();
//            if(!r.next()){
//                JOptionPane.showMessageDialog(this, "No hay registros");
//            }
//            while(r.next()){
//                Object[] fils = new Object[dat.getColumnCount()];
//
//                for (int i = 0; i < dat.getColumnCount(); i++) {
//                    fils[i] = r.getObject(i + 1);
//                }
//                ModeloTablaConsulta.addRow(fils);
//                load++;
//                if(load==100000){
//                    ld.setVisible(true);
//                }   
//                if(cancel=true){
//                    break; //Cancelar carga de datos en la tabla(Cuando se demora mucho la consulta)
//                }
//            }
//            ld.dispose();
//        } catch (SQLException e) {
//            JOptionPane.showMessageDialog(null, "Error de Conexion: "+ e.getMessage());
//        }
//    }

    public void FilterDB(String table,String t2, String t3, String t4, String t5, String t6,String column,String filter){
        CleanData();
        try {
            
            PreparedStatement psm = con.getConnection().prepareStatement("Select id_tarea\n" +
            ", tipo_actividad.tipo_actividad\n" +
            ", producto.producto\n" +
            ", detalle_actividad\n" +
            ", etapa_tarea.etapa\n" +
            ", COALESCE(responsable, '')\n" +
            ", estado_tarea.estado\n" +
            ", prioridad_tarea.prioridad\n" +
            ", COALESCE(solicitado, '')\n" +
            ", fecha_entrega\n" +
            ", adjunto FROM "+table+
            " INNER JOIN "+t2+" ON "+t2+".id_tipo_actividad="+table+".nombre_tipo_actividad"+
            " INNER JOIN "+t3+" ON "+t3+".id_producto="+table+".nombre_producto"+
            " INNER JOIN "+t4+" ON "+t4+".id_etapa="+table+".nombre_etapa"+
            " INNER JOIN "+t5+" ON "+t5+".id_estado="+table+".nombre_estado"+
            " INNER JOIN "+t6+" ON "+t6+".id_prioridad_tarea="+table+".nombre_prioridad"+
            " WHERE "+ column +" LIKE '"+filter+"%'"+
            "ORDER BY id_tarea ASC");
            
            ResultSet rs = psm.executeQuery();            
            ResultSetMetaData dats = rs.getMetaData();
            DefaultTableModel model = (DefaultTableModel) tbTablaConsulta.getModel();
            while(rs.next()){
                 Object[] fils = new Object[dats.getColumnCount()];
//
                for (int i = 0; i < dats.getColumnCount(); i++) {
                    fils[i] = rs.getObject(i + 1);
                }
                model.addRow(fils);
            }
            
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error de Conexion: "+ e.getMessage());
        }
    }
    public void CleanData(){
        int filas = ModeloTablaConsulta.getRowCount();
        for (int i = 0; i < filas; i++) {
            ModeloTablaConsulta.removeRow(0);
        }
    }
    
    /**
     * Funcion para habilitar y deshabilitar funciones dentro de la aplicacion de acuerdo a su rol
     * @param r Rol obtenido del Login
     */
    public void Priority(String r){

        System.out.println("valor: "+r);
        if (r=="01")
        {
            System.out.println("dentro 02");
                btnActualizar.setVisible(false);
                //btnNuevoUsuario.setVisible(false);
        }
        else
        {
            if("02".equals(r))
            {
              System.out.println("dentro 01");
                btnActualizar.setVisible(true);
                //btnNuevoUsuario.setVisible(true);  
            }
            else
            {
                System.out.println("nada"+r);
            }
        }
                
        
    }
    
    int x,y;
    /*----------------------------------------------------------------------------------*/
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        imp_main = new gui.ImagePanel();
        jPanel2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        btnBuscar = new javax.swing.JButton();
        btnBuscar1 = new javax.swing.JButton();
        scTable = new javax.swing.JScrollPane();
        tbTablaConsulta = new javax.swing.JTable();
        jComboBoxFprioridad = new javax.swing.JComboBox<>();
        jComboBoxFestado = new javax.swing.JComboBox<>();
        jPanel3 = new javax.swing.JPanel();
        btnActualizar = new javax.swing.JButton();
        btnRecargar = new javax.swing.JButton();
        btnLimpiar = new javax.swing.JButton();
        btnEliminar = new javax.swing.JButton();
        impBusquedaEmpresa = new gui.ImagePanel();
        jLabel2 = new javax.swing.JLabel();
        impLista = new gui.ImagePanel();
        jLabel4 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        jPanel5 = new javax.swing.JPanel();
        title = new javax.swing.JLabel();
        jPanel6 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        jPanel7 = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        jPanel8 = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        jPanel9 = new javax.swing.JPanel();
        jLabel10 = new javax.swing.JLabel();
        jPanel10 = new javax.swing.JPanel();
        jLabel11 = new javax.swing.JLabel();
        jPanel11 = new javax.swing.JPanel();
        jLabel12 = new javax.swing.JLabel();
        btnIngresar = new javax.swing.JButton();
        jPanel12 = new javax.swing.JPanel();
        jLabel13 = new javax.swing.JLabel();
        jpControl = new javax.swing.JPanel();
        bgClose = new javax.swing.JPanel();
        btnClose = new javax.swing.JLabel();
        bgMinimize = new javax.swing.JPanel();
        btnMinimize = new javax.swing.JLabel();
        ipmLogoLeft = new gui.ImagePanel();
        jLabel9 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setBackground(new java.awt.Color(255, 255, 255));
        setUndecorated(true);
        setResizable(false);

        imp_main.setBackground(new java.awt.Color(255, 255, 102));

        jPanel2.setBackground(new java.awt.Color(0, 153, 255));
        jPanel2.setOpaque(false);

        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("Prioridad:");

        jLabel6.setForeground(new java.awt.Color(255, 255, 255));
        jLabel6.setText("Estado:");

        btnBuscar.setBackground(new java.awt.Color(200, 255, 255));
        btnBuscar.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        btnBuscar.setForeground(new java.awt.Color(51, 51, 255));
        btnBuscar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Resources/search.png"))); // NOI18N
        btnBuscar.setText("Buscar por Prioridad");
        btnBuscar.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        btnBuscar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBuscarActionPerformed(evt);
            }
        });

        btnBuscar1.setBackground(new java.awt.Color(200, 255, 255));
        btnBuscar1.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        btnBuscar1.setForeground(new java.awt.Color(51, 51, 255));
        btnBuscar1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Resources/search.png"))); // NOI18N
        btnBuscar1.setText("Buscar por Estado");
        btnBuscar1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        btnBuscar1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBuscar1ActionPerformed(evt);
            }
        });

        tbTablaConsulta.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        tbTablaConsulta.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        tbTablaConsulta.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        tbTablaConsulta.setGridColor(new java.awt.Color(255, 255, 255));
        tbTablaConsulta.setRowHeight(23);
        tbTablaConsulta.setSelectionBackground(new java.awt.Color(51, 124, 207));
        scTable.setViewportView(tbTablaConsulta);

        jComboBoxFprioridad.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Seleccionar Filtro" }));
        jComboBoxFprioridad.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBoxFprioridadActionPerformed(evt);
            }
        });

        jComboBoxFestado.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Seleccionar Filtro" }));
        jComboBoxFestado.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBoxFestadoActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addGap(76, 76, 76)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1)
                    .addComponent(btnBuscar)
                    .addComponent(jComboBoxFprioridad, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(99, 99, 99)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnBuscar1)
                    .addComponent(jComboBoxFestado, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(scTable))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(jLabel1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jComboBoxFprioridad, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jComboBoxFestado, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnBuscar, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnBuscar1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(scTable, javax.swing.GroupLayout.PREFERRED_SIZE, 190, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(21, Short.MAX_VALUE))
        );

        jPanel3.setBackground(new java.awt.Color(0, 153, 255));
        jPanel3.setOpaque(false);

        btnActualizar.setBackground(new java.awt.Color(255, 102, 102));
        btnActualizar.setForeground(new java.awt.Color(255, 255, 255));
        btnActualizar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Resources/update.png"))); // NOI18N
        btnActualizar.setText("Modificar Tarea");
        btnActualizar.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        btnActualizar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnActualizarActionPerformed(evt);
            }
        });

        btnRecargar.setBackground(new java.awt.Color(255, 102, 102));
        btnRecargar.setForeground(new java.awt.Color(255, 255, 255));
        btnRecargar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Resources/reload.png"))); // NOI18N
        btnRecargar.setText("Recargar");
        btnRecargar.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        btnRecargar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRecargarActionPerformed(evt);
            }
        });

        btnLimpiar.setBackground(new java.awt.Color(200, 255, 255));
        btnLimpiar.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        btnLimpiar.setForeground(new java.awt.Color(51, 51, 255));
        btnLimpiar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Resources/reset.png"))); // NOI18N
        btnLimpiar.setText("Limpiar");
        btnLimpiar.setBorder(null);
        btnLimpiar.setBorderPainted(false);
        btnLimpiar.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        btnLimpiar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLimpiarActionPerformed(evt);
            }
        });

        btnEliminar.setBackground(new java.awt.Color(255, 102, 102));
        btnEliminar.setForeground(new java.awt.Color(255, 255, 255));
        btnEliminar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Resources/update.png"))); // NOI18N
        btnEliminar.setText("Eliminar Tarea");
        btnEliminar.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        btnEliminar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEliminarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnLimpiar, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btnEliminar)
                .addGap(18, 18, 18)
                .addComponent(btnActualizar)
                .addGap(18, 18, 18)
                .addComponent(btnRecargar)
                .addGap(30, 30, 30))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap(209, Short.MAX_VALUE)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnActualizar, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnRecargar, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnLimpiar, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnEliminar, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        jLabel2.setFont(new java.awt.Font("Bookman Old Style", 0, 14)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("Lista de tareas");

        javax.swing.GroupLayout impBusquedaEmpresaLayout = new javax.swing.GroupLayout(impBusquedaEmpresa);
        impBusquedaEmpresa.setLayout(impBusquedaEmpresaLayout);
        impBusquedaEmpresaLayout.setHorizontalGroup(
            impBusquedaEmpresaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(impBusquedaEmpresaLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel2)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        impBusquedaEmpresaLayout.setVerticalGroup(
            impBusquedaEmpresaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, 25, Short.MAX_VALUE)
        );

        jLabel4.setFont(new java.awt.Font("Bookman Old Style", 0, 14)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setText("Busqueda de tareas");

        javax.swing.GroupLayout impListaLayout = new javax.swing.GroupLayout(impLista);
        impLista.setLayout(impListaLayout);
        impListaLayout.setHorizontalGroup(
            impListaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(impListaLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel4)
                .addContainerGap(916, Short.MAX_VALUE))
        );
        impListaLayout.setVerticalGroup(
            impListaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, 25, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout imp_mainLayout = new javax.swing.GroupLayout(imp_main);
        imp_main.setLayout(imp_mainLayout);
        imp_mainLayout.setHorizontalGroup(
            imp_mainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(imp_mainLayout.createSequentialGroup()
                .addGap(13, 13, 13)
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(13, 13, 13))
            .addComponent(impBusquedaEmpresa, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(imp_mainLayout.createSequentialGroup()
                .addComponent(impLista, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 14, Short.MAX_VALUE))
        );
        imp_mainLayout.setVerticalGroup(
            imp_mainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, imp_mainLayout.createSequentialGroup()
                .addComponent(impBusquedaEmpresa, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(24, 24, 24)
                .addComponent(impLista, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        jPanel1.setBackground(new java.awt.Color(39, 39, 39));

        jPanel4.setBackground(new java.awt.Color(60, 60, 60));

        jLabel3.setBackground(new java.awt.Color(65, 65, 65));
        jLabel3.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Resources/libreta.png"))); // NOI18N
        jLabel3.setText("Datos Administrativos");
        jLabel3.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(37, 37, 37))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 35, Short.MAX_VALUE)
        );

        jSeparator1.setBackground(new java.awt.Color(255, 51, 51));

        jPanel5.setBackground(new java.awt.Color(25, 25, 25));

        title.setBackground(new java.awt.Color(0, 0, 0));
        title.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        title.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Resources/bizlinks_main.png"))); // NOI18N

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(title, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(title, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 82, Short.MAX_VALUE)
        );

        jPanel6.setBackground(new java.awt.Color(60, 60, 60));

        jLabel5.setBackground(new java.awt.Color(65, 65, 65));
        jLabel5.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(255, 255, 255));
        jLabel5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Resources/libreta.png"))); // NOI18N
        jLabel5.setText("Módulos");
        jLabel5.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 202, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel5, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 35, Short.MAX_VALUE)
        );

        jPanel7.setBackground(new java.awt.Color(60, 60, 60));

        jLabel7.setBackground(new java.awt.Color(65, 65, 65));
        jLabel7.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(255, 255, 255));
        jLabel7.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Resources/libreta.png"))); // NOI18N
        jLabel7.setText("Datos Empresas");
        jLabel7.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jLabel7.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel7MouseClicked(evt);
            }
        });

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel7Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(34, 34, 34))
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel7, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 35, Short.MAX_VALUE)
        );

        jPanel8.setBackground(new java.awt.Color(60, 60, 60));

        jLabel8.setBackground(new java.awt.Color(65, 65, 65));
        jLabel8.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(255, 255, 255));
        jLabel8.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Resources/libreta.png"))); // NOI18N
        jLabel8.setText("Detalle de Parner");
        jLabel8.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel8Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(23, 23, 23))
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel8, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 35, Short.MAX_VALUE)
        );

        jPanel9.setBackground(new java.awt.Color(60, 60, 60));

        jLabel10.setBackground(new java.awt.Color(65, 65, 65));
        jLabel10.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N
        jLabel10.setForeground(new java.awt.Color(255, 255, 255));
        jLabel10.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Resources/libreta.png"))); // NOI18N
        jLabel10.setText("Administración de Usuarios");
        jLabel10.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jLabel10.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel10MouseClicked(evt);
            }
        });

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel9Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 197, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel10, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 35, Short.MAX_VALUE)
        );

        jPanel10.setBackground(new java.awt.Color(60, 60, 60));

        jLabel11.setBackground(new java.awt.Color(65, 65, 65));
        jLabel11.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N
        jLabel11.setForeground(new java.awt.Color(255, 255, 255));
        jLabel11.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Resources/libreta.png"))); // NOI18N
        jLabel11.setText("Mesa de Ayuda");
        jLabel11.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));

        javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
        jPanel10.setLayout(jPanel10Layout);
        jPanel10Layout.setHorizontalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel10Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(36, 36, 36))
        );
        jPanel10Layout.setVerticalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel11, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 35, Short.MAX_VALUE)
        );

        jPanel11.setBackground(new java.awt.Color(60, 60, 60));

        jLabel12.setBackground(new java.awt.Color(65, 65, 65));
        jLabel12.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N
        jLabel12.setForeground(new java.awt.Color(255, 255, 255));
        jLabel12.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Resources/libreta.png"))); // NOI18N
        jLabel12.setText("Cerrar Sesion");
        jLabel12.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jLabel12.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel12MouseClicked(evt);
            }
        });

        javax.swing.GroupLayout jPanel11Layout = new javax.swing.GroupLayout(jPanel11);
        jPanel11.setLayout(jPanel11Layout);
        jPanel11Layout.setHorizontalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel12, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(62, 62, 62))
        );
        jPanel11Layout.setVerticalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel12, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 35, Short.MAX_VALUE)
        );

        btnIngresar.setBackground(new java.awt.Color(0, 102, 102));
        btnIngresar.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        btnIngresar.setForeground(new java.awt.Color(255, 255, 255));
        btnIngresar.setText("Registrar Tarea");
        btnIngresar.setBorder(null);
        btnIngresar.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        btnIngresar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnIngresarActionPerformed(evt);
            }
        });
        btnIngresar.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                btnIngresarKeyPressed(evt);
            }
        });

        jPanel12.setBackground(new java.awt.Color(60, 60, 60));

        jLabel13.setBackground(new java.awt.Color(65, 65, 65));
        jLabel13.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N
        jLabel13.setForeground(new java.awt.Color(255, 255, 255));
        jLabel13.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Resources/libreta.png"))); // NOI18N
        jLabel13.setText("Gestor de Tareas");
        jLabel13.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jLabel13.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel13MouseClicked(evt);
            }
        });

        javax.swing.GroupLayout jPanel12Layout = new javax.swing.GroupLayout(jPanel12);
        jPanel12.setLayout(jPanel12Layout);
        jPanel12Layout.setHorizontalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel13, javax.swing.GroupLayout.DEFAULT_SIZE, 170, Short.MAX_VALUE)
                .addGap(34, 34, 34))
        );
        jPanel12Layout.setVerticalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel13, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 35, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel12, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jPanel11, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jSeparator1)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jPanel10, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                                    .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                                    .addComponent(jPanel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jPanel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addGap(0, 22, Short.MAX_VALUE))))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                        .addGap(28, 28, 28)
                        .addComponent(btnIngresar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel1Layout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, 213, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(25, Short.MAX_VALUE)))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(67, 67, 67)
                .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jPanel10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel12, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnIngresar, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(61, 61, 61)
                .addComponent(jPanel11, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(20, 20, 20))
            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel1Layout.createSequentialGroup()
                    .addGap(114, 114, 114)
                    .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(524, Short.MAX_VALUE)))
        );

        jpControl.setBackground(new java.awt.Color(51, 0, 51));
        jpControl.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jpControl.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseDragged(java.awt.event.MouseEvent evt) {
                jpControlMouseDragged(evt);
            }
        });
        jpControl.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jpControlMousePressed(evt);
            }
        });

        bgClose.setBackground(new java.awt.Color(51, 0, 51));
        bgClose.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));

        btnClose.setBackground(new java.awt.Color(255, 255, 255));
        btnClose.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        btnClose.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Resources/close.png"))); // NOI18N
        btnClose.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        btnClose.setDisabledIcon(null);
        btnClose.setDoubleBuffered(true);
        btnClose.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnClose.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnCloseMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnCloseMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnCloseMouseExited(evt);
            }
        });

        javax.swing.GroupLayout bgCloseLayout = new javax.swing.GroupLayout(bgClose);
        bgClose.setLayout(bgCloseLayout);
        bgCloseLayout.setHorizontalGroup(
            bgCloseLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, bgCloseLayout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(btnClose, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        bgCloseLayout.setVerticalGroup(
            bgCloseLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(btnClose, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 31, Short.MAX_VALUE)
        );

        bgMinimize.setBackground(new java.awt.Color(51, 0, 51));
        bgMinimize.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        bgMinimize.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                bgMinimizeMouseClicked(evt);
            }
        });

        btnMinimize.setBackground(new java.awt.Color(255, 255, 255));
        btnMinimize.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        btnMinimize.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Resources/minimize.png"))); // NOI18N
        btnMinimize.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        btnMinimize.setDisabledIcon(null);
        btnMinimize.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnMinimizeMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnMinimizeMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnMinimizeMouseExited(evt);
            }
        });

        javax.swing.GroupLayout bgMinimizeLayout = new javax.swing.GroupLayout(bgMinimize);
        bgMinimize.setLayout(bgMinimizeLayout);
        bgMinimizeLayout.setHorizontalGroup(
            bgMinimizeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, bgMinimizeLayout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(btnMinimize, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        bgMinimizeLayout.setVerticalGroup(
            bgMinimizeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, bgMinimizeLayout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(btnMinimize, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        javax.swing.GroupLayout ipmLogoLeftLayout = new javax.swing.GroupLayout(ipmLogoLeft);
        ipmLogoLeft.setLayout(ipmLogoLeftLayout);
        ipmLogoLeftLayout.setHorizontalGroup(
            ipmLogoLeftLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 28, Short.MAX_VALUE)
        );
        ipmLogoLeftLayout.setVerticalGroup(
            ipmLogoLeftLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        jLabel9.setFont(new java.awt.Font("Arial", 0, 13)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(255, 255, 255));
        jLabel9.setText("Consult - SQL");

        javax.swing.GroupLayout jpControlLayout = new javax.swing.GroupLayout(jpControl);
        jpControl.setLayout(jpControlLayout);
        jpControlLayout.setHorizontalGroup(
            jpControlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jpControlLayout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addComponent(ipmLogoLeft, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(5, 5, 5)
                .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(bgMinimize, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(bgClose, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(8, 8, 8))
        );
        jpControlLayout.setVerticalGroup(
            jpControlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpControlLayout.createSequentialGroup()
                .addGroup(jpControlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jpControlLayout.createSequentialGroup()
                        .addGroup(jpControlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(bgClose, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(bgMinimize, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 2, Short.MAX_VALUE))
                    .addGroup(jpControlLayout.createSequentialGroup()
                        .addGap(4, 4, 4)
                        .addGroup(jpControlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(ipmLogoLeft, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel9))))
                .addGap(8, 8, 8))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, 0)
                        .addComponent(imp_main, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jpControl, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(jpControl, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(imp_main, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnCloseMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnCloseMouseClicked
        System.exit(0);
    }//GEN-LAST:event_btnCloseMouseClicked

    private void btnCloseMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnCloseMouseEntered
        bgClose.setBackground(new Color(240,10,10));
    }//GEN-LAST:event_btnCloseMouseEntered

    private void btnCloseMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnCloseMouseExited
        bgClose.setBackground(new Color(51,0,51));
    }//GEN-LAST:event_btnCloseMouseExited

    private void btnMinimizeMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnMinimizeMouseClicked
        this.setExtendedState(ICONIFIED);
    }//GEN-LAST:event_btnMinimizeMouseClicked

    private void btnMinimizeMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnMinimizeMouseEntered
        bgMinimize.setBackground(new Color(185,95,241));
    }//GEN-LAST:event_btnMinimizeMouseEntered

    private void btnMinimizeMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnMinimizeMouseExited
        bgMinimize.setBackground(new Color(51,0,51));
    }//GEN-LAST:event_btnMinimizeMouseExited

    private void bgMinimizeMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_bgMinimizeMouseClicked
        this.setExtendedState(ICONIFIED);
    }//GEN-LAST:event_bgMinimizeMouseClicked

    private void jpControlMouseDragged(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jpControlMouseDragged
        //Ecuacion de coordenadas para mover el Jframe
        this.setLocation(this.getLocation().x + evt.getX() - x, this.getLocation().y + evt.getY() - y);
    }//GEN-LAST:event_jpControlMouseDragged

    private void jpControlMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jpControlMousePressed
        x = evt.getX();
        y = evt.getY();
    }//GEN-LAST:event_jpControlMousePressed

    private void btnLimpiarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLimpiarActionPerformed
        jComboBoxFestado.setSelectedIndex(0);
        jComboBoxFprioridad.setSelectedIndex(0);
    }//GEN-LAST:event_btnLimpiarActionPerformed

    private void btnActualizarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnActualizarActionPerformed
        int fila = tbTablaConsulta.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "Primero Seleccine una fila");
        } else {

            ActualizarTarea Update = new ActualizarTarea(this, rootPaneCheckingEnabled);
            ActualizarTarea.jTextField1.setText(tbTablaConsulta.getValueAt(fila, 5).toString());
            ActualizarTarea.jTextField2.setText(tbTablaConsulta.getValueAt(fila, 8).toString());
            ActualizarTarea.jTextArea1.setText(tbTablaConsulta.getValueAt(fila, 9).toString());
            ActualizarTarea.jTextFieldTarea.setText(tbTablaConsulta.getValueAt(fila, 3).toString());
            ActualizarTarea.jDateChooser.setDate((Date) tbTablaConsulta.getValueAt(fila, 10));
            ActualizarTarea.txtAdjuntoName.setText((String) tbTablaConsulta.getValueAt(fila, 11));
            ActualizarTarea.jLabel3.setText(tbTablaConsulta.getValueAt(fila, 0).toString());
            ActualizarTarea.ComboBoxProyecto.setSelectedItem(tbTablaConsulta.getValueAt(fila, 2));
            Update.setVisible(true);
        }
    }//GEN-LAST:event_btnActualizarActionPerformed

    private void btnRecargarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRecargarActionPerformed
        LoadData("dbo.tareas_emp","dbo.tipo_actividad","dbo.producto","dbo.etapa_tarea","dbo.estado_tarea","dbo.prioridad_tarea");
        jComboBoxFestado.setSelectedIndex(0);
        jComboBoxFprioridad.setSelectedIndex(0);
    }//GEN-LAST:event_btnRecargarActionPerformed

    private void btnBuscarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBuscarActionPerformed
        FilterDB("dbo.tareas_emp","dbo.tipo_actividad","dbo.producto","dbo.etapa_tarea","dbo.estado_tarea","dbo.prioridad_tarea","prioridad_tarea.prioridad", filterPrioridad);
    }//GEN-LAST:event_btnBuscarActionPerformed

    private void btnBuscar1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBuscar1ActionPerformed

        FilterDB("dbo.tareas_emp","dbo.tipo_actividad","dbo.producto","dbo.etapa_tarea","dbo.estado_tarea","dbo.prioridad_tarea","estado_tarea.estado", filterEstado);
    }//GEN-LAST:event_btnBuscar1ActionPerformed

    private void jLabel10MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel10MouseClicked
        RegisterUser NewUser = new RegisterUser(this, rootPaneCheckingEnabled);
        NewUser.setVisible(true);
    }//GEN-LAST:event_jLabel10MouseClicked

    private void btnIngresarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnIngresarActionPerformed
         RegisterTarea registerTarea = new RegisterTarea(this, rootPaneCheckingEnabled);
         registerTarea.setVisible(true);
    }//GEN-LAST:event_btnIngresarActionPerformed

    private void btnIngresarKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_btnIngresarKeyPressed

    }//GEN-LAST:event_btnIngresarKeyPressed

    private void jLabel12MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel12MouseClicked
        Login login = new Login();
        this.dispose();
        login.setVisible(true);
    }//GEN-LAST:event_jLabel12MouseClicked

    private void jLabel13MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel13MouseClicked
        
    }//GEN-LAST:event_jLabel13MouseClicked

    private void jLabel7MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel7MouseClicked
        MainForm mainForm = new MainForm(rol);
        mainForm.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_jLabel7MouseClicked

    private void jComboBoxFprioridadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBoxFprioridadActionPerformed
        filterPrioridad = jComboBoxFprioridad.getSelectedItem().toString();
        
    }//GEN-LAST:event_jComboBoxFprioridadActionPerformed

    private void jComboBoxFestadoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBoxFestadoActionPerformed
        filterEstado = jComboBoxFestado.getSelectedItem().toString();              
    }//GEN-LAST:event_jComboBoxFestadoActionPerformed

    private void btnEliminarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEliminarActionPerformed
        int fila = tbTablaConsulta.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "Primero Seleccine una fila");
        } else {
            int i = JOptionPane.showConfirmDialog(this, "Seguro de eliminar estos datos?", "Confirmar Eliminar", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
            if(i==0){
                Delete("dbo.tareas_emp", tbTablaConsulta.getValueAt(fila, 0).toString());
            }
        }
    }//GEN-LAST:event_btnEliminarActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(ListarTareas.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ListarTareas.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ListarTareas.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ListarTareas.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> {
            new ListarTareas(rol).setVisible(true);
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel bgClose;
    private javax.swing.JPanel bgMinimize;
    private javax.swing.JButton btnActualizar;
    private javax.swing.JButton btnBuscar;
    private javax.swing.JButton btnBuscar1;
    private javax.swing.JLabel btnClose;
    private javax.swing.JButton btnEliminar;
    private javax.swing.JButton btnIngresar;
    private javax.swing.JButton btnLimpiar;
    private javax.swing.JLabel btnMinimize;
    private javax.swing.JButton btnRecargar;
    private gui.ImagePanel impBusquedaEmpresa;
    private gui.ImagePanel impLista;
    private gui.ImagePanel imp_main;
    private gui.ImagePanel ipmLogoLeft;
    private javax.swing.JComboBox<String> jComboBoxFestado;
    private javax.swing.JComboBox<String> jComboBoxFprioridad;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JPanel jpControl;
    private javax.swing.JScrollPane scTable;
    private javax.swing.JTable tbTablaConsulta;
    private javax.swing.JLabel title;
    // End of variables declaration//GEN-END:variables

}
