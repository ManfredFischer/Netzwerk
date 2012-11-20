/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package netzwerkverwaltung;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.HashMap;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;

public class searchAdressbuch {

    private String[] titles = new String[]{"ID", "Firma", "Ort"},
            titles2 = new String[]{"ID", "Name", "Vorname", "Abteilung", "Tel", "Fas", "Hinweis", "IDA"};
    private JTable tabFirma, tabPerson;
    private JFrame dial;
    private HauptBildschirm haupt;
    private JTextField search, searchPerson;
    private TabelFont tableFirma = new TabelFont(), tablePerson = new TabelFont();
    private ArrayList<String> daten;
    public int IDAdressbuchPerson, IDAdressbuchFirma;
    private Boolean sucheAuswahl = true;
    private ClientNetzwerk clients;
    protected int IDPerson;
    protected ArrayList<ArrayList<String>> infoAdresse = new ArrayList<ArrayList<String>>();
    protected ArrayList<ArrayList<String>> infoFirma = new ArrayList<ArrayList<String>>();
    protected ArrayList<ArrayList<String>> infoPerson = new ArrayList<ArrayList<String>>();
    private newStoerung stoerung;

    private void iniServer(ClientNetzwerk cl) {
        clients = cl;


        dial = new JFrame("Adressbuch");
        JPanel panel = new JPanel();
        panel.setLayout(null);
        panel.setBackground(new Color(149, 179, 200));
        panel.setMinimumSize(new Dimension(1015, 540));
        tabFirma = tableFirma.getTable(titles, -1, null, -1, null);
        tabPerson = tablePerson.getTable(titles2, -1, null, -1, null);
        JButton t = new JButton("OK");
        t.setBounds(920, 5, 60, 21);
        t.addActionListener(new ok());
        JLabel firma = new JLabel("Firma: ");
        firma.setBounds(5, 5, 60, 21);
        search = new JTextField();
        search.setFont(new Font("Arial", 0, 10));
        search.setBounds(65, 5, 90, 21);
        search.addActionListener(new searchFirmaInfo());

        JLabel person = new JLabel("Person: ");
        person.setBounds(350, 5, 60, 21);
        searchPerson = new JTextField();
        searchPerson.setBounds(415, 5, 90, 21);
        searchPerson.setFont(new Font("Arial", 0, 10));
        searchPerson.addActionListener(new searchPersonInfo());

        JScrollPane scFirma = new JScrollPane();
        scFirma.add(tabFirma);
        scFirma.setViewportView(tabFirma);
        scFirma.setBounds(5, 30, 340, 465);

        JScrollPane scPerson = new JScrollPane();
        scPerson.add(tabPerson);
        scPerson.setViewportView(tabPerson);
        scPerson.setBounds(350, 30, 648, 465);

        tabFirma.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    infoPerson = (ArrayList<ArrayList<String>>) clients.netzwerkVerwaltung.getAdressbuchPerson("-2", tabFirma.getValueAt(tabFirma.getSelectedRow(), 0).toString(), "searchAdressbuch/initServer",false).clone();
                    setTableInfoPerson(infoPerson);

                }
            }
        });

        tabPerson.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    infoFirma = (ArrayList<ArrayList<String>>) clients.netzwerkVerwaltung.getAdressbuchFirma(tabPerson.getValueAt(tabPerson.getSelectedRow(), 7).toString(), "searchAdressbuch/initServer",false).clone();
                    setTableInfoFirma(infoFirma);
                }
            }
        });


        panel.add(person);
        panel.add(searchPerson);
        panel.add(firma);
        panel.add(search);
        panel.add(t);
        panel.add(scFirma);
        panel.add(scPerson);
        dial.add(panel);
        dial.setMinimumSize(new Dimension(1015, 540));
        dial.setLocationRelativeTo(null);

    }

    public searchAdressbuch(ClientNetzwerk cl, HauptBildschirm h, newStoerung st) {
        iniServer(cl);
        haupt = h;
        stoerung = st;
    }

    public ArrayList<HashMap> searchUser(String userName, String userVName) {
        try {
            ArrayList<ArrayList<String>> pTemp = (ArrayList<ArrayList<String>>) clients.netzwerkVerwaltung.getAdressbuchPerson("-1", "-1", "searchAdressbuch/SearchUser",false).clone();
            ArrayList<ArrayList<String>> datenUser = new ArrayList<ArrayList<String>>();
            for (int i = 0; i < pTemp.size(); i++) {
                if (userName != null) {
                    if (pTemp.get(i).get(1).toString().toLowerCase().startsWith(userName.toLowerCase())) {
                        if (userVName != null) {
                            if (pTemp.get(i).get(2).toString().toLowerCase().startsWith(userVName.toLowerCase())) {
                                datenUser.add(pTemp.get(i));
                            }
                        } else {
                            datenUser.add(pTemp.get(i));
                        }
                    }
                } else {
                    if (pTemp.get(i).get(2).toString().toLowerCase().startsWith(userVName.toLowerCase())) {
                        datenUser.add(pTemp.get(i));
                    }
                }
            }
            return (ArrayList<HashMap>) datenUser.clone();
        } catch (Exception e) {
            return new ArrayList<HashMap>();
        }
    }

    public class ok implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            if (tabPerson.getRowCount() > 0 && (tabPerson.getSelectedRow() != -1)) {
                IDPerson = Integer.parseInt(tabPerson.getValueAt(tabPerson.getSelectedRow(), 0).toString());
                infoFirma = (ArrayList<ArrayList<String>>) clients.netzwerkVerwaltung.getAdressbuchFirma(tabPerson.getValueAt(tabPerson.getSelectedRow(), 7).toString(), "searchAdressbuch/ok",false).clone();
                infoAdresse.add(infoFirma.get(0));
                infoAdresse.add(infoPerson.get(tabPerson.getSelectedRow()));
                if (sucheAuswahl) {
                    haupt.vStoerung.setStoerungSearch(haupt.IDAdressbuchSelected.getText(),haupt.allStoerungen.isSelected());
                } else {
                    stoerung.change = false;
                    stoerung.cbFirma.setSelectedItem(infoAdresse.get(0).get(1));
                    stoerung.cbFirmaPerson.removeAllItems();
                    stoerung.cbFirmaPerson.addItem(infoAdresse.get(1).get(2) + ", " + infoAdresse.get(1).get(1));
                    stoerung.IDAdressbuch = infoAdresse.get(1).get(0).toString();

                    stoerung.AdressWahl = false;
                    stoerung.change = true;
                    stoerung.Adressaenderung = false;
                }

                dial.setVisible(false);
                dial.dispose();
            } else {
                JOptionPane.showMessageDialog(new JFrame(), "Bitte wählen Sie eine Person aus!");
            }

        }
    }

    private ArrayList<ArrayList<String>> getAdressPerson(String Person, int Kriterium) {
        ArrayList<ArrayList<String>> pTemp = (ArrayList<ArrayList<String>>) clients.netzwerkVerwaltung.getAdressbuchPerson("-1", "-1", "searchAdressbuch/getAdressPerson",false).clone();
        for (int i = 0; i < pTemp.size(); i++) {
            if (!pTemp.get(i).get(Kriterium).toString().toLowerCase().startsWith(Person.toLowerCase())) {
                pTemp.remove(i);
                i--;
            }
        }
        return pTemp;
    }

    protected void searhPerson(String Person, int Kriterium) {
        infoPerson = getAdressPerson(Person, Kriterium);
        setTableInfoPerson(infoPerson);
        setAdressbuchVisible();
    }

    private ArrayList<ArrayList<String>> getAdressFirma(String Firma, int Kriterium) {
        ArrayList<ArrayList<String>> fTemp = (ArrayList<ArrayList<String>>) clients.netzwerkVerwaltung.getAdressbuchFirma("-1", "searchAdressbuch/getAdressFirma",false).clone();
        if (Kriterium != -1) {
            for (int i = 0; i < fTemp.size(); i++) {
                if (!fTemp.get(i).get(Kriterium).toString().toLowerCase().startsWith(Firma.toLowerCase())) {
                    fTemp.remove(i);
                    i--;
                }
            }
        }
        return fTemp;
    }

    protected void searhFirma(String Person, int Kriterium) {
        infoFirma = getAdressFirma(Person, Kriterium);
        setTableInfoFirma(infoFirma);
        setAdressbuchVisible();
        infoPerson = (ArrayList<ArrayList<String>>) clients.netzwerkVerwaltung.getAdressbuchPerson("-2", tabFirma.getValueAt(0, 0).toString(), "searchAdressbuch/searhFirma",false).clone();
        setTableInfoPerson(infoPerson);
    }

    protected void setAdressbuchVisible() {
        dial.setVisible(true);
    }

    protected void setTableInfoPerson(ArrayList<ArrayList<String>> datenInfoPerson) {
        tablePerson.getInfoTable.removeAllItems();
        for (int i = 0; i < datenInfoPerson.size(); i++) {
            if (datenInfoPerson.get(i).size() > 7){
            daten = new ArrayList<String>();
            daten.add(datenInfoPerson.get(i).get(0).toString());
            daten.add(datenInfoPerson.get(i).get(1).toString());
            daten.add(datenInfoPerson.get(i).get(2).toString());
            daten.add(datenInfoPerson.get(i).get(3));
            daten.add(datenInfoPerson.get(i).get(4).toString());
            daten.add(datenInfoPerson.get(i).get(5).toString());
            daten.add(datenInfoPerson.get(i).get(6).toString());
            daten.add(datenInfoPerson.get(i).get(7).toString());
            tablePerson.getInfoTable.addRow(daten, "1");
            }
        }
    }

    protected void setTableInfoFirma(ArrayList<ArrayList<String>> datenInfoFirma) {
        tableFirma.getInfoTable.removeAllItems();
        for (int i = 0; i < datenInfoFirma.size(); i++) {
            daten = new ArrayList<String>();
            daten.add(datenInfoFirma.get(i).get(0).toString());
            daten.add(datenInfoFirma.get(i).get(1).toString());
            daten.add(datenInfoFirma.get(i).get(2).toString());
            tableFirma.getInfoTable.addRow(daten, "1");
        }
    }

    public void getSearchUser(String Name, String vorname) {
        if (Name == null) {
            if (vorname == null) {
                infoFirma = getAdressFirma("-1", -1);
                setTableInfoFirma(infoFirma);
            } else {
                infoPerson = getAdressPerson(vorname, 2);
                setTableInfoPerson(infoPerson);
            }

        } else {
            infoPerson = getAdressPerson(Name, 1);
            setTableInfoPerson(infoPerson);
        }
        sucheAuswahl = false;
        setAdressbuchVisible();
    }

    public class searchPersonInfo implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            infoPerson = getAdressPerson(searchPerson.getText(), 1);
            setTableInfoPerson(infoPerson);
        }
    }

    public class searchFirmaInfo implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                if (tabFirma.getRowCount() > 0) {
                    infoFirma = getAdressFirma(search.getText(), 1);
                    setTableInfoFirma(infoFirma);
                    infoPerson = (ArrayList<ArrayList<String>>) clients.netzwerkVerwaltung.getAdressbuchPerson("-2", tabFirma.getValueAt(0, 0).toString(), "searchAdressbuch/searchFirmaInfo",false).clone();
                    setTableInfoPerson(infoPerson);
                }
            } catch (Exception ee) {
            }
        }
    }
}
