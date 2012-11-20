/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * ServerProtokollDaten.java
 *
 * Created on 29.12.2011, 10:33:56
 */
package netzwerkverwaltung;

import java.awt.Frame;
import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.HashMap;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.TableColumn;

/**
 *
 * @author manfred.fischer
 */
public class ServerProtokollDaten extends javax.swing.JFrame {

    private ClientNetzwerk clients;
    private ArrayList<ArrayList<String>> NetzDaten,datenCS, datenP;
    private String[] titlesProtokoll = new String[]{"Datum", "User", "Tätigkeit"};
    private TabelFont tableProtokoll = new TabelFont();
    private String IDSC;
    public Frame test;
    public BufferedReader daten;
    public String ServerID;
    private JTable tabProtokoll;
    public wait n;
    private HauptBildschirm haupt;

    public ServerProtokollDaten(ClientNetzwerk c,HauptBildschirm h) {
        clients = c;
        haupt = h;
        this.setLocationRelativeTo(null);
    }

    public void activ(Boolean weiter) {
        proSFunktion.setEnabled(weiter);
        proSGruppe.setEnabled(!weiter);
        proSLE.setEnabled(weiter);
        proSName.setEnabled(weiter);
        proSSN.setEnabled(weiter);
        proSServer.setEnabled(!weiter);
    }

    private void up() {
        if (!proSFunktion.isEnabled()) {
            activ(true);
        } else {
            bearbeiten.setName("Update");

            int update = JOptionPane.showConfirmDialog(
                    new JFrame(),
                    "Möchten Sie Wirklich UPDATEN",
                    "HINWEIS",
                    JOptionPane.YES_NO_OPTION);
            if (update == 0) {


                datenCS = (ArrayList<ArrayList<String>>) clients.netzwerkVerwaltung.getHardware(NetzDaten.get(0).get(0).toString(), "all","ServerProtokollDaten/up",false).clone();
                String ID = "";
                for (int i = 0; i < this.datenCS.size(); i++) {
                    if (this.datenCS.get(i).get(1).equals(proSServer.getSelectedItem().toString())) {
                        ID = datenCS.get(i).get(0).toString();
                    }
                }
                Object[] possibilities = {"Server", "PC", "Notebook", "Terminal", "Switch", "Drucker", "Monitore"};
                String s = (String) JOptionPane.showInputDialog(
                        new JFrame(),
                        "Gruppe:\n",
                        "Customized Dialog",
                        JOptionPane.OK_OPTION,
                        null,
                        possibilities,
                        "Server");

                Object[] po;
                boolean weiter = true;
                ArrayList<String> datenTemp = new ArrayList<String>();

                for (int i = 1; i < 255; i++) {
                    for (int a = 0; a < datenCS.size(); a++) {
                        if (datenCS.get(a).get(6).equals(String.valueOf(i))) {
                            weiter = false;
                            break;
                        }
                    }
                    if (weiter) {
                        datenTemp.add(String.valueOf(i));
                    }
                    weiter = true;
                }
                po = new Object[datenTemp.size()];
                for (int i = 0; i < datenTemp.size(); i++) {
                    po[i] = datenTemp.get(i);
                }
                String IP = (String) JOptionPane.showInputDialog(
                        new JFrame(),
                        "IP:",
                        "Customized Dialog",
                        JOptionPane.PLAIN_MESSAGE,
                        null,
                        po,
                        "Server");
                if (IP != null) {
                    clients.send("2002"+clients.trennzeichen+ "CServer"+clients.trennzeichen+ "IP"+clients.trennzeichen+ "" + IP + ""+clients.trennzeichen  + ID + ""+clients.trennzeichen+ "IDSC");
                }


                String name = (String) JOptionPane.showInputDialog(
                        new JFrame(),
                        "Server Name:\n",
                        "Customized Dialog",
                        JOptionPane.PLAIN_MESSAGE,
                        null,
                        null,
                        proSServer.getSelectedItem());
                if (!name.equals("")) {
                    clients.send("2002"+clients.trennzeichen+ "CServer"+clients.trennzeichen+ "Name"+clients.trennzeichen  + clients.checkDaten(name) + ""+clients.trennzeichen  + ID + ""+clients.trennzeichen+ "IDSC");
                }
                clients.send("2002"+clients.trennzeichen+ "CServer"+clients.trennzeichen+ "gruppe"+clients.trennzeichen  + s + ""+clients.trennzeichen  + ID + ""+clients.trennzeichen+ "IDSC");
                clients.send("2002"+clients.trennzeichen+ "CServer"+clients.trennzeichen+ "Funktion"+clients.trennzeichen  + clients.checkDaten(proSFunktion.getText()) + ""+clients.trennzeichen  + ID + ""+clients.trennzeichen+ "IDSC");
                clients.send("2002"+clients.trennzeichen+ "CServer"+clients.trennzeichen+ "LeasingEnde"+clients.trennzeichen  + clients.checkDaten(proSLE.getText()) + ""+clients.trennzeichen  + ID + ""+clients.trennzeichen+ "IDSC");
                clients.send("2002"+clients.trennzeichen+ "CServer"+clients.trennzeichen+ "SerienN"+clients.trennzeichen+ "" + clients.checkDaten(proSSN.getText()) + ""+clients.trennzeichen  + ID + ""+clients.trennzeichen+ "IDSC");
                clients.send("2002"+clients.trennzeichen+ "CServer"+clients.trennzeichen+ "Typ"+clients.trennzeichen  + clients.checkDaten(proSName.getText()) + ""+clients.trennzeichen  + ID + ""+clients.trennzeichen+ "IDSC");
            }
            activ(false);
            this.setVisible(false);
            this.dispose();
        }
    }

    private void delete() {
        Object[] options = {"Yes, please",
            "No, thanks",};
        int del = JOptionPane.showOptionDialog(new JFrame(),
                "Möchten Sie wirklich den Server LÖSCHEN??",
                "Question",
                JOptionPane.YES_NO_CANCEL_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                options,
                options[1]);
        if (del == 0) {
            String ID = "";
            for (int i = 0; i < this.datenCS.size(); i++) {
                if (this.datenCS.get(i).get(1).equals(proSServer.getSelectedItem().toString())) {
                    ID = datenCS.get(i).get(0).toString();
                }
            }
            clients.send("2002"+clients.trennzeichen+ "CServer"+clients.trennzeichen+ "gruppe"+clients.trennzeichen+ "OLD"+clients.trennzeichen  + ID + ""+clients.trennzeichen+ "IDSC");
            clients.send("2002"+clients.trennzeichen+ "CServer"+clients.trennzeichen+ "IP"+clients.trennzeichen+ "KEINE"+clients.trennzeichen  + ID + ""+clients.trennzeichen+ "IDSC");
        }
        this.setVisible(false);
        this.dispose();
    }

    public void setGrafik(ArrayList<ArrayList<String>> Netz) {
        NetzDaten = Netz;
        initComponents();
        tabProtokoll = tableProtokoll.getTable(titlesProtokoll, -1, null, -1, null);
        tabProtokoll.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        TableColumn col = tabProtokoll.getColumnModel().getColumn(0);
        col.setPreferredWidth(100);
        col.setResizable(false);
        TableColumn col1 = tabProtokoll.getColumnModel().getColumn(1);
        col1.setPreferredWidth(50);
        col1.setResizable(false);
        TableColumn col2 = tabProtokoll.getColumnModel().getColumn(2);
        col2.setPreferredWidth(1075);
        col2.setResizable(false);
        proSScroll.add(tabProtokoll);
        proSScroll.setViewportView(tabProtokoll);

        try {
            datenCS = (ArrayList<ArrayList<String>>) clients.netzwerkVerwaltung.getHardware(NetzDaten.get(0).get(0).toString(),"alloOld","ServerProtokollDaten/setGrafik",false).clone();
            proSServer.removeAllItems();
            for (int i = 0; i < datenCS.size(); i++) {
                proSServer.addItem(datenCS.get(i).get(1));
            }
        } catch (Exception ex) {
            jButton1.setEnabled(false);
        }
        try {
            setInfo(0, datenCS, NetzDaten.get(0).get(1).toString());
        } catch (Exception e) {
            proSServer.addItem("--");
            while (tableProtokoll.getInfoTable.getRowCount() > 0) {
                tableProtokoll.getInfoTable.removeRowAt(0);
            }
            proSName.setText("");
            proSFunktion.setText("");
            proSLE.setText("");
            proSSN.setText("");
            proSIP.setText("");
            proSM.setText("");
        }

        this.setVisible(true);
    }

    private void setInfo(int wert, ArrayList<ArrayList<String>> datenCServer, String NetzwerkName) {
        IDSC = datenCServer.get(wert).get(0).toString();
        datenP = (ArrayList<ArrayList<String>>) clients.netzwerkVerwaltung.getProtokoll(IDSC, NetzwerkName, proSGruppe.getSelectedItem().toString(),"ServerProtokollDaten/setInfo",false).clone();

        String IP = "";
        while (tableProtokoll.getInfoTable.getRowCount() > 0) {
            tableProtokoll.getInfoTable.removeRowAt(0);
        }

        getInfo(NetzwerkName, IDSC, proSGruppe.getSelectedItem().toString());

        for (int i = 0; i < getProtokollDaten.size(); i++) {
            tableProtokoll.getInfoTable.addRow(getProtokollDaten.get(i), "1");
        }

        proSName.setText(datenCS.get(wert).get(5).toString());
        proSFunktion.setText(datenCS.get(wert).get(2).toString());
        proSLE.setText(datenCS.get(wert).get(3).toString());
        proSSN.setText(datenCS.get(wert).get(4).toString());

        for (int i = 0; i < NetzDaten.size(); i++) {
            if (NetzDaten.get(i).get(0).equals(datenCS.get(wert).get(8).toString())) {
                String[] ip = NetzDaten.get(i).get(2).toString().split("\\.");
                if (datenCS.get(wert).get(6).toString().equals("DHCP")) {
                    IP = "DHCP";
                } else if (datenCS.get(wert).get(6).toString().equals("KEINE")) {
                    IP = "KEINE";
                } else {
                    IP = ip[0] + "." + ip[1] + "." + ip[2] + "." + datenCS.get(wert).get(6).toString();
                }
                proSIP.setText(IP);
                proSM.setText(NetzDaten.get(i).get(3).toString());
            }
        }
    }

    public void getInfo(String NetzwerkName, String IDSCinfo, String Gruppe) {
        getProtokollDaten = new ArrayList<ArrayList<String>>();
        datenP = (ArrayList<ArrayList<String>>) clients.netzwerkVerwaltung.getProtokoll(IDSCinfo, NetzwerkName, Gruppe,"ServerProtokollDaten/getInfo",false).clone();
        for (int i = 0; i < datenP.size(); i++) {
            getProtokollDatenTemp = new ArrayList<String>();
            getProtokollDatenTemp.add(datenP.get(i).get(3).toString());
            getProtokollDatenTemp.add(datenP.get(i).get(2).toString());
            getProtokollDatenTemp.add(datenP.get(i).get(1).toString());
            getProtokollDatenTemp.add(IDSCinfo);
            getProtokollDaten.add(getProtokollDatenTemp);
        }
    }

    private void cc() {
        proSName.setText("");
        proSFunktion.setText("");
        proSLE.setText("");
        proSSN.setText("");
        proSIP.setText("");
        proSM.setText("");
        while (tabProtokoll.getRowCount() > 0) {
            tableProtokoll.getInfoTable.removeRowAt(0);
        }
    }
    public ArrayList<ArrayList<String>> getProtokollDaten = new ArrayList<ArrayList<String>>();
    private ArrayList<String> getProtokollDatenTemp;

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        proSSN = new javax.swing.JTextField();
        proSScroll = new javax.swing.JScrollPane();
        proSIP = new javax.swing.JTextField();
        proSM = new javax.swing.JTextField();
        proSFunktion = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        proSLE = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        proSName = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jLabel8 = new javax.swing.JLabel();
        proSGruppe = new javax.swing.JComboBox();
        proSServer = new javax.swing.JComboBox();
        bearbeiten = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Server Protokoll");
        setResizable(false);

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        proSSN.setEnabled(false);

        proSIP.setEnabled(false);

        proSM.setEnabled(false);

        proSFunktion.setEnabled(false);

        jLabel4.setText("IP:");

        proSLE.setEnabled(false);

        jLabel5.setText("Server Typ:");

        jLabel1.setText("Leas Ende:");

        proSName.setEnabled(false);

        jLabel3.setText("Funktion:");

        jLabel2.setText("Maske:");

        jLabel6.setText("Serien-Nr.:");

        jButton1.setText("neuer Eintrag");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jLabel8.setText("Gruppe:");

        proSGruppe.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "all", "Server", "PC", "Notebook", "Terminal", "Switch", "Drucker", "Monitore", "OLD" }));
        proSGruppe.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                proSGruppeActionPerformed(evt);
            }
        });

        proSServer.setMaximumSize(new java.awt.Dimension(28, 20));
        proSServer.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                proSServerItemStateChanged(evt);
            }
        });
        proSServer.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                proSServerActionPerformed(evt);
            }
        });

        bearbeiten.setText("Bearbeiten");
        bearbeiten.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bearbeitenActionPerformed(evt);
            }
        });

        jButton2.setText("IP Sortieren");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel8)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(proSGruppe, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(bearbeiten, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jButton1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel5)
                            .addComponent(jLabel4)
                            .addComponent(jLabel2))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(proSIP, javax.swing.GroupLayout.PREFERRED_SIZE, 136, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(proSM, javax.swing.GroupLayout.PREFERRED_SIZE, 136, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(jLabel3)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(proSFunktion, javax.swing.GroupLayout.PREFERRED_SIZE, 324, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(jLabel1)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(proSLE, javax.swing.GroupLayout.PREFERRED_SIZE, 91, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jLabel6)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(proSSN))))
                            .addComponent(proSName)))
                    .addComponent(proSScroll)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                        .addComponent(proSServer, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton2)))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(proSServer, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton2))
                .addGap(2, 2, 2)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton1)
                    .addComponent(jLabel5)
                    .addComponent(proSName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(bearbeiten)
                    .addComponent(jLabel4)
                    .addComponent(proSIP, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3)
                    .addComponent(proSFunktion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(proSM, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(proSLE, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(proSSN, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel6)
                    .addComponent(jLabel1)
                    .addComponent(jLabel2)
                    .addComponent(proSGruppe, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel8))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(proSScroll, javax.swing.GroupLayout.PREFERRED_SIZE, 295, javax.swing.GroupLayout.PREFERRED_SIZE)
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
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void proSGruppeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_proSGruppeActionPerformed
        try {
            if (proSGruppe.getSelectedItem().toString().equalsIgnoreCase("all"))
            datenCS = (ArrayList<ArrayList<String>>) clients.netzwerkVerwaltung.getHardware(NetzDaten.get(0).get(0).toString(), "alloOld","ServerProtokollDaten/proSGruppeActionPerformed",false).clone();
            else
            datenCS = (ArrayList<ArrayList<String>>) clients.netzwerkVerwaltung.getHardware(NetzDaten.get(0).get(0).toString(), proSGruppe.getSelectedItem().toString(),"ServerProtokollDaten/proSGruppeActionPerformed",false).clone();
            try{
            proSServer.removeAllItems();
            }catch(Exception e){
                
            }
            for (int i = 0; i < datenCS.size(); i++) {
                proSServer.addItem(datenCS.get(i).get(1));
            }
            try {
                setInfo(0, datenCS, NetzDaten.get(0).get(1).toString());
            } catch (Exception e) {
                cc();
            }
        } catch (Exception e) {
            proSServer.addItem(""+clients.trennzeichen );
        }
        if (proSGruppe.getSelectedItem().equals("OLD")) {
            jButton1.setEnabled(false);
            bearbeiten.setEnabled(false);
        } else {
            jButton1.setEnabled(true);
            bearbeiten.setEnabled(true);
        }
    }//GEN-LAST:event_proSGruppeActionPerformed

    private void bearbeitenActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bearbeitenActionPerformed
        if (proSFunktion.isEnabled()) {
            up();
        } else {
            Object[] possibilities = {"Updaten", "Loeschen"};
            String s = (String) JOptionPane.showInputDialog(
                    new JFrame(),
                    "",
                    "Auswahl",
                    JOptionPane.OK_OPTION,
                    null,
                    possibilities,
                    "Server");
            if (s != null) {
                if (s.equals("Updaten")) {
                    up();
                } else {
                    delete();
                }
            }
        }
    }//GEN-LAST:event_bearbeitenActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        creatProtokollDaten tt = new creatProtokollDaten(IDSC, clients,proSServer.getSelectedItem().toString());
    }//GEN-LAST:event_jButton1ActionPerformed

    private void proSServerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_proSServerActionPerformed
      datenCS = (ArrayList<ArrayList<String>>) clients.netzwerkVerwaltung.getHardware(NetzDaten.get(0).get(0).toString(), proSGruppe.getSelectedItem().toString(),"ServerProtokollDaten/setGrafik",false).clone();
      setInfo(proSServer.getSelectedIndex(), datenCS, NetzDaten.get(0).get(1).toString());
    }//GEN-LAST:event_proSServerActionPerformed

    private void proSServerItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_proSServerItemStateChanged
        
    }//GEN-LAST:event_proSServerItemStateChanged

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton2ActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton bearbeiten;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JTextField proSFunktion;
    private javax.swing.JComboBox proSGruppe;
    private javax.swing.JTextField proSIP;
    private javax.swing.JTextField proSLE;
    private javax.swing.JTextField proSM;
    private javax.swing.JTextField proSName;
    private javax.swing.JTextField proSSN;
    private javax.swing.JScrollPane proSScroll;
    private javax.swing.JComboBox proSServer;
    // End of variables declaration//GEN-END:variables
}
