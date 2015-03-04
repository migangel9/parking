/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.est.ctrl;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.JOptionPane;

/**
 *
 * @author m
 */
public class negocioD {
    private String nombre;
    private String nombreFiscal;
    private String direccion;
    private String RFC;
    private String porcentajeR;
    private boolean resultado;
    private float iva;
    Connection conect = null;
    
    public negocioD(){
        String SSQL="SELECT * FROM negocio;";
        System.out.println(SSQL);        
        try {
            conect = conexionBD.getConexion();        
            Statement st = conect.createStatement();            
            ResultSet rs = st.executeQuery(SSQL);
        if(rs.next()){           
            this.nombre = rs.getString("nombre");
            this.nombreFiscal = rs.getString("nombreFiscal");
            this.direccion = rs.getString("direccion");
            this.RFC = rs.getString("RFC");                                 
            this.porcentajeR = rs.getString("porcentajeR");    
            this.iva = rs.getFloat("iva");   
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
}
    
    public String getPorcentajeR(){
        return this.porcentajeR;
    }
    
    public String getNombre(){
        return this.nombre;
    }
    
    public String getNombreFiscal(){
        return this.nombreFiscal;
    }
    
    public String getDireccion(){
        return this.direccion;
    }
    
    public float getIva(){
        return this.iva;
    }
    
    public String getRFC(){
        return this.RFC;
    }
    
    public boolean setNombre(){
        return resultado;
    }
    
    public boolean setDireccion(){
        return resultado;
    }
    
    public boolean setRFC(){
        return resultado;
    }
    
    public boolean updateNegocio(String nombre, String nombreFiscal, String direccion, String RFC, String porcentajeR){
         try {
          conect = conexionBD.getConexion();  
          Statement st = conect.createStatement();
          st.executeUpdate("UPDATE negocio SET" 
                            +" nombre = '"+nombre+"',"
                            +" nombreFiscal = '"+nombreFiscal+"',"
                            +" direccion = '"+direccion+"',"
                            +" RFC = '"+RFC+"',"
                            +" porcentajeR = "+porcentajeR+""
                            +" WHERE idnegocio = 1");
          
        } catch(SQLException ex){
            resultado = false;
          JOptionPane.showMessageDialog(null, ex, "Error de conexión con la Base de Datos", JOptionPane.ERROR_MESSAGE);
        }finally{
        try {
            conect.close();
            resultado = true;
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex, "Error de desconexión", JOptionPane.ERROR_MESSAGE);
        }
    }
        return resultado;
    }
    
}
