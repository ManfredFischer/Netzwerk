/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * Vorlagen.java
 *
 * Created on 29.12.2011, 13:20:34
 */
package netzwerkverwaltung;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTable;
public class Vorlagen extends javax.swing.JFrame {

    private String[] titles = {"Name"};
    private ArrayList<String> daten = new ArrayList<String>(), datenT = new ArrayList<String>();
    private TabelFont tableServerUser = new TabelFont();
    private JTable tabServerUser;
   

    /** Creates new form Vorlagen */
    public Vorlagen() {
        initComponents();
        tabServerUser = tableServerUser.getTable(titles, -1, null, -1, null);
        tabServerUser.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    String[] text = daten.get(tabServerUser.getSelectedRow()).split("\\+--\\+")[1].split("\\+\\+\\+");
                    String wert = text[0];
                    for (int i = 1; i < text.length; i++) {
                        wert = wert + "\n" + text[i];
                    }
                    name.setText(daten.get(tabServerUser.getSelectedRow()).split("\\+--\\+")[0]);
                    info.setText(wert);
                    go.setText("Übernehmen");
                }
            }
        });
        scInfo.add(tabServerUser);
        scInfo.setViewportView(tabServerUser);
        this.setVisible(true);

        BufferedReader read;
        try {
            read = new BufferedReader(new FileReader(new File("Config/vorlagen.cfg")));
            String reader = read.readLine();
            while (reader != null) {
                daten.add(reader);
                reader = read.readLine();
            }
            read.close();
            for (int i = 0; i < daten.size(); i++) {
                datenT = new ArrayList<String>();
                datenT.add(daten.get(i).split("\\+--\\+")[0]);
                tableServerUser.getInfoTable.addRow(datenT,"1");

            }
        } catch (Exception ex) {
        }
    }
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        info = new javax.swing.JTextArea();
        name = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        scInfo = new javax.swing.JScrollPane();
        jButton1 = new javax.swing.JButton();
        go = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        info.setColumns(1);
        info.setRows(1);
        info.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));

        jLabel1.setText("Name:");

        jButton1.setText("löschen");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        go.setText("Hinzufügen");
        go.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                goActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(info, javax.swing.GroupLayout.PREFERRED_SIZE, 273, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(scInfo, javax.swing.GroupLayout.DEFAULT_SIZE, 115, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(name, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButton1)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(317, Short.MAX_VALUE)
                .addComponent(go)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(name, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(info, javax.swing.GroupLayout.DEFAULT_SIZE, 120, Short.MAX_VALUE)
                    .addComponent(scInfo, javax.swing.GroupLayout.DEFAULT_SIZE, 120, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(go)
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void goActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_goActionPerformed
        if (go.getText().equals("Übernehmen")) {
        daten.remove(tabServerUser.getSelectedRow());
        tableServerUser.getInfoTable.removeRowAt(tabServerUser.getSelectedRow());
    }

    BufferedWriter write = null;
    Boolean weiter = true;

    for (int i = 0; i < daten.size(); i++) {
        if (daten.get(i).split("\\+--\\+")[0].equals(name.getText()) || name.getText().equals("  -- Date --  ")) {
            weiter = false;
            break;
        }
    }
    if (weiter) {
        try {
            write = new BufferedWriter(new FileWriter(new File("Config/vorlagen.cfg")));
            for (int i = 0; i < daten.size(); i++) {

                write.write(daten.get(i) + "\n");
            }

            String[] text = info.getText().split("\n");
            String wert = text[0];
            for (int i = 1; i < text.length; i++) {
                wert = wert + "+++" + text[i];
            }
            write.write(name.getText() + "+--+" + wert);
            write.close();
            ArrayList<String> d = new ArrayList<String>();
            d.add(name.getText());
            tableServerUser.getInfoTable.addRow(d,"1");
            daten.add(name.getText() + "+--+" + wert);
            name.setText("");
            info.setText("");
        } catch (Exception ex) {
        }
    } else {
        JOptionPane.showMessageDialog(new JFrame(), "Name schon vorhanden...");
    }
    }//GEN-LAST:event_goActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
       try {
        daten.remove(tabServerUser.getSelectedRow());
        tableServerUser.getInfoTable.removeRowAt(tabServerUser.getSelectedRow());
        BufferedWriter write = null;
        try {
            write = new BufferedWriter(new FileWriter(new File("Config/vorlagen.cfg")));
            for (int i = 0; i < daten.size(); i++) {
                write.write(daten.get(i) + "\n");
            }
            write.close();
        } catch (Exception ex) {
        }
    } catch (Exception e) {
    }
    }//GEN-LAST:event_jButton1ActionPerformed

  
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton go;
    private javax.swing.JTextArea info;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JTextField name;
    private javax.swing.JScrollPane scInfo;
    // End of variables declaration//GEN-END:variables
}
