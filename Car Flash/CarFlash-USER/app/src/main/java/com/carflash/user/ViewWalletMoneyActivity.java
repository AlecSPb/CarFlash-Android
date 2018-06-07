package com.carflash.user;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.carflash.user.models.ViewCurrentBalanceResponse;
import com.carflash.user.manager.SessionManager;
import com.carflash.user.manager.ApiManager;
import com.carflash.user.samwork.Config;
import com.carflash.user.urls.Apis;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;


public class ViewWalletMoneyActivity extends AppCompatActivity implements  ApiManager.APIFETCHER {

    SessionManager sessionManager;
    TextView AmountWallet ;
    LinearLayout ll_back_wallet;
    LinearLayout AddMoney ;
    ApiManager apiManager ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_wallet_money);
        apiManager =new ApiManager(this , this , this );
        AmountWallet = (TextView)findViewById(R.id.amount_txt_wallet);
        AddMoney = (LinearLayout)findViewById(R.id.ll_add_money);

        sessionManager = new SessionManager(this);

        ll_back_wallet = (LinearLayout) findViewById(R.id.ll_back_wallet);

        ll_back_wallet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });



        AddMoney.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ViewWalletMoneyActivity.this , AddMoneyToWalletActivity.class));
            }
        });

    }


    @Override
    protected void onResume() {
        super.onResume();
        apiManager.execution_method_get(""+ Config.ApiKeys.Key_Wallet_blance, ""+ Apis.viewBalnceWallet+"user_id="+sessionManager.getUserDetails().get(SessionManager.USER_ID), true,ApiManager.ACTION_SHOW_DIALOG);
    }



    @Override
    public void onFetchComplete(Object script, String APINAME) {
        try{GsonBuilder builder = new GsonBuilder();
            Gson gson = builder.create();
            if (APINAME.equals(""+ Config.ApiKeys.Key_Wallet_blance)){

                ViewCurrentBalanceResponse balanceResponse = new ViewCurrentBalanceResponse();
                balanceResponse = gson.fromJson(""+script , ViewCurrentBalanceResponse.class);

                if (balanceResponse.getResult()==1){

                    AmountWallet.setText(sessionManager.getCurrencyCode()+ balanceResponse.getMsg().getWallet_money());

                }

            }}catch (Exception e){}

    }
    @Override
    public void onFetchResultZero(String s) {

    }

}
