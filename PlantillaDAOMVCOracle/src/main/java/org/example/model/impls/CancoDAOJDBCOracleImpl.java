package org.example.model.impls;

import org.example.model.daos.DAO;
import org.example.model.entities.Canço;
import org.example.model.exceptions.DAOException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

public class CancoDAOJDBCOracleImpl implements DAO<Canço> {
    /**
     * Implementació de la interfície DAO per a la classe Cançó
     */


    @Override
    public Canço get(Long id) throws DAOException {
        /**
         * Mètode per a obtenir una cançó de la base de dades
         * @param id Identificador de la cançó
         * @return Cançó amb l'identificador especificat
         * @throws DAOException Si es produeix un error en la consulta
         */

        //Declaració de variables del mètode
        Connection con = null;
        Statement st = null;
        ResultSet rs = null;
        Canço obra = null;

        //Accés a la BD usant l'API JDBC
        try {

            con = DriverManager.getConnection(
                    "jdbc:oracle:thin:@//localhost:1521/xe",
                    "C##HR",
                    "HR"
            );
            st = con.createStatement();

            rs = st.executeQuery("SELECT * FROM CANÇO");

            if (rs.next()) {
                obra = new Canço(Long.valueOf(rs.getString(1)), rs.getString(2));
            }
        } catch (SQLException throwables) {
            throw new DAOException(1);
        } finally {
            try {
                if (rs != null) rs.close();
                if (st != null) st.close();
                if (con != null) con.close();
            } catch (SQLException e) {
                throw new DAOException(1);
            }

        }
        return obra;
    }

    @Override
    public List<Canço> getAll() throws DAOException {
        /**
         * Mètode per a obtenir totes les cançons de la base de dades
         * @return Llista de totes les cançons de la base de dades
         * @throws DAOException Si es produeix un error en la consulta
         */
        //Declaració de variables del mètode
        List<Canço> obras = new ArrayList<>();

        //Accés a la BD usant l'API JDBC
        try (Connection con = DriverManager.getConnection(
                "jdbc:oracle:thin:@//localhost:1521/xe",
                "C##HR",
                "HR"
        );
             PreparedStatement st = con.prepareStatement("SELECT * FROM CANÇO");
             ResultSet rs = st.executeQuery();
        ) {

            while (rs.next()) {
                obras.add(new Canço(rs.getLong("id"), rs.getString("nom"), rs.getDouble("durada"),
                        new TreeSet<Canço.Videoclip>()));
            }
        } catch (SQLException throwables) {
            int tipoError = throwables.getErrorCode();
            //System.out.println(tipoError+" "+throwables.getMessage());
            switch (throwables.getErrorCode()) {
                case 17002: //l'he obtingut posant un sout en el throwables.getErrorCode()
                    tipoError = 0;
                    break;
                default:
                    tipoError = 1;  //error desconegut
            }
            throw new DAOException(tipoError);
        }


        return obras;
    }

    public Long getLastId() throws DAOException {
        /**
         * Mètode per a obtenir l'últim ID de la taula CANÇO
         * @return Últim ID de la taula CANÇO
         * @throws DAOException Si es produeix un error en la consulta
         */
        Connection con = null;
        Statement st = null;
        ResultSet rs = null;
        Long lastId = null;

        try {
            con = DriverManager.getConnection(
                    "jdbc:oracle:thin:@//localhost:1521/xe",
                    "C##HR",
                    "HR"
            );
            st = con.createStatement();
            rs = st.executeQuery("SELECT MAX(id) AS MAX_ID FROM CANÇO");

            if (rs.next()) {
                lastId = rs.getLong("MAX_ID");
            }
        } catch (SQLException throwables) {
            throw new DAOException(1);
        } finally {
            try {
                if (rs != null) rs.close();
                if (st != null) st.close();
                if (con != null) con.close();
            } catch (SQLException e) {
                throw new DAOException(1);
            }
        }
        return lastId;
    }

    @Override
    public void save(Canço obj) throws DAOException {
        /**
         * Mètode per a inserir una nova cançó a la base de dades
         * @param obj Cançó a inserir
         * @throws DAOException Si es produeix un error en la inserció
         */
        //Declaració de variables del mètode
        Connection con = null;
        PreparedStatement st = null;

        //obj no null
        if (obj == null) throw new DAOException(1);

        //Nova ID si es null
        if (obj.getId() == null) {
            Long lastId = getLastId();
            obj.setId(lastId != null ? lastId + 1 : 1);
        }

        System.out.println("ID: " + obj.getId());
        System.out.println("Nom: " + obj.getNom());
        System.out.println("Durada: " + obj.getDurada());

        //Accés a la BD usant l'API JDBC
        try {
            con = DriverManager.getConnection(
                    "jdbc:oracle:thin:@//localhost:1521/xe",
                    "C##HR",
                    "HR"
            );
            System.out.println("Conexió realitzada " + (con != null));
            st = con.prepareStatement("INSERT INTO CANÇO(ID, NOM, DURADA) VALUES(?,?,?)");
            st.setLong(1, obj.getId());
            st.setString(2, obj.getNom());
            st.setDouble(3, obj.getDurada());
            st.executeUpdate();
        } catch (SQLException throwables) {
            throw new DAOException(1);
        } finally {
            try {
                if (st != null) st.close();
                if (con != null) con.close();
            } catch (SQLException e) {
                throw new DAOException(1);
            }
        }
    }

    @Override
    public void update(Canço obj) throws DAOException {
        /**
         * Actualitza una cançó a la base de dades
         * @param obj Cançó a actualitzar
         * @throws DAOException Si es produeix un error en l'actualització
         */
        //Declaració de variables del mètode
        Connection con = null;
        PreparedStatement st = null;

        //obj no null
        if (obj == null) throw new DAOException(36);

        //Accés a la BD usant l'API JDBC
        try {
            con = DriverManager.getConnection(
                    "jdbc:oracle:thin:@//localhost:1521/xe",
                    "C##HR",
                    "HR"
            );
            System.out.println("Conexió realitzada " + (con != null));
            st = con.prepareStatement("UPDATE CANÇO SET NOM=?, DURADA=? WHERE ID=?");
            st.setString(1, obj.getNom());
            st.setDouble(2, obj.getDurada());
            st.setLong(3, obj.getId());

            System.out.println("Actualitzant la cançó amb ID: " + obj.getId());
            int actualitzat = st.executeUpdate();
            System.out.println("Cançó actualitzada: " + actualitzat);


        } catch (SQLException throwables) {
            System.out.println("Exepció SQL: " + throwables.getMessage());
            throw new DAOException(1);
        } finally {
            try {
                if (st != null) st.close();
                if (con != null) con.close();
            } catch (SQLException e) {
                System.out.println("Exepció SQL: " + e.getMessage());
                throw new DAOException(1);
            }
        }

    }

    @Override
    public void delete(Long id) throws DAOException {
        /**
         * Elimina una cançó de la base de dades
         * @param id Identificador de la cançó a eliminar
         * @throws DAOException Si es produeix un error en l'eliminació
         */
        //Declaració de variables del mètode
        Connection con = null;
        PreparedStatement st = null;

        //Accés a la BD usant l'API JDBC
        try {
            con = DriverManager.getConnection(
                    "jdbc:oracle:thin:@//localhost:1521/xe",
                    "C##HR",
                    "HR"
            );

            st = con.prepareStatement("DELETE FROM CANÇO WHERE ID=?");
            st.setLong(1, id);
            st.executeUpdate();
           System.out.println("Esborrant la cançó amb ID: " + id);
            int eliminat = st.executeUpdate();
            System.out.println("Cançó eliminada.");
        } catch (SQLException throwables) {
            throw new DAOException(1);
        } finally {
            try {
                if (st != null) st.close();
                if (con != null) con.close();
            } catch (SQLException e) {
                throw new DAOException(1);
            }

        }
    }
}




