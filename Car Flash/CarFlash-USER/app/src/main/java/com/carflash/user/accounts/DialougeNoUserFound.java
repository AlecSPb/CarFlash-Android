package com.carflash.user.accounts;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.carflash.user.R;


/**
 * Created by user on 2/18/2017.
 */

public class DialougeNoUserFound extends Dialog {

    public Activity c;
    public Dialog d;
    EditText CouponCode_edt  ;
    LinearLayout apply ;
    NoUserFoundListener mlistener ;


    public DialougeNoUserFound(Activity a , NoUserFoundListener listener  ) {
        super(a);
        // TODO Auto-generated constructor stub
        this.mlistener = listener ;
        this.c = a;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialouge_no_user_found);

        findViewById(R.id.cancel_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        findViewById(R.id.create_new_account).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mlistener.onCreateNewAccountClick();
                dismiss();
            }
        });

        findViewById(R.id.register_new_phone).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mlistener.onRegisterNewPhoneClick();
                dismiss();
            }
        });

    }





    public interface NoUserFoundListener{
        void onRegisterNewPhoneClick();
        void onCreateNewAccountClick();
    }
}