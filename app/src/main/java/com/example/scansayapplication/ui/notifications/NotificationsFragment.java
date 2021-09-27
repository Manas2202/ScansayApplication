package com.example.scansayapplication.ui.notifications;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.scansayapplication.Login;
import com.example.scansayapplication.LoginResponseData;
import com.example.scansayapplication.MainActivity;
import com.example.scansayapplication.PaymentDataDisplay;
import com.example.scansayapplication.PaymentListModel;
import com.example.scansayapplication.R;
import com.example.scansayapplication.Responsedata;
import com.example.scansayapplication.RetrofitInterface;
import com.example.scansayapplication.UpdateProfile;
import com.example.scansayapplication.UserRegister;
import com.example.scansayapplication.UserSqlDb;
import com.example.scansayapplication.databinding.FragmentNotificationsBinding;
import com.example.scansayapplication.loading_dialog;
import com.example.scansayapplication.optVerification;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class NotificationsFragment extends Fragment  implements ListAdapter.ListItemClickListener{
    private NotificationsViewModel notificationsViewModel;
    private FragmentNotificationsBinding binding;
    private Button getDateButton;
    private DatePickerDialog datePickerDialog;
    RecyclerView recyclerView;
    LinearLayoutManager layoutManager;
    List<list_item_model> paymentList;
    ListAdapter adapter;
    Retrofit retrofit;
    RetrofitInterface retrofitInterface;
    TextView transactionNumText,noDataText,transactionAmountText;
    loading_dialog dialog;
    private String dateValue;
    private String BASE_URL = "https://Scansay.manas2202.repl.co";
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        notificationsViewModel = new ViewModelProvider(this).get(NotificationsViewModel.class);
        binding = FragmentNotificationsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        recyclerView = binding.paymentListScroll;
        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        retrofitInterface = retrofit.create(RetrofitInterface.class);
        getDateButton = (Button) binding.setDateButton;
        dialog = new loading_dialog(getActivity());
        dialog.startLoadingAnimation();
        getDateButton.setText(getTodaysDate());
        initData();
        initDatePicker();
        transactionNumText = (TextView) binding.transactionNum;
        transactionAmountText = (TextView) binding.transactionAmount;
        noDataText = (TextView) binding.noDataTextId;
        noDataText.setVisibility(View.INVISIBLE);
        getDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                datePickerDialog.show();
            }
        });
        notificationsViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
            }
        });

        return root;
    }

    private void initData() {
        paymentList = new ArrayList<>();
        HashMap<String, String> map = new HashMap<>();
        map.put("userNumber", "9680302202");
        map.put("paymentDate", dateValue);
        Call<List<PaymentListModel>> call = retrofitInterface.fetchPaymentListData(map);
        call.enqueue(new Callback<List<PaymentListModel>>() {
            @Override
            public void onResponse(Call<List<PaymentListModel>> call, Response<List<PaymentListModel>> response) {
                if(response.code() == 200){
                    dialog.dismissDialog();
                    Log.i("Message - ","Data Fetched");
                    List<PaymentListModel> dataList = response.body();
                    Log.i("Size - ",String.valueOf(dataList.size()));
                    transactionNumText.setText(String.valueOf(dataList.size()));
                    if(dataList.size() == 0){
                        recyclerView.setVisibility(View.INVISIBLE);
                        noDataText.setVisibility(View.VISIBLE);
                        transactionAmountText.setText("0 Rs");
                        intRecyclerView();
                    }else{
                        float transactionAmountVal = 0;
                        recyclerView.setVisibility(View.VISIBLE);
                        noDataText.setVisibility(View.INVISIBLE);
                        for(int i = dataList.size()-1 ; i >= 0 ; i--){
                            transactionAmountVal += Float.parseFloat(dataList.get(i).getPaymentAmount());
                            paymentList.add(new list_item_model(dataList.get(i).getPaymentApp(),dataList.get(i).getSenderName(),dataList.get(i).getPaymentAmount() + " Rs",dataList.get(i).getPaymentDate() + " " + dataList.get(i).getPaymentTime()));
                        }
                        transactionAmountText.setText(String.valueOf(transactionAmountVal) + "Rs");
                        intRecyclerView();
                    }
                }
            }
            @Override
            public void onFailure(Call<List<PaymentListModel>> call, Throwable t) {
                dialog.dismissDialog();
                Log.i("Failure - ",t.toString());
                Log.i("Failure - ","Nhe hua");
            }
        });
    }

    private void intRecyclerView() {
        layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new ListAdapter(paymentList,this);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    private String getTodaysDate(){
        Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        month = month+1;
        int day = c.get(Calendar.DAY_OF_MONTH);
        dateValue = makeDateString(day,month,year);
        return dateValue;
    }

    private void initDatePicker() {
        Log.i("CHECK - " , "CHECK");
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener(){
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month+1;
                String date = makeDateString(day,month,year);
                getDateButton.setText(date);
                dialog.startLoadingAnimation();
                if(date == dateValue){

                }else {
                    dateValue = date;
                    initData();
                }
            }
        };
        Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);
        int style = AlertDialog.THEME_HOLO_LIGHT;
        datePickerDialog = new DatePickerDialog(getActivity(),style,dateSetListener,year,month,day);
    }

    private String makeDateString(int day, int month, int year) {
        return day + "-" + getMonthFormat(month) + "-" + year;
    }

    private String getMonthFormat(int month) {
        if(month == 1){
            return "01";
        }if(month == 2){
            return "02";
        }if(month == 3){
            return "03";
        }if(month == 4){
            return "04";
        }if(month == 5){
            return "05";
        }if(month == 6){
            return "06";
        }if(month == 7){
            return "07";
        }if(month == 8){
            return "08";
        }if(month == 9){
            return "09";
        }if(month == 10){
            return "10";
        }if(month == 11){
            return "11";
        }if(month == 12){
            return "12";
        }
        return "01";
    }

    @Override
    public void onDestroyView() {
            super.onDestroyView();
            binding = null;
        }

    @Override
    public void onListItemClick(int position) {
        Intent intent = new Intent(getActivity(), PaymentDataDisplay.class);
        intent.putExtra("appname",paymentList.get(position).getAppName());
        intent.putExtra("sendername",paymentList.get(position).getSenderName());
        intent.putExtra("paymentamount",paymentList.get(position).getPaymentAmount());
        intent.putExtra("paymenttime",paymentList.get(position).getPaymentTime());
        startActivity(intent);
    }
}