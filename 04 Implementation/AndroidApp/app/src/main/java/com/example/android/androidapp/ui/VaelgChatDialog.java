package com.example.android.androidapp.ui;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.example.android.androidapp.R;
import com.example.android.androidapp.entities.exceptions.BrugerFindesIkkeException;
import com.example.android.androidapp.model.BrugerFacade;

import java.util.ArrayList;

public class VaelgChatDialog extends DialogFragment {
    private Spinner spinner;
    private EditText editTextVaelgEmne;
    private VaelgChatListener listener;


    @SuppressLint("InflateParams")
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_ny_samtale, null);

        spinner = view.findViewById(R.id.spinner);

        ArrayList<String> behandlere = BrugerFacade.hentInstans().hentBehandlereNavne();

        ArrayAdapter<String> adapter = new ArrayAdapter<>((Context) listener, R.layout.support_simple_spinner_dropdown_item, behandlere);
        spinner.setAdapter(adapter);

        editTextVaelgEmne = view.findViewById(R.id.editTextVaelgEmne);

        Button fortryd = view.findViewById(R.id.button_cancel);
        Button bekraeft = view.findViewById(R.id.button_confirm);

        fortryd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        bekraeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    bekraeft();
                } catch (BrugerFindesIkkeException e) {
                    e.printStackTrace();
                    // TODO lav en fejlbesked
                }
                dismiss();
            }
        });


        builder.setView(view).setTitle("Ny samtale");

        return builder.create();

    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        listener = (VaelgChatListener) context;
    }

    public void bekraeft() throws BrugerFindesIkkeException {
        String modtager = spinner.getSelectedItem().toString();
        String emne = editTextVaelgEmne.getText().toString();
        listener.nySamtale(modtager, emne);
    }

    interface VaelgChatListener {
        void nySamtale(String modtager, String emne) throws BrugerFindesIkkeException;
    }
}
