package org.example.app;

import org.example.controller.Controller;
import org.example.model.impls.CancoDAOJDBCOracleImpl;
import org.example.view.VideoclipView;

import javax.swing.*;
import java.util.Locale;

public class Main {


    public static void main(String[] args) {

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                //Definim la cultura de la nostra aplicació
                Locale.setDefault(new Locale("ca","ES"));
               new Controller(new CancoDAOJDBCOracleImpl(), new VideoclipView());

            }
        });
    }
}
