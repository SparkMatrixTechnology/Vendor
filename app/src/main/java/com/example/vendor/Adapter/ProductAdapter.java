package com.example.vendor.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.vendor.Model.ProductModel;
import com.example.vendor.R;
import com.example.vendor.databinding.ProductsBinding;

import java.util.ArrayList;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ViewHolder>{
    Context context;
    ArrayList<ProductModel> list;

    public ProductAdapter(Context context, ArrayList<ProductModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.products,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ProductModel product =list.get(position);
        Glide.with(context).load(product.getProduct_image()).into(holder.binding.productImg);
        holder.binding.productName.setText(product.getProduct_name());
        holder.binding.productDes.setText(product.getProduct_description());
        holder.binding.price.setText("Tk "+product.getProduct_price());

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ProductsBinding binding;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = ProductsBinding.bind(itemView);
        }
    }

}
