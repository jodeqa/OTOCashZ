package com.example.otocashz;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class Login extends AppCompatActivity {

    private static final String TAG = "LoginActivity";
    //private static final String URL_FOR_LOGIN = "http://xcalibre.site/ale/login.php";
    private static final String URL_FOR_LOGIN = "http://sagmicddev01.oto.co.id:54321/api/Authenticate/Login";
    ProgressDialog progressDialog;
    private EditText loginInputUserName, loginInputPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginInputUserName = findViewById(R.id.login_input_username);
        loginInputPassword = findViewById(R.id.login_input_password);

        // Progress dialog
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
    }

    private void showDialog() {
        if (!progressDialog.isShowing())
            progressDialog.show();
    }
    private void hideDialog() {
        if (progressDialog.isShowing())
            progressDialog.dismiss();
    }

    private void loginUserPOSTWithRawBody(final String username, final String password) {
        try {
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            JSONObject jsonBody = new JSONObject();
            jsonBody.put("userName", username);
            jsonBody.put("password", password);
            jsonBody.put("clientID", "client1");
            jsonBody.put("clientSecret", "12345678");
            final String mRequestBody = jsonBody.toString();

            StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_FOR_LOGIN, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    hideDialog();
                    Log.d(TAG, response);
                    try {

                        JSONObject jObj = new JSONObject(response);
                        JSONObject jResult = jObj.getJSONObject("Result");
                        JSONObject jTtoken = jResult.getJSONObject("Token");

                        Log.d(TAG, "get token: " + jTtoken.getString("AccessToken"));

                        // Launch User activity
                        Intent intent = new Intent(Login.this, UserActivity.class);
                        intent.putExtra("AccessToken", jTtoken.getString("AccessToken"));
                        intent.putExtra("Uid", jTtoken.getString("Uid"));
                        startActivity(intent);
                        finish();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    hideDialog();
                    Log.d(TAG, error.toString());
                }
            }) {
                @Override
                public String getBodyContentType() {
                    return "application/json; charset=utf-8";
                }

                @Override
                public byte[] getBody() {
                    try {
                        return mRequestBody == null ? null : mRequestBody.getBytes("utf-8");
                    } catch (UnsupportedEncodingException uee) {
                        VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", mRequestBody, "utf-8");
                        return null;
                    }
                }

//                @Override
//                protected Response<String> parseNetworkResponse(NetworkResponse response) {
//                    String responseString = "";
//                    if (response != null) {
//                        responseString = String.valueOf(response.statusCode);
//                    }
//                    return Response.success(responseString, HttpHeaderParser.parseCacheHeaders(response));
//                }
            };

            requestQueue.add(stringRequest);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void loginUserPOSTWithKeyMap( final String username, final String password) {
        // Tag used to cancel the request
        String cancel_req_tag = "login";
        progressDialog.setMessage("Logging you in...");
        showDialog();
        StringRequest strReq = new StringRequest(Request.Method.POST,
                URL_FOR_LOGIN, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Register Response: " + response);
                hideDialog();
                try {
                    JSONObject jObj = new JSONObject(response);
                    Log.d(TAG, "get token: " + jObj.getString("access_token"));
                    String user = jObj.getString("access_token");
                    // Launch User activity
                    Intent intent = new Intent(
                            Login.this,
                            UserActivity.class);
                    intent.putExtra("username", user);
                    startActivity(intent);
                    finish();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Login Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
                hideDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting params to login url
                Map<String, String> params = new HashMap<>();
                //params.put("grant_type", "password");
                params.put("username", username);
                params.put("password", password);
                params.put("client_id", "client1");
                params.put("client_secret", "12345678");
                //params.put("redirect_uri", "http://");
                return params;
            }

        };
        // Adding request to request queue
        AppSingleton.getInstance(getApplicationContext()).addToRequestQueue(strReq,cancel_req_tag);
    }

    public void onLogin(View view) {
        loginUserPOSTWithRawBody(loginInputUserName.getText().toString(), loginInputPassword.getText().toString());
    }
}
