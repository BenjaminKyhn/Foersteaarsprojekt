package com.example.android.androidapp.ui;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import com.example.android.androidapp.R;
import com.example.android.androidapp.database.DatabaseManager;
import com.example.android.androidapp.entities.Chat;
import com.example.android.androidapp.entities.exceptions.BrugerFindesIkkeException;
import com.example.android.androidapp.entities.exceptions.ForMangeTegnException;
import com.example.android.androidapp.entities.exceptions.TomBeskedException;
import com.example.android.androidapp.entities.exceptions.TomEmneException;
import com.example.android.androidapp.model.BeskedFacade;
import com.example.android.androidapp.model.BrugerFacade;

import java.util.ArrayList;

public class VideoActivity extends AppCompatActivity implements VideoFeedbackDialog.VideoFeedbackListener {
    private VideoView videoView;
    private SeekBar seekBar;
    private ImageButton playPause;
    private Handler handler;
    private String videoNavn;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);

        videoView = findViewById(R.id.videoView);
        seekBar = findViewById(R.id.seekBar);
        playPause = findViewById(R.id.play_pause);

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

        videoNavn = getIntent().getStringExtra("videoNavn");

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

    public void skrivFeedback(View view) {
        DialogFragment skrivFeedback = new VideoFeedbackDialog();
        skrivFeedback.show(getSupportFragmentManager(), "skrivFeedback");
    }

    @Override
    public void feedback(String behandler, String besked) {
        BrugerFacade brugerFacade = BrugerFacade.hentInstans();
        BeskedFacade beskedFacade = BeskedFacade.hentInstans();
        beskedFacade.setBrugerManager(brugerFacade.hentBrugerManager());
        DatabaseManager databaseManager = new DatabaseManager();

        String afsender = brugerFacade.hentAktivBruger().getNavn();

        ArrayList<Chat> list = beskedFacade.hentNuvaerendeListe();

        for (Chat chat : list) {
            if (chat.getAfsender().equals(brugerFacade.hentAktivBruger().getNavn())) {
                if (chat.getModtager().equals(behandler)) {
                    if (chat.getEmne().equals("Feedback til " + videoNavn + " øvelse")) {
                        try {
                            beskedFacade.sendBesked(besked, chat, afsender, behandler);
                            databaseManager.opdaterChat(chat);
                            Toast.makeText(this, "Feedback er blevet sent", Toast.LENGTH_LONG).show();
                            return;
                        } catch (TomBeskedException e) {
                            Toast.makeText(this, "Beskeden må ikke være tom", Toast.LENGTH_LONG).show();
                        } catch (ForMangeTegnException e) {
                            Toast.makeText(this, "Beskeden må ikke være over 160 tegn", Toast.LENGTH_LONG).show();
                        }
                    }
                }
            }
        }

        try {
            beskedFacade.tjekBesked(besked);
        } catch (TomBeskedException e) {
            Toast.makeText(this, "Beskeden må ikke være tom", Toast.LENGTH_LONG).show();
            return;
        } catch (ForMangeTegnException e) {
            Toast.makeText(this, "Beskeden må ikke være over 160 tegn", Toast.LENGTH_LONG).show();
            return;
        }

        try {
            beskedFacade.opretChat(afsender, behandler, "Feedback til " + videoNavn + " øvelse");
        } catch (BrugerFindesIkkeException | TomEmneException | ForMangeTegnException e) {
            e.printStackTrace();
        }

        Chat nyChat = list.get(list.size() - 1);

        try {
            beskedFacade.sendBesked(besked, nyChat, afsender, behandler);
        } catch (TomBeskedException | ForMangeTegnException e) {
            e.printStackTrace();
        }

        databaseManager.gemChat(nyChat);
        Toast.makeText(this, "Feedback er blevet sent", Toast.LENGTH_LONG).show();
    }
}
