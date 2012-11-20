/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package netzwerkverwaltung;


import java.awt.Color;
import javax.swing.DefaultCellEditor;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.util.ArrayList;
import java.util.HashMap;



public final class TabelFont extends JPanel {

    private boolean DEBUG = false;
 
    public TabelModel getInfoTable;
    private ArrayList<HashMap> daten;
    private boolean weiter=false;

    private JTable JTableFarbe(TabelModel daten) {
        JTable temp = new JTable(daten) {

            @Override
            public Component prepareRenderer(
                    TableCellRenderer renderer, int row, int column) {
                Component c = super.prepareRenderer(renderer, row, column);
                
                int r = 226, b = 243, y = 253;
                if (!isRowSelected(row)) {
                    c.setMaximumSize(new Dimension(20, 21));
                    c.setBackground(row % 2 != 0 ? null : Color.getHSBColor(Color.RGBtoHSB(r, b, y, null)[0], Color.RGBtoHSB(r, b, y, null)[1], Color.RGBtoHSB(r, b, y, null)[2]));
                }
                if (weiter)
                    c.setFont(new Font(null,Font.BOLD,12));
                return c;
            }
        };

        //initColumnSizes(temp);
        return temp;
    }
    
     

    public JTable getTable(String[] Colum,int cScroll,JComboBox comboBox,int cScroll2,JComboBox comboBox2) {
        getInfoTable = new TabelModel();
        getInfoTable.columnNames = Colum;
        JTable table = JTableFarbe(getInfoTable);
        if (cScroll2 != -1)
         setCombobox(table, table.getColumnModel().getColumn(cScroll2),comboBox2);
        if (cScroll != -1)
         setCombobox(table, table.getColumnModel().getColumn(cScroll),comboBox);
        
        return table;
    }

    private void setCombobox(JTable table, TableColumn sportColumn, JComboBox comboBox) {
        sportColumn.setCellEditor(new DefaultCellEditor(comboBox));
        
        DefaultTableCellRenderer renderer = new DefaultTableCellRenderer();
        sportColumn.setCellRenderer(renderer);
    }

    public class TabelModel extends AbstractTableModel {
        public int wert=4;
        private String[] columnNames;

        public void setColumName(String[] CD) {
            columnNames = CD;
        }
        private ArrayList<ArrayList<String>> daten = new ArrayList<ArrayList<String>>();

        @Override
        public int getColumnCount() {
            return columnNames.length;
        }

        @Override
        public int getRowCount() {
            return daten.size();
        }

        public void removeAllItems(){
            int i=0;
            while (daten.size() > 0){
                daten.remove(i);
                fireTableRowsDeleted(i, i);
            }
        }
        @Override
        public String getColumnName(int col) {
            return columnNames[col];
        }

        @Override
        public Object getValueAt(int row, int col) {
            return daten.get(row).get(col);
        }

        @Override
        public Class getColumnClass(int c) {
            return getValueAt(0, c).getClass();
        }

       
        @Override
        public boolean isCellEditable(int row, int col) {
            if (col < wert) {
                return false;
            } else {
                return true;
            }
        }

        /* 
         * Don't need to implement this method unless your table's 
         * data can change. 
         */
        @Override
        public void setValueAt(Object value, int row, int col) {
           

            daten.get(row).add(col, String.valueOf(value));
            fireTableCellUpdated(row, col);

        }

        public void addRow(ArrayList<String> getDaten,String fett) {
            int ID = getRowCount();
            if (fett.equals("0")){
                weiter = true;
            }else{
                weiter = false;
            }
            daten.add(getDaten);
            fireTableRowsInserted(ID, ID);
        }

        public void removeRowAt(int r) {
            daten.remove(r);
            fireTableRowsDeleted(r, r);
        }
    }
}
