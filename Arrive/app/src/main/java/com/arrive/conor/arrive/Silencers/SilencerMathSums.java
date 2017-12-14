package com.arrive.conor.arrive.Silencers;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.arrive.conor.arrive.AlarmReceiver;
import com.arrive.conor.arrive.MainActivity;
import com.arrive.conor.arrive.NavigationActivity;
import com.arrive.conor.arrive.R;
import com.arrive.conor.arrive.RingtoneService;

public class SilencerMathSums extends AppCompatActivity implements View.OnClickListener,
        View.OnFocusChangeListener {
    AlarmManager alarmManager;
    Intent stopAlarm, returnHome;
    PendingIntent pendingIntent;

    Button btnSubmit;
    TextView q1op1, q1op, q1op2, q1eq, q2op1, q2op, q2op2, q2eq, q3op1, q3op, q3op2, q3eq;
    EditText q1Attempt, q2Attempt, q3Attempt;

    int q1Answer, q2Answer, q3Answer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_silencer_math_sums);

        q1op1 = (TextView) findViewById(R.id.tvQ1OperandOne);
        q1op1.setText(randomNumber());
        q1op = (TextView) findViewById(R.id.tvQ1Operator);
        q1op2 = (TextView) findViewById(R.id.tvQ1OperandTwo);
        q1op2.setText(randomNumber());
        q1eq = (TextView) findViewById(R.id.tvQ1Equals);
        q1Attempt = (EditText) findViewById(R.id.etQ1Answer);
        q1Attempt.setOnFocusChangeListener(this);
        q1Attempt.setFocusedByDefault(true);

        q2op1 = (TextView) findViewById(R.id.tvQ2OperandOne);
        q2op1.setText(randomNumber());
        q2op = (TextView) findViewById(R.id.tvQ2Operator);
        q2op2 = (TextView) findViewById(R.id.tvQ2OperandTwo);
        q2op2.setText(randomNumber());
        q2eq = (TextView) findViewById(R.id.tvQ2Equals);
        q2Attempt = (EditText) findViewById(R.id.etQ2Answer);
        q2Attempt.setOnFocusChangeListener(this);

        q3op1 = (TextView) findViewById(R.id.tvQ3OperandOne);
        q3op1.setText(randomNumber());
        q3op = (TextView) findViewById(R.id.tvQ3Operator);
        q3op2 = (TextView) findViewById(R.id.tvQ3OperandTwo);
        q3op2.setText(randomNumber());
        q3eq = (TextView) findViewById(R.id.tvQ3Equals);
        q3Attempt = (EditText) findViewById(R.id.etQ3Answer);
        q3Attempt.setOnFocusChangeListener(this);

        btnSubmit = (Button) findViewById(R.id.btnSubmit);
        btnSubmit.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btnSubmit) {
            if (q1Attempt.getText().toString().equals("") ||
                    q2Attempt.getText().toString().equals("") ||
                    q3Attempt.getText().toString().equals(""))
                Toast.makeText(this, "Please Answer ALL Questions", Toast.LENGTH_SHORT).show();

            checkAnswerOne();
            checkAnswerTwo();
            checkAnswerThree();

            if (checkAnswerOne() && checkAnswerTwo() && checkAnswerThree()) {
                //If all answers are correct stop the alarm
                stopAlarm = new Intent(this, AlarmReceiver.class);

                alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

                stopAlarm.putExtra("startAlarm", false);
                pendingIntent = PendingIntent.getBroadcast(this, 0, stopAlarm,
                        PendingIntent.FLAG_UPDATE_CURRENT);
                alarmManager.set(AlarmManager.RTC_WAKEUP,
                        System.currentTimeMillis(),
                        pendingIntent);

                AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                builder.setMessage("Is Navigation Required?").setPositiveButton("Yes", dialogClickListener)
                        .setNegativeButton("No", dialogClickListener).show();
            }
        }
    }

    DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            switch (which){
                case DialogInterface.BUTTON_POSITIVE:
                    Intent startNavigation = new Intent(getApplicationContext(), NavigationActivity.class);
                    startActivity(startNavigation);
                    break;

                case DialogInterface.BUTTON_NEGATIVE:
                    returnHome = new Intent(SilencerMathSums.super.getApplicationContext(), MainActivity.class);
                    startActivity(returnHome);
                    break;
            }
        }
    };

    private boolean checkAnswerOne() {
        q1Answer = Integer.parseInt(q1op1.getText().toString()) +
                Integer.parseInt(q1op2.getText().toString());
        try {
            if (q1Answer == Integer.parseInt(q1Attempt.getText().toString())) {
                q1Attempt.setTextColor(Color.GREEN);
                return true;
            } else {
                q1Attempt.setTextColor(Color.RED);
                return false;
            }

        } catch (NumberFormatException e) {
            q1Attempt.setText("");
            return false;
        }
    }

    private boolean checkAnswerTwo() {
        q2Answer = Integer.parseInt(q2op1.getText().toString()) +
                Integer.parseInt(q2op2.getText().toString());
        try {
            if (q2Answer == Integer.parseInt(q2Attempt.getText().toString())) {
                q2Attempt.setTextColor(Color.GREEN);
                return true;
            } else {
                q2Attempt.setTextColor(Color.RED);
                return false;
            }
        } catch (NumberFormatException e) {
            q2Attempt.setText("");
            return false;
        }
    }

    private boolean checkAnswerThree() {
        q3Answer = Integer.parseInt(q3op1.getText().toString()) +
                Integer.parseInt(q3op2.getText().toString());
        try {
            if (q3Answer == Integer.parseInt(q3Attempt.getText().toString())) {
                q3Attempt.setTextColor(Color.GREEN);
                return true;
            } else {
                q3Attempt.setTextColor(Color.RED);
                return false;
            }
        }catch (NumberFormatException e) {
            q3Attempt.setText("");
            return false;
        }
    }

    private String randomNumber() {
        int random = (int) (Math.random() * 50 + 1);
        String randomString = String.valueOf(random);
        return randomString;
    }

    @Override
    public void onFocusChange(View view, boolean b) {
        switch (view.getId()){
            case R.id.etQ1Answer:
                checkAnswerOne();
                break;
            case R.id.etQ2Answer:
                checkAnswerTwo();
                break;
            case R.id.etQ3Answer:
                checkAnswerThree();
                break;
        }
    }
}
