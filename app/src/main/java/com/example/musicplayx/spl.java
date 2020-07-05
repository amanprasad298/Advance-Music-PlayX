package com.example.musicplayx;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.View;

public class spl extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spl);

        Thread th= new Thread(){
            @Override
            public void run() {
                try {
                    sleep(300);
                }catch (Exception e){
                    e.printStackTrace();
                }finally {
                    Intent obj= new Intent(spl.this, MainActivity.class);
                    startActivity(obj);
                };

            }
        };
        th.start();

    }

    @Override
    protected  void onPause(){
        super.onPause();
        finish();
    }
}