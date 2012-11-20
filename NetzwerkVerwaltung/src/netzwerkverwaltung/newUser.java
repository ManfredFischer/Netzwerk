package netzwerkverwaltung;

import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;

public class newUser extends javax.swing.JFrame {

    private ClientNetzwerk clients;
    private String[] titles = new String[]{"Name", "Zweck", "User01", "PW01", "User02", "PW02"};
    private TabelFont table = new TabelFont();
    private JTable tabTable;
    private HauptBildschirm haupt;
    private String statusInfo;
    private ArrayList<String> userInfo2;
    private ArrayList<HashMap> InfoSoftware, InfoZubehoer, SoftwareFiles = new ArrayList<HashMap>(), ZubehoerFiles = new ArrayList<HashMap>();
    private boolean verschluesselt = true;
    private String vorschau = "ja", IDUser, User, Tel, Fax, UserEmail, IDNUser;
    protected ArrayList<ArrayList<String>> laptops;
    private String NetzU;
    private ArrayList<ArrayList<String>> laptopsTemp, InfoPfad, software, standortDaten;
    private initServer initSERVER;
    private ArrayList<ArrayList<String>> datenPW;

    public newUser(ClientNetzwerk cl, String NetzID, String D, ArrayList<String> DatenUser, HauptBildschirm h, ArrayList<String> userInfo, initServer init) {
        clients = cl;
        ordner = new SimpleDateFormat("mmDDyyyyHHMMSS").format(new Date()).toString();
        initSERVER = init;
        userInfo2 = userInfo;
        NetzU = NetzID;
        initComponents();
        tabTable = table.getTable(titles, -1, null, -1, null);


        table.getInfoTable.wert = 12;

        User = userInfo.get(3).toString() + " " + userInfo.get(2).toString();
        Tel = userInfo.get(6).toString();
        Fax = userInfo.get(7).toString();
        UserEmail = userInfo.get(5).toString();

        this.setLocationRelativeTo(h);
        antrag.setText(userInfo.get(5).toString());
        statusInfo = status.getSelectedItem() + "#0#0";
        haupt = h;

        init.infoProgress.setForeground(Color.BLACK);
        init.infoProgress.setValue(5);

        // InfoPfad = (ArrayList<HashMap>) cl.netzwerkVerwaltung.datenAPfad("newUser/newUser").clone();
        init.infoProgress.setForeground(Color.BLACK);
        init.infoProgress.setValue(20);

        laptops = (ArrayList<ArrayList<String>>) clients.netzwerkVerwaltung.getHardware("-1", "PC", "newUser/newUser",false).clone();
        init.infoProgress.setForeground(Color.BLACK);
        init.infoProgress.setValue(40);

        laptopsTemp = (ArrayList<ArrayList<String>>) clients.netzwerkVerwaltung.getHardware("-1", "Notebook", "newUser/newUser",false).clone();
        init.infoProgress.setForeground(Color.BLACK);
        init.infoProgress.setValue(60);

        standortDaten = (ArrayList<ArrayList<String>>) clients.netzwerkVerwaltung.getNetzAdresse(NetzU, "newUser/newUser",false).clone();
        init.infoProgress.setForeground(Color.BLACK);
        init.infoProgress.setValue(70);

        /*
         * for (int i = 0; i < InfoPfad.size(); i++) {
         * ADSPfad.addItem(InfoPfad.get(i).get("Name")); }
         */

        for (int i = 0; i < laptopsTemp.size(); i++) {
            laptops.add(laptopsTemp.get(i));
        }

        for (int i = 0; i < laptops.size(); i++) {
            lapSN.addItem(laptops.get(i).get(1));
        }

        InfoSoftware = new ArrayList<HashMap>();
        InfoSoftware = addSoftwareZubehor(InfoSoftware, AuswahlSoftware, "0");

        InfoZubehoer = new ArrayList<HashMap>();
        InfoZubehoer = addSoftwareZubehor(InfoZubehoer, auswahlZubehoer, "1");


        try {
            BufferedReader read = new BufferedReader(new FileReader(new File("Config/config.cfg")));
            String RTemp = read.readLine();
            while (RTemp != null) {
                if (RTemp.startsWith("## ADSPfad")) {
                    RTemp = read.readLine();
                    while (RTemp.startsWith("##Zuständigkeit:")) {
                        ADSPfad.addItem(RTemp.toString().toString().split(":")[0]);
                        RTemp = read.readLine();
                    }
                    break;
                }
                RTemp = read.readLine();
            }
        } catch (Exception ex) {
        }


        for (int i = 0; i < standortDaten.size(); i++) {
            standort.addItem(standortDaten.get(i).get(1));
        }

        strasse.setText(standortDaten.get(0).get(2).toString());
        plz.setText(standortDaten.get(0).get(3).toString());
        ort.setText(standortDaten.get(0).get(4).toString());



        if (!D.equals("leer")) {
            addInfo(DatenUser);
            addAnhaengeSoftwareZubehoerDaten(DatenUser.get(0).toString());

            datenPW = (ArrayList<ArrayList<String>>) clients.netzwerkVerwaltung.getUserPW(IDUser, "-1", "newUser/newUser",false).clone();
            init.infoProgress.setForeground(Color.BLACK);
            init.infoProgress.setValue(130);
            if (datenPW.isEmpty()) {
                fillSoftwareZubehoer();
            } else {
                getPWInfos(IDUser);
                setPWList();
            }
            SButton.setText("Save");
        } else {
            addAnhaengeSoftwareZubehoer();
            //jButton3.setVisible(false);
            datenPW = new ArrayList<ArrayList<String>>();
            fillSoftwareZubehoer();
            //SButton1.setVisible(false);
        }



        userSoft.add(tabTable);
        userSoft.setViewportView(tabTable);

        if (lapSN.getItemAt(0) == null) {
            laptop.setEnabled(false);
        }
        init.infoProgress.setForeground(Color.GREEN);
        init.infoProgress.setValue(140);

        this.setVisible(true);
    }

    private void fillSoftwareZubehoer() {
        ArrayList<String> infoTemp;
        for (int i = 0; i < InfoSoftware.size(); i++) {
            infoTemp = new ArrayList<String>();
            infoTemp.add("");
            infoTemp.add(((JTextField) InfoSoftware.get(i).get("Name")).getText());
            infoTemp.add(((JTextField) InfoSoftware.get(i).get("Zweck")).getText());
            infoTemp.add(" ");
            infoTemp.add("***");
            infoTemp.add(" ");
            infoTemp.add("***");
            infoTemp.add("--");
            infoTemp.add((InfoSoftware.get(i).get("IDS").toString()));
            datenPW.add(infoTemp);

            infoTemp = new ArrayList<String>();
            infoTemp.add(((JTextField) InfoSoftware.get(i).get("Name")).getText());
            infoTemp.add(((JTextField) InfoSoftware.get(i).get("Zweck")).getText());
            infoTemp.add(" ");
            infoTemp.add("***");
            infoTemp.add(" ");
            infoTemp.add("***");

            table.getInfoTable.addRow(infoTemp, "-1");
        }

        for (int i = 0; i < InfoZubehoer.size(); i++) {

            infoTemp = new ArrayList<String>();
            infoTemp.add("");
            infoTemp.add(((JTextField) InfoZubehoer.get(i).get("Name")).getText());
            infoTemp.add(((JTextField) InfoZubehoer.get(i).get("Zweck")).getText());
            infoTemp.add(" ");
            infoTemp.add("***");
            infoTemp.add(" ");
            infoTemp.add("***");
            infoTemp.add("--");
            infoTemp.add((InfoZubehoer.get(i).get("IDS").toString()));
            datenPW.add(infoTemp);

            infoTemp = new ArrayList<String>();
            infoTemp.add(((JTextField) InfoZubehoer.get(i).get("Name")).getText());
            infoTemp.add(((JTextField) InfoZubehoer.get(i).get("Zweck")).getText());
            infoTemp.add(" ");
            infoTemp.add("***");
            infoTemp.add(" ");
            infoTemp.add("***");


            table.getInfoTable.addRow(infoTemp, "-1");
        }
    }

    private void getPWInfos(String id) {
        try {
            ArrayList<File> dateien = clients.netzwerkVerwaltung.getDateien("Software/" + id, "dpr" + id + ".zip", "newUser/getPWInfos");
            ArrayList<String> infoTemp;
            ArrayList<ArrayList<String>> info = new ArrayList<ArrayList<String>>();

            pw.setText(clients.entschluesseln("Daten/Software/" + id + "/UserPW.dpr"));
            emailPW.setText(clients.entschluesseln("Daten/Software/" + id + "/UsereMailPW.dpr"));

            for (int i = 0; i < datenPW.size(); i++) {
                infoTemp = new ArrayList<String>();
                infoTemp.add(datenPW.get(i).get(0));
                infoTemp.add(datenPW.get(i).get(1));
                infoTemp.add(datenPW.get(i).get(2));
                infoTemp.add(datenPW.get(i).get(3));
                infoTemp.add(clients.entschluesseln("Daten/Software/" + id + "/SoftwPW1." + datenPW.get(i).get(8) + ".dpr"));
                infoTemp.add(datenPW.get(i).get(5));
                infoTemp.add(clients.entschluesseln("Daten/Software/" + id + "/SoftwPW2." + datenPW.get(i).get(8) + ".dpr"));
                infoTemp.add("-");
                infoTemp.add(datenPW.get(i).get(8));
                info.add(infoTemp);
            }
            new File("Daten/Software/" + id).delete();
            datenPW = info;
        } catch (Exception ex) {
            Logger.getLogger(newUser.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void showPWList(boolean verschluesselt) {

        ArrayList<String> datenInfoUser = new ArrayList<String>();
        if (!datenPW.isEmpty()) {
            table.getInfoTable.removeAllItems();
        }

        for (int a = 0; a < datenPW.size(); a++) {
            if (datenPW.get(a).get(0).equals("-1")) {
                datenInfoUser.add(datenPW.get(a).get(1));
                datenInfoUser.add(datenPW.get(a).get(2));
                datenInfoUser.add(datenPW.get(a).get(3));

                if (verschluesselt) {
                    datenInfoUser.add("***");
                } else {
                    datenInfoUser.add(datenPW.get(a).get(4).toString());
                }

                datenInfoUser.add(datenPW.get(a).get(5));

                if (verschluesselt) {
                    datenInfoUser.add("***");
                } else {
                    datenInfoUser.add(datenPW.get(a).get(6));
                }

                table.getInfoTable.addRow(datenInfoUser, "-1");
                datenInfoUser = new ArrayList<String>();
            } else {
                datenInfoUser.add(datenPW.get(a).get(1));
                datenInfoUser.add(datenPW.get(a).get(2));
                datenInfoUser.add(datenPW.get(a).get(3));

                if (verschluesselt) {
                    datenInfoUser.add("***");
                } else {
                    datenInfoUser.add(datenPW.get(a).get(4));
                }

                datenInfoUser.add(datenPW.get(a).get(5));

                if (verschluesselt) {
                    datenInfoUser.add("***");
                } else {
                    datenInfoUser.add(datenPW.get(a).get(6));
                }

                table.getInfoTable.addRow(datenInfoUser, "-1");
                datenInfoUser = new ArrayList<String>();
            }
        }




    }

    private void setPWList() {
        ArrayList<String> datenInfoUser = new ArrayList<String>();

        for (int a = 0; a < datenPW.size(); a++) {
            datenInfoUser.add(datenPW.get(a).get(1));
            datenInfoUser.add(datenPW.get(a).get(2));
            datenInfoUser.add(datenPW.get(a).get(3));

            if (verschluesselt) {
                datenInfoUser.add("***");
            } else {
                datenInfoUser.add(datenPW.get(a).get(4));
            }

            datenInfoUser.add(datenPW.get(a).get(5));

            if (verschluesselt) {
                datenInfoUser.add("***");
            } else {
                datenInfoUser.add(datenPW.get(a).get(6));
            }

            table.getInfoTable.addRow(datenInfoUser, "-1");
            datenInfoUser = new ArrayList<String>();
        }
    }

    private ArrayList<HashMap> addSoftwareZubehor(ArrayList<HashMap> infoDatenZS, JTabbedPane tabbel, String SZ) {

        software = (ArrayList<ArrayList<String>>) clients.netzwerkVerwaltung.getSoftware("-1", SZ, "newUser/addSoftwareZubehor",false).clone();

        for (int i = 0; i < software.size(); i++) {
            infoDatenZS.add((HashMap) SoftwareZubehoerPanel((ArrayList<String>) software.get(i).clone()).clone());
        }

        for (int i = 0; i < infoDatenZS.size(); i++) {
            tabbel.add((JPanel) infoDatenZS.get(i).get("JPanel"));
        }
        return infoDatenZS;
    }

    private void addAnhaengeSoftwareZubehoerDaten(String ID) {
        SoftwareFiles = (ArrayList<HashMap>) addSZ(InfoSoftware, ID, false, 110).clone();
        ZubehoerFiles = (ArrayList<HashMap>) addSZ(InfoZubehoer, ID, false, 120).clone();
    }
    private String pfadDaten, ordner;

    private void addAnhaengeSoftwareZubehoer() {
        SoftwareFiles = (ArrayList<HashMap>) addSZ(InfoSoftware, "-1", true, 110).clone();
        ZubehoerFiles = (ArrayList<HashMap>) addSZ(InfoZubehoer, "-1", true, 120).clone();
    }

    private ArrayList<HashMap> addSZ(ArrayList<HashMap> info, String id, boolean newDaten, int wert) {
        ArrayList<File> tempSoft;
        ArrayList<HashMap> tmp = new ArrayList<HashMap>();
        String tempSoftName;
        HashMap datenSoftware;
        for (int i = 0; i < info.size(); i++) {
            datenSoftware = new HashMap();
            if (id.equals("-1")) {
                tempSoft = clients.netzwerkVerwaltung.getDateiInfo("Software", ((JTextField) info.get(i).get("Name")).getText() + ".zip", ordner, "newUser/addSZ",false);
            } else {
                tempSoft = clients.netzwerkVerwaltung.getDateiInfoVorhanden("Software/" + id + "/", ((JTextField) info.get(i).get("Name")).getText(), "newUser/addSZ",false);
            }
            initSERVER.infoProgress.setForeground(Color.BLACK);
            initSERVER.infoProgress.setValue(wert);
            tempSoftName = ((JTextField) info.get(i).get("Name")).getText();

            if (!newDaten) {
                datenSoftware.put("Name", tempSoftName);
                datenSoftware.put("Anhaenge", tempSoft);
                tmp.add(datenSoftware);
            } else {
                datenSoftware.put("Name", tempSoftName);
                datenSoftware.put("Anhaenge", tempSoft);
                tmp.add(datenSoftware);
            }
            for (int b = 0; b < tempSoft.size(); b++) {
                ArrayList<String> tempSoftInfo = new ArrayList<String>();
                String tmpName = tempSoft.get(b).getName().split(((JTextField) info.get(i).get("Name")).getText() + "-")[1];
                tempSoftInfo.add(tmpName);
                ((TabelFont) info.get(i).get("JTable2")).getInfoTable.addRow(tempSoftInfo, "-1");
            }
        }
        return tmp;
    }

    private void cleanTempDateien() {
        File datei;
        File[] files;
        try {
            datei = new File("Daten/Temp/" + ordner + "/");
            files = datei.listFiles();
            for (int i = 0; i < files.length; i++) {
                files[i].delete();
            }
            datei.delete();
        } catch (Exception e) {
        }
            try {
                datei = new File("Daten/Software/" + IDNUser + "/");
                files = datei.listFiles();
                for (int i = 0; i < files.length; i++) {
                    files[i].delete();
                }
                datei.delete();
            } catch (Exception e) {
            }
            clients.send("99997" + clients.trennzeichen + "Daten/Software/" + ordner + "/");
        
    }

    private String getRightName(String NameS) {
        String[] Name = NameS.split("-");
        String tmpName = "";
        if (Name.length > 1) {
            for (int c = 1; c < Name.length; c++) {
                tmpName = tmpName + Name[c];
            }
        } else {
            tmpName = NameS;
        }
        return tmpName;
    }

    private boolean checkUser(String pnr) {
        boolean iO = true;
        /*
         * ArrayList<HashMap> us = clients.getInfo(1, "-1"); for (int i = 0; i <
         * us.size(); i++) { if (us.get(i).get("UserPersonalNr").equals(pnr)) {
         * iO = false; break; } }
         */
        return iO;
    }

    private void save() {
        String abteilung, systemDaten = "0#Windows#000#000#000#000", laptopDaten = "0#000", emailDaten = "0#000#000";

        if (!Name.getText().equals("") && !vname.getText().equals("") && !pnr.getText().equals("")) {
            if (checkUser(pnr.getText()) || SButton.getText().equalsIgnoreCase("Save")) {
                abteilung = clients.checkDaten(abtl1.getText());
                if (emailCheck.isSelected()) {
                    emailDaten = "1#" + email.getText() + "# ";
                }
                if (system.isSelected()) {
                    if (SWindows.isSelected()) {
                        systemDaten = "1#Windows#***#" + nickname.getText() + "# #" + ADSPfad.getSelectedItem();
                    } else {
                        systemDaten = "1#Novel#" + clients.checkDaten(NovelContext.getSelectedItem()) + "#" + nickname.getText() + "#***#***";
                    }
                }
                if (laptop.isSelected()) {
                    laptopDaten = "1#" + lapSN.getSelectedIndex();
                }



                SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy - MM:HH");
                Date datum = new Date();
                int id = 0;
                String[] zeile = kommentar.getText().split("\n");
                String inhalt = zeile[0];
                for (int i = 1; i < zeile.length; i++) {
                    inhalt = inhalt + "-?-" + zeile[i];
                }

                if (SButton.getText().equalsIgnoreCase("Save")) {
                    clients.send("2002" + clients.trennzeichen + "NewUser" + clients.trennzeichen + "Daten1" + clients.trennzeichen + clients.checkDaten(Name.getText()) + "" + clients.trennzeichen + "IDNUser" + clients.trennzeichen + IDNUser);
                    clients.send("2002" + clients.trennzeichen + "NewUser" + clients.trennzeichen + "Daten2" + clients.trennzeichen + clients.checkDaten(vname.getText()) + "" + clients.trennzeichen + "IDNUser" + clients.trennzeichen + IDNUser);
                    clients.send("2002" + clients.trennzeichen + "NewUser" + clients.trennzeichen + "Daten3" + clients.trennzeichen + clients.checkDaten(pnr.getText()) + "" + clients.trennzeichen + "IDNUser" + clients.trennzeichen + IDNUser);
                    clients.send("2002" + clients.trennzeichen + "NewUser" + clients.trennzeichen + "Daten4" + clients.trennzeichen + clients.checkDaten(funktion.getText()) + "" + clients.trennzeichen + "IDNUser" + clients.trennzeichen + IDNUser);
                    clients.send("2002" + clients.trennzeichen + "NewUser" + clients.trennzeichen + "Daten5" + clients.trennzeichen + clients.checkDaten(tel.getText()) + "" + clients.trennzeichen + "IDNUser" + clients.trennzeichen + IDNUser);
                    clients.send("2002" + clients.trennzeichen + "NewUser" + clients.trennzeichen + "Daten6" + clients.trennzeichen + clients.checkDaten(fax.getText()) + "" + clients.trennzeichen + "IDNUser" + clients.trennzeichen + IDNUser);
                    clients.send("2002" + clients.trennzeichen + "NewUser" + clients.trennzeichen + "Daten7" + clients.trennzeichen + clients.checkDaten(abteilung) + "" + clients.trennzeichen + "IDNUser" + clients.trennzeichen + IDNUser);
                    clients.send("2002" + clients.trennzeichen + "NewUser" + clients.trennzeichen + "Daten8" + clients.trennzeichen + clients.checkDaten(standort.getSelectedItem()) + "" + clients.trennzeichen + "IDNUser" + clients.trennzeichen + IDNUser);
                    clients.send("2002" + clients.trennzeichen + "NewUser" + clients.trennzeichen + "Daten9" + clients.trennzeichen + clients.checkDaten(statusInfo) + "" + clients.trennzeichen + "IDNUser" + clients.trennzeichen + IDNUser);
                    clients.send("2002" + clients.trennzeichen + "NewUser" + clients.trennzeichen + "Daten10" + clients.trennzeichen + format.format(datum) + "" + clients.trennzeichen + "IDNUser" + clients.trennzeichen + IDNUser);
                    clients.send("2002" + clients.trennzeichen + "NewUser" + clients.trennzeichen + "Daten11" + clients.trennzeichen + clients.checkDaten(emailDaten) + "" + clients.trennzeichen + "IDNUser" + clients.trennzeichen + IDNUser);
                    clients.send("2002" + clients.trennzeichen + "NewUser" + clients.trennzeichen + "Daten12" + clients.trennzeichen + clients.checkDaten(laptopDaten) + "" + clients.trennzeichen + "IDNUser" + clients.trennzeichen + IDNUser);
                    clients.send("2002" + clients.trennzeichen + "NewUser" + clients.trennzeichen + "Daten13" + clients.trennzeichen + clients.checkDaten(systemDaten) + "" + clients.trennzeichen + "IDNUser" + clients.trennzeichen + IDNUser);
                    clients.send("2002" + clients.trennzeichen + "NewUser" + clients.trennzeichen + "Daten14" + clients.trennzeichen + clients.checkDaten(inhalt) + "" + clients.trennzeichen + "IDNUser" + clients.trennzeichen + IDNUser);
                    clients.send("2000" + clients.trennzeichen + "UsersLaptopListe" + clients.trennzeichen + "IDAdressbuch" + clients.trennzeichen + IDNUser);
                    clients.send("2000" + clients.trennzeichen + "UsersPW" + clients.trennzeichen + "IDNUser" + clients.trennzeichen + IDNUser);
                    clients.send("999997" + clients.trennzeichen + "Daten/Software/" + IDNUser + "/dpr" + IDNUser + ".zip");
                    if (laptop.isSelected()) {
                        clients.send("1409" + clients.trennzeichen + laptops.get(lapSN.getSelectedIndex()).get(0) + "" + clients.trennzeichen + IDNUser);
                    }
                    id = Integer.parseInt(IDNUser);
                } else {
                    statusInfo = "in Arbeit#0#0";

                    clients.send("1500" + clients.trennzeichen
                            + clients.checkDaten(Name.getText()) + "" + clients.trennzeichen + clients.checkDaten(vname.getText()) + "" + clients.trennzeichen
                            + clients.checkDaten(pnr.getText()) + "" + clients.trennzeichen + clients.checkDaten(funktion.getText()) + "" + clients.trennzeichen
                            + clients.checkDaten(tel.getText()) + "" + clients.trennzeichen + clients.checkDaten(fax.getText()) + "" + clients.trennzeichen
                            + clients.checkDaten(abteilung) + "" + clients.trennzeichen + clients.checkDaten(standort.getSelectedItem()) + "" + clients.trennzeichen
                            + clients.checkDaten(statusInfo) + "" + clients.trennzeichen + format.format(datum) + "" + clients.trennzeichen + clients.checkDaten(emailDaten) + "" + clients.trennzeichen + clients.checkDaten(laptopDaten) + "" + clients.trennzeichen
                            + clients.checkDaten(systemDaten) + "" + clients.trennzeichen + clients.checkDaten(inhalt));


                    id = Integer.parseInt(clients.netzwerkVerwaltung.getIDNewUser());
                    if (laptop.isSelected()) {
                        clients.send("1409" + clients.trennzeichen + laptops.get(lapSN.getSelectedIndex()).get(0) + "" + clients.trennzeichen + id);
                    }


                }
                addPWListe(id);
                addSoftwareZubehoer(id, ordner);
                if (!SButton.getText().equals("Save")) {
                    Object[] options = {"Ja",
                        "Nein"};
                    int n = JOptionPane.showOptionDialog(new JFrame(),
                            "Möchten Sie einen weiteren User Anlegen?",
                            "new User",
                            JOptionPane.YES_NO_OPTION,
                            JOptionPane.INFORMATION_MESSAGE,
                            null, //do not use a custom Icon
                            options, //the titles of buttons
                            options[0]);
                    if (n == 0) {
                        newUser ne = new newUser(clients, NetzU, "leer", null, haupt, userInfo2, initSERVER);
                    }
                }
                setVisible(false);
                cleanTempDateien();
                dispose();

            } else {
                JOptionPane.showMessageDialog(new JFrame(), "Diese Personal-Nr. ist schon vergeben!");
            }
        } else {
            JOptionPane.showMessageDialog(new JFrame(), "Fehlende Daten (Vorname, Name, Personal-Nr.)");
        }


    }

    private void addSoftwareZubehoer(int id, String ordner) {
        ArrayList<HashMap> SoftwareDaten = (ArrayList<HashMap>) checkSoftwareZubehoer(InfoSoftware).clone();
        for (int i = 0; i < SoftwareDaten.size(); i++) {
            clients.send("1501" + clients.trennzeichen + id + "" + clients.trennzeichen + SoftwareDaten.get(i).get("IDS") + "" + clients.trennzeichen + clients.checkDaten(SoftwareDaten.get(i).get("Daten")));
        }

        ArrayList<HashMap> ZubehoerDaten = (ArrayList<HashMap>) checkSoftwareZubehoer(InfoZubehoer).clone();
        for (int i = 0; i < ZubehoerDaten.size(); i++) {
            clients.send("1501" + clients.trennzeichen + id + "" + clients.trennzeichen + ZubehoerDaten.get(i).get("IDS") + "" + clients.trennzeichen + clients.checkDaten(ZubehoerDaten.get(i).get("Daten")));
        }

        clients.send("999993" + clients.trennzeichen + ordner + clients.trennzeichen + "Software" + clients.trennzeichen + id);
    }

    private void addPWListe(int ID) {
        try {
            ArrayList<File> files = new ArrayList<File>();
            File datei;
            File file = new File("Daten/Temp/" + ordner);
            file.mkdir();
            for (int i = 0; i < datenPW.size(); i++) {

                datei = new File("Daten/Temp/" + ordner + "/SoftwPW1." + datenPW.get(i).get(8) + ".dpr");
                clients.verschluesseln("Daten/Temp/" + ordner + "/SoftwPW1." + datenPW.get(i).get(8).toString() + ".dpr", datenPW.get(i).get(4).toString());
                files.add(datei);
                datei = new File("Daten/Temp/" + ordner + "/SoftwPW2." + datenPW.get(i).get(8).toString() + ".dpr");
                clients.verschluesseln("Daten/Temp/" + ordner + "/SoftwPW2." + datenPW.get(i).get(8).toString() + ".dpr", datenPW.get(i).get(6).toString());
                files.add(datei);

                clients.send("1403" + clients.trennzeichen + clients.checkDaten(datenPW.get(i).get(1)) + "" + clients.trennzeichen
                        + clients.checkDaten(datenPW.get(i).get(2)) + "" + clients.trennzeichen
                        + clients.checkDaten(datenPW.get(i).get(3)) + "" + clients.trennzeichen
                        + "dpr" + ID + ".zip" + clients.trennzeichen + clients.checkDaten(datenPW.get(i).get(5))
                        + "" + clients.trennzeichen + "dpr" + ID + ".zip" + clients.trennzeichen
                        + ID + "" + clients.trennzeichen
                        + clients.checkDaten(datenPW.get(i).get(8)));


            }
            datei = new File("Daten/Temp/" + ordner + "/UserPW.dpr");
            clients.verschluesseln("Daten/Temp/" + ordner + "/UserPW.dpr", pw.getText());
            files.add(datei);
            datei = new File("Daten/Temp/" + ordner + "/UsereMailPW.dpr");
            clients.verschluesseln("Daten/Temp/" + ordner + "/UsereMailPW.dpr", emailPW.getText());
            files.add(datei);
            clients.sendDateiSoftwareEMail(files, "dpr" + ID, "Software/" + ID, ordner);

        } catch (Exception e) {
        }
    }

    private int giveNewUserID(String pnr) {
        int id = 0;
        ArrayList<ArrayList<String>> DatenNewUser = (ArrayList<ArrayList<String>>) clients.netzwerkVerwaltung.getNewUser("-1", "giveNewUserID",false).clone();
        for (int i = 0; i < DatenNewUser.size(); i++) {
            if (DatenNewUser.get(i).get(3).equals(pnr)) {
                id = Integer.parseInt(DatenNewUser.get(i).get(0).toString());
            }
        }
        return id;
    }

    private ArrayList<HashMap> checkSoftwareZubehoer(ArrayList<HashMap> infoDaten) {
        ArrayList<HashMap> daten = new ArrayList<HashMap>();
        HashMap tmp;
        TabelFont tab;
        String datenTemp = "";

        for (int i = 0; i < infoDaten.size(); i++) {
            if (((JCheckBox) infoDaten.get(i).get("checkBox")).isSelected()) {
                tmp = new HashMap();
                tmp.put("IDS", infoDaten.get(i).get("IDS"));
                tab = (TabelFont) infoDaten.get(i).get("JTable");
                datenTemp = tab.getInfoTable.getValueAt(0, 1).toString();
                for (int a = 1; a < tab.getInfoTable.getRowCount(); a++) {
                    datenTemp = datenTemp + "#" + tab.getInfoTable.getValueAt(a, 1).toString();
                }
                tmp.put("Daten", datenTemp);
                daten.add(tmp);
            } else {
            }
        }
        return daten;
    }
    private boolean vorschauAuswahl = false;

    private void sendEmailKelsterbachUseranlage(String an, String von) {
        String[][] CDaten = {{"Chips", "ProCarS", "ILS", "CIS", "Smart"}, {"-1", "-1", "-1", "-1", "-1"}};

        for (int i = 0; i < CDaten.length; i++) {
            for (int a = 0; a < table.getInfoTable.getRowCount(); a++) {
                if (table.getInfoTable.getValueAt(a, 0).toString().equalsIgnoreCase(CDaten[i][0])) {
                    CDaten[i][1] = "a";
                }
            }
        }

        Date datum = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy");
        ArrayList<String> send = new ArrayList<String>();
        send.add("9000" + clients.trennzeichen + "START" + clients.trennzeichen + an + "" + clients.trennzeichen + von + "; Useranlage" + clients.trennzeichen + User);
        send.add("9000" + clients.trennzeichen + "<b>UserAnlage:</b><br>");
        if (statusInfo.equalsIgnoreCase("Doku")) {
            send.add("9000" + clients.trennzeichen + "<br>Änderungsmeldung / Wechsel<br>");
        } else if (statusInfo.equalsIgnoreCase("delete")) {
            send.add("9000" + clients.trennzeichen + "<br>Löschen<br>");
        } else {
            send.add("9000" + clients.trennzeichen + "<br>Neuantritt<br>");
        }
        send.add("9000" + clients.trennzeichen + "<br><b>Beantragung der Useranlage durch: </b><br>");
        send.add("9000" + clients.trennzeichen + "<br>E-mail: " + von + "<br>");
        send.add("9000" + clients.trennzeichen + "<br><b>Informationen zum User: </b><br>");
        send.add("9000" + clients.trennzeichen + "<br>Vorname: " + clients.checkDaten(vname.getText()));
        send.add("9000" + clients.trennzeichen + "<br>Name: " + clients.checkDaten(Name.getText()));
        send.add("9000" + clients.trennzeichen + "<br>Geschäftsstelle: " + clients.checkDaten(ort.getText()));
        send.add("9000" + clients.trennzeichen + "<br>PLZ: " + clients.checkDaten(plz.getText()));
        send.add("9000" + clients.trennzeichen + "<br>Ort: " + clients.checkDaten(ort.getText()));
        send.add("9000" + clients.trennzeichen + "<br>Strasse: " + clients.checkDaten(strasse.getText()));
        send.add("9000" + clients.trennzeichen + "<br>Abteilung: " + clients.checkDaten(abtl1.getText()));
        send.add("9000" + clients.trennzeichen + "<br>Funktion: " + clients.checkDaten(funktion.getText()));
        send.add("9000" + clients.trennzeichen + "<br>Telefonnummer: " + clients.checkDaten(tel.getText()));
        send.add("9000" + clients.trennzeichen + "<br>Faxnummer: " + clients.checkDaten(fax.getText()));
        send.add("9000" + clients.trennzeichen + "<br>Eintrisdatum: " + formatter.format(datum));
        send.add("9000" + clients.trennzeichen + "<br><br><b>Anlage für alle Produktiobereiche: </b><br>");
        if (system.isSelected()) {
            if (emailCheck.isSelected()) {
                send.add("9000" + clients.trennzeichen + "<br>E-mail: Ja");
            } else {
                send.add("9000" + clients.trennzeichen + "<br>E-mail: Nein");
            }
        } else {
            send.add("9000" + clients.trennzeichen + "<br>E-mail: Nein");
        }

        if (false) {
            send.add("9000" + clients.trennzeichen + "<br>Remote-Zugriff: Ja");
            send.add("9000" + clients.trennzeichen + "<br>Kostenstelle: ");
        } else {
            send.add("9000" + clients.trennzeichen + "<br>Remote-Zugriff: Nein");
            send.add("9000" + clients.trennzeichen + "<br>Kostenstelle: ");
        }

        send.add("9000" + clients.trennzeichen + "<br><br><b>Anlage nur für Produktbereich Luft/See: </b><br>");
        send.add("9000" + clients.trennzeichen + "<br>Information zur Verwendeten Hardware: ");
        send.add("9000" + clients.trennzeichen + "<br>Einsatz eines neuen Rechner/Laptop: ");
        send.add("9000" + clients.trennzeichen + "<br>Einsatz eines bereits im Unternehmen bestehenden Rechner/Laptop: ");
        send.add("9000" + clients.trennzeichen + "<br>Seriennummer und Abteilung: ");
        send.add("9000" + clients.trennzeichen + "<br><br><b>Anlage in folgenden Systemen: </b><br>");
        if (SNovel.isSelected()) {
            send.add("9000" + clients.trennzeichen + "<br>Novell (durch die Zentrale Kelsterbach anzulegen): Ja");
            send.add("9000" + clients.trennzeichen + "<br>Context: " + NovelContext.getSelectedItem());
        } else {
            send.add("9000" + clients.trennzeichen + "<br>Novell (durch die Zentrale Kelsterbach anzulegen): Nein");
            send.add("9000" + clients.trennzeichen + "<br>Context: ");
        }

        send = ships(send);
        send.add("9000" + clients.trennzeichen + "<br><br>Bemerkung: ");

        send = procars(send);
        send = ils(send);
        send = cis(send);
        send = smart(send);
        send = sap(send);
        send.add("9000" + clients.trennzeichen + "<br><br>Bemerkung: ");
        send.add("9000" + clients.trennzeichen + "END");

        if (vorschauAuswahl) {
            vorschauPanel vor = new vorschauPanel(clients);
            vor.setText(send, von, an,"Useranlage");
            vor.setVisible(true);
            vor.setName("Kelsterbach - Useranlage");
            vorschauFenster.addPanel(vor);
        } else {
            clients.ClientVersand(send);
        }

    }
    protected Vorschau vorschauFenster = new Vorschau(this);
    private ArrayList<String> ships(ArrayList<String> send) {
        Boolean weiter = true;
        for (int i = 0; i < InfoSoftware.size(); i++) {
            if (((JTextField) InfoSoftware.get(i).get("Name")).getText().equals("Ships")) {
                if (((JCheckBox) InfoSoftware.get(i).get("checkBox")).isSelected()) {
                    send.add("9000" + clients.trennzeichen + "<br><br>SHIPS: Ja");
                    send.add("9000" + clients.trennzeichen + "<br>SHIPS - Gruppenzuordnung: " + clients.checkDaten(((TabelFont) InfoSoftware.get(i).get("JTable")).getInfoTable.getValueAt(0, 1)));
                    send.add("9000" + clients.trennzeichen + "<br><b>Speditionsbuchhaltung/Controlling</b>");
                    send.add("9000" + clients.trennzeichen + "<br>Berechtigung zum Buchen: " + clients.checkDaten(((TabelFont) InfoSoftware.get(i).get("JTable")).getInfoTable.getValueAt(1, 1)));
                    send.add("9000" + clients.trennzeichen + "<br>Statistiken/Auflistungen: " + clients.checkDaten(((TabelFont) InfoSoftware.get(i).get("JTable")).getInfoTable.getValueAt(2, 1)));
                    send.add("9000" + clients.trennzeichen + "<br>Datenstations-ID: " + clients.checkDaten(((TabelFont) InfoSoftware.get(i).get("JTable")).getInfoTable.getValueAt(3, 1)));
                    send.add("9000" + clients.trennzeichen + "<br><b>Import-Anwendungen</b>");
                    send.add("9000" + clients.trennzeichen + "<br>Verbuchen von Steuerbescheiden: " + clients.checkDaten(((TabelFont) InfoSoftware.get(i).get("JTable")).getInfoTable.getValueAt(4, 1)));
                    send.add("9000" + clients.trennzeichen + "<br>Verzollung: " + clients.checkDaten(((TabelFont) InfoSoftware.get(i).get("JTable")).getInfoTable.getValueAt(5, 1)));
                } else {
                    send.add("9000" + clients.trennzeichen + "<br>SHIPS: Nein");
                    send.add("9000" + clients.trennzeichen + "<br>SHIPS - Gruppenzuordnung: ");
                    send.add("9000" + clients.trennzeichen + "<br><b>Speditionsbuchhaltung/Controlling</b>");
                    send.add("9000" + clients.trennzeichen + "<br>Berechtigung zum Buchen: ");
                    send.add("9000" + clients.trennzeichen + "<br>Statistiken/Auflistungen: ");
                    send.add("9000" + clients.trennzeichen + "<br>Datenstations-ID");
                    send.add("9000" + clients.trennzeichen + "<br><b>Import-Anwendungen</b>");
                    send.add("9000" + clients.trennzeichen + "<br>Verbuchen von Steuerbescheiden: ");
                    send.add("9000" + clients.trennzeichen + "<br>Verzollung: ");
                }
                weiter = false;
                break;
            }
        }
        if (weiter) {
            send.add("9000" + clients.trennzeichen + "<br>SHIPS: Nein");
            send.add("9000" + clients.trennzeichen + "<br>SHIPS - Gruppenzuordnung: ");
            send.add("9000" + clients.trennzeichen + "<br><b>Speditionsbuchhaltung/Controlling</b>");
            send.add("9000" + clients.trennzeichen + "<br>Berechtigung zum Buchen: ");
            send.add("9000" + clients.trennzeichen + "<br>Statistiken/Auflistungen: ");
            send.add("9000" + clients.trennzeichen + "<br>Datenstations-ID");
            send.add("9000" + clients.trennzeichen + "<br><b>Import-Anwendungen</b>");
            send.add("9000" + clients.trennzeichen + "<br>Verbuchen von Steuerbescheiden: ");
            send.add("9000" + clients.trennzeichen + "<br>Verzollung: ");
        }
        return send;
    }

    private ArrayList<String> sap(ArrayList<String> send) {
        Boolean weiter = true;
        for (int i = 0; i < InfoSoftware.size(); i++) {
            if (((JTextField) InfoSoftware.get(i).get("Name")).getText().equalsIgnoreCase("SAP")) {
                if (((JCheckBox) InfoSoftware.get(i).get("checkBox")).isSelected()) {
                    send.add("9000" + clients.trennzeichen + "<br>SAP R/3: JA");
                    send.add("9000" + clients.trennzeichen + "<br>Mit der Funktion: " + clients.checkDaten(((TabelFont) InfoSoftware.get(i).get("JTable")).getInfoTable.getValueAt(0, 1)));
                    send.add("9000" + clients.trennzeichen + "<br>Lizenzübernahme: " + clients.checkDaten(((TabelFont) InfoSoftware.get(i).get("JTable")).getInfoTable.getValueAt(1, 1)));
                    send.add("9000" + clients.trennzeichen + "<br>von: " + clients.checkDaten(((TabelFont) InfoSoftware.get(i).get("JTable")).getInfoTable.getValueAt(2, 1)));
                } else {
                    send.add("9000" + clients.trennzeichen + "<br>SAP R/3: Nein");
                    send.add("9000" + clients.trennzeichen + "<br>Mit der Funktion: ");
                    send.add("9000" + clients.trennzeichen + "<br>Lizenzübernahme: ");
                    send.add("9000" + clients.trennzeichen + "<br>von: ");
                }
                weiter = false;
                break;
            }
        }
        if (weiter) {
            send.add("9000" + clients.trennzeichen + "<br>SAP R/3: Nein");
            send.add("9000" + clients.trennzeichen + "<br>Mit der Funktion: ");
            send.add("9000" + clients.trennzeichen + "<br>Lizenzübernahme: ");
            send.add("9000" + clients.trennzeichen + "<br>von: ");
        }
        return send;
    }

    private ArrayList<String> procars(ArrayList<String> send) {
        Boolean weiter = true;
        for (int i = 0; i < InfoSoftware.size(); i++) {
            if (((JTextField) InfoSoftware.get(i).get("Name")).getText().equalsIgnoreCase("procars")) {
                if (((JCheckBox) InfoSoftware.get(i).get("checkBox")).isSelected()) {
                    send.add("9000" + clients.trennzeichen + "<br><br>ProCarS: Ja");
                    send.add("9000" + clients.trennzeichen + "<br>Security Level: " + clients.checkDaten(((TabelFont) InfoSoftware.get(i).get("JTable")).getInfoTable.getValueAt(0, 1)));
                    send.add("9000" + clients.trennzeichen + "<br>ProCarS - Grupenzuordnung: " + clients.checkDaten(((TabelFont) InfoSoftware.get(i).get("JTable")).getInfoTable.getValueAt(1, 1)));
                } else {
                    send.add("9000" + clients.trennzeichen + "<br><br>ProCarS: Nein");
                    send.add("9000" + clients.trennzeichen + "<br>Security Level: securityLevelBlank");
                    send.add("9000" + clients.trennzeichen + "<br>ProCarS - Grupenzuordnung: securityLevelBlank");
                }
                weiter = false;
                break;
            }
        }
        if (weiter) {
            send.add("9000" + clients.trennzeichen + "<br><br>ProCarS: Nein");
            send.add("9000" + clients.trennzeichen + "<br>Security Level: ");
            send.add("9000" + clients.trennzeichen + "<br>ProCarS - Grupenzuordnung: ");
        }
        return send;
    }

    private ArrayList<String> ils(ArrayList<String> send) {
        Boolean weiter = true;
        for (int i = 0; i < InfoSoftware.size(); i++) {
            if (((JTextField) InfoSoftware.get(i).get("Name")).getText().equalsIgnoreCase("ILS")) {
                if (((JCheckBox) InfoSoftware.get(i).get("checkBox")).isSelected()) {
                    send.add("9000" + clients.trennzeichen + "<br>ILS: Ja");
                    send.add("9000" + clients.trennzeichen + "<br>Projektnummer: " + clients.checkDaten(((TabelFont) InfoSoftware.get(i).get("JTable")).getInfoTable.getValueAt(0, 1)));
                } else {
                    send.add("9000" + clients.trennzeichen + "<br>ILS: Nein");
                    send.add("9000" + clients.trennzeichen + "<br>Projektnummer: ");
                }
                weiter = false;
                break;
            }
        }
        if (weiter) {
            send.add("9000" + clients.trennzeichen + "<br>ILS: Nein");
            send.add("9000" + clients.trennzeichen + "<br>Projektnummer: ");
        }
        return send;
    }

    private ArrayList<String> smart(ArrayList<String> send) {
        Boolean weiter = true;
        for (int i = 0; i < InfoSoftware.size(); i++) {
            if (((JTextField) InfoSoftware.get(i).get("Name")).getText().equalsIgnoreCase("Smart")) {
                if (((JCheckBox) InfoSoftware.get(i).get("checkBox")).isSelected()) {

                    send.add("9000" + clients.trennzeichen + "<br>SMART: Ja");
                    send.add("9000" + clients.trennzeichen + "<br><br><b>Bei der Auswahl von SMART: </b><br>");
                    send.add("9000" + clients.trennzeichen + "<br><br>Laptop: " + ((TabelFont) InfoSoftware.get(i).get("JTable")).getInfoTable.getValueAt(0, 1));
                    send.add("9000" + clients.trennzeichen + "<br>Usergruppe: " + ((TabelFont) InfoSoftware.get(i).get("JTable")).getInfoTable.getValueAt(1, 1));
                    send.add("9000" + clients.trennzeichen + "<br>Office Version: " + ((TabelFont) InfoSoftware.get(i).get("JTable")).getInfoTable.getValueAt(2, 1));
                    send.add("9000" + clients.trennzeichen + "<br>Windows Version: " + ((TabelFont) InfoSoftware.get(i).get("JTable")).getInfoTable.getValueAt(3, 1));
                } else {
                    send.add("9000" + clients.trennzeichen + "<br>SMART: Nein");
                    send.add("9000" + clients.trennzeichen + "<br><br><b>Bei der Auswahl von SMART: </b><br>");
                    send.add("9000" + clients.trennzeichen + "<br>Laptop: ");
                    send.add("9000" + clients.trennzeichen + "<br>Usergruppe: usergruppe blank");
                    send.add("9000" + clients.trennzeichen + "<br>Office Version: ");
                    send.add("9000" + clients.trennzeichen + "<br>Windows Version: ");
                }
                weiter = false;
                break;
            }
        }

        if (weiter) {
            send.add("9000" + clients.trennzeichen + "<br>SMART: Nein");
            send.add("9000" + clients.trennzeichen + "<br><br><b>Bei der Auswahl von SMART: </b>");
            send.add("9000" + clients.trennzeichen + "<br>Laptop: ");
            send.add("9000" + clients.trennzeichen + "<br>Usergruppe: ");
            send.add("9000" + clients.trennzeichen + "<br>Office Version: ");
            send.add("9000" + clients.trennzeichen + "<br>Windows Version: ");
        }
        return send;
    }

    private ArrayList<String> cis(ArrayList<String> send) {
        Boolean weiter = true;
        for (int i = 0; i < InfoSoftware.size(); i++) {
            if (((JTextField) InfoSoftware.get(i).get("Name")).getText().equalsIgnoreCase("CIS")) {
                if (((JCheckBox) InfoSoftware.get(i).get("checkBox")).isSelected()) {
                    send.add("9000" + clients.trennzeichen + "<br>CIS: Ja");
                    send.add("9000" + clients.trennzeichen + "<br>Projektnummer: " + ((TabelFont) InfoSoftware.get(i).get("JTable")).getInfoTable.getValueAt(0, 1));
                } else {
                    send.add("9000" + clients.trennzeichen + "<br>CIS: Nein");
                    send.add("9000" + clients.trennzeichen + "<br>Projektnummer: ");
                }
                weiter = false;
                break;
            }
        }
        if (weiter) {
            send.add("9000" + clients.trennzeichen + "<br>CIS: Nein");
            send.add("9000" + clients.trennzeichen + "<br>Projektnummer: ");
        }
        return send;
    }

    private void sendSoftwareAntrag(String von, HashMap info, String B, String vorschau, String loeschen) {
        String[] ausgeschlossen = {"Ships", "procars", "ILS", "CIS", "SAP"};
        Boolean w = true;
        if (((JCheckBox) info.get("checkBox")).isSelected()) {
            w = true;
            for (int a = 0; a < ausgeschlossen.length; a++) {
                if (ausgeschlossen[a].equalsIgnoreCase(((JTextField) info.get("Name")).getText().toString())) {
                    w = false;
                    break;
                }
            }
            if (w) {
                ArrayList<File> tempFiles = new ArrayList<File>();
                for (int a = 0; a < SoftwareFiles.size(); a++) {
                    if (SoftwareFiles.get(a).get("Name").toString().equals(((JTextField) info.get("Name")).getText())) {
                        tempFiles = (ArrayList<File>) SoftwareFiles.get(a).get("Anhaenge");
                        break;
                    }
                }
                Boolean anhaengeS = false;
                if (tempFiles.isEmpty()) {
                    anhaengeS = false;
                } else {
                    anhaengeS = true;
                }

                ArrayList<String> send = new ArrayList<String>();
                send.add("9000" + clients.trennzeichen + "START" + clients.trennzeichen + info.get("Mail") + "" + clients.trennzeichen + von + "" + clients.trennzeichen + ((JTextField) info.get("Name")).getText() + "" + clients.trennzeichen + User);
                send.add("9000" + clients.trennzeichen + "Sehr geehrter Frau/Herr " + clients.checkDaten(info.get("Verantwortlicher").toString()));

                if (loeschen.equals("ja")) {
                    if (B.equals("0")) {
                        send.add("9000" + clients.trennzeichen + "<br><br>bitte löschen Sie für folgende Person " + ((JTextField) info.get("Name")).getText() + " ein: ");
                    } else if (B.equals("1")) {
                        send.add("9000" + clients.trennzeichen + "<br><br>bitte kündigen Sie für folgende Person " + ((JTextField) info.get("Name")).getText() + " ein: <br>");
                    }
                } else {
                    if (B.equals("0")) {
                        send.add("9000" + clients.trennzeichen + "<br><br>bitte richten Sie für folgende Person " + ((JTextField) info.get("Name")).getText() + " ein: ");
                    } else if (B.equals("1")) {
                        send.add("9000" + clients.trennzeichen + "<br><br>bitte bestellen Sie für folgende Person " + ((JTextField) info.get("Name")).getText() + " ein: <br>");
                    }
                }

                send.add("9000" + clients.trennzeichen + "<br><br>Person: " + clients.checkDaten(vname.getText()) + "  " + clients.checkDaten(Name.getText()));
                for (int c = 0; c < ((TabelFont) info.get("JTable")).getInfoTable.getRowCount(); c++) {
                    send.add("9000" + clients.trennzeichen + "<br>" + clients.checkDaten(((TabelFont) info.get("JTable")).getInfoTable.getValueAt(c, 0).toString()) + ": " + clients.checkDaten(((TabelFont) info.get("JTable")).getInfoTable.getValueAt(c, 1).toString()));
                }
                send = setFuss(send);
                for (int i=0;i<tempFiles.size();i++){
                    send.add("9000"+ clients.trennzeichen +"ANHAENGE"+ clients.trennzeichen +tempFiles.get(i).getAbsolutePath());
                }
                send.add("9000" + clients.trennzeichen + "END" + clients.trennzeichen + anhaengeS + "" + clients.trennzeichen + ((JTextField) info.get("Name")).getText().toString());
                //clients.sendDateiSoftwareEMail(tempFiles, ((JTextField) info.get("Name")).getText().toString(), "eMail/User/", ordner);
                if (vorschau.equalsIgnoreCase("ja")){
                  vorschauPanel vor = new vorschauPanel(clients);
                  vor.setText(send, von, info.get("Mail").toString() , ((JTextField) info.get("Name")).getText().toString());
                  vor.setVisible(true);
                  vor.setName(((JTextField) info.get("Name")).getText().toString());
                  vorschauFenster.addPanel(vor);
                }else
                 clients.ClientVersand(send);
            }

        }
    }

    private ArrayList<String> setFuss(ArrayList<String> send) {
        send.add("9000" + clients.trennzeichen + "<br><br> Mit freundlichen Grüßen");
        send.add("9000" + clients.trennzeichen + "<br><br> " + User);
        send.add("9000" + clients.trennzeichen + "<br><br> Schenker Deutschland AG");
        send.add("9000" + clients.trennzeichen + "<br>Geschäftstelle Bad Krozingen");
        send.add("9000" + clients.trennzeichen + "<br>Elsässer Straße 14");
        send.add("9000" + clients.trennzeichen + "<br>79189 Bad Krozingen");
        send.add("9000" + clients.trennzeichen + "<br>Telefon " + Tel);
        send.add("9000" + clients.trennzeichen + "<br>Fax: " + Fax);
        send.add("9000" + clients.trennzeichen + "<br>www.dbschenker.de ");
        send.add("9000" + clients.trennzeichen + "<br><br>Wir arbeiten ausschließlich auf Grundlage der Allgemeinen Deutschen Spediteurbedingungen (ADSp) und, soweit diese für logistische Leistungen nicht gelten, nach den Logistik-AGB, jeweils neueste Fassung.");
        send.add("9000" + clients.trennzeichen + "<br><b>- Ziffer 23 ADSp beschränkt die gesetzliche Haftung für Güterschäden nach § 431 HGB für Schäden </b>");
        send.add("9000" + clients.trennzeichen + "<br><b>- in speditionellem Gewahrsam auf 5 EUR/kg </b>");
        send.add("9000" + clients.trennzeichen + "<br><b>- bei multimodalen Transporten unter Einschluss einer Seebeförderung auf 2 SZR/kg  (Sonderziehungsrechte)</b>");
        send.add("9000" + clients.trennzeichen + "<br><b>- je Schadenfall auf 1 Mio EUR bzw. 2 SZR/kg, bzw. je Schadenereignis auf 2 Mio. EUR oder 2 SZR/kg je Schadenereignis, unabhängig davon, wie viele Ansprüche aus einem Schadenereignis erhoben werden, je nachdem, welcher Betrag höher ist</b>");
        send.add("9000" + clients.trennzeichen + "<br>Soweit wir nach den Bestimmungen des Montrealer Übereinkommens haften, gilt Ziffer 27 ADSp nicht. ");
        send.add("9000" + clients.trennzeichen + "<br>Ziffer 27 ADSp gilt auch nicht als Erweiterung unserer Haftung durch Zurechnung des Verschuldens von Leuten oder sonstigen Dritten in den Fällen der Art. 36 CIM, Art. 21 CMNI oder § 660 HGB: Im Übrigen bleibt Ziffer 27 ADSp unberührt.");


        return send;
    }

    private void sendActivDirectoryAnmeldung() {
        if (SWindows.isSelected()) {
            ArrayList<String> send2 = new ArrayList<String>();
            send2.add("9001" + clients.trennzeichen + clients.checkDaten(Name.getText()) + "" + clients.trennzeichen
                    + clients.checkDaten(vname.getText()) + "" + clients.trennzeichen
                    + InfoPfad.get(ADSPfad.getSelectedIndex()).get(5) + "" + clients.trennzeichen
                    + clients.checkDaten(funktion.getText()) + "" + clients.trennzeichen
                    + clients.checkDaten(tel.getText()) + "" + clients.trennzeichen
                    + clients.checkDaten(fax.getText()) + "" + clients.trennzeichen
                    + clients.checkDaten(email.getText()) + "" + clients.trennzeichen
                    + clients.checkDaten(abtl1.getText()) + "" + clients.trennzeichen
                    + standort.getSelectedItem().toString());
            clients.ClientVersand(send2);
        }
    }

    public void SoftwareBeantragen() {
        for (int i = 0; i < InfoSoftware.size(); i++) {
            for (int a = 0; a < userBeantragen.fontS.getInfoTable.getRowCount(); a++) {
                if (userBeantragen.fontS.getInfoTable.getValueAt(a, 0).equals(((JTextField) InfoSoftware.get(i).get("Name")).getText())) {
                    if (userBeantragen.fontS.getInfoTable.getValueAt(a, 1).toString().equalsIgnoreCase("Ja")) {
                        sendSoftwareAntrag(UserEmail, InfoSoftware.get(i), "0", vorschau, antragloeschen);
                    }
                    break;
                }
            }
        }




    }

    public void ZubehoerBeantragen() {
        for (int i = 0; i < InfoZubehoer.size(); i++) {
            for (int a = 0; a < userBeantragen.fontH.getInfoTable.getRowCount(); a++) {
                if (userBeantragen.fontH.getInfoTable.getValueAt(a, 0).equals(((JTextField) InfoZubehoer.get(i).get("Name")).getText())) {
                    if (userBeantragen.fontH.getInfoTable.getValueAt(a, 1).toString().equalsIgnoreCase("Ja")) {
                        sendSoftwareAntrag(UserEmail, InfoZubehoer.get(i), "0", vorschau, antragloeschen);
                    }
                    break;
                }
            }
        }
    }

    public void ADSBeantragen() {
        if (system.isSelected()) {
            if (SWindows.isSelected()) {
               // sendActivDirectoryAnmeldung();
            }
        }
    }

    public void KelsterbachBeantragen() {
        if (vorschau.equals("ja")) {
            vorschauAuswahl = true;
            sendEmailKelsterbachUseranlage("itsupport.kelsterbach@dbschenker.com", UserEmail);
        } else {
            sendEmailKelsterbachUseranlage("itsupport.kelsterbach@dbschenker.com", UserEmail);
        }
    }
    private UserBeantragen userBeantragen;

    private void beantragenLoeschen(String Vorschau) {
        vorschau = Vorschau;
        userBeantragen = new UserBeantragen(this);
        ArrayList<String> sh;
        for (int i = 0; i < InfoSoftware.size(); i++) {
            if (((JCheckBox) InfoSoftware.get(i).get("checkBox")).isSelected()) {
                sh = new ArrayList<String>();
                sh.add(((JTextField) InfoSoftware.get(i).get("Name")).getText().toString());
                sh.add("Ja");
                userBeantragen.fontS.getInfoTable.addRow(sh, "-1");
            }
        }
        for (int i = 0; i < InfoZubehoer.size(); i++) {
            if (((JCheckBox) InfoZubehoer.get(i).get("checkBox")).isSelected()) {
                sh = new ArrayList<String>();
                sh.add(((JTextField) InfoZubehoer.get(i).get("Name")).getText().toString());
                sh.add("Ja");
                userBeantragen.fontS.getInfoTable.addRow(sh, "-1");
            }
        }
        userBeantragen.start();
    }

    private void addInfo(ArrayList<String> DatenUser) {

        IDUser = DatenUser.get(0).toString();
        if (DatenUser.get(11).toString().toString().split("#")[0].equals("1")) {

            email.setText(DatenUser.get(11).toString().toString().split("#")[1]);
            emailCheck.setSelected(true);
            email.setEnabled(true);
            emailPW.setEnabled(true);
        }
        if (DatenUser.get(13).toString().toString().split("#")[0].equals("1")) {
            system.setSelected(true);
            if (DatenUser.get(13).toString().toString().split("#")[1].equals("Windows")) {
                SWindows.setSelected(true);
                SWindows.setEnabled(true);
                ADSPfad.setVisible(true);
                ADSPfad.setEnabled(true);
                SNovel.setEnabled(true);
                nickname.setEnabled(true);
                pw.setEnabled(true);
                nickname.setText(DatenUser.get(13).toString().toString().split("#")[3]);
                ADSPfad.setSelectedItem(DatenUser.get(13).toString().toString().split("#")[5]);
            } else {
                SNovel.setSelected(true);
                nickname.setEnabled(true);
                SWindows.setEnabled(true);
                SNovel.setEnabled(true);
                pw.setEnabled(true);
                NovelContext.setEnabled(true);
                NovelContext.setSelectedItem(DatenUser.get(13).toString().toString().split("#")[2]);
                nickname.setText(DatenUser.get(13).toString().toString().split("#")[3]);
            }
        }

        String Status = "";
        if (DatenUser.get(9).toString().equals("Doku")) {
            Status = "1";
            statusInfo = "Doku";
            status.setEnabled(false);
        } else {
            Status = DatenUser.get(9).toString().split("#")[1];
        }

        if (DatenUser.get(12).toString().toString().split("#")[0].equals("1")) {
            laptop.setSelected(true);
            lapSN.setEnabled(true);
            ArrayList<ArrayList<String>> laptopListe = (ArrayList<ArrayList<String>>) clients.netzwerkVerwaltung.getUserLaptop(IDUser, "newUser/addInfo",false).clone();
            initSERVER.infoProgress.setForeground(Color.BLACK);
            initSERVER.infoProgress.setValue(80);
            if (!laptopListe.isEmpty()) {
                lapSN.setSelectedItem(laptopListe.get(0).get(1));
            }

        }


        setAllgemeineInfo(DatenUser);
        setSoftwareZubehoerDaten(InfoSoftware, Status, 90);
        setSoftwareZubehoerDaten(InfoZubehoer, Status, 100);

    }

    private void setAllEnable(boolean w) {
        Name.setEnabled(w);
        vname.setEnabled(w);
        pnr.setEnabled(w);
        standort.setEnabled(w);;
        funktion.setEnabled(w);
        tel.setEnabled(w);
        fax.setEnabled(w);;
        abtl1.setEnabled(w);

        if (system.isSelected()) {
            system.setEnabled(w);
            SWindows.setEnabled(w);
            SNovel.setEnabled(w);
            nickname.setEnabled(w);
            pw.setEnabled(w);
            ADSPfad.setEnabled(w);
            NovelContext.setEnabled(w);
        }
        if (laptop.isSelected()) {
            lapSN.setEnabled(w);
            laptop.setEnabled(w);
        }

        if (emailCheck.isSelected()) {
            emailCheck.setEnabled(w);
            email.setEnabled(w);
            emailPW.setEnabled(w);
        }





    }

    private void setAllgemeineInfo(ArrayList<String> DatenUser) {
        ArrayList<ArrayList<String>> NetzwerkNamen = (ArrayList<ArrayList<String>>) clients.netzwerkVerwaltung.getHardware("-1", "Laptop", "newUser/setAllgeineInfo",false).clone();
        initSERVER.infoProgress.setForeground(Color.BLACK);

        initSERVER.infoProgress.setValue(90);

        for (int i = 0; i < NetzwerkNamen.size(); i++) {
            lapSN.addItem(NetzwerkNamen.get(i).get(1) + "" + clients.trennzeichen + NetzwerkNamen.get(i).get(4));
        }
        IDNUser = DatenUser.get(0).toString();
        Name.setText(DatenUser.get(1).toString());
        vname.setText(DatenUser.get(2).toString());
        pnr.setText(DatenUser.get(3).toString());
        funktion.setText(DatenUser.get(4).toString());
        tel.setText(DatenUser.get(5).toString());
        fax.setText(DatenUser.get(6).toString());
        abtl1.setText(DatenUser.get(7).toString());
        standort.setSelectedItem(DatenUser.get(8));
        if (!DatenUser.get(9).equals("Doku")) {
            status.setSelectedItem(DatenUser.get(9).toString().split("#")[0]);
            if (DatenUser.get(9).toString().split("#")[1].equals("1")) {
                sendUser.setEnabled(true);
                setAllEnable(false);
                bearbeiten.setVisible(true);
                SButton2.setText("aktuallisieren");
            }
            if (DatenUser.get(9).toString().split("#")[2].equals("1")) {
                addDoku.setEnabled(true);
            }
            status.setSelectedItem(DatenUser.get(9).toString().split("#")[0]);
        } else {
            addDoku.setVisible(false);
            SButton2.setText("aktuallisieren");
            bearbeiten.setVisible(false);
            status.setSelectedItem("Doku");
            sendUser.setEnabled(true);
        }

        String[] komentar = DatenUser.get(14).toString().split("\\" + clients.trennzeichen);
        String inhalt = komentar[0];
        for (int i = 1; i < komentar.length; i++) {
            inhalt = inhalt + "\n" + komentar[i];
        }

        kommentar.setText(inhalt);


    }

    private void setSoftwareZubehoerDaten(ArrayList<HashMap> info, String beantragt, int pro) {
        ArrayList<ArrayList<String>> datenSoftware = clients.netzwerkVerwaltung.getNewSoftware(IDNUser, "-1", "newUser/setSoftwareZubehoerDaten",false);
        initSERVER.infoProgress.setForeground(Color.BLACK);
        initSERVER.infoProgress.setValue(pro);
        String[] temp;
        for (int i = 0; i < datenSoftware.size(); i++) {
            for (int a = 0; a < info.size(); a++) {
                if (datenSoftware.get(i).get(0).equals(info.get(a).get("IDS"))) {
                    ((JCheckBox) info.get(a).get("checkBox")).setSelected(true);
                    temp = datenSoftware.get(i).get(7).toString().split("#");
                    for (int c = 0; c < temp.length; c++) {
                        ((TabelFont) info.get(a).get("JTable")).getInfoTable.setValueAt(temp[c], c, 1);
                    }
                    if (beantragt.equals("1")) {
                        ((JCheckBox) info.get(a).get("checkBox")).setEnabled(false);
                        ((TabelFont) info.get(a).get("JTable")).getInfoTable.wert = 3;
                    }
                    break;
                }
            }
        }
    }

    private void sendMail() {
        if (emailCheck.isSelected()) {
            ArrayList<String> send = new ArrayList<String>();
            send.add("9000" + clients.trennzeichen + "START" + clients.trennzeichen + email.getText() + "" + clients.trennzeichen + antrag.getText() + "+User Daten" + clients.trennzeichen + User);
            send.add("9000" + clients.trennzeichen + "Hi " + Name.getText() + ", <br>");

            if (system.isSelected()) {
                send.add("9000" + clients.trennzeichen + "<br>Windows Anmeldung: " + nickname.getText() + " PW: " + pw.getText());
            }
            send.add("9000" + clients.trennzeichen + "<br>eMail: " + email.getText() + " <br>PW: " + emailPW.getText());

            for (int i = 0; i < table.getInfoTable.getRowCount(); i++) {
                send.add("9000" + clients.trennzeichen + "<br><br>Software: " + table.getInfoTable.getValueAt(i, 1) + " <br>Anmeldung (1): " + table.getInfoTable.getValueAt(i, 2) + " <br>Password (1): " + table.getInfoTable.getValueAt(i, 3) + " <br><br>Anmeldung (2): " + table.getInfoTable.getValueAt(i, 4) + " <br>PW (2): " + table.getInfoTable.getValueAt(i, 5) + "<br>");
                send.add("9000" + clients.trennzeichen + "<br>-----------------------<br>");

            }

            send = setFuss(send);
            send.add("9000" + clients.trennzeichen + "END" + clients.trennzeichen + "false+leer");
            clients.ClientVersand(send);
            JOptionPane.showMessageDialog(new JFrame(), "User erfolgreich informiert!");
        }
    }

    private HashMap SoftwareZubehoerPanel(ArrayList<String> Info) {
        TabelFont tabelle = new TabelFont();
        String[] titel = {"Info", "Daten"};
        JTable JTTable = tabelle.getTable(titel, -1, null, -1, null);

        JLabel JLName = new JLabel("Name: ");
        JLName.setBounds(5, 34, 80, 25);
        final JTextField tfName = new JTextField(Info.get(1).toString());
        tfName.setEnabled(false);
        tfName.setBounds(105, 34, 160, 25);

        TabelFont tabelle2 = new TabelFont();
        String[] titel2 = {"Anhang"};
        final JTable JTTable2 = tabelle2.getTable(titel2, -1, null, -1, null);
        JTTable2.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    for (int i = 0; i < SoftwareFiles.size(); i++) {
                        if (SoftwareFiles.get(i).get("Name").equals(tfName.getText())) {
                            File file = (File) ((ArrayList<File>) SoftwareFiles.get(i).get("Anhaenge")).get(JTTable2.getSelectedRow());
                            if (file.exists()) {
                                try {
                                    new ProcessBuilder("cmd", "/c", file.getAbsolutePath()).start();
                                } catch (IOException ex) {
                                }
                            }
                        }
                    }
                }
            }
        });

        tabelle.getInfoTable.wert = 1;

        JPanel softw = new JPanel();
        softw.setBackground(Color.white);
        softw.setLayout(null);
        softw.setName(Info.get(1).toString());

        JCheckBox check = new JCheckBox("Aktiv");
        check.setSelected(false);
        check.setBounds(5, 5, 80, 25);




        JLabel JLFunktion = new JLabel("Funktion: ");
        JLFunktion.setBounds(270, 34, 100, 25);
        JTextField tfFunktion = new JTextField(Info.get(2).toString());
        tfFunktion.setEnabled(false);
        tfFunktion.setBounds(370, 34, 185, 25);

        JLabel JLeinmKosten = new JLabel("einm. Kosten: ");
        JLeinmKosten.setBounds(5, 62, 100, 25);
        JTextField tfeinmKosten = new JTextField(Info.get(3).toString());
        tfeinmKosten.setEnabled(false);
        tfeinmKosten.setBounds(105, 62, 160, 25);

        JLabel JLkosten = new JLabel("montl. Kosten: ");
        JLkosten.setBounds(270, 62, 100, 25);
        JTextField tfkosten = new JTextField(Info.get(4).toString());
        tfkosten.setEnabled(false);
        tfkosten.setBounds(370, 62, 185, 25);



        JLabel JLVerantworlicher = new JLabel("Verantworlicher: ");
        JLVerantworlicher.setBounds(5, 84, 100, 25);
        JTextField tfVerantworlicher = new JTextField(Info.get(5).toString());
        tfVerantworlicher.setEnabled(false);
        tfVerantworlicher.setBounds(105, 84, 160, 25);

        JLabel JLeMail = new JLabel("eMail: ");
        JLeMail.setBounds(270, 84, 100, 25);
        JTextField tfeMail = new JTextField(Info.get(6).toString());
        tfeMail.setEnabled(false);
        tfeMail.setBounds(370, 84, 185, 25);

        JLabel JLDaten = new JLabel("Informationen: ");
        JLDaten.setBounds(5, 114, 120, 25);

        JScrollPane sc2 = new JScrollPane();
        sc2.add(JTTable2);
        sc2.setViewportView(JTTable2);
        sc2.setBounds(375, 144, 180, 120);


        JScrollPane sc = new JScrollPane();
        sc.add(JTTable);
        sc.setViewportView(JTTable);
        sc.setBounds(5, 144, 370, 120);

        String[] DatenInfo = Info.get(7).toString().split("#");
        ArrayList<String> temp;
        for (int i = 0; i < DatenInfo.length; i++) {
            temp = new ArrayList<String>();
            temp.add(DatenInfo[i]);
            temp.add("");
            tabelle.getInfoTable.addRow(temp, "-1");
        }

        softw.add(check);
        softw.add(JLName);
        softw.add(tfName);
        softw.add(JLeinmKosten);
        softw.add(tfeinmKosten);
        softw.add(JLkosten);
        softw.add(tfkosten);
        softw.add(JLFunktion);
        softw.add(tfFunktion);
        softw.add(JLeMail);
        softw.add(tfeMail);
        softw.add(JLVerantworlicher);
        softw.add(tfVerantworlicher);
        softw.add(JLDaten);
        softw.add(sc);
        softw.add(sc2);

        HashMap info = new HashMap();
        info.put("Zweck", tfFunktion);
        info.put("JPanel", softw);
        info.put("Verantwortlicher", tfVerantworlicher.getText());
        info.put("Mail", tfeMail.getText());
        info.put("checkBox", check);
        info.put("IDS", Info.get(0).toString());
        info.put("Name", tfName);
        info.put("JTable", tabelle);
        info.put("JTable2", tabelle2);


        return info;
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        buttonGroup2 = new javax.swing.ButtonGroup();
        jPanel5 = new javax.swing.JPanel();
        SButton = new javax.swing.JButton();
        Auswahl = new javax.swing.JTabbedPane();
        jPanel1 = new javax.swing.JPanel();
        standort = new javax.swing.JComboBox();
        jLabel3 = new javax.swing.JLabel();
        vname = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        fax = new javax.swing.JTextField();
        tel = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        funktion = new javax.swing.JTextField();
        abtl1 = new javax.swing.JTextField();
        jLabel13 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        Name = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        pnr = new javax.swing.JTextField();
        strasse = new javax.swing.JTextField();
        jLabel22 = new javax.swing.JLabel();
        jLabel28 = new javax.swing.JLabel();
        plz = new javax.swing.JTextField();
        jLabel29 = new javax.swing.JLabel();
        ort = new javax.swing.JTextField();
        jPanel2 = new javax.swing.JPanel();
        system = new javax.swing.JCheckBox();
        jLabel27 = new javax.swing.JLabel();
        ADSPfad = new javax.swing.JComboBox();
        SWindows = new javax.swing.JRadioButton();
        SNovel = new javax.swing.JRadioButton();
        jLabel15 = new javax.swing.JLabel();
        NovelContext = new javax.swing.JComboBox();
        jLabel11 = new javax.swing.JLabel();
        nickname = new javax.swing.JTextField();
        jLabel12 = new javax.swing.JLabel();
        laptop = new javax.swing.JCheckBox();
        lapSN = new javax.swing.JComboBox();
        emailCheck = new javax.swing.JCheckBox();
        email = new javax.swing.JTextField();
        jLabel16 = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        emailPW = new javax.swing.JPasswordField();
        pw = new javax.swing.JPasswordField();
        jButton2 = new javax.swing.JButton();
        ePWanzeige = new javax.swing.JLabel();
        spw = new javax.swing.JLabel();
        jPanel6 = new javax.swing.JPanel();
        auswahlZubehoer = new javax.swing.JTabbedPane();
        jPanel13 = new javax.swing.JPanel();
        AuswahlSoftware = new javax.swing.JTabbedPane();
        jPanel3 = new javax.swing.JPanel();
        userSoft = new javax.swing.JScrollPane();
        jButton1 = new javax.swing.JButton();
        jLabel9 = new javax.swing.JLabel();
        PWName = new javax.swing.JTextField();
        jLabel14 = new javax.swing.JLabel();
        PWZweck = new javax.swing.JTextField();
        jButton3 = new javax.swing.JButton();
        jLabel17 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        jButton4 = new javax.swing.JButton();
        jLabel19 = new javax.swing.JLabel();
        user01 = new javax.swing.JTextField();
        jLabel20 = new javax.swing.JLabel();
        user02 = new javax.swing.JTextField();
        pw01 = new javax.swing.JPasswordField();
        pw02 = new javax.swing.JPasswordField();
        sendUser = new javax.swing.JButton();
        addDoku = new javax.swing.JButton();
        jLabel10 = new javax.swing.JLabel();
        kommentar = new javax.swing.JTextArea();
        SButton1 = new javax.swing.JButton();
        SButton2 = new javax.swing.JButton();
        status = new javax.swing.JComboBox();
        jLabel1 = new javax.swing.JLabel();
        antrag = new javax.swing.JTextField();
        bearbeiten = new javax.swing.JButton();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        jMenuItem1 = new javax.swing.JMenuItem();
        jMenuItem9 = new javax.swing.JMenuItem();
        jMenuItem2 = new javax.swing.JMenuItem();
        jMenu4 = new javax.swing.JMenu();
        jMenuItem7 = new javax.swing.JMenuItem();
        jMenuItem8 = new javax.swing.JMenuItem();
        jMenu3 = new javax.swing.JMenu();
        jMenuItem6 = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("User Anlage");
        setBackground(new java.awt.Color(153, 153, 153));
        setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        setIconImages(null);
        setModalExclusionType(null);
        setResizable(false);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                windowsClosing(evt);
            }
        });

        jPanel5.setBackground(javax.swing.UIManager.getDefaults().getColor("InternalFrame.activeTitleGradient"));

        SButton.setText("add");
        SButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                SButtonActionPerformed(evt);
            }
        });

        Auswahl.setBackground(new java.awt.Color(204, 204, 204));

        jPanel1.setBackground(new java.awt.Color(133, 133, 149));

        standort.setBackground(new java.awt.Color(204, 204, 204));
        standort.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        standort.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                standortActionPerformed(evt);
            }
        });

        jLabel3.setBackground(java.awt.Color.white);
        jLabel3.setText("Vorname:");

        vname.setHorizontalAlignment(javax.swing.JTextField.LEFT);

        jLabel5.setBackground(java.awt.Color.white);
        jLabel5.setText("Funktion:");

        jLabel8.setBackground(java.awt.Color.white);
        jLabel8.setText("Abteilung:");

        fax.setHorizontalAlignment(javax.swing.JTextField.LEFT);

        tel.setHorizontalAlignment(javax.swing.JTextField.LEFT);

        jLabel2.setBackground(java.awt.Color.white);
        jLabel2.setText("Name:");

        jLabel6.setBackground(java.awt.Color.white);
        jLabel6.setText("Tel.:");

        funktion.setHorizontalAlignment(javax.swing.JTextField.LEFT);

        abtl1.setHorizontalAlignment(javax.swing.JTextField.LEFT);

        jLabel13.setBackground(java.awt.Color.white);
        jLabel13.setText("Standort:");

        jLabel7.setBackground(java.awt.Color.white);
        jLabel7.setText("Fax:");

        Name.setHorizontalAlignment(javax.swing.JTextField.LEFT);
        Name.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                NameActionPerformed(evt);
            }
        });

        jLabel4.setBackground(java.awt.Color.white);
        jLabel4.setText("Personal-Nr.:");

        pnr.setHorizontalAlignment(javax.swing.JTextField.LEFT);

        strasse.setBackground(new java.awt.Color(204, 204, 204));
        strasse.setEditable(false);
        strasse.setHorizontalAlignment(javax.swing.JTextField.LEFT);
        strasse.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                strasseActionPerformed(evt);
            }
        });

        jLabel22.setBackground(java.awt.Color.white);
        jLabel22.setText("Strasse:");

        jLabel28.setBackground(java.awt.Color.white);
        jLabel28.setText("PLZ:");

        plz.setBackground(new java.awt.Color(204, 204, 204));
        plz.setEditable(false);
        plz.setHorizontalAlignment(javax.swing.JTextField.LEFT);

        jLabel29.setBackground(java.awt.Color.white);
        jLabel29.setText("Ort:");

        ort.setBackground(new java.awt.Color(204, 204, 204));
        ort.setEditable(false);
        ort.setHorizontalAlignment(javax.swing.JTextField.LEFT);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel13)
                        .addGap(29, 29, 29)
                        .addComponent(standort, 0, 481, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel28)
                                    .addComponent(jLabel22))
                                .addGap(35, 35, 35)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addComponent(plz, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 239, Short.MAX_VALUE)
                                    .addComponent(strasse, javax.swing.GroupLayout.Alignment.LEADING)))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel3)
                                    .addComponent(jLabel2)
                                    .addComponent(jLabel4))
                                .addGap(11, 11, 11)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(Name, javax.swing.GroupLayout.DEFAULT_SIZE, 239, Short.MAX_VALUE)
                                    .addComponent(vname)
                                    .addComponent(pnr))))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel5)
                            .addComponent(jLabel7)
                            .addComponent(jLabel8)
                            .addComponent(jLabel6)
                            .addComponent(jLabel29))
                        .addGap(6, 6, 6)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(ort)
                            .addComponent(fax)
                            .addComponent(tel)
                            .addComponent(abtl1, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(funktion, javax.swing.GroupLayout.Alignment.TRAILING))))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel13)
                    .addComponent(standort, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel22)
                    .addComponent(strasse, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel29)
                    .addComponent(ort, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel28, javax.swing.GroupLayout.PREFERRED_SIZE, 12, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(plz, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel2)
                            .addComponent(Name, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(5, 5, 5)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 12, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(vname, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel4)
                            .addComponent(pnr, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel5)
                            .addComponent(funktion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(5, 5, 5)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel6)
                            .addComponent(tel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel7)
                            .addComponent(fax, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel8)
                            .addComponent(abtl1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(190, Short.MAX_VALUE))
        );

        Auswahl.addTab("Allgemein", jPanel1);

        jPanel2.setBackground(new java.awt.Color(133, 133, 149));
        jPanel2.setEnabled(false);

        system.setBackground(new java.awt.Color(133, 133, 149));
        system.setText("System Anmeldung");
        system.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                systemActionPerformed(evt);
            }
        });

        jLabel27.setBackground(java.awt.Color.white);
        jLabel27.setText("Pfad:");

        ADSPfad.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Terminal User", "Laptop User", "PC User" }));
        ADSPfad.setEnabled(false);
        ADSPfad.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ADSPfadActionPerformed(evt);
            }
        });

        SWindows.setBackground(new java.awt.Color(133, 133, 149));
        buttonGroup1.add(SWindows);
        SWindows.setSelected(true);
        SWindows.setText("Windows");
        SWindows.setEnabled(false);
        SWindows.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                SWindowsActionPerformed(evt);
            }
        });

        SNovel.setBackground(new java.awt.Color(133, 133, 149));
        buttonGroup1.add(SNovel);
        SNovel.setText("Novel");
        SNovel.setEnabled(false);
        SNovel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                SNovelActionPerformed(evt);
            }
        });

        jLabel15.setBackground(java.awt.Color.white);
        jLabel15.setText("Contex:");

        NovelContext.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Import", "Export_Air", "Export_Sea", "Laptop_User", "Leitung" }));
        NovelContext.setEnabled(false);
        NovelContext.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                NovelContextActionPerformed(evt);
            }
        });

        jLabel11.setBackground(java.awt.Color.white);
        jLabel11.setText("Nickname:");

        nickname.setDisabledTextColor(new java.awt.Color(255, 255, 255));
        nickname.setEnabled(false);

        jLabel12.setBackground(java.awt.Color.white);
        jLabel12.setText("PW:");

        laptop.setBackground(new java.awt.Color(133, 133, 149));
        laptop.setText("Laptop / PC");
        laptop.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                laptopActionPerformed(evt);
            }
        });

        lapSN.setEnabled(false);

        emailCheck.setBackground(new java.awt.Color(133, 133, 149));
        emailCheck.setText("eMail");
        emailCheck.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                emailCheckActionPerformed(evt);
            }
        });

        email.setEnabled(false);

        jLabel16.setBackground(java.awt.Color.white);
        jLabel16.setText("PW:");

        emailPW.setEnabled(false);
        emailPW.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                emailPWActionPerformed(evt);
            }
        });
        emailPW.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                ePW(evt);
            }
        });

        pw.setEnabled(false);
        pw.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                pwActionPerformed(evt);
            }
        });
        pw.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                spw(evt);
            }
        });

        jButton2.setText("PW anzeigen");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(system)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel2Layout.createSequentialGroup()
                                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(emailCheck, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(jLabel16)
                                            .addComponent(laptop))
                                        .addGap(18, 18, 18)
                                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                            .addComponent(email)
                                            .addComponent(lapSN, 0, 345, Short.MAX_VALUE)
                                            .addGroup(jPanel2Layout.createSequentialGroup()
                                                .addComponent(emailPW, javax.swing.GroupLayout.PREFERRED_SIZE, 172, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(ePWanzeige, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                                    .addGroup(jPanel2Layout.createSequentialGroup()
                                        .addGap(11, 11, 11)
                                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(SWindows)
                                            .addGroup(jPanel2Layout.createSequentialGroup()
                                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                                    .addGroup(jPanel2Layout.createSequentialGroup()
                                                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                                            .addComponent(SNovel)
                                                            .addGroup(jPanel2Layout.createSequentialGroup()
                                                                .addComponent(jLabel27)
                                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                                .addComponent(ADSPfad, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                .addGap(51, 51, 51)
                                                                .addComponent(jLabel15)))
                                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                        .addComponent(NovelContext, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                    .addGroup(jPanel2Layout.createSequentialGroup()
                                                        .addComponent(jLabel11)
                                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                        .addComponent(nickname, javax.swing.GroupLayout.PREFERRED_SIZE, 123, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                        .addComponent(jLabel12)
                                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                        .addComponent(pw, javax.swing.GroupLayout.PREFERRED_SIZE, 117, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(spw, javax.swing.GroupLayout.PREFERRED_SIZE, 161, javax.swing.GroupLayout.PREFERRED_SIZE)))))))
                        .addGap(0, 53, Short.MAX_VALUE))
                    .addComponent(jSeparator1, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jButton2)))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(system)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(SWindows)
                    .addComponent(SNovel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(ADSPfad, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel27)
                    .addComponent(jLabel15)
                    .addComponent(NovelContext, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(nickname)
                        .addComponent(jLabel11)
                        .addComponent(jLabel12)
                        .addComponent(pw))
                    .addComponent(spw, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(1, 1, 1)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(laptop)
                    .addComponent(lapSN, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(emailCheck)
                    .addComponent(email, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(ePWanzeige, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel16)
                        .addComponent(emailPW, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 115, Short.MAX_VALUE)
                .addComponent(jButton2)
                .addContainerGap())
        );

        Auswahl.addTab("System", jPanel2);

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(auswahlZubehoer, javax.swing.GroupLayout.DEFAULT_SIZE, 576, Short.MAX_VALUE)
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(auswahlZubehoer, javax.swing.GroupLayout.DEFAULT_SIZE, 352, Short.MAX_VALUE)
        );

        Auswahl.addTab("Zubehör", jPanel6);

        javax.swing.GroupLayout jPanel13Layout = new javax.swing.GroupLayout(jPanel13);
        jPanel13.setLayout(jPanel13Layout);
        jPanel13Layout.setHorizontalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(AuswahlSoftware, javax.swing.GroupLayout.DEFAULT_SIZE, 576, Short.MAX_VALUE)
        );
        jPanel13Layout.setVerticalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(AuswahlSoftware, javax.swing.GroupLayout.DEFAULT_SIZE, 352, Short.MAX_VALUE)
        );

        Auswahl.addTab("Software", jPanel13);

        jPanel3.setBackground(new java.awt.Color(255, 255, 255));

        userSoft.setBackground(new java.awt.Color(255, 255, 255));
        userSoft.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jButton1.setText("Add");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jLabel9.setText("Name:");

        PWName.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                PWNameActionPerformed(evt);
            }
        });

        jLabel14.setText("Zweck:");

        jButton3.setText("PW anzeigen");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jLabel17.setText("PW (1):");

        jLabel18.setText("PW (2):");

        jButton4.setText("hinzufügen");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        jLabel19.setText("User (1):");

        jLabel20.setText("User (2):");

        pw01.setColumns(16);
        pw01.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                pw01ActionPerformed(evt);
            }
        });

        pw02.setColumns(16);

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(userSoft)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel9)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(PWName, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel14)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(PWZweck, javax.swing.GroupLayout.PREFERRED_SIZE, 146, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButton3))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addComponent(jLabel19)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(user01, javax.swing.GroupLayout.DEFAULT_SIZE, 190, Short.MAX_VALUE))
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addComponent(jLabel20)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(user02)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel17)
                            .addComponent(jLabel18))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(pw02, javax.swing.GroupLayout.DEFAULT_SIZE, 177, Short.MAX_VALUE)
                            .addComponent(pw01))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton4)))
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel9)
                    .addComponent(PWName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel14)
                    .addComponent(PWZweck, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton1)
                    .addComponent(jButton3))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(userSoft, javax.swing.GroupLayout.DEFAULT_SIZE, 246, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel19)
                    .addComponent(user01, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel17)
                    .addComponent(pw01, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel20)
                    .addComponent(user02, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel18)
                    .addComponent(jButton4)
                    .addComponent(pw02, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        Auswahl.addTab("PW Lister", jPanel3);

        sendUser.setText("send to User");
        sendUser.setEnabled(false);
        sendUser.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                sendUserActionPerformed(evt);
            }
        });

        addDoku.setText("add Doku ");
        addDoku.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addDokuActionPerformed(evt);
            }
        });

        jLabel10.setText("Kommentar:");

        kommentar.setColumns(20);
        kommentar.setRows(2);
        kommentar.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        SButton1.setText("del");
        SButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                SButton1ActionPerformed(evt);
            }
        });

        SButton2.setText("Beantragen");
        SButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                SButton2ActionPerformed(evt);
            }
        });

        status.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "neu", "in Arbeit", "warten", "Doku" }));

        jLabel1.setText("User:");

        antrag.setEditable(false);

        bearbeiten.setText("bearbeiten");
        bearbeiten.setVisible(false);
        bearbeiten.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bearbeitenActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addComponent(jLabel10)
                .addContainerGap())
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(status, javax.swing.GroupLayout.PREFERRED_SIZE, 89, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(antrag, javax.swing.GroupLayout.PREFERRED_SIZE, 205, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(bearbeiten)
                .addContainerGap())
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(SButton1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(SButton2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(sendUser)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(addDoku)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(SButton))
                    .addComponent(kommentar))
                .addGap(14, 14, 14))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(Auswahl, javax.swing.GroupLayout.PREFERRED_SIZE, 581, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(status, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1)
                    .addComponent(antrag, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(bearbeiten))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(Auswahl, javax.swing.GroupLayout.PREFERRED_SIZE, 380, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(addDoku)
                    .addComponent(sendUser)
                    .addComponent(SButton2)
                    .addComponent(SButton1)
                    .addComponent(SButton))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel10)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(kommentar, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jMenu1.setText("Datei");

        jMenuItem1.setText("Speichern");
        jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem1ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem1);

        jMenuItem9.setText("Löschen");
        jMenuItem9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem9ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem9);

        jMenuItem2.setText("Beenden");
        jMenuItem2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem2ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem2);

        jMenuBar1.add(jMenu1);

        jMenu4.setText("Bearbeiten");

        jMenuItem7.setText("send to User");
        jMenuItem7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem7ActionPerformed(evt);
            }
        });
        jMenu4.add(jMenuItem7);

        jMenuItem8.setText("Create Dokumentation");
        jMenuItem8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem8ActionPerformed(evt);
            }
        });
        jMenu4.add(jMenuItem8);

        jMenuBar1.add(jMenu4);

        jMenu3.setText("?");

        jMenuItem6.setText("Info");
        jMenuItem6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem6ActionPerformed(evt);
            }
        });
        jMenu3.add(jMenuItem6);

        jMenuBar1.add(jMenu3);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

private void standortActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_standortActionPerformed
    strasse.setText(standortDaten.get(standort.getSelectedIndex()).get(2).toString());
    plz.setText(standortDaten.get(standort.getSelectedIndex()).get(3).toString());
    ort.setText(standortDaten.get(standort.getSelectedIndex()).get(4).toString());
}//GEN-LAST:event_standortActionPerformed

private void NameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_NameActionPerformed
// TODO add your handling code here:
}//GEN-LAST:event_NameActionPerformed

private void SWindowsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_SWindowsActionPerformed
    nickname.setText(vname.getText() + "." + Name.getText());
}//GEN-LAST:event_SWindowsActionPerformed

private void SNovelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_SNovelActionPerformed
    if (SNovel.isSelected()) {
        NovelContext.setEnabled(true);
        nickname.setText(Name.getText());
    } else {
        NovelContext.setEnabled(false);
    }
}//GEN-LAST:event_SNovelActionPerformed

private void emailCheckActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_emailCheckActionPerformed
    if (emailCheck.isSelected()) {
        email.setEnabled(true);
        emailPW.setEnabled(true);
        email.setText(vname.getText() + "." + Name.getText() + "@dbschenker.com");
    } else {
        email.setEnabled(false);
        emailPW.setEnabled(false);
        email.setText("");
    }


}//GEN-LAST:event_emailCheckActionPerformed

private void sendUserActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_sendUserActionPerformed

    if (statusInfo.equals("Doku")) {
        Object[] option = {"Ja, Info an User", "Abrechen"};
        int e = JOptionPane.showOptionDialog(new JFrame(),
                "Möchten Sie den User Informieren?",
                "Userbearbeiten",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.INFORMATION_MESSAGE,
                null, //do not use a custom Icon
                option, //the titles of buttons
                option[0]);
        if (e == 0) {
            sendMail();
            save();
        }
    } else {
        Object[] option = {"Ja, Info an User (ohne Ablage)", "Nein, nur Ablage", "Abrechen"};
        int e = JOptionPane.showOptionDialog(new JFrame(),
                "Möchten Sie den User Informieren?",
                "Userbearbeiten",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.INFORMATION_MESSAGE,
                null, //do not use a custom Icon
                option, //the titles of buttons
                option[0]);
        statusInfo = status.getSelectedItem() + "#1#1";
        if (e == 0) {
            sendMail();
            save();
        } else if (e == 1) {
            statusInfo = "Doku";
            save();
        }
    }


}//GEN-LAST:event_sendUserActionPerformed

private void addDokuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addDokuActionPerformed
    Object[] options = {"Ja",
        "Nein (abrechen)"};
    int n = JOptionPane.showOptionDialog(new JFrame(),
            "Hiermit wird der User Abgelegt. Bitte beachten Sie das der User nicht mehr in TODO+Liste zu sehen ist.\n soll forgefahren werden?",
            "Dokumenten Ablage",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.INFORMATION_MESSAGE,
            null, //do not use a custom Icon
            options, //the titles of buttons
            options[0]);


    if (n == 0) {
        save();
        clients.send("2002" + clients.trennzeichen + "NewUser" + clients.trennzeichen + "Daten9" + clients.trennzeichen + "Doku" + clients.trennzeichen + "IDNUser" + clients.trennzeichen + IDNUser);
        this.setVisible(false);
        this.dispose();

    } else {
    }
}//GEN-LAST:event_addDokuActionPerformed

private void systemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_systemActionPerformed
    if (system.isSelected()) {
        SWindows.setEnabled(true);
        ADSPfad.setEnabled(true);
        SNovel.setEnabled(true);
        nickname.setEnabled(true);
        pw.setEnabled(true);
        nickname.setText(vname.getText() + "." + Name.getText());
    } else {
        SWindows.setEnabled(false);
        ADSPfad.setEnabled(false);
        SNovel.setEnabled(false);
        NovelContext.setEnabled(false);
        nickname.setEnabled(false);
        pw.setEnabled(false);
        nickname.setText("");
        pw.setText("");
    }
}//GEN-LAST:event_systemActionPerformed

private void laptopActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_laptopActionPerformed
    if (laptop.isSelected()) {
        lapSN.setEnabled(true);
    } else {
        lapSN.setEnabled(false);
    }
}//GEN-LAST:event_laptopActionPerformed

private void NovelContextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_NovelContextActionPerformed
// TODO add your handling code here:
}//GEN-LAST:event_NovelContextActionPerformed

private void SButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_SButtonActionPerformed
    save();
}//GEN-LAST:event_SButtonActionPerformed
    private String antragloeschen = "nein";
private void SButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_SButton1ActionPerformed
    Object[] options = {"Ja",
        "Nein", "Abrechen"};
    int n = JOptionPane.showOptionDialog(new JFrame(),
            "Hiermit wird der User Gelöscht. .\n soll forgefahren werden?",
            "Eintrag Entfernen",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.INFORMATION_MESSAGE,
            null, //do not use a custom Icon
            options, //the titles of buttons
            options[0]);


    if (n == 0) {

        int c = JOptionPane.showOptionDialog(new JFrame(),
                "Vorschau?",
                "Eintrag Entfernen",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.INFORMATION_MESSAGE,
                null, //do not use a custom Icon
                options, //the titles of buttons
                options[0]);


        if (c == 0) {
            vorschau = "ja";
            statusInfo = "delete";
            antragloeschen = "ja";
            beantragenLoeschen(vorschau);


        } else if (c == 1) {
            vorschau = "nein";
            statusInfo = "delete";
            antragloeschen = "ja";
            beantragenLoeschen(vorschau);

        }
        clients.send("2004" + clients.trennzeichen + IDUser + ";Software");
        clients.send("2001" + clients.trennzeichen + "NewUser" + clients.trennzeichen + pnr.getText() + "" + clients.trennzeichen + "Daten3");
        clients.send("2001" + clients.trennzeichen + "SoftwareNewUser" + clients.trennzeichen + IDUser + "" + clients.trennzeichen + "IDNUser");

    }
    cleanTempDateien();
    this.setVisible(false);
    this.dispose();
}//GEN-LAST:event_SButton1ActionPerformed

private void SButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_SButton2ActionPerformed
    if (!Name.getText().equals("") && !vname.getText().equals("") && !pnr.getText().equals("")) {
        Object[] options = {"Beantragen",
            "Vorschau", "Abrechen"};
        int n = JOptionPane.showOptionDialog(new JFrame(),
                "Möchten Sie zuerste eine Vorschau sehen?",
                "User Beantragen",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.INFORMATION_MESSAGE,
                null, //do not use a custom Icon
                options, //the titles of buttons
                options[0]);


        if (n == 0) {
            beantragenLoeschen("nein");
            if (!statusInfo.equalsIgnoreCase("Doku")) {
                statusInfo = status.getSelectedItem() + "#1#0";
            }
            save();
        } else if (n == 1) {
            beantragenLoeschen("ja");
            if (!statusInfo.equalsIgnoreCase("Doku")) {
                statusInfo = status.getSelectedItem() + "#1#0";
            }
            //save();
        } else {
           // save();
        }
    } else {
        JOptionPane.showMessageDialog(new JFrame(), "Fehlende Angaben (Name, Vorname, Personal-Nr.)");
    }
}//GEN-LAST:event_SButton2ActionPerformed

private void ADSPfadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ADSPfadActionPerformed
// TODO add your handling code here:
}//GEN-LAST:event_ADSPfadActionPerformed

    private void strasseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_strasseActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_strasseActionPerformed

    private void windowsClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_windowsClosing
        cleanTempDateien();
    }//GEN-LAST:event_windowsClosing

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        ArrayList<String> temp = new ArrayList<String>();
        temp.add(PWName.getText());
        temp.add(PWZweck.getText());
        temp.add("");
        temp.add("");
        temp.add("");
        temp.add("");
        table.getInfoTable.addRow(temp, "-1");

        ArrayList<String> temp2 = new ArrayList<String>();

        temp2.add("-" + (table.getInfoTable.getRowCount() - 1));
        temp2.add(PWName.getText());
        temp2.add(PWZweck.getText());
        temp2.add("");
        temp2.add("");
        temp2.add("***");
        temp2.add("***");
        temp2.add("-" + (table.getInfoTable.getRowCount() - 1));
        datenPW.add(temp2);

        PWName.setText("");
        PWZweck.setText("");
    }//GEN-LAST:event_jButton1ActionPerformed
    public newHardware newServerClientDaten = new newHardware();
    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        if (jButton2.getText().equals("PW anzeigen")) {
            ePWanzeige.setText(emailPW.getText());
            spw.setText(pw.getText());
            jButton2.setText("PW ausblenden");
        } else {
            ePWanzeige.setText("");
            spw.setText("");
            jButton2.setText("PW anzeigen");
        }

    }//GEN-LAST:event_jButton2ActionPerformed

    private void setTableInfo(boolean verschluesselt) {
        if (verschluesselt) {
        }
    }
    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        if (jButton3.getText().equals("PW anzeigen")) {
            verschluesselt = false;
            showPWList(verschluesselt);
            jButton3.setText("PW ausblenden");
        } else {
            showPWList(true);
            verschluesselt = true;
            jButton3.setText("PW anzeigen");
        }
    }//GEN-LAST:event_jButton3ActionPerformed

    private void ePW(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_ePW
    }//GEN-LAST:event_ePW

    private void spw(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_spw
    }//GEN-LAST:event_spw

    private void pwActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_pwActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_pwActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        ArrayList<ArrayList<String>> info = new ArrayList<ArrayList<String>>();
        if (tabTable.getSelectedRow() != -1) {
            for (int a = 0; a < datenPW.size(); a++) {
                if (a == tabTable.getSelectedRow()) {
                    ArrayList<String> daten = new ArrayList<String>();
                    daten.add(datenPW.get(a).get(0));
                    daten.add(tabTable.getValueAt(tabTable.getSelectedRow(), 0).toString());
                    daten.add(tabTable.getValueAt(tabTable.getSelectedRow(), 1).toString());
                    daten.add(clients.checkDaten(user01.getText()));
                    daten.add(pw01.getText());
                    daten.add(clients.checkDaten(user02.getText()));
                    daten.add(pw02.getText());
                    daten.add(datenPW.get(a).get(7));
                    daten.add(datenPW.get(a).get(8));
                    info.add(daten);
                } else {
                    info.add(datenPW.get(a));
                }
            }
            datenPW = (ArrayList<ArrayList<String>>) info.clone();
            showPWList(verschluesselt);
        }
    }//GEN-LAST:event_jButton4ActionPerformed

    private void PWNameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_PWNameActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_PWNameActionPerformed

    private void pw01ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_pw01ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_pw01ActionPerformed

    private void emailPWActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_emailPWActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_emailPWActionPerformed

    private void bearbeitenActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bearbeitenActionPerformed
        setAllEnable(true);
    }//GEN-LAST:event_bearbeitenActionPerformed

    private void jMenuItem6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem6ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jMenuItem6ActionPerformed

    private void jMenuItem7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem7ActionPerformed
        if (statusInfo.equals("Doku")) {
            Object[] option = {"Ja, Info an User", "Abrechen"};
            int e = JOptionPane.showOptionDialog(new JFrame(),
                    "Möchten Sie den User Informieren?",
                    "Userbearbeiten",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.INFORMATION_MESSAGE,
                    null, //do not use a custom Icon
                    option, //the titles of buttons
                    option[0]);
            if (e == 0) {
                sendMail();
                save();
            }
        } else {
            Object[] option = {"Ja, Info an User (ohne Ablage)", "Nein, nur Ablage", "Abrechen"};
            int e = JOptionPane.showOptionDialog(new JFrame(),
                    "Möchten Sie den User Informieren?",
                    "Userbearbeiten",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.INFORMATION_MESSAGE,
                    null, //do not use a custom Icon
                    option, //the titles of buttons
                    option[0]);
            statusInfo = status.getSelectedItem() + "#1#1";
            if (e == 0) {
                sendMail();
                save();
            } else if (e == 1) {
                statusInfo = "Doku";
                save();
            }
        }
    }//GEN-LAST:event_jMenuItem7ActionPerformed

    private void jMenuItem8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem8ActionPerformed
        Object[] options = {"Ja",
            "Nein (abrechen)"};
        int n = JOptionPane.showOptionDialog(new JFrame(),
                "Hiermit wird der User Abgelegt. Bitte beachten Sie das der User nicht mehr in TODO+Liste zu sehen ist.\n soll forgefahren werden?",
                "Dokumenten Ablage",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.INFORMATION_MESSAGE,
                null, //do not use a custom Icon
                options, //the titles of buttons
                options[0]);


        if (n == 0) {
            save();
            clients.send("2002" + clients.trennzeichen + "NewUser" + clients.trennzeichen + "Daten9" + clients.trennzeichen + "Doku" + clients.trennzeichen + "IDNUser" + clients.trennzeichen + IDNUser);
            this.setVisible(false);
            this.dispose();

        } else {
        }
    }//GEN-LAST:event_jMenuItem8ActionPerformed

    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem1ActionPerformed
        save();
    }//GEN-LAST:event_jMenuItem1ActionPerformed

    private void jMenuItem9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem9ActionPerformed
        Object[] options = {"Ja",
            "Nein", "Abrechen"};
        int n = JOptionPane.showOptionDialog(new JFrame(),
                "Hiermit wird der User Gelöscht. .\n soll forgefahren werden?",
                "Eintrag Entfernen",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.INFORMATION_MESSAGE,
                null, //do not use a custom Icon
                options, //the titles of buttons
                options[0]);


        if (n == 0) {

            int c = JOptionPane.showOptionDialog(new JFrame(),
                    "Vorschau?",
                    "Eintrag Entfernen",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.INFORMATION_MESSAGE,
                    null, //do not use a custom Icon
                    options, //the titles of buttons
                    options[0]);


            if (c == 0) {
                vorschau = "ja";
                statusInfo = "delete";
                antragloeschen = "ja";
                beantragenLoeschen(vorschau);


            } else if (c == 1) {
                vorschau = "nein";
                statusInfo = "delete";
                antragloeschen = "ja";
                beantragenLoeschen(vorschau);

            }
            clients.send("2004" + clients.trennzeichen + IDUser + ";Software");
            clients.send("2001" + clients.trennzeichen + "NewUser" + clients.trennzeichen + pnr.getText() + "" + clients.trennzeichen + "Daten3");
            clients.send("2001" + clients.trennzeichen + "SoftwareNewUser" + clients.trennzeichen + IDUser + "" + clients.trennzeichen + "IDNUser");

        }
        cleanTempDateien();
        this.setVisible(false);
        this.dispose();
    }//GEN-LAST:event_jMenuItem9ActionPerformed

    private void jMenuItem2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem2ActionPerformed
        this.setVisible(false);
        this.dispose();
    }//GEN-LAST:event_jMenuItem2ActionPerformed
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox ADSPfad;
    private javax.swing.JTabbedPane Auswahl;
    private javax.swing.JTabbedPane AuswahlSoftware;
    private javax.swing.JTextField Name;
    private javax.swing.JComboBox NovelContext;
    private javax.swing.JTextField PWName;
    private javax.swing.JTextField PWZweck;
    private javax.swing.JButton SButton;
    private javax.swing.JButton SButton1;
    private javax.swing.JButton SButton2;
    private javax.swing.JRadioButton SNovel;
    private javax.swing.JRadioButton SWindows;
    private javax.swing.JTextField abtl1;
    private javax.swing.JButton addDoku;
    private javax.swing.JTextField antrag;
    private javax.swing.JTabbedPane auswahlZubehoer;
    private javax.swing.JButton bearbeiten;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.ButtonGroup buttonGroup2;
    private javax.swing.JLabel ePWanzeige;
    private javax.swing.JTextField email;
    private javax.swing.JCheckBox emailCheck;
    private javax.swing.JPasswordField emailPW;
    private javax.swing.JTextField fax;
    private javax.swing.JTextField funktion;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
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
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu3;
    private javax.swing.JMenu jMenu4;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JMenuItem jMenuItem6;
    private javax.swing.JMenuItem jMenuItem7;
    private javax.swing.JMenuItem jMenuItem8;
    private javax.swing.JMenuItem jMenuItem9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel13;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JTextArea kommentar;
    public javax.swing.JComboBox lapSN;
    public javax.swing.JCheckBox laptop;
    private javax.swing.JTextField nickname;
    private javax.swing.JTextField ort;
    private javax.swing.JTextField plz;
    private javax.swing.JTextField pnr;
    private javax.swing.JPasswordField pw;
    private javax.swing.JPasswordField pw01;
    private javax.swing.JPasswordField pw02;
    private javax.swing.JButton sendUser;
    private javax.swing.JLabel spw;
    private javax.swing.JComboBox standort;
    private javax.swing.JComboBox status;
    private javax.swing.JTextField strasse;
    private javax.swing.JCheckBox system;
    private javax.swing.JTextField tel;
    private javax.swing.JTextField user01;
    private javax.swing.JTextField user02;
    private javax.swing.JScrollPane userSoft;
    private javax.swing.JTextField vname;
    // End of variables declaration//GEN-END:variables
}
