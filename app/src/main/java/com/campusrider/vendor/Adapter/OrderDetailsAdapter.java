package com.campusrider.vendor.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.campusrider.vendor.Model.OrderDetailsModel;
import com.campusrider.vendor.R;
import com.campusrider.vendor.databinding.OrderDetailsBinding;

import java.util.ArrayList;

public class OrderDetailsAdapter extends RecyclerView.Adapter<OrderDetailsAdapter.ViewHolder> {
    Context context;
    ArrayList<OrderDetailsModel> list;

    public OrderDetailsAdapter(Context context, ArrayList<OrderDetailsModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.order_details,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        OrderDetailsModel order=list.get(position);
        holder.binding.quantity.setText(""+order.getQuantity());
        holder.binding.itemName.setText(order.getProduct_name());
        holder.binding.cost.setText("Tk "+order.getPrice());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        OrderDetailsBinding binding;
        public ViewHolder(@NonNull View itemView){
            super(itemView);
            binding=OrderDetailsBinding.bind(itemView);
        }
    }
}
