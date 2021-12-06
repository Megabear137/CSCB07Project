package com.example.cscb07project;

import android.view.View;
import android.widget.AdapterView;

public class ProductSpinnerListener implements AdapterView.OnItemSelectedListener {

    ViewOrders vo;

    public ProductSpinnerListener(ViewOrders vo){
        this.vo = vo;
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        vo.updateTotals();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
