package ca.csf.tp2.Vue_Controleur;

import java.util.ArrayList;

import ca.csf.tp2.Modele.Etudiant;
import ca.csf.tp2.Modele.FindMePartie;
import ca.csf.tp2.Modele.Portail.InterfaceMinuteur;
import ca.csf.tp2.Vue_Controleur.Portail.ObservateurFindMePartie;

public class ControleurRecherche {

    private FindMePartie partie;

    public ControleurRecherche(ArrayList<Etudiant> etudiants,
                               ObservateurFindMePartie observateurFindMePartie) {

        partie = new FindMePartie(etudiants, observateurFindMePartie);
        partie.reinitialiserInterfaceMinuteur();
    }

    public Etudiant getProchainEtudiant() {
        return partie.getProchainEtudiant();
    }

    public long[] pause() {
        return partie.pauserTemps();
    }

    public void recommencer(long[] tempsRestants) {
        partie.repartirTemps(tempsRestants);
    }

    public ArrayList<Etudiant> getTousLesEtudiants() {
        return partie.getListeEtudiants();
    }

    public long getPointage() {
        return partie.getPointage();
    }

    public void setPointage(long pointage) {
        partie.setPointage(pointage);
    }

    public void restorerListeEtudiants(ArrayList<Etudiant> etudiants) {
        partie.restorerEtudiants(etudiants);
    }

    public void setEtudiantATrouver(Etudiant etudiantATrouver) {
        partie.setEtudiantATrouver(etudiantATrouver);
    }

    public void getEtudiantParCode(String code) {
        partie.getEtudiantParCode(code);
    }
}
