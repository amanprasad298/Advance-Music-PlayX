package com.example.musicplayx;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;

public class PlayerActivity extends AppCompatActivity {

    Button btn_next, btn_pause, btn_previous;
    TextView songText;
    SeekBar sb;
    String sname;
    static MediaPlayer myMedia;
    int position;

    ArrayList<File> mySongs;
    Thread updateSeekBar;




    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);

        btn_next= (Button) findViewById(R.id.next);
        btn_previous= (Button) findViewById(R.id.prev);
        btn_pause= (Button) findViewById(R.id.pause);
        songText= (TextView) findViewById(R.id.songLabel);
        sb = (SeekBar) findViewById(R.id.seekBar);

        getSupportActionBar().setTitle("Song Playing......");

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        updateSeekBar= new Thread(){
            @Override
            public void run() {

                int totalDuration= myMedia.getDuration();
                int currentPoss= 0;

                while(currentPoss<totalDuration){
                    try {
                        sleep(500);
                        currentPoss=myMedia.getCurrentPosition();
                        sb.setProgress(currentPoss);

                    }
                    catch (InterruptedException e){
                        e.printStackTrace();
                    }
                }

            }
        };

        if(myMedia!=null){
            myMedia.stop();
            myMedia.release();
        }

        Intent i = getIntent();
        Bundle bundle=i.getExtras();

        mySongs=(ArrayList) bundle.getParcelableArrayList("songs");

        sname=mySongs.get(position).getName().toString();

        String songName = i.getStringExtra("songname");
        songText.setText(songName);
        songText.setSelected(true);

        position = bundle.getInt("pos", 0);
        Uri u = Uri.parse(mySongs.get(position).toString());

        myMedia = MediaPlayer.create(getApplicationContext(),u);

        myMedia.start();

        sb.setMax(myMedia.getDuration());

        updateSeekBar.start();

        sb.getProgressDrawable().setColorFilter(getResources().getColor(R.color.colorPrimary), PorterDuff.Mode.MULTIPLY);
        sb.getThumb().setColorFilter(getResources().getColor(R.color.colorPrimary), PorterDuff.Mode.SRC_IN);

        sb.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                myMedia.seekTo(seekBar.getProgress());

            }
        });



        btn_pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                sb.setMax(myMedia.getDuration());
                if(myMedia.isPlaying()){
                    btn_pause.setBackgroundResource(R.drawable.play);
                    myMedia.pause();
                }
                else{
                    btn_pause.setBackgroundResource(R.drawable.pause);
                    myMedia.start();
                }


            }
        });

        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myMedia.stop();
                myMedia.release();
                position = ((position+1)%mySongs.size());

                Uri u = Uri.parse(mySongs.get(position).toString());
                myMedia= MediaPlayer.create(getApplicationContext(),u);
                sname = mySongs.get(position).getName().toString();
                songText.setText(sname);

                myMedia.start();
            }
        });

        btn_previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myMedia.stop();
                myMedia.release();
                position = ((position-1)<0)?(mySongs.size()-1):(position-1);
                Uri u = Uri.parse(mySongs.get(position).toString());
                myMedia = MediaPlayer.create(getApplicationContext(), u);

                sname = mySongs.get(position).getName().toString();
                songText.setText(sname);

                myMedia.start();

            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if(item.getItemId() == android.R.id.home){
            onBackPressed();
        }


        return super.onOptionsItemSelected(item);
    }
}