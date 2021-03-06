package com.example.englishdictionary.oxford;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import javax.net.ssl.HttpsURLConnection;
import android.os.AsyncTask;
import android.util.Log;


public class DictionaryRequest extends AsyncTask<String, Integer, String> {
    @Override
    protected String doInBackground(String... params) {

        //TODO: replace with your own app id and app key
        final String app_id = "a09288f7";
        final String app_key = "74cbb91dc4a9c63d81b07fa4e57e01cc";
        try {
            URL url = new URL(params[0]);
            HttpsURLConnection urlConnection = (HttpsURLConnection) url.openConnection();
            urlConnection.setRequestProperty("Accept","application/json");
            urlConnection.setRequestProperty("app_id",app_id);
            urlConnection.setRequestProperty("app_key",app_key);

            // read the output from the server
            BufferedReader reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            StringBuilder stringBuilder = new StringBuilder();

            String line = null;
            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line + "\n");
            }

            return stringBuilder.toString();

        }
        catch (Exception e) {
            e.printStackTrace();
            return e.toString();
        }
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);

        Log.v("Dic result",result); // TODO test
    }
}
