package fr.tournois.ui.controller;

import fr.tournois.dao.StaffDAO;
import fr.tournois.dao.TournoiDAO;
import fr.tournois.dao.UtilisateurDAO;
import fr.tournois.dao.AffectationDAO;
import fr.tournois.security.SecurityContext;
import fr.tournois.ui.util.DialogUtils;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.sql.Connection;

/**
 * Contrôleur principal de l'application.
 * Gère la fenêtre principale et la navigation entre les différentes fonctionnalités.
 */
public class AppMainFrameController {
    @FXML
    private MenuBar menuBar;
    @FXML
    private MenuItem menuDeconnexion;
    @FXML
    private MenuItem menuQuitter;
    @FXML
    private MenuItem menuGestionTournois;
    @FXML
    private MenuItem menuStaff;
    @FXML
    private MenuItem menuConnexion;
    @FXML
    private Menu menuCompte;
    @FXML
    private Label labelUtilisateur;
    @FXML
    private StackPane mainContentPane;
    @FXML
    private MenuItem menuGestionJeux;
    @FXML
    private MenuItem menuUtilisateurs;

    private TournoisManagementController tournoisManagementController;
    private StaffManagementController staffManagementController;
    private UtilisateursManagementController utilisateursManagementController;

    private Connection connection;

    /**
     * Définit la connexion à la base de données et met à jour l'interface
     * @param connection la connexion à utiliser
     */
    public void setConnection(Connection connection) {
        this.connection = connection;
        // Mise à jour des menus car nouvelle connexion
        this.updateMenuCompte();
    }

    /**
     * Met à jour l'affichage du menu Compte en fonction de l'état de connexion
     */
    private void updateMenuCompte() {
        SecurityContext securityContext = SecurityContext.getInstance();
        boolean connecte = securityContext.isAuthenticated();
        menuConnexion.setVisible(!connecte);
        menuDeconnexion.setVisible(connecte);
        if (labelUtilisateur != null) {
            if (connecte) {
                var u = securityContext.getCurrentUser();
                String nomComplet = u.getPseudo();
                labelUtilisateur.setText("Connecté : " + nomComplet + " (" + u.getRole().name() + ")");
            } else {
                labelUtilisateur.setText("Non connecté");
            }
        }
        updateMenuAccess();
    }

    /**
     * Met à jour l'accès aux menus en fonction du rôle de l'utilisateur.
     * - Non connecté : seuls les menus Fichier (Quitter) et Compte (Connexion) sont accessibles
     * - ADMIN : accès complet à toutes les fonctionnalités
     * - ORGANISATEUR : accès restreint (pas de gestion des utilisateurs ni des joueurs)
     */
    private void updateMenuAccess() {
        SecurityContext securityContext = SecurityContext.getInstance();
        boolean isAuthenticated = securityContext.isAuthenticated();
        boolean isAdmin = securityContext.isAdmin();
        boolean isOrganisateur = securityContext.isOrganisateur();

        // D'abord, désactiver tous les menus sauf les menus de base
        menuQuitter.setDisable(false);
        menuConnexion.setDisable(false);
        menuDeconnexion.setDisable(false);
        menuGestionTournois.setDisable(true);
        menuGestionJeux.setDisable(true);
        menuStaff.setDisable(true);
        menuUtilisateurs.setDisable(true);

        // Si non connecté, on s'arrête là
        if (!isAuthenticated) {
            return;
        }

        // Si connecté, activer les menus de base
        menuGestionTournois.setDisable(false);
        menuGestionJeux.setDisable(false);
        menuStaff.setDisable(false);

        // Si ORGANISATEUR, ajouter ses droits spécifiques
        if (isOrganisateur) {
        }

        // Si ADMIN, accès complet
        if (isAdmin) {
            menuUtilisateurs.setDisable(false);

        }
    }

    /**
     * Affiche la fenêtre de connexion
     */
    @FXML
    private void doConnexion() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fr/tournois/ui/fxml/Login.fxml"));
            GridPane loginPane = loader.load();
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Connexion");
            dialogStage.initOwner(menuBar.getScene().getWindow());
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.setResizable(false);
            Scene scene = new Scene(loginPane);
            scene.getStylesheets().add(getClass().getResource("/fr/tournois/ui/css/style.css").toExternalForm());
            dialogStage.setScene(scene);
            DialogUtils.centerDialog(dialogStage, (Stage) menuBar.getScene().getWindow());
            LoginController controller = loader.getController();
            controller.setUtilisateurDAO(new UtilisateurDAO(connection));
            controller.setDialogStage(dialogStage);
            dialogStage.showAndWait();
            updateMenuCompte();
        } catch (Exception e) {
            showError("Erreur", "Impossible d'ouvrir la fenêtre de connexion :\n" + e.getMessage());
        }
    }

    /**
     * Déconnecte l'utilisateur et réinitialise l'interface
     */
    @FXML
    private void doDeconnexion() {
        SecurityContext.getInstance().logout();
        updateMenuCompte();
        mainContentPane.getChildren().clear();
    }

    /**
     * Ferme l'application
     */
    @FXML
    private void doQuitter() {
        Stage stage = (Stage) menuBar.getScene().getWindow();
        stage.close();
    }

    /**
     * Affiche l'interface de gestion des tournois
     */
    @FXML
    private void doAfficherGestionTournois() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fr/tournois/ui/fxml/TournoisManagement.fxml"));
            BorderPane tournoisManagement = loader.load();
            mainContentPane.getChildren().setAll(tournoisManagement);
            tournoisManagementController = loader.getController();
            tournoisManagementController.setDAOs(new TournoiDAO(connection), new StaffDAO(connection));
            tournoisManagementController.setParentStage((Stage) mainContentPane.getScene().getWindow());
        } catch (Exception e) {
            showError("Erreur", "Impossible d'ouvrir la gestion des tournois :\n" + e.getMessage());
        }
    }

    /**
     * Affiche l'interface de gestion des jeux
     */
    @FXML
    private void doAfficherGestionJeux() {
        afficherADevelopper("Gestion des jeux");
    }


    /**
     * Affiche l'interface de gestion du staff
     */
    @FXML
    private void doAfficherStaff() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fr/tournois/ui/fxml/StaffManagement.fxml"));
            BorderPane staffManagement = loader.load();
            mainContentPane.getChildren().setAll(staffManagement);
            staffManagementController = loader.getController();
            staffManagementController.setDAOs(
                new StaffDAO(connection),
                new UtilisateurDAO(connection),
                new AffectationDAO(connection)
            );
        } catch (Exception e) {
            showError("Erreur", "Impossible d'ouvrir la gestion du staff :\n" + e.getMessage());
        }
    }

    /**
     * Affiche l'interface de gestion des utilisateurs
     */
    @FXML
    private void doAfficherUtilisateurs() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fr/tournois/ui/fxml/UtilisateursManagement.fxml"));
            BorderPane utilisateursManagement = loader.load();
            mainContentPane.getChildren().setAll(utilisateursManagement);
            utilisateursManagementController = loader.getController();
            utilisateursManagementController.setUtilisateurDAO(new UtilisateurDAO(connection));
            utilisateursManagementController.setParentStage((Stage) mainContentPane.getScene().getWindow());
        } catch (Exception e) {
            showError("Erreur", "Impossible d'ouvrir la gestion des utilisateurs :\n" + e.getMessage());
        }
    }

    /**
     * Affiche la fenêtre "À propos"
     */
    @FXML
    private void doAfficherAPropos() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("À propos de LiveTournois");
        alert.setHeaderText("LiveTournois - Version 1.01");
        alert.setContentText("Auteurs : F. Pelleau - A. Péninou");
        
        DialogUtils.centerDialog((Stage) alert.getDialogPane().getScene().getWindow(), (Stage) mainContentPane.getScene().getWindow());
        
        alert.showAndWait();
    }

    /**
     * Affiche une boîte de dialogue d'erreur
     * @param titre titre de la fenêtre d'erreur
     * @param message message d'erreur à afficher
     */
    private void showError(String titre, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(titre);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * Affiche une fenêtre temporaire
     * @param stage la fenêtre à afficher
     */
    public void displayTemporaire(Stage stage) {
        this.updateMenuCompte();
        stage.show();
    }

    /**
     * Affiche un message indiquant qu'une fonctionnalité est en cours de développement
     * @param fonctionnalite nom de la fonctionnalité à développer
     */
    private void afficherADevelopper(String fonctionnalite) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fr/tournois/ui/fxml/ADevelopper.fxml"));
            VBox root = loader.load();
            ADevelopperController controller = loader.getController();
            controller.setMessage(fonctionnalite);
            mainContentPane.getChildren().setAll(root);
        } catch (Exception e) {
            showError("Erreur", "Impossible d'afficher le message 'à développer' : " + e.getMessage());
        }
    }
}
