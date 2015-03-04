package com.est.ctrl;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import giovynet.serial.Baud;
import giovynet.serial.Com;
import giovynet.serial.Parameters;
import java.util.List;
import giovynet.nativelink.SerialPort;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author MAGC
 */
public class conexionCOM {  
      Parameters settings;
      Com puertoCom;

    public conexionCOM() {
          try {
            this.settings = new Parameters();
              //definición del puerto que se va a utilizar
            this.settings.setPort("COM9");
            //definición de la velocidad de impresión, se debe tener en cuenta dicho argumento en las especificacion de velocidad del dispositivo
            this.settings.setBaudRate(Baud._9600);  
            //settings.setMinDelayWrite(100);
            //asignamos los parametros al objeto com            
            this.puertoCom = new Com(settings);
          } catch (Exception ex) {
              //Logger.getLogger(conexionCOM.class.getName()).log(Level.SEVERE, null, ex);
              System.out.println("--Error con la comunicacion de la Placa");
          }
         
    }

    public void cerrarCOM(){
          try {
              this.puertoCom.close();
          } catch (Exception ex) {
              //Logger.getLogger(conexionCOM.class.getName()).log(Level.SEVERE, null, ex);
              System.out.println("Error al cerrar la conexion");
          }
    }
    
    public void envia(String valor, String puerto) throws Exception{                 
        this.puertoCom.sendSingleData(valor);                  
    }
    
    public String read() throws Exception{           
        //String cad = "";    
        //while(!rev.equals("\n") ){
        String rev;    
           rev = this.puertoCom.receiveSingleString();                
        return rev;        
        //    cad += rev;
        //}                    
    }
    
    public void obtenPuertos() throws Exception{
            SerialPort serialPort = new SerialPort();                        
            List<String> portsFree = serialPort.getFreeSerialPort();
            System.out.println("Free RS-232 ports:");
            for (String free : portsFree) {      
                System.out.println(free);
            }            
    }    
    
    /*public static void main(String[] args) throws Exception{
        while(true){
            envia("0");     
        }        
    }*/
    
    
    
}
