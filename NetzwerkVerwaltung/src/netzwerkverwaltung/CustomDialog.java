/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package netzwerkverwaltung;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

public class CustomDialog extends JDialog implements ActionListener {
    private JPanel myPanel = null;
    private JButton yesButton = null;
    private JColorChooser color;
    private Color col;
    public Color getAnswer() { return col; }

    public CustomDialog(JFrame frame, boolean modal) {
        super(frame, modal);
        myPanel = new JPanel();
        myPanel.setLayout(new BorderLayout());
        getContentPane().add(myPanel);
        color = new JColorChooser();
        myPanel.add(color, BorderLayout.CENTER);
        yesButton = new JButton("Yes");
        yesButton.addActionListener(this);
        myPanel.add(yesButton, BorderLayout.SOUTH); 
        
        pack();
        setLocationRelativeTo(frame);
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(yesButton == e.getSource()) {
            System.err.println("User chose yes.");
            col = color.getColor();
            setVisible(false);
        }
        
    }
    
}