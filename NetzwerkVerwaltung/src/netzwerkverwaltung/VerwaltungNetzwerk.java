/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package netzwerkverwaltung;

import java.awt.event.MouseEvent;
import java.io.*;
import java.util.ArrayList;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTable;

/**
 *
 * @author manfred.fischer
 */
public class VerwaltungNetzwerk {

    private HauptBildschirm haupt;
    private ClientNetzwerk clients;
    protected String DatenNetzIDAuswahl;
    protected String IDSSelectet = "0";
    protected ServerProtokollDaten ServerProtokoll;
    protected ArrayList<ArrayList<String>> datenNetz, standortDaten, datenClientServer,datenHardware;
    private TabelFont tableServerProtokoll, tableNetz, tableIPPanel;
    private JTable tabIPPanel;
    private Thread importsThreads;
    private BufferedReader datenIMPORT;
    private wait warteSchleife = new wait();
    private IPPannel ip = new IPPannel(clients);

    public VerwaltungNetzwerk(HauptBildschirm h, ClientNetzwerk c, TabelFont tSP, TabelFont tN, TabelFont tIPP, JTable tabIP) {
        tableServerProtokoll = tSP;
        tabIPPanel = tabIP;
        tableNetz = tN;
        tableIPPanel = tIPP;
        haupt = h;
        clients = c;
    }

    protected void sortierenNachIP(TabelFont table) {

        ArrayList<ArrayList<String>> TempIP = new ArrayList<ArrayList<String>>();
        ArrayList<ArrayList<String>> TempIPSort = new ArrayList<ArrayList<String>>();
        ArrayList<ArrayList<String>> TempDHCP = new ArrayList<ArrayList<String>>();
        ArrayList<ArrayList<String>> TempKEINE = new ArrayList<ArrayList<String>>();
        ArrayList<ArrayList<String>> TempUSB = new ArrayList<ArrayList<String>>();
        ArrayList<ArrayList<String>> TempFREIEIP = new ArrayList<ArrayList<String>>();
        ArrayList<String> tempInfo;
        for (int i = 0; i < table.getInfoTable.getRowCount(); i++) {
            if (table.getInfoTable.getValueAt(i, 2).toString().equalsIgnoreCase("DHCP")) {
                tempInfo = new ArrayList<String>();
                for (int a = 0; a < table.getInfoTable.getColumnCount(); a++) {
                    tempInfo.add(table.getInfoTable.getValueAt(i, a).toString());
                }
                TempDHCP.add(tempInfo);
            } else if (table.getInfoTable.getValueAt(i, 2).toString().equalsIgnoreCase("KEINE")) {
                tempInfo = new ArrayList<String>();
                for (int a = 0; a < table.getInfoTable.getColumnCount(); a++) {
                    tempInfo.add(table.getInfoTable.getValueAt(i, a).toString());
                }
                TempKEINE.add(tempInfo);
            } else if (table.getInfoTable.getValueAt(i, 2).toString().equalsIgnoreCase("USB")) {
                tempInfo = new ArrayList<String>();
                for (int a = 0; a < table.getInfoTable.getColumnCount(); a++) {
                    tempInfo.add(table.getInfoTable.getValueAt(i, a).toString());
                }
                TempUSB.add(tempInfo);
            } else if (table.getInfoTable.getValueAt(i, 1).toString().equalsIgnoreCase("FREI")) {
                tempInfo = new ArrayList<String>();
                for (int a = 0; a < table.getInfoTable.getColumnCount(); a++) {
                    tempInfo.add(table.getInfoTable.getValueAt(i, a).toString());
                }
                TempFREIEIP.add(tempInfo);
            } else {
                tempInfo = new ArrayList<String>();
                for (int a = 0; a < table.getInfoTable.getColumnCount(); a++) {
                    tempInfo.add(table.getInfoTable.getValueAt(i, a).toString());
                }
                TempIP.add(tempInfo);
            }


        }
        table.getInfoTable.removeAllItems();
        int temp, check, kleinster;
        while (TempIP.size() > 0) {
            temp = Integer.parseInt(TempIP.get(0).get(2).split("\\.")[3]);
            check = 0;
            kleinster = 0;
            for (int i = 1; i < TempIP.size(); i++) {
                check = Integer.parseInt(TempIP.get(i).get(2).split("\\.")[3]);
                if (temp > check) {
                    temp = Integer.parseInt(TempIP.get(i).get(2).split("\\.")[3]);
                    kleinster = i;
                }
            }
            TempIPSort.add((ArrayList<String>) TempIP.get(kleinster).clone());
            TempIP.remove(kleinster);
        }

        for (int i = 0; i < TempIPSort.size(); i++) {
            table.getInfoTable.addRow(TempIPSort.get(i), "-1");
        }
        for (int i = 0; i < TempFREIEIP.size(); i++) {
            table.getInfoTable.addRow(TempFREIEIP.get(i), "-1");
        }
        for (int i = 0; i < TempDHCP.size(); i++) {
            table.getInfoTable.addRow(TempDHCP.get(i), "-1");
        }
        for (int i = 0; i < TempUSB.size(); i++) {
            table.getInfoTable.addRow(TempUSB.get(i), "-1");
        }
        for (int i = 0; i < TempKEINE.size(); i++) {
            table.getInfoTable.addRow(TempKEINE.get(i), "-1");
        }
    }

    protected void insertCSProtokoll() {
        tableServerProtokoll.getInfoTable.removeAllItems();
        if (haupt.sCProtokoll.getSelectedItem() != null) {
            for (int i = 0; i < datenHardware.size(); i++) {
                if (datenHardware.get(i).get(1).equals(haupt.sCProtokoll.getSelectedItem().toString())) {
                    IDSSelectet = datenHardware.get(i).get(0).toString();
                }
            }

            ServerProtokoll.getInfo(haupt.netz.getSelectedItem().toString(), IDSSelectet, haupt.GruppeProtokoll.getSelectedItem().toString());
            ArrayList<ArrayList<String>> ProtokollDaten = ServerProtokoll.getProtokollDaten;

            for (int i = 0; i < ProtokollDaten.size(); i++) {
                tableServerProtokoll.getInfoTable.addRow(ProtokollDaten.get(i), "1");
            }
        }
    }

    private void start() {
        importsThreads.start();
        warteSchleife.setVisible(true);
        haupt.setVisible(false);
        //doku.setVisible(false);
    }

    private void end() {
        warteSchleife.setVisible(false);
        importsThreads.interrupt();
        haupt.setVisible(true);
        insertNetzInfo(0, "all");
    }

    protected void importNetzwerk() {
        JFileChooser datei = new JFileChooser();
        File file;

        int getInfo = datei.showOpenDialog(datei);
        if (getInfo == JFileChooser.APPROVE_OPTION) {
            file = datei.getSelectedFile();
            try {
                datenIMPORT = new BufferedReader(new FileReader(file));
                importsThreads = new Thread(new Runnable() {

                    @Override
                    public void run() {
                        try {
                            String Daten;
                            Daten = datenIMPORT.readLine();
                            while (Daten != null) {
                                String[] temp = Daten.split(";");
                                String Name = checkNetzDoppelt(temp[0]);
                                if (Name != null) {
                                    if (temp.length >= 3) {
                                        clients.send("1000" + clients.trennzeichen + Name + clients.trennzeichen + temp[1] + clients.trennzeichen + temp[2] + clients.trennzeichen + temp[3] + clients.trennzeichen + temp[4] + clients.trennzeichen + temp[5] + clients.trennzeichen + temp[6]);        //db.addNetzwerk(temp[0], temp[1], temp[2], temp[3] + ";" + temp[4],temp[5]);
                                    }
                                }
                                Daten = datenIMPORT.readLine();
                            }
                            clients.send("1000" + clients.trennzeichen + "END");
                            end();
                        } catch (IOException ex) {
                            clients.FehlerBerichtSchreiben("impNetz: " + ex, "Log/error.log",haupt);
                        }
                    }
                });
                start();
            } catch (FileNotFoundException ex) {
                clients.FehlerBerichtSchreiben("impNetz: " + ex, "Log/error.log",haupt);
            }

        }
    }

    protected void importStandort() {
        JFileChooser datei = new JFileChooser();
        File file;

        int getInfo = datei.showOpenDialog(datei);
        if (getInfo == JFileChooser.APPROVE_OPTION) {
            file = datei.getSelectedFile();
            try {

                datenIMPORT = new BufferedReader(new FileReader(file));
                importsThreads = new Thread(new Runnable() {

                    @Override
                    public void run() {
                        try {
                            String Daten;
                            Daten = datenIMPORT.readLine();
                            Daten = datenIMPORT.readLine();
                            while (Daten != null) {
                                String[] temp = Daten.split(";");
                                if (temp.length >= 3) {
                                    clients.send("1903" + clients.trennzeichen + temp[0] + clients.trennzeichen + temp[1] + clients.trennzeichen + temp[2] + clients.trennzeichen + temp[3] + clients.trennzeichen + temp[4]);        //db.addNetzwerk(temp[0], temp[1], temp[2], temp[3] + ";" + temp[4],temp[5]);
                                }
                                Daten = datenIMPORT.readLine();
                            }
                            clients.send("1903" + clients.trennzeichen + "END");
                            // while (clients.daten.erledigt);
                            end();
                            // clients.daten.erledigt = true;
                        } catch (IOException ex) {
                            clients.FehlerBerichtSchreiben("impNetz: " + ex, "Log/error.log",haupt);
                            end();
                        }
                    }
                });
                start();

            } catch (FileNotFoundException ex) {
                clients.FehlerBerichtSchreiben("impNetz: " + ex, "Log/error.log",haupt);
            }

        }
    }

    protected void importNetzStandort() {
        JFileChooser datei = new JFileChooser();
        File file;

        int getInfo = datei.showOpenDialog(datei);
        if (getInfo == JFileChooser.APPROVE_OPTION) {
            file = datei.getSelectedFile();
            try {

                datenIMPORT = new BufferedReader(new FileReader(file));
                importsThreads = new Thread(new Runnable() {

                    @Override
                    public void run() {
                        try {
                            String Daten;
                            Daten = datenIMPORT.readLine();
                            Daten = datenIMPORT.readLine();
                            while (Daten != null) {
                                String[] temp = Daten.split(";");
                                if (temp.length >= 3) {
                                    clients.send("1904" + clients.trennzeichen + temp[0] + clients.trennzeichen + temp[1] + clients.trennzeichen + temp[2] + clients.trennzeichen + temp[3] + clients.trennzeichen + temp[4]);
                                }
                                Daten = datenIMPORT.readLine();
                            }
                            clients.send("1904" + clients.trennzeichen + "END");
                            // while (clients.daten.erledigt);
                            end();
                            // clients.daten.erledigt = true;
                        } catch (IOException ex) {
                            clients.FehlerBerichtSchreiben("impNetz: " + ex, "Log/error.log",haupt);
                            end();
                        }
                    }
                });
                start();

            } catch (FileNotFoundException ex) {
                clients.FehlerBerichtSchreiben("impNetz: " + ex, "Log/error.log",haupt);
            }

        }
    }

    protected void importClientServer() {
        JFileChooser datei = new JFileChooser();
        File file;

        int getInfo = datei.showOpenDialog(datei);
        if (getInfo == JFileChooser.APPROVE_OPTION) {
            file = datei.getSelectedFile();
            try {
                datenIMPORT = new BufferedReader(new FileReader(file));
                importsThreads = new Thread(new Runnable() {

                    @Override
                    public void run() {
                        try {
                            String Daten, NetzwerkID = "";
                            ArrayList<ArrayList<String>> netzWerk = (ArrayList<ArrayList<String>>) clients.netzwerkVerwaltung.getNetz("-1", "HauptBildschirm/initDaten", false).clone();
                            Daten = datenIMPORT.readLine();
                            while (Daten != null) {
                                String[] temp = Daten.split(";");
                                if (temp.length > 6) {
                                    for (int a = 0; a < netzWerk.size(); a++) {
                                        if (netzWerk.get(a).get(1).equals(temp[6])) {
                                            NetzwerkID = netzWerk.get(a).get(0).toString();
                                        }
                                    }
                                    if (!NetzwerkID.equals("")) {
                                        for (int c = 0; c < temp.length; c++) {
                                            if (temp[c].equals(" ")) {
                                                temp[c] = "####";
                                            }
                                        }
                                        if (temp.length >= 3) {
                                            String name = temp[0];//checkCSDoppelt(temp[0], netzWerk, "all");
                                            if (name != null) {
                                                clients.send("1100" + clients.trennzeichen + name + clients.trennzeichen + temp[1] + clients.trennzeichen + temp[2] + clients.trennzeichen + temp[3]
                                                        + clients.trennzeichen + temp[4] + clients.trennzeichen + temp[5] + "" + clients.trennzeichen + "" + temp[7]
                                                        + "" + clients.trennzeichen + "" + NetzwerkID + "" + clients.trennzeichen + "" + temp[8]);
                                            }
                                        }
                                    }
                                    Daten = datenIMPORT.readLine();
                                }
                            }
                            clients.send("1100" + clients.trennzeichen + "END");
                            //  while (clients.daten.erledigt);
                            end();
                            // clients.daten.erledigt = true;

                        } catch (IOException ex) {
                            clients.FehlerBerichtSchreiben("impServer: " + ex, "Log/error.log",haupt);
                            end();
                        }
                    }
                });
                start();

            } catch (FileNotFoundException ex) {
                clients.FehlerBerichtSchreiben("impServer: " + ex, "Log/error.log",haupt);
            }
        }
    }

    protected void importSoftware(final String SZ) {
        JFileChooser datei = new JFileChooser();
        File file;

        int getInfo = datei.showOpenDialog(datei);
        if (getInfo == JFileChooser.APPROVE_OPTION) {
            file = datei.getSelectedFile();
            try {

                datenIMPORT = new BufferedReader(new FileReader(file));
                importsThreads = new Thread(new Runnable() {

                    @Override
                    public void run() {
                        try {
                            String readLine = datenIMPORT.readLine(), datenInfo = "";
                            ArrayList<ArrayList<String>> datenSoftware = (ArrayList<ArrayList<String>>) clients.netzwerkVerwaltung.getSoftware("-1", SZ, "HauptBildschirm/importSoftware", false).clone();
                            Boolean weiter = true;

                            readLine = datenIMPORT.readLine();


                            while (readLine != null) {
                                String[] temp = readLine.split(";");
                                for (int i = 0; i < datenSoftware.size(); i++) {
                                    if (temp.length > 1) {
                                        if (datenSoftware.get(i).get(1).equals(temp[0])) {
                                            weiter = false;
                                            break;
                                        }
                                    }
                                }
                                if (weiter) {
                                    if (temp.length > 3) {
                                        datenInfo = temp[7];
                                        for (int i = 8; i < temp.length; i++) {
                                            datenInfo = datenInfo + "#" + temp[i];
                                        }
                                        clients.send("1300" + clients.trennzeichen + "" + temp[0] + "" + clients.trennzeichen + "" + temp[1] + "" + clients.trennzeichen + "" + temp[2] + "" + clients.trennzeichen + "" + temp[3] + "" + clients.trennzeichen + "" + temp[4] + clients.trennzeichen + temp[5] + clients.trennzeichen + datenInfo + clients.trennzeichen + temp[6]);
                                    }
                                }
                                weiter = true;
                                readLine = datenIMPORT.readLine();
                                datenSoftware = (ArrayList<ArrayList<String>>) clients.netzwerkVerwaltung.getSoftware("-1", "0", "HauptBildschirm/importSoftware", false).clone();
                            }
                            clients.send("1300+END");
                            end();
                        } catch (IOException ex) {
                            clients.FehlerBerichtSchreiben("impServer: " + ex, "Log/error.log",haupt);
                            end();
                        }
                    }
                });
                start();
            } catch (FileNotFoundException ex) {
                clients.FehlerBerichtSchreiben("impServer: " + ex, "Log/error.log",haupt);
            }

        }
    }

    protected void importProtokolle() {
        JFileChooser datei = new JFileChooser();
        File file;

        int getInfo = datei.showOpenDialog(datei);
        if (getInfo == JFileChooser.APPROVE_OPTION) {
            file = datei.getSelectedFile();
            try {
                datenIMPORT = new BufferedReader(new FileReader(file));
                importsThreads = new Thread(new Runnable() {

                    @Override
                    public void run() {

                        try {

                            String readLine = datenIMPORT.readLine(), ID = "", ServerID;
                            datenClientServer = (ArrayList<ArrayList<String>>) clients.netzwerkVerwaltung.getHardware("-1", "all", "HauptBildschirm/importProtokoll", false).clone();

                            ServerID = readLine.split(";")[0];
                            for (int i = 0; i < datenClientServer.size(); i++) {
                                if (datenClientServer.get(i).get(1).equals(ServerID)) {
                                    ID = datenClientServer.get(i).get(0).toString();
                                }
                            }
                            readLine = datenIMPORT.readLine();

                            if (!ID.equals("")) {
                                while (readLine != null) {
                                    String[] temp = readLine.split(";");
                                    for (int c = 0; c < temp.length; c++) {
                                        if (temp[c].equals(" ")) {
                                            temp[c] = "####";
                                        }
                                    }
                                    if (temp.length >= 3) {
                                        clients.send("1200" + clients.trennzeichen + temp[0] + clients.trennzeichen + temp[2] + clients.trennzeichen + temp[1] + clients.trennzeichen + ID);
                                    }
                                    readLine = datenIMPORT.readLine();
                                }
                                clients.send("1200+END");
                                end();

                            } else {
                            }

                        } catch (IOException ex) {
                            clients.FehlerBerichtSchreiben("impServer: " + ex, "Log/error.log",haupt);
                        }

                    }
                });
                start();


            } catch (FileNotFoundException ex) {
                clients.FehlerBerichtSchreiben("impServer: " + ex, "Log/error.log",haupt);
            }

        }
    }

    private void getCSServerDaten(String Group) {
        if (Group.equalsIgnoreCase("All")) {
            if (haupt.netz.getSelectedItem().equals("all")) {
                datenHardware = (ArrayList<ArrayList<String>>) clients.netzwerkVerwaltung.getHardware("-1", "alloOld", "HauptBildschirm/setIPPannelInfo", false).clone();
            } else {
                datenHardware = (ArrayList<ArrayList<String>>) clients.netzwerkVerwaltung.getHardware(datenNetz.get(haupt.netz.getSelectedIndex()).get(0).toString(), "alloOld", "HauptBildschirm/setIPPannelInfo", false).clone();
            }

        } else {
            if (haupt.netz.getSelectedItem().equals("all")) {
                datenHardware = (ArrayList<ArrayList<String>>) clients.netzwerkVerwaltung.getHardware("-1", Group, "HauptBildschirm/setIPPannelInfo", false).clone();
            } else {
                datenHardware = (ArrayList<ArrayList<String>>) clients.netzwerkVerwaltung.getHardware(datenNetz.get(haupt.netz.getSelectedIndex()).get(0).toString(), Group, "HauptBildschirm/setIPPannelInfo", false).clone();
            }

        }
    }

    private String checkNetzDoppelt(String Daten) {

        ArrayList<ArrayList<String>> NetzDaten = (ArrayList<ArrayList<String>>) clients.netzwerkVerwaltung.getNetz("-1", "checkNetzDoppelt", false).clone();
        if (NetzDaten != null) {
            for (int i = 0; i < NetzDaten.size(); i++) {
                if (NetzDaten.get(i).get(1).equals(Daten)) {
                    Daten = null;
                    clients.FehlerBerichtSchreiben("Log/impNetz: " + Daten, "import.log",haupt);
                    break;
                }
            }
        } else {
            Daten = null;
        }
        return Daten;
    }

    protected Boolean checkServerSystem() {
        if (datenNetz != null) {
            if (datenNetz.isEmpty() || datenNetz.get(0).isEmpty()) {
                JOptionPane.showMessageDialog(new JFrame(), "Bitte zuerst ein Netz hinzufügen!");
                return false;
            } else {
                return true;
            }
        } else {
            JOptionPane.showMessageDialog(new JFrame(), "Bitte zuerst ein Netz hinzufügen!");
            return false;
        }
    }

    private void initNetzStandorte() {
        haupt.NetzStandort.removeAllItems();
        for (int i = 0; i < standortDaten.size(); i++) {
            haupt.NetzStandort.addItem(standortDaten.get(i).get(1));
        }
        haupt.NetzStandort.addItem("all");
        haupt.NetzStandort.addItem("Old");
    }
    
    protected void ClickIPPanel(MouseEvent e){
        if (e.getClickCount() == 2) {
                    newHardware test;
                    int ar = 0;
                    for (int i = 0; i < datenHardware.size(); i++) {
                        if (datenHardware.get(i).get(0).equals(tabIPPanel.getValueAt(tabIPPanel.getSelectedRow(), 0))) {
                            ar = i;
                            break;
                        }
                    }
                    if (checkServerSystem()) {
                        test = new newHardware();
                        test.StartServer(clients, tabIPPanel.getValueAt(tabIPPanel.getSelectedRow(), 8).toString(), (ArrayList<String>) datenHardware.get(ar).clone(), haupt, null, haupt.serverInit, datenNetz, haupt.freieIP);
                    }
                    //datenNetz = (ArrayList<ArrayList<String>>) clients.netzwerkVerwaltung.getNetz(DatenNetzIDAuswahl, "initTables").clone();//DatenNetzIDAuswahl
                }
                if (e.getButton() == 3) {
                    if (tabIPPanel.getSelectedRow() != -1) {
                        String IPs = tabIPPanel.getValueAt(tabIPPanel.getSelectedRow(), 2).toString();
                        if (IPs.equals("DHCP")) {
                            ip.getIPInfo(e.getClickCount(), tabIPPanel.getValueAt(tabIPPanel.getSelectedRow(), 1).toString(), haupt);
                        } else {
                            ip.getIPInfo(e.getClickCount(), IPs, haupt);
                        }
                    }
                } 
    }

    protected void initNetzWerk() {
        haupt.serverInit.infoProgress.setValue(90);
            haupt.serverInit.infoProgressSee.setValue(90);
            haupt.serverInit.infoProgressSee.setString("initHaupt initialisieren ... 80%");
            haupt.serverInit.infoText.append(" OK! \n initHaupt initialisieren ...");
            
        insertNetzInfo(0, "all"); 
        haupt.serverInit.infoProgress.setValue(20);
        haupt.serverInit.infoProgressSee.setValue(20);
        haupt.serverInit.infoProgressSee.setString("initDaten initialisieren ... 20%");
        haupt.serverInit.infoText.append(" OK! \n initDaten initialisieren ...");
        initDaten();

        haupt.serverInit.infoProgress.setValue(40);
        haupt.serverInit.infoProgressSee.setValue(40);
        haupt.serverInit.infoProgressSee.setString("initNetzStandorte initialisieren ... 30%");
        haupt.serverInit.infoText.append(" OK! \n initNetzStandorte initialisieren ...");
        initNetzStandorte();
        haupt.serverInit.infoProgress.setValue(50);
        haupt.serverInit.infoProgressSee.setValue(50);
        haupt.serverInit.infoProgressSee.setString("initNetzInfo initialisieren ... 40%");
        haupt.serverInit.infoText.append(" OK! \n initNetzInfo initialisieren ...");
        initNetzInfo();
        haupt.serverInit.infoProgress.setValue(60);
            haupt.serverInit.infoProgressSee.setValue(60);
            haupt.serverInit.infoProgressSee.setString("initNetzTableInfo initialisieren ... 45%");
            haupt.serverInit.infoText.append(" OK! \n initNetzTableInfo initialisieren ...");
            initNetzTableInfo();
    }

    private void initDaten() {

        ServerProtokoll = new ServerProtokollDaten(clients, haupt);
        standortDaten = (ArrayList<ArrayList<String>>) clients.netzwerkVerwaltung.getNetzAdresse("-1", "HauptBildschirm/initDaten", false).clone();
        
        if (standortDaten == null) {
            haupt.setVisible(false);
            haupt.serverInit.fehleranalyse.append("StandortDaten: " + standortDaten + "\n");
            haupt.serverInit.setVisible(true);
        }

        // eMailOrdner = clients.netzwerkVerwaltung.datenAPfad();
    }

    protected void initNetzInfo() {

        haupt.netz.removeAllItems();
        if (standortDaten.isEmpty() || haupt.NetzStandort.getSelectedItem().equals("all")) {
            DatenNetzIDAuswahl = "-1";
        } else if (haupt.NetzStandort.getSelectedItem().equals("Old")) {
            DatenNetzIDAuswahl = "-2";
        } else {
            if (haupt.NetzStandort.getSelectedIndex() != -1) {
                DatenNetzIDAuswahl = standortDaten.get(haupt.NetzStandort.getSelectedIndex()).get(0).toString();
            } else {
                DatenNetzIDAuswahl = standortDaten.get(0).get(0).toString();
            }
        }
        datenNetz = (ArrayList<ArrayList<String>>) clients.netzwerkVerwaltung.getNetz(DatenNetzIDAuswahl, "initNetzInfo", false).clone();
        if (datenNetz != null) {
            for (int e = 0; e < datenNetz.size(); e++) {
                haupt.netz.addItem(datenNetz.get(e).get(1));
            }
        } else {
            haupt.netz.addItem("KEINE INFORMATIONEN");
        }
    }

    private void initNetzTableInfo() {
        ArrayList<String> temp;
        for (int i = 0; i < datenNetz.size(); i++) {
            temp = new ArrayList<String>();
            temp.add(datenNetz.get(i).get(1).toString());
            temp.add(datenNetz.get(i).get(2).toString());
            temp.add(datenNetz.get(i).get(3).toString());
            temp.add(datenNetz.get(i).get(5).toString());
            if (datenNetz.get(i).get(4).toString().equals("0;0")) {
                temp.add("NEIN");
            } else {
                temp.add("JA");
            }
            tableNetz.getInfoTable.addRow(temp, "1");
        }
    }

    protected void insertNetzInfo(int wert, String netzdaten) {
        try {
            if (!haupt.netz.getSelectedItem().toString().equals("all")) {
                haupt.subnetz.setText(datenNetz.get(wert).get(2).toString());
                haupt.Netz.setText(datenNetz.get(wert).get(2).toString());
                haupt.dhcp.setText(datenNetz.get(wert).get(4).toString());
                haupt.DHCPNetz.setText(datenNetz.get(wert).get(4).toString());
                haupt.maske.setText(datenNetz.get(wert).get(3).toString());
                haupt.Maske.setText(datenNetz.get(wert).get(3).toString());
                datenHardware = clients.netzwerkVerwaltung.getHardware(datenNetz.get(wert).get(0).toString(), "alloOld", "HauptBildschirm/insertNetzInfo", false);
                if (!datenHardware.isEmpty()){
                int anzIP = 0, anzKEINE = 0, anzUSB = 0, anzDHCP = 0, anzServer = 0, anzNotebooks = 0, anzPC = 0;

                for (int c = 0; c < datenHardware.size(); c++) {

                    if (datenHardware.get(c).get(7).equals("Server")) {
                        anzServer++;
                    } else if (datenHardware.get(c).get(7).equals("Notebook")) {
                        anzNotebooks++;
                    } else if (datenHardware.get(c).get(7).equals("PC")) {
                        anzPC++;
                    }

                    if (datenHardware.get(c).get(6).equals("DHCP")) {
                        anzDHCP++;
                    } else if (datenHardware.get(c).get(6).equals("KEINE")) {
                        anzKEINE++;
                    } else if (datenHardware.get(c).get(6).equals("USB")) {
                        anzUSB++;
                    } else {
                        String[] ipAdresse = datenHardware.get(c).get(6).toString().split(",");
                        anzIP = anzIP + ipAdresse.length;
                    }
                }

                haupt.serveranz.setText(String.valueOf(anzServer));
                haupt.lapanz.setText(String.valueOf(anzNotebooks));
                haupt.pcanz.setText(String.valueOf(anzPC));
                haupt.fip2.setText(String.valueOf(anzDHCP));
                haupt.fip3.setText(String.valueOf(anzIP));
                haupt.fip4.setText(anzUSB + "");
                haupt.fip5.setText(anzKEINE + "");
                haupt.fip.setText(String.valueOf(anzIP + anzDHCP));
                haupt.fip1.setText(String.valueOf(Integer.parseInt(datenNetz.get(wert).get(5).toString()) - anzIP - anzDHCP));
                haupt.freeIP.setText(String.valueOf(Integer.parseInt(datenNetz.get(wert).get(5).toString()) - anzIP - anzDHCP));
            }
            setIPPannelInfo("All");
            insertProtokollCSDaten("All");
            setProtokollDaten();
            //insertFreieIPs(wert);
            }

        } catch (Exception e) {
            clients.FehlerBerichtSchreiben("Verwaltung (insertNetzInfo): " + e, "Log/error.log",haupt);
        }
    }

    protected void setIPPannelInfo(String Gruppe) {

        tableIPPanel.getInfoTable.removeAllItems();
        if (datenNetz.isEmpty()) {
        } else {
            if (!haupt.subnetz.getText().isEmpty()) {
                String ipName = haupt.subnetz.getText().toString().split("\\.")[0] + "."
                        + haupt.subnetz.getText().toString().split("\\.")[1] + "."
                        + haupt.subnetz.getText().toString().split("\\.")[2] + ".";

                ip.insertInfo(haupt.netz.getSelectedItem().toString(), ipName, datenHardware, haupt.netz.getSelectedItem().toString(), datenNetz.get(haupt.netz.getSelectedIndex()).get(0).toString());

                for (int a = 0; a < ip.getIPs.size(); a++) {
                    if (ip.getIPs.get(a).get(5).equalsIgnoreCase(Gruppe) || Gruppe.equalsIgnoreCase("All"))
                     tableIPPanel.getInfoTable.addRow(ip.getIPs.get(a), "1");
                }
            }
        }
    }

    protected void insertProtokollCSDaten(String Gruppe) {
        tableServerProtokoll.getInfoTable.removeAllItems();
        haupt.sCProtokoll.removeAllItems();
        for (int i = 0; i < datenHardware.size(); i++) {
            if (datenHardware.get(i).get(7).equalsIgnoreCase(Gruppe) || Gruppe.equalsIgnoreCase("All"))
            haupt.sCProtokoll.addItem(datenHardware.get(i).get(1));
        }
    }

    private void setProtokollDaten() {
        ServerProtokoll.getInfo(haupt.netz.getSelectedItem().toString(), datenHardware.get(0).get(0).toString(), haupt.GruppeProtokoll.getSelectedItem().toString());
        ArrayList<ArrayList<String>> ProtokollDaten = ServerProtokoll.getProtokollDaten;
        tableServerProtokoll.getInfoTable.removeAllItems();
        for (int i = 0; i < ProtokollDaten.size(); i++) {
            tableServerProtokoll.getInfoTable.addRow(ProtokollDaten.get(i), "1");
        }
    }

    private void insertFreieIPs(int netzWahl) {
        boolean weiter = true, wdhcp = true;
        int netzID = netzWahl;
        haupt.freieIP.removeAllItems();

        String[] IPNetz = datenNetz.get(netzID).get(2).toString().split("\\.");
        String[] DHCP = datenNetz.get(netzID).get(4).toString().split(" - ");
        ArrayList<ArrayList<String>> cs = (ArrayList<ArrayList<String>>) clients.netzwerkVerwaltung.getHardware(datenNetz.get(netzID).get(0), "all", "newServerClientDaten/StartServer", false).clone();
        int start = Integer.parseInt(IPNetz[3]), sDHCP = 0, eDHCP = 0;
        int end = start + Integer.parseInt(datenNetz.get(netzID).get(5).toString());

        for (int i = start + 1; i < end; i++) {
            for (int b = 0; b < DHCP.length; b++) {
                if (DHCP[b].split(";").length > 1) {
                    sDHCP = Integer.parseInt(DHCP[b].split(";")[0]);
                    eDHCP = Integer.parseInt(DHCP[b].split(";")[1]);
                } else {
                    sDHCP = 0;
                    eDHCP = Integer.parseInt(DHCP[b].split(";")[0]);
                }

                if (sDHCP != 0 && eDHCP != 0) {
                    if (sDHCP <= i) {
                        if (eDHCP >= i) {
                            wdhcp = false;
                            break;
                        }
                    }
                }
            }
            if (wdhcp) {
                for (int a = 0; a < cs.size(); a++) {
                    if (cs.get(a).get(6).equals("" + i)) {
                        weiter = false;
                        break;
                    }
                }

                if (weiter) {
                    haupt.freieIP.addItem(i);
                }

            }
            weiter = true;
            wdhcp = true;
        }
    }

    private boolean checkInhalt(String daten, String suche) {
        for (int i = 0; i < daten.length(); i++) {
            if (daten.substring(i, daten.length()).toLowerCase().startsWith(suche.toLowerCase())) {
                return true;
            }
        }
        return false;
    }

    protected void searchIPPanel() {
        tableIPPanel.getInfoTable.removeAllItems();
        ArrayList<String> info;
        ArrayList<ArrayList<String>> datenN = (ArrayList<ArrayList<String>>) clients.netzwerkVerwaltung.getNetz("-1", "searchIPPannel1", false).clone();
        datenHardware = (ArrayList<ArrayList<String>>) clients.netzwerkVerwaltung.getHardware("-1", "all", "HauptBildschirm/insertCSDaten", false).clone();
        for (int i = 0; i < datenHardware.size(); i++) {
            if (checkInhalt(datenHardware.get(i).get(0).toString(), haupt.searchIPPannel1.getText())
                    || checkInhalt(datenHardware.get(i).get(1).toString(), haupt.searchIPPannel1.getText())
                    || checkInhalt(datenHardware.get(i).get(2).toString(), haupt.searchIPPannel1.getText())
                    || checkInhalt(datenHardware.get(i).get(3).toString(), haupt.searchIPPannel1.getText())
                    || checkInhalt(datenHardware.get(i).get(4).toString(), haupt.searchIPPannel1.getText())
                    || checkInhalt(datenHardware.get(i).get(5).toString(), haupt.searchIPPannel1.getText())
                    || checkInhalt(datenHardware.get(i).get(6).toString(), haupt.searchIPPannel1.getText())
                    || checkInhalt(datenHardware.get(i).get(7).toString(), haupt.searchIPPannel1.getText())
                    || checkInhalt(datenHardware.get(i).get(8).toString(), haupt.searchIPPannel1.getText())
                    || checkInhalt(datenHardware.get(i).get(9).toString(), haupt.searchIPPannel1.getText())) {
                info = new ArrayList<String>();
                info.add(datenHardware.get(i).get(0).toString());
                info.add(datenHardware.get(i).get(1).toString());
                Boolean weiter = true;
                for (int a = 0; a < datenN.size(); a++) {
                    if (datenN.get(a).get(0).equals(datenHardware.get(i).get(8))) {

                        String ipName = datenN.get(a).get(2).split("\\.")[0] + "."
                                + datenN.get(a).get(2).split("\\.")[1] + "."
                                + datenN.get(a).get(2).split("\\.")[2] + ".";

                        if (!datenHardware.get(i).get(6).toString().equalsIgnoreCase("USB")
                                && !datenHardware.get(i).get(6).toString().equalsIgnoreCase("DHCP")
                                && !datenHardware.get(i).get(6).toString().equalsIgnoreCase("KEINE")) {
                            info.add(ipName + datenHardware.get(i).get(6).toString());
                        } else {
                            info.add(datenHardware.get(i).get(6).toString());
                        }

                        info.add(datenHardware.get(i).get(5).toString());
                        info.add(datenHardware.get(i).get(2).toString());
                        info.add(datenHardware.get(i).get(3).toString());
                        info.add(datenHardware.get(i).get(4).toString());

                        info.add(datenN.get(a).get(1).toString());
                        info.add(datenN.get(a).get(0).toString());
                        weiter = false;
                        break;
                    }
                }
                if (weiter) {
                    info.add(datenHardware.get(i).get(6).toString());
                    info.add(datenHardware.get(i).get(5).toString());
                    info.add(datenHardware.get(i).get(2).toString());
                    info.add(datenHardware.get(i).get(3).toString());
                    info.add(datenHardware.get(i).get(4).toString());
                    info.add("unbekannt");
                }
                tableIPPanel.getInfoTable.addRow(info, "-1");

            }
        }
        haupt.suchergebniss.setText("Suchergebnis: " + tableIPPanel.getInfoTable.getRowCount() + " (" + haupt.searchIPPannel1.getText() + ")");
        datenN = null;
    }
}
