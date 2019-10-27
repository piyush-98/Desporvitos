package com.example.jaspreet.sportsfest;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MakeTeamActivity extends AppCompatActivity {

    Button indbutton, sabutton;
    TextView ortxt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_team);

        indbutton=(Button)findViewById(R.id.button4);
        sabutton=(Button)findViewById(R.id.button5);

        ortxt=(TextView)findViewById(R.id.textView12);

        final String[] team1players={"Rohit Sharma","Mayank Agarwal","Cheteshwar Pujara","Virat Kohli","Ajinkya Rahane", "Jaddu","Saha","Ashwin","Nadeem","Umesh Yadav", "Shami"};
        final String[] team2players={"Quinton De Kock","Dean Algar","Hamza","Plesis","Bavuma","Klasen","Linde","Rabada","Nortje","Ngidi","Assu"};

        final ArrayList<String> myteam=new ArrayList<>();
        final int[] currind = {0};



            indbutton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.e("CURRENTIND", String.valueOf(currind[0]));

                    myteam.add(team1players[currind[0]]);
                    currind[0]++;
                    indbutton.setText(team1players[currind[0]]);
                    sabutton.setText(team2players[currind[0]]);
                    if(currind[0]==10)
                    {
                        indbutton.setVisibility(View.GONE);
                        sabutton.setVisibility(View.GONE);
                        showteam(myteam);


                    }

                }
            });


            sabutton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.e("CURRENTIND", String.valueOf(currind[0]));


                    myteam.add(team2players[currind[0]]);
                    currind[0]++;
                    indbutton.setText(team1players[currind[0]]);
                    sabutton.setText(team2players[currind[0]]);
                    if(currind[0]==10)
                    {
                        indbutton.setVisibility(View.GONE);
                        sabutton.setVisibility(View.GONE);
                        ortxt.setVisibility(View.GONE);
                        showteam(myteam);


                    }


                }
            });
        }

//
//        else {
//
    void showteam(ArrayList<String> myteam) {
        TextView textView = (TextView) findViewById(R.id.textView7);
        TextView textView1 = (TextView) findViewById(R.id.textView8);
        textView1.setVisibility(View.GONE);


        StringBuilder string = new StringBuilder("\nYour chosen players are: \n\n");
        for (int i = 0; i < 10; i++)
            string.append(String.valueOf(i + 1)).append(". ").append(myteam.get(i)).append("\n");

        textView.setText(string);

        Toast.makeText(getApplicationContext(), "Submitted your chosen players to blockchain", Toast.LENGTH_LONG).show();

        // find fantasy leaugue from firebase

        // increment // for now only 1


//        }


    }






    private class VOTE extends AsyncTask<String, Void, Integer> {



        public VOTE() {

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

                    Log.e("HASH", response);
                    //       final String compno=response.replace("\"","") + " DAI" ;
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
//                            txnhashtxt.setVisibility(View.VISIBLE);

                            Toast.makeText(getApplicationContext(),response,Toast.LENGTH_LONG).show();



//                            Snackbar mySnackbar = Snackbar.make(rl, "Transaction initiated", Snackbar.LENGTH_LONG);
//                            mySnackbar.setAction("View", new ViewHash());
//                            mySnackbar.show();


//                            Snackbar snackbar=Snackbar.make(rl,"Transaction initiated",Snackbar.LENGTH_LONG).setAction("VIEW", new View.OnClickListener() {
//                                @Override
//                                public void onClick(View v) {
//                                    Intent i=new Intent(Allquestions.this,WebhashActivity.class);
//                                    i.putExtra("hashlink",response);
//                                    startActivity(i);
//
//                                }
//                            });
//                            snackbar.show();
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
