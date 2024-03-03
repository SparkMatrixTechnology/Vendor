package com.example.vendor.ui.SellReport;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.vendor.Activity.OrderActivity;
import com.example.vendor.Model.OrderModel;
import com.example.vendor.Model.SalesReportModel;
import com.example.vendor.databinding.FragmentSalesReportBinding;
import com.example.vendor.session.SharedPrefManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.time.Year;
import java.util.Calendar;


public class SalesReportFragment extends Fragment {
    String from,to;
    SharedPrefManager sharedPrefManager;
    private FragmentSalesReportBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentSalesReportBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        sharedPrefManager=new SharedPrefManager(getContext());
        int id=sharedPrefManager.getUser().getVendor_id();

         binding.fromDate.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 openDialogue1();

             }
         });
        binding.toDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialogue2();
            }
        });
        binding.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getInfo(id);
            }
        });


        return root;
    }

    private void getInfo(int id) {
        RequestQueue queue= Volley.newRequestQueue(getContext());
        StringRequest request=new StringRequest(Request.Method.GET,"http://campusriderbd.com/Customer/vendor/report.php?vendor_id=" +id +"&from=" +binding.fromDate.getText().toString() +"&to=" +binding.toDate.getText().toString(), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    Log.e("err",response);
                    JSONObject mainObj = new JSONObject(response);
                    if(mainObj.getString("status").equals("success")){
                        JSONArray order_array=mainObj.getJSONArray("Report");
                        for(int i=0;i<order_array.length();i++){
                            JSONObject object=order_array.getJSONObject(i);
                            int total_order=object.getInt("torder");
                            int total_cost=object.getInt("tcost");
                            binding.textViewOrder.setText(""+total_order);
                            binding.textViewSell.setText("TK "+total_cost);
                        }
                    }
                    if(mainObj.getString("status").equals("failed")) {
                            binding.textViewOrder.setText(""+0);
                            binding.textViewSell.setText("TK "+0);
                    }


                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }
        );
        queue.add(request);
    }


    public void openDialogue1(){

        int y=Calendar.getInstance().get(Calendar.YEAR);
        int m=Calendar.getInstance().get(Calendar.MONTH);
        int d=Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
        DatePickerDialog dialog=new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                from=String.valueOf(year)+"-"+String.valueOf(month+1)+"-"+String.valueOf(dayOfMonth);
                binding.fromDate.setText(from);
            }
        }, y, m, d);
        dialog.show();
    }
    public void openDialogue2(){

        int y=Calendar.getInstance().get(Calendar.YEAR);
        int m=Calendar.getInstance().get(Calendar.MONTH);
        int d=Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
        DatePickerDialog dialog=new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                to=String.valueOf(year)+"-"+String.valueOf(month+1)+"-"+String.valueOf(dayOfMonth);
                binding.toDate.setText(to);
            }
        }, y, m, d);
        dialog.show();
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}