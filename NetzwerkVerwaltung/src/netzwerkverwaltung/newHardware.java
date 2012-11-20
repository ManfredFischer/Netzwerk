/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * newServerClientDaten.java
 *
 * Created on 29.12.2011, 14:12:55
 */
package netzwerkverwaltung;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class newHardware extends javax.swing.JFrame {
    
    private ClientNetzwerk clients;
    private newUser nUser;
    private String ID = "";
    private int netzID = 0;
    private ArrayList<ArrayList<String>> cs, NetzDaten;

    /**
     * Creates new form newServerClientDaten
     */
    public newHardware() {
        initComponents();
    }
    
    public void StartServer(ClientNetzwerk client, String IDNetz, ArrayList<String> Daten, HauptBildschirm h, newUser nU, initServer init, ArrayList<ArrayList<String>> dN, JComboBox box) {
        clients = client;
        nUser = nU;
        NetzDaten = dN;
        for (int i = 0; i < NetzDaten.get(0).size(); i++) {
            if (NetzDaten.get(i).get(0).equals(IDNetz)) {
                netzID = i;
                this.setTitle(NetzDaten.get(i).get(1).toString());
                break;
            }
        }
        ip.addItem("KEINE");
        for (int i=0;i<box.getItemCount();i++)
         ip.addItem(box.getItemAt(i));
        
        this.setLocationRelativeTo(h);
        
        init.infoProgress.setForeground(Color.BLACK);
        init.infoProgress.setValue(10);
        init.infoProgress.setForeground(Color.BLACK);
        init.infoProgress.setValue(70);
        cs = (ArrayList<ArrayList<String>>) clients.netzwerkVerwaltung.getHardware(NetzDaten.get(netzID).get(0), "all", "newServerClientDaten/StartServer",false).clone();
        init.infoProgress.setValue(140);
        init.infoProgress.setForeground(Color.GREEN);
        
        for (int i = 0; i < NetzDaten.size(); i++) {
            cbNetzN.addItem(NetzDaten.get(i).get(1).toString());
            cbClientNetz.addItem(NetzDaten.get(i).get(1).toString());
        }
        
        
        boolean weiter = true, wdhcp = true;
        String[] IPNetz = NetzDaten.get(netzID).get(2).toString().split("\\.");
        String[] DHCP = NetzDaten.get(netzID).get(4).toString().split(" - ");
        
        
        for (int a = 0; a < cs.size(); a++) {
            cbClient.addItem(cs.get(a).get(1));
        }
        
       
        
        if (Daten != null) {
            hinzu.setText("bearbeiten");
            setInfoDaten(Daten);
        }
        this.setVisible(true);
    }
    
    private void setInfoDaten(ArrayList<String> Daten) {
        netzName.setText(Daten.get(1).toString());
        netzFunktion.setText(Daten.get(2).toString());
        netzLE.setText(Daten.get(3).toString());
        netzSN.setText(Daten.get(4).toString());
        netzTyp.setText(Daten.get(5).toString());
        netzGruppe.setSelectedItem(Daten.get(7).toString());
        ID = Daten.get(0).toString();
        if (Daten.get(6).toString().equals("DHCP")) {
            ip.setEnabled(false);
            netzDHCP.setSelected(true);
        } else {
            ip.addItem(Daten.get(6));
            ip.setSelectedItem(Daten.get(6).toString());
        }
        
    }
    
    private String checkCSDoppelt(String Daten, String Gruppe) {
        ArrayList<ArrayList<String>> datenCS = (ArrayList<ArrayList<String>>) clients.netzwerkVerwaltung.getHardware(Daten, Gruppe, "newServerClientDaten/checkCSDoppelt",false).clone();
        
        for (int i = 0; i < datenCS.size(); i++) {
            if (datenCS.get(i).get(1).equals(Daten)) {
                String s = (String) JOptionPane.showInputDialog(
                        new JFrame(),
                        "User schon vorhanden!! \n \n Username:",
                        "Fehler",
                        JOptionPane.PLAIN_MESSAGE,
                        null, null,
                        Daten);
                if (s != null) {
                    Daten = checkCSDoppelt(s, Gruppe);
                } else {
                    Daten = null;
                }
            }
        }
        return Daten;
    }
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        auswahl = new javax.swing.JTabbedPane();
        jPanel2 = new javax.swing.JPanel();
        netzDHCP = new javax.swing.JCheckBox();
        hinzu = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        ip = new javax.swing.JComboBox();
        jLabel6 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        netzTyp = new javax.swing.JTextField();
        netzFunktion = new javax.swing.JTextField();
        netzSN = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        netzLE = new javax.swing.JTextField();
        netzGruppe = new javax.swing.JComboBox();
        netzName = new javax.swing.JTextField();
        jPanel3 = new javax.swing.JPanel();
        jLabel9 = new javax.swing.JLabel();
        cbNetzN = new javax.swing.JComboBox();
        jButton1 = new javax.swing.JButton();
        jLabel10 = new javax.swing.JLabel();
        cbClient = new javax.swing.JComboBox();
        jLabel11 = new javax.swing.JLabel();
        cbClientNetz = new javax.swing.JComboBox();
        jButton2 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setResizable(false);

        jPanel1.setBackground(javax.swing.UIManager.getDefaults().getColor("InternalFrame.activeTitleGradient"));

        jPanel2.setBackground(new java.awt.Color(133, 133, 149));

        netzDHCP.setBackground(new java.awt.Color(133, 133, 149));
        netzDHCP.setText("DHCP");
        netzDHCP.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                netzDHCPActionPerformed(evt);
            }
        });

        hinzu.setText("OK");
        hinzu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                hinzuActionPerformed(evt);
            }
        });

        jLabel1.setText("IP:");

        jLabel6.setText("Typ.:");

        jLabel3.setText("Funktion:");

        jLabel7.setText("Gruppe.:");

        jLabel4.setText("Leasing Ende:");

        jLabel2.setText("Name:");

        jLabel5.setText("Serien-Nr.:");

        netzGruppe.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Server", "Terminal", "Notebook", "PC", "Drucker", "Monitor", "Switch" }));

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(hinzu, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel4)
                            .addComponent(jLabel3)
                            .addComponent(jLabel2)
                            .addComponent(jLabel5)
                            .addComponent(jLabel7)
                            .addComponent(jLabel1)
                            .addComponent(jLabel6))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(netzTyp, javax.swing.GroupLayout.DEFAULT_SIZE, 164, Short.MAX_VALUE)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(ip, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(netzDHCP))
                            .addComponent(netzGruppe, 0, 164, Short.MAX_VALUE)
                            .addComponent(netzSN, javax.swing.GroupLayout.DEFAULT_SIZE, 164, Short.MAX_VALUE)
                            .addComponent(netzName, javax.swing.GroupLayout.DEFAULT_SIZE, 164, Short.MAX_VALUE)
                            .addComponent(netzFunktion, javax.swing.GroupLayout.DEFAULT_SIZE, 164, Short.MAX_VALUE)
                            .addComponent(netzLE, javax.swing.GroupLayout.DEFAULT_SIZE, 164, Short.MAX_VALUE))))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(ip, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(netzDHCP))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(netzName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(netzFunktion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(netzLE, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(netzSN, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(netzGruppe, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(netzTyp, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(hinzu)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        auswahl.addTab("new", jPanel2);

        jPanel3.setBackground(new java.awt.Color(133, 133, 149));

        jLabel9.setText("new Netz:");

        jButton1.setText("move All");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jLabel10.setText("Client:");

        jLabel11.setText("Netz:");

        jButton2.setText("move");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButton1, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jButton2, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel9)
                            .addComponent(jLabel10)
                            .addComponent(jLabel11))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(cbClientNetz, 0, 176, Short.MAX_VALUE)
                            .addComponent(cbClient, 0, 176, Short.MAX_VALUE)
                            .addComponent(cbNetzN, 0, 176, Short.MAX_VALUE))))
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cbNetzN, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel9))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton1)
                .addGap(18, 18, 18)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cbClient, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel10))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cbClientNetz, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel11))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton2)
                .addContainerGap(74, Short.MAX_VALUE))
        );

        auswahl.addTab("move", jPanel3);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(auswahl)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(auswahl)
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private String checkDaten(String Check) {
        if (Check.equals("")) {
            Check = "---";
        }
        String tm = "";
        String[] temp = Check.split("");
        for (int i = 0; i < temp.length; i++) {
            if (temp[i].equals("\\+")) {
                tm = tm + "-?-";
            } else {
                tm = tm + temp[i];
            }
        }
        return tm;
    }
    private void hinzuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_hinzuActionPerformed
        if (hinzu.getText().equals("bearbeiten")) {
            clients.send("2002" + clients.trennzeichen + "CServer" + clients.trennzeichen + "Name" + clients.trennzeichen + netzName.getText() + "" + clients.trennzeichen + ID + "" + clients.trennzeichen + "IDSC");
            clients.send("2002" + clients.trennzeichen + "CServer" + clients.trennzeichen + "gruppe" + clients.trennzeichen + netzGruppe.getSelectedItem() + "" + clients.trennzeichen + ID + "" + clients.trennzeichen + "IDSC");
            clients.send("2002" + clients.trennzeichen + "CServer" + clients.trennzeichen + "Funktion" + clients.trennzeichen + clients.checkDaten(netzFunktion.getText()) + "" + clients.trennzeichen + ID + "" + clients.trennzeichen + "IDSC");
            clients.send("2002" + clients.trennzeichen + "CServer" + clients.trennzeichen + "LeasingEnde" + clients.trennzeichen + clients.checkDaten(netzLE.getText()) + "" + clients.trennzeichen + ID + "" + clients.trennzeichen + "IDSC");
            clients.send("2002" + clients.trennzeichen + "CServer" + clients.trennzeichen + "SerienNr" + clients.trennzeichen + "" + clients.checkDaten(netzSN.getText()) + "" + clients.trennzeichen + ID + "" + clients.trennzeichen + "IDSC");
            clients.send("2002" + clients.trennzeichen + "CServer" + clients.trennzeichen + "Typ" + clients.trennzeichen + clients.checkDaten(netzTyp.getText()) + "" + clients.trennzeichen + ID + "" + clients.trennzeichen + "IDSC");
            if (netzDHCP.isSelected()) {
                clients.send("2002" + clients.trennzeichen + "CServer" + clients.trennzeichen + "IP" + clients.trennzeichen + "DHCP" + clients.trennzeichen + ID + "" + clients.trennzeichen + "IDSC");
            } else {
                clients.send("2002" + clients.trennzeichen + "CServer" + clients.trennzeichen + "IP" + clients.trennzeichen + clients.checkDaten(ip.getSelectedItem()) + "" + clients.trennzeichen + ID + "" + clients.trennzeichen + "IDSC");
            }
            this.setVisible(false);
            this.dispose();
        } else {
            String IDN = NetzDaten.get(netzID).get(0).toString();
            String IP = " ";
            
            if (!netzDHCP.isSelected()) {
                IP = ip.getSelectedItem().toString();
            } else {
                IP = "DHCP";
            }
            try {
                if (!IP.equals("DHCP")) {
                    if (!IP.equals("KEINE")) {
                        int test = Integer.parseInt(IP);
                    }                
                }
                ArrayList<String> send = new ArrayList<String>();
                String Daten = checkCSDoppelt(NetzDaten.get(netzID).get(0).toString(), "all");
                if (Daten != null) {
                    send.add("1100" + clients.trennzeichen + checkDaten(netzName.getText()) + clients.trennzeichen
                            + checkDaten(netzFunktion.getText()) + clients.trennzeichen
                            + checkDaten(netzLE.getText()) + clients.trennzeichen
                            + checkDaten(netzSN.getText()) + clients.trennzeichen
                            + checkDaten(netzTyp.getText()) + clients.trennzeichen
                            + checkDaten(IP) + clients.trennzeichen
                            + checkDaten(netzGruppe.getSelectedItem().toString()) + clients.trennzeichen + IDN + "" + clients.trennzeichen + "1");
                    clients.ClientVersand(send);
                    
                    if (nUser != null) {
                        nUser.laptops = clients.netzwerkVerwaltung.getHardware("-1", "all", "newServerClientDaten/hinzuActionPerformed",false);
                        for (int i = 0; i < nUser.laptops.size(); i++) {
                            nUser.lapSN.addItem(nUser.laptops.get(i).get(1));
                        }
                        nUser.setEnabled(true);
                        nUser.laptop.setEnabled(true);
                        this.setVisible(false);
                        this.dispose();
                    }
                }
                netzName.setText("");
                netzFunktion.setText("");
                netzLE.setText("");
                netzSN.setText("");
                netzTyp.setText("");
                
                
            } catch (Exception e) {
            }
        }
        
    }//GEN-LAST:event_hinzuActionPerformed
    
    private void netzDHCPActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_netzDHCPActionPerformed
        if (netzDHCP.isSelected()) {
            ip.setEnabled(false);
        } else {
            ip.setEnabled(true);
        }
    }//GEN-LAST:event_netzDHCPActionPerformed
    
    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        for (int i = 0; i < cs.size(); i++) {
            clients.send("2003" + clients.trennzeichen + "CServer" + clients.trennzeichen + "IDN" + clients.trennzeichen + NetzDaten.get(cbNetzN.getSelectedIndex()).get(0) + "" + clients.trennzeichen + "IDSC" + clients.trennzeichen + cs.get(i).get(0));
        }
    }//GEN-LAST:event_jButton1ActionPerformed
    
    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        clients.send("2003" + clients.trennzeichen + "CServer" + clients.trennzeichen + "IDN" + clients.trennzeichen + NetzDaten.get(cbClientNetz.getSelectedIndex()).get(0) + "" + clients.trennzeichen + "IDSC" + clients.trennzeichen + cs.get(cbClient.getSelectedIndex()).get(0));
    }//GEN-LAST:event_jButton2ActionPerformed
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTabbedPane auswahl;
    private javax.swing.JComboBox cbClient;
    private javax.swing.JComboBox cbClientNetz;
    private javax.swing.JComboBox cbNetzN;
    private javax.swing.JButton hinzu;
    private javax.swing.JComboBox ip;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JCheckBox netzDHCP;
    private javax.swing.JTextField netzFunktion;
    private javax.swing.JComboBox netzGruppe;
    private javax.swing.JTextField netzLE;
    private javax.swing.JTextField netzName;
    private javax.swing.JTextField netzSN;
    private javax.swing.JTextField netzTyp;
    // End of variables declaration//GEN-END:variables
}
