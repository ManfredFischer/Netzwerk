/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package netzwerkverwaltung;

import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author manfred.fischer
 */
public class IPPannel {

    private ClientNetzwerk clients;
    public ArrayList<ArrayList<String>> getIPs = new ArrayList<ArrayList<String>>();
    private ArrayList<String> datenIP = new ArrayList<String>();

    public IPPannel(ClientNetzwerk cl) {
        clients = cl;
    }

    public void getIPInfo(int klick, String IP, HauptBildschirm h) {
        if (klick == 1) {
            JOptionAuswahl show = new JOptionAuswahl(IP, h);
            show.setVisible(true);
        }

    }

    public String anzahlIP(String Netz, String ergebnis) {
        int[] wert = {1, 2, 4, 8, 16, 32, 64, 128};
        int wertTemp = 0, xTemp = 0, anzahlNULLER = 0, anzahlNULLERGesamt = 0, ip = 0, t = 2;
        String[] netz = Netz.split("\\.");
        for (int c = 0; c < netz.length; c++) {
            wertTemp = Integer.parseInt(netz[c]);
            for (int i = wert.length - 1; i > 0; i--) {
                xTemp = wertTemp;
                if (wertTemp != 0) {
                    if (wertTemp == 0) {
                        anzahlNULLER = i;
                        break;
                    } else if (wertTemp < 0) {
                        wertTemp = xTemp;
                    }
                } else {
                    anzahlNULLER = i;
                    break;
                }
                wertTemp = wertTemp - wert[i];
            }
            if (c == 0) {
                if (anzahlNULLER > 0) {
                    anzahlNULLER = anzahlNULLER + 1;
                }
            } else if (c == 1) {
                if (anzahlNULLER > 0) {
                    anzahlNULLER = anzahlNULLER + 1;
                }
            } else if (c == 2) {
                if (anzahlNULLER > 0) {
                    anzahlNULLER = anzahlNULLER + 1;
                }
            }
            anzahlNULLERGesamt = anzahlNULLERGesamt + anzahlNULLER;
        }
        
        
        for (int i = 0; i <= anzahlNULLERGesamt; i++) {
            ip = (int) (ip + Math.pow(2, i));
        }
        ergebnis = (ip - 2) + "";
        return ergebnis;

    }

    private HashMap BerechnungAnzahlIP(String Maske, String Netz) {
        int[] wert = {1, 2, 4, 8, 16, 32, 64, 128};
        String[] netz = Netz.split("\\.");
        
        HashMap info = new HashMap();
        info.put("anzahl", anzahlIP(Maske, ""));
        
        int anzahlNetzID = Integer.parseInt(info.get("anzahl").toString()) / 8,
            anzahlNetz = Integer.parseInt(info.get("anzahl").toString()) % 8, 
            gesamt=0;
        
        for (int i=anzahlNetz+1;i<wert.length;i++){
            gesamt = gesamt + wert[i];
        }
        
        
        
        return info;
    }

    public void insertInfo(String Server, String IP, ArrayList<ArrayList<String>> daten2, String Netz, String NetzID) {
        getIPs = new ArrayList<ArrayList<String>>();
        int anzahl = 1;
        HashMap info = BerechnungAnzahlIP("255.255.192.0", "0");
        int anzahlGesamt = Integer.parseInt(info.get("anzahl").toString());
        if (!daten2.isEmpty()) {
           
             for (int a = 0; a < daten2.size(); a++) {
                String[] IPW = daten2.get(a).get(6).toString().split(",");
                if (IPW.length > 1) {
                    for (int c = 0; c < IPW.length; c++) {
                        getIPs.add(setInfo(daten2, IP + IPW[c], Netz, NetzID, a));
                    }

                } else {
                    if (IPW[0].equals("USB")) {
                        getIPs.add(setInfo(daten2, "USB", Netz, NetzID, a));
                    } else if (IPW[0].equals("DHCP")) {
                        getIPs.add(setInfo(daten2, "DHCP", Netz, NetzID, a));
                    } else if (IPW[0].equals("KEINE")) {
                        getIPs.add(setInfo(daten2, "KEINE", Netz, NetzID, a));
                    } else {
                        getIPs.add(setInfo(daten2, IP + daten2.get(a).get(6).toString(), Netz, NetzID, a));
                    }
                    anzahl++;
                 }
             
            }
             
             for (int i=0;i<anzahlGesamt;i++){
                 
             }
             
        }
    }

    private ArrayList<String> setInfo(ArrayList<ArrayList<String>> daten2, String IP, String Netz, String NetzID, int a) {
        datenIP = new ArrayList<String>();
        datenIP.add(daten2.get(a).get(0).toString());
        datenIP.add(daten2.get(a).get(1).toString());
        datenIP.add(IP);
        datenIP.add(daten2.get(a).get(3).toString());
        datenIP.add(daten2.get(a).get(2).toString());
        datenIP.add(daten2.get(a).get(7).toString());
        datenIP.add(daten2.get(a).get(4).toString());
        datenIP.add(Netz);
        datenIP.add(NetzID);
        return datenIP;
    }
}
/*
 * 
 * 
 */