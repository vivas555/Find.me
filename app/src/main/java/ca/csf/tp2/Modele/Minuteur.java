package ca.csf.tp2.Modele;

import android.support.annotation.NonNull;

import java.util.Timer;
import java.util.TimerTask;

import ca.csf.tp2.Modele.Portail.InterfaceMinuteur;
import ca.csf.tp2.Modele.Portail.ObservateurMinuteur;


/**
 * Classe pour faire le minuteur de la partie, contient deux timer Java. Un pour la partie , et un pour le joueur en cour
 *
 * @author Felix
 */
public class Minuteur implements InterfaceMinuteur {

    public static final long DUREE_PARTIE = 1200000;
    public static final long DUREE_TROUVER_ETUDIANT = 60000;
    private ObservateurMinuteur observateurMinuteur = null;
    private Timer minuteurPartie = null;
    private Timer minuteurEtudiant = null;
    private long tempsPourEtudiant = DUREE_TROUVER_ETUDIANT;
    private long tempsPourPartieTotale = DUREE_PARTIE;
    private int passagePourDebug = 0;

    /**
     * Constructeur de la classe minuteur, crée le minuteur global ainsi que la première instance du minuteur de joueur
     *
     * @param _observateurMinuteur La classe qui observe le déroulement du minuteur
     */
    public Minuteur(ObservateurMinuteur _observateurMinuteur) {
        this.observateurMinuteur = _observateurMinuteur;
        minuteurPartie = getTimer();
        creerMinuteurEtudiant();
        minuteurPartie.schedule(initialiserTacheMinuteurPartie(), DUREE_PARTIE, DUREE_PARTIE);
        minuteurPartie.schedule(initialiserTachePourAfficherTempsPartieTotale(), 1, 1);
    }


    /**
     * Quand un étudiant est trouvé, crée un nouveau timer pour un autre étudiant et retourne le temps restant pour
     * trouver l'ancien étudiant pour servire de pointage
     *
     * @return le temps restant pour trovuer l'ancien étudiant
     */
    @Override
    public long quandEtudiantTrouvee() {
        long aRetourner = tempsPourEtudiant;
        minuteurEtudiant.cancel();
        tempsPourEtudiant = DUREE_TROUVER_ETUDIANT;
        long[] tableauPourTemps = new long[2];
        tableauPourTemps[0] = tempsPourPartieTotale;
        tableauPourTemps[1] = tempsPourEtudiant;
        repartirLesMinuteurs(tableauPourTemps);
        return aRetourner;
    }

    @Override
    public long[] mettreLesMinuteursEnPause() {
        long[] aRetourner = new long[2];
        aRetourner[0] = tempsPourPartieTotale;
        aRetourner[1] = tempsPourEtudiant;
        minuteurEtudiant.cancel();
        minuteurPartie.cancel();

        return aRetourner;
    }

    @Override
    public void repartirLesMinuteurs(long[] tempsRestant) {

        if (tempsRestant.length == 2 || tempsRestant[0] > 0 || tempsRestant[1] > 0) {
            minuteurEtudiant.cancel();

            minuteurPartie.cancel();


            tempsPourPartieTotale = tempsRestant[0];
            tempsPourEtudiant = tempsRestant[1];

            minuteurPartie = getTimer();

            minuteurPartie.schedule(initialiserTacheMinuteurPartie(), tempsPourPartieTotale);
            minuteurPartie.schedule(initialiserTachePourAfficherTempsPartieTotale(), 1, 1);

            minuteurEtudiant = getTimer();

            minuteurEtudiant.schedule(initialiserTachePourAfficherTempsRestantEtudiantEtDecrementerScore(), 1, 1);
            minuteurEtudiant.schedule(initialiserTacheMinuteurJoueurTempsTotal(), tempsRestant[1], DUREE_TROUVER_ETUDIANT);
        }
    }

    @NonNull
    protected Timer getTimer() {
        return new Timer();
    }

    @Override
    public long[] obtenirTempsRestantPourLesMinuteurs() {
        long[] aRetourner = new long[2];
        aRetourner[0] = tempsPourPartieTotale;
        aRetourner[1] = tempsPourEtudiant;
        return aRetourner;
    }


    /**
     * Fonction appelée quand le temps alloué pour trouver un étudiant est fini.
     * Elle appele l'observateur du minuteur.
     */
    private void quandTempsPourTrouverEtudiantExpire() {
        passagePourDebug++;
        tempsPourEtudiant = DUREE_TROUVER_ETUDIANT;
        observateurMinuteur.notifierTempsTrouverEtudiantExpire();
    }


    /**
     * Crée la tâche du minuteur globale
     *
     * @return la tâche du minuteur globale
     */
    private TimerTask initialiserTacheMinuteurPartie() {
        TimerTask tacheMinuteurPartie = new TimerTask() {
            @Override
            public void run() {
                observateurMinuteur.notifierPartieTerminee();
            }
        };
        return tacheMinuteurPartie;
    }

    /**
     * Crée la tâche du minuteur du joueur servant à décrémenter le temps restant
     *
     * @return la tâche du minuteur du joueur servant à décrémenter le temps restant
     */
    private TimerTask initialiserTachePourAfficherTempsRestantEtudiantEtDecrementerScore() {
        TimerTask tacheMinuteurJoueurScore = new TimerTask() {
            @Override
            public void run() {
                tempsPourEtudiant--;
                observateurMinuteur.notifierChangementAuTempsRestantPourJoueur(tempsPourEtudiant);
            }
        };
        return tacheMinuteurJoueurScore;
    }

    /**
     * Crée la tâche du minuteur du joueur servant à dire quand la fin du temps pour ce joueur
     *
     * @return la tâche du minuteur du joueur servant à dire quand la fin du temps pour ce joueur
     */
    private TimerTask initialiserTacheMinuteurJoueurTempsTotal() {
        TimerTask tacheMinuteurJoueurTempsTotal = new TimerTask() {
            @Override
            public void run() {
                quandTempsPourTrouverEtudiantExpire();
            }
        };
        return tacheMinuteurJoueurTempsTotal;
    }

    /**
     * Décrémente d'une miliseconde le temps et l'envoie à l'affichage.
     *
     * @return La tâche initialisée à envoyé au timer
     */
    private TimerTask initialiserTachePourAfficherTempsPartieTotale() {
        TimerTask tacheMinuteurTempsTotalAfficher = new TimerTask() {
            @Override
            public void run() {
                tempsPourPartieTotale--;
                observateurMinuteur.notifierChangementAuTempsRestantPourPartieTotale(tempsPourPartieTotale);
            }
        };
        return tacheMinuteurTempsTotalAfficher;
    }


    /**
     * Crée le minuteur pour le joueur et lui assigne ses tâches.
     */
    private void creerMinuteurEtudiant() {

        minuteurEtudiant = getTimer();

        tempsPourEtudiant = DUREE_TROUVER_ETUDIANT;
        minuteurEtudiant.schedule(initialiserTachePourAfficherTempsRestantEtudiantEtDecrementerScore(), 1, 1);
        minuteurEtudiant.schedule(initialiserTacheMinuteurJoueurTempsTotal(), DUREE_TROUVER_ETUDIANT, DUREE_TROUVER_ETUDIANT);
    }

}
