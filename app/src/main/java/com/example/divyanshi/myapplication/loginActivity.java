package com.example.divyanshi.myapplication;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.CursorLoader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static android.R.attr.data;

public class loginActivity extends AppCompatActivity {

    TextView tx2;
    Button logout,upload,choosefile;
    //private ProgressBar progressBar;
    private ImageView imageView;
    private final int IMG_REQUEST = 1;
    private String upload_url = "http://192.168.42.220:80/UploadToServer.php";
    String filepath;
    Bitmap bitmap;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        tx2 = (TextView)findViewById(R.id.textView4);
        logout = (Button)findViewById(R.id.logout);
        upload = (Button)findViewById(R.id.btUpload);
        choosefile = (Button)findViewById(R.id.choose);
        final EditText etImageName = (EditText) findViewById(R.id.eTImgname);
        //progressBar = (ProgressBar)findViewById(R.id.progressBar);

        Bundle bundle = getIntent().getExtras();
        if(bundle != null)
        {
            String email = bundle.getString("email");
            tx2.setText("WELCOME " + email);
        }


        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),MainActivity.class));
            }
        });





                choosefile.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ActivityCompat.requestPermissions(loginActivity.this,
                                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                                1);

                        Intent intent = new Intent();
                        intent.setType("image/*");
                        intent.setAction(Intent.ACTION_GET_CONTENT);
                        startActivityForResult(intent, IMG_REQUEST);
                    }
                });

                upload.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        StringRequest upload_request = new StringRequest(Request.Method.POST, upload_url,
                                new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {

                                        try {
                                            JSONObject jsonObject = new JSONObject(response);
                                            String message = jsonObject.getString("response");
                                            Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
                                            imageView.setImageResource(0);
                                            imageView.setVisibility(View.GONE);
                                            etImageName.setText("");
                                            etImageName.setVisibility(View.GONE);


                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                            Toast.makeText(getApplicationContext(), "json error" + e.getMessage(), Toast.LENGTH_LONG).show();
                                        }
                                    }
                                }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();

                            }

                        }) {
                            @Override
                            protected Map<String, String> getParams() throws AuthFailureError {
                                HashMap<String, String> params = new HashMap<String, String>();
                                params.put("name", etImageName.getText().toString().trim());
                                params.put("image", imageTostring(bitmap));
                                return params;
                            }
                        };


                        //upload_request.addFile("file",filepath);
                        MySingleton.getInstance(loginActivity.this).addToRequestQueue(upload_request);


                    }
                });
    }


    /*@Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 1: {

                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    Uri path = grantResults[];
                    try {
                        bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), path);
                        imageView.setImageBitmap(bitmap);
                        imageView.setVisibility(View.VISIBLE);

                    }catch(IOException e){
                        e.printStackTrace();

                    }

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(loginActivity.this, "Permission denied to read your External storage", Toast.LENGTH_SHORT).show();
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }*/

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == IMG_REQUEST && resultCode == RESULT_OK && data != null) {

            Uri path = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), path);
                imageView.setImageBitmap(bitmap);
                imageView.setVisibility(View.VISIBLE);

            }catch(IOException e){
                e.printStackTrace();

            }

        }

    }

     private String imageTostring(Bitmap bitmap){
         ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
         bitmap.compress(Bitmap.CompressFormat.JPEG,100,byteArrayOutputStream);
         byte[] imageBytes = byteArrayOutputStream.toByteArray();
         return Base64.encodeToString(imageBytes,Base64.DEFAULT);
     }




}
