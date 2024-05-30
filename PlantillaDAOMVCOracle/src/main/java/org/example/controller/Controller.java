package org.example.controller;

import org.example.model.entities.Canço;
import org.example.model.exceptions.DAOException;
import org.example.view.ModelComponentsVisuals;
import org.example.model.impls.CancoDAOJDBCOracleImpl;
import org.example.view.VideoclipView;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.List;
import java.util.Locale;
import java.util.TreeSet;

public class Controller implements PropertyChangeListener {
    //1. Implementació de interfície PropertyChangeListener
    /**
     * Classe que controla les accions de l'aplicació
     */


    private ModelComponentsVisuals modelComponentsVisuals = new ModelComponentsVisuals();
    private CancoDAOJDBCOracleImpl dadesCancons;
    private VideoclipView view;

    public Controller(CancoDAOJDBCOracleImpl dadesCancons, VideoclipView view) {
        /**
         * Constructor de la classe Controller
         * @param dadesCancons Objecte que conté les dades de les cançons
         * @param view Objecte que conté la vista de l'aplicació
         */
        this.dadesCancons = dadesCancons;
        this.view = view;

        //5. Necessari per a que Controller reaccione davant de canvis a les propietats lligades
        //canvis.addPropertyChangeListener(this);

        lligaVistaModel();

        afegirListeners();


        view.setVisible(true);

    }

    private void lligaVistaModel() {
        /**
         * 1. Lligam les propietats de la vista amb les propietats del model
         */


        try {
            setmodelTaulaCanco(modelComponentsVisuals.getModelTaulaCanço(),dadesCancons.getAll());
        } catch (DAOException e) {
            this.setExcepcio(e);
        }


        JTable taula = view.getTaula();
        taula.setModel(this.modelComponentsVisuals.getModelTaulaCanço());

        taula.getColumnModel().getColumn(2).setMinWidth(0);
        taula.getColumnModel().getColumn(2).setMaxWidth(0);
        taula.getColumnModel().getColumn(2).setPreferredWidth(0);


        JTable taulaMat = view.getTaulaMat();
        taulaMat.setModel(this.modelComponentsVisuals.getModelTaulaVid());


        view.getComboPais().setModel(modelComponentsVisuals.getComboBoxModel());

        view.getPestanyes().setEnabledAt(1, false);
        view.getPestanyes().setTitleAt(1, "Videoclip de ...");

        //5. Necessari per a que Controller reaccione davant de canvis a les propietats lligades
        canvis.addPropertyChangeListener(this);
    }

    private void setmodelTaulaCanco(DefaultTableModel modelTaulaCanco, List<Canço> all) {
        /**
         * Omple el model de la taula de cançons amb les dades de la col·lecció
         * @param modelTaulaCanco Model de la taula de cançons
         * @param all Col·lecció de cançons
         */

        // Fill the table model with data from the collection
        for (Canço obra : all) {
            modelTaulaCanco.addRow(new Object[]{obra.getNom(), obra.getDurada(),obra});
        }
    }

    private void afegirListeners() {
        /**
         * 2. Afegim els listeners als components de la vista
         */

        ModelComponentsVisuals modelo = this.modelComponentsVisuals;
        DefaultTableModel model = modelo.getModelTaulaCanço();
        DefaultTableModel modelVid = modelo.getModelTaulaVid();
        JTable taula = view.getTaula();

        JTable taulaVid = view.getTaulaMat();
        JButton insertarButton = view.getInsertarButton();
        JButton modificarButton = view.getModificarButton();
        JButton borrarButton = view.getBorrarButton();

        JTextField campNom = view.getCampNom();
        JTextField campDurada = view.getCampDurada();
       // JCheckBox caixaSingle = view.getCaixaSin();
        JTabbedPane pestanyes = view.getPestanyes();

        JTextField campAny = view.getCampAny();
        JComboBox comboPais = view.getComboPais();


        //Botó insertar
        view.getInsertarButton().addActionListener(
                new ActionListener() {
                    /**
                     * Invoked when an action occurs.
                     *
                     * @param e the event to be processed
                     */

                    @Override
                    public void actionPerformed(ActionEvent e) {
                        JTextField campNom = view.getCampNom();
                        JTextField campDurada = view.getCampDurada();

                     // JCheckBox caixaSingle = view.getCaixaSingle();

                        if (pestanyes.getSelectedIndex() == 0) {        //Si estem a la pestanya de cançó
                            //Comprovem que totes les caselles continguen informació
                            if (campNom.getText().isBlank() || campDurada.getText().isBlank() ) {
                                JOptionPane.showMessageDialog(null, "Falta omplir alguna dada!!");
                            } else {
                                try {
                                    NumberFormat num = NumberFormat.getNumberInstance(Locale.getDefault());   //Creem un número que entèn la cultura que utilitza l'aplicació
                                    double durada = num.parse(campDurada.getText().trim()).doubleValue();  //intentem convertir el text a double
                                    if (durada < 1 || durada > 60) throw new ParseException("", 0);
                                    Canço canco = new Canço(campNom.getText(), durada, new TreeSet<Canço.Videoclip>());
                                    //Verifica si la canço no és null
                                    if (canco != null) {
                                        System.out.println("Cançó creada " + canco.getNom() + ", " + canco.getDurada() );
                                        dadesCancons.save(canco);
                                    }
                                    model.addRow(new Object[]{campNom.getText(), durada, canco});
                                    campNom.setText("Bad Romance");
                                    campNom.setSelectionStart(0);
                                    campNom.setSelectionEnd(campNom.getText().length());
                                    campDurada.setText("3");
                                    campNom.requestFocus();         //intentem que el foco vaigue al camp del nom
                                } catch (ParseException parseException) {
                                    setExcepcio(new DAOException(3));
                                    campDurada.setSelectionStart(0);
                                    campDurada.setSelectionEnd(campDurada.getText().length());
                                    campDurada.requestFocus();
                                }catch (DAOException DAOex){
                                    setExcepcio(DAOex);
                                }
                            }
                        } else {         //Si estem a la pestanya de videoclip
                            Canço canco = (Canço) model.getValueAt(taula.getSelectedRow(), 2);
                            //Creem un nou objecte videoclip amb les dades de la casella
                            if (campAny.getText().isBlank()) {
                                JOptionPane.showMessageDialog(null, "Falta omplir alguna dada!!");
                            } else {
                                Canço.Videoclip videoclip = new Canço.Videoclip((Canço.Videoclip.Modul) comboPais.getSelectedItem(), Integer.parseInt(campAny.getText()));
                                canco.getVideoclips().add(videoclip);
                                //Omplim la taula de videoclip
                                ompliVideoclip(canco, modelVid);
                            }
                        }



                    }
                }
        );

        //Botó modificar
        view.getModificarButton().addActionListener(
                new ActionListener() {
                    /**
                     * Invoked when an action occurs.
                     *
                     * @param e the event to be processed
                     */

                    @Override
                    public void actionPerformed(ActionEvent e) {
                        JTextField campNom = view.getCampNom();
                        JTextField campDurada = view.getCampDurada();
                        //JCheckBox caixaSingle = view.getCaixaSingle();

                        if (pestanyes.getSelectedIndex() == 0) {
                            //Comprovem que totes les caselles continguen informació
                            if (campNom.getText().isBlank() || campDurada.getText().isBlank() ) {
                                JOptionPane.showMessageDialog(null, "Falta omplir alguna dada!!");

                            } else {
                                try {
                                    NumberFormat num = NumberFormat.getNumberInstance(Locale.getDefault());   //Creem un número que entèn la cultura que utilitza l'aplicació
                                    double durada = num.parse(campDurada.getText().trim()).doubleValue();  //intentem convertir el text a double
                                    if (durada < 1 || durada > 60) throw new ParseException("", 0);
                                    Canço canco = (Canço) model.getValueAt(taula.getSelectedRow(), 2);

                                    canco.setNom(campNom.getText());
                                    canco.setDurada(durada);

                                    try {
                                        dadesCancons.update(canco);
                                        model.setValueAt(campNom.getText(), taula.getSelectedRow(), 0);
                                        model.setValueAt(durada, taula.getSelectedRow(), 1);
                                       // model.setValueAt(caixaSingle.isSelected(), taula.getSelectedRow(), 2);

                                        campNom.setText("Bad Romance");
                                        campNom.setSelectionStart(0);
                                        campNom.setSelectionEnd(campNom.getText().length());
                                        campDurada.setText("3");
                                        campNom.requestFocus();
                                    }catch (DAOException DAOex){
                                        DAOex.printStackTrace();
                                        setExcepcio(DAOex);
                                    }
                                } catch (ParseException ex) {
                                    ex.printStackTrace();
                                    setExcepcio(new DAOException(3));
                                    campDurada.setSelectionStart(0);
                                    campDurada.setSelectionEnd(campDurada.getText().length());
                                    campDurada.requestFocus();
                                }
                            }
                        } else if (taulaVid.getSelectedRow() != -1) {        //Si estem a la pestanya de videoclip
                            Canço canco = (Canço) model.getValueAt(taula.getSelectedRow(), 2);
                            //Obtinc la cançó de la columna que conté l'objecte
                            Canço.Videoclip videoclip = canco.getVideoclips().stream().skip(taulaVid.getSelectedRow()).findFirst().get();
                            //Creem un nou objecte matrícula amb les dades de la casella
                            if (campAny.getText().isBlank()) {
                                JOptionPane.showMessageDialog(null, "Falta omplir alguna dada!!");
                            } else {
                                videoclip.setModul((Canço.Videoclip.Modul) comboPais.getSelectedItem());
                                videoclip.setAny(Integer.parseInt(campAny.getText()));
                                //Omplim la taula de matrícula
                                ompliVideoclip(canco, modelVid);
                            }
                        }
                        


                }
                }
        );

        //Botó borrar
        view.getBorrarButton().addActionListener(
                new ActionListener() {
                    /**
                     * Invoked when an action occurs.
                     *
                     * @param e the event to be processed
                     */

                    @Override
                    public void actionPerformed(ActionEvent e) {
                        if (pestanyes.getSelectedIndex() == 0) {        //Si estem a la pestanya de cançó
                            int filaSel = taula.getSelectedRow();
                            if(filaSel != -1 ){
                                Canço canco = (Canço) modelo.getModelTaulaCanço().getValueAt(filaSel, 2);
                                try {
                                    dadesCancons.delete(canco.getId());
                                    model.removeRow(filaSel);
                                } catch (DAOException DAOex) {
                                    setExcepcio(DAOex);
                                }
                            }
                        } else {         //Si estem a la pestanya de videoclip
                            int filaSel = taulaVid.getSelectedRow();
                            if(filaSel != -1 ){
                                Canço canco = (Canço) model.getValueAt(filaSel, 2);
                               int filaSelec2 = taulaVid.getSelectedRow();
                               if(filaSelec2 != -1){
                                   modelVid.removeRow(filaSelec2);
                               }
                            }

                        }
                    }
                }
        );

        taula.addMouseListener(new MouseAdapter() {
            /**
             * {@inheritDoc}
             *
             * @param e
             */
            @Override

            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);

                //Obtenim el número de la fila seleccionada
                int filaSel = taula.getSelectedRow();

                if (filaSel != -1) {        //Tenim una fila seleccionada
                    //Posem els valors de la fila seleccionada als camps respectius
                    campNom.setText(model.getValueAt(filaSel, 0).toString());
                    campDurada.setText(model.getValueAt(filaSel, 1).toString().replaceAll("\\.", ","));
                   // caixaSingle.setSelected((Boolean) model.getValueAt(filaSel, 2));

                    //Activem la pestanya de la matrícula de l'alumne seleccionat
                    view.getPestanyes().setEnabledAt(1, true);
                    view.getPestanyes().setTitleAt(1, "Videoclip de " + campNom.getText());

                    //Posem valor a el combo d'MPs
                    //view.getComboMP().setModel(modelo.getComboBoxModel());
                    ompliVideoclip((Canço) model.getValueAt(filaSel, 2), modelVid);
                } else {                  //Hem deseleccionat una fila
                    //Posem els camps de text en blanc
                    campNom.setText("");
                    campDurada.setText("");

                    //Desactivem pestanyes
                    view.getPestanyes().setEnabledAt(1, false);
                    view.getPestanyes().setTitleAt(1, "Videoclip de ...");
                }
            }
        });

        //Restriccions
        //Fem que campNom nomes puga contindre lletres.
        campNom.addKeyListener(new KeyAdapter() {
            /**
             * Invoked when a key has been typed.
             * See the class description
             * for {@code KeyEvent} for a definition of
             * a key typed event.
             * @param e
             */
            @Override
            public void keyTyped(KeyEvent e) {
                super.keyTyped(e);

                JTextField textField = (JTextField) e.getSource();
                String text = textField.getText();

                char keyChar = e.getKeyChar();

                String regex = "^[A-Z][a-zA-Z]*$";

                if (Character.isLetter(keyChar)) {
                    text += keyChar;
                } else if (keyChar == KeyEvent.VK_BACK_SPACE || keyChar == KeyEvent.VK_DELETE) {
                    return;
                } else {
                    mostrarMensajeError();
                    e.consume();
                    setExcepcio(new DAOException(2));
                    return;
                }

                if (!text.matches(regex)) {
                    mostrarMensajeError();
                    e.consume();
                    setExcepcio(new DAOException(34));
                }
            }

            private void mostrarMensajeError() {
                JPanel panel = new JPanel();
                JOptionPane.showMessageDialog(panel, "Aquest camp té que començar en MAJÚSCULA i només pot contindre lletres.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });


        //Fem que campDurada nomes puga contindre números
        campDurada.addKeyListener(new KeyAdapter() {
            /**
             * Invoked when a key has been typed.
             * See the class description
             * for {@code KeyEvent} for a definition of
             * a key typed event.
             * @param e
             */
            @Override

            public void keyTyped(KeyEvent e) {
                super.keyTyped(e);
                if (!Character.isDigit(e.getKeyChar()) && e.getKeyChar() != KeyEvent.VK_PERIOD && e.getKeyChar() != KeyEvent.VK_COMMA && e.getKeyChar() != KeyEvent.VK_BACK_SPACE && e.getKeyChar() != KeyEvent.VK_DELETE){
                    JPanel panel = new JPanel();
                    JOptionPane.showMessageDialog(panel, "Aquest camp no pot contindre lletres ", "Error", JOptionPane.ERROR_MESSAGE);
                    e.consume();
                    setExcepcio(new DAOException(35));
                }
            }
            //Fem que si la durada és superior a 60, no es puga introduir. Fem que accepte . i ,
            @Override
            public void keyReleased(KeyEvent e) {
                super.keyReleased(e);
                try {
                    NumberFormat num = NumberFormat.getNumberInstance(Locale.getDefault());
                    double durada = num.parse(campDurada.getText().trim()).doubleValue();
                    if (durada > 60) {
                        JPanel panel = new JPanel();
                        JOptionPane.showMessageDialog(panel, "La durada no pot ser superior a 60 minuts (1h)", "Error", JOptionPane.ERROR_MESSAGE);
                        campDurada.setText("60");
                    }
                } catch (ParseException ex) {
                    setExcepcio(new DAOException(3));
                    campDurada.setSelectionStart(0);
                    campDurada.setSelectionEnd(campDurada.getText().length());
                    campDurada.requestFocus();
                }
                if (e.getKeyChar() == ',') {
                    e.setKeyChar('.');
                }

            }
        });
        //Fem que campAny nomes puga contindre números
        campAny.addKeyListener(new KeyAdapter() {
            /**
             * Invoked when a key has been typed.
             * See the class description
             * for {@code KeyEvent} for a definition of
             * a key typed event.
             * @param e
             */
            @Override

            public void keyTyped(KeyEvent e) {
                super.keyTyped(e);
                if (!Character.isDigit(e.getKeyChar()) && e.getKeyChar() != KeyEvent.VK_BACK_SPACE && e.getKeyChar() != KeyEvent.VK_DELETE){
                    JPanel panel = new JPanel();
                    JOptionPane.showMessageDialog(panel, "Aquest camp no pot contindre lletres i té que ser un número sense decimals.", "Error", JOptionPane.ERROR_MESSAGE);
                    e.consume();
                    setExcepcio(new DAOException(35));
                }
            }

            //Fem que any nomes pugue tindre valors entre 1900 i 2100
            @Override
            public void keyReleased(KeyEvent e) {
                super.keyReleased(e);
                try {
                    int any = Integer.parseInt(campAny.getText());
                    if (any < 1900 || any > 2100) {
                        JPanel panel = new JPanel();
                        JOptionPane.showMessageDialog(panel, "L'any ha d'estar entre 1900 i 2100", "Error", JOptionPane.ERROR_MESSAGE);
                        campAny.setText("2021");
                    }
                } catch (NumberFormatException ex) {
                    setExcepcio(new DAOException(35));
                    campAny.setSelectionStart(0);
                    campAny.setSelectionEnd(campAny.getText().length());
                    campAny.requestFocus();
                }
            }
        });

        //Quan insertem un valor null, salta excepció
        campNom.addFocusListener(new FocusAdapter() {
            /**
             * Invoked when a component gains the keyboard focus.
             *
             * @param e
             */
            @Override

            public void focusLost(FocusEvent e) {
                super.focusLost(e);
                if (campNom.getText().isBlank() || campNom.getText().equals("null") | campDurada.getText().equals("null") || campDurada.getText().isBlank() || campAny.getText().equals("null") || campAny.getText().isBlank()){
                    setExcepcio(new DAOException(36));
                }
            }
        });
        //Quan la taula no existeix
        taula.addMouseListener(new MouseAdapter() {
            /**
             * {@inheritDoc}
             *
             * @param e
             */
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                if (taula.getSelectedRow() == -1){
                    setExcepcio(new DAOException(942));
                }
            }
        });
    }

    private static void ompliVideoclip(Canço al, DefaultTableModel modelVid) {
        /**
         * Omple el model de la taula de videoclips amb les dades de la col·lecció
         * @param al Objecte que conté les dades de la cançó
         * @param modelVid Model de la taula de videoclips
         */

        modelVid.setRowCount(0);

        for (Canço.Videoclip videoclip : al.getVideoclips()) {
            modelVid.addRow(new Object[]{videoclip.getModul(), videoclip.getAny()});

        }
    }


    //TRACTAMENT D'EXCEPCIONS

    //2. Propietat lligada per controlar quan genero una excepció
    public static final String PROP_EXCEPCIO="excepcio";
    private DAOException excepcio;

    public DAOException getExcepcio() {
        return excepcio;
    }

    public void setExcepcio(DAOException excepcio) {
        /**
         * Setter de l'atribut excepcio
         * @param excepcio Objecte de tipus DAOException
         */
        DAOException valorVell=this.excepcio;
        this.excepcio = excepcio;
        canvis.firePropertyChange(PROP_EXCEPCIO, valorVell,excepcio);
    }


    //3. Propietat PropertyChangesupport necessària per poder controlar les propietats lligades
    PropertyChangeSupport canvis=new PropertyChangeSupport(this);


    //4. Mètode on posarem el codi de tractament de les excepcions --> generat per la interfície PropertyChangeListener
    /**
     * This method gets called when a bound property is changed.
     *
     * @param evt A PropertyChangeEvent object describing the event source
     *            and the property that has changed.
     */
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        /**
         * Mètode que s'executa quan es produeix un canvi en una propietat lligada
         * @param evt Objecte de tipus PropertyChangeEvent
         */
        DAOException rebuda=(DAOException)evt.getNewValue();

        try {
            throw rebuda;
        } catch (DAOException e) {
            //Aquí farem ele tractament de les excepcions de l'aplicació
            switch(evt.getPropertyName()){
                case PROP_EXCEPCIO:

                    switch(rebuda.getTipo()){
                        case 0:
                            JOptionPane.showMessageDialog(null, rebuda.getMessage());
                            System.exit(1);
                            break;
                        case 1:
                            JOptionPane.showMessageDialog(null, rebuda.getMessage());
                            break;
                        case 2:
                            JOptionPane.showMessageDialog(null, rebuda.getMessage());
                            //this.view.getCampNom().setText(rebuda.getMissatge());
                            this.view.getCampNom().setSelectionStart(0);
                            this.view.getCampNom().setSelectionEnd(this.view.getCampNom().getText().length());
                            this.view.getCampNom().requestFocus();

                            break;
                    }


            }
        }
    }

}
