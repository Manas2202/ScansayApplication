package com.example.scansayapplication.ui.notifications;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ListAdapter extends RecyclerView.Adapter<ListAdapter.ViewHolder> {
    public interface ListItemClickListener {
        void onListItemClick(int position);
    }

    ListItemClickListener mOnClickListener;

    private List<list_item_model> paymentList;
    public ListAdapter(List<list_item_model> paymentList,ListItemClickListener onClickListener){
        this.paymentList = paymentList;
        this.mOnClickListener = onClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(com.example.scansayapplication.R.layout.list_item,parent,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        String appname = paymentList.get(position).getAppName();
        String sendername = paymentList.get(position).getSenderName();
        String paymentamount = paymentList.get(position).getPaymentAmount();
        String paymenttime = paymentList.get(position).getPaymentTime();
        holder.setData(appname,sendername,paymentamount,paymenttime);
    }

    @Override
    public int getItemCount() {
        return paymentList.size();
    }



    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        RelativeLayout parentLayout;
        private TextView aName;
        private TextView sName;
        private TextView pAmount;
        private TextView pTime;
        public ViewHolder(@NonNull View v) {
            super(v);
            aName = v.findViewById(com.example.scansayapplication.R.id.appNameId);
            sName = v.findViewById(com.example.scansayapplication.R.id.senderNameId);
            pAmount = v.findViewById(com.example.scansayapplication.R.id.paymentAmountId);
            pTime = v.findViewById(com.example.scansayapplication.R.id.paymentTimeId);
            v.setOnClickListener(this);
        }

        public void setData(String appname, String sendername, String paymentamount, String paymenttime){
            aName.setText(appname);
            sName.setText(sendername);
            pAmount.setText(paymentamount);
            pTime.setText(paymenttime);
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            mOnClickListener.onListItemClick(position);
        }
    }
}
