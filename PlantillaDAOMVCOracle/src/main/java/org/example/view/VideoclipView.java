package org.example.view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class VideoclipView extends JFrame{
    /**
     * Atributs de la classe
     */
    private JTabbedPane pestanyes;
    private JTable taula;
    private JScrollPane scrollPane1;
    private JButton insertarButton;
    private JButton modificarButton;
    private JButton borrarButton;
    private JTextField campNom;
    private JTextField campDurada;
    private JPanel panel;
    private JTable taulaMat;
    private JComboBox comboPais;
    private JTextField campAny;



    //private JCheckBox caixaSin;


    //Getters

    public JTable getTaulaMat() {
        return taulaMat;
    }

    public JComboBox getComboPais() {
        return comboPais;
    }

    public JTextField getCampAny() {
        return campAny;
    }

    public JTabbedPane getPestanyes() {
        return pestanyes;
    }

  //  public JCheckBox getCaixaSin() {
   //     return caixaSin;
   // }

    public JTable getTaula() {
        return taula;
    }

    public JButton getBorrarButton() {
        return borrarButton;
    }

    public JButton getModificarButton() {
        return modificarButton;
    }

    public JButton getInsertarButton() {
        return insertarButton;
    }

    public JTextField getCampNom() {
        return campNom;
    }

    public JTextField getCampDurada() {
        return campDurada;
    }



    public VideoclipView() {
        /**
         * Constructor de la classe
         */

        //Per poder vore la finestra
        this.setContentPane(panel);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.pack();
        this.setVisible(false);
    }

        private void createUIComponents() {
        // TODO: place custom component creation code here
        scrollPane1 = new JScrollPane();
        taula = new JTable();
        pestanyes = new JTabbedPane();
        taula.setModel(new DefaultTableModel());
        taula.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        scrollPane1.setViewportView(taula);
        //tamany del JPanel
        panel = new JPanel();
        panel.setSize(800, 600);


    }
}
