package Forms;

import Classes.Conexion;
import Classes.Funciones;
import java.awt.Color;
import com.sun.awt.AWTUtilities;
import java.awt.HeadlessException;
import java.awt.Shape;
import java.awt.geom.RoundRectangle2D;
import java.io.File;
import java.io.FileInputStream;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.sql.Date;

public class RegisterTarea extends javax.swing.JDialog {
    
    private static String dato1;
    private static String dato2;
    private static String dato3;
    Date sqlDate;     
    
    Funciones funciones = new Funciones();
    Conexion con = new Conexion();
    String solicitado,respon,descrip;
    //Variable donde se guardará el archivo adjunto
    private int indexProyecto,indexProducto,indexEtapa,indexEstado,indexPrioridad;
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

    public RegisterTarea(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        FillComboProyecto();
        FillComboProducto();
        FillComboEtapa();
        FillComboPrioridad();
        FillComboEstado();
        //btnRegistrarTarea.setEnabled(false);
        this.setLocationRelativeTo(parent);
        ipmLogoLeft.setImage(new ImageIcon(getClass().getResource("/Resources/icon_250.png")).getImage());
        impRegister.setImage(new ImageIcon(getClass().getResource("/Resources/fond_register.jpg")).getImage());
        Shape redondear = new RoundRectangle2D.Double(0, 0, this.getBounds().width, this.getBounds().height, 6, 6);
        AWTUtilities.setWindowShape(this, redondear);
        //btnRegistrarTarea.setEnabled(false);
        
        
        
    }
   
    
    
    public void registroTarea(){
        
        try{
            //Verificamos si la infoAdicional esta vacia y realizamos el registro con los campos en null
            
            if("".equals(getDato1()) || "".equals(getDato2()) || "".equals(getDato3())){
                PreparedStatement ps = con.getConnection().prepareStatement("INSERT INTO dbo.tareas_emp (tarea,fecha_entrega,adjunto,responsable,solicitado,descripcion,nombre_etapa,nombre_proyecto,nombre_prioridad,nombre_producto,nombre_estado) VALUES (?,?,?,?,?,?,?,?,?,?,?)");
            
                sqlDate = new Date(jDateChooser.getDate().getTime());

                ps.setString(1, jTextFieldTarea.getText());
                ps.setDate(2, sqlDate);
                ps.setString(3, filename);
                ps.setString(4, null);
                ps.setString(5,null);
                ps.setString(6,null);
                ps.setInt(7, indexEtapa);
                ps.setInt(8, indexProyecto);
                ps.setInt(9, indexPrioridad);
                ps.setInt(10, indexProducto);
                ps.setInt(11, indexEstado);

                ps.execute();
                JOptionPane.showMessageDialog(null, "Tarea registrada con éxito");
            }else{
                PreparedStatement ps = con.getConnection().prepareStatement("INSERT INTO dbo.tareas_emp (tarea,fecha_entrega,adjunto,responsable,solicitado,descripcion,nombre_etapa,nombre_proyecto,nombre_prioridad,nombre_producto,nombre_estado) VALUES (?,?,?,?,?,?,?,?,?,?,?)");
            
                sqlDate = new Date(jDateChooser.getDate().getTime());

                ps.setString(1, jTextFieldTarea.getText());
                ps.setDate(2, sqlDate);
                ps.setString(3, filename);
                ps.setString(4, getDato1());
                ps.setString(5, getDato2());
                ps.setString(6,getDato3());
                ps.setInt(7, indexEtapa);
                ps.setInt(8, indexProyecto);
                ps.setInt(9, indexPrioridad);
                ps.setInt(10, indexProducto);
                ps.setInt(11, indexEstado);

                ps.execute();
                JOptionPane.showMessageDialog(null, "Tarea registrada con éxito");
            }
            
            
        }catch(HeadlessException | SQLException e){
            System.out.println("ERROR EN LA INSERCION: "+e);
        }
    }
      
    
    //Cargar los datos en el combo box de la base de datos Tabla Proyecto
    
    private void FillComboProyecto(){
        try{
            PreparedStatement ps = con.getConnection().prepareStatement("SELECT * FROM dbo.proyecto");
            ResultSet r = ps.executeQuery();
            
            while(r.next()){
                String proyecto= r.getString("proyecto");
                ComboBoxProyecto.addItem(proyecto);               
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

        jpControl = new javax.swing.JPanel();
        bgClose = new javax.swing.JPanel();
        btnClose = new javax.swing.JLabel();
        ipmLogoLeft = new gui.ImagePanel();
        jLabel9 = new javax.swing.JLabel();
        impRegister = new gui.ImagePanel();
        jLabel2 = new javax.swing.JLabel();
        btnRegistrarTarea = new javax.swing.JButton();
        jLabel11 = new javax.swing.JLabel();
        ComboBoxProyecto = new javax.swing.JComboBox<>();
        ComboBoxProducto = new javax.swing.JComboBox<>();
        jLabel10 = new javax.swing.JLabel();
        jTextFieldTarea = new javax.swing.JTextField();
        jLabel12 = new javax.swing.JLabel();
        ComboBoxEtapa = new javax.swing.JComboBox<>();
        jLabel13 = new javax.swing.JLabel();
        ComboBoxPrioridad = new javax.swing.JComboBox<>();
        jLabel14 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jCheckBox1 = new javax.swing.JCheckBox();
        jButton3 = new javax.swing.JButton();
        jSeparator1 = new javax.swing.JSeparator();
        jLabel16 = new javax.swing.JLabel();
        ComboBoxEstado = new javax.swing.JComboBox<>();
        jDateChooser = new com.toedter.calendar.JDateChooser();
        txtAdjuntoName = new javax.swing.JLabel();
        jLabelSol = new javax.swing.JLabel();
        jLabelRes = new javax.swing.JLabel();
        jButton4 = new javax.swing.JButton();

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
        jLabel9.setText("Crear Tarea");

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
                            .addComponent(jLabel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addGap(8, 8, 8))
        );

        impRegister.setForeground(new java.awt.Color(255, 255, 255));

        jLabel2.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("Producto *");

        btnRegistrarTarea.setText("Crear");
        btnRegistrarTarea.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        btnRegistrarTarea.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRegistrarTareaActionPerformed(evt);
            }
        });

        jLabel11.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel11.setForeground(new java.awt.Color(255, 255, 255));
        jLabel11.setText("Proyecto *");

        ComboBoxProyecto.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Seleccionar Proyecto" }));
        ComboBoxProyecto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ComboBoxProyectoActionPerformed(evt);
            }
        });

        ComboBoxProducto.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Seleccionar Producto" }));
        ComboBoxProducto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ComboBoxProductoActionPerformed(evt);
            }
        });

        jLabel10.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel10.setForeground(new java.awt.Color(255, 255, 255));
        jLabel10.setText("Nombre de la tarea *");

        jLabel12.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel12.setForeground(new java.awt.Color(255, 255, 255));
        jLabel12.setText("Etapa *");

        ComboBoxEtapa.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Seleccionar Etapa" }));
        ComboBoxEtapa.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ComboBoxEtapaActionPerformed(evt);
            }
        });

        jLabel13.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel13.setForeground(new java.awt.Color(255, 255, 255));
        jLabel13.setText("Prioridad *");

        ComboBoxPrioridad.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Seleccionar Prioridad" }));
        ComboBoxPrioridad.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ComboBoxPrioridadActionPerformed(evt);
            }
        });

        jLabel14.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel14.setForeground(new java.awt.Color(255, 255, 255));
        jLabel14.setText("Fecha de entrega *");

        jButton1.setText("Info. Adicional");
        jButton1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jButton1MouseClicked(evt);
            }
        });
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setText("Subir archivo adjunto");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jCheckBox1.setText("Crear otra");

        jButton3.setText("Cancelar");
        jButton3.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jButton3MouseClicked(evt);
            }
        });

        jLabel16.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel16.setForeground(new java.awt.Color(255, 255, 255));
        jLabel16.setText("Estado *");

        ComboBoxEstado.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Seleccionar Estado" }));
        ComboBoxEstado.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ComboBoxEstadoActionPerformed(evt);
            }
        });

        txtAdjuntoName.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        txtAdjuntoName.setForeground(new java.awt.Color(255, 255, 255));
        txtAdjuntoName.setText("Archivo adjunto");

        jLabelSol.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabelSol.setForeground(new java.awt.Color(255, 255, 255));
        jLabelSol.setText("Solicitado por");

        jLabelRes.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabelRes.setForeground(new java.awt.Color(255, 255, 255));
        jLabelRes.setText("Responsable");

        jButton4.setText("Ver");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout impRegisterLayout = new javax.swing.GroupLayout(impRegister);
        impRegister.setLayout(impRegisterLayout);
        impRegisterLayout.setHorizontalGroup(
            impRegisterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(impRegisterLayout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addGroup(impRegisterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(impRegisterLayout.createSequentialGroup()
                        .addGroup(impRegisterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel12)
                            .addComponent(ComboBoxEtapa, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel14, javax.swing.GroupLayout.PREFERRED_SIZE, 164, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel16, javax.swing.GroupLayout.PREFERRED_SIZE, 164, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(ComboBoxEstado, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jDateChooser, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(impRegisterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(impRegisterLayout.createSequentialGroup()
                                .addGap(143, 143, 143)
                                .addGroup(impRegisterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(ComboBoxPrioridad, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel13)
                                    .addGroup(impRegisterLayout.createSequentialGroup()
                                        .addComponent(jButton1)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jButton4))))
                            .addGroup(impRegisterLayout.createSequentialGroup()
                                .addGap(154, 154, 154)
                                .addGroup(impRegisterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabelSol)
                                    .addComponent(jLabelRes))))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(impRegisterLayout.createSequentialGroup()
                        .addComponent(txtAdjuntoName)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, impRegisterLayout.createSequentialGroup()
                        .addGroup(impRegisterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jButton2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jSeparator1, javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(impRegisterLayout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 249, Short.MAX_VALUE)
                                .addComponent(jCheckBox1)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(btnRegistrarTarea)
                                .addGap(18, 18, 18)
                                .addComponent(jButton3))
                            .addComponent(jTextFieldTarea, javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, impRegisterLayout.createSequentialGroup()
                                .addGroup(impRegisterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel11)
                                    .addComponent(jLabel2)
                                    .addComponent(jLabel10)
                                    .addGroup(impRegisterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                        .addComponent(ComboBoxProyecto, javax.swing.GroupLayout.Alignment.LEADING, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(ComboBoxProducto, javax.swing.GroupLayout.Alignment.LEADING, 0, 179, Short.MAX_VALUE)))
                                .addGap(0, 0, Short.MAX_VALUE)))
                        .addGap(22, 22, 22))))
        );
        impRegisterLayout.setVerticalGroup(
            impRegisterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(impRegisterLayout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addComponent(jLabel11)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(ComboBoxProyecto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(38, 38, 38)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(ComboBoxProducto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(2, 2, 2)
                .addComponent(jLabel10)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jTextFieldTarea, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(impRegisterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel12)
                    .addComponent(jLabel13))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(impRegisterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(ComboBoxEtapa, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(ComboBoxPrioridad, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jLabel16)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(impRegisterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(impRegisterLayout.createSequentialGroup()
                        .addComponent(ComboBoxEstado, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED, 27, Short.MAX_VALUE)
                        .addComponent(jLabel14)
                        .addGap(15, 15, 15)
                        .addComponent(jDateChooser, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(impRegisterLayout.createSequentialGroup()
                        .addGroup(impRegisterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jButton1)
                            .addComponent(jButton4))
                        .addGap(8, 8, 8)
                        .addComponent(jLabelRes)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabelSol)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addGap(12, 12, 12)
                .addComponent(jButton2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtAdjuntoName)
                .addGap(9, 9, 9)
                .addGroup(impRegisterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jCheckBox1)
                    .addComponent(btnRegistrarTarea, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton3))
                .addGap(15, 15, 15))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jpControl, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(impRegister, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jpControl, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(impRegister, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
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
    private void ComboBoxProyectoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ComboBoxProyectoActionPerformed
            indexProyecto = ComboBoxProyecto.getSelectedIndex();        
    }//GEN-LAST:event_ComboBoxProyectoActionPerformed

    private void btnRegistrarTareaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRegistrarTareaActionPerformed
        if("".equals(jTextFieldTarea.getText())){
            JOptionPane.showMessageDialog(this, " No puede dejar Campos Vacios \n Verifique e intente de Nuevo ");
        }else{   
            registroTarea();
            this.dispose();
        }
    }//GEN-LAST:event_btnRegistrarTareaActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        RegisterInfoAdicional dialog = new RegisterInfoAdicional(new javax.swing.JFrame(), true);
        dialog.setVisible(true);
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton3MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton3MouseClicked
        this.dispose();
    }//GEN-LAST:event_jButton3MouseClicked

    private void jButton1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton1MouseClicked
        //RegisterInfoAdicional2 adicional2 = new RegisterInfoAdicional2( , rootPaneCheckingEnabled);
        //adicional2.setVisible(true);
    }//GEN-LAST:event_jButton1MouseClicked

    //Botón para subir archivos
    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        
        JFileChooser chooser = new JFileChooser();
        FileFilter filter = new FileNameExtensionFilter("Documentos de Word (*.docx)","docx"); 
        FileFilter filter2 = new FileNameExtensionFilter("Libro de Excel (*.xlsx)","xlsx"); 
        FileFilter filter3 = new FileNameExtensionFilter("PDF (*.pdf)","pdf"); 
        FileFilter filter4 = new FileNameExtensionFilter("PNG (*.png)","png"); 
        chooser.setFileFilter(filter4);
        chooser.setFileFilter(filter3);
        chooser.setFileFilter(filter2);
        chooser.setFileFilter(filter);
        chooser.setDialogTitle("Adjuntar archivo");
        chooser.setAcceptAllFileFilterUsed(false);            
        chooser.showOpenDialog(null);
      
        File file = chooser.getSelectedFile();
        try{         
            long lengthKB=file.length()/1000;
            long lengthMB=lengthKB/1000;
            
            System.out.println("BYTES:"+file.length()+"KB:"+lengthKB+"MB:"+lengthMB);
            
            if(lengthMB<=5){
                filename = file.getAbsolutePath();
                txtAdjuntoName.setText(filename);
            }else{
                 JOptionPane.showMessageDialog(null, "El tamaño del archivo superó el límite: por favor ingrese un archivo con tamaño menor a 5MB");
            }
            
        }catch(NullPointerException e){
            JOptionPane.showMessageDialog(null, "Adjuntar archivo");
            //System.out.println(e);
        }
        
        
    }//GEN-LAST:event_jButton2ActionPerformed
    //Capturamos los indices de los comboBox y los guardamos en variables
    
    private void ComboBoxProductoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ComboBoxProductoActionPerformed
        indexProducto= ComboBoxProducto.getSelectedIndex();
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

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        jLabelRes.setText(getDato1());
        jLabelSol.setText(getDato2());
    }//GEN-LAST:event_jButton4ActionPerformed

    
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
            java.util.logging.Logger.getLogger(RegisterTarea.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(RegisterTarea.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(RegisterTarea.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(RegisterTarea.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() { 
                RegisterTarea dialog = new RegisterTarea(new javax.swing.JFrame(), true);
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
    private javax.swing.JComboBox<String> ComboBoxProyecto;
    private javax.swing.JPanel bgClose;
    private javax.swing.JLabel btnClose;
    private javax.swing.JButton btnRegistrarTarea;
    private gui.ImagePanel impRegister;
    private gui.ImagePanel ipmLogoLeft;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JCheckBox jCheckBox1;
    private com.toedter.calendar.JDateChooser jDateChooser;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JLabel jLabelRes;
    private javax.swing.JLabel jLabelSol;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JTextField jTextFieldTarea;
    private javax.swing.JPanel jpControl;
    private javax.swing.JLabel txtAdjuntoName;
    // End of variables declaration//GEN-END:variables

  

   
}