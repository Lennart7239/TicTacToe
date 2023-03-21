package com.example.geld;

import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.json.JSONException;
import org.json.JSONObject;

import javax.net.ssl.HttpsURLConnection;

class stockAPI extends AsyncTask<String, Void, String> {
    protected String doInBackground(String... params) {

        try {
            String apiUrl = "https://www.alphavantage.co/query?function=TIME_SERIES_INTRADAY&symbol="+params[0]+"&interval=5min&apikey=0BUJQQN8T55F3UCC";
            URL url = new URL(apiUrl);
            HttpsURLConnection con = (HttpsURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            int responseCode = con.getResponseCode();
            if (con.getResponseCode() == HttpsURLConnection.HTTP_OK) {
                BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
                String inputLine;
                StringBuffer response = new StringBuffer();
                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();
                JSONObject json = new JSONObject(response.toString());


                JSONObject quote = json.getJSONObject("Time Series (5min)");

                JSONObject lastRefresh = json.getJSONObject("Meta Data");
                String letzt = lastRefresh.getString("3. Last Refreshed");

                JSONObject quote1 = quote.getJSONObject(letzt);





                String price = quote1.getString("2. high");
                return(price);
            } else {
                return("Fehler beim Abrufen des Aktienkurses.");
            }
        } catch (IOException | JSONException e) {
            return ("Fehler beim Abrufen des Aktienkurses: " + e.getMessage());
        }
    }
}