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
public class preciosD {
    private float hrPrecioAutomovil;
    private float hrPrecioCamioneta;
    private float hrPrecioHotel1;
    private float hrPrecioHotel2;
    private float hrPrecioHotel3;
    private float hrPrecioNocturno;
    private float ticketPerdido;    
    private float noLlave;
    Connection conect = null;
    private boolean resultado;
    
    public preciosD(){
        String SSQL="SELECT * FROM precios;";
        System.out.println(SSQL);        
        try {
            conect = conexionBD.getConexion();        
            Statement st = conect.createStatement();            
            ResultSet rs = st.executeQuery(SSQL);
        while(rs.next()){           
            switch(rs.getInt("idprecios")){
                case 1:
                    this.hrPrecioAutomovil = rs.getFloat("hrPrecio");
                    break;
                case 2:
                    this.hrPrecioCamioneta = rs.getFloat("hrPrecio");
                    break;
                case 3:
                    this.hrPrecioHotel1 = rs.getFloat("hrPrecio");
                    break;
                case 4:
                    this.hrPrecioHotel2 = rs.getFloat("hrPrecio");
                    break;
                case 5:
                    this.hrPrecioHotel3 = rs.getFloat("hrPrecio");
                    break;
                case 6:
                    this.hrPrecioNocturno = rs.getFloat("hrPrecio");
                    break;
                case 7:
                    this.ticketPerdido = rs.getFloat("hrPrecio");
                    break;
                case 8:
                    this.noLlave = rs.getFloat("hrPrecio");
                    break;                
            }                        
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
    
    public float getPrecio(int id){
        switch(id){            
                case 1:
                    return this.hrPrecioAutomovil;                    
                case 2:
                    return this.hrPrecioCamioneta;
                    
                case 3:
                    return this.hrPrecioHotel1;
                    
                case 4:
                    return this.hrPrecioHotel2;
                    
                case 5:
                    return this.hrPrecioHotel3;
                    
                case 6:
                    return this.hrPrecioNocturno;
                    
                case 7:
                    return this.ticketPerdido;
                    
                case 8:
                    return this.noLlave;
            }   
        return 0;
    } 
   
    public boolean updatePrecio(int id, float hrPrecio){
         try {
          conect = conexionBD.getConexion();  
          Statement st = conect.createStatement();
          st.executeUpdate("UPDATE precios SET" 
                            +" hrPrecio = "+hrPrecio+""                           
                            +" WHERE idprecios = "+id+";");
          
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
