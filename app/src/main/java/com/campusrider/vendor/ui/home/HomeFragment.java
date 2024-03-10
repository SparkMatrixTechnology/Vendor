package com.campusrider.vendor.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.campusrider.vendor.Activity.OrderActivity;
import com.campusrider.vendor.databinding.FragmentHomeBinding;
import com.campusrider.vendor.session.SharedPrefManager;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    SharedPrefManager sharedPrefManager;
    String accesstoken;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {


        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        sharedPrefManager=new SharedPrefManager(getContext());
        int id=sharedPrefManager.getUser().getVendor_id();

        System.out.println(sharedPrefManager.getUser().getVendor_token());
        binding.pending.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String status="Pending";
                Intent intent=new Intent(getContext(), OrderActivity.class);
                intent.putExtra("vendor_id",id);
                intent.putExtra("status",1);
                intent.putExtra("status_name",status);
                startActivity(intent);
            }
        });
        binding.accepted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String status="Accepted";
                Intent intent=new Intent(getContext(), OrderActivity.class);
                intent.putExtra("vendor_id",id);
                intent.putExtra("status",4);
                intent.putExtra("status_name",status);
                startActivity(intent);
            }
        });
        binding.complete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String status="Delivered";

                Intent intent=new Intent(getContext(), OrderActivity.class);
                intent.putExtra("vendor_id",id);
                intent.putExtra("status",3);
                intent.putExtra("status_name",status);
                startActivity(intent);
            }
        });
        binding.picked.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String status="Picked Up";
                Intent intent=new Intent(getContext(), OrderActivity.class);
                intent.putExtra("vendor_id",id);
                intent.putExtra("status",2);
                intent.putExtra("status_name",status);
                startActivity(intent);
            }
        });
        binding.cancelled.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String status="Cancelled";
                Intent intent=new Intent(getContext(), OrderActivity.class);
                intent.putExtra("vendor_id",id);
                intent.putExtra("status",5);
                intent.putExtra("status_name",status);
                startActivity(intent);
            }
        });



        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}