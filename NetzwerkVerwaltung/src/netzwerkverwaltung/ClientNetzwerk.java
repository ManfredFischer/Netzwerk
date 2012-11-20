/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package netzwerkverwaltung;

/**
 *
 * @author manfred.fischer
 */
import java.io.*;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.channels.FileChannel;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.net.ssl.HandshakeCompletedListener;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocket;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class ClientNetzwerk {

    private BufferedReader reader;
    private SSLSocket dataSocket;
    private PrintWriter writer;
    private String[] empfang;
    public VerwaltungDatenverarbeitung netzwerkVerwaltung;
    private ArrayList<String> send;
    private BufferedWriter write;
    private ZipFile zip = new ZipFile();
    private sicherheit sicher = new sicherheit();
    public boolean Dateigeholt = false;
    protected String trennzeichen = ";";
    String sIP, sPort, cIP;
    public int Wert = 0;

    public ClientNetzwerk(initServer in, HauptBildschirm h) {
        netzwerkVerwaltung = new VerwaltungDatenverarbeitung(this, in, h);
    }
    private wait waiting;
    private Thread waitTreaThread;

    public void startWait() {
        waitTreaThread = new Thread(new Runnable() {

            @Override
            public void run() {
                waiting = new wait();
                waiting.setVisible(true);
            }
        });
        waitTreaThread.start();
    }

    public void stopWait() {
        waiting.setVisible(false);
        waitTreaThread.interrupt();
    }

    ;

    public String checkDaten(Object Check) {
        try {
            if (Check != null) {
                if (Check.equals("")) {
                    Check = "---";
                }
                byte[] plainTextBytes = Check.toString().getBytes("ISO-8859-1");
                String tm = "";
                String[] temp = Check.toString().split("");
                for (int i = 0; i < temp.length; i++) {
                    if (temp[i].equals(";")) {
                        tm = tm + "-?-";
                    } else {
                        tm = tm + temp[i];
                    }
                }
                return tm;
            } else {
                return "";
            }
        } catch (Exception e) {
            if (Check != null) {
                if (Check.equals("")) {
                    Check = " ";
                }
                String tm = "";
                String[] temp = Check.toString().split("");
                for (int i = 0; i < temp.length; i++) {
                    if (temp[i].equals(";")) {
                        tm = tm + "-?-";
                    } else {
                        tm = tm + temp[i];
                    }
                }
                return tm;
            } else {
                return "";
            }
        }
    }

    protected String verschluesseln(String File, String Daten) {
        return sicher.verschluesseln(File, Daten);
    }

    protected String entschluesseln(String File) {
        return sicher.entschluesseln(File);
    }

    public void FehlerBerichtSchreiben(String Daten, String FileName, HauptBildschirm haupt) {

        try {
            SimpleDateFormat datum = new SimpleDateFormat("yyyy.MM.dd 'at' HH:mm:ss ");
            haupt.fehlerProtokoll.append(datum.format(new Date())+" - "+Daten +"\n");
            ArrayList<String> oldD = new ArrayList<String>();
            try {
                FileReader fileD = new FileReader(new File(FileName));
                BufferedReader w = new BufferedReader(fileD);
                String te = w.readLine();

                while (te != null) {
                    oldD.add(te);
                    te = w.readLine();
                }
                w.close();
            } catch (Exception e) {
            }
            

            if (oldD.size() <= 0 || oldD == null) {
                write.write(datum.format(new Date()) + " +> " + Daten.toString() + "\n");
            } else {
                for (int i = 0; i < oldD.size(); i++) {
                    write.write(oldD.get(i) + "\n");
                }
                write.write(datum.format(new Date()) + " +> " + Daten.toString() + "\n");
            }
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(new JFrame(), ex);
        }
    }

    private String[] reset(String[] daten) {
        try {
            String[] info = new String[daten.length];
            String datenI = "";
            for (int i = 0; i < daten.length; i++) {
                String[] temp = daten[i].split("\\-\\?\\-");
                if (temp.length > 1) {
                    datenI = temp[0];
                    for (int a = 1; a < temp.length; a++) {
                        datenI = datenI + trennzeichen + temp[a];
                    }
                    info[i] = datenI;
                } else {
                    info[i] = temp[0];
                }
            }
            return info;
        } catch (Exception e) {
            return daten;
        }
    }

    private void ClienEmpfang() {
        try {
            String SDaten;
            while ((SDaten = reader.readLine()) != null) {
                if (!SDaten.isEmpty()) {
                    empfang = SDaten.split(";");
                    empfang = reset(empfang);
                    netzwerkVerwaltung.DatenEmpfang(empfang);
                }
            }
        } catch (IOException e) {
            FehlerBerichtSchreiben("ClientEmpfang: " + e, "Log/error.log",null);
            JOptionPane.showMessageDialog(new JFrame(), "Server Verbindung verloren!");
            System.exit(0);

        }
    }

    public void copyFile(File in, File out) {
        try {
            FileChannel inChannel = new FileInputStream(in).getChannel();
            FileChannel outChannel = new FileOutputStream(out).getChannel();

            inChannel.transferTo(0, inChannel.size(), outChannel);
            inChannel.close();
            outChannel.close();
        } catch (Exception e) {
        }
    }

    public void send(String daten) {
        send = new ArrayList<String>();
        send.add(daten);
        ClientVersand(send);
    }

    public HashMap netzInfo(String IP) {
        try {
            HashMap info = new HashMap();
            
            String cmd = "cmd /c ping " + IP;
            Process p = Runtime.getRuntime().exec(cmd);

            InputStream is = p.getInputStream();
            BufferedReader b = new BufferedReader(new InputStreamReader(is));
            String line = null;
            int anz = 0;
            while ((line = b.readLine()) != null) {
                anz++;
                if (anz == 3) {
                    if (!line.split(" ")[4].equalsIgnoreCase("nicht")) {
                        info.put("Name", InetAddress.getByName(IP).getHostName());
                        info.put("IP", IP);
                        info.put("Dauer", line.split(" ")[4]);
                        info.put("Status", "online");
                        
                    } else {
                        info.put("Name", "???");
                        info.put("IP", IP);
                        info.put("Dauer", "---");
                        info.put("Status", "offline");
                    }
                    break;
                }
            }
            return info;
        } catch (Exception ex) {
            HashMap info = new HashMap();
            info.put("Name", "???");
            info.put("IP", IP);
            info.put("Dauer", "---");
            info.put("Status", "offline");
            return info;
        }
    }

    public void ClientVersand(ArrayList<String> Daten) {
        try {

            for (int i = 0; i < Daten.size(); i++) {
                writer.println(Daten.get(i));
            }
            writer.flush();
            Daten.clear();
        } catch (Exception e) {
            FehlerBerichtSchreiben("ClientVersand: " + e, "Log/error.log",null);
            System.exit(0);
        }
    }

    public void ClientVerbindung(String IP, String Port) throws IOException {
        write = new BufferedWriter(new FileWriter(new File("Log/error.log")));
        dataSocket = new SSLSocket(IP, Integer.parseInt(Port)) {

            @Override
            public String[] getSupportedCipherSuites() {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            @Override
            public String[] getEnabledCipherSuites() {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            @Override
            public void setEnabledCipherSuites(String[] strings) {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            @Override
            public String[] getSupportedProtocols() {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            @Override
            public String[] getEnabledProtocols() {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            @Override
            public void setEnabledProtocols(String[] strings) {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            @Override
            public SSLSession getSession() {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            @Override
            public void addHandshakeCompletedListener(HandshakeCompletedListener hl) {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            @Override
            public void removeHandshakeCompletedListener(HandshakeCompletedListener hl) {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            @Override
            public void startHandshake() throws IOException {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            @Override
            public void setUseClientMode(boolean bln) {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            @Override
            public boolean getUseClientMode() {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            @Override
            public void setNeedClientAuth(boolean bln) {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            @Override
            public boolean getNeedClientAuth() {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            @Override
            public void setWantClientAuth(boolean bln) {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            @Override
            public boolean getWantClientAuth() {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            @Override
            public void setEnableSessionCreation(boolean bln) {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            @Override
            public boolean getEnableSessionCreation() {
                throw new UnsupportedOperationException("Not supported yet.");
            }
        };//192.168.200.20
        sIP = IP;
        sPort = Port;
        cIP = dataSocket.getLocalAddress().toString();

        writer = new PrintWriter(dataSocket.getOutputStream());
        reader = new BufferedReader(new InputStreamReader(dataSocket.getInputStream()));
        Thread n = new Thread(new EingehendenStrom());
        n.start();
    }

    private class EingehendenStrom implements Runnable {

        @Override
        public void run() {
            ClienEmpfang();
        }
    }
    public DateiVerwaltung dateiV = new DateiVerwaltung();

    public boolean sendDateiSoftwareEMail(ArrayList<File> dateien, String SoftwareName, String pfad, String ordner) {
        if (!dateien.isEmpty()) {
            File zipDatei;
            zip.setZipFile(dateien, "Daten/Temp/" + ordner + "/" + SoftwareName + ".zip");
            zipDatei = new File("Daten/Temp/" + ordner + "/" + SoftwareName + ".zip");
            int Port = dateiV.Port();
            send("99998" + trennzeichen + Port + trennzeichen + pfad + trennzeichen + SoftwareName + ".zip");
            dateiV.dateiVersand(zipDatei, sIP, Port);
            dateiV = new DateiVerwaltung();
            for (int i = 0; i < dateien.size(); i++) {
                dateien.get(i).deleteOnExit();
            }
            zipDatei.deleteOnExit();
            new File("Daten/Temp/" + ordner).deleteOnExit();
        }
        return false;
    }

    public String sendDateiStoerung(ArrayList<File> dateien, int UID, int IDStoerung, String pfad) {
        ArrayList<File> Dateien = new ArrayList<File>();
        String Name;
        File newDatei, zipDatei;
        for (int i = 0; i < dateien.size(); i++) {
            Name = IDStoerung + "-" + i + "-" + dateien.get(i).getName();
            newDatei = new File(Name);

            copyFile(dateien.get(i), newDatei);

            Dateien.add(newDatei);
        }
        int Port = dateiV.Port();
        send("99998" + trennzeichen + Port + trennzeichen + pfad + trennzeichen + IDStoerung + "-" + UID + ".zip");
        zip.setZipFile(Dateien, IDStoerung + "-" + UID + ".zip");
        zipDatei = new File(IDStoerung + "-" + UID + ".zip");
        dateiV.dateiVersand(zipDatei, sIP, Port);
        zipDatei.delete();
        for (int i = 0; i < Dateien.size(); i++) {
            Dateien.get(i).delete();
        }
        dateiV = new DateiVerwaltung();
        return "//"+sIP+"/"+pfad+"/"+IDStoerung + "-" + UID+"/";
    }
}
