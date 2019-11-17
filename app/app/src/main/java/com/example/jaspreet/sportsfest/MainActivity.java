package com.example.jaspreet.sportsfest;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    QListViewAdapter adapter;
    ListView qlist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        qlist=(ListView)findViewById(R.id.listView);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(MainActivity.this,CreateQActivity.class));

            }
        });

        LinearLayout fc=(LinearLayout)findViewById(R.id.fc);
        fc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,WebfantasyActivity.class));
            }
        });

        if(MainIntroActivity.cricket==0){
            HorizontalScrollView horizontalScrollView=(HorizontalScrollView)findViewById(R.id.rlh);
            horizontalScrollView.setVisibility(View.GONE);
        }




        readINTQuestions();


    }

    @Override
    public void onResume() {
        super.onResume();

        readINTQuestions();


    }

    private void readINTQuestions() {

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference myRef = database.getReference("Questions");

        // myRef.setValue("Hello, World!");

// Read from the database

        final ArrayList<String> INTquestionContent=new ArrayList<>();
        final ArrayList<String> INTquestionRate=new ArrayList<>();
        final ArrayList<String> INTquestionid=new ArrayList<>();
        final ArrayList<String> INTquestiontype=new ArrayList<>();
        final ArrayList<String> MCQop1=new ArrayList<>();
        final ArrayList<String> MCQop2=new ArrayList<>();
        final ArrayList<String> INTquestionParticipant=new ArrayList<>();


        if(MainIntroActivity.cricket==1) {
            myRef.child("INT")
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                // Questions user = snapshot.getValue(Questions.class);
                                INTquestionid.add(snapshot.getKey());
                                INTquestionContent.add(snapshot.child("content").getValue().toString());
                                INTquestionRate.add(snapshot.child("rate").getValue().toString());
                                INTquestiontype.add(snapshot.child("type").getValue().toString());
                                INTquestionParticipant.add(snapshot.child("participant").getValue().toString());
                                MCQop1.add(snapshot.child("op1").getValue().toString());
                                MCQop2.add(snapshot.child("op2").getValue().toString());


                                //       Toast.makeText(getApplicationContext(),INTquestionid.get(0),Toast.LENGTH_LONG).show();
                            }

                            QListViewAdapter qListViewAdapter;

                            Log.e("qqqqqqq", INTquestionid.toString() + "  " + INTquestionContent.toString() + "  " + INTquestionRate.toString());
                            qListViewAdapter = new QListViewAdapter(MainActivity.this, INTquestionid, INTquestionContent, INTquestionRate, INTquestiontype);
                            qlist.setAdapter(qListViewAdapter);

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                        }
                    });
        }
        else{


            myRef.child("FOOTBALL")
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                // Questions user = snapshot.getValue(Questions.class);
                                INTquestionid.add(snapshot.getKey());
                                INTquestionContent.add(snapshot.child("content").getValue().toString());
                                INTquestionRate.add(snapshot.child("rate").getValue().toString());
                                INTquestiontype.add(snapshot.child("type").getValue().toString());
                                INTquestionParticipant.add(snapshot.child("participant").getValue().toString());
                                MCQop1.add(snapshot.child("op1").getValue().toString());
                                MCQop2.add(snapshot.child("op2").getValue().toString());


                                //       Toast.makeText(getApplicationContext(),INTquestionid.get(0),Toast.LENGTH_LONG).show();
                            }

                            QListViewAdapter qListViewAdapter;

                            Log.e("qqqqqqq", INTquestionid.toString() + "  " + INTquestionContent.toString() + "  " + INTquestionRate.toString());
                            qListViewAdapter = new QListViewAdapter(MainActivity.this, INTquestionid, INTquestionContent, INTquestionRate, INTquestiontype);
                            qlist.setAdapter(qListViewAdapter);

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                        }
                    });











        }

        qlist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if(INTquestiontype.get(position).equals("1")){
                    Intent i = new Intent(MainActivity.this, Allquestions.class);
                    i.putExtra("qid",INTquestionid.get(position));
                    i.putExtra("qcontent",INTquestionContent.get(position));
                    i.putExtra("qrate",INTquestionRate.get(position));
                    i.putExtra("qparticipant",INTquestionParticipant.get(position));

                    startActivity(i);
                }
                else{
                    Intent i = new Intent(MainActivity.this, MCQActivity.class);
                    i.putExtra("qid",INTquestionid.get(position));
                    i.putExtra("qcontent",INTquestionContent.get(position));
                    i.putExtra("qrate",INTquestionRate.get(position));
                    i.putExtra("op1",MCQop1.get(position));
                    i.putExtra("op2",MCQop2.get(position));
                    i.putExtra("qparticipant",INTquestionParticipant.get(position));

                    startActivity(i);

                }


            }
        });

//        Intent i = new Intent(MainActivity.this, Allquestions.class);
//
//        startActivity(i);


        final ArrayList<String> MCQquestionContent=new ArrayList<>();
        final ArrayList<String> MCQquestionRate=new ArrayList<>();
        final ArrayList<String> MCQquestionOptions=new ArrayList<String>();
        final ArrayList<String> MCQquestionid=new ArrayList<String>();


        myRef.child("MCQ")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            // Questions user = snapshot.getValue(Questions.class);
                            MCQquestionid.add(snapshot.getKey());
                            MCQquestionContent.add(snapshot.child("content").getValue().toString());
                            MCQquestionRate.add(snapshot.child("rate").getValue().toString());
                            MCQquestionOptions.add(snapshot.child("options").getValue().toString());

                          //  Toast.makeText(getApplicationContext(),MCQquestionOptions.get(0),Toast.LENGTH_LONG).show();
                        }
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.setting) {

            Intent i = new Intent(MainActivity.this, MainsettingActivity.class);
            startActivity(i);
            return true;
        }


            return super.onOptionsItemSelected(item);


    }


    }
