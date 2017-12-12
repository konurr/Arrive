package com.arrive.conor.arrive;

import android.app.Activity;
import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class InputDestinationFragment extends DialogFragment implements View.OnClickListener {
    EditText streetNum, streetName, city, postcode;
    EditText[] fieldGroup = new EditText[4];
    Button btnSubmit;
    InputDestinationFragment.Communicator communicator;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        communicator = (InputDestinationFragment.Communicator) activity;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_input_destination, null);

        streetNum = (EditText) view.findViewById(R.id.etStreetNum);
        streetName = (EditText) view.findViewById(R.id.etStreetName);
        city = (EditText) view.findViewById(R.id.etCity);
        postcode = (EditText) view.findViewById(R.id.etPostcode);
        fieldGroup[0] = streetNum;
        fieldGroup[1] = streetName;
        fieldGroup[2] = city;
        fieldGroup[3] = postcode;


        btnSubmit = (Button) view.findViewById(R.id.btnDestinationSubmit);
        btnSubmit.setOnClickListener(this);

        setCancelable(false);
        return view;
    }

    @Override
    public void onClick(View view) {
        if (noEmptyFields(fieldGroup)) {
            communicator.setDestination(
                    streetNum.getText().toString(),
                    streetName.getText().toString(),
                    city.getText().toString(),
                    postcode.getText().toString()
            );
            dismiss();
        } else {
            Toast.makeText(getContext(), "Please fill in all fields", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean noEmptyFields(EditText[] fields){
        for(int i = 0; i < fields.length; i++){
            EditText currentField = fields[i];
            if(TextUtils.isEmpty(currentField.getText().toString().trim())){
                return false;
            }
        }
       return true;
    }

    interface Communicator {
        void setDestination(String streetNumber, String streetName, String city, String postcode);
    }
}
