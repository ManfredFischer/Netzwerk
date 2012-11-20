/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package netzwerkverwaltung;

import java.awt.TrayIcon;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTable;

/**
 *
 * @author manfred.fischer
 */
public class VerwaltungStoerung {

    private HauptBildschirm haupt;
    private ClientNetzwerk clients;
    private TabelFont tabelStoerung, tabelAnhaenge;
    private JTable tabStoerung;
    protected int StoerungsAuswahl = 0;
    private int UserID;
    private String Stoerungsauswahl = "", UserName;
    private ArrayList<ArrayList<String>> KontoInfo;
    private ArrayList<ArrayList<String>> datenStoerung = new ArrayList<ArrayList<String>>();
    private searchAdressbuch adressbuch;
    private ArrayList<ArrayList<String>> datenFirma;

    public VerwaltungStoerung(HauptBildschirm h, ClientNetzwerk c, TabelFont tabel, TabelFont tabelA, JTable tab, int IDU, ArrayList<ArrayList<String>> KInfo, int IDKI) {
        haupt = h;
        UserID = IDU;
        KontoInfo = KInfo;
        UserName = KontoInfo.get(IDKI).get(1);
        tabelStoerung = tabel;
        tabelAnhaenge = tabelA;
        tabStoerung = tab;
        clients = c;
    }

    // set Info From Firmen zugehörigkeit der Störung
    private void setInfoFirmaInfo(
            ArrayList<String> DatenFirma,
            ArrayList<String> DatenPerson) {
        resetInfo(false);
        if (DatenFirma.size() > 4 && DatenPerson.size() > 6) {
            haupt.IDAdressbuchSelected.setText(DatenFirma.get(0).toString() + ";" + DatenPerson.get(0).toString());
            haupt.firmenNamen.setText(DatenFirma.get(1).toString());
            haupt.strasse.setText(DatenFirma.get(2).toString());
            haupt.Name.setText(DatenPerson.get(2).toString() + ", " + DatenPerson.get(1).toString());
            haupt.plz.setText(DatenFirma.get(3).toString());
            haupt.ort.setText(DatenFirma.get(4).toString());
            haupt.tel.setText(DatenPerson.get(4).toString());
            haupt.fax.setText(DatenPerson.get(5).toString());
            haupt.handy.setText(DatenPerson.get(6).toString());
            haupt.auftragsNr.setText("");
        }
    }

    // get Info From Firmen zugehörigkeit der Störung
    private ArrayList<ArrayList<ArrayList<String>>> getFirmaInfo(
            ArrayList<ArrayList<String>> daten,
            JTable table) {
        try {
            ArrayList<ArrayList<ArrayList<String>>> getInfo = new ArrayList<ArrayList<ArrayList<String>>>();
            ArrayList<ArrayList<String>> firma, person;

            for (int i = 0; i < daten.size(); i++) {
                if (daten.get(i).get(0).toString().equals(table.getValueAt(table.getSelectedRow(), 0).toString())) {
                    ArrayList<ArrayList<String>> datenStoerungAdressPerson = (ArrayList<ArrayList<String>>) clients.netzwerkVerwaltung.getAdressbuchPerson(daten.get(i).get(8).toString(), "-1", "HauptBildschirm/setInfoFirma", false).clone();
                    getInfo.add(datenStoerungAdressPerson);
                    getInfo.add((ArrayList<ArrayList<String>>) clients.netzwerkVerwaltung.getAdressbuchFirma(datenStoerungAdressPerson.get(0).get(7).toString(), "HauptBildschirm/setInfoFirma", false).clone());
                    break;
                }
            }
            return getInfo;
        } catch (Exception e) {
            return null;
        }

    }

    // set Störung welche Störung sind vorhanden!
    protected void setStoerung(
            int Stoerung,
            String IDAdressbuch,
            Boolean checkAllStoerungen) {
        StoerungsAuswahl = Stoerung;

        switch (Stoerung) {
            case 1:
                resetInfo(true);
                Stoerungsauswahl = "-1,0,-1,-1,-,-,-1,HauptBildschirm/resetStoerungsDaten";
                datenStoerung = addNewStoerung((ArrayList<ArrayList<String>>) clients.netzwerkVerwaltung.getStoerung("-1", 0, -1, -1, "-", "-", "-1", "HauptBildschirm/resetStoerungsDaten", true).clone(), "0");
                break;
            case 2:
                resetInfo(true);
                Stoerungsauswahl = "wartend," + UserID + ",-1,-1,-,-,-1,HauptBildschirm/resetStoerungsDaten";
                filterWV((ArrayList<ArrayList<String>>) clients.netzwerkVerwaltung.getStoerung("wartend", UserID, -1, -1, "-", "-", "-1", "HauptBildschirm/resetStoerungsDaten", true).clone(), "warten");
                datenStoerung = addNewStoerung(filterWV((ArrayList<ArrayList<String>>) clients.netzwerkVerwaltung.getStoerung("in Arbeit", UserID, -1, -1, "-", "-", "-1", "HauptBildschirm/resetStoerungsDaten", true).clone(), "in Arbeit"), "" + UserID);
                break;
            case 3:
                resetInfo(true);
                Stoerungsauswahl = "neu," + UserID + ",-1,-1,-,-,-1,HauptBildschirm/resetStoerungsDaten";
                filterWV((ArrayList<ArrayList<String>>) clients.netzwerkVerwaltung.getStoerung("warten", UserID, -1, -1, "-", "-", "-1", "HauptBildschirm/resetStoerungsDaten", true).clone(), "warten");
                datenStoerung = addNewStoerung((ArrayList<ArrayList<String>>) clients.netzwerkVerwaltung.getStoerung("neu", UserID, -1, -1, "-", "-", "-1", "HauptBildschirm/resetStoerungsDaten", true).clone(), "" + UserID);
                break;
            case 4:
                if (!IDAdressbuch.equals("") && IDAdressbuch != null) {
                    String[] datenIDAdressbuch = IDAdressbuch.split(";");
                    if (datenIDAdressbuch.length > 1) {
                        ArrayList<ArrayList<String>> datenSuche;
                        if (checkAllStoerungen) {
                            Stoerungsauswahl = "-4,-1,-1,-1,IDAdressbuch," + datenIDAdressbuch[1] + ",-1,HauptBildschirm/resetStoerungsDaten";
                            datenSuche = clients.netzwerkVerwaltung.getStoerung("-4", -1, -1, -1, "IDAdressbuch", datenIDAdressbuch[1], "-1", "HauptBildschirm/checkStoerungsAuswahl", true);
                        } else {
                            Stoerungsauswahl = "-3,-1,-1,-1,IDAdressbuch," + datenIDAdressbuch[1] + ",-1,HauptBildschirm/resetStoerungsDaten";
                            datenSuche = clients.netzwerkVerwaltung.getStoerung("-3", -1, -1, -1, "IDAdressbuch", datenIDAdressbuch[1], "-1", "HauptBildschirm/checkStoerungsAuswahl", true);
                        }
                        datenStoerung = addNewStoerung(datenSuche, "-1");
                    }
                }
                break;
        }
    }

    protected void changeCombobox(
            String Auswahl,
            String IDAdressbuch,
            Boolean checkAllStoerungen) {
        try {

            if (datenStoerung != null && !datenStoerung.isEmpty()) {
                String vergleichName = "", getName = "";
                int idS = 0;

                if (Auswahl.equals("bearbeiter")) {
                    for (int i = 0; i < KontoInfo.size(); i++) {
                        if (datenStoerung.get(tabStoerung.getSelectedRow()).get(7).equals(KontoInfo.get(i).get(0))) {
                            vergleichName = KontoInfo.get(i).get(1).toString();
                            getName = tabStoerung.getValueAt(tabStoerung.getSelectedRow(), 5).toString();
                            break;
                        }

                    }

                    if (!vergleichName.equals(getName)) {
                        for (int i = 0; i < KontoInfo.size(); i++) {
                            if (getName.equals(KontoInfo.get(i).get(1))) {
                                idS = Integer.parseInt(KontoInfo.get(i).get(0).toString());
                                break;
                            }
                        }
                        clients.send("2003" + clients.trennzeichen + "Stoerung" + clients.trennzeichen + "bearbeiter" + clients.trennzeichen + idS + clients.trennzeichen + "IDStoerung" + clients.trennzeichen + datenStoerung.get(tabStoerung.getSelectedRow()).get(0));
                    }
                    setStoerung(StoerungsAuswahl, IDAdressbuch, checkAllStoerungen);
                } else {
                    for (int i = 0; i < datenStoerung.size(); i++) {
                        if (datenStoerung.get(tabStoerung.getSelectedRow()).get(3).equals(tabStoerung.getValueAt(tabStoerung.getSelectedRow(), 5).toString())) {
                            vergleichName = datenStoerung.get(tabStoerung.getSelectedRow()).get(3).toString();
                            getName = tabStoerung.getValueAt(tabStoerung.getSelectedRow(), 4).toString();
                            break;
                        }

                    }
                    if (!vergleichName.equals(getName)) {
                        clients.send("2002" + clients.trennzeichen + "Stoerung" + clients.trennzeichen + "Status" + clients.trennzeichen + getName + clients.trennzeichen + "IDStoerung" + clients.trennzeichen + datenStoerung.get(tabStoerung.getSelectedRow()).get(0));
                        setStoerung(StoerungsAuswahl, IDAdressbuch, checkAllStoerungen);
                    }


                }
            }
        } catch (Exception ex) {
        }

    }

    protected void CheckInfoStoerungen(TrayIcon trayIcon, String ak, String IDAdressbuch, Boolean checkAllStoerungen) {
        ArrayList<ArrayList<String>> stAll = (ArrayList<ArrayList<String>>) clients.netzwerkVerwaltung.getStoerung("-1", 0, -1, -1, "-1", "-1", "-1", "HauptBildschirm/CheckInfoStoerung", false).clone();
        String Betreff = "Information";
        int anzahlAll = 0;
        int anzahlSelf = 0;
        for (int i = 0; i < stAll.size(); i++) {
            if (stAll.get(i).get(11).equals("0")) {
                anzahlAll++;
                Betreff = stAll.get(0).get(9).toString();
            }
        }
        ArrayList<ArrayList<String>> stSelf = (ArrayList<ArrayList<String>>) clients.netzwerkVerwaltung.getStoerung("-1", UserID, -1, -1, "-1", "-1", "-1", "HauptBildschirm/CheckInfoStoerung", false).clone();
        for (int i = 0; i < stSelf.size(); i++) {
            if (stSelf.get(i).get(11).equals("0")) {
                anzahlSelf++;
                Betreff = stSelf.get(0).get(9).toString();
            }
        }
        if (anzahlSelf > 0 || anzahlAll > 0) {
            trayIcon.displayMessage("(" + anzahlSelf + "/" + anzahlAll + ") Neue Nachricht", Betreff, TrayIcon.MessageType.INFO);
        } else {
            if (ak.equals("show")) {
                trayIcon.displayMessage("Keine neuen Nachrichten (" + stAll.size() + "/" + stSelf.size() + ")", Betreff, TrayIcon.MessageType.INFO);
            }
        }
        setStoerung(StoerungsAuswahl, IDAdressbuch, checkAllStoerungen);
    }

    private ArrayList<ArrayList<String>> filterWV(ArrayList<ArrayList<String>> DatenStoerung, String Status) {
        ArrayList<ArrayList<String>> DatenTemp = new ArrayList<ArrayList<String>>();
        for (int i = 0; i < DatenStoerung.size(); i++) {
            if (DatenStoerung.get(i).get(2).equals("00.00.0000-00:00")) {
                DatenTemp.add(DatenStoerung.get(i));
            } else {
                Boolean weiter = false;
                String[] aDatum, aUhr, gDatum, gUhr;
                String DatumTemp;
                Date datum = new Date();
                String Datum = DatenStoerung.get(i).get(2).toString();   // 22.11.2011 23:00
                SimpleDateFormat sdfToDate = new SimpleDateFormat("dd.MM.yyyy  HH:mm:ss ");
                DatumTemp = sdfToDate.format(datum);
                aDatum = DatumTemp.split("  ")[0].split("\\.");
                aUhr = DatumTemp.split("  ")[1].split(":");

                gDatum = Datum.split("-")[0].split("\\.");
                gUhr = Datum.split("-")[1].split(":");

                if (Integer.parseInt(aDatum[2]) == Integer.parseInt(gDatum[2])) {
                    if (Integer.parseInt(aDatum[1]) == Integer.parseInt(gDatum[1])) {
                        if (Integer.parseInt(aDatum[0]) == Integer.parseInt(gDatum[0])) {
                            if (Integer.parseInt(aUhr[0]) == Integer.parseInt(gUhr[0])) {
                                if (Integer.parseInt(aUhr[1]) >= Integer.parseInt(gUhr[1])) {
                                    weiter = true;
                                }
                            } else if (Integer.parseInt(aUhr[0]) > Integer.parseInt(gUhr[0])) {
                                weiter = true;
                            }
                        } else if (Integer.parseInt(aDatum[0]) > Integer.parseInt(gDatum[0])) {
                            weiter = true;
                        }
                    } else if (Integer.parseInt(aDatum[1]) > Integer.parseInt(gDatum[1])) {
                        weiter = true;
                    }
                } else if (Integer.parseInt(aDatum[2]) > Integer.parseInt(gDatum[2])) {
                    weiter = true;
                }

                if (weiter) {
                    clients.send("2002" + clients.trennzeichen + "Stoerung" + clients.trennzeichen + "Status" + clients.trennzeichen + "neu" + clients.trennzeichen + "IDStoerung" + clients.trennzeichen + DatenStoerung.get(i).get(0).toString());
                    clients.send("2002" + clients.trennzeichen + "Stoerung" + clients.trennzeichen + "aktiv" + clients.trennzeichen + "0" + clients.trennzeichen + "IDStoerung" + clients.trennzeichen + DatenStoerung.get(i).get(0).toString());
                    clients.send("2002" + clients.trennzeichen + "Stoerung" + clients.trennzeichen + "WVDatum" + clients.trennzeichen + "00.00.0000 - 00:00" + clients.trennzeichen + "IDStoerung" + clients.trennzeichen + DatenStoerung.get(i).get(0).toString());
                    DatenTemp.add(DatenStoerung.get(i));
                }
            }
        }
        if (Status.equals("in Arbeit")) {
            return DatenStoerung;
        } else {
            return DatenTemp;
        }
    }

    protected void newStoerung(String ID, int UserID, String UserName) {
        resetAdressbuch();
        newStoerung test;
        if (datenFirma != null) {
            if (!datenFirma.isEmpty()) {
                if (ID.equals("")) {
                    test = new newStoerung("neue Störung", "neu", UserID, null, null, null, null, clients, UserName, haupt, null);
                } else {
                    test = new newStoerung("neue Störung", "neu", UserID, null, ID, null, null, clients, UserName, haupt, null);
                }
            } else {
                JOptionPane.showMessageDialog(new JFrame(), "Keine Firma Vorhanden! \nBitte zuerst eine Firma anlegen.");
            }
        } else {
            JOptionPane.showMessageDialog(new JFrame(), "Keine Firma Vorhanden! \nBitte zuerst eine Firma anlegen.");
        }
    }

    private void resetAdressbuch() {
        datenFirma = (ArrayList<ArrayList<String>>) clients.netzwerkVerwaltung.getAdressbuchFirma("-1", "HauptBildschirm/resetAdressbuch", false).clone();
    }

    private ArrayList<File> getStoerungInfo(
            String pfad,
            TabelFont tableAnhaenge) {

        haupt.inhalt.setText("");
        AnhaengeStoerungen = new ArrayList<File>();
        ArrayList<File> dateienStoerung = new ArrayList<File>();
        File[] files = new File(pfad).listFiles();
        if (files != null) {
            tableAnhaenge.getInfoTable.removeAllItems();
            ArrayList<String> datenInhalt = new ArrayList<String>();
            ArrayList<String> datenTemp;
            for (int i = 0; i < files.length; i++) {
                dateienStoerung.add(files[i]);
                String[] temp = files[i].getName().split("-");
                if (temp[temp.length - 1].equals("Stoerung.txt")) {
                    try {
                        BufferedReader read = new BufferedReader(new FileReader(files[i]));
                        String info = read.readLine();
                        while (info != null) {
                            haupt.inhalt.append(info + "\n");
                            datenInhalt.add(info);
                            info = read.readLine();
                        }
                        read.close();
                    } catch (Exception ex) {
                    }
                } else {
                    datenTemp = new ArrayList<String>();
                    AnhaengeStoerungen.add(files[i]);
                    String[] datenName = files[i].getName().split("-");
                    String name = "";
                    if (datenName.length > 2) {
                        for (int a = 2; a < datenName.length; a++) {
                            name = name + datenName[a];
                        }
                    } else {
                        name = files[i].getName();

                    }
                    datenTemp.add(name);
                    tableAnhaenge.getInfoTable.addRow(datenTemp, "1");
                }
            }
            return dateienStoerung;
        } else {
            return new ArrayList<File>();
        }

    }

    private ArrayList<ArrayList<String>> addNewStoerung(
            ArrayList<ArrayList<String>> DatenStoerung,
            String bearbeiter) {
        if (DatenStoerung != null) {
            if (!DatenStoerung.isEmpty() && DatenStoerung.get(0).size() == 12) {
                ArrayList<String> datenS;
                boolean weiter = false;
                int row = tabelStoerung.getInfoTable.getRowCount() - 1;
                for (int a = row; a >= 0; a--) {
                    weiter = true;
                    for (int i = 0; i < DatenStoerung.size(); i++) {
                        if ((tabelStoerung.getInfoTable.getValueAt(a, 0).equals(DatenStoerung.get(i).get(0)))
                                && (tabelStoerung.getInfoTable.getValueAt(a, 1).equals(DatenStoerung.get(i).get(9)))
                                && (tabelStoerung.getInfoTable.getValueAt(a, 2).equals(DatenStoerung.get(i).get(4)))
                                && (tabelStoerung.getInfoTable.getValueAt(a, 3).equals(DatenStoerung.get(i).get(2)))
                                && (tabelStoerung.getInfoTable.getValueAt(a, 4).equals(DatenStoerung.get(i).get(3)))
                                && (tabelStoerung.getInfoTable.getValueAt(a, 5).equals(bearbeiter))) {
                            weiter = false;
                            break;
                        } else {
                            weiter = true;
                        }
                    }
                    if (weiter) {
                        tabelStoerung.getInfoTable.removeRowAt(a);
                    }
                }
                Boolean keinInhalt = false;
                for (int a = 0; a < DatenStoerung.size(); a++) {
                    weiter = true;
                    if (!keinInhalt) {
                        for (int i = 0; i < tabelStoerung.getInfoTable.getRowCount(); i++) {
                            if (DatenStoerung.get(a).get(0).equals(tabelStoerung.getInfoTable.getValueAt(i, 0))
                                    || (DatenStoerung.get(a).get(0).equals(tabelStoerung.getInfoTable.getValueAt(i, 1)))
                                    || (DatenStoerung.get(a).get(0).equals(tabelStoerung.getInfoTable.getValueAt(i, 2)))
                                    || (DatenStoerung.get(a).get(0).equals(tabelStoerung.getInfoTable.getValueAt(i, 3)))
                                    || (DatenStoerung.get(a).get(0).equals(tabelStoerung.getInfoTable.getValueAt(i, 4)))
                                    || (DatenStoerung.get(a).get(0).equals(tabelStoerung.getInfoTable.getValueAt(i, 5)))) {
                                weiter = false;
                                break;
                            } else {
                                weiter = true;
                            }
                        }
                        if (tabelStoerung.getInfoTable.getRowCount() < 1) {
                            keinInhalt = true;
                        }
                    }
                    if (weiter) {
                        String tempName = "";
                        for (int i = 0; i < KontoInfo.size(); i++) {
                            if (DatenStoerung.get(a).size() >= 7) {
                                if (KontoInfo.get(i).get(0).equals(DatenStoerung.get(a).get(7).toString())) {
                                    tempName = KontoInfo.get(i).get(1);
                                    break;
                                }
                            } else {
                            }
                        }
                        datenS = new ArrayList<String>();
                        datenS.add(DatenStoerung.get(a).get(0).toString());
                        datenS.add(DatenStoerung.get(a).get(9).toString());
                        datenS.add(DatenStoerung.get(a).get(4).toString());
                        datenS.add(DatenStoerung.get(a).get(2).toString());
                        datenS.add(DatenStoerung.get(a).get(3).toString());
                        datenS.add(tempName);
                        tabelStoerung.getInfoTable.addRow(datenS, DatenStoerung.get(a).get(10).toString());

                    }
                }

                return DatenStoerung;
            } else {
                return new ArrayList<ArrayList<String>>();
            }
        } else {
            return new ArrayList<ArrayList<String>>();
        }

    }

    protected void setStoerungSearch(
            String IDAdressbuch,
            Boolean ALL) {
        resetInfo(true);
        setInfoFirmaInfo(adressbuch.infoAdresse.get(0), adressbuch.infoAdresse.get(1));
        StoerungsAuswahl = 4;
        setStoerung(StoerungsAuswahl, IDAdressbuch, ALL);
    }

    protected void SucheStoerung(
            int bed,
            String wert,
            String Firma) {
        ArrayList<ArrayList<String>> datenSuche;
        adressbuch = new searchAdressbuch(clients, haupt, null);
        try {
            if (bed == 0) {
                datenSuche = (ArrayList<ArrayList<String>>) clients.netzwerkVerwaltung.getStoerung("-1", -1, -1, Integer.parseInt(wert), "-", "-", "-1", "HauptBildschirm/Suche", false).clone();
                datenStoerung = addNewStoerung(datenSuche, "-1");
            } else {
                if (Firma.equals("Firma")) {
                    adressbuch.searhFirma(wert, bed);
                } else {
                    adressbuch.searhPerson(wert, bed);
                }
            }
        } catch (Exception e) {
            while (tabelStoerung.getInfoTable.getRowCount() > 0) {
                tabelStoerung.getInfoTable.removeRowAt(0);
            }
        }

    }
    protected ArrayList<File> AnhaengeStoerungen;

    protected void AuswahlStoerung(
            MouseEvent e,
            TabelFont tableAnhaenge) {
        try {
            if (clients != null) {
                clients.send("2002" + clients.trennzeichen + "Stoerung" + clients.trennzeichen + "gelesen" + clients.trennzeichen + "1" + clients.trennzeichen + "IDStoerung" + clients.trennzeichen + tabStoerung.getValueAt(tabStoerung.getSelectedRow(), 0));
                int auswahlRow = tabStoerung.getSelectedRow();
                String[] STTemp = Stoerungsauswahl.split(",");
                if (STTemp.length > 7 && tabStoerung.getSelectedRow() != -1) {
                    datenStoerung = (ArrayList<ArrayList<String>>) clients.netzwerkVerwaltung.getStoerung(STTemp[0], Integer.parseInt(STTemp[1]), Integer.parseInt(STTemp[2]), Integer.parseInt(STTemp[3]), STTemp[4], STTemp[5], STTemp[6], STTemp[7], false).clone();
                    if (datenStoerung != null) {
                        ArrayList<ArrayList<ArrayList<String>>> firmenDaten = getFirmaInfo(datenStoerung, tabStoerung);
                        if (firmenDaten != null) {
                            setInfoFirmaInfo(firmenDaten.get(1).get(0), firmenDaten.get(0).get(0));
                            ArrayList<File> dateien = (ArrayList<File>) getStoerungInfo(datenStoerung.get(auswahlRow).get(5), tableAnhaenge);

                            if (e.getClickCount() == 2) {
                                if (dateien.isEmpty()) {
                                    JOptionPane.showMessageDialog(new JFrame(), " Leider gab es ein Problem beim Übertragen \n beim speichern werden die alten Daten gelöscht \n bitte Kontaktieren Sie den Administrator bei wieder herrstellung der alten Daten");
                                }
                                ArrayList<String> stoerung = datenStoerung.get(auswahlRow);
                                ArrayList<String> adress = firmenDaten.get(1).get(0);
                                ArrayList<String> person = firmenDaten.get(0).get(0);
                                if (!adress.isEmpty() && !person.isEmpty() && !stoerung.isEmpty()) {
                                    if (datenStoerung.get(auswahlRow).get(10).equals("---")) {
                                        clients.send("2002" + clients.trennzeichen + "Stoerung" + clients.trennzeichen + "aktiv" + clients.trennzeichen + UserName + clients.trennzeichen + "IDStoerung" + clients.trennzeichen + tabStoerung.getValueAt(auswahlRow, 0).toString());
                                        new newStoerung("Stoerungs",
                                                "off",
                                                UserID,
                                                (ArrayList<String>) stoerung.clone(),
                                                null,
                                                (ArrayList<String>) adress.clone(),
                                                (ArrayList<String>) person.clone(),
                                                clients,
                                                UserName,
                                                haupt,
                                                dateien);
                                    } else {
                                        new newStoerung("Schreibgeschützt (in Arbeit: " + datenStoerung.get(auswahlRow).get(10) + ")",
                                                "on",
                                                UserID,
                                                (ArrayList<String>) stoerung.clone(),
                                                null,
                                                (ArrayList<String>) adress.clone(),
                                                (ArrayList<String>) person.clone(),
                                                clients,
                                                UserName,
                                                haupt,
                                                dateien);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } catch (Exception ex) {
        }
    }

    private void resetInfo(Boolean table) {
        haupt.Name.setText("");
        haupt.tel.setText("");
        haupt.fax.setText("");
        haupt.firmenNamen.setText("");
        haupt.strasse.setText("");
        haupt.plz.setText("");
        haupt.ort.setText("");
        haupt.handy.setText("");
        haupt.IDAdressbuchSelected.setText("");
        haupt.inhalt.setText("");
        haupt.auftragsNr.setText("");
        if (table) {
            tabelStoerung.getInfoTable.removeAllItems();
            tabelAnhaenge.getInfoTable.removeAllItems();
        }
    }
}
