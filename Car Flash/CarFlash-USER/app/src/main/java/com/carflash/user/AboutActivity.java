package com.carflash.user;

import android.app.Activity;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.carflash.user.manager.LanguageManager;
import com.carflash.user.models.About;
import com.carflash.user.manager.ApiManager;
import com.carflash.user.samwork.Config;
import com.carflash.user.urls.Apis;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class AboutActivity extends AppCompatActivity implements  ApiManager.APIFETCHER{

    LinearLayout back;
    TextView abottext, version;
    public static Activity aboutactivity;
    LanguageManager languageManager;
    String language_id;

    ApiManager apiManager ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        apiManager = new ApiManager(this , this , this );
        setContentView(R.layout.activity_about);
         aboutactivity = this;

        back = (LinearLayout) findViewById(R.id.bck);
        abottext = (TextView) findViewById(R.id.abouustextv);
        version = (TextView) findViewById(R.id.versionnametextV);

        languageManager = new LanguageManager(this);
        language_id = languageManager.getLanguageDetail().get(LanguageManager.LANGUAGE_ID);

        apiManager.execution_method_get(""+Config.ApiKeys.KeyAboutUs , ""+ Apis.aboutUs+"language_id="+language_id , true,ApiManager.ACTION_SHOW_DIALOG);

        PackageManager manager = aboutactivity.getPackageManager();
        PackageInfo info = null;
        try {
            info = manager.getPackageInfo(AboutActivity.this.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        String version1 = info.versionName;
        version.setText(getString(R.string.version) + " " + version1);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }



    @Override
    public void onFetchComplete(Object script, String APINAME) {
        try{GsonBuilder builder = new GsonBuilder();
            Gson gson = builder.create();

            if (APINAME.equals(""+Config.ApiKeys.KeyAboutUs)) {
                About about;
                about = gson.fromJson(""+script, About.class);
                if (about.getResult()== 1) {
                    String desc = about.getDetails().getDescription();
                    abottext.setText(Html.fromHtml(""+desc));
                }
            }}catch (Exception e){}

    }

    @Override
    public void onFetchResultZero(String s) {

    }

}
