/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.est.ctrl;

import com.est.views.viewEntrada;
import com.est.views.viewPrincipal;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author m
 */
public class hiloEnviaCOM extends Thread
{
    conexionCOM con;
    String senal;
    
    public hiloEnviaCOM(conexionCOM con, String senal){
        this.con = con;        
        this.senal = senal;
    }   
      
   // Metodo del hilo
   public void run()
   { 
       System.out.println("Inicia HILO");
      // mientras continuar ...            
       if(senal.equals("abrir1")){
                    try {            
                        this.con.envia("A","COM9"); 
                    } catch (Exception ex) {
                        //Logger.getLogger(viewPrincipal.class.getName()).log(Level.SEVERE, null, ex);
                    }
       }
       if(senal.equals("abrir2")){
                    try {            
                        this.con.envia("B","COM9"); 
                    } catch (Exception ex) {
                        //Logger.getLogger(viewPrincipal.class.getName()).log(Level.SEVERE, null, ex);
                    }
       }
       if(senal.equals("cerrar1")){
                    try {            
                        this.con.envia("C","COM9"); 
                    } catch (Exception ex) {
//                        Logger.getLogger(viewPrincipal.class.getName()).log(Level.SEVERE, null, ex);
                    }
       }
       if(senal.equals("cerrar2")){
                    try {            
                        this.con.envia("D","COM9"); 
                    } catch (Exception ex) {
                        //Logger.getLogger(viewPrincipal.class.getName()).log(Level.SEVERE, null, ex);
                    }
       }
      
   } 
}