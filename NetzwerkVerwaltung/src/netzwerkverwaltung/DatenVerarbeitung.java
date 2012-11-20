/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package netzwerkverwaltung;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author manfred.fischer
 */
public class DatenVerarbeitung {

    public ArrayList<HashMap> datenNetz = new ArrayList<HashMap>(),
            datenCServer = new ArrayList<HashMap>(),
            datenADSPfad = new ArrayList<HashMap>(),
            datenHandy = new ArrayList<HashMap>(),
            datenUserEMail = new ArrayList<HashMap>(),
            datenUserVertraege = new ArrayList<HashMap>(),
            datenProtokoll = new ArrayList<HashMap>(),
            datenHandyVertrageListe = new ArrayList<HashMap>(),
            datenSoftware = new ArrayList<HashMap>(),
            datenUserAllg = new ArrayList<HashMap>(),
            datenUserLaptop = new ArrayList<HashMap>(),
            datenUserPW = new ArrayList<HashMap>(),
            datenUserGruppe = new ArrayList<HashMap>(),
            datenVertraege = new ArrayList<HashMap>(),
            datenUserHeadset = new ArrayList<HashMap>(),
            datenUserVertrageListe = new ArrayList<HashMap>(),
            datenUserSoftwareInfo = new ArrayList<HashMap>(),
            datenUserBerechtigung = new ArrayList<HashMap>(),
            datenBerechtigungInfo = new ArrayList<HashMap>(),
            datenStoerungArchiv = new ArrayList<HashMap>(),
            datenDoku = new ArrayList<HashMap>(),
            datenNewUser = new ArrayList<HashMap>(),
            datenNewSoftware = new ArrayList<HashMap>(),
            datenStoerung = new ArrayList<HashMap>(),
            datenAdress = new ArrayList<HashMap>(),
            datenKonten = new ArrayList<HashMap>(),
            datenAdressFirma = new ArrayList<HashMap>(),
            datenKontenCheck = new ArrayList<HashMap>(),
            datenInfo = new ArrayList<HashMap>(),
            datenNetzAdresse = new ArrayList<HashMap>(),
            datenTemp = new ArrayList<HashMap>();
    public Boolean go = false;
    public Boolean erledigt = true;
    public String ID="-2",IDNewUser="-1";

    public void Daten(String[] Daten) {
        HashMap temp = new HashMap();
        switch (Integer.parseInt(Daten[0])) {
            case 0:
                if (Daten[1].equals("START")) {
                    datenNetz.clear();
                } else if (Daten[1].equals("ERROR")) {
                    temp.put("ERROR", "error");
                    datenTemp.add(temp);
                } else if (!Daten[1].equals("END")) {
                    
                    temp.put("NetzwerkID", Daten[1]);
                    temp.put("NetzwerkName", Daten[2]);
                    temp.put("NetzwerkNetz", Daten[3]);
                    temp.put("NetzwerkMaske", Daten[4]);
                    temp.put("NetzwerkDHCP", Daten[5]);
                    temp.put("NetzwerkIPANZ", Daten[6]);
                    temp.put("NetzwerkStandortID", Daten[7]);
                    datenTemp.add(temp);
                } else {
                    datenNetz = datenTemp;
                    datenTemp = new ArrayList<HashMap>();
                    go = true;
                }
                break;
            case 1:
                if (Daten[1].equals("START")) {
                    datenKonten.clear();
                } else if (Daten[1].equals("ERROR")) {
                    temp.put("ERROR", "error");
                    datenTemp.add(temp);
                } else if (!Daten[1].equals("END")) {
                    temp.put("IDK", Daten[1]);
                    temp.put("User", Daten[2]);
                    temp.put("Name", Daten[3]);
                    temp.put("Vorname", Daten[4]);
                    temp.put("eMail", Daten[5]);
                    temp.put("Tel", Daten[6]);
                    temp.put("Fax", Daten[7]);
                    temp.put("online", Daten[8]);
                    datenTemp.add(temp);
                } else {
                    datenKonten = datenTemp;
                    datenTemp = new ArrayList<HashMap>();
                    go = true;
                }
                break;
            case 2:
                if (Daten[1].equals("START")) {
                    datenKontenCheck.clear();
                } else if (Daten[1].equals("ERROR")) {
                    temp.put("ERROR", "error");
                    datenTemp.add(temp);
                } else if (!Daten[1].equals("END")) {
                    temp.put("weiter", Daten[1]);
                    datenTemp.add(temp);
                } else {
                    datenKontenCheck = datenTemp;
                    datenTemp = new ArrayList<HashMap>();
                    go = true;
                }
                break;
            case 3:
                if (Daten[1].equals("START")) {
                    datenADSPfad.clear();
                } else if (Daten[1].equals("ERROR")) {
                    temp.put("ERROR", "error");
                    datenTemp.add(temp);
                } else if (!Daten[1].equals("END")) {
                    temp.put("Name", Daten[1]);
                    temp.put("Pfad", Daten[2]);
                    datenTemp.add(temp);
                } else {
                    datenADSPfad = datenTemp;
                    datenTemp = new ArrayList<HashMap>();
                    go = true;
                }
                break;
            case 100:
                if (Daten[1].equals("START")) {
                    datenCServer.clear();
                } else if (Daten[1].equals("ERROR")) {
                    temp.put("ERROR", "error");
                    datenTemp.add(temp);
                } else if (!Daten[1].equals("END")) {
                    temp.put("CServerIDSC", Daten[1]);
                    temp.put("CServerName", Daten[2]);
                    temp.put("CServerFunktion", Daten[3]);
                    temp.put("CServerLeasingEnde", Daten[4]);
                    temp.put("CServerSN", Daten[5]);
                    temp.put("CServerTyp", Daten[6]);
                    temp.put("CServerIP", Daten[7]);
                    temp.put("CServerGruppe", Daten[8]);
                    temp.put("CServerIDN", Daten[9]);
                    temp.put("CServerOnline", Daten[10]);
                    datenTemp.add(temp);
                } else {
                    datenCServer = datenTemp;
                    datenTemp = new ArrayList<HashMap>();
                    go = true;
                }
                break;
            case 200:
                if (Daten[1].equals("START")) {
                    datenSoftware.clear();
                } else if (Daten[1].equals("ERROR")) {
                    temp.put("ERROR", "error");
                    datenTemp.add(temp);
                } else if (!Daten[1].equals("END")) {
                    temp.put("IDS", Daten[1]);
                    temp.put("Name", Daten[2]);
                    temp.put("EinmKosten", Daten[3]);
                    temp.put("UserKosten", Daten[4]);
                    temp.put("Funktion", Daten[5]);
                    temp.put("Verantwortlicher", Daten[6]);
                    temp.put("eMail", Daten[7]);
                    temp.put("Daten", Daten[8]);
                    datenTemp.add(temp);
                } else {
                    datenSoftware = datenTemp;
                    datenTemp = new ArrayList<HashMap>();
                    go = true;
                }
                break;
            case 300:
                if (Daten[1].equals("START")) {
                    datenProtokoll.clear();
                } else if (Daten[1].equals("ERROR")) {
                    temp.put("ERROR", "error");
                    datenTemp.add(temp);
                } else if (!Daten[1].equals("END")) {
                    temp.put("ProtokollIDP", Daten[1]);
                    temp.put("ProtokollDatum", Daten[2]);
                    temp.put("ProtokollUser", Daten[3]);
                    temp.put("ProtokollTaetigkeit", Daten[4]);
                    temp.put("ProtokollIDSC", Daten[5]);
                    datenTemp.add(temp);
                } else {
                    datenProtokoll = datenTemp;
                    datenTemp = new ArrayList<HashMap>();
                    go = true;
                }
                break;


            case 400:
                if (Daten[1].equals("START")) {
                    datenUserLaptop.clear();
                } else if (Daten[1].equals("ERROR")) {
                    temp.put("ERROR", "error");
                    datenTemp.add(temp);
                } else if (!Daten[1].equals("END")) {
                    temp.put("UsersLaptopName", Daten[1]);
                    temp.put("UsersLaptopSN", Daten[2]);
                    datenTemp.add(temp);
                } else {
                    datenUserLaptop = datenTemp;
                    datenTemp = new ArrayList<HashMap>();
                    go = true;
                }
                break;
            case 401:
                if (Daten[1].equals("START")) {
                    datenUserPW.clear();
                } else if (Daten[1].equals("ERROR")) {
                    temp.put("ERROR", "error");
                    datenTemp.add(temp);
                } else if (!Daten[1].equals("END")) {
                    temp.put("IDPW", Daten[1]);
                    temp.put("Name", Daten[2]);
                    temp.put("Zweck", Daten[3]);
                    temp.put("User1", Daten[4]);
                    temp.put("PW1", Daten[5]);
                    temp.put("User2", Daten[6]);
                    temp.put("PW2", Daten[7]);
                    temp.put("IDNUser", Daten[8]);
                    temp.put("IDS", Daten[9]);
                    datenTemp.add(temp);
                } else {
                    datenUserPW = datenTemp;
                    datenTemp = new ArrayList<HashMap>();
                    go = true;
                }
                break;
           
           
            case 402:
                if (Daten[1].equals("START")) {
                    datenUserSoftwareInfo.clear();
                } else if (Daten[1].equals("ERROR")) {
                    temp.put("ERROR", "error");
                    datenTemp.add(temp);
                } else if (!Daten[1].equals("END")) {
                    temp.put("UserSoftwareID", Daten[1]);
                    temp.put("UserSoftwareInfoName", Daten[2]);
                    temp.put("UserSoftwareInfoEinmalig", Daten[3]);
                    temp.put("UserSoftwareInfoMonatlich", Daten[4]);
                    temp.put("UserSoftwareInfoFunktion", Daten[5]);
                    datenTemp.add(temp);
                } else {
                    datenUserSoftwareInfo = datenTemp;
                    datenTemp = new ArrayList<HashMap>();
                    go = true;
                }
                break;
            case 405:
                if (Daten[1].equals("START")) {
                    datenUserEMail.clear();
                } else if (Daten[1].equals("ERROR")) {
                    temp.put("ERROR", "error");
                    datenTemp.add(temp);
                } else if (!Daten[1].equals("END")) {
                    temp.put("IDeMV", Daten[1]);
                    temp.put("DateiName", Daten[2]);
                    temp.put("Anhang", Daten[3]);
                    temp.put("Ordner", Daten[4]);
                    temp.put("Von", Daten[5]);
                    temp.put("An", Daten[6]);
                    temp.put("Betreff", Daten[7]);
                    temp.put("Datum", Daten[8]);
                    temp.put("Info", Daten[9]);
                    temp.put("Gelesen", Daten[10]);
                    temp.put("IDK", Daten[11]);
                    datenTemp.add(temp);
                } else {
                    datenUserEMail = datenTemp;
                    datenTemp = new ArrayList<HashMap>();
                    go = true;
                }
                break;

            case 600:
                if (Daten[1].equals("START")) {
                    datenBerechtigungInfo.clear();
                } else if (Daten[1].equals("ERROR")) {
                    temp.put("ERROR", "error");
                    datenTemp.add(temp);
                } else if (!Daten[1].equals("END")) {
                    temp.put("BerechtigungsID", Daten[1]);
                    temp.put("Berechtigung", Daten[2]);
                    datenTemp.add(temp);
                } else {
                    datenBerechtigungInfo = datenTemp;
                    datenTemp = new ArrayList<HashMap>();
                    go = true;
                }
                break;
            case 700:
                if (Daten[1].equals("START")) {
                    datenUserBerechtigung.clear();
                } else if (Daten[1].equals("ERROR")) {
                    temp.put("ERROR", "error");
                    datenTemp.add(temp);
                } else if (!Daten[1].equals("END")) {
                    temp.put("Berechtigung", Daten[1]);
                    datenTemp.add(temp);
                } else {
                    datenUserBerechtigung = datenTemp;
                    datenTemp = new ArrayList<HashMap>();
                    go = true;
                }
                break;
                case 701:
                if (Daten[1].equals("START")) {
                    datenStoerungArchiv.clear();
                } else if (Daten[1].equals("ERROR")) {
                    temp.put("ERROR", "error");
                    datenTemp.add(temp);
                } else if (!Daten[1].equals("END")) {
                    temp.put("IDAnhaenge", Daten[1]);
                    temp.put("Name", Daten[2]);
                    temp.put("DateiName", Daten[3]);
                    temp.put("Pfad", Daten[4]);
                    temp.put("IDStoerung", Daten[5]);
                    datenTemp.add(temp);
                } else {
                    datenStoerungArchiv = datenTemp;
                    datenTemp = new ArrayList<HashMap>();
                    go = true;
                }
                break;
             case 702:
                    go = true;
                break;
            case 800:
                if (Daten[1].equals("START")) {
                    datenDoku.clear();
                } else if (Daten[1].equals("ERROR")) {
                    temp.put("ERROR", "error");
                    datenTemp.add(temp);
                } else if (!Daten[1].equals("END")) {
                    temp.put("DokuName", Daten[1]);
                    temp.put("DokuKName", Daten[2]);
                    datenTemp.add(temp);
                } else {
                    datenDoku = datenTemp;
                    datenTemp = new ArrayList<HashMap>();
                    go = true;
                }
                break;

            case 900:
                if (Daten[1].equals("START")) {
                    datenNewUser.clear();
                } else if (Daten[1].equals("ERROR")) {
                    temp.put("ERROR", "error");
                    datenTemp.add(temp);
                } else if (!Daten[1].equals("END")) {
                    temp.put("UserID", Daten[1]);
                    temp.put("UserName", Daten[2]);
                    temp.put("UserVorname", Daten[3]);
                    temp.put("UserPersonalNr", Daten[4]);
                    temp.put("UserFunktion", Daten[5]);
                    temp.put("UserTel", Daten[6]);
                    temp.put("UserFax", Daten[7]);
                    temp.put("UserAbteilung", Daten[8]);
                    temp.put("UserStandort", Daten[9]);
                    temp.put("UserStatus", Daten[10]);
                    temp.put("UserDatum", Daten[11]);
                    temp.put("UsereMail", Daten[12]);
                    temp.put("UserHardware", Daten[13]);
                    temp.put("UserSystem", Daten[14]);
                    temp.put("UserKommentar", Daten[15]);
                    datenTemp.add(temp);
                } else {
                    datenNewUser = datenTemp;
                    datenTemp = new ArrayList<HashMap>();
                    go = true;
                }
                break;
             case 500:
                if (Daten[1].equals("START")) {
                    datenDoku.clear();
                } else if (Daten[1].equals("ERROR")) {
                    temp.put("ERROR", "error");
                    datenTemp.add(temp);
                } else if (!Daten[1].equals("END")) {
                    temp.put("IDNUser", Daten[1]);
                    temp.put("IDS", Daten[2]);
                    temp.put("Daten", Daten[3]);
                    datenTemp.add(temp);
                } else {
                    datenNewSoftware = datenTemp;
                    datenTemp = new ArrayList<HashMap>();
                    go = true;
                }
                break;
            case 901:
                if (Daten[1].equals("START")) {
                    datenStoerung.clear();
                } else if (Daten[1].equals("ERROR")) {
                    temp.put("error","error");
                    datenTemp.add(temp);
                } else if (!Daten[1].equals("END")) {
                    temp.put("IDStoerung", Daten[1]);
                    temp.put("Datum", Daten[2]);
                    temp.put("WVDatum", Daten[3]);
                    temp.put("Status", Daten[4]);
                    temp.put("Melder", Daten[5]);
                    temp.put("Inhalt", Daten[6]);
                    temp.put("ersteller", Daten[7]);
                    temp.put("bearbeiter", Daten[8]);
                    temp.put("IDAdressbuch", Daten[9]);
                    temp.put("Betreff", Daten[10]);
                    temp.put("Online", Daten[11]);
                    temp.put("aktiv", Daten[12]);
                    datenTemp.add(temp);
                } else {
                    datenStoerung = datenTemp;
                    datenTemp = new ArrayList<HashMap>();
                    go = true;
                }
                break;
            case 902:
                if (Daten[1].equals("START")) {
                    datenAdress.clear();
                } else if (Daten[1].equals("ERROR")) {
                    temp.put("ERROR", "error");
                    datenTemp.add(temp);
                } else if (!Daten[1].equals("END")) {
                    temp.put("IDAdressbuch", Daten[1]);
                    temp.put("Name", Daten[2]);
                    temp.put("Vorname", Daten[3]);
                    temp.put("Abteilung", Daten[4]);
                    temp.put("Tel", Daten[5]);
                    temp.put("Handy", Daten[6]);
                    temp.put("Fax", Daten[7]);
                    temp.put("Hinweis", Daten[8]);
                    temp.put("IDAdressbuchFirma", Daten[9]);
                    temp.put("eMail", Daten[10]);
                    
                    datenTemp.add(temp);
                } else {
                    datenAdress = datenTemp;
                    datenTemp = new ArrayList<HashMap>();
                    go = true;
                }
                break;
            case 903:
                if (Daten[1].equals("START")) {
                    datenAdressFirma.clear();
                } else if (Daten[1].equals("ERROR")) {
                    temp.put("ERROR", "error");
                    datenTemp.add(temp);
                } else if (!Daten[1].equals("END")) {
                    temp.put("IDAdressbuchFirma", Daten[1]);
                    temp.put("FirmenNamen", Daten[2]);
                    temp.put("Strasse", Daten[3]);
                    temp.put("PLZ", Daten[4]);
                    temp.put("Ort", Daten[5]);
                    temp.put("Hinweis", Daten[6]);
                    datenTemp.add(temp);
                } else {
                    datenAdressFirma = datenTemp;
                    datenTemp = new ArrayList<HashMap>();
                    go = true;
                }
                break;
            case 904:
                if (Daten[1].equals("START")) {
                    datenNetzAdresse.clear();
                } else if (Daten[1].equals("ERROR")) {
                    temp.put("ERROR", "error");
                    datenTemp.add(temp);
                } else if (!Daten[1].equals("END")) {
                    temp.put("IDAdressbuchFirma", Daten[1]);
                    temp.put("FirmenNamen", Daten[2]);
                    temp.put("Strasse", Daten[3]);
                    temp.put("PLZ", Daten[4]);
                    temp.put("Ort", Daten[5]);
                    temp.put("Hinweis", Daten[6]);
                    datenTemp.add(temp);
                } else {
                    datenNetzAdresse = datenTemp;
                    datenTemp = new ArrayList<HashMap>();
                    go = true;
                }
                break;
           

            case 999:
                if (Daten[1].equals("START")) {
                    datenTemp.clear();
                } else if (Daten[1].equals("ERROR")) {
                    temp.put("ERROR", "error");
                    datenTemp.add(temp);
                } else if (!Daten[1].equals("END")) {
                    temp.put("Datei", Daten[1]);
                    datenTemp.add(temp);
                } else {
                    try {
                        BufferedWriter write = new BufferedWriter(new FileWriter(new File(Daten[2])));
                        for (int i = 0; i < datenTemp.size(); i++) {
                            write.write(datenTemp.get(i).get("Datei")+"\n");
                        }
                        write.close();
                    } catch (Exception ex) {
                    }

                }
                break;

            case 1000:
                erledigt = false;
                break;
            case 1001:
                erledigt = false;
                break;
            case 1002:
                erledigt = false;
                break;
            case 1003:
                erledigt = false;
                break;
            case 1004:
                erledigt = false;
                break;
            case 1005:
                erledigt = false;
                break;
            case 1006:
                erledigt = false;
                break;
            case 1007:
                erledigt = false;
                break;
            case 1008:
                erledigt = false;
                break;
            case 1500:
                IDNewUser=Daten[2];
                break;
            case 1901:
                ID=Daten[2];
                break;
            case 99999:
                go = true;
               break;
            
           
            case 8888:
                if (Daten[1].equals("START")) {
                    datenInfo.clear();
                } else if (Daten[1].equals("ERROR")) {
                    temp.put("ERROR", "error");
                    datenTemp.add(temp);
                } else if (!Daten[1].equals("END")) {
                    temp.put("Betreff", Daten[1]);
                    temp.put("Info", Daten[2]);
                    datenTemp.add(temp);
                } else {
                    datenInfo = datenTemp;
                    datenTemp = new ArrayList<HashMap>();
                }
                break;
        }
    }
}
