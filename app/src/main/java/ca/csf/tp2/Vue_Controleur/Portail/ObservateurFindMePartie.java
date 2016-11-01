package ca.csf.tp2.Vue_Controleur.Portail;

import ca.csf.tp2.Modele.Etudiant;

/**
 * Created by Utilisateur on 2016-03-14.
 */

/**
 * Permet aux activités d'avoir accèsà une partie des actions de la classe FindMePartie
 * @author Felix
 */
public interface ObservateurFindMePartie {
    /**
     *Averti que l'étudiant à trouver à changé
     */
    void notifierChangementEtudiantATrouver(Etudiant etudiant);

    /**
     *Le temps pour trouver l'étudiant est écoulé, envoie donc un autre étudiant
     *
     * @param nomEtudiant Nom du nouvel étudiant à trouver
     */
    void notifierTempsEcoulePourTrouverEtudiant(String nomEtudiant);

    /**
     * Le temps pour la partie est finie, averti l'activité et envoie le pointage du joueur
     *
     * @param pointage le pointage du joueur
     */
    void notifierTempsPourLaPartieFinie(long pointage);



    /**
     * Formate le temps restant en millisecondes pour trouver l'étudiant recherché en temps affichable dans la vue
     *
     * @param tempsRestantEnMillisecondes Le temps restant pour trouver le joueur
     */
    void notifierDiminutionDuTempsPourTrouverUnEtudiant(long tempsRestantEnMillisecondes);

    /**
     * Formate le temps restant en millisecondes pour trouver l'étudiant recherché en temps affichable dans la vue
     *
     * @param tempsRestantEnMillisecondes Le temps restant pour trouver le joueur
     */
    void notifierDIminutionDuTempsPourLaPArtieTotale(long tempsRestantEnMillisecondes);

}
