package com.example.scansayapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.os.Bundle;
import android.service.notification.StatusBarNotification;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

//new imports
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.provider.Settings;
import android.text.TextUtils;
import android.widget.ImageView;

import java.util.HashMap;
import java.util.List;

public class optVerification extends AppCompatActivity {
    private Button otpSubBtn;
    private EditText otpInputObj;
    private String mobNum;
    UserSqlDb db = new UserSqlDb(optVerification.this);
    private Retrofit retrofit;
    private StatusBarNotification sbn;
    private RetrofitInterface retrofitInterface;
    private String BASE_URL = "https://Scansay.manas2202.repl.co";
    private static final String ENABLED_NOTIFICATION_LISTENERS = "enabled_notification_listeners";
    private static final String ACTION_NOTIFICATION_LISTENER_SETTINGS = "android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS";
    private String TAG = "Notification";
    private AlertDialog enableNotificationListenerAlertDialog;
    private ImageChangeBroadcastReceiver imageChangeBroadcastReceiver;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_opt_verification);
        this.getSupportActionBar().hide();
        Intent intent = getIntent();
        mobNum = intent.getStringExtra("mobNum");
        retrofit = new Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build();
        retrofitInterface = retrofit.create(RetrofitInterface.class);
        if(!isNotificationServiceEnabled()){
            enableNotificationListenerAlertDialog = buildNotificationServiceAlertDialog();
            enableNotificationListenerAlertDialog.show();
        }
        imageChangeBroadcastReceiver = new ImageChangeBroadcastReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("com.github.chagall.notificationlistenerexample");
        registerReceiver(imageChangeBroadcastReceiver,intentFilter);
        otpSubBtn = (Button) findViewById(R.id.otpSubBtnId);
        otpInputObj = (EditText) findViewById(R.id.otpInputId);
        otpSubBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String otp = otpInputObj.getText().toString();
                retrofitInterface = retrofit.create(RetrofitInterface.class);
                HashMap<String, String> map = new HashMap<>();
                map.put("otp", otp);
                map.put("userMobNum", mobNum);
                Call<List<LoginResponseData>> call = retrofitInterface.executeLoginOtpCheckData(map);
                call.enqueue(new Callback<List<LoginResponseData>>() {
                    @Override
                    public void onResponse(Call<List<LoginResponseData>> call, Response<List<LoginResponseData>> response) {
                        Log.i("Response Code - ",String.valueOf(response.code()));
                        if(response.code() == 200){
                            Log.i("Message - ","Logged In");
                            List<LoginResponseData> dataList = response.body();
                            String username = dataList.get(0).getUserName();
                            String userMobNum = dataList.get(0).getUserMobNum();
                            String shopname = dataList.get(0).getShopname();
                            String address = dataList.get(0).getAddress();
                            Boolean x = db.insertUserData(username,userMobNum,shopname,address);
                        if(x){
                            Log.i("Message - ","Db User Done");
                            Intent intent = new Intent(optVerification.this,MainActivity.class);
                            startActivity(intent);
                        }else{
                            Log.i("Message - ","Db User Not Done");
                        }
                    }
                    else if(response.code() == 201){
                        Log.i("Message - ","Registered");
                        Intent intent = new Intent(optVerification.this, UserRegister.class);
                        intent.putExtra("mobNum", mobNum);
                        startActivity(intent);
                    }else if(response.code() == 300){
                        Log.i("Message - ","Incorrect Otp");
                    }else{
                        Log.i("Error - ","UnSuccessful User Data transfer");
                    }
                    }
                    @Override
                    public void onFailure(Call<List<LoginResponseData>> call, Throwable t) {
                        Log.i("Failure - ",t.toString());
                        Log.i("Failure - ","Nhe hua");
                    }
                });

            }
        });
    }

    private boolean isNotificationServiceEnabled(){
        String pkgName = getPackageName();
        final String flat = Settings.Secure.getString(getContentResolver(),
                ENABLED_NOTIFICATION_LISTENERS);
        if (!TextUtils.isEmpty(flat)) {
            final String[] names = flat.split(":");
            for (int i = 0; i < names.length; i++) {
                final ComponentName cn = ComponentName.unflattenFromString(names[i]);
                if (cn != null) {
                    if (TextUtils.equals(pkgName, cn.getPackageName())) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public class ImageChangeBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            int receivedNotificationCode = intent.getIntExtra("Notification Code",-1);
//            String notificationTitle = intent.getStringExtra("Title");
//            String notificationContent = intent.getStringExtra("Content");
//            String OTP = intent.getStringExtra("OTP");
//            Log.i("Content - ",notificationTitle);
//            Log.i("OTP - ",OTP);
//            if(notificationContent.equals("Your OTP")){
//                otpInputObj.setText(OTP);
//                retrofitInterface = retrofit.create(RetrofitInterface.class);
//                HashMap<String, String> map = new HashMap<>();
//                map.put("otp", OTP);
//                map.put("userMobNum", mobNum);
//                Call<List<LoginResponseData>> call = retrofitInterface.executeLoginOtpCheckData(map);
//                call.enqueue(new Callback<List<LoginResponseData>>() {
//                    @Override
//                    public void onResponse(Call<List<LoginResponseData>> call, Response<List<LoginResponseData>> response) { if(response.code() == 200){
//                            Log.i("Message - ","Logged In");
//                            List<LoginResponseData> dataList = response.body();
//                            String username = dataList.get(0).getUserName();
//                            String userMobNum = dataList.get(0).getUserMobNum();
//                            String shopname = dataList.get(0).getShopname();
//                            String address = dataList.get(0).getAddress();
//                            Cursor data = db.getData();
//                            if(data.getCount() == 0){
//                                Boolean x = db.insertUserData(username,userMobNum,shopname,address);
//                                if(x){
//                                    Log.i("Message - ","Db User Done");
//                                    Intent intent = new Intent(optVerification.this,MainActivity.class);
//                                    startActivity(intent);
//                                }else{
//                                    Log.i("Message - ","Db User Not Done");
//                                }
//                            }
//                            else{
//                                if(db.clearUserTable()){
//                                    Boolean x = db.insertUserData(username,userMobNum,shopname,address);
//                                    if(x){
//                                        Log.i("Message - ","Db User Done");
//                                        Intent intent = new Intent(optVerification.this,MainActivity.class);
//                                        startActivity(intent);
//                                    }else{
//                                        Log.i("Message - ","Db User Not Done");
//                                    }
//                                }
//                            }
//                        }
//                        else if(response.code() == 201){
//                            Log.i("Message - ","Registered");
//                            Intent intent = new Intent(optVerification.this, UserRegister.class);
//                            intent.putExtra("mobNum", mobNum);
//                            startActivity(intent);
//                        }else if(response.code() == 300){
//                            Log.i("Message - ","Incorrect Otp");
//                        }else{
//                            Log.i("Error - ","UnSuccessful User Data transfer");
//                        }
//                    }
//                    @Override
//                    public void onFailure(Call<List<LoginResponseData>> call, Throwable t) {
//                        Log.i("Failure - ",t.toString());
//                        Log.i("Failure - ","Nhe hua");
//                    }
//                });
//            }else{
//            }
        }
    }

    private AlertDialog buildNotificationServiceAlertDialog(){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle("Notification Access Required!");
        alertDialogBuilder.setMessage("This app want to access and read your notifications");
        alertDialogBuilder.setPositiveButton(R.string.yes,
            new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    startActivity(new Intent(ACTION_NOTIFICATION_LISTENER_SETTINGS));
                }
            });
        alertDialogBuilder.setNegativeButton(R.string.no,
            new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    buildNotificationServiceAlertDialog();
                }
            }
        );
        return(alertDialogBuilder.create());
    }

}