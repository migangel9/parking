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
import com.est.views.viewReporteEst;
import com.est.views.viewReporteFis;
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
import javax.swing.table.DefaultTableModel;
import net.sourceforge.jbarcodebean.BarcodeException;
import net.sourceforge.jbarcodebean.JBarcodeBean;
import net.sourceforge.jbarcodebean.model.Codabar;
import net.sourceforge.jbarcodebean.model.Code39;
import net.sourceforge.jbarcodebean.model.Interleaved25;

public class impresoraReporteEspejo implements Printable {                  
        viewReporteFis reporte;
        negocioD negocio = new negocioD();
        String tabla;
        
        public String getFechaActual() {
        Date ahora = new Date();
        SimpleDateFormat formateador = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return formateador.format(ahora);
    }
        
        public impresoraReporteEspejo(viewReporteFis rep) {      
                /* Construct the print request specification.                
                */         
            this.reporte = rep;              
                PrintRequestAttributeSet aset = new HashPrintRequestAttributeSet();
                aset.add(OrientationRequested.PORTRAIT);
                aset.add(new Copies(1));
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
        }
    
    public int print(Graphics g,PageFormat pf,int pageIndex) {                     
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
                        g2d.drawString("REPORTE", 5, 63);                                                     
                        g2d.drawString("Fecha Reporte: "+getFechaActual(), 5, 73);                           
                        //g2d.drawLine(5, 95, 205, 95);                                                                        
                        //g2d.drawString("------------------------------------------------------------------------------", 5, 73);                                                                      
                        g2d.drawString("------------------------------------------------------------------------------", 5, 83);                        
                        int y = 93;                        
                        int i=0;
                        float total = 0;
                                while(i < reporte.tablaReporte.getRowCount()) {
                                    g2d.drawString("- "+reporte.tablaReporte.getValueAt(i, 0).toString()+" Hr. Entrada"+reporte.tablaReporte.getValueAt(i, 2).toString(), 5, y);                           
                                    g2d.drawString(" Hr. Salida"+reporte.tablaReporte.getValueAt(i, 3).toString()+"       $"+reporte.tablaReporte.getValueAt(i, 4).toString(), 5, y+10);                           
                                    total += Float.valueOf(reporte.tablaReporte.getValueAt(i, 4).toString());
                                    y+=20;
                                    i++;                            
                                } 
                        g2d.drawString("Total: "+total, 80,y);                                                        
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

