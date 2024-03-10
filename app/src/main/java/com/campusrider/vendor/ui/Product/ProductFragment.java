package com.campusrider.vendor.ui.Product;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.campusrider.vendor.Adapter.CategoryAdapter;
import com.campusrider.vendor.Model.CategoryModel;

import com.campusrider.vendor.databinding.FragmentProductBinding;
import com.campusrider.vendor.utils.Constants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ProductFragment extends Fragment {
    ArrayList<CategoryModel> categoryModels;
    CategoryAdapter categoryAdapter;
    String url= Constants.GET_VENDOR_CAT_URL;

    private FragmentProductBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentProductBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        categoryModels=new ArrayList<>();
        categoryAdapter=new CategoryAdapter(getActivity(),categoryModels);
        binding.categoryRec.setAdapter(categoryAdapter);
        getCategoryList(getContext());
        binding.categoryRec.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL,false));
        binding.categoryRec.setHasFixedSize(true);
        binding.categoryRec.setNestedScrollingEnabled(false);


        return root;
    }

    private void getCategoryList(Context context) {
        RequestQueue queue= Volley.newRequestQueue(context);
        StringRequest request=new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    Log.e("err",response);
                    JSONObject mainObj = new JSONObject(response);
                    if(mainObj.getString("status").equals("success")){
                        JSONArray vendor_cat_array=mainObj.getJSONArray("Category");
                        for(int i=0;i<vendor_cat_array.length();i++){
                            JSONObject object=vendor_cat_array.getJSONObject(i);
                            CategoryModel category=new CategoryModel(
                                    object.getInt("cat_id"),
                                    object.getString("cat_name")
                            );
                            categoryModels.add(category);
                        }
                        categoryAdapter.notifyDataSetChanged();
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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}