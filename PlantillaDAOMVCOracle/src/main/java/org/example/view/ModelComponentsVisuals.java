package org.example.view;

import org.example.model.entities.Canço;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class ModelComponentsVisuals {

    private DefaultTableModel modelTaulaCanço;
    private DefaultTableModel modelTaulaVid;
    private ComboBoxModel<Canço.Videoclip.Modul> comboBoxModel;

    //Getters


    public ComboBoxModel<Canço.Videoclip.Modul> getComboBoxModel() {
        return comboBoxModel;
    }

    public DefaultTableModel getModelTaulaCanço() {
        return modelTaulaCanço;
    }

    public DefaultTableModel getModelTaulaVid() {
        return modelTaulaVid;
    }

    public ModelComponentsVisuals() {


        //Anem a definir l'estructura de la taula dels alumnes
        modelTaulaCanço =new DefaultTableModel(new Object[]{"Nom","Durada", "Object"},0){
            /**
             * Returns true regardless of parameter values.
             *
             * @param row    the row whose value is to be queried
             * @param column the column whose value is to be queried
             * @return true
             * @see #setValueAt
             */
            @Override
            public boolean isCellEditable(int row, int column) {

                //Fem que TOTES les cel·les de la columna 1 de la taula es puguen editar
                //if(column==1) return true;
                return false;
            }



            //Permet definir el tipo de cada columna
            @Override
            public Class getColumnClass(int column) {
                switch (column) {
                    case 0:
                        return String.class;
                    case 1:
                        return Double.class;
                    default:
                        return Object.class;
                }
            }
        };

        //Anem a definir l'estructura de la taula de les matrícules
        modelTaulaVid =new DefaultTableModel(new Object[]{"Pais","Any"},0){
            /**
             * Returns true regardless of parameter values.
             *
             * @param row    the row whose value is to be queried
             * @param column the column whose value is to be queried
             * @return true
             * @see #setValueAt
             */
            @Override
            public boolean isCellEditable(int row, int column) {

                //Fem que TOTES les cel·les de la columna 1 de la taula es puguen editar
                //if(column==1) return true;
                return false;
            }

            //Permet definir el tipo de cada columna
            @Override
            public Class getColumnClass(int column) {
                switch (column) {
                    case 0:
                        return Canço.Videoclip.Modul.class;
                    case 1:
                        return Integer.class;
                    default:
                        return Object.class;
                }
            }
        };
        
        //Estructura del comboBox
        comboBoxModel=new DefaultComboBoxModel<>(Canço.Videoclip.Modul.values());



    }
}
