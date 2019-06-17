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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Login extends AppCompatActivity {

    private static final String TAG = "LoginActivity";
    //private static final String URL_FOR_LOGIN = "http://xcalibre.site/ale/login.php";
    private static final String URL_FOR_LOGIN = "http://sagmicddev01.oto.co.id:54321/api/Authenticate/Login";
    private static final String URL_FOR_UID = "http://sagmicddev01.oto.co.id:996/api/userinfo/role?uid=";
    private static final String URL_FOR_MASTERTYPE = "http://sagmicddev01.oto.co.id:996/api/master/contracttype";
    private static final String OAUTH_CLIENT_ID = "client1";
    private static final String OAUTH_CLIENT_SECRET = "12345678";
    ProgressDialog progressDialog;
    private EditText loginInputUserName, loginInputPassword;

    private JSONArray ContractType;

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

    public void onLogin(View view) {
        if (loginInputUserName.getText().toString().equalsIgnoreCase("") || loginInputPassword.getText().toString().equalsIgnoreCase("")) {
            if (loginInputUserName.getText().toString().equalsIgnoreCase("")) {
                loginInputUserName.setHint(getResources().getString(R.string.err_mandatory));
                loginInputUserName.setError(getResources().getString(R.string.err_mandatory));
            }
            if (loginInputPassword.getText().toString().equalsIgnoreCase("")) {
                loginInputPassword.setHint(getResources().getString(R.string.err_mandatory));
                loginInputPassword.setError(getResources().getString(R.string.err_mandatory));
            }
        } else {
            loginUserPOSTWithRawBody(loginInputUserName.getText().toString(), loginInputPassword.getText().toString());
            //loginDummy(loginInputUserName.getText().toString(), loginInputPassword.getText().toString());
        }
    }

    private void showDialog() {
        if (!progressDialog.isShowing())
            progressDialog.show();
    }
    private void hideDialog() {
        if (progressDialog.isShowing())
            progressDialog.dismiss();
    }

    private void loginDummy(final String username, final String password) {
        Log.d(TAG, "Dummy Test");
        Intent intent = new Intent(Login.this, UserActivity.class);
        intent.putExtra("AccessToken", username);
        intent.putExtra("Uid", password);
        startActivity(intent);
    }

    private void getMasterType() {
        Log.d(TAG, "get Master Type");

        String cancel_req_tag = "TYPE";
        progressDialog.setMessage("Getting type definition...");
        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.GET, URL_FOR_MASTERTYPE, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, response);
                try {
                    JSONObject jObj = new JSONObject(response);
                    if (jObj.optBoolean("Success"))
                    {
                        ContractType = jObj.getJSONArray("Result");
                        hideDialog();
                    } else {
                        hideDialog();
                        Toast.makeText(getApplicationContext(), jObj.optString("Msg"), Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    hideDialog();
                    Toast.makeText(getApplicationContext(), e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                    Log.d(TAG, e.toString());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                hideDialog();
                Toast.makeText(getApplicationContext(), error.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                Log.d(TAG, error.toString());
            }
        });
        // Adding request to request queue
        AppSingleton.getInstance(getApplicationContext()).addToRequestQueue(strReq,cancel_req_tag);
    }

    private void loginUIDGET(final String uid) {
        Log.d(TAG, "get User Info via UID");

        String cancel_req_tag = "UID";
        progressDialog.setMessage("Getting user info...");
        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.GET, URL_FOR_UID+uid, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, response);
                try {
                    JSONObject jObj = new JSONObject(response);
                    if (jObj.optBoolean("Success"))
                    {
                        JSONObject jResult = jObj.getJSONObject("Result");

                        Log.d(TAG, "get token: " + jObj.toString());
                        String jsoCompName = jResult.getString("CompName");
                        String jsoBranchCode = jResult.getString("BranchCode");
                        String jsoBranchName = jResult.getString("BranchName");
                        String jsoModulId = jResult.getString("ModulId");
                        String jsoModulSubId = jResult.getString("ModulSubId");
                        String jsoRoleLevel = jResult.getString("RoleLevel");
                        String jsoRoleName = jResult.getString("RoleName");
                        String jsoUserName = jResult.getString("UserName");
                        String jsoCashlezUser = jResult.getString("CashlezUser");
                        String jsoCashlezPwd = jResult.getString("CashlezPwd");

                        //Launch User activity
                        Intent intent = new Intent(Login.this, UserActivity.class);
                        intent.putExtra("CompName", jsoCompName);
                        intent.putExtra("BranchCode", jsoBranchCode);
                        intent.putExtra("BranchName", jsoBranchName);
                        intent.putExtra("ModulId", jsoModulId);
                        intent.putExtra("ModulSubId", jsoModulSubId);
                        intent.putExtra("RoleLevel", jsoRoleLevel);
                        intent.putExtra("RoleName", jsoRoleName);
                        intent.putExtra("UserName", jsoUserName);
                        intent.putExtra("CashlezUser", jsoCashlezUser);
                        intent.putExtra("CashlezPwd", jsoCashlezPwd);
                        intent.putExtra("ContractType", ContractType.toString());
                        startActivity(intent);
                        //finish();
                        hideDialog();
                    } else {
                        hideDialog();
                        Toast.makeText(getApplicationContext(), jObj.optString("Msg"), Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    hideDialog();
                    Toast.makeText(getApplicationContext(), e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                    Log.d(TAG, e.toString());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                hideDialog();
                Toast.makeText(getApplicationContext(), error.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                Log.d(TAG, error.toString());
            }
        });
        // Adding request to request queue
        AppSingleton.getInstance(getApplicationContext()).addToRequestQueue(strReq,cancel_req_tag);
    }

    private void loginUserPOSTWithRawBody(final String username, final String password) {
        Log.d(TAG, "login User POST With Raw Body");
        progressDialog.setMessage("Logging you in...");
        showDialog();
        try {
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            JSONObject jsonBody = new JSONObject();
            jsonBody.put("userName", username);
            jsonBody.put("password", password);
            jsonBody.put("clientID", OAUTH_CLIENT_ID);
            jsonBody.put("clientSecret", OAUTH_CLIENT_SECRET);
            final String mRequestBody = jsonBody.toString();

            StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_FOR_LOGIN, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    //hideDialog();
                    Log.d(TAG, response);
                    try {
                        JSONObject jObj = new JSONObject(response);
                        if (jObj.optBoolean("Success"))
                        {
                            JSONObject jResult = jObj.getJSONObject("Result");
                            JSONObject jTtoken = jResult.getJSONObject("Token");

                            Log.d(TAG, "get token: " + jTtoken.getString("AccessToken"));
                            hideDialog();

                            getMasterType();
                            // Launch User activity
                            loginUIDGET(jTtoken.getString("Uid"));
                        } else {
                            Toast.makeText(getApplicationContext(), "Login Error: " + jObj.optString("Msg"), Toast.LENGTH_LONG).show();
                        }
                    } catch (JSONException e) {
                        hideDialog();
                        Toast.makeText(getApplicationContext(), "Login Error: " + e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    hideDialog();
                    Toast.makeText(getApplicationContext(), error.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                    Log.d(TAG, error.toString());
                }
            }) {
                @Override
                public String getBodyContentType() {
                    return "application/json; charset=utf-8";
                }

                @Override
                public byte[] getBody() {
                    return mRequestBody == null ? null : mRequestBody.getBytes(StandardCharsets.UTF_8);
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
            hideDialog();
            Toast.makeText(getApplicationContext(), "Login Error: " + e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }

    private void loginUserPOSTWithKeyMap( final String username, final String password) {
        Log.d(TAG, "login User POST With KeyMap");

        String cancel_req_tag = "login";
        progressDialog.setMessage("Logging you in...");
        showDialog();
        StringRequest strReq = new StringRequest(Request.Method.POST, URL_FOR_LOGIN, new Response.Listener<String>() {

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
                    Toast.makeText(getApplicationContext(), e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG, "Login Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(), "Login Error: " + error.getLocalizedMessage(), Toast.LENGTH_LONG).show();
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
                params.put("client_id", OAUTH_CLIENT_ID);
                params.put("client_secret", OAUTH_CLIENT_SECRET);
                //params.put("redirect_uri", "http://");
                return params;
            }

        };
        // Adding request to request queue
        AppSingleton.getInstance(getApplicationContext()).addToRequestQueue(strReq,cancel_req_tag);
    }

}
