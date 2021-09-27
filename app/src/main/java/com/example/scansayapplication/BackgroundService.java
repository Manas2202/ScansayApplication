//package com.example.scansayapplication;
//
//import android.app.Service;
//import android.content.Intent;
//import android.os.IBinder;
//
//import androidx.annotation.Nullable;
//
//import java.util.Timer;
//
//public class BackgroundService extends Service {
//    @Nullable
//    @Override
//    public IBinder onBind(Intent intent) {
//        return null;
//    }
//
//    @Override
//    public int onStartCommand(Intent intent, int flags, int startId) {
//        return super.onStartCommand(intent, flags, startId);
//    }

//    Timer timer = new Timer();
//    timer.schedule(new TimerTask() {
//        @Override
//        public void run() {
//            //what you want to do
//        }
//    }, 0, 1000)
//}
