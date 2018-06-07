package com.carflash.user;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.carflash.user.adapter.CardsAdapter;
import com.carflash.user.manager.LanguageManager;
import com.carflash.user.models.SaveCardResponse;
import com.carflash.user.models.ViewCard;
import com.carflash.user.manager.ApiManager;
import com.carflash.user.urls.Apis;
import com.carflash.user.manager.SessionManager;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.stripe.android.Stripe;
import com.stripe.android.TokenCallback;
import com.stripe.android.model.Card;
import com.stripe.android.model.Token;

public class AddCardActivity extends AppCompatActivity implements ApiManager.APIFETCHER {

    EditText edt_card_number, edt_expiry, edt_cvv;
    LinearLayout ll_submit_add_card, ll_back_card;
    public static TextView tv_cards;

    ViewCard viewCard;
    SessionManager sessionManager;

    LanguageManager languageManager;

    public int pos = 0;

    String a;
    int keyDel;

    public static ListView lv_cards;
    String user_id;

    ApiManager apiManager ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_card);
        apiManager = new ApiManager(this , this , this );
        sessionManager = new SessionManager(this);
        user_id = sessionManager.getUserDetails().get(SessionManager.USER_ID);
        final String user_email = sessionManager.getUserDetails().get(SessionManager.USER_EMAIL);
        languageManager = new LanguageManager(this);

        edt_card_number = (EditText) findViewById(R.id.edt_card_number);
        edt_expiry = (EditText) findViewById(R.id.edt_expiry);
        edt_cvv = (EditText) findViewById(R.id.edt_cvv);

        lv_cards = (ListView) findViewById(R.id.lv_cards);

        ll_submit_add_card = (LinearLayout) findViewById(R.id.ll_submit_add_card);
        ll_back_card = (LinearLayout) findViewById(R.id.ll_back_card);
        tv_cards = (TextView) findViewById(R.id.tv_card);

        apiManager.execution_method_get("view_card" , ""+ Apis.viewCard+"user_id="+user_id+"&language_id=1", true,ApiManager.ACTION_SHOW_TOP_BAR);

        String from = super.getIntent().getExtras().getString("From");


        ll_back_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        edt_expiry.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                pos = edt_expiry.getText().length();
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (edt_expiry.getText().length() == 2 && pos != 3) {
                    edt_expiry.setText(edt_expiry.getText().toString() + "/");
                    edt_expiry.setSelection(3);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        edt_card_number.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                boolean flag = true;
                String eachBlock[] = edt_card_number.getText().toString().split("-");
                for (int i = 0; i < eachBlock.length; i++) {
                    if (eachBlock[i].length() > 4) {
                        flag = false;
                    }
                }
                if (flag) {

                    edt_card_number.setOnKeyListener(new View.OnKeyListener() {

                        @Override
                        public boolean onKey(View v, int keyCode, KeyEvent event) {

                            if (keyCode == KeyEvent.KEYCODE_DEL)
                                keyDel = 1;
                            return false;
                        }
                    });

                    if (keyDel == 0) {

                        if (((edt_card_number.getText().length() + 1) % 5) == 0) {

                            if (edt_card_number.getText().toString().split("-").length <= 3) {
                                edt_card_number.setText(edt_card_number.getText() + "-");
                                edt_card_number.setSelection(edt_card_number.getText().length());
                            }
                        }
                        a = edt_card_number.getText().toString();
                    } else {
                        a = edt_card_number.getText().toString();
                        keyDel = 0;
                    }

                } else {
                    edt_card_number.setText(a);
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        ll_submit_add_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String card_number = edt_card_number.getText().toString();
                String expiry = edt_expiry.getText().toString();
                String cvv = edt_cvv.getText().toString();

                String[] parts = expiry.split("/");
                String month = parts[0];
                String year = parts[1];

                if(card_number.equals("") || expiry.equals("") || cvv.equals("")){
                    Toast.makeText(AddCardActivity.this, "Please Enter Details first", Toast.LENGTH_SHORT).show();
                }else {
                    Card card = new Card(card_number, Integer.parseInt(month), Integer.parseInt(year), cvv);

                    try {
                        Stripe stripe = new Stripe(AddCardActivity.this.getString(R.string.stripe_key));
                        stripe.createToken(card,
                                new TokenCallback() {
                                    public void onSuccess(Token token) {

                                        String stripe_token = token.getId() + "";

                                        Log.e("Id  ", token.getId() + "");
                                        apiManager.execution_method_get("save_card" , ""+Apis.saveCard+"user_id="+user_id+"&user_email="+user_email+"&stripe_token="+stripe_token+"&language_id=1", true,ApiManager.ACTION_SHOW_TOP_BAR);
                                    }

                                    public void onError(Exception error) {

                                        showDialouge();
                                        Toast.makeText(AddCardActivity.this, getString(R.string.incorrect_card_number), Toast.LENGTH_SHORT).show();
                                        Log.e("err", error.toString());
                                    }
                                });
                    } catch (Exception e) {
                        Toast.makeText(AddCardActivity.this, "" + e.toString(), Toast.LENGTH_SHORT).show();
                        Log.e("exception ", e.toString());
                    }
                }
            }
        });

        if (from.equals("Navigation")) {
//            Ignore This Part
        } else if (from.equals("PaymentFailedActivity")) {
            lv_cards.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    String card_id = viewCard.getDetails().get(position).getCard_id();
                    Intent intent = new Intent();
                    intent.putExtra("card_id", card_id);
                    setResult(Activity.RESULT_OK, intent);
                    finish();
                }
            });
        }


    }






    public void showDialouge(){
        final Dialog dialog = new Dialog(AddCardActivity.this, android.R.style.Theme_Translucent_NoTitleBar);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        Window window = dialog.getWindow();
        dialog.setCancelable(true);
        window.setGravity(Gravity.CENTER);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(R.layout.dialog_for_demo_card);
        dialog.show();


        dialog.findViewById(R.id.ok).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
    }


    @Override
    public void onFetchComplete(Object script, String APINAME) {
        try{GsonBuilder builder = new GsonBuilder();
            Gson gson = builder.create();

            if (APINAME.equals("save_card")) {
                SaveCardResponse saveCardResponse;
                saveCardResponse = gson.fromJson(""+script, SaveCardResponse.class);
                if (saveCardResponse.getResult() == 1) {
                    Toast.makeText(this, "" + saveCardResponse.getMsg(), Toast.LENGTH_LONG).show();
                    apiManager.execution_method_get("view_card" , ""+ Apis.viewCard+"user_id="+user_id+"&language_id=1", true,ApiManager.ACTION_SHOW_TOP_BAR);
                    edt_expiry.setText("");
                    edt_card_number.setText("");
                    edt_cvv.setText("");
                } else {
                    Toast.makeText(this, "" + saveCardResponse.getMsg(), Toast.LENGTH_LONG).show();
                }
            }

            if (APINAME.equals("view_card")) {

                viewCard = gson.fromJson(""+script, ViewCard.class);
                if (viewCard.getResult() == 1) {

                    lv_cards.setAdapter(new CardsAdapter(this, viewCard , "0" , this));
                    lv_cards.setVisibility(View.VISIBLE);
                    tv_cards.setVisibility(View.GONE);
                } else {
                    lv_cards.setVisibility(View.GONE);
                    tv_cards.setVisibility(View.VISIBLE);
                }
            }}catch (Exception e){}

    }


    @Override
    public void onFetchResultZero(String s) {

    }

}
