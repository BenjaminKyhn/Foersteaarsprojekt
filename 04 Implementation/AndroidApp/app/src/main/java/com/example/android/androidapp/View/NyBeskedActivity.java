package com.example.android.androidapp.View;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.android.androidapp.R;
/**@author Patrick**/
public class NyBeskedActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ny_besked);
        configureSendButton();
    }
    private void configureSendButton() {
        Button changeActivityButton = findViewById(R.id.sendButton);
        changeActivityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(NyBeskedActivity.this, SendBeskedActivity.class));
            }
        });
    }
}
