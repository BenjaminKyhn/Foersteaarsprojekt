package com.example.android.androidapp.view;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.android.androidapp.R;
/**@author Kelvin**/
public class BesvarBeskedActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_besvar_besked);
        configuresendButton();
    }
    /**@author Patrick**/
    private void configuresendButton() {
        Button changeActivityButton = findViewById(R.id.sendButton);
        changeActivityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(BesvarBeskedActivity.this, SendBeskedActivity.class));
            }
        });
    }
}
