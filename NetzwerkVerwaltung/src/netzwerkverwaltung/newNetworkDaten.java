/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * 
 * 
 * 
 * <style type='text/css'>"
 + "#infoTabelle"
 + "{"
 + " width:100%;"
 + "  height:100%;"
 + "align:center;"
 + "border:1px solid black; "
 + "border-bottom:3px solid black;  "
 + "border-right:3px solid black;"
 + "border-collapse:collapse;"
 + "}"
 + "#inhaltTabelle"
 + "{"
 + "    width:95%;"
 + " height:95%;"
 + "    align:center;"
 + "}"
 + "#th{"
 + "    valign:top;"
 + "    align:right;"
 + "}"
 + "#tr, #td {"
 + "    valign:button; "
 + "    colspan:2;"
 + "    border:1px solid black;   "
 + "}"
 + "#tr:hover"
 + "{"
 + "    background-color: #CECECE "
 + "}"
 + "</style>
 * 
 * newNetworkDaten.java
 *
 * Created on 29.12.2011, 13:55:24
 */
package netzwerkverwaltung;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTable;

public class newNetworkDaten extends javax.swing.JFrame {

    private ClientNetzwerk clients;
    private ArrayList<ArrayList<String>> datenNetz,standortDaten;
    private String[] titles = {"Bereich", "begin", "end"};
    private JTable tabNetzA, tabNetzNeu;
    private TabelFont tableNetzA = new TabelFont(), tableNetzNeu = new TabelFont();
  

    public newNetworkDaten(ClientNetzwerk client, ArrayList<ArrayList<String>> dN, initServer init, HauptBildschirm haupt) {
        clients = client;

        initComponents();
        init.infoProgress.setForeground(Color.BLACK);
        init.infoProgress.setValue(10);
        datenNetz = dN;
        init.infoProgress.setForeground(Color.BLACK);
        init.infoProgress.setValue(70);
        standortDaten = clients.netzwerkVerwaltung.getNetzAdresse("-1", "newNetworkDaten/newNetworkDaten",false);
        init.infoProgress.setValue(140);
        init.infoProgress.setForeground(Color.GREEN);

        for (int i = 0; i < standortDaten.size(); i++) {
            Standort.addItem(standortDaten.get(i).get(1));
            if (i == 0) {
                strasse.setText(standortDaten.get(0).get(2).toString());
                Ort.setText(standortDaten.get(0).get(3).toString());
            }
        }

        tabNetzA = tableNetzA.getTable(titles, -1, null, -1, null);
        scNetzA.add(tabNetzA);
        scNetzA.setViewportView(tabNetzA);

        tabNetzNeu = tableNetzNeu.getTable(titles, -1, null, -1, null);
        scNetzNeu.add(tabNetzNeu);
        scNetzNeu.setViewportView(tabNetzNeu);


        for (int i = 0; i < datenNetz.size(); i++) {
            netz.addItem(datenNetz.get(i).get(1));

        }
        if (datenNetz.size() > 0) {
            setInfoNetz(0);
            aendern.setEnabled(true);
        }
        this.setVisible(true);
        this.setLocationRelativeTo(haupt);
    }

    private void setEnable(Boolean w) {
        AnetzDHCP.setSelected(w);
        anetzDHCPb.setEnabled(w);
        anetzDHCPe.setEnabled(w);
        add.setEnabled(w);
        del.setEnabled(w);
        scNetzA.setEnabled(w);
    }

    private void setInfoNetz(int auswahl) {
        tableNetzA.getInfoTable.removeAllItems();
        aMaske.setText(datenNetz.get(auswahl).get(3).toString());
        aSubnetz.setText(datenNetz.get(auswahl).get(2).toString());
        afIPs.setText(datenNetz.get(auswahl).get(5).toString());
        String[] DHCPBereich = new String[2];
        ArrayList<String> tempInfo;
        String[] dhcp = datenNetz.get(auswahl).get(4).toString().split(" - ");
        if (dhcp.length > 0) {
            if (dhcp[0].equals("0;0")) {
                setEnable(false);
            } else {
                setEnable(true);
                for (int a = 0; a < dhcp.length; a++) {
                    tempInfo = new ArrayList<String>();
                    DHCPBereich = dhcp[a].split(";");
                    tempInfo.add("Bereich " + a);
                    if (DHCPBereich.length > 1) {
                        tempInfo.add(DHCPBereich[0]);
                        tempInfo.add(DHCPBereich[1]);
                    } else {
                        tempInfo.add(0 + "");
                        tempInfo.add(DHCPBereich[0]);
                    }
                    tableNetzA.getInfoTable.addRow(tempInfo, "1");
                }
            }
        }
    }

    private String checkNetzDoppelt(String Daten) {

        ArrayList<ArrayList<String>> daten = (ArrayList<ArrayList<String>>) clients.netzwerkVerwaltung.getNetz("-1", "checkNetzDoppelt",false).clone();
        for (int i = 0; i < daten.size(); i++) {
            if (daten.get(i).get(0).equals(Daten)) {
                String s = (String) JOptionPane.showInputDialog(
                        new JFrame(),
                        "User schon vorhanden!! \n \n Username:",
                        "Fehler",
                        JOptionPane.PLAIN_MESSAGE,
                        null, null,
                        "");
                if (s != null) {
                    Daten = checkNetzDoppelt(s);
                } else {
                    Daten = null;
                }
            }
        }
        return Daten;
    }

    public boolean checkIP(String IP) {
        String[] ip = IP.split("\\.");
        if (ip.length == 4) {
            String[] ip1 = ip[0].split("");
            String[] ip2 = ip[1].split("");
            String[] ip3 = ip[2].split("");
            String[] ip4 = ip[2].split("");
            if ((ip1.length < 5) && (ip2.length < 5) && (ip3.length < 5) && (ip4.length < 5)) {
                return true;
            } else {
                JOptionPane.showMessageDialog(new JFrame(), "Fehler: falscher Aufbau vom Subnetz, Maske");
                return false;
            }
        } else {
            JOptionPane.showMessageDialog(new JFrame(), "Fehler: Subnetz,Maske aufbau (x.x.x.x)");
            return false;
        }

    }

    private String getDHCP(JCheckBox chNetz, JTable table) {
        String dhcp = "";
        if (chNetz.isSelected()) {
            dhcp = "";

            if (table.getRowCount() > 0) {
                for (int i = 0; i < table.getRowCount(); i++) {
                    dhcp = dhcp + " - " + table.getValueAt(i, 1) + ";" + table.getValueAt(i, 2);
                }
                dhcp = dhcp.substring(3);
            } else {
                dhcp = "0;0";
            }
        } else {
            dhcp = "0;0";
        }
        return dhcp;
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel4 = new javax.swing.JPanel();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel2 = new javax.swing.JPanel();
        netzDHCPe = new javax.swing.JTextField();
        netzSub = new javax.swing.JTextField();
        netzDHCPb = new javax.swing.JTextField();
        scNetzNeu = new javax.swing.JScrollPane();
        jLabel4 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        netzAn = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        netzName = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        netzMask = new javax.swing.JTextField();
        addN = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        delN = new javax.swing.JButton();
        netzDHCP = new javax.swing.JCheckBox();
        jLabel10 = new javax.swing.JLabel();
        Standort = new javax.swing.JComboBox();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        strasse = new javax.swing.JTextField();
        Ort = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        anetzDHCPb = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();
        scNetzA = new javax.swing.JScrollPane();
        aMaske = new javax.swing.JTextField();
        aSubnetz = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        afIPs = new javax.swing.JTextField();
        jLabel12 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        anetzDHCPe = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        netz = new javax.swing.JComboBox();
        AnetzDHCP = new javax.swing.JCheckBox();
        aendern = new javax.swing.JButton();
        add = new javax.swing.JButton();
        del = new javax.swing.JButton();
        aendern1 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("new Network");
        setResizable(false);

        jPanel4.setBackground(javax.swing.UIManager.getDefaults().getColor("InternalFrame.activeTitleGradient"));

        jTabbedPane1.setBackground(javax.swing.UIManager.getDefaults().getColor("InternalFrame.activeTitleGradient"));

        jPanel2.setBackground(new java.awt.Color(133, 133, 149));

        netzDHCPe.setEnabled(false);

        netzDHCPb.setEnabled(false);

        scNetzNeu.setEnabled(false);

        jLabel4.setText("von:");

        jLabel1.setText("Netzwerk Name:");

        jLabel2.setText("Subnetz:");

        jLabel5.setText("bis:");

        jLabel6.setText("anzahl freier IPs:");

        jLabel3.setText("Subnetzmaske:");

        addN.setText("add");
        addN.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addNActionPerformed(evt);
            }
        });

        jButton2.setText("OK");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        delN.setText("del");
        delN.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                delNActionPerformed(evt);
            }
        });

        netzDHCP.setBackground(new java.awt.Color(133, 133, 149));
        netzDHCP.setText("DHCP");
        netzDHCP.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                netzDHCPActionPerformed(evt);
            }
        });

        jLabel10.setText("Standort:");

        Standort.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                StandortActionPerformed(evt);
            }
        });

        jLabel13.setText("Strasse:");

        jLabel14.setText("Ort:");

        strasse.setEditable(false);
        strasse.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                strasseActionPerformed(evt);
            }
        });

        Ort.setEditable(false);

        jButton1.setText("new Standort");
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
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(4, 4, 4)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel14)
                            .addComponent(jLabel13)
                            .addComponent(jLabel10))
                        .addGap(41, 41, 41)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(strasse)
                            .addComponent(Standort, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(Ort, javax.swing.GroupLayout.Alignment.TRAILING)))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(4, 4, 4)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                                .addComponent(jButton1)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jButton2))
                            .addComponent(scNetzNeu)))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(4, 4, 4)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel1)
                            .addComponent(jLabel2)
                            .addComponent(jLabel3))
                        .addGap(8, 8, 8)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(netzMask)
                            .addComponent(netzSub)
                            .addComponent(netzName)))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(5, 5, 5)
                        .addComponent(jLabel6)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(netzAn))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addComponent(netzDHCP)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(netzDHCPb, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel5)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(netzDHCPe, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(addN)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 14, Short.MAX_VALUE)
                        .addComponent(delN)))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(Standort, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel10))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel13)
                    .addComponent(strasse, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel14)
                    .addComponent(Ort, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(netzName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(netzSub, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(netzMask, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(netzAn, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(netzDHCP)
                    .addComponent(delN)
                    .addComponent(jLabel4)
                    .addComponent(netzDHCPb, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5)
                    .addComponent(netzDHCPe, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(addN))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(scNetzNeu, javax.swing.GroupLayout.DEFAULT_SIZE, 79, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton2)
                    .addComponent(jButton1))
                .addContainerGap())
        );

        jTabbedPane1.addTab("neu", jPanel2);

        jPanel3.setBackground(new java.awt.Color(133, 133, 149));
        jPanel3.setForeground(new java.awt.Color(255, 255, 255));

        anetzDHCPb.setEnabled(false);

        jLabel11.setText("Subnetzmaske:");

        scNetzA.setEnabled(false);

        jLabel7.setText("anzahl freier IPs:");

        jLabel12.setText("von:");

        jLabel8.setText("bis:");

        anetzDHCPe.setEnabled(false);

        jLabel9.setText("Subnetz:");

        netz.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                netzActionPerformed(evt);
            }
        });

        AnetzDHCP.setBackground(new java.awt.Color(133, 133, 149));
        AnetzDHCP.setText("DHCP");
        AnetzDHCP.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                AnetzDHCPActionPerformed(evt);
            }
        });

        aendern.setText("Ändern");
        aendern.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                aendernActionPerformed(evt);
            }
        });

        add.setText("add");
        add.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addActionPerformed(evt);
            }
        });

        del.setText("del");
        del.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                delActionPerformed(evt);
            }
        });

        aendern1.setText("Delete Netzwerk");
        aendern1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                aendern1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(scNetzA, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 300, Short.MAX_VALUE)
                    .addComponent(netz, javax.swing.GroupLayout.Alignment.LEADING, 0, 300, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel3Layout.createSequentialGroup()
                        .addComponent(AnetzDHCP)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 16, Short.MAX_VALUE)
                        .addComponent(jLabel12)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(anetzDHCPb, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel8)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(anetzDHCPe, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(add)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(del))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(aendern1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(aendern))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel11)
                            .addComponent(jLabel9)
                            .addComponent(jLabel7))
                        .addGap(15, 15, 15)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(afIPs)
                            .addComponent(aSubnetz)
                            .addComponent(aMaske))))
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(netz, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(aSubnetz, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel9))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel11)
                    .addComponent(aMaske, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(afIPs, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(AnetzDHCP)
                    .addComponent(jLabel12)
                    .addComponent(anetzDHCPb, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel8)
                    .addComponent(anetzDHCPe, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(add)
                    .addComponent(del))
                .addGap(7, 7, 7)
                .addComponent(scNetzA, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(aendern)
                    .addComponent(aendern1))
                .addContainerGap())
        );

        jTabbedPane1.addTab("ändern", jPanel3);

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 325, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void netzDHCPActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_netzDHCPActionPerformed
        if (netzDHCP.isSelected()) {
            netzDHCPb.setEnabled(true);
            netzDHCPe.setEnabled(true);
            addN.setEnabled(true);
            delN.setEnabled(true);
        } else {
            addN.setEnabled(false);
            delN.setEnabled(false);
            netzDHCPb.setEnabled(false);
            netzDHCPe.setEnabled(false);
        }
    }//GEN-LAST:event_netzDHCPActionPerformed

    private void addNActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addNActionPerformed
        ArrayList<String> temp = new ArrayList<String>();
        temp.add("Bereich " + tabNetzNeu.getRowCount());
        try {
            int i = Integer.parseInt(netzDHCPb.getText());
            i = Integer.parseInt(netzDHCPe.getText());
            temp.add(netzDHCPb.getText());
            temp.add(netzDHCPe.getText());
            tableNetzNeu.getInfoTable.addRow(temp, "1");
            netzDHCPb.setText("");
            netzDHCPe.setText("");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(new JFrame(), "Bitte nur Zahlen!");
            netzDHCPb.setText("");
            netzDHCPe.setText("");
        }

    }//GEN-LAST:event_addNActionPerformed

    private void delNActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_delNActionPerformed
        if (tabNetzNeu.getSelectedRow() != -1) {
            tabNetzNeu.remove(tabNetzNeu.getSelectedRow());
        }
    }//GEN-LAST:event_delNActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        try {
            if (!Standort.getSelectedItem().toString().isEmpty()) {
                String dhcp = getDHCP(netzDHCP, tabNetzNeu);
                int anzahl = Integer.parseInt(netzAn.getText());
                if (checkIP(netzSub.getText()) && checkIP(netzMask.getText())) {
                    String Netz = checkNetzDoppelt(netzName.getText());
                    if (Netz != null) {
                        clients.send("1000" + clients.trennzeichen + ""
                                + Netz + "" + clients.trennzeichen
                                + clients.checkDaten(netzSub.getText()) + clients.trennzeichen
                                + clients.checkDaten(netzMask.getText()) + "" + clients.trennzeichen
                                + clients.checkDaten(dhcp) + "" + clients.trennzeichen
                                + clients.checkDaten(netzAn.getText()) + clients.trennzeichen+ standortDaten.get(Standort.getSelectedIndex()).get(0));
                    

                    netzName.setText("");
                    netzSub.setText("");
                    netzMask.setText("");
                    netzDHCPb.setText("");
                    netzDHCPe.setText("");
                    netzAn.setText("");
                    netzDHCPb.setEditable(false);
                    netzDHCPe.setEditable(false);
                    netzDHCP.setSelected(false);
                    tableNetzNeu.getInfoTable.removeAllItems();
                    }else{
                      JOptionPane.showMessageDialog(new JFrame(), "Netzwerkname schon vorhanden!");  
                    }

                }
            } else {
                JOptionPane.showMessageDialog(new JFrame(), "Kein Standort ausgewählt");

            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(new JFrame(), "Leider konnte die Anzahl nicht ermittelt werden bitte geben Sie diese mit ganzen Zahlen an (0 - X)");
        }
    }//GEN-LAST:event_jButton2ActionPerformed

    private void delActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_delActionPerformed


        if (tabNetzA.getSelectedRow() != -1) {
            tableNetzA.getInfoTable.removeRowAt(tabNetzA.getSelectedRow());
        }
    }//GEN-LAST:event_delActionPerformed

    private void addActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addActionPerformed
        ArrayList<String> temp = new ArrayList<String>();
        temp.add("Bereich " + tabNetzA.getRowCount());
        temp.add(anetzDHCPb.getText());
        temp.add(anetzDHCPe.getText());
        tableNetzA.getInfoTable.addRow(temp, "1");
    }//GEN-LAST:event_addActionPerformed

    private void AnetzDHCPActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_AnetzDHCPActionPerformed
        if (AnetzDHCP.isSelected()) {
            setEnable(true);
        } else {
            setEnable(false);
        }
    }//GEN-LAST:event_AnetzDHCPActionPerformed

    private void netzActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_netzActionPerformed
        setInfoNetz(netz.getSelectedIndex());
    }//GEN-LAST:event_netzActionPerformed

    private void aendernActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_aendernActionPerformed
        ArrayList<String> update = new ArrayList<String>();
        update.add("2002" + clients.trennzeichen + "Network" + clients.trennzeichen + "Netz" + clients.trennzeichen + clients.checkDaten(aSubnetz.getText()) + "" + clients.trennzeichen + "IDN" + clients.trennzeichen + datenNetz.get(netz.getSelectedIndex()).get(0));
        update.add("2002" + clients.trennzeichen + "Network" + clients.trennzeichen + "Maske" + clients.trennzeichen + clients.checkDaten(aMaske.getText()) + "" + clients.trennzeichen + "IDN" + clients.trennzeichen + datenNetz.get(netz.getSelectedIndex()).get(0));
        update.add("2002" + clients.trennzeichen + "Network" + clients.trennzeichen + "anz" + clients.trennzeichen + clients.checkDaten(afIPs.getText()) + "" + clients.trennzeichen + "IDN" + clients.trennzeichen + datenNetz.get(netz.getSelectedIndex()).get(0));
        update.add("2002" + clients.trennzeichen + "Network" + clients.trennzeichen + "DHCP" + clients.trennzeichen + clients.checkDaten(getDHCP(AnetzDHCP, tabNetzA)) + "" + clients.trennzeichen + "IDN" + clients.trennzeichen + datenNetz.get(netz.getSelectedIndex()).get(0));
        clients.ClientVersand(update);
        this.setVisible(false);
        this.dispose();
    }//GEN-LAST:event_aendernActionPerformed

    private void strasseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_strasseActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_strasseActionPerformed

    private void StandortActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_StandortActionPerformed
        int i = Standort.getSelectedIndex();
        strasse.setText(standortDaten.get(i).get(2).toString());
        Ort.setText(standortDaten.get(i).get(4).toString());
    }//GEN-LAST:event_StandortActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        addAdressbuch ad = new addAdressbuch(clients, null, null);
        ad.newNetzAdresse();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void aendern1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_aendern1ActionPerformed
        int n = JOptionPane.showConfirmDialog(
                new JFrame(),
                "Möchten Sie wirklich das Netz löschen?",
                "Netz löschen",
                JOptionPane.YES_NO_OPTION);
        if (n == 0) {
            ArrayList<ArrayList<String>> cs = (ArrayList<ArrayList<String>>) clients.netzwerkVerwaltung.getHardware(datenNetz.get(netz.getSelectedIndex()).get(0).toString(), "all", "newNetworkDaten/aendern11ActionPerformed",false).clone();
            if (!cs.isEmpty()) {
                Object[] possibilities = new Object[datenNetz.size()];
                for (int i = 0; i < datenNetz.size(); i++) {
                    possibilities[i] = datenNetz.get(i).get(1);
                }
                String id = "";

                String s = (String) JOptionPane.showInputDialog(
                        new JFrame(),
                        "mit welchem Netz sollen die\n"
                        + "Clients(" + cs.size() + ") verknüpft werden",
                        "Clients",
                        JOptionPane.PLAIN_MESSAGE,
                        null,
                        possibilities,
                        null);
                if (s != null && !s.equals("")) {
                    for (int i = 0; i < datenNetz.size(); i++) {
                        if (s.equals(datenNetz.get(i).get(1))) {
                            id = datenNetz.get(i).get(0).toString();
                            break;
                        }
                    }

                    for (int i = 0; i < cs.size(); i++) {
                        clients.send("2003" + clients.trennzeichen + "CServer" + clients.trennzeichen + "IDN" + clients.trennzeichen + id + clients.trennzeichen + "IDSC" + clients.trennzeichen + cs.get(i).get(0));
                    }
                }
                // DELETE
            }
            clients.send("2003" + clients.trennzeichen + "Network" + clients.trennzeichen + "IDAdressbuchFirma" + clients.trennzeichen + "-2" + clients.trennzeichen + "IDN" + clients.trennzeichen + datenNetz.get(netz.getSelectedIndex()).get(0).toString());
           }
        this.setVisible(false);
        this.dispose();
    }//GEN-LAST:event_aendern1ActionPerformed
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JCheckBox AnetzDHCP;
    private javax.swing.JTextField Ort;
    private javax.swing.JComboBox Standort;
    private javax.swing.JTextField aMaske;
    private javax.swing.JTextField aSubnetz;
    private javax.swing.JButton add;
    private javax.swing.JButton addN;
    private javax.swing.JButton aendern;
    private javax.swing.JButton aendern1;
    private javax.swing.JTextField afIPs;
    private javax.swing.JTextField anetzDHCPb;
    private javax.swing.JTextField anetzDHCPe;
    private javax.swing.JButton del;
    private javax.swing.JButton delN;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JComboBox netz;
    private javax.swing.JTextField netzAn;
    private javax.swing.JCheckBox netzDHCP;
    private javax.swing.JTextField netzDHCPb;
    private javax.swing.JTextField netzDHCPe;
    private javax.swing.JTextField netzMask;
    private javax.swing.JTextField netzName;
    private javax.swing.JTextField netzSub;
    private javax.swing.JScrollPane scNetzA;
    private javax.swing.JScrollPane scNetzNeu;
    private javax.swing.JTextField strasse;
    // End of variables declaration//GEN-END:variables
}
