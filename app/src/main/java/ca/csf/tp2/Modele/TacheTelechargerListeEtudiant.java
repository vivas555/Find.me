package ca.csf.tp2.Modele;

import android.os.AsyncTask;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

/**
 * @author Alicia
 */
public class TacheTelechargerListeEtudiant extends AsyncTask<String, Void, ArrayList> {

    private final Callback callback;

    public TacheTelechargerListeEtudiant(Callback callback) {
        this.callback = callback;
    }

    @Override
    protected ArrayList doInBackground(String... params) {
        if (params.length != 1) {
            throw new IllegalArgumentException("La tache ne prend qu'une seule String en paramètre");
        }

        try {
            URL url = new URL(params[0]);

            URLConnection connection = url.openConnection();

            HttpURLConnection connectionHttp = (HttpURLConnection) connection;

            connectionHttp.setRequestProperty("Accept", "application/json");

            connectionHttp.connect();

            ObjectMapper mapper = new ObjectMapper();

            ArrayList<Etudiant> etudiants = mapper.readValue(connectionHttp.getInputStream(), mapper.getTypeFactory().constructCollectionType(ArrayList.class, Etudiant.class));

            connectionHttp.disconnect();


            return etudiants;

        } catch (MalformedURLException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

    }


    @Override
    protected void onPostExecute(ArrayList etudiants) {
        callback.onEtudiantsTelecharges(etudiants);
    }

    public interface Callback {
        void onEtudiantsTelecharges(ArrayList etudiants);
    }

}
