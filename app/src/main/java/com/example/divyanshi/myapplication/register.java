package com.example.divyanshi.myapplication;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class register extends AppCompatActivity {
    private static final String URL_REGISTER = "http://192.168.42.220:80/register_new.php";
    private final Context ctx_register = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        final Button reg = (Button)findViewById(R.id.register);
        final EditText etname = (EditText)findViewById(R.id.etName);
        final EditText etemail = (EditText)findViewById(R.id.etEmail);
        final EditText etpassword = (EditText)findViewById(R.id.etPassword);

        reg.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //final String name = etname.getText().toString();
                //final String email = etemail.getText().toString();
                //final String password = etpassword.getText().toString();
                StringRequest register_request = new StringRequest(Request.Method.POST, URL_REGISTER, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try{
                            JSONObject jsonObject = new JSONObject(response);
                            //boolean success = jsonObject.getBoolean("success");
                            if(jsonObject.names().get(0).equals("success")){
                                Toast.makeText(getApplicationContext(),"SUCCESS" + jsonObject.getString("success"),Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(register.this,MainActivity.class);
                                register.this.startActivity(intent);

                                //startActivity(new Intent(getApplicationContext(),MainActivity.class));
                            }
                            else{
                                Toast.makeText(getApplicationContext(),"ERROR" + jsonObject.getString("error"),Toast.LENGTH_SHORT).show();
                            }

                        }
                        catch(JSONException e){
                            e.printStackTrace();
                        }
                    }
                } ,new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error){
                    Toast.makeText(getApplicationContext(),"volley error",Toast.LENGTH_SHORT).show();
                    }
                }
                )

            {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        HashMap<String, String> params = new HashMap<String, String>();
                        params.put("name", etname.getText().toString());
                        params.put("email", etemail.getText().toString());
                        params.put("password", etpassword.getText().toString());
                        return params;
                    }
                };
                MySingleton.getInstance(ctx_register).addToRequestQueue(register_request);


            }
        });


    }
}
