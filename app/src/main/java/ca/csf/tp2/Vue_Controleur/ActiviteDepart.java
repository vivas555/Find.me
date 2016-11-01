
package ca.csf.tp2.Vue_Controleur;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import ca.csf.tp2.Modele.Etudiant;
import ca.csf.tp2.Modele.TacheTelechargerListeEtudiant;
import ca.csf.tp2.R;

/**
 * La première activité que l'utilisateur voit. Elle accueille le joueur et lui demande
 * de scanner son propore code barre.
 *
 * @author Alicia Lamontagne
 */
public class ActiviteDepart extends AppCompatActivity implements TacheTelechargerListeEtudiant.Callback {
    /**
     * Le bouton dans la vue activity_debut permettant au joueur d'ouvrir une application
     * pour scanner un code barre
     */
    private Button scanButton;

    TextView texteBienvenue;

    private ProgressBar barreProgression;

    //private PortailPartie partie;
    private ControleurDepart controleurDepart;

    /**
     * Le code qui sert à s'assurer que le résultat de l'activité de scan
     * est celui auquel on s'attend
     */
    public static final int CODE_REQUETE = 42;
    /**
     * Le numéro du code barre de l'étudiant qui utilise l'application
     */
    private String codeEtudiant;
    /**
     * Constante qui permet d'identifier la liste d'étudiants à trouver quand on
     * la passe en extra à un intent
     */
    public static final String ETUDIANTS_ACTUELS = "ETUDIANTS_ACTUELS";
    /**
     * La liste complète de tous les étudiants à trouver
     */
    private ArrayList<Etudiant> etudiantsRestants;


    /**
     * Crée la vue et assigne le bouton scan à un attribut, en plus de
     * restorer la liste d'étudiants si le paramètre savedInstanceState n'est pas null
     *
     * @param savedInstanceState
     * @see ActiviteDepart#etudiantsRestants
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_debut);

        scanButton = (Button) findViewById(R.id.buttonScan);
        scanButton.setOnClickListener(clickScan);
        codeEtudiant = "";

        barreProgression = (ProgressBar) findViewById(R.id.barreProgression);
        barreProgression.setVisibility(View.INVISIBLE);

        texteBienvenue = (TextView) findViewById(R.id.textBienvenue);

        // Réassignation de la liste d'étudiants lorsque l'on revient de
        // l'application de scan
        if (savedInstanceState != null) {
            etudiantsRestants = savedInstanceState.getParcelableArrayList(ETUDIANTS_ACTUELS);
        }

        controleurDepart = new ControleurDepart();

    }

    /**
     * Initialise le contrôleur et restore la liste d'étudiants dans le
     * modèle de données
     */
    @Override
    protected void onStart() {
        super.onStart();


        if (etudiantsRestants == null) {
            TacheTelechargerListeEtudiant telechargerListeEtudiant = new TacheTelechargerListeEtudiant(this);
            telechargerListeEtudiant.execute("https://findme-acodebreak.rhcloud.com/students.json");
        }

    }

    @Override
    protected void onResume() {
        super.onResume();

        // Si notre liste comprend déjà des étudiants, on la réassigne dans le
        // modèle de données via le contrôleur
        if (etudiantsRestants != null)
            controleurDepart.restorerEtudiantsPartie(etudiantsRestants);
        else {
            barreProgression.setVisibility(View.VISIBLE);
            scanButton.setVisibility(View.INVISIBLE);
        }
    }

    /**
     * Méthode appelée lorsqu'il a un clic sur le bouton Scan. Elle s'occupe
     * de lancer une nouvelle activité pour scanner un code barre provenant
     * d'une application tierce.
     *
     * @see ActiviteDepart#onActivityResult(int, int, Intent)
     */
    private View.OnClickListener clickScan = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            // Utiliser la caméra pour scanner un code barre

            Intent barCodeIntent = new Intent("com.google.zxing.client.android.SCAN");
            barCodeIntent.putExtra("SCAN_FORMATS", "CODE_128");
            startActivityForResult(barCodeIntent, CODE_REQUETE);
        }
    };

    /**
     * Vérifie, lors du retour de l'application de scan, si le code barre scanné
     * correspond à une élève de la liste à trouver. Si oui, l'activité de recherche
     * est lancée. Sinon, un message d'erreur est affiché à l'utilisateur.
     *
     * @param requestCode le code de la requête
     * @param resultCode  le résultat de la requête
     * @param data        les données dans l'intent
     * @see ActiviteDepart#clickScan
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Si c'est le code 42, c'est donc le retour d'un scan de code barre
        if (requestCode == CODE_REQUETE) {
            if (resultCode == Activity.RESULT_OK) {
                codeEtudiant = data.getStringExtra("SCAN_RESULT");
                //String messageErreur = controleur.validerEntreeUtilisateur(codeEtudiant);
                if (controleurDepart.enleverEtudiantParCode(codeEtudiant)) {
                    Intent partie = new Intent(this, ActiviteRechercheEtudiant.class);
                    partie.putExtra(ActiviteRechercheEtudiant.ETUDIANTS_ACTUELS, etudiantsRestants);
                    finish();
                    startActivity(partie);
                } else {
                    Toast.makeText(ActiviteDepart.this, R.string.JoueurPasInscrit, Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    /**
     * Sauvegarde la liste d'étudiants en la plaçant dans le bundle de sortie
     *
     * @param outState le bundle servant à stocker les paramètres à sauvegarder
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putParcelableArrayList(ETUDIANTS_ACTUELS, etudiantsRestants);
    }

    /**
     * Restore la liste d'étudiants à trouver à partir de ce qui a
     * été sauvegarder précédemment
     *
     * @param outState le bundle contenant les paramètres sauvegarder
     * @see ActiviteDepart#onSaveInstanceState(Bundle)
     * @see ActiviteDepart#etudiantsRestants
     */
    @Override
    protected void onRestoreInstanceState(Bundle outState) {
        super.onRestoreInstanceState(outState);
        etudiantsRestants = outState.getParcelableArrayList(ETUDIANTS_ACTUELS);
        if (etudiantsRestants != null)
            controleurDepart.restorerEtudiantsPartie(etudiantsRestants);
    }


    @Override
    public void onEtudiantsTelecharges(ArrayList etudiants) {
        etudiantsRestants = etudiants;
        if (etudiants == null) {
            texteBienvenue.setText(R.string.ErreurConnexion);
            scanButton.setVisibility(View.INVISIBLE);
        } else {
            controleurDepart.restorerEtudiantsPartie(this.etudiantsRestants);
            scanButton.setVisibility(View.VISIBLE);
        }
        barreProgression.setVisibility(View.INVISIBLE);
    }
}

