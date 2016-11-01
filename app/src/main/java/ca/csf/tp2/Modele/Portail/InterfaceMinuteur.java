package ca.csf.tp2.Modele.Portail;

/**
 * Created by Utilisateur on 2016-03-18.
 */
public interface InterfaceMinuteur {

    /**
     * Quand un étudiant est trouvé, crée un nouveau timer pour un autre étudiant et retourne le temps restant pour
     * trouver l'ancien étudiant pour servire de pointage
     *
     * @return le temps restant pour trovuer l'ancien étudiant
     */
    long quandEtudiantTrouvee();

    long[] mettreLesMinuteursEnPause();

    void repartirLesMinuteurs(long[] tempsRestant);

    long[] obtenirTempsRestantPourLesMinuteurs();

}
