package com.example.android.androidapp.ui;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.MediaController;
import android.widget.RatingBar;
import android.widget.SeekBar;
import android.widget.VideoView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.android.androidapp.R;

public class VideoActivity extends AppCompatActivity {
    VideoView videoView;
    SeekBar seekBar;
    ImageButton playPause;
    ImageButton stop;
    private Handler handler;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);

        videoView = findViewById(R.id.videoView);
        seekBar = findViewById(R.id.seekBar);
        playPause = findViewById(R.id.play_pause);
        stop = findViewById(R.id.stop);

        String videoPath = getIntent().getStringExtra("videoPath");
        Uri uri = Uri.parse(videoPath);
        videoView.setVideoURI(uri);
        videoView.start();
        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                videoView.seekTo(1);
                videoView.pause();
                seekBar.setMax(videoView.getDuration()/1000);
                seekBar.setProgress(0);
            }
        });

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(videoView != null && fromUser){
                    videoView.seekTo(progress * 1000);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

//        RatingBar ratingBar = findViewById(R.id.ratingBar);
//        Drawable progress = ratingBar.getProgressDrawable();
//        progress.setTint(Color.YELLOW);

    }

    public void playPause(View view) {
        if (videoView.isPlaying()) {
            videoView.pause();
            playPause.setImageResource(R.drawable.ic_play_arrow_white_24dp);
        } else {
            videoView.start();
            playPause.setImageResource(R.drawable.ic_pause_white_24dp);

            handler = new Handler();
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (videoView != null) {
                        int current = videoView.getCurrentPosition() / 1000;
                        seekBar.setProgress(current);

                        handler.postDelayed(this, 1000);
                    }
                }
            });

            videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    playPause.setImageResource(R.drawable.ic_play_arrow_white_24dp);
                }
            });
        }
    }


    public void stop(View view) {
        if (videoView.isPlaying()) {
            videoView.pause();
            videoView.seekTo(1);
            playPause.setImageResource(R.drawable.ic_play_arrow_white_24dp);
        }
        else {
            videoView.seekTo(1);
        }
    }
}
