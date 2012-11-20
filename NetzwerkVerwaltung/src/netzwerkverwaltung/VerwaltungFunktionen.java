/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package netzwerkverwaltung;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

/**
 *
 * @author manfred.fischer
 */
public class VerwaltungFunktionen {
    
    private ClientNetzwerk clients;
    private HauptBildschirm haupt;
    private HashMap SSAction = new HashMap();

    public VerwaltungFunktionen(HauptBildschirm h, ClientNetzwerk c){
        haupt = h;
        clients = c;
    }
    protected void getArbeitsplatz() throws IOException {
        try {
            Process p1;
            p1 = Runtime.getRuntime().exec("C:/WINDOWS/system32/cmd.exe /C start explorer.exe");
        } catch (IOException ex) {
        }
    }

    protected void getBrowser(String HTMLSeite) {
        try {
            Process p1;
            p1 = Runtime.getRuntime().exec("C:/WINDOWS/system32/cmd.exe /C \"C:\\Program Files\\Internet Explorer\\iexplore.exe\" " + HTMLSeite);
        } catch (Exception ex) {
        }
    }

    private ArrayList<String> getSchnellstartInfo(){
      ArrayList<String> SSTemp = new ArrayList<String>();
        try {
            BufferedReader read = new BufferedReader(new FileReader("Config/ss.cfg"));
            String Zeile = read.readLine();
            while (Zeile != null) {
                SSTemp.add(Zeile);
                Zeile = read.readLine();
            }
            read.close();
        } catch (Exception e) {
        }
        return SSTemp;
    }
    
    protected void delSchnellstart(String Button){
      ArrayList<String> SSTemp = getSchnellstartInfo(); 
        try {
            BufferedWriter write = new BufferedWriter(new FileWriter("Config/ss.cfg"));
            for (int i = 0; i < SSTemp.size(); i++) {
              if (!SSTemp.get(i).split(";")[0].equals(Button))
               write.write(SSTemp.get(i)+"\n");
            }
            write.close();
        } catch (Exception e) {
        }
        setSchnellstart();
    }
    protected void addSchnellstart(String Button) {
        String name = JOptionPane.showInputDialog(new JFrame(),"Bitte geben Sie den Name des Programms an");
        
        ArrayList<String> SSTemp = getSchnellstartInfo();
        try {
            BufferedWriter write = new BufferedWriter(new FileWriter("Config/ss.cfg"));
            JFileChooser datei = new JFileChooser();
            int getInfo = datei.showOpenDialog(datei);
            if (getInfo == JFileChooser.APPROVE_OPTION) {
                for (int i = 0; i < SSTemp.size(); i++) {
                    write.write(SSTemp.get(i)+"\n");
                }
                write.write(Button+";"+name+";"+datei.getSelectedFile().getAbsolutePath()+"\n");
            }
            write.close();
        } catch (Exception e) {
        }
        setSchnellstart();
    }
    
    protected void setSchnellstart(){
      SSAction = new HashMap();
      ArrayList<String> SSDaten = getSchnellstartInfo();
      int anz=0;
      for(int i=0;i<SSDaten.size();i++){
        SSAction.put(SSDaten.get(i).split(";")[0], SSDaten.get(i).split(";")[2]);
        haupt.ssButton.get(i).setText(SSDaten.get(i).split(";")[1]);  
        haupt.ssButton.get(i).setVisible(true); 
        anz++;
      }
      if (anz <= haupt.ssButton.size()){
          haupt.ssButton.get(anz).setVisible(true);
      }
    }
    
    protected void startSchnellstart(String BName){
        try {
            Process p1;
            p1 = Runtime.getRuntime().exec("C:/WINDOWS/system32/cmd.exe /C " + SSAction.get(BName));
        } catch (Exception ex) {
        }
    }


}
