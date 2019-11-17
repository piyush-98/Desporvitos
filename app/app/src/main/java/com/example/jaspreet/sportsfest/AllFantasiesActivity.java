package com.example.jaspreet.sportsfest;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

public class AllFantasiesActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_fantasies);
        RelativeLayout rl1,rl2;
        rl1=(RelativeLayout)findViewById(R.id.rl1);
        rl2=(RelativeLayout)findViewById(R.id.rl2);

        rl1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AllFantasiesActivity.this,MakeTeamActivity.class));
            }
        });

        rl2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AllFantasiesActivity.this,MakeTeamActivity.class));

            }
        });

    }
}
