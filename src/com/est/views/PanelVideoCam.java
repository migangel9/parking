/*
 * PanelVideo.java
 *
 * Created on 01 Oct 2014
 */
package com.est.views;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.Authenticator;
import java.net.MalformedURLException;
import java.net.PasswordAuthentication;
import java.net.URL;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;

/**
 *
 * @author Bro0k
 */
public class PanelVideoCam extends javax.swing.JPanel {

    private String direccionIP1 = "";
    private String direccionIP2 = "";
    public BufferedImage frame1 = null;
    public BufferedImage frame2 = null;
    private JLabel etiqueta=null;
    URL url1 = null;
    URL url2 = null;
    Authenticator au=null;
    private String usuario = "";
    private String contra = "";
    Graphics2D g2 = null;

    /** Creates new form PanelVideo */
    public PanelVideoCam() {
        initComponents();
    }

    /**
     * RETORNA LA DIRECCION IP DE LA CAMARA1
     */
    public String getDireccionIP1() {
        return direccionIP1;
    }
    
    /**
     * RETORNA LA DIRECCION IP DE LA SEGUNDA CAMARA
     */
    public String getDireccionIP2() {
        return direccionIP2;
    }

    /**
     * ESTA FUNCION RECIBE LA DIRECCION IP PARA LA CAMARA1
     */
    public void setDireccionIP1(String direccionIP1) {
        this.direccionIP1 = direccionIP1;
    }
    
    /**
     * ESTA FUNCION RECIBE LA DIRECCION IP PARA LA CAMARA2
     */
    public void setDireccionIP2(String direccionIP2) {
        this.direccionIP2 = direccionIP2;
    }
    
    /**
     * ESTA FUNCION RECIBE LA ETIQUETA PARA REPINTAR LA CAMARA
     */
    public void setLabel(JLabel etiqueta){
        this.etiqueta=etiqueta;
    }
    
     /**
    RECIBE EL USUARIO PARA AUTENTICAR LA CONEXION IP
    String usuario
    */
    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }
    
    /**
    RECIBE LA CONTRASEÑA PARA AUTENTICAR LA CONEXION IP
    String contra
    */
    public void setContra(String contra) {
        this.contra = contra;
    }
    
    @Override
    public void paintComponent(Graphics g) {
        g2 = (Graphics2D) g;
        
        try {
            this.url1 = new URL(this.direccionIP1);//"http://192.168.1.88/tmpfs/auto.jpg"
            this.url2 = new URL(this.direccionIP2);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        this.au = new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication (usuario, contra.toCharArray());
            }
        };
        Authenticator.setDefault(au);
                
        try {
            frame1 = ImageIO.read(this.url1);
            frame2 = ImageIO.read(this.url2);
        } catch (IOException ex) {
                g2.drawString(ex.toString(), 5, 15);
        }
        this.etiqueta.setText("");
        ImageIcon fotoParaMostrar = new ImageIcon(frame2.getScaledInstance(500, 275, 1));
        this.etiqueta.setIcon(fotoParaMostrar);
        g2.drawImage(frame1.getScaledInstance(500 , 275  , 1), 0, 0, this);
        repaint();
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 153), 2));
        setFocusable(false);
        setMaximumSize(new java.awt.Dimension(500, 275));
        setMinimumSize(new java.awt.Dimension(500, 275));
        setPreferredSize(new java.awt.Dimension(500, 275));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 636, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 476, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}