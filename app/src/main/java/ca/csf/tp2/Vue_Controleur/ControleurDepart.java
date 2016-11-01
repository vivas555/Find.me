package ca.csf.tp2.Vue_Controleur;

import java.util.ArrayList;

import ca.csf.tp2.Modele.Etudiant;
import ca.csf.tp2.Modele.FindMePartie;


public class ControleurDepart {

    private FindMePartie partie;

    public ControleurDepart() {

        partie = new FindMePartie(null, null);
    }

    public void restorerEtudiantsPartie(ArrayList<Etudiant> etudiants) {
        partie.restorerEtudiants(etudiants);
    }

    public boolean enleverEtudiantParCode(String code) {
        return partie.enleverEtudiantParCode(code);
    }


}
