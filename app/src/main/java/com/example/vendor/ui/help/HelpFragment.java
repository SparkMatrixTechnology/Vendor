package com.example.vendor.ui.help;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.vendor.Adapter.HelpAdapter;
import com.example.vendor.Model.HelpModel;
import com.example.vendor.R;
import com.example.vendor.databinding.FragmentHelpBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class HelpFragment extends Fragment {
    FragmentHelpBinding binding;

    ArrayList<HelpModel> helpModels;
    HelpAdapter helpAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentHelpBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        helpModels=new ArrayList<>();
        helpAdapter=new HelpAdapter(getActivity(),helpModels);
        binding.qsRec.setAdapter(helpAdapter);
        getQs(getContext());
        binding.qsRec.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL,false));
        binding.qsRec.setHasFixedSize(true);
        binding.qsRec.setNestedScrollingEnabled(false);


        return root;
    }

    private void getQs(Context context) {
        RequestQueue queue= Volley.newRequestQueue(context);
        StringRequest request=new StringRequest(Request.Method.GET, "http://campusriderbd.com/Customer/customer/help.php?cat=vendor", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    Log.e("err",response);
                    JSONObject mainObj = new JSONObject(response);
                    if(mainObj.getString("status").equals("success")){
                        JSONArray qs_array=mainObj.getJSONArray("List");
                        for(int i=0;i<qs_array.length();i++){
                            JSONObject object=qs_array.getJSONObject(i);
                            HelpModel category=new HelpModel(
                                    object.getInt("id"),
                                    object.getString("cat"),
                                    object.getString("question"),
                                    object.getString("answer")
                            );
                            helpModels.add(category);
                        }
                        helpAdapter.notifyDataSetChanged();
                    }
                    else {

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
}