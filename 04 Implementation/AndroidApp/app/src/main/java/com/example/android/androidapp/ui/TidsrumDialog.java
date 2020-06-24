package com.example.android.androidapp.ui;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.android.androidapp.R;

public class TidsrumDialog extends DialogFragment {
    TextView t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12;
    TidsrumDialogListener listener;

    @SuppressLint("InflateParams")
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_tidsrum, null);

        t1 = view.findViewById(R.id.tid1);
        t2 = view.findViewById(R.id.tid2);
        t3 = view.findViewById(R.id.tid3);
        t4 = view.findViewById(R.id.tid4);
        t5 = view.findViewById(R.id.tid5);
        t6 = view.findViewById(R.id.tid6);
        t7 = view.findViewById(R.id.tid7);
        t8 = view.findViewById(R.id.tid8);
        t9 = view.findViewById(R.id.tid9);
        t10 = view.findViewById(R.id.tid10);
        t11 = view.findViewById(R.id.tid11);
        t12 = view.findViewById(R.id.tid12);

        tilfoejListener(t1);
        tilfoejListener(t2);
        tilfoejListener(t3);
        tilfoejListener(t4);
        tilfoejListener(t5);
        tilfoejListener(t6);
        tilfoejListener(t7);
        tilfoejListener(t8);
        tilfoejListener(t9);
        tilfoejListener(t10);
        tilfoejListener(t11);
        tilfoejListener(t12);

        builder.setView(view).setTitle("Vælg tidsrum");

        return builder.create();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        if (TidsrumDialogListener.class.isAssignableFrom(context.getClass())) {
            listener = (TidsrumDialogListener) context;
        }
    }

    private void tilfoejListener(TextView textView) {
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String start = "";
                String slut = "";
                switch (textView.getId()) {
                    case R.id.tid1:
                        start = "8:00";
                        slut = "8:45";
                        break;
                    case R.id.tid2:
                        start = "8:45";
                        slut = "9:15";
                        break;
                    case R.id.tid3:
                        start = "9:15";
                        slut = "10:00";
                        break;
                    case R.id.tid4:
                        start = "10:00";
                        slut = "10:45";
                        break;
                    case R.id.tid5:
                        start = "10:45";
                        slut = "11:15";
                        break;
                    case R.id.tid6:
                        start = "11:15";
                        slut = "12:00";
                        break;
                    case R.id.tid7:
                        start = "12:00";
                        slut = "12:45";
                        break;
                    case R.id.tid8:
                        start = "12:45";
                        slut = "13:15";
                        break;
                    case R.id.tid9:
                        start = "13:15";
                        slut = "14:00";
                        break;
                    case R.id.tid10:
                        start = "14:00";
                        slut = "14:45";
                        break;
                    case R.id.tid11:
                        start = "14:45";
                        slut = "15:15";
                        break;
                    case R.id.tid12:
                        start = "15:15";
                        slut = "16:00";
                        break;
                }
                valgteTidsrum(start, slut);
            }
        });
    }

    private void valgteTidsrum(String start, String slut) {
        listener.angivTidsrum(start, slut);
        dismiss();
    }

    interface TidsrumDialogListener {
        void angivTidsrum(String start, String slut);
    }
}
