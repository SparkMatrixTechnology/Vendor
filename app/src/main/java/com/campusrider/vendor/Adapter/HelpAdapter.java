package com.campusrider.vendor.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.campusrider.vendor.Activity.QuestionActivity;
import com.campusrider.vendor.Model.HelpModel;
import com.campusrider.vendor.R;
import com.campusrider.vendor.databinding.QsListBinding;

import java.util.ArrayList;

public class HelpAdapter extends RecyclerView.Adapter<HelpAdapter.ViewHolder>{

    Context context;
    ArrayList<HelpModel> list;

    public HelpAdapter(Context context, ArrayList<HelpModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.qs_list,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        HelpModel help=list.get(position);

        holder.binding.question.setText(help.getQuestion());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context, QuestionActivity.class);
                intent.putExtra("ques",help.getQuestion());
                intent.putExtra("ans",help.getAnswer());
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        QsListBinding binding;
        public ViewHolder(@NonNull View itemView){
            super(itemView);
            binding= QsListBinding.bind(itemView);
        }
    }
}
