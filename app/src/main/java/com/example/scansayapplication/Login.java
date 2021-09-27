package com.example.scansayapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Notification;
import android.content.Intent;
import android.os.Bundle;
import android.service.notification.StatusBarNotification;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Login extends AppCompatActivity {
    private Button loginBtn;
    private EditText mobNumObj;
    private Retrofit retrofit;
    private StatusBarNotification sbn;
    private RetrofitInterface retrofitInterface;
    private String BASE_URL = "https://Scansay.manas2202.repl.co";
    loading_dialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        this.getSupportActionBar().hide();
        loginBtn = (Button) findViewById(R.id.loginBtnId);
        mobNumObj = (EditText) findViewById(R.id.mobNumId);
        dialog = new loading_dialog(Login.this);
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.startLoadingAnimation();
                String mobNum = mobNumObj.getText().toString();
                retrofit = new Retrofit.Builder()
                        .baseUrl(BASE_URL)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();
                retrofitInterface = retrofit.create(RetrofitInterface.class);
                HashMap<String, String> map = new HashMap<>();
                map.put("userMobNum", mobNum);
                Call<Void> call = retrofitInterface.executeOtpData(map);
                call.enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        if(response.code() == 201){
                            Log.i("Message - ","Successful data transfer");
                            dialog.dismissDialog();
                            Intent intent = new Intent(Login.this, optVerification.class);
                            intent.putExtra("mobNum", mobNum);
                            startActivity(intent);
                        }else{
                            Log.i("Error - ","UnSuccessful data transfer");
                            dialog.dismissDialog();
                        }
                    }
                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        dialog.dismissDialog();
                        Log.i("Failure - ",t.toString());
                        Log.i("Failure - ","Nhe hua");
                    }
                });
            }
        });
    }
}