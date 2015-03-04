package com.est.ctrl;


import com.est.ctrl.conexionBD;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.JOptionPane;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author m
 */
public class registroD {
    private String tipoAuto;
    private boolean dejaLlave;
    private String hrEntrada;
    private String hrSalida;
    private float pago;
    private String rutaEntrada;
    private String rutaSalida;    
    Connection conect = null;
    private boolean resp;
    
     public String getFechaActual() {
        Date ahora = new Date();
        SimpleDateFormat formateador = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return formateador.format(ahora);
    }        
     
     public String getFolio(){
         String folio;
            folio = "";
            String SSQL="SELECT MAX(idRegistros) as ultimoID FROM registros";
                System.out.println(SSQL);        
                try {
                    conect = conexionBD.getConexion();        
                    Statement st = conect.createStatement();            
                    ResultSet rs = st.executeQuery(SSQL);
                if(rs.next()){           
                    int i = rs.getInt("ultimoID")+1;
                   folio = "0000000"+i;                   
                }               
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, ex, "Error de conexión con la Base de Datos", JOptionPane.ERROR_MESSAGE);
            }finally{
                try {
                    conect.close();
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(null, ex, "Error de desconexión", JOptionPane.ERROR_MESSAGE);
                }
            }
         return folio;
     }
     
     public String getHrEntrada(){
         return this.hrEntrada;
     }
     
     public boolean setEntrada(String folio, String tipoAuto, boolean dejaLlave, boolean cancelado, int idprecio, String hrEntrada){
        System.out.println(getFechaActual());
        this.hrEntrada = hrEntrada;
        try {            
          conect = conexionBD.getConexion();  
          Statement st = conect.createStatement();
          st.executeUpdate("INSERT INTO registros(folio, tipoAuto, dejaLlave, hrEntrada, cancelado, precios_idprecios, rutaFotoEntrada) VALUES(" 
                            +"'"+folio+"',"                            
                            +"'"+tipoAuto+"',"
                            +""+dejaLlave+","
                            +"'"+this.hrEntrada+"',"
                            +""+cancelado+","
                            +""+idprecio+","
                            +"'"+"data/E"+this.hrEntrada.replace(" ","").replace(":","_").replace("-","")+".ppj')"
                            +";");          
         } catch(SQLException ex){
            resp = false;
          JOptionPane.showMessageDialog(null, ex, "Error de conexión con la Base de Datos", JOptionPane.ERROR_MESSAGE);
        }finally{
        try {
            conect.close();
            impresoraTicketEntrada impresoraTicketEntrada = new impresoraTicketEntrada(this.hrEntrada, folio, tipoAuto);
            resp = true;
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex, "Error de desconexión", JOptionPane.ERROR_MESSAGE);
        }
    }        
      return resp;
      
    }
    
       public void setSalida(boolean promo, String folio, String subtotal, String iva, String hrSalida, String tiempoT, int ticket, String pago, long hrsCobradas){
        try {            
          conect = conexionBD.getConexion();  
          Statement st = conect.createStatement();
          st.executeUpdate("UPDATE registros SET"
                            +" promoNoche ="+promo+","                  
                            +" hrSalida ='"+hrSalida+"',"                  
                            +" tiempTranscurrido ='"+tiempoT+"',"
                            +" perdioTicket ="+ticket+","
                            +" subtotal ="+subtotal+","
                            +" iva ="+iva+","
                            +" hrsCobradas ="+hrsCobradas+","
                            +" pago ='"+pago+"'"                   
                            +"WHERE folio = '"+folio+"';");          
         } catch(SQLException ex){
            resp = false;
          JOptionPane.showMessageDialog(null, ex, "Error de conexión con la Base de Datos", JOptionPane.ERROR_MESSAGE);
        }finally{
        try {
            conect.close();                        
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex, "Error de desconexión", JOptionPane.ERROR_MESSAGE);
        }
    }                    
    }
     
      public boolean setCancelado(String folio, String tipoAuto, boolean dejaLlave, boolean cancelado, int idprecio, String hrEntrada){
        System.out.println(getFechaActual());
        this.hrEntrada = hrEntrada;
        try {            
          conect = conexionBD.getConexion();  
          Statement st = conect.createStatement();
          st.executeUpdate("INSERT INTO registros(folio, tipoAuto, dejaLlave, hrEntrada, cancelado, precios_idprecios, rutaFotoEntrada) VALUES(" 
                            +"'"+folio+"',"
                            +"'"+tipoAuto+"',"
                            +""+dejaLlave+","
                            +"'"+this.hrEntrada+"',"
                            +""+cancelado+","
                            +""+idprecio+","
                            +"'"+"data/E"+this.hrEntrada.replace(" ","").replace(":","_").replace("-","")+".ppj')"
                            +";");          
         } catch(SQLException ex){
            resp = false;
          JOptionPane.showMessageDialog(null, ex, "Error de conexión con la Base de Datos", JOptionPane.ERROR_MESSAGE);
        }finally{
        try {
            conect.close();            
            resp = true;
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex, "Error de desconexión", JOptionPane.ERROR_MESSAGE);
        }
    }        
      return resp;      
    }       
    
}
