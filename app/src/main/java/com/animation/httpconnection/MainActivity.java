package com.animation.httpconnection;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {
    String response = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        new HttpConnectionGetRequest().execute();
    }

    class HttpConnectionGetRequest extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            HttpURLConnection urlConnection = null;

            /**
             * GET Request
             */
            try {
                URL url;
                url = new URL("https://reqres.in/api/users?page=2");
                urlConnection = (HttpURLConnection) url.openConnection();

                int code = urlConnection.getResponseCode();
                if (code != 200) {
                    throw new IOException("Invalid response from server:" + code);
                }

                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                String string;
                while ((string=bufferedReader.readLine())!=null){
                    Log.d("data",string);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            finally {
                if (urlConnection!=null){
                    urlConnection.disconnect();
                }
            }

            /**
             * POST request
             */
            try {
                JSONObject payload=new JSONObject();
                payload.accumulate("name","Naimish");
                URL url=new URL("https://reqres.in/api/users");
                urlConnection=(HttpURLConnection) url.openConnection();
                urlConnection.setRequestProperty("Content-Type","application/json");
                urlConnection.setRequestMethod("POST");
                urlConnection.setDoOutput(true);
                urlConnection.setDoInput(true);
                urlConnection.setChunkedStreamingMode(0);
                OutputStream outputStream=new BufferedOutputStream(urlConnection.getOutputStream());
                BufferedWriter writer=new BufferedWriter(new OutputStreamWriter(outputStream,"UTF-8"));
                writer.write(payload.toString());
                writer.flush();

                int responseCode=urlConnection.getResponseCode();
                if (responseCode!=200){
                    throw new IOException("Invalid Respone: "+responseCode);
                }

                BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                String string;
                while ((string=bufferedReader.readLine())!=null){
                    Log.d("Response",string);
                }
            } catch (JSONException | IOException e) {
                e.printStackTrace();
            }

            return null;
        }

    }
}