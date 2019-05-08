package com.example.otocashz;


import android.app.Application;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.cashlez.android.sdk.CLErrorResponse;
import com.cashlez.android.sdk.login.CLLoginHandler;
import com.cashlez.android.sdk.login.CLLoginResponse;
import com.cashlez.android.sdk.login.ICLLoginService;


public class UserActivity extends AppCompatActivity implements ICLLoginService {

    private static final String TAG = "UserActivity";

    private TextView greetingTextView;
    private Button btnLogOut;

    private CLLoginHandler loginHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        Bundle bundle = getIntent().getExtras();
        String user = bundle.getString("AccessToken");

        greetingTextView = findViewById(R.id.greeting_text_view);
        btnLogOut = findViewById(R.id.btnLogOut);
        greetingTextView.setText(user);

        loginHandler = new CLLoginHandler(this, this);

        executeLogin();

    }


    public void goLogOut(View view) {
        executeLogOut();
    }


    public void goConfig(View view) {
        Toast.makeText(getApplicationContext(), "Device Configuration Test", Toast.LENGTH_SHORT).show();
        Log.d(TAG, "Config Device");
        Intent i = new Intent(getApplicationContext(), ConfigActivity.class);
        startActivity(i);
    }

    private void executeGetUserInfo() {
        //not Implemented Yet
    }

    private void executeLogin() {
        Log.d(TAG, "Login Device Initialized");
        loginHandler.doLogin("Oto", "123456");
    }

    private void executeLogOut() {
        Log.d(TAG, "LogOut Device");
        Intent i = new Intent(getApplicationContext(), Login.class);
        startActivity(i);
    }

    @Override
    public void onStartActivation(String s) {
        //not implementing
    }

    @Override
    public void onLoginSuccess(CLLoginResponse clLoginResponse) {
        onLoginOK();
    }

    public void onLoginOK() {
        Toast.makeText(getApplicationContext(), "Device Login Accepted", Toast.LENGTH_SHORT).show();
        Log.d(TAG, "Device Login Accepted");
    }

    @Override
    public void onLoginError(CLErrorResponse clErrorResponse) {
        onLoginFailed(clErrorResponse.getErrorMessage());
    }

    public void onLoginFailed(String failedMessage) {
        Toast.makeText(getApplicationContext(), failedMessage, Toast.LENGTH_SHORT).show();
        Log.d(TAG, "Login Device Failed");
        Log.d(TAG, failedMessage);
        executeLogOut();
    }

    @Override
    public void onNewVersionAvailable(CLErrorResponse clErrorResponse) {
        //not implementing
    }

    @Override
    public void onApplicationExpired(CLErrorResponse clErrorResponse) {
        //not implementing
    }

}


