package com.campusrider.vendor.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.campusrider.vendor.Activity.OrderDetailsActivity;
import com.campusrider.vendor.Model.OrderModel;
import com.campusrider.vendor.R;
import com.campusrider.vendor.databinding.OrderBinding;

import java.util.ArrayList;


public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.ViewHolder>{
    Context context;
    ArrayList<OrderModel> list;

    public OrderAdapter(Context context, ArrayList<OrderModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.order,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        OrderModel order=list.get(position);
        holder.binding.orderNumber.setText("#"+order.getId());
        holder.binding.address.setText(order.getAddress());
        holder.binding.totalBill.setText("TK "+order.getTotal_price());
        holder.binding.date.setText(order.getOrder_date());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context, OrderDetailsActivity.class);
                intent.putExtra("id",order.getId());
                intent.putExtra("address",order.getAddress());
                intent.putExtra("payment",order.getPayment_type());
                intent.putExtra("total_price",order.getTotal_price());
                intent.putExtra("customer_token",order.getCustomer_token());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        OrderBinding binding;
        public ViewHolder(@NonNull View itemView){
            super(itemView);
            binding=OrderBinding.bind(itemView);
        }
    }
}
