package com.example.android.androidapp.ui;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.android.androidapp.R;
import com.example.android.androidapp.database.DatabaseManager;
import com.example.android.androidapp.entities.Chat;
import com.example.android.androidapp.entities.exceptions.BrugerFindesIkkeException;
import com.example.android.androidapp.entities.exceptions.ForMangeTegnException;
import com.example.android.androidapp.entities.exceptions.TomBeskedException;
import com.example.android.androidapp.entities.exceptions.TomEmneException;
import com.example.android.androidapp.model.BeskedFacade;
import com.example.android.androidapp.model.BrugerFacade;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import javax.net.ssl.HttpsURLConnection;

/**@author Tommy**/
public class VideoActivity extends AppCompatActivity implements VideoFeedbackDialog.VideoFeedbackListener {
    private VideoView videoView;
    private SeekBar seekBar;
    private ImageButton playPause;
    private Handler handler;
    private String videoNavn;
    private File file;
    private Uri uri;
    private VideoViewModel videoViewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);

        videoView = findViewById(R.id.videoView);
        seekBar = findViewById(R.id.seekBar);
        playPause = findViewById(R.id.play_pause);
        videoViewModel = new ViewModelProvider(this).get(VideoViewModel.class);

        if (videoViewModel.getFile() == null && videoViewModel.getUri() == null) {
            ProgressBar progressBar = findViewById(R.id.progressBar);
            progressBar.setVisibility(View.VISIBLE);

            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    String videoURL = getIntent().getStringExtra("videoURL");
                    try {
                        file = File.createTempFile("oevelseVideo", ".mp4");
                        file.deleteOnExit();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    try {
                        downloadFil(videoURL);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    uri = Uri.fromFile(file);

                    videoViewModel.setFile(file);
                    videoViewModel.setUri(uri);
                    runOnUiThread(() -> faerdigloadetMedie());
                }
            });
            thread.start();
        }
        else {
            faerdigloadetMedie();
        }

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
        } else {
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

    public void downloadFil(String fileURL) throws IOException {
        URL url = new URL(fileURL);
        HttpsURLConnection httpsConn = (HttpsURLConnection) url.openConnection();
        int responseCode = httpsConn.getResponseCode();

        if (responseCode == HttpURLConnection.HTTP_OK) {

            InputStream inputStream = httpsConn.getInputStream();
            FileOutputStream outputStream = new FileOutputStream(file);

            int bytesRead = -1;
            byte[] buffer = new byte[1024];
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }

            outputStream.close();
            inputStream.close();

        } else {
            System.out.println("Ingen fil at downloade. Serveren svarede med HTTPS code: " + responseCode);
        }
        httpsConn.disconnect();
    }

    private void faerdigloadetMedie() {
        ProgressBar progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);
        videoView.setVideoURI(videoViewModel.getUri());
        videoView.start();
        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                videoView.seekTo(1);
                videoView.pause();
                seekBar.setMax(videoView.getDuration() / 1000);
                seekBar.setProgress(0);
            }
        });

        videoNavn = getIntent().getStringExtra("videoNavn");

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (videoView != null && fromUser) {
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
    }

    public static class VideoViewModel extends ViewModel {
        private File file;
        private Uri uri;

        public File getFile() {
            return file;
        }

        public void setFile(File file) {
            this.file = file;
        }

        public Uri getUri() {
            return uri;
        }

        public void setUri(Uri uri) {
            this.uri = uri;
        }
    }
}
