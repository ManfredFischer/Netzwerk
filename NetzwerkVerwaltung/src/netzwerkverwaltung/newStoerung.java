/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * newStoerung.java
 *
 * Created on 27.12.2011, 15:58:55
 */
package netzwerkverwaltung;

import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.TimeZone;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTable;

/**
 *
 * @author manfred.fischer
 */
public class newStoerung extends javax.swing.JFrame {

    private String Schreibschutz = "off", UserName, Bearbeiter = "", DNeu = "ja";
    private HauptBildschirm haupt;
    private ClientNetzwerk client;
    private ArrayList<String> DatenInfo;
    private int IDU;
    private File Datei;
    private searchAdressbuch AD;
    private ArrayList<String> datenInhalt;
    private String[] titles = new String[]{"Anhänge"};
    private JTable tabAnhaenge;
    private TabelFont tableAnhaenge = new TabelFont();
    public boolean AdressWahl = true, change = true;
    private ArrayList<String> datenAktualisieren = new ArrayList<String>();
    private ArrayList<ArrayList<String>> ADFirma, ADP;
    private ArrayList<File> files = new ArrayList<File>();
    private Boolean anhaengeAenderung = false;
    public Boolean Adressaenderung = false;
    ArrayList<ArrayList<String>> kontoDaten;

    public newStoerung(String titel, String Schreibschutz, int UserID, ArrayList<String> Daten, String SelectetUser, ArrayList<String> DatenFirma, ArrayList<String> DatenFirmaPerson, ClientNetzwerk c, String UserN, HauptBildschirm h, ArrayList<File> dateien) {
        initComponents();
        try {
            String IDF = "0", IDPerson = "0";
            if (SelectetUser != null) {
                IDF = SelectetUser.split(";")[0];
                IDPerson = SelectetUser.split(";")[1];
            }

            IDU = UserID;
            haupt = h;
            this.Schreibschutz = Schreibschutz;
            client = c;
            this.setTitle(titel);
            DatenInfo = Daten;
            UserName = UserN;
            kontoDaten = (ArrayList<ArrayList<String>>) client.netzwerkVerwaltung.datenKonto("UserAnmeldung/checkUser",false).clone();

            tabAnhaenge = tableAnhaenge.getTable(titles, -1, null, -1, null);
            anhaenge.add(tabAnhaenge);
            anhaenge.setViewportView(tabAnhaenge);
            tabAnhaenge.setFillsViewportHeight(true);
            tabAnhaenge.addMouseListener(new MouseAdapter() {

                @Override
                public void mouseClicked(MouseEvent e) {
                    if (e.getButton() == 3) {
                        if (tabAnhaenge.getSelectedRow() != -1) {
                            DateiSpeichern();
                        } else {
                            Dateihinzufuegen();
                        }
                    }
                    if (e.getClickCount() == 2) {
                        if (tabAnhaenge.getSelectedRow() != -1) {
                            File file = AnhaengeDateien.get(tabAnhaenge.getSelectedRow());
                            if (file.exists()) {
                                try {
                                    new ProcessBuilder("cmd", "/c", file.getAbsolutePath()).start();
                                } catch (IOException ex) {
                                }
                            }
                        }
                    }

                }
            });


            inhalt.addMouseListener(new MouseAdapter() {

                @Override
                public void mouseClicked(MouseEvent e) {
                    if (e.getButton() == 3) {
                        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        format.setTimeZone(TimeZone.getDefault());
                        Date datum = new Date();
                        try {
                            inhalt.insert("[" + UserName.toUpperCase() + " " + format.format(datum) + "]\n", inhalt.getCaretPosition());
                            inhalt.append("");
                        } catch (Exception ee) {
                            inhalt.append("[" + UserName.toUpperCase() + " " + format.format(datum) + "]\n");
                            inhalt.append("");
                        }
                    }
                }
            });

            ADFirma = (ArrayList<ArrayList<String>>) client.netzwerkVerwaltung.getAdressbuchFirma("-1", "newStoerung/newStoerung",false).clone();
            for (int i = 0; i < ADFirma.size(); i++) {
                cbFirma.addItem(ADFirma.get(i).get(1));
                if (SelectetUser != null) {
                    if (IDF.equals(ADFirma.get(i).get(0))) {
                        cbFirma.setSelectedIndex(i);
                    }
                }
            }

            ADP = (ArrayList<ArrayList<String>>) client.netzwerkVerwaltung.getAdressbuchPerson("-2", ADFirma.get(cbFirma.getSelectedIndex()).get(0).toString(), "newStoerung/newStoerung",false).clone();
            for (int i = 0; i < ADP.size(); i++) {
                cbFirmaPerson.addItem(ADP.get(i).get(2) + ", " + ADP.get(i).get(1));
                if (SelectetUser != null) {
                    if (IDPerson.equals(ADP.get(i).get(0))) {
                        cbFirmaPerson.setSelectedIndex(i);
                    }
                }
            }

            cbFirma.addItemListener(new ItemListener() {

                @Override
                public void itemStateChanged(ItemEvent e) {
                    if (e.getStateChange() == 2) {
                        if (change) {
                            ADP = (ArrayList<ArrayList<String>>) client.netzwerkVerwaltung.getAdressbuchPerson("-2", ADFirma.get(cbFirma.getSelectedIndex()).get(0).toString(), "newStoerung/newStoerung",false).clone();
                            cbFirmaPerson.removeAllItems();
                            Adressaenderung = true;
                            for (int i = 0; i < ADP.size(); i++) {
                                cbFirmaPerson.addItem(ADP.get(i).get(2) + ", " + ADP.get(i).get(1));
                            }
                            AdressWahl = true;
                        }
                    }
                }
            });

            cWV.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    if (!cWV.isSelected()) {
                        wvDate.setEnabled(false);
                        wv.setEnabled(false);
                    } else {
                        wv.setEnabled(true);
                        wvDate.setEnabled(true);
                    }
                }
            });

            ArrayList<ArrayList<String>> K = (ArrayList<ArrayList<String>>) client.netzwerkVerwaltung.datenKonto("newStoerung/newStoerung",false).clone();
            for (int i = 0; i < K.size(); i++) {
                cbUser.addItem(K.get(i).get(1));

            }

            for (int i = 0; i < 24; i++) {
                for (int a = 0; a < 60; a = a + 10) {
                    if (i < 10 && a < 10) {
                        wvDate.addItem("0" + i + ":0" + a);
                    } else if (i < 10) {
                        wvDate.addItem("0" + i + ":" + a);
                    } else if (a < 10) {
                        wvDate.addItem(i + ":0" + a);
                    } else {
                        wvDate.addItem(i + ":" + a);
                    }
                }
            }

            if (Schreibschutz.equals("on")) {
                ok.setText("Close");
                //suche.setEnabled(false);
            }
            if (Daten != null) {
                setInfo(DatenFirma, DatenFirmaPerson, dateien);
            }
        } catch (Exception ex) {
            System.out.println(ex);
        }
        this.setVisible(true);
        this.setLocationRelativeTo(h);

    }
    private ArrayList<File> AnhaengeDateien = new ArrayList<File>(),
            AnhaengeNeu = new ArrayList<File>();

    private void Dateihinzufuegen() {

        JFileChooser fc = new JFileChooser();
        fc.setDialogTitle("Datei Auswahl");
        if (fc.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
            Datei = fc.getSelectedFile();
            AnhaengeNeu.add(Datei);
            ArrayList<String> tmp = new ArrayList<String>();
            tmp.add("(temporär):" + Datei.getName());
            tableAnhaenge.getInfoTable.addRow(tmp, "1");
            anhaengeAenderung = true;
        }

    }

    private void DateiEntfernen() {
        if (tabAnhaenge.getValueAt(tabAnhaenge.getSelectedRow(), 0).toString().startsWith("(temporär):")) {
            AnhaengeDateien.remove(tabAnhaenge.getSelectedRow());
            anhaengeAenderung = true;
            tableAnhaenge.getInfoTable.removeRowAt(tabAnhaenge.getSelectedRow());
        } else {
            haupt.vStoerung.AnhaengeStoerungen.get(tabAnhaenge.getSelectedRow()).delete();
            tableAnhaenge.getInfoTable.removeRowAt(tabAnhaenge.getSelectedRow());
        }
    }

    private void DateiSpeichern() {
        JFileChooser fc = new JFileChooser();
        fc.setDialogTitle("Select folder");
        fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        if (fc.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
            try {
                File dateiSend = new File(fc.getSelectedFile().getAbsolutePath() + "/" + tabAnhaenge.getValueAt(tabAnhaenge.getSelectedRow(), 0));
                client.copyFile(AnhaengeDateien.get(tabAnhaenge.getSelectedRow()), dateiSend);
            } catch (Exception ex) {
            }
        }
    }

    private void InsertUser() {


        AD = new searchAdressbuch(client, haupt, this);
        ArrayList<HashMap> User;
        if (rbv.isSelected()) {
            User = AD.searchUser(null, searchUser.getText());
            Name(User, null, searchUser.getText());
        } else {
            User = AD.searchUser(searchUser.getText(), null);
            Name(User, searchUser.getText(), null);
        }



    }

    private void Name(ArrayList<HashMap> User, String Nachname, String Vorname) {
        if (User.isEmpty()) {
            Object[] options = {"Ja - " + cbFirma.getSelectedItem(),
                "Ja, neue Firma", "Nein, Danke"};
            int n = JOptionPane.showOptionDialog(new JFrame(),
                    "User nicht Vorhanden. \n Soll der User neu angelegt werden? ",
                    "new User",
                    JOptionPane.YES_NO_CANCEL_OPTION,
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    options, options[0]);
            if (n == 0) {
                addAdressbuch ad = new addAdressbuch(client, this, haupt);
                ad.newAdressPersonInfo(Vorname, Nachname, ADFirma.get(cbFirma.getSelectedIndex()).get(0).toString());

            } else if (n == 1) {
                addAdressbuch ad = new addAdressbuch(client, this, haupt);
                ad.newAdressFirma(Nachname, Vorname);

            }

        } else {
            AD.getSearchUser(Nachname, Vorname);

        }
    }

    private String checkDaten(String Check) {
        if (Check.equals("")) {
            Check = "   ";
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
    private String pfadStoerung = "", OrdnerStoerung = "";

    private void setInfo(ArrayList<String> DatenFirma, ArrayList<String> DatenPerson, ArrayList<File> dateien) throws IOException {


        for (int a = 0; a < kontoDaten.size(); a++) {
            if (kontoDaten.get(a).get(0).equals(DatenInfo.get(7))) {
                Bearbeiter = kontoDaten.get(a).get(1).toString();
                break;
            }
        }

        files = dateien;
        ArrayList<String> datenTemp;
        if (files.isEmpty()) {
            String path = "//" + client.sIP + "/Stoerung/" + IDU + "/" + DatenInfo.get(0).toString() + "-" + IDU + "/";
            String dateiname = DatenInfo.get(0).toString() + "-0-" + String.valueOf(Math.round(Math.random() * 999999)) + "-Stoerung.txt";
            new File(path).mkdir();
            File datei = new File(path + dateiname);
            FileWriter write = new FileWriter(datei);
            write.close();
            files.add(new File(path + dateiname));
        }

        datenInhalt = new ArrayList<String>();
        OrdnerStoerung = files.get(0).getParent();
        for (int i = 0; i < files.size(); i++) {
            String[] temp = files.get(i).getName().split("-");

            if (temp[temp.length - 1].equals("Stoerung.txt")) {
                pfadStoerung = files.get(i).getAbsolutePath();
                try {
                    BufferedReader read = new BufferedReader(new FileReader(files.get(i)));
                    String info = read.readLine();
                    while (info != null) {
                        inhalt.append(info + "\n");
                        datenInhalt.add(info);
                        info = read.readLine();
                    }
                } catch (Exception ex) {
                }
            } else {
                datenTemp = new ArrayList<String>();
                AnhaengeDateien.add(files.get(i));
                String[] datenName = files.get(i).getName().split("-");
                String name = "";
                for (int a = 2; a < datenName.length; a++) {
                    name = name + datenName[a];
                }
                datenTemp.add(name);
                tableAnhaenge.getInfoTable.addRow(datenTemp, "1");
            }
        }



        DNeu = "nein";
        ok.setText("Save");
        betreffS.setText(DatenInfo.get(9).toString());
        melderS.setText(DatenInfo.get(4).toString());
        if (!DatenInfo.get(2).equals("00.00.0000-00:00")) {
            cWV.setSelected(true);
            wvDate.setEnabled(true);
            wv.setEnabled(true);
            String Datum = DatenInfo.get(2).toString().split("-")[0];
            String Uhrzeit = DatenInfo.get(2).toString().split("-")[1];
            wvDate.setSelectedItem(Uhrzeit);
            wv.setText(Datum);
        }
        cbUser.setSelectedItem(Bearbeiter);
        cbFirma.setSelectedItem(DatenFirma.get(1));
        String Name = DatenPerson.get(2) + ", " + DatenPerson.get(1);
        cbFirmaPerson.setSelectedItem(Name);
        Status.setSelectedItem(DatenInfo.get(3));
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane4 = new javax.swing.JScrollPane();
        inhalt = new javax.swing.JTextArea();
        jLabel1 = new javax.swing.JLabel();
        betreffS = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        melderS = new javax.swing.JTextField();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel9 = new javax.swing.JPanel();
        cbFirma = new javax.swing.JComboBox();
        jLabel3 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        cbFirmaPerson = new javax.swing.JComboBox();
        searchUser = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        rbv = new javax.swing.JRadioButton();
        jRadioButton2 = new javax.swing.JRadioButton();
        jPanel10 = new javax.swing.JPanel();
        jLabel9 = new javax.swing.JLabel();
        Status = new javax.swing.JComboBox();
        jLabel8 = new javax.swing.JLabel();
        cbUser = new javax.swing.JComboBox();
        cWV = new javax.swing.JCheckBox();
        wv = new javax.swing.JTextField();
        wvDate = new javax.swing.JComboBox();
        jPanel11 = new javax.swing.JPanel();
        anhaenge = new javax.swing.JScrollPane();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        ok = new javax.swing.JButton();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        jMenuItem1 = new javax.swing.JMenuItem();
        jMenuItem2 = new javax.swing.JMenuItem();
        jMenu2 = new javax.swing.JMenu();
        jMenuItem5 = new javax.swing.JMenuItem();
        jMenuItem4 = new javax.swing.JMenuItem();
        jMenuItem3 = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setResizable(false);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                windowsClosing(evt);
            }
        });

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));

        inhalt.setColumns(20);
        inhalt.setRows(5);
        jScrollPane4.setViewportView(inhalt);

        jLabel1.setText("Betreff:");

        jLabel2.setText("Melder:");

        jPanel9.setBackground(new java.awt.Color(153, 153, 153));

        cbFirma.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbFirmaActionPerformed(evt);
            }
        });

        jLabel3.setText("Firma:");

        jLabel6.setText("Person:");

        cbFirmaPerson.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                insert(evt);
            }
        });

        searchUser.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                searchUserActionPerformed(evt);
            }
        });

        jLabel7.setText("Suche:");

        rbv.setBackground(new java.awt.Color(153, 153, 153));
        buttonGroup1.add(rbv);
        rbv.setText("Vorname");

        jRadioButton2.setBackground(new java.awt.Color(153, 153, 153));
        buttonGroup1.add(jRadioButton2);
        jRadioButton2.setSelected(true);
        jRadioButton2.setText("Nachname");

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel6)
                    .addComponent(jLabel3)
                    .addComponent(jLabel7))
                .addGap(18, 18, 18)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(cbFirmaPerson, javax.swing.GroupLayout.Alignment.LEADING, 0, 291, Short.MAX_VALUE)
                    .addComponent(cbFirma, javax.swing.GroupLayout.Alignment.LEADING, 0, 291, Short.MAX_VALUE)
                    .addGroup(jPanel9Layout.createSequentialGroup()
                        .addComponent(searchUser, javax.swing.GroupLayout.DEFAULT_SIZE, 145, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(rbv)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jRadioButton2)))
                .addContainerGap())
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(cbFirma, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(1, 1, 1)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(cbFirmaPerson, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(10, 10, 10)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(searchUser, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(rbv)
                    .addComponent(jRadioButton2))
                .addContainerGap(48, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Person", jPanel9);

        jPanel10.setBackground(new java.awt.Color(1, 118, 173));

        jLabel9.setText("Status:");

        Status.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "neu", "in Arbeit", "wartend", "fertig" }));

        jLabel8.setText("Bearbeiter:");

        cWV.setBackground(new java.awt.Color(1, 118, 173));
        cWV.setText("WV:");

        wv.setEnabled(false);

        wvDate.setEnabled(false);

        javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
        jPanel10.setLayout(jPanel10Layout);
        jPanel10Layout.setHorizontalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(cWV)
                    .addComponent(jLabel9))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel10Layout.createSequentialGroup()
                        .addComponent(Status, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel8)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cbUser, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel10Layout.createSequentialGroup()
                        .addComponent(wv, javax.swing.GroupLayout.PREFERRED_SIZE, 91, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(wvDate, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap(93, Short.MAX_VALUE))
        );
        jPanel10Layout.setVerticalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(Status, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel8)
                    .addComponent(cbUser, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel9))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(wv, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(wvDate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cWV))
                .addContainerGap(74, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Informationen", jPanel10);

        jPanel11.setBackground(new java.awt.Color(1, 118, 173));

        anhaenge.setBackground(new java.awt.Color(1, 118, 173));
        anhaenge.setBorder(javax.swing.BorderFactory.createTitledBorder(""));

        jButton2.setText("Löschen");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jButton3.setText("add");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel11Layout = new javax.swing.GroupLayout(jPanel11);
        jPanel11.setLayout(jPanel11Layout);
        jPanel11Layout.setHorizontalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButton2, javax.swing.GroupLayout.DEFAULT_SIZE, 73, Short.MAX_VALUE)
                    .addComponent(jButton3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(anhaenge, javax.swing.GroupLayout.DEFAULT_SIZE, 267, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel11Layout.setVerticalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(anhaenge, javax.swing.GroupLayout.DEFAULT_SIZE, 111, Short.MAX_VALUE)
                    .addGroup(jPanel11Layout.createSequentialGroup()
                        .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 18, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );

        jTabbedPane1.addTab("Anhänge", jPanel11);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane4, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(melderS, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(betreffS)))
                .addContainerGap())
            .addComponent(jTabbedPane1)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 161, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(melderS, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1)
                    .addComponent(betreffS, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 314, Short.MAX_VALUE)
                .addContainerGap())
        );

        ok.setText("OK");
        ok.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                okActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(ok)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(ok, javax.swing.GroupLayout.PREFERRED_SIZE, 18, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jMenu1.setText("Datei");

        jMenuItem1.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_S, java.awt.event.InputEvent.CTRL_MASK));
        jMenuItem1.setText("Save");
        jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem1ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem1);

        jMenuItem2.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_E, java.awt.event.InputEvent.CTRL_MASK));
        jMenuItem2.setText("Close");
        jMenuItem2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem2ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem2);

        jMenuBar1.add(jMenu1);

        jMenu2.setText("Bearbeiten");
        jMenu2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenu2ActionPerformed(evt);
            }
        });

        jMenuItem5.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_W, java.awt.event.InputEvent.CTRL_MASK));
        jMenuItem5.setText("Anhänge");
        jMenuItem5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem5ActionPerformed(evt);
            }
        });
        jMenu2.add(jMenuItem5);

        jMenuItem4.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_M, java.awt.event.InputEvent.CTRL_MASK));
        jMenuItem4.setText("new Email");
        jMenuItem4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem4ActionPerformed(evt);
            }
        });
        jMenu2.add(jMenuItem4);

        jMenuItem3.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F, java.awt.event.InputEvent.CTRL_MASK));
        jMenuItem3.setText("freischalten");
        jMenuItem3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem3ActionPerformed(evt);
            }
        });
        jMenu2.add(jMenuItem3);

        jMenuBar1.add(jMenu2);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void windowsClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_windowsClosing
        if (Schreibschutz.equals("off")) {
            // client.send("2002"+client.trennzeichen+"Stoerung"+client.trennzeichen+"aktiv"+client.trennzeichen+"---"+client.trennzeichen+"IDStoerung"+client.trennzeichen+"" + DatenInfo.get(0).toString());
        }

    }//GEN-LAST:event_windowsClosing
//getAdressbuch();

    private void StoerungSave() {
        if (ok.getText().equals("Close")) {
            for (int i = 0; i < files.size(); i++) {
                files.get(i).delete();
            }

            this.setVisible(false);
            this.dispose();
        } else {
            if (DNeu.equals("ja")) {
                CreateDaten();
                for (int i = 0; i < files.size(); i++) {
                    files.get(i).delete();
                }
            } else {
                UpdateDaten();
                client.send("2002" + client.trennzeichen + "Stoerung" + client.trennzeichen + "aktiv" + client.trennzeichen + "---" + client.trennzeichen + "IDStoerung" + client.trennzeichen + "" + DatenInfo.get(0).toString());

            }
            haupt.vStoerung.setStoerung(haupt.vStoerung.StoerungsAuswahl,haupt.IDAdressbuchSelected.getText(),haupt.allStoerungen.isSelected());
            haupt.resetAdressbuch();

            this.setVisible(false);
            this.dispose();
        }
    }

    private void okActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_okActionPerformed
        StoerungSave();
    }//GEN-LAST:event_okActionPerformed

    private void insert(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_insert
        AdressWahl = true;
    }//GEN-LAST:event_insert

    private void searchUserActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_searchUserActionPerformed
        try {

            AdressWahl = false;
            InsertUser();
        } catch (Exception e) {
            AdressWahl = true;
        }
        Adressaenderung = false;
    }//GEN-LAST:event_searchUserActionPerformed

    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem1ActionPerformed
        StoerungSave();
    }//GEN-LAST:event_jMenuItem1ActionPerformed

    private void jMenuItem2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem2ActionPerformed
        this.setVisible(false);
        this.dispose();
    }//GEN-LAST:event_jMenuItem2ActionPerformed

    private void jMenu2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenu2ActionPerformed
        Dateihinzufuegen();
    }//GEN-LAST:event_jMenu2ActionPerformed

    private void jMenuItem4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem4ActionPerformed
    }//GEN-LAST:event_jMenuItem4ActionPerformed

    private void jMenuItem3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem3ActionPerformed
        String s = (String) JOptionPane.showInputDialog(new JFrame(), "Bitte geben Sie das PW ein", "Sicherheitsabfrage", JOptionPane.PLAIN_MESSAGE, null, null, "");
        if (s != null) {
            if (s.equals("k1ngSize")) {
                client.send("2002" + client.trennzeichen + "Stoerung" + client.trennzeichen + "aktiv" + client.trennzeichen + "---" + client.trennzeichen + DatenInfo.get(0).toString() + "" + client.trennzeichen + "IDStoerung");
                this.setVisible(false);
                this.dispose();
            }
        }
    }//GEN-LAST:event_jMenuItem3ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        if (tabAnhaenge.getSelectedRow() != -1) {
            DateiEntfernen();
        }
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        Dateihinzufuegen();
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jMenuItem5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem5ActionPerformed
        Dateihinzufuegen();
    }//GEN-LAST:event_jMenuItem5ActionPerformed

    private void cbFirmaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbFirmaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cbFirmaActionPerformed

    public void getAdressbuch() {
        AD = new searchAdressbuch(client, haupt, this);
        AD.getSearchUser(null, null);
        Adressaenderung = true;
    }
    public String IDAdressbuch = "0";

    private String getFirmaID() {
        //ADP = client.netzwerkVerwaltung.datenAdressbuchPerson("-1", ADFirma.get(cbFirma.getSelectedIndex()).get("IDAdressbuchFirma").toString());
        if (ADP != null) {
            if (!ADP.isEmpty()) {
                IDAdressbuch = ADP.get(cbFirmaPerson.getSelectedIndex()).get(0).toString();
            }
        }
        return IDAdressbuch;

    }

    private void CreateDaten() {

        SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy");
        Date dt = new Date();
        String IDBearbeiter = "0";
        if (AdressWahl) {
            IDAdressbuch = getFirmaID();
        }


        if (!cbUser.getSelectedItem().equals("") || cbUser.getSelectedItem() != null) {
            for (int i = 0; i < kontoDaten.size(); i++) {
                if (kontoDaten.get(i).get(1).equals(cbUser.getSelectedItem())) {
                    IDBearbeiter = kontoDaten.get(i).get(0).toString();
                }
            }
        }

        String Datum, Uhr;
        if (cWV.isSelected()) {
            Datum = wv.getText();
            Uhr = wvDate.getSelectedItem().toString();
        } else {
            Datum = "00.00.0000";
            Uhr = "00:00";
        }

        try {
            int info = (int) Math.round(Math.random() * 999999);
            System.setProperty("file.encoding", "UTF8");
            String Name = info + "-Stoerung.txt";
            File file = new File(Name);
            Writer out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), "UTF8"));
            out.append(checkDaten(inhalt.getText()));
            out.flush();
            out.close();
            files.add(file);
        } catch (Exception e) {
            System.out.println(e);
        }


        client.send("1901" + client.trennzeichen
                + format.format(dt) + client.trennzeichen
                + checkDaten(Datum) + "-" + checkDaten(Uhr) + client.trennzeichen
                + checkDaten(Status.getSelectedItem().toString()) + client.trennzeichen
                + checkDaten(melderS.getText()) + client.trennzeichen
                + checkDaten("--") + client.trennzeichen
                + checkDaten("" + IDU) + client.trennzeichen
                + checkDaten(IDBearbeiter) + client.trennzeichen
                + checkDaten(IDAdressbuch) + client.trennzeichen
                + checkDaten(betreffS.getText()) + client.trennzeichen
                + "---;0");

        String IDStoerung = client.netzwerkVerwaltung.getID();
        for (int i = 0; i < AnhaengeNeu.size(); i++) {
            files.add(AnhaengeNeu.get(i));
        }
        String pfad = client.sendDateiStoerung(files, IDU, Integer.parseInt(IDStoerung), "Stoerung/" + IDU);
        client.send("2002" + client.trennzeichen + "Stoerung" + client.trennzeichen + "Inhalt" + client.trennzeichen + pfad + "" + client.trennzeichen + "IDStoerung" + client.trennzeichen + "" + IDStoerung);

        this.setVisible(false);
        this.dispose();
    }

    private void UpdateDaten() {

        String IDBearbeiter = "0", IDA = "0";
        String Datum = "00.00.0000";
        String Uhrzeit = "00:00";
        if (cWV.isSelected()) {
            Datum = DatenInfo.get(2).toString().split("-")[0];
            Uhrzeit = DatenInfo.get(2).toString().split("-")[1];

            SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy");
            try {
                if (!format.format(wv.getText()).equals(Datum) || !wvDate.getSelectedItem().equals(Uhrzeit)) {
                    Datum = format.format(wv.getText()) + " - " + wvDate.getSelectedItem();
                    datenAktualisieren.add("2002" + client.trennzeichen + "Stoerung" + client.trennzeichen + "WVDatum" + client.trennzeichen + checkDaten(Datum) + "" + client.trennzeichen + "IDStoerung" + client.trennzeichen + "" + DatenInfo.get(0).toString());
                    datenAktualisieren.add("2002" + client.trennzeichen + "Stoerung" + client.trennzeichen + "gelesen" + client.trennzeichen + "0" + client.trennzeichen + "IDStoerung" + client.trennzeichen + "" + DatenInfo.get(0).toString());
                }
            } catch (Exception ex) {
            }

        }
        if (!cbUser.getSelectedItem().equals(Bearbeiter)) {

            for (int i = 0; i < kontoDaten.size(); i++) {
                if (kontoDaten.get(i).get(1).equals(cbUser.getSelectedItem())) {
                    IDBearbeiter = kontoDaten.get(i).get(0).toString();
                }
            }
            datenAktualisieren.add("2003" + client.trennzeichen + "Stoerung" + client.trennzeichen + "bearbeiter" + client.trennzeichen + IDBearbeiter + "" + client.trennzeichen + "IDStoerung" + client.trennzeichen + "" + DatenInfo.get(0).toString());
            datenAktualisieren.add("2002" + client.trennzeichen + "Stoerung" + client.trennzeichen + "gelesen" + client.trennzeichen + "0" + client.trennzeichen + "IDStoerung" + client.trennzeichen + "" + DatenInfo.get(0).toString());
        }

        if (!Status.getSelectedItem().equals(DatenInfo.get(3))) {
            datenAktualisieren.add("2002" + client.trennzeichen + "Stoerung" + client.trennzeichen + "Status" + client.trennzeichen + checkDaten(Status.getSelectedItem().toString()) + "" + client.trennzeichen + "IDStoerung" + client.trennzeichen + "" + DatenInfo.get(0).toString());
            datenAktualisieren.add("2002" + client.trennzeichen + "Stoerung" + client.trennzeichen + "gelesen" + client.trennzeichen + "0" + client.trennzeichen + "IDStoerung" + client.trennzeichen + "" + DatenInfo.get(0).toString());
        }
        if (!betreffS.getText().equals(DatenInfo.get(9))) {
            datenAktualisieren.add("2002" + client.trennzeichen + "Stoerung" + client.trennzeichen + "Betreff" + client.trennzeichen + checkDaten(betreffS.getText()) + "" + client.trennzeichen + "IDStoerung" + client.trennzeichen + "" + DatenInfo.get(0).toString());
        }
        if (!melderS.getText().equals(DatenInfo.get(4))) {
            datenAktualisieren.add("2002" + client.trennzeichen + "Stoerung" + client.trennzeichen + "Melder" + client.trennzeichen + checkDaten(melderS.getText()) + "" + client.trennzeichen + "IDStoerung" + client.trennzeichen + "" + DatenInfo.get(0).toString());
        }
        if (AdressWahl) {
            IDA = getFirmaID();
            datenAktualisieren.add("2003" + client.trennzeichen + "Stoerung" + client.trennzeichen + "IDAdressbuch" + client.trennzeichen + IDA + "" + client.trennzeichen + "IDStoerung" + client.trennzeichen + "" + DatenInfo.get(0).toString());

        } else {
            IDA = IDAdressbuch;
            datenAktualisieren.add("2003" + client.trennzeichen + "Stoerung" + client.trennzeichen + "IDAdressbuch" + client.trennzeichen + IDA + "" + client.trennzeichen + "IDStoerung" + client.trennzeichen + "" + DatenInfo.get(0).toString());

        }




        String[] datenInhaltTemp = inhalt.getText().split("\n");
        Boolean fehler = false;
        try {
            for (int i = 0; i < datenInhaltTemp.length; i++) {
                try {
                    if (!datenInhaltTemp[i].equals(datenInhalt.get(i))) {
                        fehler = true;
                    }
                } catch (Exception ex) {
                    fehler = true;
                }
            }

            if (!fehler) {
                for (int i = 0; i < datenInhalt.size(); i++) {
                    try {
                        if (!datenInhalt.get(i).equals(datenInhaltTemp[i])) {
                            fehler = true;
                        }
                    } catch (Exception ex) {
                        fehler = true;
                    }
                }
            }
            if (datenInhalt.isEmpty()) {
                if (datenInhaltTemp.length > 0) {
                    fehler = true;
                }
            }
        } catch (Exception ex) {
            fehler = true;
        }


        if (fehler) {
            try {
                System.setProperty("file.encoding", "UTF8");
                File file = new File(pfadStoerung);
                Writer out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), "UTF8"));
                out.append(inhalt.getText());
                out.flush();
                out.close();
            } catch (Exception e) {
            }
        }

        if (anhaengeAenderung) {
            int Port = client.dateiV.Port();
            client.sendDateiStoerung(AnhaengeNeu, Integer.parseInt(DatenInfo.get(6).toString()), Integer.parseInt(DatenInfo.get(0).toString()), "Stoerung/" + DatenInfo.get(6).toString());
        }
        if (!datenAktualisieren.isEmpty()) {
            client.ClientVersand(datenAktualisieren);
        }
        this.setVisible(false);
        this.dispose();
    }

    public void farbAuswahl() {
        JFrame f = new JFrame();
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JButton b = new JButton("Farbe ändern");
        f.add(b);
        b.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                Component comp = (Component) e.getSource();
                Color newColor = JColorChooser.showDialog(
                        null, "Wähle neue Farbe", comp.getBackground());
                comp.setBackground(newColor);
            }
        });
        f.pack();
        f.setVisible(true);
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox Status;
    private javax.swing.JScrollPane anhaenge;
    private javax.swing.JTextField betreffS;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JCheckBox cWV;
    public javax.swing.JComboBox cbFirma;
    public javax.swing.JComboBox cbFirmaPerson;
    private javax.swing.JComboBox cbUser;
    private javax.swing.JTextArea inhalt;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JMenuItem jMenuItem3;
    private javax.swing.JMenuItem jMenuItem4;
    private javax.swing.JMenuItem jMenuItem5;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JRadioButton jRadioButton2;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTextField melderS;
    private javax.swing.JButton ok;
    private javax.swing.JRadioButton rbv;
    public javax.swing.JTextField searchUser;
    private javax.swing.JTextField wv;
    private javax.swing.JComboBox wvDate;
    // End of variables declaration//GEN-END:variables
}
