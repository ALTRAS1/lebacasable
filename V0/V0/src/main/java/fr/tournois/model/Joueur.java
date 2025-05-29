package fr.tournois.model;

/**
 * Représente un joueur participant à un tournoi.
 * Un joueur est identifié par son nom, prénom et pseudo.
 */
public class Joueur {
    private Integer id;
    private String nom;
    private String prenom;
    private String pseudo;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getPseudo() {
        return pseudo;
    }

    public void setPseudo(String pseudo) {
        this.pseudo = pseudo;
    }
}