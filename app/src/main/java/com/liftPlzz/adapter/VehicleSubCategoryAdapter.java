package com.liftPlzz.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.liftPlzz.R;
import com.liftPlzz.model.vehiclesubcategory.VehicleSubCategoryData;

import java.util.ArrayList;
import java.util.List;

public class VehicleSubCategoryAdapter extends BaseAdapter {

    Context context;
    List<VehicleSubCategoryData> arrayList;
    LayoutInflater inflter;

    public VehicleSubCategoryAdapter(Context applicationContext, List<VehicleSubCategoryData> subCategoryData) {
        this.context = applicationContext;
        this.arrayList = subCategoryData;
        //9770297761
        inflter = (LayoutInflater.from(applicationContext));
    }

    @Override
    public int getCount() {
        return arrayList.size();
    }

    @Override
    public Object getItem(int i) {
        return arrayList.get(i).getId();
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }


    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = inflter.inflate(R.layout.custom_spinner_item, null);
        TextView textView = (TextView) view.findViewById(R.id.tv_category);
        textView.setText(arrayList.get(i).getCategory());
        return view;
    }

}
