/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.est.ctrl;

/**
 *
 * @author m
 */
import com.est.views.viewCorteCaja;
import java.awt.*;
import java.awt.image.*;
import java.awt.image.BufferedImage;
import java.awt.print.*;
import java.io.*;
import java.io.File;
import java.io.IOException;
import java.net.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.imageio.ImageIO;
import javax.imageio.ImageIO;
import javax.print.*;
import javax.print.attribute.*;
import javax.print.attribute.standard.*;
import javax.swing.JOptionPane;
import net.sourceforge.jbarcodebean.BarcodeException;
import net.sourceforge.jbarcodebean.JBarcodeBean;
import net.sourceforge.jbarcodebean.model.Codabar;
import net.sourceforge.jbarcodebean.model.Code39;
import net.sourceforge.jbarcodebean.model.Interleaved25;

public class impresoraCorte implements Printable {           
        negocioD negocio = new negocioD();
        Connection conect = null;
        viewCorteCaja corte = new viewCorteCaja(new javax.swing.JFrame(),true);
        String fechaHr = getFechaActual();
        String folioCorte = getFolioCorte();  
        String ultimoCorte = getUltimoCorte(); 
        String hrsCobradas = getHrsCobradas();
        int numTickets;
        
        public String getFechaActual() {
        Date ahora = new Date();
        SimpleDateFormat formateador = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return formateador.format(ahora);
    }
        
        public impresoraCorte() {                                            
                /* Construct the print request specification.                
                */         
            impresoraCorteEspejo impres = new impresoraCorteEspejo();
                PrintRequestAttributeSet aset = new HashPrintRequestAttributeSet();
                aset.add(OrientationRequested.PORTRAIT);
                aset.add(new Copies(2));
                aset.add(MediaSize.findMedia(72, 1000000000, MediaPrintableArea.MM));    
                float xmargin = 0.5f;
                float ymargin = 0.5f;
                float w = 72;
                float h = 1000000000;
                aset.add(new MediaPrintableArea(xmargin, ymargin, w - 2*xmargin, h - 2*ymargin, MediaPrintableArea.MM));                
                aset.add(new JobName("Mi ticket", null));

                /* Create a print job */
                PrinterJob pj = PrinterJob.getPrinterJob();       
                pj.setPrintable(this);
                /* locate a print service that can handle the request */
                PrintService printService = PrintServiceLookup.lookupDefaultPrintService();                
                        System.out.println("Impresora: " + printService.getName());
                        try {
                                pj.setPrintService(printService);
                                pj.print(aset);                                                                       
                        } catch (PrinterException pe) { 
                                System.err.println(pe);
                        } 
                setCorte(this.fechaHr,this.ultimoCorte,corte.getTotalCaja(),corte.getTotalAuto(),corte.getTotalCamioneta(),this.hrsCobradas, String.valueOf(this.numTickets),"caja");
                updateRegistro(folioCorte);                        
                updateRegistroS(folioCorte); 
                
        }

    private int getNumFilas(ResultSet resultSet) {
        if (resultSet == null) {
            return 0;
        }
        try {
            resultSet.last();
            return resultSet.getRow();
        } catch (SQLException exp) {
            exp.printStackTrace();
        } finally {
            try {
                resultSet.beforeFirst();
            } catch (SQLException exp) {
                exp.printStackTrace();
            }
        }
        return 0;
    }
    
    public String getUltimoCorte(){
        String ultimaFecha = "";
            String SSQL="SELECT MAX(fechaCorte) as ultimoCorte FROM corte";
                System.out.println(SSQL);        
                try {
                    conect = conexionBD.getConexion();        
                    Statement st = conect.createStatement();            
                    ResultSet rs = st.executeQuery(SSQL);
                if(rs.next()){           
                    ultimaFecha = rs.getString("ultimoCorte");                   
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
         return ultimaFecha;
    }
    
    public String getHrsCobradas(){
            String ultimaFecha = "";
            String SSQL="SELECT SUM(hrsCobradas) totalHrs FROM registros WHERE pago is not null AND idcorte is NULL AND tipoAuto in ('Automovil','Camioneta')";
                System.out.println(SSQL);        
                try {
                    conect = conexionBD.getConexion();        
                    Statement st = conect.createStatement();            
                    ResultSet rs = st.executeQuery(SSQL);
                if(rs.next()){           
                    ultimaFecha = rs.getString("totalHrs");                   
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
         return ultimaFecha;
    }
    
    public String getFolioCorte(){
        String folio;
            folio = "";
            String SSQL="SELECT MAX(idCorte) as ultimoID FROM corte";
                System.out.println(SSQL);        
                try {
                    conect = conexionBD.getConexion();        
                    Statement st = conect.createStatement();            
                    ResultSet rs = st.executeQuery(SSQL);
                if(rs.next()){           
                    int i = rs.getInt("ultimoID")+1;
                   folio = String.valueOf(i);                   
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
    
    public void updateRegistro(String folio){                
        try {            
          conect = conexionBD.getConexion();  
          Statement st = conect.createStatement();
          st.executeUpdate("UPDATE registros SET" 
                            +" idcorte = "+folio+""
                            +" WHERE (pago is not null OR cancelado = 1) AND idcorte is NULL"
                            +";");          
         } catch(SQLException ex){            
          JOptionPane.showMessageDialog(null, ex, "Error de conexión con la Base de Datos", JOptionPane.ERROR_MESSAGE);
        }finally{
        try {
            conect.close();                        
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex, "Error de desconexión", JOptionPane.ERROR_MESSAGE);
        }
    }                   
    }
    
    public void updateRegistroS(String folio){                
        try {            
          conect = conexionBD.getConexion();  
          Statement st = conect.createStatement();
          st.executeUpdate("UPDATE registros_salida SET" 
                            +" idcorte = "+folio+""
                            +" WHERE idcorte is NULL"
                            +";");          
         } catch(SQLException ex){            
          JOptionPane.showMessageDialog(null, ex, "Error de conexión con la Base de Datos", JOptionPane.ERROR_MESSAGE);
        }finally{
        try {
            conect.close();                        
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex, "Error de desconexión", JOptionPane.ERROR_MESSAGE);
        }
    }                   
    }
    
    public void setCorte(String fechaCorte, String fechaAnterior, String totalCorte, String totalAutos, String totalCamionetas, String hrsCobradas, String ticketsCobrados, String login){        
        try {            
          conect = conexionBD.getConexion();  
          Statement st = conect.createStatement();
          st.executeUpdate("INSERT INTO corte(fechaCorte, fechaCorteAnterior, totalCorte, totalAutos, totalCamionetas, hrsCobradas, ticketsCobrados, login) VALUES(" 
                            +"'"+fechaCorte+"',"
                            +"'"+fechaAnterior+"',"
                            +""+totalCorte+","
                            +""+totalAutos+","
                            +"'"+totalCamionetas+"',"
                            +""+hrsCobradas+","
                            +""+ticketsCobrados+","
                            +"'"+login+"')"                                                        
                            +";");          
         } catch(SQLException ex){            
          JOptionPane.showMessageDialog(null, ex, "Error de conexión con la Base de Datos", JOptionPane.ERROR_MESSAGE);
        }finally{
        try {
            conect.close();            
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex, "Error de desconexión", JOptionPane.ERROR_MESSAGE);
        }
    }              
      
    }
        
        public int print(Graphics g,PageFormat pf,int pageIndex) {            
            String SSQL="SELECT * FROM registros WHERE pago is not null AND idcorte is NULL";
            System.out.println(SSQL);            
            
            
            //System.out.println(img);
                Font fuente = new Font("Dialog", Font.PLAIN, 8);                                                
                if (pageIndex == 0) {
                        Graphics2D g2d= (Graphics2D)g;
                        g2d.translate(pf.getImageableX(), pf.getImageableY());                         
                        g2d.setFont(fuente);
                        g2d.setColor(Color.black);                        
                        g2d.drawString(negocio.getNombre(), 50, 10);
                        g2d.drawString(negocio.getDireccion(), 70, 20);                        
                        g2d.drawString(negocio.getNombreFiscal(), 5, 43);
                        g2d.drawString("RFC: "+negocio.getRFC(), 5, 53);                                               
                        g2d.drawString("CORTE DE CAJA", 5, 63);                           
                        g2d.drawString("Folio Corte: "+folioCorte, 5, 73);   
                        g2d.drawString("Fecha Inicio: "+this.ultimoCorte, 5, 83);   
                        g2d.drawString("Fecha Fin: "+this.fechaHr, 5, 93);   
                        //g2d.drawLine(5, 95, 205, 95);                                                                        
                        //g2d.drawString("------------------------------------------------------------------------------", 5, 73);
                        g2d.drawString("Total en caja: $"+corte.getTotalCaja(), 5, 103);                        
                        g2d.drawString(":: ", 5, 113);                        
                        g2d.drawString("Autos: $"+corte.getTotalAuto(), 5, 123);
                        g2d.drawString("Camionetas: $"+corte.getTotalCamioneta(), 5, 133);                                                
                        g2d.drawString("Total de hrs cobradas: "+this.hrsCobradas, 5, 143);
                        g2d.drawString("No incluido en total caja:: ", 5, 153);                                                
                        g2d.drawString("Conv. Hotel1: $"+corte.getTotalHotel1(), 5, 163);                                                
                        g2d.drawString("Conv. Hotel2: $"+corte.getTotalHotel2(), 5, 173);                                                
                        g2d.drawString("Pensionados: $"+corte.getTotalHotel3(), 5, 183);     
                        g2d.drawString("------------------------------------------------------------------------------", 5, 193);                        
                        try {
                            conect = conexionBD.getConexion();
                            Statement st = conect.createStatement();
                            ResultSet rs = st.executeQuery(SSQL);   
                            this.numTickets = getNumFilas(rs);
                            g2d.drawString("No. de Tickets: "+numTickets, 5, 203);                                                        
                            int x = 5;
                            int y = 213;
                            int y2 = 223;
                             while(rs.next()){    
                                 String mas = "";
                                 if(rs.getBoolean("dejaLlave") == false){
                                     mas += " NoLL";                                     
                                 }
                                 if(rs.getBoolean("perdioTicket") == true){
                                     mas += " TP";                                     
                                 }
                                 if(rs.getBoolean("promoNoche") == true){
                                     mas += " NOC";                                     
                                 }
                                 g2d.drawString("-"+rs.getString("folio")+" "+rs.getString("tipoAuto")+" "+rs.getString("hrEntrada"), x, y);                                     
                                 g2d.drawString(" "+rs.getString("hrSalida")+"       $"+rs.getString("pago")+ " "+mas, x, y2);                                     
                                 y += 20;
                                 y2 += 20;
                               } 
                             g2d.drawString("------------------------------------------------------------------------------", 5, y2+10);                        
                    } catch (SQLException ex) {

                        JOptionPane.showMessageDialog(null, ex, "Error de conexión con la Base de Datos", JOptionPane.ERROR_MESSAGE);
                    }finally{
                        try {
                            conect.close();
                        } catch (SQLException ex) {
                            JOptionPane.showMessageDialog(null, ex, "Error de desconexión", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                                                                        
                        //g2d.drawString("ADIOS", 10,15);                                                        
                        //g2d.drawImage(bufferedImage, null, 10, 15);
                        g2d.dispose();                                                
                        return Printable.PAGE_EXISTS;                                   
                } else {
                        return Printable.NO_SUCH_PAGE;
                }
        }

       /* public static void main(String arg[]) {

                impresoraTicketSalida sp = new impresoraTicketSalida();
        }*/
}

