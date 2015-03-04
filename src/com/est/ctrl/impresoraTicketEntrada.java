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

public class impresoraTicketEntrada implements Printable {
    negocioD negocio = new negocioD();
    preciosD precio = new preciosD();
    String fechaEntrada;    
    String folio;
    String tipoAuto;
    
        public impresoraTicketEntrada(String fechaEntrada, String folio, String tipoAuto) {
                this.fechaEntrada = fechaEntrada;                
                this.folio = folio;
                this.tipoAuto = tipoAuto;
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
            //Image img1 = Toolkit.getDefaultToolkit().getImage("images/login.jpg");
            JBarcodeBean barcode = new JBarcodeBean();
            // nuestro tipo de codigo de barra
            //barcode.setCodeType(new Interleaved25());
            barcode.setCodeType(new Codabar());
            // nuestro valor a codificar y algunas configuraciones mas
            barcode.setCode(folio);
            barcode.setCheckDigit(true);
            BufferedImage bufferedImage = barcode.draw(new BufferedImage(200, 20, BufferedImage.TYPE_INT_RGB));
            //Image img = null;
            //img = Toolkit.getDefaultToolkit().getImage("images/login.jpg");
            //System.out.println(img);
                Font fuente = new Font("Dialog", Font.PLAIN, 8);                
                Font fuenteN = new Font("Dialog", Font.BOLD, 11);
                Font fuenteMin = new Font("Dialog", Font.PLAIN, 7);
                if (pageIndex == 0) {
                        Graphics2D g2d= (Graphics2D)g;
                        g2d.translate(pf.getImageableX(), pf.getImageableY());                         
                        g2d.setFont(fuente);
                        g2d.setColor(Color.black);                        
                        g2d.drawString(negocio.getNombre(), 50, 10);
                        g2d.drawString(negocio.getDireccion(), 70, 20);
                        g2d.drawString("Fecha/Hora: "+fechaEntrada, 5, 33);                        
                        g2d.drawString(negocio.getNombreFiscal(), 5, 43);
                        g2d.drawString("RFC: "+negocio.getRFC(), 5, 53);
                        g2d.drawString("FOLIO: "+folio, 5, 63);
                        g2d.drawString("TIPO: "+tipoAuto, 130, 63);
                        g2d.drawImage(bufferedImage, null, 0, 70);
                        //g2d.drawLine(5, 95, 205, 95);                                                
                        g2d.setFont(fuenteMin);
                        g2d.drawString("------------------------------------------------------------------------------", 5, 97);
                        g2d.drawString("Toda persona que deje su vehiculo en el estacionamiento", 5, 105);
                        g2d.drawString("acepta estas condiciones.", 5, 113);
                        g2d.drawString("Entregue su boleto al solicitar su vehiculo.", 5, 121);
                        g2d.drawString("*No pierda este boleto, su auto no sera entregado sin el.", 5, 129);
                        g2d.drawString("Nos esforzamos por cuidar la integridad del auto no", 5, 137);
                        g2d.drawString("haciendonos responsables por objetos dejados en su interior", 5, 145);
                        g2d.drawString("y que no hayan sido reportados en la Administración", 5, 153);
                        g2d.drawString("Ni nos hacemos responsables por:", 5, 161);
                        g2d.drawString("-Robo total del vehiculo y daños ocasionados por causa de", 5, 169);
                        g2d.drawString("huelga y albortos populares, descomposturas mecanicas, ", 5, 177);
                        g2d.drawString("incendios, ponchaduras de llantas, daños naturales, incluyendo", 5, 185);
                        g2d.drawString("fenomenos meteorologicos, daños ocacionados al vehiculo si el", 5, 193);
                        g2d.drawString("propietario maniobra dentro de las instalaciones.", 5, 201);
                        g2d.drawString("-En caso de siniestro (golpes, rayaduras, abolladuras, tallones,", 5, 209);
                        g2d.drawString("etc.) que hayan sido en nuestras instalaciones, ocasionados", 5, 217);
                        g2d.drawString("por el personal del estacionamiento, la reparación será en", 5, 225);
                        g2d.drawString("nuestros talleres.", 5, 233);
                        g2d.drawString("-Tiempo de tolerancia 10 min, una vez pasados se le cobrara", 5, 241);
                        g2d.drawString("otra hora.", 5, 249);
                        g2d.drawString("Nota: Una vez saliendo la unidad no se aceptan reclamaciones.", 5, 257);
                        g2d.drawString("por el personal del estacionamiento, la reparación será en", 5, 265);
                        g2d.drawString("-En este estacionamiento tenemos una poliza de seguridad de", 5, 273);
                        g2d.drawString("$250 mil pesos menos el 10% del importe fijado al vehiculo, que", 5, 281);
                        g2d.drawString("es deducible establecido por la aseguradora y que quedara a", 5, 289);
                        g2d.drawString("cargo del propietario del auto.", 5, 297);
                        g2d.setFont(fuenteN);
                        g2d.drawString("Hr/Fracción Automovil: $"+precio.getPrecio(1), 5, 307);
                        g2d.drawString("Hr/Fracción Camioneta: $"+precio.getPrecio(2), 5, 317);
                        //g2d.drawString("ADIOS", 10,15);                     
                        
           
                        //g2d.drawImage(bufferedImage, null, 10, 15);
                        g2d.dispose();
                        
                        return Printable.PAGE_EXISTS;                                   
                } else {
                        return Printable.NO_SUCH_PAGE;
                }
        }

        /*public static void main(String arg[]) {

                impresoraTicketEntrada sp = new impresoraTicketEntrada("2014-10-02 12:20", "0000007");
        }*/
}

