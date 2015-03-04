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
import java.sql.*;
import javax.swing.JOptionPane;


public class conexionBD {
    public static String db = "estacionamiento";
    public static String url = "jdbc:mysql://localhost/"+db;
    public static String user = "root";
    public static String pass = "";//"ELLEP@$$w0rd";


   public static Connection getConexion(){
       Connection link = null;
       try{
           Class.forName("com.mysql.jdbc.Driver");
           link = DriverManager.getConnection(url, user, pass);
       }catch(Exception ex){
           JOptionPane.showMessageDialog(null, ex);
       }
       return link;
   }
}