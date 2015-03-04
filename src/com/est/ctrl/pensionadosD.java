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
public class pensionadosD {    
    Connection conect = null;
    private final String [] pensionado = {"","","","",""};
    boolean resultado;
           
    
    public String [] getPensionado(String palabra){  
        String SSQL="SELECT * FROM pensionados WHERE idpensionados like '%"+palabra+"%' or nombre like '%"+palabra+"%' or apellidoPaterno like '%"+palabra+"%' or apellidoMaterno like '%"+palabra+"%' or direccion like '%"+palabra+"%' or telefono like '%"+palabra+"%';";        
        System.out.println(SSQL);                
        try {
            conect = conexionBD.getConexion();        
            Statement st = conect.createStatement();            
            ResultSet rs = st.executeQuery(SSQL);                                    
        if(rs.next()){
                pensionado[0] = rs.getString("nombre");
                pensionado[1] = rs.getString("apellidoPaterno");
                pensionado[2] = rs.getString("apellidoMaterno");
                pensionado[3] = rs.getString("direccion");
                pensionado[4] = rs.getString("telefono");
             }                           
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex, "Error de conexión con la Base de Datos", JOptionPane.ERROR_MESSAGE);
        }finally{
            try {
                conect.close();
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, ex, "Error al desconectar la BD", JOptionPane.ERROR_MESSAGE);
            }
        }     
        return this.pensionado;
    } 
    
    public boolean insertPensionado(String nombre, String aPaterno, String aMaterno, String direccion, String telefono){
         try {
          conect = conexionBD.getConexion();  
          Statement st = conect.createStatement();
          st.executeUpdate("INSERT pensionados(nombre,apellidoPaterno,apellidoMaterno,direccion,telefono) VALUES(" 
                            +"'"+nombre+"',"                           
                            +"'"+aPaterno+"',"
                            +"'"+aMaterno+"',"
                            +"'"+direccion+"',"
                            +"'"+telefono+"'"
                            +");");
          
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
   
    public boolean updatePensionado(int id,String nombre, String aPaterno, String aMaterno, String direccion, String telefono){
         try {
          conect = conexionBD.getConexion();  
          Statement st = conect.createStatement();
          st.executeUpdate("UPDATE pensionados SET" 
                            +" nombre = "+nombre+","
                            +" apellidoPaterno = '"+aPaterno+"',"
                            +" apellidoMaterno = '"+aMaterno+"',"
                            +" direccion = '"+direccion+"',"
                            +" telefono = '"+telefono+"'"
                            +" WHERE idpensionados = "+id+";");
          
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
