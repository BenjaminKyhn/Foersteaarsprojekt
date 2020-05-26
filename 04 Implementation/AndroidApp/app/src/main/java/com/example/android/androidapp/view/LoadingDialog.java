package com.example.android.androidapp.view;

import android.app.Activity;
import android.os.Bundle;

import androidx.annotation.Nullable;

import com.example.android.androidapp.R;

public class LoadingDialog extends Activity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loading_dialog);
    }
}
