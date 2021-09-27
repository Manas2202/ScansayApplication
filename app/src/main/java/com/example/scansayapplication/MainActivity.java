package com.example.scansayapplication;
import android.Manifest;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import com.example.scansayapplication.databinding.ActivityMainBinding;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {
private ActivityMainBinding binding;
    private static final String ENABLED_NOTIFICATION_LISTENERS = "enabled_notification_listeners";
    private static final String ACTION_NOTIFICATION_LISTENER_SETTINGS = "android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS";
    private AlertDialog enableNotificationListenerAlertDialog;
    UserSqlDb db = new UserSqlDb(MainActivity.this);
    private Button logoutBtn;
    TextView shopName , mobNum;
    private int arr[];
    Uri selectedImageUri;
    private Retrofit retrofit;
    private RetrofitInterface retrofitInterface;
    private String BASE_URL = "https://Scansay.manas2202.repl.co";
    private ImageView selectImageIcon , profilePic;
    private int GalleryPick = 1;
    public static int[] gPayArr = new int[3000];
    public static int gPayCounter = 0;

    //testing loader
    private ProgressBar pgsBar;
    private Button showbtn, hidebtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        this.getSupportActionBar().hide();
        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        retrofitInterface = retrofit.create(RetrofitInterface.class);
        BottomNavigationView navView = findViewById(R.id.nav_view);
//        pgsBar = (ProgressBar) findViewById(R.id.pBar);

//        pgsBar.setVisibility(View.VISIBLE);
        logoutBtn = (Button) findViewById(R.id.logoutBtn);
        shopName = (TextView) findViewById(R.id.shopNameId);
        mobNum = (TextView) findViewById(R.id.mobNumId);
        selectImageIcon = (ImageView) findViewById(R.id.imageView3);
        profilePic = (ImageView) findViewById(R.id.imageView2);
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
        R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications)
        .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);
        Cursor c = db.getData();
        if(c.getCount() > 0){
            if(!isNotificationServiceEnabled()){
                enableNotificationListenerAlertDialog = buildNotificationServiceAlertDialog();
                enableNotificationListenerAlertDialog.show();
            }else{
                c.moveToNext();
                String shopNameText = c.getString(2);
                String mobileNumberText = c.getString(1);
                shopName.setText(shopNameText);
                mobNum.setText(mobileNumberText);
            }
        }else{
            Intent intent = new Intent(MainActivity.this, Login.class);
            startActivity(intent);
        }
    }

    public void logoutMethod(View view){
        if(db.clearUserTable()){
            Log.i("Check - ","Logged Out");
            Intent intent = new Intent(MainActivity.this, Login.class);
            startActivity(intent);
        }
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
                Log.i("Code - ",String.valueOf(receivedNotificationCode));
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