package com.example.jaspreet.sportsfest;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jaspreet.sportsfest.Interfaces.ApiInterface_submitans;

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

public class Allquestions extends AppCompatActivity {

    RelativeLayout rl;
    String hash="";
    TextView txnhashtxt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_allquestions);

        final String qid=this.getIntent().getExtras().getString("qid","0");
        String qcontent=this.getIntent().getExtras().getString("qcontent","0");
        final String qrate=this.getIntent().getExtras().getString("qrate","0");
        final String qparticipant=this.getIntent().getExtras().getString("qparticipant","2");

        rl=(RelativeLayout)findViewById(R.id.rl);
        final EditText ans= (EditText)findViewById(R.id.editText2);
        TextView counttxt=(TextView)findViewById(R.id.textView11);;
        TextView qcontenttxt=(TextView)findViewById(R.id.textView69);;
        TextView qidtxt=(TextView)findViewById(R.id.textView68);
        TextView qratetxt=(TextView)findViewById(R.id.textView3);
        Button submit=(Button)findViewById(R.id.button2);



        qidtxt.setText("Quest ID: " + qid);
        qcontenttxt.setText(qcontent);
        qratetxt.setText("Fee: " +qrate+" COS");
        counttxt.setText("Participations till now: 0/"+qparticipant);



    submit.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        vote(qid,ans.getText().toString(),qrate);


    }
});



    }





    void vote(String qid, String ansid,String qrate){


        SharedPreferences prefs = getSharedPreferences("acckeys", MODE_PRIVATE);

//        String contractBal = prefs.getString("appdai", "0");
//        Log.e("TRANSFERRING BAL:",contractBal);

      //  Toast.makeText(getApplicationContext(),"Submitting your answer to blockchain!",Toast.LENGTH_LONG).show();
        String pubkey = prefs.getString("acctname", "0");
        String prikey = prefs.getString("prikey", "0");


        Log.e("accname", pubkey);

        Log.e("prik", prikey);
        ApiInterface_submitans apiService = ApiClient.getClient().create(ApiInterface_submitans.class);


        Call<ResponseBody> call = apiService.getall(qid,ansid,qrate,pubkey);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                String url = response.raw().request().url().toString();
                Log.e("compno", url);
                VOTE mytask = new VOTE();
                mytask.execute(url);


            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e("bbbbb","fail!");

            }



        });

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



                    final String response = convertInputStreamToString(inputStream);

                    Log.e("HASH", response);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {





                            Snackbar snackbar = Snackbar
                                    .make(rl, "Participation submitted to blockchain", Snackbar.LENGTH_LONG)
                                    .setAction("VIEW", new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            Intent i=new Intent(Allquestions.this,WebhashActivity.class);
                                            i.putExtra("link","https://testexplorer.contentos.io/#/tx/"+response);
                                            startActivity(i);
                                        }
                                    });
                            snackbar.show();






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
