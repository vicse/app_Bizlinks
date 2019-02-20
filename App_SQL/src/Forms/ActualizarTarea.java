package Forms;

import Classes.Conexion;
import Classes.Funciones;
import java.awt.Color;
import com.sun.awt.AWTUtilities;
import java.awt.Shape;
import java.awt.geom.RoundRectangle2D;
import java.io.File;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.sql.Date;
import java.text.SimpleDateFormat;

public class ActualizarTarea extends javax.swing.JDialog {
    
    private static String dato1;
    private static String dato2;
    private static String dato3;
    Date sqlDate;     
    String periodoDe;
    Funciones funciones = new Funciones();
    Conexion con = new Conexion();
    String solicitado,respon,descrip;
    //Variable donde se guardará el archivo adjunto
    private int indexProyecto,indexProducto,indexEtapa,indexEstado,indexPrioridad,indexTipoDia,indexActAscCli;
    String filename;

    public static String getDato1() {
        return dato1;
    }
    
    public static void setDato1(String Dato1) {
        dato1 = Dato1;
    }

    public static String getDato2() {
        return dato2;
    }

    public static void setDato2(String Dato2) {
        dato2 = Dato2;
    }
    
    public static String getDato3() {
        return dato3;
    }

    public static void setDato3(String Dato3) {
        dato3 = Dato3;
    }

    public ActualizarTarea(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        FillComboTipoActividad();
        FillComboProducto();
        FillComboEtapa();
        FillComboPrioridad();
        FillComboEstado();
        FillComboTipoDiaTarea();
        FillComboActAscCli();
        //btnRegistrarTarea.setEnabled(false);
        this.setLocationRelativeTo(parent);
        ipmLogoLeft.setImage(new ImageIcon(getClass().getResource("/Resources/icon_250.png")).getImage());
        impRegister.setImage(new ImageIcon(getClass().getResource("/Resources/fond_register.jpg")).getImage());
        Shape redondear = new RoundRectangle2D.Double(0, 0, this.getBounds().width, this.getBounds().height, 6, 6);
        AWTUtilities.setWindowShape(this, redondear);
        //btnRegistrarTarea.setEnabled(false);
        
        
        
    }
    
     public void UpdateDB(String table, String a,Date b,String d,String e,int g,int h,int i,int j,int k,int l,int m,String n,String o,String p,String q,int ID){
        try {
            PreparedStatement ps = con.getConnection().prepareStatement("UPDATE "+table+
                    " SET detalle_actividad='" + a + "',"
                    + "fecha_entrega='" + b + "',"
                    + "responsable='" + d +"',"
                    + "solicitado='" + e +"',"
                    + "nombre_etapa='" + g +"',"
                    + "nombre_tipo_actividad='" + h + "',"
                    + "nombre_prioridad='" + i +"',"
                    + "nombre_producto='" + j + "',"
                    + "nombre_estado='" + k + "',"
                    + "nombre_tipo_dia='" + l + "',"
                    + "nombre_act_asoc_cliente='" + m + "',"
                    + "razon_social_cliente='" + n + "',"
                    + "detalle_nuevo_producto='" + o + "',"
                    + "numero_horas='" + p + "',"
                    + "periodo_declarar='" + q + "' WHERE id_tarea='" + ID +"'");
            
                    
            ps.execute();
            JOptionPane.showMessageDialog(this, "Valores Actualizados Exitosamente", "Proceso Culminado", JOptionPane.INFORMATION_MESSAGE);
            this.dispose();
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
            JOptionPane.showMessageDialog(this, "Error de Actualizacion: "+ex.getMessage());
        }
    }
     
     public String CapturarFecha(Date fecha){
        
        try{
            SimpleDateFormat dateFormat = new SimpleDateFormat("MM-yyyy");
            periodoDe = dateFormat.format(fecha);
            
            return periodoDe;
        
        }catch(NullPointerException e){
            System.out.println(e);
            return "Error: "+e;
        }
       
    }
    
    //Cargar los datos en el combo box de la base de datos Tabla Proyecto
     
    private void FillComboTipoDiaTarea(){
        try{
            PreparedStatement ps = con.getConnection().prepareStatement("SELECT * FROM dbo.tipo_dia_tarea");
            ResultSet r = ps.executeQuery();
            
            while(r.next()){
                String tipoDia = r.getString("tipo_dia");
                jComboBox1.addItem(tipoDia);
            }
            
        }catch(SQLException e){
            JOptionPane.showMessageDialog(null, e);
        }
    }
    
    private void FillComboTipoActividad(){
        try{
            PreparedStatement ps = con.getConnection().prepareStatement("SELECT * FROM dbo.tipo_actividad");
            ResultSet r = ps.executeQuery();
            
            while(r.next()){
                String tipoActividad = r.getString("tipo_actividad");
                ComboBoxTipo_Act.addItem(tipoActividad);
            }
            
        }catch(SQLException e){
            JOptionPane.showMessageDialog(null, e);
        }
    }
    
    private void FillComboActAscCli(){
        try{
            PreparedStatement ps = con.getConnection().prepareStatement("SELECT * FROM dbo.act_asoc_cliente");
            ResultSet r = ps.executeQuery();
            
            while(r.next()){
                String actAscCli= r.getString("act_asc_cliente");
                jComboBox2.addItem(actAscCli);               
            }            
        }catch(SQLException e){
           JOptionPane.showMessageDialog(null, e);
        }
    }
    
    private void FillComboProducto(){
        try{
            PreparedStatement ps = con.getConnection().prepareStatement("SELECT * FROM dbo.producto");
            ResultSet r = ps.executeQuery();
            
            while(r.next()){
                String producto = r.getString("producto");
                ComboBoxProducto.addItem(producto);
            }
        }catch(SQLException e){
            JOptionPane.showMessageDialog(null, e);
        }
    }
    
    private void FillComboEtapa(){
        try{
            PreparedStatement ps = con.getConnection().prepareStatement("SELECT * FROM dbo.etapa_tarea");
            ResultSet r = ps.executeQuery();
            
            while(r.next()){
                String etapa = r.getString("etapa");
                ComboBoxEtapa.addItem(etapa);
            }
        }catch(SQLException e){
            JOptionPane.showMessageDialog(null, e);
        }
    }
    
    private void FillComboPrioridad(){
        try{
            PreparedStatement ps = con.getConnection().prepareStatement("SELECT * FROM dbo.prioridad_tarea");
            ResultSet r = ps.executeQuery();
            
            while(r.next()){
                String prioridad = r.getString("prioridad");
                ComboBoxPrioridad.addItem(prioridad);
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
                ComboBoxEstado.addItem(estado);
            }         
        }catch(SQLException e){
            JOptionPane.showMessageDialog(null, e);
        }
    }
    
    int x,y;
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jpControl = new javax.swing.JPanel();
        bgClose = new javax.swing.JPanel();
        btnClose = new javax.swing.JLabel();
        ipmLogoLeft = new gui.ImagePanel();
        jLabel9 = new javax.swing.JLabel();
        jLabelIdTarea = new javax.swing.JLabel();
        impRegister = new gui.ImagePanel();
        jLabel2 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        ComboBoxTipo_Act = new javax.swing.JComboBox<>();
        ComboBoxProducto = new javax.swing.JComboBox<>();
        jLabel10 = new javax.swing.JLabel();
        jTextFieldDetaNvoPro = new javax.swing.JTextField();
        jLabel12 = new javax.swing.JLabel();
        ComboBoxEtapa = new javax.swing.JComboBox<>();
        jLabel13 = new javax.swing.JLabel();
        ComboBoxPrioridad = new javax.swing.JComboBox<>();
        jLabel14 = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        jLabel16 = new javax.swing.JLabel();
        ComboBoxEstado = new javax.swing.JComboBox<>();
        jDateChooser = new com.toedter.calendar.JDateChooser();
        jLabel15 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        jTextFieldRes = new javax.swing.JTextField();
        jTextFieldSol = new javax.swing.JTextField();
        jLabel18 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jTextFieldDetalleAct = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jComboBox1 = new javax.swing.JComboBox<>();
        jLabel5 = new javax.swing.JLabel();
        jComboBox2 = new javax.swing.JComboBox<>();
        jLabel19 = new javax.swing.JLabel();
        jTextFieldNroHoras = new javax.swing.JTextField();
        jLabel20 = new javax.swing.JLabel();
        jTextFieldRaSocialCli = new javax.swing.JTextField();

        jLabel1.setText("jLabel1");

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setUndecorated(true);
        setResizable(false);

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
        jLabel9.setText("Actualizar Tarea");

        jLabelIdTarea.setForeground(new java.awt.Color(255, 255, 255));
        jLabelIdTarea.setText("ID tarea");

        javax.swing.GroupLayout jpControlLayout = new javax.swing.GroupLayout(jpControl);
        jpControl.setLayout(jpControlLayout);
        jpControlLayout.setHorizontalGroup(
            jpControlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jpControlLayout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addComponent(ipmLogoLeft, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(5, 5, 5)
                .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabelIdTarea)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 300, Short.MAX_VALUE)
                .addComponent(bgClose, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(8, 8, 8))
        );
        jpControlLayout.setVerticalGroup(
            jpControlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpControlLayout.createSequentialGroup()
                .addGroup(jpControlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jpControlLayout.createSequentialGroup()
                        .addComponent(bgClose, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 2, Short.MAX_VALUE))
                    .addGroup(jpControlLayout.createSequentialGroup()
                        .addGap(8, 8, 8)
                        .addGroup(jpControlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(ipmLogoLeft, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(jpControlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabelIdTarea)))))
                .addGap(8, 8, 8))
        );

        impRegister.setForeground(new java.awt.Color(255, 255, 255));

        jLabel2.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("Tipo de día");

        jLabel11.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel11.setForeground(new java.awt.Color(255, 255, 255));
        jLabel11.setText("Tipo de Actividad *");

        ComboBoxTipo_Act.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "--Seleccionar Tipo de Act.--" }));
        ComboBoxTipo_Act.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ComboBoxTipo_ActActionPerformed(evt);
            }
        });

        ComboBoxProducto.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "--Seleccionar Producto--" }));
        ComboBoxProducto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ComboBoxProductoActionPerformed(evt);
            }
        });

        jLabel10.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel10.setForeground(new java.awt.Color(255, 255, 255));
        jLabel10.setText("Detalle de la actividad *");

        jLabel12.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel12.setForeground(new java.awt.Color(255, 255, 255));
        jLabel12.setText("Etapa *");

        ComboBoxEtapa.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "--Seleccionar Etapa--" }));
        ComboBoxEtapa.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ComboBoxEtapaActionPerformed(evt);
            }
        });

        jLabel13.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel13.setForeground(new java.awt.Color(255, 255, 255));
        jLabel13.setText("Prioridad *");

        ComboBoxPrioridad.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "--Seleccionar Prioridad--" }));
        ComboBoxPrioridad.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ComboBoxPrioridadActionPerformed(evt);
            }
        });

        jLabel14.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel14.setForeground(new java.awt.Color(255, 255, 255));
        jLabel14.setText("Fecha de entrega *");

        jLabel16.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel16.setForeground(new java.awt.Color(255, 255, 255));
        jLabel16.setText("Estado *");

        ComboBoxEstado.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "--Seleccionar Estado--" }));
        ComboBoxEstado.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ComboBoxEstadoActionPerformed(evt);
            }
        });

        jLabel15.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel15.setForeground(new java.awt.Color(255, 255, 255));
        jLabel15.setText("Responsable *");

        jLabel17.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel17.setForeground(new java.awt.Color(255, 255, 255));
        jLabel17.setText("Solicitado por *");

        jTextFieldRes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextFieldResActionPerformed(evt);
            }
        });

        jLabel18.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel18.setForeground(new java.awt.Color(255, 255, 255));
        jLabel18.setText("Detalle Nvo Producto *");

        jButton1.setBackground(new java.awt.Color(255, 255, 255));
        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Resources/save.png"))); // NOI18N
        jButton1.setText("Actualizar");
        jButton1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton4.setBackground(new java.awt.Color(255, 255, 255));
        jButton4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Resources/cancel.png"))); // NOI18N
        jButton4.setText("Cancelar");
        jButton4.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        jLabel4.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setText("Producto *");

        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "--Seleccionar tipo de dia--" }));
        jComboBox1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox1ActionPerformed(evt);
            }
        });

        jLabel5.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(255, 255, 255));
        jLabel5.setText("Act. Asoc. A Cliente");

        jComboBox2.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "--Seleccionar act asc a cli--" }));
        jComboBox2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox2ActionPerformed(evt);
            }
        });

        jLabel19.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel19.setForeground(new java.awt.Color(255, 255, 255));
        jLabel19.setText("Nro de Horas");

        jLabel20.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel20.setForeground(new java.awt.Color(255, 255, 255));
        jLabel20.setText("Razón Social del Cliente");

        javax.swing.GroupLayout impRegisterLayout = new javax.swing.GroupLayout(impRegister);
        impRegister.setLayout(impRegisterLayout);
        impRegisterLayout.setHorizontalGroup(
            impRegisterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(impRegisterLayout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addGroup(impRegisterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(impRegisterLayout.createSequentialGroup()
                        .addComponent(jTextFieldDetalleAct)
                        .addContainerGap())
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, impRegisterLayout.createSequentialGroup()
                        .addGroup(impRegisterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(impRegisterLayout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 127, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 127, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, impRegisterLayout.createSequentialGroup()
                                .addComponent(jLabel10)
                                .addGap(0, 0, Short.MAX_VALUE)))
                        .addGap(22, 22, 22))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, impRegisterLayout.createSequentialGroup()
                        .addGroup(impRegisterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jSeparator1, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jTextFieldDetaNvoPro))
                        .addContainerGap())
                    .addGroup(impRegisterLayout.createSequentialGroup()
                        .addGroup(impRegisterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel18)
                            .addGroup(impRegisterLayout.createSequentialGroup()
                                .addGroup(impRegisterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(ComboBoxEstado, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel16, javax.swing.GroupLayout.PREFERRED_SIZE, 164, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(ComboBoxEtapa, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(143, 143, 143)
                                .addGroup(impRegisterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jTextFieldRes)
                                    .addGroup(impRegisterLayout.createSequentialGroup()
                                        .addGroup(impRegisterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(ComboBoxPrioridad, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(jLabel13)
                                            .addComponent(jDateChooser, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(jLabel14, javax.swing.GroupLayout.PREFERRED_SIZE, 164, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(jLabel15))
                                        .addGap(0, 0, Short.MAX_VALUE)))))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(impRegisterLayout.createSequentialGroup()
                        .addGroup(impRegisterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(impRegisterLayout.createSequentialGroup()
                                .addGroup(impRegisterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel2)
                                    .addComponent(jLabel12)
                                    .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel5))
                                .addGap(140, 140, 140)
                                .addGroup(impRegisterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jLabel19)
                                    .addComponent(ComboBoxTipo_Act, javax.swing.GroupLayout.PREFERRED_SIZE, 179, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel11)
                                    .addComponent(jTextFieldNroHoras, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel20)
                                    .addComponent(jTextFieldRaSocialCli)))
                            .addComponent(ComboBoxProducto, javax.swing.GroupLayout.PREFERRED_SIZE, 179, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel4)
                            .addComponent(jComboBox2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel17)
                            .addComponent(jTextFieldSol, javax.swing.GroupLayout.PREFERRED_SIZE, 156, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );
        impRegisterLayout.setVerticalGroup(
            impRegisterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(impRegisterLayout.createSequentialGroup()
                .addGap(9, 9, 9)
                .addGroup(impRegisterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel11)
                    .addComponent(jLabel2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(impRegisterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(ComboBoxTipo_Act, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(impRegisterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(jLabel19))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(impRegisterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jComboBox2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextFieldNroHoras, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(impRegisterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(jLabel20))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(impRegisterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(ComboBoxProducto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextFieldRaSocialCli, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(33, 33, 33)
                .addComponent(jLabel18)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jTextFieldDetaNvoPro, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(12, 12, 12)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel10)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTextFieldDetalleAct, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(24, 24, 24)
                .addGroup(impRegisterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel12)
                    .addComponent(jLabel13))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(impRegisterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(ComboBoxPrioridad, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(ComboBoxEtapa, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(impRegisterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel14, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel16))
                .addGap(18, 18, 18)
                .addGroup(impRegisterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(ComboBoxEstado, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jDateChooser, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(impRegisterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel17)
                    .addComponent(jLabel15))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(impRegisterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextFieldSol, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextFieldRes, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(49, 49, 49)
                .addGroup(impRegisterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(152, 152, 152))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jpControl, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(impRegister, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jpControl, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(impRegister, javax.swing.GroupLayout.PREFERRED_SIZE, 721, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnCloseMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnCloseMouseClicked
        this.dispose();
    }//GEN-LAST:event_btnCloseMouseClicked

    private void btnCloseMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnCloseMouseEntered
        bgClose.setBackground(new Color(240,10,10));
    }//GEN-LAST:event_btnCloseMouseEntered

    private void btnCloseMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnCloseMouseExited
        bgClose.setBackground(new Color(51,0,51));
    }//GEN-LAST:event_btnCloseMouseExited

    private void jpControlMouseDragged(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jpControlMouseDragged
        this.setLocation(this.getLocation().x + evt.getX() - x, this.getLocation().y + evt.getY() - y);
    }//GEN-LAST:event_jpControlMouseDragged

    private void jpControlMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jpControlMousePressed
        x = evt.getX();
        y = evt.getY();
    }//GEN-LAST:event_jpControlMousePressed

    //Evento que se lanza al seleccionar un Item del combobox
    private void ComboBoxTipo_ActActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ComboBoxTipo_ActActionPerformed
        indexProyecto = ComboBoxTipo_Act.getSelectedIndex();        
    }//GEN-LAST:event_ComboBoxTipo_ActActionPerformed

   //Capturamos los indices de los comboBox y los guardamos en variables
    
    private void ComboBoxProductoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ComboBoxProductoActionPerformed
        indexProducto= ComboBoxProducto.getSelectedIndex();
        
        if(indexProducto == 4){
            jLabel18.setVisible(true);
            jTextFieldDetaNvoPro.setVisible(true);
        }else{
            jLabel18.setVisible(false);
            jTextFieldDetaNvoPro.setVisible(false);
        }
        
    }//GEN-LAST:event_ComboBoxProductoActionPerformed

    private void ComboBoxEtapaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ComboBoxEtapaActionPerformed
        indexEtapa = ComboBoxEtapa.getSelectedIndex();
    }//GEN-LAST:event_ComboBoxEtapaActionPerformed

    private void ComboBoxEstadoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ComboBoxEstadoActionPerformed
        indexEstado = ComboBoxEstado.getSelectedIndex();
    }//GEN-LAST:event_ComboBoxEstadoActionPerformed

    private void ComboBoxPrioridadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ComboBoxPrioridadActionPerformed
        indexPrioridad = ComboBoxPrioridad.getSelectedIndex();
    }//GEN-LAST:event_ComboBoxPrioridadActionPerformed

    private void jTextFieldResActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextFieldResActionPerformed

    }//GEN-LAST:event_jTextFieldResActionPerformed
    
    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        int i = JOptionPane.showConfirmDialog(this, "Seguro de actualizar estos datos?", "Confirmar Actualizacion", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        if(i==0){
            sqlDate = new Date(jDateChooser.getDate().getTime());
            
            if(indexActAscCli==0 || indexEstado==0 || indexEtapa==0 || indexPrioridad==0 || indexProducto==0 || indexProyecto==0 || indexTipoDia==0){
                JOptionPane.showMessageDialog(null, "No puede dejar Campos Vacios \n Verifique e intente de Nuevo ");
            }else{
                UpdateDB("dbo.tareas_emp",jTextFieldDetalleAct.getText(),sqlDate, jTextFieldRes.getText(),jTextFieldSol.getText(),indexEtapa, indexProyecto, indexPrioridad, indexProducto, indexEstado,indexTipoDia,indexActAscCli, jTextFieldRaSocialCli.getText(), jTextFieldDetaNvoPro.getText(),jTextFieldNroHoras.getText(), CapturarFecha(sqlDate), Integer.parseInt(jLabelIdTarea.getText()));
            }
        }

    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        this.dispose();
    }//GEN-LAST:event_jButton4ActionPerformed

    private void jComboBox1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox1ActionPerformed
        indexTipoDia = jComboBox1.getSelectedIndex();
        
    }//GEN-LAST:event_jComboBox1ActionPerformed

    private void jComboBox2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox2ActionPerformed
        indexActAscCli = jComboBox2.getSelectedIndex();
        
        if(indexActAscCli == 2){
            jLabel20.setVisible(false);
            jTextFieldRaSocialCli.setVisible(false);
        }else{
            jLabel20.setVisible(true);
            jTextFieldRaSocialCli.setVisible(true);
        }
    }//GEN-LAST:event_jComboBox2ActionPerformed

    
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
            java.util.logging.Logger.getLogger(ActualizarTarea.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ActualizarTarea.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ActualizarTarea.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ActualizarTarea.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() { 
                ActualizarTarea dialog = new ActualizarTarea(new javax.swing.JFrame(), true);
                dialog.addWindowListener(new java.awt.event.WindowAdapter() {
                    @Override
                    public void windowClosing(java.awt.event.WindowEvent e) {
                        System.exit(0);
                    }
                });
                dialog.setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox<String> ComboBoxEstado;
    private javax.swing.JComboBox<String> ComboBoxEtapa;
    private javax.swing.JComboBox<String> ComboBoxPrioridad;
    private javax.swing.JComboBox<String> ComboBoxProducto;
    public static javax.swing.JComboBox<String> ComboBoxTipo_Act;
    private javax.swing.JPanel bgClose;
    private javax.swing.JLabel btnClose;
    private gui.ImagePanel impRegister;
    private gui.ImagePanel ipmLogoLeft;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton4;
    private javax.swing.JComboBox<String> jComboBox1;
    private javax.swing.JComboBox<String> jComboBox2;
    public static com.toedter.calendar.JDateChooser jDateChooser;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel9;
    public static javax.swing.JLabel jLabelIdTarea;
    private javax.swing.JSeparator jSeparator1;
    public static javax.swing.JTextField jTextFieldDetaNvoPro;
    public static javax.swing.JTextField jTextFieldDetalleAct;
    public static javax.swing.JTextField jTextFieldNroHoras;
    public static javax.swing.JTextField jTextFieldRaSocialCli;
    public static javax.swing.JTextField jTextFieldRes;
    public static javax.swing.JTextField jTextFieldSol;
    private javax.swing.JPanel jpControl;
    // End of variables declaration//GEN-END:variables
}
