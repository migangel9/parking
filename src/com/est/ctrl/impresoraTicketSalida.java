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
import java.awt.*;
import java.awt.image.*;
import java.awt.print.*;
import java.io.*;
import java.net.*;
import javax.imageio.ImageIO;
import javax.print.*;
import javax.print.attribute.*;
import javax.print.attribute.standard.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import net.sourceforge.jbarcodebean.BarcodeException;
import net.sourceforge.jbarcodebean.JBarcodeBean;
import net.sourceforge.jbarcodebean.model.Codabar;
import net.sourceforge.jbarcodebean.model.Code39;
import net.sourceforge.jbarcodebean.model.Interleaved25;

public class impresoraTicketSalida implements Printable {
        negocioD negocio = new negocioD();
        String fechaEntrada;
        String hrEntrada;
        String fechaSalida;
        String hrSalida;
        String tiempo;
        String subtotal;
        String iva;
        String total;
        String folio;
        String tipo;
        
        public impresoraTicketSalida(String fechaEntrada, String fechaSalida, String tiempo, String subtotal, String iva, String total, String folio, String tipo) {
                this.fechaEntrada = fechaEntrada;                                
                this.fechaSalida = fechaSalida;                
                this.tiempo = tiempo;
                this.subtotal = subtotal;
                this.iva = iva;
                this.total = total;
                this.folio = folio;
                this.tipo = tipo;
                /* Construct the print request specification.                
                */
                PrintRequestAttributeSet aset = new HashPrintRequestAttributeSet();
                aset.add(OrientationRequested.PORTRAIT);
                aset.add(new Copies(1));
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
        }

        public int print(Graphics g,PageFormat pf,int pageIndex) {
           
            //System.out.println(img);
                Font fuente = new Font("Dialog", Font.PLAIN, 8);                                
                Font fuenteBig = new Font("Dialog", Font.PLAIN, 13);  
                if (pageIndex == 0) {
                        Graphics2D g2d= (Graphics2D)g;
                        g2d.translate(pf.getImageableX(), pf.getImageableY());                         
                        g2d.setFont(fuente);                        
                        g2d.setColor(Color.black);                                                                        
                        g2d.drawString(negocio.getNombre(), 50, 10);
                        g2d.drawString(negocio.getDireccion(), 70, 20);                        
                        g2d.drawString(negocio.getNombreFiscal(), 5, 43);
                        g2d.drawString("RFC: "+negocio.getRFC(), 5, 53);
                        g2d.drawString("FOLIO: "+folio, 5, 63);                        
                        //g2d.drawLine(5, 95, 205, 95);                                                                        
                        g2d.drawString("------------------------------------------------------------------------------", 5, 73);
                        g2d.drawString(""+tipo, 5, 83);                                                
                        g2d.drawString("Entrada: "+fechaEntrada, 5, 93);                                                
                        g2d.drawString("Salida: "+fechaSalida, 5, 103);                        
                        g2d.drawString("Tiempo: "+tiempo, 5, 113);
                        g2d.drawString("Subtotal: $"+subtotal, 5, 123);                        
                        g2d.drawString("IVA: $"+iva, 100, 133);     
                        g2d.setFont(fuenteBig);
                        g2d.drawString("Total: $"+total, 5, 148);
                        g2d.setFont(fuente);
                        g2d.drawString("<<< GRACIAS POR SU PREFERENCIA >>>", 25, 165);                        
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

