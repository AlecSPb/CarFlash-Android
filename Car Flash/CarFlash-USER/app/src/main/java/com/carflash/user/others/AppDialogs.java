package com.carflash.user.others;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.carflash.user.R;


/**
 * Created by lenovo-pc on 8/31/2017.
 */

public class AppDialogs {

    Context mContext;
    AppDialogListeners mAppDialogListener ;

    public AppDialogs(Context context , AppDialogListeners appDialogListeners){
        mContext = context ;
        this.mAppDialogListener = appDialogListeners;
    }

    public  void  showMessageDialog(boolean setCancelable , String Message , final String dialog_name){
        final Dialog dialog = new Dialog(mContext, android.R.style.Theme_Translucent_NoTitleBar);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        Window window = dialog.getWindow();
        window.setGravity(Gravity.CENTER);
        dialog.setContentView(R.layout.dialog_for_start);
        dialog.setCancelable(setCancelable);

        LinearLayout ll_ok_start = (LinearLayout) dialog.findViewById(R.id.ll_ok_start);
        TextView message = (TextView) dialog.findViewById(R.id.message);
        message.setText(""+Message);
        ll_ok_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAppDialogListener.onDialogDismiss(""+dialog_name);
                dialog.dismiss();
            }
        });
        dialog.show();
    }



    public interface AppDialogListeners {


        void onDialogDismiss( String Dialogname);


    }



}
