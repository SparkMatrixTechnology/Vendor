package com.campusrider.vendor.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.campusrider.vendor.Activity.ProductListActivity;
import com.campusrider.vendor.Model.CategoryModel;
import com.campusrider.vendor.R;
import com.campusrider.vendor.databinding.CategoryListBinding;

import java.util.ArrayList;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder> {
    Context context;
    ArrayList<CategoryModel> list;

    public CategoryAdapter(Context context, ArrayList<CategoryModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.category_list,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        CategoryModel categoryModel=list.get(position);
        holder.binding.categoryName.setText(categoryModel.getName());
        int id=categoryModel.getId();
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context, ProductListActivity.class);
                intent.putExtra("id",id);
                intent.putExtra("name",categoryModel.getName());
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        CategoryListBinding binding;
        public ViewHolder(@NonNull View itemView){
            super(itemView);
            binding=CategoryListBinding.bind(itemView);
        }
    }

}
