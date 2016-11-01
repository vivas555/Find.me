package ca.csf.tp2.Modele;

import android.support.annotation.NonNull;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import java.util.Timer;
import java.util.TimerTask;

import ca.csf.tp2.Modele.Portail.ObservateurMinuteur;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

/**
 * Created by Felix on 2016-04-08.
 */
public class MinuteurTest {

    private ObservateurMinuteur observateurMinuteur;
    private Minuteur minuteur;
    private Timer timer;
    private long[] liste;


    @Before
    public void setUp() throws Exception {
        liste = new long[2];
        liste[0] = 5;
        liste[1] = 1;
        observateurMinuteur = mock(ObservateurMinuteur.class);
        timer = mock(Timer.class);
        minuteur = new Minuteur(observateurMinuteur) {
            @NonNull
            @Override
            protected Timer getTimer() {
                return timer;
            }
        };

    }

    @Test
    public void peutRedemarerMinuteurLorsqueEtudiantTrouve() throws Exception {
        minuteur.quandEtudiantTrouvee();
        verify(timer).schedule(any(TimerTask.class), eq(Minuteur.DUREE_PARTIE));
        verify(timer, times(4)).schedule(any(TimerTask.class), eq((long) 1), eq((long) 1));
        verify(timer, times(2)).schedule(any(TimerTask.class), eq(Minuteur.DUREE_TROUVER_ETUDIANT), eq(Minuteur.DUREE_TROUVER_ETUDIANT));
        verify(timer, times(4)).schedule(any(TimerTask.class), eq((long) 1), eq((long) 1));
    }

    @Test
    public void retourneLeTempsRestantPourTrouverEtudiant() throws Exception {
        Assert.assertEquals(minuteur.quandEtudiantTrouvee(), Minuteur.DUREE_TROUVER_ETUDIANT);
    }

    @Test
    public void peutMettreLEsMinuteursEnPauseLorsDeLaFonctionReprise() throws Exception {
        minuteur.repartirLesMinuteurs(liste);
        verify(timer, times(2)).cancel();
    }

    @Test
    public void peutMettreLEsMinuteursEnPauseLorsDeLaFonctionPause() throws Exception {
        minuteur.mettreLesMinuteursEnPause();
        verify(timer, times(2)).cancel();
    }

    @Test
    public void neRedemarrePasLesMinuteursLorsDeLaFonctionPause() throws Exception {
        minuteur.repartirLesMinuteurs(liste);
        verify(timer, times(0)).schedule(any(TimerTask.class), eq(5));
        verify(timer, times(0)).schedule(any(TimerTask.class), eq(1));
    }

    @Test
    public void testObtenirTempsRestantPourLesMinuteurs() throws Exception {
        long[] resultats = minuteur.obtenirTempsRestantPourLesMinuteurs();
        Assert.assertEquals(resultats[0], Minuteur.DUREE_PARTIE);
        Assert.assertEquals(resultats[1], Minuteur.DUREE_TROUVER_ETUDIANT);

    }
}