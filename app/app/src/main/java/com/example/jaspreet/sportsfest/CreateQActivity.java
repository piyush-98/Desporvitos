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
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.Toast;

import com.example.jaspreet.sportsfest.Interfaces.ApiInterface_createINTQ;
import com.example.jaspreet.sportsfest.Interfaces.ApiInterface_createMCQ;
import com.example.jaspreet.sportsfest.Interfaces.ApiInterface_submitans;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

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

public class CreateQActivity extends AppCompatActivity {
    private static final String TAG = "QuestionCreation";
    FirebaseDatabase database;
    RelativeLayout rl;
    Switch switch1;
    EditText op1edittext,op2edittext;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_q);

        database = FirebaseDatabase.getInstance();
        rl=(RelativeLayout)findViewById(R.id.rl);

        final EditText editText=(EditText)findViewById(R.id.editText3);
        final EditText editText4=(EditText)findViewById(R.id.editText4);
        final EditText editText5=(EditText)findViewById(R.id.editText5);

        final LinearLayout op1=(LinearLayout)findViewById(R.id.op1);

        final LinearLayout op2=(LinearLayout)findViewById(R.id.op2);

         switch1=(Switch)findViewById(R.id.switch1);
        switch1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    op1.setVisibility(View.VISIBLE);
                    op2.setVisibility(View.VISIBLE);
                }else {

                    op1.setVisibility(View.GONE);
                    op2.setVisibility(View.GONE);
                }
            }
        });

         op1edittext=(EditText)findViewById(R.id.op1edittext);
         op2edittext=(EditText)findViewById(R.id.op2edittext);


        Button submit=(Button)findViewById(R.id.button3);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addQtoFirebase(editText.getText().toString(),editText4.getText().toString(),editText5.getText().toString());
            }
        });
    }

    private void addQtoFirebase(final String s, final String rate, final String participants) {

        final String[] qidd = new String[1];
        final DatabaseReference myRef = database.getReference("Questions");
        myRef.child("qid").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                qidd[0] = dataSnapshot.getValue().toString();
                createQFirebase(qidd[0],s,rate,participants);
                
              //  Toast.makeText(getApplicationContext(), qidd[0],Toast.LENGTH_LONG).show();
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });




    }

    private void createQFirebase(String s, String content, String rate, String participant) {


        final DatabaseReference myRef = database.getReference("Questions");

        DatabaseReference myRef1;
        if(MainIntroActivity.cricket==1)
            myRef1 = myRef.child("INT");
        else
            myRef1 = myRef.child("FOOTBALL");

        DatabaseReference myRef2 = myRef1.child(String.valueOf(s));

        myRef2.child("content").setValue(content);
        myRef2.child("rate").setValue(rate);
        myRef2.child("participant").setValue(participant);

        if (switch1.isChecked()) {
            String op1= op1edittext.getText().toString();
            String op2= op2edittext.getText().toString();
            myRef2.child("type").setValue("2");
            myRef2.child("op1").setValue(op1);
            myRef2.child("op2").setValue(op2);


            int newqid=Integer.parseInt(s)+1;

            myRef.child("qid").setValue(String.valueOf(newqid));

            createMCQQBl(newqid-1,rate,content,participant);
        }

        else {
            myRef2.child("type").setValue("1");
            myRef2.child("op1").setValue("");
            myRef2.child("op2").setValue("");


            int newqid=Integer.parseInt(s)+1;

            myRef.child("qid").setValue(String.valueOf(newqid));

            createQBl(newqid-1,rate,content,participant);
        }
    }


    private void createQBl(int newqid,String qrate, String content, String participant) {


        SharedPreferences prefs = getSharedPreferences("acckeys", MODE_PRIVATE);

        final String pubkey = prefs.getString("acctname", "0");
        String prikey = prefs.getString("prikey", "0");


        Log.e("acctname", pubkey);

        Log.e("prik", prikey);
        ApiInterface_createINTQ apiService = ApiClient.getClient().create(ApiInterface_createINTQ.class);


        // content should be in byte32

        Call<ResponseBody> call = apiService.getall(String.valueOf(newqid),content,qrate,participant,pubkey);


        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                String url = response.raw().request().url().toString();
                Log.e("compno", url);
                SUBMITINTQ mytask = new SUBMITINTQ();
                mytask.execute(url);
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e("bbbbb","fail!");
            }
        });

//        Intent i=new Intent(CreateQActivity.this,WebhashActivity.class);
//        i.putExtra("link","https://testexplorer.contentos.io/#/tx/"+pubkey);
//        startActivity(i);

    }

    private void createMCQQBl(int newqid,String qrate, String content, String participant) {


        SharedPreferences prefs = getSharedPreferences("acckeys", MODE_PRIVATE);

        final String pubkey = prefs.getString("acctname", "0");
        String prikey = prefs.getString("prikey", "0");


        Log.e("acctname", pubkey);

        Log.e("prik", prikey);
        ApiInterface_createMCQ apiService = ApiClient.getClient().create(ApiInterface_createMCQ.class);


        Call<ResponseBody> call = apiService.getall(String.valueOf(newqid),content,qrate,participant,pubkey);


        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                String url = response.raw().request().url().toString();
                Log.e("compno", url);
                SUBMITINTQ mytask = new SUBMITINTQ();
                mytask.execute(url);
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e("bbbbb","fail!");
            }
        });


    }


    private class SUBMITINTQ extends AsyncTask<String, Void, Integer> {



        public SUBMITINTQ() {

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
                          //  Snackbar.make(rl,"Question submitted to blockchain",Snackbar.LENGTH_LONG).show();

                            Snackbar snackbar = Snackbar
                                    .make(rl, "Question submitted to blockchain", Snackbar.LENGTH_LONG)
                                    .setAction("VIEW", new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            Intent i=new Intent(CreateQActivity.this,WebhashActivity.class);
                                            //  i.putExtra("link","https://testexplorer.contentos.io/#/tx/"+response);
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
