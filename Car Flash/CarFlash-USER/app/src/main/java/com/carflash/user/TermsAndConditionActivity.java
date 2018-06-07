package com.carflash.user;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.carflash.user.manager.LanguageManager;
import com.carflash.user.models.TermsAndConditionResponse;
import com.carflash.user.manager.ApiManager;
import com.carflash.user.samwork.Config;
import com.carflash.user.urls.Apis;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class TermsAndConditionActivity extends AppCompatActivity implements  ApiManager.APIFETCHER{

    LinearLayout bck;
    TextView tv_desc;
    public static Activity tca;

    LanguageManager languageManager;
    String language_id;
    ApiManager apiManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tc);
        tca = this;
        apiManager = new ApiManager(this , this , this );

        bck = (LinearLayout) findViewById(R.id.bck);
        tv_desc = (TextView) findViewById(R.id.tc);

        languageManager = new LanguageManager(this);
        language_id = languageManager.getLanguageDetail().get(LanguageManager.LANGUAGE_ID);

        apiManager.execution_method_get(""+Config.ApiKeys.Ket_terms_and_condition, ""+ Apis.tC+"language_id="+language_id, true,ApiManager.ACTION_SHOW_TOP_BAR);

        bck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }




    @Override
    public void onFetchComplete(Object script, String APINAME) {
        try{GsonBuilder builder = new GsonBuilder();
            Gson gson = builder.create();

            if (APINAME.equals(""+ Config.ApiKeys.Ket_terms_and_condition)) {
                TermsAndConditionResponse terms_response;
                terms_response = gson.fromJson(""+script, TermsAndConditionResponse.class);

                if (terms_response.getResult() == 1) {
                    String desc = terms_response.getDetails().getDescription();
                    tv_desc.setText(Html.fromHtml(""+desc));
                }
            }}catch (Exception e){}

    }

    @Override
    public void onFetchResultZero(String s) {

    }
}
