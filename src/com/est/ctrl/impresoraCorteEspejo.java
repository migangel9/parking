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
import java.awt.print.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.print.*;
import javax.print.attribute.*;
import javax.print.attribute.standard.*;
import javax.swing.JOptionPane;

public class impresoraCorteEspejo implements Printable {           
        negocioD negocio = new negocioD();
        viewCorteCaja corte = new viewCorteCaja(new javax.swing.JFrame(),true);
        float porcentajeEspejo = Float.valueOf(negocio.getPorcentajeR());
        float totalEspejo;            
        Connection conect = null;        
        String fechaHr = getFechaActual();
        String folioCorte = getFolioCorte();  
        String ultimoCorte = getUltimoCorte();               
        float totalReal = 0;
        int numTickets;
        float auto = 0;
        float camioneta = 0;
        float limite = 0; 
        float total = 0; 
        int totalHrs = 0;
        
        public String getFechaActual() {
        Date ahora = new Date();
        SimpleDateFormat formateador = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return formateador.format(ahora);
    }
        
        public impresoraCorteEspejo() {                                            
                /* Construct the print request specification.                
                */          
                this.totalReal = Float.valueOf(corte.getTotalCaja());
                this.totalEspejo = (this.totalReal * this.porcentajeEspejo)/100;
                doCorte();
                PrintRequestAttributeSet aset = new HashPrintRequestAttributeSet();
                aset.add(OrientationRequested.PORTRAIT);
                aset.add(new Copies(2));
                aset.add(MediaSize.findMedia(72, 1000, MediaPrintableArea.MM));    
                float xmargin = 0.5f;
                float ymargin = 0.5f;
                float w = 72;
                float h = 842;
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
                setCorteEspejo(folioCorte,this.totalHrs, this.fechaHr,this.ultimoCorte,String.valueOf(this.total),String.valueOf(this.auto),String.valueOf(this.camioneta),"caja");                                
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
    
    public String getRegEspejo(){
        String folio;
            folio = "";
            String SSQL="SELECT MAX(idRegEspejo) as ultimoID FROM registros";
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
    
    public void updateRegistro(String folio, String folioEspejo){                
        try {            
          conect = conexionBD.getConexion();  
          Statement st = conect.createStatement();
          st.executeUpdate("UPDATE registros SET" 
                            +" idCorteEspejo = "+folioEspejo+""
                            +" WHERE idRegistros='"+folio+"'"
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
    
     public void updateFolioEspejo(String folio, String regEspejo){                
        try {            
          conect = conexionBD.getConexion();  
          Statement st = conect.createStatement();
          st.executeUpdate("UPDATE registros SET" 
                            +" idRegistroEspejo = "+regEspejo+""
                            +" WHERE idRegistros='"+folio+"'"
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
    
    public void setCorteEspejo(String idCorteEspejo, int totalHrs, String fechaCorte, String fechaAnterior, String totalCorte, String totalAutos, String totalCamionetas, String login){        
        try {            
          conect = conexionBD.getConexion();  
          Statement st = conect.createStatement();
          st.executeUpdate("INSERT INTO corteespejo(idCorteEspejo, totalHrs, fechaCorte, fechaCorteAnterior, totalCorte, totalAutos, totalCamionetas, login) VALUES(" 
                            +"'"+idCorteEspejo+"',"
                            +""+totalHrs+","
                            +"'"+fechaCorte+"',"
                            +"'"+fechaAnterior+"',"
                            +""+totalCorte+","
                            +""+totalAutos+","
                            +"'"+totalCamionetas+"',"                          
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
    
    private void doCorte(){
        String SSQL="SELECT * FROM registros WHERE pago is not null AND idcorte is NULL";
        try {
                conect = conexionBD.getConexion();
                Statement st = conect.createStatement();
                ResultSet rs = st.executeQuery(SSQL);                   
                while(this.total <= this.totalEspejo){                                                   
                    if(rs.next()){
                        if(this.totalEspejo - this.total <= 12){
                            if((rs.getString("tipoAuto").equals("Automovil")) && (rs.getFloat("pago") <= 12.00)) {                                    
                                updateRegistro(rs.getString("idRegistros"),folioCorte); 
                                this.total += rs.getFloat("pago");                                        
                                this.totalHrs += rs.getInt("hrsCobradas");                                        
                            }
                        }                        
                        else if((rs.getString("tipoAuto").equals("Automovil")) && (rs.getFloat("pago") <= 36.00)) {                                    
                            updateRegistro(rs.getString("idRegistros"),folioCorte); 
                            this.total += rs.getFloat("pago");                                        
                            this.totalHrs += rs.getInt("hrsCobradas");                                        
                        }
                    }
                }                                       
        }catch (SQLException ex) {
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
            String SSQL="SELECT * FROM registros WHERE pago is not null AND idCorteEspejo = (SELECT MAX(idCorteEspejo) FROM corteespejo)";
            System.out.println(SSQL);            
                Font fuente = new Font("Dialog", Font.PLAIN, 8);                                                
                Font fuenteBig = new Font("Dialog", Font.BOLD, 12); 
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
                        g2d.drawString("Total en caja: $"+this.total, 5, 103);                        
                        g2d.drawString("Total de hrs cobradas: "+this.totalHrs, 5, 113);
                        g2d.drawString("--------------------------------------------------", 5, 123);                        
                        try {
                            conect = conexionBD.getConexion();
                            Statement st = conect.createStatement();
                            ResultSet rs = st.executeQuery(SSQL);   
                            this.numTickets = getNumFilas(rs);
                            //g2d.drawString("No. de Tickets cobrados: "+numTickets, 5, 133);                                                        
                            int x = 5;
                            int y = 133;
                            int y2 = 143;
                            while(rs.next()){                                                                                                                                                         
                                        g2d.drawString("-"+rs.getString("folio")+" Entrada-"+rs.getString("hrEntrada"), x, y);
                                        g2d.drawString(" Salida-"+rs.getString("hrSalida")+"       $"+rs.getString("pago"), x, y2);                                        
                                        y += 20;
                                        y2 += 20;                                                                           
                            }       
                                g2d.setFont(fuenteBig);                                                                                                   
                                g2d.drawString("Total Hrs: "+this.totalHrs, x, y2+10);                                     
                                g2d.drawString("Total: "+this.total, x, y2+25);                                     
                        } catch (SQLException ex) {
                            JOptionPane.showMessageDialog(null, ex, "Error de conexión con la Base de Datos", JOptionPane.ERROR_MESSAGE);
                    }finally{
                        try {
                            conect.close();
                        } catch (SQLException ex) {
                            JOptionPane.showMessageDialog(null, ex, "Error de desconexión", JOptionPane.ERROR_MESSAGE);
                        }
                    }                                                                                                
                        //g2d.drawImage(bufferedImage, null, 10, 15);
                        g2d.dispose();                                                
                        return Printable.PAGE_EXISTS;                                   
                } else {
                        return Printable.NO_SUCH_PAGE;
                }
        }
        
  /*      public static void main(String arg[]) {

                impresoraCorteEspejo sp = new impresoraCorteEspejo();
        }*/
}

