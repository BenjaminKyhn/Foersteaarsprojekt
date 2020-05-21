package com.example.android.androidapp.View;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.android.androidapp.R;

public class LaesBeskedActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_laes_besked);
        configureBesvarBeskedButton();
    }

    private void configureBesvarBeskedButton() {
        TextView changeActivityButton = findViewById(R.id.besvarButton);
        changeActivityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LaesBeskedActivity.this, BesvarBeskedActivity.class));
            }
        });
    }
}