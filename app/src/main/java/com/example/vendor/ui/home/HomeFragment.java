package com.example.vendor.ui.home;

import static android.content.Context.MODE_PRIVATE;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.vendor.Activity.OrderActivity;
import com.example.vendor.MainActivity;
import com.example.vendor.Model.Messeging;
import com.example.vendor.databinding.FragmentHomeBinding;
import com.example.vendor.session.SharedPrefManager;
import com.example.vendor.utils.Constants;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

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