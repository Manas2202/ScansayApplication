package com.example.scansayapplication;

import androidx.appcompat.app.AppCompatActivity;

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

public class UserRegister extends AppCompatActivity {
    private EditText usernameEditText , shopnameEditText , addressEditText;
    private Button sendDataBtn;
    private String mobNum;
    private Retrofit retrofit;
    private StatusBarNotification sbn;
    private RetrofitInterface retrofitInterface;
    UserSqlDb db = new UserSqlDb(UserRegister.this);
    private String BASE_URL = "https://Scansay.manas2202.repl.co";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_register);
        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        retrofitInterface = retrofit.create(RetrofitInterface.class);
        Intent intent = getIntent();
        mobNum = intent.getStringExtra("mobNum");
        usernameEditText = (EditText) findViewById(R.id.usernameEditText);
        shopnameEditText = (EditText) findViewById(R.id.shopnameEditText);
        addressEditText = (EditText) findViewById(R.id.addressEditText);
        sendDataBtn = (Button) findViewById(R.id.sendDataBtn);
        sendDataBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = usernameEditText.getText().toString();
                String shopname = shopnameEditText.getText().toString();
                String address = addressEditText.getText().toString();
                Log.i("Username - ",username);
                Log.i("shopname - ",shopname);
                Log.i("address - ",address);
                Log.i("mobNum - ",mobNum);
                HashMap<String, String> map = new HashMap<>();
                map.put("userName", username);
                map.put("userMobNum", mobNum);
                map.put("shopname", shopname);
                map.put("address", address);
                Call<Void> call = retrofitInterface.executeUserData(map);
                call.enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        Log.i("Response - " , String.valueOf(response.code()));
                        if(response.code() == 201){
                            Log.i("Message - ","User Created Successfully");
                            Boolean x = db.insertUserData(username,mobNum,shopname,address);
                            if(x){
                                Log.i("Message - ","Db User Done");
                                Intent intent = new Intent(UserRegister.this,MainActivity.class);
                                startActivity(intent);
                            }else{
                                Log.i("Message - ","Db User Not Done");
                            }
                        }else{
                            Log.i("Error - ","Not Created");
                        }
                    }
                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        Log.i("Failure - ",t.toString());
                        Log.i("Failure - ","Nhe hua");
                    }
                });
            }
        });
    }
}