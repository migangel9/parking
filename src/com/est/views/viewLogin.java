/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.est.views;
import com.est.ctrl.conexionBD;
import com.est.ctrl.usuarioD;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.sql.Connection;
import javax.swing.JOptionPane;

/**
 *
 * @author m
 */
public class viewLogin extends javax.swing.JFrame {
        public static Connection miConexion;
    /**
     * Creates new form viewPrincipal
     */
    public viewLogin() {
        initComponents();        
        miConexion = conexionBD.getConexion();    
        if(miConexion!=null){
            System.out.println("Conectado correctamente a la BD");
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        usuarioLbl = new javax.swing.JLabel();
        usuarioTextBox = new javax.swing.JTextField();
        contrasenaLbl = new javax.swing.JLabel();
        entrarBtn = new javax.swing.JButton();
        cancelarBtn = new javax.swing.JButton();
        candadoImg = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        fondoImg = new javax.swing.JLabel();
        contrasenaTextBox = new javax.swing.JPasswordField();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Estacionamiento");
        setBackground(new java.awt.Color(255, 255, 255));
        setBounds(new java.awt.Rectangle(300, 200, 0, 0));
        setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        setIconImage(getIconImage());
        setResizable(false);

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        usuarioLbl.setText("Usuario:");

        usuarioTextBox.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N

        contrasenaLbl.setText("Contraseña:");

        entrarBtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/check-32.png"))); // NOI18N
        entrarBtn.setText("Entrar");
        entrarBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                entrarBtnActionPerformed(evt);
            }
        });

        cancelarBtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/error-32.png"))); // NOI18N
        cancelarBtn.setText("Cancelar");
        cancelarBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelarBtnActionPerformed(evt);
            }
        });

        candadoImg.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/login.png"))); // NOI18N

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/user-alt-128.png"))); // NOI18N

        fondoImg.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/fondo.png"))); // NOI18N

        contrasenaTextBox.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        contrasenaTextBox.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                contrasenaTextBoxKeyPressed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                .addComponent(fondoImg)
                .addGap(0, 0, Short.MAX_VALUE))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 117, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(contrasenaLbl)
                                    .addComponent(usuarioLbl))
                                .addGap(18, 18, 18)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(usuarioTextBox)
                                    .addComponent(contrasenaTextBox, javax.swing.GroupLayout.DEFAULT_SIZE, 158, Short.MAX_VALUE)))
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                                .addGap(124, 124, 124)
                                .addComponent(entrarBtn)
                                .addGap(62, 62, 62)
                                .addComponent(cancelarBtn)))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(candadoImg)))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(candadoImg)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 12, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(usuarioTextBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(usuarioLbl))
                        .addGap(10, 10, 10)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(contrasenaLbl)
                            .addComponent(contrasenaTextBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(60, 60, 60))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(34, 34, 34)))
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(entrarBtn)
                    .addComponent(cancelarBtn))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(fondoImg))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    public void entrar(){
        usuarioD user = new usuarioD();
        String usuario = usuarioTextBox.getText();
        String pass = contrasenaTextBox.getText();
        boolean resp = user.validarLogin(usuario, pass);
        System.out.println(resp);
        if(resp){
            this.dispose();
            viewPrincipal Principal = new viewPrincipal(user);
            Principal.setVisible(true);}
        else{
            //JOptionPane.showMessageDialog(null, ex, "Usuario y/o contraseña incorrecta, Intentelo de nuevo.", JOptionPane.ERROR_MESSAGE);
            JOptionPane.showMessageDialog(null, "Usuario y/o contraseña incorrecta, Intentelo de nuevo.");
            contrasenaTextBox.setText("");
            usuarioTextBox.setText("");                                    
        }
    }
    
    private void entrarBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_entrarBtnActionPerformed
        // TODO add your handling code here:
        entrar();
    }//GEN-LAST:event_entrarBtnActionPerformed

    private void contrasenaTextBoxKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_contrasenaTextBoxKeyPressed
        // TODO add your handling code here:
        if(evt.getKeyCode()==KeyEvent.VK_ENTER){
            entrar();
        }
    }//GEN-LAST:event_contrasenaTextBoxKeyPressed

    private void cancelarBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelarBtnActionPerformed
        // TODO add your handling code here:
        this.dispose();
    }//GEN-LAST:event_cancelarBtnActionPerformed

    @Override
    public Image getIconImage() {
        Image retValue = Toolkit.getDefaultToolkit().
                getImage(ClassLoader.getSystemResource("images/car.png"));
        return retValue;
    }    
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(viewLogin.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(viewLogin.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(viewLogin.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(viewLogin.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new viewLogin().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    javax.swing.JButton cancelarBtn;
    javax.swing.JLabel candadoImg;
    javax.swing.JLabel contrasenaLbl;
    javax.swing.JPasswordField contrasenaTextBox;
    javax.swing.JButton entrarBtn;
    javax.swing.JLabel fondoImg;
    javax.swing.JLabel jLabel1;
    javax.swing.JPanel jPanel1;
    javax.swing.JLabel usuarioLbl;
    javax.swing.JTextField usuarioTextBox;
    // End of variables declaration//GEN-END:variables
}
