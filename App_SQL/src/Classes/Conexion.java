package Classes;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import javax.swing.JOptionPane;

/**
 * Clase con los parametros y funciones necesarias para la conexion a la Base de Datos
 * @author Luis Cardenas
 */
public class Conexion {
    //Datos necesarios para la conexion: ubicacion de  Base de datos - Usuario - Contraseña
    
    //Base de datos SQL Server
    /* String  BaseDatos="ATENA",            
            Url="jdbc:sqlserver://172.19.71.150:1433;databaseName="+BaseDatos,
            Usuario="sa",
            Contraseña="sa"; */
    
    //Base de datos PostgreSQL
    String  BaseDatos="atenea",            
            Url="jdbc:postgresql://172.19.71.3:5432/"+BaseDatos,
            Usuario="dbadmin",
            Contraseña="dbmast2019+";
    
    //Creando un metodo con una variable Connection para la conexion 
    
    Connection conexion=null;
    /**
     * Funcion que realiza y obtiene la conexion a la base de datos
     * @return variable de tipo Connection con la conexión establecida
     */
    public Connection getConnection(){
      
        try {
           Class.forName("org.postgresql.Driver");//Invocacion del driver de conexion
           conexion= DriverManager.getConnection(Url,Usuario,Contraseña);//haciendo uso del driver
           //System.out.println("Conexion a la DB correcta");
            
        } catch (ClassNotFoundException | SQLException e) {
            JOptionPane.showMessageDialog(null,"ERROR: "+e.getMessage());
        }
        return conexion;
    }
}
