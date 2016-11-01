package ca.csf.tp2.Modele;

import junit.framework.Assert;
import junit.framework.TestCase;

import java.util.ArrayList;

import ca.csf.tp2.Modele.Portail.InterfaceMinuteur;
import ca.csf.tp2.Vue_Controleur.Portail.ObservateurFindMePartie;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.timeout;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @author Felix
 * @author Alicia
 */
public class FindMePartieTest extends TestCase {
    private ArrayList<Etudiant> etudiants = new ArrayList<>();
    private long pointage = 0;
    private ObservateurFindMePartie observateurFindMePartie = null;
    private InterfaceMinuteur interfacerMinuteur = null;
    private FindMePartie findMePartie = null;


    @Override
    protected void setUp() throws Exception {
        super.setUp();
        initialiserTest();
    }

    void initialiserTest() {
        etudiants.add(new Etudiant("A", "123456789000"));
        etudiants.add(new Etudiant("B", "223456789123"));
        etudiants.add(new Etudiant("C", "345678901234"));
        observateurFindMePartie = mock(ObservateurFindMePartie.class);
        interfacerMinuteur = mock(InterfaceMinuteur.class);
        findMePartie = new FindMePartie(etudiants, observateurFindMePartie, interfacerMinuteur);
        when(interfacerMinuteur.quandEtudiantTrouvee()).thenReturn((long) 100);
        long tempsTimers[] = new long[2];
        when(interfacerMinuteur.mettreLesMinuteursEnPause()).thenReturn(tempsTimers);

    }


    public void testGetEtudiantParCodeRetraitEtudiant() throws Exception {
        findMePartie.getEtudiantParCode("123456789000");
        assertEquals("B", etudiants.get(0).getNom());
    }


    public void testGetEtudiantParCodeIncrementerPointage() throws Exception {
        findMePartie.getEtudiantParCode("123456789000");
        assertEquals(100, findMePartie.getPointage());
    }


    public void testGetEtudiantParCodeMauvaisEtudiant() throws Exception {
        findMePartie.getEtudiantParCode("Badger");
        verify(observateurFindMePartie, times(1)).notifierChangementEtudiantATrouver(null);
    }


    public void testGetProchainEtudiantListeNonVide() throws Exception {
        Assert.assertEquals(etudiants.get(0), findMePartie.getProchainEtudiant());
        etudiants.remove(0);
        Assert.assertEquals(etudiants.get(0), findMePartie.getProchainEtudiant());
    }


    public void testGetProchainEtudiantListeVide() throws Exception {
        etudiants.clear();
        Assert.assertNull(findMePartie.getProchainEtudiant());

    }

    public void testNotifierTempsTrouverEtudiantExpireRetraitEtudiant() throws Exception {
        findMePartie.notifierTempsTrouverEtudiantExpire();
        Assert.assertEquals("B", etudiants.get(0).getNom());
    }

    public void testNotifierTempsTrouverEtudiantExpireAppelMethodeNotifier() throws Exception {
        findMePartie.notifierTempsTrouverEtudiantExpire();
        verify(observateurFindMePartie, times(1)).notifierTempsEcoulePourTrouverEtudiant("B");
    }

    public void testNotifierPartieTerminee() throws Exception {
        findMePartie.notifierPartieTerminee();
        verify(observateurFindMePartie, times(1)).notifierTempsPourLaPartieFinie(0);
    }

    public void testSetEtudiantATrouverEtudiantExistant() {
        assertTrue(findMePartie.setEtudiantATrouver(etudiants.get(0)));
        assertEquals(etudiants.get(0), findMePartie.getProchainEtudiant());
    }

    public void testSetEtudiantATrouverEtudiantInexistant() {
        Etudiant etudiant = new Etudiant("D", "012345567789");
        assertFalse(findMePartie.setEtudiantATrouver(etudiant));
        assertEquals(etudiants.get(0), findMePartie.getProchainEtudiant());
    }

    public void testRestorerEtudiantListeNonVide() {
        ArrayList<Etudiant> listeEtudiants = new ArrayList<>();
        Etudiant etudiant = new Etudiant("D", "012345567789");
        findMePartie.restorerEtudiants(listeEtudiants);
        assertEquals(listeEtudiants, findMePartie.getListeEtudiants());
    }

    public void testRestorerEtudiantListeVide() {
        ArrayList<Etudiant> listeEtudiants = new ArrayList<>();
        findMePartie.restorerEtudiants(listeEtudiants);
        assertEquals(listeEtudiants, findMePartie.getListeEtudiants());
    }


    public void testRestorerEtudiantListeNulle() {
        ArrayList<Etudiant> listeEtudiants = null;
        findMePartie.restorerEtudiants(listeEtudiants);
        assertEquals(listeEtudiants, findMePartie.getListeEtudiants());
    }

    public void testEnleverEtudiantParCodeEtudiantExistant() {
        assertTrue(findMePartie.enleverEtudiantParCode(etudiants.get(0).getCode()));
    }

    public void testEnleverEtudiantParCodeEtudiantInexistant() {
        assertFalse(findMePartie.enleverEtudiantParCode("888888888888"));
    }

    public void testEnleverEtudiantParCodeEtudiantNul() {
        assertFalse(findMePartie.enleverEtudiantParCode(null));
    }

    public void testEnleverEtudiantParCodeListeVide() {
        assertTrue(findMePartie.enleverEtudiantParCode(etudiants.get(0).getCode()));
        assertTrue(findMePartie.enleverEtudiantParCode(etudiants.get(0).getCode()));
        assertTrue(findMePartie.enleverEtudiantParCode(etudiants.get(0).getCode()));
        assertFalse(findMePartie.enleverEtudiantParCode("123456789000"));
    }

    public void testConstructeurListeEstMélangée() {
        ArrayList<Etudiant> listeOriginale = new ArrayList<>(etudiants);
        FindMePartie partie = new FindMePartie(etudiants, observateurFindMePartie);
        assertFalse(partie.getListeEtudiants().equals(listeOriginale));
    }

    public void testSetPointagePositif() {
        assertTrue(findMePartie.getPointage() == 0);
        findMePartie.setPointage(43221);
        assertTrue(findMePartie.getPointage() == 43221);
    }

    public void testSetPointageNegatif() {
        assertTrue(findMePartie.getPointage() == 0);
        findMePartie.setPointage(-43221);
        assertTrue(findMePartie.getPointage() == 0);
    }

    public void testRepartirTempsValide() {
        long temps[] = new long[2];
        temps[0] = 123145;
        temps[1] = 1234325544;
        assertTrue(findMePartie.repartirTemps(temps));
    }

    public void testRepartirTempsNulle() {
        assertFalse(findMePartie.repartirTemps(null));
    }

    public void testRepartirTempsMauvaiseTailleTableau() {
        long temps[] = new long[3];
        temps[0] = 123145;
        temps[1] = 1234325544;
        temps[2] = 442363;
        assertFalse(findMePartie.repartirTemps(temps));
    }

    public void testPauserTempsRetourneTableauDeTaille2() {
        long tableauTest[] = findMePartie.pauserTemps();

        assertEquals(2, tableauTest.length);
        assertNotNull(tableauTest[0]);
        assertNotNull(tableauTest[1]);
    }

    public void testNotifierChangementAuTempsRestantPourJoueurMethodeAppelee() {
        findMePartie.notifierChangementAuTempsRestantPourJoueur(2000);
        verify(observateurFindMePartie, times(1)).notifierDiminutionDuTempsPourTrouverUnEtudiant(2000);
    }

    public void testNotifierChangementAuTempsRestantPourPartieTotalee() {
        findMePartie.notifierChangementAuTempsRestantPourPartieTotale(-2000);
        verify(observateurFindMePartie, times(1)).notifierDIminutionDuTempsPourLaPArtieTotale(-2000);
    }

    public void testReinitialiserInterfaceMinuteur() {
        assertEquals(interfacerMinuteur, findMePartie.getInterfaceMinuteur());

        findMePartie.reinitialiserInterfaceMinuteur();
        assertFalse(interfacerMinuteur.equals(findMePartie.getInterfaceMinuteur()));
    }

    public void testNotifierTempsPourEtudiantExpireDernierEtudiantFinDePartieAppelee() {
        findMePartie.enleverEtudiantParCode("123456789000");
        findMePartie.enleverEtudiantParCode("223456789123");
        findMePartie.notifierTempsTrouverEtudiantExpire();

        verify(observateurFindMePartie, times(1)).notifierTempsPourLaPartieFinie(0);
    }


}