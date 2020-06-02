package com.example.json;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethod;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ResponseCache;
import java.net.URL;

public class MainActivity extends AppCompatActivity {
    EditText cityName;
    TextView txtresult;



  class user{

  }
    public class DownloadTask extends AsyncTask <String, Void, String>
    {

        @Override
        protected String doInBackground(String... urls) {
            String result="";
            URL url;
            HttpURLConnection urlConnection=null;

            try {
                url= new URL(urls[0]);
                urlConnection= (HttpURLConnection) url.openConnection();
                InputStream in =urlConnection.getInputStream();
                InputStreamReader reader= new InputStreamReader(in);
                int data=reader.read();

                while (data!=-1)
                {
                    char current=(char)data;
                    result+=current;
                    data=reader.read();
                }
                return  result;

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;

        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            String message1;
            String message2;
            try {
                JSONObject jsonPart  = (JSONObject) fetchingJson(result,"weather");
                String main= jsonPart.getString("main");
                String description=jsonPart.getString("description");

                JSONObject jsonPart2  = (JSONObject) fetchingJson2(result,"main");
                String min_temp= jsonPart2.getString("temp_min");
                String max_temp=jsonPart2.getString("temp_max");
                String humidity=jsonPart2.getString("humidity");


                if(main!="" && description!="" && min_temp!=""&& max_temp!=""&& humidity!="")
                {
                    message1= main+" : "+description+"\r\n";
                    message2= "Min Temp"+" : "+min_temp+"\r\n"+"Max Temp"+" : "+max_temp+"\r\n"+"Humidity"+" : "+humidity;
                    txtresult.setText(message1+message2);
                }


            } catch (JSONException e) {
                e.printStackTrace();
            }
            catch (Exception e) {
                txtresult.setText("");
                Toast.makeText(getApplicationContext(),"Could not find weather ",Toast.LENGTH_SHORT).show();
            }
        }
    }

    public  JSONObject fetchingJson(String x,String tag) throws JSONException { // working with string values only !!
        JSONObject jsonObject= new JSONObject(x);
        String jsonClass=jsonObject.getString(tag);
        JSONArray jsonArray= new JSONArray(jsonClass);
        JSONObject jsonPart = null;
        for(int i=0;i<jsonArray.length();i++) {
            jsonPart = jsonArray.getJSONObject(i);
        }

        return jsonPart;
    }

    public  JSONObject fetchingJson2(String x,String tag) throws JSONException { // working with float values only !!
        JSONObject jsonObject= new JSONObject(x);
        String jsonClass=jsonObject.getString(tag);
        //Log.i("kk", jsonClass);
        JSONArray jsonArray= new JSONArray();
        jsonArray.put(jsonObject.get(tag));
        JSONObject jsonPart = null;
        for(int i=0;i<jsonArray.length();i++) {
            jsonPart = jsonArray.getJSONObject(i);
        }

        return jsonPart;
    }

    public void getWeather(View view)
    {
        Log.d("Clicked", "Clicked");
        Log.d("City Name", cityName.getText().toString());
        DownloadTask task= new DownloadTask();
        String city=cityName.getText().toString();
        task.execute("http://api.openweathermap.org/data/2.5/weather?q="+city+"&APPID=eac3e6cefe14737559d5eab21678012c");
        InputMethodManager inputMethodManager=(InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(cityName.getWindowToken(),0);
    }
    public void animation()
    {

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        cityName= findViewById(R.id.cityText);
        txtresult = (TextView)findViewById(R.id.textViewResult);



    }



}