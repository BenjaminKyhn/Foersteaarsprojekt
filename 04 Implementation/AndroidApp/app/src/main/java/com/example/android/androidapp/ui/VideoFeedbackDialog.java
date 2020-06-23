package com.example.android.androidapp.ui;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.android.androidapp.R;
import com.example.android.androidapp.model.BrugerFacade;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;

/**@author Tommy**/
public class VideoFeedbackDialog extends DialogFragment {
    Context context;
    Spinner spinner;
    TextInputEditText textInputEditText;
    VideoFeedbackListener listener;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        View view = View.inflate(context, R.layout.dialog_video_feedback, null);

        spinner = view.findViewById(R.id.behandlerSpinner);
        textInputEditText = view.findViewById(R.id.beskedfelt);

        ArrayList<String> behandlere = BrugerFacade.hentInstans().hentBehandlereNavne();

        ArrayAdapter<String> adapter = new ArrayAdapter<>(context, R.layout.support_simple_spinner_dropdown_item, behandlere);
        spinner.setAdapter(adapter);

        Button bekraeft = view.findViewById(R.id.bekraeft);
        Button fortryd = view.findViewById(R.id.fortryd);

        bekraeft.setOnClickListener(v -> {
            bekraeft();
            dismiss();
        });
        fortryd.setOnClickListener(v -> dismiss());

        builder.setView(view);
        return builder.create();
    }

    public void bekraeft() {
        String behandler = spinner.getSelectedItem().toString();
        String besked = textInputEditText.getText().toString();

        listener.feedback(behandler, besked);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        this.context = context;
        if (VideoFeedbackListener.class.isAssignableFrom(context.getClass())) {
            listener = (VideoFeedbackListener) context;
        }
    }

    interface VideoFeedbackListener {
        void feedback(String behandler, String besked);
    }
}
