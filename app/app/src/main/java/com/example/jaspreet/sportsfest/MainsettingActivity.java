package com.example.jaspreet.sportsfest;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jaspreet.sportsfest.Interfaces.ApiInterface_getappbalance;
import com.example.jaspreet.sportsfest.Interfaces.ApiInterface_getcontractbal;
import com.example.jaspreet.sportsfest.Interfaces.ApiInterface_getCOSbal;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSerializer;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.dimorinny.floatingtextbutton.FloatingTextButton;

public class MainsettingActivity extends AppCompatActivity {
    static String name="unknown";
    RelativeLayout rl;
    String username;
    TextView cosbal,contractbal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_arrow_back_black_24dp);
        setContentView(R.layout.activity_mainsetting);
        actionBar.setDisplayShowHomeEnabled(true);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        SharedPreferences prefs = getSharedPreferences("acckeys", MODE_PRIVATE);

        cosbal =(TextView)findViewById(R.id.daibal);
        contractbal=(TextView)findViewById(R.id.daiearn);

        String appcos = prefs.getString("appcos", "0");

        String cosbaltxt = prefs.getString("cosbal", "0");

        cosbal.setText(cosbaltxt+ " COS");
        contractbal.setText(appcos+ " COS");


//      rl=(RelativeLayout) findViewById(R.id.done);
        username = prefs.getString("username", "");
        String acctname = prefs.getString("acctname", "");

        final TextView nam = (TextView) findViewById(R.id.textView2);
        final TextView id = (TextView) findViewById(R.id.textView4);
        if (username.equals("null")) {
            SharedPreferences.Editor editor = getSharedPreferences("acckeys", MODE_PRIVATE).edit();
            editor.putString("username", "");
            editor.apply();
        }
        username = prefs.getString("username", "");
        nam.setText(username);
        id.setText(acctname);
        ImageView edit = (ImageView) findViewById(R.id.edit);
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                LayoutInflater inflater = getLayoutInflater();
                View alertLayout = inflater.inflate(R.layout.layoutchange, null);
                final EditText namee = (EditText) alertLayout.findViewById(R.id.editText);
                final EditText pubb = (EditText) alertLayout.findViewById(R.id.pubb);
                final EditText prii = (EditText) alertLayout.findViewById(R.id.prii);

                namee.setText(username);
                AlertDialog.Builder alert = new AlertDialog.Builder(MainsettingActivity.this);

                // this is set the view from XML inside AlertDialog
                alert.setView(alertLayout);
                // disallow cancel of AlertDialog on click of back button and outside touch
                alert.setTitle("My Account ");
                alert.setIcon(R.drawable.ic_person_outline_rblack_24dp);
                alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                alert.setPositiveButton("Set", new DialogInterface.OnClickListener() {


                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        SharedPreferences.Editor editor = getSharedPreferences("acckeys", MODE_PRIVATE).edit();
                        editor.putString("username", "" + namee.getText().toString());
                        editor.putString("acctname", "" + pubb.getText().toString());
                        editor.putString("prikey", "" + prii.getText().toString());

                        editor.apply();
                        nam.setText(namee.getText().toString());
                        id.setText(pubb.getText().toString());


                    }
                });
                AlertDialog dialog = alert.create();
                dialog.show();

            }
        });


        getCOSbal();
        getcontractbal();

        FloatingTextButton transfer=(FloatingTextButton) findViewById(R.id.tranferbutton1);
        transfer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                transferCOSbal();
            }
        });
    }


    @Override
    public void onResume() {
        super.onResume();

        getCOSbal();
        getcontractbal();


    }


void  getcontractbal(){


    SharedPreferences prefs = getSharedPreferences("acckeys", MODE_PRIVATE);

    String acctname = prefs.getString("acctname", "gameoasisacc1");

    ApiInterface_getcontractbal apiService = ApiClient.getClient().create(ApiInterface_getcontractbal.class);

    Call<ResponseBody> call = apiService.getall();
    call.enqueue(new Callback<ResponseBody>() {
        @Override
        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
            String url = response.raw().request().url().toString();
            Log.e("appcontentos", url);
            CONTRACTBAL mytask = new CONTRACTBAL();
            mytask.execute(url);


        }

        @Override
        public void onFailure(Call<ResponseBody> call, Throwable t) {
            Log.e("appcontentos","fail!");

        }



    });

}




    void getCOSbal()
    {

        SharedPreferences prefs = getSharedPreferences("acckeys", MODE_PRIVATE);
        String acctname = prefs.getString("acctname", "gameoasisacct1");

        ApiInterface_getCOSbal apiService = ApiClient.getClient().create(ApiInterface_getCOSbal.class);

        Call<ResponseBody> call = apiService.getall(acctname);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                String url = response.raw().request().url().toString();
                Log.e("compno", url);
                COSBAL mytask = new COSBAL();
                mytask.execute(url);


            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e("bbbbb","fail!");

            }



        });

    }


    void transferCOSbal()
    {

        SharedPreferences prefs = getSharedPreferences("acckeys", MODE_PRIVATE);
        String acctname = prefs.getString("acctname", "gameoasisacct1");

        ApiInterface_getappbalance apiService = ApiClient.getClient().create(ApiInterface_getappbalance.class);
        String appcos = prefs.getString("appcos", "0");

        Call<ResponseBody> call = apiService.getall(acctname,appcos);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                String url = response.raw().request().url().toString();
                Log.e("compno", url);
                TRANSFERCOSBAL mytask = new TRANSFERCOSBAL();
                mytask.execute(url);


            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e("bbbbb","fail!");

            }



        });

    }






    private class COSBAL extends AsyncTask<String, Void, Integer> {



        public COSBAL() {

            super();

        }


        // The onPreExecute is executed on the main UI thread before background processing is

        // started. In this method, we start the progressdialog.

        @Override

        protected void onPreExecute() {

            super.onPreExecute();


            // Show the progress dialog on the screen


        }


        // This method is executed in the background and will return a result to onPostExecute

        // method. It receives the file name as input parameter.

        @Override

        protected Integer doInBackground(String... urls) {


            InputStream inputStream = null;

            HttpURLConnection urlConnection = null;

            Integer result = 0;


            // TODO connect to server, download and process the JSON string


            // Now we read the file, line by line and construct the

            // Json string from the information read in.

            try {

                /* forming th java.net.URL object */

                URL url = new URL(urls[0]);

                urlConnection = (HttpURLConnection) url.openConnection();



                /* optional request header */

                urlConnection.setRequestProperty("Content-Type", "application/json");



                /* optional request header */

                urlConnection.setRequestProperty("Accept", "application/json");



                /* for Get request */

                urlConnection.setRequestMethod("GET");

                int statusCode = urlConnection.getResponseCode();



                /* 200 represents HTTP OK */

                if (statusCode == 200) {

                    inputStream = new BufferedInputStream(urlConnection.getInputStream());


                    // Convert the read in information to a Json string

                    final String response = convertInputStreamToString(inputStream);

                    Log.e("complaintno", response);
                    final String compno=response.replace("\"","")  ;
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {


//                            String st=compno.substring(0,3);
//                            String addDec=st.charAt(0)+"."+st.charAt(1)+st.charAt(2)+" DAI";

                            cosbal.setText(response+" COS");
                            SharedPreferences.Editor editor= getSharedPreferences("acckeys",MODE_PRIVATE).edit();
                            editor.putString("cosbal", String.valueOf(response));
                            editor.apply();


                        }
                    });




                    // now process the string using the method that we implemented in the previous exercise




                    result = 1; // Successful

                } else {

                    result = 0; //"Failed to fetch data!";

                }

            } catch (Exception e) {



            }

            return result; //"Failed to fetch data!";

        }

    }



    private class CONTRACTBAL extends AsyncTask<String, Void, Integer> {



        public CONTRACTBAL() {

            super();

        }


        // The onPreExecute is executed on the main UI thread before background processing is

        // started. In this method, we start the progressdialog.

        @Override

        protected void onPreExecute() {

            super.onPreExecute();


            // Show the progress dialog on the screen


        }


        // This method is executed in the background and will return a result to onPostExecute

        // method. It receives the file name as input parameter.

        @Override

        protected Integer doInBackground(String... urls) {


            InputStream inputStream = null;

            HttpURLConnection urlConnection = null;

            Integer result = 0;


            // TODO connect to server, download and process the JSON string


            // Now we read the file, line by line and construct the

            // Json string from the information read in.

            try {

                /* forming th java.net.URL object */

                URL url = new URL(urls[0]);

                urlConnection = (HttpURLConnection) url.openConnection();



                /* optional request header */

                urlConnection.setRequestProperty("Content-Type", "application/json");



                /* optional request header */

                urlConnection.setRequestProperty("Accept", "application/json");



                /* for Get request */

                urlConnection.setRequestMethod("GET");

                int statusCode = urlConnection.getResponseCode();



                /* 200 represents HTTP OK */

                if (statusCode == 200) {

                    inputStream = new BufferedInputStream(urlConnection.getInputStream());


                    // Convert the read in information to a Json string

                    String response = convertInputStreamToString(inputStream);

                   // Log.e("appcontentos", response);


                    JsonParser jsonParser=new JsonParser();
                    JsonArray array=(JsonArray) jsonParser.parse(response);
                    String ans = "";

                    SharedPreferences prefs = getSharedPreferences("acckeys", MODE_PRIVATE);


                    String accname = prefs.getString("acctname", "0");

                    for(int i=0;i<array.size();i++){
                        JsonObject jsonObject = array.get(i).getAsJsonObject();
                        Log.e("appcontentos", String.valueOf(jsonObject));

                        Log.e("appcontentos", String.valueOf(jsonObject.get("amount")));
                        Log.e("appcontentosacc", String.valueOf(accname));

                        String name=String.valueOf(jsonObject.get("owner")).replace("\"","");
                        Log.e("appcontentos", name);

                        if(name.equals(accname)){
                            ans=String.valueOf(jsonObject.get("amount"));
                        }

                    }

                    Log.e("appcontentos", ans);

                    final String finalAns = ans+" COS";
                    final String finalAns1 = ans;
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            contractbal.setText(finalAns);
                            SharedPreferences.Editor editor= getSharedPreferences("acckeys",MODE_PRIVATE).edit();
                            editor.putString("appcos", String.valueOf(finalAns1));
                            editor.apply();


                        }
                    });




                    // now process the string using the method that we implemented in the previous exercise




                    result = 1; // Successful

                } else {

                    result = 0; //"Failed to fetch data!";

                }

            } catch (Exception e) {



            }

            return result; //"Failed to fetch data!";

        }

    }



    private class TRANSFERCOSBAL extends AsyncTask<String, Void, Integer> {



        public TRANSFERCOSBAL() {

            super();

        }


        // The onPreExecute is executed on the main UI thread before background processing is

        // started. In this method, we start the progressdialog.

        @Override

        protected void onPreExecute() {

            super.onPreExecute();


            // Show the progress dialog on the screen


        }


        // This method is executed in the background and will return a result to onPostExecute

        // method. It receives the file name as input parameter.

        @Override

        protected Integer doInBackground(String... urls) {


            InputStream inputStream = null;

            HttpURLConnection urlConnection = null;

            Integer result = 0;


            // TODO connect to server, download and process the JSON string


            // Now we read the file, line by line and construct the

            // Json string from the information read in.

            try {

                /* forming th java.net.URL object */

                URL url = new URL(urls[0]);

                urlConnection = (HttpURLConnection) url.openConnection();



                /* optional request header */

                urlConnection.setRequestProperty("Content-Type", "application/json");



                /* optional request header */

                urlConnection.setRequestProperty("Accept", "application/json");



                /* for Get request */

                urlConnection.setRequestMethod("GET");

                int statusCode = urlConnection.getResponseCode();



                /* 200 represents HTTP OK */

                if (statusCode == 200) {

                    inputStream = new BufferedInputStream(urlConnection.getInputStream());


                    // Convert the read in information to a Json string

                    final String response = convertInputStreamToString(inputStream);

                    Toast.makeText(getApplicationContext(),"Conversion successful",Toast.LENGTH_LONG).show();



                    // now process the string using the method that we implemented in the previous exercise




                    result = 1; // Successful

                } else {

                    result = 0; //"Failed to fetch data!";

                }

            } catch (Exception e) {



            }

            return result; //"Failed to fetch data!";

        }

    }



    private String convertInputStreamToString(InputStream inputStream) throws IOException {

        BufferedReader bufferedReader = new BufferedReader( new InputStreamReader(inputStream));

        String line = "";

        String result = "";

        while((line = bufferedReader.readLine()) != null){

            result += line;

        }



        /* Close Stream */

        if(null!=inputStream){

            inputStream.close();

        }

        return result;

    }














}
