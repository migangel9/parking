/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.est.views;

import com.est.ctrl.*;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.Authenticator;
import java.net.MalformedURLException;
import java.net.PasswordAuthentication;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author m
 */
public class viewPrincipal extends javax.swing.JFrame {        
    conexionCOM conexionCom = new conexionCOM();
    hiloEscuchaCOM elHilo = new hiloEscuchaCOM(this.conexionCom, this);          
    negocioD negocio = new negocioD();
    public static usuarioD user;      
    private final String pathFotos = "data\\";
    private final String usuario = "admin";
    private final String contra = "admin";
    private final String FRENTE = "http://192.168.1.88/tmpfs/auto.jpg";
    private final String ATRAS = "http://192.168.1.89/tmpfs/auto.jpg";    
    String hrE;
    String hrP;
    String hrPregunta;
    String hrEntrada;
    float subtotal;
    float iva;
    long hrsCobradas;    
    boolean promo = false;
    Connection conect = null;
    DefaultTableModel modeloEntradas;    
    DefaultTableModel modeloSalidas;
    DefaultTableModel modeloSalidasF;
    
    /**
     * Creates new form viewPrincipal
     */
    
    private void inicializaCamaras(){
        this.panelVideoCam_21.setUsuario(this.usuario);
        this.panelVideoCam_21.setContra(this.contra);
        this.panelVideoCam_21.setHayConexion(true);
        this.panelVideoCam_21.setDireccionIP(this.FRENTE);
        this.panelVideoCam_21.revalidate();
        this.panelVideoCam_22.setUsuario(this.usuario);
        this.panelVideoCam_22.setContra(this.contra);
        this.panelVideoCam_22.setHayConexion(true);
        this.panelVideoCam_22.setDireccionIP(this.ATRAS);
        this.panelVideoCam_22.revalidate();
    }
    
    public viewPrincipal(usuarioD usuario) {
        user = usuario;
        initComponents();
        this.modeloEntradas = (DefaultTableModel) tablaEntradas.getModel();
        this.modeloSalidas = (DefaultTableModel) tablaSalidas.getModel();
        this.modeloSalidasF = (DefaultTableModel) tablaFotoSalida.getModel();
        this.lblContadorE.setText("0");
        this.lblContadorS.setText("0");        
        String tipo = user.getTipo();
        checkPromo.setEnabled(false);
        switch(tipo){
            case "admin":
                    menuConfiguracion.setVisible(false);
                    menuEliminarPensionado.setVisible(false);
                    menuReporteFiscal.setVisible(false);
                break;   
            case "adminf":
                    menuConfiguracion.setVisible(false);
                    menuEliminarPensionado.setVisible(false);                    
                break;  
            case "empleado":
                    menuPensionado.setVisible(false);
                    menuConfiguracion.setVisible(false);
                    menuReportes.setVisible(false);
                break;  
        }           
        this.actualizarFrame();
        this.inicializaCamaras();        
        elHilo.start();                    
    }
    
    private int getNumFilas(ResultSet resultSet) {
        if (resultSet == null) {
            return 0;
        }
        try {
            resultSet.last();
            return resultSet.getRow();
        } catch (SQLException exp) {
            exp.printStackTrace();
        } finally {
            try {
                resultSet.beforeFirst();
            } catch (SQLException exp) {
                exp.printStackTrace();
            }
        }
        return 0;
    }
    
    public void getContadores() {
        String SSQL="SELECT * FROM registros WHERE cancelado = 0 AND idcorte is NULL;";
        String SSQL2="SELECT * FROM registros WHERE pago is not null AND idcorte is NULL;";
        System.out.println(SSQL);        
        System.out.println(SSQL2);
        try {
            conect = conexionBD.getConexion();        
            Statement st = conect.createStatement();            
            Statement st2 = conect.createStatement();            
            ResultSet rs = st.executeQuery(SSQL);
            ResultSet rs2 = st2.executeQuery(SSQL2);
            int entrada = getNumFilas(rs);
            int salida = getNumFilas(rs2);
            System.out.println(entrada+" - "+salida);
            lblContadorE.setText(Integer.toString(entrada));
            lblContadorS.setText(Integer.toString(salida));
            
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
    
    public String getFechaActual() {
        Date ahora = new Date();
        SimpleDateFormat formateador = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return formateador.format(ahora);
    }
    
    public String getHrActual() {
        Date ahora = new Date();
        SimpleDateFormat formateador = new SimpleDateFormat("HH:mm:ss");
        return formateador.format(ahora);
    }
                
    public void getRegistrosE(){
        String SSQL="SELECT * FROM registros WHERE idcorte is NULL;";
        System.out.println(SSQL);        
        try {
            conect = conexionBD.getConexion();        
            Statement st = conect.createStatement();            
            ResultSet rs = st.executeQuery(SSQL);
            int nroFilas = getNumFilas(rs);
            System.out.println(Integer.toString(nroFilas));     
            int i = 0;
            limpiarTabla(modeloEntradas);
        while(rs.next()){           
             System.out.println(rs.getString("idRegistros")+"-"+rs.getString("tipoAuto"));                                
              modeloEntradas.addRow(new Object[nroFilas]);               
              tablaEntradas.setValueAt(rs.getString("folio"), i, 0);
              tablaEntradas.setValueAt(rs.getString("tipoAuto"), i, 1);              
              if(rs.getBoolean("cancelado") == true){
                tablaEntradas.setValueAt("Cancelado por el usuario", i, 2);  
              }
              else{
                tablaEntradas.setValueAt("No", i, 2);  
              }
              tablaEntradas.setValueAt(rs.getTime("hrEntrada"), i, 3);  
              i++;
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
    
    public void muestraFoto(String nombreArchivo){        
        ImageIcon fotoParaMostrar = new ImageIcon(".\\" + nombreArchivo);        
        this.lblfoto.setIcon(fotoParaMostrar);        
    }
    
    float roundDosDecimales(double d) {
        DecimalFormat twoDForm = new DecimalFormat("#.##");
        return Float.valueOf(twoDForm.format(d));
    }

private int compararHoras(String hrE, String hrS) {
        int horas = 0;
        String horaEntrada = hrE;  // hr entrada
        String horaInicio = "21:00:00";
        String horaSalida = hrS;   // hr salida
        String horaFinal = "9:00:00";
        try {            
                DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
                Date horaIni;
                Date horaFin;
                horaIni = dateFormat.parse(horaEntrada);
                horaFin = dateFormat.parse(horaInicio);                
                               
                if (horaIni.compareTo(horaFin) < 0) {
                    System.out.println("antes");     
                    
                    String[] h1 = horaEntrada.split(":");
                    String[] h2 = horaInicio.split(":");
                    int resto = 0;
                    int segundo = Integer.valueOf(h2[2]) - Integer.valueOf(h1[2]);
                    if (segundo < 0){
                       resto = 1;
                       segundo = 60 + segundo;
                    }
                    int minuto = (Integer.valueOf(h2[1]) - Integer.valueOf(h1[1])) - resto;
                    resto = 0;
                    if (minuto < 0){
                       minuto = 60 + minuto;
                       resto = 1;
                    }
                    int hora = (Integer.valueOf(h2[0]) - Integer.valueOf(h1[0])) - resto;
                    System.out.println("Diferencia= "+hora+":"+minuto+":"+segundo); 
                    if(minuto >= 10){
                        horas++;
                    }
                        horas += hora;
                }   
                
                Date horaIni2;
                Date horaFin2;
                horaIni2 = dateFormat.parse(horaSalida);
                horaFin2 = dateFormat.parse(horaFinal); 
                if (horaIni2.compareTo(horaFin2) > 0) {
                    System.out.println("despues");     
                    
                    String[] h1 = horaFinal.split(":");
                    String[] h2 = horaSalida.split(":");
                    int resto = 0;
                    int segundo = Integer.valueOf(h2[2]) - Integer.valueOf(h1[2]);
                    if (segundo < 0){
                       resto = 1;
                       segundo = 60 + segundo;
                    }
                    int minuto = (Integer.valueOf(h2[1]) - Integer.valueOf(h1[1])) - resto;
                    resto = 0;
                    if (minuto < 0){
                       minuto = 60 + minuto;
                       resto = 1;
                    }
                    int hora = (Integer.valueOf(h2[0]) - Integer.valueOf(h1[0])) - resto;
                    System.out.println("Diferencia= "+hora+":"+minuto+":"+segundo);
                    if(minuto >= 10){
                        horas++;
                    }
                        horas += hora;
                }
        } catch (ParseException ex) {
            //Logger.getLogger(Main2.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("Errores de fecha");            
        }
        
        System.out.println("Las horas a cobrar son = "+horas);
        return horas;
}    
    
    public void getTicket(String folio){  
        long hrsxCobrar;
        checkComprobante.setSelected(false);
        checkPromo.setSelected(false);
        checkNoDejoLlave.setSelected(false);
        checkTicketPerdido.setSelected(false);
        preciosD precios = new preciosD();                
        //String hrE;
        long hrs;
        long min;        
        float total = 0;
        lblFolio.setText(folio);                
        String SSQL="SELECT * FROM registros where folio ='"+folio+"';";
        System.out.println(SSQL);        
        try {
            conect = conexionBD.getConexion();
            Statement st = conect.createStatement();
            ResultSet rs = st.executeQuery(SSQL);
            if(rs.next()){
                if(rs.getString("pago") != null || rs.getBoolean("cancelado") == true){
                        btnCobrar.setEnabled(false);
                        checkTicketPerdido.setEnabled(false);
                        checkPromo.setEnabled(false);
                        checkNoDejoLlave.setEnabled(false);
                        checkComprobante.setEnabled(false);
                        lblPagado.setVisible(true);                        
                        lblTiempo.setText(rs.getString("tiempTranscurrido"));
                        lblTotal.setText(String.valueOf(total));  
                        btnReimprimir.setEnabled(true);
                        muestraFoto(rs.getString("rutaFotoEntrada"));
                        this.hrEntrada = rs.getString("hrEntrada");
                        this.hrPregunta = rs.getString("hrSalida");                        
                        this.iva = rs.getFloat("iva");
                        this.subtotal = rs.getFloat("subtotal");                        
                        System.out.println("iva "+negocio.getIva());
                        System.out.println("sub "+subtotal);
                        System.out.println("iva "+iva);
                        lblHrEntrada.setText(rs.getTime("hrEntrada").toString());
                        lblTipo.setText(rs.getString("tipoAuto"));
                        lblTotal.setText(rs.getString("pago"));
                }
                else{
                    btnReimprimir.setEnabled(false);
                    checkTicketPerdido.setEnabled(true);
                    checkNoDejoLlave.setEnabled(true);
                    checkComprobante.setEnabled(true);
                    btnCobrar.setEnabled(true);
                    lblPagado.setVisible(false);                
                    this.hrPregunta = getFechaActual(); 
                    this.hrP = getHrActual();
                    this.hrEntrada = rs.getString("hrEntrada");                    
                    hrE = rs.getTime("hrEntrada").toString();                    
                    muestraFoto(rs.getString("rutaFotoEntrada"));
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    try
                    {   
                        Calendar fecha1 = Calendar.getInstance();
                        Calendar fecha2 = Calendar.getInstance();
                        fecha1.setTime(simpleDateFormat.parse(hrEntrada));
                        fecha2.setTime(simpleDateFormat.parse(hrPregunta));
                        //System.out.println(fecha1.getTime()+" --------- "+fecha2.getTime());
                        long milis1 = fecha1.getTimeInMillis();
                        long milis2 = fecha2.getTimeInMillis();
                        long diff = milis2 - milis1;
                        long difMin = diff / (60 * 1000);
                        hrs = difMin / 60;
                        hrsxCobrar = hrs;
                        min = difMin % 60;
                        if(min > 10){
                            hrsxCobrar += 1;                            
                        }
                        this.hrsCobradas = hrsxCobrar;
                        if(this.hrsCobradas >= 6){
                            checkPromo.setEnabled(true);
                        }
                        //System.out.println("milis1:"+milis1+" --------- Milis2:"+milis2+" dif:"+diff);               
                        System.out.println("En minutos: " + min + " minutos.");
                        System.out.println("En horas: " + hrs + " horas.");                

                            switch(rs.getInt("precios_idprecios")){
                            case 1:
                                total = hrsxCobrar * precios.getPrecio(1);
                                break;
                            case 2:
                                total = hrsxCobrar * precios.getPrecio(2);
                                break;
                            case 3:
                                total = hrsxCobrar * precios.getPrecio(3);
                                break;
                            case 4:
                                total = hrsxCobrar * precios.getPrecio(4);
                                break;
                            case 5:
                                total = hrsxCobrar * precios.getPrecio(5);
                                break;     
                            case 6:
                                total = precios.getPrecio(6);
                                break;     
                            case 9:
                                total = hrsxCobrar * precios.getPrecio(9);
                                break;
                            }                            
                        switch (rs.getString("tipoAuto")) {
                            case "La.Alhondiga":
                            case "Puebla.de.Antano":
                                this.iva = (float) 0.00;  //
                                this.subtotal = (float) 0.00;
                                total = total + iva;
                                break;                            
                            default:
                                this.iva = (total * negocio.getIva())/116;
                                this.subtotal = total - this.iva;
                                this.iva = roundDosDecimales(this.iva);
                                this.subtotal = roundDosDecimales(this.subtotal);
                                
                                break;
                        }
                        lblTiempo.setText(hrs+" Hrs. "+min+" Min.");
                        lblTotal.setText(String.valueOf(total));

                    }
                    catch (ParseException ex)
                    {
                        System.out.println("Exception "+ex);
                    }                                

                 System.out.println(rs.getString("folio")+"-"+rs.getString("tipoAuto"));                        
                 lblHrEntrada.setText(hrE);                      
                 lblTipo.setText(rs.getString("tipoAuto"));
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
        txtTicket.setText("");
    }
    
     public void getFoto(String folio){  
        lblFolio.setText("");
        lblTipo.setText("");
        lblHrEntrada.setText("");
        lblTiempo.setText("");
        lblTotal.setText("");
        btnCobrar.setEnabled(false);
        checkTicketPerdido.setEnabled(false);
        checkNoDejoLlave.setEnabled(false);
        checkComprobante.setEnabled(false);           
        String SSQL="SELECT * FROM registros_salida where idRegistroSalida ='"+folio+"';";
        System.out.println(SSQL);        
        try {
            conect = conexionBD.getConexion();
            Statement st = conect.createStatement();
            ResultSet rs = st.executeQuery(SSQL);
            if(rs.next()){
                    muestraFoto(rs.getString("ruta"));
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
    
    
    private void limpiarTabla(DefaultTableModel modeloT){        
        for (int i = 0; i < modeloT.getRowCount(); i++) {
            modeloT.removeRow(i);
            i-=1;
        }
    }
    
    private void getRegistrosS(){
        String SSQL="SELECT * FROM registros WHERE pago is not null AND idcorte is NULL;";
        System.out.println(SSQL);        
        try {
            conect = conexionBD.getConexion();        
            Statement st = conect.createStatement();            
            ResultSet rs = st.executeQuery(SSQL);
            int nroFilas = rs.getFetchSize();
            int i = 0;
            limpiarTabla(modeloSalidas);
        while(rs.next()){           
             System.out.println(rs.getString("idRegistros")+"-"+rs.getString("tipoAuto"));    
              modeloSalidas.addRow(new Object[nroFilas]);               
              tablaSalidas.setValueAt(rs.getString("folio"), i, 0);
              tablaSalidas.setValueAt(rs.getString("tipoAuto"), i, 1);                              
              tablaSalidas.setValueAt(rs.getTime("hrEntrada"), i, 2);  
              tablaSalidas.setValueAt(rs.getTime("hrSalida"), i, 3);  
              tablaSalidas.setValueAt(rs.getFloat("pago"), i, 4);  
              i++; 
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

    private void getRegistrosF(){
        String SSQL="SELECT * FROM registros_salida WHERE idcorte is NULL;";
        System.out.println(SSQL);        
        try {
            conect = conexionBD.getConexion();        
            Statement st = conect.createStatement();            
            ResultSet rs = st.executeQuery(SSQL);
            int nroFilas = rs.getFetchSize();
            int i = 0;
            limpiarTabla(modeloSalidasF);
        while(rs.next()){                         
              modeloSalidasF.addRow(new Object[nroFilas]);               
              tablaFotoSalida.setValueAt(rs.getString("idRegistroSalida"), i, 0);
              tablaFotoSalida.setValueAt(rs.getString("fecha"), i, 1);
              tablaFotoSalida.setValueAt(rs.getString("ruta").replace("data/", "").replace(".ppj","").replace("_", ""), i, 2);                                            
              i++; 
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
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel5 = new javax.swing.JPanel();
        jButton4 = new javax.swing.JButton();
        abrirPlumaS = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        jLayeredPane2 = new javax.swing.JLayeredPane();
        etiqueta1 = new javax.swing.JLabel();
        panelVideoCam_22 = new com.est.views.PanelVideoCam_2();
        jLabel5 = new javax.swing.JLabel();
        lblContadorS = new javax.swing.JLabel();
        btnEntraS = new javax.swing.JButton();
        jPanel4 = new javax.swing.JPanel();
        jLayeredPane1 = new javax.swing.JLayeredPane();
        panelVideoCam_21 = new com.est.views.PanelVideoCam_2();
        jLabel1 = new javax.swing.JLabel();
        abrirPlumaE = new javax.swing.JButton();
        cerrarPlumaE = new javax.swing.JButton();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jScrollPane1 = new javax.swing.JScrollPane();
        tablaEntradas = new javax.swing.JTable();
        jScrollPane2 = new javax.swing.JScrollPane();
        tablaSalidas = new javax.swing.JTable();
        jScrollPane3 = new javax.swing.JScrollPane();
        tablaFotoSalida = new javax.swing.JTable();
        jLabel4 = new javax.swing.JLabel();
        lblContadorE = new javax.swing.JLabel();
        btnEntraE = new javax.swing.JButton();
        error = new javax.swing.JLabel();
        jPanel6 = new javax.swing.JPanel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        txtTicket = new javax.swing.JTextField();
        btnBuscarFolio = new javax.swing.JButton();
        jLabel11 = new javax.swing.JLabel();
        lblTipo = new javax.swing.JLabel();
        lblHrEntrada = new javax.swing.JLabel();
        lblTiempo = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        lblTotal = new javax.swing.JLabel();
        btnCobrar = new javax.swing.JButton();
        checkTicketPerdido = new javax.swing.JCheckBox();
        jLabel6 = new javax.swing.JLabel();
        lblFolio = new javax.swing.JLabel();
        lblfoto = new javax.swing.JLabel();
        lblPagado = new javax.swing.JLabel();
        checkNoDejoLlave = new javax.swing.JCheckBox();
        btnReimprimir = new javax.swing.JButton();
        checkComprobante = new javax.swing.JCheckBox();
        checkPromo = new javax.swing.JCheckBox();
        jMenuBar1 = new javax.swing.JMenuBar();
        menuCorteCaja = new javax.swing.JMenu();
        menuPensionado = new javax.swing.JMenu();
        menuAgregarPensionado = new javax.swing.JMenuItem();
        menuEliminarPensionado = new javax.swing.JMenuItem();
        menuReportes = new javax.swing.JMenu();
        menuReporteEstadistico = new javax.swing.JMenuItem();
        menuReporteFiscal = new javax.swing.JMenuItem();
        menuConfiguracion = new javax.swing.JMenu();
        menuConfigSistema = new javax.swing.JMenuItem();
        menuConfigContrasenas = new javax.swing.JMenuItem();
        menuCambioUsuario = new javax.swing.JMenu();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Estacionamiento");
        setAutoRequestFocus(false);
        setBackground(new java.awt.Color(255, 255, 255));
        setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        setExtendedState(6);
        setIconImage(getIconImage());
        setModalExclusionType(java.awt.Dialog.ModalExclusionType.APPLICATION_EXCLUDE);

        jPanel5.setBackground(new java.awt.Color(255, 255, 255));
        jPanel5.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jButton4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/Down32.png"))); // NOI18N
        jButton4.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        abrirPlumaS.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/Up32.png"))); // NOI18N
        abrirPlumaS.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        abrirPlumaS.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                abrirPlumaSActionPerformed(evt);
            }
        });

        jLabel2.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel2.setText("Salida");

        jLayeredPane2.setBorder(javax.swing.BorderFactory.createTitledBorder("Camara Salida"));
        jLayeredPane2.setPreferredSize(new java.awt.Dimension(512, 384));

        etiqueta1.setPreferredSize(new java.awt.Dimension(500, 275));

        javax.swing.GroupLayout panelVideoCam_22Layout = new javax.swing.GroupLayout(panelVideoCam_22);
        panelVideoCam_22.setLayout(panelVideoCam_22Layout);
        panelVideoCam_22Layout.setHorizontalGroup(
            panelVideoCam_22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 496, Short.MAX_VALUE)
        );
        panelVideoCam_22Layout.setVerticalGroup(
            panelVideoCam_22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 271, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jLayeredPane2Layout = new javax.swing.GroupLayout(jLayeredPane2);
        jLayeredPane2.setLayout(jLayeredPane2Layout);
        jLayeredPane2Layout.setHorizontalGroup(
            jLayeredPane2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jLayeredPane2Layout.createSequentialGroup()
                .addComponent(etiqueta1, javax.swing.GroupLayout.DEFAULT_SIZE, 0, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(panelVideoCam_22, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jLayeredPane2Layout.setVerticalGroup(
            jLayeredPane2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jLayeredPane2Layout.createSequentialGroup()
                .addGroup(jLayeredPane2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(etiqueta1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jLayeredPane2Layout.createSequentialGroup()
                        .addComponent(panelVideoCam_22, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jLayeredPane2.setLayer(etiqueta1, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jLayeredPane2.setLayer(panelVideoCam_22, javax.swing.JLayeredPane.DEFAULT_LAYER);

        jLabel5.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel5.setText("# Autos");

        lblContadorS.setFont(new java.awt.Font("Tahoma", 1, 44)); // NOI18N
        lblContadorS.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblContadorS.setText("18");

        btnEntraS.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        btnEntraS.setText("Entra Auto");
        btnEntraS.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnEntraS.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEntraSActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addComponent(jLayeredPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 524, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel5Layout.createSequentialGroup()
                                .addGap(26, 26, 26)
                                .addComponent(jLabel2))
                            .addGroup(jPanel5Layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 8, Short.MAX_VALUE)
                                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(abrirPlumaS, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 108, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(btnEntraS, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jButton4, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 107, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addContainerGap(20, Short.MAX_VALUE))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel5Layout.createSequentialGroup()
                                .addGap(29, 29, 29)
                                .addComponent(jLabel5))
                            .addGroup(jPanel5Layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(lblContadorS, javax.swing.GroupLayout.PREFERRED_SIZE, 113, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnEntraS, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(abrirPlumaS)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton4)
                .addGap(8, 8, 8)
                .addComponent(jLabel5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblContadorS)
                .addContainerGap(31, Short.MAX_VALUE))
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addComponent(jLayeredPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 312, Short.MAX_VALUE)
                .addContainerGap())
        );

        jPanel4.setBackground(new java.awt.Color(255, 255, 255));
        jPanel4.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jLayeredPane1.setBackground(new java.awt.Color(255, 255, 255));
        jLayeredPane1.setBorder(javax.swing.BorderFactory.createTitledBorder("Camara Entrada"));
        jLayeredPane1.setPreferredSize(new java.awt.Dimension(512, 300));

        javax.swing.GroupLayout panelVideoCam_21Layout = new javax.swing.GroupLayout(panelVideoCam_21);
        panelVideoCam_21.setLayout(panelVideoCam_21Layout);
        panelVideoCam_21Layout.setHorizontalGroup(
            panelVideoCam_21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 496, Short.MAX_VALUE)
        );
        panelVideoCam_21Layout.setVerticalGroup(
            panelVideoCam_21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 271, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jLayeredPane1Layout = new javax.swing.GroupLayout(jLayeredPane1);
        jLayeredPane1.setLayout(jLayeredPane1Layout);
        jLayeredPane1Layout.setHorizontalGroup(
            jLayeredPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jLayeredPane1Layout.createSequentialGroup()
                .addComponent(panelVideoCam_21, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 11, Short.MAX_VALUE))
        );
        jLayeredPane1Layout.setVerticalGroup(
            jLayeredPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jLayeredPane1Layout.createSequentialGroup()
                .addComponent(panelVideoCam_21, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 17, Short.MAX_VALUE))
        );
        jLayeredPane1.setLayer(panelVideoCam_21, javax.swing.JLayeredPane.DEFAULT_LAYER);

        jLabel1.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel1.setText("Entrada");

        abrirPlumaE.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/Up32.png"))); // NOI18N
        abrirPlumaE.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        abrirPlumaE.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                abrirPlumaEActionPerformed(evt);
            }
        });

        cerrarPlumaE.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/Down32.png"))); // NOI18N
        cerrarPlumaE.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        cerrarPlumaE.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cerrarPlumaEActionPerformed(evt);
            }
        });

        jTabbedPane1.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N

        tablaEntradas.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        tablaEntradas.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Folio", "Tipo de Auto", "Cancelado", "Hora de entrada"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tablaEntradas.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tablaEntradasMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tablaEntradas);

        jTabbedPane1.addTab("Registro de entradas", jScrollPane1);

        tablaSalidas.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Folio", "Tipo de Auto", "Hora de entrada", "Hora de Salida", "Cobro"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tablaSalidas.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tablaSalidasMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(tablaSalidas);

        jTabbedPane1.addTab("Registro de Pagos", jScrollPane2);

        tablaFotoSalida.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID", "Fecha/Hr", "Foto"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tablaFotoSalida.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tablaFotoSalidaMouseClicked(evt);
            }
        });
        jScrollPane3.setViewportView(tablaFotoSalida);

        jTabbedPane1.addTab("Fotos de Salidas", jScrollPane3);

        jLabel4.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel4.setText("# Autos");

        lblContadorE.setFont(new java.awt.Font("Tahoma", 1, 44)); // NOI18N
        lblContadorE.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblContadorE.setText("18");

        btnEntraE.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        btnEntraE.setText("Entra Auto");
        btnEntraE.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnEntraE.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEntraEActionPerformed(evt);
            }
        });

        error.setText(" ");

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(jTabbedPane1)
                        .addGap(6, 6, 6))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addGap(516, 516, 516)
                                .addComponent(lblContadorE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addComponent(jLayeredPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 523, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(btnEntraE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(abrirPlumaE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addGroup(jPanel4Layout.createSequentialGroup()
                                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addGroup(jPanel4Layout.createSequentialGroup()
                                                .addGap(13, 13, 13)
                                                .addComponent(jLabel1))
                                            .addComponent(cerrarPlumaE, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addGroup(jPanel4Layout.createSequentialGroup()
                                                .addGap(18, 18, 18)
                                                .addComponent(jLabel4)))
                                        .addGap(0, 0, Short.MAX_VALUE)))))
                        .addGap(12, 12, 12))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(error)
                        .addGap(0, 0, Short.MAX_VALUE))))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnEntraE, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(abrirPlumaE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cerrarPlumaE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblContadorE))
                    .addComponent(jLayeredPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 315, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 399, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(error)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel6.setBackground(new java.awt.Color(255, 255, 255));
        jPanel6.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 102, 255), 6, true));

        jLabel9.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        jLabel9.setText("Hr. de entrada:");

        jLabel10.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        jLabel10.setText("Tiempo:");

        jLabel8.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        jLabel8.setText("Tipo de Auto:");

        jLabel3.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        jLabel3.setText("Ticket:");

        txtTicket.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        txtTicket.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));
        txtTicket.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtTicketKeyPressed(evt);
            }
        });

        btnBuscarFolio.setBackground(new java.awt.Color(255, 255, 255));
        btnBuscarFolio.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/search32.png"))); // NOI18N
        btnBuscarFolio.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnBuscarFolio.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBuscarFolioActionPerformed(evt);
            }
        });

        jLabel11.setFont(new java.awt.Font("Tahoma", 1, 36)); // NOI18N
        jLabel11.setText("Total:");

        lblTipo.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        lblTipo.setForeground(new java.awt.Color(0, 0, 204));
        lblTipo.setText("Automovil");

        lblHrEntrada.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        lblHrEntrada.setForeground(new java.awt.Color(0, 0, 204));
        lblHrEntrada.setText("00:00:00");

        lblTiempo.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        lblTiempo.setForeground(new java.awt.Color(0, 0, 204));
        lblTiempo.setText("0 Hrs. 00 Min.");

        jLabel15.setFont(new java.awt.Font("Tahoma", 1, 48)); // NOI18N
        jLabel15.setForeground(new java.awt.Color(0, 0, 153));
        jLabel15.setText("$");

        lblTotal.setFont(new java.awt.Font("Tahoma", 1, 48)); // NOI18N
        lblTotal.setForeground(new java.awt.Color(0, 0, 153));
        lblTotal.setText("0.00");

        btnCobrar.setFont(new java.awt.Font("Tahoma", 0, 36)); // NOI18N
        btnCobrar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/64_cashbox.png"))); // NOI18N
        btnCobrar.setText("Cobrar");
        btnCobrar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnCobrar.setEnabled(false);
        btnCobrar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnCobrarMouseClicked(evt);
            }
        });
        btnCobrar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCobrarActionPerformed(evt);
            }
        });

        checkTicketPerdido.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        checkTicketPerdido.setText("Ticket Perdido");
        checkTicketPerdido.setToolTipText("Seleccione en caso de que el cliente perdio el ticket");
        checkTicketPerdido.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        checkTicketPerdido.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                checkTicketPerdidoStateChanged(evt);
            }
        });
        checkTicketPerdido.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                checkTicketPerdidoActionPerformed(evt);
            }
        });

        jLabel6.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        jLabel6.setText("Folio:");

        lblFolio.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        lblFolio.setForeground(new java.awt.Color(0, 0, 204));
        lblFolio.setText("00000000");

        lblPagado.setFont(new java.awt.Font("Tahoma", 1, 20)); // NOI18N
        lblPagado.setForeground(new java.awt.Color(0, 102, 0));
        lblPagado.setText("Ticket Pagado");

        checkNoDejoLlave.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        checkNoDejoLlave.setText("No Dejo llave");
        checkNoDejoLlave.setToolTipText("Seleccione en caso de que el cliente perdio el ticket");
        checkNoDejoLlave.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        checkNoDejoLlave.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                checkNoDejoLlaveStateChanged(evt);
            }
        });
        checkNoDejoLlave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                checkNoDejoLlaveActionPerformed(evt);
            }
        });

        btnReimprimir.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N
        btnReimprimir.setText("Reimprimir");
        btnReimprimir.setEnabled(false);
        btnReimprimir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnReimprimirActionPerformed(evt);
            }
        });

        checkComprobante.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N
        checkComprobante.setText("Comprobante");

        checkPromo.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        checkPromo.setText("Promoción Noche");
        checkPromo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                checkPromoActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel10)
                        .addGap(18, 18, 18)
                        .addComponent(lblTiempo))
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGap(24, 24, 24)
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel6Layout.createSequentialGroup()
                                .addGap(10, 10, 10)
                                .addComponent(jLabel8)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(lblTipo))
                            .addGroup(jPanel6Layout.createSequentialGroup()
                                .addGap(52, 52, 52)
                                .addComponent(jLabel6)
                                .addGap(18, 18, 18)
                                .addComponent(lblFolio))
                            .addGroup(jPanel6Layout.createSequentialGroup()
                                .addComponent(jLabel3)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(txtTicket, javax.swing.GroupLayout.PREFERRED_SIZE, 213, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(btnBuscarFolio, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel6Layout.createSequentialGroup()
                                .addComponent(jLabel11)
                                .addGap(18, 18, 18)
                                .addComponent(jLabel15)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(lblTotal))
                            .addGroup(jPanel6Layout.createSequentialGroup()
                                .addComponent(jLabel9)
                                .addGap(18, 18, 18)
                                .addComponent(lblHrEntrada))))
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(checkTicketPerdido)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(checkNoDejoLlave))
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGap(69, 69, 69)
                        .addComponent(checkPromo)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(btnCobrar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(jPanel6Layout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addComponent(checkComprobante)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(btnReimprimir)))
                        .addGap(47, 47, 47))
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblfoto, javax.swing.GroupLayout.PREFERRED_SIZE, 261, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel6Layout.createSequentialGroup()
                                .addGap(44, 44, 44)
                                .addComponent(lblPagado)))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGap(7, 7, 7)
                        .addComponent(lblfoto, javax.swing.GroupLayout.PREFERRED_SIZE, 199, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btnReimprimir)
                            .addComponent(checkComprobante))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnCobrar)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(lblPagado))
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGap(23, 23, 23)
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(txtTicket, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel3))
                            .addComponent(btnBuscarFolio, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel6)
                            .addComponent(lblFolio))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel8)
                            .addComponent(lblTipo))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel9)
                            .addComponent(lblHrEntrada))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel10)
                            .addComponent(lblTiempo))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel15)
                            .addComponent(jLabel11)
                            .addComponent(lblTotal))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(checkTicketPerdido)
                            .addComponent(checkNoDejoLlave))
                        .addGap(18, 18, 18)
                        .addComponent(checkPromo)))
                .addContainerGap(31, Short.MAX_VALUE))
        );

        jMenuBar1.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N

        menuCorteCaja.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/32_cash_register.png"))); // NOI18N
        menuCorteCaja.setText("Corte Caja");
        menuCorteCaja.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        menuCorteCaja.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                menuCorteCajaMouseClicked(evt);
            }
        });
        jMenuBar1.add(menuCorteCaja);

        menuPensionado.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/group.png"))); // NOI18N
        menuPensionado.setText("Pensionados");
        menuPensionado.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));

        menuAgregarPensionado.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/add-user.png"))); // NOI18N
        menuAgregarPensionado.setText("Agregar Pensionado");
        menuAgregarPensionado.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuAgregarPensionadoActionPerformed(evt);
            }
        });
        menuPensionado.add(menuAgregarPensionado);

        menuEliminarPensionado.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/delete-user.png"))); // NOI18N
        menuEliminarPensionado.setText("Eliminar Pensionado");
        menuEliminarPensionado.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuEliminarPensionadoActionPerformed(evt);
            }
        });
        menuPensionado.add(menuEliminarPensionado);

        jMenuBar1.add(menuPensionado);

        menuReportes.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/ordering.png"))); // NOI18N
        menuReportes.setText("Reportes");
        menuReportes.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));

        menuReporteEstadistico.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/order-history.png"))); // NOI18N
        menuReporteEstadistico.setText("Reporte Estadistico");
        menuReporteEstadistico.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuReporteEstadisticoActionPerformed(evt);
            }
        });
        menuReportes.add(menuReporteEstadistico);

        menuReporteFiscal.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/Rank-History.png"))); // NOI18N
        menuReporteFiscal.setText("Reporte Fiscal");
        menuReporteFiscal.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuReporteFiscalActionPerformed(evt);
            }
        });
        menuReportes.add(menuReporteFiscal);

        jMenuBar1.add(menuReportes);

        menuConfiguracion.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/setting.png"))); // NOI18N
        menuConfiguracion.setText("Configuración");
        menuConfiguracion.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));

        menuConfigSistema.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/config_32.png"))); // NOI18N
        menuConfigSistema.setText("Configuración del Sistema");
        menuConfigSistema.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuConfigSistemaActionPerformed(evt);
            }
        });
        menuConfiguracion.add(menuConfigSistema);

        menuConfigContrasenas.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/signin_-32.png"))); // NOI18N
        menuConfigContrasenas.setText("Cambio de contraseñas");
        menuConfigContrasenas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuConfigContrasenasActionPerformed(evt);
            }
        });
        menuConfiguracion.add(menuConfigContrasenas);

        jMenuBar1.add(menuConfiguracion);

        menuCambioUsuario.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/logout.png"))); // NOI18N
        menuCambioUsuario.setText("Cambio de Usuario");
        menuCambioUsuario.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                menuCambioUsuarioMouseClicked(evt);
            }
        });
        jMenuBar1.add(menuCambioUsuario);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, 656, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, 742, Short.MAX_VALUE))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void menuAgregarPensionadoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuAgregarPensionadoActionPerformed
        // TODO add your handling code here:
        viewAgregarPensionado pensionado = new viewAgregarPensionado(new javax.swing.JDialog(), true);        
        pensionado.setVisible(true);        
    }//GEN-LAST:event_menuAgregarPensionadoActionPerformed

    private void menuEliminarPensionadoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuEliminarPensionadoActionPerformed
        // TODO add your handling code here:
        viewEliminarPensionado pensionadoEliminar = new viewEliminarPensionado(new javax.swing.JDialog(), true);        
        pensionadoEliminar.setVisible(true);
    }//GEN-LAST:event_menuEliminarPensionadoActionPerformed

    private void menuReporteEstadisticoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuReporteEstadisticoActionPerformed
        // TODO add your handling code here:
         viewReporteEst reporteEst = new viewReporteEst(new javax.swing.JDialog(), true);        
         reporteEst.setVisible(true);
    }//GEN-LAST:event_menuReporteEstadisticoActionPerformed

    private void menuReporteFiscalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuReporteFiscalActionPerformed
        // TODO add your handling code here:
         viewReporteFis reporteFis = new viewReporteFis(new javax.swing.JDialog(), true);        
         reporteFis.setVisible(true);
    }//GEN-LAST:event_menuReporteFiscalActionPerformed
    
    public void registroSalida(String IP, String cam){
        String hrSalida = getFechaActual();
        String nombre = "S"+hrSalida.replace(" ","").replace(":","_").replace("-","");
        if(cam.equals("cam1") && panelVideoCam_21.getHayConexion()){
            guardarFoto(nombre, IP);
        }
        if(cam.equals("cam2") && panelVideoCam_22.getHayConexion()){
            guardarFoto(nombre, IP);
        }
        setRegistroFoto(hrSalida, nombre);        
    }
    
     private void setRegistroFoto(String hrSalida, String ruta){        
        try {            
          conect = conexionBD.getConexion();  
          Statement st = conect.createStatement();
          st.executeUpdate("INSERT INTO registros_salida(fecha, ruta) VALUES(" 
                            +"'"+hrSalida+"',"                                                     
                            +"'"+"data/"+ruta+".ppj')"
                            +";");          
         } catch(SQLException ex){            
          JOptionPane.showMessageDialog(null, ex, "Error de conexión con la Base de Datos", JOptionPane.ERROR_MESSAGE);
        }finally{
        try {
            conect.close();                        
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex, "Error de desconexión", JOptionPane.ERROR_MESSAGE);
        }
    }              
    }
    
    public void guardarFoto(String nombreArchivo, String direccionIP ){        
        URL nurl = null;
        BufferedImage imagen = null;
        try {
            nurl = new URL(direccionIP);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        Authenticator au = new Authenticator() {@Override
        protected PasswordAuthentication getPasswordAuthentication() {
            return new PasswordAuthentication (usuario, contra.toCharArray());
        }};
        Authenticator.setDefault(au);
        try {
            imagen = ImageIO.read(nurl);
        } catch (IOException ex) {
            //Logger.getLogger(PanelVideoCam.class.getName()).log(Level.SEVERE, null, ex);
            //ex.printStackTrace(); 
            JOptionPane.showMessageDialog(null, "Verifique la conexión a la cámara ", "Error al leer la imagen", JOptionPane.ERROR_MESSAGE);
        }
        if (imagen != null) {
            try {
                ImageIO.write(imagen, "png", new File(pathFotos+nombreArchivo+".ppj"));
                 System.out.println(pathFotos+nombreArchivo+".ppj");
            } catch (IOException e) {
                //e.printStackTrace(); 
                JOptionPane.showMessageDialog(null, "No se puede guardar la captura", "Error al guardar la captura", JOptionPane.ERROR_MESSAGE);
            }
        }
        else{
            JOptionPane.showMessageDialog(null, "Verifique la conexión a la cámara ", "Error al accesar a la cámara", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void menuConfigSistemaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuConfigSistemaActionPerformed
        // TODO add your handling code here:
         viewConfiguracion config = new viewConfiguracion(new javax.swing.JDialog(), true);        
         config.setVisible(true);
         
         System.out.println("ya cerro la ventada");
    }//GEN-LAST:event_menuConfigSistemaActionPerformed

    private void menuConfigContrasenasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuConfigContrasenasActionPerformed
        // TODO add your handling code here:
         viewConfigContrasenas configCon = new viewConfigContrasenas(new javax.swing.JDialog(), true);        
         configCon.setVisible(true);
    }//GEN-LAST:event_menuConfigContrasenasActionPerformed

    private void menuCambioUsuarioMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_menuCambioUsuarioMouseClicked
        // TODO add your handling code here:
        //elHilo.detenElHilo();        
        this.elHilo.detenElHilo();
        this.conexionCom.cerrarCOM();        
        this.dispose();        
        viewLogin log = new viewLogin();            
        log.setVisible(true);
    }//GEN-LAST:event_menuCambioUsuarioMouseClicked

    public void actualizarFrame(){
        getContadores();    //obtiene contadores
        getRegistrosS();    //obtiene registros de salida
        getRegistrosE();    //obtiene registros de entrada
        getRegistrosF();   //obtiene registros de las fotos de salida
    }
    private void menuCorteCajaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_menuCorteCajaMouseClicked
        // TODO add your handling code here:
        viewCorteCaja corte = new viewCorteCaja(viewPrincipal.this, true);            
        corte.setVisible(true);
        actualizarFrame();
    }//GEN-LAST:event_menuCorteCajaMouseClicked

    private void tablaEntradasMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tablaEntradasMouseClicked
        // TODO add your handling code here:        
        String valor = tablaEntradas.getValueAt(tablaEntradas.getSelectedRow(), 0).toString();
        System.out.println(" - "+valor);
        getTicket(valor);
    }//GEN-LAST:event_tablaEntradasMouseClicked

    private void checkTicketPerdidoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_checkTicketPerdidoActionPerformed
        // TODO add your handling code here:
        preciosD precios = new preciosD();
        if(checkTicketPerdido.isSelected()){
            float total = Float.parseFloat(lblTotal.getText());
            total += precios.getPrecio(7);
            lblTotal.setText(String.valueOf(total));
        }
        else{
            float total = Float.parseFloat(lblTotal.getText());
            total -= precios.getPrecio(7);
            lblTotal.setText(String.valueOf(total));
        }
    }//GEN-LAST:event_checkTicketPerdidoActionPerformed

    private void checkTicketPerdidoStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_checkTicketPerdidoStateChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_checkTicketPerdidoStateChanged

    private void btnCobrarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCobrarActionPerformed
        // TODO add your handling code here:
     
    }//GEN-LAST:event_btnCobrarActionPerformed

    private void btnBuscarFolioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBuscarFolioActionPerformed
        // TODO add your handling code here:
        String folio = txtTicket.getText();
        getTicket(folio);
    }//GEN-LAST:event_btnBuscarFolioActionPerformed

    private void txtTicketKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtTicketKeyPressed
        // TODO add your handling code here:
        if(evt.getKeyCode()==KeyEvent.VK_ENTER){
            String folio = txtTicket.getText();
            getTicket(folio);
        }
    }//GEN-LAST:event_txtTicketKeyPressed

    private void btnEntraSActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEntraSActionPerformed
        // TODO add your handling code here:
         viewEntrada entrada = new viewEntrada(this, true, this.ATRAS,this.panelVideoCam_21.getHayConexion());             
         entrada.setVisible(true);                                                       
         this.actualizarFrame();      
    }//GEN-LAST:event_btnEntraSActionPerformed

    private void btnEntraEActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEntraEActionPerformed
        // TODO add your handling code here:
        viewEntrada entrada = new viewEntrada(this, true, this.FRENTE,this.panelVideoCam_22.getHayConexion());             
         entrada.setVisible(true);                                                       
         this.actualizarFrame();
    }//GEN-LAST:event_btnEntraEActionPerformed

    private void checkNoDejoLlaveStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_checkNoDejoLlaveStateChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_checkNoDejoLlaveStateChanged

    private void checkNoDejoLlaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_checkNoDejoLlaveActionPerformed
        // TODO add your handling code here:
          //preciosD precios = new preciosD();
        if(checkNoDejoLlave.isSelected()){
            float total = Float.parseFloat(lblTotal.getText());
            //total += precios.getPrecio(8);
            total = total * 2;
            lblTotal.setText(String.valueOf(total));
        }
        else{
            float total = Float.parseFloat(lblTotal.getText());
            //total -= precios.getPrecio(8);
            total = total/2;
            lblTotal.setText(String.valueOf(total));
        }                                                                    
    }//GEN-LAST:event_checkNoDejoLlaveActionPerformed

    private void btnReimprimirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnReimprimirActionPerformed
        // TODO add your handling code here:
        impresoraTicketSalida im = new impresoraTicketSalida(this.hrEntrada,this.hrPregunta,lblTiempo.getText(),String.valueOf(this.subtotal),String.valueOf(this.iva),lblTotal.getText(),lblFolio.getText(), lblTipo.getText());        
    }//GEN-LAST:event_btnReimprimirActionPerformed

    private void tablaSalidasMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tablaSalidasMouseClicked
        // TODO add your handling code here:
        String valor = tablaSalidas.getValueAt(tablaSalidas.getSelectedRow(), 0).toString();
        System.out.println(" - "+valor);
        getTicket(valor);
    }//GEN-LAST:event_tablaSalidasMouseClicked

    private void tablaFotoSalidaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tablaFotoSalidaMouseClicked
        // TODO add your handling code here:
        String valor = tablaFotoSalida.getValueAt(tablaFotoSalida.getSelectedRow(), 0).toString();
        System.out.println(" - "+valor);
        getFoto(valor);
    }//GEN-LAST:event_tablaFotoSalidaMouseClicked

    private void btnCobrarMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnCobrarMouseClicked
        // TODO add your handling code here:
        int ticket = 0;
        if(checkTicketPerdido.isSelected()){
            ticket = 1;
        }                
        registroD registro = new registroD();
        registro.setSalida(this.promo, lblFolio.getText(), String.valueOf(this.subtotal), String.valueOf(this.iva), this.hrPregunta, lblTiempo.getText(), ticket, lblTotal.getText(), this.hrsCobradas);
        this.actualizarFrame();        
        if(checkComprobante.isSelected()){
            impresoraTicketSalida im = new impresoraTicketSalida(this.hrEntrada,this.hrPregunta,lblTiempo.getText(),String.valueOf(this.subtotal), String.valueOf(this.iva),lblTotal.getText(),lblFolio.getText(),lblTipo.getText());        
        }         
       /* try{
            System.out.println("********************entra try************");
                FileWriter imp = new FileWriter("COM100");                
                imp.write(27);
                imp.write(7);
                imp.write(11);
                imp.write(55);
                imp.write(7);
                imp.close();
            }catch(Exception e){
                System.out.println("********************no conecto************");
            }*/
           
        this.promo = false;
        btnCobrar.setEnabled(false);
        lblPagado.setVisible(true);
        checkTicketPerdido.setSelected(false);
    }//GEN-LAST:event_btnCobrarMouseClicked

    private void checkPromoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_checkPromoActionPerformed
         preciosD precios = new preciosD();         
         //float total = Float.parseFloat(lblTotal.getText());
         float total = 0;
         float desc = 0;         
        if(checkPromo.isSelected()){                                    
             switch (lblTipo.getText()) {
                 case "Automovil":                                          
                     desc = compararHoras(this.hrE, this.hrP) * precios.getPrecio(1);
                     total += desc;
                     total += precios.getPrecio(6);
                     this.hrsCobradas = compararHoras(this.hrE, this.hrP);
                     this.promo = true;
                     lblTotal.setText(String.valueOf(total));
                     break;
                 case "Camioneta":
                     desc = compararHoras(this.hrE, this.hrP) * precios.getPrecio(2);
                     total += desc;
                     total += precios.getPrecio(6);
                     this.hrsCobradas = compararHoras(this.hrE, this.hrP);
                     this.promo = true;
                     lblTotal.setText(String.valueOf(total));
                     break;
             }            
        }
        else{           
             getTicket(lblFolio.getText());
            }
    }//GEN-LAST:event_checkPromoActionPerformed

    private void abrirPlumaEActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_abrirPlumaEActionPerformed
        hiloEnviaCOM hiloEnviaCOM = new hiloEnviaCOM(this.conexionCom,"abrir1");        
        hiloEnviaCOM.start();
    }//GEN-LAST:event_abrirPlumaEActionPerformed

    private void cerrarPlumaEActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cerrarPlumaEActionPerformed
        // TODO add your handling code here:
        hiloEnviaCOM hiloEnviaCOM = new hiloEnviaCOM(this.conexionCom,"cerrar1");   
        hiloEnviaCOM.start();
    }//GEN-LAST:event_cerrarPlumaEActionPerformed

    private void abrirPlumaSActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_abrirPlumaSActionPerformed
        // TODO add your handling code here:
        hiloEnviaCOM hiloEnviaCOM = new hiloEnviaCOM(this.conexionCom,"abrir2");   
        hiloEnviaCOM.start();
    }//GEN-LAST:event_abrirPlumaSActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        // TODO add your handling code here:
        hiloEnviaCOM hiloEnviaCOM = new hiloEnviaCOM(this.conexionCom,"cerrar2");  
        hiloEnviaCOM.start();
    }//GEN-LAST:event_jButton4ActionPerformed
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
            java.util.logging.Logger.getLogger(viewPrincipal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(viewPrincipal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(viewPrincipal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(viewPrincipal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {            
            public void run() {
                new viewPrincipal(user).setVisible(true);
            }
        });                
        
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton abrirPlumaE;
    private javax.swing.JButton abrirPlumaS;
    private javax.swing.JButton btnBuscarFolio;
    private javax.swing.JButton btnCobrar;
    private javax.swing.JButton btnEntraE;
    private javax.swing.JButton btnEntraS;
    private javax.swing.JButton btnReimprimir;
    private javax.swing.JButton cerrarPlumaE;
    private javax.swing.JCheckBox checkComprobante;
    private javax.swing.JCheckBox checkNoDejoLlave;
    private javax.swing.JCheckBox checkPromo;
    private javax.swing.JCheckBox checkTicketPerdido;
    public static javax.swing.JLabel error;
    public javax.swing.JLabel etiqueta1;
    private javax.swing.JButton jButton4;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JLayeredPane jLayeredPane1;
    private javax.swing.JLayeredPane jLayeredPane2;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JLabel lblContadorE;
    private javax.swing.JLabel lblContadorS;
    private javax.swing.JLabel lblFolio;
    private javax.swing.JLabel lblHrEntrada;
    private javax.swing.JLabel lblPagado;
    private javax.swing.JLabel lblTiempo;
    private javax.swing.JLabel lblTipo;
    private javax.swing.JLabel lblTotal;
    private javax.swing.JLabel lblfoto;
    private javax.swing.JMenuItem menuAgregarPensionado;
    private javax.swing.JMenu menuCambioUsuario;
    private javax.swing.JMenuItem menuConfigContrasenas;
    private javax.swing.JMenuItem menuConfigSistema;
    private javax.swing.JMenu menuConfiguracion;
    private javax.swing.JMenu menuCorteCaja;
    private javax.swing.JMenuItem menuEliminarPensionado;
    private javax.swing.JMenu menuPensionado;
    private javax.swing.JMenuItem menuReporteEstadistico;
    private javax.swing.JMenuItem menuReporteFiscal;
    private javax.swing.JMenu menuReportes;
    public com.est.views.PanelVideoCam_2 panelVideoCam_21;
    public com.est.views.PanelVideoCam_2 panelVideoCam_22;
    private javax.swing.JTable tablaEntradas;
    private javax.swing.JTable tablaFotoSalida;
    private javax.swing.JTable tablaSalidas;
    private javax.swing.JTextField txtTicket;
    // End of variables declaration//GEN-END:variables
}
