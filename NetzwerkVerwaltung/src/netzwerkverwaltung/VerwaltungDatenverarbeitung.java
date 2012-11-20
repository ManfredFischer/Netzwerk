/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package netzwerkverwaltung;


import java.awt.Color;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author manfred.fischer
 */
public class VerwaltungDatenverarbeitung {

    private DatenVerarbeitung daten = new DatenVerarbeitung();
    private ClientNetzwerk netz;
    private ArrayList<String> send;
    public VerwaltungDatenverarbeitung netzV;
    private ArrayList<ArrayList<String>> empfang = new ArrayList<ArrayList<String>>(), getInfos = new ArrayList<ArrayList<String>>();
    public Boolean uebertragungFertig = false;
    private ArrayList<File> files = new ArrayList<File>(), files2 = new ArrayList<File>();
    private DateiVerwaltung datei = new DateiVerwaltung();
    private initServer recieve;
    private String id = "-2";
    private HauptBildschirm haupt;
    private HashMap DatensammlungInfo = new HashMap();
    private ArrayList<ArrayList<String>> datenNetz = new ArrayList<ArrayList<String>>(),
            datenHardware = new ArrayList<ArrayList<String>>(),
            datenAdressbuchPerson = new ArrayList<ArrayList<String>>(),
            datenAdressbuchFirma = new ArrayList<ArrayList<String>>(),
            datenADSPfad = new ArrayList<ArrayList<String>>(),
            datenHandy = new ArrayList<ArrayList<String>>(),
            datenUserEMail = new ArrayList<ArrayList<String>>(),
            datenUserVertraege = new ArrayList<ArrayList<String>>(),
            datenProtokoll = new ArrayList<ArrayList<String>>(),
            datenHandyVertrageListe = new ArrayList<ArrayList<String>>(),
            datenSoftware = new ArrayList<ArrayList<String>>(),
            datenUserAllg = new ArrayList<ArrayList<String>>(),
            datenUserLaptop = new ArrayList<ArrayList<String>>(),
            datenUserPW = new ArrayList<ArrayList<String>>(),
            datenUserGruppe = new ArrayList<ArrayList<String>>(),
            datenVertraege = new ArrayList<ArrayList<String>>(),
            datenUserHeadset = new ArrayList<ArrayList<String>>(),
            datenUserVertrageListe = new ArrayList<ArrayList<String>>(),
            datenUserSoftwareInfo = new ArrayList<ArrayList<String>>(),
            datenUserBerechtigung = new ArrayList<ArrayList<String>>(),
            datenBerechtigungInfo = new ArrayList<ArrayList<String>>(),
            datenStoerungArchiv = new ArrayList<ArrayList<String>>(),
            datenDoku = new ArrayList<ArrayList<String>>(),
            datenNewUser = new ArrayList<ArrayList<String>>(),
            datenNewSoftware = new ArrayList<ArrayList<String>>(),
            datenStoerung = new ArrayList<ArrayList<String>>(),
            datenAdress = new ArrayList<ArrayList<String>>(),
            datenKonten = new ArrayList<ArrayList<String>>(),
            datenAdressFirma = new ArrayList<ArrayList<String>>(),
            datenKontenCheck = new ArrayList<ArrayList<String>>(),
            datenInfo = new ArrayList<ArrayList<String>>(),
            datenNetzAdresse = new ArrayList<ArrayList<String>>(),
            datenTemp = new ArrayList<ArrayList<String>>();
    private Boolean newInfo = true;

    public VerwaltungDatenverarbeitung(ClientNetzwerk n, initServer in, HauptBildschirm h) {
        netz = n;
        recieve = in;
        haupt = h;
    }

    public void DatenGO() {
        daten.go = true;
    }

    public void DatenEmpfang(String[] temp) {
        if (temp[0].equals("FERTIG")) {
            try {
              if (temp.length > 1)
                this.id = temp[1];
            } catch (Exception e) {
            }
            empfang = (ArrayList<ArrayList<String>>) getInfos.clone();
            getInfos = new ArrayList<ArrayList<String>>();
            uebertragungFertig = true;
        } else if (temp[0].equals("NEWINFO")) {
            newInfo = true;
        } else {
            ArrayList<String> tempDaten = new ArrayList<String>();
            for (int i = 0; i < temp.length; i++) {
                tempDaten.add(temp[i]);
            }
            getInfos.add(tempDaten);
        }
    }

    public String getID() {
        String id = "0";
        while (true) {
            if (!this.id.equals("-2")) {
                id = this.id;
                this.id = "-2";
                break;
            }
        }
        return id;
    }

    public String getIDNewUser() {
        String idGet;
        while (true) {
            if (!id.equals("-2")) {
                idGet = id;
                id = "-2";
                break;
            }
        }
        return idGet;
    }

    public ArrayList<String> dateiRead(String FileName) {
        ArrayList<String> Daten = new ArrayList<String>();
        BufferedReader read = null;
        try {
            read = new BufferedReader(new FileReader(new File(FileName)));
            String readDaten = read.readLine();
            boolean weiter = false;
            String[] temp = {"", "c:/"};
            while (readDaten != null) {
                Daten.add(readDaten);
                readDaten = read.readLine();
            }
            read.close();
            return Daten;
        } catch (Exception ex) {
            return null;
        }
    }

    public ArrayList<ArrayList<String>> getNetz(String ID, String Methode, Boolean newDaten) {
         return getDaten("0" + netz.trennzeichen + "" + ID, Methode, "getNetz",newDaten);
    }

    public ArrayList<File> getDateiInfo(String Ziel, String Ordner, String datum, String Methode, Boolean newDaten) {
        sendInfo("999994" + netz.trennzeichen + "" + Ziel + "" + netz.trennzeichen + "" + Ordner + netz.trennzeichen + "" + datum, Methode, id,newDaten);
        ArrayList<File> FileDaten = new ArrayList<File>();
        File tempDatei;
        for (int i = 0; i < empfang.size(); i++) {
            tempDatei = new File(empfang.get(i).get(0));
            FileDaten.add(tempDatei);
        }
        return FileDaten;
    }

    public ArrayList<File> getDateiInfoVorhanden(String Ziel, String Software, String Methode, Boolean newDaten) {
        sendInfo("999995" + netz.trennzeichen + "" + Ziel + netz.trennzeichen + Software, Methode, id,newDaten);
        ArrayList<File> FileDaten = new ArrayList<File>();
        File tempDatei;
        for (int i = 0; i < empfang.size(); i++) {
            tempDatei = new File(empfang.get(i).get(0));
            FileDaten.add(tempDatei);
        }
        return FileDaten;
    }

    public void addDateiInfo(String Pfad, String DateiName, String datum, String Methode, Boolean newDaten) {
        sendInfo("999993" + netz.trennzeichen + "" + Pfad + "" + netz.trennzeichen + "" + DateiName + netz.trennzeichen + "" + datum, Methode, id,newDaten);
    }

    public ArrayList<File> getDateien(final String Pfad, final String DateiName, String Methode) {
        send = new ArrayList<String>();
        netzV = this;
        final int Port = netz.dateiV.Port();
        send.add("99999" + netz.trennzeichen + "" + Pfad + "" + netz.trennzeichen + "" + DateiName + "" + netz.trennzeichen + "" + Port);
        files = new ArrayList<File>();
        Thread thread = new Thread(new Runnable() {

            @Override
            public void run() {
                files = (ArrayList<File>) datei.dateiEmpfang(Port, Pfad, DateiName, netzV).clone();
            }
        });
        thread.start();
        netz.ClientVersand(send);
        Boolean wert = getDaten("getDateien", Methode);
        if (wert) {
            return files;
        } else {
            return null;
        }
    }

    public ArrayList<ArrayList<String>> datenKonto(String Methode, Boolean newDaten) {
        
         return getDaten("1" + netz.trennzeichen + "-", Methode, "datenKonto",newDaten);
    }

    public Boolean check(String User, Boolean newDaten) {
        sendInfo("2" + netz.trennzeichen + "" + User, "UserAnmeldung", "check",newDaten);
        if (!empfang.isEmpty()) {
            if (empfang.get(0).get(0).equals("true")) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }
    private int PID = 0;

    private void setProtokollDaten(String Zugriff, String Daten, String newDaten) {
        ArrayList<String> daten = new ArrayList<String>();
        daten.add(String.valueOf(PID++));
        daten.add(Zugriff);
        daten.add(Daten);
        daten.add(newDaten);
        if (newDaten.equalsIgnoreCase("new"))
         haupt.tableDatenProtokollierung.getInfoTable.addRow(daten, "-1");
        else
         haupt.tableDatenProtokollierungOLD.getInfoTable.addRow(daten, "-1");   
    }

    public ArrayList<ArrayList<String>> getHardware(String IDN, String Gruppe, String Methode, Boolean newDaten) {
        return getDaten("100" + netz.trennzeichen + "" + Gruppe + "" + netz.trennzeichen + "" + IDN, Methode, "getHardware",newDaten);
    }

    public ArrayList<ArrayList<String>> getProtokoll(String IDSC, String Server, String Gruppe, String Methode, Boolean newDaten) {
        return getDaten("300" + netz.trennzeichen + "" + IDSC + "" + netz.trennzeichen + "" + Server + "" + netz.trennzeichen + "" + Gruppe, Methode, "getProtokoll",newDaten);
    }

    public ArrayList<ArrayList<String>> getDoku(String Methode, Boolean newDaten) {

        return getDaten("800" + netz.trennzeichen + "-", Methode, "getDoku",newDaten);
    }

    public ArrayList<ArrayList<String>> getNewUser(String Doku, String Methode, Boolean newDaten) {

        return getDaten("900" + netz.trennzeichen + "" + Doku, Methode, "getNewUser",newDaten);
    }

    public ArrayList<ArrayList<String>> getStoerung(String Status, int bearbeiter, int besitzer, int id, String b, String w, String Derstellung, String Methode, Boolean newDaten) {
        String getInfo = "901" + netz.trennzeichen + "" + Status + "" + netz.trennzeichen + "" + bearbeiter + "" + netz.trennzeichen + "" + besitzer + "" + netz.trennzeichen + "" + id + "" + netz.trennzeichen + "" + b + "" + netz.trennzeichen + "" + w + "" + netz.trennzeichen + "" + Derstellung;
        return getDaten(getInfo, Methode, "getStoerung",newDaten);
    }

    public ArrayList<ArrayList<String>> getAdressbuchPerson(String idS, String IDFirma, String Methode, Boolean newDaten) {
        return getDaten("902" + netz.trennzeichen + "" + idS + "" + netz.trennzeichen + "" + IDFirma, Methode, "getAdressbuchPerson",newDaten);
    }

    public ArrayList<ArrayList<String>> getAdressbuchFirma(String idS, String Methode, Boolean newDaten) {

        return getDaten("903" + netz.trennzeichen + "" + idS, Methode, "getAdressbuchFirma",newDaten);
    }

    public ArrayList<ArrayList<String>> getNetzAdresse(String idS, String Methode, Boolean newDaten) {

        return getDaten("904" + netz.trennzeichen + "" + idS, Methode, "getNetzAdresse",newDaten);
    }

    public ArrayList<ArrayList<String>> getNewSoftware(String IDNuser, String ID, String Methode, Boolean newDaten) {

        return getDaten("500" + netz.trennzeichen + "" + IDNuser + netz.trennzeichen + ID, Methode, "getNewSoftware",newDaten);
    }

    public ArrayList<ArrayList<String>> getSoftware(String ID, String SZ, String Methode, Boolean newDaten) {
        return getDaten("200" + netz.trennzeichen + "" + ID + netz.trennzeichen + SZ, Methode, "getSoftware",newDaten);
    }

    public ArrayList<ArrayList<String>> getAPfad(String Methode, Boolean newDaten) {

        return getDaten("3" + netz.trennzeichen + "-", Methode, "getAPFad", newDaten);
    }

    public ArrayList<ArrayList<String>> getUserLaptop(String IDU, String Methode, Boolean newDaten) {

        return getDaten("400" + netz.trennzeichen + "" + IDU, Methode, "getUserLaptop",newDaten);
    }

    public ArrayList<ArrayList<String>> getUserPW(String IDU, String Daten, String Methode, Boolean newDaten) {
        return getDaten("401" + netz.trennzeichen + "" + IDU + netz.trennzeichen + Daten, Methode, "getUserPW",newDaten);
    }

    private ArrayList<ArrayList<String>> getDaten(String getInfo, String Methode, String interneMethode, Boolean newDaten) {
        if (sendInfo(getInfo, Methode, interneMethode,newDaten)) {
            DatensammlungInfo.put(getInfo, (ArrayList<ArrayList<String>>) empfang.clone());
            empfang = new ArrayList<ArrayList<String>>();
            setProtokollDaten(getInfo.split(netz.trennzeichen)[0], getInfo, "new");
        } else {
            setProtokollDaten(getInfo.split(netz.trennzeichen)[0], getInfo, "old");
        }
        return (ArrayList<ArrayList<String>>) DatensammlungInfo.get(getInfo);
    }

    private Boolean sendInfo(String info, String Methode, String c, Boolean getNew) {
      Boolean newInfoDaten = false,getNewDaten=true;
      
        try{
        
        if (newInfo) {
            ArrayList<ArrayList<String>> temp = (ArrayList<ArrayList<String>>) DatensammlungInfo.get(info);
            
            if (temp == null) {
                send = new ArrayList<String>();
                send.add(info);
                netz.ClientVersand(send);
                getDaten(c, Methode);
                newInfoDaten = true;
            } else {
                if (!getNew){
                send = new ArrayList<String>();
                send.add(info.split(netz.trennzeichen)[0] +netz.trennzeichen+ "-?-");
                netz.ClientVersand(send);
                while (true){
                    if (uebertragungFertig){
                       if ((empfang.get(0).get(0).equals("1"))){
                         getNewDaten=true;  
                       }else{
                           getNewDaten=false;
                       }
                       uebertragungFertig = false;
                       break;
                    }
                }
                }else{
                  getNewDaten=true;  
                }
                if (getNewDaten) {
                    send = new ArrayList<String>();
                    send.add(info);
                    netz.ClientVersand(send);
                    getDaten(c, Methode);
                    newInfoDaten = true;
                }
            }
        }
        return newInfoDaten;
       }catch(Exception e){
         return newInfoDaten;  
       }
    }

    private Boolean getDaten(String Text, String Methode) {
        int i = 5, a = 0;
        Boolean uebertragen = false;
        recieve.setDatenInfo(Text + " (" + Methode + ")", true, false, false);
        recieve.wait.setBackground(Color.LIGHT_GRAY);
        if (Text.equals("getDateien")) {
            // recieve.wait.setValue(Math.round(datei.zwischenWert));
            // recieve.wait.setMaximum(Math.round(datei.Gesamtwert));
        } else {
            recieve.wait.setValue(i);
        }

        while (true) {
            a++;
            if (i < 85) {
                i = i + 5;
            } else {
                if (datei.zwischenWert == 0) {
                    if (a == 500) {
                        uebertragungFertig = false;
                        break;
                    }
                }
            }
            if (Text.equals("getDateien")) {
                recieve.wait.setValue(Math.round(datei.zwischenWert));
            } else {
                recieve.wait.setValue(i);
            }

            if (uebertragungFertig) {
                datei.Gesamtwert = 0;
                uebertragen = true;
                recieve.wait.setValue(100);
                uebertragungFertig = false;
                break;
            }
            try {
                Thread.sleep(5);
            } catch (Exception ex) {
            }
        }
        recieve.setDatenInfo(Text + " (" + Methode + ")", false, true, !uebertragen);

        if (uebertragungFertig) {

            recieve.wait.setBackground(Color.GREEN);
        } else {
            recieve.wait.setBackground(Color.RED);
        }
        return uebertragen;
    }
}