package com.example.android.androidapp.View;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.android.androidapp.R;

/**@author Kelvin**/
public class indbakkeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_indbakke);
        configureLaesBeskedButton();
        configureBurgerButton();
    }
    private void configureLaesBeskedButton() {
        TextView changeActivityButton1 = findViewById(R.id.beskedEmne1);
        changeActivityButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(indbakkeActivity.this, LaesBeskedActivity.class));
            }
        });
        TextView changeActivityButton2 = findViewById(R.id.besked1);
        changeActivityButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(indbakkeActivity.this, LaesBeskedActivity.class));
            }
        });
        TextView changeActivityButton3 = findViewById(R.id.beskedEmne2);
        changeActivityButton3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(indbakkeActivity.this, LaesBeskedActivity.class));
            }
        });
        TextView changeActivityButton4 = findViewById(R.id.besked2);
        changeActivityButton4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(indbakkeActivity.this, LaesBeskedActivity.class));
            }
        });
    }
    private void configureBurgerButton() {
        ImageView changeActivityButton = findViewById(R.id.burgerMenu);
        changeActivityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(indbakkeActivity.this, NaviView.class));
            }
        });
    }
}
