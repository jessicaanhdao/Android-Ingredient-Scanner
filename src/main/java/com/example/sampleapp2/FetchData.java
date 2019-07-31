package com.example.sampleapp2;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class FetchData extends AsyncTask {
    String searchString="";
    String data="";
    String dataParsed="";
    String title="";

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected Object doInBackground(Object[] objects) {
        try {
            URL url = new URL("https://en.wikipedia.org/w/api.php?action=query&prop=extracts&explaintext&redirects=1&format=json&titles="+objects[0]);
            HttpsURLConnection httpsURLConnection = (HttpsURLConnection) url.openConnection();
            InputStream inputStream = httpsURLConnection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String line = "";
            while (line != null) {
                line = bufferedReader.readLine();
                data = data + line;
            }

            JSONObject jsonObject = new JSONObject(data).getJSONObject("query").getJSONObject("pages");
            String pageID = jsonObject.keys().next();
            JSONObject pageObject  = (JSONObject) jsonObject.getJSONObject(pageID);

            if  (!pageObject.has("extract")) {
                dataParsed=("This item does not exist");
            } else {
                dataParsed+=pageObject.getString("extract");
                title+=pageObject.getString("title");
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;

    }

    @Override
    protected void onPostExecute(Object o) {
        super.onPostExecute(o);
        SearchFragment.data.setText(this.dataParsed);
        SearchFragment.title.setText(this.title);
    }
}
