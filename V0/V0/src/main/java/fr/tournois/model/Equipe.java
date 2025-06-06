package fr.tournois.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Représente une équipe participant à un tournoi.
 * Une équipe est composée d'un nom, d'une date de création et d'une liste de joueurs.
 */
public class Equipe {
    private Integer id;
    private String nom;
    private LocalDate dateCreation;
    private List<Joueur> joueurs;

    public Equipe() {
        this.joueurs = new ArrayList<>();
    }

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

    public LocalDate getDateCreation() {
        return dateCreation;
    }

    public void setDateCreation(LocalDate dateCreation) {
        this.dateCreation = dateCreation;
    }

    public List<Joueur> getJoueurs() {
        return joueurs;
    }

    public void setJoueurs(List<Joueur> joueurs) {
        this.joueurs = joueurs;
    }

    public void addJoueur(Joueur joueur) {
        if (!this.joueurs.contains(joueur)) {
            this.joueurs.add(joueur);
        }
    }

    public void removeJoueur(Joueur joueur) {
        this.joueurs.remove(joueur);
    }
}