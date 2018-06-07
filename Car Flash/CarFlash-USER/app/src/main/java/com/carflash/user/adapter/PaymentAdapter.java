package com.carflash.user.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.carflash.user.R;
import com.carflash.user.manager.LanguageManager;
import com.carflash.user.models.ViewPaymentOption;

import butterknife.Bind;
import butterknife.ButterKnife;

public class PaymentAdapter extends BaseAdapter {

    Context context;
    ViewPaymentOption viewPaymentOption;

    LanguageManager languageManager;
    String language_id;


    public PaymentAdapter(Context context, ViewPaymentOption viewPaymentOption) {
        this.context = context;
        this.viewPaymentOption = viewPaymentOption;
        languageManager = new LanguageManager(context);
        language_id = languageManager.getLanguageDetail().get(LanguageManager.LANGUAGE_ID);
    }

    @Override
    public int getCount() {
        return viewPaymentOption.getMsg().size();
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
            convertView = LayoutInflater.from(context).inflate(R.layout.itemlayoutforpaymentoption, parent, false);
            myHolder = new MyHolder(convertView);
            convertView.setTag(myHolder);
        } else {
            myHolder = (MyHolder) convertView.getTag();
        }
        myHolder.tv_payment.setText(viewPaymentOption.getMsg().get(position).getPayment_option_name());
        return convertView;
    }

    static class MyHolder {
        @Bind(R.id.tv_payment)
        TextView tv_payment;

        public MyHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
