/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package netzwerkverwaltung;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Panel;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.plaf.basic.BasicProgressBarUI;

/**
 *
 * @author manfred.fischer
 */
public class initServer extends javax.swing.JFrame {

    /**
     * Creates new form initServer
     */
    
    public void setDefault(){
     this.setDefaultCloseOperation(EXIT_ON_CLOSE);    
    }
    
    private ClientNetzwerk client;
    public initServer(ClientNetzwerk cl) {
        initComponents();
        
        client = cl;
        this.setLocationRelativeTo(null);
        BasicProgressBarUI ui = new BasicProgressBarUI() {
             
            
            @Override
            protected Color getSelectionForeground(){ 
                return Color.BLUE; 
            } 
            @Override
            protected Color getSelectionBackground(){ 
                return Color.BLUE; 
            } 
        }; 
         BasicProgressBarUI ui2 = new BasicProgressBarUI() {
             
            
            @Override
            protected Color getSelectionForeground(){ 
                return Color.BLUE; 
            } 
            @Override
            protected Color getSelectionBackground(){ 
                return Color.BLUE; 
            } 
        }; 
        infoProgress.setUI(ui);
        wait.setUI(ui2);
    }
    
    public void setAnsicht(Boolean detail){
        if (detail){
          this.setSize(new Dimension(435,502));
          jPanel3.setVisible(false);
          jPanel4.setVisible(true);
        }else{
            this.setSize(new Dimension(333,380));
           jPanel3.setVisible(true);
           jPanel4.setVisible(false);
        }
    }
    int i = 0;

    protected void setInfo(HashMap daten){
        ServerIP.setText(daten.get("IP").toString());
        ServerPort.setText(daten.get("Port").toString());
        timeHaupt.setText(daten.get("THaupt").toString());
        timeStoerung.setText(daten.get("TStoerung").toString());
        
        HR.setText(daten.get("CHintergrund").toString().split("#")[0]);
        HB.setText(daten.get("CHintergrund").toString().split("#")[1]);
        HY.setText(daten.get("CHintergrund").toString().split("#")[2]);
        
        HNR.setText(daten.get("CHintergrundN").toString().split("#")[0]);
        HNB.setText(daten.get("CHintergrundN").toString().split("#")[1]);
        HNY.setText(daten.get("CHintergrundN").toString().split("#")[2]);
        
        HBR.setText(daten.get("CHintergrundB").toString().split("#")[0]);
        HBB.setText(daten.get("CHintergrundB").toString().split("#")[1]);
        HBY.setText(daten.get("CHintergrundB").toString().split("#")[2]);
    }
    
    protected void setDatenInfo(String Text, Boolean wert, Boolean erhalten, Boolean fehler) {
         
        //wait.setEnabled(wert);
        DatenInfo.setEnabled(wert);
        infoDaten.setEnabled(wert);

        if (erhalten) {
            if (fehler) {
                DatenInfo.setText(Text + " --- > FEHLER");
                getDatenInfo.append("FEHLER!\n");
            } else {
                DatenInfo.setText(Text + " ----> OK!");
                getDatenInfo.append("OK!\n");
            }
        } else {
            i++;
            getDatenInfo.append(i + " -> " + Text + ".. ");
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

        jPanel3 = new javax.swing.JPanel();
        infoProgressSee = new javax.swing.JProgressBar();
        jLabel7 = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel1 = new javax.swing.JPanel();
        infoProgress = new javax.swing.JProgressBar();
        wait = new javax.swing.JProgressBar();
        infoDaten = new javax.swing.JLabel();
        DatenInfo = new javax.swing.JLabel();
        jTabbedPane2 = new javax.swing.JTabbedPane();
        jScrollPane1 = new javax.swing.JScrollPane();
        infoText = new javax.swing.JTextArea();
        jScrollPane2 = new javax.swing.JScrollPane();
        getDatenInfo = new javax.swing.JTextArea();
        jScrollPane3 = new javax.swing.JScrollPane();
        fehleranalyse = new javax.swing.JTextArea();
        jPanel2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        ServerIP = new javax.swing.JTextField();
        ServerPort = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        timeHaupt = new javax.swing.JTextField();
        jLabel18 = new javax.swing.JLabel();
        timeStoerung = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        HR = new javax.swing.JTextField();
        HB = new javax.swing.JTextField();
        HY = new javax.swing.JTextField();
        HBR = new javax.swing.JTextField();
        HBB = new javax.swing.JTextField();
        HBY = new javax.swing.JTextField();
        HNR = new javax.swing.JTextField();
        HNB = new javax.swing.JTextField();
        HNY = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();

        setBackground(new java.awt.Color(255, 255, 255));

        jPanel3.setBackground(new java.awt.Color(255, 255, 255));

        infoProgressSee.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        infoProgressSee.setForeground(new java.awt.Color(0, 0, 0));
        infoProgressSee.setMaximum(140);
        infoProgressSee.setToolTipText("info...");
        infoProgressSee.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        infoProgressSee.setName("info...");
        infoProgressSee.setString("Info...");
        infoProgressSee.setStringPainted(true);

        jLabel7.setIcon(new javax.swing.ImageIcon(getClass().getResource("/netzwerkverwaltung/Bilder/netzwerk-neu.jpg"))); // NOI18N
        jLabel7.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel7MouseClicked(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(infoProgressSee, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(infoProgressSee, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jPanel4.setBackground(new java.awt.Color(255, 255, 255));

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setMinimumSize(new java.awt.Dimension(515, 533));

        infoProgress.setForeground(new java.awt.Color(204, 204, 204));
        infoProgress.setMaximum(140);

        wait.setBackground(new java.awt.Color(204, 204, 204));
        wait.setForeground(new java.awt.Color(240, 240, 240));
        wait.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));

        infoDaten.setBackground(new java.awt.Color(255, 255, 255));
        infoDaten.setText("Daten-Empfang:");

        DatenInfo.setBackground(new java.awt.Color(255, 255, 255));

        jScrollPane1.setBackground(new java.awt.Color(255, 255, 255));
        jScrollPane1.setAutoscrolls(true);

        infoText.setColumns(20);
        infoText.setEditable(false);
        infoText.setRows(5);
        jScrollPane1.setViewportView(infoText);

        jTabbedPane2.addTab("init Server", jScrollPane1);

        jScrollPane2.setAutoscrolls(true);

        getDatenInfo.setColumns(20);
        getDatenInfo.setRows(5);
        jScrollPane2.setViewportView(getDatenInfo);

        jTabbedPane2.addTab("init Daten", jScrollPane2);

        fehleranalyse.setColumns(20);
        fehleranalyse.setRows(5);
        jScrollPane3.setViewportView(fehleranalyse);

        jTabbedPane2.addTab("Fehlerberichte", jScrollPane3);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(infoDaten)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(DatenInfo, javax.swing.GroupLayout.PREFERRED_SIZE, 306, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 114, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(infoProgress, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jTabbedPane2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 400, Short.MAX_VALUE)
                            .addComponent(wait, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addContainerGap())))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(infoDaten)
                    .addComponent(DatenInfo, javax.swing.GroupLayout.PREFERRED_SIZE, 17, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(9, 9, 9)
                .addComponent(infoProgress, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(wait, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTabbedPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 355, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(101, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Monitor", jPanel1);

        jLabel1.setText("Server:");

        jLabel2.setText("Port:");

        jLabel16.setText("------------------------------- Haupt ------------------------------------------------------");

        jLabel3.setText("Aktualisierung der Informationen (min):");

        jLabel4.setText("Hintergrund");

        jLabel18.setText("Aktualisierung der Störungen       (min):");

        jLabel5.setText("Hintergrund - Border");

        jLabel6.setText("Hintergrund - Navigation");

        jButton1.setText("Save");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(ServerIP, javax.swing.GroupLayout.PREFERRED_SIZE, 245, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(ServerPort))
                    .addComponent(jLabel16, javax.swing.GroupLayout.DEFAULT_SIZE, 397, Short.MAX_VALUE)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(jLabel18)
                                .addGap(18, 18, 18)
                                .addComponent(timeStoerung, javax.swing.GroupLayout.PREFERRED_SIZE, 74, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(jLabel3)
                                .addGap(18, 18, 18)
                                .addComponent(timeHaupt, javax.swing.GroupLayout.PREFERRED_SIZE, 74, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addComponent(jLabel4, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jLabel5, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jLabel6, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel2Layout.createSequentialGroup()
                                        .addComponent(HR, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(HB, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(HY, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(jPanel2Layout.createSequentialGroup()
                                        .addComponent(HBR, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(HBB, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(HBY, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(jPanel2Layout.createSequentialGroup()
                                        .addComponent(HNR, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(HNB, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(HNY, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jButton1)))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(ServerIP, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2)
                    .addComponent(ServerPort, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel16)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(timeHaupt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel18)
                    .addComponent(timeStoerung, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(HR, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(HB, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(HY, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(HBR, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(HBB, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(HBY, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(HNR, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(HNB, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(HNY, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel6))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 217, Short.MAX_VALUE)
                .addComponent(jButton1)
                .addContainerGap())
        );

        jTabbedPane1.addTab("Einstellungen", jPanel2);

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 422, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 472, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
       try {
        BufferedWriter schreib = new BufferedWriter(new FileWriter(new File("Config/config.cfg")));
        schreib.write("##Server\n");
        schreib.append("IP: " + ServerIP.getText() + ":" + ServerPort.getText() + "\n");
        schreib.write("##Zeiten\n");
        schreib.append("Haupt.: " + timeHaupt.getText() + "\n");
        schreib.append("Stoer.: " + timeStoerung.getText() + "\n");
        schreib.write("##Einstellungen\n");
        schreib.append("Color-Hintergrund:" + HR.getText()+"#"+ HB.getText()+"#"+ HY.getText() + "\n");
        schreib.append("Color-Hintergrund-Navi:" + HNR.getText()+"#"+ HNB.getText()+"#"+ HNY.getText() + "\n");
        schreib.append("Color-Hintergrund-Border:" + HBR.getText()+"#"+ HBB.getText()+"#"+ HBY.getText() + "\n");
        schreib.close();
        
        JOptionPane.showMessageDialog(new JFrame(), "Config Datei geschrieben.... :) \nBitte Neustarten damit die Einstellungen Angewendet werden!!!");
       
    } catch (IOException ex) {
        
    }
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jLabel7MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel7MouseClicked
       setAnsicht(true);
    }//GEN-LAST:event_jLabel7MouseClicked

    // Variables declaration - do not modify//GEN-BEGIN:variables
    protected javax.swing.JLabel DatenInfo;
    private javax.swing.JTextField HB;
    private javax.swing.JTextField HBB;
    private javax.swing.JTextField HBR;
    private javax.swing.JTextField HBY;
    private javax.swing.JTextField HNB;
    private javax.swing.JTextField HNR;
    private javax.swing.JTextField HNY;
    private javax.swing.JTextField HR;
    private javax.swing.JTextField HY;
    private javax.swing.JTextField ServerIP;
    private javax.swing.JTextField ServerPort;
    protected javax.swing.JTextArea fehleranalyse;
    private javax.swing.JTextArea getDatenInfo;
    private javax.swing.JLabel infoDaten;
    protected javax.swing.JProgressBar infoProgress;
    public javax.swing.JProgressBar infoProgressSee;
    protected javax.swing.JTextArea infoText;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    public javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTabbedPane jTabbedPane2;
    private javax.swing.JTextField timeHaupt;
    private javax.swing.JTextField timeStoerung;
    protected javax.swing.JProgressBar wait;
    // End of variables declaration//GEN-END:variables
}
