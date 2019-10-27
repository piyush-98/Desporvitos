package com.example.jaspreet.sportsfest;

import android.content.Intent;
import android.net.Uri;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class MainIntroActivity extends AppCompatActivity {

    static  int cricket;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_intro);
        FloatingActionButton cvideo=(FloatingActionButton)findViewById(R.id.cricvideo);
        FloatingActionButton fvideo=(FloatingActionButton)findViewById(R.id.footballvideo);

        ImageView c=(ImageView)findViewById(R.id.imageView2);
        ImageView f=(ImageView)findViewById(R.id.imageView3);

        c.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cricket=1;
                startActivity(new Intent(MainIntroActivity.this,MainActivity.class));
            }
        });

        f.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cricket=0;
                startActivity(new Intent(MainIntroActivity.this,MainActivity.class));
            }
        });

        cvideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "https://cos.tv/videos/play/1572156852179911697";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);

            }
        });


        fvideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "https://cos.tv/videos/play/1572157654105815641";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);

            }
        });
    }
}
