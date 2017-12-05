package com.arrive.conor.arrive;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

public class SilenceAlarmFragment extends DialogFragment implements View.OnClickListener {
    Button math, shake, picture, barcode;
    Communicator communicator;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        communicator= (Communicator) activity;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_silence_method, null);
        math = (Button) view.findViewById(R.id.btn_math_sums);
        math.setOnClickListener(this);

        shake = (Button) view.findViewById(R.id.btn_shake);
        shake.setOnClickListener(this);

        picture = (Button) view.findViewById(R.id.btn_camera);
        picture.setOnClickListener(this);

        barcode = (Button) view.findViewById(R.id.btn_barcode);
        barcode.setOnClickListener(this);

        setCancelable(false);
        return view;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_math_sums:
                communicator.onSilenceMethodSelected("Math Sums");
                dismiss();
                break;
            case R.id.btn_shake:
                communicator.onSilenceMethodSelected("Shake To Wake");
                dismiss();
                break;
            case R.id.btn_camera:
                communicator.onSilenceMethodSelected("Take a Picture");
                dismiss();
                break;
            case R.id.btn_barcode:
                communicator.onSilenceMethodSelected("Scan a Barcode");
                dismiss();
                break;
        }
    }

    interface Communicator {
        void onSilenceMethodSelected(String message);
    }
}
