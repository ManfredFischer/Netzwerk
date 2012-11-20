/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package netzwerkverwaltung;

import javax.swing.JTable;

/**
 *
 * @author manfred.fischer
 */
public class UserBeantragen extends javax.swing.JFrame {

    /**
     * Creates new form UserBeantragen
     */
    private newUser user;
    
    private String[] titelS = {"Software", "beantragen"};
    private JTable tabS;
    public TabelFont fontS = new TabelFont();
    
    private String[] titelH = {"Hardware", "beantragen"};
    private JTable tabH;
    public TabelFont fontH = new TabelFont();

    public UserBeantragen(newUser user) {
        this.user = user;
        initComponents();
        tabS = fontS.getTable(titelS, -1, null, -1, null);
        tabH = fontH.getTable(titelH, -1, null, -1, null);
        
        scS.add(tabS);
        scS.setViewportView(tabS);
        
        scH.add(tabH);
        scH.setViewportView(tabH);
        this.setLocationRelativeTo(null);
        
    }
    
    public void start(){
      this.setVisible(true);  
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        ads = new javax.swing.JCheckBox();
        kelt = new javax.swing.JCheckBox();
        sof = new javax.swing.JCheckBox();
        hard = new javax.swing.JCheckBox();
        jButton1 = new javax.swing.JButton();
        scS = new javax.swing.JScrollPane();
        scH = new javax.swing.JScrollPane();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setResizable(false);

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        ads.setBackground(new java.awt.Color(255, 255, 255));
        ads.setText("ADS eintrag");
        ads.setEnabled(false);
        ads.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                adsActionPerformed(evt);
            }
        });

        kelt.setBackground(new java.awt.Color(255, 255, 255));
        kelt.setSelected(true);
        kelt.setText("Kelsterbach");
        kelt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                keltActionPerformed(evt);
            }
        });

        sof.setBackground(new java.awt.Color(255, 255, 255));
        sof.setSelected(true);
        sof.setText("Software");
        sof.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                sofActionPerformed(evt);
            }
        });

        hard.setBackground(new java.awt.Color(255, 255, 255));
        hard.setSelected(true);
        hard.setText("Hardware");
        hard.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                hardActionPerformed(evt);
            }
        });

        jButton1.setText("Beantragen");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(scS)
                    .addComponent(scH)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(hard)
                            .addComponent(sof))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(kelt)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(ads)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 87, Short.MAX_VALUE)
                        .addComponent(jButton1)))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(sof)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(scS, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(hard)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(scH, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(kelt)
                    .addComponent(ads)
                    .addComponent(jButton1))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
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

    private void adsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_adsActionPerformed
 
    }//GEN-LAST:event_adsActionPerformed

    private void keltActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_keltActionPerformed
       
    }//GEN-LAST:event_keltActionPerformed

    private void sofActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_sofActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_sofActionPerformed

    private void hardActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_hardActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_hardActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        if (sof.isSelected()) {
            user.SoftwareBeantragen();
        }
        if (hard.isSelected()) {
            user.ZubehoerBeantragen();
        }
        if (kelt.isSelected()) {
            user.KelsterbachBeantragen();
        }
        if (ads.isSelected()) {
            user.ADSBeantragen();
        }
        user.vorschauFenster.setVisible(true);
        this.setVisible(false);
        this.dispose();
    }//GEN-LAST:event_jButton1ActionPerformed
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JCheckBox ads;
    private javax.swing.JCheckBox hard;
    private javax.swing.JButton jButton1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JCheckBox kelt;
    private javax.swing.JScrollPane scH;
    private javax.swing.JScrollPane scS;
    private javax.swing.JCheckBox sof;
    // End of variables declaration//GEN-END:variables
}
