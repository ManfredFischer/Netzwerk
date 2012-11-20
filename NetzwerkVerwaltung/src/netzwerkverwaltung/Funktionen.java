/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package netzwerkverwaltung;


import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;
import javax.swing.*;

/**
 *
 * @author manfred.fischer
 */
public class Funktionen {

    private HauptBildschirm grafik;
    private int loganz;
    private ArrayList<ArrayList<String>> SiedleDaten;
    private ArrayList<ArrayList<String>> ZehnderDaten;
    private ArrayList<ArrayList<String>> AuftragsDaten;
    private String StartDaten;
    private String trai;
    private String fileName;
    private String header;
    private HashMap VerladeEinheit;
    private ArrayList Z, K;
    private JFileChooser datei;
    private File file;
    private String EndDaten;
    private Date date;
    private SimpleDateFormat formatter, da;
    private String[] DateiNamen = {"475735magnetic", "bpv", "buerkle-freiburg", "cell_gries", "Ebco-Albburck",
        "e-wittmann", "Ketterer", "kft", "ksp", "MISTRAL", "rau", "Rotzler", "SCHENKER", "scherzinger", "schladerer",
        "schneider", "slg", "sphinx", "wildenAG"};
    private ArrayList<String> Daten = new ArrayList<String>();
    private ArrayList<ArrayList<ArrayList<ArrayList<String>>>> gardnerDenfer = new ArrayList<ArrayList<ArrayList<ArrayList<String>>>>();

    public Funktionen(HauptBildschirm gr) {
        grafik = gr;
        datei = new JFileChooser();
        Z = new ArrayList();
        K = new ArrayList();
        da = new SimpleDateFormat("yyyy.MM.dd");
        formatter = new SimpleDateFormat("yyyy.MM.dd 'at' HH:mm:ss ");
        loganz = 1;
        SiedleDaten = new ArrayList();
        ZehnderDaten = new ArrayList();
        AuftragsDaten = new ArrayList();
        formatter = new SimpleDateFormat("yyyy.MM.dd 'at' HH:mm:ss ");
        VerladeEinheit = new HashMap();
        VerladeEinheit.put("FP", "FP");
        VerladeEinheit.put("F", "FP");
        VerladeEinheit.put("KI", "KI");
        VerladeEinheit.put("SACK", "SA");
        VerladeEinheit.put("SA", "SA");
        VerladeEinheit.put("OFFEN", "UV");
        VerladeEinheit.put("PACK", "PA");
        VerladeEinheit.put("VG", "VG");
        VerladeEinheit.put("KT", "KT");
        VerladeEinheit.put("KTM", "KT");
        VerladeEinheit.put("BD", "BU");
        VerladeEinheit.put("BU", "BU");
        VerladeEinheit.put("BEH", "BH");
        VerladeEinheit.put("COLLI", "CO");
        VerladeEinheit.put("CONT", "CO");
        VerladeEinheit.put("EURO", "FP");
        VerladeEinheit.put("EW", "EW");
        VerladeEinheit.put("EWP", "EW");
        VerladeEinheit.put("PACK", "PA");
        VerladeEinheit.put("POOL", "GP");
        VerladeEinheit.put("BL", "BL");
        VerladeEinheit.put("CL", "CL");
        VerladeEinheit.put("DIS", "KT");
        VerladeEinheit.put("EI", "EI");
        VerladeEinheit.put("FL", "FL");
        VerladeEinheit.put("HOBB", "HB");
        VerladeEinheit.put("KA", "CO");
        VerladeEinheit.put("LDG", "PT");
        VerladeEinheit.put("PA", "PA");
        VerladeEinheit.put("PK", "PK");
        VerladeEinheit.put("PT", "PT");
        VerladeEinheit.put("RING", "RI");
        VerladeEinheit.put("ROLLE", "RO");
        VerladeEinheit.put("ST", "ST");
        VerladeEinheit.put("TANK", "CO");
        VerladeEinheit.put("TRO", "TR");
        VerladeEinheit.put("VERP", "CO");
    }

   

    private boolean containsString(String s, String subString) {
        return s.toLowerCase().indexOf(subString.toLowerCase()) > -1 ? true : false;
    }

   
    public void datenSortieren(String Ort) {
        try {
            Date Datum = new Date();
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            File dirA = new File(Ort);
            File dir;
            File[] dList = dirA.listFiles();
            for (File vDatei : dList) {
                if (!vDatei.isDirectory()) {
                    String DateOrdner = Ort + "/" + formatter.format(vDatei.lastModified());
                    dir = new File(DateOrdner);
                    dir.mkdir();
                    File send = new File(DateOrdner + "/" + vDatei.getName());
                    vDatei.renameTo(send);
                }
            }
        } catch (Exception c) {
        }

    }

    public void convertieren() {
        if (grafik.auswahl.getSelectedItem().equals("IFTMIN_SIEDLE")) {
            auslesenDerSIEDLEAuftraege(Daten);
        } else if (grafik.auswahl.getSelectedItem().equals("RUNTAL")) {
            auslesenDerRuntalAuftraege(K, Z, fileName);
        } else if (grafik.auswahl.getSelectedItem().equals("Zehnder_SCHENKER_ZDE")) {
            auslesenDerZehnderZDEAuftraege(K, Z, fileName);
        } else if (grafik.auswahl.getSelectedItem().equals("Zehnder_SCHENKER_ZBN")) {
            auslesenDerZehnderZBNAuftraege(K, Z, fileName);
        } else if (grafik.auswahl.getSelectedItem().equals("Gardner_Denver")) {
            auslesenDerGardnerDenverDaten(DateiNameGardner);
        }
        grafik.conv.setEnabled(false);
        grafik.convAdmin.setEnabled(false);
        grafik.auswahl.setEnabled(true);
    }

    public void auswalDateiQuelle() {
        Object[] options = {"Yes, please",
            "No, thanks"};
        grafik.auswahlK.setEnabled(false);
        int n = JOptionPane.showOptionDialog(new JFrame(),
                " Fortfahren mit " + grafik.auswahlK.getSelectedItem(),
                "Richtig?",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.INFORMATION_MESSAGE,
                null, //do not use a custom Icon
                options, //the titles of buttons
                options[0]);
        if (n == 0) {
            int getInfo = datei.showOpenDialog(datei);
            if (getInfo == JFileChooser.APPROVE_OPTION) {
                file = datei.getSelectedFile();
                grafik.desti.setText(file.getAbsolutePath());
                DatenBearbeitung(file, grafik.auswahlK.getSelectedItem().toString());
            } else {
                grafik.auswahlK.setEnabled(true);
            }

        }
    }

    public void auswalDateiZiel() {
        JFileChooser fc = new JFileChooser();
        fc.setDialogTitle("Select folder");
        fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        if (fc.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
            grafik.source.setText(fc.getSelectedFile().getAbsolutePath());
        }
    }

    private void DatenBearbeitung(File files, String Siedle) {

        try {
            readDatei(files, Siedle);
        } catch (Exception d) {
            JOptionPane.showMessageDialog(new JFrame(), "Datei nicht vorhanden");
        }
    }

    

    private void Zehnder() {
        String Satzart = "";
        int anz = 0;
        Z = new ArrayList();
        K = new ArrayList();
        ArrayList info = new ArrayList();
        ArrayList infoZ = new ArrayList();
        for (int i = 0; i < Daten.size(); i++) {
            Satzart = ((String) Daten.get(i)).substring(0, 1);
            if (Satzart.equals("K")) {
                info = new ArrayList();
                info.add(((String) Daten.get(i)).substring(1, 26));
                info.add(((String) Daten.get(i)).substring(26, 51));
                info.add(((String) Daten.get(i)).substring(51, 76));
                info.add(((String) Daten.get(i)).substring(76, 101));
                info.add(((String) Daten.get(i)).substring(101, 126));
                info.add(((String) Daten.get(i)).substring(126, 132));
                info.add(((String) Daten.get(i)).substring(132, 172));
                info.add(((String) Daten.get(i)).substring(172, 178));
                info.add(((String) Daten.get(i)).substring(178, 181));
                info.add(((String) Daten.get(i)).substring(181, 184));
                info.add(((String) Daten.get(i)).substring(184, 189));
                info.add(((String) Daten.get(i)).substring(189, 195));
                info.add(((String) Daten.get(i)).substring(195, 201));
                info.add(((String) Daten.get(i)).substring(201, 207));
                info.add(((String) Daten.get(i)).substring(207, 216));
                info.add(((String) Daten.get(i)).substring(216, 220));
                info.add(((String) Daten.get(i)).substring(220, 226));
                info.add(((String) Daten.get(i)).substring(226, 228));
                info.add(((String) Daten.get(i)).substring(228, 231));
                info.add(((String) Daten.get(i)).substring(231, 232));
                info.add(((String) Daten.get(i)).substring(232, 238));
                try {
                    info.add(((String) Daten.get(i)).substring(238, 244));
                    info.add(((String) Daten.get(i)).substring(244, 250));
                } catch (Exception e) {
                }
                K.add(info);
                continue;
            }
            if (Satzart.equals("Z")) {
                infoZ = new ArrayList();
                infoZ.add(((String) Daten.get(i)).substring(1, 6));
                infoZ.add(((String) Daten.get(i)).substring(6, 11));
                infoZ.add(((String) Daten.get(i)).substring(11, 16));
                infoZ.add(((String) Daten.get(i)).substring(16, 21));
                infoZ.add(((String) Daten.get(i)).substring(21, 26));
                infoZ.add(((String) Daten.get(i)).substring(26, 34));
                infoZ.add(((String) Daten.get(i)).substring(34, 43));
                infoZ.add(((String) Daten.get(i)).substring(43, 52));
                infoZ.add(((String) Daten.get(i)).substring(52, 61));
                infoZ.add(((String) Daten.get(i)).substring(61, 70));
                infoZ.add(((String) Daten.get(i)).substring(70, 79));
                infoZ.add(((String) Daten.get(i)).substring(79, 84));
                infoZ.add(((String) Daten.get(i)).substring(84, 89));
                infoZ.add(((String) Daten.get(i)).substring(89, 94));
                infoZ.add(((String) Daten.get(i)).substring(94, 99));
                infoZ.add(((String) Daten.get(i)).substring(99, 104));
                infoZ.add(((String) Daten.get(i)).substring(104, 109));
                infoZ.add(((String) Daten.get(i)).substring(109, 114));
                infoZ.add(((String) Daten.get(i)).substring(114, 119));
                infoZ.add(((String) Daten.get(i)).substring(119, 124));
                infoZ.add(((String) Daten.get(i)).substring(124, 129));
                infoZ.add(((String) Daten.get(i)).substring(129, 133));
                infoZ.add(((String) Daten.get(i)).substring(133, 137));
                infoZ.add(((String) Daten.get(i)).substring(137, 141));
                infoZ.add(((String) Daten.get(i)).substring(141, 145));
                infoZ.add(((String) Daten.get(i)).substring(145, 149));
                infoZ.add(((String) Daten.get(i)).substring(149, 153));
                infoZ.add(((String) Daten.get(i)).substring(153, 157));
                infoZ.add(((String) Daten.get(i)).substring(157, 161));
                infoZ.add(((String) Daten.get(i)).substring(161, 165));
                infoZ.add(((String) Daten.get(i)).substring(165, 169));
                infoZ.add(((String) Daten.get(i)).substring(169, 174));
                infoZ.add(((String) Daten.get(i)).substring(174, 179));
                infoZ.add(((String) Daten.get(i)).substring(179, 184));
                infoZ.add(((String) Daten.get(i)).substring(184, 189));
                infoZ.add(((String) Daten.get(i)).substring(189, 194));
                infoZ.add(((String) Daten.get(i)).substring(194, 201));
                Z.add(infoZ);
            }
        }

        String te = "";
        for (int c = 0; c < K.size(); c++) {
            for (int d = 0; d < ((ArrayList) K.get(c)).size(); d++) {
                String KTemp[] = ((String) ((ArrayList) K.get(c)).get(d)).split(" ");
                if (KTemp.length > 0) {
                    if (KTemp[0].equals(" ")) {
                        ((ArrayList) K.get(c)).set(d, "");
                        continue;
                    }
                    for (int r = 0; r < KTemp.length; r++) {
                        te = (new StringBuilder()).append(te).append(" ").append(KTemp[r]).toString();
                    }

                    ((ArrayList) K.get(c)).set(d, te.substring(1, te.length()));
                    te = "";
                } else {
                    ((ArrayList) K.get(c)).set(d, "");
                }
            }

        }

        for (int c = 0; c < Z.size(); c++) {
            for (int d = 0; d < ((ArrayList) Z.get(c)).size(); d++) {
                String KTemp[] = ((String) ((ArrayList) Z.get(c)).get(d)).split(" ");
                if (KTemp.length > 0) {
                    if (KTemp[0].equals(" ")) {
                        ((ArrayList) Z.get(c)).set(d, "");
                        continue;
                    }
                    for (int r = 0; r < KTemp.length; r++) {
                        te = (new StringBuilder()).append(te).append(" ").append(KTemp[r]).toString();
                    }

                    ((ArrayList) Z.get(c)).set(d, te.substring(1, te.length()));
                    te = "";
                } else {
                    ((ArrayList) Z.get(c)).set(d, "");
                }
            }

        }

        for (int e = 0; e < Z.size(); e++) {
            anz++;
            grafik.text.append((new StringBuilder()).append(anz).append("+ ").toString());
            for (int r = 0; r < ((ArrayList) K.get(e)).size(); r++) {
                if (!NullenWeg((String) ((ArrayList) K.get(e)).get(r)).equals("")) {
                    grafik.text.append((new StringBuilder()).append("+ [").append(r).append("] ").append(NullenWeg((String) ((ArrayList) K.get(e)).get(r))).toString());
                }
            }

            grafik.text.append("\n");
            anz++;
            grafik.text.append((new StringBuilder()).append(anz).append("+ ").toString());
            for (int d = 0; d < ((ArrayList) Z.get(e)).size(); d++) {
                if (!NullenWeg((String) ((ArrayList) Z.get(e)).get(d)).equals("")) {
                    grafik.text.append((new StringBuilder()).append("+ [").append(d).append("] ").append(NullenWeg((String) ((ArrayList) Z.get(e)).get(d))).toString());
                }
            }

            grafik.text.append("\n");
        }
    }

    public void DateiAuswahl() {
        grafik.text.setText("");
        grafik.text2.setText("");
        Object[] options = {"Yes, please",
            "No, thanks"};
        grafik.auswahl.setEnabled(false);
        int n = JOptionPane.showOptionDialog(new JFrame(),
                " Fortfahren mit " + grafik.auswahl.getSelectedItem(),
                "Richtig?",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.INFORMATION_MESSAGE,
                null, //do not use a custom Icon
                options, //the titles of buttons
                options[0]);
        if (n == 0) {
            int getInfo = datei.showOpenDialog(datei);
            if (getInfo == JFileChooser.APPROVE_OPTION) {
                file = datei.getSelectedFile();
                try {
                    grafik.text.setText("");
                    readDatei(file, grafik.auswahl.getSelectedItem().toString());

                } catch (Exception d) {
                    JOptionPane.showMessageDialog(new JFrame(), d);
                }
            }
            grafik.convAdmin.setEnabled(true);
        } else {
            grafik.convAdmin.setEnabled(false);
            grafik.auswahl.setEnabled(true);
        }

    }

    private void readDatei(File file, String bedingung) throws Exception {
        Daten.clear();
        BufferedReader fileDaten = new BufferedReader(new FileReader(file));
        String[] tempN = file.getName().split(".TXT");
        fileName = tempN[0];
        String data = fileDaten.readLine();
        while (data != null) {
            if (bedingung.equals("IFTMIN_SIEDLE")) {
                String[] temp = data.split("'");
                for (int i = 0; i < temp.length; i++) {

                    if (temp[i].startsWith("UNH")) {
                        Daten.add(temp[i]);
                        grafik.text.append("Auftrag " + i + ": \n");
                        grafik.text.append("\n" + temp[i] + "\n");
                    } else {
                        Daten.add(temp[i]);
                        grafik.text.append(temp[i] + "\n");
                    }

                }

            } else {
                Daten.add(data);
            }
            data = fileDaten.readLine();
        }
        fileDaten.close();

        if (bedingung.startsWith("Zehnder")) {
            Zehnder();
        } else if (bedingung.startsWith("Gardner")) {
            DateiNameGardner = file.getName();
            gardnerDenverDaten();
        }

    }

    public void DateiSchreiben() {
        if (!grafik.desti.getText().equals("") && !grafik.source.getText().equals("")) {
            if (grafik.auswahlK.getSelectedItem().equals("IFTMIN_SIEDLE")) {
                auslesenDerSIEDLEAuftraege(Daten);
                schreibSiedleDaten(grafik.source.getText(), grafik.Name1.getText());
            } else if (grafik.auswahlK.getSelectedItem().equals("Zehnder_RUNTAL")) {
                auslesenDerRuntalAuftraege(K, Z, fileName);
                runtalDatenSchreiben(grafik.Name1.getText(), grafik.source.getText());
            } else if (grafik.auswahlK.getSelectedItem().equals("Zehnder_SCHENKER_ZDE")) {
                auslesenDerZehnderZDEAuftraege(K, Z, fileName);
                zehnderZDEDatenSchreiben(grafik.Name1.getText(), grafik.source.getText());
            } else if (grafik.auswahlK.getSelectedItem().equals("Zehnder_SCHENKER_ZBN")) {
                auslesenDerZehnderZBNAuftraege(K, Z, fileName);
                zehnderZBNDatenSchreiben(grafik.Name1.getText(), grafik.source.getText());
            } else if (grafik.auswahlK.getSelectedItem().equals("Gardner_Denver")) {
                auslesenDerGardnerDenverDaten(DateiNameGardner);
                GardnerDenverDatenSchreiben(grafik.Name1.getText(), grafik.source.getText());
            }
            grafik.desti.setText("");
        } else {
            JOptionPane.showMessageDialog(new JFrame(), "Bitte Dest. Angeben");
        }
        grafik.auswahlK.setEnabled(true);
        grafik.Name1.setText("");
    }
    private String DateiNameGardner = "";

    public void auslesenDerSIEDLEAuftraege(ArrayList<String> Daten) {
        ArrayList<String> temp;
        int a = 0;
        while (a < Daten.size()) {
            temp = new ArrayList<String>();
            if (Daten.get(a).startsWith("UNB")) {
                StartDaten = Daten.get(a);
                a++;
            } else if (Daten.get(a).startsWith("UNH")) {
                do {
                    if (!Daten.get(a).startsWith("UNZ")) {
                        temp.add(Daten.get(a));
                        a++;
                        if (Daten.size() <= a) {
                            break;
                        }
                    } else {
                        EndDaten = Daten.get(a);
                        a++;
                        break;
                    }
                } while ((!Daten.get(a).startsWith("UNH")));
                AuftragsDaten.add(temp);
            } else {
                a++;
            }
        }
        for (int y = 0; y < AuftragsDaten.size(); y++) {
            for (int c = 0; c < AuftragsDaten.get(y).size(); c++) {
                String[] d = AuftragsDaten.get(y).get(c).split("\\?\\+");
                if (d.length > 1) {
                    // String t = d[1].substring(1, d[1].length() - 1);
                    AuftragsDaten.get(y).set(c, (d[0] + "&" + d[1]));
                }
            }
        }

        ArrayList<HashMap> kost = iftminSIEDLEKost();

        ArrayList<String> tempDaten = new ArrayList<String>();
        ArrayList<HashMap> auko = iftminSIEDLEAUKO();
        ArrayList<HashMap> aupo = iftminSIEDLEAUPO();
        int anzahlSegmente = 2;
        header = WeikundaConvertierenHEADER(iftminSIEDLEHeader(StartDaten));


        grafik.text2.append("Weikunder Daten: \n\n");
        grafik.text2.append(header + "'\n");

        String tempD = "";
        for (int c = 0; c < kost.size(); c++) {

            tempDaten.add(WeikundaConvertierenKOST(kost.get(c)) + "'");
            tempDaten.add(WeikundaConvertierenAUKO(auko.get(c)) + "'");
            tempDaten.add(WeikundaConvertierenAUPO(aupo.get(c)) + "'");

            SiedleDaten.add(tempDaten);
            tempDaten = new ArrayList<String>();
        }

        for (int i = 0; i < SiedleDaten.size(); i++) {
            grafik.text2.append("Auftrag: \n");
            for (int e = 0; e < SiedleDaten.get(i).size(); e++) {
                anzahlSegmente++;
                grafik.text2.append("Nr. " + e + ": " + SiedleDaten.get(i).get(e) + "\n");

            }
        }
        trai = WeikundaConvertierenTRAI(iftminSIELDETrai(StartDaten, anzahlSegmente));
        grafik.text2.append(trai + "'\n");


    }

    public void schreibSiedleDaten(String desti, String name) {
        Date dada = new Date();
        try {

            int anz = 1;
            String[] tempDaten = desti.split("\\\\");
            String d = tempDaten[0];
            for (int a = 1; a < tempDaten.length; a++) {
                d = d + "/" + tempDaten[a];
            }
            FileWriter Datei = new FileWriter(new File(d + "/" + name + ".txt"));
            BufferedWriter schreib = new BufferedWriter(Datei);
            schreib.write(header + "'");
            for (int i = 0; i < SiedleDaten.size(); i++) {
                for (int b = 0; b < SiedleDaten.get(i).size(); b++) {
                    anz++;
                    schreib.write(SiedleDaten.get(i).get(b));
                }
            }
            schreib.write(trai + "'");
            anz++;
            schreib.close();
            Datei.close();
            grafik.hist.append(formatter.format(dada) + ": Siedle (" + name + ")erfolgreich eingespielt \n");
            loganz++;
            SiedleDaten.clear();
            AuftragsDaten.clear();
        } catch (IOException e) {
            grafik.hist.append(formatter.format(dada) + ": Siedle FEHLER!!!! \n");
        }
    }

    private HashMap iftminSIEDLEHeader(String tDaten) {
        String[] DHeader = tDaten.split("\\+");
        HashMap Daten = new HashMap();
        String[] tempDaten = DHeader[2].split(":");
        Daten.put("SHVK", tempDaten[0]);

        tempDaten = DHeader[3].split(":");
        Daten.put("SHEK", "Freiburg");

        tempDaten = DHeader[4].split(":");
        String ja = tempDaten[0].substring(0, 2);
        String mo = tempDaten[0].substring(2, 4);
        String tag = tempDaten[0].substring(4, 6);
        Daten.put("SHDT", tag + mo + ja);
        Daten.put("SHZT", tempDaten[1]);
        Daten.put("SHRN", DHeader[5]);
        Daten.put("SHZS", "07");
        Daten.put("SHSN", "1");
        Daten.put("SHVO", "Freiburg");
        Daten.put("SHVA", "MISTRAL");
        Daten.put("SHVT", "TAC211");
        return Daten;
    }

    private ArrayList<HashMap> iftminSIEDLEKost() {
        ArrayList<HashMap> Daten = new ArrayList<HashMap>();
        ArrayList<HashMap> dtemp = new ArrayList<HashMap>();
        HashMap temp;

        for (int i = 0; i < AuftragsDaten.size(); i++) {
            dtemp = new ArrayList<HashMap>();
            for (int b = 0; b < AuftragsDaten.get(i).size(); b++) {
                temp = new HashMap();
                if (AuftragsDaten.get(i).get(b).toString().startsWith("NAD+CZ")) {
                    String[] tempDaten = AuftragsDaten.get(i).get(b).split("\\+");
                    temp.put("N074", "01");
                    temp.put("N080", "01");
                    temp.put("N083", "78");
                    temp.put("N045", tempDaten[2]);
                    temp.put("A022", tempDaten[4]);
                    temp.put("A023", tempDaten[3]);
                    temp.put("S684", tempDaten[9]);
                    temp.put("A151", tempDaten[6]);
                    temp.put("A027", tempDaten[5]);
                    temp.put("N241", tempDaten[8]);
                    temp.put("S055", "2");
                    Daten.add(temp);
                    break;
                }

            }

        }
        return Daten;
    }

    private ArrayList<HashMap> iftminSIEDLEAUKO() {

        ArrayList<HashMap> tDaten = new ArrayList<HashMap>();
        HashMap temp, t;
        HashMap tempNAD = new HashMap();
        String[] tempDaten;
        for (int i = 0; i < AuftragsDaten.size(); i++) {
            temp = new HashMap();
            for (int b = 0; b < AuftragsDaten.get(i).size(); b++) {
                if (AuftragsDaten.get(i).get(b).startsWith("UNB")) {
                } else if (AuftragsDaten.get(i).get(b).startsWith("UNH")) {
                } else if (AuftragsDaten.get(i).get(b).startsWith("BGM")) {
                    tempDaten = AuftragsDaten.get(i).get(b).split("\\+");
                    temp.put("N009", tempDaten[2]);
                } else if (AuftragsDaten.get(i).get(b).startsWith("DTM")) {
                } else if (AuftragsDaten.get(i).get(b).startsWith("TSR")) {
                } else if (AuftragsDaten.get(i).get(b).startsWith("CNT")) {
                } else if (AuftragsDaten.get(i).get(b).startsWith("TOD")) {
                    tempDaten = AuftragsDaten.get(i).get(b).split("\\+");
                    if (tempDaten[3].equals("CPT")) {
                        temp.put("S093", "04");
                    }
                    if (tempDaten[3].equals("EXW")) {
                        temp.put("S093", "11");
                    }
                } else if (AuftragsDaten.get(i).get(b).startsWith("RFF")) {
                    tempDaten = AuftragsDaten.get(i).get(b).split("\\+");
                    tempDaten = tempDaten[1].split(":");
                    if (tempDaten[0].equals("VN")) {
                        temp.put("N058", tempDaten[1]);
                    }
                    //temp.put("N314", tempDaten[1]);
                } else if (AuftragsDaten.get(i).get(b).startsWith("DTM")) {
                } else if (AuftragsDaten.get(i).get(b).startsWith("NAD+CZ")) {
                } else if (AuftragsDaten.get(i).get(b).startsWith("NAD+CN")) {
                    tempDaten = AuftragsDaten.get(i).get(b).split("\\+");
                    //temp.put("N072", tempDaten[2]);
                    temp.put("A012", tempDaten[4]);
                    temp.put("A013", tempDaten[3]);
                    temp.put("A152", tempDaten[6]);
                    temp.put("A017", tempDaten[5]);

                    try {
                        temp.put("N242", tempDaten[8]);
                        temp.put("S687", tempDaten[9]);
                    } catch (Exception e) {
                        temp.put("N242", tempDaten[7]);
                        temp.put("S687", tempDaten[8]);
                    }

                } else if (AuftragsDaten.get(i).get(b).startsWith("NAD+FW")) {
                } else if (AuftragsDaten.get(i).get(b).startsWith("GID")) {
                } else if (AuftragsDaten.get(i).get(b).startsWith("FTX+AAA")) {
                    tempDaten = AuftragsDaten.get(i).get(b).split("\\+");
                    temp.put("A034", tempDaten[4]);
                } else if (AuftragsDaten.get(i).get(b).startsWith("FTX+SPH")) {
                    tempDaten = AuftragsDaten.get(i).get(b).split("\\+");
                    if (tempDaten.length > 2) {
                        temp.put("A039", tempDaten[4]);
                    }
                } else if (AuftragsDaten.get(i).get(b).startsWith("PCI")) {
                } else if (AuftragsDaten.get(i).get(b).startsWith("UNT")) {
                    tDaten.add(temp);
                }

            }

        }
        return tDaten;

    }

    private ArrayList<HashMap> iftminSIEDLEAUPO() {

        ArrayList<String> tempNADDaten = new ArrayList<String>(),
                tempNADDaten2 = new ArrayList<String>(),
                tempNADDaten3 = new ArrayList<String>(),
                tempNADDaten5 = new ArrayList<String>(),
                tempNADDaten4 = new ArrayList<String>();
        ArrayList<HashMap> tDaten = new ArrayList<HashMap>();
        ArrayList<HashMap> datenAUPO = new ArrayList<HashMap>();
        int anzahl = 0;
        HashMap t, tempDaten1;


        tDaten = new ArrayList<HashMap>();
        for (int i = 0; i < AuftragsDaten.size(); i++) {
            tempNADDaten = new ArrayList<String>();
            tempNADDaten3 = new ArrayList<String>();
            tempNADDaten2 = new ArrayList<String>();
            tempNADDaten4 = new ArrayList<String>();
            tempNADDaten5 = new ArrayList<String>();
            String[] tempDaten;
            for (int b = 0; b < AuftragsDaten.get(i).size(); b++) {
                if (AuftragsDaten.get(i).get(b).startsWith("UNB")) {
                } else if (AuftragsDaten.get(i).get(b).startsWith("UNH")) {
                } else if (AuftragsDaten.get(i).get(b).startsWith("BGM")) {
                } else if (AuftragsDaten.get(i).get(b).startsWith("DTM")) {
                } else if (AuftragsDaten.get(i).get(b).startsWith("TSR")) {
                } else if (AuftragsDaten.get(i).get(b).startsWith("CNT")) {
                } else if (AuftragsDaten.get(i).get(b).startsWith("TOD")) {
                } else if (AuftragsDaten.get(i).get(b).startsWith("RFF")) {
                } else if (AuftragsDaten.get(i).get(b).startsWith("DTM")) {
                } else if (AuftragsDaten.get(i).get(b).startsWith("NAD+CZ")) {
                } else if (AuftragsDaten.get(i).get(b).startsWith("NAD+CN")) {
                } else if (AuftragsDaten.get(i).get(b).startsWith("NAD+FW")) {
                } else if (AuftragsDaten.get(i).get(b).startsWith("MEA")) {
                    tempDaten = AuftragsDaten.get(i).get(b).split("\\+");
                    tempDaten = tempDaten[3].split(":");
                    tempNADDaten.add(tempDaten[1]);
                } else if (AuftragsDaten.get(i).get(b).startsWith("GID")) {
                    tempDaten = AuftragsDaten.get(i).get(b).split("\\+");
                    tempDaten = tempDaten[2].split(":");
                    tempNADDaten2.add(tempDaten[0]);
                    tempNADDaten3.add(tempDaten[1]);
                } else if (AuftragsDaten.get(i).get(b).startsWith("FTX+AAA")) {
                    tempDaten = AuftragsDaten.get(i).get(b).split("\\+");
                    tempNADDaten4.add(tempDaten[4]);
                } else if (AuftragsDaten.get(i).get(b).startsWith("FTX+SPH")) {
                } else if (AuftragsDaten.get(i).get(b).startsWith("PCI")) {
                    tempDaten = AuftragsDaten.get(i).get(b).split("\\+");
                    tempNADDaten5.add(tempDaten[2]);
                } else if (AuftragsDaten.get(i).get(b).startsWith("UNT")) {
                    tDaten = new ArrayList<HashMap>();
                    t = new HashMap();
                    String Z001 = "", S121 = "", A034 = "", A037 = "", G038 = "";
                    anzahl = 0;
                    for (int c = 0; c < tempNADDaten.size(); c++) {
                        anzahl++;
                        if (Z001.equals("")) {
                            Z001 = Z001 + tempNADDaten2.get(c);
                        } else {
                            Z001 = Z001 + ":" + tempNADDaten2.get(c);
                        }
                        if (S121.equals("")) {
                            S121 = S121 + tempNADDaten3.get(c);
                        } else {
                            S121 = S121 + ":" + tempNADDaten3.get(c);
                        }
                        if (A034.equals("")) {
                            A034 = A034 + tempNADDaten4.get(c);
                        } else {
                            A034 = A034 + ":" + tempNADDaten4.get(c);
                        }
                        if (A037.equals("")) {
                            A037 = A037 + tempNADDaten5.get(c);
                        } else {
                            A037 = A037 + ":" + tempNADDaten5.get(c);
                        }
                        if (G038.equals("")) {
                            G038 = G038 + tempNADDaten.get(c);
                        } else {
                            G038 = G038 + ":" + tempNADDaten.get(c);
                        }
                        tDaten.add(t);
                    }
                    tempDaten1 = new HashMap();
                    tempDaten1.put("Z001", Z001);
                    tempDaten1.put("S121", S121);
                    tempDaten1.put("A034", A034);
                    tempDaten1.put("A037", A037);
                    tempDaten1.put("G038", G038);
                    tempDaten1.put("anzahl", anzahl);
                    datenAUPO.add(tempDaten1);

                }

            }

        }
        return datenAUPO;
    }

    private HashMap iftminSIELDETrai(String start, int anzahl) {
        String[] daten = start.split("\\+");

        HashMap Daten = new HashMap();
        Daten.put("STRN", daten[5]);
        String[] tempDaten = daten[4].split(":");
        String ja = tempDaten[0].substring(0, 2);
        String mo = tempDaten[0].substring(2, 4);
        String tag = tempDaten[0].substring(4, 6);
        Daten.put("STDT", tag + mo + ja);
        Daten.put("STZT", tempDaten[1]);
        Daten.put("STDZ", anzahl);
        return Daten;
    }

    public void auslesenDerRuntalAuftraege(ArrayList<ArrayList<String>> K, ArrayList<ArrayList<String>> Z, String dateiName) {

        K = checkDatenAll(K);
        Z = checkDatenAll(Z);
        ArrayList<HashMap> AuftraegeKost = new ArrayList<HashMap>();
        ArrayList<HashMap> AuftraegeAUKO = new ArrayList<HashMap>();
        ArrayList<HashMap> AuftraegeAUPO = new ArrayList<HashMap>();
        ArrayList tempZehnderDaten = new ArrayList();
        int anzahlSegmenten = 0;
        HashMap Header = runtalHeader(dateiName, K.get(0));

        for (int i = 0; i < K.size(); i++) {
            anzahlSegmenten++;
            AuftraegeKost.add(runtalKOST(K.get(i)));
            anzahlSegmenten++;
            AuftraegeAUKO.add(runtalAUKO(K.get(i), Header.get("SHDT").toString()));
            anzahlSegmenten++;
            AuftraegeAUPO.add(runtalAUPO(K.get(i), Z.get(i)));
        }

        anzahlSegmenten++;
        anzahlSegmenten++;
        ZehnderDaten.clear();

        tempZehnderDaten.add(WeikundaConvertierenHEADER(Header) + "'");
        ZehnderDaten.add(tempZehnderDaten);
        int anz = 0;
        int e = 0;
        grafik.text2.append((new StringBuilder()).append(WeikundaConvertierenHEADER(Header)).append("'\n").toString());
        for (int f = 0; f < AuftraegeAUKO.size(); f++) {
            tempZehnderDaten = new ArrayList<String>();
            tempZehnderDaten.add(WeikundaConvertierenKOST(AuftraegeKost.get(f)) + "'");
            tempZehnderDaten.add(WeikundaConvertierenAUKO(AuftraegeAUKO.get(f)) + "'");
            tempZehnderDaten.add(WeikundaConvertierenAUPO(AuftraegeAUPO.get(f)) + "'");
            ZehnderDaten.add(tempZehnderDaten);
            grafik.text2.append("Auftrag " + (f + 1) + ": \n");
            anz++;
            grafik.text2.append(anz + ": " + tempZehnderDaten.get(e) + "\n");
            anz++;
            e++;
            grafik.text2.append(anz + ": " + tempZehnderDaten.get(e) + "\n");
            anz++;
            e++;
            grafik.text2.append(anz + ": " + tempZehnderDaten.get(e) + "\n");
            e = 0;
        }

        HashMap Trai = runtalTrai(dateiName, anzahlSegmenten, Header.get("SHDT").toString(), Header.get("SHZT").toString());
        tempZehnderDaten = new ArrayList();
        tempZehnderDaten.add(WeikundaConvertierenTRAI(runtalTrai(dateiName, anzahlSegmenten, Header.get("SHDT").toString(), Header.get("SHZT").toString())) + "'");
        // grs.text2.append((new StringBuilder()).append(WeikundaConvertierenTRAI(Trai)).append("'\n\n").toString());
        ZehnderDaten.add(tempZehnderDaten);

    }

    public void runtalDatenSchreiben(String dateiName2, String Ort) {
        try {
            String tempD[] = Ort.split("\\\\");
            String d = tempD[0];
            for (int a = 1; a < tempD.length; a++) {
                d = d + "/" + tempD[a];
            }

            FileWriter file = new FileWriter(new File(d + "/" + dateiName2 + ".txt"));
            BufferedWriter schreib = new BufferedWriter(file);
            for (int f = 0; f < ZehnderDaten.size(); f++) {
                for (int z = 0; z < ((ArrayList) ZehnderDaten.get(f)).size(); z++) {
                    schreib.write((String) ((ArrayList) ZehnderDaten.get(f)).get(z));
                }

            }

            schreib.close();
            date = new Date();
            grafik.hist.append(formatter.format(date) + ": Runtal (" + dateiName2 + ")erfolgreich eingespielt \n");
            ZehnderDaten.clear();
        } catch (IOException d) {
            grafik.hist.append(formatter.format(date) + ": Runtal FEHLER!!!! \n");
        }
    }

    private HashMap runtalHeader(String dateiName, ArrayList<String> Daten) {
        HashMap HEADER = new HashMap();
        HEADER.put("SHVK", "Runtal");
        HEADER.put("SHEK", "Freiburg");
        String DatumsAngabe[] = dateiName.split("_");
        String datum[] = Daten.get(11).split("");
        String wo = datum[1] + datum[2];
        String mo = datum[3] + datum[4];
        String ya = datum[5] + datum[6];
        HEADER.put("SHDT", (new StringBuilder()).append(ya).append(mo).append(wo).toString());
        datum = DatumsAngabe[2].split("");
        String h = (new StringBuilder()).append(datum[1]).append(datum[2]).toString();
        String m = (new StringBuilder()).append(datum[3]).append(datum[4]).toString();
        HEADER.put("SHZT", (new StringBuilder()).append(h).append(m).toString());
        HEADER.put("SHRN", dateiName);
        HEADER.put("SHZS", "07");
        HEADER.put("SHSN", "1");
        HEADER.put("SHVO", "Freiburg");
        HEADER.put("SHVA", "MISTRAL");
        HEADER.put("SHVT", "TAC211");
        return HEADER;
    }

    private HashMap runtalKOST(ArrayList<String> Daten) {
        HashMap temp = new HashMap();
        temp.put("N074", "01");
        temp.put("N080", "01");
        temp.put("N083", "78");
        temp.put("N045", Daten.get(Daten.size() - 1));
        temp.put("S421", "1");
        return temp;
    }

    private HashMap runtalAUKO(ArrayList<String> Daten, String datum) {
        HashMap temp = new HashMap();
        temp.put("S102", "0");
        temp.put("A012", Daten.get(0));
        temp.put("A013", Daten.get(1));
        temp.put("A019", Daten.get(2));
        temp = getAdresse(temp, Daten);
        temp.put("A017", Daten.get(3));
        temp.put("S093", NullenWeg(Daten.get(13)));
        //temp.put("N314", datum);
        temp.put("A039", Daten.get(6));
        temp.put("N009", Daten.get(5));
        if (temp.get("N242").toString().startsWith("230")) {
            temp.put("N048", "441");
        } else {
            temp.put("N048", NullenWeg(Daten.get(10)));
        }
        temp.put("D014", naechsterTag());
        return temp;
    }

    private HashMap runtalAUPO(ArrayList Daten, ArrayList Daten2) {
        return zehnderHauptsatz(Daten, Daten2);  //ab Anz 200 = 1 EW + Anzahl
    }

    private HashMap runtalTrai(String dateiName, int anzahlSegmenten, String Datum, String Zeit) {
        HashMap TRAI = new HashMap();
        TRAI.put("STDT", Datum);
        TRAI.put("STZT", Zeit);
        TRAI.put("STRN", dateiName);
        TRAI.put("STDZ", Integer.valueOf(anzahlSegmenten));
        return TRAI;
    }

    public void auslesenDerZehnderZDEAuftraege(ArrayList<ArrayList<String>> K, ArrayList<ArrayList<String>> Z, String dateiName) {

        K = checkDatenAll(K);
        Z = checkDatenAll(Z);
        ArrayList<HashMap> AuftraegeKost = new ArrayList<HashMap>();
        ArrayList<HashMap> AuftraegeAUKO = new ArrayList<HashMap>();
        ArrayList<HashMap> AuftraegeAUPO = new ArrayList<HashMap>();
        int anzahlSegmenten = 0;
        HashMap Header = zehnderZDEHeader(dateiName, K.get(0));
        for (int i = 0; i < K.size(); i++) {
            anzahlSegmenten++;
            AuftraegeKost.add(zehnderZDEKOST(K.get(i)));
            anzahlSegmenten++;
            AuftraegeAUKO.add(zehnderZDEAUKO((K.get(i)), Header.get("SHDT").toString()));
            anzahlSegmenten++;
            AuftraegeAUPO.add(zehnderZDEAUPO(K.get(i), Z.get(i)));
        }

        anzahlSegmenten++;
        anzahlSegmenten++;
        HashMap Trai = zehnderZDETrai(dateiName, anzahlSegmenten, Header.get("SHDT").toString(), Header.get("SHZT").toString());
        Header.put("anzahl", Integer.valueOf(anzahlSegmenten));
        ArrayList tempZehnderDaten = new ArrayList();
        tempZehnderDaten.add((new StringBuilder()).append(WeikundaConvertierenHEADER(Header)).append("'").toString());
        ZehnderDaten.add(tempZehnderDaten);
        int anz = 0;
        int e = 0;
        grafik.text2.append((new StringBuilder()).append(WeikundaConvertierenHEADER(Header)).append("'\n").toString());
        for (int f = 0; f < AuftraegeAUKO.size(); f++) {
            tempZehnderDaten = new ArrayList();
            tempZehnderDaten.add((new StringBuilder()).append(WeikundaConvertierenKOST(AuftraegeKost.get(f))).append("'").toString());
            tempZehnderDaten.add((new StringBuilder()).append(WeikundaConvertierenAUKO(AuftraegeAUKO.get(f))).append("'").toString());
            tempZehnderDaten.add((new StringBuilder()).append(WeikundaConvertierenAUPO(AuftraegeAUPO.get(f))).append("'").toString());
            ZehnderDaten.add(tempZehnderDaten);
            grafik.text2.append((new StringBuilder()).append("Auftrag ").append(f + 1).append(": \n").toString());
            anz++;
            grafik.text2.append((new StringBuilder()).append(anz).append(":").append((String) tempZehnderDaten.get(e)).append("\n").toString());
            anz++;
            e++;
            grafik.text2.append((new StringBuilder()).append(anz).append(":").append((String) tempZehnderDaten.get(e)).append("\n").toString());
            anz++;
            e++;
            grafik.text2.append((new StringBuilder()).append(anz).append(":").append((String) tempZehnderDaten.get(e)).append("\n").toString());
            e = 0;
        }

        tempZehnderDaten = new ArrayList();
        tempZehnderDaten.add((new StringBuilder()).append(WeikundaConvertierenTRAI(Trai)).append("'").toString());
        grafik.text2.append((new StringBuilder()).append(WeikundaConvertierenTRAI(Trai)).append("'\n\n").toString());
        ZehnderDaten.add(tempZehnderDaten);

    }

    public void zehnderZDEDatenSchreiben(String dateiName2, String Ort) {
        try {
            String tempD[] = Ort.split("\\\\");
            String d = tempD[0];
            for (int a = 1; a < tempD.length; a++) {
                d = (new StringBuilder()).append(d).append("/").append(tempD[a]).toString();
            }

            FileWriter file = new FileWriter(new File(d + "/" + dateiName2 + ".txt"));
            BufferedWriter schreib = new BufferedWriter(file);
            for (int f = 0; f < ZehnderDaten.size(); f++) {
                for (int z = 0; z < ((ArrayList) ZehnderDaten.get(f)).size(); z++) {
                    schreib.write((String) ((ArrayList) ZehnderDaten.get(f)).get(z));
                }

            }

            date = new Date();
            grafik.hist.append((new StringBuilder()).append(formatter.format(date)).append(": Zehnder ZDE (").append(dateiName2).append(") erfolgreich eingespielt \n").toString());
            schreib.close();
            ZehnderDaten.clear();
        } catch (IOException d) {
            grafik.hist.append(formatter.format(date) + ": Zehnder ZDE FEHLER!!!! \n");
        }
    }

    private HashMap zehnderZDEHeader(String dateiName, ArrayList Daten) {
        HashMap HEADER = new HashMap();
        HEADER.put("SHVK", "Zehnder");
        HEADER.put("SHEK", "Freiburg");
        String dateInof[] = dateiName.split("_");
        String datum[] = ((String) Daten.get(11)).split("");
        String wo = (new StringBuilder()).append(datum[1]).append(datum[2]).toString();
        String mo = (new StringBuilder()).append(datum[3]).append(datum[4]).toString();
        String ya = (new StringBuilder()).append(datum[5]).append(datum[6]).toString();
        HEADER.put("SHDT", (new StringBuilder()).append(ya).append(mo).append(wo).toString());
        datum = dateInof[2].split("");
        String h = (new StringBuilder()).append(datum[1]).append(datum[2]).toString();
        String m = (new StringBuilder()).append(datum[3]).append(datum[4]).toString();
        HEADER.put("SHZT", (new StringBuilder()).append(h).append(m).toString());
        HEADER.put("SHRN", dateiName);
        HEADER.put("SHZS", "07");
        HEADER.put("SHSN", "1");
        HEADER.put("SHVO", "1");
        HEADER.put("SHVA", "MISTRAL");
        HEADER.put("SHVT", "TAC211");
        return HEADER;
    }

    private HashMap zehnderZDEKOST(ArrayList<String> Daten) {
        HashMap temp = new HashMap();
        temp.put("N074", "01");
        temp.put("N080", "01");
        temp.put("N083", "78");
        temp.put("N045", Daten.get(20));
        temp.put("S421", "1");
        return temp;
    }

    private HashMap zehnderZDEAUKO(ArrayList<String> Daten, String datum) {
        HashMap temp = new HashMap();
        temp = getAdresse(temp, Daten);
        temp.put("A012", Daten.get(0));
        temp = setRelation(temp);
        temp = setRouting(temp, temp.get("A152").toString(), temp.get("A012").toString());
        temp.put("S102", "0");
        temp.put("A013", checkInhaltWert((String) Daten.get(1)));
        temp.put("A019", Daten.get(2));
        temp.put("A017", Daten.get(3));
        if (temp.get("S687").toString().startsWith("D")) {
            if (temp.get("N242").toString().startsWith("1010")) {
                temp.put("N048", "14");
            }

            temp.put("S093", "04");
        } else {
            temp.put("S093", "16");
        }
        //temp.put("N314", datum);
        temp.put("A039", Daten.get(6));
        try {
            //temp.put("N314", Daten.get(21));
        } catch (Exception e) {
        }
        try {
            temp.put("N791", Daten.get(22));
        } catch (Exception e) {
        }

        temp.put("N009", Daten.get(5));
        if (Daten.get(8).startsWith("85") || (Daten.get(8).startsWith("65"))) {
            temp.put("D014", naechsterTag());
        }

        temp.put("N058", Daten.get(7));
        temp.put("S686", "5");
        temp.put("S075", "1");
        return temp;
    }

    private HashMap zehnderZDEAUPO(ArrayList<String> Daten, ArrayList Daten2) {
        return zehnderHauptsatz(Daten, Daten2);
    }

    private HashMap zehnderZDETrai(String dateiName, int anzahlSegmenten, String Datum, String Zeit) {
        HashMap TRAI = new HashMap();
        TRAI.put("STDT", Datum);
        TRAI.put("STZT", Zeit);
        TRAI.put("STRN", dateiName);
        TRAI.put("STDZ", Integer.valueOf(anzahlSegmenten));
        return TRAI;
    }

    public void auslesenDerZehnderZBNAuftraege(ArrayList K, ArrayList Z, String dateiName) {

        K = checkDatenAll(K);
        Z = checkDatenAll(Z);
        ArrayList AuftraegeKost = new ArrayList();
        ArrayList AuftraegeAUKO = new ArrayList();
        ArrayList AuftraegeAUPO = new ArrayList();
        int anzahlSegmenten = 0;
        HashMap Header = zehnderZBNHeader(dateiName, (ArrayList) K.get(0));
        for (int i = 0; i < K.size(); i++) {
            anzahlSegmenten++;
            AuftraegeKost.add(zehnderZBNKOST((ArrayList) K.get(i)));
            anzahlSegmenten++;
            AuftraegeAUKO.add(zehnderZBNAUKO((ArrayList) K.get(i), Header.get("SHDT").toString()));
            anzahlSegmenten++;
            AuftraegeAUPO.add(zehnderZBNAUPO((ArrayList) K.get(i), (ArrayList) Z.get(i)));
        }

        anzahlSegmenten++;
        anzahlSegmenten++;
        HashMap Trai = zehnderZBNTrai(dateiName, anzahlSegmenten, Header.get("SHDT").toString(), Header.get("SHZT").toString());
        Header.put("anzahl", Integer.valueOf(anzahlSegmenten));
        ArrayList tempZehnderDaten = new ArrayList();
        tempZehnderDaten.add((new StringBuilder()).append(WeikundaConvertierenHEADER(Header)).append("'").toString());
        ZehnderDaten.add(tempZehnderDaten);
        int anz = 0;
        int e = 0;
        grafik.text2.append((new StringBuilder()).append(WeikundaConvertierenHEADER(Header)).append("'\n").toString());
        for (int f = 0; f < AuftraegeAUKO.size(); f++) {
            tempZehnderDaten = new ArrayList();
            tempZehnderDaten.add((new StringBuilder()).append(WeikundaConvertierenKOST((HashMap) AuftraegeKost.get(f))).append("'").toString());
            tempZehnderDaten.add((new StringBuilder()).append(WeikundaConvertierenAUKO((HashMap) AuftraegeAUKO.get(f))).append("'").toString());
            tempZehnderDaten.add((new StringBuilder()).append(WeikundaConvertierenAUPO((HashMap) AuftraegeAUPO.get(f))).append("'").toString());
            ZehnderDaten.add(tempZehnderDaten);
            grafik.text2.append((new StringBuilder()).append("Auftrag ").append(f + 1).append(": \n").toString());
            anz++;
            grafik.text2.append((new StringBuilder()).append(anz).append(":").append((String) tempZehnderDaten.get(e)).append("\n").toString());
            anz++;
            e++;
            grafik.text2.append((new StringBuilder()).append(anz).append(":").append((String) tempZehnderDaten.get(e)).append("\n").toString());
            anz++;
            e++;
            grafik.text2.append((new StringBuilder()).append(anz).append(":").append((String) tempZehnderDaten.get(e)).append("\n").toString());
            e = 0;
        }

        tempZehnderDaten = new ArrayList();
        tempZehnderDaten.add((new StringBuilder()).append(WeikundaConvertierenTRAI(Trai)).append("'").toString());
        grafik.text2.append((new StringBuilder()).append(WeikundaConvertierenTRAI(Trai)).append("'\n\n").toString());
        ZehnderDaten.add(tempZehnderDaten);
    }

    public void zehnderZBNDatenSchreiben(String dateiName2, String Ort) {
        try {
            String tempD[] = Ort.split("\\\\");
            String d = tempD[0];
            for (int a = 1; a < tempD.length; a++) {
                d = (new StringBuilder()).append(d).append("/").append(tempD[a]).toString();
            }

            FileWriter file = new FileWriter(new File((new StringBuilder()).append(d).append("/").append(dateiName2).append(".txt").toString()));
            BufferedWriter schreib = new BufferedWriter(file);
            for (int f = 0; f < ZehnderDaten.size(); f++) {
                for (int z = 0; z < ((ArrayList) ZehnderDaten.get(f)).size(); z++) {
                    schreib.write((String) ((ArrayList) ZehnderDaten.get(f)).get(z));
                }

            }

            date = new Date();
            grafik.hist.append((new StringBuilder()).append(formatter.format(date)).append(": Zehnder (ZBN) erfolgreich eingespielt \n").toString());
            schreib.close();
            ZehnderDaten.clear();
        } catch (IOException d) {
            grafik.hist.append(formatter.format(date) + ": Zehnder ZBN FEHLER!!!! \n");
        }
    }

    private HashMap zehnderZBNHeader(String dateiName, ArrayList Daten) {
        HashMap HEADER = new HashMap();
        HEADER.put("SHVK", "Zehnder");
        HEADER.put("SHEK", "Freiburg");
        String dateInof[] = dateiName.split("_");
        String datum[] = ((String) Daten.get(11)).split("");
        String wo = (new StringBuilder()).append(datum[1]).append(datum[2]).toString();
        String mo = (new StringBuilder()).append(datum[3]).append(datum[4]).toString();
        String ya = (new StringBuilder()).append(datum[5]).append(datum[6]).toString();
        HEADER.put("SHDT", (new StringBuilder()).append(ya).append(mo).append(wo).toString());
        datum = dateInof[2].split("");
        String h = (new StringBuilder()).append(datum[1]).append(datum[2]).toString();
        String m = (new StringBuilder()).append(datum[3]).append(datum[4]).toString();
        HEADER.put("SHZT", (new StringBuilder()).append(h).append(m).toString());
        HEADER.put("SHRN", dateiName);
        HEADER.put("SHZS", "07");
        HEADER.put("SHSN", "1");
        HEADER.put("SHVO", "1");
        HEADER.put("SHVA", "MISTRAL");
        HEADER.put("SHVT", "TAC211");
        return HEADER;
    }

    private HashMap zehnderZBNKOST(ArrayList Daten) {
        HashMap temp = new HashMap();
        temp.put("N074", "01");
        temp.put("N080", "01");
        temp.put("N083", "78");
        String adresseTemp[] = ((String) Daten.get(4)).split("-");
        if (adresseTemp.length > 1) {
            if (adresseTemp[0].length() < 3) {
                temp.put("N045", Daten.get(Daten.size() - 1));
            } else {
                temp.put("N045", "342020");
            }
        } else {
            temp.put("N045", "342020");
        }
        temp.put("S421", "1");
        return temp;
    }

    private HashMap zehnderZBNAUKO(ArrayList Daten, String datum) {
        HashMap temp = new HashMap();
        temp.put("S686", "5");
        temp.put("S075", "1");
        temp.put("S102", "0");
        temp.put("A012", Daten.get(0));
        temp.put("A013", checkInhaltWert((String) Daten.get(1)));
        temp.put("A019", Daten.get(2));
        temp = getAdresse(temp, Daten);
        temp.put("A017", Daten.get(3));
        if (temp.get("S687").toString().startsWith("D")) {
            temp.put("S093", "04");
        } else {
            temp.put("S093", "16");
        }
        //temp.put("N314", datum);
        temp = setRelation(temp);
        temp.put("A039", Daten.get(6));
        temp.put("N009", Daten.get(5));
        temp.put("N058", Daten.get(7));
        return temp;
    }

    private HashMap zehnderZBNAUPO(ArrayList Daten, ArrayList Daten2) {
        return zehnderHauptsatz(Daten, Daten2);
    }

    private HashMap zehnderZBNTrai(String dateiName, int anzahlSegmenten, String Datum, String Zeit) {
        HashMap TRAI = new HashMap();
        TRAI.put("STDT", Datum);
        TRAI.put("STZT", Zeit);
        TRAI.put("STRN", dateiName);
        TRAI.put("STDZ", Integer.valueOf(anzahlSegmenten));
        return TRAI;
    }

    private HashMap zehnderHauptsatz(ArrayList Daten, ArrayList Daten2) {
        HashMap temp = new HashMap();
        String tDaten = "1";
        if (!((String) Daten.get(15)).equals("0000")) {
            if (((String) Daten.get(20)).equalsIgnoreCase("538909")) {
                if (Integer.parseInt((String) Daten.get(15)) > 4) {
                    temp.put("S121", "EW");
                    temp.put("A034", (new StringBuilder()).append((String) Daten.get(15)).append(" W-K.").toString());
                    temp.put("Z001", "1");
                } else {
                    temp.put("S121", "CO");
                    temp.put("A034", "Waerme-K.");
                    if (NullenWeg((String) Daten.get(15)).equals("")) {
                        tDaten = "1";
                    } else {
                        tDaten = NullenWeg((String) Daten.get(15));
                    }
                    temp.put("Z001", tDaten);
                }
                temp.put("A037", Daten.get(5));
                Double gew = Double.valueOf(Double.parseDouble(((String) Daten.get(14)).substring(0, ((String) Daten.get(14)).length() - 1)) / 100D);
                temp.put("G038", gew);
                temp = zehnderZ(temp, Daten, Daten2, 1);
            } else if (((String) Daten2.get(11)).equalsIgnoreCase("OFFEN")) {
                temp.put("S121", "CO");
                temp.put("A034", "Waerme-K.");
                if (NullenWeg((String) Daten.get(15)).equals("")) {
                    tDaten = "1";
                } else {
                    tDaten = NullenWeg((String) Daten.get(15));
                }
                temp.put("Z001", tDaten);
                temp.put("A037", Daten.get(5));
                Double gew = Double.valueOf(Double.parseDouble(((String) Daten.get(14)).substring(0, ((String) Daten.get(14)).length() - 1)) / 100D);
                temp.put("G038", gew);
                temp = zehnderZ(temp, Daten, Daten2, 1);
            } else {
                temp.put("S121", VerladeEinheit.get(Daten2.get(11)));
                temp.put("A034", (new StringBuilder()).append((String) Daten.get(15)).append(" W-K.").toString());
                if (NullenWeg((String) Daten2.get(21)).equals("")) {
                    tDaten = "1";
                } else {
                    tDaten = NullenWeg((String) Daten2.get(21));
                }
                temp.put("Z001", tDaten);
                temp.put("A037", Daten.get(5));
                Double gew = Double.valueOf(Double.parseDouble(((String) Daten.get(14)).substring(0, ((String) Daten.get(14)).length() - 1)) / 100D);
                temp.put("G038", gew);
                temp = zehnderZ(temp, Daten, Daten2, 1);
            }
        } else {
            Boolean weiter = Boolean.valueOf(false);
            for (int i = 0; i < 4; i++) {
                if (!NullenWeg((String) Daten2.get(i)).equals("")) {
                    weiter = Boolean.valueOf(true);
                }
                if (!NullenWeg((String) Daten2.get(i + 6)).equals("")) {
                    weiter = Boolean.valueOf(true);
                }
                if (!NullenWeg((String) Daten2.get(i + 12)).equals("")) {
                    weiter = Boolean.valueOf(true);
                }
                if (!NullenWeg((String) Daten2.get(i + 22)).equals("")) {
                    weiter = Boolean.valueOf(true);
                }
                if (!NullenWeg((String) Daten2.get(i + 27)).equals("")) {
                    weiter = Boolean.valueOf(true);
                }
            }

            if (weiter.booleanValue()) {
                temp.put("A037", Daten.get(5));
                temp = zehnderZ(temp, Daten, Daten2, 0);
            } else {
                temp.put("A037", Daten.get(5));
                temp.put("A034", "Waerme-K.");
                temp.put("Z001", "1");
                temp.put("S121", "CO");
                temp.put("G038", "0.0");
                temp.put("anzahl", "1");
            }
        }
        return temp;
    }

    private HashMap zehnderZ(HashMap temp, ArrayList Daten, ArrayList Daten2, int mHaupt) {
        int anzahl = 1;
        int anz = 0;
        String inhalt = "";
        String Verpackung[] = {
            "KT", "PA", "SA", "BU"
        };
        for (int c = 0; c < Verpackung.length; c++) {
            int DatenWert = Integer.parseInt((String) Daten2.get(c));
            Double DatenGew;
            try {
                DatenGew = Double.valueOf(Double.parseDouble(NullenWeg(((String) Daten2.get(6 + c)).toString())));
                DatenGew = Double.valueOf(DatenGew.doubleValue() / 1000D);
            } catch (Exception f) {
                DatenGew = Double.valueOf(0.0D);
            }
            if (DatenGew.doubleValue() <= 0.0D && DatenWert <= 0) {
                continue;
            }
            if (((String) Daten2.get(c)).equals("0000")) {
                anz = 1;
            } else {
                if (((String) Daten2.get(c)).equals("00000")) {
                    anz = 1;
                } else {
                    anz = Integer.parseInt(NullenWeg((String) Daten2.get(c)));
                }
            }
            inhalt = "Zubehoer";
            if (mHaupt == 1) {
                anzahl++;
                temp = zehnderZuebehoer(temp, VerladeEinheit, Verpackung[c], DatenGew, anz, inhalt, mHaupt);
            } else {
                temp = zehnderZuebehoer(temp, VerladeEinheit, Verpackung[c], DatenGew, anz, inhalt, mHaupt);
                mHaupt = 1;
            }
        }

        for (int i = 0; i < 4; i++) {
            if (((String) Daten2.get(i + 12)).equals("")) {
                continue;
            }
            inhalt = (new StringBuilder()).append((String) Daten2.get(i + 27)).append(" W-K.").toString();
            if (NullenWeg((String) Daten2.get(i + 22)).equals("")) {
                anz = 1;
            } else {
                anz = Integer.parseInt(NullenWeg((String) Daten2.get(i + 22)));
            }
            if (mHaupt == 1) {
                anzahl++;
                temp = zehnderZuebehoer(temp, VerladeEinheit, ((String) Daten2.get(i + 12)).toString(), Double.valueOf(0.0D), anz, inhalt, mHaupt);
            } else {
                temp = zehnderZuebehoer(temp, VerladeEinheit, ((String) Daten2.get(i + 12)).toString(), Double.valueOf(0.0D), anz, inhalt, mHaupt);
                mHaupt = 1;
            }
        }

        temp.put("anzahl", Integer.valueOf(anzahl));
        return temp;
    }

    private HashMap zehnderZuebehoer(HashMap temp, HashMap VerpackungArt, String Art, Double Gewicht, int Anzahl, String Inhalt, int mHaupt) {
        if (VerpackungArt.containsKey(Art)) {
            if (mHaupt == 1) {
                String tDaten = (new StringBuilder()).append(temp.get("A034")).append(":").append(Inhalt).toString();
                temp.put("A034", tDaten);
                if (temp.containsKey("Z001")) {
                    tDaten = (new StringBuilder()).append(temp.get("Z001")).append(":").append(Anzahl).toString();
                } else {
                    tDaten = "1";
                }
                temp.put("Z001", tDaten);
                tDaten = (new StringBuilder()).append(temp.get("S121")).append(":").append(VerpackungArt.get(Art)).toString();
                temp.put("S121", tDaten);
                tDaten = (new StringBuilder()).append(temp.get("G038")).append(":").append(Gewicht).toString();
                temp.put("G038", tDaten);
            } else {
                temp.put("A034", Inhalt);
                temp.put("Z001", Integer.valueOf(Anzahl));
                temp.put("S121", VerpackungArt.get(Art));
                temp.put("G038", Gewicht);
            }
        }
        return temp;
    }

    private HashMap getAdresse(HashMap temp, ArrayList<String> Daten) {
        String tempDaten = "";
        if (Daten.get(19) != null && ((String) Daten.get(19)).equals("J")) {
            temp.put("K960", "J");
        }
        String adresseTemp[] = ((String) Daten.get(4)).split("-");
        if (adresseTemp.length > 1) {
            if (adresseTemp[0].length() < 3) {
                temp.put("S687", adresseTemp[0]);
                for (int i = 2; i < adresseTemp.length; i++) {
                    adresseTemp[1] = (new StringBuilder()).append(adresseTemp[1]).append("-").append(adresseTemp[i]).toString();
                }

                adresseTemp = adresseTemp[1].split(" ");
                temp.put("N242", adresseTemp[0]);
                for (int c = 1; c < adresseTemp.length; c++) {
                    tempDaten = (new StringBuilder()).append(tempDaten).append(adresseTemp[c]).append(" ").toString();
                }

            } else {
                temp.put("S687", "D");
                for (int i = 1; i < adresseTemp.length; i++) {
                    adresseTemp[0] = (new StringBuilder()).append(adresseTemp[0]).append("-").append(adresseTemp[i]).toString();
                }

                adresseTemp = adresseTemp[0].split(" ");
                temp.put("N242", adresseTemp[0]);
                for (int c = 1; c < adresseTemp.length; c++) {
                    tempDaten = (new StringBuilder()).append(tempDaten).append(adresseTemp[c]).append(" ").toString();
                }

            }
        } else {
            temp.put("S687", "D");
            adresseTemp = adresseTemp[0].split(" ");
            temp.put("N242", adresseTemp[0]);
            for (int c = 1; c < adresseTemp.length; c++) {
                tempDaten = (new StringBuilder()).append(tempDaten).append(adresseTemp[c]).append(" ").toString();
            }

        }
        temp.put("A152", tempDaten);
        tempDaten = "";
        return temp;
    }

    public String NullenWeg(String Wert) {
        String dTemp[] = Wert.split("");
        String wert = "";
        for (int i = 1; i < dTemp.length; i++) {
            if (!dTemp[i].equals("0")) {
                for (int a = i; a < dTemp.length; a++) {
                    wert = (new StringBuilder()).append(wert).append(dTemp[a]).toString();
                }

                return wert;
            }
        }

        return wert;
    }

    private String checkDatenSegmente(String Daten) {
        String temp[] = Daten.split("'");
        String dTemp = temp[0];
        if (temp.length > 1) {
            for (int a = 1; a < temp.length; a++) {
                dTemp = (new StringBuilder()).append(dTemp).append("?'").append(temp[a]).toString();
            }

        }
        temp = dTemp.split(":");
        dTemp = temp[0];
        if (temp.length > 1) {
            for (int a = 1; a < temp.length; a++) {
                dTemp = (new StringBuilder()).append(dTemp).append("?:").append(temp[a]).toString();
            }

        }
        temp = dTemp.split("\\+");
        dTemp = temp[0];
        if (temp.length > 1) {
            for (int a = 1; a < temp.length; a++) {
                dTemp = (new StringBuilder()).append(dTemp).append("?+").append(temp[a]).toString();
            }

        }
        return dTemp;
    }

    private String checkInhaltWert(String Daten) {
        if (Daten != null) {
            return Daten;
        } else if (Daten.equals("")) {
           return " ";
        } else{
            return Daten = " ";
        }
    }

    private ArrayList<ArrayList<String>> checkDatenAll(ArrayList<ArrayList<String>> Daten) {
        ArrayList DatenTemp = new ArrayList();
        ArrayList Temp = new ArrayList();
        for (int i = 0; i < Daten.size(); i++) {
            for (int a = 0; a < (Daten.get(i)).size(); a++) {
                Temp.add(checkDatenSegmente(Daten.get(i).get(a)));
            }

            DatenTemp.add(Temp);
            Temp = new ArrayList();
        }

        return DatenTemp;
    }

    private String WeikundaConvertierenHEADER(HashMap Daten) {
        String Header = "HEAD";
        String vergleichMUSS[] = {
            "SHVK", "SHEK", "SHDT", "SHZT", "SHRN", "SHZS", "SHSN", "SHVO", "SHVA", "SHVT"
        };
        String vergleichKANN[] = {
            "SHWK", "SHRK", "SHWK", "SHRK"
        };
        Header = (new StringBuilder()).append(Header).append(CheckDatenMUSS(vergleichMUSS, Daten)).toString();
        Header = (new StringBuilder()).append(Header).append(CheckDatenKANN(vergleichKANN, Daten)).toString();
        return Header;
    }

    private String WeikundaConvertierenKOST(HashMap Daten) {
        String kost = "KOST";
        if (Daten.containsKey("S684") && Daten.get("S684").equals("DE")) {
            Daten.put("S684", "d");
        }
        if (Daten.containsKey("N045")) {
            if (!Daten.get("N045").equals("")) {
                String vergleichMUSS[][] = {
                    {
                        "N074", "N080", "N083", "N045"
                    }, new String[0]
                };
                String vergleichKANN[][] = {
                    {
                        "A151", "A022", "A021", "A023", "S684"
                    }, {
                        "N090", "S009", "S055", "S421", "S105", "A027", "N241"
                    }
                };
                for (int i = 0; i < 2; i++) {
                    kost = (new StringBuilder()).append(kost).append(CheckDatenMUSS(vergleichMUSS[i], Daten)).toString();
                    kost = (new StringBuilder()).append(kost).append(CheckDatenKANN(vergleichKANN[i], Daten)).toString();
                }

            } else {
                String vergleichMUSS[][] = {
                    {
                        "N074", "N080", "N083", "N045"
                    }, {
                        "A027", "N241"
                    }
                };
                String vergleichKANN[][] = {
                    {
                        "A151", "A022", "A021", "A023", "S684"
                    }, {
                        "N090", "S009", "S055", "S421", "S105"
                    }
                };
                for (int i = 0; i < 2; i++) {
                    kost = (new StringBuilder()).append(kost).append(CheckDatenMUSS(vergleichMUSS[i], Daten)).toString();
                    kost = (new StringBuilder()).append(kost).append(CheckDatenKANN(vergleichKANN[i], Daten)).toString();
                }

            }
        } else {
            String vergleichMUSS[][] = {
                {
                    "N074", "N080", "N083", "N045"
                }, {
                    "A027", "N241"
                }
            };
            String vergleichKANN[][] = {
                {
                    "A151", "A022", "A021", "A023", "S684"
                }, {
                    "N090", "S009", "S055", "S421", "S105"
                }
            };
            for (int i = 0; i < 2; i++) {
                kost = (new StringBuilder()).append(kost).append(CheckDatenMUSS(vergleichMUSS[i], Daten)).toString();
                kost = (new StringBuilder()).append(kost).append(CheckDatenKANN(vergleichKANN[i], Daten)).toString();
            }

        }
        return kost;
    }

    private String WeikundaConvertierenAUKO(HashMap tDaten) {
        if (tDaten.get("S687").equals("DE")) {
            tDaten.put("S687", "d");
        }
        String auko = "AUKO";
        Boolean weiter = Boolean.valueOf(false);
        String vergleichKANN[] = {
            "S686", "A018", "N066", "N065", "K989", "N009", "N314"
        };
        String vergleichKANNevt[] = {
            "N072", "A012", "A019", "A013", "A152", "A017", "N242", "S687", "A014", "N064",
            "N058", "S093"
        };
        String vergleichMUSS1[] = {
            "A012"
        };
        String vergleichMUSS2[] = {
            "A152", "A017", "N242", "S093"
        };
        String vergleichKANN1[] = {
            "N072"
        };
        String vergleichKANN2[] = {
            "A019", "A013"
        };
        String vergleichKANN3[] = {
            "S687", "A014", "N064", "N058"
        };
        String vergleichKANN4[] = {
            "K964", "A039", "A035", "A036", "N791", "N792", "S050", "D142", "N014", "B037",
            "B011", "S075", "B043", "N048", "S204", "A040", "L163", "K960", "M001", "M002",
            "M003", "K004", "D014"
        };
        auko = (new StringBuilder()).append(auko).append(CheckDatenKANN(vergleichKANN, tDaten)).toString();

        if (tDaten.get("N072") != null) {
            if (!tDaten.get("N072").equals("")) {
                auko = (new StringBuilder()).append(auko).append(CheckDatenKANN(vergleichKANNevt, tDaten)).toString();
            } else {
                weiter = Boolean.valueOf(true);
            }
        } else {
            weiter = Boolean.valueOf(true);
        }
        if (weiter.booleanValue()) {
            auko = (new StringBuilder()).append(auko).append(CheckDatenKANN(vergleichKANN1, tDaten)).toString();
            auko = (new StringBuilder()).append(auko).append(CheckDatenMUSS(vergleichMUSS1, tDaten)).toString();
            auko = (new StringBuilder()).append(auko).append(CheckDatenKANN(vergleichKANN2, tDaten)).toString();
            auko = (new StringBuilder()).append(auko).append(CheckDatenMUSS(vergleichMUSS2, tDaten)).toString();
            auko = (new StringBuilder()).append(auko).append(CheckDatenKANN(vergleichKANN3, tDaten)).toString();
        }
        auko = (new StringBuilder()).append(auko).append(CheckDatenKANN(vergleichKANN4, tDaten)).toString();
        return auko;
    }

    private String WeikundaConvertierenAUPO(HashMap Daten) {
        String aupo = (new StringBuilder()).append("AUPO:").append(Daten.get("anzahl")).toString();
        String vergleichMUSS[] = {
            "Z001", "S121", "A034"
        };
        String vergleichKANN[] = {
            "A037"
        };
        String vergleichMUSS1[] = {
            "G038"
        };
        String vergleichKANN1[] = {
            "G039"
        };
        String gewichtT[] = Daten.get("G038").toString().split(":");
        String gewicht = "";
        if (gewichtT.length > 1) {
            for (int i = 0; i < gewichtT.length; i++) {
                gewicht = (new StringBuilder()).append(gewicht).append(":").append(String.valueOf(Math.ceil(Double.parseDouble(gewichtT[i])))).toString();
            }

            gewicht = gewicht.substring(1, gewicht.length());
        } else {
            gewicht = String.valueOf(Math.ceil(Double.parseDouble(Daten.get("G038").toString())));
        }
        Daten.put("G038", gewicht);
        aupo = (new StringBuilder()).append(aupo).append(CheckDatenMUSS(vergleichMUSS, Daten)).toString();
        aupo = (new StringBuilder()).append(aupo).append(CheckDatenKANN(vergleichKANN, Daten)).toString();
        aupo = (new StringBuilder()).append(aupo).append(CheckDatenMUSS(vergleichMUSS1, Daten)).toString();
        aupo = (new StringBuilder()).append(aupo).append(CheckDatenKANN(vergleichKANN1, Daten)).toString();
        return aupo;
    }

    private String WeikundaConvertierenTRAI(HashMap Daten) {
        String Trail = "TRAI";
        String vergleichMUSS[] = {
            "STRN", "STDT", "STZT", "STDZ"
        };
        Trail = (new StringBuilder()).append(Trail).append(CheckDatenMUSS(vergleichMUSS, Daten)).toString();
        return Trail;
    }

    private String naechsterTag() {
        Date datum = new Date();
        String da = "";
        SimpleDateFormat yahr = new SimpleDateFormat("yyyy");
        SimpleDateFormat monat1 = new SimpleDateFormat("MM");
        SimpleDateFormat tag1 = new SimpleDateFormat("dd");
        Calendar calle = new GregorianCalendar(Integer.parseInt(yahr.format(datum)), Integer.parseInt(monat1.format(datum)), Integer.parseInt(tag1.format(datum)));
        if (datum.getDay() == 5) {
            calle.add(Calendar.DAY_OF_MONTH, 3);
        } else {
            calle.add(Calendar.DAY_OF_MONTH, 1);
        }
        int year = calle.get(Calendar.YEAR);
        int monat = calle.get(Calendar.MONTH);
        int tag = calle.get(Calendar.DAY_OF_MONTH);
        if (String.valueOf(monat).length() > 1) {
            da = String.valueOf(year) + String.valueOf(monat) + String.valueOf(tag);
            return da;
        } else {
            da = String.valueOf(year) + "0" + String.valueOf(monat) + String.valueOf(tag);
            return da;
        }
    }

    private HashMap setRouting(HashMap temp, String Ort, String Kunde) {
        String[] KName = Kunde.split(" ");
        String[] OName = Ort.split(" ");
        String[] tempName;
        ArrayList<ArrayList<String>> KdnName = addRoutingList();
        for (int i = 0; i < KdnName.size(); i++) {
            for (int a = 0; a < KName.length; a++) {
                for (int o = 0; o < OName.length; o++) {
                    tempName = OName[o].split("-");
                    for (int c = 0; c < tempName.length; c++) {
                        if (KName[a].length() > 2 && tempName[c].length() > 2) {
                            if ((KdnName.get(i).get(0).startsWith(KName[a])) && (KdnName.get(i).get(1).startsWith(tempName[c]))) {
                                temp.put("N048", KdnName.get(i).get(2));
                                temp.put("N072", KdnName.get(i).get(3));
                            }
                        }
                    }
                }
            }
        }
        return temp;
    }

    private HashMap setRelation(HashMap temp) {
        try {
            BufferedReader daten = new BufferedReader(new FileReader(new File("Routing.csv")));
            try {
                String RoutingD = daten.readLine();
                while ((RoutingD = daten.readLine()) != null) {
                    String[] dTemp = RoutingD.split(";");
                    if (temp.get("S687").equals(dTemp[0])) {
                        if ((Integer.parseInt(dTemp[1]) <= Integer.parseInt(temp.get("N242").toString())) && (Integer.parseInt(dTemp[2]) >= Integer.parseInt(temp.get("N242").toString()))) {
                            temp.put("N048", dTemp[3]);
                        }
                    }
                }
            } catch (IOException ex) {
            }

        } catch (FileNotFoundException ex) {
        }
        return temp;
    }

    private ArrayList<ArrayList<String>> addRoutingList() {
        ArrayList<ArrayList<String>> KdnName = new ArrayList<ArrayList<String>>();
        ArrayList<String> KdnDaten = new ArrayList<String>();
        KdnDaten.add("MAINMETALL");
        KdnDaten.add("BUERGERSTADT");
        KdnDaten.add("731");
        KdnDaten.add("141802");
        KdnName.add(KdnDaten);
        KdnDaten = new ArrayList<String>();
        KdnDaten.add("RICHTER+FRENZEL");
        KdnDaten.add("KERPEN");
        KdnDaten.add("732");
        KdnDaten.add("507169");
        KdnName.add(KdnDaten);
        KdnDaten = new ArrayList<String>();
        KdnDaten.add("BERGMANN + FRANZ");
        KdnDaten.add("BLUMENBERG");
        KdnDaten.add("733");
        KdnDaten.add("174242");
        KdnName.add(KdnDaten);
        KdnDaten = new ArrayList<String>();
        KdnDaten.add("SCHMIDT");
        KdnDaten.add("MOENCHENGLADBACH");
        KdnDaten.add("734");
        KdnDaten.add("191244");
        KdnName.add(KdnDaten);
        KdnDaten = new ArrayList<String>();
        KdnDaten.add("KORSING");
        KdnDaten.add("KOELN");
        KdnDaten.add("746");
        KdnDaten.add("471987");
        KdnName.add(KdnDaten);
        KdnDaten = new ArrayList<String>();
        KdnDaten.add("PFEIFFER+MAY");
        KdnDaten.add("KARLSRUHE");
        KdnDaten.add("752");
        KdnDaten.add("585153");
        KdnName.add(KdnDaten);
        KdnDaten = new ArrayList<String>();
        KdnDaten.add("WIEDERMANN");
        KdnDaten.add("SARSTEDT");
        KdnDaten.add("735");
        KdnDaten.add("157149");
        KdnName.add(KdnDaten);
        KdnDaten = new ArrayList<String>();
        KdnDaten.add("WIEDERMANN");
        KdnDaten.add("BURG");
        KdnDaten.add("748");
        KdnDaten.add("100392");
        KdnName.add(KdnDaten);
        KdnDaten = new ArrayList<String>();
        KdnDaten.add("LINSS");
        KdnDaten.add("SCHWABHAUSEN");
        KdnDaten.add("738");
        KdnDaten.add("486381");
        KdnName.add(KdnDaten);
        KdnDaten = new ArrayList<String>();
        KdnDaten.add("RICHTER + FRENZEL");
        KdnDaten.add("GIESSEN");
        KdnDaten.add("744");
        KdnDaten.add("850834");
        KdnName.add(KdnDaten);
        KdnDaten = new ArrayList<String>();
        KdnDaten.add("WESCO");
        KdnDaten.add("WENDEN");
        KdnDaten.add("739");
        KdnDaten.add("576063");
        KdnName.add(KdnDaten);
        KdnDaten = new ArrayList<String>();
        KdnDaten.add("JENSEN");
        KdnDaten.add("HAMBURG");
        KdnDaten.add("740");
        KdnDaten.add("363893");
        KdnName.add(KdnDaten);
        KdnDaten = new ArrayList<String>();
        KdnDaten.add("RICHTER + FRENZEL");
        KdnDaten.add("BUETTELBORN");
        KdnDaten.add("741");
        KdnDaten.add("471947");
        KdnName.add(KdnDaten);
        KdnDaten = new ArrayList<String>();
        KdnDaten.add("RICHTER + FRENZEL");
        KdnDaten.add("ASCHAFFENBURG");
        KdnDaten.add("749");
        KdnDaten.add("282608");
        KdnName.add(KdnDaten);
        KdnDaten = new ArrayList<String>();
        KdnDaten.add("KOENIG");
        KdnDaten.add("KOBLENZ");
        KdnDaten.add("750");
        KdnDaten.add("799429");
        KdnName.add(KdnDaten);
        KdnDaten = new ArrayList<String>();
        KdnDaten.add("LINSS");
        KdnDaten.add("MALSFELD");
        KdnDaten.add("742");
        KdnDaten.add("887364");
        KdnName.add(KdnDaten);
        KdnDaten = new ArrayList<String>();
        KdnDaten.add("LINSS");
        KdnDaten.add("KASSEL");
        KdnDaten.add("743");
        KdnDaten.add("101712");
        KdnName.add(KdnDaten);
        KdnDaten = new ArrayList<String>();
        KdnDaten.add("KORSING");
        KdnDaten.add("KOELN");
        KdnDaten.add("746");
        KdnDaten.add("471987");
        KdnName.add(KdnDaten);
        KdnDaten = new ArrayList<String>();
        KdnDaten.add("GITTFRIED");
        KdnDaten.add("KARLSFELD");
        KdnDaten.add("747");
        KdnDaten.add("478933");
        KdnName.add(KdnDaten);
        KdnDaten = new ArrayList<String>();
        KdnDaten.add("R+F");
        KdnDaten.add("WUERZBURG");
        KdnDaten.add("751");
        KdnDaten.add("136101");
        KdnName.add(KdnDaten);
        KdnDaten = new ArrayList<String>();
        KdnDaten.add("RICHTER + FRENZEL");
        KdnDaten.add("MAINZ");
        KdnDaten.add("745");
        KdnDaten.add("507158");
        KdnName.add(KdnDaten);
        KdnDaten = new ArrayList<String>();
        KdnDaten.add("PIETSCH");
        KdnDaten.add("AHAUS");
        KdnDaten.add("736");
        KdnDaten.add("527929");
        KdnName.add(KdnDaten);
        return KdnName;
    }

    private String CheckDatenKANN(String vergleich[], HashMap tDaten) {
        String daten = "";
        Object options[] = {
            "Yes, please", "No, thanks"
        };
        int n = 0;
        for (int d = 0; d < vergleich.length; d++) {
            if (tDaten.get(vergleich[d]) == null) {
                continue;
            }
            if (!tDaten.get(vergleich[d]).equals("")) {
                daten = (new StringBuilder()).append(daten).append("+").append(vergleich[d]).append(tDaten.get(vergleich[d])).toString();
            } else {
                daten = (new StringBuilder()).append(daten).append("+").append(vergleich[d]).append(tDaten.get(vergleich[d])).toString();
            }
        }

        return daten;
    }

    private String CheckDatenMUSS(String[] vergleich, HashMap tDaten) {
        String daten = "";
        Object[] options = {
            "Yes, please", "No, thanks"
        };
        int n = 0;
        for (int d = 0; d < vergleich.length; d++) {
            if (tDaten.get(vergleich[d]) != null) {
                daten = (new StringBuilder()).append(daten).append("+").append(vergleich[d]).append(tDaten.get(vergleich[d])).toString();
            } else {
                
            }
        }
        return daten;
    }
    private ArrayList<ArrayList> GardnerWeikunderDaten = new ArrayList<ArrayList>();
    private ArrayList<String> eMail = new ArrayList<String>();
    private ArrayList<String> eMailAll = new ArrayList<String>();
    private String[] EULaender ={"BE","IT","RO","BG","LV","SE","DK","LT","SK","DE","LU","SI","EE","MT","ES","FI","NL","CZ","FR","AT","HU","GR","PL","GB","IE","PT","CY"};

    private void gardnerDenverDaten() {
        ArrayList<String> TDaten = new ArrayList<String>();
        eMail = new ArrayList<String>();
        eMailAll = new ArrayList<String>();
        ArrayList<ArrayList<String>> ADaten = new ArrayList<ArrayList<String>>();
        ArrayList<ArrayList<ArrayList<ArrayList<String>>>> AllDaten = new ArrayList<ArrayList<ArrayList<ArrayList<String>>>>();

        String[] pDaten;
        for (int i = 0; i < Daten.size(); i++) {
            pDaten = Daten.get(i).split(";");
            if (pDaten[15].equalsIgnoreCase("01") &&
                pDaten[15].equalsIgnoreCase("A")  ||
                pDaten[15].equalsIgnoreCase("1") ||
                pDaten[15].equalsIgnoreCase("01") &&
                !pDaten[7].equalsIgnoreCase("900") &&
                !pDaten[12].equalsIgnoreCase("CN") && 
                !pDaten[12].equalsIgnoreCase("US") && 
                !pDaten[12].equalsIgnoreCase("NZ") &&
                !pDaten[12].equalsIgnoreCase("JP") &&
                !pDaten[9].equalsIgnoreCase("Enter drop ship address here")) {
                grafik.text.append(Daten.get(i) + "\n");
                TDaten = new ArrayList<String>();
                TDaten.addAll(Arrays.asList(pDaten));
                ADaten.add(TDaten);
            } else {
                if (pDaten[9].equalsIgnoreCase("Enter drop ship address here")) {
                    grafik.text.append(Daten.get(i) + "\n");
                    eMail.add(Daten.get(i));
                }
            }
            eMailAll.add(Daten.get(i));
        }

        ArrayList<ArrayList<ArrayList<String>>> DatenGardner = sortieren(ADaten, 0);
        for (int i = 0; i < DatenGardner.size(); i++) {
            AllDaten.add(sortieren(DatenGardner.get(i), 22));
        }
        gardnerDenfer = AllDaten;
    }

    private ArrayList<ArrayList<ArrayList<String>>> sortieren(ArrayList<ArrayList<String>> ADaten, int spalte) {
        ArrayList<ArrayList<String>> ATempDaten = new ArrayList<ArrayList<String>>();
        ArrayList<ArrayList<ArrayList<String>>> AllDaten = new ArrayList<ArrayList<ArrayList<String>>>();

        String info = "";
        for (int a = 0; a < ADaten.size(); a++) {
            info = ADaten.get(a).get(spalte);
            ATempDaten.add((ArrayList<String>) ADaten.get(a).clone());
            ADaten.remove(0);
            for (int i = 0; i < ADaten.size(); i++) {
                if (spalte == 22) {
                    info = ADaten.get(i).get(spalte);
                    if (info.equals("")) {
                        ATempDaten.add((ArrayList<String>) ADaten.get(i).clone());
                        ADaten.remove(i);
                        i--;
                    } else {
                        break;
                    }
                } else {
                    if (info.equals(ADaten.get(i).get(spalte))) {
                        ATempDaten.add((ArrayList<String>) ADaten.get(i).clone());
                        ADaten.remove(i);
                        i--;
                    }
                }
            }
            AllDaten.add(ATempDaten);
            ATempDaten = new ArrayList<ArrayList<String>>();
            a = -1;
        }
        return AllDaten;
    }

    private void auslesenDerGardnerDenverDaten(String dateiName) {
        Date datumA = new Date();
        SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyy");
        SimpleDateFormat formatUhr = new SimpleDateFormat("HH:MM");





        ArrayList gardnerKost = new ArrayList();
        ArrayList gardnerAUKO = new ArrayList();
        ArrayList gardnerAUPO = new ArrayList();
        int anzahlSegmenten = 0;
        HashMap Header = gardnerHEADER(dateiName, format.format(datumA), formatUhr.format(datumA));
        for (int i = 0; i < gardnerDenfer.size(); i++) {
            anzahlSegmenten++;
            gardnerKost.add(gardnerKost());
            anzahlSegmenten++;
            gardnerAUKO.add(gardnerAUKO(gardnerDenfer.get(i).get(0).get(0), gardnerDenfer.get(i)));
            anzahlSegmenten++;
            gardnerAUPO.add(GardnerAUPO(gardnerDenfer.get(i)));

        }
        anzahlSegmenten++;
        anzahlSegmenten++;


        HashMap Trai = gardnerTRAIL(dateiName, format.format(datumA), formatUhr.format(datumA), anzahlSegmenten);
        Header.put("anzahl", Integer.valueOf(anzahlSegmenten));

        ArrayList GardnerDatenTemp = new ArrayList();
        GardnerDatenTemp.add((new StringBuilder()).append(WeikundaConvertierenHEADER(Header)).append("'").toString());
        GardnerWeikunderDaten.add(GardnerDatenTemp);
        int anz = 0;
        int e = 0;
        grafik.text2.append((new StringBuilder()).append(WeikundaConvertierenHEADER(Header)).append("'\n").toString());
        for (int f = 0; f < gardnerAUKO.size(); f++) {
            GardnerDatenTemp = new ArrayList();
            GardnerDatenTemp.add((new StringBuilder()).append(WeikundaConvertierenKOST((HashMap) gardnerKost.get(f))).append("'").toString());
            GardnerDatenTemp.add((new StringBuilder()).append(WeikundaConvertierenAUKO((HashMap) gardnerAUKO.get(f))).append("'").toString());
            GardnerDatenTemp.add((new StringBuilder()).append(WeikundaConvertierenAUPO((HashMap) gardnerAUPO.get(f))).append("'").toString());
            GardnerWeikunderDaten.add(GardnerDatenTemp);

            grafik.text2.append((new StringBuilder()).append("Auftrag ").append(f + 1).append(": \n").toString());
            anz++;
            grafik.text2.append((new StringBuilder()).append(anz).append(":").append((String) WeikundaConvertierenKOST((HashMap) gardnerKost.get(f))).append("\n").toString());
            anz++;
            e++;
            grafik.text2.append((new StringBuilder()).append(anz).append(":").append((String) WeikundaConvertierenAUKO((HashMap) gardnerAUKO.get(f))).append("\n").toString());
            anz++;
            e++;
            grafik.text2.append((new StringBuilder()).append(anz).append(":").append((String) WeikundaConvertierenAUPO((HashMap) gardnerAUPO.get(f))).append("\n").toString());
            e = 0;
        }

        GardnerDatenTemp = new ArrayList();
        GardnerDatenTemp.add(WeikundaConvertierenTRAI(Trai) + "'");
        grafik.text2.append((new StringBuilder()).append(WeikundaConvertierenTRAI(Trai)).append("'\n\n").toString());
        GardnerWeikunderDaten.add(GardnerDatenTemp);



    }

    public void GardnerDenverDatenSchreiben(String dateiName2, String Ort) {
        try {
            String tempD[] = Ort.split("\\\\");
            String d = tempD[0];
            for (int a = 1; a < tempD.length; a++) {
                d = (new StringBuilder()).append(d).append("/").append(tempD[a]).toString();
            }

            FileWriter file = new FileWriter(new File((new StringBuilder()).append(d).append("/").append(dateiName2).append(".txt").toString()));
            BufferedWriter schreib = new BufferedWriter(file);
            for (int f = 0; f < GardnerWeikunderDaten.size(); f++) {
                for (int z = 0; z < ((ArrayList) GardnerWeikunderDaten.get(f)).size(); z++) {
                    schreib.write((String) ((ArrayList) GardnerWeikunderDaten.get(f)).get(z));
                }

            }

            try {
                File datei1 = new File("Gardner_EnterDropShipAddressHere.csv");
                BufferedWriter write = new BufferedWriter(new FileWriter(datei1));
                for (int i = 0; i < eMail.size(); i++) {
                    write.write(eMail.get(i) + "\n");
                }
                write.close();

                

                
            } catch (Exception ex) {
                System.out.print(ex);
            }
            date = new Date();
            grafik.hist.append((new StringBuilder()).append(formatter.format(date)).append(": Gardner Denver erfolgreich eingespielt \n").toString());
            schreib.close();
            ZehnderDaten.clear();
        } catch (IOException d) {
            grafik.hist.append(formatter.format(date) + ": GardnerWeikunderDaten FEHLER!!!! \n");
        }
    }

    private HashMap gardnerHEADER(String SHRN, String datum, String Uhrzeit) {
        HashMap HEADER = new HashMap();
        HEADER.put("SHVK", "Gardner Denver");
        HEADER.put("SHEK", "Freiburg");
        HEADER.put("SHDT", datum);
        HEADER.put("SHZT", Uhrzeit);
        HEADER.put("SHRN", SHRN);
        HEADER.put("SHZS", "07");
        HEADER.put("SHSN", "1");
        HEADER.put("SHVO", "1");
        HEADER.put("SHVA", "MISTRAL");
        HEADER.put("SHVT", "TAC211");
        return HEADER;
    }

    private HashMap gardnerKost() {
        HashMap temp = new HashMap();
        temp.put("N074", "01");
        temp.put("N080", "01");
        temp.put("N083", "78");
        temp.put("N045", "317499");
        temp.put("S421", "1");
        return temp;
    }

    private HashMap gardnerAUKO(ArrayList<String> Daten, ArrayList<ArrayList<ArrayList<String>>> Daten2) {
        HashMap temp = new HashMap();
        temp.put("S686", "5");
        temp.put("S075", "1");
        temp.put("S102", "0");
        temp.put("B011", "0");
        temp.put("A012", Daten.get(9));
        temp.put("A013", checkInhaltWert((String) Daten.get(10)));
        temp.put("A017", Daten.get(11));
        temp.put("N242", Daten.get(13));
        temp.put("S687", Daten.get(12));
        temp.put("A152", Daten.get(14));
        float gesamt = Math.round(Integer.parseInt(Daten2.get(0).get(0).get(23).split(",")[0]) * Integer.parseInt(Daten2.get(0).get(0).get(25).split(",")[0]) * Integer.parseInt(Daten2.get(0).get(0).get(27).split(",")[0]) / 1000);
        float gesamtQ = gesamt / 1000F;
        temp.put("M002", gesamtQ);

        if (Daten.get(17).equals("EXW")) {
            if (temp.get("S687").toString().equals("DE")) {
                temp.put("S093", "06");
            } else {
                temp.put("S093", "11");
            }
        } else if (Daten.get(17).equals("CPT")) {
            if (temp.get("S687").toString().equals("DE")) {
                temp.put("S093", "04");
            } else {
                temp.put("S093", "16");
            }
        }
        return temp;
    }

    private HashMap GardnerAUPO(ArrayList<ArrayList<ArrayList<String>>> Daten) {
        HashMap infoAUPO = new HashMap();
        String S121 = "", Z001 = "", A034 = "", A037 = "", G038 = "";
        infoAUPO.put("anzahl", Daten.size() + 1);
        Z001 = "1";
        S121 = Daten.get(0).get(0).get(29);
        A034 = "Pumpen";
        A037 = Daten.get(0).get(0).get(0);
        String[] daten = Daten.get(0).get(0).get(18).split(",");
        String gewicht = daten[0];
        for (int i = 1; i < daten.length; i++) {
            gewicht = gewicht + "." + daten[i];
        }
        G038 = gewicht;
        for (int i = 1; i < Daten.size(); i++) {
            Z001 = Z001 + ":1";
            S121 = S121 + ":" + Daten.get(i).get(0).get(29);
            A034 = A034 + ":Pumpen";
            A037 = A037 + ":" + Daten.get(i).get(0).get(0);

            daten = Daten.get(0).get(0).get(18).split(",");
            gewicht = daten[0];
            for (int a = 1; a < daten.length; a++) {
                gewicht = gewicht + "." + daten[a];
            }
            G038 = G038 + ":" + gewicht;
        }
        A037 = A037 + ":" + Daten.get(0).get(0).get(23).split(",")[0] + "*" + Daten.get(0).get(0).get(25).split(",")[0] + "*" + Daten.get(0).get(0).get(27).split(",")[0];
        infoAUPO.put("Z001", Z001);
        infoAUPO.put("S121", S121);
        infoAUPO.put("A034", A034);
        infoAUPO.put("A037", A037);
        infoAUPO.put("G038", G038);

        return infoAUPO;
    }

    private HashMap gardnerTRAIL(String STRN, String Uhrzeit, String Datum, int Anzahl) {
        HashMap TRAI = new HashMap();
        TRAI.put("STDT", Datum);
        TRAI.put("STZT", Uhrzeit);
        TRAI.put("STRN", STRN);
        TRAI.put("STDZ", Anzahl);
        return TRAI;
    }

}
