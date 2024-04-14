package com.campusrider.vendor.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.campusrider.vendor.Model.VariationModel;
import com.campusrider.vendor.R;
import com.campusrider.vendor.databinding.VariationListBinding;

import java.util.ArrayList;

public class VariationAdapter extends RecyclerView.Adapter<VariationAdapter.ViewHolder>{

    Context context;
    ArrayList<VariationModel> list;

    public VariationAdapter(Context context, ArrayList<VariationModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.variation_list,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        VariationModel variationModel=list.get(position);
        holder.binding.variationName.setText(variationModel.getName()+",a");
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        VariationListBinding binding;

        public ViewHolder(@NonNull View itemView){
            super(itemView);
            binding=VariationListBinding.bind(itemView);



        }
    }
}
