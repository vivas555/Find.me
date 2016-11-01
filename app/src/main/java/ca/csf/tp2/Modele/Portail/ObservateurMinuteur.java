package ca.csf.tp2.Modele.Portail;

/**
 * Created by Utilisateur on 2016-03-18.
 */
public interface ObservateurMinuteur {
    /**
     * Envoie le message que le temps pour trouver un étudiant est expiré et envoie un nouvel étudiant
     */
    void notifierTempsTrouverEtudiantExpire();
    /**
     * Envoie le message que el temps pour la partie est finie et envoie le pointage.
     */
    void notifierPartieTerminee();

    /**
     * Envoie à la vue le temps formaté en String sous le format MM:SS indiquant le temps restant pour trouver un joueur
     */
    void notifierChangementAuTempsRestantPourJoueur(long tempsRestant);

    /**
     * Envoie à la vue le temps formaté en String sous le format MM:SS indiquant le temps restant pour trouver un joueur
     */
    void notifierChangementAuTempsRestantPourPartieTotale(long tempsRestant);
}
