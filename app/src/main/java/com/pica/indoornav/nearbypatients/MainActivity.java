package com.pica.indoornav.nearbypatients;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.view.View;
import android.widget.Button;

/**
 * Created by Anj on 4 Sep 2016.
 */
public class MainActivity extends Activity {

    Button btnLogin;
    TextInputLayout tilUsername;
    TextInputEditText editUsername;
    TextInputLayout tilPassword;
    TextInputEditText editPassword;

    boolean isSuccessful = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnLogin = (Button) findViewById(R.id.login);
        tilUsername = (TextInputLayout) findViewById(R.id.username_input_layout);
        editUsername = (TextInputEditText) findViewById(R.id.username);
        tilPassword = (TextInputLayout) findViewById(R.id.password_input_layout);
        editPassword = (TextInputEditText) findViewById(R.id.password);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!isEmpty(editUsername) & !isEmpty(editPassword)) {
                    isSuccessful = verifyUser();

                    if(isSuccessful) {
                        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
                        pref.edit().putBoolean("isLoggedIn", isSuccessful).apply();

                        startPatientsActivity();
                    } else {
                        // TODO: handle not verified accounts here
                    }
                } else {
                    tilUsername.setError("Username is required!");
                    tilPassword.setError("Password is required!");
                }
            }
        });
    }

    private boolean verifyUser() {
        // TODO: connect to server here

        return true;
    }

    private boolean isEmpty(TextInputEditText editText) {
        return editText.getText().toString().trim().length() == 0;
    }

    private void startPatientsActivity() {
        Intent intent = new Intent(MainActivity.this, PatientsActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();

        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
        boolean isLoggedIn = pref.getBoolean("isLoggedIn", false);

        if(isLoggedIn)
            startPatientsActivity();
    }
}
