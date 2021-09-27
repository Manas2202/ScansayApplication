package com.example.scansayapplication;

import static com.example.scansayapplication.MainActivity.gPayArr;
import static com.example.scansayapplication.MainActivity.gPayCounter;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.os.IBinder;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class NotificationListenerExampleService extends NotificationListenerService {
    private StatusBarNotification sbn;
    private Retrofit retrofit;
    UserSqlDb db = new UserSqlDb(NotificationListenerExampleService.this);
    private RetrofitInterface retrofitInterface;
    private String BASE_URL = "https://Scansay.manas2202.repl.co";
    String username , userNumber;
    int x = 0;
    private static final class ApplicationPackageNames {
        public static final String WHATSAPP_NAME = "com.whatsapp";
        public static final String GOOGLEPAY_PACK_NAME = "com.google.android.apps.nbu.paisa.user";
        public static final String PHONEPE_PACK_NAME = "com.phonepe.app";
        public static final String SMS_PACK_NAME = "com.google.android.apps.messaging";
    }
    public static final class InterceptedNotificationCode {
        public static final int GOOGLEPAY_CODE = 1;
        public static final int WHATSAPP_CODE = 5;
        public static final int PHONEPE_CODE = 2;
        public static final int SMS_NOTIFICATIONS_CODE = 3;
        public static final int OTHER_NOTIFICATIONS_CODE = 4;
    }
    @Override
    public IBinder onBind(Intent intent) {
        return super.onBind(intent);
    }

    @Override
    public void onNotificationPosted(StatusBarNotification sbn){
        String TAG = "NotifyBroadcastReceiver";
        String TAG1 = "Hidden Data";
        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        retrofitInterface = retrofit.create(RetrofitInterface.class);
        int notificationCode = matchNotificationCode(sbn);
        Cursor c = db.getData();
        if(c.getCount() > 0){
            while(c.moveToNext()){
                username = c.getString(0);
                userNumber = c.getString(1);
            }
        }
        if(notificationCode == InterceptedNotificationCode.SMS_NOTIFICATIONS_CODE){
            String title = String.valueOf(sbn.getNotification().extras.get(Notification.EXTRA_TITLE));
            String content = String.valueOf(sbn.getNotification().extras.get(Notification.EXTRA_TEXT));
            if(content.contains("Your OTP:")) {
                Cursor data = db.getData();
                if(data.getCount() == 0){
                    String[] contentArr = content.split(": ", 0);
                    Intent intent = new Intent("com.github.chagall.notificationlistenerexample");
                    intent.putExtra("Title", title);
                    intent.putExtra("Content", contentArr[0]);
                    intent.putExtra("OTP", contentArr[1]);
                    intent.putExtra("Notification Code", notificationCode);
                    sendBroadcast(intent);
                }
            }
        }
        else if(notificationCode == InterceptedNotificationCode.GOOGLEPAY_CODE){
            String paymentDataStr = String.valueOf(sbn.getNotification().extras.get(Notification.EXTRA_TITLE));
            if(paymentDataStr.contains(" paid you ₹")) {
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
                    cancelNotification(sbn.getPackageName(), sbn.getTag(), sbn.getId());
                }
                else {
                    cancelNotification(sbn.getKey());
                }
                String[] paymentArr = paymentDataStr.split(" paid you ₹", 0);
                Calendar calendar = Calendar.getInstance();
                SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
                String strDate = sdf.format(calendar.getTime());
                String[] dateArr = strDate.split(" ", 0);
                Log.d("Date","DATE : " + strDate);
                HashMap<String, String> map = new HashMap<>();
                map.put("senderName",paymentArr[0]);
                map.put("paymentAmount",paymentArr[1]);
                map.put("paymentApp","Google Pay");
                map.put("userName",username);
                map.put("paymentDate",dateArr[0]);
                map.put("userNumber",userNumber);
                map.put("paymentTime",dateArr[1]);
                Log.i("Sender Name - ",paymentArr[0]);
                Log.i("Sender Name - ",paymentArr[1]);
                Log.i("Sender Name - ","Google Pay");
                Log.i("Sender Name - ",username);
                Log.i("Sender Name - ",dateArr[0]);
                Log.i("Sender Name - ",userNumber);
                Log.i("Sender Name - ",dateArr[1]);
                Call<Void> call = retrofitInterface.executePaymentSendRequest(map);
                call.enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        if(response.code() == 201){
                            Log.i("Message - ","Google Pay Payment Received");
                        }
                    }
                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        Log.i("Failure - ",t.toString());
                        Log.i("Failure - ","Nhe hua");
                    }
                });
                Intent intent = new Intent("com.github.chagall.notificationlistenerexample");
                intent.putExtra("Notification Code", notificationCode);
                sendBroadcast(intent);
            }else{

            }
        }
        else if(notificationCode == InterceptedNotificationCode.WHATSAPP_CODE){
            String msg = String.valueOf(sbn.getNotification().extras.get(Notification.EXTRA_TEXT));
            Log.i("Msg - ",msg);
            Intent intent = new  Intent("com.github.chagall.notificationlistenerexample");
            intent.putExtra("Notification Code", notificationCode);
            sendBroadcast(intent);
        }
        else if(notificationCode == InterceptedNotificationCode.PHONEPE_CODE) {
            //sender name , username , paymentDate , paymentTime , userNumber , paymentApp ,

            //fetching data
            String titleText = String.valueOf(sbn.getNotification().extras.get(Notification.EXTRA_TITLE));
            String dataText = String.valueOf(sbn.getNotification().extras.get(Notification.EXTRA_TEXT));

            //Stopping multiple code execution
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
                cancelNotification(sbn.getPackageName(), sbn.getTag(), sbn.getId());
            }
            else {
                cancelNotification(sbn.getKey());
            }

            //checking the notification
            if(dataText.contains(" has been sent to ")) {
                //fetching sendername
                String[] senderDataStr1 = titleText.split(":", 0);
                String[] senderDataStrMain = senderDataStr1[0].split(" from ",0);
                String senderName = senderDataStrMain[1];

                //creating current date and time
                Calendar calendar = Calendar.getInstance();
                SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
                String strDate = sdf.format(calendar.getTime());
                String[] dateArr = strDate.split(" ", 0);

                //fetching payment amount
                String[] paymentAmountStr1 = dataText.split(" has been sent to ", 0);
                String paymentAmount = paymentAmountStr1[0].substring(1);

                if(x == 0) {
                    x++;
                    //logging full data
                    Log.i("senderName - ", senderName);
                    Log.i("paymentAmount - ", paymentAmount);
                    Log.i("Date - ", dateArr[0]);
                    Log.i("Time - ", dateArr[1]);
                    Log.i("username - ", username);
                    Log.i("userNumber - ", userNumber);
                    Log.i("App - ", "Phone Pe");
                    HashMap<String, String> map = new HashMap<>();
                    map.put("senderName", senderName);
                    map.put("paymentAmount", paymentAmount);
                    map.put("paymentApp", "Phone Pe");
                    map.put("userName", username);
                    map.put("paymentDate", dateArr[0]);
                    map.put("userNumber", userNumber);
                    map.put("paymentTime", dateArr[1]);
                    Call<Void> call = retrofitInterface.executePaymentSendRequest(map);
                    call.enqueue(new Callback<Void>() {
                        @Override
                        public void onResponse(Call<Void> call, Response<Void> response) {
                            if (response.code() == 201) {
                                Log.i("Message - ", "Phone Pe Payment Received");
                                Intent intent = new Intent("com.github.chagall.notificationlistenerexample");
                                intent.putExtra("Notification Code", notificationCode);
                                sendBroadcast(intent);
                            }
                        }

                        @Override
                        public void onFailure(Call<Void> call, Throwable t) {
                            Log.i("Failure - ", t.toString());
                            Log.i("Failure - ", "Nhe hua");
                        }
                    });
                }
            }else{

            }
        }
    }

    @Override
    public void onNotificationRemoved(StatusBarNotification sbn){
        int notificationCode = matchNotificationCode(sbn);
        if(notificationCode != InterceptedNotificationCode.OTHER_NOTIFICATIONS_CODE) {
            StatusBarNotification[] activeNotifications = this.getActiveNotifications();
            if(activeNotifications != null && activeNotifications.length > 0) {
                for (int i = 0; i < activeNotifications.length; i++) {
                    if (notificationCode == matchNotificationCode(activeNotifications[i])) {
                        Intent intent = new  Intent("com.github.chagall.notificationlistenerexample");
                        intent.putExtra("Notification Code", notificationCode);
                        sendBroadcast(intent);
                        break;
                    }
                }
            }
        }
    }

    private int matchNotificationCode(StatusBarNotification sbn) {
        String packageName = sbn.getPackageName();
        Log.i("Package Name - ",packageName);
        if(packageName.equals(ApplicationPackageNames.GOOGLEPAY_PACK_NAME)){
            return(InterceptedNotificationCode.GOOGLEPAY_CODE);
        }else if(packageName.equals(ApplicationPackageNames.PHONEPE_PACK_NAME)){
            return(InterceptedNotificationCode.PHONEPE_CODE);
        }else if(packageName.equals(ApplicationPackageNames.WHATSAPP_NAME)){
            return(InterceptedNotificationCode.WHATSAPP_CODE);
        }else if(packageName.equals(ApplicationPackageNames.SMS_PACK_NAME)){
            return(InterceptedNotificationCode.SMS_NOTIFICATIONS_CODE);
        }else{
            return(InterceptedNotificationCode.OTHER_NOTIFICATIONS_CODE);
        }
    }
}