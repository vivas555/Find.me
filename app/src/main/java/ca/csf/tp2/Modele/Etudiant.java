package ca.csf.tp2.Modele;

import android.os.Parcel;
import android.os.Parcelable;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Classe servant à la simulation d'un étudiant qui sera plus tard obtenu
 * par un service Web. L'étudiant possède un nom et un code. Il implémente
 * la classe Parcelable afin de pouvoir être sauvegardé et récupéré lors
 * des sauvegardes et resaurations d'instances.
 *
 * @author Alicia Lamontagne
 * @see Etudiant#nom
 * @see Etudiant#code
 */
public class Etudiant implements Parcelable {

    /**
     * Le prénom et le nom de famille de l'étudiant.
     *
     * @see Etudiant#getNom()
     * @see Etudiant#setNom(String)
     */
    private String nom;
    /**
     * Le code venant d'un code barre représentant l'étudiant
     *
     * @see Etudiant#code
     * @see Etudiant#setCode(String)
     */
    private String code;

    /**
     * Constructeur de la classe étudiant. Assigne le nom du joueur et son code selon les valeurs
     * passées en paramètre, si non nulles. Si le paramètre est null, l'attribut est initialisé
     * à "defaut".
     *
     * @param nom  Le nom du joueur
     * @param code Le code du joueur
     */
    public Etudiant(String nom, String code) {
        if (!setNom(nom))
            this.nom = "defaut";

        if (!setCode(code))
            this.code = "defaut";
    }

    public Etudiant() {
    }


    /**
     * Constructeur privé pour le parcelable
     *
     * @param in
     */
    private Etudiant(Parcel in) {
        nom = in.readString();
        code = in.readString();
    }

    /*Méthode non testée puisqu'il s'agit de code généré et qui
    * ne s'applique pas à la logique de l'application */
    @Override
    public int describeContents() {
        return 0;
    }


    /*Méthode non testée puisqu'il s'agit de code généré et qui
    * ne s'applique pas à la logique de l'application */
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(nom);
        dest.writeString(code);
    }

    /*Méthode non testée puisqu'il s'agit de code généré et qui
    * ne s'applique pas à la logique de l'application */
    public static final Parcelable.Creator<Etudiant> CREATOR = new Parcelable.Creator<Etudiant>() {
        public Etudiant createFromParcel(Parcel in) {
            return new Etudiant(in);
        }

        public Etudiant[] newArray(int size) {
            return new Etudiant[size];
        }
    };

    /**
     * Obtient le code de l'étudiant
     *
     * @return le code de l'étudiant
     * @see Etudiant#code
     */
    @JsonProperty("uniqueIdentifier")
    public String getCode() {
        return code;
    }

    /**
     * Modifie la valeur du code de l'étudiant selon la valeur du paramètre
     *
     * @param code le nouveau code de l'étudiant
     * @see Etudiant#code
     */
    @JsonProperty("uniqueIdentifier")
    public boolean setCode(String code) {
        if (code != null) {
            code = code.trim();
            if (code.length() == 12) {
                this.code = code;
                return true;
            }
        }
        return false;
    }

    /**
     * Obtient le nom de l'étudiant
     *
     * @return le nom de l'étudiant
     * @see Etudiant#nom
     */
    @JsonProperty("fullName")
    public String getNom() {
        return nom;
    }

    @JsonProperty("fullName")
    public boolean setNom(String nom) {
        if (nom != null) {
            nom = nom.trim();
            if (nom.length() > 0) {
                this.nom = nom;
                return true;
            }
        }
        return false;
    }


}
