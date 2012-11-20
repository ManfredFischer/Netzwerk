/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * UserAnmeldung.java
 *
 * Created on 29.12.2011, 12:37:34
 */
package netzwerkverwaltung;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

/**
 *
 * @author manfred.fischer
 */
public class UserAnmeldung extends javax.swing.JFrame {
   
    private ClientNetzwerk netz;
    public UserAnmeldung() {
        initComponents();
        new Thread(new Runnable() {

            @Override
            public void run() {
                try {
                    serverInit = new initServer(netz);

                } catch (Exception ex) {
                    Logger.getLogger(UserAnmeldung.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }).start();
        this.setLocationRelativeTo(null);


    }
    public Boolean Anmeldung = false;

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        User = new javax.swing.JTextField();
        pa = new javax.swing.JPasswordField();
        jButton1 = new javax.swing.JButton();
        pwCheck = new javax.swing.JCheckBox();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Anmeldung");

        jPanel1.setBackground(new java.awt.Color(133, 133, 149));

        User.setForeground(new java.awt.Color(204, 204, 204));
        User.setText("User Name");
        User.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                UserActionPerformed(evt);
            }
        });
        User.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                UserFocusGained(evt);
            }
        });

        pa.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                paActionPerformed(evt);
            }
        });

        jButton1.setText("OK");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        pwCheck.setBackground(new java.awt.Color(133, 133, 149));
        pwCheck.setText("PW ändern");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(pwCheck)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(User, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(pa, javax.swing.GroupLayout.DEFAULT_SIZE, 200, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jButton1)))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(User, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(pa, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pwCheck)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 62, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void paActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_paActionPerformed
        checkUser();
    }
// TODO add your handling code here:}//GEN-LAST:event_paActionPerformed
    private initServer serverInit;
    private int IDK;
    private ArrayList<ArrayList<String>> userDaten;
    private UserAnmeldung us;
    private String Port, IP;
    private HashMap configDaten = new HashMap();

    
    private Boolean initConfigDaten() {
        BufferedReader read;
        try {
            read = new BufferedReader(new FileReader(new File("Config/config.cfg")));
            String DatenTemp = read.readLine();
            while (DatenTemp != null) {
                if (DatenTemp.equals("##Server")) {
                    DatenTemp = read.readLine();
                    Port = DatenTemp.substring(4).split(":")[1];
                    IP = DatenTemp.substring(4).split(":")[0];
                    configDaten.put("Port", Port);
                    configDaten.put("IP", IP);
                } else if (DatenTemp.equals("##Zeiten")) {
                    DatenTemp = read.readLine();
                    configDaten.put("THaupt", DatenTemp.substring(8).toString());
                    DatenTemp = read.readLine();
                    configDaten.put("TStoerung", DatenTemp.substring(8).toString());
                } else if (DatenTemp.equals("##Einstellungen")) {
                    DatenTemp = read.readLine();
                    configDaten.put("CHintergrund", DatenTemp.toString().split(":")[1].toString());
                    DatenTemp = read.readLine();
                    configDaten.put("CHintergrundN", DatenTemp.toString().split(":")[1].toString());
                    DatenTemp = read.readLine();
                    configDaten.put("CHintergrundB", DatenTemp.toString().split(":")[1].toString());
                }
                DatenTemp = read.readLine();
            }
            if (Port.isEmpty() || IP.isEmpty()) {
                return false;
            } else {
                return true;
            }
        } catch (Exception e) {
            return false;
        }
    }
    private HauptBildschirm neu = new HauptBildschirm();
    private void checkUser() {
        serverInit.setAnsicht(false);
        this.setVisible(false);
        netz = new ClientNetzwerk(serverInit,neu);
        if (initConfigDaten()) {
            us = this;
            serverInit.setVisible(true);
            new Thread(new Runnable() {

                @Override
                public void run() {
                    try {
                        serverInit.infoProgress.setValue(10);
                        serverInit.infoProgressSee.setValue(10);
                        serverInit.infoProgressSee.setString("User Anmeldung ... 5%");
                        serverInit.infoText.append("User Anmeldung ...");
                        try{
                            serverInit.setInfo(configDaten);
                        }catch(Exception e){
                            
                        }

                        try {
                            netz.ClientVerbindung(IP, Port);
                            if (User.getText().equals("")) {
                                us.setAlwaysOnTop(true);
                                serverInit.setVisible(false);
                                JOptionPane.showMessageDialog(new JFrame(), "Fehlerhafte Eingabe");
                            } else {
                                if (netz.netzwerkVerwaltung.check(User.getText(),false)) {
                                    try {
                                        ArrayList<File> dateien = netz.netzwerkVerwaltung.getDateien("System/User/" + User.getText(), User.getText() + ".zip", "UserAnmeldung/checkUser");
                                        String pw = netz.entschluesseln("Daten/System/User/" + User.getText() + "/info.dfg");
                                        if (pw.equals(pa.getText())) {
                                            if (pwCheck.isSelected() || pw.equals("stern") || pw.equals("schenker")) {
                                                pwCheck.setSelected(false);
                                                newPW nPW = new newPW(netz, User.getText());
                                            } else {

                                                dateien.get(0).delete();
                                                userDaten = (ArrayList<ArrayList<String>>) netz.netzwerkVerwaltung.datenKonto("UserAnmeldung/checkUser",false).clone();
                                                for (int i = 0; i < userDaten.size(); i++) {
                                                    if (userDaten.get(i).get(1).equals(User.getText())) {
                                                        IDK = i;
                                                        break;
                                                    }
                                                }


                                                
                                                neu.initialisierung(netz, userDaten, IDK, serverInit, (HashMap) configDaten.clone());
                                                us.setVisible(false);
                                                us.dispose();

                                            }

                                        } else {
                                            pa.setText("");
                                            serverInit.infoProgress.setValue(10);
                                            serverInit.infoText.append(" FEHLER \n");
                                            us.setAlwaysOnTop(true);
                                            us.setVisible(true);

                                        }
                                    } catch (Exception ex) {
                                        serverInit.infoProgress.setValue(10);
                                        serverInit.infoText.append(ex.getLocalizedMessage() + " FEHLER \n");
                                    }
                                } else {
                                    us.setAlwaysOnTop(true);
                                }
                            }
                        } catch (Exception ex) {
                            JOptionPane.showMessageDialog(new JFrame(), "Server leider nicht erreichbar!!!");

                            us.setVisible(false);
                            serverInit.setVisible(false);
                            serverInit.setAnsicht(true);
                            serverInit.jTabbedPane1.remove(0);
                            serverInit.setDefault();
                            serverInit.setVisible(true);
                        }
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(new JFrame(), "Server leider nicht erreichbar!!!");
                        us.setVisible(false);
                        serverInit.setVisible(false);
                        serverInit.jTabbedPane1.remove(0);
                        serverInit.setVisible(true);
                    }
                }
            }).start();
        } else {
           
            serverInit.jTabbedPane1.remove(0);
            serverInit.setVisible(true);

        }
    }
        private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed

            checkUser();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void UserActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_UserActionPerformed
        pa.setFocusable(true);
    }//GEN-LAST:event_UserActionPerformed

    private void UserFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_UserFocusGained
        User.setText("");
        User.setForeground(Color.BLACK);
    }//GEN-LAST:event_UserFocusGained

    public static void main(String args[]) {
        /*
         * Set the Nimbus look and feel
         */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /*
         * If Nimbus (introduced in Java SE 6) is not available, stay with the
         * default look and feel. For details see
         * http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(UserAnmeldung.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(UserAnmeldung.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(UserAnmeldung.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(UserAnmeldung.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /*
         * Create and display the form
         */
        java.awt.EventQueue.invokeLater(new Runnable() {

            @Override
            public void run() {
               
                new UserAnmeldung().setVisible(true);
            }
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField User;
    private javax.swing.JButton jButton1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPasswordField pa;
    private javax.swing.JCheckBox pwCheck;
    // End of variables declaration//GEN-END:variables
}
