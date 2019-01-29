package Classes;

import com.toedter.calendar.JDateChooser;
import java.text.SimpleDateFormat;

public class Funciones {
    
    SimpleDateFormat formato = new SimpleDateFormat("dd-MM-yyyy"); 
    
    public String getFecha(JDateChooser jd){
        if(jd.getDate()!=null){
            return formato.format(jd.getDate());
        }else{
            return null;
        }
    }
}
