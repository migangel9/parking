/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.est.ctrl;

import com.est.views.viewEntrada;
import com.est.views.viewPrincipal;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author m
 */
public class hiloEscuchaCOM extends Thread
{
    conexionCOM con;    
    viewPrincipal v;
    //FileWriter archivo = null;
    //PrintWriter pw = null;
    private final String FRENTE = "http://192.168.1.88/tmpfs/auto.jpg";
    private final String ATRAS = "http://192.168.1.89/tmpfs/auto.jpg";
    
    public hiloEscuchaCOM(conexionCOM con, viewPrincipal view){
        this.con = con;
        this.v = view;
    }
    
   // boolean que pondremos a false cuando queramos parar el hilo
//   conexionCOM conexionCom = new conexionCOM();    
   private boolean continuar = true;
   String lee;

   // metodo para poner el boolean a false.
   public void detenElHilo()
   {
      continuar=false;
   }

    public String getFechaActual() {
        Date ahora = new Date();
        SimpleDateFormat formateador = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return formateador.format(ahora);
    }
   
   // Metodo del hilo
   public void run()
   { 
       System.out.println("Inicia HILO");
        /*try {
            // mientras continuar ...
            archivo = new FileWriter("logCOM.txt",true);
            pw = new PrintWriter(archivo);
        } catch (IOException ex) {
            Logger.getLogger(hiloEscuchaCOM.class.getName()).log(Level.SEVERE, null, ex);
        }*/
      while (continuar)
      {                    
          try {
              this.lee = con.read();
              //this.pw.append(getFechaActual()+" -- "+ this.lee + "\n");
              //System.out.println("Rec: "+this.lee);              
              if(this.lee.equals("F")){
                  try {            
                        this.con.envia("A","COM9"); 
                    } catch (Exception ex) {
                        Logger.getLogger(viewPrincipal.class.getName()).log(Level.SEVERE, null, ex);
                    }
                  viewEntrada entrada = new viewEntrada(new javax.swing.JFrame(), true, this.FRENTE,v.panelVideoCam_21.getHayConexion());             
                  entrada.setVisible(true);
                  v.actualizarFrame();                  
                  System.out.println("*******************Entra por la entrada");
              }
               if(this.lee.equals("G")){
                  viewEntrada entrada = new viewEntrada(new javax.swing.JFrame(), true, this.ATRAS,v.panelVideoCam_22.getHayConexion());             
                  entrada.setVisible(true);
                  v.actualizarFrame();                   
                  System.out.println("*******************Entra por la salida");
              }
                if(this.lee.equals("H")){                  
                  v.registroSalida(this.FRENTE, "cam1");
                  v.actualizarFrame();
                  System.out.println("*******************Sale por la entrada");
              }
               if(this.lee.equals("I")){                        
                  v.registroSalida(this.ATRAS, "cam2");
                  v.actualizarFrame();
                  /*try {            
                        this.con.envia("B","COM9"); 
                    } catch (Exception ex) {
                        Logger.getLogger(viewPrincipal.class.getName()).log(Level.SEVERE, null, ex);
                    }*/
                  System.out.println("*******************Sale por la salida");
              }
          } catch (Exception ex) {
              //Logger.getLogger(hiloEscuchaCOM.class.getName()).log(Level.SEVERE, null, ex);
              System.out.println("ERROR en la comunicacion de la placa");
              
          }                    
      } 
        /*try {
            archivo.close();
        } catch (IOException ex) {            
        }*/
   } 
}