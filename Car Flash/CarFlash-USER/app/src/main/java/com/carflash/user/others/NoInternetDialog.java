package com.carflash.user.others;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import com.carflash.user.R;

/**
 * Created by lenovo-pc on 9/4/2017.
 */

public class NoInternetDialog {

    Context mContext;
    NoInternetDialogListener mnoInternetDialogListener ;

    public NoInternetDialog(Context context , NoInternetDialogListener noInternetDialogListener){
        mContext = context ;
        this.mnoInternetDialogListener = noInternetDialogListener;
    }

    public  void  showMessageDialog(){
        final Dialog dialog = new Dialog(mContext, android.R.style.Theme_Translucent_NoTitleBar);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        Window window = dialog.getWindow();
        window.setGravity(Gravity.CENTER);
        dialog.setContentView(R.layout.activity_no_internet);

        LinearLayout retry = (LinearLayout) dialog.findViewById(R.id.retry);
        retry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mnoInternetDialogListener.onDialogRetry();
                dialog.dismiss();
            }
        });


        dialog.findViewById(R.id.close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mnoInternetDialogListener.onDialogDismiss();
                dialog.dismiss();
            }
        });
        dialog.show();
    }



    public interface NoInternetDialogListener {


        void onDialogDismiss();
        void onDialogRetry();

    }

}
