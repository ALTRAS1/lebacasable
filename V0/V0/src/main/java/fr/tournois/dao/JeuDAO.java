package fr.tournois.dao;

import fr.tournois.model.Jeu;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO pour la gestion des jeux
 */
public class JeuDAO {

    private final Connection connection;

    public JeuDAO(Connection connection) {
        this.connection = connection;
    }

    public List<Jeu> findAll() throws DAOException {
        List<Jeu> jeux = new ArrayList<>();
        
        try (Statement st = connection.createStatement();
             ResultSet rs = st.executeQuery("SELECT * FROM Jeu")) {
            
            while (rs.next()) {
                jeux.add(mapResultSetToEntity(rs));
            }
        } catch (SQLException e) {
            throw new DAOException("Erreur lors de la récupération de la liste des jeux: " + e.getMessage());
        }
        
        return jeux;
    }

    private Jeu mapResultSetToEntity(ResultSet rs) throws SQLException {
        Jeu jeu = new Jeu();
        jeu.setId(rs.getInt("id_jeu"));
        jeu.setNom(rs.getString("nom"));
        jeu.setEditeur(rs.getString("editeur"));
        jeu.setAnneeSortie(rs.getInt("annee_sortie"));
        jeu.setGenre(rs.getString("genre"));
        jeu.setDescription(rs.getString("description"));
        return jeu;
    }
}
