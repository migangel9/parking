/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.est.ctrl;

import com.est.views.viewEntrada;
import com.est.views.viewPrincipal;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.Authenticator;
import java.net.MalformedURLException;
import java.net.PasswordAuthentication;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

/**
 *
 * @author m
 */
public class hiloCam1 extends Thread
{
    conexionCOM con;    
    viewPrincipal v;
    private final String FRENTE = "http://192.168.1.88/tmpfs/auto.jpg";
    private final String ATRAS = "http://192.168.1.89/tmpfs/auto.jpg";
    private boolean hayConexion = true;
    private String direccionIP = "";
    private String usuario = "admin";
    private String contra = "admin";
    public BufferedImage imagen = null;
    private URL nurl = null;
    URL url1 = null;
        URL url2 = null;
        
    
    public hiloCam1(){
      
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

    
   // Metodo del hilo
   public void run()
   {        
      // mientras continuar ...
      while (continuar)
      {
        
        try {
            url1= new URL(this.FRENTE);//"http://192.168.1.88/tmpfs/auto.jpg"
        } catch (MalformedURLException e) {
            //e.printStackTrace();
            //viewPrincipal.cam1.setText("Sin conexión a la cámara");
        }
        Authenticator au1 = new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication (usuario, contra.toCharArray());
            }
        };
        Authenticator.setDefault(au1);
        
       
        ///////////OBTENIENDO IMAGEN
        try {
            imagen = ImageIO.read(url1);
       } catch (IOException ex) {
           ex.printStackTrace();
       }
        if (imagen != null) {
            ImageIcon fotoParaMostrar = new ImageIcon(imagen.getScaledInstance(500 , 275  , 1));
            //viewPrincipal.cam1.setIcon(fotoParaMostrar);
        }
        
            
        
                
      } 
   } 
}