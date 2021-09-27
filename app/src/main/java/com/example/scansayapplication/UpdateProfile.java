package com.example.scansayapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.service.notification.StatusBarNotification;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class UpdateProfile extends AppCompatActivity {
    UserSqlDb db = new UserSqlDb(this);
    private EditText usernameText , shopnameText , addressText;
    private Button updateButton;
    private Retrofit retrofit;
    private RetrofitInterface retrofitInterface;
    private String BASE_URL = "https://Scansay.manas2202.repl.co";
    private String userMobNum;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_profile);
        Cursor c = db.getData();
        usernameText = (EditText) findViewById(R.id.usernameId);
        shopnameText = (EditText) findViewById(R.id.shopnameId);
        addressText = (EditText) findViewById(R.id.addressId);
        updateButton = (Button) findViewById(R.id.updateBtnId);
        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        if(c.getCount() > 0){
            while(c.moveToNext()){
                String shopName = c.getString(2);
                userMobNum = c.getString(1);
                String userName = c.getString(0);
                String address = c.getString(3);
                usernameText.setText(userName);
                shopnameText.setText(shopName);
                addressText.setText(address);
            }
        }else{
            Intent intent = new Intent(UpdateProfile.this,Login.class);
            startActivity(intent);
        }
        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String upShopName = shopnameText.getText().toString();
                String upUserName = usernameText.getText().toString();
                String upAddress = addressText.getText().toString();
                retrofitInterface = retrofit.create(RetrofitInterface.class);
                HashMap<String, String> map = new HashMap<>();
                map.put("userName", upUserName);
                map.put("userMobNum", userMobNum);
                map.put("shopname", upShopName);
                map.put("address", upAddress);
                Call<Void> call = retrofitInterface.executeUpdateProcess(map);
                call.enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        if(response.code() == 200){
                            Log.i("Message - ","Profile Updated Successfully");
                            if(db.clearUserTable()){
                                Boolean x = db.insertUserData(upUserName,userMobNum,upShopName,upAddress);
                                if(x){
                                    Log.i("Tag - ","Profile Updated Successfully!");
                                    Intent intent = new Intent(UpdateProfile.this , MainActivity.class);
                                    startActivity(intent);
                                }
                            }
                        }else{
                            Log.i("Error - ","UnSuccessful User Update transfer");
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