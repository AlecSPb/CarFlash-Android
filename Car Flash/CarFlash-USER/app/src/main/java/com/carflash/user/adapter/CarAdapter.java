package com.carflash.user.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.carflash.user.R;
import com.carflash.user.manager.LanguageManager;
import com.carflash.user.models.ViewCarType;
import com.carflash.user.urls.Apis;
import com.bumptech.glide.Glide;

import butterknife.Bind;
import butterknife.ButterKnife;

public class CarAdapter extends BaseAdapter {

    Context context;
    ViewCarType viewCarType;

    LanguageManager languageManager;
    String language_id;

    public CarAdapter(Context context, ViewCarType viewCarType) {
        this.context = context;
        this.viewCarType = viewCarType;
        languageManager = new LanguageManager(context);
        language_id = languageManager.getLanguageDetail().get(LanguageManager.LANGUAGE_ID);
    }

    @Override
    public int getCount() {
        return viewCarType.getMsg().size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        MyHolder myHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.itemlayoutforcars, parent, false);
            myHolder = new MyHolder(convertView);
            convertView.setTag(myHolder);
        } else {
            myHolder = (MyHolder) convertView.getTag();
        }
        Glide.with(context).load(""+ Apis.imageDomain+viewCarType.getMsg().get(position).getCar_type_image()).into(myHolder.car_image);


        myHolder.tv_car1.setText(viewCarType.getMsg().get(position).getCar_type_name());

        return convertView;
    }

    static class MyHolder {
        @Bind(R.id.tv_car1) TextView tv_car1;
        @Bind(R.id.car_image)
        ImageView car_image;


        public MyHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
