/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.est.ctrl;


import java.sql.*;
import java.sql.Connection;
import javax.swing.JOptionPane;

/**
 *
 * @author m
 */
public class usuarioD{
    private int id;
    private String login;
    private String password;
    private String tipo;
    private boolean validaLogin;
    private boolean resp;
    Connection conect = null;
        
    public int getId(){
        return id;
    }

    public String getLogin(){
        return login;
    }
    
    public String getPass(){
        return password;
    }
    
    public String getTipo(){
        return tipo;
    }
    
    public boolean setPass(String password, int id){
        try {
          conect = conexionBD.getConexion();  
          Statement st = conect.createStatement();
          st.executeUpdate("UPDATE usuarios SET" 
                            +" password = '"+password+"'"
                            +" WHERE idUsuarios ="+id+";");
          
        } catch(SQLException ex){
            resp = false;
          JOptionPane.showMessageDialog(null, ex, "Error de conexión con la Base de Datos", JOptionPane.ERROR_MESSAGE);
        }finally{
        try {
            conect.close();
            resp = true;
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex, "Error de desconexión", JOptionPane.ERROR_MESSAGE);
        }
    }
      return resp;
      
    }
    
    public boolean validarLogin(String usuario, String password){
        //this.login = usuario; 
        //this.password = password;
        System.out.println(usuario + "-" + password);
        String SSQL="SELECT * FROM usuarios WHERE login = '"+usuario+"' AND password = '"+password+"';";
        System.out.println(SSQL);        
        try {
            conect = conexionBD.getConexion();        
            Statement st = conect.createStatement();            
            ResultSet rs = st.executeQuery(SSQL);
        if(rs.next()){           
            this.id = rs.getInt("idUsuarios");
            this.tipo = rs.getString("nivel");
            this.login = rs.getString("login");
            this.password = rs.getString("password");            
            validaLogin = true;
        }
        else{
            validaLogin = false;
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
        
        return validaLogin;
    }
}
