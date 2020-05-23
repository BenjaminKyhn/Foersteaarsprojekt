package com.example.android.androidapp.view;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.example.android.androidapp.R;
/**@author Patrick**/
public class MenuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_menu);
        configureIndbakkeButton();
        configureBurgerButton();
    }
    private void configureIndbakkeButton() {
        Button changeActivityButton = findViewById(R.id.BeskederButton);
        changeActivityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MenuActivity.this, indbakkeActivity.class));
            }
        });
    }
    private void configureBurgerButton() {
        ImageView changeActivityButton = findViewById(R.id.burgerMenu);
        changeActivityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MenuActivity.this, NaviView.class));
            }
        });
    }
}
