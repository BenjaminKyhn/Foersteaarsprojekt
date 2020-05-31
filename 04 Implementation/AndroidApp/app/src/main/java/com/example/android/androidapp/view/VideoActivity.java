package com.example.android.androidapp.view;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.widget.MediaController;
import android.widget.RatingBar;
import android.widget.VideoView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.android.androidapp.R;

public class VideoActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);

        final ProgressDialog progressDialog = ProgressDialog.show(this, "", "Indlæser video", true);

        final VideoView videoView = findViewById(R.id.videoView);
        String videoPath = "android.resource://" + getPackageName() + "/" + R.raw.hofteboejer;
        Uri uri = Uri.parse(videoPath);
        videoView.setVideoURI(uri);
        videoView.start();
        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                videoView.seekTo(1);
                videoView.pause();
                progressDialog.dismiss();
            }
        });

        RatingBar ratingBar = findViewById(R.id.ratingBar);
        Drawable progress = ratingBar.getProgressDrawable();
        progress.setTint(Color.YELLOW);

        MediaController mediaController = new MediaController(this) {
            @Override
            public void show() {
                super.show(0);
            }

            @Override
            public void hide() {
                super.show(0);
            }
        };
        videoView.setMediaController(mediaController);
        mediaController.setAnchorView(videoView);

    }
}
