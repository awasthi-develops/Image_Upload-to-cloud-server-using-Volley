package com.example.divyanshi.myapplication;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewParent;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
 Button lg;

    //TextView tx1;
    //private RequestQueue requestQueue;
    private static final String URL_LOGIN = "http://192.168.42.220:80/login_new.php" ;
    private final Context ctx_login = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        lg = (Button) findViewById(R.id.blogin);
        final EditText etemail = (EditText) findViewById(R.id.etEmail);
        final EditText etpassword = (EditText) findViewById(R.id.etPassword);
        final TextView registerhere = (TextView)findViewById(R.id.tvRegisterlink);
        //tx1 = (TextView) findViewById(R.id.textView2);
        //tx1.setVisibility(View.GONE);

        registerhere.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent register_intent = new Intent(MainActivity.this,register.class);
                MainActivity.this.startActivity(register_intent);
            }
        });

        //requestQueue = Volley.newRequestQueue(this);
        lg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               StringRequest login_request = new StringRequest(Request.Method.POST, URL_LOGIN, new Response.Listener<String>(){
                @Override
                 public void onResponse(String response){
                    try{
                        JSONObject jsonObject = new JSONObject(response);
                        //boolean success = jsonObject.getBoolean("success");
                        if(jsonObject.names().get(0).equals("success")){
                           Toast.makeText(getApplicationContext(),"SUCCESS" + jsonObject.getString("success"),Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(MainActivity.this,loginActivity.class);
                            intent.putExtra("email",etemail.getText().toString());
                            MainActivity.this.startActivity(intent);
                           //startActivity(new Intent(getApplicationContext(),loginActivity.class));
                        }
                        else{
                            Toast.makeText(getApplicationContext(),"ERROR" + jsonObject.getString("error"),Toast.LENGTH_SHORT).show();

                        }
                    }
                    catch(JSONException e){
                        e.printStackTrace();
                    }

                }
            }, new Response.ErrorListener(){
                   @Override
                    public void onErrorResponse(VolleyError error){
                 Toast.makeText(getApplicationContext(),"volley error",Toast.LENGTH_SHORT).show();
                    }
                }
                )


               {
                    @Override
                    protected Map<String,String> getParams() throws AuthFailureError{
                        HashMap<String,String> params = new HashMap<String, String>();
                        params.put("email",etemail.getText().toString());
                        params.put("password",etpassword.getText().toString());
                        return params;
                    }
                };
              MySingleton.getInstance(ctx_login).addToRequestQueue(login_request);

            }
        } );





    }


    }


