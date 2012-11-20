/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * HauptBildschirm.java
 *
 * Created on 29.12.2011, 14:35:58
 */
package netzwerkverwaltung;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.*;
import java.net.Socket;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javax.swing.*;
import javax.swing.table.TableColumn;
import javax.swing.table.TableRowSorter;
import javax.swing.tree.*;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.junit.internal.runners.statements.RunAfters;

public class HauptBildschirm extends javax.swing.JFrame {

    private String DateiName = "Config/Default.txt";
    private Long zeit, Zeit2;
    private String[] titlesServerUser = new String[]{"Personal+Nr.", "UserName", "Datum", "Status"},
            titlesServerInfo = new String[]{"ID", "Server", "Info", "Status"},
            titlesIPScan = new String[]{"IP", "Hostname", "Dauer", "Status"},
            titlesPortScan = new String[]{"Port", "Security"},
            titlesSSHClient = new String[]{"Server", "Port", "User", "PW"},
            titlesVPNCDateien = new String[]{"Name", "Größe", "Datum"},
            titlesFTPClient = new String[]{"Name", "Typ", "Größe", "Datum"},
            titlesVPNCOrdner = new String[]{"Ordner", "Path"},
            titlesDatenProtokollierung = new String[]{"ID", "Zugriff", "Daten", "new"},
            titelLDAP = new String[]{"Display Name", "Nachname", "Vorname", "Staße", "PLZ", "Ort", "Telefon", "Mobile", "Fax", "eMail"},
            titlesIPPannel = new String[]{"Nr.", "Name", "IP", "Type", "Zweck", "Leasingende", "Serien Nr.", "Netz", "NID"},
            titlesUser = new String[]{"Personal-Nr.", "Nickname", "Name", "Vorname", "Abteilung", "eMail", "Tel", "Fax"},
            titlesFirma = new String[]{"ID", "Firma", "Strasse", "PLZ", "Ort", "Hinweis"},
            titlesServerProtokoll = new String[]{"Datum", "User", "Tätigkeit"},
            titlesPerson = new String[]{"ID", "Name", "Vorname", "Abteilung", "Tel", "Handy", "Fax", "eMail", "IDA"},
            titlesServerStoerung = new String[]{"A.-Nr.", "Betreff", "Melder", "WV Datum", "Status", "Bearbeiter"},
            titlesNetz = new String[]{"Netwerk Namen", "Netz", "Subnetzmaske", "freie IPs", "DHCP"},
            titlesDoku = new String[]{"Name"}, titelAnhaenge = new String[]{"Name"};
    private JTable tabTableDokuIntern, tabFTPClient, tabSSHClient, tabIPScan, tabVPNCDateien, tabVPNCOrdner, tabPortScan, tabDatenProtokollierung, tabDatenProtokollierungOLD, tabInfo, tabNetz, tabServerProtokoll, tabTableDokuExtern, tabServerUser, tabServerStoerung, tabAnhaenge, tabFirmen, tabPerson, tabIPPanel, tabUsers, tabLDAP;
    protected TabelFont tableServerStoerung = new TabelFont(),
            tableServerUser = new TabelFont(),
            tableSSHClient = new TabelFont(),
            tableFTPClient = new TabelFont(),
            tableVPNCDateien = new TabelFont(),
            tableVPNCOrdner = new TabelFont(),
            tableLDAP = new TabelFont(),
            tableDatenProtokollierung = new TabelFont(),
            tableIPScan = new TabelFont(),
            tablePortScan = new TabelFont(),
            tableDatenProtokollierungOLD = new TabelFont(),
            tableInfo = new TabelFont(),
            tableIPPanel = new TabelFont(),
            tableUsers = new TabelFont(),
            tableNetz = new TabelFont(),
            tableServerProtokoll = new TabelFont(),
            tableAnhaenge = new TabelFont(),
            tablePerson = new TabelFont(),
            tableFirma = new TabelFont();
   
    protected ArrayList<JButton> ssButton = new ArrayList<JButton>();
    private ClientNetzwerk clients;
    // private wait n;
    private int UserID;
    private TableRowSorter sort, sortUsers, sortServerProtokoll, sortAdressbuchPerson, sortAdressbuchFirma;
    
    
    private TrayIcon trayIcon;
    public int StoerungsAuswahl = 0;
    private MenuItem anzeigen;
    private JComboBox UserNameComboBox, UserNameComboBox2;
    private addAdressbuch addAB;
    private HauptBildschirm haupt;
    private int Ar = 235, Ab = 233, Ay = 235,
            Nr = 133, Nb = 133, Ny = 149,
            Br = 255, Bb = 255, By = 255, IDKI,
            TS = 60, TH = 60;
    public ArrayList<HashMap> eMailVerwaltung = new ArrayList<HashMap>();
    private HashMap datenInfoEmail = new HashMap();
    private ArrayList<TreeNode[]> expanded;
    private JTree baum;
    private String[] infoEmail = {"Mail", "Anhang", "Info", "Von", "Betreff", "Datum", "Status"};
    private JTable TabelleEmail;
    private TabelFont tabFontEmail = new TabelFont();
    private DefaultMutableTreeNode root = new DefaultMutableTreeNode("Root");
    private ArrayList<ArrayList<String>>  datenFirmaChange, KontoInfo, DatenNewUserDOKU;
    public ArrayList<HashMap> eMailOrdner;
    public ExecutorService threadsVerwaltung = Executors.newFixedThreadPool(5);
    public ExecutorService threadsVerwaltungPORT = Executors.newFixedThreadPool(5);
    private String IPDaten;
    int anzIP = -1, anzIPPort = -1, anz = 0, anzPort = 0, anzahlLiveIPScan = 0, anzahlGesamtIPScan = 0;
    private int[] ips, ipe;
    private VerwaltungFunktionen vFunktion;
    
    
    protected VerwaltungStoerung vStoerung;
    private VerwaltungNetzwerk vNetzwerk;

    public void setEMailKonto() {

        for (int i = 0; i < eMailOrdner.size(); i++) {
        }

    }

    //ArrayList<String> Baum
    private DefaultMutableTreeNode treeCreated(DefaultMutableTreeNode mode, String FileName) {
        DefaultMutableTreeNode child;
        try {
            ArrayList<File> unterDatei = clients.netzwerkVerwaltung.getDateien("eMail/User/" + UserID + "/" + FileName, "archiv.ort", "Hauptbildschirm/treeCreated");
            BufferedReader read = new BufferedReader(new FileReader((File) unterDatei.get(0)));
            String info = read.readLine();
            while (info != null) {
                datenInfoEmail.put(info.split("-")[1], info.split("-")[0]);
                child = new DefaultMutableTreeNode(info.split("-")[1]);
                mode.add(treeCreated(child, info.split("-")[0]));
                info = read.readLine();
            }
        } catch (Exception e) {
        }
        return mode;
    }

    private JTree setTreeBaum() {

        root = new DefaultMutableTreeNode("Mails");
        DefaultMutableTreeNode child;
        ArrayList<File> hauptDatei = clients.netzwerkVerwaltung.getDateien("eMail/User/" + UserID + "/", "archiv.ort", "Hauptbildschirm/setTreeBaum");
        try {
            BufferedReader read = new BufferedReader(new FileReader((File) hauptDatei.get(0)));
            String info = read.readLine();
            while (info != null) {

                datenInfoEmail.put(info.split("-")[1], info.split("-")[0]);
                child = new DefaultMutableTreeNode(info.split("-")[1]);
                root.add(treeCreated(child, info.split("-")[0]));
                info = read.readLine();
            }
        } catch (Exception e) {
        }

        return new JTree(root);

    }

    private void addBaum() {
        TreePath selectedPath = baum.getSelectionPath();
        if (selectedPath == null) {
            return;
        }
        DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) selectedPath.getLastPathComponent();
        DefaultMutableTreeNode newChild = new DefaultMutableTreeNode("Ordner Name");
        ((DefaultTreeModel) baum.getModel()).insertNodeInto(newChild, selectedNode, selectedNode.getChildCount());
        TreePath newPath = selectedPath.pathByAddingChild(newChild);
        baum.setSelectionPath(newPath);
        baum.startEditingAtPath(newPath);
    }

    private void saveBaum() {
        try {
            XMLEncoder o = new XMLEncoder(new BufferedOutputStream(new FileOutputStream("tree.xml")));
            o.writeObject(baum.getModel());
            o.writeObject(expanded);
            o.close();
        } catch (FileNotFoundException ex) {
            System.err.println(ex);
        }

    }

    private void reinistalisieren() {
        try {
            XMLDecoder d = new XMLDecoder(new BufferedInputStream(new FileInputStream("tree.xml")));
            baum.setModel((TreeModel) d.readObject());
            expanded = (ArrayList<TreeNode[]>) d.readObject();
            d.close();
        } catch (FileNotFoundException ex) {
        }
    }

    
    protected initServer serverInit;

    public HauptBildschirm() {

        initComponents();
       
        ssButton.add(ss1);
        ssButton.add(ss2);
        ssButton.add(ss3);
        ssButton.add(ss4);
        ssButton.add(ss5);
        ssButton.add(ss6);
        ssButton.add(ss7);
        ssButton.add(ss8);
        ssButton.add(ss9); 
        ssButton.add(ss10);
        
        for (int i=0;i<ssButton.size();i++)
            ssButton.get(i).setVisible(false);
        
        // init Table DatenProtokollierung --> Start
        tabDatenProtokollierung = tableDatenProtokollierung.getTable(titlesDatenProtokollierung, -1, null, -1, null);
        tabDatenProtokollierung.setFillsViewportHeight(true);
        scDatenProtokollierungNew.add(tabDatenProtokollierung);
        scDatenProtokollierungNew.setViewportView(tabDatenProtokollierung);

        tabDatenProtokollierungOLD = tableDatenProtokollierungOLD.getTable(titlesDatenProtokollierung, -1, null, -1, null);
        tabDatenProtokollierungOLD.setFillsViewportHeight(true);
        scDatenProtokollierungOld.add(tabDatenProtokollierungOLD);
        scDatenProtokollierungOld.setViewportView(tabDatenProtokollierungOLD);

    }

    public void initialisierung(ClientNetzwerk cn, ArrayList<ArrayList<String>> KI, int IDKI, initServer sInit, HashMap ConfigInfo) throws InterruptedException {
        try {
            haupt = this;
            try {
                Ar = Integer.parseInt(ConfigInfo.get("CHintergrund").toString().split("#")[0]);
                Ab = Integer.parseInt(ConfigInfo.get("CHintergrund").toString().split("#")[1]);
                Ay = Integer.parseInt(ConfigInfo.get("CHintergrund").toString().split("#")[2]);

                Nr = Integer.parseInt(ConfigInfo.get("CHintergrundN").toString().split("#")[0]);
                Nb = Integer.parseInt(ConfigInfo.get("CHintergrundN").toString().split("#")[1]);
                Ny = Integer.parseInt(ConfigInfo.get("CHintergrundN").toString().split("#")[2]);

                Br = Integer.parseInt(ConfigInfo.get("CHintergrundB").toString().split("#")[0]);
                Bb = Integer.parseInt(ConfigInfo.get("CHintergrundB").toString().split("#")[1]);
                By = Integer.parseInt(ConfigInfo.get("CHintergrundB").toString().split("#")[2]);




                Zeit2 = Long.parseLong(ConfigInfo.get("THaupt").toString());
                zeit = Long.parseLong(ConfigInfo.get("TStoerung").toString());
            } catch (Exception e) {
                System.err.println(e);
            }

            serverInit = sInit;
            
            clients = cn;
            vFunktion = new VerwaltungFunktionen(this, clients);
            vFunktion.setSchnellstart();
            KontoInfo = (ArrayList<ArrayList<String>>) KI.clone();
            this.IDKI = IDKI;
            UserID = Integer.parseInt(KontoInfo.get(IDKI).get(0).toString());
            this.setTitle("Hauptmenue - Angemeldeter User: " + KontoInfo.get(IDKI).get(1));


            serverInit.infoText.append("OK! \n Componenten initialisieren ...");

            serverInit.infoProgress.setValue(30);
            serverInit.infoProgressSee.setValue(30);
            serverInit.infoProgressSee.setString("initTables initialisieren ... 10%");
            serverInit.infoText.append(" OK! \n initTables initialisieren ...");
            initTables();

            vStoerung = new VerwaltungStoerung(this, clients, tableServerStoerung, tableAnhaenge, tabServerStoerung, UserID, KontoInfo, IDKI);
            vNetzwerk = new VerwaltungNetzwerk(this, clients, tableServerProtokoll, tableNetz, tableIPPanel,tabIPPanel);
            
            
            vNetzwerk.initNetzWerk();
            
            serverInit.infoProgress.setValue(70);
            serverInit.infoProgressSee.setValue(70);
            serverInit.infoProgressSee.setString("initTimerInfo initialisieren ... 50%");
            serverInit.infoText.append(" OK! \n initTimerInfo initialisieren ...");
            initTimerInfo();
            serverInit.infoProgress.setValue(70);
            serverInit.infoProgressSee.setValue(70);
            serverInit.infoProgressSee.setString("initTrayler initialisieren ... 60%");
            serverInit.infoText.append(" OK! \n initTrayler initialisieren ...");
            initTrayler();
            serverInit.infoProgress.setValue(80);
            serverInit.infoProgressSee.setValue(80);
            serverInit.infoProgressSee.setString("initUser initialisieren ... 70%");
            serverInit.infoText.append(" OK! \n initUser initialisieren ...");
            initUser();
            
            serverInit.infoProgress.setValue(100);
            serverInit.infoProgressSee.setValue(100);
            serverInit.infoProgressSee.setString("initBerechtigung initialisieren ... 90%");
            serverInit.infoText.append(" OK! \n initBerechtigung initialisieren ...");
            initBerechtigung();
            serverInit.infoProgress.setValue(110);
            serverInit.infoProgressSee.setValue(110);
            serverInit.infoProgressSee.setString("initThread initialisieren ... 95%");
            serverInit.infoText.append(" OK! \n initThread initialisieren ...");
            initThread();
            serverInit.infoProgress.setValue(130);
            serverInit.infoProgressSee.setValue(130);
            serverInit.infoProgressSee.setString("initloadConfig initialisieren ... 98%");
            serverInit.infoText.append(" OK! \n initloadConfig initialisieren ...");
            initloadConfig();
            resetAdressbuch();
            readDatei();
            serverInit.infoProgress.setValue(140);
            serverInit.infoProgressSee.setValue(140);
            serverInit.infoProgressSee.setString("initAdressbuch initialisieren ... 100%");
            serverInit.infoProgress.setForeground(Color.green);
            serverInit.infoText.append(" OK!");
            pannel.setSelectedIndex(1);


            
            this.setLocationRelativeTo(null);
            this.setVisible(true);
            serverInit.setFocusable(true);
            serverInit.setVisible(false);
        } catch (Exception ex) {
            serverInit.infoText.append(" Fehler!");
            serverInit.infoProgress.setForeground(Color.RED);
            serverInit.fehleranalyse.append(ex.getMessage());
            this.setVisible(false);
        }
    }

    

    private ArrayList<String> listArbeitsplatz() {
        ArrayList<String> Ordner = new ArrayList<String>();

        File[] f = File.listRoots();

        for (int i = 0; i < f.length; i++) {
            Ordner.add("" + f[i]);

        }
        return Ordner;
    }

    private void setTable(JScrollPane sc, JTable tab, TabelFont table, String[] Title) {
    }

    private void initTables() {

        tabServerUser = tableServerUser.getTable(titlesServerUser, -1, null, -1, null);
        newUser.add(tabServerUser);
        newUser.setViewportView(tabServerUser);
        tabServerUser.setFillsViewportHeight(true);


        UserNameComboBox = new JComboBox();
        for (int i = 0; i < KontoInfo.size(); i++) {
            UserNameComboBox.addItem(KontoInfo.get(i).get(1).toString());
        }

        ActionListener actionListener = new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                vStoerung.changeCombobox("bearbeiter", IDAdressbuchSelected.getText(), allStoerungen.isSelected());
            }
        };
        UserNameComboBox.addActionListener(actionListener);
        UserNameComboBox2 = new JComboBox();
        UserNameComboBox2.addItem("neu");
        UserNameComboBox2.addItem("in Arbeit");
        UserNameComboBox2.addItem("warten");
        UserNameComboBox2.addItem("fertig");
        UserNameComboBox2.addItem("loeschen");

        ActionListener actionListener2 = new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                vStoerung.changeCombobox("status", IDAdressbuchSelected.getText(), allStoerungen.isSelected());
            }
        };
        UserNameComboBox2.addActionListener(actionListener2);

        tabServerStoerung = tableServerStoerung.getTable(titlesServerStoerung, -1, null, -1, null);
        sort = new TableRowSorter(tableServerStoerung.getInfoTable);
        tabServerStoerung.setRowSorter(sort);
        tabServerStoerung.setFillsViewportHeight(true);
        // init Table StoerungsMeldung --> END ####

        // init Table Tabelle Email --> Start ####
        TabelleEmail = tabFontEmail.getTable(infoEmail, -1, null, -1, null);
        scEmail.add(TabelleEmail);
        scEmail.setViewportView(TabelleEmail);
        tabFontEmail.getInfoTable.wert = 13;
        TabelleEmail.setFillsViewportHeight(true);
        // init Table StoerungsMeldung --> END ####

        // init Table Adressbuch Firma --> Start ####
        tabFirmen = tableFirma.getTable(titlesFirma, -1, null, -1, null);
        tabFirmen.setFillsViewportHeight(true);
        scFirma.add(tabFirmen);
        scFirma.setViewportView(tabFirmen);
        sortAdressbuchFirma = new TableRowSorter(tableFirma.getInfoTable);
        tabFirmen.setRowSorter(sortAdressbuchFirma);
        // init Table Adressbuch Firma --> Ende ####

        //init table Tool IPScan {"IP", "Port", "Status"},

        tabIPScan = tableIPScan.getTable(titlesIPScan, -1, null, -1, null);
        SCIPScan.add(tabIPScan);
        SCIPScan.setViewportView(tabIPScan);
        tabIPScan.setFillsViewportHeight(true);

        tabFTPClient = tableFTPClient.getTable(titlesFTPClient, -1, null, -1, null);
        scFTPClientDaten.add(tabFTPClient);
        scFTPClientDaten.setViewportView(tabFTPClient);
        tabFTPClient.setFillsViewportHeight(true);

        tabSSHClient = tableSSHClient.getTable(titlesSSHClient, -1, null, -1, null);
        scSSHClient.add(tabSSHClient);
        scSSHClient.setViewportView(tabSSHClient);
        tabSSHClient.setFillsViewportHeight(true);

        tabSSHClient.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    new Thread(new Runnable() {

                        @Override
                        public void run() {

                            Properties props = new Properties();
                            if (tabSSHClient.getSelectedRow() != -1) {
                                SSHClient ssh2 = new SSHClient(props, tabSSHClient.getValueAt(tabSSHClient.getSelectedRow(), 0).toString(),
                                        tabSSHClient.getValueAt(tabSSHClient.getSelectedRow(), 1).toString(),
                                        tabSSHClient.getValueAt(tabSSHClient.getSelectedRow(), 2).toString(),
                                        tabSSHClient.getValueAt(tabSSHClient.getSelectedRow(), 3).toString());
                                ssh2.run();
                            } else {
                                SSHClient ssh2 = new SSHClient(props, "", "", "", "");
                                ssh2.run();
                            }
                        }
                    }).start();
                }
            }
        });


        tabFTPClient.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent e) {

                if (e.getClickCount() == 2) {
                    if (tabFTPClient.getSelectedRow() != -1) {
                        listFTPFiles(tabFTPClient.getValueAt(tabFTPClient.getSelectedRow(), 0).toString());
                    }
                }

            }
        });

        tabVPNCDateien = tableVPNCDateien.getTable(titlesVPNCDateien, -1, null, -1, null);
        scVPNCDateien.add(tabVPNCDateien);
        scVPNCDateien.setViewportView(tabVPNCDateien);
        tabVPNCDateien.setFillsViewportHeight(true);

        tabVPNCOrdner = tableVPNCOrdner.getTable(titlesVPNCOrdner, -1, null, -1, null);
        scftpcordner.add(tabVPNCOrdner);
        scftpcordner.setViewportView(tabVPNCOrdner);
        tabVPNCOrdner.setFillsViewportHeight(true);
        ArrayList<String> Arbeitsplatz = listArbeitsplatz();
        ArrayList<String> temp;
        for (int i = 0; i < Arbeitsplatz.size(); i++) {
            temp = new ArrayList<String>();
            temp.add(Arbeitsplatz.get(i));
            temp.add(Arbeitsplatz.get(i));
            tableVPNCOrdner.getInfoTable.addRow(temp, "-1");
        }
        FTPLastIndex.add("Arbeitsplatz");
        tabVPNCOrdner.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent e) {

                if (e.getClickCount() == 1) {
                    try {
                        tableVPNCDateien.getInfoTable.removeAllItems();
                        ArrayList<String> datenTEMP;
                        File[] dateien = new File(tabVPNCOrdner.getValueAt(tabVPNCOrdner.getSelectedRow(), 1).toString()).listFiles();
                        for (int i = 0; i < dateien.length; i++) {
                            if (dateien[i].isFile()) {
                                datenTEMP = new ArrayList<String>();
                                datenTEMP.add(dateien[i].getName());
                                datenTEMP.add(dateien[i].length() + " Byte");
                                datenTEMP.add(new SimpleDateFormat("dd.MM.yyyy HH:MM").format(new Date(dateien[i].lastModified())));
                                tableVPNCDateien.getInfoTable.addRow(datenTEMP, "-1");
                            }
                        }
                    } catch (Exception ex) {
                    }
                }
                if (e.getClickCount() == 2) {
                    try {
                        File[] dateien;
                        tableVPNCDateien.getInfoTable.removeAllItems();
                        ArrayList<String> datenTEMP;

                        if (tabVPNCOrdner.getValueAt(tabVPNCOrdner.getSelectedRow(), 0).toString().equals(".. ")) {

                            if (tabVPNCOrdner.getValueAt(tabVPNCOrdner.getSelectedRow(), 1).toString().equals("Arbeitsplatz")) {
                                dateien = File.listRoots();
                            } else {
                                dateien = new File(tabVPNCOrdner.getValueAt(tabVPNCOrdner.getSelectedRow(), 1).toString()).listFiles();
                                FTPLastIndex.remove(FTPLastIndex.size() - 1);
                                FTPBack--;
                            }

                            datenTEMP = new ArrayList<String>();
                            datenTEMP.add(".. ");
                            datenTEMP.add(FTPLastIndex.get(FTPBack - 1));

                        } else {

                            dateien = new File(tabVPNCOrdner.getValueAt(tabVPNCOrdner.getSelectedRow(), 1).toString()).listFiles();
                            FTPLastIndex.add(tabVPNCOrdner.getValueAt(tabVPNCOrdner.getSelectedRow(), 1).toString());

                            datenTEMP = new ArrayList<String>();
                            datenTEMP.add(".. ");
                            datenTEMP.add(FTPLastIndex.get(FTPBack));
                            FTPBack++;

                        }


                        tableVPNCOrdner.getInfoTable.removeAllItems();
                        tableVPNCOrdner.getInfoTable.addRow(datenTEMP, "-1");

                        for (int i = 0; i < dateien.length; i++) {
                            if (dateien[i].isDirectory()) {
                                datenTEMP = new ArrayList<String>();
                                datenTEMP.add(dateien[i].getName());
                                datenTEMP.add(dateien[i].getAbsolutePath());
                                tableVPNCOrdner.getInfoTable.addRow(datenTEMP, "-1");
                            }
                        }



                    } catch (Exception ex) {
                        System.out.println(ex);
                    }
                }

            }
        });

        tabPortScan = tablePortScan.getTable(titlesPortScan, -1, null, -1, null);
        scPort.add(tabPortScan);
        scPort.setViewportView(tabPortScan);
        tabPortScan.setFillsViewportHeight(true);

        // init Table Netz --> Start ####
        tabNetz = tableNetz.getTable(titlesNetz, -1, null, -1, null);
        scNetz.add(tabNetz);
        scNetz.setViewportView(tabNetz);
        // init Table NETZ  --> END ####

        // init Table  ServerProtokoll--> Start ####
        tabServerProtokoll = tableServerProtokoll.getTable(titlesServerProtokoll, -1, null, -1, null);
        scServerProtokoll.add(tabServerProtokoll);
        scServerProtokoll.setViewportView(tabServerProtokoll);
        sortServerProtokoll = new TableRowSorter(tableServerProtokoll.getInfoTable);
        tabServerProtokoll.setRowSorter(sortServerProtokoll);
        tabServerProtokoll.setFillsViewportHeight(true);
        // init Table ServerProtokoll --> END ####


        tabInfo = tableInfo.getTable(titlesServerInfo, -1, null, -1, null);
        scInfo.add(tabInfo);
        scInfo.setViewportView(tabInfo);
        tabInfo.setFillsViewportHeight(true);

        tabUsers = tableUsers.getTable(titlesUser, -1, null, -1, null);
        scUsers.add(tabUsers);
        scUsers.setViewportView(tabUsers);
        sortUsers = new TableRowSorter(tableUsers.getInfoTable);
        tabUsers.setRowSorter(sortUsers);
        tabUsers.setFillsViewportHeight(true);
        tabUsers.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    if (!setUserInfo("Doku", tabUsers)) {
                        JOptionPane.showMessageDialog(new JFrame(), "Fehler bitter versuchen Sie es später erneut\n oder kontaktieren Sie den Admin");
                    }
                }
            }
        });

        tabIPPanel = tableIPPanel.getTable(titlesIPPannel, -1, null, -1, null);
        scIPPannel.add(tabIPPanel);
        scIPPannel.setViewportView(tabIPPanel);
        tabIPPanel.setFillsViewportHeight(true);
        tabIPPanel.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent e) {
               vNetzwerk.ClickIPPanel(e);
            }
        });

        tabLDAP = tableLDAP.getTable(titelLDAP, -1, null, -1, null);
        tabLDAP.setFillsViewportHeight(true);
        scLDAP.add(tabLDAP);
        scLDAP.setViewportView(tabLDAP);
        tabLDAP.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    if (tabLDAP.getSelectedRow() != -1) {
                        dN.setText(tabLDAP.getValueAt(tabLDAP.getSelectedRow(), 0).toString());
                        nachname.setText(tabLDAP.getValueAt(tabLDAP.getSelectedRow(), 1).toString());
                        vorname.setText(tabLDAP.getValueAt(tabLDAP.getSelectedRow(), 2).toString());
                        street.setText(tabLDAP.getValueAt(tabLDAP.getSelectedRow(), 3).toString());
                        plzLDAP.setText(tabLDAP.getValueAt(tabLDAP.getSelectedRow(), 4).toString());
                        city.setText(tabLDAP.getValueAt(tabLDAP.getSelectedRow(), 5).toString());
                        telefon.setText(tabLDAP.getValueAt(tabLDAP.getSelectedRow(), 6).toString());
                        mobile.setText(tabLDAP.getValueAt(tabLDAP.getSelectedRow(), 7).toString());
                        faxNummer.setText(tabLDAP.getValueAt(tabLDAP.getSelectedRow(), 8).toString());
                        eMail.setText(tabLDAP.getValueAt(tabLDAP.getSelectedRow(), 9).toString());
                    }
                }
            }
        });


        tabFirmen.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent e) {
                try {
                    if (tabFirmen.getSelectedRow() != -1) {
                        while (tabPerson.getRowCount() > 0) {
                            tablePerson.getInfoTable.removeRowAt(0);
                        }
                        datenFirmaChange = (ArrayList<ArrayList<String>>) clients.netzwerkVerwaltung.getAdressbuchPerson("-2", tabFirmen.getValueAt(tabFirmen.getSelectedRow(), 0).toString(), "HauptBildschirm/initTables",false).clone();
                        ArrayList<String> temp; //{"ID", "Name", "Vorname", "Abteilung", "Tel", "Handy", "Fax", "eMail", "IDA"},
                        for (int i = 0; i < datenFirmaChange.size(); i++) {
                            temp = new ArrayList<String>();
                            temp.add(datenFirmaChange.get(i).get(0));
                            temp.add(datenFirmaChange.get(i).get(1));
                            temp.add(datenFirmaChange.get(i).get(2));
                            temp.add(datenFirmaChange.get(i).get(3));
                            temp.add(datenFirmaChange.get(i).get(4));
                            temp.add(datenFirmaChange.get(i).get(9));
                            temp.add(datenFirmaChange.get(i).get(5));
                            temp.add(datenFirmaChange.get(i).get(8));
                            temp.add(datenFirmaChange.get(i).get(7));
                            tablePerson.getInfoTable.addRow(temp, "1");
                        }

                        if (e.getClickCount() == 2) {
                            ArrayList<String> datenFirma = new ArrayList<String>();
                            datenFirma.add(tabFirmen.getValueAt(tabFirmen.getSelectedRow(), 5).toString());
                            datenFirma.add(tabFirmen.getValueAt(tabFirmen.getSelectedRow(), 0).toString());
                            datenFirma.add(tabFirmen.getValueAt(tabFirmen.getSelectedRow(), 1).toString());
                            datenFirma.add(tabFirmen.getValueAt(tabFirmen.getSelectedRow(), 2).toString());
                            datenFirma.add(tabFirmen.getValueAt(tabFirmen.getSelectedRow(), 3).toString());
                            datenFirma.add(tabFirmen.getValueAt(tabFirmen.getSelectedRow(), 4).toString());
                            addAB = new addAdressbuch(clients, null, haupt);
                            addAB.startAdressFirma(datenFirma);
                        }
                    }
                } catch (Exception ex) {
                }
            }
        });


        tabPerson = tablePerson.getTable(titlesPerson, -1, null, -1, null);
        scPerson.add(tabPerson);
        scPerson.setViewportView(tabPerson);
        tablePerson.getInfoTable.wert = 13;
        sortAdressbuchPerson = new TableRowSorter(tablePerson.getInfoTable);
        tabPerson.setRowSorter(sortAdressbuchPerson);
        tabPerson.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    addAB = new addAdressbuch(clients, null, haupt);
                    if (tabFirmen.getSelectedRow() != -1) {
                        int ar = 0;
                        for (int i = 0; i < datenFirmaChange.size(); i++) {
                            if (datenFirmaChange.get(i).get(0).equals(tabPerson.getValueAt(tabPerson.getSelectedRow(), 0))) {
                                ar = i;
                                break;
                            }
                        }
                        addAB.startAdressPerson(datenFirmaChange.get(ar), tabFirmen.getValueAt(tabFirmen.getSelectedRow(), 0).toString());
                    }

                }
            }
        });


        tabAnhaenge = tableAnhaenge.getTable(titelAnhaenge, -1, null, -1, null);
        tabAnhaenge.setFillsViewportHeight(true);
        tabAnhaenge.setTableHeader(null);
        scAnhaenge.add(tabAnhaenge);
        scAnhaenge.setViewportView(tabAnhaenge);
        tabAnhaenge.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    if (tabAnhaenge.getSelectedRow() != -1) {
                        if (!vStoerung.AnhaengeStoerungen.isEmpty()){
                        File file = vStoerung.AnhaengeStoerungen.get(tabAnhaenge.getSelectedRow());
                        if (file.exists()) {
                            try {
                                new ProcessBuilder("cmd", "/c", file.getAbsolutePath()).start();
                            } catch (IOException ex) {
                            }
                        }

                    }}
                }
            }
        });




        newStoerung.add(tabServerStoerung);
        newStoerung.setViewportView(tabServerStoerung);




        tabServerStoerung.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent e) {

                vStoerung.AuswahlStoerung(e,
                        tableAnhaenge);


            }
        });

        tabServerUser.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getButton() == 3) {
                    if (!setUserInfo("Nein", tabServerUser)) {
                        JOptionPane.showMessageDialog(new JFrame(), "Fehler bitter versuchen Sie es später erneut\n oder kontaktieren Sie den Admin");
                    }
                }
            }
        });


        tabIPPanel.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        TableColumn col = tabIPPanel.getColumnModel().getColumn(0);
        col.setPreferredWidth(30);
        TableColumn col1 = tabIPPanel.getColumnModel().getColumn(1);
        col1.setPreferredWidth(300);
        TableColumn col2 = tabIPPanel.getColumnModel().getColumn(2);
        col2.setPreferredWidth(80);
        TableColumn col3 = tabIPPanel.getColumnModel().getColumn(3);
        col3.setPreferredWidth(80);
        TableColumn col4 = tabIPPanel.getColumnModel().getColumn(4);
        col4.setPreferredWidth(300);
        TableColumn col5 = tabIPPanel.getColumnModel().getColumn(5);
        col5.setPreferredWidth(80);
        TableColumn col6 = tabIPPanel.getColumnModel().getColumn(6);
        col6.setPreferredWidth(100);


        tabServerProtokoll.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        TableColumn colP = tabServerProtokoll.getColumnModel().getColumn(0);
        colP.setPreferredWidth(100);
        TableColumn colP1 = tabServerProtokoll.getColumnModel().getColumn(1);
        colP1.setPreferredWidth(30);
        TableColumn colP2 = tabServerProtokoll.getColumnModel().getColumn(2);
        colP2.setPreferredWidth(800);

        tabServerStoerung.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        TableColumn colS = tabServerStoerung.getColumnModel().getColumn(0);
        colS.setPreferredWidth(10);
        TableColumn colS1 = tabServerStoerung.getColumnModel().getColumn(1);
        colS1.setPreferredWidth(300);

        tableServerStoerung.getInfoTable.wert = 5;
        tableLDAP.getInfoTable.wert = 18;
        tableFirma.getInfoTable.wert = 13;
        tableIPPanel.getInfoTable.wert = 13;
        tableServerProtokoll.getInfoTable.wert = 13;
        tableUsers.getInfoTable.wert = 13;

    }
    private ArrayList<String> FTPLastIndex = new ArrayList<String>();
    private int FTPBack = 0;

    private void clearLDAP() {
        dN.setText("");
        nachname.setText("");
        vorname.setText("");
        street.setText("");
        plzLDAP.setText("");
        city.setText("");
        telefon.setText("");
        mobile.setText("");
        faxNummer.setText("");
        eMail.setText("");
    }

    
    private void initTimerInfo() {
        zeit = Long.valueOf(String.valueOf((TS) * 600000));
        Zeit2 = Long.valueOf(String.valueOf((TH) * 60000));
    }

    private void initTrayler() {
        try {
            if (!SystemTray.isSupported()) {
                return;
            }
            final PopupMenu popup = new PopupMenu();
            trayIcon = new TrayIcon(createImage("icon.gif", "tray icon"));
            final SystemTray tray = SystemTray.getSystemTray();

            // Create a popup menu components

            MenuItem aktualisieren = new MenuItem("Aktualisieren");
            anzeigen = new MenuItem("Anzeigen / Minimieren");
            anzeigen.setName("Minimieren");
            MenuItem exitItem = new MenuItem("Exit");

            //Add components to popup menu

            popup.add(aktualisieren);
            popup.add(anzeigen);
            popup.addSeparator();
            popup.add(exitItem);

            trayIcon.setPopupMenu(popup);

            try {
                tray.add(trayIcon);
            } catch (AWTException e) {
                return;
            }

            trayIcon.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    setHauptVisible(true);
                }
            });



            anzeigen.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    if (anzeigen.getName().equals("Minimieren")) {
                        setHauptVisible(false);
                        anzeigen.setName("Anzeigen");
                    } else {
                        setHauptVisible(true);
                        anzeigen.setName("Minimieren");
                    }
                }
            });
            aktualisieren.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    vStoerung.CheckInfoStoerungen(trayIcon, "show", IDAdressbuchSelected.getText(), allStoerungen.isSelected());
                }
            });

            exitItem.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    tray.remove(trayIcon);
                    try {
                        BufferedReader read = new BufferedReader(new FileReader("Config/config.cfg"));
                        ArrayList<String> daten2 = new ArrayList<String>();
                        String datenRead = read.readLine();
                        daten2.add("999" + clients.trennzeichen + "START");
                        while (datenRead != null) {
                            daten2.add("999" + clients.trennzeichen + datenRead);
                            datenRead = read.readLine();
                        }
                        read.close();
                        daten2.add("999" + clients.trennzeichen + "END" + clients.trennzeichen + "config.cfg");
                        clients.ClientVersand(daten2);

                    } catch (Exception ex) {
                    }
                    System.exit(0);
                }
            });
        } catch (Exception e) {
        }
    }

    private Image createImage(String path, String description) {
        URL imageURL = HauptBildschirm.class.getResource(path);

        if (imageURL == null) {
            return null;
        } else {
            return (new ImageIcon(imageURL, description)).getImage();
        }
    }

    private void setHauptVisible(boolean g) {
        this.setVisible(g);
    }

    private void initBerechtigung() {
        if (UserID == 1) {
            userB.setVisible(true);
            userS.setVisible(true);
        } else {
            HauptPannel.remove(4);
            HauptPannel.remove(1);
            userS.setVisible(false);
            userB.setVisible(false);
            pannel.remove(4);
            pannel.remove(4);
            jButton7.setVisible(false);
        }
        jPanel5.setVisible(false);
    }

   

    private void initThread() {
        new Thread(new Runnable() {

            @Override
            public void run() {
                Timer t = new Timer();
                t.schedule(new TimerTask() {

                    @Override
                    public void run() {
                        //NewUserSuche();
                        vStoerung.setStoerung(StoerungsAuswahl,
                                IDAdressbuchSelected.getText(),
                                allStoerungen.isSelected());
                    }
                }, 0, Zeit2);
            }
        }).start();


        new Thread(new Runnable() {

            @Override
            public void run() {

                Timer t = new Timer();
                t.schedule(new TimerTask() {

                    @Override
                    public void run() {
                        int anzLE = 0, anzS = 0;
                        ArrayList<HashMap> info = new ArrayList<HashMap>();
                        if (info.size() > 0) {
                            tableInfo.getInfoTable.removeAllItems();
                            ArrayList<String> infoT = new ArrayList<String>();
                            for (int i = 0; i < info.size(); i++) {
                                infoT = new ArrayList<String>();
                                infoT.add(i + "");
                                infoT.add(info.get(i).get("Betreff").toString());
                                infoT.add(info.get(i).get("Info").toString());
                                infoT.add("neu");
                                tableInfo.getInfoTable.addRow(infoT, "-1");
                            }

                        } else {
                            tableInfo.getInfoTable.removeAllItems();
                        }
                    }
                }, 0, 60000);

            }
        }).start();
    }
    
    // SERVER Start ------------------------------------
    private void initUser() throws InterruptedException {
        DatenNewUserDOKU = (ArrayList<ArrayList<String>>) clients.netzwerkVerwaltung.getNewUser("Doku", "HauptBildschirm/initDaten", false).clone();

        tableUsers.getInfoTable.removeAllItems();
        ArrayList<String> info = new ArrayList<String>();
        for (int i = 0; i < DatenNewUserDOKU.size(); i++) {
            info.add(DatenNewUserDOKU.get(i).get(3).toString());
            info.add(DatenNewUserDOKU.get(i).get(13).toString().toString().split("#")[3]);
            info.add(DatenNewUserDOKU.get(i).get(1).toString());
            info.add(DatenNewUserDOKU.get(i).get(2).toString());
            info.add(DatenNewUserDOKU.get(i).get(7).toString());
            if (DatenNewUserDOKU.get(i).get(11).toString().toString().split("#")[0].equals("1")) {
                info.add(DatenNewUserDOKU.get(i).get(11).toString().toString().split("#")[2]);
            } else {
                info.add("--");
            }

            info.add(DatenNewUserDOKU.get(i).get(5).toString());
            info.add(DatenNewUserDOKU.get(i).get(6).toString());
            tableUsers.getInfoTable.addRow(info, "-1");
            info = new ArrayList<String>();
        }
    }

   

    private void NewUserSuche() {
        ArrayList<String> UserDaten;
        int pos = 0;
        ArrayList<ArrayList<String>> DatenNewUser = (ArrayList<ArrayList<String>>) clients.netzwerkVerwaltung.getNewUser("-1", "HauptBildschirm/initDaten",false).clone();
        if (DatenNewUser == null) {
        } else {
            boolean weiter = false;


            for (int a = 0; a < tableServerUser.getInfoTable.getRowCount(); a++) {
                weiter = true;
                pos = a;
                for (int i = 0; i < DatenNewUser.size(); i++) {
                    if (tableServerUser.getInfoTable.getValueAt(a, 0).equals(DatenNewUser.get(i).get(3))) {
                        weiter = false;
                        break;
                    } else {
                        pos = i;
                        weiter = true;
                    }
                }
                if (weiter) {
                    tableServerUser.getInfoTable.removeRowAt(pos);
                }

            }

            for (int a = 0; a < DatenNewUser.size(); a++) {
                weiter = true;
                for (int i = 0; i < tableServerUser.getInfoTable.getRowCount(); i++) {
                    if (DatenNewUser.get(a).get(3).equals(tableServerUser.getInfoTable.getValueAt(i, 0))) {
                        weiter = false;
                        break;
                    } else {
                        pos = i;
                        weiter = true;
                    }
                }
                if (weiter) {
                    UserDaten = new ArrayList<String>();
                    UserDaten.add(DatenNewUser.get(a).get(3).toString());
                    UserDaten.add(DatenNewUser.get(a).get(1).toString());
                    UserDaten.add(DatenNewUser.get(a).get(10).toString());
                    UserDaten.add(DatenNewUser.get(a).get(9).toString());
                    tableServerUser.getInfoTable.addRow(UserDaten, "1");
                }
            }
        }
    }

    // SERVER Stopppp -----------------------------
    

    private Boolean setUserInfo(String Doku, JTable tab) {
        if (vNetzwerk.checkServerSystem()) {
            ArrayList<ArrayList<String>> user = (ArrayList<ArrayList<String>>) clients.netzwerkVerwaltung.getNewUser(Doku, "HauptBildschirm/initDaten",false).clone();
            if (user != null) {
                for (int i = 0; i < user.size(); i++) {
                    if (tab.getSelectedRow() > -1) {
                        if (tab.getValueAt(tab.getSelectedRow(), 0).equals(user.get(i).get(3))) {
                            newUser u = new newUser(clients, vNetzwerk.datenNetz.get(0).get(6).toString(), "info", user.get(i), this, (ArrayList<String>) KontoInfo.get(IDKI).clone(), serverInit);
                        }
                    }
                }
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    private void schreib() {
        try {
            ArrayList<String> oldD = new ArrayList<String>();
            try {
                FileReader fileD = new FileReader(new File(DateiName));
                BufferedReader readDaten = new BufferedReader(fileD);
                String readTemp = readDaten.readLine();

                while (readTemp != null) {
                    oldD.add(readTemp);
                    readTemp = readDaten.readLine();
                }
                readDaten.close();
            } catch (Exception e) {
            }
            BufferedWriter write = new BufferedWriter(new FileWriter(new File(DateiName)));
            JFileChooser datei = new JFileChooser();
            String s = (String) JOptionPane.showInputDialog(
                    new JFrame(),
                    "Link Name:",
                    "Name",
                    JOptionPane.PLAIN_MESSAGE,
                    null, null,
                    "");

            File file;
            int getInfo = datei.showOpenDialog(datei);
            if (getInfo == JFileChooser.APPROVE_OPTION) {

                file = datei.getSelectedFile();
                if (oldD.size() <= 0 || oldD == null) {
                    write.write("x+02+" + file.getAbsolutePath() + clients.trennzeichen + s + "\n");
                } else {
                    for (int i = 0; i < oldD.size(); i++) {
                        String[] d = oldD.get(i).split("\\+");
                        if (!d[3].equals(s)) {
                            write.write(oldD.get(i) + "\n");
                        }
                    }
                    write.write("x+02+" + file.getAbsolutePath() + clients.trennzeichen + s + "\n");
                    Object[] temp = {s};
                    //table2.addRow(temp);
                }

                write.close();
            }
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(new JFrame(), ex);
        }
    }

    private void loeschen(String Name) {
        try {
            ArrayList<String> oldD = new ArrayList<String>();
            try {
                FileReader fileD = new FileReader(new File(DateiName));
                BufferedReader read = new BufferedReader(fileD);
                String readTemp = read.readLine();

                while (readTemp != null) {
                    oldD.add(readTemp);
                    readTemp = read.readLine();
                }
                read.close();
            } catch (Exception e) {
            }
            BufferedWriter write = new BufferedWriter(new FileWriter(new File(DateiName)));
            for (int i = 0; i < oldD.size(); i++) {
                String[] d = oldD.get(i).split("\\+");
                if (!d[3].equals(Name)) {
                    write.write(oldD.get(i) + "\n");
                }
            }
            write.close();

        } catch (IOException ex) {
            JOptionPane.showMessageDialog(new JFrame(), ex);
        }
    }

    private void dateiHinzufuegen(String s) {


        if (s != null) {
            if (s.equals("Datei")) {
                String name = (String) JOptionPane.showInputDialog(
                        new JFrame(),
                        "Link Name:",
                        "Name",
                        JOptionPane.PLAIN_MESSAGE,
                        null, null,
                        "");
                JFileChooser datei = new JFileChooser();
                File file;
                int getInfo = datei.showOpenDialog(datei);
                if (getInfo == JFileChooser.APPROVE_OPTION) {
                    file = datei.getSelectedFile();
                    File de = new File("//10.203.24.14/Ablage/" + file.getName().toString());
                    file.renameTo(de);
                    clients.send("1600" + clients.trennzeichen + name + clients.trennzeichen + file.getName().toString());
                    Object[] temp = {name};
                    // table.addRow(temp);
                }
            } else if (s.equals("Verknüpfung")) {
                schreib();
            } else if (s.equals("Löschen")) {

                ArrayList<ArrayList<String>> da = clients.netzwerkVerwaltung.getDoku("HauptBildschirm/dateiHinzufuegen",false);
                boolean weiter = true;
                try {
                    String DokuVergleich = tabTableDokuIntern.getValueAt(tabTableDokuIntern.getSelectedRow(), 0).toString();
                    if (DokuVergleich != null) {
                        for (int i = 0; i < da.size(); i++) {
                            if (da.get(i).get(1).equals(DokuVergleich)) {
                                clients.send("2001" + clients.trennzeichen + "Doku" + clients.trennzeichen + DokuVergleich + clients.trennzeichen + "Name");
                                weiter = false;
                                // table.removeRow(tab.getSelectedRow());
                            }
                        }
                    }
                } catch (Exception e) {
                    clients.FehlerBerichtSchreiben("Log/NetzwerkDoku: " + e, "Log/error.log",this);
                }
            } else if (s.equals("Löschen Privates")) {
                String datenClean = tabTableDokuExtern.getValueAt(tabTableDokuExtern.getSelectedRow(), 0).toString();
                loeschen(datenClean);
                // table2.removeRow(tab2.getSelectedRow());
            }
        }
    }

    private void lesen(String Name) {
        try {
            FileReader fileD = new FileReader(new File(DateiName));
            BufferedReader read = new BufferedReader(fileD);
            String readTemp = read.readLine();
            boolean weiter = false;
            String[] temp = {"", "c:/"};
            while (readTemp != null) {
                temp = readTemp.split("\\+");
                if (temp[3].equals(Name)) {
                    if (temp[1].equals("02")) {
                        weiter = true;
                        break;
                    }
                }
                readTemp = read.readLine();
            }
            read.close();
            if (weiter) {
                new ProcessBuilder("cmd", "/c", temp[2]).start();
            } else {
            }

            weiter = false;
        } catch (IOException ex) {
        }
    }

    // HAUPT INFORMATION --------------------------
    
    

    

    

    private String checkCSDoppelt(String Daten, ArrayList<HashMap> S, String Gruppe) {

        /*
         * ArrayList<HashMap> checkClientServer =
         * clients.netzwerkVerwaltung.datenCServer(S, Daten, Gruppe); try { for
         * (int i = 0; i < checkClientServer.size(); i++) {
         *
         * if (checkClientServer.get(i).get("CServerName").equals(Daten)) {
         *
         * Daten = null; clients.FehlerBerichtSchreiben("impCSServer: " + Daten,
         * "import.log"); } } } catch (Exception e) { Daten = null; }
         */
        return Daten;
    }

    
    private FTPClient clientFTP = new FTPClient();

    private Boolean connectFTP(String usr, String pwd, String host, int port) {
        try {
            FTPProtokoll.append("(" + new SimpleDateFormat("dd.MM.yyyy HH:MM").format(new Date()) + ") FTP Verbindung wird herrgestellt (" + host + ":" + port + ")...\n");
            clientFTP.connect(host, port);
            clientFTP.login(usr, pwd);
            FTPProtokoll.append("(" + new SimpleDateFormat("dd.MM.yyyy HH:MM").format(new Date()) + ") FTP Verbindung herrgestellt (" + host + ":" + port + ")\n");
            return true;
        } catch (IOException e) {
            FTPProtokoll.append("(" + new SimpleDateFormat("dd.MM.yyyy HH:MM").format(new Date()) + ") FTP Verbindung konnte nicht herrgestellt werden!! (" + host + ":" + port + ")");
            return false;
        }
    }

    public boolean upload(String localSourceFile, String remoteResultFile, String dateiName,
            String host, int port, String usr, String pwd, boolean showMessages) throws IOException {

        boolean resultOk = true;
        FileInputStream fis = null;

        try {





            clientFTP.setFileType(clientFTP.BINARY_FILE_TYPE, clientFTP.BINARY_FILE_TYPE);
            clientFTP.setFileTransferMode(clientFTP.BINARY_FILE_TYPE);

            clientFTP.makeDirectory(remoteResultFile);
            clientFTP.cwd(remoteResultFile);
            fis = new FileInputStream(localSourceFile);

            clientFTP.storeFile(dateiName + ".tiff", fis);
            clientFTP.logout();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (fis != null) {
                    fis.close();
                }
                clientFTP.disconnect();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


        return resultOk;
    }

    

    

   

    

    

    

    /*
     * public ArrayList<HashMap> dNetz() { ArrayList<HashMap> datenNetzwerk =
     * new ArrayList<HashMap>(); HashMap temp = new HashMap(); for (int i = 0; i
     * < datenNetz.size(); i++) { if
     * (datenNetz.get(i).get(1).equals(netz.getSelectedItem())) {
     * temp.put("NetzwerkID", datenNetz.get(i).get(0)); temp.put("NetzwerkName",
     * datenNetz.get(i).get(1)); temp.put("NetzwerkNetz",
     * datenNetz.get(i).get(2)); temp.put("NetzwerkMaske",
     * datenNetz.get(i).get(3)); temp.put("NetzwerkDHCP",
     * datenNetz.get(i).get(4)); temp.put("NetzwerkIPANZ",
     * datenNetz.get(i).get(5)); datenNetzwerk.add(temp); break; } } return
     * datenNetzwerk; }
     */
    private String checkSoftDoppelt(String Daten) {

        ArrayList<ArrayList<String>> DatenSoftware = (ArrayList<ArrayList<String>>) clients.netzwerkVerwaltung.getSoftware("-1", "0", "HauptBildschirm/checkSoftDoppelt",false).clone();
        for (int i = 0; i < DatenSoftware.size(); i++) {
            if (DatenSoftware.get(i).get(1).equals(Daten)) {
                String s = (String) JOptionPane.showInputDialog(
                        new JFrame(),
                        "User schon vorhanden!! \n \n Username:",
                        "Fehler",
                        JOptionPane.PLAIN_MESSAGE,
                        null, null,
                        "");
                if (s != null) {
                    Daten = checkSoftDoppelt(s);
                } else {
                    Daten = null;
                }
            }
        }
        return Daten;
        // searchAdressbuch(clients, this, null);
    }
    // HAUPT INFORMATION ENDE --------------------------
    private ArrayList<ArrayList<String>> datenFirma;

    protected void resetAdressbuch() {
        try {
            while (tableFirma.getInfoTable.getRowCount() > 0) {
                tableFirma.getInfoTable.removeRowAt(0);
            }
            datenFirma = (ArrayList<ArrayList<String>>) clients.netzwerkVerwaltung.getAdressbuchFirma("-1", "HauptBildschirm/resetAdressbuch",false).clone();
            ArrayList<String> datenFirmaTemp;
            for (int i = 0; i < datenFirma.size(); i++) {
                datenFirmaTemp = new ArrayList<String>();
                datenFirmaTemp.add(datenFirma.get(i).get(0).toString());
                datenFirmaTemp.add(datenFirma.get(i).get(1).toString());
                datenFirmaTemp.add(datenFirma.get(i).get(2).toString());
                datenFirmaTemp.add(datenFirma.get(i).get(3).toString());
                datenFirmaTemp.add(datenFirma.get(i).get(4).toString());
                datenFirmaTemp.add(datenFirma.get(i).get(5).toString());
                tableFirma.getInfoTable.addRow(datenFirmaTemp, "1");
            }
            while (tablePerson.getInfoTable.getRowCount() > 0) {
                tablePerson.getInfoTable.removeRowAt(0);
            }

        } catch (Exception e) {
        }

        //resetAdressbuchPerson();
    }

    
    

    

    private void initloadConfig() {
        BufferedReader read = null;
        try {
            read = new BufferedReader(new FileReader(new File("Config/configHBildschirm.cfg")));
            HauptPannel.setSelectedIndex(Integer.parseInt(read.readLine().toString()));
            pannel.setSelectedIndex(Integer.parseInt(read.readLine().toString()));
            NetzStandort.setSelectedIndex(Integer.parseInt(read.readLine().toString()));
            netz.setSelectedIndex(Integer.parseInt(read.readLine().toString()));
            GruppeIPPanel.setSelectedIndex(Integer.parseInt(read.readLine().toString()));
            GruppeProtokoll.setSelectedIndex(Integer.parseInt(read.readLine().toString()));
            sCProtokoll.setSelectedIndex(Integer.parseInt(read.readLine().toString()));
            setSizeColums(read.readLine(), tabInfo);
            setSizeColums(read.readLine(), tabIPPanel);
            setSizeColums(read.readLine(), tabServerProtokoll);
            setSizeColums(read.readLine(), tabUsers);
            setSizeColums(read.readLine(), tabNetz);
            setSizeColums(read.readLine(), tabServerStoerung);
            setSizeColums(read.readLine(), tabServerUser);
            setSizeColums(read.readLine(), tabFirmen);
            setSizeColums(read.readLine(), tabPerson);
            setSizeColums(read.readLine(), tabLDAP);
            read.close();
        } catch (Exception ex) {
            clients.FehlerBerichtSchreiben("initloadConfig: " + ex, "Log/error.log",this);
        }
    }

    private void saveConfig() {
        BufferedWriter write = null;
        try {
            write = new BufferedWriter(new FileWriter("Config/configHBildschirm.cfg"));
            write.write(HauptPannel.getSelectedIndex() + "\n");
            write.write(pannel.getSelectedIndex() + "\n");
            write.write(NetzStandort.getSelectedIndex() + "\n");
            write.write(netz.getSelectedIndex() + "\n");
            write.write(GruppeIPPanel.getSelectedIndex() + "\n");
            write.write(GruppeProtokoll.getSelectedIndex() + "\n");
            write.write(sCProtokoll.getSelectedIndex() + "\n");
            write.write(getSizeColums(tabInfo) + "\n");
            write.write(getSizeColums(tabIPPanel) + "\n");
            write.write(getSizeColums(tabServerProtokoll) + "\n");
            write.write(getSizeColums(tabUsers) + "\n");
            write.write(getSizeColums(tabNetz) + "\n");
            write.write(getSizeColums(tabServerStoerung) + "\n");
            write.write(getSizeColums(tabServerUser) + "\n");
            write.write(getSizeColums(tabFirmen) + "\n");
            write.write(getSizeColums(tabPerson) + "\n");
            write.write(getSizeColums(tabLDAP) + "\n");
            write.close();
        } catch (Exception ex) {
            System.out.println(ex);
        }
    }

    private String getSizeColums(JTable tabel) {
        TableColumn col2 = tabel.getColumnModel().getColumn(0);
        String size = String.valueOf(col2.getPreferredWidth());
        for (int i = 1; i < tabel.getColumnCount(); i++) {
            col2 = tabel.getColumnModel().getColumn(i);
            size = size + ";" + String.valueOf(col2.getPreferredWidth());
        }
        return size;
    }

    private void setSizeColums(String size, JTable tabel) {
        String[] gr = size.split(";");
        for (int i = 0; i < tabel.getColumnCount(); i++) {
            TableColumn col2 = tabel.getColumnModel().getColumn(i);
            col2.setPreferredWidth(Integer.parseInt(gr[i]));
        }
    }

    

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jMenuBar2 = new javax.swing.JMenuBar();
        jMenu4 = new javax.swing.JMenu();
        jMenu5 = new javax.swing.JMenu();
        buttonGroup1 = new javax.swing.ButtonGroup();
        jPanel1 = new javax.swing.JPanel();
        HauptPannel = new javax.swing.JTabbedPane();
        jPanel2 = new javax.swing.JPanel();
        pannel = new javax.swing.JTabbedPane();
        jPanel19 = new javax.swing.JPanel();
        Info = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        infoP = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel35 = new javax.swing.JLabel();
        jLabel36 = new javax.swing.JLabel();
        fip = new javax.swing.JTextField();
        fip1 = new javax.swing.JTextField();
        subnetz = new javax.swing.JTextField();
        maske = new javax.swing.JTextField();
        dhcp = new javax.swing.JTextField();
        useranz = new javax.swing.JTextField();
        pcanz = new javax.swing.JTextField();
        serveranz = new javax.swing.JTextField();
        lapanz = new javax.swing.JTextField();
        jScrollPane8 = new javax.swing.JScrollPane();
        fip2 = new javax.swing.JTextPane();
        fip3 = new javax.swing.JTextField();
        fip4 = new javax.swing.JTextField();
        fip5 = new javax.swing.JTextField();
        scInfo = new javax.swing.JScrollPane();
        jPanel3 = new javax.swing.JPanel();
        scIPPannel = new javax.swing.JScrollPane();
        GruppeIPPanel = new javax.swing.JComboBox();
        jButton11 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        searchIPPannel1 = new javax.swing.JTextField();
        suchergebniss = new javax.swing.JLabel();
        jButton3 = new javax.swing.JButton();
        jLabel37 = new javax.swing.JLabel();
        freieIP = new javax.swing.JComboBox();
        jButton18 = new javax.swing.JButton();
        jPanel4 = new javax.swing.JPanel();
        jButton7 = new javax.swing.JButton();
        GruppeProtokoll = new javax.swing.JComboBox();
        sCProtokoll = new javax.swing.JComboBox();
        protokollEintrag = new javax.swing.JTextField();
        jButton14 = new javax.swing.JButton();
        scServerProtokoll = new javax.swing.JScrollPane();
        jPanel7 = new javax.swing.JPanel();
        scNetz = new javax.swing.JScrollPane();
        jTextField1 = new javax.swing.JTextField();
        netz = new javax.swing.JComboBox();
        jLabel6 = new javax.swing.JLabel();
        jLabel34 = new javax.swing.JLabel();
        NetzStandort = new javax.swing.JComboBox();
        jButton26 = new javax.swing.JButton();
        jButton17 = new javax.swing.JButton();
        jButton21 = new javax.swing.JButton();
        remote1 = new javax.swing.JTextField();
        remote = new javax.swing.JTextField();
        vnc1 = new javax.swing.JTextField();
        pingTest = new javax.swing.JTextField();
        vnc = new javax.swing.JTextField();
        jLabel58 = new javax.swing.JLabel();
        jLabel55 = new javax.swing.JLabel();
        Netz = new javax.swing.JTextField();
        DHCPNetz = new javax.swing.JTextField();
        Maske = new javax.swing.JTextField();
        jLabel56 = new javax.swing.JLabel();
        freeIP = new javax.swing.JTextField();
        jPanel32 = new javax.swing.JPanel();
        jTabbedPane5 = new javax.swing.JTabbedPane();
        jPanel14 = new javax.swing.JPanel();
        newUser = new javax.swing.JScrollPane();
        newUserB = new javax.swing.JButton();
        jPanel5 = new javax.swing.JPanel();
        scUsers = new javax.swing.JScrollPane();
        searchUsers = new javax.swing.JTextField();
        jButton25 = new javax.swing.JButton();
        userB = new javax.swing.JButton();
        userS = new javax.swing.JButton();
        jPanel8 = new javax.swing.JPanel();
        PannelAll = new javax.swing.JPanel();
        jPanel9 = new javax.swing.JPanel();
        jLabel15 = new javax.swing.JLabel();
        firmenNamen = new javax.swing.JTextField();
        jLabel16 = new javax.swing.JLabel();
        strasse = new javax.swing.JTextField();
        plz = new javax.swing.JTextField();
        jLabel17 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        ort = new javax.swing.JTextField();
        jLabel19 = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        tel = new javax.swing.JTextField();
        fax = new javax.swing.JTextField();
        auftragsNr = new javax.swing.JTextField();
        jLabel21 = new javax.swing.JLabel();
        jLabel22 = new javax.swing.JLabel();
        Name = new javax.swing.JTextField();
        IDAdressbuchSelected = new javax.swing.JTextField();
        allStoerungen = new javax.swing.JCheckBox();
        handy = new javax.swing.JTextField();
        jLabel23 = new javax.swing.JLabel();
        jScrollPane6 = new javax.swing.JScrollPane();
        inhalt = new javax.swing.JTextArea();
        jButton5 = new javax.swing.JButton();
        jButton12 = new javax.swing.JButton();
        jButton6 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        scAnhaenge = new javax.swing.JScrollPane();
        newStoerung = new javax.swing.JScrollPane();
        jPanel10 = new javax.swing.JPanel();
        jTabbedPane3 = new javax.swing.JTabbedPane();
        jPanel18 = new javax.swing.JPanel();
        scLDAP = new javax.swing.JScrollPane();
        jPanel16 = new javax.swing.JPanel();
        plzLDAP = new javax.swing.JTextField();
        jLabel14 = new javax.swing.JLabel();
        jLabel33 = new javax.swing.JLabel();
        vorname = new javax.swing.JTextField();
        jLabel31 = new javax.swing.JLabel();
        telefon = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        faxNummer = new javax.swing.JTextField();
        jButton16 = new javax.swing.JButton();
        eMail = new javax.swing.JTextField();
        mobile = new javax.swing.JTextField();
        jLabel29 = new javax.swing.JLabel();
        street = new javax.swing.JTextField();
        jLabel24 = new javax.swing.JLabel();
        dN = new javax.swing.JTextField();
        jLabel25 = new javax.swing.JLabel();
        city = new javax.swing.JTextField();
        nachname = new javax.swing.JTextField();
        jLabel26 = new javax.swing.JLabel();
        jLabel30 = new javax.swing.JLabel();
        jLabel32 = new javax.swing.JLabel();
        jPanel17 = new javax.swing.JPanel();
        jButton1 = new javax.swing.JButton();
        jButton8 = new javax.swing.JButton();
        scFirma = new javax.swing.JScrollPane();
        searchFirma = new javax.swing.JTextField();
        jButton20 = new javax.swing.JButton();
        jLabel27 = new javax.swing.JLabel();
        jTextField10 = new javax.swing.JTextField();
        jLabel28 = new javax.swing.JLabel();
        jButton9 = new javax.swing.JButton();
        jButton10 = new javax.swing.JButton();
        scPerson = new javax.swing.JScrollPane();
        jButton22 = new javax.swing.JButton();
        jPanel11 = new javax.swing.JPanel();
        jSplitPane1 = new javax.swing.JSplitPane();
        jSplitPane2 = new javax.swing.JSplitPane();
        scEmail = new javax.swing.JScrollPane();
        jScrollPane5 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();
        jPanel6 = new javax.swing.JPanel();
        treeEmail = new javax.swing.JScrollPane();
        jPanel13 = new javax.swing.JPanel();
        jButton31 = new javax.swing.JButton();
        jButton13 = new javax.swing.JButton();
        jButton28 = new javax.swing.JButton();
        jButton29 = new javax.swing.JButton();
        jButton15 = new javax.swing.JButton();
        jTextField3 = new javax.swing.JTextField();
        jButton27 = new javax.swing.JButton();
        jButton30 = new javax.swing.JButton();
        jPanel26 = new javax.swing.JPanel();
        jTabbedPane2 = new javax.swing.JTabbedPane();
        jSplitPane4 = new javax.swing.JSplitPane();
        jPanel27 = new javax.swing.JPanel();
        jLabel42 = new javax.swing.JLabel();
        ipstart = new javax.swing.JTextField();
        ipend = new javax.swing.JTextField();
        jLabel43 = new javax.swing.JLabel();
        SCIPScan = new javax.swing.JScrollPane();
        jButton19 = new javax.swing.JButton();
        SearchIP = new javax.swing.JProgressBar();
        IPScanLive = new javax.swing.JButton();
        jLabel57 = new javax.swing.JLabel();
        IPScanAnz = new javax.swing.JTextField();
        IPScanAliv = new javax.swing.JTextField();
        jLabel59 = new javax.swing.JLabel();
        jLabel60 = new javax.swing.JLabel();
        IPScanDeath = new javax.swing.JTextField();
        IPScanBDeath = new javax.swing.JButton();
        IPScanAll = new javax.swing.JButton();
        jPanel28 = new javax.swing.JPanel();
        jLabel44 = new javax.swing.JLabel();
        ipport = new javax.swing.JTextField();
        pstart = new javax.swing.JTextField();
        pend = new javax.swing.JTextField();
        jLabel45 = new javax.swing.JLabel();
        jLabel46 = new javax.swing.JLabel();
        jButton23 = new javax.swing.JButton();
        scPort = new javax.swing.JScrollPane();
        portVisible = new javax.swing.JCheckBox();
        PortSearchBar = new javax.swing.JProgressBar();
        portOpen = new javax.swing.JCheckBox();
        jPanel29 = new javax.swing.JPanel();
        FTPServer = new javax.swing.JTextField();
        jLabel47 = new javax.swing.JLabel();
        FTPPort = new javax.swing.JTextField();
        jLabel48 = new javax.swing.JLabel();
        jLabel49 = new javax.swing.JLabel();
        FTPUser = new javax.swing.JTextField();
        jLabel50 = new javax.swing.JLabel();
        FTPPW = new javax.swing.JTextField();
        jButton24 = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        FTPProtokoll = new javax.swing.JTextArea();
        scFTPClientDaten = new javax.swing.JScrollPane();
        jSplitPane5 = new javax.swing.JSplitPane();
        scVPNCDateien = new javax.swing.JScrollPane();
        scftpcordner = new javax.swing.JScrollPane();
        jPanel30 = new javax.swing.JPanel();
        jPanel31 = new javax.swing.JPanel();
        jButton35 = new javax.swing.JButton();
        scSSHClient = new javax.swing.JScrollPane();
        jButton36 = new javax.swing.JButton();
        jLabel51 = new javax.swing.JLabel();
        SSHServer = new javax.swing.JTextField();
        SSHPort = new javax.swing.JTextField();
        jLabel52 = new javax.swing.JLabel();
        SSHUser = new javax.swing.JTextField();
        jLabel53 = new javax.swing.JLabel();
        SSHPW = new javax.swing.JTextField();
        jLabel54 = new javax.swing.JLabel();
        jButton37 = new javax.swing.JButton();
        jPanel12 = new javax.swing.JPanel();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel21 = new javax.swing.JPanel();
        jTabbedPane4 = new javax.swing.JTabbedPane();
        jPanel22 = new javax.swing.JPanel();
        auswahlK = new javax.swing.JComboBox();
        jButton32 = new javax.swing.JButton();
        jButton33 = new javax.swing.JButton();
        desti = new javax.swing.JTextField();
        source = new javax.swing.JTextField();
        conv = new javax.swing.JButton();
        ziel = new javax.swing.JTextField();
        Name1 = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        hist = new javax.swing.JTextArea();
        jPanel23 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        text = new javax.swing.JTextArea();
        jScrollPane7 = new javax.swing.JScrollPane();
        text2 = new javax.swing.JTextArea();
        jLabel41 = new javax.swing.JLabel();
        auswahl = new javax.swing.JComboBox();
        jButton34 = new javax.swing.JButton();
        convAdmin = new javax.swing.JButton();
        jPanel15 = new javax.swing.JPanel();
        jSplitPane3 = new javax.swing.JSplitPane();
        jPanel24 = new javax.swing.JPanel();
        jLabel38 = new javax.swing.JLabel();
        scDatenProtokollierungNew = new javax.swing.JScrollPane();
        jPanel25 = new javax.swing.JPanel();
        jLabel39 = new javax.swing.JLabel();
        scDatenProtokollierungOld = new javax.swing.JScrollPane();
        jScrollPane4 = new javax.swing.JScrollPane();
        fehlerProtokoll = new javax.swing.JTextArea();
        jLabel40 = new javax.swing.JLabel();
        jPanel33 = new javax.swing.JPanel();
        jButton38 = new javax.swing.JButton();
        jButton39 = new javax.swing.JButton();
        ss1 = new javax.swing.JButton();
        ss2 = new javax.swing.JButton();
        ss3 = new javax.swing.JButton();
        ss6 = new javax.swing.JButton();
        ss5 = new javax.swing.JButton();
        ss4 = new javax.swing.JButton();
        ss9 = new javax.swing.JButton();
        ss8 = new javax.swing.JButton();
        ss7 = new javax.swing.JButton();
        ss10 = new javax.swing.JButton();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        jMenuItem7 = new javax.swing.JMenuItem();
        jMenuItem6 = new javax.swing.JMenuItem();
        jMenu3 = new javax.swing.JMenu();
        jMenuItem3 = new javax.swing.JMenuItem();
        jMenuItem4 = new javax.swing.JMenuItem();
        jMenuItem2 = new javax.swing.JMenuItem();

        jMenu4.setText("File");
        jMenuBar2.add(jMenu4);

        jMenu5.setText("Edit");
        jMenuBar2.add(jMenu5);

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(Ar, Ab, Ay));

        HauptPannel.setBackground(new java.awt.Color(235, 233, 235));

        jPanel2.setBackground(new java.awt.Color(Nr, Nb, Ny));

        jPanel19.setBackground(new java.awt.Color(Ar, Ab, Ay));

        Info.setBackground(new java.awt.Color(Ar, Ab, Ay));

        jLabel4.setBackground(new java.awt.Color(0, 0, 0));
        jLabel4.setText("Netz:");

        jLabel3.setBackground(new java.awt.Color(0, 0, 0));
        jLabel3.setText("DHCP:");

        jLabel2.setBackground(new java.awt.Color(0, 0, 0));
        jLabel2.setText("vergeben IPs:");

        jLabel12.setBackground(new java.awt.Color(0, 0, 0));
        jLabel12.setText(", fest:");

        jLabel11.setBackground(new java.awt.Color(0, 0, 0));
        jLabel11.setText("(DHCP:");

        jLabel10.setBackground(new java.awt.Color(0, 0, 0));
        jLabel10.setText("freie IPs:");

        jLabel9.setBackground(new java.awt.Color(0, 0, 0));
        jLabel9.setText("Laptop Anzahl:");

        jLabel8.setBackground(new java.awt.Color(0, 0, 0));
        jLabel8.setText("Server Anzahl:");

        jLabel13.setBackground(java.awt.Color.white);
        jLabel13.setText(")");

        jLabel7.setBackground(new java.awt.Color(0, 0, 0));
        jLabel7.setText("PC Anzahl:");

        jLabel5.setBackground(new java.awt.Color(0, 0, 0));
        jLabel5.setText("Maske:");

        jLabel35.setBackground(new java.awt.Color(0, 0, 0));
        jLabel35.setText(", USB:");

        jLabel36.setBackground(new java.awt.Color(0, 0, 0));
        jLabel36.setText(", KEINE:");

        fip.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                fipActionPerformed(evt);
            }
        });

        jScrollPane8.setViewportView(fip2);

        javax.swing.GroupLayout InfoLayout = new javax.swing.GroupLayout(Info);
        Info.setLayout(InfoLayout);
        InfoLayout.setHorizontalGroup(
            InfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(InfoLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(InfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(InfoLayout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addGap(8, 8, 8))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, InfoLayout.createSequentialGroup()
                        .addGroup(InfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel10)
                            .addComponent(jLabel4)
                            .addComponent(jLabel5)
                            .addComponent(jLabel3))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)))
                .addGroup(InfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(maske, javax.swing.GroupLayout.DEFAULT_SIZE, 127, Short.MAX_VALUE)
                    .addComponent(subnetz)
                    .addComponent(dhcp)
                    .addComponent(fip1)
                    .addComponent(fip))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(InfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(InfoLayout.createSequentialGroup()
                        .addComponent(jLabel11)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane8, javax.swing.GroupLayout.DEFAULT_SIZE, 45, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel12)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(fip3, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel35)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(fip4, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel36)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(InfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(infoP)
                            .addGroup(InfoLayout.createSequentialGroup()
                                .addComponent(fip5, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel13)))
                        .addGap(24, 24, 24))
                    .addGroup(InfoLayout.createSequentialGroup()
                        .addGroup(InfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(useranz, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(InfoLayout.createSequentialGroup()
                                .addGroup(InfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel8)
                                    .addComponent(jLabel9)
                                    .addComponent(jLabel7))
                                .addGap(10, 10, 10)
                                .addGroup(InfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addComponent(lapanz, javax.swing.GroupLayout.DEFAULT_SIZE, 140, Short.MAX_VALUE)
                                    .addComponent(serveranz)
                                    .addComponent(pcanz))))
                        .addContainerGap())))
        );
        InfoLayout.setVerticalGroup(
            InfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(InfoLayout.createSequentialGroup()
                .addComponent(infoP)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(InfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(InfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel12)
                        .addComponent(fip3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel35)
                        .addComponent(fip4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel36)
                        .addComponent(fip5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel13))
                    .addGroup(InfoLayout.createSequentialGroup()
                        .addGroup(InfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jScrollPane8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(InfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(fip, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel2)
                                .addComponent(jLabel11)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(InfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(fip1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel10)
                            .addComponent(jLabel9)
                            .addComponent(lapanz, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(InfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(InfoLayout.createSequentialGroup()
                        .addGroup(InfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(subnetz, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel4)
                            .addComponent(jLabel8))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(InfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(maske, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel5)
                            .addComponent(jLabel7))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(InfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(dhcp, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel3)))
                    .addGroup(InfoLayout.createSequentialGroup()
                        .addComponent(serveranz, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(pcanz, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(useranz, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(33, 33, 33))
        );

        javax.swing.GroupLayout jPanel19Layout = new javax.swing.GroupLayout(jPanel19);
        jPanel19.setLayout(jPanel19Layout);
        jPanel19Layout.setHorizontalGroup(
            jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel19Layout.createSequentialGroup()
                .addGroup(jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel19Layout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addComponent(scInfo, javax.swing.GroupLayout.DEFAULT_SIZE, 1133, Short.MAX_VALUE))
                    .addGroup(jPanel19Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(Info, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel19Layout.setVerticalGroup(
            jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel19Layout.createSequentialGroup()
                .addComponent(Info, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(scInfo, javax.swing.GroupLayout.DEFAULT_SIZE, 464, Short.MAX_VALUE)
                .addContainerGap())
        );

        pannel.addTab("Netz Info", jPanel19);

        jPanel3.setBackground(new java.awt.Color(Ar, Ab, Ay));

        GruppeIPPanel.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "All", "Server", "PC", "Notebook", "Terminal", "Switch", "Drucker", "Monitore", "OLD" }));
        GruppeIPPanel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                GruppeIPPanelActionPerformed(evt);
            }
        });

        jButton11.setText("liste Drucken");
        jButton11.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton11ActionPerformed(evt);
            }
        });

        jButton2.setText("liste Exportieren");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        searchIPPannel1.setForeground(new java.awt.Color(204, 204, 204));
        searchIPPannel1.setText(" - All Suche - ");
        searchIPPannel1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                searchIPPannel1ActionPerformed(evt);
            }
        });
        searchIPPannel1.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                searchIPPannel1FocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                searchIPPannel1FocusLost(evt);
            }
        });

        suchergebniss.setBackground(new java.awt.Color(Ar, Ab, Ay));

        jButton3.setText("IP Sortieren");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jLabel37.setText("freie IP:");

        jButton18.setText("Ping Test");
        jButton18.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton18ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(GruppeIPPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 143, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton11)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton2)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel37)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(freieIP, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton18)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 19, Short.MAX_VALUE)
                        .addComponent(suchergebniss, javax.swing.GroupLayout.PREFERRED_SIZE, 282, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(searchIPPannel1, javax.swing.GroupLayout.PREFERRED_SIZE, 166, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(scIPPannel))
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(GruppeIPPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jButton11)
                        .addComponent(jButton2)
                        .addComponent(jLabel37)
                        .addComponent(freieIP, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jButton18))
                    .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jButton3)
                        .addComponent(searchIPPannel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(suchergebniss, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(scIPPannel, javax.swing.GroupLayout.DEFAULT_SIZE, 595, Short.MAX_VALUE)
                .addContainerGap())
        );

        pannel.addTab("IP Pannel", jPanel3);

        jPanel4.setBackground(new java.awt.Color(Ar, Ab, Ay));

        jButton7.setText("Server Protokoll");
        jButton7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton7ActionPerformed(evt);
            }
        });

        GruppeProtokoll.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "All", "Server", "PC", "Notebook", "Terminal", "Switch", "Drucker", "Monitore", "OLD" }));
        GruppeProtokoll.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                GruppeProtokollActionPerformed(evt);
            }
        });

        sCProtokoll.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                sCProtokollActionPerformed(evt);
            }
        });

        jButton14.setText("Eintrag hinzufügen");
        jButton14.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton14ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(scServerProtokoll)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(jButton7)
                        .addGap(133, 133, 133)
                        .addComponent(GruppeProtokoll, javax.swing.GroupLayout.PREFERRED_SIZE, 91, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(sCProtokoll, javax.swing.GroupLayout.PREFERRED_SIZE, 396, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(protokollEintrag, javax.swing.GroupLayout.DEFAULT_SIZE, 247, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton14)))
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(protokollEintrag, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(sCProtokoll, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(GruppeProtokoll, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton14)
                    .addComponent(jButton7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(scServerProtokoll, javax.swing.GroupLayout.PREFERRED_SIZE, 545, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        pannel.addTab("Server Protokoll", jPanel4);

        jPanel7.setBackground(new java.awt.Color(Ar, Ab, Ay));

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(scNetz, javax.swing.GroupLayout.DEFAULT_SIZE, 1133, Short.MAX_VALUE)
                    .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 199, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(scNetz, javax.swing.GroupLayout.DEFAULT_SIZE, 598, Short.MAX_VALUE)
                .addContainerGap())
        );

        pannel.addTab("Netzübersicht", jPanel7);

        netz.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                netzActionPerformed(evt);
            }
        });

        jLabel6.setText("Netz:");

        jLabel34.setText("Standort:");

        NetzStandort.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                NetzStandortActionPerformed(evt);
            }
        });

        jButton26.setText("Standort");
        jButton26.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton26ActionPerformed(evt);
            }
        });

        jButton17.setText("Netzwerk");
        jButton17.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton17ActionPerformed(evt);
            }
        });

        jButton21.setText("Hardware");
        jButton21.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton21ActionPerformed(evt);
            }
        });

        remote1.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        remote1.setForeground(new java.awt.Color(204, 204, 204));
        remote1.setText(" - SSH2 Zugriff - ");
        remote1.setDisabledTextColor(new java.awt.Color(204, 204, 204));
        remote1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                remote1enterRemote(evt);
            }
        });
        remote1.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                remote1getInRemote(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                remote1FocusLost(evt);
            }
        });

        remote.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        remote.setForeground(new java.awt.Color(204, 204, 204));
        remote.setText(" - Remotezugriff - ");
        remote.setDisabledTextColor(new java.awt.Color(204, 204, 204));
        remote.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                enterRemote(evt);
            }
        });
        remote.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                getInRemote(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                remoteFocusLost(evt);
            }
        });

        vnc1.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        vnc1.setForeground(new java.awt.Color(204, 204, 204));
        vnc1.setText(" - Browser Anzeige - ");
        vnc1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        vnc1.setDisabledTextColor(new java.awt.Color(204, 204, 204));
        vnc1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                vnc1ActionPerformed(evt);
            }
        });
        vnc1.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                vnc1FocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                vnc1FocusLost(evt);
            }
        });

        pingTest.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        pingTest.setForeground(new java.awt.Color(204, 204, 204));
        pingTest.setText(" - Ping - ");
        pingTest.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        pingTest.setDisabledTextColor(new java.awt.Color(204, 204, 204));
        pingTest.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                pingTestActionPerformed(evt);
            }
        });
        pingTest.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                pingTestFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                pingTestFocusLost(evt);
            }
        });

        vnc.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        vnc.setForeground(new java.awt.Color(204, 204, 204));
        vnc.setText(" - VNC Zugriff - ");
        vnc.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        vnc.setDisabledTextColor(new java.awt.Color(204, 204, 204));
        vnc.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                vncActionPerformed(evt);
            }
        });
        vnc.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                vncFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                vncFocusLost(evt);
            }
        });

        jLabel58.setText("DHCP:");

        jLabel55.setText("Netz:");

        Netz.setEditable(false);
        Netz.setForeground(new java.awt.Color(204, 204, 204));

        DHCPNetz.setEditable(false);
        DHCPNetz.setForeground(new java.awt.Color(204, 204, 204));

        Maske.setEditable(false);
        Maske.setForeground(new java.awt.Color(204, 204, 204));

        jLabel56.setText("frei:");

        freeIP.setEditable(false);
        freeIP.setForeground(new java.awt.Color(204, 204, 204));

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(pannel, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(remote1, javax.swing.GroupLayout.PREFERRED_SIZE, 129, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(remote, javax.swing.GroupLayout.PREFERRED_SIZE, 129, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(vnc1, javax.swing.GroupLayout.PREFERRED_SIZE, 129, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(pingTest, javax.swing.GroupLayout.PREFERRED_SIZE, 117, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(vnc, javax.swing.GroupLayout.PREFERRED_SIZE, 126, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                                .addComponent(jLabel34)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(NetzStandort, javax.swing.GroupLayout.PREFERRED_SIZE, 136, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jLabel6)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(netz, javax.swing.GroupLayout.PREFERRED_SIZE, 167, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jButton26)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton17)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton21, javax.swing.GroupLayout.PREFERRED_SIZE, 87, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(jLabel55)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(Netz, javax.swing.GroupLayout.PREFERRED_SIZE, 94, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(Maske, javax.swing.GroupLayout.PREFERRED_SIZE, 117, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(jLabel58)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(DHCPNetz, javax.swing.GroupLayout.PREFERRED_SIZE, 143, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel56)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(freeIP, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE, false)
                                .addComponent(jButton26, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jButton21, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jButton17))
                            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel34)
                                .addComponent(NetzStandort, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel6)
                                .addComponent(netz, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(remote1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(remote, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(vnc1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(pingTest, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(vnc, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(Netz, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(Maske, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel55))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE, false)
                            .addComponent(freeIP, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(DHCPNetz, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel58)
                            .addComponent(jLabel56))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pannel))
        );

        HauptPannel.addTab("Netzwerk Verwaltung", jPanel2);

        jPanel14.setBackground(new java.awt.Color(Ar, Ab, Ay));

        newUser.setBackground(new java.awt.Color(255, 255, 255));
        newUser.setMaximumSize(new java.awt.Dimension(32767, 87));

        newUserB.setText("User");
        newUserB.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                newUserBActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel14Layout = new javax.swing.GroupLayout(jPanel14);
        jPanel14.setLayout(jPanel14Layout);
        jPanel14Layout.setHorizontalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel14Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(newUser, javax.swing.GroupLayout.DEFAULT_SIZE, 1143, Short.MAX_VALUE)
                    .addGroup(jPanel14Layout.createSequentialGroup()
                        .addComponent(newUserB)
                        .addGap(0, 1088, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel14Layout.setVerticalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel14Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(newUserB, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(newUser, javax.swing.GroupLayout.DEFAULT_SIZE, 655, Short.MAX_VALUE)
                .addGap(19, 19, 19))
        );

        jTabbedPane5.addTab("User", jPanel14);

        jPanel5.setBackground(new java.awt.Color(Ar, Ab, Ay));

        searchUsers.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                searchUsersActionPerformed(evt);
            }
        });

        jButton25.setText("aktuallisieren");
        jButton25.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton25ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(scUsers, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 1143, Short.MAX_VALUE)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(jButton25)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 875, Short.MAX_VALUE)
                        .addComponent(searchUsers, javax.swing.GroupLayout.PREFERRED_SIZE, 173, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(searchUsers, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton25))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(scUsers, javax.swing.GroupLayout.DEFAULT_SIZE, 663, Short.MAX_VALUE)
                .addContainerGap())
        );

        jTabbedPane5.addTab("User Übersicht", jPanel5);

        userB.setText("User");
        userB.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                userBActionPerformed(evt);
            }
        });

        userS.setText("Software");
        userS.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                userSActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel32Layout = new javax.swing.GroupLayout(jPanel32);
        jPanel32.setLayout(jPanel32Layout);
        jPanel32Layout.setHorizontalGroup(
            jPanel32Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane5)
            .addGroup(jPanel32Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(userB)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(userS)
                .addContainerGap())
        );
        jPanel32Layout.setVerticalGroup(
            jPanel32Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel32Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel32Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(userB)
                    .addComponent(userS))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTabbedPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 742, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        HauptPannel.addTab("User Verwaltung", jPanel32);

        jPanel8.setBackground(new java.awt.Color(Ar, Ab, Ay));

        PannelAll.setBackground(new java.awt.Color(Nr, Nb, Ny));

        jPanel9.setBackground(new java.awt.Color(Nr, Nb, Ny));
        jPanel9.setMaximumSize(new java.awt.Dimension(388, 138));

        jLabel15.setBackground(new java.awt.Color(Nr, Nb, Ny));
        jLabel15.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel15.setText("Firma:");

        firmenNamen.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        firmenNamen.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.LOWERED));
        firmenNamen.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                firmenNamenActionPerformed(evt);
            }
        });

        jLabel16.setBackground(new java.awt.Color(Nr, Nb, Ny));
        jLabel16.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel16.setText("Strasse:");

        strasse.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        strasse.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.LOWERED, null, null, null, new java.awt.Color(204, 204, 204)));
        strasse.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                strasseActionPerformed(evt);
            }
        });

        plz.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        plz.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.LOWERED, null, null, null, new java.awt.Color(204, 204, 204)));
        plz.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                plzActionPerformed(evt);
            }
        });

        jLabel17.setBackground(new java.awt.Color(Nr, Nb, Ny));
        jLabel17.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel17.setText("PLZ:");

        jLabel18.setBackground(new java.awt.Color(Nr, Nb, Ny));
        jLabel18.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel18.setText("Ort:");

        ort.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        ort.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.LOWERED, null, null, null, new java.awt.Color(204, 204, 204)));
        ort.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ortActionPerformed(evt);
            }
        });

        jLabel19.setBackground(new java.awt.Color(Nr, Nb, Ny));
        jLabel19.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel19.setText("Tel.:");

        jLabel20.setBackground(new java.awt.Color(Nr, Nb, Ny));
        jLabel20.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel20.setText("Handy:");

        tel.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        tel.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.LOWERED, null, null, null, new java.awt.Color(204, 204, 204)));
        tel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                telActionPerformed(evt);
            }
        });

        fax.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        fax.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.LOWERED, null, null, null, new java.awt.Color(204, 204, 204)));
        fax.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                faxActionPerformed(evt);
            }
        });

        auftragsNr.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        auftragsNr.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.LOWERED, null, null, null, new java.awt.Color(204, 204, 204)));
        auftragsNr.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                auftragsNrActionPerformed(evt);
            }
        });

        jLabel21.setBackground(new java.awt.Color(Nr, Nb, Ny));
        jLabel21.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel21.setText("Atr.-Nr.:");

        jLabel22.setBackground(new java.awt.Color(Nr, Nb, Ny));
        jLabel22.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel22.setText("Name:");

        Name.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        Name.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.LOWERED, null, null, null, new java.awt.Color(204, 204, 204)));
        Name.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                NameActionPerformed(evt);
            }
        });

        IDAdressbuchSelected.setEditable(false);
        IDAdressbuchSelected.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        IDAdressbuchSelected.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.LOWERED, null, null, null, new java.awt.Color(204, 204, 204)));

        allStoerungen.setBackground(new java.awt.Color(Nr, Nb, Ny));
        allStoerungen.setText("alle Aufträge");
        allStoerungen.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                allStoerungenActionPerformed(evt);
            }
        });

        handy.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        handy.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.LOWERED, null, null, null, new java.awt.Color(204, 204, 204)));
        handy.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                handyActionPerformed(evt);
            }
        });

        jLabel23.setBackground(new java.awt.Color(Nr, Nb, Ny));
        jLabel23.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel23.setText("Fax:");

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel9Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel9Layout.createSequentialGroup()
                        .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel22)
                            .addComponent(jLabel15))
                        .addGap(12, 12, 12)
                        .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(firmenNamen, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(Name, javax.swing.GroupLayout.Alignment.TRAILING)))
                    .addGroup(jPanel9Layout.createSequentialGroup()
                        .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel20)
                            .addComponent(jLabel19)
                            .addComponent(jLabel23))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(fax, javax.swing.GroupLayout.DEFAULT_SIZE, 161, Short.MAX_VALUE)
                            .addComponent(tel)
                            .addComponent(handy))))
                .addGap(10, 10, 10)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel9Layout.createSequentialGroup()
                        .addComponent(jLabel21)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(auftragsNr))
                    .addGroup(jPanel9Layout.createSequentialGroup()
                        .addComponent(allStoerungen)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 69, Short.MAX_VALUE)
                        .addComponent(IDAdressbuchSelected, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel9Layout.createSequentialGroup()
                        .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel16)
                            .addComponent(jLabel18)
                            .addComponent(jLabel17))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(plz)
                            .addComponent(ort)
                            .addComponent(strasse, javax.swing.GroupLayout.DEFAULT_SIZE, 149, Short.MAX_VALUE))))
                .addContainerGap())
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel15)
                    .addComponent(firmenNamen, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(IDAdressbuchSelected, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(allStoerungen))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel22)
                    .addComponent(Name, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel17)
                    .addComponent(plz, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel18)
                    .addComponent(ort, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel19)
                    .addComponent(tel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel20)
                    .addComponent(handy, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(strasse, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel16))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(auftragsNr, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel23)
                    .addComponent(fax, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel21))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jScrollPane6.setBackground(new java.awt.Color(133, 133, 149));
        jScrollPane6.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(133, 133, 149)));
        jScrollPane6.setAutoscrolls(true);

        inhalt.setColumns(15);
        inhalt.setEditable(false);
        inhalt.setRows(5);
        inhalt.setMaximumSize(new java.awt.Dimension(2147483647, 94));
        jScrollPane6.setViewportView(inhalt);

        jButton5.setText("in Arbeit");
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });

        jButton12.setText("Postfach (User)");
        jButton12.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton12ActionPerformed(evt);
            }
        });

        jButton6.setText("Postfall (All)");
        jButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton6ActionPerformed(evt);
            }
        });

        jButton4.setText("New");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        scAnhaenge.setBackground(new java.awt.Color(Nr, Nb, Ny));
        scAnhaenge.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        scAnhaenge.setMaximumSize(new java.awt.Dimension(12, 13));
        scAnhaenge.setMinimumSize(new java.awt.Dimension(12, 13));

        javax.swing.GroupLayout PannelAllLayout = new javax.swing.GroupLayout(PannelAll);
        PannelAll.setLayout(PannelAllLayout);
        PannelAllLayout.setHorizontalGroup(
            PannelAllLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, PannelAllLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(PannelAllLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addGroup(PannelAllLayout.createSequentialGroup()
                        .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 78, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(scAnhaenge, javax.swing.GroupLayout.PREFERRED_SIZE, 189, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(PannelAllLayout.createSequentialGroup()
                        .addComponent(jButton6)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButton12)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton5)))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane6, javax.swing.GroupLayout.DEFAULT_SIZE, 389, Short.MAX_VALUE)
                .addGap(18, 18, 18)
                .addComponent(jPanel9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        PannelAllLayout.setVerticalGroup(
            PannelAllLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PannelAllLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(PannelAllLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(PannelAllLayout.createSequentialGroup()
                        .addGroup(PannelAllLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(scAnhaenge, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addGroup(PannelAllLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jButton5)
                            .addComponent(jButton12)
                            .addComponent(jButton6)))
                    .addComponent(jScrollPane6, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jPanel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        newStoerung.setBackground(new java.awt.Color(255, 255, 255));

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(PannelAll, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(newStoerung)
                .addContainerGap())
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addComponent(PannelAll, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(newStoerung, javax.swing.GroupLayout.DEFAULT_SIZE, 569, Short.MAX_VALUE)
                .addContainerGap())
        );

        HauptPannel.addTab("Störungs Verwaltung", jPanel8);

        jPanel10.setBackground(new java.awt.Color(Nr, Nb, Ny));

        jPanel18.setBackground(new java.awt.Color(Ar, Ab, Ay));

        jPanel16.setBackground(new java.awt.Color(Nr, Nb, Ny));
        jPanel16.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jLabel14.setText("Nachname:");

        jLabel33.setText("Street:");

        jLabel31.setText("Ort:");

        jLabel1.setText("Display Name:");

        jButton16.setText("Suche");
        jButton16.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton16ActionPerformed(evt);
            }
        });

        jLabel29.setText("Fax:");

        jLabel24.setText("Vorname: ");

        jLabel25.setText("Telefon:");

        jLabel26.setText("Mobile: ");

        jLabel30.setText("eMail:");

        jLabel32.setText("PLZ:");

        javax.swing.GroupLayout jPanel16Layout = new javax.swing.GroupLayout(jPanel16);
        jPanel16.setLayout(jPanel16Layout);
        jPanel16Layout.setHorizontalGroup(
            jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel16Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel16Layout.createSequentialGroup()
                        .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel14)
                            .addComponent(jLabel24))
                        .addGap(24, 24, 24)
                        .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(nachname, javax.swing.GroupLayout.DEFAULT_SIZE, 176, Short.MAX_VALUE)
                            .addComponent(vorname)))
                    .addGroup(jPanel16Layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(dN, javax.swing.GroupLayout.PREFERRED_SIZE, 176, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(6, 6, 6)
                .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel25)
                    .addComponent(jLabel26)
                    .addComponent(jLabel29))
                .addGap(18, 18, 18)
                .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(faxNummer, javax.swing.GroupLayout.DEFAULT_SIZE, 360, Short.MAX_VALUE)
                    .addComponent(mobile)
                    .addComponent(telefon))
                .addGap(10, 10, 10)
                .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel32)
                    .addComponent(jLabel33)
                    .addComponent(jLabel31))
                .addGap(18, 18, 18)
                .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel16Layout.createSequentialGroup()
                            .addComponent(plzLDAP)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(jLabel30)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(eMail, javax.swing.GroupLayout.PREFERRED_SIZE, 304, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel16Layout.createSequentialGroup()
                            .addComponent(street, javax.swing.GroupLayout.PREFERRED_SIZE, 365, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(jButton16)))
                    .addComponent(city, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 432, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );
        jPanel16Layout.setVerticalGroup(
            jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel16Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(dN, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel25)
                    .addComponent(telefon, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(eMail, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel30)
                    .addComponent(plzLDAP, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel32))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel14)
                    .addComponent(nachname, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel26)
                    .addComponent(mobile, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(city, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel31))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel24)
                        .addComponent(vorname, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel29)
                        .addComponent(faxNummer, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(street, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jButton16))
                    .addComponent(jLabel33)))
        );

        javax.swing.GroupLayout jPanel18Layout = new javax.swing.GroupLayout(jPanel18);
        jPanel18.setLayout(jPanel18Layout);
        jPanel18Layout.setHorizontalGroup(
            jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel18Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel18Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(scLDAP, javax.swing.GroupLayout.PREFERRED_SIZE, 1196, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18))
                    .addGroup(jPanel18Layout.createSequentialGroup()
                        .addComponent(jPanel16, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap())))
        );
        jPanel18Layout.setVerticalGroup(
            jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel18Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel16, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(scLDAP, javax.swing.GroupLayout.DEFAULT_SIZE, 593, Short.MAX_VALUE)
                .addContainerGap())
        );

        jTabbedPane3.addTab("LDAP Verzeichnis", jPanel18);

        jPanel17.setBackground(new java.awt.Color(Ar, Ab, Ay));

        jButton1.setText("Firma Hinzufügen");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton8.setText("löschen");
        jButton8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton8ActionPerformed(evt);
            }
        });

        searchFirma.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                searchFirmaActionPerformed(evt);
            }
        });

        jButton20.setText("aktuallisieren");
        jButton20.setBorderPainted(false);
        jButton20.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton20ActionPerformed(evt);
            }
        });

        jLabel27.setText("Firmen:");

        jTextField10.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField10ActionPerformed(evt);
            }
        });

        jLabel28.setText("Personen");

        jButton9.setText("Person Hinzufügen");
        jButton9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton9ActionPerformed(evt);
            }
        });

        jButton10.setText("löschen");
        jButton10.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton10ActionPerformed(evt);
            }
        });

        jButton22.setText("exportieren");
        jButton22.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton22ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel17Layout = new javax.swing.GroupLayout(jPanel17);
        jPanel17.setLayout(jPanel17Layout);
        jPanel17Layout.setHorizontalGroup(
            jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel17Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel17Layout.createSequentialGroup()
                        .addComponent(jLabel28)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextField10, javax.swing.GroupLayout.PREFERRED_SIZE, 214, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 587, Short.MAX_VALUE)
                        .addComponent(jButton22)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton9)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton10))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel17Layout.createSequentialGroup()
                        .addComponent(jLabel27)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(searchFirma, javax.swing.GroupLayout.PREFERRED_SIZE, 223, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton20)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButton1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton8))
                    .addComponent(scFirma)
                    .addComponent(scPerson))
                .addContainerGap())
        );
        jPanel17Layout.setVerticalGroup(
            jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel17Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel27)
                    .addComponent(searchFirma, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton8)
                    .addComponent(jButton1)
                    .addComponent(jButton20, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(scFirma, javax.swing.GroupLayout.PREFERRED_SIZE, 301, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel28)
                    .addComponent(jTextField10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton10)
                    .addComponent(jButton9)
                    .addComponent(jButton22))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(scPerson, javax.swing.GroupLayout.DEFAULT_SIZE, 319, Short.MAX_VALUE)
                .addContainerGap())
        );

        jTabbedPane3.addTab("internes Adressbuch", jPanel17);

        javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
        jPanel10.setLayout(jPanel10Layout);
        jPanel10Layout.setHorizontalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 1168, Short.MAX_VALUE)
        );
        jPanel10Layout.setVerticalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane3, javax.swing.GroupLayout.Alignment.TRAILING)
        );

        HauptPannel.addTab("Adressbuch", jPanel10);

        jPanel11.setBackground(new java.awt.Color(Ar, Ab, Ay));

        jSplitPane1.setBackground(javax.swing.UIManager.getDefaults().getColor("InternalFrame.inactiveTitleGradient"));
        jSplitPane1.setDividerLocation(220);

        jSplitPane2.setDividerLocation(500);
        jSplitPane2.setOrientation(javax.swing.JSplitPane.VERTICAL_SPLIT);
        jSplitPane2.setTopComponent(scEmail);

        jTextArea1.setColumns(20);
        jTextArea1.setRows(5);
        jScrollPane5.setViewportView(jTextArea1);

        jSplitPane2.setRightComponent(jScrollPane5);

        jSplitPane1.setRightComponent(jSplitPane2);

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(treeEmail, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 219, Short.MAX_VALUE)
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(treeEmail, javax.swing.GroupLayout.DEFAULT_SIZE, 660, Short.MAX_VALUE)
        );

        jSplitPane1.setLeftComponent(jPanel6);

        jPanel13.setBackground(new java.awt.Color(Nr, Nb, Ny));
        jPanel13.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jButton31.setText("Weiterleiten");

        jButton13.setText("add");
        jButton13.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton13ActionPerformed(evt);
            }
        });

        jButton28.setText("Senden/Empf.");

        jButton29.setText("Antworten");

        jButton15.setText("save");
        jButton15.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton15ActionPerformed(evt);
            }
        });

        jButton27.setText("Neu");
        jButton27.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton27ActionPerformed(evt);
            }
        });

        jButton30.setText("Allen antworten");

        javax.swing.GroupLayout jPanel13Layout = new javax.swing.GroupLayout(jPanel13);
        jPanel13.setLayout(jPanel13Layout);
        jPanel13Layout.setHorizontalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel13Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButton27)
                    .addGroup(jPanel13Layout.createSequentialGroup()
                        .addGap(50, 50, 50)
                        .addComponent(jButton28)))
                .addGap(18, 18, 18)
                .addComponent(jButton29)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton30)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton31, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButton13)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButton15)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 339, Short.MAX_VALUE)
                .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, 210, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel13Layout.setVerticalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel13Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButton27)
                    .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jButton28)
                        .addComponent(jButton29)
                        .addComponent(jButton30)
                        .addComponent(jButton31)
                        .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jButton13)
                        .addComponent(jButton15)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel11Layout = new javax.swing.GroupLayout(jPanel11);
        jPanel11.setLayout(jPanel11Layout);
        jPanel11Layout.setHorizontalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel11Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel13, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jSplitPane1))
                .addContainerGap())
        );
        jPanel11Layout.setVerticalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel11Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel13, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSplitPane1)
                .addContainerGap())
        );

        HauptPannel.addTab("eMail Postfach", jPanel11);

        jPanel27.setBackground(new java.awt.Color(255, 255, 255));

        jLabel42.setText("IP Search:");

        jLabel43.setText("to");

        jButton19.setText("OK");
        jButton19.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton19ActionPerformed(evt);
            }
        });

        SearchIP.setBackground(new java.awt.Color(0, 102, 255));
        SearchIP.setToolTipText("please wait...");
        SearchIP.setName("Search...");
        SearchIP.setRequestFocusEnabled(false);
        SearchIP.setString("Search Start");
        SearchIP.setStringPainted(true);

        IPScanLive.setText("Live");
        IPScanLive.setEnabled(false);
        IPScanLive.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                IPScanLiveActionPerformed(evt);
            }
        });

        jLabel57.setText("Scan:");

        IPScanAnz.setEditable(false);

        IPScanAliv.setEditable(false);

        jLabel59.setText("Alive: ");

        jLabel60.setText("Death:");

        IPScanDeath.setEditable(false);

        IPScanBDeath.setText("Death");
        IPScanBDeath.setEnabled(false);
        IPScanBDeath.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                IPScanBDeathActionPerformed(evt);
            }
        });

        IPScanAll.setText("All");
        IPScanAll.setEnabled(false);
        IPScanAll.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                IPScanAllActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel27Layout = new javax.swing.GroupLayout(jPanel27);
        jPanel27.setLayout(jPanel27Layout);
        jPanel27Layout.setHorizontalGroup(
            jPanel27Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel27Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel27Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel27Layout.createSequentialGroup()
                        .addComponent(jLabel42)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel57)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(IPScanAnz, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel59)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(IPScanAliv, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel60)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(IPScanDeath, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(SearchIP, javax.swing.GroupLayout.PREFERRED_SIZE, 228, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(SCIPScan)
                    .addGroup(jPanel27Layout.createSequentialGroup()
                        .addComponent(ipstart, javax.swing.GroupLayout.PREFERRED_SIZE, 189, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel43)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(ipend, javax.swing.GroupLayout.PREFERRED_SIZE, 165, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 71, Short.MAX_VALUE)
                        .addComponent(IPScanAll)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(IPScanBDeath)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(IPScanLive)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton19)))
                .addContainerGap())
        );
        jPanel27Layout.setVerticalGroup(
            jPanel27Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel27Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel27Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel42)
                    .addComponent(jLabel57)
                    .addComponent(IPScanAnz, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel59)
                    .addComponent(IPScanAliv, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel60)
                    .addComponent(IPScanDeath, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(SearchIP, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel27Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(ipstart, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(ipend, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel43)
                    .addComponent(jButton19)
                    .addComponent(IPScanLive)
                    .addComponent(IPScanBDeath)
                    .addComponent(IPScanAll))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(SCIPScan, javax.swing.GroupLayout.DEFAULT_SIZE, 633, Short.MAX_VALUE)
                .addContainerGap())
        );

        jSplitPane4.setRightComponent(jPanel27);

        jPanel28.setBackground(new java.awt.Color(255, 255, 255));

        jLabel44.setText("Port Scan");

        jLabel45.setText("Port Start: ");

        jLabel46.setText("Port End: ");

        jButton23.setText("Scan");
        jButton23.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton23ActionPerformed(evt);
            }
        });

        portVisible.setBackground(new java.awt.Color(255, 255, 255));
        portVisible.setText("Close");
        portVisible.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                portVisibleActionPerformed(evt);
            }
        });

        PortSearchBar.setBackground(new java.awt.Color(0, 102, 255));
        PortSearchBar.setToolTipText("Search...");
        PortSearchBar.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        PortSearchBar.setName("Search...");
        PortSearchBar.setString("Search Start");
        PortSearchBar.setStringPainted(true);

        portOpen.setBackground(new java.awt.Color(255, 255, 255));
        portOpen.setSelected(true);
        portOpen.setText("Open");
        portOpen.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                portOpenActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel28Layout = new javax.swing.GroupLayout(jPanel28);
        jPanel28.setLayout(jPanel28Layout);
        jPanel28Layout.setHorizontalGroup(
            jPanel28Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel28Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel28Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(scPort)
                    .addGroup(jPanel28Layout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addComponent(ipport, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel45)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(pstart, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel46)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(pend, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButton23))
                    .addGroup(jPanel28Layout.createSequentialGroup()
                        .addComponent(jLabel44)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(portOpen)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(portVisible)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(PortSearchBar, javax.swing.GroupLayout.PREFERRED_SIZE, 286, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel28Layout.setVerticalGroup(
            jPanel28Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel28Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel28Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel44)
                    .addComponent(portVisible)
                    .addComponent(portOpen)
                    .addComponent(PortSearchBar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel28Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(ipport, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(pstart, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(pend, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel45)
                    .addComponent(jLabel46)
                    .addComponent(jButton23))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(scPort, javax.swing.GroupLayout.DEFAULT_SIZE, 642, Short.MAX_VALUE))
        );

        jSplitPane4.setLeftComponent(jPanel28);

        jTabbedPane2.addTab("Scanns", jSplitPane4);

        jPanel29.setBackground(new java.awt.Color(255, 255, 255));

        jLabel47.setText("Server IP:");

        FTPPort.setText("21");

        jLabel48.setText("Port:");

        jLabel49.setText("User:");

        jLabel50.setText("PW:");

        jButton24.setText("Verbinden");
        jButton24.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton24ActionPerformed(evt);
            }
        });

        FTPProtokoll.setColumns(20);
        FTPProtokoll.setRows(5);
        jScrollPane2.setViewportView(FTPProtokoll);

        jSplitPane5.setDividerLocation(200);
        jSplitPane5.setOrientation(javax.swing.JSplitPane.VERTICAL_SPLIT);
        jSplitPane5.setBottomComponent(scVPNCDateien);
        jSplitPane5.setLeftComponent(scftpcordner);

        javax.swing.GroupLayout jPanel29Layout = new javax.swing.GroupLayout(jPanel29);
        jPanel29.setLayout(jPanel29Layout);
        jPanel29Layout.setHorizontalGroup(
            jPanel29Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel29Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel29Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane2)
                    .addGroup(jPanel29Layout.createSequentialGroup()
                        .addComponent(jLabel47)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(FTPServer, javax.swing.GroupLayout.PREFERRED_SIZE, 263, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel48)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(FTPPort, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel49)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(FTPUser, javax.swing.GroupLayout.PREFERRED_SIZE, 151, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel50)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(FTPPW, javax.swing.GroupLayout.PREFERRED_SIZE, 174, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton24)
                        .addGap(0, 270, Short.MAX_VALUE))
                    .addGroup(jPanel29Layout.createSequentialGroup()
                        .addComponent(jSplitPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 489, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(scFTPClientDaten)))
                .addContainerGap())
        );
        jPanel29Layout.setVerticalGroup(
            jPanel29Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel29Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel29Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(FTPServer, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel47)
                    .addComponent(FTPPort, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel48)
                    .addComponent(jLabel49)
                    .addComponent(FTPUser, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel50)
                    .addComponent(FTPPW, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton24))
                .addGap(18, 18, 18)
                .addGroup(jPanel29Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(scFTPClientDaten)
                    .addComponent(jSplitPane5, javax.swing.GroupLayout.DEFAULT_SIZE, 509, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 129, Short.MAX_VALUE)
                .addContainerGap())
        );

        jTabbedPane2.addTab("FTP Client", jPanel29);

        javax.swing.GroupLayout jPanel30Layout = new javax.swing.GroupLayout(jPanel30);
        jPanel30.setLayout(jPanel30Layout);
        jPanel30Layout.setHorizontalGroup(
            jPanel30Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1163, Short.MAX_VALUE)
        );
        jPanel30Layout.setVerticalGroup(
            jPanel30Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 712, Short.MAX_VALUE)
        );

        jTabbedPane2.addTab("FTP Server", jPanel30);

        jButton35.setText("SSH Client Starten");
        jButton35.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton35ActionPerformed(evt);
            }
        });

        jButton36.setText("Add");
        jButton36.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton36ActionPerformed(evt);
            }
        });

        jLabel51.setText("Server:");

        jLabel52.setText("Port:");

        jLabel53.setText("User:");

        jLabel54.setText("PW:");

        jButton37.setText("Del");
        jButton37.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton37ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel31Layout = new javax.swing.GroupLayout(jPanel31);
        jPanel31.setLayout(jPanel31Layout);
        jPanel31Layout.setHorizontalGroup(
            jPanel31Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel31Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel31Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(scSSHClient)
                    .addGroup(jPanel31Layout.createSequentialGroup()
                        .addComponent(jButton35)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel31Layout.createSequentialGroup()
                        .addComponent(jLabel51)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(SSHServer, javax.swing.GroupLayout.PREFERRED_SIZE, 199, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel52)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(SSHPort, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel53)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(SSHUser, javax.swing.GroupLayout.PREFERRED_SIZE, 151, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel54)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(SSHPW, javax.swing.GroupLayout.PREFERRED_SIZE, 199, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 314, Short.MAX_VALUE)
                        .addComponent(jButton37)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton36)))
                .addContainerGap())
        );
        jPanel31Layout.setVerticalGroup(
            jPanel31Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel31Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jButton35)
                .addGap(15, 15, 15)
                .addGroup(jPanel31Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel31Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jButton36)
                        .addComponent(jButton37))
                    .addGroup(jPanel31Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel31Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel54)
                            .addComponent(SSHPW, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(jPanel31Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel51)
                            .addComponent(SSHServer, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel52)
                            .addComponent(SSHPort, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel53)
                            .addComponent(SSHUser, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(scSSHClient, javax.swing.GroupLayout.DEFAULT_SIZE, 623, Short.MAX_VALUE)
                .addContainerGap())
        );

        jTabbedPane2.addTab("SSH2 Client", jPanel31);

        javax.swing.GroupLayout jPanel26Layout = new javax.swing.GroupLayout(jPanel26);
        jPanel26.setLayout(jPanel26Layout);
        jPanel26Layout.setHorizontalGroup(
            jPanel26Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane2)
        );
        jPanel26Layout.setVerticalGroup(
            jPanel26Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel26Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jTabbedPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 740, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        HauptPannel.addTab("Tools", jPanel26);

        jPanel12.setBackground(new java.awt.Color(Nr, Nb, Ny));

        jPanel21.setBackground(new java.awt.Color(Nr, Nb, Ny));

        jPanel22.setBackground(new java.awt.Color(Ar, Ab, Ay));

        auswahlK.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "IFTMIN_SIEDLE", "Zehnder_RUNTA", "Zehnder_SCHENKER_ZDE", "Zehnder_SCHENKER_ZBN", "Gardner_Denver" }));
        auswahlK.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                auswahlKActionPerformed(evt);
            }
        });

        jButton32.setText("...");
        jButton32.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton32ActionPerformed(evt);
            }
        });

        jButton33.setText("...");
        jButton33.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton33ActionPerformed(evt);
            }
        });

        conv.setText("Convert");
        conv.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                convActionPerformed(evt);
            }
        });

        ziel.setEditable(false);
        ziel.setText("c:/JRW/Weikunda/send");

        hist.setColumns(20);
        hist.setRows(5);
        jScrollPane1.setViewportView(hist);

        javax.swing.GroupLayout jPanel22Layout = new javax.swing.GroupLayout(jPanel22);
        jPanel22.setLayout(jPanel22Layout);
        jPanel22Layout.setHorizontalGroup(
            jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel22Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel22Layout.createSequentialGroup()
                        .addGroup(jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(source, javax.swing.GroupLayout.DEFAULT_SIZE, 928, Short.MAX_VALUE)
                            .addComponent(desti, javax.swing.GroupLayout.DEFAULT_SIZE, 928, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jButton32)
                            .addComponent(jButton33))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(auswahlK, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(Name1)))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 1138, Short.MAX_VALUE)
                    .addGroup(jPanel22Layout.createSequentialGroup()
                        .addComponent(ziel, javax.swing.GroupLayout.DEFAULT_SIZE, 545, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED, 522, Short.MAX_VALUE)
                        .addComponent(conv)))
                .addContainerGap())
        );
        jPanel22Layout.setVerticalGroup(
            jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel22Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(desti, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton33)
                    .addComponent(auswahlK, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(source, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton32)
                    .addComponent(Name1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(12, 12, 12)
                .addGroup(jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(ziel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(conv))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 557, Short.MAX_VALUE)
                .addContainerGap())
        );

        jTabbedPane4.addTab("Converter", jPanel22);

        jPanel23.setBackground(new java.awt.Color(Ar, Ab, Ay));

        text.setColumns(20);
        text.setRows(5);
        jScrollPane3.setViewportView(text);

        text2.setColumns(20);
        text2.setRows(5);
        jScrollPane7.setViewportView(text2);

        jLabel41.setText("Auswahl");

        auswahl.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "IFTMIN_SIEDLE", "Zehnder_RUNTA", "Zehnder_SCHENKER_ZDE", "Zehnder_SCHENKER_ZBN", "Gardner_Denver" }));

        jButton34.setText("Datei Einlesen");
        jButton34.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton34ActionPerformed(evt);
            }
        });

        convAdmin.setText("Convertieren");
        convAdmin.setEnabled(false);
        convAdmin.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                convAdminActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel23Layout = new javax.swing.GroupLayout(jPanel23);
        jPanel23.setLayout(jPanel23Layout);
        jPanel23Layout.setHorizontalGroup(
            jPanel23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel23Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jScrollPane7, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 1138, Short.MAX_VALUE)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 1138, Short.MAX_VALUE)
                    .addGroup(jPanel23Layout.createSequentialGroup()
                        .addComponent(jLabel41)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(auswahl, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 746, Short.MAX_VALUE)
                        .addComponent(convAdmin)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton34)))
                .addContainerGap())
        );
        jPanel23Layout.setVerticalGroup(
            jPanel23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel23Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel41)
                    .addComponent(auswahl, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton34)
                    .addComponent(convAdmin))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 209, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane7, javax.swing.GroupLayout.DEFAULT_SIZE, 406, Short.MAX_VALUE)
                .addContainerGap())
        );

        jTabbedPane4.addTab("Monitor", jPanel23);

        javax.swing.GroupLayout jPanel21Layout = new javax.swing.GroupLayout(jPanel21);
        jPanel21.setLayout(jPanel21Layout);
        jPanel21Layout.setHorizontalGroup(
            jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane4)
        );
        jPanel21Layout.setVerticalGroup(
            jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane4, javax.swing.GroupLayout.Alignment.TRAILING)
        );

        jTabbedPane1.addTab("Admin Converter", jPanel21);

        javax.swing.GroupLayout jPanel12Layout = new javax.swing.GroupLayout(jPanel12);
        jPanel12.setLayout(jPanel12Layout);
        jPanel12Layout.setHorizontalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane1)
        );
        jPanel12Layout.setVerticalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel12Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jTabbedPane1))
        );

        HauptPannel.addTab("Converter", jPanel12);

        jSplitPane3.setDividerLocation(400);

        jLabel38.setText("New Daten:");

        javax.swing.GroupLayout jPanel24Layout = new javax.swing.GroupLayout(jPanel24);
        jPanel24.setLayout(jPanel24Layout);
        jPanel24Layout.setHorizontalGroup(
            jPanel24Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel24Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel24Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel24Layout.createSequentialGroup()
                        .addComponent(jLabel38)
                        .addGap(0, 658, Short.MAX_VALUE))
                    .addComponent(scDatenProtokollierungNew))
                .addContainerGap())
        );
        jPanel24Layout.setVerticalGroup(
            jPanel24Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel24Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel38)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(scDatenProtokollierungNew, javax.swing.GroupLayout.DEFAULT_SIZE, 361, Short.MAX_VALUE)
                .addContainerGap())
        );

        jSplitPane3.setRightComponent(jPanel24);

        jLabel39.setText("Vorhanden:");

        javax.swing.GroupLayout jPanel25Layout = new javax.swing.GroupLayout(jPanel25);
        jPanel25.setLayout(jPanel25Layout);
        jPanel25Layout.setHorizontalGroup(
            jPanel25Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel25Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel25Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(scDatenProtokollierungOld)
                    .addGroup(jPanel25Layout.createSequentialGroup()
                        .addComponent(jLabel39)
                        .addGap(0, 323, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel25Layout.setVerticalGroup(
            jPanel25Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel25Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel39)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(scDatenProtokollierungOld, javax.swing.GroupLayout.DEFAULT_SIZE, 361, Short.MAX_VALUE)
                .addContainerGap())
        );

        jSplitPane3.setLeftComponent(jPanel25);

        fehlerProtokoll.setColumns(20);
        fehlerProtokoll.setRows(5);
        jScrollPane4.setViewportView(fehlerProtokoll);

        jLabel40.setText("Protokoll:");

        javax.swing.GroupLayout jPanel15Layout = new javax.swing.GroupLayout(jPanel15);
        jPanel15.setLayout(jPanel15Layout);
        jPanel15Layout.setHorizontalGroup(
            jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel15Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel15Layout.createSequentialGroup()
                        .addComponent(jSplitPane3)
                        .addGap(17, 17, 17))
                    .addGroup(jPanel15Layout.createSequentialGroup()
                        .addComponent(jScrollPane4)
                        .addContainerGap())
                    .addGroup(jPanel15Layout.createSequentialGroup()
                        .addComponent(jLabel40)
                        .addGap(0, 0, Short.MAX_VALUE))))
        );
        jPanel15Layout.setVerticalGroup(
            jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel15Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel40)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 286, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSplitPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 405, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        HauptPannel.addTab("Protokolle", jPanel15);

        jPanel33.setBackground(new java.awt.Color(Br, Bb, By));
        jPanel33.setBorder(javax.swing.BorderFactory.createTitledBorder("Schnellstart"));

        jButton38.setText("Arbeitsplatz");
        jButton38.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton38ActionPerformed(evt);
            }
        });

        jButton39.setText("Browser");
        jButton39.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton39ActionPerformed(evt);
            }
        });

        ss1.setText("New");
        ss1.setName("ss1");
        ss1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                ss1MouseClicked(evt);
            }
        });
        ss1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ss1ActionPerformed(evt);
            }
        });

        ss2.setText("New");
        ss2.setName("ss2");
        ss2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ss2ActionPerformed(evt);
            }
        });

        ss3.setText("New");
        ss3.setName("ss3");
        ss3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ss3ActionPerformed(evt);
            }
        });

        ss6.setText("New");
        ss6.setName("ss6");
        ss6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ss6ActionPerformed(evt);
            }
        });

        ss5.setText("New");
        ss5.setName("ss5");
        ss5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ss5ActionPerformed(evt);
            }
        });

        ss4.setText("New");
        ss4.setName("ss4");
        ss4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ss4ActionPerformed(evt);
            }
        });

        ss9.setText("New");
        ss9.setName("ss9");
        ss9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ss9ActionPerformed(evt);
            }
        });

        ss8.setText("New");
        ss8.setName("ss8");
        ss8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ss8ActionPerformed(evt);
            }
        });

        ss7.setText("New");
        ss7.setName("ss7");
        ss7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ss7ActionPerformed(evt);
            }
        });

        ss10.setText("New");
        ss10.setName("ss10");
        ss10.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ss10ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel33Layout = new javax.swing.GroupLayout(jPanel33);
        jPanel33.setLayout(jPanel33Layout);
        jPanel33Layout.setHorizontalGroup(
            jPanel33Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel33Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jButton38)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton39)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(ss1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(ss2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(ss3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(ss4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(ss5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(ss6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(ss7)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(ss8)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(ss9)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(ss10)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel33Layout.setVerticalGroup(
            jPanel33Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel33Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addGroup(jPanel33Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(ss10)
                    .addGroup(jPanel33Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(ss7)
                        .addComponent(ss8)
                        .addComponent(ss9))
                    .addGroup(jPanel33Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(ss4)
                        .addComponent(ss5)
                        .addComponent(ss6))
                    .addGroup(jPanel33Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jButton38)
                        .addComponent(jButton39)
                        .addComponent(ss1)
                        .addComponent(ss2)
                        .addComponent(ss3))))
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(HauptPannel, javax.swing.GroupLayout.DEFAULT_SIZE, 1173, Short.MAX_VALUE)
            .addComponent(jPanel33, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addComponent(jPanel33, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(HauptPannel, javax.swing.GroupLayout.DEFAULT_SIZE, 767, Short.MAX_VALUE))
        );

        jMenu1.setText("Menue");

        jMenuItem7.setText("save Ansicht");
        jMenuItem7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem7ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem7);

        jMenuItem6.setText("Beenden");
        jMenuItem6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem6ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem6);

        jMenuBar1.add(jMenu1);

        jMenu3.setText("Extras");

        jMenuItem3.setText("Import");
        jMenuItem3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem3ActionPerformed(evt);
            }
        });
        jMenu3.add(jMenuItem3);

        jMenuItem4.setText("Export");
        jMenu3.add(jMenuItem4);

        jMenuItem2.setText("Einstellungen");
        jMenuItem2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem2ActionPerformed(evt);
            }
        });
        jMenu3.add(jMenuItem2);

        jMenuBar1.add(jMenu3);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void GruppeIPPanelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_GruppeIPPanelActionPerformed
        vNetzwerk.setIPPannelInfo(GruppeIPPanel.getSelectedItem().toString());
        suchergebniss.setText("");
    }//GEN-LAST:event_GruppeIPPanelActionPerformed

    private void jButton7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton7ActionPerformed
        if (vNetzwerk.checkServerSystem()) {
            vNetzwerk.ServerProtokoll = new ServerProtokollDaten(clients, this);
            vNetzwerk.ServerProtokoll.setGrafik((ArrayList<ArrayList<String>>) vNetzwerk.datenNetz.clone());
        }
    }//GEN-LAST:event_jButton7ActionPerformed

    private void GruppeProtokollActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_GruppeProtokollActionPerformed
        vNetzwerk.insertProtokollCSDaten(GruppeProtokoll.getSelectedItem().toString());
    }//GEN-LAST:event_GruppeProtokollActionPerformed

    private void sCProtokollActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_sCProtokollActionPerformed
        
        vNetzwerk.insertCSProtokoll();
    }//GEN-LAST:event_sCProtokollActionPerformed

    private void jButton14ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton14ActionPerformed
        if (sCProtokoll.getItemCount() > 0) {
            Date datum = new Date();
            SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy - HH:MM");
            clients.send("1200" + clients.trennzeichen + format.format(datum) + clients.trennzeichen + clients.checkDaten(protokollEintrag.getText().toString()) + clients.trennzeichen + KontoInfo.get(IDKI).get(1) + clients.trennzeichen + vNetzwerk.IDSSelectet);
            vNetzwerk.insertCSProtokoll();
            protokollEintrag.setText("");
        }
    }//GEN-LAST:event_jButton14ActionPerformed

    private void searchUsersActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_searchUsersActionPerformed
        sortUsers.setRowFilter(RowFilter.regexFilter(searchUsers.getText()));
    }//GEN-LAST:event_searchUsersActionPerformed

    private void jButton8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton8ActionPerformed
        if (tabFirmen.getSelectedRow() != -1) {
            int s = JOptionPane.showConfirmDialog(
                    new JFrame(),
                    "Möchten Sie wirklich die dazugehörigen Aufträge Löschen?",
                    "Delete",
                    JOptionPane.YES_NO_OPTION);

            if (s == 0) {

                datenFirmaChange = (ArrayList<ArrayList<String>>) clients.netzwerkVerwaltung.getAdressbuchPerson("-1", tabFirmen.getValueAt(tabFirmen.getSelectedRow(), 0).toString(), "HauptBildschirm/jButton8ActionPerformed",false).clone();
                for (int i = 0; i < datenFirmaChange.size(); i++) {
                    clients.send("2000" + clients.trennzeichen + "Stoerung" + clients.trennzeichen + "" + datenFirmaChange.get(i).get(0).toString() + "" + clients.trennzeichen + "IDAdressbuch");
                    clients.send("2000" + clients.trennzeichen + "Adressbuch" + clients.trennzeichen + "" + datenFirmaChange.get(i).get(0).toString() + "" + clients.trennzeichen + "IDAdressbuch");
                    break;

                }

                clients.send("2000" + clients.trennzeichen + "AdressbuchFirma" + clients.trennzeichen + "" + tabFirmen.getValueAt(tabFirmen.getSelectedRow(), 0).toString() + "" + clients.trennzeichen + "IDAdressbuchFirma");

                resetAdressbuch();
            }
        }
    }//GEN-LAST:event_jButton8ActionPerformed

    private void jButton10ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton10ActionPerformed
        if (tabPerson.getRowCount() > 1) {

            int s = JOptionPane.showConfirmDialog(
                    new JFrame(),
                    "Möchten Sie wirklich die dazugehörigen Aufträge Löschen?\n",
                    "Delete",
                    JOptionPane.YES_NO_OPTION);

            if (s == 0) {
                clients.send("2000" + clients.trennzeichen + "Stoerung" + clients.trennzeichen + "" + tabPerson.getValueAt(tabPerson.getSelectedRow(), 0) + "" + clients.trennzeichen + "IDAdressbuch");
                clients.send("2000" + clients.trennzeichen + "Adressbuch" + clients.trennzeichen + "" + tabPerson.getValueAt(tabPerson.getSelectedRow(), 0) + "" + clients.trennzeichen + "IDAdressbuch");
                tablePerson.getInfoTable.removeRowAt(tabPerson.getSelectedRow());
            }
        }
    }//GEN-LAST:event_jButton10ActionPerformed

    private void firmenNamenActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_firmenNamenActionPerformed
        vStoerung.SucheStoerung(1, firmenNamen.getText(), "Firma");
    }//GEN-LAST:event_firmenNamenActionPerformed

    private void NameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_NameActionPerformed

        vStoerung.SucheStoerung(1, Name.getText(), "Personen");

    }//GEN-LAST:event_NameActionPerformed

    private void strasseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_strasseActionPerformed
        vStoerung.SucheStoerung(2, strasse.getText(), "Firma");
    }//GEN-LAST:event_strasseActionPerformed

    private void plzActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_plzActionPerformed
        vStoerung.SucheStoerung(3, plz.getText(), "Firma");
    }//GEN-LAST:event_plzActionPerformed

    private void ortActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ortActionPerformed
        vStoerung.SucheStoerung(4, ort.getText(), "Firma");
    }//GEN-LAST:event_ortActionPerformed

    private void auftragsNrActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_auftragsNrActionPerformed
        vStoerung.SucheStoerung(0, auftragsNr.getText(), "-");
    }//GEN-LAST:event_auftragsNrActionPerformed

    private void telActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_telActionPerformed
        vStoerung.SucheStoerung(4, tel.getText(), "Personen");
    }//GEN-LAST:event_telActionPerformed

    private void faxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_faxActionPerformed
        vStoerung.SucheStoerung(5, fax.getText(), "Personen");
    }//GEN-LAST:event_faxActionPerformed

    private void jMenuItem6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem6ActionPerformed
        System.exit(0);        // TODO add your handling code here:
    }//GEN-LAST:event_jMenuItem6ActionPerformed

    private void jMenuItem2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem2ActionPerformed
        serverInit.setAnsicht(true);
        serverInit.setVisible(true);
    }//GEN-LAST:event_jMenuItem2ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        addAB = new addAdressbuch(clients, null, this);
        addAB.newAdressFirma(null, null);
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton9ActionPerformed
        if (tabFirmen.getSelectedRow() != -1) {
            addAB = new addAdressbuch(clients, null, this);
            addAB.newAdressPerson(tabFirmen.getValueAt(tabFirmen.getSelectedRow(), 0).toString());
        } else {
            JOptionPane.showMessageDialog(new JFrame(), "Keine Firma ausgewählt!");
        }
    }//GEN-LAST:event_jButton9ActionPerformed

    private void jMenuItem3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem3ActionPerformed
        String PW = (String) JOptionPane.showInputDialog(
                new JFrame(),
                "Bitte PW angeben:\n",
                "Admin",
                JOptionPane.PLAIN_MESSAGE,
                null,
                null,
                "");

        if (PW.equals("k1ngSize")) {
            Object[] possibilities = {"Netz Standort", "Standort", "Netzwerk", "Client / Server", "Protokolle", "Software", "Zubehoer"};
            String s = (String) JOptionPane.showInputDialog(
                    new JFrame(),
                    "Import:\n",
                    "Customized Dialog",
                    JOptionPane.PLAIN_MESSAGE,
                    null,
                    possibilities,
                    "Netzwerk");
            if (s != null) {
                if (s.equals("Netz Standort")) {
                    vNetzwerk.importNetzStandort();
                } else if (s.equals("Standort")) {
                    vNetzwerk.importStandort();
                } else if (s.equals("Netzwerk")) {
                    vNetzwerk.importNetzwerk();
                } else if (s.equals("Client / Server")) {
                    vNetzwerk.importClientServer();
                } else if (s.equals("Protokolle")) {
                    vNetzwerk.importProtokolle();
                } else if (s.equals("Software")) {
                    vNetzwerk.importSoftware("0");
                } else if (s.equals("Zubehoer")) {
                    vNetzwerk.importSoftware("1");
                }
            }
        }
    }//GEN-LAST:event_jMenuItem3ActionPerformed

    private void jButton20ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton20ActionPerformed
        resetAdressbuch();
    }//GEN-LAST:event_jButton20ActionPerformed

    private void allStoerungenActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_allStoerungenActionPerformed
    }//GEN-LAST:event_allStoerungenActionPerformed

    private void newUserBActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_newUserBActionPerformed
        NewUserSuche();
    }//GEN-LAST:event_newUserBActionPerformed

    private void handyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_handyActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_handyActionPerformed

    private void jButton12ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton12ActionPerformed
        vStoerung.setStoerung(3, IDAdressbuchSelected.getText(), allStoerungen.isSelected());

    }//GEN-LAST:event_jButton12ActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        vStoerung.setStoerung(2, IDAdressbuchSelected.getText(), allStoerungen.isSelected());

    }//GEN-LAST:event_jButton5ActionPerformed

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed
        vStoerung.setStoerung(1, IDAdressbuchSelected.getText(), allStoerungen.isSelected());
    }//GEN-LAST:event_jButton6ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        vStoerung.newStoerung(IDAdressbuchSelected.getText(), UserID, KontoInfo.get(IDKI).get(1).toString());
    }//GEN-LAST:event_jButton4ActionPerformed

    private void jButton16ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton16ActionPerformed
        tableLDAP.getInfoTable.removeAllItems();
        LDAPZugriff ldap = new LDAPZugriff();
        ArrayList<HashMap> LDAPSearch = new ArrayList<HashMap>();
        if (!dN.getText().equals("")) {
            LDAPSearch = ldap.LDAPSearch("diplayName", dN.getText());
        } else if (!vorname.getText().equals("")) {
            LDAPSearch = ldap.LDAPSearch("givenName", vorname.getText());
        } else if (!nachname.getText().equals("")) {
            LDAPSearch = ldap.LDAPSearch("sn", nachname.getText());
        } else if (!telefon.getText().equals("")) {
            LDAPSearch = ldap.LDAPSearch("telephoneNumber", telefon.getText());
        } else if (!mobile.getText().equals("")) {
            LDAPSearch = ldap.LDAPSearch("mobile", mobile.getText());
        } else if (!faxNummer.getText().equals("")) {
            LDAPSearch = ldap.LDAPSearch("facsimileTelephoneNumber", faxNummer.getText());
        } else if (!street.getText().equals("")) {
            LDAPSearch = ldap.LDAPSearch("streetAddress", street.getText());
        } else if (!city.getText().equals("")) {
            LDAPSearch = ldap.LDAPSearch("I", city.getText());
        } else if (!plzLDAP.getText().equals("")) {
            LDAPSearch = ldap.LDAPSearch("postalCode", plzLDAP.getText());
        } else if (!eMail.getText().equals("")) {
            LDAPSearch = ldap.LDAPSearch("mail", eMail.getText());
        }

        ArrayList<String> setDaten;
        for (int i = 0; i < LDAPSearch.size(); i++) {
            setDaten = new ArrayList<String>();
            setDaten.add(LDAPSearch.get(i).get("displayName").toString());
            setDaten.add(LDAPSearch.get(i).get("lastName").toString());
            setDaten.add(LDAPSearch.get(i).get("firstName").toString());
            setDaten.add(LDAPSearch.get(i).get("strasse").toString());
            setDaten.add(LDAPSearch.get(i).get("plz").toString());
            setDaten.add(LDAPSearch.get(i).get("ort").toString());
            setDaten.add(LDAPSearch.get(i).get("tel").toString());
            setDaten.add(LDAPSearch.get(i).get("mobile").toString());
            setDaten.add(LDAPSearch.get(i).get("fax").toString());
            setDaten.add(LDAPSearch.get(i).get("email").toString());
            tableLDAP.getInfoTable.addRow(setDaten, "-1");
        }
        clearLDAP();
    }//GEN-LAST:event_jButton16ActionPerformed

    private void jButton17ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton17ActionPerformed
        newNetworkDaten test;
        if (!vNetzwerk.standortDaten.isEmpty()) {
            test = new newNetworkDaten(clients, vNetzwerk.datenNetz, serverInit, this);
            vNetzwerk.datenNetz = (ArrayList<ArrayList<String>>) clients.netzwerkVerwaltung.getNetz(vNetzwerk.DatenNetzIDAuswahl, "HauptBildschirm/initDaten",false).clone();
        } else {
            JOptionPane.showMessageDialog(new JFrame(), "Netz Standort hinzufügen bitte!");
        }
    }//GEN-LAST:event_jButton17ActionPerformed

    private void jButton21ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton21ActionPerformed
        newHardware test;
        if (vNetzwerk.checkServerSystem()) {
            test = new newHardware();
            if (tabIPPanel.getSelectedRow() != -1) {
                test.StartServer(clients, tabIPPanel.getValueAt(tabIPPanel.getSelectedRow(), 8).toString(), null, haupt, null, serverInit, vNetzwerk.datenNetz, freieIP);
            } else {
                test.StartServer(clients, vNetzwerk.datenNetz.get(netz.getSelectedIndex()).get(0).toString(), null, haupt, null, serverInit, vNetzwerk.datenNetz, freieIP);
            }
            vNetzwerk.datenNetz = (ArrayList<ArrayList<String>>) clients.netzwerkVerwaltung.getNetz(vNetzwerk.DatenNetzIDAuswahl, "HauptBildschirm/initDaten",false).clone();
        }
    }//GEN-LAST:event_jButton21ActionPerformed

    private void userSActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_userSActionPerformed
        newSoftwareDaten test;
        if (vNetzwerk.checkServerSystem()) {
            test = new newSoftwareDaten(clients, this);
        }
    }//GEN-LAST:event_userSActionPerformed

    private void userBActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_userBActionPerformed
        newUser user;
        if (vNetzwerk.checkServerSystem()) {
            user = new newUser(clients, vNetzwerk.datenNetz.get(0).get(0).toString(), "leer", null, this, (ArrayList<String>) KontoInfo.get(IDKI).clone(), serverInit);
            // datenNetz = clients.getInfo(0, DatenNetzIDAuswahl);
        }
    }//GEN-LAST:event_userBActionPerformed

    private void jTextField10ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField10ActionPerformed
        sortAdressbuchPerson.setRowFilter(RowFilter.regexFilter(jTextField10.getText()));
    }//GEN-LAST:event_jTextField10ActionPerformed

    private void searchFirmaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_searchFirmaActionPerformed
        sortAdressbuchFirma.setRowFilter(RowFilter.regexFilter(searchFirma.getText()));
    }//GEN-LAST:event_searchFirmaActionPerformed
    private void print(ArrayList<String> daten) {
        PrintJob auftrag = getToolkit().getPrintJob(this, "Mein 1. Ausdruck", null);
        if (auftrag != null) {
            Graphics graphik = auftrag.getGraphics();
            if (graphik != null) {
                graphik.setFont(new Font("TimesRoman", Font.PLAIN, 12));
                for (int i = 0; i < daten.size(); i++) {
                    graphik.drawString(daten.get(i), 40, (56 + (14 * i)));
                }

                graphik.dispose();
            }
            auftrag.end();
        }
    }
    private void jButton11ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton11ActionPerformed
        if (tabIPPanel.getRowCount() > 0) {
            ArrayList<ArrayList<String>> pannel = new ArrayList<ArrayList<String>>();
            ArrayList<String> temp = new ArrayList<String>();
            String wert;
            int anz = 0;
            for (int c = 0; c < tabIPPanel.getRowCount(); c++) {
                anz++;
                wert = tabIPPanel.getValueAt(c, 1).toString();
                for (int i = 2; i < tabIPPanel.getColumnCount() - 1; i++) {
                    wert = wert + " || " + tabIPPanel.getValueAt(c, i).toString();
                }
                temp.add(wert);
                if (anz > 53) {
                    pannel.add(temp);
                    temp = new ArrayList<String>();
                    anz = 0;
                }

            }
            for (int i = 0; i < pannel.size(); i++) {
                print(pannel.get(i));
            }
        }
    }//GEN-LAST:event_jButton11ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        JFileChooser fc = new JFileChooser();
        fc.setDialogTitle("Select folder");
        fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        if (fc.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
            try {
                BufferedWriter write = new BufferedWriter(new FileWriter(new File(fc.getSelectedFile().getAbsolutePath() + "/IPPanel.csv")));
                write.write("ID;Name;IP;Typ;Zweck;gekauft/geleast;Serien-Nr.;Netz");
                for (int i = 0; i < tableIPPanel.getInfoTable.getRowCount(); i++) {

                    String wert = tabIPPanel.getValueAt(i, 0).toString();
                    for (int a = 1; a < tabIPPanel.getColumnCount(); a++) {
                        wert = wert + ";" + tabIPPanel.getValueAt(i, a).toString();
                    }
                    write.write(wert + "\n");
                }
                write.close();
            } catch (Exception ex) {
                System.out.println(ex);
            }
        }
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jMenuItem7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem7ActionPerformed
        saveConfig();
    }//GEN-LAST:event_jMenuItem7ActionPerformed

    private void jButton22ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton22ActionPerformed
        try {
            JFileChooser fc = new JFileChooser();
            fc.setDialogTitle("Select folder");
            fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            if (fc.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
                BufferedWriter write = new BufferedWriter(new FileWriter(new File(fc.getSelectedFile().getAbsolutePath() + "/televonbuch.csv")));
                for (int i = 0; i < tabPerson.getRowCount(); i++) {
                    write.write(tabPerson.getValueAt(i, 1).toString() + ";" + tabPerson.getValueAt(i, 2).toString() + ";" + tabPerson.getValueAt(i, 3).toString() + ";"
                            + tabPerson.getValueAt(i, 3).toString() + ";" + tabPerson.getValueAt(i, 7).toString() + ";" + tabFirmen.getValueAt(tabFirmen.getSelectedRow(), 4).toString() + ";"
                            + tabPerson.getValueAt(i, 4).toString() + ";" + tabPerson.getValueAt(i, 6).toString() + "\n");
                }
                write.close();
            }
        } catch (Exception ex) {
            System.out.println(ex);
        }
    }//GEN-LAST:event_jButton22ActionPerformed

    private void netzActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_netzActionPerformed
        vNetzwerk.insertNetzInfo(netz.getSelectedIndex(), "all");
        suchergebniss.setText("");
    }//GEN-LAST:event_netzActionPerformed

    private void auswahlKActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_auswahlKActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_auswahlKActionPerformed
    private Funktionen fc = new Funktionen(this);
    private void jButton32ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton32ActionPerformed
        fc.auswalDateiZiel();
    }//GEN-LAST:event_jButton32ActionPerformed

    private void jButton33ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton33ActionPerformed
        fc.auswalDateiQuelle();
    }//GEN-LAST:event_jButton33ActionPerformed

    private void convActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_convActionPerformed
        fc.DateiSchreiben();
    }//GEN-LAST:event_convActionPerformed

    private void jButton34ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton34ActionPerformed
        fc.DateiAuswahl();
    }//GEN-LAST:event_jButton34ActionPerformed

    private void convAdminActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_convAdminActionPerformed
        fc.convertieren();
    }//GEN-LAST:event_convAdminActionPerformed

    private void jButton25ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton25ActionPerformed
        ArrayList<ArrayList<String>> DatenNewUser = (ArrayList<ArrayList<String>>) clients.netzwerkVerwaltung.getNewUser("Doku", "HauptBildschirm/initDaten",false).clone();
        tableUsers.getInfoTable.removeAllItems();
        ArrayList<String> info = new ArrayList<String>();
        for (int i = 0; i < DatenNewUser.size(); i++) {
            info.add(DatenNewUser.get(i).get(3).toString());
            info.add(DatenNewUser.get(i).get(13).toString().toString().split("#")[3]);
            info.add(DatenNewUser.get(i).get(1).toString());
            info.add(DatenNewUser.get(i).get(2).toString());
            info.add(DatenNewUser.get(i).get(7).toString());
            if (DatenNewUser.get(i).get(11).toString().toString().split("#")[0].equals("1")) {
                info.add(DatenNewUser.get(i).get(11).toString().toString().split("#")[2]);
            } else {
                info.add("--");
            }

            info.add(DatenNewUser.get(i).get(5).toString());
            info.add(DatenNewUser.get(i).get(6).toString());
            tableUsers.getInfoTable.addRow(info, "-1");
            info = new ArrayList<String>();
        }

    }//GEN-LAST:event_jButton25ActionPerformed

    private void jButton26ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton26ActionPerformed
        addAdressbuch ad = new addAdressbuch(clients, null, null);
        ad.newNetzAdresse();
    }//GEN-LAST:event_jButton26ActionPerformed

    private void enterRemote(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_enterRemote
        try {
            Process p1;
            String[] netzInfo = remote.getText().split("\\.");
            String netzSub = subnetz.getText().split("\\.")[0] + "." + subnetz.getText().split("\\.")[1] + "." + subnetz.getText().split("\\.")[2] + ".";
            if (netzInfo.length > 2) {
                p1 = Runtime.getRuntime().exec("C:/WINDOWS/system32/cmd.exe /C start mstsc /v: " + remote.getText().toString());
            } else {

                p1 = Runtime.getRuntime().exec("C:/WINDOWS/system32/cmd.exe /C start mstsc /v: " + netzSub + remote.getText().toString());
            }
        } catch (IOException ex) {
        }
    }//GEN-LAST:event_enterRemote

    private void getInRemote(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_getInRemote
        remote.setText("");
        remote.setForeground(Color.BLACK);
    }//GEN-LAST:event_getInRemote

    private void remoteFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_remoteFocusLost
        remote.setText(" - Remote Zugriff -");
        remote.setForeground(Color.LIGHT_GRAY);
    }//GEN-LAST:event_remoteFocusLost

    private void pingTestActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_pingTestActionPerformed
        try {
            Process p1;
            String[] netzInfo = pingTest.getText().split("\\.");
            String netzSub = subnetz.getText().split("\\.")[0] + "." + subnetz.getText().split("\\.")[1] + "." + subnetz.getText().split("\\.")[2] + ".";
            if (netzInfo.length > 2) {
                p1 = Runtime.getRuntime().exec("C:/WINDOWS/system32/cmd.exe /C start ping " + pingTest.getText().toString() + " -t");
            } else {

                p1 = Runtime.getRuntime().exec("C:/WINDOWS/system32/cmd.exe /C start ping " + netzSub + pingTest.getText().toString() + " -t");
            }
        } catch (IOException ex) {
        }
    }//GEN-LAST:event_pingTestActionPerformed

    private void pingTestFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_pingTestFocusGained
        pingTest.setText("");
        pingTest.setForeground(Color.BLACK);
    }//GEN-LAST:event_pingTestFocusGained

    private void pingTestFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_pingTestFocusLost
        pingTest.setText(" - Ping -");
        pingTest.setForeground(Color.LIGHT_GRAY);
    }//GEN-LAST:event_pingTestFocusLost
    private JPanel panel;

    private void jButton27ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton27ActionPerformed
        // new newEmail(KontoInfo.get(IDKI), clients);
    }//GEN-LAST:event_jButton27ActionPerformed

    private void jButton15ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton15ActionPerformed
        saveBaum();
    }//GEN-LAST:event_jButton15ActionPerformed

    private void jButton13ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton13ActionPerformed
        addBaum();        // TODO add your handling code here:
    }//GEN-LAST:event_jButton13ActionPerformed

    private void NetzStandortActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_NetzStandortActionPerformed
        vNetzwerk.initNetzInfo();
        suchergebniss.setText("");
    }//GEN-LAST:event_NetzStandortActionPerformed

   
    private void searchIPPannel1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_searchIPPannel1ActionPerformed
        vNetzwerk.searchIPPanel();
    }//GEN-LAST:event_searchIPPannel1ActionPerformed

    private void searchIPPannel1FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_searchIPPannel1FocusGained
        searchIPPannel1.setText("");
        searchIPPannel1.setForeground(Color.BLACK);
    }//GEN-LAST:event_searchIPPannel1FocusGained

    private void searchIPPannel1FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_searchIPPannel1FocusLost
        searchIPPannel1.setText(" - All Suche - ");
        searchIPPannel1.setForeground(Color.LIGHT_GRAY);
    }//GEN-LAST:event_searchIPPannel1FocusLost

    private void vncActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_vncActionPerformed
        try {
            Process p1;
            String[] netzInfo = vnc.getText().split("\\.");
            String netzSub = subnetz.getText().split("\\.")[0] + "." + subnetz.getText().split("\\.")[1] + "." + subnetz.getText().split("\\.")[2] + ".";
            if (netzInfo.length > 1) {
                p1 = Runtime.getRuntime().exec("C:/WINDOWS/system32/cmd.exe /C start C:/\"Program Files (x86)\"/RealVNC/VNC4/vncviewer.exe " + vnc.getText());
            } else {
                p1 = Runtime.getRuntime().exec("C:/WINDOWS/system32/cmd.exe /C start C:/\"Program Files (x86)\"/RealVNC/VNC4/vncviewer.exe " + netzSub + "" + vnc.getText());
            }

        } catch (Exception ex) {
        }
    }//GEN-LAST:event_vncActionPerformed

    private void vncFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_vncFocusGained
        vnc.setText("");
        vnc.setForeground(Color.BLACK);
    }//GEN-LAST:event_vncFocusGained

    private void vncFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_vncFocusLost
        vnc.setText(" - VNC Zugriff -");
        vnc.setForeground(Color.LIGHT_GRAY);
    }//GEN-LAST:event_vncFocusLost

    private void vnc1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_vnc1ActionPerformed
        try {
            Process p1;
            String[] netzInfo = vnc1.getText().split("\\.");
            String netzSub = subnetz.getText().split("\\.")[0] + "." + subnetz.getText().split("\\.")[1] + "." + subnetz.getText().split("\\.")[2] + ".";
            if (netzInfo.length > 1) {
                p1 = Runtime.getRuntime().exec("C:/WINDOWS/system32/cmd.exe /C \"C:\\Program Files\\Internet Explorer\\iexplore.exe\" " + vnc1.getText());
            } else {
                p1 = Runtime.getRuntime().exec("C:/WINDOWS/system32/cmd.exe /C \"C:\\Program Files\\Internet Explorer\\iexplore.exe\" " + netzSub + "" + vnc1.getText());
            }

        } catch (Exception ex) {
        }
    }//GEN-LAST:event_vnc1ActionPerformed

    private void vnc1FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_vnc1FocusGained
        vnc1.setText("");
        vnc1.setForeground(Color.BLACK);
    }//GEN-LAST:event_vnc1FocusGained

    private void vnc1FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_vnc1FocusLost
        vnc1.setText(" - Browser Anzeige -");
        vnc1.setForeground(Color.LIGHT_GRAY);
    }//GEN-LAST:event_vnc1FocusLost

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed

        vNetzwerk.sortierenNachIP(tableIPPanel);


    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton18ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton18ActionPerformed
        try {
            Process p1;
            String netzInfo = freieIP.getSelectedItem().toString();
            String netzSub = subnetz.getText().split("\\.")[0] + "." + subnetz.getText().split("\\.")[1] + "." + subnetz.getText().split("\\.")[2] + ".";

            p1 = Runtime.getRuntime().exec("C:/WINDOWS/system32/cmd.exe /C start ping " + netzSub + netzInfo + " -t");

        } catch (IOException ex) {
        }
    }//GEN-LAST:event_jButton18ActionPerformed

    private void Scan(int start, int end, String netz) {
        ArrayList<String> tempSCANINFO;
        for (int i = start; i < end; i++) {
            anz++;
            IPDaten = netz + i;
            tempSCANINFO = new ArrayList<String>();
            tempSCANINFO.add(IPDaten);
            tempSCANINFO.add("");
            tempSCANINFO.add("");
            tempSCANINFO.add("");
            tableIPScan.getInfoTable.addRow(tempSCANINFO, "-1");
            threadsVerwaltung.execute(new Runnable() {

                @Override
                public void run() {
                    anzIP++;
                    try {
                        String IPName = tableIPScan.getInfoTable.getValueAt(anzIP, 0).toString();
                        HashMap temp = clients.netzInfo(IPName);
                        if (temp != null) {
                            if (!temp.isEmpty()) {
                                for (int a = 0; a < tableIPScan.getInfoTable.getRowCount(); a++) {
                                    if (tableIPScan.getInfoTable.getValueAt(a, 0).equals(IPName)) {
                                        tableIPScan.getInfoTable.setValueAt(temp.get("Name").toString(), a, 1);
                                        tableIPScan.getInfoTable.setValueAt(temp.get("Dauer").toString(), a, 2);
                                        tableIPScan.getInfoTable.setValueAt(temp.get("Status").toString(), a, 3);
                                    }
                                }
                            }
                        }
                    } catch (Exception e) {
                    }
                    anz--;
                }
            });
            if (anz > 5) {
                SearchIP.setString("please wait.. (" + netz + (i - 5) + " - " + i + ")");
                while (true) {
                    if (anz == 0) {
                        break;
                    }
                };
                threadsVerwaltung.shutdown();
                while (!threadsVerwaltung.isShutdown());
                threadsVerwaltung = Executors.newFixedThreadPool(5);
            }
        }
    }

    private void jButton19ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton19ActionPerformed
        anzIP = -1;
        SearchIP.setString("please wait...");
        SearchIP.setIndeterminate(true);
        new Thread(new Runnable() {

            @Override
            public void run() {
                try {
                    jButton19.setEnabled(false);
                    tableIPScan.getInfoTable.removeAllItems();
                    String[] tmpstr = ipstart.getText().split("\\.");
                    int st = tmpstr.length;
                    ips = new int[st];
                    for (int i = 0; i < st; i++) {
                        ips[i] = Integer.parseInt(tmpstr[i]);
                    }

                    String[] tmpend = ipend.getText().split("\\.");
                    int end = tmpend.length;
                    ipe = new int[end];
                    for (int i = 0; i < end; i++) {
                        ipe[i] = Integer.parseInt(tmpend[i]);
                    }
                    while (true) {
                        if (ips[0] == ipe[0]) {
                            if (ips[1] == ipe[1]) {
                                if (ips[2] == ipe[2]) {
                                    Scan(ips[3], ipe[3] + 1, ips[0] + "." + ips[1] + "." + ips[2] + ".");
                                    break;
                                } else {
                                    Scan(1, 255, ips[0] + "." + ips[1] + "." + ips[2] + ".");
                                    ips[2]++;
                                }
                            } else {
                                Scan(1, 255, ips[0] + "." + ips[1] + "." + ips[2] + ".");
                                ips[1]++;
                            }
                        } else {
                            Scan(1, 255, ips[0] + "." + ips[1] + "." + ips[2] + ".");
                            ips[0]++;
                        }
                    }

                    threadsVerwaltung.shutdown();
                    while (!threadsVerwaltung.isTerminated());
                    jButton19.setEnabled(true);
                    SearchIP.setString("search");
                    SearchIP.setIndeterminate(false);
                    IPScanBDeath.setEnabled(true);
                    IPScanLive.setEnabled(true);
                    IPScanBerechnung();
                } catch (Exception e) {
                    SearchIP.setIndeterminate(false);
                    System.out.println(e);
                }
            }
        }).start();
        //titlesIPScan = new String[]{"ID", "IP", "Hostname","Dauer", "Status"};
    }//GEN-LAST:event_jButton19ActionPerformed
    private ArrayList<ArrayList<String>> PortINFOSAktiv = new ArrayList<ArrayList<String>>();
    private ArrayList<ArrayList<String>> PortINFOSinAktiv = new ArrayList<ArrayList<String>>();

    private void IPScanBerechnung() {
        anzahlLiveIPScan = 0;
        anzahlGesamtIPScan = 0;
        for (int a = 0; a < tableIPScan.getInfoTable.getRowCount(); a++) {
            anzahlGesamtIPScan++;
            if (!tableIPScan.getInfoTable.getValueAt(a, 1).equals("???")) {
                anzahlLiveIPScan++;
            }
        }
        IPScanAliv.setText(anzahlLiveIPScan + "");
        IPScanDeath.setText((anzahlGesamtIPScan - anzahlLiveIPScan) + "");
        IPScanAnz.setText(anzahlGesamtIPScan + "");
    }

    private void jButton23ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton23ActionPerformed
        PortINFOSAktiv = new ArrayList<ArrayList<String>>();
        PortINFOSinAktiv = new ArrayList<ArrayList<String>>();
        portVisible.setEnabled(false);
        portOpen.setEnabled(false);
        tablePortScan.getInfoTable.removeAllItems();
        jButton23.setEnabled(false);

        anzIPPort = -1;

        new Thread(new Runnable() {

            @Override
            public void run() {

                try {
                    PortSearchBar.setMinimum(Integer.parseInt(pstart.getText()));
                    PortSearchBar.setMaximum(Integer.parseInt(pend.getText()));
                    for (int i = Integer.parseInt(pstart.getText()); i < Integer.parseInt(pend.getText()); i++) {
                        PortSearchBar.setString("please wait... (Port Scan: " + i + ")");
                        PortSearchBar.setValue(i);
                        String IP = ipport.getText();
                        try {

                            Socket ServerSok = new Socket(IP, i);
                            if (portOpen.isSelected()) {
                                ArrayList<String> tempDatenPORTSCAN = new ArrayList<String>();
                                tempDatenPORTSCAN.add("" + i);
                                tempDatenPORTSCAN.add("aktiv");
                                tablePortScan.getInfoTable.addRow(tempDatenPORTSCAN, "-1");
                            }
                            ServerSok.close();
                        } catch (Exception e) {
                            if (portVisible.isSelected()) {
                                ArrayList<String> tempDatenPORTSCAN = new ArrayList<String>();
                                tempDatenPORTSCAN.add("" + i);
                                tempDatenPORTSCAN.add("--");
                                tablePortScan.getInfoTable.addRow(tempDatenPORTSCAN, "-1");
                            }

                        }
                    }
                    PortSearchBar.setString("Search Start");
                    jButton23.setEnabled(true);
                    portVisible.setEnabled(true);
                    portOpen.setEnabled(true);
                    PortSearchBar.setString("Search Start");
                } catch (Exception e) {
                    jButton23.setEnabled(true);
                    portVisible.setEnabled(true);
                    portOpen.setEnabled(true);
                    PortSearchBar.setString("Search Start");
                }
            }
        }).start();


    }//GEN-LAST:event_jButton23ActionPerformed

    private void fipActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_fipActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_fipActionPerformed

    private void listFTPFiles(String Direktory) {
        FTPProtokoll.append("(" + new SimpleDateFormat("dd.MM.yyyy HH:MM").format(new Date()) + ") FTP Datenabruf (" + Direktory + ")...\n");
        tableFTPClient.getInfoTable.removeAllItems();
        try {
            if (!Direktory.equals("root")) {
                clientFTP.cwd(Direktory);
            }
            ArrayList<String> datenTEMPFTP;
            FTPFile[] dateien = clientFTP.listFiles();
            for (int i = 0; i < dateien.length; i++) {
                if (!dateien[i].getName().equals(".")) {
                    datenTEMPFTP = new ArrayList<String>();
                    datenTEMPFTP.add(dateien[i].getName());
                    if (dateien[i].isDirectory()) {
                        datenTEMPFTP.add("Ordner");
                    } else {
                        datenTEMPFTP.add("File");
                    }
                    datenTEMPFTP.add(dateien[i].getSize() + " Byte");
                    datenTEMPFTP.add(new SimpleDateFormat("dd.MM.yyyy HH:MM").format(new Date(dateien[i].getTimestamp().getTimeInMillis())));
                    tableFTPClient.getInfoTable.addRow(datenTEMPFTP, "-1");
                }
            }
            FTPProtokoll.append("(" + new SimpleDateFormat("dd.MM.yyyy HH:MM").format(new Date()) + ") FTP Datenabruf fertig gestellt!\n");
            //{"Name", "Typ","Größe", "Datum"},
        } catch (Exception ex) {
            FTPProtokoll.append("(" + new SimpleDateFormat("dd.MM.yyyy HH:MM").format(new Date()) + ") FTP Datenabruf fehlgeschlagen!\n");
        }
    }
    private void jButton24ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton24ActionPerformed

        if (connectFTP(FTPUser.getText(), FTPPW.getText(), FTPServer.getText(), Integer.parseInt(FTPPort.getText())));
        listFTPFiles("root");
    }//GEN-LAST:event_jButton24ActionPerformed
    private void readDatei() {
        try {
            ArrayList<String> tempSSH;
            BufferedReader read = new BufferedReader(new FileReader("Config/SSHServer.sh"));
            String datei = read.readLine();
            while (datei != null) {
                String[] tempFil = datei.split(";");
                tempSSH = new ArrayList<String>();
                tempSSH.add(tempFil[0]);
                tempSSH.add(tempFil[1]);
                tempSSH.add(tempFil[2]);
                if (tempFil.length > 3) {
                    tempSSH.add(tempFil[3]);
                } else {
                    tempSSH.add("");
                }
                tableSSHClient.getInfoTable.addRow(tempSSH, "-1");
                datei = read.readLine();
            }
        } catch (Exception e) {
            System.out.println();
        }
    }
    private void jButton36ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton36ActionPerformed
        ArrayList<ArrayList<String>> tempSSHALL = new ArrayList<ArrayList<String>>();

        ArrayList<String> tempSSH = new ArrayList<String>();
        tempSSH.add(SSHServer.getText());
        tempSSH.add(SSHPort.getText());
        tempSSH.add(SSHUser.getText());
        tempSSH.add(SSHPW.getText());
        tableSSHClient.getInfoTable.addRow(tempSSH, "-1");
        tempSSHALL.add(tempSSH);
        SSHServer.setText("");
        SSHPort.setText("");
        SSHUser.setText("");
        SSHPW.setText("");
        try {
            BufferedReader read = new BufferedReader(new FileReader("Config/SSHServer.sh"));
            String datei = read.readLine();
            while (datei != null) {
                String[] tempFil = datei.split(";");
                tempSSH = new ArrayList<String>();
                tempSSH.add(tempFil[0]);
                tempSSH.add(tempFil[1]);
                tempSSH.add(tempFil[2]);
                if (tempFil.length > 3) {
                    tempSSH.add(tempFil[3]);
                } else {
                    tempSSH.add("");
                }
                tempSSHALL.add(tempSSH);
                datei = read.readLine();
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        try {
            BufferedWriter write = new BufferedWriter(new FileWriter("Config/SSHServer.sh"));
            for (int i = 0; i < tempSSHALL.size(); i++) {
                write.write(tempSSHALL.get(i).get(0) + ";" + tempSSHALL.get(i).get(1) + ";" + tempSSHALL.get(i).get(2) + ";" + tempSSHALL.get(i).get(3) + "\n");
            }
            write.close();
        } catch (Exception ex) {
            System.out.println(ex);
        }
    }//GEN-LAST:event_jButton36ActionPerformed

    private void jButton35ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton35ActionPerformed
        newSSHConnection(tabSSHClient.getSelectedRow(), "");
    }//GEN-LAST:event_jButton35ActionPerformed

    private void newSSHConnection(final int row, final String Server) {
        new Thread(new Runnable() {

            @Override
            public void run() {
                Properties props = new Properties();
                if (row > -1) {
                    SSHClient ssh2 = new SSHClient(props, tabSSHClient.getValueAt(tabSSHClient.getSelectedRow(), 0).toString(),
                            tabSSHClient.getValueAt(tabSSHClient.getSelectedRow(), 1).toString(),
                            tabSSHClient.getValueAt(tabSSHClient.getSelectedRow(), 2).toString(),
                            tabSSHClient.getValueAt(tabSSHClient.getSelectedRow(), 3).toString());
                    ssh2.run();
                } else if (row == -2) {
                    SSHClient ssh2 = new SSHClient(props, Server, "", null, "");
                    ssh2.run();
                } else {
                    SSHClient ssh2 = new SSHClient(props, "", "", null, "");
                    ssh2.run();
                }
            }
        }).start();
    }
    private void jButton37ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton37ActionPerformed
        tableSSHClient.getInfoTable.removeRowAt(tabSSHClient.getSelectedRow());
        ArrayList<ArrayList<String>> tempSSHALL = new ArrayList<ArrayList<String>>();
        ArrayList<String> tempSSH;
        for (int i = 0; i < tableSSHClient.getInfoTable.getRowCount(); i++) {
            tempSSH = new ArrayList<String>();
            tempSSH.add(tableSSHClient.getInfoTable.getValueAt(i, 0).toString());
            tempSSH.add(tableSSHClient.getInfoTable.getValueAt(i, 1).toString());
            tempSSH.add(tableSSHClient.getInfoTable.getValueAt(i, 2).toString());
            tempSSH.add(tableSSHClient.getInfoTable.getValueAt(i, 3).toString());
            tempSSHALL.add(tempSSH);
        }
        try {
            BufferedWriter write = new BufferedWriter(new FileWriter("Config/SSHServer.sh"));
            for (int i = 0; i < tempSSHALL.size(); i++) {
                write.write(tempSSHALL.get(i).get(0) + ";" + tempSSHALL.get(i).get(1) + ";" + tempSSHALL.get(i).get(2) + ";" + tempSSHALL.get(i).get(3) + "\n");
            }
            write.close();
        } catch (Exception ex) {
        }
    }//GEN-LAST:event_jButton37ActionPerformed

    private void remote1enterRemote(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_remote1enterRemote
        newSSHConnection(-2, remote1.getText());
    }//GEN-LAST:event_remote1enterRemote

    private void remote1getInRemote(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_remote1getInRemote
        remote1.setText("");
        remote1.setForeground(Color.BLACK);
    }//GEN-LAST:event_remote1getInRemote

    private void remote1FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_remote1FocusLost
        remote1.setText(" - SSH2 Zugriff -");
        remote1.setForeground(Color.LIGHT_GRAY);
    }//GEN-LAST:event_remote1FocusLost

    private void portVisibleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_portVisibleActionPerformed

        if (portVisible.isSelected()) {
            for (int i = 0; i < PortINFOSinAktiv.size(); i++) {
                tablePortScan.getInfoTable.addRow(PortINFOSinAktiv.get(i), "-1");
            }
        } else {
            for (int i = 0; i < tablePortScan.getInfoTable.getRowCount(); i++) {
                try {
                    if (tablePortScan.getInfoTable.getValueAt(i, 1).equals("--")) {
                        tablePortScan.getInfoTable.removeRowAt(i);
                        i--;
                    }
                } catch (Exception e) {
                }
            }
        }

    }//GEN-LAST:event_portVisibleActionPerformed

    private void portOpenActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_portOpenActionPerformed

        if (portOpen.isSelected()) {
            for (int i = 0; i < PortINFOSAktiv.size(); i++) {
                tablePortScan.getInfoTable.addRow(PortINFOSAktiv.get(i), "-1");
            }
        } else {
            for (int i = 0; i < tabPortScan.getRowCount(); i++) {
                if (tablePortScan.getInfoTable.getValueAt(i, 1).equals("in Use")) {
                    tablePortScan.getInfoTable.removeRowAt(i);
                }
            }
        }

    }//GEN-LAST:event_portOpenActionPerformed
    ArrayList<ArrayList<String>> IPScanALLFail = new ArrayList<ArrayList<String>>();
    ArrayList<ArrayList<String>> IPScanALLAlive = new ArrayList<ArrayList<String>>();
    ArrayList<ArrayList<String>> IPScanALLAll = new ArrayList<ArrayList<String>>();
    Boolean show = false;
    private void IPScanLiveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_IPScanLiveActionPerformed

        if (!show) {
            gehtIPScanInhalt();
            IPScanAll.setEnabled(true);
            show = true;
        }
        tableIPScan.getInfoTable.removeAllItems();
        for (int i = 0; i < IPScanALLAlive.size(); i++) {
            tableIPScan.getInfoTable.addRow(IPScanALLAlive.get(i), "-1");
        }
    }//GEN-LAST:event_IPScanLiveActionPerformed
    private void gehtIPScanInhalt() {
        ArrayList<String> IPScanTEMP;
        for (int a = 0; a < tableIPScan.getInfoTable.getRowCount(); a++) {
            if (tableIPScan.getInfoTable.getValueAt(a, 1).equals("???")) {
                IPScanTEMP = new ArrayList<String>();
                IPScanTEMP.add(tableIPScan.getInfoTable.getValueAt(a, 0).toString());
                IPScanTEMP.add(tableIPScan.getInfoTable.getValueAt(a, 1).toString());
                IPScanTEMP.add(tableIPScan.getInfoTable.getValueAt(a, 2).toString());
                IPScanTEMP.add(tableIPScan.getInfoTable.getValueAt(a, 3).toString());
                IPScanALLFail.add(IPScanTEMP);
            } else {
                IPScanTEMP = new ArrayList<String>();
                IPScanTEMP.add(tableIPScan.getInfoTable.getValueAt(a, 0).toString());
                IPScanTEMP.add(tableIPScan.getInfoTable.getValueAt(a, 1).toString());
                IPScanTEMP.add(tableIPScan.getInfoTable.getValueAt(a, 2).toString());
                IPScanTEMP.add(tableIPScan.getInfoTable.getValueAt(a, 3).toString());
                IPScanALLAlive.add(IPScanTEMP);
            }
            IPScanALLAll.add(IPScanTEMP);
        }
    }
    private void IPScanBDeathActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_IPScanBDeathActionPerformed

        if (!show) {
            IPScanAll.setEnabled(true);
            gehtIPScanInhalt();

            show = true;
        }
        tableIPScan.getInfoTable.removeAllItems();
        for (int i = 0; i < IPScanALLFail.size(); i++) {
            tableIPScan.getInfoTable.addRow(IPScanALLFail.get(i), "-1");
        }

    }//GEN-LAST:event_IPScanBDeathActionPerformed

    private void IPScanAllActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_IPScanAllActionPerformed
        if (!show) {

            gehtIPScanInhalt();

            show = true;
        }
        tableIPScan.getInfoTable.removeAllItems();
        for (int i = 0; i < IPScanALLAll.size(); i++) {
            tableIPScan.getInfoTable.addRow(IPScanALLAll.get(i), "-1");
        }
    }//GEN-LAST:event_IPScanAllActionPerformed

    private void jButton38ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton38ActionPerformed
     try{
        vFunktion.getArbeitsplatz();        
     }catch(Exception e){         
     }
    }//GEN-LAST:event_jButton38ActionPerformed

    private void jButton39ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton39ActionPerformed
      try{
          vFunktion.getBrowser("");
      }catch(Exception e){
          
      }
    }//GEN-LAST:event_jButton39ActionPerformed

    private void ssDaten(JButton b){
        if (b.getText().equalsIgnoreCase("new")){
            vFunktion.addSchnellstart(b.getName());
        }else{
            vFunktion.startSchnellstart(b.getName());
        }
    }
    
    private void ssDatenRemove(String d){
       Object[] options = {"Yes, please",
            "No, thanks"};
        int n = JOptionPane.showOptionDialog(new JFrame(),
                " Möchten Sie wirklich löschen?",
                "Schnellstart Delete",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.INFORMATION_MESSAGE,
                null, //do not use a custom Icon
                options, //the titles of buttons
                options[0]);
        if (n == 0) { 
            vFunktion.delSchnellstart(d);
        }
        
    }
    
    private void ss1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ss1ActionPerformed
       ssDaten(ss1);
    }//GEN-LAST:event_ss1ActionPerformed

    private void ss2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ss2ActionPerformed
        ssDaten(ss2);
    }//GEN-LAST:event_ss2ActionPerformed

    private void ss3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ss3ActionPerformed
        ssDaten(ss3);
    }//GEN-LAST:event_ss3ActionPerformed

    private void ss4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ss4ActionPerformed
        ssDaten(ss4);
    }//GEN-LAST:event_ss4ActionPerformed

    private void ss5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ss5ActionPerformed
        ssDaten(ss5);
    }//GEN-LAST:event_ss5ActionPerformed

    private void ss6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ss6ActionPerformed
        ssDaten(ss6);
    }//GEN-LAST:event_ss6ActionPerformed

    private void ss7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ss7ActionPerformed
        ssDaten(ss7);
    }//GEN-LAST:event_ss7ActionPerformed

    private void ss8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ss8ActionPerformed
       ssDaten(ss8);
    }//GEN-LAST:event_ss8ActionPerformed

    private void ss9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ss9ActionPerformed
        ssDaten(ss9);
    }//GEN-LAST:event_ss9ActionPerformed

    private void ss10ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ss10ActionPerformed
       ssDaten(ss10);
    }//GEN-LAST:event_ss10ActionPerformed

    private void ss1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_ss1MouseClicked
        if (evt.getButton() == 3){
            
          ssDatenRemove(ss1.getName());  
        }
    }//GEN-LAST:event_ss1MouseClicked

    // Variables declaration - do not modify//GEN-BEGIN:variables
    protected javax.swing.JTextField DHCPNetz;
    private javax.swing.JTextField FTPPW;
    private javax.swing.JTextField FTPPort;
    private javax.swing.JTextArea FTPProtokoll;
    private javax.swing.JTextField FTPServer;
    private javax.swing.JTextField FTPUser;
    protected javax.swing.JComboBox GruppeIPPanel;
    protected javax.swing.JComboBox GruppeProtokoll;
    private javax.swing.JTabbedPane HauptPannel;
    protected javax.swing.JTextField IDAdressbuchSelected;
    private javax.swing.JTextField IPScanAliv;
    private javax.swing.JButton IPScanAll;
    private javax.swing.JTextField IPScanAnz;
    private javax.swing.JButton IPScanBDeath;
    private javax.swing.JTextField IPScanDeath;
    private javax.swing.JButton IPScanLive;
    private javax.swing.JPanel Info;
    protected javax.swing.JTextField Maske;
    protected javax.swing.JTextField Name;
    public javax.swing.JTextField Name1;
    protected javax.swing.JTextField Netz;
    protected javax.swing.JComboBox NetzStandort;
    private javax.swing.JPanel PannelAll;
    private javax.swing.JProgressBar PortSearchBar;
    private javax.swing.JScrollPane SCIPScan;
    private javax.swing.JTextField SSHPW;
    private javax.swing.JTextField SSHPort;
    private javax.swing.JTextField SSHServer;
    private javax.swing.JTextField SSHUser;
    private javax.swing.JProgressBar SearchIP;
    protected javax.swing.JCheckBox allStoerungen;
    protected javax.swing.JTextField auftragsNr;
    public javax.swing.JComboBox auswahl;
    protected javax.swing.JComboBox auswahlK;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JTextField city;
    protected javax.swing.JButton conv;
    public javax.swing.JButton convAdmin;
    private javax.swing.JTextField dN;
    protected javax.swing.JTextField desti;
    protected javax.swing.JTextField dhcp;
    private javax.swing.JTextField eMail;
    protected javax.swing.JTextField fax;
    private javax.swing.JTextField faxNummer;
    protected javax.swing.JTextArea fehlerProtokoll;
    protected javax.swing.JTextField fip;
    protected javax.swing.JTextField fip1;
    protected javax.swing.JTextPane fip2;
    protected javax.swing.JTextField fip3;
    protected javax.swing.JTextField fip4;
    protected javax.swing.JTextField fip5;
    protected javax.swing.JTextField firmenNamen;
    protected javax.swing.JTextField freeIP;
    protected javax.swing.JComboBox freieIP;
    protected javax.swing.JTextField handy;
    protected javax.swing.JTextArea hist;
    private javax.swing.JLabel infoP;
    protected javax.swing.JTextArea inhalt;
    private javax.swing.JTextField ipend;
    private javax.swing.JTextField ipport;
    private javax.swing.JTextField ipstart;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton10;
    private javax.swing.JButton jButton11;
    private javax.swing.JButton jButton12;
    private javax.swing.JButton jButton13;
    private javax.swing.JButton jButton14;
    private javax.swing.JButton jButton15;
    private javax.swing.JButton jButton16;
    private javax.swing.JButton jButton17;
    private javax.swing.JButton jButton18;
    private javax.swing.JButton jButton19;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton20;
    private javax.swing.JButton jButton21;
    private javax.swing.JButton jButton22;
    private javax.swing.JButton jButton23;
    private javax.swing.JButton jButton24;
    private javax.swing.JButton jButton25;
    private javax.swing.JButton jButton26;
    private javax.swing.JButton jButton27;
    private javax.swing.JButton jButton28;
    private javax.swing.JButton jButton29;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton30;
    private javax.swing.JButton jButton31;
    protected javax.swing.JButton jButton32;
    protected javax.swing.JButton jButton33;
    private javax.swing.JButton jButton34;
    private javax.swing.JButton jButton35;
    private javax.swing.JButton jButton36;
    private javax.swing.JButton jButton37;
    protected javax.swing.JButton jButton38;
    private javax.swing.JButton jButton39;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JButton jButton7;
    private javax.swing.JButton jButton8;
    private javax.swing.JButton jButton9;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel31;
    private javax.swing.JLabel jLabel32;
    private javax.swing.JLabel jLabel33;
    private javax.swing.JLabel jLabel34;
    private javax.swing.JLabel jLabel35;
    private javax.swing.JLabel jLabel36;
    private javax.swing.JLabel jLabel37;
    private javax.swing.JLabel jLabel38;
    private javax.swing.JLabel jLabel39;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel40;
    private javax.swing.JLabel jLabel41;
    private javax.swing.JLabel jLabel42;
    private javax.swing.JLabel jLabel43;
    private javax.swing.JLabel jLabel44;
    private javax.swing.JLabel jLabel45;
    private javax.swing.JLabel jLabel46;
    private javax.swing.JLabel jLabel47;
    private javax.swing.JLabel jLabel48;
    private javax.swing.JLabel jLabel49;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel50;
    private javax.swing.JLabel jLabel51;
    private javax.swing.JLabel jLabel52;
    private javax.swing.JLabel jLabel53;
    private javax.swing.JLabel jLabel54;
    private javax.swing.JLabel jLabel55;
    private javax.swing.JLabel jLabel56;
    private javax.swing.JLabel jLabel57;
    private javax.swing.JLabel jLabel58;
    private javax.swing.JLabel jLabel59;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel60;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu3;
    private javax.swing.JMenu jMenu4;
    private javax.swing.JMenu jMenu5;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuBar jMenuBar2;
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JMenuItem jMenuItem3;
    private javax.swing.JMenuItem jMenuItem4;
    private javax.swing.JMenuItem jMenuItem6;
    private javax.swing.JMenuItem jMenuItem7;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel13;
    private javax.swing.JPanel jPanel14;
    private javax.swing.JPanel jPanel15;
    private javax.swing.JPanel jPanel16;
    private javax.swing.JPanel jPanel17;
    private javax.swing.JPanel jPanel18;
    private javax.swing.JPanel jPanel19;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel21;
    private javax.swing.JPanel jPanel22;
    private javax.swing.JPanel jPanel23;
    private javax.swing.JPanel jPanel24;
    private javax.swing.JPanel jPanel25;
    private javax.swing.JPanel jPanel26;
    private javax.swing.JPanel jPanel27;
    private javax.swing.JPanel jPanel28;
    private javax.swing.JPanel jPanel29;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel30;
    private javax.swing.JPanel jPanel31;
    private javax.swing.JPanel jPanel32;
    private javax.swing.JPanel jPanel33;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JScrollPane jScrollPane7;
    private javax.swing.JScrollPane jScrollPane8;
    private javax.swing.JSplitPane jSplitPane1;
    private javax.swing.JSplitPane jSplitPane2;
    private javax.swing.JSplitPane jSplitPane3;
    private javax.swing.JSplitPane jSplitPane4;
    private javax.swing.JSplitPane jSplitPane5;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTabbedPane jTabbedPane2;
    private javax.swing.JTabbedPane jTabbedPane3;
    private javax.swing.JTabbedPane jTabbedPane4;
    private javax.swing.JTabbedPane jTabbedPane5;
    private javax.swing.JTextArea jTextArea1;
    protected javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField10;
    private javax.swing.JTextField jTextField3;
    protected javax.swing.JTextField lapanz;
    protected javax.swing.JTextField maske;
    private javax.swing.JTextField mobile;
    private javax.swing.JTextField nachname;
    protected javax.swing.JComboBox netz;
    private javax.swing.JScrollPane newStoerung;
    private javax.swing.JScrollPane newUser;
    private javax.swing.JButton newUserB;
    protected javax.swing.JTextField ort;
    private javax.swing.JTabbedPane pannel;
    protected javax.swing.JTextField pcanz;
    private javax.swing.JTextField pend;
    private javax.swing.JTextField pingTest;
    protected javax.swing.JTextField plz;
    private javax.swing.JTextField plzLDAP;
    private javax.swing.JCheckBox portOpen;
    private javax.swing.JCheckBox portVisible;
    protected javax.swing.JTextField protokollEintrag;
    private javax.swing.JTextField pstart;
    private javax.swing.JTextField remote;
    private javax.swing.JTextField remote1;
    protected javax.swing.JComboBox sCProtokoll;
    private javax.swing.JScrollPane scAnhaenge;
    private javax.swing.JScrollPane scDatenProtokollierungNew;
    private javax.swing.JScrollPane scDatenProtokollierungOld;
    private javax.swing.JScrollPane scEmail;
    private javax.swing.JScrollPane scFTPClientDaten;
    private javax.swing.JScrollPane scFirma;
    private javax.swing.JScrollPane scIPPannel;
    private javax.swing.JScrollPane scInfo;
    private javax.swing.JScrollPane scLDAP;
    private javax.swing.JScrollPane scNetz;
    private javax.swing.JScrollPane scPerson;
    private javax.swing.JScrollPane scPort;
    private javax.swing.JScrollPane scSSHClient;
    private javax.swing.JScrollPane scServerProtokoll;
    private javax.swing.JScrollPane scUsers;
    private javax.swing.JScrollPane scVPNCDateien;
    private javax.swing.JScrollPane scftpcordner;
    private javax.swing.JTextField searchFirma;
    protected javax.swing.JTextField searchIPPannel1;
    protected javax.swing.JTextField searchUsers;
    protected javax.swing.JTextField serveranz;
    protected javax.swing.JTextField source;
    protected javax.swing.JButton ss1;
    protected javax.swing.JButton ss10;
    protected javax.swing.JButton ss2;
    protected javax.swing.JButton ss3;
    protected javax.swing.JButton ss4;
    protected javax.swing.JButton ss5;
    protected javax.swing.JButton ss6;
    protected javax.swing.JButton ss7;
    protected javax.swing.JButton ss8;
    protected javax.swing.JButton ss9;
    protected javax.swing.JTextField strasse;
    private javax.swing.JTextField street;
    protected javax.swing.JTextField subnetz;
    protected javax.swing.JLabel suchergebniss;
    protected javax.swing.JTextField tel;
    private javax.swing.JTextField telefon;
    public javax.swing.JTextArea text;
    public javax.swing.JTextArea text2;
    private javax.swing.JScrollPane treeEmail;
    private javax.swing.JButton userB;
    private javax.swing.JButton userS;
    protected javax.swing.JTextField useranz;
    private javax.swing.JTextField vnc;
    private javax.swing.JTextField vnc1;
    private javax.swing.JTextField vorname;
    protected javax.swing.JTextField ziel;
    // End of variables declaration//GEN-END:variables
}
